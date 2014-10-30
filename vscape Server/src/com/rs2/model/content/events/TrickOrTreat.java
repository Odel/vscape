package com.rs2.model.content.events;

import java.util.ArrayList;
import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class TrickOrTreat {

	private final static String insults[] = {
		"Hey zombie, Nice chrome dome!",
		"Hey what happened to your hair!",
		"Nice weather around hair... I mean here!",
		"Why did the bald man go outside? To get some fresh hair!",
		"What do you call a pen with no hair? A bald point!"
	};
	
	public static boolean handleNpcFirstClick(final Player player, Npc npc) {
		final int id = npc.getDefinition().getId();
		switch(id) {
			case 2868:
				catchHead(player,npc);
			return true;
		}
		return false;
	}
	
	private static void catchHead(final Player player, final Npc npc){
		if(player.hasItem(6722)){
			player.getActionSender().sendMessage("You already have a zombie head!");
			return;
		}
		player.getUpdateFlags().sendAnimation(832);
		player.getActionSender().sendMessage("You attempt to catch the zombie head...");
		player.setStopPacket(true);
        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
            	if(Misc.random(100) > 25){
            		player.getActionSender().sendMessage("But fail catching the head...");
            		npc.getFollowing().stepAway();
            		container.stop();
            		return;
            	}else{
            		player.getActionSender().sendMessage("You manage to catch the zombie head!");
            		player.getInventory().addItemOrDrop(new Item(6722,1));
            		player.setHasZombieHead(true);
            		NpcLoader.destroyNpc(npc);
            		container.stop();
            		return;
            	}
            }
            @Override
            public void stop(){
            	player.setStopPacket(false);
            }
        }, 2);
	}
	
	public static boolean handleNpcSecondClick(final Player player, Npc npc) {
		final int id = npc.getDefinition().getId();
		switch(id) {
			case 2866:
				Dialogues.sendDialogue(player, id, 4, 0);
			return true;
		}
		return false;
	}
	
	private static void removeHead(Player player, Npc npc){
		npc.getUpdateFlags().setForceChatMessage("Arrrrrrrrrrrrgh!!!");
		npc.sendTransform(2867, 100);
		spawnHead(player, npc.getPosition(), 100);
	}
	
	public static void spawnHead(Player player, Position position, int ticks) {
	    final Npc npc = new Npc(2868);
	    npc.setPosition(position);
	    npc.setSpawnPosition(position);
		int x = 0, y = 0;
		if (npc.canMove(1, 0)) {
			x = 1;
			y = 0;
		} else if (npc.canMove(-1, 0)) {
			x = -1;
			y = 0;
		} else if (npc.canMove(0, 1)) {
			x = 0;
			y = 1;
		} else if (npc.canMove(0, -1)) {
			x = 0;
			y = -1;
		}
		x = npc.getPosition().getX() + x;
		y = npc.getPosition().getY() + y;
	    npc.setSpawnPosition(new Position(x, y, player.getPosition().getZ()));
	    npc.setPosition(new Position(x, y, player.getPosition().getZ()));
	    npc.setCurrentX(x);
	    npc.setCurrentY(y);
	    npc.setPlayerOwner(player.getIndex());
	    World.register(npc);
		World.submit(new Tick(ticks) {
			@Override
			public void execute() {
				NpcLoader.destroyNpc(npc);
			}
		});
	}
	
	private static void childRunaway(final Npc npc){
		if (!Misc.goodDistance(npc.getPosition(), npc.getSpawnPosition(), 3)) {
			npc.resetActions();
			npc.walkTo(npc.getSpawnPosition().getX(), npc.getSpawnPosition().getY(), true);
			return;
		}
		npc.getFollowing().stepAway();
	}
	
	private static String getInsult(){
		return insults[Misc.random(insults.length-1)];
	}
	
	private static Item getSweets(){
		return new Item(Constants.SWEETS[Misc.random(Constants.SWEETS.length-1)],1);
	}
	
	private static Item[] hasSweets(final Player player){
		ArrayList<Item> sweets = new ArrayList<Item>();
		for(int i = 0; i < Constants.SWEETS.length; i++){
			if(player.getInventory().playerHasItem(Constants.SWEETS[i]))
			{
				sweets.add(new Item(Constants.SWEETS[i],1));
			}
		}
		if(sweets.size() > 0)
			return sweets.toArray(new Item[sweets.size()]);
		else
			return null;
	}
	
	public static boolean handleDialogue(final Player player, int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		switch(id) {
			case 2866 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						d.sendPlayerChat("Hello, how's it going?", Dialogues.CONTENT);
					return true;
					case 2 :
						d.sendNpcChat("*grumble* human.. *grumble*", Dialogues.ANGRY_1);
					return true;
					case 3 :
						d.sendPlayerChat("Well.. I guess he doesn't want to talk.", Dialogues.DISTRESSED);
						d.endDialogue();
					return true;
					case 4 :
						d.sendPlayerChat(getInsult(), Dialogues.LAUGHING);
						d.setNextChatId(1000);
					return true;
					case 1000 :
						d.sendNpcChat("Arrrrrrrrrrrrgh!!!", Dialogues.ANGRY_1);
						removeHead(player, npc);
						d.endDialogue();
					return true;
				}
			break;
			case 2867 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						d.sendStatement("I don't think that will work...");
						d.endDialogue();
					return true;
				}
			break;
			case 2872 :
			case 2873 :
			case 2874 :
			case 2875 :
			case 2876 :
			case 2877 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						d.sendNpcChat("Trick or Treat!", Dialogues.HAPPY);
					return true;
					case 2 :
						player.getDialogue().sendOption("Trick", "Treat");
					return true;
					case 3 :
						switch(optionId) {
						case 1 :
								if(!player.getInventory().playerHasItem(6722))
								{
									d.sendStatement("You don't have anything to trick the child with.");
									d.endDialogue();
								}else{
									player.getUpdateFlags().sendAnimation(2844);
									player.getUpdateFlags().setForceChatMessage("Muahahahah!");
									d.sendNpcChat("AHHHHHHH!", Dialogues.DISTRESSED);
									npc.getUpdateFlags().setForceChatMessage("AHHHHHHH!");	
									if(Misc.random(100) < 60){
										GroundItemManager.getManager().dropItem(new GroundItem(getSweets(), player, npc.getPosition()));
									}
									childRunaway(npc);
									d.endDialogue();
								}
							return true;
						case 2 :
							player.getDialogue().sendOption("Give Coins", "Give Sweet", "Cancel");
							return true;
						}
					break;
					case 4 :
						switch(optionId) {
							case 1 :
								if(player.getInventory().playerHasItem(995, 10)){
									player.getInventory().removeItem(new Item(995, 10));
									d.sendPlayerChat("Here, have 10gp", Dialogues.CONTENT);
								}else{
									d.sendPlayerChat("I don't have enough coins.", Dialogues.CONTENT);
									d.endDialogue();
								}	
							return true;
							case 2 :
								if(hasSweets(player) != null)
								{
									Item[] sweets = hasSweets(player);
									Item sweet = sweets[Misc.random(sweets.length-1)];
									player.getInventory().removeItem(sweet);
									d.sendPlayerChat("Here, have these "+sweet.getDefinition().getName(), Dialogues.CONTENT);
								}else{
									d.sendPlayerChat("I don't have any sweets.", Dialogues.CONTENT);
									d.endDialogue();
								}	
							return true;
							case 3 :
								d.sendPlayerChat("Nervermind", Dialogues.CONTENT);
								d.endDialogue();
							return true;
						}
					break;
					case 5 : // treat end
						d.sendNpcChat(player.getUsername()+" is the bestest adventurer EVER!", Dialogues.HAPPY);
						npc.getUpdateFlags().setForceChatMessage(player.getUsername()+" is the bestest adventurer EVER!");
						d.endDialogue();
					return true;
				}
			break;
		}
		return false;
	}
}
