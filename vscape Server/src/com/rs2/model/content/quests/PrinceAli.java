package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISORIENTED_RIGHT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.NEAR_TEARS_2;
import static com.rs2.model.content.dialogue.Dialogues.PLAIN_EVIL;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.dialogue.Dialogues.startDialogue;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class PrinceAli implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int FIND_LEELA = 2;
	public static final int NEDS_WIG = 3;
	public static final int AGGIE = 4;
	public static final int AGGIE_NEEDS_SUPPLIES = 5;
	public static final int LADY_KELI_IMPRINT = 6;
	public static final int IMPRINT_TO_OSMAN = 7;
	public static final int GET_JOE_DRUNK = 8;
	public static final int TIPSY_JOE = 9;
	public static final int FREE_ALI = 10;
	public static final int ALI_NEEDS_DISGUISE = 11;
	public static final int RETURN_TO_HASSAN = 12;
	public static final int QUEST_COMPLETE = 13;

	public int dialogueStage = 0;
	private int reward[][] = {
		{995, 700}, //itemID, amount
	};
	private int expReward[][] = {};

	private static final int questPointReward = 3;

	public int getQuestID() {
		return 9;
	}

	public String getQuestName() {
		return "Prince Ali Rescue";
	}

	public String getQuestSaveName() {
		return "prince-ali";
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
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString((int) (reward[0][1]) + " coins", 12151);
		player.getActionSender().sendString("Free Passage to Al-Kharid", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7342);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);

			player.getActionSender().sendString("Hassan has told you to talk to Osman.", 8150);
		} else if (questStage == FIND_LEELA) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);

			player.getActionSender().sendString("Osman needs you to find Leela near Draynor.", 8151);

		} else if (questStage == NEDS_WIG) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
			player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);

			player.getActionSender().sendString("You have a feeling Ned the wool man can make you a wig.", 8152);

		} else if (questStage == AGGIE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
			player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
			player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);

			player.getActionSender().sendString("Speak with Aggie for skin paste and dye for the wig.", 8153);

		} else if (questStage == LADY_KELI_IMPRINT) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
			player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
			player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
			player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);

			player.getActionSender().sendString("You now need to get a key imprint from Lady Keli near", 8154);
			player.getActionSender().sendString("Draynor's prison.", 8155);

		} else if (questStage == IMPRINT_TO_OSMAN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
			player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
			player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
			player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
			player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
			player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);

			player.getActionSender().sendString("Take the key imprint to Osman.", 8156);
		} else if (questStage == GET_JOE_DRUNK) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
			player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
			player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
			player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
			player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
			player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
			player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);

			player.getActionSender().sendString("Find a way around Joe, the guard, to rescue Prince Ali.", 8157);
		} else if (questStage == FREE_ALI) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
			player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
			player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
			player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
			player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
			player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
			player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);
			player.getActionSender().sendString("@str@" + "Find a way around Joe, the guard, to rescue Prince Ali.", 8156);

			player.getActionSender().sendString("Free Prince Ali by dealing with Lady Keli.", 8158);
		} else if (questStage == ALI_NEEDS_DISGUISE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
			player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
			player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
			player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
			player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
			player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
			player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);
			player.getActionSender().sendString("@str@" + "Find a way around Joe, the guard, to rescue Prince Ali.", 8156);

			player.getActionSender().sendString("Make sure you have a blonde wig (yellow dye), the skirt,", 8158);
			player.getActionSender().sendString("the skin paste and the cell door key.", 8159);
		} else if (questStage == RETURN_TO_HASSAN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
			player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
			player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
			player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
			player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
			player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
			player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);
			player.getActionSender().sendString("@str@" + "Find a way around Joe, the guard, to rescue Prince Ali.", 8156);
			player.getActionSender().sendString("@str@" + "Free Prince Ali by dealing with Lady Keli.", 8157);

			player.getActionSender().sendString("Return to Hassan.", 8159);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
			player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
			player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
			player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
			player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
			player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
			player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
			player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);
			player.getActionSender().sendString("@str@" + "Get Joe the cell guard drunk to rescue Prince Ali.", 8156);
			player.getActionSender().sendString("@str@" + "Free Prince Ali by dealing with Lady Keli.", 8157);
			player.getActionSender().sendString("@str@" + "Return to Hassan.", 8158);

			player.getActionSender().sendString("@red@You have completed this quest!", 8160);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(prefix + "To start the quest, you should talk to Hassan", 8147);
			player.getActionSender().sendString(prefix + "found in the Al-Kharid palace.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7342);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7342);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7342);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7342);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "To start the quest, you should talk to Hassan", 8147);
		player.getActionSender().sendString(prefix + "found in the Al-Kharid palace.", 8148);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public boolean itemHandling(final Player player, int itemId) {
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
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
			case 923: //hassanawannarescueali
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(9) >= 1 && player.getQuestStage(9) != 12) {
							player.getDialogue().sendOption("It's just too hot here, how can you stand it?", "Do you mind if I just kill your warriors?");
							player.getDialogue().setNextChatId(8);
							return true;
						} else if (player.getQuestStage(9) == 12) {
							player.getDialogue().sendNpcChat("Oh! My son is rescued! Thank you so very much!", HAPPY);
							player.getDialogue().setNextChatId(50);
							return true;
						} else {
							player.getDialogue().sendNpcChat("Greetings! I am Hassan, Chancellor to", "the Emir of Al-Kharid.", CONTENT);
							return true;
						}
					case 2:
						player.getDialogue().sendOption("Can I help you? You must need help here in this desert.", "It's just too hot here, how can you stand it?", "Do you mind if I just kill your warriors?");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Can I help you? You must need help here in this desert.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("It's just too hot here, how can you stand it?", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Do you mind if I just kill your warriors?", PLAIN_EVIL);
								player.getDialogue().setNextChatId(6);
								return true;
						}
					case 4:
						player.getDialogue().sendNpcChat("I need the services of someone, yes.", "If you are interested, see the spymaster, Osman.", CONTENT);
						player.setQuestStage(9, 1);
						QuestHandler.getQuests()[9].startQuest(player);
						player.getDialogue().endDialogue();
						return true;
					case 5:
						player.getDialogue().sendNpcChat("I manage the finances here.", "There's nothing you can't stand when money is involved!", LAUGHING);
						player.getDialogue().endDialogue();
						return true;
					case 6:
						if (player.getQuestStage(9) != 13) {
							player.getDialogue().endDialogue();
							for (Npc npc : World.getNpcs()) {
								if (npc == null) {
									continue;
								} else if (npc.getNpcId() == player.getDialogue().getLastNpcTalk()) {
									npc.getUpdateFlags().sendAnimation(810);
								}
							}
							player.getUpdateFlags().sendGraphic(254, 100 << 16);
							player.hit(1, HitType.NORMAL);
						} else if (player.getQuestStage(9) == 13) {
							player.getDialogue().sendNpcChat("I suppose I am in your debt.", "I was lost without poor Ali.", SAD);
							player.getDialogue().endDialogue();
							return true;
						}
						break;
					case 8:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("It's just too hot here, how can you stand it?", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Do you mind if I just kill your warriors?", PLAIN_EVIL);
								player.getDialogue().setNextChatId(6);
								return true;
						}
					case 50:
						player.getDialogue().endDialogue();
						player.setQuestStage(9, 13);
						QuestHandler.completeQuest(player, 9);
						return true;
				}
				break;
			case 924: //osman
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(9) == 1) {
							player.getDialogue().sendPlayerChat("The Chancellor trusts me.", "I have come here for instructions.", CONTENT);
							return true;
						} else if (player.getQuestStage(9) == 7) {
							player.getDialogue().sendNpcChat("Well done, we can make the key now.", CONTENT);
							player.getDialogue().setNextChatId(30);
							return true;
						} else if (player.getQuestStage(9) >= 2 && player.getQuestStage(9) != 7 && player.getQuestStage(9) < 11) {
							player.getDialogue().sendPlayerChat("What items do I need for the rescue again?", CONTENT);
							player.getDialogue().setNextChatId(40);
							return true;
						} else {
							player.getDialogue().sendStatement("Osman seems to not want to be disturbed.");
							player.getDialogue().endDialogue();
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("Our Prince is held captive by the Lady Keli.", "We just need to make the rescue.", "There are two things we need you to do.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("What is the first thing I must do?", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("The prince is guarded by some stupid guards...", "and a clever woman.", "The woman is our only way to get the prince out.", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("I think you will need to tie her up.", "One coil of rope should do for that.", "Then disguise the prince as her to sneak out.", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendPlayerChat("How good must the disguise be?", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendNpcChat("Only good enough to fool the guards at a distance.", "Get a skirt like hers. Same color, same style.", "We will only have a short time.", CONTENT);
						return true;
					case 8:
						player.getDialogue().sendNpcChat("Get a blonde wig and something to color the prince's skin.", "Those are up to you to make or find.", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("My daughter and top spy, Leela, can help you.", "She has sent word she knows where the Prince is held.", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendNpcChat("The location is near Draynor Village.", "She is lurking there now.", CONTENT);
						return true;
					case 11:
						player.getDialogue().sendOption("Explain the first thing again.", "And the second thing?");
						return true;
					case 12:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Explain the first thing again.", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("And the second thing?", CONTENT);
								return true;
						}
					case 13:
						player.getDialogue().sendNpcChat("We need the key, or we need a copy made.", "If you get some soft clay, then you can copy the key...", CONTENT);
						return true;
					case 14:
						player.getDialogue().sendNpcChat("...If you can convince Lady Keli to show it to you.", "She is very boastful. It should not be difficult.", CONTENT);
						return true;
					case 15:
						player.getDialogue().sendNpcChat("Then bring the imprint to me, along with a bronze bar.", CONTENT);
						return true;
					case 16:
						player.getDialogue().sendOption("Can you explain the first thing again?", "Can you explain the second thing again?", "I better go find some things.");
						return true;
					case 17:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Can you explain the first thing again?", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Can you explain the second thing again?", CONTENT);
								player.getDialogue().setNextChatId(14);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I better go find some things.", CONTENT);
								return true;
						}
					case 18:
						player.getDialogue().sendNpcChat("May good luck travel with you.", "Don't forget to find Leela...", "It can't be done without her help.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(9, 2);
						return true;
					case 30:
						if (player.getInventory().playerHasItem(2349)) {
							player.getDialogue().sendStatement("Osman takes the imprint and the bronze bar.");
							player.getInventory().removeItem(new Item(2423));
							player.getInventory().removeItem(new Item(2349));
							return true;
						} else {
							player.getDialogue().sendNpcChat("You seem to have forgotten a bronze bar.", "I need one to fabricate the key.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
					case 31:
						player.getDialogue().sendNpcChat("A quality imprint, good job.", CONTENT);
						return true;
					case 32:
						player.getDialogue().sendStatement("After some strange motions Osman hands you a key.");
						player.getInventory().addItem(new Item(2418));
						return true;
					case 33:
						player.getDialogue().sendPlayerChat("Thank you, I'll do my best to rescue the prince.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(9, 8);
						return true;
					case 40:
						player.getDialogue().sendNpcChat("You'll need a wig dyed yellow,", " a rope to tie up Lady Keli,", "some skin paste,", "and a long pink skirt like Lady Keli's.", CONTENT);
						return true;
					case 41:
						player.getDialogue().sendOption("A pink skirt?", "Thank you.");
						return true;
					case 42:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("A pink skirt?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Thank you.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
					case 43:
						player.getDialogue().sendNpcChat("Yes, I believe the clothes store in Varrock", "has those skirts in stock.", "However, do not choose the dwarven variant of skirt.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 915: // leela
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(9) == 2) {
							player.getDialogue().sendPlayerChat("Your father has sent me, in regards to Prince Ali.", CONTENT);
							player.getDialogue().setNextChatId(3);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("What are you doing alone in the woods?", CONTENT);
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("Shh! Leave me alone!", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Ah yes, the Prince.", "He is being held captive in the prison south of me.", ANNOYED);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("I need to be able to free him with a disguise.", "Can you help me with that?", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("...grumble...grumble...Ned...grumble...", ANNOYED);
						return true;
					case 6:
						player.getDialogue().sendStatement("Leela seems to have lost interest in you.", "However, you heard the name Ned upon asking for help with a disguise.");
						player.getDialogue().endDialogue();
						player.setQuestStage(9, 3);
						return true;
				}
				break;
			case 916: //joe the guardsman
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(9) == 8) {
							player.getDialogue().sendNpcChat("Ugh, my head is killing me...", "I shouldn't have had that Karamja Rum last night...", "Wait, who are you?", CONTENT);
							return true;
						} else if (player.getQuestStage(9) == 9) {
							player.getDialogue().sendNpcChat("Say, you wouldn't happen to have any", "more of that beer would you?", HAPPY);
							player.getDialogue().setNextChatId(9);
							return true;
						} else {
							player.getDialogue().sendNpcChat("I'm watching you.", ANNOYED);
							player.getDialogue().endDialogue();
							return true;
						}
					case 2:
						player.getDialogue().sendPlayerChat("You know what's best for a hangover?", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("What's that?", CONTENT);
						return true;
					case 4:
						if (player.getInventory().playerHasItem(1917)) {
							player.getDialogue().sendPlayerChat("Hair of the dog that bit you!", "Here, have a beer!", LAUGHING);
							player.getInventory().removeItem(new Item(1917));
							player.setQuestStage(9, 9);
							player.getDialogue().setNextChatId(7);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("Err, ... water?", SAD);
							return true;
						}
					case 5:
						player.getDialogue().sendNpcChat("I suppose you're right, I can try that.", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendStatement("Joe looks like quite the alcoholic, and quite hungover.", "Some beer might push him over the edge.");
						player.getDialogue().endDialogue();
						return true;
					case 7:
						player.getDialogue().sendNpcChat("Not quite like that Karamja Rum,", "But it certainly takes the edge off!", LAUGHING);
						return true;
					case 8:
						player.getDialogue().sendStatement("Joe finishes the beer in one gulp.", "Two more beers might do him in.");
						return true;
					case 9:
						if (player.getInventory().playerHasItem(1917, 2)) {
							player.getDialogue().sendPlayerChat("Here, I've got two more! My treat!", HAPPY);
							player.getInventory().removeItem(new Item(1917, 2));
							player.getDialogue().setNextChatId(12);
							return true;
						} else {
							player.getDialogue().sendNpcChat("I could really go for...", "...2 more beers!", "Have you got that friend?", HAPPY);
							return true;
						}
					case 10:
						player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
						return true;
					case 11:
						player.getDialogue().sendNpcChat("A shame, I'll probably sober up sometime soon.", SAD);
						player.getDialogue().endDialogue();
						return true;
					case 12:
						player.getDialogue().sendNpcChat("Ah hah! My best friend! Cheers mate!", LAUGHING);
						return true;
					case 13:
						player.getDialogue().sendStatement("Joe quickly finishes off his remaining beers.");
						return true;
					case 14:
						player.getDialogue().sendNpcChat("*Hic* Th-thanks mate. Really hits the spot.", HAPPY);
						return true;
					case 15:
						player.getDialogue().sendNpcChat("Since we're friends, I'll let you in...", "*Hic*...on a little secret!", HAPPY);
						return true;
					case 16:
						player.getDialogue().sendStatement("Joe leans in close, and begins to whisper...");
						return true;
					case 17:
						player.getDialogue().sendNpcChat("Karamja Rum tastes... *Hic*", "...best in the Tzhaar Caves!", LAUGHING);
						return true;
					case 18:
						player.getDialogue().sendStatement("Joe lets out a bolsterous laugh and seems disoriented.");
						return true;
					case 19:
						player.getDialogue().sendNpcChat("*Hic* W-woah, that'sh a sh'trong brew...", "...", "...", DISORIENTED_RIGHT);
						return true;
					case 20:
						player.getDialogue().sendStatement("Joe dies unexpectedly.");
						player.getDialogue().endDialogue();
						for (Npc npc : World.getNpcs()) {
							if (npc == null) {
								continue;
							} else if (npc.getNpcId() == player.getDialogue().getLastNpcTalk()) {
								npc.setDead(true);
								CombatManager.startDeath(npc);
							}
						}
						player.setQuestStage(9, 10);
						return true;
				}
			case 920: //prince ali
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(9) < 11 && player.getQuestStage(9) != 10) {
							player.getDialogue().sendNpcChat("H-help me...", SAD);
							player.getDialogue().endDialogue();
							return true;
						} else if (player.getQuestStage(9) == 10) {
							player.getDialogue().sendNpcChat("H-help me...", SAD);
							return true;
						} else if (player.getQuestStage(9) == 11) {
							player.getDialogue().sendNpcChat("Can we get out safely now?", CONTENT);
							player.getDialogue().setNextChatId(8);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("Prince! I have come to rescue you!", HAPPY);
							player.getDialogue().setNextChatId(3);
							return true;
						}
					case 2:
						player.getDialogue().sendStatement("Lady Keli is still around, but Joe is gone...", "...you could tie her up with some rope.");
						player.getDialogue().endDialogue();
						return true;
					case 3:
						player.getDialogue().sendNpcChat("How very kind!", "How do you propose I get out of here?", ANNOYED);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("I've murdered Lady Keli and all her guards.", HAPPY);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("What!? I cannot condone this behavior!", ANGRY_2);
						return true;
					case 6:
						player.getDialogue().sendPlayerChat("I'm kidding. Lady Keli is tied up.", "However, her main guard Joe died unexpectedly.", "I guess he couldn't hold his liquor.", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendNpcChat("Oh... But what about the guards outside?", "If they see me leaving, we'll both die!", CONTENT);
						return true;
					case 8:
						if (!player.getInventory().playerHasItem(2418)) {
							player.getDialogue().sendPlayerChat("Well, I don't even have the key to your cell", "on me, so... nevermind.", SAD);
							player.getDialogue().endDialogue();
							return true;
						} else if (player.getInventory().playerHasItem(2419) && player.getInventory().playerHasItem(1013) && player.getInventory().playerHasItem(2424) && player.getInventory().playerHasItem(2418)) {
							player.getDialogue().sendPlayerChat("That's what this disguise is for!", HAPPY);
							return true;
						} else if (player.getInventory().playerHasItem(2419) && player.getInventory().playerHasItem(5050) && player.getInventory().playerHasItem(2424) && player.getInventory().playerHasItem(2418)) {
							player.getDialogue().sendPlayerChat("This skirt seems a bit short for you.", "I may have bought the wrong one.", SAD);
							player.setQuestStage(9, 11);
							player.getDialogue().endDialogue();
							return true;
						} else {
							player.getDialogue().sendStatement("Prince Ali needs a disguise to escape!");
							player.setQuestStage(9, 11);
							player.getDialogue().endDialogue();
							return true;
						}
					case 9:
						player.getDialogue().sendStatement("You hand Prince Ali the wig, skin paste and skirt.", "You unlock Prince Ali's cell door as well.");
						for (Npc npc : World.getNpcs()) {
							if (npc == null) {
								continue;
							} else if (npc.getNpcId() == 920) {
								player.setQuestStage(9, 12);
								player.getInventory().removeItem(new Item(2419));
								player.getInventory().removeItem(new Item(2424));
								player.getInventory().removeItem(new Item(1013));
								player.getInventory().removeItem(new Item(2418));
								npc.setDead(true);
								CombatManager.startDeath(npc);
								player.getPets().unregisterPet();
								player.getPets().registerPet(-1, 921);
							}
						}
						return true;
					case 10:
						player.getDialogue().endDialogue();
						startDialogue(player, 921);
						return true;
				}
				break;
			case 921: //girly prince ali
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("This feels... Almost right.", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("What?", DISTRESSED);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Nothing, I'm going to return to my father.", CONTENT);
						player.getPets().getPet().getUpdateFlags().sendAnimation(714);
						player.getPets().getPet().getUpdateFlags().sendHighGraphic(301);
						return true;
					case 4:
						player.getDialogue().sendStatement("You are now a friend of Al-Kharid.");
						player.getDialogue().endDialogue();
						player.getPets().unregisterPet();
						return true;
				}
				break;
			case 918: //get fucked ned
				switch (player.getQuestStage(9)) {
					case 3: //ned makes wig
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10: //just in case
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello there!", CONTENT);
								return true;
							case 2:
								if (player.getEquipment().getItemContainer().get(Constants.HAT) != null && (player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("boater")
									|| player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("cavalier"))) {
									player.getDialogue().sendNpcChat("Nice hat, sir. I make wool hats sometimes!", "How can I help you?", HAPPY);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Why hello! I'm the wool man. I make wool things.", "How can I help you today?", HAPPY);
									return true;
								}
							case 3:
								player.getDialogue().sendOption("I need a wig made.", "I need some rope.", "You can't!");
								return true;
							case 4:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I need a wig made.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Actually, I need some rope.", CONTENT);
										player.getDialogue().setNextChatId(15);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("You can't!", ANGRY_2);
										player.getDialogue().endDialogue();
										return true;
								}
							case 5:
								player.getDialogue().sendNpcChat("I normally make ropes and sweaters...", "But I suppose a wig wouldn't be too tough.", HAPPY);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("What do you need it for?", "If you don't mind me asking?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("I'm trying to stop having to talk", "to the guards outside Al-Kharid.", SAD);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Enough said, bring me 3 balls of wool.", CONTENT);
								return true;
							case 9:
								if (player.getInventory().playerHasItem(1759, 3)) {
									player.getInventory().removeItem(new Item(1759, 3));
									player.getDialogue().sendStatement("You hand Ned 3 balls of wool...", "Ned works with the wool...", "His hands move with a speed you can't comprehend.");
									return true;
								} else {
									player.getDialogue().sendPlayerChat("It seems I don't have the wool.", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 10:
								player.getDialogue().sendNpcChat("Here you go! How's that for quick work?", HAPPY);
								return true;
							case 11:
								player.getDialogue().sendStatement("Ned hands you a pretty good wig.", "However, it is plain and undyed.");
								player.getInventory().addItem(new Item(2421));
								if (player.getQuestStage(9) == 3) {
									player.setQuestStage(9, 4);
								}
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("Thanks Ned, there's more to you than there seems.", HAPPY);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("I may be able to make pretty hair...", "But I'll never be a little girl...", NEAR_TEARS_2);
								player.getDialogue().endDialogue();
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Some rope eh? I can do that!", "Either pay me, or hand over 4 balls of wool!", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendOption("Pay Ned (18 gp)", "Give wool.", "Nevermind.");
								return true;
							case 17:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(995, 18)) {
											player.getDialogue().sendPlayerChat("Here you are!", CONTENT);
											player.getInventory().removeItem(new Item(995, 18));
											return true;
										} else {
											player.getDialogue().sendPlayerChat("I'm afraid I don't have the coin...", SAD);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										if (player.getInventory().playerHasItem(1759, 4)) {
											player.getDialogue().sendPlayerChat("Here you are!", CONTENT);
											player.getDialogue().setNextChatId(8);
											player.getInventory().removeItem(new Item(1759, 4));
											return true;
										} else {
											player.getDialogue().sendPlayerChat("I'm afraid I don't have the balls...", SAD);
											player.getDialogue().endDialogue();
											return true;
										}
									case 3:
										player.getDialogue().sendPlayerChat("Nevermind.", SAD);
										player.getDialogue().endDialogue();
										return true;
								}
							case 18:
								player.getDialogue().sendNpcChat("And here's yer rope!", CONTENT);
								player.getInventory().addItem(new Item(954));
								player.getDialogue().endDialogue();
								return true;
							case 19:
								player.getDialogue().sendStatement("Ned works with the wool.");
								return true;
							case 20:
								player.getDialogue().sendNpcChat("And here's yer rope!", CONTENT);
								player.getInventory().addItem(new Item(954));
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case 922: //aggie
				switch (player.getQuestStage(9)) {
					case 5:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Did you bring me those supplies?", CONTENT);
								return true;
							case 2:
								if (player.getInventory().playerHasItem(592) && player.getInventory().playerHasItem(1933) && player.getInventory().playerHasItem(1929) && player.getInventory().playerHasItem(1951)) {
									player.getDialogue().sendPlayerChat("I have the supplies you requested.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Did you not listen before!?", "I cannot make the paste without these supplies!", ANGRY_2);
									player.getDialogue().setNextChatId(25);
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("Excellent!", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendStatement("You hand Aggie the supplies for the paste.");
								player.getInventory().removeItem(new Item(592));
								player.getInventory().removeItem(new Item(1933));
								player.getInventory().removeItem(new Item(1929));
								player.getInventory().removeItem(new Item(1951));
								return true;
							case 5:
								player.getDialogue().sendStatement("Aggie begins mixing the ingredients.");
								return true;
							case 6:
								player.getDialogue().sendGiveItemNpc("Aggie hands you the completed paste.", new Item(2424));
								player.getDialogue().endDialogue();
								player.setQuestStage(9, 6);
								player.getInventory().addItem(new Item(2424));
								return true;
							case 25:
								player.getDialogue().sendStatement("Aggie shows you a list of supplies:", "Ash, flour, a bucket of water, and some redberries.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 4:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Ack ack ack! A brew, a brew!", "I shall make! Just for you! A log? A dog?", "Perhaps a fat hog? Ack ack ack!", LAUGHING);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I was thinking of something like a skin paste.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("How droll! How dull!", "A paste for the skin, a paste to blend in!", "Bring me these supplies and they will suffice!", LAUGHING);
								return true;
							case 4:
								player.getDialogue().sendStatement("Aggie shows you a list of supplies:", "Ash, flour, a bucket of water, and redberries for the dwarf.");
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("The dwarf?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("I moonlight as a smith! Ack ack ack!", LAUGHING);
								player.getDialogue().endDialogue();
								player.setQuestStage(9, 5);
								return true;
						}
						return false;
				}
				return false;
			case 919: //lady keli
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("Hello there!", CONTENT);
						return true;
					case 2:
						if (player.getQuestStage(9) == 6) {
							player.getDialogue().sendNpcChat("Is there something I can do for you?", "I'm quite busy right now...", ANNOYED);
							return true;
						} else {
							player.getDialogue().sendNpcChat("Please go away.", ANNOYED);
							player.getDialogue().endDialogue();
							return true;
						}
					case 3:
						player.getDialogue().sendPlayerChat("I was curious as to why you have so many guards.", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("I'm the leader of the most powerful gang in the land!", "We're holding Prince Ali hostage...", "...Until his people decide to pay up!", LAUGHING);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("They don't seem to be concerned about him though...", "It's been weeks since we took him...", ANNOYED);
						return true;
					case 6:
						player.getDialogue().sendPlayerChat("How are you sure he won't break out of his cell?", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendNpcChat("You doubt my genius!", "We've fashioned a lock that only opens to this key!", ANNOYED);
						return true;
					case 8:
						player.getDialogue().sendStatement("Lady Keli hands you the key.", "It's attached to her wrist by a long Runite Chain.");
						return true;
					case 9:
						player.getDialogue().sendPlayerChat("Impressive!", "Is that guard over there hurting an innocent woman?", PLAIN_EVIL);
						return true;
					case 10:
						if (player.getInventory().playerHasItem(1761)) {
							player.getDialogue().sendStatement("Lady Keli looks away worried.", "You quickly make an imprint of the key in the wet clay.");
							player.getInventory().removeItem(new Item(1761));
							player.getInventory().addItem(new Item(2423));
							player.setQuestStage(9, 7);
							return true;
						} else {
							player.getDialogue().sendStatement("Lady Keli looks away worried.", "If only you had some soft clay to make a key imprint.");
							return true;
						}
					case 11:
						player.getDialogue().sendNpcChat("It doesn't appear so, but I like your keen eye.", "Would you like to join the Civil Judicatory Knights?", CONTENT);
						return true;
					case 12:
						player.getDialogue().sendPlayerChat("Maybe some other time.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
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
