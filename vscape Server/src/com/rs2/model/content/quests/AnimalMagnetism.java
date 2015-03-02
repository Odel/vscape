package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;

import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.quests.GhostsAhoy.BLACK_INTERFACE_TEXT;
import static com.rs2.model.content.quests.GhostsAhoy.STRING_ON_BLACK;

import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class AnimalMagnetism implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int TALK_TO_HUSBAND = 2;
	public static final int BACK_TO_ALICE = 3;
	public static final int BACK_TO_HUSBAND = 4;
	public static final int BACK_TO_ALICE_AGAIN = 5;
	public static final int BACK_TO_HUSBAND_AGAIN = 6;
	public static final int BACK_TO_ALICE_TWICE = 7;
	public static final int TALK_TO_CRONE = 8;
	public static final int TALK_TO_CRONE_AGAIN = 9;
	public static final int AMULET_TO_HUSBAND = 10;
	public static final int CHASE_SOME_COCK = 11;
	public static final int BUY_SOME_COCK = 12;
	public static final int TALK_TO_WITCH = 13;
	public static final int WITCH_NEEDS_IRON = 14;
	public static final int HAMMER_BAR = 15;
	public static final int CHOP_TREE = 16;
	public static final int TELL_AVA_ABOUT_TREE = 17;
	public static final int TALK_TO_TURAEL = 18;
	public static final int MATERIALS_FOR_TURAEL = 19;
	public static final int CHOP_TREE_PART_DUEX = 20;
	public static final int NOTES = 21;
	public static final int CRAFT_CONTAINER = 22;
	public static final int QUEST_COMPLETE = 23;

	public static final int GHOSTSPEAK_AMULET = 552;
	public static final int HARD_LEATHER = 1743;
	public static final int ECTOTOKEN = 4278;
	public static final int IRON_BAR = 2351;
	public static final int HOLY_SYMBOL = 1718;
	public static final int MITHRIL_AXE = 1355;
	public static final int HAMMER = 2347;
	public static final int UNDEAD_CHICKEN = 10487;
	public static final int SELECTED_IRON = 10488;
	public static final int BAR_MAGNET = 10489;
	public static final int UNDEAD_TWIGS = 10490;
	public static final int BLESSED_AXE = 10491;
	public static final int RESEARCH_NOTES = 10492;
	public static final int TRANSLATED_NOTES = 10493;
	public static final int PATTERN = 10494;
	public static final int CONTAINER = 10495;
	public static final int BUTTONS = 688;
	public static final int POLISHED_BUTTONS = 10496;
	public static final int AVAS_ATTRACTOR = 10498;
	public static final int AVAS_ACCUMULATOR = 10499;
	public static final int CRONE_AMULET = 10500;

	public static final int AVA = 5198;
	public static final int TURAEL = 70;
	public static final int WITCH = 5200;
	public static final int ALICE = 2307;
	public static final int ALICES_HUSBAND = 5202;
	public static final int UNDEAD_TREE = 5208;

	public int dialogueStage = 0;

	private int reward[][] = {};
	private int expReward[][] = {
		{Skill.CRAFTING, 1000},
		{Skill.FLETCHING, 1000},
		{Skill.SLAYER, 1000},
		{Skill.WOODCUTTING, 2500},};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 25;
	}

	public String getQuestName() {
		return "Animal Magnetism";
	}

	public String getQuestSaveName() {
		return "animal-magnetism";
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
		player.getActionSender().sendString("2250 XP in each of Crafting,", 12150);
		player.getActionSender().sendString("Fletching and Slayer", 12151);
		player.getActionSender().sendString("5625 Woodcutting XP", 12152);
		player.getActionSender().sendString("1 Quest Point", 12153);
		player.getActionSender().sendString("Ava's device", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 12772);
	}

	public void sendQuestRequirements(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);

			player.getActionSender().sendString("I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("near Port Phasmatys, one for her and one for my 'reward'.", 8151);
		} else if (questStage == TALK_TO_HUSBAND) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);

			player.getActionSender().sendString("Alice said her husband can get me 2 chickens. I need", 8153);
			player.getActionSender().sendString("to speak with him with a Ghostspeak amulet equipped.", 8154);
		} else if (questStage == BACK_TO_ALICE || questStage == BACK_TO_ALICE_AGAIN || questStage == BACK_TO_ALICE_TWICE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);

			player.getActionSender().sendString("Alice's husband told me to speak to Alice again.", 8153);
		} else if (questStage == BACK_TO_HUSBAND || questStage == BACK_TO_HUSBAND_AGAIN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);

			player.getActionSender().sendString("Alice told me to ask her husband about their savings.", 8153);
		} else if (questStage == TALK_TO_CRONE || questStage == TALK_TO_CRONE_AGAIN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);

			player.getActionSender().sendString("Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("Alice said the witch is west of Frenkenstrain's Castle.", 8155);
		} else if (questStage == AMULET_TO_HUSBAND) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);

			player.getActionSender().sendString("I found the old crone and had her make the amulet.", 8156);
			player.getActionSender().sendString("I just need to give it to Alice's husband.", 8157);
		} else if (questStage == CHASE_SOME_COCK) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);

			player.getActionSender().sendString("I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("to catch me some chickens now.", 8157);
		} else if (questStage == BUY_SOME_COCK) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);

			player.getActionSender().sendString("I should buy two chickens and return to Ava.", 8159);
		} else if (questStage == TALK_TO_WITCH) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);

			player.getActionSender().sendString("I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("her plan for some sort of Range affiliated contraption.", 8160);
			player.getActionSender().sendString("She told me to speak to the Witch next door in regards", 8161);
			player.getActionSender().sendString("to some sort of super-magnet.", 8162);
		} else if (questStage == WITCH_NEEDS_IRON) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);

			player.getActionSender().sendString("The Witch told me she needs 5 iron bars for", 8162);
			player.getActionSender().sendString("that super-magnet.", 8163);
		} else if (questStage == HAMMER_BAR) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);

			player.getActionSender().sendString("The Witch gave me the special bar for the magnet.", 8162);
			player.getActionSender().sendString("She told me to hit it with a hammer in the Rimmington mine.", 8163);
		} else if (questStage == CHOP_TREE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);
			player.getActionSender().sendString("@str@" + "The Witch gave me the special bar for the magnet.", 8161);
			player.getActionSender().sendString("@str@" + "She told me to hit it with a hammer in the Rimmington mine.", 8162);

			player.getActionSender().sendString("I crafted the magnet for Ava's device. Ava says I now", 8164);
			player.getActionSender().sendString("need to chop a branch off of an animated tree.", 8165);
		} else if (questStage == TELL_AVA_ABOUT_TREE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);
			player.getActionSender().sendString("@str@" + "I crafted the magnet for Ava's device. Ava says I now", 8162);
			player.getActionSender().sendString("@str@" + "need to chop a branch off of an animated tree.", 8163);

			player.getActionSender().sendString("I couldn't manage to cut a branch off of the tree.", 8165);
			player.getActionSender().sendString("I should ask Ava for help.", 8166);
		} else if (questStage == TALK_TO_TURAEL) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);
			player.getActionSender().sendString("@str@" + "I crafted the magnet for Ava's device. Ava says I now", 8162);
			player.getActionSender().sendString("@str@" + "need to chop a branch off of an animated tree.", 8163);

			player.getActionSender().sendString("I couldn't manage to cut a branch off of the tree.", 8165);
			player.getActionSender().sendString("Ava told me to find Turael and talk to him about it.", 8166);
		} else if (questStage == MATERIALS_FOR_TURAEL) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);
			player.getActionSender().sendString("@str@" + "I crafted the magnet for Ava's device. Ava says I now", 8162);
			player.getActionSender().sendString("@str@" + "need to chop a branch off of an animated tree.", 8163);

			player.getActionSender().sendString("Turael told me I need a blessed axe to get the branch.", 8165);
			player.getActionSender().sendString("He said I can have his if I bring him replacment materials.", 8166);
			player.getActionSender().sendString("He needs a mithril axe and a holy symbol of Saradomin.", 8167);
		} else if (questStage == CHOP_TREE_PART_DUEX) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);
			player.getActionSender().sendString("@str@" + "I crafted the magnet for Ava's device. Ava says I now", 8162);
			player.getActionSender().sendString("@str@" + "need to chop a branch off of an animated tree.", 8163);

			player.getActionSender().sendString("Turael gave me his blessed axe. I need to chop", 8165);
			player.getActionSender().sendString("that undead branch off for Ava now.", 8166);
		} else if (questStage == NOTES) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);
			player.getActionSender().sendString("@str@" + "I crafted the magnet for Ava's device.", 8162);
			player.getActionSender().sendString("@str@" + "I chopped the branch for Ava's device.", 8164);

			player.getActionSender().sendString("I need to unscramble Ava's research notes now.", 8166);
		} else if (questStage == CRAFT_CONTAINER) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);
			player.getActionSender().sendString("@str@" + "I crafted the magnet for Ava's device.", 8162);
			player.getActionSender().sendString("@str@" + "I chopped the branch for Ava's device.", 8164);
			player.getActionSender().sendString("@str@" + "I unscrambled Ava's research notes.", 8166);

			player.getActionSender().sendString("I just need to craft this container for the device.", 8168);
			player.getActionSender().sendString("The pattern and notes said to use hard leather with", 8169);
			player.getActionSender().sendString("some polished buttons.", 8170);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Ava in Draynor Manor to begin this quest.", 8147);
			player.getActionSender().sendString("@str@" + "I agreed to help Ava fix up her room in Draynor Manor.", 8149);
			player.getActionSender().sendString("@str@" + "She said I need to get 2 undead chickens from the farm", 8150);
			player.getActionSender().sendString("@str@" + "near Port Phasmatys, one for her and one for my 'reward'.", 8151);
			player.getActionSender().sendString("@str@" + "Alice told me I may be able to find a witch who", 8153);
			player.getActionSender().sendString("@str@" + "can make a modified Ghostspeak amulet for her husband.", 8154);
			player.getActionSender().sendString("@str@" + "I gave the amulet to Alice's husband, he just needs", 8156);
			player.getActionSender().sendString("@str@" + "to catch me some chickens now.", 8157);
			player.getActionSender().sendString("@str@" + "I gave Ava her undead chicken, and she detailed", 8159);
			player.getActionSender().sendString("@str@" + "her plan for some sort of Range affiliated contraption.", 8160);
			player.getActionSender().sendString("@str@" + "I crafted the magnet for Ava's device.", 8162);
			player.getActionSender().sendString("@str@" + "I chopped the branch for Ava's device.", 8164);
			player.getActionSender().sendString("@str@" + "I unscrambled Ava's research notes.", 8166);
			player.getActionSender().sendString("@str@" + "Ava and I crafted her device!", 8168);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8170);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Talk to @dre@Ava @bla@in @dre@Draynor Manor @bla@to begin this quest.", 8147);
			player.getActionSender().sendString("@dre@Requirements:", 8149);
			if (player.getSkill().getLevel()[Skill.SLAYER] < 18) {
				player.getActionSender().sendString("-18 Slayer.", 8150);
			} else {
				player.getActionSender().sendString("@str@-18 Slayer.", 8150);
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < 19) {
				player.getActionSender().sendString("-19 Crafting.", 8151);
			} else {
				player.getActionSender().sendString("@str@-19 Crafting.", 8151);
			}
			if (player.getSkill().getLevel()[Skill.RANGED] < 30) {
				player.getActionSender().sendString("-30 Range.", 8152);
			} else {
				player.getActionSender().sendString("@str@-30 Range.", 8152);
			}
			if (player.getSkill().getLevel()[Skill.WOODCUTTING] < 35) {
				player.getActionSender().sendString("-35 Woodcutting.", 8153);
			} else {
				player.getActionSender().sendString("@str@-35 Woodcutting.", 8153);
			}
			if (player.getQuestStage(23) < 12) {
				player.getActionSender().sendString("-Access to Canifis.", 8154);
			} else {
				player.getActionSender().sendString("@str@ -Access to Canifis.", 8154);
			}
			if (player.getQuestStage(22) < 3) {
				player.getActionSender().sendString("-Completion of Ernest the Chicken.", 8155);
			} else {
				player.getActionSender().sendString("@str@-Completion of Ernest the Chicken.", 8155);
			}
			if (player.getQuestStage(2) < 4) {
				player.getActionSender().sendString("-Completion of The Restless Ghost.", 8156);
			} else {
				player.getActionSender().sendString("@str@-Completion of The Restless Ghost.", 8156);
			}
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 12772);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 12772);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 12772);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 12772);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to @dre@Ava @bla@in @dre@Draynor Manor @bla@to begin this quest.", 8147);
		player.getActionSender().sendString("@dre@Requirements:", 8149);
		if (player.getSkill().getLevel()[Skill.SLAYER] < 18) {
			player.getActionSender().sendString("-18 Slayer.", 8150);
		} else {
			player.getActionSender().sendString("@str@-18 Slayer.", 8150);
		}
		if (player.getSkill().getLevel()[Skill.CRAFTING] < 19) {
			player.getActionSender().sendString("-19 Crafting.", 8151);
		} else {
			player.getActionSender().sendString("@str@-19 Crafting.", 8151);
		}
		if (player.getSkill().getLevel()[Skill.RANGED] < 30) {
			player.getActionSender().sendString("-30 Range.", 8152);
		} else {
			player.getActionSender().sendString("@str@-30 Range.", 8152);
		}
		if (player.getSkill().getLevel()[Skill.WOODCUTTING] < 35) {
			player.getActionSender().sendString("-35 Woodcutting.", 8153);
		} else {
			player.getActionSender().sendString("@str@-35 Woodcutting.", 8153);
		}
		if (player.getQuestStage(23) < 12) {
			player.getActionSender().sendString("-Access to Canifis.", 8154);
		} else {
			player.getActionSender().sendString("@str@ -Access to Canifis.", 8154);
		}
		if (player.getQuestStage(22) < 3) {
			player.getActionSender().sendString("-Completion of Earnest the Chicken.", 8155);
		} else {
			player.getActionSender().sendString("@str@-Completion of Earnest the Chicken.", 8155);
		}
		if (player.getQuestStage(2) < 4) {
			player.getActionSender().sendString("-Completion of The Restless Ghost.", 8156);
		} else {
			player.getActionSender().sendString("@str@-Completion of The Restless Ghost.", 8156);
		}
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static boolean handleNpcClick(Player player, int npcId) {
		switch (npcId) {
			case UNDEAD_TREE:
				switch (player.getQuestStage(25)) {
					case CHOP_TREE:
					case TELL_AVA_ABOUT_TREE:
						if (player.getSkill().getLevel()[Skill.WOODCUTTING] < 35) {
							player.getDialogue().sendStatement("You need 35 Woodcutting to chop this tree.");
							return true;
						}
						final Tools.Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
						if (axe == null) {
							player.getActionSender().sendMessage("You do not have an axe which you have the woodcutting level to use.");
							return true;
						} else {
							player.getActionSender().sendMessage("The axe bounces off the undead wood. I should report this to Ava.");
							if (player.getQuestStage(25) == CHOP_TREE) {
								player.setQuestStage(25, TELL_AVA_ABOUT_TREE);
							}
							return true;
						}
					case TALK_TO_TURAEL:
						player.getDialogue().sendPlayerChat("I need to go talk to Turael before", "trying this again!", CONTENT);
						return true;
					case CHOP_TREE_PART_DUEX:
						if (player.getSkill().getLevel()[Skill.WOODCUTTING] < 35) {
							player.getDialogue().sendStatement("You need 35 Woodcutting to chop this tree.");
							return true;
						}
						if (player.getSkill().getLevel()[Skill.SLAYER] < 18) {
							player.getDialogue().sendStatement("You need 18 Slayer to use the blessed axe correctly.");
							return true;
						}
						if (!player.getInventory().playerHasItem(BLESSED_AXE) && player.getEquipment().getId(Constants.WEAPON) != BLESSED_AXE) {
							player.getActionSender().sendMessage("You need the blessed axe to chop a branch!");
							return true;
						} else {
							final Player thisPlayer = player;
							CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer b) {
									thisPlayer.getActionSender().sendSound(472, 0, 0);
									thisPlayer.getUpdateFlags().sendAnimation(871);
									b.stop();
								}

								@Override
								public void stop() {
									thisPlayer.getActionSender().sendMessage("You cut some undead twigs.");
									thisPlayer.getInventory().addItemOrDrop(new Item(UNDEAD_TWIGS));
								}
							}, 2);
						}
				}
				return false;
		}
		return false;
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case RESEARCH_NOTES:
				Dialogues.startDialogue(player, 10478);
				return true;
			case BUTTONS:
				player.getInventory().replaceItemWithItem(new Item(BUTTONS), new Item(POLISHED_BUTTONS));
				player.getActionSender().sendMessage("You polish the buttons.");
				return true;
		}
		return false;
	}

	@Override
	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if ((firstItem == HAMMER && secondItem == SELECTED_IRON) || (firstItem == SELECTED_IRON && secondItem == HAMMER)) {
			if (player.inRimmingtonMine()) {
				if (player.getSkill().getLevel()[Skill.CRAFTING] < 19) {
					player.getDialogue().sendStatement("You need 19 crafting to hammer the magnet.");
					return true;
				} else {
					player.getUpdateFlags().sendAnimation(898); //smithing anim
					player.getActionSender().sendMessage("You hammer the selected iron carefully... It seems to work.");
					player.getInventory().replaceItemWithItem(new Item(SELECTED_IRON), new Item(BAR_MAGNET));
					return true;
				}
			}
		}
		if ((firstItem == HARD_LEATHER && secondItem == POLISHED_BUTTONS) || (firstItem == POLISHED_BUTTONS && secondItem == HARD_LEATHER) && player.getQuestStage(25) == CRAFT_CONTAINER) {
			if (!player.getInventory().playerHasItem(PATTERN)) {
				player.getDialogue().sendStatement("You need the pattern to make the container for Ava.");
				return true;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < 19) {
				player.getDialogue().sendStatement("You need 19 Crafting to make the container.");
				return true;
			} else {
				player.getActionSender().sendMessage("You put Ava's mysterious container together.");
				player.getInventory().removeItem(new Item(PATTERN));
				player.getInventory().removeItem(new Item(POLISHED_BUTTONS));
				player.getInventory().replaceItemWithItem(new Item(HARD_LEATHER), new Item(CONTAINER));
				return true;
			}
		}
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
			case 10478:
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("Hmm, there seems to be a pattern here...", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendStatement("You shuffle the notes around.");
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("No, that's not quite right.", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendStatement("You move the pages around some more.");
						return true;
					case 5:
						player.getDialogue().sendPlayerChat("'Combine... ttons... with... ther' ... This", "makes a little more sense.", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendStatement("You switch the last page with the first page.");
						return true;
					case 7:
						player.getDialogue().sendPlayerChat("'Combine some polished buttons with hard leather.'", "'This will form a container with which to hold the'", "'implements needed for the device.'...", "That sounds clear enough.", CONTENT);
						player.getDialogue().endDialogue();
						player.getInventory().replaceItemWithItem(new Item(RESEARCH_NOTES), new Item(TRANSLATED_NOTES));
						return true;
				}
			case TURAEL:
				switch (player.getQuestStage(25)) {
					case MATERIALS_FOR_TURAEL:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if ((player.getInventory().playerHasItem(MITHRIL_AXE) && player.getInventory().playerHasItem(HOLY_SYMBOL)) || (player.getEquipment().getId(Constants.WEAPON) == MITHRIL_AXE && player.getInventory().playerHasItem(HOLY_SYMBOL))) {
									player.getDialogue().sendPlayerChat("I have the materials for that blessed axe.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Have those materials yet?", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("I can make an axe for you now, if you wish.", "Remember, it will be no use for normal woodcutting", "after I have added the silver edge.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I understand, give me the axe.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Here's a new axe; may it serve you well.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendGiveItemNpc("Turael hands you a blessed axe for your materials.", new Item(BLESSED_AXE));
								player.getDialogue().endDialogue();
								if (player.getEquipment().getId(Constants.WEAPON) == MITHRIL_AXE) {
									player.getEquipment().unequip(Constants.WEAPON);
								}
								player.getInventory().replaceItemWithItem(new Item(MITHRIL_AXE), new Item(BLESSED_AXE));
								player.getInventory().removeItem(new Item(HOLY_SYMBOL));
								player.setQuestStage(25, CHOP_TREE_PART_DUEX);
								return true;
							case 20:
								player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case TALK_TO_TURAEL:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("'Ello, and what are you after, then?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm here about a quest. Ava said she saw you hanging", "around the moving trees near Draynor Manor.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Ahh, you came to the right man, odd things - those trees.", "What is it you are needing exactly?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("I need some of the wood from them, but", "my axe just bounced off the trunk.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Sounds like you need a blessed axe. No one really", "makes them, though, these days.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Blessed axes are too soft to cut normal wood since the", "axe's edge must be replaced by holy silver. I can do", "you a favor though.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("If you give me a mithril axe and a holy symbol of", "Saradomin I can let you have my axe. I'll make myself", "a new one when no one is pestering me for Slayer", "tasks.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Okay, so I'll see whether I can spare", "an axe and a symbol. Thanks.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, MATERIALS_FOR_TURAEL);
								return true;
						}
						return false;
				}
				return false;
			case WITCH:
				switch (player.getQuestStage(25)) {
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello, hello, my poppet. What brings you to my little", "room?", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Nothing, just having a peek around.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Ah, okay, I hope you have a wonderful rest of", "your day peeping around in people's rooms!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case WITCH_NEEDS_IRON:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(IRON_BAR, 5)) {
									player.getDialogue().sendNpcChat("Great, you'll go far! I made some nice painted metal", "toys for you, snookums.", HAPPY);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Did you get those iron bars yet sweetie?", HAPPY);
									player.getDialogue().setNextChatId(10);
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("Toys? Snookums? What are you on about, you", "deranged old bat?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Oh, forget it, then. If you won't react to kindness, I'm", "back to luring infants into my oven. You'll have it on", "your conscience.", ANGRY_2);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Go to the iron mine just north-east of Rimmington and", "hit the bar with a plain old smithing hammer.", "Then take your new magnet to Ava. Poor girl,", "having to deal with whippersnappers like you.", ANNOYED);
								player.getDialogue().endDialogue();
								player.getInventory().replaceItemWithItem(new Item(IRON_BAR, 5), new Item(SELECTED_IRON));
								player.setQuestStage(25, 15);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case TALK_TO_WITCH:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello, hello, my poppet. What brings you to my little", "room?", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Ava told me to ask you about making magnets.", "Something about natural fields and other stuff.", "Sounded like she needed a farmer, to be honest.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Don't worry, deary, I can tell you just what to do and", "you won't have to worry your pretty head about the", "complicated bits.", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("No need to patronize me quite so much, you know.", ANNOYED);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I went to anger management classes, my lambkin; that's", "why I was treating you so kindly. It's either this way", "of talking or I'll go back to shoving children into ovens.", HAPPY);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Just bring me 5 iron bars, and you're well on the", "way to never having to talk to me again.", HAPPY);
								return true;
							case 7:
								if (player.getInventory().playerHasItem(IRON_BAR, 5)) {
									player.getDialogue().sendPlayerChat("I happen to have those bars right here.", CONTENT);
									player.getDialogue().setNextChatId(1);
									player.setQuestStage(25, 14);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I'll be back.", CONTENT);
									player.getDialogue().endDialogue();
									player.setQuestStage(25, 14);
									return true;
								}
						}
						return false;
				}
			case GhostsAhoy.OLD_CRONE:
				switch (player.getQuestStage(25)) {
					case AMULET_TO_HUSBAND:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().ownsItem(CRONE_AMULET)) {
									player.getDialogue().sendPlayerChat("I lost that amulet you made for me...", SAD);
									return true;
								} else {
									return false;
								}
							case 2:
								player.getDialogue().sendPlayerChat("Can you do your mystical stuff", "to my ghostspeak amulet again?", CONTENT);
								return true;
							case 3:
								if (!player.getInventory().playerHasItem(GHOSTSPEAK_AMULET) && player.getEquipment().getId(Constants.AMULET) == GHOSTSPEAK_AMULET) {
									player.getDialogue().sendNpcChat("You need to take off your Ghostspeak amulet first sonny.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else if (!player.getInventory().canAddItem(new Item(CRONE_AMULET))) {
									player.getDialogue().sendNpcChat("You need a free inventory space for the new amulet sonny.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendNpcChat("Sure I can sonny, here we go.", CONTENT);
									return true;
								}
							case 4:
								player.getDialogue().sendPlayerChat("Thank you again.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Just being a good neighbor.", HAPPY);
								player.getDialogue().endDialogue();
								player.getInventory().addItem(new Item(CRONE_AMULET));
								return true;
						}
						return false;
					case TALK_TO_CRONE_AGAIN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I'm here about the farmers east of here.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Are you ready to do your mystical stuff", "with my ghostspeak amulet?", CONTENT);
								return true;
							case 3:
								if (!player.getInventory().playerHasItem(GHOSTSPEAK_AMULET) && player.getEquipment().getId(Constants.AMULET) == GHOSTSPEAK_AMULET) {
									player.getDialogue().sendNpcChat("You need to take off your Ghostspeak amulet first sonny.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else if (!player.getInventory().canAddItem(new Item(CRONE_AMULET))) {
									player.getDialogue().sendNpcChat("You need a free inventory space for the new amulet sonny.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendNpcChat("I most certainly am, there we go!", CONTENT);
									return true;
								}
							case 4:
								player.getDialogue().sendPlayerChat("Wow, that was quick!", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Just being a good neighbor.", HAPPY);
								player.getDialogue().endDialogue();
								player.getInventory().addItem(new Item(CRONE_AMULET));
								player.setQuestStage(25, 10);
								return true;
						}
						return false;
					case TALK_TO_CRONE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I'm here about the farmers east of here.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Alice and her husband are having trouble talking to", "one another and said you might be able to help.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Ah, I know them; shame about those cows. Why would", "they think that I could help?", CONTENT);
								return true;
							case 4:
								if (QuestHandler.questCompleted(player, 24)) {
									player.getDialogue().sendPlayerChat("Uhh, because you enchanted my ghostspeak amulet", "not too long ago to help defeat Necrovarus.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Alice seems to think you could alter a ghostspeak amulet", "in order to allow them to communicate.", CONTENT);
									return true;
								}
							case 5:
								player.getDialogue().sendNpcChat("Hmm, well, the poor young lady has such family problems;", "I quite feel her pain. I'd be happy to help.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("You seem to have one of her golden hairs on your", "shoulder, so I can use that...", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendStatement("In a flash, the crone whisks away an unsee hair from", "your shoulder.");
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Talk to me again with a ghostspeak amulet and some", "space in your backpack and I'll be ready to work", "on this little good deed. The way I plan is quite simple,", "really.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("I can mirror part of the unused mystical essence of the", "ghostpeak amulet, bind it with Alice's hair and thus", "create a second amulet.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("The second amulet will be useful for the purpose you", "desire, though it won't work for any other ghost or", "human other than the farmer and his wife.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, 9);
								return true;
						}
						return false;
				}
				return false;
			case ALICES_HUSBAND:
				switch (player.getQuestStage(25)) {
					case BUY_SOME_COCK:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getEquipment().getId(Constants.AMULET) == GHOSTSPEAK_AMULET) {
									player.getDialogue().sendNpcChat("I gotchur chickens right here.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("OOOoOooOOoooooOOOOooooOOOo!", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("So, I can finally buy a chicken?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Oh, yes. I can hand over a chicken if you give me", "10 of them ecto-token thingies per bird.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendOption("Can I buy 1 chicken?", "Can I buy 2 chickens?");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(ECTOTOKEN, 10)) {
											player.getDialogue().sendNpcChat("Great! I'm laying away me tokens for some killer cows.", "That'll learn them bone rustlers.", CONTENT);
											player.getDialogue().endDialogue();
											player.getInventory().replaceItemWithItem(new Item(ECTOTOKEN, 10), new Item(UNDEAD_CHICKEN));
											return true;
										} else {
											player.getDialogue().sendPlayerChat("Oh, wait. I don't have the Ecto-tokens.", SAD);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										if (player.getInventory().playerHasItem(ECTOTOKEN, 20)) {
											player.getDialogue().sendNpcChat("Great! I'm laying away me tokens for some killer cows.", "That'll learn them bone rustlers.", CONTENT);
											player.getDialogue().endDialogue();
											player.getInventory().replaceItemWithItem(new Item(ECTOTOKEN, 20), new Item(UNDEAD_CHICKEN));
											player.getInventory().addItemOrDrop(new Item(UNDEAD_CHICKEN));
											return true;
										} else {
											player.getDialogue().sendPlayerChat("Oh, wait. I don't have the Ecto-tokens.", SAD);
											player.getDialogue().endDialogue();
											return true;
										}
								}
							case 20:
								player.getDialogue().sendStatement("You can't understand anything this ghost is saying.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case CHASE_SOME_COCK:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getEquipment().getId(Constants.AMULET) == GHOSTSPEAK_AMULET) {
									player.getDialogue().sendNpcChat("Ahhh, many thanks. Now what was it you were wanting", "again?", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("OOOoOooOOoooooOOOOooooOOOo!", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("I need a couple of your chickens.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Chickens are tricksy, 'specially dead 'uns. I'll have to catch", "'em for ye.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("They look pretty pathetic, how hard can it be?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Stand back while I catches 'em, ya city slicker.", CONTENT);
								return true;
							case 6:
								player.setStopPacket(true);
								player.getActionSender().sendInterface(BLACK_INTERFACE_TEXT);
								player.getActionSender().sendString("You hear some loud squawking noises...", STRING_ON_BLACK);
								player.getDialogue().dontCloseInterface();
								final Player thisPlayer = player;
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										thisPlayer.getActionSender().sendInterface(BLACK_INTERFACE_TEXT);
										thisPlayer.getActionSender().sendString("It seems Alice's husband caught the chickens.", STRING_ON_BLACK);
										thisPlayer.setQuestStage(25, 12);
										thisPlayer.setStopPacket(false);
										CycleEventHandler.getInstance().addEvent(thisPlayer, new CycleEvent() {
											@Override
											public void execute(CycleEventContainer b) {
												b.stop();
											}

											@Override
											public void stop() {
												Dialogues.startDialogue(thisPlayer, ALICES_HUSBAND);
											}
										}, 5);
									}
								}, 5);
								player.getDialogue().endDialogue();
								return true;
							case 20:
								player.getDialogue().sendStatement("You can't understand anything this ghost is saying.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case AMULET_TO_HUSBAND:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getEquipment().getId(Constants.AMULET) == GHOSTSPEAK_AMULET) {
									player.getDialogue().sendPlayerChat("I talked to your wife and thought that if you had a", "special amulet, you could speak to her and sort out the", "bank situation without me being involved.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("OOOoOooOOoooooOOOOooooOOOo!", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Arr, that makes far more sense than I was expecting", "from a muscle-head like you. My wife's a clever one.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Well... Never mind. I just want those chickens.", ANNOYED);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Give me that amulet then, and we'll see about", "your unnatural desire for chickens.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Okay, you need it more than I do, I suppose.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Ta, mate.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Lucky we had such a brilliant idea.", CONTENT);
								return true;
							case 8:
								if (player.getInventory().playerHasItem(CRONE_AMULET)) {
									player.getDialogue().sendGiveItemNpc("You hand the Crone-made amulet to Alice's husband.", new Item(CRONE_AMULET));
									player.getDialogue().setNextChatId(1);
									player.getInventory().removeItem(new Item(CRONE_AMULET));
									player.setQuestStage(25, 11);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Oh, I don't seem to have the amulet in my backpack...", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 20:
								player.getDialogue().sendStatement("You can't understand anything this ghost is saying.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case BACK_TO_HUSBAND_AGAIN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getEquipment().getId(Constants.AMULET) == GHOSTSPEAK_AMULET) {
									player.getDialogue().sendPlayerChat("You may not believe me, but she wants me to", "get your bank pin now.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("OOOoOooOOoooooOOOOooooOOOo!", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Maybe she said that, maybe she didn't. I think you're", "just after me savings. Tell 'er no one but a fool", "gives away their pin numbers.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Go tell 'er now, if you're not a double-dealin' scammer,", "that is.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, 7);
								return true;
							case 20:
								player.getDialogue().sendStatement("You can't understand anything this ghost is saying.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case BACK_TO_HUSBAND:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getEquipment().getId(Constants.AMULET) == GHOSTSPEAK_AMULET) {
									player.getDialogue().sendPlayerChat("Your wife says she needs the family cash and", "wants to know what you did with it.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("OOOoOooOOoooooOOOOooooOOOo!", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Tell 'er I spent it on cheap booze, har har.", LAUGHING);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Your sense of humor died too, it seems...", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Hah, just trying to lift your spirits.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("I rest my case.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Suit yerself, stick-in-the-mud. Anyways, Oim not one o'", "them yokels. Tell 'er I putted the cash in the bank like", "she always told me to.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("A warning to ya, too: annoy her and I'll haunt ya til", "yer hair turns white.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, 5);
								return true;
							case 20:
								player.getDialogue().sendStatement("You can't understand anything this ghost is saying.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case TALK_TO_HUSBAND:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getEquipment().getId(Constants.AMULET) == GHOSTSPEAK_AMULET) {
									player.getDialogue().sendPlayerChat("Your animals don't look too healthy.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("OOOoOooOOoooooOOOOooooOOOo!", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("It's that fountain thingy in the temple to the east. It's", "turned them all into zombies.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("What use are zombie animals?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("None at all, mate, except that those worshippers at that", "temple keep comin' and killen' 'em all for their bones.", "Don't ask me why.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("But you're a ghost - surely you know", "something about it.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("I don't know nuthin' about nuthin'. Oim a simple ghost", "with simple needs. All I know is, years ago, that temple", "started glowin' green and, a few months later, I woke", "up dead. That's all there is to it.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("I do miss the wife though; tell 'er I still loves her.", SAD);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Would I be able to buy some of your chickens?", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Talk to my wife and I'll think about it.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, 3);
								return true;
							case 20:
								player.getDialogue().sendStatement("You can't understand anything this ghost is saying.");
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case ALICE:
				switch (player.getQuestStage(25)) {
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello. How can I help you?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Some spooky animals you have around here.", SAD);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("That they are.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case BACK_TO_ALICE_TWICE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello. How can I help you?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm here about that quest... still.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("He says he won't trust me with the bank pin.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("What if I gave some sort of altered ghostspeak", "amulet to him - surely that would work?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("You're so clever - I've overhead passing adventurers", "say there's some witch near here who changes", "ghostspeak amulets.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("I think she lives a bit west of that mad Professor", "Fenk-something's castle, past the Farming patch.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("I'll see if I can find her. Big nose and a huge hat,", "I assume? I wonder where the beautiful young", "witches hide...", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Mysterious indeed, but in this case she actually looks", "pretty normal.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, 8);
								return true;
						}
						return false;
					case BACK_TO_ALICE_AGAIN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello. How can I help you?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm here about that quest.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Your husband says he put the cash in the bank.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("I'll need his bank pin, in that case.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Can't you just take a ghostspeak amulet? Then you", "could talk to him directly.", ANNOYED);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("I tried that once, but all those other ghosts - and even", "the undead chickens and cows, scared me so much. I", "wouldn't try it again for all the cash in Varrock bank.", SAD);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, 6);
								return true;
						}
						return false;
					case BACK_TO_ALICE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello. How can I help you?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm here about a quest.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I have a message from your husband. He wants you", "to know that he still loves you, despite his ghostly state.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("The curse of undeath was so cruel; all the men out", "here succumbed, but Lyra and I were left alive.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Ever since that day, I've not been able to speak to him.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Tell him I love him but I can't find our savings. I", "know he had our collection of gold and 'prize cow'", "rosettes just before the curse struck.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("I'll have a word with him then. Magic has it's uses", " I suppose.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, 4);
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hello. How can I help you?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm here about a quest.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I am after one of your, er, unhealthier poultry.", "Could you help me?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("You need those useless, undead chickens? How odd", "you adventurers are.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("You need to talk to my husband, though - not that I", "can these days.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Why would that be?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Can't you see, he is dead. I can't talk to the dead.", SAD);
								player.setQuestStage(25, 2);
								return true;
						}
						return false;
				}
			case AVA:
				switch (player.getQuestStage(25)) {
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("You weren't too bad of an assistant there", "partner. Thank you again for the help.", CONTENT);
								return true;
							case 2:
								if (!player.getInventory().ownsItem(AVAS_ATTRACTOR) && player.getSkill().getLevel()[Skill.RANGED] < 50) {
									player.getDialogue().sendPlayerChat("I seem to have lost my backpack.", SAD);
									return true;
								} else if (!player.getInventory().ownsItem(AVAS_ATTRACTOR) && !player.getInventory().ownsItem(AVAS_ACCUMULATOR) && player.getSkill().getLevel()[Skill.RANGED] >= 50) {
									player.getDialogue().sendPlayerChat("I seem to have lost my backpack.", SAD);
									player.getDialogue().setNextChatId(10);
									return true;
								} else if (!player.getInventory().ownsItem(AVAS_ACCUMULATOR) && player.getSkill().getLevel()[Skill.RANGED] >= 50 && player.getInventory().ownsItem(AVAS_ATTRACTOR)) {
									player.getDialogue().sendPlayerChat("I've grown levels in range, I need a better", "backpack.", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Just happy to help", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("That's fine, I can make you another.", "It'll cost you 75 steel arrows and 999 gold.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendOption("Sounds reasonable.", "No, thank you.");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(886, 75) && player.getInventory().playerHasItem(995, 999)) {
											player.getDialogue().sendPlayerChat("Sounds reasonable, I've got the items here.", CONTENT);
											return true;
										} else {
											player.getDialogue().sendPlayerChat("Sounds reasonable, I'll have to fetch", "the items.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 6:
								player.getDialogue().sendGiveItemNpc("You hand Ava the payment.", "And she hands you a new backpack.", new Item(995), new Item(AVAS_ATTRACTOR));
								player.getDialogue().endDialogue();
								player.getInventory().removeItem(new Item(995, 999));
								player.getInventory().removeItem(new Item(886, 75));
								player.getInventory().addItem(new Item(AVAS_ATTRACTOR));
								return true;
							case 10:
								player.getDialogue().sendNpcChat("That's fine, I can make you another.", "It'll cost you 75 steel arrows and 999 gold.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendOption("Sounds reasonable.", "No, thank you.");
								return true;
							case 12:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(886, 75) && player.getInventory().playerHasItem(995, 999)) {
											player.getDialogue().sendPlayerChat("Sounds reasonable, I've got the items here.", CONTENT);
											return true;
										} else {
											player.getDialogue().sendPlayerChat("Sounds reasonable, I'll have to fetch", "the items.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 13:
								player.getDialogue().sendGiveItemNpc("You hand Ava the payment.", "And she hands you a new backpack.", new Item(995), new Item(AVAS_ACCUMULATOR));
								player.getDialogue().endDialogue();
								player.getInventory().removeItem(new Item(995, 999));
								player.getInventory().removeItem(new Item(886, 75));
								player.getInventory().addItem(new Item(AVAS_ACCUMULATOR));
								return true;
							case 20:
								player.getDialogue().sendNpcChat("Hmm, I can probably upgrade your old backpack.", "It'll cost you 75 steel arrows and 999 gold.", "And the old backpack of course.", CONTENT);
								return true;
							case 21:
								player.getDialogue().sendOption("Sounds reasonable.", "No, thank you.");
								return true;
							case 22:
								switch (optionId) {
									case 1:
										if (player.getInventory().playerHasItem(886, 75) && player.getInventory().playerHasItem(995, 999) && player.getInventory().playerHasItem(AVAS_ATTRACTOR)) {
											player.getDialogue().sendPlayerChat("Sounds reasonable, I've got the items here.", CONTENT);
											return true;
										} else {
											player.getDialogue().sendPlayerChat("Sounds reasonable, I'll have to fetch", "the items.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 23:
								player.getDialogue().sendGiveItemNpc("Ava's swaps your backpack and payment for a new pack.", new Item(AVAS_ACCUMULATOR));
								player.getDialogue().endDialogue();
								player.getInventory().removeItem(new Item(995, 999));
								player.getInventory().removeItem(new Item(886, 75));
								player.getInventory().removeItem(new Item(AVAS_ATTRACTOR));
								player.getInventory().addItem(new Item(AVAS_ACCUMULATOR));
								return true;
						}
						return false;
					case CRAFT_CONTAINER:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(CONTAINER)) {
									player.getDialogue().sendNpcChat("Wow, great, now the arrow manufacturer is ready for", "use! Talk to me if you need more", "information later!", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Did you make the container?", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getInventory().replaceItemWithItem(new Item(CONTAINER), new Item(player.getSkill().getLevel()[Skill.RANGED] < 50 ? AVAS_ATTRACTOR : AVAS_ACCUMULATOR));
								player.setQuestStage(25, QUEST_COMPLETE);
								QuestHandler.completeQuest(player, 25);
								player.getDialogue().dontCloseInterface();
								return true;
							case 20:
								player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case NOTES:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(TRANSLATED_NOTES)) {
									player.getDialogue().sendPlayerChat("I've translated these notes. See? I'm not just a", "thuggish moron like you seem to think.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Did you translate those notes?", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("For all I know, it was pure luck, so don't jump to any", "conclusions about your mighty intellect.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I can see why you don't have any assitants...", "you're not exactly easy to work with.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Let's get back to the work we're doing, then.", "Remember, this is all a favor to you - I could have", "just decided to fob you off with a feather duster.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I've given you a pattern for the container; you'll need", "to combine them with some polished buttons and hard", "leather. Then we're almost done. Good news, eh?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("If you are having trouble finding buttons, I've heard", "rumors that the H.A.M society have quite the affinity", "for buttons.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Really? How would you know this?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("I hear they lose their clothes a lot to thieves so they", "have to make do with shoddy goods. Whatever the", "reason, they seem to carry buttons about in their", "pockets.", CONTENT);
								player.getDialogue().endDialogue();
								player.getInventory().replaceItemWithItem(new Item(TRANSLATED_NOTES), new Item(PATTERN));
								player.setQuestStage(25, CRAFT_CONTAINER);
								return true;
							case 20:
								player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case CHOP_TREE_PART_DUEX:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(UNDEAD_TWIGS)) {
									player.getDialogue().sendPlayerChat("I have that undead wood at last. Well, twigs anyways.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Did you get those undead twigs yet?", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("You certainly took your time.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I'd say they didn't grow on trees, but I guess you'd", "just be sarcastic about my sense of humor.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Quite. Now that we have all the ingredients for infinite", "arrows, we just need a container in which we can keep", "the components in the correct mutual alignment.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I've gathered together some research notes from various", "sources but I can't quite make out what they mean. If", "you want to have a go at making them out, here they are.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendGiveItemNpc("Ava hands you some research notes...", "In exchange for the undead twig.", new Item(RESEARCH_NOTES), new Item(UNDEAD_TWIGS));
								player.getDialogue().endDialogue();
								player.getInventory().replaceItemWithItem(new Item(UNDEAD_TWIGS), new Item(RESEARCH_NOTES));
								player.setQuestStage(25, NOTES);
								return true;
							case 20:
								player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case TELL_AVA_ABOUT_TREE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Well, I tried to hack the tree with my axe, but,", "it just bounced off the trunk! It did seem too,", "convenient to work on the first try.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Fortunately for you, I've done some research and it", "seems to suggest that there are two choices", "open to you.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Tell me the worst.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("The first is more interesting. We cut off your arms,", "have them reanimated as undead, re-attach them and", "then you should be able to cut the trees normally.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Of couse, you won't be able to pick your nose any", "more, so I suppose you'll want to try the second option.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("I'm not exactly addicted to picking my nose,", "but I do think I'll pass on that method.", ANNOYED);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Well, in that case, I think it may have something to do", "with Slayer abilities. After all, I did see Turael poking", "around the trees while I was moving in.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("As he's not known for his random touristic activities,", "you should try chatting with Turael. He's the", "Slayer Master near Burthorpe.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Oh dear, I hope he doesn't want me to buy one of his", "ridiculous fashion accessories. Those earmuffs he", "sells make heroic adventurers into laughing stock.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, TALK_TO_TURAEL);
								return true;
						}
						return false;
					case HAMMER_BAR:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(BAR_MAGNET)) {
									player.getDialogue().sendPlayerChat("I've manufactured the magnet, here it is.", HAPPY);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Get that magnet yet?", CONTENT);
									player.getDialogue().setNextChatId(20);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Great stuff! With the Witch's influence within the", "magnet, the undead chicken can use this, I'm sure.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("The plan is that the chicken will operate the magnet to", "attract bits of iron and steel, maybe even your own", "recently fired arrows. There are plenty of totally lost", "arrowheads lying about in the field of Vscape, I bet.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("In addition, arrows which you fire should be able to be", "attracted back to your quiver by the cunning avian.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("I begin to understand your plan. We've covered", "feathers and arrowheads now, what next?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("We need a source of wood, but one which is spiritually", "active and can regenerate itself. That will save you", "some axework in the future.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Try using a woodcutting axe on the pesky trees in the", "garden here, the ones that attack rather than the really", "dead ones. They are probably just the sort of thing we", "could use.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("They will try to kill me, though, and I can't fight back!", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Now you know how those poor guards feel when you", "hide behind mushrooms and fences and attack them", "from afar! Anyway, you'll need to try a mithril or", "better axe on the trees. At least the trees are pretty close.", CONTENT);
								player.getInventory().removeItem(new Item(BAR_MAGNET));
								player.getDialogue().endDialogue();
								player.setQuestStage(25, 16);
								return true;
							case 20:
								player.getDialogue().sendPlayerChat("I'm afraid not...", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case BUY_SOME_COCK:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("My spiritometric devices show that you have been", "in close contact with ghostly animals. Are we closer to", "success?", HAPPY);
								return true;
							case 2:
								if (player.getInventory().playerHasItem(UNDEAD_CHICKEN, 2)) {
									player.getDialogue().sendPlayerChat("Here they are.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I'm sorry, I don't have the chickens...", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("Amazing! Success! I can look forward to some good", "night sleep after all.", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Can I ask exactly how an undead chicken will", "help you sleep?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Well, I need the feathers to make my bed more", "comfortable. A comfortable bed will help me sleep.", "Obvious, really.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Obviously, yes, but why the hell would you need", "an undead chicken when there are perfectly good live", "chickens just down the road?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Well, for a start, undead feathers are much cleaner", "than living ones; no dust mites or anything. Secondly, I", "always think of Ernest when I see a chicken, so my", "nerves can't take killing them.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Then why do I need a chicken for my reward, we", "already established that I don't use a bed?", ANNOYED);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Seeing as how you ranger types use so many feathers", "in your arrows, I was thinking I could harness an", "undead chicken to make an un-ending supply of arrow", "flights for you.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("Beats chicken slaying or hanging around in fishing", "shops, I suppose. So, what next?", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("We'll need a magnet next, one with purely natural", "fields and made from a carefully selected iron bar. A", "firm impact when the iron is parallel to Vscape's", "field will stabilize this field in the rod.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Go and talk to the Witch next door for help", "with such a magnet.", "I'll also take my chicken now, thank you.", CONTENT);
								player.getDialogue().endDialogue();
								player.getInventory().removeItem(new Item(UNDEAD_CHICKEN));
								player.getInventory().removeItem(new Item(UNDEAD_CHICKEN));
								player.setQuestStage(25, 13);
								return true;
						}
						return false;
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!QuestHandler.questCompleted(player, 22)) {
									player.getDialogue().sendStatement("You must complete Ernest the Chicken before talking to Ava.");
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendNpcChat("Hello there, and welcome to my humble abode. It's sadly", "rather more humble than I'd like, to be honest, although", "perhaps you can help with that?", CONTENT);
									return true;
								}
							case 2:
								player.getDialogue().sendOption("I would be happy to make your home a better place.", "I'm not much into interior design, to tell the truth.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I would be happy to make your home a better place.", HAPPY);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("I'm not much into interior design, sorry.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("Yay, I didn't even have to talk about a reward; you're", "more gullible than most adventurers, that's for sure.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Don't worry, though; I just need you to help fix this", "vile old bed for me. Then I'll find a suitable reward for", "you.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Great, will I be able to take a nap in it?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Don't be silly; everyone knows that true warriors don't", "ever sleep... or perform many other bodily functions, for", "that matter. I'll come up with something, though.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("I'm not convinced by just a vague something; can", "you be a slight bit more inspiring in your offer?", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("What I need is simple: a couple of undead chickens.", "You should be able to pick some up at the farm near", "Port Phasmatys.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("I'll use one for my bed, then see what I can make", "from the other in the way of a reward. I have some", "ideas involving infinite feathers...", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("Very well then, I shall await my mystery prize", "with bated breath.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(25, 1);
								QuestHandler.startQuest(player, 25);
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
