package com.rs2.net.packet.packets;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.TradeManager;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class CloseInterfacePacketHandler implements PacketHandler {

	public static final int CLOSE_INTERFACE = 130;

	@Override
	public void handlePacket(Player player, Packet packet) {
		TradeManager.declineTrade(player);
		if (player.getStatedInterface() == "duel" || player.getStatedInterface() == "duel2") {
			if (player.getDuelMainData().getOpponent() != null && player.getDuelMainData().getOpponent().isLoggedIn()) {
				player.getDuelMainData().getOpponent().getDuelInteraction().endDuelInteraction(true);
				player.getDuelMainData().getOpponent().getActionSender().sendMessage("Other played declined the duel.");
			}
			player.getDuelInteraction().endDuelInteraction(true);
		}
		player.getAttributes().put("isBanking", Boolean.FALSE);
		player.getAttributes().put("isShopping", Boolean.FALSE);
		if(player.getStatedInterface().equals("PcExpInterface")) {
		    player.setSkillAnswer(0);
		    player.setPcSkillPoints(0);
		}
		if(player.getStatedInterface().equals("hazelmere")) {
		    for(int i = 6968; i <= 6975; i++) {
			player.getActionSender().sendString("", i);
		    }
		}
		if(player.getStatedInterface().equals("hangoverCure") || player.getStatedInterface().equals("combinationBalls")) {
			for(int i = 1142; i < 1146; i++) {
				player.getActionSender().sendString("", i);
			}
		}
		if(player.getStatedInterface().equals("gamblingDice")) {
			player.getDice().quitGameInterface();
			Dialogues.sendDialogue(player, 1078, 20, 0);
		}
		if(player.getStatedInterface().equals("gamblingDice2")) {
			player.getDice().quitGameInterface();
			Dialogues.sendDialogue(player, 1078, 43, 0);
		}
		if(player.getStatedInterface().equals("gamblingDiceTie")) {
			player.getDice().quitGameInterface();
		}
		player.setStatedInterface("");
		if (player.getNewComersSide().isInTutorialIslandStage())
			player.getActionSender().removeInterfaces();
		if (!player.getNewComersSide().isInTutorialIslandStage())
			player.getActionSender().sendSidebarInterface(3, 3213);
		player.setInterface(0);
	}

}
