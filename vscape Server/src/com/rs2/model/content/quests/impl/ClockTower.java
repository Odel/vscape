package com.rs2.model.content.quests.impl;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.DialogueManager;
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
import com.rs2.util.Misc;
import java.util.ArrayList;

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
	public static final Position[] FOOD_TROUGH_POSITIONS = {new Position(2587, 9655, 0),  new Position(2586, 9655, 0), new Position(2585, 9655, 0)};
	public static final Position RAT_DOOR_POSITION = new Position(2579, 9656, 0);
	
	private int reward[][] = { //{itemId, count},
		{995, 1125}
	};

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
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());

	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, 617);
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
				lastIndex = 12;
				break;
		}
		lastIndex++;

		ActionSender a = player.getActionSender();
		a.sendQuestLogString("I can start this quest by talking to Brother Kojo at the", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Clock Tower which is located South of Ardougne.", 2, this.getQuestID(), 0);
		a.sendQuestLogString("To repair the clock I need to find the four colored cogs", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("and place them on the four correctly colored spindles.", 5, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString((player.getQuestVars().getBlackCogPlaced() ? "@str@" : "@dbl@") + "The black cog needs to be placed on it's spindle.", 7, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString((player.getQuestVars().getBlueCogPlaced() ? "@str@" : "@dbl@") + "The blue cog needs to be placed on it's spindle.", 8, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString((player.getQuestVars().getRedCogPlaced() ? "@str@" : "@dbl@") + "The red cog needs to be placed on it's spindle.", 9, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString((player.getQuestVars().getWhiteCogPlaced() ? "@str@" : "@dbl@") + "The white cog needs to be placed on it's spindle.", 10, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I should talk to Brother Kojo now that the clock is fixed.", 12, this.getQuestID(), CLOCK_FIXED);

		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("I can start this quest by talking to @dre@Brother Kojo @bla@at the", 1);
				a.sendQuestLogString("@dre@Clock Tower @bla@which is located @dre@South @bla@of @dre@Ardougne.", 2);
				break;
			case QUEST_COMPLETE:
				a.sendQuestLogString("@dre@" + "You have completed this quest!", lastIndex);
				a.sendQuestLogString("", lastIndex + 1);
				break;
		}
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
					} else if (player.getQuestVars().blackCogQuenched) {
						player.getQuestVars().blackCogQuenched = false;
						return false;
					} else {
						player.getDialogue().sendPlayerChat("I think I'll burn my hands getting that!");
						return true;
					}
				} else {
					return false;
				}
			}
			player.getDialogue().sendPlayerChat("It's to heavy for me to pick up at the moment.");
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
			player.getQuestVars().blackCogQuenched = true;
			player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_WATER), new Item(BUCKET));
			return true;
		}
		return false;
	}

	@Override
	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public void spindlesFilled(Player player) {
		if (player.getQuestVars().getBlackCogPlaced() && player.getQuestVars().getRedCogPlaced() && player.getQuestVars().getBlueCogPlaced() && player.getQuestVars().getWhiteCogPlaced() && player.getQuestStage(this.getQuestID()) < CLOCK_FIXED) {
			player.setQuestStage(42, CLOCK_FIXED);
		}
	}
	
	public static boolean cogOnSpindleHandling(Player player, int object, int item) {
		if (checkIfCog(item)) {
			switch (item) {
				case BLACK_COG:
					if (object != BLACK_SPINDLE) {
						return false;
					}
					player.getQuestVars().setBlackCogPlaced(true);
					player.getInventory().removeItem(new Item(BLACK_COG));
					return true;
				case BLUE_COG:
					if (object != BLUE_SPINDLE) {
						return false;
					}
					player.getQuestVars().setBlueCogPlaced(true);
					player.getInventory().removeItem(new Item(BLUE_COG));
					return true;
				case RED_COG:
					if (object != RED_SPINDLE) {
						return false;
					}
					player.getQuestVars().setRedCogPlaced(true);
					player.getInventory().removeItem(new Item(RED_COG));
					return true;
				case WHITE_COG:
					if (object != WHITE_SPINDLE) {
						return false;
					}
					player.getQuestVars().setWhiteCogPlaced(true);
					player.getInventory().removeItem(new Item(WHITE_COG));
					return true;

			}
		}
		return false;
	}
	
	public static void handlePoisoningRats(final Player player) {
		player.setStopPacket(true);
		player.getActionSender().sendMessage("You carefully pour the poison into the food trough...");
		player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
		player.getInventory().removeItem(new Item(RAT_POISON));
		ArrayList<Npc> ratsArray = new ArrayList<>();
		for (Npc npc : player.getNpcs()) {
			if (npc != null && npc.getNpcId() == RAT) {
				ratsArray.add(npc);
			}
		}
		if (ratsArray.isEmpty()) {
			player.setStopPacket(false);
			return;
		}
		int numRats = 0;
		final ArrayList<Npc> finalRatsArray = ratsArray;
		for (final Npc rat : finalRatsArray) {
			if (numRats++ == 4) {
				rat.setTransformId(4936);
			}
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						int count = 0;
						int count_2 = 0;
						boolean flag = false;
						boolean unfreezeFlag = false;

						@Override
						public void execute(CycleEventContainer b) {
							count++;
							for (Position p : FOOD_TROUGH_POSITIONS) {
								if (rat.getPosition().equals(p)) {
									flag = true;
								}
							}
							if (!flag) {
								rat.walkTo(FOOD_TROUGH_POSITIONS[Misc.randomMinusOne(FOOD_TROUGH_POSITIONS.length)], true);
							} else {
								if (rat.getPosition().equals(RAT_DOOR_POSITION)) {
									b.stop();
								} else {
									if (count_2++ == 2) {
										rat.walkTo(RAT_DOOR_POSITION, true);
									}
								}
							}
							if (count == 20) {
								unfreezeFlag = true;
								b.stop();
							}
						}

						@Override
						public void stop() {
							if (rat.getTransformId() == 4936) {
								finalRatsArray.clear();
								unfreezeFlag = true;
							}
							CombatManager.startDeath(rat);
							if (unfreezeFlag) {
								player.setStopPacket(false);
							}
						}
					}, 1);

				}
			}, numRats + 1);
		}
		ratsArray.clear();
		player.getQuestVars().ratsPoisoned = true;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case BLACK_SPINDLE:
			case BLUE_SPINDLE:
			case RED_SPINDLE:
			case WHITE_SPINDLE:
				if(cogOnSpindleHandling(player, object, item)) {
					player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
					player.getActionSender().sendMessage("The cog slides onto the spindle and locks in place.");
					spindlesFilled(player);
					return true;
				}
				return false;
			case FOOD_TROUGH:
				if (item == RAT_POISON) {
					handlePoisoningRats(player);
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
				NpcLoader.newWanderNPC(RAT, 2582, 9656, 0);
			}
		}, 1);
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case 1586:
				if (x == 2575 && y == 9631) {
					player.getActionSender().walkTo(player.getPosition().getX() < 2576 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				}
				return false;
			case 33:
				if (x == 2591 && y == 9661) {
					player.getActionSender().sendMessage("You pull the lever.");
					player.getQuestVars().clockTowerGateOne = true;
					player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
					return true;
				}
				return false;
			case 34:
				if (x == 2593 && y == 9661) {
					player.getActionSender().sendMessage("You pull the lever.");
					player.getQuestVars().clockTowerGateTwo = true;
					player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
					return true;
				}
				return false;
			case 37:
				if (x == 2595 && y == 9657) {
					if (player.getPosition().getX() > 2595) {
						if (player.getQuestVars().clockTowerGateOne && player.getQuestVars().clockTowerGateTwo) {
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
				if (x == 2579 && y == 9656) {
					if (player.getPosition().getX() > 2578) {
						if (player.getQuestVars().ratsPoisoned) {
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
		DialogueManager d = player.getDialogue();
		switch (id) {
			case BROTHER_KOJO:
				switch (player.getQuestStage(this.getQuestID())) {
					case 0:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello monk.");
								return true;
							case 2:
								d.sendNpcChat("Hello adventurer. My name is Brother Kojo.", "Do you happen to know the time?");
								return true;
							case 3:
								d.sendPlayerChat("No, sorry, I don't.", HAPPY);
								return true;
							case 4:
								d.sendNpcChat("Exactly! This clock tower has recently broken down,", "and without it nobody can tell the correct time.", "I must fix it before the town people become too angry!", DISTRESSED);
								return true;
							case 5:
								d.sendNpcChat("I don't suppose you could assist me in the repairs?", "I'll pay you for your help.");
								return true;
							case 6:
								d.sendOption("Ok old monk, what can I do?", "How much reward are we talking?", "Not now old monk.");
								return true;
							case 7:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 2:
										d.setNextChatId(10);
										break;
									case 3:
										d.setNextChatId(12);
										break;
								}
								return true;
							case 8:
								d.sendNpcChat("Oh, thank you kind sir!", "In the cellar below, you'll find four cogs, they're too heavy", "for me, but you should be able to carry them one at a time.");
								return true;
							case 9:
								d.sendNpcChat("I know one goes on each floor...", "but I can't exactly remember which goes where specifically.", "Oh well, I'm sure you can figure it out fairly easily.");
								d.endDialogue();
								QuestHandler.startQuest(player, 42);
								return true;
							case 10:
								d.sendNpcChat("Well, I'm only a monk so I'm not exactly rich,", "but I assure you I will give you a fair reward for", "the time spent assisting me in repairing the clock.");
								return true;
							case 11:
								d.sendNpcChat("So how about it?");
								d.setDialogueId(6); //TODO figure out a way to go back
								return true;
							case 12:
								d.sendNpcChat("Ok then, come back and let me know", "when you change your mind.", DISTRESSED);
								d.endDialogue();
								return true;
						}
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Have you replaced all the cogs yet?", DISTRESSED);
								return true;
							case 2:
								d.sendOption("Not yet, I'm still working on it.", "Where are these cogs again?");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 1)
									d.endDialogue();
								return true;
							case 4:
								d.sendNpcChat("In the cellar below, you'll find four cogs, they're too heavy", "for me, but you should be able to carry them one at a time.");
								return true;
							case 5:
								d.sendNpcChat("I know one goes on each floor...", "but I can't exactly remember which goes where specifically.", "Oh well, I'm sure you can figure it out fairly easily.");
								d.endDialogue();
								return true;
						}
					return false;
					case CLOCK_FIXED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("I have replaced all the cogs!");
								return true;
							case 2:
								d.sendNpcChat("Really...? Wait, Listen! Well done, well done! Yes yes", "yes, you've done it! You ARE clever!", "The townsfolk will be able to know the correct time now!", HAPPY);
								return true;
							case 3:
								d.sendNpcChat("Thank you so much for all of your help!", "And as promised, here is your reward!", HAPPY);
								return true;
							case 4:
								d.dontCloseInterface();
								QuestHandler.completeQuest(player, this.getQuestID());
								return true;
						}
						return false;
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thanks to you the clock is working smoothly!", HAPPY);
								return true;
							case 2:
								d.sendPlayerChat("No problem, happy to help any... -time-!", LAUGHING);
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
