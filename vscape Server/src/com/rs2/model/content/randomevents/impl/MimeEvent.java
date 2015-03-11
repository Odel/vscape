package com.rs2.model.content.randomevents.impl;

import java.util.ArrayList;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class MimeEvent implements RandomEvent {
	
	private final static int mysteriousOldManId = 410;
	private final static int mimeId = 1056;
	private Npc mime;
	
	private Player player;
	public MimeEvent(final Player player)
	{
		this.player = player;
	}
	
	private enum MimeEmote {
		THINK(857, 5),
		LAUGH(861, 5),
		CLIMBROPE(1130, 5),
		GLASSBOX(1131, 5),
		CRY(860, 5),
		DANCE(866, 8),
		LEAN(1129, 5),
		GLASSWALL(1128, 5);
		
		private int anim;
		private int duration;
		
		private MimeEmote(int anim, int duration)
		{
			this.anim = anim;
			this.duration = duration;
		}
	}
	private MimeEmote emoteToDo = MimeEmote.THINK;
	
	private final static int stages = 8;
	private int currentStage = 0;
	private int correct = 0;
	
	private boolean myTurn = false;
	
	@Override
	public void spawnEvent() {
		myTurn = false;
		currentStage = 0;
		correct = 0;
		player.setStopPacket(true);
		player.getMovementHandler().reset();
		player.getMovementHandler().resetOnWalkPacket();
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		player.getRandomHandler().spawnEventNpc(mysteriousOldManId);
		player.setMovementDisabled(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    int cycle = 0;
		    final String name = Misc.formatPlayerName(player.getUsername());
		    @Override
		    public void execute(CycleEventContainer container) {
		    	switch (cycle) {
		    		case 0:
		    			player.getRandomEventNpc().getUpdateFlags().sendForceMessage("Come with me "+name+"!");
		    		break;
		    		case 2:
		    			player.teleport(new Position(2008,4762,player.getIndex()*4));
		    			player.getUpdateFlags().sendFaceToDirection(new Position(2008,4761,player.getIndex()*4));
		    			player.getActionSender().hideAllSideBars();
		    			player.getActionSender().enableSideBarInterfaces(new int[]{7, 8, 9, 10});
		    			player.getRandomHandler().destroyEventNpc();
		    			player.getRandomHandler().spawnEventNpc(mimeId);
		    			mime = player.getRandomEventNpc();
		    			mime.teleport(new Position(2011,4762,player.getIndex()*4));
		    			mime.setFollowingEntity(null);
		    			mime.setFace(3);
				    break;
		    		case 3:
		    			player.setStopPacket(false);
		    			player.getActionSender().animateObject(2007, 4761, 1, 1135);
		    			Dialogues.startDialogue(player, mysteriousOldManId);
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
		player.getRandomHandler().destroyEventNpc();
		mime = null;
		player.setMovementDisabled(false);
		if(!logout)
		{
			player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			player.getActionSender().sendSideBarInterfaces();
			player.getEquipment().sendWeaponInterface();
			player.teleport(player.getLastPosition());
		}
	}

	@Override
	public void showInterface() {
	}

	@Override
	public boolean handleButtons(int buttonID) {
		if(myTurn)
		{
			switch(buttonID)
			{
				case 25147 :
					performEmotePlayer(MimeEmote.THINK);
				return true;
				case 25148 :
					performEmotePlayer(MimeEmote.LAUGH);
				return true;
				case 25150 :
					performEmotePlayer(MimeEmote.CLIMBROPE);
				return true;
				case 25153 :
					performEmotePlayer(MimeEmote.GLASSBOX);
				return true;
				case 25146 :
					performEmotePlayer(MimeEmote.CRY);
				return true;
				case 25149 :
					performEmotePlayer(MimeEmote.DANCE);
				return true;
				case 25151 :
					performEmotePlayer(MimeEmote.LEAN);
				return true;
				case 25152 :
					performEmotePlayer(MimeEmote.GLASSWALL);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean sendDialogue(int id, int chatId, int optionId, int npcChatId) {
		switch(id)
		{
			case mysteriousOldManId :
			    switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("You need to copy the mime's performance,", "then you'll return to where you were.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(2);
					return true;
					case 2:
						player.getDialogue().endDialogue();
						performEmoteMime(true);
					return false;
			    }
			break;
		}
		return false;
	}
	
	private final static int[] itemRewards = {
		3057,
		3058,
		3059,
		3060,
		3061
	};
	
	private int getItemReward() {
		int reward = 995;
		ArrayList<Integer> possibleRewards = new ArrayList<Integer>();
		for(int rew : itemRewards)
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
		return reward;
	}
	
	public void handleReward(){
		destroyEvent(false);
		if(correct >= 4)
		{
			int itemReward = getItemReward();
			Item item = new Item(itemReward, itemReward == 995 ? 1250 : 1);
			player.getInventory().addItemOrDrop(item);
			player.getDialogue().sendGiveItemNpc("Your performance was great, The mime has rewarded you.", item);
		}else{
			player.getDialogue().sendStatement("Your performance was poor, The mime did not reward you.");
		}
	}

	public void performEmotePlayer(final MimeEmote emote) {
		if(player != null)
		{
			myTurn = false;
			player.getActionSender().removeInterfaces();
			player.getUpdateFlags().sendAnimation(emote.anim);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer container) {
			    	if(player == null)
			    	{
			    		container.stop();
			    		return;
			    	}
			    	if(emote == emoteToDo)
			    	{
			    		player.getUpdateFlags().sendAnimation(862);
			    		correct++;
			  			if(correct >= 4)
		    			{
		    				handleReward();
		    		    	container.stop();
		    		    	return;
		    			}
			    	}else{
			    		player.getUpdateFlags().sendAnimation(860);
			    	}
	    			currentStage++;
	    			if(currentStage >= stages)
	    			{
	    				handleReward();
	    		    	container.stop();
	    		    	return;
	    			}
	    			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    			    @Override
	    			    public void execute(CycleEventContainer container) {
	    	    			performEmoteMime(false);
	    			    	container.stop();
	    			    }
	    		
	    			    @Override
	    			    public void stop() {
	    			    }
	    			}, 2);
			    	container.stop();
			    }
		
			    @Override
			    public void stop() {
			    }
			}, emote.duration);
		}
	}
	
	public void performEmoteMime(boolean firstTime) {
		if(mime != null)
		{
			emoteToDo = MimeEmote.values()[Misc.random(MimeEmote.values().length-1)];
			mime.setFace(3);
			if(!firstTime){
				player.getActionSender().animateObject(2007, 4761, 1, 1135);
				player.getActionSender().animateObject(2010, 4761, 1, 1136);
			}
			CycleEventHandler.getInstance().addEvent(mime, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer container) {
			    	if(mime == null)
			    	{
			    		container.stop();
			    		return;
			    	}
					mime.getUpdateFlags().sendAnimation(emoteToDo.anim);
			    	container.stop();
			    }
		
			    @Override
			    public void stop() {
			    }
			}, 2);
			CycleEventHandler.getInstance().addEvent(mime, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer container) {
	    			mime.setFace(5);
	    			CycleEventHandler.getInstance().addEvent(mime, new CycleEvent() {
	    			    @Override
	    			    public void execute(CycleEventContainer container) {
	    			    	if(mime == null)
	    			    	{
	    			    		container.stop();
	    			    		return;
	    			    	}
			    			player.getActionSender().animateObject(2007, 4761, 1, 1136);
			    			player.getActionSender().animateObject(2010, 4761, 1, 1135);
	    	    			mime.getUpdateFlags().sendAnimation(858);
	    	    			player.getActionSender().sendChatInterface(6543);
	    	    			myTurn = true;
	    			    	container.stop();
	    			    }
	    		
	    			    @Override
	    			    public void stop() {
	    			    }
	    			}, 1);
			    	container.stop();
			    }
		
			    @Override
			    public void stop() {
			    }
			}, emoteToDo.duration + 2);
		}
	}

	@Override
	public boolean doObjectClicking(int object, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
