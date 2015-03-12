package com.rs2.model.content.quests.impl;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;

import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import com.rs2.model.content.quests.QuestHandler;
import static com.rs2.model.content.quests.impl.ErnestTheChicken.PULL_LEVER_ANIM;

public class ClockTower implements Quest {

	public static final int questIndex = 7353;

	public static final int QUEST_STARTED = 1;
	public static final int CLOCK_FIXED = 2;
	public static final int QUEST_COMPLETE = 3;

	//Items
	public static final int BLACK_COG = 21;
	public static final int RED_COG = 23;
	public static final int BLUE_COG = 22;
	public static final int WHITE_COG = 20;
	public static final int ICE_GLOVES = 1580;
	public static final int BUCKET_OF_WATER = 1929;
	public static final int BUCKET = 1925;
	public static final int RAT_POISON = 24;

	//Objects
	public static final int BLACK_SPINDLE = 30;
	public static final int RED_SPINDLE = 29;
	public static final int BLUE_SPINDLE = 32;
	public static final int WHITE_SPINDLE = 31;
	public static final int FOOD_TROUGH = 40;

	//NPCS
	public static final int BROTHER_KOJO = 223;
	public static final int RAT = 224;

	//Positions
	public static final Position FOOD_TROUGH_POSITION1 = new Position(2587, 9655, 0);
	public static final Position FOOD_TROUGH_POSITION2 = new Position(2586, 9655, 0);
	public static final Position FOOD_TROUGH_POSITION3 = new Position(2585, 9655, 0);
	public static final Position RAT_DOOR_POSITION = new Position(2579, 9656, 0);

	public static final int questPointReward = 1;

	public int getQuestID() {
		return 42;
	}

	public String getQuestName() {
		return "Clock Tower";
	}

	public String getQuestSaveName() {
		return "clocktower";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());

	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(995, 200, 617);
		player.getActionSender().sendString("You have completed" + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are awarded:", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("1125 Coins", 12151);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		int lastIndex = 0;

		switch (questStage) {
			case QUEST_STARTED:
				lastIndex = 9;
				break;
			case CLOCK_FIXED:
			case QUEST_COMPLETE:
				lastIndex = 11;
				break;
		}
		lastIndex++;

		ActionSender a = player.getActionSender();
		a.sendQuestLogString("I can start this quest by talking to @red@Brother Kojo @bla@at the", 1, this.getQuestID(), 0);
		a.sendQuestLogString("@red@Clock Tower @bla@which is located @red@South @bla@of @red@Ardougne", 2, this.getQuestID(), 0);
		a.sendQuestLogString("To repair the clock i need to find the four coloured cogs", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("and place them on the four correctly coloured spindles", 5, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString((cogPlaced(player.getQuestVars().getBlackCogPlaced()) ? "@str@" : "") + "Black cog still needs to be placed on it's spindle", 6, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString((cogPlaced(player.getQuestVars().getBlueCogPlaced()) ? "@str@" : "") + "Blue cog still needs to be placed on it's spindle", 7, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString((cogPlaced(player.getQuestVars().getRedCogPlaced()) ? "@str@" : "") + "Red cog still needs to be placed on it's spindle", 8, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString((cogPlaced(player.getQuestVars().getWhiteCogPlaced()) ? "@str@" : "") + "White cog still needs to be placed on it's spindle", 9, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I should talk to Brother Kojo now the clock is fixed.", 11, this.getQuestID(), CLOCK_FIXED);

		switch (questStage) {
			case QUEST_COMPLETE:
				a.sendQuestLogString("@red@" + "You have completed this quest!", lastIndex);
				a.sendQuestLogString("", lastIndex + 1);
				break;
		}
	}

	public boolean cogPlaced(boolean Cog) {
		if (Cog) {
			return true;
		}
		return false;
	}

	public static boolean cogInInventory(final Player player) {
		return player.getInventory().playerHasItem(BLACK_COG) || player.getInventory().playerHasItem(BLUE_COG) || player.getInventory().playerHasItem(RED_COG) || player.getInventory().playerHasItem(WHITE_COG);
	}

	public static boolean itemPickupHandling(Player player, int itemId) {
		if (checkIfCog(itemId)) {
			if (!cogInInventory(player)) {
				if (itemId == BLACK_COG) {
					if (player.getEquipment().getId(Constants.HANDS) == ICE_GLOVES) {
						return false;
					} else if (player.getQuestVars().getBlackCogQuenched()) {
						player.getQuestVars().setBlackCogQuenched(false);
						return false;
					} else {
						player.getDialogue().sendPlayerChat("I think i'll burn my hands getting that!", CONTENT);
						return true;
					}
				} else {
					return false;
				}
			}
			player.getDialogue().sendPlayerChat("It's to heavy for me to pick up at the moment", CONTENT);
			return true;
		}
		return false;
	}

	public static boolean checkIfCog(int itemId) {
		return itemId >= 20 && itemId <= 23;
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

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	@Override
	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		String Color = "@red@";
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			Color = "@yel@";
		} else if (questStage == QUEST_COMPLETE) {
			Color = "@gre@";
		}
		player.getActionSender().sendString(Color + getQuestName(), questIndex);
	}

	@Override
	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	@Override
	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}

	@Override
	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		return false;
	}

	@Override
	public boolean itemHandling(Player player, int itemId) {
		return false;
	}

	public static boolean itemOnGroundItemHandling(Player player, int itemId) {
		if (itemId == BLACK_COG) {
			player.getQuestVars().setBlackCogQuenched(true);
			player.getInventory().removeItem(new Item(BUCKET_OF_WATER));
			player.getInventory().addItem(new Item(BUCKET));
			return true;
		}
		return false;
	}

	@Override
	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public void spindlesFilled(Player player) {
		if (cogPlaced(player.getQuestVars().getBlackCogPlaced()) && cogPlaced(player.getQuestVars().getRedCogPlaced()) && cogPlaced(player.getQuestVars().getBlueCogPlaced()) && cogPlaced(player.getQuestVars().getWhiteCogPlaced()) && player.getQuestStage(this.getQuestID()) < CLOCK_FIXED) {
			player.setQuestStage(39, CLOCK_FIXED);
		}
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case BLACK_SPINDLE:
				if (item == BLACK_COG) {
					player.getQuestVars().setBlackCogPlaced(true);
					player.getActionSender().sendMessage("The cog slides onto the spindle and locks in place.");
					player.getInventory().removeItem(new Item(BLACK_COG));
					spindlesFilled(player);
					return true;
				}
				return false;
			case BLUE_SPINDLE:
				if (item == BLUE_COG) {
					player.getQuestVars().setBlueCogPlaced(true);
					player.getActionSender().sendMessage("The cog slides onto the spindle and locks in place.");
					player.getInventory().removeItem(new Item(BLUE_COG));
					spindlesFilled(player);
					return true;
				}
				return false;
			case RED_SPINDLE:
				if (item == RED_COG) {
					player.getQuestVars().setRedCogPlaced(true);
					player.getActionSender().sendMessage("The cog slides onto the spindle and locks in place.");
					player.getInventory().removeItem(new Item(RED_COG));
					spindlesFilled(player);
					return true;
				}
				return false;
			case WHITE_SPINDLE:
				if (item == WHITE_COG) {
					player.getQuestVars().setWhiteCogPlaced(true);
					player.getActionSender().sendMessage("The cog slides onto the spindle and locks in place.");
					player.getInventory().removeItem(new Item(WHITE_COG));
					spindlesFilled(player);
					return true;
				}
				return false;
			case FOOD_TROUGH:
				if (item == RAT_POISON) {
					player.setStopPacket(true);
					int numRats = 0;
					for (final Npc rat : player.getNpcs()) {
						if (rat != null && rat.getNpcId() == RAT) {
							numRats++;
							CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
								int count = 0;
								int count1 = 0;
								boolean flag = false;
								boolean unfreezeFlag = false;

								@Override
								public void execute(CycleEventContainer b) {
									count++;
									if (!flag) {
										if (rat.getPosition().equals(FOOD_TROUGH_POSITION1) || rat.getPosition().equals(FOOD_TROUGH_POSITION2) || rat.getPosition().equals(FOOD_TROUGH_POSITION3)) {
											flag = true;
										} else {
											rat.walkTo(FOOD_TROUGH_POSITION1, true);
										}
									} else {
										if (rat.getPosition().equals(RAT_DOOR_POSITION)) {
											b.stop();
										} else {
											rat.walkTo(RAT_DOOR_POSITION, true);
										}
									}
									if (count == 20) {
										unfreezeFlag = true;
										b.stop();
									}
								}

								@Override
								public void stop() {
									for (Npc rat : player.getNpcs()) {
										if (rat.getNpcId() == RAT) {
											count1++;
										}
									}
									if (count1 == 1) {
										unfreezeFlag = true;
									}
									NpcLoader.destroyNpc(rat);
									System.out.println(unfreezeFlag);
									if (unfreezeFlag) {
										player.setStopPacket(false);
									}
								}
							}, 1);
						}
					}
					for (int i = 0; i <= numRats; i++) {
						if (i == numRats) {
							spawnRatCycle(player, 30, true);
						} else {
							spawnRatCycle(player, 30, false);
						}
					}
					player.getQuestVars().RatsPoisoned = true;
					player.getInventory().removeItem(new Item(RAT_POISON));
					return true;
				}
				return false;
		}
		return false;

	}

	public void spawnRatCycle(final Player player, final int spawnTime, final boolean lastRat) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;

			@Override
			public void execute(CycleEventContainer b) {
				count++;
				if (count == spawnTime) {
					b.stop();
				}
			}

			@Override
			public void stop() {
				if (lastRat) {
					NpcLoader.newWanderNPC(RAT, 2582, 9656, 0);
					//rat.setTransformId(4936)   ---- 
				} else {
					NpcLoader.newWanderNPC(RAT, 2582, 9656, 0);
				}
			}
		}, 1);
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case 1586:
				if (x == 0 && y == 0) {
					player.getActionSender().walkTo(player.getPosition().getX() < 2576 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				}
				return false;
			case 33:
				if (x == 0 && y == 0) {
					player.getActionSender().sendMessage("You pull the lever.");
					player.getQuestVars().ClockTowerGateOne = true;
					player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
					return true;
				}
				return false;
			case 34:
				if (x == 0 && y == 0) {
					player.getActionSender().sendMessage("You pull the lever.");
					player.getQuestVars().ClockTowerGateTwo = true;
					player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
					return true;
				}
				return false;
			case 37:
				if (x == 0 && y == 0) {
					if (player.getPosition().getX() > 2595) {
						if (player.getQuestVars().ClockTowerGateOne && player.getQuestVars().ClockTowerGateTwo) {
							player.getActionSender().walkTo(player.getPosition().getX() < 2596 ? 1 : -1, 0, true);
							player.getActionSender().walkThroughDoor(object, x, y, 0);
							boolean spawnRats = true;
							for (Npc rat : player.getNpcs()) {
								if (rat.getNpcId() == RAT) {
									spawnRats = false;
								}
							}
							if (spawnRats) {
								spawnRatCycle(player, 5, false);
							}
						} else {
							player.getActionSender().sendMessage("The gate is locked.");
						}
					} else {
						player.getActionSender().walkTo(player.getPosition().getX() < 2596 ? 1 : -1, 0, true);
						player.getActionSender().walkThroughDoor(object, x, y, 0);
					}
					return true;
				}
				return false;
			case 39:
				if (x == 0 && y == 0) {
					if (player.getPosition().getX() > 2578) {
						if (player.getQuestVars().RatsPoisoned) {
							player.getDialogue().sendStatement("The death throes of the rats seem to have shaken the door loose of", "its hinges. You pick it up and go through.");
							player.getActionSender().walkTo(player.getPosition().getX() < 2579 ? 1 : -1, 0, true);
							player.getActionSender().walkThroughDoor(object, x, y, 0);
						} else {
							player.getActionSender().sendMessage("The gate won't budge.");
						}
					} else {
						player.getActionSender().walkTo(player.getPosition().getX() < 2579 ? 1 : -1, 0, true);
						player.getActionSender().walkThroughDoor(object, x, y, 0);
					}
					return true;
				}
				return false;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case BROTHER_KOJO:
				switch (player.getQuestStage(this.getQuestID())) {
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello monk.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Hello adventurer. My name is Brother Kojo.", "Do you happen to know the time?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("No, sorry, i don't.", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Exactly! This clock tower has recently broken down,", "and without it nobody can tell the correct time.", "I must fix it before the town people become too angry!", DISTRESSED);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I don't suppose you could assist me in the repairs?", "I'll pay you for your help.", DISTRESSED);
								return true;
							case 6:
								player.getDialogue().sendOption("OK old monk, what can i do?", "How much reward are we talking?", "Not now old monk.");
								return true;
							case 7:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Sure i'd be happy to help, what can i do?", CONTENT);
										player.getDialogue().setNextChatId(8);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("How much reward are we talking?", CONTENT);
										player.getDialogue().setNextChatId(10);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("Not now old monk.", CONTENT);
										player.getDialogue().setNextChatId(12);
										return true;
								}
							case 8:
								player.getDialogue().sendNpcChat("Oh, thank you kind sir!", "In the cellar below, you'll find four cogs, they're too heavy", "for me, but you should be able to carry them one at a time.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("I know one goes on each floor...", "but I can't exactly remember which goes where specifically.", "Oh well, I'm sure you can figure it out fairly easily.", CONTENT);
								player.getDialogue().endDialogue();
								QuestHandler.startQuest(player, 39);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Well, i'm only a monk so i'm not exactly rich,", "but i assure you i will give you a fair reward for", "the time spent assisting me in repairing the clock.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("So how about it?", CONTENT);
								player.getDialogue().setDialogueId(6); //TODO figure out a way to go back
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Ok then, come back and let me know", "when you change your mind.", DISTRESSED);
								player.getDialogue().endDialogue();
								return true;
						}
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you replaced all the cogs yet?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Not yet, i'm still working on it.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case CLOCK_FIXED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I have replaced all the cogs!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Really...? Wait, Listen! Well done, well done! Yes yes", "yes, you've done it! You ARE clever!", "The townsfolk will be able to know the correct time now!", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Thank you so much for all of your help!", "And as promised, here is your reward!", HAPPY);
								player.getDialogue().endDialogue();
								QuestHandler.completeQuest(player, this.getQuestID());
								return true;
						}
						return true;
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Thanks to you the clock is working smoothly!", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("No problem, happy to help anytime.", LAUGHING);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
