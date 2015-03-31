package com.rs2.model.content.quests.impl;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;

import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;
import com.rs2.util.clip.ObjectDef;

public class JunglePotion implements Quest {

	public static final int questIndex = 7365; //Used in player's quest log interface, id is in Player.java //Change
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int GET_SNAKE = 2;
	public static final int GET_ARDRIGAL = 3;
	public static final int GET_SITO = 4;
	public static final int GET_MOSS = 5;
	public static final int GET_PURSE = 6;
	public static final int QUEST_COMPLETE = 7;

	//Items
	public static final int SNAKE_WEED_G = 1525;
	public static final int SNAKE_WEED_C = 1526;
	public static final int ARDRIGAL_G = 1527;
	public static final int ARDRIGAL_C = 1528;
	public static final int SITO_FOIL_G = 1529;
	public static final int SITO_FOIL_C = 1530;
	public static final int VOLENCIA_MOSS_G = 1531;
	public static final int VOLENCIA_MOSS_C = 1532;
	public static final int ROGUES_PURSE_G = 1533;
	public static final int ROGUES_PURSE_C = 1534;

    //Positions
	public static final Position ROGUES_PURSE_CAVE = new Position(2830, 9522, 0);
	public static final Position ROGUES_PURSE_CAVE_EXIT = new Position(2823, 3119, 0);
	
    //Npcs
	public static final int WITCHDOCTA = 740;

	//Objects
	public static final int SNAKE_WEED_VINE = 2575;
	public static final int ARDRIGAL_TREE = 2577;
	public static final int SITO_GROUND = 2579;
	public static final int VOLENCIA_ROCK = 2581;
	public static final int ROUGE_CAVE_ROCKS = 2584;
	public static final int ROUGES_CAVE_HANDHOLDS = 2585;
	public static final int ROUGES_CAVE_FUNGUS = 2583;
	public int dialogueStage = 0;

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = {
		{Skill.HERBLORE, 775}
	};

	private static final int questPointReward = 1; //Change

	public int getQuestID() { //Change
		return 41;
	}

	public String getQuestName() { //Change
		return "Jungle Potion";
	}

	public String getQuestSaveName() { //Change
		return "junglepotion";
	}

	public boolean canDoQuest(final Player player) {
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1]));
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, ROGUES_PURSE_C); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("1743.75 Herblore XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		int lastIndex = 0;
		switch (questStage) {
			case QUEST_COMPLETE:
				lastIndex = 17;
				break;

		}
		lastIndex++;
		
		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Trufitus in Tai Bwo Wannai Village to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Trufitus wants some Snake Weed that grows near", 3, this.getQuestID(), GET_SNAKE);
		a.sendQuestLogString("the river to the south west of the village.", 4, this.getQuestID(), GET_SNAKE);
		a.sendQuestLogString("Now he wants some Ardrigal, which grows", 6, this.getQuestID(), GET_ARDRIGAL);
		a.sendQuestLogString("on some palm trees to the north east of the village.", 7, this.getQuestID(), GET_ARDRIGAL);
		a.sendQuestLogString("I am supposed to get Sito Foil, which likes", 9, this.getQuestID(), GET_SITO);
		a.sendQuestLogString("burned ground, maybe there is some around the village.", 10, this.getQuestID(), GET_SITO);
		a.sendQuestLogString("I have to get some Volencia Moss, sounds like it grows", 12, this.getQuestID(), GET_MOSS);
		a.sendQuestLogString("on rocks that have a high metal content,", 13, this.getQuestID(), GET_MOSS);
		a.sendQuestLogString("there is a mine near me I should check.", 14, this.getQuestID(), GET_MOSS);
		a.sendQuestLogString("The last herb is Rogue's Purse, and it only grows", 16, this.getQuestID(), GET_PURSE);
		a.sendQuestLogString("in a cave to the north.", 17, this.getQuestID(), GET_PURSE);
		
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@Trufitus @bla@in @dre@Tai Bwo Wannai Village @bla@to begin.", 1);
				a.sendQuestLogString("@dre@Requirements:", 3);
				a.sendQuestLogString((QuestHandler.questCompleted(player, QuestHandler.getQuestId("Druidic Ritual")) ? "@str@" : "@dbl@") + "Druidic Ritual", 4);
				break;
			case QUEST_COMPLETE:
				a.sendQuestLogString("@red@" + "You have completed this quest!", lastIndex + 1);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}
	
	public static int herbIdForObject(final Player player, final int id) {
		switch(id) {
			default:
				return 0;
			case SNAKE_WEED_VINE:
				player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
				if (player.getQuestStage(41) < GET_SNAKE) {
					failHerbReq(player);
					return 0;
				} else {
					player.getActionSender().sendMessage("You grab a bit of Snake weed.", true);
					return SNAKE_WEED_G;
				}
			case ARDRIGAL_TREE:
				player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
				if (player.getQuestStage(41) < GET_ARDRIGAL) {
					failHerbReq(player);
					return 0;
				} else {
					player.getActionSender().sendMessage("You manage to grab some Ardrigal from the tree.", true);
					return ARDRIGAL_G;
				}
			case SITO_GROUND:
				player.getUpdateFlags().sendAnimation(827); //"bury"
				if (player.getQuestStage(41) < GET_SITO) {
					failHerbReq(player);
					return 0;
				} else {
					player.getActionSender().sendMessage("You sweep away some dirt and find some Sito Foil.", true);
					return SITO_FOIL_G;
				}
			case VOLENCIA_ROCK:
				player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
				if (player.getQuestStage(41) < GET_MOSS) {
					failHerbReq(player);
					return 0;
				} else {
					player.getActionSender().sendMessage("You find some Moss on the rock.", true);
					return VOLENCIA_MOSS_G;
				}
			case ROUGES_CAVE_FUNGUS:
				player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
				if (player.getQuestStage(41) < GET_PURSE) {
					failHerbReq(player);
					return 0;
				} else {
					player.getActionSender().sendMessage("You pull some Rogue's Purse from the wall.", true);
					return ROGUES_PURSE_G;
				}
		}
	}
	
	public static void replaceObject(final Player player, final int object, final int x, final int y) {
		final CacheObject o = ObjectLoader.object(object, x, y, player.getPosition().getZ());
		if(o == null || object == 2583) {
			return;
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			ObjectHandler.getInstance().removeObject(x, y, player.getPosition().getZ(), 10);
			new GameObject(object + 1, x, y, player.getPosition().getZ(), o.getRotation(), 10, object, 999999);
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.setStopPacket(false);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				b.stop();
			    }

			    @Override
			    public void stop() {
				ObjectHandler.getInstance().removeObject(x, y, player.getPosition().getZ(), 10);
				new GameObject(object, x, y, player.getPosition().getZ(), o.getRotation(), 10, 0, 999999);
			    }
			}, 20);
		    }
		}, 2);
	}
	
	public static void failHerbReq(final Player player) {
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}
			@Override
			public void stop() {
				player.getActionSender().sendMessage("You don't find anything interesting.", true);
				player.setStopPacket(false);
			}
		}, 2);
	}

	public boolean itemHandling(final Player player, final int itemId) {
		switch (itemId) {
			case SNAKE_WEED_G:
			case ARDRIGAL_G:
			case SITO_FOIL_G:
			case ROGUES_PURSE_G:
			case VOLENCIA_MOSS_G:
				player.setStopPacket(true);
				player.getActionSender().sendMessage("You carefully clean the dirt and grime from the herb.");
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						b.stop();
					}
					@Override
					public void stop() {
						player.getInventory().replaceItemWithItem(new Item(itemId), new Item(itemId + 1));
						player.setStopPacket(false);
					}
				}, 1);
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		return false;
	}

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case ROUGES_CAVE_HANDHOLDS:
				Ladders.climbLadder(player, ROGUES_PURSE_CAVE_EXIT);
				return true;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {
			case SNAKE_WEED_VINE: //snake weed pick
			case ARDRIGAL_TREE: //palm tree pick
			case SITO_GROUND: //burnt ground search
			case VOLENCIA_ROCK: //rocks that have herbs
			case ROUGES_CAVE_FUNGUS: //cave fungus
				player.getActionSender().sendMessage("You search the " + ObjectDef.getObjectDef(object).getName().toLowerCase() + "...");
				final int itemId = herbIdForObject(player, object);
				if (itemId != 0) {
					player.setStopPacket(true);
					replaceObject(player, object, x, y);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}
						@Override
						public void stop() {
							player.getInventory().addItemOrDrop(new Item(itemId));
							player.setStopPacket(false);
						}
					}, 1);
					return true;
				}
				return false;
			case ROUGE_CAVE_ROCKS:
				Ladders.climbLadderDown(player, ROGUES_PURSE_CAVE);
				return true;

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case WITCHDOCTA:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case 0:
						switch (d.getChatId()) {
							case 1:
								if (!QuestHandler.questCompleted(player, QuestHandler.getQuestId("Druidic Ritual"))) {
									d.sendNpcChat("Go away, I need to work on this potion.", Dialogues.ANNOYED);
									d.endDialogue();
									return true;
								} else {
									d.sendNpcChat("Greetings Bwana, I am Trufitus Shakaya", "of the Tai Bwo Wannai Village.");
									return true;
								}
							case 2:
								d.sendNpcChat("Welcome to my humble village.");
								return true;
							case 3:
								d.sendOption("What does Bwana mean?", "It's a nice village, where is everyone?");
								return true;
							case 4:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 2:
										d.setNextChatId(7);
										break;
								}
								return true;
							case 5:
								d.sendNpcChat("It means outsider.");
								d.setNextChatId(3);
								return true;
							case 7:
								d.sendNpcChat("My people are afraid to stay in the village. They have", "returned to the jungle. I need to commune with the", "gods to see what fate befalls us.");
								return true;
							case 8:
								d.sendNpcChat("You may be able to help with this.");
								return true;
							case 9:
								d.sendOption("Me? How can I help?", "I'm much too busy to help a savage.");
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 11:
								d.sendNpcChat("I need to make a special brew! A potion that helps me", "to commune with the gods. For this potion, I need very", "special herbs, that are only found in the deep jungle.");
								return true;
							case 12:
								d.sendNpcChat("I can only guide you so far as the herbs are not easy", "to find. With some luck, you will find each herb in turn", "and bring it to me. I will then give you details of where", "to find the next herb.");
								return true;
							case 13:
								d.sendNpcChat("In return for this favor I will give you training", "in Herblore.");
								return true;
							case 14:
								d.sendOption("Hmmm, sounds difficult... No thank you.", "It sounds just like the challenge for me.");
								return true;
							case 15:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 1)
									d.endDialogue();
								return true;
							case 16:
								d.sendNpcChat("That is excellent Bwana! The first herb you need", "to gather is called Snake Weed.");
								return true;
							case 17:
								d.sendNpcChat("It grows near vines in an area to the south west where", "the ground turns soft and the water kisses your feet.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), GET_SNAKE);
								return true;
						}
						return false;
					case GET_SNAKE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you bring me the Snake Weed?");
								return true;
							case 2:
								d.sendOption("Yes it's right here.", "Oh that's what I need.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if (optionId == 2) {
									d.setNextChatId(5);
								}
								return true;
							case 4:
								if (player.getInventory().playerHasItem(SNAKE_WEED_C)) {
									d.sendNpcChat("Great you do have the Snake Weed! Many thanks. Ok,", "the next herb is called Ardrigal. It is related to the palm.", "and grows to the east in it's brothers shady profusion.");
									player.getInventory().removeItem(new Item(SNAKE_WEED_C));
									player.setQuestStage(this.getQuestID(), GET_ARDRIGAL);
								} else {
									d.sendNpcChat("Don't lie to me, return to me when", "you have the Snake weed.");
								}
								d.endDialogue();
								return true;

							case 5:
								d.sendNpcChat("Well return to me when you have it then.");
								d.endDialogue();
								return true;
						}
						return false;
					case GET_ARDRIGAL:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you bring me the Ardrigal?");
								return true;
							case 2:
								d.sendOption("Yes it's right here.", "Oh that's what I need.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if (optionId == 2) {
									d.setNextChatId(6);
								}
								return true;
							case 4:
								if (player.getInventory().playerHasItem(ARDRIGAL_C)) {
									d.sendNpcChat("Great you do have the Ardrigal! Many thanks.");
									player.getInventory().removeItem(new Item(ARDRIGAL_C));
									return true;
								} else {
									d.sendNpcChat("Don't lie to me, return to me", "when you have the Ardrigal.");
									d.endDialogue();
								}
								return true;
							case 5:
								d.sendNpcChat("You are doing well Bwana. The next herb is called Sito", "Foil, and it grows best were the ground has been", "blackened by living flame.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), GET_SITO);
								return true;
							case 6:
								d.sendNpcChat("Well return to me when you have it then.");
								d.endDialogue();
								return true;
						}
						return false;
					case GET_SITO:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you bring me the Sito Foil?");
								return true;
							case 2:
								d.sendOption("Yes it's right here.", "Oh that's what I need.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if (optionId == 2) {
									d.setNextChatId(7);
								}
								return true;
							case 4:
								if (player.getInventory().playerHasItem(SITO_FOIL_C)) {
									d.sendNpcChat("Well done Bwana, only two more herbs to collect.");
									player.getInventory().removeItem(new Item(SITO_FOIL_C));
								} else {
									d.sendNpcChat("Don't lie to me, return to me", "when you have the Sito Foil.");
									d.endDialogue();
								}
								return true;
							case 5:
								d.sendNpcChat("The next herb is called Volencia Moss. It clings to", "rocks for its existence. It is difficult to see, so you", "must search for it well.");
								return true;
							case 6:
								d.sendNpcChat("It prefers rocks of high metal content and a frequently", "disturbed enviroment. There is some, I believe to the", "southeast of this village.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), GET_MOSS);
								return true;
							case 7:
								d.sendNpcChat("Well return to me when you have it then.");
								d.endDialogue();
								return true;
						}
						return false;
					case GET_MOSS:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you bring me the Volencia Moss?");
								return true;
							case 2:
								d.sendOption("Yes it's right here.", "Oh that's what I need.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if (optionId == 2) {
									d.setNextChatId(7);
								}
								return true;
							case 4:
								if (player.getInventory().playerHasItem(VOLENCIA_MOSS_C)) {
									d.sendNpcChat("Ah Volencia Moss, beautiful. One final herb and the", "potion will be complete.");
									player.getInventory().removeItem(new Item(VOLENCIA_MOSS_C));
								} else {
									d.sendNpcChat("Don't lie to me, return to me", "when you have the Volencia Moss.");
									d.endDialogue();
								}
								return true;
							case 5:
								d.sendNpcChat("This is the most difficult to find as it inhabits the", "darkness of the underground. It is called Rogue's", "Purse, and is only to be found in the caverns");
								return true;
							case 6:
								d.sendNpcChat("in the nothern part of the island. A secret entrance to", "the cavern is set into the nothern cliffs of this island.", "Take care Bwana as it may be dangerous");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), GET_PURSE);
								return true;
							case 7:
								d.sendNpcChat("Well return to me when you have it then.");
								d.endDialogue();
								return true;
						}
						return false;
					case GET_PURSE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you bring me the Rogue's Purse?");
								return true;
							case 2:
								d.sendOption("Yes it's right here.", "Oh that's what I need.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if (optionId == 2) {
									d.setNextChatId(7);
								}
								return true;
							case 4:
								if (player.getInventory().playerHasItem(ROGUES_PURSE_C)) {
									d.sendNpcChat("Rogue's Purse, I can't believe it!");
									player.getInventory().removeItem(new Item(ROGUES_PURSE_C));
								} else {
									d.sendNpcChat("Don't lie to me, return to me when", "you have the Rogue's Purse.");
									d.endDialogue();						
								}
								return true;
							case 5:
								d.sendNpcChat("Many blessings on you! I must prepare, please", "excuse me while I make the arrangements.");
								return true;
							case 6:
								d.dontCloseInterface();
								QuestHandler.completeQuest(player, this.getQuestID());
								return true;
							case 7:
								d.sendNpcChat("Well return to me when you have it then.");
								d.endDialogue();
								return true;
						}
						return false;

					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("I can see for miles.", Dialogues.HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
