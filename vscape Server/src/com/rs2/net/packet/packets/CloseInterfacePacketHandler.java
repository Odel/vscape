package com.rs2.net.packet.packets;

import com.rs2.model.content.skills.cooking.FlourRelated;
import com.rs2.model.players.Player;
import com.rs2.model.players.TradeManager;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class CloseInterfacePacketHandler implements PacketHandler {

	public static final int CLOSE_INTERFACE = 130;

	@Override
	public void handlePacket(Player player, Packet packet) {
		TradeManager.declineTrade(player);
        if(player.getStatedInterface() == "flour")
            FlourRelated.handleButton(player, 53204);
		if (player.getStatedInterface() == "duel" || player.getStatedInterface() == "duel2") {
			if (player.getDuelMainData().getOpponent() != null && player.getDuelMainData().getOpponent().isLoggedIn()) {
				player.getDuelMainData().getOpponent().getDuelInteraction().endDuelInteraction(true);
				player.getDuelMainData().getOpponent().getActionSender().sendMessage("Other played declined the duel.");
			}
			player.getDuelInteraction().endDuelInteraction(true);
		}
		player.getAttributes().put("isBanking", Boolean.FALSE);
		player.getAttributes().put("isShopping", Boolean.FALSE);
		player.setStatedInterface("");
		if (player.getNewComersSide().isInTutorialIslandStage())
			player.getActionSender().removeInterfaces();
		if (!player.getNewComersSide().isInTutorialIslandStage())
			player.getActionSender().sendSidebarInterface(3, 3213);
        player.setInterface(0);
	}

}
