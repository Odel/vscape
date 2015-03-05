package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CALM;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.NEAR_TEARS;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.dialogue.Dialogues.VERY_SAD;
import static com.rs2.model.content.dialogue.Dialogues.checkTrim;
import static com.rs2.model.content.dialogue.Dialogues.trimCape;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;
import com.rs2.model.players.ShopManager;

/**
 * @date 1-jun-2011
 * @author Satan666
 */
public class TheKnightsSword implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int ITEMS_GIVEN = 2;
	public static final int QUEST_NEARING_COMPLETION = 3;
	public static final int QUEST_COMPLETE = 4;

	public int dialogueStage = 0;
	private int reward[][] = {
		{995, 0}, //itemID, amount
	};
	private int expReward[][] = {
		{Skill.SMITHING, 12725} // skill ID, exp amount
	};
	private static final int questPointReward = 1;

	public int getQuestID() {
		return 1;
	}

	public String getQuestName() {
		return "The Knight's Sword";
	}

	public String getQuestSaveName() {
		return "knights-sword";
	}

	/**
	 * Does the user meet the requirements if yes return true.
	 *
	 * @param player
	 * @return
	 */
	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItem(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1])); //DO NOT TIMES EXP BY *Constants.EXP_RATE (its done by addexp)
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Smithing Experience", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@The Knight's Sword", 7346);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with the", 8147);
			player.getActionSender().sendString("@str@" + "Squire found in Falador Castle.", 8148);

			player.getActionSender().sendString("The Squire said you should find a redberry pie", 8150);
			player.getActionSender().sendString("Imcando dwarves love redberry pies", 8151);
			player.getActionSender().sendString("After one pie, he'll have no choice but to make it", 8152);
			player.getActionSender().sendString("He should be able to make a new sword.", 8153);
		} else if (questStage == ITEMS_GIVEN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with the", 8147);
			player.getActionSender().sendString("@str@" + "Squire found in Falador Castle.", 8148);

			player.getActionSender().sendString("@str@" + "The Squire said you should make a redberry pie", 8150);
			player.getActionSender().sendString("@str@" + "Imcando dwarves love redberry pies", 8151);
			player.getActionSender().sendString("@str@" + "After one pie, he'll have no choice but to make it", 8152);
			player.getActionSender().sendString("@str@" + "He should be able to make a new sword.", 8153);

			player.getActionSender().sendString("The dwarf asked for blurite ore and an iron bar.", 8154);
		} else if (questStage == QUEST_NEARING_COMPLETION) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with the", 8147);
			player.getActionSender().sendString("@str@" + "Squire found in Falador Castle.", 8148);

			player.getActionSender().sendString("@str@" + "The Squire said you should find a redberry pie", 8150);
			player.getActionSender().sendString("@str@" + "Imcando dwarves love redberry pies", 8151);
			player.getActionSender().sendString("@str@" + "After one pie, he'll have no choice but to make it", 8152);
			player.getActionSender().sendString("@str@" + "He should be able to make a new sword.", 8153);

			player.getActionSender().sendString("I need to deliver the sword to the Squire.", 8154);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with the", 8147);
			player.getActionSender().sendString("@str@" + "Squire found in Falador Castle.", 8148);

			player.getActionSender().sendString("@str@" + "The Squire said you should find a redberry pie", 8150);
			player.getActionSender().sendString("@str@" + "Imcando dwarves love redberry pies", 8151);
			player.getActionSender().sendString("@str@" + "After one pie, he'll have no choice but to make it", 8152);
			player.getActionSender().sendString("@str@" + "He should be able to make a new sword.", 8153);

			player.getActionSender().sendString("@str@" + "I delivered the new sword to the Squire.", 8154);

			player.getActionSender().sendString("@red@You have completed this quest!", 8155);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(prefix + "To start the quest, you should talk with the", 8147);
			player.getActionSender().sendString(prefix + "Squire found in Falador Castle.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@The Knight's Sword", 7346);
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
			player.getActionSender().sendString("@yel@The Knight's Sword", 7346);
		} else if (questStage >= QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@The Knight's Sword", 7346);
		} else {
			player.getActionSender().sendString("@red@The Knight's Sword", 7346);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "To start the quest, you should talk with Squire", 8147);
		player.getActionSender().sendString(prefix + "found in Falador Castle.", 8148);
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
			case 606: //Squire for The Knight's Sword
				switch (player.getQuestStage(1)) {
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("What am I going to do?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendOption("What's wrong?", "You don't look very happy.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("I've lost Sir Vyvin's sword!", DISTRESSED);
										player.getDialogue().setNextChatId(4);
										return true;
									case 2:
										player.getDialogue().sendNpcChat("Yes, please go away unless you can help!", NEAR_TEARS);
										player.getDialogue().setNextChatId(5);
										return true;
								}
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Could you please help me create another?", DISTRESSED);
								return true;
							case 5:
								player.getDialogue().sendOption("I'm always happy to help a person in distress.", "Sorry, I have troubles of my own.");
								return true;
							case 6:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Thank you! You can talk to the dwarf found near Rimmington.", "You'll need a redberry pie and an iron bar.", HAPPY);
										player.setQuestStage(1, 1);
										QuestHandler.getQuests()[1].startQuest(player);
										player.getDialogue().setNextChatId(3);

										return true;
									case 2:
										player.getDialogue().sendNpcChat("Oh, okay then...", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;

								}
						}
						return false;
					case 1:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you created a new sword yet?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Not yet, I'm working on it..", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Please hurry!", DISTRESSED);
								return true;
							case 4:
								player.getDialogue().sendOption("I'll get right on it.", "What do I need before I talk to the dwarf");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("There's no time to waste! Hurry!", DISTRESSED);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendNpcChat("He'll probably require some other", "materials from what I remember.", "You'll need to make a redberry pie to make him talk.", CONTENT);
										player.getDialogue().setNextChatId(6);
										return true;
								}
							case 6:
								player.getDialogue().sendNpcChat("Then you'll need an iron bar to", "create the base of the sword.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 3:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you made a new sword yet?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Yes, I have!", HAPPY);
								if (player.carryingItem(667)) {
									player.getDialogue().setNextChatId(3);
								} else {
									player.getDialogue().setNextChatId(10);
								}
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Oh thank the Heavens!", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Thank you!", HAPPY);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Oh, it was nothing.", HAPPY);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("I'm sure it really was, hero!", HAPPY);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Ah, it was really nothing.", HAPPY);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Thanks so much, hero. Here's a reward.", HAPPY);
								return true;
							case 9:
								player.getDialogue().endDialogue();
								player.getInventory().removeItem(new Item(667, 1));
								player.setQuestStage(1, 4);
								QuestHandler.completeQuest(player, 1);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("You didn't bring the sword with you.", NEAR_TEARS);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return true;
			case 604: //Thurgo for Knight's Sword
				switch (player.getQuestStage(1)) {
					case 1:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Bring me redberry pie!", CALM);
								if (player.carryingItem(2325)) {
									player.getDialogue().setNextChatId(2);
								} else {
									player.getDialogue().setNextChatId(9);
								}
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Oo! Redberry pie! Thankya human!", HAPPY);
								player.getInventory().removeItem(new Item(2325, 1));
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Could you make a new copy of Sir Vyvin's Sword?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Me remember that guy, he ask for blurite sword.", "He pay many redberry pies. Why need another?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("The Squire kind of lost it...", "He sent me to get a new one.", DISTRESSED);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Me suppose me make another.", "I out of blurite though, bring me some.", "Also need an iron bar.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Alright, do you know where I can find the ore?", HAPPY);
								return true;
							case 8:
								player.setQuestStage(1, 2);
								player.getDialogue().sendNpcChat("Maybe in mine on hill. Deep in dungeon me used to find", "very big veins. Contain very scary monsters, though.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Go away!", "Don't come back until you bring pie!", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 2:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you found the materials yet?", DISTRESSED);
								if ((player.carryingItem(668) && player.carryingItem(2351))) {
									player.getDialogue().setNextChatId(2);
								} else {
									player.getDialogue().setNextChatId(7);
								}
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I've found the materials!", "Could you make the sword now?", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Yeah, I make now.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Thanks so much.", HAPPY);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Here your sword.", "Come back with more redberry pie next time!", CONTENT);
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(667, 1));
								player.getInventory().removeItem(new Item(668, 1));
								player.getInventory().removeItem(new Item(2351, 1));
								player.setQuestStage(1, 3);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Oh, I will! Thanks Thurgo!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Oh, no. I'm having trouble finding it.", DISTRESSED);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("You can find ore in mine, on hill next to shack.", "Iron bar you find by yourself.", CALM);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 3:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.hasItem(667)) //check inv and bank idiot lost sword
								{
									player.getDialogue().sendNpcChat("what human want now?", CALM);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Go away human.", CALM);
									player.getDialogue().setNextChatId(4);
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("I've lost the sword!", "can you make me a new one?", DISTRESSED);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("stupid human bring material i make new.", ANNOYED);
								player.getDialogue().endDialogue();
								player.setQuestStage(1, 2);
								return true;
							case 4:
								if (player.getSkill().getLevel()[Skill.SMITHING] == 99 && !checkTrim(player)) {
									player.getDialogue().sendPlayerChat("B-but I need my skillcape!", SAD);
									player.getDialogue().setNextChatId(7);
									return true;
								} else if (player.getSkill().getLevel()[Skill.SMITHING] == 99 && checkTrim(player)) {
									player.getDialogue().sendOption("B-but I need my skillcape!", "Can you trim my skillcape?");
								} else {
									player.getDialogue().endDialogue();
								}
								return true;
							case 5:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("B-but I need my skillcape!", NEAR_TEARS);
										player.getDialogue().setNextChatId(7);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Can you trim my skillcape?", CONTENT);
										player.getDialogue().setNextChatId(10);
										return true;
								}
							case 7:
								player.getDialogue().sendNpcChat("stupid human always concerned with weak clothing", "but I s'pose you've earned it", ANGRY_2);
								return true;
							case 8:
								ShopManager.openShop(player, 187);
								player.getDialogue().dontCloseInterface();
								return true;
							case 10:
								player.getDialogue().sendNpcChat("i guess thurgo can, but thurgo can no undo trimming!", "human can always buy another anyways...", ANGRY_1);
								return true;
							case 11:
								if (player.getInventory().playerHasItem(9786)) {
									player.getDialogue().sendNpcChat("here", ANGRY_1);
									trimCape(player, 9786);
								} else {
									player.getDialogue().sendPlayerChat("I guess I don't have the cape on me.", VERY_SAD);
									player.getDialogue().endDialogue();
								}
								return true;
						}
						return false;
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Go away human.", CALM);
								return true;
							case 2:
								if (player.getSkill().getLevel()[Skill.SMITHING] == 99 && !checkTrim(player)) {
									player.getDialogue().sendPlayerChat("B-but I need my skillcape!", SAD);
									player.getDialogue().setNextChatId(5);
									return true;
								} else if (player.getSkill().getLevel()[Skill.SMITHING] == 99 && checkTrim(player)) {
									player.getDialogue().sendOption("B-but I need my skillcape!", "Can you trim my skillcape?");
									return true;
								} else {
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("B-but I need my skillcape!", NEAR_TEARS);
										player.getDialogue().setNextChatId(5);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Can you trim my skillcape?", CONTENT);
										player.getDialogue().setNextChatId(8);
										return true;
								}
							case 5:
								player.getDialogue().sendNpcChat("stupid human always concerned with weak clothing", "but I s'pose you've earned it", ANGRY_2);
								return true;
							case 6:
								ShopManager.openShop(player, 187);
								player.getDialogue().dontCloseInterface();
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Sure, but this is irreversible!", "However, you can always buy another untrimmed cape.", CONTENT);
								return true;
							case 9:
								if (player.getInventory().playerHasItem(9795)) {
									player.getDialogue().sendNpcChat("Here you are.", CONTENT);
									trimCape(player, 9795);
								} else {
									player.getDialogue().sendPlayerChat("I guess I don't have the cape on me.", VERY_SAD);
									player.getDialogue().endDialogue();
								}
						}
						return false;
				}
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
