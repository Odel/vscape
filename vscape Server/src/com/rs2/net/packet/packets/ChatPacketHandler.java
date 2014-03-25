package com.rs2.net.packet.packets;

import com.rs2.model.players.Player;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class ChatPacketHandler implements PacketHandler {

	public static final int CHAT = 4;

	@Override
	public void handlePacket(Player player, Packet packet) {
		int effects = packet.getIn().readByte(false, StreamBuffer.ValueType.S);
		int color = packet.getIn().readByte(false, StreamBuffer.ValueType.S);
		int chatLength = packet.getPacketLength() - 2;
		byte[] text = packet.getIn().readBytesReverse(chatLength, StreamBuffer.ValueType.A);
		if (player.isMuted()) {
			player.getActionSender().sendMessage("You are muted and cannot talk.");
			return;
		}
		if (player.getNewComersSide().isInTutorialIslandStage())
			return;
		player.setChatEffects(effects);
		player.setChatColor(color);
		player.setChatText(text);
		player.setChatUpdateRequired(true);
	}

}
