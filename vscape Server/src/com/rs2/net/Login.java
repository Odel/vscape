package com.rs2.net;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.security.SecureRandom;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.util.NameUtil;

public class Login {

	private static final BigInteger RSA_MODULUS = new BigInteger(
			"107712039694095576939431091074499502318821368185543817710214551837743576731966702937430677570365351116665020131991421260010744323075482541553857524846962418401811066451978330520647191868135865171986073611431747107680458258502268270300389397844051013900644581276875470121130541138768121181537294462354865108961");

	private static final BigInteger RSA_EXPONENT = new BigInteger(
			"101295697735114403978027014300236932212580944891887363122108938056603901074527424235815874249573945792047472203412229221939396140563517028298349036863036657924753548163504517363028507005271453357332449456957079951695899967128573105137579923326259482708790678142592368823162375493502538616166798856849572725189");

	public void handleLogin(Player player, ByteBuffer inData) throws Exception {
		switch (player.getLoginStage()) {
		case CONNECTED:
			if (inData.remaining() < 2) {
				inData.compact();
				return;
			}

			// Validate the request.
			int request = inData.get() & 0xff;
			inData.get(); // Name hash.
			
			//checks if player is banned
			if (player.isBanned()){
				System.err.println("You have been banned, you will be unbanned in" + getBanExpire()*6000 + "minutes.");
				player.disconnect();
				return
			}
			
			
			if (request != 14) {
				System.err.println("Invalid login request: " + request);
				player.disconnect();
				return;
			}

			// Write the response.
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(17);
			out.writeLong(0); // First 8 bytes are ignored by the client.
			out.writeByte(0); // The response opcode, 0 for logging in.
			out.writeLong(new SecureRandom().nextLong()); // SSK.
			player.send(out.getBuffer());

			player.setLoginStage(LoginStages.LOGGING_IN);
			break;
		case LOGGING_IN:
			if (inData.remaining() < 2) {
				inData.compact();
				return;
			}

			// Validate the login type.
			int loginType = inData.get();
			if (loginType != 16 && loginType != 18) {
				System.err.println("Invalid login type: " + loginType);
				player.disconnect();
				return;
			}

			// Ensure that we can read all of the login block.
			int blockLength = inData.get() & 0xff;
			int loginEncryptSize = blockLength - (36 + 1 + 1 + 2);
			if (inData.remaining() < blockLength) {
				inData.compact();
				return;
			}

			// Read the login block.
			StreamBuffer.InBuffer in = StreamBuffer.newInBuffer(inData);
			
			// Set the magic id
			player.setMagicId(in.readByte());

			// Set the client version.
			player.setClientVersion(in.readShort());

			in.readByte(); // Skip the high/low memory version.

			// Skip the CRC keys.
			for (int i = 0; i < 9; i++) {
				in.readInt();
			}
			if (Constants.RSA_CHECK) {
				loginEncryptSize--;
				int reportedSize = inData.get() & 0xFF;
				if (reportedSize != loginEncryptSize) {
					System.err.println("Encrypted packet size zero or negative : " + loginEncryptSize);
					player.disconnect();
					return;
				}
				byte[] encryptionBytes = new byte[loginEncryptSize];
				inData.get(encryptionBytes);
				ByteBuffer rsaBuffer = ByteBuffer.wrap(new BigInteger(encryptionBytes).modPow(RSA_EXPONENT, RSA_MODULUS).toByteArray());
				int rsaOpcode = rsaBuffer.get() & 0xFF;
				// Validate that the RSA block was decoded properly.
				if (rsaOpcode != 10) {
					System.err.println("Unable to decode RSA block properly!");
					player.disconnect();
					return;
				}
				long clientHalf = rsaBuffer.getLong();
				long serverHalf = rsaBuffer.getLong();
				int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf,
						(int) (serverHalf >> 32), (int) serverHalf };
				player.setDecryptor(new ISAACCipher(isaacSeed));
				for (int i = 0; i < isaacSeed.length; i++) {
					isaacSeed[i] += 50;
				}
				player.setEncryptor(new ISAACCipher(isaacSeed));
				rsaBuffer.getInt();
				String username = NameUtil.getRS2String(rsaBuffer).trim();
				String password = NameUtil.getRS2String(rsaBuffer).trim();
				player.setPassword(password);
				player.setUsername(NameUtil.uppercaseFirstLetter(username));
			} else {
				in.readByte(); // Skip RSA block length.
				// Validate that the RSA block was decoded properly.
				int rsaOpcode = in.readByte();
				if (rsaOpcode != 10) {
					System.err.println("Unable to decode RSA block properly!");
					player.disconnect();
					return;
				}
				// Set up the ISAAC ciphers.
				long clientHalf = in.readLong();
				long serverHalf = in.readLong();
				int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
				player.setDecryptor(new ISAACCipher(isaacSeed));
				for (int i = 0; i < isaacSeed.length; i++) {
					isaacSeed[i] += 50;
				}
				player.setEncryptor(new ISAACCipher(isaacSeed));

				// Read the user authentication.
				in.readInt(); // Skip the user ID.
				String username = in.readString().trim();
				String password = in.readString().toLowerCase().trim();
				player.setPassword(password);
				player.setUsername(NameUtil.uppercaseFirstLetter(username));
			}
			
            player.setUsernameAsLong(NameUtil.nameToLong(player.getUsername().toLowerCase()));
            player.setLoginStage(LoginStages.AWAITING_LOGIN_COMPLETE);

            if (player.beginLogin()) {
                // Switch the player to the cycled reactor.
                synchronized (DedicatedReactor.getInstance()) {
                    DedicatedReactor.getInstance().getSelector().wakeup();
                    player.getKey().interestOps(player.getKey().interestOps() & ~SelectionKey.OP_READ);
                    player.getSocketChannel().register(Server.getSingleton().getSelector(), SelectionKey.OP_READ, player);
                }
            }
			break;
		}
	}

	public static boolean checkName(String username) {
		/*
		 * String[] names = {"Mod Caleb", "Mod Vault", "Mod Russian",
		 * "Mod Vayken", "Mod James", "Mod Blake", "Mod Josh", "Mod Nick",
		 * "Mod Calvin", "Mod Ian", "Mod Patrick", "Darrel", "Divine", "Ftw",
		 * "Liberty", "Melee", "Ness", "Rex", "Jwc"}; for (String name : names)
		 * { if (username.equalsIgnoreCase(name)) { return true; } } return
		 * false;
		 */
		return true;
	}

}
