package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class BlackKnightsFortress implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int IN_FORTRESS = 2;
	public static final int CABBAGE_TIME = 3;
	public static final int BACK_TO_AMIK = 4;
	public static final int QUEST_COMPLETE = 5;

	public static final int CABBAGE = 1965;
	public static final int BAD_CABBAGE = 1967;
	public static final int IRON_CHAINBODY = 1101;
	public static final int BRONZE_MED = 1139;
	public static final int DOSSIER = 9589;

	public int dialogueStage = 0;
	private int reward[][] = {
		{995, 5625}
	};
	private int expReward[][] = {};

	private static final int questPointReward = 3;

	public int getQuestID() {
		return 18;
	}

	public String getQuestName() {
		return "Black Knight's Fortress";
	}

	public String getQuestSaveName() {
		return "black-knights-fortress";
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
		player.getActionSender().sendString("3 Quest Points,", 12150);
		player.getActionSender().sendString("5625 Coins", 12151);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7332);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Sir Amik Varze, on the third floor in the", 8147);
			player.getActionSender().sendString("@str@" + "western tower of the White Knight's Castle.", 8148);

			player.getActionSender().sendString("Sir Amik told me I need to sabotage the Black Knights.", 8150);
			player.getActionSender().sendString("He gave me a dossier on the mission.", 8151);
			player.getActionSender().sendString("Getting into the fortress unnoticed is a good start.", 8152);
		} else if (questStage == IN_FORTRESS) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Sir Amik Varze, on the third floor in the", 8147);
			player.getActionSender().sendString("@str@" + "western tower of the White Knight's Castle.", 8148);
			player.getActionSender().sendString("@str@" + "Sir Amik told me I need to sabotage the Black Knights.", 8150);
			player.getActionSender().sendString("@str@" + "He gave me a dossier on the mission.", 8151);
			player.getActionSender().sendString("@str@" + "Getting into the fortress unnoticed is a good start.", 8152);

			player.getActionSender().sendString("I made it into the Black Knight's fortress with a disguise.", 8154);
			player.getActionSender().sendString("I need to do some reconnaissance in order", 8155);
			player.getActionSender().sendString("to learn how to sabotage their  secret weapon.", 8156);
		} else if (questStage == CABBAGE_TIME) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Sir Amik Varze, on the third floor in the", 8147);
			player.getActionSender().sendString("@str@" + "western tower of the White Knight's Castle.", 8148);
			player.getActionSender().sendString("@str@" + "Sir Amik told me I need to sabotage the Black Knights.", 8150);
			player.getActionSender().sendString("@str@" + "He gave me a dossier on the mission.", 8151);
			player.getActionSender().sendString("@str@" + "Getting into the fortress unnoticed is a good start.", 8152);
			player.getActionSender().sendString("@str@" + "I made it into the Black Knight's fortress with a disguise.", 8154);
			player.getActionSender().sendString("@str@" + "I need to do some reconnaissance in order", 8155);
			player.getActionSender().sendString("@str@" + "to learn how to sabotage their  secret weapon.", 8156);

			player.getActionSender().sendString("It seems a cabbage -not- from Draynor Manor will ruin", 8158);
			player.getActionSender().sendString("the potion. There must be a place to drop it secretly.", 8159);
		} else if (questStage == BACK_TO_AMIK) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Sir Amik Varze, on the third floor in the", 8147);
			player.getActionSender().sendString("@str@" + "western tower of the White Knight's Castle.", 8148);
			player.getActionSender().sendString("@str@" + "Sir Amik told me I need to sabotage the Black Knights.", 8150);
			player.getActionSender().sendString("@str@" + "He gave me a dossier on the mission.", 8151);
			player.getActionSender().sendString("@str@" + "Getting into the fortress unnoticed is a good start.", 8152);
			player.getActionSender().sendString("@str@" + "I made it into the Black Knight's fortress with a disguise.", 8154);
			player.getActionSender().sendString("@str@" + "I need to do some reconnaissance in order", 8155);
			player.getActionSender().sendString("@str@" + "to learn how to sabotage their  secret weapon.", 8156);
			player.getActionSender().sendString("@str@" + "It seems a cabbage -not- from Draynor Manor will ruin", 8158);
			player.getActionSender().sendString("@str@" + "the potion. There must be a place to drop it secretly.", 8159);

			player.getActionSender().sendString("I should return to Sir Amik Varze with the good news.", 8161);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Sir Amik Varze, on the third floor in the", 8147);
			player.getActionSender().sendString("@str@" + "western tower of the White Knight's Castle.", 8148);
			player.getActionSender().sendString("@str@" + "Sir Amik told me I need to sabotage the Black Knights.", 8150);
			player.getActionSender().sendString("@str@" + "He gave me a dossier on the mission.", 8151);
			player.getActionSender().sendString("@str@" + "Getting into the fortress unnoticed is a good start.", 8152);
			player.getActionSender().sendString("@str@" + "I made it into the Black Knight's fortress with a disguise.", 8154);
			player.getActionSender().sendString("@str@" + "I need to do some reconnaissance in order", 8155);
			player.getActionSender().sendString("@str@" + "to learn how to sabotage their  secret weapon.", 8156);
			player.getActionSender().sendString("@str@" + "It seems a cabbage -not- from Draynor Manor will ruin", 8158);
			player.getActionSender().sendString("@str@" + "the potion. There must be a place to drop it secretly.", 8159);
			player.getActionSender().sendString("@str@" + "I should return to Sir Amik Varze with the good news.", 8161);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8163);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Talk to Sir Amik Varze, on the third floor in the", 8147);
			player.getActionSender().sendString("western tower of the White Knight's Castle.", 8148);
			player.getActionSender().sendString("Requirements:", 8150);
			if (player.getQuestPoints() < 12) {
				player.getActionSender().sendString("@dre@-12 Quest Points", 8151);
			} else {
				player.getActionSender().sendString("@str@-12 Quest Points", 8151);
			}
			player.getActionSender().sendString("@dre@-Ability to evade level 33 black knights.", 8152);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7332);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7332);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7332);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7332);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to Sir Amik Varze, on the third floor in the", 8147);
		player.getActionSender().sendString("western tower of the White Knight's Castle.", 8148);
		player.getActionSender().sendString("Requirements:", 8150);
		if (player.getQuestPoints() < 12) {
			player.getActionSender().sendString("@dre@-12 Quest Points", 8151);
		} else {
			player.getActionSender().sendString("@str@-12 Quest Points", 8151);
		}
		player.getActionSender().sendString("@dre@-Ability to evade level 33 black knights.", 8152);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static boolean guardOutfit(final Player player) {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) != null
			&& player.getEquipment().getItemContainer().get(Constants.CHEST) != null) {
			return player.getEquipment().getId(Constants.HAT) == BRONZE_MED
				&& player.getEquipment().getId(Constants.CHEST) == IRON_CHAINBODY;
		}
		return false;
	}

	public static boolean attackPlayer(final Player player) {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 178 && Misc.goodDistance(npc.getPosition().clone(), player.getPosition().clone(), 2)
				&& !guardOutfit(player) && player.getPosition().getY() > 3510 && npc.getCombatDelayTick().completed()
				&& player.getCombatingEntity() != npc) {
				//CombatManager.attack(npc, player);
				return true;
			}
		}
		return false;
	}

	public static void attackPlayer(final Player player, final boolean attack) {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 178 && attack && player != null) {
				CombatManager.attack(npc, player);
			}
		}
	}

	public boolean itemHandling(Player player, int itemId) {
		if (itemId == DOSSIER) {
			Dialogues.startDialogue(player, 10701);
			return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(Player player, int object, int item) {
		switch (object) {
			case 2336: //cabbage hole
				if (item == CABBAGE && player.getQuestStage(18) == 3) {
					player.getUpdateFlags().sendAnimation(827);
					player.getInventory().removeItem(new Item(CABBAGE));
					player.getDialogue().sendStatement("You drop your cabbage down the hole, into the cauldron.", "You hear shrieks and items being knocked over.", "The potion seems to be ruined.");
					player.setQuestStage(18, 4);
					return true;
				} else if (item == BAD_CABBAGE && player.getQuestStage(18) == 3) {
					player.getDialogue().sendPlayerChat("I probably shouldn't drop the cabbage that", "would complete the invincibility potion.", CONTENT);
					return true;
				} else {
					return false;
				}
		}
		return false;
	}

	public boolean doObjectClicking(Player player, int object, int x, int y) {
		switch (object) {
			case 2337:
				if (player.getPosition().getY() < 3515) {
					if (!guardOutfit(player)) {
						for (Npc guard : World.getNpcs()) {
							if (guard == null) {
								continue;
							}
							if (guard.getNpcId() == 609 && Misc.goodDistance(player.getPosition().clone(), guard.getPosition().clone(), 4)) {
								CombatManager.attack(guard, player);
								guard.getUpdateFlags().sendForceMessage("Intruder!");
							}
						}
						return true;
					} else if (guardOutfit(player)) {
						Dialogues.startDialogue(player, 610);
						return true;
					}
				} else {
					player.getActionSender().walkTo(0, -1, true);
					player.getActionSender().walkThroughDoor(2337, 3016, 3514, 0);
					return true;
				}
			case 2341: //push wall
				if (x == 3030 && y == 3510) {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3510 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(2341, x, y, 1);
					return true;
				} else if (x == 3016 && y == 3517) {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3517 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(2341, x, y, 0);
					return true;
				} else {
					return false;
				}
			case 2342: //grill to listen to
				if (player.getQuestStage(18) == 2) {
					Dialogues.startDialogue(player, 611);
					return true;
				} else {
					player.getDialogue().sendPlayerChat("I should get to work on sabotaging...", CONTENT);
					return true;
				}
			case 2338: //door to banquet hall or whatever
				if (player.getQuestStage(18) == 3 && x == 3020 && y == 3515) {
					player.getActionSender().walkTo(player.getPosition().getX() < 3020 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(2338, x, y, 0);
					return true;
				} else if (player.getQuestStage(18) < 3 && x == 3020 && y == 3515) {
					player.getDialogue().sendPlayerChat("I probably shouldn't go in there.", "There's too many knights, especially since I don't", "know quite what I'm going to do yet.", CONTENT);
					return true;
				} else {
					return false;
				}
			case 73:
			case 74:
				if (player.getPosition().getX() < 3008 && (y == 3516 || y == 3515)) {
					if (!guardOutfit(player)) {
						for (Npc guard : World.getNpcs()) {
							if (guard == null) {
								continue;
							}
							if (guard.getNpcId() == 609 && Misc.goodDistance(player.getPosition().clone(), guard.getPosition().clone(), 4)) {
								CombatManager.attack(guard, player);
								guard.getUpdateFlags().sendForceMessage("Intruder!");
							}
						}
						return true;
					} else if (guardOutfit(player)) {
						Dialogues.startDialogue(player, 610);
						return true;
					}
				} else if (player.getPosition().getX() > 3007 && (y == 3516 || y == 3515)) {
					player.getActionSender().walkTo(-1, 0, true);
					player.getActionSender().walkThroughDoubleDoor(73, 74, 3007, 3516, 3007, 3515, 0);
					return true;
				} else {
					return false;
				}
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
			case 612: //greldo
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Yeth, mithreth.", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("Hmm....", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 611: //witch
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("The invinciblity potion is almost ready Greldo!", LAUGHING);
						return true;
					case 2:
						player.getDialogue().sendNpcChat("It's taken me FIVE YEARS... but it's almost ready.", HAPPY);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("I just need you to fetch the last ingredient Greldo.", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("It's a special cabbage grown by my cousin, Helda.", "She lives in Draynor Manor.", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("The soil there is slightly magical and it gives", "the cabbages magical properties...", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendNpcChat("Now, remember, Greldo, only a Draynor Manor cabbage", "will do! Don't get lazy and bring any old", "cabbage, that would ENTIRELY wreck the potion!", ANNOYED);
						if (player.getQuestStage(18) == 2) {
							player.setQuestStage(18, 3);
						}
						return true;
					case 7:
						Dialogues.startDialogue(player, 612);
						return true;
				}
				return false;
			case 10701: //dossier
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("There's a lot of information here, hmm...", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("It seems the Black Knights use fortress guards.", "Their outfit according to this is a bronze med helm", "and an iron chainbody.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("I should be able to get into the fortress", "disguised as one of their guards.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 610: //entrance to black knight castle
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Halt! Who goes there?", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("It's just me, a lowly member of your guard, sir.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Ah, alright. Come inside, quickly.", CONTENT);
						if (player.getPosition().getY() < 3515) {
							player.getActionSender().walkTo(0, 1, true);
							player.getActionSender().walkThroughDoor(2337, 3016, 3514, 0);
						} else {
							player.getActionSender().walkTo(1, 0, true);
							player.getActionSender().walkThroughDoubleDoor(73, 74, 3007, 3516, 3007, 3515, 0);
						}
						if (player.getQuestStage(18) == 1) {
							player.setQuestStage(18, 2);
						}
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 608: //sir amik
				switch (player.getQuestStage(18)) {
					case 0: //start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("I am the leader of the White Knights of Falador.", "Why do you seek my audience?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendOption("I seek a quest!", "I don't, I'm just looking around.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I seek a quest!", HAPPY);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("I dont, I'm just looking around.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("Well, I need some work done, but it's quite dangerous.", "It will involve going into the Black Knight's fortress.", CONTENT);
								return true;
							case 5:
								if (player.getQuestPoints() >= 12) {
									player.getDialogue().sendOption("I laugh in the face of danger!", "I go and cower in a corner at the first sign of danger.");
									return true;
								} else {
									player.getDialogue().sendNpcChat("And I'm afraid you don't have the quest", "experience needed for a job this dangerous.", "Come back when you have 12 quest points.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 6:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I laugh in the face of danger!", LAUGHING);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("I go and cower in a corner", "at the first sign of danger...", SAD);
										player.getDialogue().endDialogue();
										return true;
								}
							case 7:
								player.getDialogue().sendNpcChat("That's good. Don't get too overconfident though.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("You've come at the right time actually.", "All of my knights are already known to the Black Knights.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Sublety isn't exactly our strong point.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("Can't you just take the White Knights' armor off?", "They wouldn't recognize you then.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("I'm afraid our charter prevents us from", "using espionage in any form,", "that is the domain of the Temple Knights.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("Temple Knights? Who are they?", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("That information is classified, I am forbidden", "from sharing it with outsiders.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("So... what do you need doing?", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Well, the Black Knights have started making strange", "threats to us; demaning large amounts of money and land,", "and threatening to invade Falador if we don't pay them.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("Now, NORMALLY this wouldn't be a problem...", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("But they claim to have a powerful new secret weapon.", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendNpcChat("Your mission, should you choose to accept it, is to:", "infiltrate their fortress, find out what their secret", "weapon is, and then sabotage it.", CONTENT);
								return true;
							case 19:
								player.getDialogue().sendOption("Okay, I'll do my best.", "No, I'm not ready to do that.");
								return true;
							case 20:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Okay, I'll do my best!", HAPPY);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("No, I'm not ready to do that.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 21:
								player.getDialogue().sendNpcChat("Good luck! Let me know how you get on.", "Here's the dossier for the case, I've already given you", "most of the details.", CONTENT);
								return true;
							case 22:
								if (player.getInventory().canAddItem(new Item(DOSSIER))) {
									player.getDialogue().sendGiveItemNpc("Sir Amik Varze hands you a dossier on the mission.", new Item(DOSSIER));
									player.getInventory().addItem(new Item(DOSSIER));
									if (player.getQuestStage(18) == 0) {
										player.setQuestStage(18, 1);
										QuestHandler.getQuests()[18].startQuest(player);
									}
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Erm, I'm afraid I don't have room", "in my inventory for the dossier.", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
						}
					case 1:
					case 2:
					case 3: //durr finish quest first pls
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Well, have you foiled the Black Knights' plans yet?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Return to me when you have.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 4: //returning to end quest
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I have ruined the Black Knights' invincibility potion.", "That should put a stop to your problem,", "and an end to their little schemes.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Yes, we have just received a message from them", "stating they withdraw their demands, which would", "confirm your story.", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Please accept this reward.", HAPPY);
								return true;
							case 4:
								player.setQuestStage(18, 5);
								QuestHandler.completeQuest(player, 18);
								player.getDialogue().dontCloseInterface();
								return true;
						}
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
