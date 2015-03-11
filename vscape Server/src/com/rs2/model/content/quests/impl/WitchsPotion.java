package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class WitchsPotion implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int DRINK_POTION = 2;
	public static final int QUEST_COMPLETE = 3;

	public int dialogueStage = 0;

	private int expReward[][] = {
		{Skill.MAGIC, 325} // skill ID, exp amount
	};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 6;
	}

	public String getQuestName() {
		return "Witch's Potion";
	}

	public String getQuestSaveName() {
		return "witchs-potion";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
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
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Magic Experience", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7348);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case 1:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@Talk to Hetty in her house in Rimmington,", 8147);
				player.getActionSender().sendString("@str@south of Falador and west of Port Sarim.", 8148);

				player.getActionSender().sendString("Hetty has asked you to get the following items:", 8150);
				if (player.carryingItem(2146)) {
					player.getActionSender().sendString("@str@Burnt Meat", 8151);
				} else {
					player.getActionSender().sendString("Burnt Meat", 8151);
				}
				if (player.carryingItem(221)) {
					player.getActionSender().sendString("@str@Eye of newt", 8152);
				} else {
					player.getActionSender().sendString("Eye of newt", 8152);
				}
				if (player.carryingItem(1957)) {
					player.getActionSender().sendString("@str@Onion", 8153);
				} else {
					player.getActionSender().sendString("Onion", 8153);
				}
				if (player.carryingItem(300)) {
					player.getActionSender().sendString("@str@Rat's tail", 8154);
				} else {
					player.getActionSender().sendString("Rat's tail", 8154);
				}
				if (player.carryingItem(2146) && player.carryingItem(221) && player.carryingItem(1957) && player.carryingItem(300)) {
					player.getActionSender().sendString("@str@Hetty has asked you to get the following items:", 8150);
					player.getActionSender().sendString("You need to give Hetty all the items", 8155);
				} else {
					player.getActionSender().sendString("@str@Hetty has asked you to get the following items:", 8150);
					player.getActionSender().sendString("", 8155);
				}
				break;
			case 2:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@Talk to Hetty in her house in Rimmington,", 8147);
				player.getActionSender().sendString("@str@south of Falador and west of Port Sarim.", 8148);
				player.getActionSender().sendString("@str@Hetty has asked you to get the following items:", 8150);
				player.getActionSender().sendString("@str@Burnt Meat", 8151);
				player.getActionSender().sendString("@str@Eye of newt", 8152);
				player.getActionSender().sendString("@str@Onion", 8153);
				player.getActionSender().sendString("@str@Rat's tail", 8154);
				player.getActionSender().sendString("@str@You gave Hetty all the items", 8155);
				player.getActionSender().sendString("I need to drink the potion!", 8156);
				break;
			case 3:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@Talk to Hetty in her house in Rimmington,", 8147);
				player.getActionSender().sendString("@str@south of Falador and west of Port Sarim.", 8148);
				player.getActionSender().sendString("@str@Hetty has asked you to get the following items:", 8150);
				player.getActionSender().sendString("@str@Burnt Meat", 8151);
				player.getActionSender().sendString("@str@Eye of newt", 8152);
				player.getActionSender().sendString("@str@Onion", 8153);
				player.getActionSender().sendString("@str@Rat's tail", 8154);
				player.getActionSender().sendString("@str@You gave Hetty all the items", 8155);
				player.getActionSender().sendString("@str@I need to drink the potion!", 8156);
				player.getActionSender().sendString("@red@You have completed this quest!", 8157);
				break;
			default:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString(prefix + "Talk to Hetty in her house in Rimmington,", 8147);
				player.getActionSender().sendString(prefix + "south of Falador and west of Port Sarim.", 8148);
		}

	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7348);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7348);
		} else if (questStage >= QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7348);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7348);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "Talk to Hetty in her house in Rimmington,", 8147);
		player.getActionSender().sendString(prefix + "south of Falador and west of Port Sarim.", 8148);
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
			//witchs potion
			case 307:
				switch (player.getQuestStage(6)) {
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Ara Ara~", "What could you want with an old woman like me?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendOption("I am in search of a quest.", "N-nothing, sorry.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I am in search of a quest.", CONTENT);
										player.getDialogue().setNextChatId(4);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("N-nothing, sorry.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
								return false;
							case 4:
								player.getDialogue().sendNpcChat("Hmmm... Maybe i can think of something for you.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Would you like to become more proficient in the dark", "arts?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Dark arts? What you mean improve my magic?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendStatement("The witch sighs.");
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Yes, improve your magic...", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Do you have no sense of drama?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendOption("Yes I'd like to improve my magic.", "No I'm not interested.");
								return true;
							case 11:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Yes I'd like to improve my magic.", CONTENT);
										player.getDialogue().setNextChatId(12);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("No I'm not interested.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
								return false;
							case 12:
								player.getDialogue().sendStatement("The witch sighs.");
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Ok I'm going to make a potion to help bring out your", "darker self.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("You will need certain ingredients.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendPlayerChat("What do i need?", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("You need an eye of newt, a rat's tail, an onion... Oh", "and a peice of burnt meat.", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendPlayerChat("Great, I'll go and get them.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(6, 1);
								QuestHandler.getQuests()[6].startQuest(player);
								return true;
						}
						return false;
					case 1:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("So have you found the things for the potion?", CONTENT);
								if (player.carryingItem(2146) && player.carryingItem(221) && player.carryingItem(1957) && player.carryingItem(300)) {
									player.getDialogue().setNextChatId(5);
								} else {
									player.getDialogue().setNextChatId(2);
								}
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("No, Not yet.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Remember You need an eye of newt, a rat's tail, an onion", "and a peice of burnt meat.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Ok, thanks.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Yes I have everything!", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Excellent, can I have them then?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendStatement("You pass the ingredients to Hetty and she puts them all into her", "cauldron. Hetty closes her eyes and begins to chant. The cauldron", "bubbles mysteriously.");
								player.getInventory().removeItem(new Item(2146, 1));
								player.getInventory().removeItem(new Item(221, 1));
								player.getInventory().removeItem(new Item(1957, 1));
								player.getInventory().removeItem(new Item(300, 1));
								player.setQuestStage(6, 2);
								player.getDialogue().setNextChatId(1);
								return true;
						}
						return false;
					case 2:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Well, is it ready?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Ok, drink from the cauldron", CONTENT);
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
