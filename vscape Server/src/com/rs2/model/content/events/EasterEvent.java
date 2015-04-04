package com.rs2.model.content.events;

import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

public class EasterEvent {
	
	public static final int TO_GIVE = 20;
	private static final int EGG_ITEM = 1961;
	private static final int BASKET_ITEM = 4565;
	
	public static boolean handleDialogue(final Player player, int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch(id) {
			case 1835 :
				if(!player.getReceivedBasket()) {
					switch(d.getChatId()) {
						case 1 :
							d.sendPlayerChat("Wow!", Dialogues.CONTENT);
						return true;
						case 2 :
							d.sendPlayerChat("You're a big bunny!", Dialogues.HAPPY);
						return true;
						case 3 :
							d.sendNpcChat("For you.", Dialogues.DELIGHTED_EVIL);
						return true;
						case 4 :
							d.sendPlayerChat("Do you have any tasks for me?", Dialogues.HAPPY);
						return true;
						case 5 :
							d.sendNpcChat("I beleive i do " + player.getUsername() +".", Dialogues.CONTENT);
						return true;
						case 6 :
							if(player.getInventory().canAddItem(new Item(BASKET_ITEM))) {
								d.sendGiveItemNpc("The easter bunny hands you a basket full of eggs.", new Item(BASKET_ITEM,1));
								player.getInventory().addItem(new Item(BASKET_ITEM,1));
								player.setReceivedBasket(true);
								d.setNextChatId(1);
							} else {
								d.sendNpcChat("You don't seem to have the inventory space though!");
								d.endDialogue();
							}
						return true;
					}
				} else {
					if(!player.getInventory().ownsItem(BASKET_ITEM))
					{
						switch(d.getChatId()) {
							case 1 :
								d.sendPlayerChat("I've lost my basket!", Dialogues.DISTRESSED);
							return true;
							case 2 :
								if(player.getInventory().canAddItem(new Item(BASKET_ITEM))) {
									d.sendGiveItemNpc("The easter bunny hands you a new basket.", new Item(BASKET_ITEM,1));
									player.getInventory().addItem(new Item(BASKET_ITEM,1));
									player.setReceivedBasket(true);
								} else {
									d.sendNpcChat("You don't have the space for a new one!");
									d.endDialogue();
								}
								d.endDialogue();
							return true;
						}
					}else{
						if(!player.getReceivedEasterReward()) {
							switch(d.getChatId()) {
								case 1 :
									if(player.getEggsGiven() > 0 && player.getEggsGiven() < TO_GIVE)
									{
										d.sendNpcChat("As of right now you have given out...Lets see here", player.getEggsGiven() + " Easter egg"+(player.getEggsGiven() == 1 ? "" : "s")+".", Dialogues.CONTENT);
										d.setNextChatId(5);
									}
									else if(player.getEggsGiven() >= TO_GIVE)
									{
										d.sendNpcChat("As of right now you have given out...Lets see here", player.getEggsGiven() + " Easter eggs.", Dialogues.CONTENT);
										d.setNextChatId(6);
									}
									else{
										d.sendNpcChat("I need you to hand out "+TO_GIVE+" Easter eggs to the players", "all other the world.", Dialogues.CONTENT);
										d.setNextChatId(2);
									}
								return true;
								case 2 :
									d.sendPlayerChat("Will i be rewarded for this task?", Dialogues.HAPPY);
								return true;
								case 3 :
									d.sendNpcChat("Yes! I'll see what i can rustle up for you!", Dialogues.CONTENT);
								return true;
								case 4 :
									d.sendPlayerChat("Great, I'll get right to this task!", Dialogues.HAPPY);
									d.endDialogue();
								return true;
								case 5 :
									d.sendNpcChat("You still have some easter eggs left to give out.", Dialogues.CONTENT);
									d.endDialogue();
								return true;
								case 6 :
									d.sendNpcChat("Brilliant " + player.getUsername() + "! You're all done with my task.", "You did a great job!", Dialogues.CONTENT);
								return true;
								case 7 :
									d.sendPlayerChat("Well... What can I say? I'm a bit of an eggspert!", Dialogues.LAUGHING);
								return true;
								case 8 :
									d.sendGiveItemNpc("The easter bunny hands you your reward.", new Item(1037, 1));
									player.getInventory().addItemOrDrop(new Item(1037,1));
									if(!player.getInventory().ownsItem(4566)){
										player.getInventory().addItemOrDrop(new Item(4566,1));
									}
									player.setReceivedEasterReward(true);
									d.endDialogue();
								return true;
							}
						}
					}
				}
			break;
		}
		return false;
	}

	public static void GiveEgg(final Player player, final Player otherPlayer){
		player.setInteractingEntity(otherPlayer);
		player.setFollowDistance(1);
		player.setFollowingEntity(otherPlayer);
		final int taskId = player.getTask();
		World.submit(new Tick(1) {
			@Override
			public void execute() {
				if (otherPlayer == null || otherPlayer.isDead() || !player.checkTask(taskId)) {
					Following.resetFollow(player);
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
					return;
				}
				if(player.getEggsGiven() >= TO_GIVE)
				{
					player.getActionSender().sendMessage("You have handed out all of your eggs! Go see the @blu@Easter bunny!", true);
					Following.resetFollow(player);
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					this.stop();
					return;
				}
				if (player.goodDistanceEntity(otherPlayer, 1) && !player.inEntity(otherPlayer)) {
					if(player.getPlayerEgged(otherPlayer.getUsernameAsLong()))
					{
						player.getActionSender().sendMessage("You already gave this player an egg!", true);
						Following.resetFollow(player);
						player.setInteractingEntity(null);
						player.getMovementHandler().reset();
						this.stop();
						return;
					}
					if(!otherPlayer.getInventory().getItemContainer().hasRoomFor(new Item(EGG_ITEM,1)))
					{
						player.getActionSender().sendMessage("This players inventory is full!", true);
						Following.resetFollow(player);
						player.setInteractingEntity(null);
						player.getMovementHandler().reset();
						this.stop();
						return;
					}
					Following.resetFollow(player);
					player.getUpdateFlags().sendFaceToDirection(otherPlayer.getPosition());
					player.setInteractingEntity(null);
					player.getMovementHandler().reset();
					int eggIndex = player.getEggsGiven();
					player.setEggPlayer(eggIndex, otherPlayer.getUsernameAsLong());
					player.setEggsGiven(eggIndex+1);
					player.getActionSender().sendMessage("You give an easter egg to " + otherPlayer.getUsername() + "!", true);
					otherPlayer.getInventory().addItem(new Item(EGG_ITEM,1));
					otherPlayer.getActionSender().sendMessage(player.getUsername() + " has given you an easter egg!", true);
					this.stop();
				}
			}
		});
	}
	
}
