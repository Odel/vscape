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

	private static final BigInteger RSA_MODULUS = new BigInteger("169885750724363784459917080609337482536839129601875456173526407359290544325272667000733686649412173234652149153464632549656395615602414312478152555713810011543630242588422020353041975816059911130162084434701288693650940900379658199377383979989747865423058390069686994226663888386373467733265328219494354731803");
	private static final BigInteger RSA_EXPONENT = new BigInteger("103211484365187122420252048179522730742737488506160995514062704051413893111601942556131841061278286914428642914603167822712651598804121767331890720635687602141561662259957147474314969912125980563698621320984697859752241033501411355069632374214421433101509504778843230346187810212556513983963266846505782176257");
	
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
			/*
			//checks if player is banned
			if (player.isBanned()){
				System.err.println("You have been banned, you will be unbanned in" + player.getBanExpire()*6000 + "minutes.");
				player.disconnect();
				return;
			}
			
			//checks if player is banned
			if (player.isIpBanned()){
				System.err.println("You have been ip banned banned");
				player.disconnect();
				return;
			}
			*/
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
			if(Constants.MAC_CHECK)
			{
				player.setMacAddress(in.readString().trim());
			}
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
	    
	    /*
            if(!player.validName())
            {
				player.disconnect();
	            return;
            }
            */
            if (player.beginLogin()) {
                // Switch the player to the cycled reactor.
                synchronized (DedicatedReactor.getInstance()) {
                    DedicatedReactor.getInstance().getSelector().wakeup();
                    player.getKey().interestOps(player.getKey().interestOps() & ~SelectionKey.OP_READ);
                    player.getSocketChannel().register(Server.getSingleton().getSelector(), SelectionKey.OP_READ, player);
                }
            }
			break;
		case AWAITING_LOGIN_COMPLETE:
			break;
		case LOGGED_IN:
			break;
		case LOGGED_OUT:
			break;
		case LOGGING_OUT:
			break;
		default:
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
