package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class ShieldOfArrav implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int FIND_HIDEOUT = 2;
	public static final int KILL_JOHNNY = 3;
	public static final int PHOENIX_GANG = 4;
	public static final int CHARLIE = 5;
	public static final int GET_CROSSBOWS = 6;
	public static final int BLACK_ARM_GANG = 7;
	public static final int SHIELD_TO_CURATOR = 10;
	public static final int CERTS_TO_ROALD = 11;
	public static final int QUEST_COMPLETE = 12;

	public static final int SHIELD_LEFT_HALF = 763;
	public static final int LEFT_HALF_CERTIFICATE = 11173;
	public static final int SHIELD_RIGHT_HALF = 765;
	public static final int RIGHT_HALF_CERTIFICATE = 11174;
	public static final int CERTIFICATE = 769;
	public static final int KATRINE = 642;
	public static final int CHARLIE_THE_TRAMP = 641;
	public static final int KING_ROALD = 648;
	public static final int CURATOR = 646;
	public static final int RELDO = 2660;
	public static final int BARAEK = 547;
	public static final int STRAVEN = 644;
	public int dialogueStage = 0;
	private int reward[][] = {
		{995, 6000}
	};
	private int expReward[][] = {};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 13;
	}

	public String getQuestName() {
		return "Shield of Arrav";
	}

	public String getQuestSaveName() {
		return "shield-arrav";
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
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point,", 12150);
		player.getActionSender().sendString("6000 coins", 12151);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7345);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);

			player.getActionSender().sendString("Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("Or Baraek for the Phoenix Gang.", 8152);
		} else if (questStage == FIND_HIDEOUT) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);

			player.getActionSender().sendString("Baraek has told you where the Phoenix Gang is.", 8154);
			player.getActionSender().sendString("He said it's dangerous and south of the east bank.", 8155);
		} else if (questStage == CHARLIE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);

			player.getActionSender().sendString("Charlie has told you where the Black Arm Gang is.", 8154);
			player.getActionSender().sendString("He said it's behind him in the alleyway.", 8155);
		} else if (questStage == KILL_JOHNNY) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Baraek has told you where the Phoenix Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's dangerous and south of the east bank.", 8155);

			player.getActionSender().sendString("You found the Phoenix Gang hideout and Straven.", 8157);
			player.getActionSender().sendString("You need to complete Straven's task to join the gang.", 8158);
		} else if (questStage == GET_CROSSBOWS) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Charlie has told you where the Black Arm Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's behind him in the alleyway.", 8155);

			player.getActionSender().sendString("You found the Black Arm Gang hideout and Katrine.", 8157);
			player.getActionSender().sendString("You need to get 2 Phoenix crossbows to join the gang.", 8158);
		} else if (questStage == PHOENIX_GANG) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Baraek has told you where the Phoenix Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's dangerous and south of the east bank.", 8155);
			player.getActionSender().sendString("@str@" + "You found the Phoenix Gang hideout and Straven.", 8157);
			player.getActionSender().sendString("@str@" + "You need to kill Johnny the Beard to join the gang.", 8158);

			player.getActionSender().sendString("You are now a member of the Phoenix Gang.", 8160);
			player.getActionSender().sendString("The Phoenix half of the shield should be nearby.", 8161);
		} else if (questStage == BLACK_ARM_GANG) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Charlie has told you where the Black Arm Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's behind him in the alleyway.", 8155);
			player.getActionSender().sendString("@str@" + "You found the Black Arm Gang hideout and Katrine.", 8157);
			player.getActionSender().sendString("@str@" + "You need to get 2 Phoenix crossbows to join the gang.", 8158);

			player.getActionSender().sendString("You are now a member of the Black Arm Gang.", 8160);
			player.getActionSender().sendString("The Black Arm half of the shield should be nearby.", 8161);
		} else if (questStage == SHIELD_TO_CURATOR && player.getQuestVars().isPhoenixGang()) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Baraek has told you where the Phoenix Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's dangerous and south of the east bank.", 8155);
			player.getActionSender().sendString("@str@" + "You found the Phoenix Gang hideout and Straven.", 8157);
			player.getActionSender().sendString("@str@" + "You need to kill Johnny the Beard to join the gang.", 8158);
			player.getActionSender().sendString("@str@" + "You are now a member of the Phoenix Gang.", 8160);
			player.getActionSender().sendString("@str@" + "The Phoenix half of the shield should be nearby.", 8161);

			player.getActionSender().sendString("Take your half of the shield to the musuem curator.", 8163);
			player.getActionSender().sendString("Your partner will have the other half-certificate.", 8164);
		} else if (questStage == SHIELD_TO_CURATOR && player.getQuestVars().isBlackArmGang()) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Charlie has told you where the Black Arm Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's behind him in the alleyway.", 8155);
			player.getActionSender().sendString("@str@" + "You found the Black Arm Gang hideout and Katrine.", 8157);
			player.getActionSender().sendString("@str@" + "You need to get 2 Phoenix crossbows to join the gang.", 8158);
			player.getActionSender().sendString("@str@" + "You are now a member of the Black Arm Gang.", 8160);
			player.getActionSender().sendString("@str@" + "The Black Arm half of the shield should be nearby.", 8161);

			player.getActionSender().sendString("Take your half of the shield to the musuem curator.", 8163);
			player.getActionSender().sendString("Your partner will have the other half-certificate.", 8164);
		} else if (questStage == CERTS_TO_ROALD && player.getQuestVars().isPhoenixGang()) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Baraek has told you where the Phoenix Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's dangerous and south of the east bank.", 8155);
			player.getActionSender().sendString("@str@" + "You found the Phoenix Gang hideout and Straven.", 8157);
			player.getActionSender().sendString("@str@" + "You need to kill Johnny the Beard to join the gang.", 8158);
			player.getActionSender().sendString("@str@" + "You are now a member of the Phoenix Gang.", 8160);
			player.getActionSender().sendString("@str@" + "The Phoenix half of the shield should be nearby.", 8161);
			player.getActionSender().sendString("@str@" + "Work with your friend in the Black Arm Gang,", 8163);
			player.getActionSender().sendString("@str@" + "To get the other half of the Shielf of Arrav.", 8164);
			player.getActionSender().sendString("@str@" + "Have one turn in both halves to the Museum Curator.", 8165);

			player.getActionSender().sendString("Take your certificate to King Roald.", 8167);
		} else if (questStage == CERTS_TO_ROALD && player.getQuestVars().isBlackArmGang()) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Charlie has told you where the Black Arm Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's behind him in the alleyway.", 8155);
			player.getActionSender().sendString("@str@" + "You found the Black Arm Gang hideout and Katrine.", 8157);
			player.getActionSender().sendString("@str@" + "You need to get 2 Phoenix crossbows to join the gang.", 8158);
			player.getActionSender().sendString("@str@" + "You are now a member of the Black Arm Gang.", 8160);
			player.getActionSender().sendString("@str@" + "The Black Arm half of the shield should be nearby.", 8161);
			player.getActionSender().sendString("@str@" + "Work with your friend in the Phoenix Gang,", 8163);
			player.getActionSender().sendString("@str@" + "to get the other half of the Shielf of Arrav.", 8164);
			player.getActionSender().sendString("@str@" + "Have one turn in both halves to the Museum Curator.", 8165);

			player.getActionSender().sendString("Take your certificate to King Roald.", 8167);
		} else if (questStage == QUEST_COMPLETE && player.getQuestVars().isPhoenixGang()) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Baraek has told you where the Phoenix Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's dangerous and south of the east bank.", 8155);
			player.getActionSender().sendString("@str@" + "You found the Phoenix Gang hideout and Straven.", 8157);
			player.getActionSender().sendString("@str@" + "You need to kill Johnny the Beard to join the gang.", 8158);
			player.getActionSender().sendString("@str@" + "You are now a member of the Phoenix Gang.", 8160);
			player.getActionSender().sendString("@str@" + "The Phoenix half of the shield should be nearby.", 8161);
			player.getActionSender().sendString("@str@" + "Work with your friend in the Black Arm Gang,", 8163);
			player.getActionSender().sendString("@str@" + "To get the other half of the Shielf of Arrav.", 8164);
			player.getActionSender().sendString("@str@" + "Have one turn in both halves to the Museum Curator.", 8165);
			player.getActionSender().sendString("@str@" + "Take your certificate to King Roald.", 8167);

			player.getActionSender().sendString("@red@You have completed this quest!", 8169);
		} else if (questStage == QUEST_COMPLETE && player.getQuestVars().isBlackArmGang()) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Charlie has told you where the Black Arm Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's behind him in the alleyway.", 8155);
			player.getActionSender().sendString("@str@" + "You found the Black Arm Gang hideout and Katrine.", 8157);
			player.getActionSender().sendString("@str@" + "You need to get 2 Phoenix crossbows to join the gang.", 8158);
			player.getActionSender().sendString("@str@" + "You are now a member of the Black Arm Gang.", 8160);
			player.getActionSender().sendString("@str@" + "The Black Arm half of the shield should be nearby.", 8161);
			player.getActionSender().sendString("@str@" + "Work with your friend in the Phoenix Gang,", 8163);
			player.getActionSender().sendString("@str@" + "to get the other half of the Shielf of Arrav.", 8164);
			player.getActionSender().sendString("@str@" + "Have one turn in both halves to the Museum Curator.", 8165);
			player.getActionSender().sendString("@str@" + "Take your certificate to King Roald.", 8167);

			player.getActionSender().sendString("@red@You have completed this quest!", 8169);
		} else if (questStage == QUEST_COMPLETE && !player.getQuestVars().isBlackArmGang() && !player.getQuestVars().isPhoenixGang()) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("@str@" + "He can be found in service to Varrock, as the librarian.", 8148);
			player.getActionSender().sendString("@str@" + "Reldo showed you a book on the Shield of Arrav.", 8150);
			player.getActionSender().sendString("@str@" + "Talk to Charlie the Tramp for the Black Arm Gang...", 8151);
			player.getActionSender().sendString("@str@" + "Or Baraek for the Phoenix Gang.", 8152);
			player.getActionSender().sendString("@str@" + "Charlie has told you where the Black Arm Gang is.", 8154);
			player.getActionSender().sendString("@str@" + "He said it's behind him in the alleyway.", 8155);
			player.getActionSender().sendString("@str@" + "You found the gang hideout.", 8157);
			player.getActionSender().sendString("@str@" + "You are now a member of the gang.", 8160);
			player.getActionSender().sendString("@str@" + "Work with your friend in the other gang,", 8163);
			player.getActionSender().sendString("@str@" + "to get the other half of the Shielf of Arrav.", 8164);
			player.getActionSender().sendString("@str@" + "Have one turn in both halves to the Museum Curator.", 8165);
			player.getActionSender().sendString("@str@" + "Take your certificate to King Roald.", 8167);

			player.getActionSender().sendString("@red@You have completed this quest!", 8169);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("To start this quest, talk with Reldo.", 8147);
			player.getActionSender().sendString("He can be found in service to Varrock, as the librarian.", 8148);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7345);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7345);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7345);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7345);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("To start this quest, talk with Reldo.", 8147);
		player.getActionSender().sendString("He can be found in service to Varrock, as the librarian.", 8148);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public boolean itemHandling(Player player, int itemId) {
		if (itemId == 761) { //intel report
			player.getDialogue().sendPlayerChat("I probably shouldn't read this.", CONTENT);
			return true;
		} else if (itemId == 757) { //book
			player.getDialogue().sendPlayerChat("Books are boring.", "Reldo already told me what I need to know.", CONTENT);
			return true;
		} else if (itemId == 769) { //certificate
			player.getDialogue().sendPlayerChat("\"This certificate proves that you helped", "recover the Shield of Arrav.\" ", CONTENT);
			return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if (firstItem == RIGHT_HALF_CERTIFICATE && secondItem == LEFT_HALF_CERTIFICATE) {
			if (player.getQuestStage(13) == 10) {
				player.setQuestStage(13, 11);
			}
			player.getActionSender().sendMessage("You put the certificate together.");
			player.getInventory().replaceItemWithItem(new Item(RIGHT_HALF_CERTIFICATE), new Item(CERTIFICATE));
			player.getInventory().removeItem(new Item(LEFT_HALF_CERTIFICATE));
			return true;
		} else if (firstItem == LEFT_HALF_CERTIFICATE && secondItem == RIGHT_HALF_CERTIFICATE) {
			if (player.getQuestStage(13) == 10) {
				player.setQuestStage(13, 11);
			}
			player.getActionSender().sendMessage("You put the certificate together.");
			player.getInventory().replaceItemWithItem(new Item(RIGHT_HALF_CERTIFICATE), new Item(CERTIFICATE));
			player.getInventory().removeItem(new Item(LEFT_HALF_CERTIFICATE));
			return true;
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		return false;
	}

	public boolean doObjectClicking(Player player, int object, int x, int y) {
		switch (object) {
			case 2405:
				if (x == 3244 && y == 9783) {
					Ladders.climbLadder(player, new Position(3243, 3383, 0));
					return true;
				}
			case 2397: //phoenix gang door
				if (player.getQuestVars().isPhoenixGang() && x == 3247 && y == 9779) {
					player.getActionSender().walkTo(0, player.getPosition().getY() > 9779 ? -1 : 1, true);
					player.getActionSender().walkThroughDoor(2397, 3247, 9779, 0);
					return true;
				} else if (x == 3247 && y == 9779) {
					player.getDialogue().sendStatement("You need to be in the Phoenix Gang to enter.");
					return true;
				}
				return false;
			case 2398: //phoenix weapon store door
				if (player.getInventory().playerHasItem(759) && x == 3251 && y == 3385) {
					player.getActionSender().walkTo(0, player.getPosition().getY() > 3385 ? -1 : 1, true);
					player.getActionSender().walkThroughDoor(2398, 3251, 3385, 0);
					return true;
				} else if (x == 3251 && y == 3385) {
					player.getDialogue().sendStatement("This door is locked.", "There's a small phoenix carved above the keyhole.");
					return true;
				}
				return false;
			case 2399: //black arm gang door
				if (player.getQuestVars().isBlackArmGang() && x == 3185 && y == 3388) {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3388 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(2399, 3185, 3388, 0);
					return true;
				} else if (x == 3185 && y == 3388) {
					player.getDialogue().sendStatement("You need to be in the Black Arm Gang to enter.");
					return true;
				}
				return false;
			case 2403: //phoenix gang chest
				if (y == 9761) {
					new GameObject(2403, 3235, 9761, 0, 0, 10, 2404, 0);
					return true;
				}
				return true;
			case 2404: //phoneix gang open chest
				if (!player.getInventory().ownsItem(SHIELD_LEFT_HALF)) {
					player.getDialogue().sendStatement("You find the left half of the Shield of Arrav.");
					player.getInventory().addItem(new Item(SHIELD_LEFT_HALF));
					if (player.getQuestStage(13) == 4) {
						player.setQuestStage(13, 10);
					}
					return true;
				}
				return true;
			case 2400: //black arm cupboard
				if (y == 3385) {
					new GameObject(2400, 3189, 3385, 1, 2, 10, 2401, 0);
					return true;
				}
				return true;
			case 2401: //black arm open cupboard
				if (!player.getInventory().ownsItem(SHIELD_RIGHT_HALF) && player.getQuestStage(13) == 7) {
					player.getDialogue().sendStatement("You find the right half of the Shield of Arrav.");
					player.getInventory().addItem(new Item(SHIELD_RIGHT_HALF));
					player.setQuestStage(13, 10);
					return true;
				}
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

	public static boolean moreThanTwoCertificates(Player player) {
		int x = 0;
		for (Item item : player.getInventory().getItemContainer().getItems()) {
			if (item == null) {
				continue;
			}
			if (item.getId() == 769) {
				x++;
			}
		}
		return x >= 2;
	}

	public static void handleDrops(Player player, Npc npc) {
		if (npc.getNpcId() == 645 && player.getQuestStage(13) == 3) {
			GroundItem drop = new GroundItem(new Item(761), player, new Position(npc.getPosition().getX(), npc.getPosition().getY()));
			GroundItemManager.getManager().dropItem(drop);
		} else if (npc.getNpcId() == 643) {
			GroundItem drop = new GroundItem(new Item(767), player, new Position(npc.getPosition().getX(), npc.getPosition().getY(), 1));
			GroundItemManager.getManager().dropItem(drop);
			//   GroundItem drop2 = new GroundItem(new Item(767), player, new Position(npc.getPosition().getX(), npc.getPosition().getY(), 1));
			GroundItemManager.getManager().dropItem(drop);
		}
	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case KATRINE:
				switch (player.getQuestStage(13)) {
					case 1:
					case 2:
					case 5:
					case 6:
					case 7:
					case 10:
					case 11:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getQuestStage(13) == 5) {
									player.getDialogue().sendPlayerChat("I know about your gang.", CONTENT);
									return true;
								} else if (player.getQuestStage(13) == 6) {
									if (player.getInventory().playerHasItem(767, 2)) {
										player.getDialogue().sendPlayerChat("I got you your crossbows.", CONTENT);
										player.getInventory().removeItem(new Item(767, 2));
										player.getDialogue().setNextChatId(15);
										return true;
									} else {
										return false;
									}
								} else {
									return false;
								}
							case 2:
								player.getDialogue().sendNpcChat("Who told you?!", ANGRY_2);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I won't reveal my source...", "...and I won't tell anyone about you...", "...if you allow me to join.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("You're in luck, we're still gutting our last victim.", "And I'm in need of something.", ANNOYED);
								return true;
							case 5:
								player.getDialogue().sendStatement("You hear a scream from upstairs.");
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Good, I'm not afraid of you.", "What do I need to do?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Our weapon stores have become low as of late.", "I need you to steal 2 Phoenix crossbows for me.", CONTENT);
								player.setQuestStage(13, 6);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("The Phoenix Gang weapon storeroom is", "up the ladder of a locked house near the chaos altar.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("If it's locked, how do I get in?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Im sure you'll figure it out.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Good, you were up to the task.", "I suppose you are worthy of the gang.", CONTENT);
								player.getQuestVars().joinBlackArmGang(true);
								player.setQuestStage(13, 7);
								return true;
							case 16:
								player.getDialogue().sendStatement("You are now a member of the Black Arm Gang.");
								return true;
							case 17:
								player.getDialogue().sendNpcChat("Welcome to the jungle.", LAUGHING);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case CHARLIE_THE_TRAMP:
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(13) == 1) {
							player.getDialogue().sendPlayerChat("I'm looking for info on the Black Arm Gang.", CONTENT);
							return true;
						}
						return false;
					case 2:
						player.getDialogue().sendNpcChat("Oi! The gang!", "I hear'em bangin' around all night!", ANNOYED);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("You can hear them?", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("Yes'sir they'er be right down that there alleym'way!", CONTENT);
						player.setQuestStage(13, 5);
						return true;
					case 5:
						player.getDialogue().sendPlayerChat("Interesting...", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case KING_ROALD:
				switch (player.getQuestStage(13)) {
					case 11:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I found the Shield of Arrav!", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Prove it.", CONTENT);
								return true;
							case 3:
								if (player.getInventory().playerHasExactItem(769, 1)) {
									player.getDialogue().sendStatement("You give King Roald your certificate.");
									player.getInventory().removeItem(new Item(769));
									return true;
								} else if (moreThanTwoCertificates(player)) {
									player.getDialogue().sendStatement("You have too many certificates.");
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I-I can't...", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("Fantastic! It's true!", "Here's your reward!", HAPPY);
								return true;
							case 5:
								player.setQuestStage(13, 12);
								QuestHandler.completeQuest(player, 13);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case CURATOR:
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Hello, and welcome to the museum!", "Feel free to look around!", HAPPY);
						return true;
					case 2:
						if (player.getQuestStage(13) == 10 && player.getInventory().playerHasItem(SHIELD_LEFT_HALF)) {
							player.getDialogue().sendPlayerChat("I've recovered the left half of the Shield of Arrav!", HAPPY);
							return true;
						} else if (player.getQuestStage(13) == 10 && player.getInventory().playerHasItem(SHIELD_RIGHT_HALF)) {
							player.getDialogue().sendPlayerChat("I've recovered the right half of the Shield of Arrav!", HAPPY);
							player.getDialogue().setNextChatId(9);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("Thank you!", HAPPY);
							player.getDialogue().endDialogue();
							return true;
						}
					case 3:
						player.getDialogue().sendNpcChat("Oh! You have!", "This is quite the day, heavens be blessed!", HAPPY);
						return true;
					case 4:
						player.getDialogue().sendGiveItemNpc("You hand the curator your half...", "He hands you two half-certificates in exchange.", new Item(SHIELD_LEFT_HALF), new Item(LEFT_HALF_CERTIFICATE));
						player.getInventory().replaceItemWithItem(new Item(SHIELD_LEFT_HALF), new Item(LEFT_HALF_CERTIFICATE, 2));
						return true;
					case 5:
						player.getDialogue().sendNpcChat("Here, take these halves of the certificate.", "They've been drawn up for a long time...", "...waiting for someone to find the Shield!", HAPPY);
						return true;
					case 6:
						player.getDialogue().sendNpcChat("Give one half to your partner in exchange for his half.", "Then put them together and return to King Roald,", "I'm sure he has a reward for you.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 9:
						player.getDialogue().sendGiveItemNpc("You hand the curator your half...", "He hands you two half-certificates in exchange.", new Item(SHIELD_RIGHT_HALF), new Item(RIGHT_HALF_CERTIFICATE));
						player.getInventory().replaceItemWithItem(new Item(SHIELD_RIGHT_HALF), new Item(RIGHT_HALF_CERTIFICATE, 2));
						return true;
					case 10:
						player.getDialogue().sendNpcChat("Here, take these halves of the certificate.", "They've been drawn up for a long time...", "...waiting for someone to find the Shield!", HAPPY);
						return true;
					case 11:
						player.getDialogue().sendNpcChat("Give one half to your partner in exchange for his half.", "Then put them together and return to King Roald,", "I'm sure he has a reward for you.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case RELDO:
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Hello there!", HAPPY);
						return true;
					case 2:
						if (player.getQuestStage(13) == 0) {
							player.getDialogue().sendOption("I'm looking for a quest.", "It's a great day!");
							return true;
						} else {
							player.getDialogue().sendPlayerChat("It's a great day!", HAPPY);
							player.getDialogue().setNextChatId(4);
							return true;
						}
					case 3:
						switch (optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("I'm looking for a quest.", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("It's a great day!", HAPPY);
								return true;
						}
					case 4:
						player.getDialogue().sendNpcChat("Indeed it is, a great day for knowledge!", LAUGHING);
						player.getDialogue().endDialogue();
						return true;
					case 5:
						player.getDialogue().sendNpcChat("A quest eh? Hmm...", "...I did just find this book on our shelves.", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendStatement("Reldo hands you an old book.");
						player.setQuestStage(13, 1);
						QuestHandler.getQuests()[13].startQuest(player);
						player.getInventory().addItem(new Item(757));
						return true;
					case 7:
						player.getDialogue().sendNpcChat("The Shield of Arrav! Ah yes, a great mystery.", CONTENT);
						return true;
					case 8:
						player.getDialogue().sendPlayerChat("What's so mysterious about it?", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("It disappeared from the King's treasury years ago.", "Rumor has it that it was split in two and divided.", "One half for each of the two gangs in Varrock.", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendPlayerChat("What are the names of these two gangs?", CONTENT);
						return true;
					case 11:
						player.getDialogue().sendNpcChat("You don't know? Oh my dear boy!", "The Phoenix Gang and the Black Arm Gang...", "...rivals for many years.", LAUGHING);
						return true;
					case 12:
						player.getDialogue().sendPlayerChat("Do you think the shield could be recovered?", CONTENT);
						return true;
					case 13:
						player.getDialogue().sendNpcChat("Perhaps, but it's been so long...", "...only the gangs themselves know at this point.", CONTENT);
						return true;
					case 14:
						player.getDialogue().sendPlayerChat("Who would I speak with about the gangs?", CONTENT);
						return true;
					case 15:
						player.getDialogue().sendNpcChat("Baraek the fur man knows about Phoenix...", "...but not much is known about the Black Arms.", "They're known to hang around Charlie the Tramp.", CONTENT);
						return true;
					case 16:
						player.getDialogue().sendPlayerChat("Sounds like a lead to me.", "I'm going to go do some infiltrating.", "So long, Reldo.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case BARAEK:
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(13) == 1) {
							player.getDialogue().sendPlayerChat("I need information about the Phoenix Gang.", CONTENT);
							return true;
						} else {
							player.getDialogue().sendNpcChat("Can't you see I'm busy!", ANGRY_1);
							player.getDialogue().endDialogue();
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("Hah! I bet you do!", LAUGHING);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("I've got 20 gold right here.", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("Oooh... Well now...", "Perhaps I do know something.", CONTENT);
						return true;
					case 5:
						if (player.getInventory().playerHasItem(995, 20)) {
							player.getDialogue().sendGiveItemNpc("You hand Baraek 20 gold.", new Item(995, 20));
							player.getInventory().removeItem(new Item(995, 20));
							return true;
						} else {
							player.getDialogue().sendNpcChat("You filthy liar! You don't have the coin!", ANGRY_2);
							player.getDialogue().endDialogue();
							return true;
						}
					case 6:
						player.getDialogue().sendNpcChat("Their hideout is south of the east bank.", "It's a dangerous place though!", "Tell no one you heard this from me.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(13, 2);
						return true;
				}
				return false;
			case STRAVEN:
				switch (player.getQuestStage(13)) {
					case 1:
					case 2:
					case 3:
					case 4:
					case 10:
					case 11:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getQuestStage(13) == 2) {
									player.getDialogue().sendPlayerChat("I know who you are.", CONTENT);
									return true;
								} else if (player.getQuestStage(13) == 3) {
									if (player.getInventory().playerHasItem(761)) {
										player.getDialogue().sendPlayerChat("Here's what Johnny was carrying.", CONTENT);
										player.getInventory().removeItem(new Item(761));
										player.getDialogue().setNextChatId(15);
										return true;
									} else {
										player.getDialogue().sendNpcChat("Well? Do you have anything for me?", CONTENT);
										player.getDialogue().setNextChatId(20);
										return true;
									}
								} else if (player.getQuestStage(13) >= 4 && !player.getInventory().ownsItem(759)) {
									player.getDialogue().sendNpcChat("Don't lose the key this time.", ANGRY_1);
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(759));
									return true;
								} else {
									return false;
								}
							case 2:
								player.getDialogue().sendNpcChat("Oh, and who might that be?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("You must be the leader of the Phoenix Gang.", "You're standing in front of the entrance to their hideout.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Alright, you got me. What do you want?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("I want to join the Phoenix Gang.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("HA HA HA! A COMEDIAN!", LAUGHING);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("I'm serious.", ANGRY_1);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("If you're serious, I have a task for you.", "Kill Johnny the Beard in the Blue Moon Inn.", ANNOYED);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Why?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("He has something I need.", "I thought you were serious?", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("I am, I'll be right back.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(13, 3);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Excellent. You were serious.", "I suppose you are worthy of the gang.", CONTENT);
								player.getQuestVars().joinPhoenixGang(true);
								player.setQuestStage(13, 4);
								return true;
							case 16:
								player.getDialogue().sendStatement("You are now a member of the Phoenix Gang.");
								return true;
							case 17:
								player.getDialogue().sendNpcChat("Here's a spare key to our weapons store room.", "It's in the room east of our hideout entrance.", "Just go up the ladder.", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendStatement("Straven hands you a small key.");
								player.getInventory().addItem(new Item(759));
								return true;
							case 19:
								player.getDialogue().sendPlayerChat("Thank you.", "I'll be your best member some day!", HAPPY);
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
