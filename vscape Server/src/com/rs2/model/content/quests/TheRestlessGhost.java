package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CALM;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.NEAR_TEARS;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;

public class TheRestlessGhost implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int ITEMS_GIVEN = 2;
	public static final int QUEST_NEARING_COMPLETION = 3;
	public static final int QUEST_COMPLETE = 4;

	public int dialogueStage = 0;
	private int reward[][] = {
		{995, 0}, //itemID, amount
	};
	private int expReward[][] = {
		{Skill.PRAYER, 1125} // skill ID, exp amount
	};
	private static final int questPointReward = 1;

	public int getQuestID() {
		return 2;
	}

	public String getQuestName() {
		return "The Restless Ghost";
	}

	public String getQuestSaveName() {
		return "restless-ghost";
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
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Prayer Experience", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@The Restless Ghost", 7337);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
			player.getActionSender().sendString("@str@" + "Father Aereck at the church in Lumbridge.", 8148);

			player.getActionSender().sendString("Father Aereck told me to talk to Father Urhney who", 8150);
			player.getActionSender().sendString("can be found in his shack inside the swamp.", 8151);
			player.getActionSender().sendString("Apparently he can give me a bit of information", 8152);
			player.getActionSender().sendString("on how to get rid of the ghost.", 8153);
		} else if (questStage == ITEMS_GIVEN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
			player.getActionSender().sendString("@str@" + "Father Aereck at the church in Lumbridge.", 8148);

			player.getActionSender().sendString("@str@" + "Father Aereck told me to talk to Father Urhney who", 8150);
			player.getActionSender().sendString("@str@" + "can be found in his shack inside the swamp.", 8151);
			player.getActionSender().sendString("@str@" + "Apparently he can give me a bit of information", 8152);
			player.getActionSender().sendString("@str@" + "on how to get rid of the ghost.", 8153);

			player.getActionSender().sendString("Father Urhney gave me an Amulet of Ghostspeak.", 8154);
		} else if (questStage == QUEST_NEARING_COMPLETION) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
			player.getActionSender().sendString("@str@" + "Father Aereck at the church in Lumbridge.", 8148);

			player.getActionSender().sendString("@str@" + "Father Aereck told me to talk to Father Urhney who", 8150);
			player.getActionSender().sendString("@str@" + "can be found in his shack inside the swamp.", 8151);
			player.getActionSender().sendString("@str@" + "Apparently he can give me a bit of information", 8152);
			player.getActionSender().sendString("@str@" + "on how to get rid of the ghost.", 8153);

			player.getActionSender().sendString("I talked to the ghost, he asked me to get back his skull.", 8154);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
			player.getActionSender().sendString("@str@" + "Father Aereck at the church in Lumbridge.", 8148);

			player.getActionSender().sendString("@str@" + "Father Aereck told me to talk to Father Urhney who", 8150);
			player.getActionSender().sendString("@str@" + "can be found in his shack inside the swamp.", 8151);
			player.getActionSender().sendString("@str@" + "Apparently he can give me a bit of information", 8152);
			player.getActionSender().sendString("@str@" + "on how to get rid of the ghost.", 8153);

			player.getActionSender().sendString("@str@" + "I've helped the Ghost return to his sleep.", 8154);

			player.getActionSender().sendString("@red@You have completed this quest!", 8155);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(prefix + "To start the quest, you should talk with", 8147);
			player.getActionSender().sendString(prefix + "Father Aereck at the church in Lumbridge.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@The Restless Ghost", 7337);
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
			player.getActionSender().sendString("@yel@The Restless Ghost", 7337);
		} else if (questStage >= QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@The Restless Ghost", 7337);
		} else {
			player.getActionSender().sendString("@red@The Restless Ghost", 7337);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "To start the quest, you should talk with", 8147);
		player.getActionSender().sendString(prefix + "Father Aereck in Lumbridge Church.", 8148);
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
			case 456: //Father Aereck
				switch (player.getQuestStage(2)) {
					case 0: // quest stage 0 start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Yes, adventurer?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendOption("You don't look very happy.", "Sorry, didn't mean to bother you.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("*sigh*", "There appears to be a ghost haunting the Church.", "And the only man capable of exorcising it,", "is meditating in the swamp.", DISTRESSED);
										player.getDialogue().setNextChatId(4);
										return true;
									case 2:
										player.getDialogue().sendNpcChat("It's fine.", NEAR_TEARS);
										player.getDialogue().setNextChatId(5);
										return true;
								}
								return false;
							case 4:
								player.getDialogue().sendNpcChat("It'd be really nice if you could help me out.", DISTRESSED);
								return true;
							case 5:
								player.getDialogue().sendOption("Yeah, alright. I don't see why not.", "Sorry, I have troubles of my own.");
								return true;
							case 6:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Thanks, you can start by talking to Father Urhney,", "his shack is somewhere in the swamp.", "He can give you some information.", HAPPY);
										QuestHandler.getQuests()[2].startQuest(player);
										player.getDialogue().setNextChatId(3);
										return true;
									case 2:
										player.getDialogue().sendNpcChat("Oh, that's fine.", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
								}
								return false;
						}
						return false; // quest stage 0 end
					case 1: // quest stage 1 start
					case 2: // quest stage 2 start
					case 3: // quest stage 3 start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Did you get rid of the ghost yet?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Not yet, but I'm working on it, don't worry.", CALM);
								player.getDialogue().endDialogue();
								return true;
						}
						return false; // quest stage 1 2 3 end
				}
				return false;
			case 458: //Father Urhney
				switch (player.getQuestStage(2)) {
					case 0: // quest stage 0 start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Go away.", DISTRESSED);
								player.getDialogue().endDialogue();
								return true;
						}
						return false; // quest stage 0 end
					case 1: // quest stage 1 start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I've been told you could help", "get rid of the ghost haunting", "the Lumbridge Church?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("What has that fool, Aereck, gotten", "himself into this time?!", ANNOYED);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Well, apparently there is a ghost that", "has been haunting the church for some time.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Gah, I leave for a couple months and he's", "already got himself in a jam! Useless!", ANNOYED);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Here, adventurer, take this amulet.", "It'll allow you to speak to the ghost.", "Find out why it won't move on.", ANNOYED);
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(552, 1));
								player.setQuestStage(2, 2);
								return true;
						}
						return false; // quest stage 1 end
					case 2:// quest stage 2 start
					case 3:// quest stage 3 start
					case 4:// quest stage 4 start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("You didn't lose your amulet, did you?", ANNOYED);
								if ((player.hasItem(552))) {
									player.getDialogue().setNextChatId(4);
								} else {
									player.getDialogue().setNextChatId(2);
								}
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Sorry, can I get another?", CALM);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("This time, try not to lose it!", "I don't have very many more.", ANNOYED);
								player.getInventory().addItem(new Item(552, 1));
								player.getDialogue().endDialogue();
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("No, sorry I didn't mean to bother you.", CALM);
								player.getDialogue().endDialogue();
								return true;
						}
						return false; // quest stage 2 3 4 end
				}
				return false;
			case 457:
				switch (player.getQuestStage(2)) {
					case 0: // quest stage 0 start
					case 1: // quest stage 1 start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("OoooOoOooo! OooooOooOoo!", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendOption("Ah, so that's why all my pies have been burning.", "Oh, so the coal union is just a bunch of shills?", "I have no idea what you just said.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("OooooooOooOoOoOooo!", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendNpcChat("OoOooOoooOOOOOOOOO!!", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
									case 3:
										player.getDialogue().sendNpcChat("OooooooOooOoOoOooo!", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
								}
								return false;
						}
						return false; // quest stage 0 1 end
					case 2: // quest stage 2 start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("OoOoooOoo!", DISTRESSED);
								if ((player.getEquipment().getId(2) == 552)) {
									player.getDialogue().setNextChatId(2);
								} else {
									player.getDialogue().setNextChatId(9);
								}
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Now where'd I put that amulet?.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Please help me!", DISTRESSED);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("That's better. What's wrong?", CALM);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("You can understand me?!", "Please! Adventurer! Help me!", "My skull was stolen!", NEAR_TEARS);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Stolen? By who?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("By a skeleton, he resides in the basement", "of the Wizard's Tower now.", "Go there and get it back, please!", "Hurry, my voice is fading in your world!", DISTRESSED);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Alright, I'll get it back.", CALM);
								player.setQuestStage(2, 3);
								player.getDialogue().endDialogue();
								return true;
							case 9:
								player.getDialogue().sendNpcChat("OoOooooo! OOOOOoooo!", DISTRESSED);
								return true;
							case 10:
								player.getDialogue().sendOption("Ah, so that's why all my pies have been burning.", "Oh, so the coal union is just a bunch of shills?", "I have no idea what you just said.");
								return true;
							case 11:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("OooooooOooOoOoOooo!", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendNpcChat("OoOooOoooOOOOOOOOO!!", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
									case 3:
										player.getDialogue().sendNpcChat("OooooooOooOoOoOooo!", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
								}
								return false;
						}
						return false; // quest stage 2 end
					case 3:// quest stage 3 start
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you found my skull yet, adventurer?", DISTRESSED);
								if ((player.carryingItem(553))) {
									player.getDialogue().setNextChatId(2);
								} else {
									player.getDialogue().setNextChatId(5);
								}
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Yes, I have!", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Great! I can finally rest in peace!", "Thank you so much adventurer.", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Here, have some prayer experience,", "as a reward.", CONTENT);
								player.getDialogue().endDialogue();
								player.getInventory().removeItem(new Item(553, 1));
								player.setQuestStage(2, 4);
								QuestHandler.completeQuest(player, 2);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Sorry, not yet.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Hurry!", DISTRESSED);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;// quest stage 3 end
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
