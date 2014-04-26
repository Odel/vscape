package com.rs2.net.packet.packets;

import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class PrivateMessagingPacketHandler implements PacketHandler {

    public static final int ADD_FRIEND = 188; // CONVERTED
    public static final int REMOVE_FRIEND = 215; // CONVERTED
    public static final int ADD_IGNORE = 133;      // CONVERTED
    public static final int REMOVE_IGNORE = 74;  // CONVERTED
    public static final int SEND_PM = 126;     // CONVERTED

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case ADD_FRIEND:
			long name = packet.getIn().readLong();
			player.getPrivateMessaging().addToFriendsList(name);
			break;
		case REMOVE_FRIEND:
			name = packet.getIn().readLong();
			player.getPrivateMessaging().removeFromList(player.getFriends(), name);
			break;
		case ADD_IGNORE:
			name = packet.getIn().readLong();
			player.getPrivateMessaging().addToIgnoresList(name);
			break;
		case REMOVE_IGNORE:
			name = packet.getIn().readLong();
			player.getPrivateMessaging().removeFromList(player.getIgnores(), name);
			break;
		case SEND_PM:
			long to = packet.getIn().readLong();
			int size = packet.getPacketLength() - 8;
			if (size < 0) {
				return;
			}
			byte[] message = packet.getIn().readBytes(size);
			if (player.isMuted()) {
				player.getActionSender().sendMessage("You are muted and cannot talk.");
				return;
			}
			player.getPrivateMessaging().sendPrivateMessage(player, to, message, size);
			break;
		}

	}

}