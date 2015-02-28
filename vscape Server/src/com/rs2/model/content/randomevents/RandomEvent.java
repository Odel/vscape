package com.rs2.model.content.randomevents;

import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;

public class RandomEvent {

	public RandomEvent(Player player, int npcId) {
		final Npc npc = new Npc(npcId);
		NpcLoader.spawnNpc(player, npc, false, false);
		npc.getUpdateFlags().sendForceMessage(player.getRandomInterfaceClick().getEvents(npcId).cycleMessages()[0].replaceAll("%", Misc.formatPlayerName(player.getUsername())));
		if(npcId == 3117) {
		    player.getRandomInterfaceClick().getEvents(npcId).setRandomNumber();
		    player.getRandomInterfaceClick().randomNumber = player.getRandomInterfaceClick().getEvents(3117).getRandomNumber();
		}
		player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
		player.getRandomInterfaceClick().sendCycleAdvertisement(npc);
		player.setSpawnedNpc(npc);
	}

	public static void resetEvents(Player player) {
		player.getRandomInterfaceClick().completed = false;
		player.getRandomInterfaceClick().stage = 0;
	}

}
