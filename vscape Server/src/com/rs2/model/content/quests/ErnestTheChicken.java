package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class ErnestTheChicken implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int HUNT_FOR_ITEMS = 2;
	public static final int QUEST_COMPLETE = 3;

	public static final int SPADE = 952;
	public static final int PRESSURE_GAUGE = 271;
	public static final int FISH_FOOD = 272;
	public static final int POISON = 273;
	public static final int POISON_FISH_FOOD = 274;
	public static final int KEY = 275;
	public static final int RUBBER_TUBE = 276;
	public static final int OIL_CAN = 277;

	public static final int X = 1;
	public static final int Y = 2;
	public static final int[] LEVER_A = {146, 3108, 9745};
	public static final int[] LEVER_B = {147, 3118, 9752};
	public static final int[] LEVER_C = {148, 3112, 9760};
	public static final int[] LEVER_D = {149, 3108, 9767};
	public static final int[] LEVER_E = {150, 3097, 9767};
	public static final int[] LEVER_F = {151, 3096, 9765};
	public static final int COMPOST_HEAP = 152;
	public static final int FOUNTAIN = 153;
	public static final int CRATE = 11485;
	public static final int RUBBER_TUBE_DOOR = 131;

	public static final int PULL_LEVER_ANIM = 2140;

	public static final Position VERONICA_POS = new Position(3111, 3330, 0);
	public static final Position PROFESSOR_POS = new Position(3110, 3365, 2);
	public static final Position MANOR = new Position(3109, 3352, 0);
	public static final Position DOWN_IN_ROOM = new Position(3117, 9753, 0);
	public static final Position UP_FROM_ROOM = new Position(3092, 3361, 0);
	public static final Position MAIN_ROOM = new Position(3108, 9753, 0);
	public static final Position OIL_CAN_ROOM = new Position(3092, 9755, 0);
	public static final Position ROOM_1 = new Position(3098, 9765, 0);
	public static final Position ROOM_2 = new Position(3102, 9765, 0);
	public static final Position ROOM_3 = new Position(3098, 9760, 0);
	public static final Position ROOM_4 = new Position(3102, 9760, 0);
	public static final Position ROOM_5 = new Position(3108, 9760, 0);
	public static final Position DOWN_SPIRAL_STAIRS = new Position(3105, 3364, 1);

	/* maybe someday if we write a proper packet send method for constructing instanced regions
	 public static final int[] ROOM2_WEST_DOOR = {138, 3100, 9765};
	 public static final int[] ROOM5_SOUTHWEST_DOOR = {139, 3105, 9760};
	 public static final int[] ROOM4_WEST_DOOR = {140, 3100, 9760};
	 public static final int[] OIL_CAN_DOOR = {141, 3100, 9755};
	 public static final int[] ROOM2_SOUTH_DOOR = {142, 3100, 9755};
	 public static final int[] ROOM1_SOUTH_DOOR = {143, 3097, 9763};
	 public static final int[] ROOM5_SOUTH_DOOR = {144, 3108, 9758};
	 public static final int[] ROOM4_SOUTH_DOOR = {145, 3102, 9758};
	 */
	public static final int STAIRS_DOWN = 2616;
	public static final int COFFIN = 2614;

	public static final int VERONICA = 285;
	public static final int PROFESSOR = 286;
	public static final int ERNEST = 287;
	public static final int ERNEST_CHICKEN = 288;

	public int dialogueStage = 0;
	private int reward[][] = {
		{995, 1300}
	};
	private int expReward[][] = {};

	private static final int questPointReward = 4;

	public int getQuestID() {
		return 22;
	}

	public String getQuestName() {
		return "Ernest the Chicken";
	}

	public String getQuestSaveName() {
		return "ernest-chicken";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItem(new Item(rewards[0], rewards[1]));
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
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("4 Quest Points", 12150);
		player.getActionSender().sendString("1300 coins", 12151);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7339);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Veronica near Draynor Manor to start this quest.", 8147);

			player.getActionSender().sendString("Veronica told me Ernest may be lost in Draynor Manor.", 8149);
			player.getActionSender().sendString("I should go check it out, perhaps I'll find him.", 8150);
		} else if (questStage == HUNT_FOR_ITEMS) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Veronica near Draynor Manor to start this quest.", 8147);
			player.getActionSender().sendString("@str@" + "Veronica told me Ernest may be lost in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "I should go check it out, perhaps I'll find him.", 8150);

			player.getActionSender().sendString("Well, I discovered Ernest has been turned into a chicken.", 8152);
			player.getActionSender().sendString("Professor Oddenstein transformed him, and to", 8153);
			player.getActionSender().sendString("transform him back, I need a few items for his machine:", 8154);

			if (player.getInventory().ownsItem(PRESSURE_GAUGE)) {
				player.getActionSender().sendString("@str@" + "-The pressure gauge.", 8155);
			} else {
				player.getActionSender().sendString("@dre@" + "-The pressure gauge.", 8155);
			}
			if (player.getInventory().ownsItem(RUBBER_TUBE)) {
				player.getActionSender().sendString("@str@" + "-The rubber tube.", 8156);
			} else {
				player.getActionSender().sendString("@dre@" + "-The rubber tube.", 8157);
			}
			if (player.getInventory().ownsItem(OIL_CAN)) {
				player.getActionSender().sendString("@str@" + "-The oil can", 8158);
			} else {
				player.getActionSender().sendString("@dre@" + "-The oil can.", 8158);
			}
			if (hasItems(player)) {
				player.getActionSender().sendString("I should bring the items to the Professor.", 8160);
			} else {
				player.getActionSender().sendString("The professor said the items should be around the Manor.", 8160);
			}
		} /*else if (questStage == ITEMS_GIVEN) { can't talk to ernest wat the fug
		 player.getActionSender().sendString(getQuestName(), 8144);
		 player.getActionSender().sendString("@str@" + "Talk to Veronica near Draynor Manor to start this quest.", 8147);
		 player.getActionSender().sendString("@str@" + "Veronica told me Ernest may be lost in Draynor Manor.", 8149);
		 player.getActionSender().sendString("@str@" + "I should go check it out, perhaps I'll find him.", 8150);
		 player.getActionSender().sendString("@str@" + "Well, I discovered Ernest has been turned into a chicken.", 8152);
		 player.getActionSender().sendString("@str@" + "Professor Oddenstein transformed him, and to", 8153);
		 player.getActionSender().sendString("@str@" + "transform him back, I need a few items for his machine:", 8154);
	    
		 player.getActionSender().sendString("Professor Oddenstein turned Ernest back to a human!", 8156);
		 player.getActionSender().sendString("I should talk to Ernest for my reward.", 8157);
		 }*/ else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Veronica near Draynor Manor to start this quest.", 8147);
			player.getActionSender().sendString("@str@" + "Veronica told me Ernest may be lost in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "I should go check it out, perhaps I'll find him.", 8150);
			player.getActionSender().sendString("@str@" + "Well, I discovered Ernest has been turned into a chicken.", 8152);
			player.getActionSender().sendString("@str@" + "Professor Oddenstein transformed him, and to", 8153);
			player.getActionSender().sendString("@str@" + "transform him back, I need a few items for his machine:", 8154);
			player.getActionSender().sendString("@str@" + "Professor Oddenstein turned Ernest back to a human!", 8156);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8158);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Talk to Veronica near Draynor Manor to start this quest.", 8147);
			player.getActionSender().sendString("Requirements: Ability to dodge a level 22 skeleton.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7339);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7339);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7339);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7339);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void clickObject(Player player, int object) {
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to Veronica near Draynor Manor to start this quest.", 8147);
		player.getActionSender().sendString("Requirements: Ability to dodge a level 22 skeleton.", 8148);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static void resetLevers(final Player player) {
		player.setErnestLevers(0, false); //A
		player.setErnestLevers(1, false); //B
		player.setErnestLevers(2, false); //C
		player.setErnestLevers(3, false); //D
		player.setErnestLevers(4, false); //E
		player.setErnestLevers(5, false); //F
	}

	public static boolean hasItems(final Player player) {
		Inventory i = player.getInventory();
		return i.playerHasItem(PRESSURE_GAUGE) && i.playerHasItem(RUBBER_TUBE) && i.playerHasItem(OIL_CAN);
	}

	public static boolean ernestSpawned() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == ERNEST) {
				return true;
			}
		}
		return false;
	}

	public static void destroyErnest() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == ERNEST) {
				NpcLoader.destroyNpc(npc);
			}
		}
	}

	public boolean itemHandling(Player player, int itemId) {
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if ((firstItem == POISON && secondItem == FISH_FOOD) || (firstItem == FISH_FOOD && secondItem == POISON)) {
			player.getInventory().removeItem(new Item(POISON));
			player.getInventory().replaceItemWithItem(new Item(FISH_FOOD), new Item(POISON_FISH_FOOD));
			player.getActionSender().sendMessage("You poison the fish food.");
			return true;
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case COMPOST_HEAP:
				if (item == SPADE && player.getQuestStage(22) == HUNT_FOR_ITEMS && !player.getInventory().playerHasItem(KEY)) {
					player.getUpdateFlags().sendAnimation(830);
					player.getActionSender().sendMessage("You dig through the compost...");
					player.getActionSender().sendMessage("...and find a small key.");
					player.getInventory().addItem(new Item(KEY));
					return true;
				}
			case FOUNTAIN:
				if (item == POISON_FISH_FOOD && player.getQuestStage(22) == HUNT_FOR_ITEMS && !player.getInventory().playerHasItem(PRESSURE_GAUGE)) {
					player.getActionSender().sendMessage("You put the poisoned fish food into the fountain and all the fish die.");
					player.getInventory().removeItem(new Item(POISON_FISH_FOOD));
					player.getUpdateFlags().sendAnimation(832);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							player.getDialogue().sendGiveItemNpc("You find a pressure gauge in the fountain.", new Item(PRESSURE_GAUGE));
							player.getInventory().addItem(new Item(PRESSURE_GAUGE));
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
						}
					}, 6);
					return true;
				}
		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case 146: //0 A
				if (x == LEVER_A[X] && y == LEVER_A[Y]) {
					if (!player.getErnestLevers()[0]) {
						player.getActionSender().sendMessage("You pull Lever A.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.setErnestLevers(0, true);
						return true;
					} else {
						player.getActionSender().sendMessage("You pull Lever A.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.setErnestLevers(0, false);
						return true;
					}
				}
				return false;
			case 147: //1 B
				if (x == LEVER_B[X] && y == LEVER_B[Y]) {
					if (player.getErnestLevers()[0] && !player.getErnestLevers()[1] && !player.getErnestLevers()[3]) {
						player.getActionSender().sendMessage("You pull Lever B.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.setErnestLevers(1, true);
						player.fadeTeleport(ROOM_5);
						return true;
					} else if (!player.getErnestLevers()[0] && player.getErnestLevers()[1] && player.getErnestLevers()[3]) {
						player.getActionSender().sendMessage("You pull Lever B.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.setErnestLevers(1, false);
						player.fadeTeleport(ROOM_1);
						return true;
					} else {
						player.getActionSender().sendMessage("You pull Lever B.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.fadeTeleport(MANOR);
						resetLevers(player);
						return true;
					}
				}
				return false;
			case 148: //2 C
				if (x == LEVER_C[X] && y == LEVER_C[Y]) {
					if (player.getErnestLevers()[5]) {
						player.getActionSender().sendMessage("You pull Lever C.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.setErnestLevers(2, true);
						player.fadeTeleport(ROOM_1);
						return true;
					} else {
						player.getActionSender().sendMessage("You pull Lever C.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.fadeTeleport(MANOR);
						resetLevers(player);
						return true;
					}
				}
				return false;
			case 149: //3 D
				if (x == LEVER_D[X] && y == LEVER_D[Y]) {
					if (player.getErnestLevers()[0] && player.getErnestLevers()[1]) {
						player.getActionSender().sendMessage("You pull Lever D.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.setErnestLevers(3, true);
						player.fadeTeleport(MAIN_ROOM);
						return true;
					} else {
						player.getActionSender().sendMessage("You pull Lever D.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.fadeTeleport(MANOR);
						resetLevers(player);
						return true;
					}
				}
				return false;
			case 150: //4 E
				if (x == LEVER_E[X] && y == LEVER_E[Y]) {
					if (player.getErnestLevers()[5] && !player.getErnestLevers()[2]) {
						player.getActionSender().sendMessage("You pull Lever E.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.fadeTeleport(ROOM_5);
						player.setErnestLevers(4, true);
						return true;
					} else if (player.getErnestLevers()[2]) {
						player.getActionSender().sendMessage("You pull Lever E.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.fadeTeleport(OIL_CAN_ROOM);
						resetLevers(player);
						return true;
					} else {
						player.getActionSender().sendMessage("You pull Lever E.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.fadeTeleport(MANOR);
						resetLevers(player);
						return true;
					}
				}
				return false;
			case 151: //5 F
				if (x == LEVER_F[X] && y == LEVER_F[Y]) {
					if (!player.getErnestLevers()[0] && !player.getErnestLevers()[1] && player.getErnestLevers()[3]) {
						player.getActionSender().sendMessage("You pull Lever F.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.setErnestLevers(5, true);
						return true;
					} else {
						player.getActionSender().sendMessage("You pull Lever F.");
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.fadeTeleport(MANOR);
						resetLevers(player);
						return true;
					}
				}
				return false;
			case 155:
			case 156: //bookshelf secret doors
				if (x == 3097 && player.getPosition().getX() > 3097) {
					player.getActionSender().sendMessage("As you pull one of the books, the bookcase opens...");
					player.teleport(new Position(player.getPosition().getX() - 2, player.getPosition().getY(), 0));
					return true;
				} else if (x == 3097 && player.getPosition().getX() < 3097) {
					player.getActionSender().sendMessage("The wall won't budge from this side.");
					return true;
				}
				return false;
			case 160: //bookcase lever
				if (x == 3096) {
					player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
					player.teleport(new Position(player.getPosition().getX() + 2, player.getPosition().getY(), 0));
					return true;
				}
				return false;
			case 133: //ladder down to room
				if (x == 3092) {
					if (player.getQuestStage(22) > 0) {
						Ladders.climbLadder(player, DOWN_IN_ROOM);
						resetLevers(player);
						return true;
					} else {
						player.getDialogue().sendPlayerChat("I'm not sure about this. It looks dark down there.", CONTENT);
						return true;
					}
				}
				return false;
			case 132: //ladder up to manor
				if (x == 3117) {
					Ladders.climbLadder(player, UP_FROM_ROOM);
					resetLevers(player);
					return true;
				}
				return false;
			case 9584: //spiral stairs down from professor
				if (x == 3105 && y == 3363 && ernestSpawned() && player.getQuestStage(22) == QUEST_COMPLETE) {
					destroyErnest();
					player.teleport(DOWN_SPIRAL_STAIRS);
					return true;
				}
				return false;
			case CRATE:
				if (player.getPosition().getX() < 3100 && player.getPosition().getY() < 9758) {
					if (player.getInventory().canAddItem(new Item(OIL_CAN)) && player.getQuestStage(22) > 0 && !player.getInventory().playerHasItem(OIL_CAN)) {
						player.getUpdateFlags().sendAnimation(832);
						player.getActionSender().sendMessage("You find an old oil can in the crate.");
						player.getInventory().addItem(new Item(OIL_CAN));
						player.fadeTeleport(MANOR);
						resetLevers(player);
						return true;
					} else if (!player.getInventory().canAddItem(new Item(OIL_CAN)) && player.getQuestStage(22) > 0 && !player.getInventory().playerHasItem(OIL_CAN)) {
						player.getActionSender().sendMessage("Not enough room in your inventory.");
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			case RUBBER_TUBE_DOOR:
				if (player.getInventory().playerHasItem(KEY) && y == 3367) {
					player.getActionSender().walkTo(player.getPosition().getX() < 3108 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(RUBBER_TUBE_DOOR, x, y, 0);
					return true;
				} else {
					player.getActionSender().sendMessage("This door is locked.");
					return true;
				}
			case COMPOST_HEAP:
				player.getDialogue().sendPlayerChat("That's gross! I'm not digging through that with my hands!", DISTRESSED);
				return true;
			case FOUNTAIN:
				player.getActionSender().sendMessage("You see something shiny in the fountain, you reach in...");
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("...And get bitten by vicious fish!");
						player.hit(1, HitType.NORMAL);
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 2);
				return true;
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
			case VERONICA:
				switch (player.getQuestStage(22)) {
					case 0: //start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Can you please help me? I'm in a terrible spot of", "trouble.", SAD);
								return true;
							case 2:
								player.getDialogue().sendOption("Aha, sounds like a quest. I'll help.", "No, I'm looking for something to kill.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Aha, sounds like a quest. I'll help.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("No, I'm looking for something to kill", ANNOYED);
										player.getDialogue().endDialogue();
										return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("Yes yes, I suppose it is a quest. My fiance Ernest and", "I came upon this house.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Seeing as were a little lost Ernest decided to go in", "and ask for directions", SAD);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("That was an hour ago. That house looks spooky, can", "you go and see if you can find him for me?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Ok, I'll see what I can do.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Thank you, thank you so much.", HAPPY);
								player.getDialogue().endDialogue();
								player.setQuestStage(22, 1);
								QuestHandler.getQuests()[22].startQuest(player);
								return true;
						}
						return false;
				}
				return false;
			case PROFESSOR:
				switch (player.getQuestStage(22)) {
					case 1: //where the fUCK is ernest
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Be careful in here, there's lots of dangerous equipment.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm looking for a guy called Ernest.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Ah Ernest, top notch bloke. He's helping me with my", "experiments.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("So you know where he is then?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("He's that chicken over there.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Ernest is a chicken...? Are you sure...?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Oh, he isn't normally a chicken, or at least he wasn't", "until he helped me test my pouletmorph machine.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("It was originally going to be called a transmutation", "machine. But after testing, pouletmorph seems more", "appropriate.", HAPPY);
								return true;
							case 9:
								player.getDialogue().sendOption("Oh, I thought Veronica actually got engaged to a chicken!", "Change him back this instant!");
								return true;
							case 10:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Oh, I thought Veronica actually got engaged to a chicken!", LAUGHING);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Change him back this instant!", DISTRESSED);
										player.getDialogue().setNextChatId(12);
										return true;
								}
							case 11:
								player.getDialogue().sendPlayerChat("But I am going to need him changed back.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Umm... It's not so easy...", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("My machine is broken, and the house gremlins have", "run off with some vital bits.", SAD);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Well, I can look for them.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("That would be a help. They'll be somewhere in the", "manor house or its grounds, the gremlins never get", "further than the entrance gate.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("I'm missing the pressure gauge and a rubber tube.", "They've also taken my oil can, which I'm going to need", "to get this thing started again.", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendPlayerChat("Alright, I'll get to looking.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(22, 2);
								return true;
						}
						return false;
					case 2: //where the fUCK are these god damn items
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you found all the items yet?", CONTENT);
								return true;
							case 2:
								if (hasItems(player)) {
									player.getDialogue().sendPlayerChat("I have everything!", HAPPY);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I don't have everything yet...", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("Give 'em here then.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendStatement("You hand the Professor all the items.");
								return true;
							case 5:
								player.getInventory().removeItem(new Item(PRESSURE_GAUGE));
								player.getInventory().removeItem(new Item(RUBBER_TUBE));
								player.getInventory().removeItem(new Item(OIL_CAN));
								for (Npc npc : World.getNpcs()) {
									if (npc == null) {
										continue;
									}
									if (npc.getNpcId() == ERNEST_CHICKEN) {
										CombatManager.startDeath(npc);
										NpcLoader.spawnNpc(null, new Npc(ERNEST), npc.getPosition().clone(), false, "Woah.");
									}
								}
								Dialogues.startDialogue(player, ERNEST);
								return true;
						}
						return false;
				}
				return false;
			case ERNEST:
				switch (player.getQuestStage(22)) {
					case 2: //oh there's ernest
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Thank you sir. It was dreadfull irritating being a", "chicken. How can I ever thank you?", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Well a cash reward is always nice...", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Of course, of course. Thank you again!", HAPPY);
								player.getDialogue().endDialogue();
								player.setQuestStage(22, 4);
								QuestHandler.completeQuest(player, 22);
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

	@Override
	public boolean doNpcClicking(Player player, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

}
