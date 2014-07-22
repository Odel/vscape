package com.rs2.model.content.randomevents;

import com.rs2.model.content.randomevents.TalkToEvent.TalkToNpc;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class RandomEvent {

	public RandomEvent(Player player, int npcId) {
		final Npc npc = new Npc(npcId);
		NpcLoader.spawnNpc(player, npc, false, false);
		npc.getUpdateFlags().sendForceMessage(player.getRandomInterfaceClick().getEvents(npcId).cycleMessages()[0].replaceAll("%", Misc.formatPlayerName(player.getUsername())));
		player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
		player.getRandomInterfaceClick().sendCycleAdvertisement(npc);

	}

	public static void resetEvents(Player player) {
		player.getRandomInterfaceClick().completed = false;
	}

	public static void destroyEventNpc(Player player) {
		if (player.getRandomEventNpc() != null && !player.getRandomEventNpc().isDead()) {
			player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, player.getRandomEventNpc().getPosition(), 100 << 16);
			NpcLoader.destroyNpc(player.getRandomEventNpc());
			player.setRandomEventNpc(null);
		}
	}

	public static void startRandomEvent(final Player player) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				if (player.getRandomEventNpc() != null || player.cantTeleport()) {
					return;
				}
				int random = Misc.random(100);
				switch(random) {
					case 0 : //Wasp
						player.getPjTimer().setWaitDuration(0);
						player.getPjTimer().reset();
						NpcLoader.spawnNpc(player, new Npc(411), true, false);
						break;
					case 1 :
						TalkToEvent.spawnNpc(player, TalkToNpc.DRUNKEN_DWARF);
						break;
					case 2 :
						TalkToEvent.spawnNpc(player, TalkToNpc.GENIE);
						break;
					case 3 :
						TalkToEvent.spawnNpc(player, TalkToNpc.JEKYLL);
						break;
					//case 4 :
						//TalkToEvent.spawnNpc(player, TalkToNpc.RICK);
						//break;
				}
			}
			@Override
			public void stop() {
			}
		}, 1000);
	}
}
