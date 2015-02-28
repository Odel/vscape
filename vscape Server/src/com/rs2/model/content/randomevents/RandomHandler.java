package com.rs2.model.content.randomevents;

import com.rs2.model.content.randomevents.TalkToEvent.TalkToNpc;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class RandomHandler {

	private Player player;
	public RandomHandler(final Player player)
	{
		this.player = player;
	}
	
	private FreakyForester freakyForester = new FreakyForester(player);
	private Pillory pillory = new Pillory(player);
	
	public FreakyForester getFreakyForester() {
		return freakyForester;
	}
	
	public Pillory getPillory() {
		return pillory;
	}
	
	public void process() {
		if(player.getStaffRights() >= 3) {
		    return;
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				if (player.getRandomEventNpc() != null || player.cantTeleport() || player.getInJail() || player.isCrossingObstacle) {
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
					case 4:
						player.getRandomHandler().getFreakyForester().spawnForester();
						break;
					case 5:
						player.getRandomInterfaceClick().sendEventRandomly(); //Sandwich lady
						break;
					//case 5 :
						//TalkToEvent.spawnNpc(player, TalkToNpc.RICK);
						//break;
				}
			}
			@Override
			public void stop() {
			}
		}, 1000);
	}
	
	public void spawnEventNpc(int npcID) {
		final Npc npc = new Npc(npcID);
		NpcLoader.spawnNpc(player, npc, false, false);
		player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
		player.setSpawnedNpc(npc);
		player.setRandomEventNpc(npc);
	}
	
	public void destroyEventNpc() {
		if (player.getRandomEventNpc() != null && !player.getRandomEventNpc().isDead()) {
			player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, player.getRandomEventNpc().getPosition(), 100 << 16);
			NpcLoader.destroyNpc(player.getRandomEventNpc());
			player.setRandomEventNpc(null);
			player.setSpawnedNpc(null);
		}
	}
}
