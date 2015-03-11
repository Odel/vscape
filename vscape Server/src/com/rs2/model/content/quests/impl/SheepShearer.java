package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class SheepShearer implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int QUEST_STARTEDB = 2;
	public static final int ITEMS_GIVEN = 3;
	public static final int QUEST_COMPLETE = 4;

	public int dialogueStage = 0;
	private int reward[][] = {
		{995, 60} //itemID, amount
	};

	private int expReward[][] = {
		{Skill.CRAFTING, 160} // skill ID, exp amount
	};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 8;
	}

	public String getQuestName() {
		return "Sheep Shearer";
	}

	public String getQuestSaveName() {
		return "sheep-shearer";
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
		player.getActionSender().sendString(questPointReward + " Quest Point", 12150);
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Crafting Experience", 12151);
		player.getActionSender().sendString("60 Coins", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7344);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		player.getActionSender().sendString(getQuestName(), 8144);
		switch (questStage) {
			case 1:
			case 2:
				player.getActionSender().sendString("@str@I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
				player.getActionSender().sendString("@str@@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);

				int woolCount = player.getInventory().getItemAmount(1759);
				if (player.carryingItem(1759) && woolCount >= 20) {
					player.getActionSender().sendString("@str@Fred has asked me to get the following items:", 8150);
					player.getActionSender().sendString("@str@20 Balls of wool", 8151);
					player.getActionSender().sendString("I need to give Fred the balls of wool", 8153);
				} else {
					player.getActionSender().sendString("Fred has asked me to get the following items:", 8150);
					player.getActionSender().sendString("20 Balls of wool", 8151);
					player.getActionSender().sendString("", 8153);
				}
				break;
			case 3:
				player.getActionSender().sendString("@str@I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
				player.getActionSender().sendString("@str@@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);

				player.getActionSender().sendString("@str@Fred has asked me to get the following items:", 8150);
				player.getActionSender().sendString("@str@20 Balls of wool", 8151);

				player.getActionSender().sendString("@str@I need to give Fred the balls of wool", 8153);

				player.getActionSender().sendString("Maybe i can collect my reward now.", 8154);
				break;
			case 4:
				player.getActionSender().sendString("@str@I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
				player.getActionSender().sendString("@str@@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);

				player.getActionSender().sendString("@str@Fred has asked me to get the following items:", 8150);
				player.getActionSender().sendString("@str@20 Balls of wool", 8151);

				player.getActionSender().sendString("@str@I need to give Fred the balls of wool", 8153);

				player.getActionSender().sendString("@red@You have completed this quest!", 8155);
				break;
			default:
				player.getActionSender().sendString("I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
				player.getActionSender().sendString("@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);
		}

	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7344);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7344);
		} else if (questStage >= QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7344);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7344);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
		player.getActionSender().sendString("@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);
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
			case 758: //fred
				if (!QuestHandler.questCompleted(player, 8)) {
					switch (player.getQuestStage(8)) {
						case 0:
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("What are you doing on my land? You're not the one", "who keeps leaving all my gates open and letting out all", "my sheep are you?", CONTENT);
									return true;
								case 2:
									player.getDialogue().sendOption("I'm looking for a quest.", "I'm lost.");
									return true;
								case 3:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("I'm looking for a quest.", CONTENT);
											player.getDialogue().setNextChatId(6);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("I'm lost.", CONTENT);
											player.getDialogue().setNextChatId(4);
											return true;
									}
									break;
								case 4:
									player.getDialogue().sendNpcChat("You're at my farm North of Lumbridge castle.", CONTENT);
									return true;
								case 5:
									player.getDialogue().sendPlayerChat("Ok thanks.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								case 6:
									player.getDialogue().sendNpcChat("You're after a quest you say? Actually I could do with", "a bit of help.", CONTENT);
									return true;
								case 7:
									player.getDialogue().sendNpcChat("My sheep are getting mighty woolly. I'd be much", "obliged if you could sheer them. And while you're at it", "spin the wool for me too.", CONTENT);
									return true;
								case 8:
									player.getDialogue().sendNpcChat("Yes that's it. Bring me 20 balls of wool. And I'm sure", "I could sort out some sort of payment. Of course,", "there's the small matter of The Thing.", CONTENT);
									return true;
								case 9:
									player.getDialogue().sendOption("Yes okay. I can do that.", "That doesn't sound a very exciting quest.", "What do you mean, The Thing?");
									return true;
								case 10:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("Yes okay. I can do that.", CONTENT);
											if (!player.hasItem(1735)) {
												if (player.getInventory().getItemContainer().freeSlots() >= 1) {
													player.getInventory().addItem(new Item(1735, 1));
												}
											}
											player.setQuestStage(8, 1);
											QuestHandler.getQuests()[8].startQuest(player);
											player.getDialogue().setNextChatId(1);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("That doesn't sound a very exciting quest.", CONTENT);
											player.getDialogue().setNextChatId(11);
											return true;
										case 3:
											player.getDialogue().sendPlayerChat("What do you mean, The Thing?", CONTENT);
											player.getDialogue().setNextChatId(12);
											return true;
									}
									break;
								case 11:
									player.getDialogue().sendNpcChat("Well, what do you expect if you ask a farmer? ", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								case 12:
									player.getDialogue().sendNpcChat("Well, now, no one has ever seen 'The Thing'. That's why", "we call it 'The Thing', 'cos we don't know what it is.", CONTENT);
									return true;
								case 13:
									player.getDialogue().sendNpcChat("Some say it's a black-hearted shape-shifter, hungering for the", "souls of decent, hardworking folk like me, Others say it's just a sheep.", CONTENT);
									player.getDialogue().setNextChatId(9);
									return true;
							}
							break;
						case 1:
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("Good! Now one more thing, do you actually know how", "to shear a sheep?", CONTENT);
									return true;
								case 2:
									player.getDialogue().sendOption("Of course!", "Err. No, I don't know actually.");
									return true;
								case 3:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("Of course!", CONTENT);
											player.getDialogue().setNextChatId(7);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("Err. No, I don't know actually.", CONTENT);
											player.getDialogue().setNextChatId(4);
											return true;
									}
									break;
								case 4:
									if (player.carryingItem(1735)) {
										player.getDialogue().sendNpcChat("Well, you're halfway there already. You have some shears", "in your inventory. Just use those on a sheep to shear it.", CONTENT);
									} else {
										player.getDialogue().sendNpcChat("Well, get some shears from the general store south of here", "and Just use those on a sheep to shear it.", CONTENT);
									}
									return true;
								case 5:
									player.getDialogue().sendPlayerChat("That's all i have to do?", CONTENT);
									return true;
								case 6:
									player.getDialogue().sendNpcChat("Well, Once you've collected some wool", "you'll need to spin it into balls.", CONTENT);
									return true;
								case 7:
									player.getDialogue().sendNpcChat("Do you know how to spin wool?", CONTENT);
									return true;
								case 8:
									player.getDialogue().sendOption("I don't know how to spin wool sorry.", "I'm something of an expert actually.");
									return true;
								case 9:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("I don't know how to spin wool sorry.", CONTENT);
											player.getDialogue().setNextChatId(10);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("I'm something of an expert actually.", CONTENT);
											player.getDialogue().endDialogue();
											player.setQuestStage(8, 2);
											return true;
									}
									break;
								case 10:
									player.getDialogue().sendNpcChat("Don't worry it's quite simple!", CONTENT);
									return true;
								case 11:
									player.getDialogue().sendNpcChat("The nearest Spinning Wheel can be found on the first", "floor of Lumbridge Castle.", CONTENT);
									return true;
								case 12:
									player.getDialogue().sendNpcChat("To get to Lumbridge Castle just follow the road east.", CONTENT);
									return true;
								case 13:
									player.getDialogue().sendPlayerChat("Thank you!", CONTENT);
									player.getDialogue().endDialogue();
									player.setQuestStage(8, 2);
									return true;
							}
							break;
						case 2:
							switch (player.getDialogue().getChatId()) {
								case 1:
									if (player.carryingItem(1759)) {
										if (player.getInventory().getItemAmount(1759) >= 20) {
											player.getDialogue().sendPlayerChat("I have 20 balls of wool!", CONTENT);
											player.getDialogue().setNextChatId(4);
										} else {
											player.getDialogue().sendPlayerChat("How many more balls of wool do you need?", CONTENT);
											player.getDialogue().setNextChatId(2);
										}
									} else {
										player.getDialogue().sendPlayerChat("How many more balls of wool do you need?", CONTENT);
										player.getDialogue().setNextChatId(2);
									}
									return true;
								case 2:
									if (!player.carryingItem(1759)) {
										player.getDialogue().sendNpcChat("You need 20 balls of wool.", CONTENT);
										player.getDialogue().setNextChatId(3);
									} else {
										int woolCount = player.getInventory().getItemAmount(1759);
										if (woolCount < 20) {
											int neededWool = 20 - woolCount;
											player.getDialogue().sendNpcChat("You need " + neededWool + " more balls of wool.", CONTENT);
											player.getDialogue().setNextChatId(3);
										}
									}
									return true;
								case 3:
									player.getDialogue().sendPlayerChat("Okay, I'll work on it!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								case 4:
									player.getDialogue().sendNpcChat("Give 'em here then", CONTENT);
									return true;
								case 5:
									player.getDialogue().sendPlayerChat("Here you go.", CONTENT);
									if (player.carryingItem(1759)) {
										if (player.getInventory().getItemAmount(1759) >= 20) {
											player.getInventory().removeItem(new Item(1759, 20));
											player.setQuestStage(8, 3);
										}
									}
									player.getDialogue().setNextChatId(1);
									return true;
							}
							break;
						case 3:
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendNpcChat("I guess I'd better pay yo then.", CONTENT);
									player.getDialogue().endDialogue();
									player.setQuestStage(8, 4);
									QuestHandler.completeQuest(player, 8);
									return true;
							}
							break;
					}
				} else {

				}
				break;
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
