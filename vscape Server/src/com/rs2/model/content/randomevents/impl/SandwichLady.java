package com.rs2.model.content.randomevents.impl;

import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class SandwichLady implements RandomEvent {
	
	private Player player;
	public SandwichLady(final Player player)
	{
		this.player = player;
	}

	private enum Reward {
		REDBERRYPIE("Redberry pie", 2325, 63013),
		KEBAB("Kebab", 1971, 63014),
		CHOCOLATE("Chocolate Bar", 1973, 63015),
		BAGUETTE("Baguette", 6961, 63009),
		TRIANGLE("Triangle sandwich", 6962, 63010),
		SQUARE("Square sandwich", 6965, 63011),
		ROLL("Bread Roll", 2309, 63012);
		
		private String name;
		private int itemId;
		private int btnId;
		
		private Reward(String name, int itemId, int btnId){
			this.name = name;
			this.itemId = itemId;
			this.btnId = btnId;
		}
		
		public static Reward forButtonId(int button) {
			for (Reward reward : Reward.values())
					if (button == reward.btnId)
						return reward;
			return null;
		}
		
		public String getName(){
			return name;
		}
		public int getItemId(){
			return itemId;
		}
		public int getBtnId(){
			return btnId;
		}
	}
	private final static int sandwichLadyID = 3117;
	private Reward reward = Reward.REDBERRYPIE;
	private boolean complete = false;

	@Override
	public void spawnEvent() {
		complete = false;
		player.getRandomHandler().spawnEventNpc(sandwichLadyID);
		reward = Reward.values()[Misc.random(Reward.values().length-1)];
		player.getRandomEventNpc().getUpdateFlags().sendForceMessage(cycleMessages()[0].replaceAll("%", player.getUsername()));
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int cycles = 0;
			@Override
			public void execute(CycleEventContainer container) {
				if (complete || player.getRandomEventNpc() == null || !player.getRandomEventNpc().isVisible() || player.getRandomEventNpc().getNpcId() != sandwichLadyID) {
					container.stop();
					return;
				}
				if (cycles == cycleMessages().length - 1) {
					sayGoodBye(true);
					container.stop();
					return;
				}
				cycles++;
				player.getRandomEventNpc().getUpdateFlags().sendForceMessage(cycleMessages()[cycles].replaceAll("%", player.getUsername()));
			}

			@Override
			public void stop() {
			}
		}, 20);
	}
	
	@Override
	public void destroyEvent(boolean logout) {
		player.getRandomHandler().setCurrentEvent(null);
		player.getRandomHandler().destroyEventNpc();
		player.setStatedInterface("");
	}
	
	@Override
	public void showInterface() {
		player.setStatedInterface("SandwichLady");
		player.getActionSender().sendInterface(16135);
	}

	@Override
	public boolean handleButtons(int buttonID) {
		if(player.getStatedInterface().equals("SandwichLady"))
		{
			Reward rew = Reward.forButtonId(buttonID);
			if(rew != null && !complete)
			{
				if(rew.getBtnId() != reward.getBtnId())
				{
					player.hit(1, HitType.NORMAL);
					player.getRandomEventNpc().getUpdateFlags().sendForceMessage("That wasn't the treat I offered you!");
				}else{
					sayGoodBye(false);
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean sendDialogue(int id, int chatId, int optionId, int npcChatId) {
		switch(id)
		{
			case sandwichLadyID :
				final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
				if(npc != null && !player.getRandomEventNpc().equals(npc) || complete)
				{
					return false;
				}
			    switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("You look hungry to me. I tell you what - ", "have a " + reward.getName() + " on me.", Dialogues.HAPPY);
				    return true;
					case 2:
						showInterface();
						player.getDialogue().dontCloseInterface();
				    break;
			    }
			break;
		}
		return false;
	}
	
	public void sayGoodBye(boolean failed) {
		final int randNum = Misc.random(EventsConstants.remoteLocations.length - 1);
		player.getActionSender().removeInterfaces();
		if (!failed) {
			player.getRandomEventNpc().getUpdateFlags().sendForceMessage("Hope that fills you up!!");
			player.getRandomEventNpc().getUpdateFlags().sendAnimation(EventsConstants.GOOD_BYE_EMOTE);
			player.getInventory().addItem(new Item(reward.getItemId(), 1));
			complete = true;
		} else {
		    if(player.getRandomEventNpc() != null && Misc.goodDistance(player.getPosition(), player.getRandomEventNpc().getPosition(), 7)) {
		    	player.getRandomEventNpc().sendPlayerAway(player, 402, 2304, EventsConstants.remoteLocations[randNum].getX(), EventsConstants.remoteLocations[randNum].getY(), EventsConstants.remoteLocations[randNum].getZ(), "Let's see how you like this!", false);
		    }
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				destroyEvent(false);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 5);
	}
	
	public String[] cycleMessages() {
		return new String[]{"Hello %, I would like to offer you a treat!", "You'd better start showing some manners, %!", "Well, I've never been so insulted!!", "Fine, ignore me, you will regret it!"};
	}

	@Override
	public boolean doObjectClicking(int object, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

}
