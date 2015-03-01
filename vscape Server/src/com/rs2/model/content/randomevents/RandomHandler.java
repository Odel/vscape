package com.rs2.model.content.randomevents;

import com.rs2.model.content.randomevents.TalkToEvent.TalkToNpc;
import com.rs2.model.content.randomevents.impl.FreakyForester;
import com.rs2.model.content.randomevents.impl.MimeEvent;
import com.rs2.model.content.randomevents.impl.Pillory;
import com.rs2.model.content.randomevents.impl.RandomEvent;
import com.rs2.model.content.randomevents.impl.SandwichLady;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class RandomHandler {

	private SandwichLady sandwichLady;
	private FreakyForester freakyForester;
	private Pillory pillory;
	private MimeEvent mimeEvent;
	
	private Player player;
	public RandomHandler(final Player player)
	{
		this.player = player;
		
		sandwichLady = new SandwichLady(player);
		freakyForester = new FreakyForester(player);
		pillory = new Pillory(player);
		mimeEvent = new MimeEvent(player);
	}
	
	public SandwichLady getSandwichLady() {
		return sandwichLady;
	}
	
	public FreakyForester getFreakyForester() {
		return freakyForester;
	}
	
	public Pillory getPillory() {
		return pillory;
	}
	
	public MimeEvent getMimeEvent() {
		return mimeEvent;
	}
	
	private RandomEvent currentEvent;
	public void setCurrentEvent(RandomEvent event) {
		currentEvent = event;
	}
	public RandomEvent getCurrentEvent() {
		return currentEvent;
	}
	
	public void process() {
		if(player.getStaffRights() >= 3) {
		    return;
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				spawnEvent();
			}
			@Override
			public void stop() {
			}
		}, 3600);
	}
	
	public void spawnEvent() {
		if (player.getRandomEventNpc() != null || player.cantTeleport() || player.getInJail() || player.isCrossingObstacle || (player.onApeAtoll() && player.getMMVars().isMonkey())) {
			return;
		}
		int random = Misc.random(7);
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
				getFreakyForester().spawnForester();
				break;
			case 5:
				getSandwichLady().spawnEvent();
				setCurrentEvent(sandwichLady);
				break;
			case 6:
				getMimeEvent().spawnEvent();
				setCurrentEvent(mimeEvent);
				break;
			//case 5 :
				//TalkToEvent.spawnNpc(player, TalkToNpc.RICK);
				//break;
		}
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
	
	public void destroyEvent() {
		if(getCurrentEvent() != null)
		{
			getCurrentEvent().destroyEvent();
			setCurrentEvent(null);
		}
	}
}
