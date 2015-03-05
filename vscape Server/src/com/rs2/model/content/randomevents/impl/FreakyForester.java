package com.rs2.model.content.randomevents.impl;

import java.util.ArrayList;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;


public class FreakyForester implements RandomEvent {

	private final static int foresterId = 2458;
	public static final int[] CLOTHES = {6182, 6180, 6181};
	
	public boolean complete = false;
	public int tails = 1;
			
	private Player player;
	public FreakyForester(final Player player)
	{
		this.player = player;
	}
	
	@Override
	public void spawnEvent() {
		complete = false;
		tails = Misc.random(1, 4);
		player.setStopPacket(true);
		player.getMovementHandler().reset();
		player.getMovementHandler().resetOnWalkPacket();
		player.getRandomHandler().spawnEventNpc(foresterId);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    int cycle = 0;
		    final String name = Misc.formatPlayerName(player.getUsername());
		    @Override
		    public void execute(CycleEventContainer container) {
		    	switch (cycle) {
		    		case 0:
		    			player.getRandomEventNpc().getUpdateFlags().sendForceMessage("Hello "+name+"! I need your help!");
		    		break;
		    		case 2:
		    			player.getRandomEventNpc().getUpdateFlags().sendForceMessage("Quickly, come with me "+name+"!");
		    		break;
		    		case 4:
		    			player.getRandomHandler().destroyEventNpc();
		    			player.teleport(new Position(2601, 4776, 0));
		    			player.setStopPacket(false);
		    			Dialogues.startDialogue(player, foresterId);
					    container.stop();
				    return;
		    	}
		    	cycle++;
		    }
	
		    @Override
		    public void stop() {
		    }
		}, 1);
	}

	@Override
	public void destroyEvent(boolean logout) {
		player.getRandomHandler().setCurrentEvent(null);
		complete = false;
		if(!logout)
		{
			for(Item item : player.getInventory().getItemContainer().getItems()) {
				if(item == null) continue;
				if(item.getId() == 6179) player.getInventory().removeItem(new Item(6179));
				if(item.getId() == 6178) player.getInventory().removeItem(new Item(6178));
		    }
			player.teleport(player.getLastPosition());
		}
	}

	@Override
	public void showInterface() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleButtons(int buttonID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendDialogue(int id, int chatId, int optionId, int npcChatId) {
		switch(id) {
			case foresterId :
			    switch (player.getDialogue().getChatId()) {
			    	case 1 :
			    		if(complete) {
							player.getDialogue().sendNpcChat("You can leave using that portal over there.", Dialogues.CONTENT);
							player.getDialogue().endDialogue();
							return true;
			    		} else {
			    			if (player.getInventory().playerHasItem(6178)) {
								player.getDialogue().sendNpcChat("That's it! That's the right pheasant! Thank", "you so much " + player.getUsername() + "! Here's a little", "something in exchange for helping me.", Dialogues.CONTENT);
			    				return true;
			    			} else {
							    player.getDialogue().sendNpcChat("Shhhh! Quiet! I need you to help me kill", "the pheasant with " + tails + " "+(tails == 1 ? "tail" : "tails")+"! Bring me the", "raw pheasant it drops and I'll reward you.", Dialogues.CONTENT);
							    player.getDialogue().endDialogue();
							    return true;
			    			}
			    		}
			    	case 2 :
			    		Item reward = getReward();
					    player.getDialogue().sendGiveItemNpc("The Forester hands you your reward.", reward);
					    player.getInventory().replaceItemWithItem(new Item(6178), reward);
					    complete = true;
		    		return true;
			    	case 3:
					    player.getDialogue().sendNpcChat("You can leave using that portal over there.", "Thank you again.", Dialogues.CONTENT);
					    player.getDialogue().endDialogue();
				    return true;
			    }
			break;
		}
		return false;
	}

	@Override
	public boolean doObjectClicking(int object, int x, int y, int z) {
		switch(object)
		{
			case 8972: //freaky forester portal
				if(x == 2611 && y == 4776) {
					if(!complete)
					{
					    player.getDialogue().sendNpcChat("Hey! D-don't leave yet! I still need", "your help with this pheasant...", Dialogues.SAD);
					    return true;
					} else {
						destroyEvent(false);
					    return true;
					}
				}
			return false;
		}
		return false;
	}
	
	public void handleDrops(Npc npc) {
		if(complete || player.getRandomHandler().getCurrentEvent() != player.getRandomHandler().getFreakyForester()) {
		    return;
		}
		if(npc.getNpcId() == getNpcIdForTails()) {
		    GroundItem drop = new GroundItem(new Item(6178), player, npc.getPosition().clone());
		    GroundItemManager.getManager().dropItem(drop);
		} else {
		    switch(npc.getNpcId()) {
				case 2459:
				case 2460:
				case 2461:
				case 2462:
					GroundItem drop = new GroundItem(new Item(6179), player, npc.getPosition().clone()); //bad pheasant
					GroundItemManager.getManager().dropItem(drop);
			    break;
		    }
		}
	}
	
    private int getNpcIdForTails() {
		switch(tails) {
		    case 1:
			return 2459;
		    case 2:
			return 2460;
		    case 3:
			return 2461;
		    case 4:
			return 2462;
		}
		return 0;
    }
	
	private Item getReward() {
		int reward = 995;
		ArrayList<Integer> possibleRewards = new ArrayList<Integer>();
		for(int rew : CLOTHES)
		{
			if(!player.getInventory().ownsItem(rew))
			{
				possibleRewards.add(rew);
			}
		}
		if(possibleRewards.size() > 0)
		{
			reward = possibleRewards.get(Misc.random(possibleRewards.size()-1));
		}
		return new Item(reward, (reward == 995 ? 1250 : 1));
	}
}
