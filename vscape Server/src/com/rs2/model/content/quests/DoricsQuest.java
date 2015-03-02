package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;

public class DoricsQuest implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int NEEDS_ITEMS = 2;
	public static final int QUEST_COMPLETE = 3;

	public int dialogueStage = 0;
	private int reward[][] = {
		{995, 180}, //itemID, amount
	};
	private int expReward[][] = {
		{Skill.MINING, 1300} // skill ID, exp amount
	};
	private static final int questPointReward = 1;

	public int getQuestID() {
		return 3;
	}

	public String getQuestName() {
		return "Doric's Quest";
	}

	public String getQuestSaveName() {
		return "dorics-quest";
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
		player.getActionSender().sendString((int) (reward[0][1]) + " coins", 12151);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Mining Experience", 12152);
		player.getActionSender().sendString("Use of Doric's Anvils", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7336);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Doric", 8147);
			player.getActionSender().sendString("@str@" + "found in North West of Falador.", 8148);

			player.getActionSender().sendString("Doric has asked you to get the following items:", 8150);
			player.getActionSender().sendString("6 clay", 8151);
			player.getActionSender().sendString("4 copper ore", 8152);
			player.getActionSender().sendString("2 iron ore", 8153);
		} else if (questStage == NEEDS_ITEMS) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Doric", 8147);
			player.getActionSender().sendString("@str@" + "found in North West of Falador.", 8148);

			int clayCount = player.getInventory().getItemAmount(434);
			int copperCount = player.getInventory().getItemAmount(436);
			int ironCount = player.getInventory().getItemAmount(440);
			if (player.carryingItem(434) && clayCount >= 6) {
				player.getActionSender().sendString("@str@" + "6 clay", 8151);
			} else {
				player.getActionSender().sendString("6 clay", 8151);
			}
			if (player.carryingItem(436) && copperCount >= 4) {
				player.getActionSender().sendString("@str@" + "4 copper ore", 8152);
			} else {
				player.getActionSender().sendString("4 copper ore", 8152);
			}
			if (player.carryingItem(440) && ironCount >= 2) {
				player.getActionSender().sendString("@str@" + "2 iron ore", 8153);
			} else {
				player.getActionSender().sendString("2 iron ore", 8153);
			}
			if (clayCount >= 6 && copperCount >= 4 && ironCount >= 2) {
				player.getActionSender().sendString("@str@" + "Doric has asked you to get the following items:", 8150);
				player.getActionSender().sendString("You need to give Doric all the items", 8154);
			} else {
				player.getActionSender().sendString("Doric has asked you to get the following items:", 8150);
				player.getActionSender().sendString("", 8154);
			}
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Doric", 8147);
			player.getActionSender().sendString("@str@" + "found in North West of Falador.", 8148);

			player.getActionSender().sendString("@str@" + "Doric has asked you to get the following items:", 8150);
			player.getActionSender().sendString("@str@" + "6 clay", 8151);
			player.getActionSender().sendString("@str@" + "4 copper ore", 8152);
			player.getActionSender().sendString("@str@" + "2 iron ore", 8153);

			player.getActionSender().sendString("@str@" + "You brought Doric all the items", 8154);

			player.getActionSender().sendString("@red@You have completed this quest!", 8155);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(prefix + "To start the quest, you should talk to Doric", 8147);
			player.getActionSender().sendString(prefix + "found in North West of Falador.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7336);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7336);
		} else if (questStage >= QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7336);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7336);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "To start the quest, you should talk to Doric", 8147);
		player.getActionSender().sendString(prefix + "found in North West of Falador.", 8148);
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
			case 284: // Doric
				switch (player.getQuestStage(3)) {
					case 0: // quest stage 0
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello traveller, what brings you to my humble smithy?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I wanted to use your anvils.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("My anvils get enough work with my own use.", "I make pickaxes, and it takes a lot of hard work.", "If you could get me some more materials,", "then I could let you use them.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendOption("Yes, I will get you the materials.", "No, hitting rocks is for the boring people, sorry.");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Yes, I will get you the materials.", CONTENT);
										player.getDialogue().setNextChatId(6);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("No, hitting rocks is for the boring people, sorry.", CONTENT);
										player.getDialogue().setNextChatId(7);
										return true;
								}
								return false;
							case 6:
								//set quest stage 1
								player.setQuestStage(3, 1);
								QuestHandler.getQuests()[3].startQuest(player);
								player.getDialogue().sendNpcChat("Clay is what I use more than anything, to make casts.", "Could you get me 6 clay, 4 copper ore, and 2 iron ore, please?", "I could pay a little, and let you use my anvils.", "Take this pickaxe with you just in case you need it.", CONTENT);
								player.getDialogue().setNextChatId(1);
								player.getInventory().addItemOrDrop(new Item(1265, 1));
								return true;
							case 7:
								player.getDialogue().sendNpcChat("That is your choice. Nice to meet you anyway", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 1: // quest stage 1
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Where can I find those?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("You'll be able to find all those ores in the rocks,", "just inside the Dwarven Mine.", "Head east from here and you'll find the entrance,", " in the side of Ice Mountain.", CONTENT);
								if (player.getSkill().getLevel()[Skill.MINING] < 15) {
									player.getDialogue().setNextChatId(3);
								} else {
									//set quest stage 2
									player.setQuestStage(3, 2);
									player.getDialogue().endDialogue();
								}
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("But I'm not a good enough miner to get iron ore.", CONTENT);
								return true;
							case 4:
								//set quest stage 2
								player.setQuestStage(3, 2);
								player.getDialogue().sendNpcChat("Oh well, you could practice mining until you can.", "Can't beat a bit of mining - it's a useful skill. Failing that,", "you might be able to find a more experienced adventurer,", "to buy the iron ore off.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 2: // quest stage 2
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you got my materials yet, traveller?", CONTENT);
								if (player.carryingItem(434) && player.carryingItem(436) && player.carryingItem(440)) {
									int clayCount = player.getInventory().getItemAmount(434);
									int copperCount = player.getInventory().getItemAmount(436);
									int ironCount = player.getInventory().getItemAmount(440);
									if (clayCount >= 6 && copperCount >= 4 && ironCount >= 2) {
										player.getDialogue().setNextChatId(2);
									} else {
										player.getDialogue().setNextChatId(5);
									}
								} else {
									player.getDialogue().setNextChatId(5);
								}
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I have everything you need.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Many thanks. Pass them here, please.", " I can spare you some coins for your trouble,", "and please use my anvils any time you want.", CONTENT);
								return true;
							case 4:
								player.getInventory().removeItem(new Item(434, 6));
								player.getInventory().removeItem(new Item(436, 4));
								player.getInventory().removeItem(new Item(440, 2));
								player.getDialogue().sendStatement("You hand the clay, copper, and iron to Doric.");
								player.getDialogue().endDialogue();
								QuestHandler.completeQuest(player, 3);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Sorry, I don't have them all yet.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Not to worry, stick at it.", "Remember, I need 6 clay, 4 copper ore, and 2 iron ore.", CONTENT);
								player.getDialogue().endDialogue();
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
