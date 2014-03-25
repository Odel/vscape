package com.rs2.net.packet.packets;

import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class FlashingSideIcon implements PacketHandler {

	public static final int FLASH_ICON = 152;

	@Override
	public void handlePacket(Player player, Packet packet) {
		player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);

	}

}
