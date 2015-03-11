package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.CALM;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;

public class ImpCatcher implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int QUEST_COMPLETE = 2;

	public int dialogueStage = 0;
	private int reward[][] = {
		{1478, 1}, //itemID, amount
	};
	private int expReward[][] = {
		{Skill.MAGIC, 875} // skill ID, exp amount
	};
	private static final int questPointReward = 1;

	public int getQuestID() {
		return 4;
	}

	public String getQuestName() {
		return "Imp Catcher";
	}

	public String getQuestSaveName() {
		return "imp-catcher";
	}

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
		player.getActionSender().sendString((int) (expReward[0][1] * Constants.EXP_RATE) + " Magic Experience", 12151);
		player.getActionSender().sendString("An Amulet of Accuracy", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@Imp Catcher", 7340);
	}

	public void sendQuestRequirements(Player player) {
		String prefix = "";
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
			player.getActionSender().sendString("@str@" + "Wizard Mizgog in The Wizard's Tower.", 8148);

			player.getActionSender().sendString("Wizard Mizgog has asked you to get the following items:", 8150);
			player.getActionSender().sendString("Red Bead", 8151);
			player.getActionSender().sendString("Black Bead", 8152);
			player.getActionSender().sendString("White Bead & Yellow Bead", 8153);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
			player.getActionSender().sendString("@str@" + "Wizard Mizgog in The Wizard's Tower.", 8148);

			player.getActionSender().sendString("@str@ Wizard Mizgog has asked you to get the following items:", 8150);
			player.getActionSender().sendString("@str@" + "Red Bead", 8151);
			player.getActionSender().sendString("@str@" + "Black Bead", 8152);
			player.getActionSender().sendString("@str@" + "White Bead & Yellow Bead", 8153);

			player.getActionSender().sendString("@str@" + "You were rewarded for finding all the beads.", 8154);

			player.getActionSender().sendString("@red@You have completed this quest!", 8155);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString(prefix + "To start the quest, you should talk with", 8147);
			player.getActionSender().sendString(prefix + "Wizard Mizgog in The Wizard's Tower.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@Imp Catcher", 7340);
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
			player.getActionSender().sendString("@yel@Imp Catcher", 7340);
		} else if (questStage >= QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@Imp Catcher", 7340);
		} else {
			player.getActionSender().sendString("@red@Imp Catcher", 7340);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		String prefix = "";
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(prefix + "To start the quest, you should talk with", 8147);
		player.getActionSender().sendString(prefix + "Wizard Mizgog in The Wizard's Tower.", 8148);
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
			case 706: //wizard mizgog
				switch (player.getQuestStage(4)) {
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Looking for a quest, adventurer?", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendOption("I'm always up for a quest!", "Sorry, I'm too busy right now.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendNpcChat("Great! I've got just the one for you.", HAPPY);
										player.getDialogue().setNextChatId(4);
										return true;
									case 2:
										player.getDialogue().sendNpcChat("Ah, come back whenever then. I'll be waiting.", CALM);
										player.getDialogue().endDialogue();
										return true;
								}
								return false;
							case 4:
								player.getDialogue().sendNpcChat("So Wizard Grayzag has sent out his imp to steal", "my precious beads once again, I was", "hoping you could get them back.", HAPPY);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("If you could find all four, I'll reward you.", "I need a black, red, yellow, and white bead.", HAPPY);
								return true;
							case 6:
								player.setQuestStage(4, 1);
								player.getDialogue().sendPlayerChat("Alright, I'm on it.", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 1:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Have you found all my beads yet, Adventurer?", HAPPY);
								if ((player.carryingItem(1470) && player.carryingItem(1472) && player.carryingItem(1474) && player.carryingItem(1476))) {
									player.getDialogue().setNextChatId(2);
								} else {
									player.getDialogue().setNextChatId(5);
								}
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Yes, I've found them all.", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Perfect! Now I can get back to my study!", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Here is your reward.", HAPPY);
								player.getDialogue().endDialogue();
								player.getInventory().removeItem(new Item(1470, 1));
								player.getInventory().removeItem(new Item(1472, 1));
								player.getInventory().removeItem(new Item(1474, 1));
								player.getInventory().removeItem(new Item(1476, 1));
								player.setQuestStage(4, 2);
								QuestHandler.completeQuest(player, 4);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("I'm still working on finding them.", CALM);
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
