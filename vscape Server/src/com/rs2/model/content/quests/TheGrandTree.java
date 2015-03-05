package com.rs2.model.content.quests;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class TheGrandTree implements Quest {

	//Quest stages

	public static final int QUEST_STARTED = 1;
	public static final int TALK_TO_HAZELMERE = 2;
	public static final int BACK_TO_NARNODE = 3;
	public static final int TALK_TO_GLOUGH = 4;
	public static final int BACK_TO_NARNODE_2 = 5;
	public static final int TALK_TO_PRISONER = 6;
	public static final int SEARCH_GLOUGHS_HOME = 7;
	public static final int JOURNAL_GET = 8;
	public static final int IN_JAIL = 9;
	public static final int ESCAPE_FROM_ALCATRAZ = 10;
	public static final int FIND_GLOUGHS_KEY = 11;
	public static final int KEY_GET = 12;
	public static final int INVASION_PLANS_FOUND = 13;
	public static final int TWIGS_GET = 14;
	public static final int TRAPDOOR_OPEN = 15;
	public static final int WARN_NARNODE = 16;
	public static final int HIDDEN_DACONIA_ROCK = 17;
	public static final int QUEST_COMPLETE = 18;
	//Items
	public static final int BARK_SAMPLE = 783;
	public static final int TRANSLATION_BOOK = 784;
	public static final int GLOUGHS_JOURNAL = 785;
	public static final int HAZELMERES_SCROLL = 786;
	public static final int LUMBER_ORDER = 787;
	public static final int GLOUGHS_KEY = 788;
	public static final int TWIGS_T = 789;
	public static final int TWIGS_U = 790;
	public static final int TWIGS_Z = 791;
	public static final int TWIGS_O = 792;
	public static final int DACONIA_ROCK = 793;
	public static final int INVASION_PLANS = 794;
	//Positions
	public static final Position UNDER_GRAND_TREE = new Position(2464, 9897, 0);
	//Interfaces
	public static final int SCROLL_INTERFACE = 6965;
	//Npcs
	public static final int HAZELMERE = 669;
	public static final int KING_NARNODE_SHAREEN = 670;
	public static final int GLOUGH = 671;
	public static final int ANITA = 672;
	public static final int CHARLIE = 673;
	public static final int FOREMAN = 674;
	public static final int SHIPYARD_WORKER = 675;
	public static final int FEMI = 676;
	public static final int BLACK_DEMON = 677;
	public static final int PORTOBELLO = 4593;
	public static final Npc glough = new Npc(GLOUGH);
	//Objects
	public static final int TREE_TRAPDOOR = 2446;
	public static final int CUPBOARD = 2434;
	public static final int SHIPYARD_GATE = 2439;
	public static final int SHIPYARD_GATE_2 = 2438;
	public static final int ANITA_STAIRS_UP = 1742;
	public static final int ANITA_STAIRS_DOWN = 1744;
	public static final int GLOUGHS_CHEST = 2436;
	public static final int GLOUGHS_TREE_UP = 2447;
	public static final int GLOUGHS_TREE_DOWN = 2448;
	public static final int T_PILLAR = 2440;
	public static final int U_PILLAR = 2441;
	public static final int Z_PILLAR = 2442;
	public static final int O_PILLAR = 2443;
	public static final int GLOUGHS_TRAPDOOR = 2444;
	public static final int ROOTS_BIG = 1985;
	public static final int ROOTS_SMALL = 1986;
	public static final int MINE_ROOTS = 2451;

	public static final int PLACE_ANIM = 832;

	public int dialogueStage = 0; //Ignore

	private int reward[][] = { //Items in the form of {Id, #},
	};
	private int expReward[][] = { //Exp in the form of {Skill.AGILITY, x},
		{Skill.AGILITY, 7900},
		{Skill.ATTACK, 18400},
		{Skill.MAGIC, 2150},}; //The 2.25 multiplier is added later, use vanilla values

	private static final int questPointReward = 5;

	public int getQuestID() { //Don't change
		return 29;
	}

	public String getQuestName() { //Don't change
		return "The Grand Tree";
	}

	public String getQuestSaveName() { //Don't change
		return "grandtree";
	}

	public boolean canDoQuest(Player player) { //Use to check for strict auxiliary quest requirements
		return true;
	}

	public void getReward(Player player) { //Don't change
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1]));
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	//End of quest reward scroll interface, tweak what's necessary
	public void completeQuest(Player player) {
		//If writing in exp, be sure to express it manually as 2.25 the vanilla reward
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("5 Quest Points", 12150);
		player.getActionSender().sendString("17775 Agility XP", 12151);
		player.getActionSender().sendString("41400 Attack XP", 12152);
		player.getActionSender().sendString("4837.5 Magic XP", 12153);
		player.getActionSender().sendString("Access to the Gnome Glider.", 12154);
		player.getActionSender().sendString("Access to the Grand Tree mine.", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		//Updates the players quest list to show the green complete quest
		player.getActionSender().sendString("@gre@" + getQuestName(), 7361);
	}
    //Here we send the quest log, with the text and then the line number in sendString(string, line number)
	//The line number is according to the interface, just add to it for the next line

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case QUEST_STARTED:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);

				player.getActionSender().sendString("King Narnode Shareen needs my help. I should find out", 8149);
				player.getActionSender().sendString("how I can help save the Grand Tree.", 8150);
				break;
			case TALK_TO_HAZELMERE:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);

				player.getActionSender().sendString("King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				break;
			case BACK_TO_NARNODE:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);

				player.getActionSender().sendString("I found Hazelmere and had him write down what he was", 8152);
				player.getActionSender().sendString("saying on a scroll, I should return to King Narnode.", 8153);
				break;
			case TALK_TO_GLOUGH:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);

				player.getActionSender().sendString("The Grand Tree is in immediate danger, King Narnode asked", 8154);
				player.getActionSender().sendString("me to warn Glough, the head of the Grand Tree guard.", 8155);
				player.getActionSender().sendString("Narnode told me he resides just south of the Tree.", 8156);
				break;
			case BACK_TO_NARNODE_2:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);

				player.getActionSender().sendString("I told Glough about the Daconia rocks, he seemed", 8154);
				player.getActionSender().sendString("to think humans are going to invade gnomes!", 8155);
				player.getActionSender().sendString("I need to return to King Narnode to sort this out.", 8156);
				break;
			case SEARCH_GLOUGHS_HOME:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);

				player.getActionSender().sendString("The plot thickens. The prisoner told me that Glough", 8156);
				player.getActionSender().sendString("was paying him to retrieve Daconia rocks for him. I", 8157);
				player.getActionSender().sendString("should search Glough's home for any clues as to why.", 8158);
				break;
			case JOURNAL_GET:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);

				player.getActionSender().sendString("I found Glough's journal. I should confront him", 8156);
				player.getActionSender().sendString("about his potentially treasonous actions.", 8157);
				break;
			case IN_JAIL:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);

				player.getActionSender().sendString("I found Glough's journal. I then confronted him about", 8156);
				player.getActionSender().sendString("his potentially treasonous actions and was thrown in", 8157);
				player.getActionSender().sendString("a cell by the other prisoner.", 8158);
				break;
			case ESCAPE_FROM_ALCATRAZ:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);

				player.getActionSender().sendString("Apparently I am wanted in the Grand Tree now. King", 8156);
				player.getActionSender().sendString("Narnode told me to use his glider to 'escape'.", 8157);
				player.getActionSender().sendString("I should take the glider to Karamja and investigate", 8158);
				player.getActionSender().sendString("the shipyard Charlie told me about. He said the pass-", 8159);
				player.getActionSender().sendString("code for the main gate is 'Ka-Lu-Min'.", 8160);
				break;
			case FIND_GLOUGHS_KEY:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);

				player.getActionSender().sendString("The Foreman at the shipyard had orders to build", 8156);
				player.getActionSender().sendString("a fleet of battleships! I tried to tell the King", 8157);
				player.getActionSender().sendString("but he still won't believe me. Charlie told me I", 8158);
				player.getActionSender().sendString("should try getting Glough's chest key from his", 8159);
				player.getActionSender().sendString("girlfriend Anita's house, west of the swamp.", 8160);
				break;
			case KEY_GET:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);
				player.getActionSender().sendString("@str@" + "The Foreman at the shipyard had orders to build", 8156);
				player.getActionSender().sendString("@str@" + "a fleet of battleships from Glough! I tried to", 8157);
				player.getActionSender().sendString("@str@" + "tell the King but he still won't believe me.", 8158);

				player.getActionSender().sendString("Anita gave me Glough's chest key. I should find", 8160);
				player.getActionSender().sendString("out what he is truly hiding.", 8161);
				break;
			case INVASION_PLANS_FOUND:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);
				player.getActionSender().sendString("@str@" + "The Foreman at the shipyard had orders to build", 8156);
				player.getActionSender().sendString("@str@" + "a fleet of battleships from Glough! I tried to", 8157);
				player.getActionSender().sendString("@str@" + "tell the King but he still won't believe me.", 8158);

				player.getActionSender().sendString("It's worse than I feared. Glough is planning to", 8160);
				player.getActionSender().sendString("invade all of /v/scape! I need to tell King Narnode", 8161);
				player.getActionSender().sendString("as quick as possible! He'll have to believe me!", 8162);
				break;
			case TWIGS_GET:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);
				player.getActionSender().sendString("@str@" + "The Foreman at the shipyard had orders to build", 8156);
				player.getActionSender().sendString("@str@" + "a fleet of battleships from Glough! I tried to", 8157);
				player.getActionSender().sendString("@str@" + "tell the King but he still won't believe me.", 8158);
				player.getActionSender().sendString("@str@" + "It's worse than I feared. Glough is planning to", 8160);
				player.getActionSender().sendString("@str@" + "invade all of /v/scape! I need to tell King Narnode.", 8161);

				player.getActionSender().sendString("I showed King Narnode the invasion plans, he said", 8163);
				player.getActionSender().sendString("anyone could have forged them. He then told me", 8164);
				player.getActionSender().sendString("guards searched Glough's home and only found strange", 8165);
				player.getActionSender().sendString("twigs resembling letters. Perhaps they spell something?", 8166);
				break;
			case TRAPDOOR_OPEN:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);
				player.getActionSender().sendString("@str@" + "The Foreman at the shipyard had orders to build", 8156);
				player.getActionSender().sendString("@str@" + "a fleet of battleships from Glough! I tried to", 8157);
				player.getActionSender().sendString("@str@" + "tell the King but he still won't believe me.", 8158);
				player.getActionSender().sendString("@str@" + "It's worse than I feared. Glough is planning to", 8160);
				player.getActionSender().sendString("@str@" + "invade all of /v/scape! I need to tell King Narnode.", 8161);

				player.getActionSender().sendString("I used the twigs King Narnode's guards found", 8163);
				player.getActionSender().sendString("to spell the ancient Gnome word for 'open' on", 8164);
				player.getActionSender().sendString("the pillars up above the tree at Glough's house.", 8165);
				player.getActionSender().sendString("This trapdoor that opened surely leads to the", 8166);
				player.getActionSender().sendString("the end of all this nonsense.", 8167);
				break;
			case WARN_NARNODE:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);
				player.getActionSender().sendString("@str@" + "The Foreman at the shipyard had orders to build", 8156);
				player.getActionSender().sendString("@str@" + "a fleet of battleships from Glough! I tried to", 8157);
				player.getActionSender().sendString("@str@" + "tell the King but he still won't believe me.", 8158);
				player.getActionSender().sendString("@str@" + "It's worse than I feared. Glough is planning to", 8160);
				player.getActionSender().sendString("@str@" + "invade all of /v/scape! I need to tell King Narnode.", 8161);
				player.getActionSender().sendString("@str@" + "I used the twigs King Narnode's guards to open", 8163);
				player.getActionSender().sendString("@str@" + "Glough's trapdoor... surely it leads to the", 8166);
				player.getActionSender().sendString("@str@" + "the end of all this nonsense.", 8167);

				player.getActionSender().sendString("Glough's trapdoor led underneath the Grand Tree,", 8169);
				player.getActionSender().sendString("to a deposit of Daconia rocks. He then attacked", 8170);
				player.getActionSender().sendString("me with a pet Black Demon, I barely survived.", 8171);
				player.getActionSender().sendString("I should follow this tunnel and tell King Narnode.", 8172);
				break;
			case HIDDEN_DACONIA_ROCK:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);
				player.getActionSender().sendString("@str@" + "The Foreman at the shipyard had orders to build", 8156);
				player.getActionSender().sendString("@str@" + "a fleet of battleships from Glough! I tried to", 8157);
				player.getActionSender().sendString("@str@" + "tell the King but he still won't believe me.", 8158);
				player.getActionSender().sendString("@str@" + "It's worse than I feared. Glough is planning to", 8160);
				player.getActionSender().sendString("@str@" + "invade all of /v/scape! I need to tell King Narnode.", 8161);
				player.getActionSender().sendString("@str@" + "I used the twigs King Narnode's guards to open", 8163);
				player.getActionSender().sendString("@str@" + "Glough's trapdoor... surely it leads to the", 8166);
				player.getActionSender().sendString("@str@" + "the end of all this nonsense.", 8167);
				player.getActionSender().sendString("@str@" + "Glough's trapdoor led to a store of Daconia.", 8169);

				player.getActionSender().sendString("King Narnode finally believes me about Glough.", 8171);
				player.getActionSender().sendString("I need to find the last Daconia rock hidden", 8172);
				player.getActionSender().sendString("amongst these Grand Tree roots.", 8173);
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
				player.getActionSender().sendString("@str@" + "King Narnode told me to take a bark sample from the", 8149);
				player.getActionSender().sendString("@str@" + "Grand Tree to Hazelmere, who lives east of Yanille.", 8150);
				player.getActionSender().sendString("@str@" + "I found Hazelmere and got his scroll of warning.", 8152);
				player.getActionSender().sendString("@str@" + "I told Glough about the Daconia rocks.", 8154);
				player.getActionSender().sendString("@str@" + "The Foreman at the shipyard had orders to build", 8156);
				player.getActionSender().sendString("@str@" + "a fleet of battleships from Glough! I tried to", 8157);
				player.getActionSender().sendString("@str@" + "tell the King but he still won't believe me.", 8158);
				player.getActionSender().sendString("@str@" + "It's worse than I feared. Glough is planning to", 8160);
				player.getActionSender().sendString("@str@" + "invade all of /v/scape! I need to tell King Narnode.", 8161);
				player.getActionSender().sendString("@str@" + "I used the twigs King Narnode's guards to open", 8163);
				player.getActionSender().sendString("@str@" + "Glough's trapdoor... surely it leads to the", 8166);
				player.getActionSender().sendString("@str@" + "the end of all this nonsense.", 8167);
				player.getActionSender().sendString("@str@" + "Glough's trapdoor led to a store of Daconia.", 8169);
				player.getActionSender().sendString("@str@" + "King Narnode finally believed me about Glough.", 8171);
				player.getActionSender().sendString("@str@" + "I then found the last hidden Daconia rock.", 8173);

				player.getActionSender().sendString("@red@" + "You have completed this quest!", 8175);
				break;
			default:
				player.getActionSender().sendString("Talk to @dre@King Narnode Shareen @bla@in the @dre@Grand Tree @bla@ to begin.", 8147);
				player.getActionSender().sendString("@dre@Requirements:", 8148);
				if (player.getSkill().getLevel()[Skill.AGILITY] < 25) {
					player.getActionSender().sendString("-25 Agility.", 8149);
				} else {
					player.getActionSender().sendString("@str@-25 Agility.", 8149);
				}
				player.getActionSender().sendString("@dre@-I must be able to defeat a level 172 demon.", 8150);
				break;
		}
	}

	public void sendQuestInterface(Player player) { //Don't change
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) { //Don't change
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7361);
	}

	public boolean questCompleted(Player player) //Don't change
	{
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) { //Don't change
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), 7361); //These numbers correspond to the index of the quest in
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7361); //the quest list on the quest tab. I've listed them all
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7361); //in the player class, just search and then replace
			//also add the name while you're there @red@Quest Name
		}
	}

	public int getQuestPoints() { //Don't change
		return questPointReward;
	}

	public void showInterface(Player player) { //Don't change
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public void handleDeath(final Player player, final Npc npc) {
		if (npc.getNpcId() == BLACK_DEMON && player.getQuestStage(29) == TRAPDOOR_OPEN) {
			player.getDialogue().sendPlayerChat("Phew! That was close! I need to tell the King", "about this right away. It looks as if this tunnel is", "a part of the cave underneath the Grand Tree...", CONTENT);
			player.getDialogue().endDialogue();
			player.setQuestStage(29, WARN_NARNODE);
		}
		if (npc.getNpcId() == FOREMAN && player.getQuestStage(29) == ESCAPE_FROM_ALCATRAZ) {
			GroundItem drop = new GroundItem(new Item(LUMBER_ORDER), player, npc.getPosition().clone());
			GroundItemManager.getManager().dropItem(drop);
			player.getActionSender().sendMessage("The foreman drops a piece of paper as he dies.");
		}
	}

	public static boolean placedAllTwigs(final Player player) {
		return player.getQuestVars().hasPlacedTTwig() && player.getQuestVars().hasPlacedUTwig() && player.getQuestVars().hasPlacedZTwig() && player.getQuestVars().hasPlacedOTwig();
	}

	public static boolean itemPickupHandling(final Player player, final int id) {
		if ((id == TWIGS_T || id == TWIGS_U || id == TWIGS_Z || id == TWIGS_O) && player.getInventory().getItemContainer().emptySlots() >= 1) {
			if (Misc.goodDistance(player.getPosition(), new Position(player.getClickX(), player.getClickY(), player.getClickZ()), 1)) {
				player.getUpdateFlags().sendAnimation(PLACE_ANIM);
				for (GroundItem item : player.getGroundItems()) {
					if (item != null && item.getItem().getId() == id) {
						player.getGroundItems().remove(item);
						GroundItemManager.getManager().destroyItem(item);
						player.getInventory().addItem(new Item(id));
					}
				}
				return true;
			} else {
				player.walkTo(new Position(player.getClickX(), player.getClickY(), 2), true);
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean itemHandling(final Player player, int itemId) { //Inherited, will work without a call to it
		switch (itemId) {
			case TRANSLATION_BOOK:
				player.getActionSender().sendInterface(12624);
				player.getActionSender().sendString("Gnome-English Translation, by Anita", 12666);
				player.getActionSender().sendString("@dre@Arpos: @bla@rocks @dre@Ando: @bla@gate", 12715);
				player.getActionSender().sendString("@dre@Cinqo: @bla@King", 12716);
				player.getActionSender().sendString("@dre@Eis: @bla@me @dre@Et: @bla@and @dre@Eto: @bla@will", 12717);
				player.getActionSender().sendString("@dre@Gandius: @bla@jungle", 12718);
				player.getActionSender().sendString("@dre@Hewo: @bla@grass", 12719);
				player.getActionSender().sendString("@dre@Ip: @bla@you", 12720);
				player.getActionSender().sendString("@dre@Kar: @bla@no @dre@Kai: @bla@boat", 12721);
				player.getActionSender().sendString("@dre@Lemanto: @bla@man @dre@Lovos: @bla@gave", 12722);
				player.getActionSender().sendString("@dre@Meso: @bla@came @dre@Mond: @bla@seal", 12723);
				player.getActionSender().sendString("@dre@O: @bla@for", 12724);
				player.getActionSender().sendString("@dre@Prit: @bla@with @dre@Priw: @bla@tree @dre@Pro: @bla@to", 12725);
				player.getActionSender().sendString("@dre@Qui: @bla@guard", 12726);
				player.getActionSender().sendString("@dre@Rento: @bla@agility", 12727);
				player.getActionSender().sendString("@dre@Sarkos: @bla@begone @dre@Sind: @bla@big", 12728);
				player.getActionSender().sendString("@dre@Ta: @bla@the @dre@Tuzo: @bla@open", 12729);
				player.getActionSender().sendString("@dre@Undri: @bla@lands @dre@Umesco: @bla@soul", 12730);
				for (int i = 12731; i < 12740; i++) {
					player.getActionSender().sendString("", i);
				}
				return true;
			case INVASION_PLANS:
				player.getActionSender().sendInterface(SCROLL_INTERFACE);
				player.setStatedInterface("hazelmere");
				player.getActionSender().sendString("@red@Invasion plan:", 6968);
				player.getActionSender().sendString("@yel@Troops board fleets at Karamja.", 6969);
				player.getActionSender().sendString("", 6974);
				player.getActionSender().sendString("@red@ **Take no prisoners!**", 6975);
				player.getActionSender().sendString("@dre@-Fleet 1: Attack Misthalin.", 6970);
				player.getActionSender().sendString("@dre@-Fleet 2: Attack Asgarnia.", 6971);
				player.getActionSender().sendString("@dre@-Fleet 3: Attack Kandarin from the", 6972);
				player.getActionSender().sendString("@dre@south, using gnome foot soldiers.", 6973);
				return true;
			case LUMBER_ORDER:
				player.getActionSender().sendInterface(SCROLL_INTERFACE);
				player.setStatedInterface("hazelmere");
				player.getActionSender().sendString("", 6968);
				player.getActionSender().sendString("", 6969);
				player.getActionSender().sendString("", 6974);
				player.getActionSender().sendString("", 6975);
				player.getActionSender().sendString("@dre@--Karamja Shipyard--", 6970);
				player.getActionSender().sendString("@yel@Order for 30 Karamja Battleships:", 6971);
				player.getActionSender().sendString("@yel@Lumber required: @red@2000", 6972);
				player.getActionSender().sendString("@yel@Troop capacity: @red@300", 6973);
				return true;
			case HAZELMERES_SCROLL:
				player.getActionSender().sendInterface(SCROLL_INTERFACE);
				player.setStatedInterface("hazelmere");
				player.getActionSender().sendString("", 6968);
				player.getActionSender().sendString("", 6969);
				player.getActionSender().sendString("", 6974);
				player.getActionSender().sendString("", 6975);
				player.getActionSender().sendString("@yel@Es lemanto meso pro eis prit ta", 6970);
				player.getActionSender().sendString("@yel@Cinqo mond. Mi lovos ta lemanto", 6971);
				player.getActionSender().sendString("@yel@Daconia arpos et Daconia arpos", 6972);
				player.getActionSender().sendString("@yel@eto meriz ta priw!", 6973);
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) { //Inherited, will work without a call to it
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) { //Inherited, will work without a call to it
		switch (object) {
			case T_PILLAR:
				switch (item) {
					case TWIGS_T:
						player.getActionSender().sendMessage("You place the 'T' twigs on the pillar.");
						player.getUpdateFlags().sendAnimation(PLACE_ANIM);
						player.getQuestVars().setPlacedTTwig(true);
						if (placedAllTwigs(player) && player.getQuestStage(29) == TWIGS_GET) {
							player.getActionSender().sendMessage("You hear something grind and click into place in the trapdoor.");
							player.setQuestStage(29, TRAPDOOR_OPEN);
						}
						player.getInventory().removeItem(new Item(TWIGS_T));
						GroundItemManager.getManager().dropItem(new GroundItem(new Item(TWIGS_T), player, new Position(2485, 3467, 2)));
						return true;
					case TWIGS_U:
					case TWIGS_Z:
					case TWIGS_O:
						player.getActionSender().sendMessage("You place the twigs on the pillar.");
						player.getUpdateFlags().sendAnimation(PLACE_ANIM);
						player.getInventory().removeItem(new Item(item));
						GroundItemManager.getManager().dropItem(new GroundItem(new Item(item), player, new Position(2485, 3467, 2)));
						return true;
				}
				return false;
			case U_PILLAR:
				switch (item) {
					case TWIGS_U:
						player.getActionSender().sendMessage("You place the 'U' twigs on the pillar.");
						player.getUpdateFlags().sendAnimation(PLACE_ANIM);
						player.getQuestVars().setPlacedUTwig(true);
						if (placedAllTwigs(player) && player.getQuestStage(29) == TWIGS_GET) {
							player.getActionSender().sendMessage("You hear something grind and click into place in the trapdoor.");
							player.setQuestStage(29, TRAPDOOR_OPEN);
						}
						player.getInventory().removeItem(new Item(TWIGS_U));
						GroundItemManager.getManager().dropItem(new GroundItem(new Item(TWIGS_U), player, new Position(2486, 3467, 2)));
						return true;
					case TWIGS_T:
					case TWIGS_Z:
					case TWIGS_O:
						player.getActionSender().sendMessage("You place the twigs on the pillar.");
						player.getUpdateFlags().sendAnimation(PLACE_ANIM);
						player.getInventory().removeItem(new Item(item));
						GroundItemManager.getManager().dropItem(new GroundItem(new Item(item), player, new Position(2486, 3467, 2)));
						return true;
				}
				return false;
			case Z_PILLAR:
				switch (item) {
					case TWIGS_Z:
						player.getActionSender().sendMessage("You place the 'Z' twigs on the pillar.");
						player.getUpdateFlags().sendAnimation(PLACE_ANIM);
						player.getQuestVars().setPlacedZTwig(true);
						if (placedAllTwigs(player) && player.getQuestStage(29) == TWIGS_GET) {
							player.getActionSender().sendMessage("You hear something grind and click into place in the trapdoor.");
							player.setQuestStage(29, TRAPDOOR_OPEN);
						}
						player.getInventory().removeItem(new Item(TWIGS_Z));
						GroundItemManager.getManager().dropItem(new GroundItem(new Item(TWIGS_Z), player, new Position(2487, 3467, 2)));
						return true;
					case TWIGS_T:
					case TWIGS_U:
					case TWIGS_O:
						player.getActionSender().sendMessage("You place the twigs on the pillar.");
						player.getUpdateFlags().sendAnimation(PLACE_ANIM);
						player.getInventory().removeItem(new Item(item));
						GroundItemManager.getManager().dropItem(new GroundItem(new Item(item), player, new Position(2487, 3467, 2)));
						return true;
				}
				return false;
			case O_PILLAR:
				switch (item) {
					case TWIGS_O:
						player.getActionSender().sendMessage("You place the 'O' twigs on the pillar.");
						player.getUpdateFlags().sendAnimation(PLACE_ANIM);
						player.getQuestVars().setPlacedOTwig(true);
						if (placedAllTwigs(player) && player.getQuestStage(29) == TWIGS_GET) {
							player.getActionSender().sendMessage("You hear something grind and click into place in the trapdoor.");
							player.setQuestStage(29, TRAPDOOR_OPEN);
						}
						player.getInventory().removeItem(new Item(TWIGS_O));
						GroundItemManager.getManager().dropItem(new GroundItem(new Item(TWIGS_O), player, new Position(2488, 3467, 2)));
						return true;
					case TWIGS_T:
					case TWIGS_U:
					case TWIGS_Z:
						player.getActionSender().sendMessage("You place the twigs on the pillar.");
						player.getUpdateFlags().sendAnimation(PLACE_ANIM);
						player.getInventory().removeItem(new Item(item));
						GroundItemManager.getManager().dropItem(new GroundItem(new Item(item), player, new Position(2488, 3467, 2)));
						return true;
				}
				return false;
			case GLOUGHS_CHEST:
				if (item == GLOUGHS_KEY && !player.getInventory().ownsItem(INVASION_PLANS) && player.getQuestStage(29) == KEY_GET) {
					player.getUpdateFlags().sendAnimation(PLACE_ANIM);
					player.setQuestStage(29, INVASION_PLANS_FOUND);
					player.getDialogue().sendGiveItemNpc("You quietly open the chest and find a scroll!", new Item(INVASION_PLANS));
					player.getInventory().addItemOrDrop(new Item(INVASION_PLANS));
				} else {
					return false;
				}
		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) { //Inherited, will work without a call to it
		switch (object) {
			case MINE_ROOTS:
				if (player.getQuestStage(29) == QUEST_COMPLETE) {
					player.getActionSender().sendMessage("You push through the roots.");
					player.getActionSender().walkTo(0, player.getPosition().getY() > 9904 ? -2 : 2, true);
					return true;
				} else {
					player.getDialogue().sendStatement("You push on the roots...", "...the roots seem to push right back against you.");
					return true;
				}
			case ROOTS_SMALL:
			case ROOTS_BIG:
				player.getActionSender().sendMessage("You search the roots...");
				player.getUpdateFlags().sendAnimation(PLACE_ANIM);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						b.stop();
					}

					@Override
					public void stop() {
						if (player.getQuestStage(29) == HIDDEN_DACONIA_ROCK && Entity.inArea(player, 2480, 2485, 9903, 9907) && !player.getInventory().ownsItem(DACONIA_ROCK)) {
							player.getDialogue().sendGiveItemNpc("You've found a Daconia rock!", new Item(DACONIA_ROCK));
							player.getDialogue().endDialogue();
							player.getInventory().addItemOrDrop(new Item(DACONIA_ROCK));
						} else {
							player.getActionSender().sendMessage("...you find nothing but dirt.");
						}
					}
				}, 3);
				return true;
			case GLOUGHS_TRAPDOOR:
				if (player.getQuestStage(29) == TRAPDOOR_OPEN) {
					player.getActionSender().sendMessage("You carefully open and climb down the trapdoor...");
					player.fadeTeleport(new Position(2491, 9864, 0));
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, glough, new Position(2478, 9864, 0), false, null);
							glough.walkTo(new Position(2490, 9864, 0), true);
							glough.setFollowingEntity(player);
							Dialogues.startDialogue(player, GLOUGH);
						}
					}, 5);
					return true;
				} else {
					player.getDialogue().sendStatement("The trapdoor is mechanically locked.");
					return true;
				}
			case GLOUGHS_CHEST:
				player.getDialogue().sendStatement("The chest is locked.");
				player.getDialogue().endDialogue();
				return true;
			case GLOUGHS_TREE_DOWN:
				player.getActionSender().sendMessage("You quietly climb down the tree while Glough isn't looking.");
				Ladders.climbLadder(player, new Position(2483, 3463, 1));
				return true;
			case GLOUGHS_TREE_UP:
				if (player.getQuestStage(29) >= TWIGS_GET) {
					if (player.getSkill().getLevel()[Skill.AGILITY] >= 25) {
						player.getActionSender().sendMessage("You carefully, quickly climb up the tree while Glough isn't looking.");
						Ladders.climbLadder(player, new Position(2485, 3465, 2));
						return true;
					} else {
						player.getDialogue().sendStatement("You need 25 Agility to climb up this tree quietly.");
						return true;
					}
				} else {
					player.getDialogue().setLastNpcTalk(GLOUGH);
					player.getDialogue().sendNpcChat("Hey! What are you doing on my tree?!", ANGRY_1);
					player.getDialogue().endDialogue();
					return true;
				}
			case ANITA_STAIRS_UP:
				if (Misc.goodDistance(player.getPosition(), new Position(2389, 3512, 0), 3)) {
					Ladders.climbLadder(player, new Position(2388, 3513, 1));
					return true;
				} else {
					return false;
				}
			case ANITA_STAIRS_DOWN:
				if (Misc.goodDistance(player.getPosition(), new Position(2388, 3513, 1), 3)) {
					player.teleport(new Position(2388, 3512, 0));
					return true;
				} else {
					return false;
				}
			case SHIPYARD_GATE:
			case SHIPYARD_GATE_2:
				if (player.getQuestStage(36) > 0) {
					return false;
				}
				if (player.getQuestStage(29) < ESCAPE_FROM_ALCATRAZ) {
					player.getDialogue().setLastNpcTalk(SHIPYARD_WORKER);
					player.getDialogue().sendNpcChat("Hey! Get away from there!", ANGRY_1);
					return true;
				} else {
					if (!player.getQuestVars().getShipyardGateOpen() && player.getPosition().getX() < 2945) {
						Dialogues.startDialogue(player, SHIPYARD_WORKER);
						return true;
					} else {
						player.getActionSender().walkTo(player.getPosition().getX() < 2945 ? 1 : -1, 0, true);
						return true;
					}
				}
			case CUPBOARD:
				if (player.getPosition().getX() < 2500 && player.getQuestStage(29) == SEARCH_GLOUGHS_HOME && !player.getInventory().ownsItem(GLOUGHS_JOURNAL) && player.getGroundItems().isEmpty()) {
					player.getUpdateFlags().sendAnimation(PLACE_ANIM);
					player.getDialogue().sendGiveItemNpc("You quietly reach in the cupboard for Glough's journal.", new Item(GLOUGHS_JOURNAL));
					player.getInventory().addItemOrDrop(new Item(GLOUGHS_JOURNAL));
					player.setQuestStage(29, JOURNAL_GET);
					return true;
				} else {
					player.getDialogue().sendPlayerChat("I better not touch that...", CONTENT);
					return true;
				}
			case TREE_TRAPDOOR:
				Ladders.climbLadder(player, UNDER_GRAND_TREE);
				player.getActionSender().sendMessage("You open the trapdoor and climb down...");
				return true;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) { //Inherited
		switch (id) { //Npc ID
			case ANITA:
				switch (player.getQuestStage(29)) {
					case KEY_GET:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().ownsItem(GLOUGHS_KEY)) {
									player.getDialogue().sendPlayerChat("I lost that key...", SAD);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Oh, do hurry and get the key to Glough!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Silly Traveller, it's a good thing I have a copy.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendGiveItemNpc("Anita gives you a key.", new Item(GLOUGHS_KEY));
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(GLOUGHS_KEY));
								return true;
						}
						return false;
					case FIND_GLOUGHS_KEY:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello there.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Oh hello, I've seen you with the King.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Yes, I'm helping him with a problem.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("You must know my boyfriend Glough then?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Indeed!", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Could you do me a favor?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("I suppose so.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Please give this key to Glough, he left it here last night.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Well, that was easy.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("What?", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("Oh, nothing.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendGiveItemNpc("Anita gives you a key.", new Item(GLOUGHS_KEY));
								player.getDialogue().endDialogue();
								player.setQuestStage(29, KEY_GET);
								player.getInventory().addItemOrDrop(new Item(GLOUGHS_KEY));
								return true;
						}
						return false;
				}
				return false;
			case FOREMAN:
				switch (player.getQuestStage(29)) {
					case ESCAPE_FROM_ALCATRAZ:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("What do you want?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm here to uhh, retrieve an update on...", "Glough's project.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Alright, I'll have to go through the formalities then...", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("How is Glough's wife?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendOption("She's doing fine, she lives just east of the Grand Tree.", "She is no longer with us.", "She's is not doing well, with all the humans around.");
								return true;
							case 6:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("She's doing fine, she lives just east of the Grand Tree.", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("She is no longer with us.", SAD);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("She's is not doing well, with all the humans around.", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
								}
							case 7:
								player.getDialogue().sendNpcChat("Good. What is his new girlfriend's name?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendOption("Anita.", "He doesn't have a girlfriend, he is still mourning.", "Gloria.");
								return true;
							case 9:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Anita.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("He doesn't have a girlfriend, he is still mourning.", SAD);
										player.getDialogue().setNextChatId(25);
									case 3:
										player.getDialogue().sendPlayerChat("Gloria.", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
								}
							case 10:
								player.getDialogue().sendNpcChat("Alright, sorry for doubting you. Here's what we need.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendGiveItemNpc("The Foreman hands you a piece of paper.", new Item(LUMBER_ORDER));
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(LUMBER_ORDER));
								return true;
							case 25:
								player.getDialogue().sendNpcChat("Get out of my sight! I'm not giving you anything!", ANGRY_1);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case SHIPYARD_WORKER:
				switch (player.getQuestStage(29)) {
					case ESCAPE_FROM_ALCATRAZ:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hey you! What are you up to?", ANNOYED);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm trying to open the gate!", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("I can see that! Why?", ANNOYED);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Glough sent me.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Hmm... really? What for?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("You're wasting my time! Take me to your superior!", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Alright. Password.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendOption("Ka.", "Ko.", "Ke.");
								return true;
							case 9:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Ka.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Ko.", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("Ke.", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
								}
							case 10:
								player.getDialogue().sendOption("Lo.", "Lu.", "Le.");
								return true;
							case 11:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Lo.", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Lu.", CONTENT);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("Le.", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
								}
							case 12:
								player.getDialogue().sendOption("Mon.", "Min.", "Men,");
								return true;
							case 13:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Mon.", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Min.", CONTENT);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("Men.", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
								}
							case 14:
								player.getDialogue().sendNpcChat("Sorry to have kept you.", CONTENT);
								player.getDialogue().endDialogue();
								player.getQuestVars().setShipyardGateOpen(true);
								return true;
							case 25:
								player.getDialogue().sendNpcChat("What are you trying to pull?! That's not", "how the password goes!", ANGRY_1);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case PORTOBELLO:
				switch (player.getQuestStage(29)) {
					case ESCAPE_FROM_ALCATRAZ:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!Entity.inArea(player, 2914, 2929, 3050, 3065)) {
									player.getDialogue().sendNpcChat("Hi, the King said that you need to leave?", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("I'm sorry Traveller, I can't fly anywhere after that.", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("Apparently humans are invading!", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("I find that hard to believe! I have lots of human friends.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("I don't understand it either!", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("So, where to?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendOption("Take me to Karamja please!", "Not anywhere for now.");
								return true;
							case 7:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Take me to Karamja please!", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Not anywhere for now.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 8:
								player.getDialogue().sendNpcChat("OK! You're the boss! Hold on tight, it'll be a rough ride!", CONTENT);
								return true;
							case 9:
								player.getDialogue().endDialogue();
								player.fadeTeleport(new Position(2918, 3056, 0));
								return true;
						}
						return false;
				}
				return false;
			case CHARLIE:
				switch (player.getQuestStage(29)) {
					case ESCAPE_FROM_ALCATRAZ:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(LUMBER_ORDER)) {
									player.getDialogue().sendPlayerChat("How are you doing Charlie?", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("You heard the King, you better get out of here!", "Head to the shipyard, see what is up over there!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("I've been better.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Glough has some plan to rule Vscape!", DISTRESSED);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("I wouldn't put it past him, the Gnome's crazy!", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("I need some further proof to convince the King.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Hmmm... you could be in luck! Before Glough had me", "locked up I heard him mention that he'd left his chest", "keys at his girlfriend's.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Where does she live?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Just west of the swamp here.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Okay, I'll see what I can find.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(29, FIND_GLOUGHS_KEY);
								return true;
						}
						return false;
					case IN_JAIL:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("So they got you as well?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("It's Glough! He's trying to cover something up.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("I shouldn't tell you this adventurer...", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("But if you want to get to the bottom of this you should", "go and talk to the Karamja Shipyard foreman.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Why?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Glough sent me to Karamja to meet him. I delivered a", "large amount of gold. For what? I don't know. He may", "be able to tell you what Glough's up to. That's if you", "can get out of here. You'll find him in the", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Karamja Shipyard, east of Shilo village. Be careful! If he", "discovers you're not working for Glough there'll be", "trouble! The sea men use the password 'Ka-Lu-Min'.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Thanks Charlie!", CONTENT);
								return true;
							case 9:
								Npc king = new Npc(KING_NARNODE_SHAREEN);
								NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, king, new Position(2466, 3496, 3), false, null);
								king.walkTo(player.getPosition(), true);
								player.getDialogue().setLastNpcTalk(KING_NARNODE_SHAREEN);
								player.getDialogue().sendNpcChat("Traveller please accept my apologies! Glough had no", "right to arrest you! I just think he's scared of humans.", "Let me get you out of there...", CONTENT);
								return true;
							case 10:
								player.getDialogue().endDialogue();
								player.setStopPacket(true);
								final Player finalPlayer = player;
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										finalPlayer.setStopPacket(false);
										finalPlayer.fadeTeleport(new Position(2466, 3496, 0));
									}
								}, 15);
								return true;
						}
						return false;
					case TALK_TO_PRISONER:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Tell me. Why would you want to kill", "the Grand Tree?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("What do you mean?!", DISTRESSED);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Don't tell me... you just happened to be caught", "carrying Daconia rocks!", Dialogues.ANNOYED);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("All I know is that I did what I was asked.", SAD);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("I don't understand.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Glough paid me to go to this gnome on a hill. I gave", "the gnome a seal and he gave me some rocks to give to", "Glough.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("I've been doing it for weeks, this time though, Glough", "locked me up here! I just don't understand it...", SAD);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Oh shit. Sounds like Glough is hiding something.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("I don't know what he's up to. If you want to find out", "you'd better search his home.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("Alright, thanks Charlie.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("Good luck!", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(29, SEARCH_GLOUGHS_HOME);
								return true;

						}
						return false;
				}
				return false;
			case GLOUGH:
				switch (player.getQuestStage(29)) {
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getQuestStage(36) != 4) {
									player.getDialogue().sendNpcChat("You look shifty, human. I've got my eye", "on you.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else {
									return false;
								}
						}
						return false;
					case TRAPDOOR_OPEN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getPosition().getY() > 9000) { //le over 9000 faec
									player.getDialogue().sendNpcChat("You really are becoming a headache! Well, at least now", "you can die knowing you were right, it will save me", "having to hunt you down like all the other human filth", "of /v/scape!", ANGRY_1);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Filthy human, you're lucky Narnode isn't allowing", "me to arrest you again...", ANNOYED);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("You're crazy Glough!", DISTRESSED);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Bah! Well, soon you'll see, the gnome's are ready to", "fight, in three weeks this tree will be dead wood, in ten", "weeks it will be 30 battleships! Finally we will rid the", "world of the disease called humanity!", ANNOYED);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("What makes you think I'll let you get away with it?", ANGRY_1);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Fool... meet my little friend!", LAUGHING);
								return true;
							case 6:
								player.getActionSender().sendMessage("You hear loud footsteps approaching...");
								NpcLoader.destroyNpc(glough);
								player.getDialogue().endDialogue();
								Npc blackDemon = new Npc(BLACK_DEMON);
								NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, blackDemon, new Position(2477, 9865, 0), false, null);
								CombatManager.attack(blackDemon, player);
								return true;
						}
						return false;
					case JOURNAL_GET:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Glough! I don't know what you're up to but I know", "you paid Charlie to get those rocks!", ANGRY_1);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("You're a fool human! You have no idea what's going on.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I know the Grand Tree's dying! And I think you're", "part of the reason.", Dialogues.ANNOYED);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("How dare you accuse me! I'm the head tree guardian!", Dialogues.ANGRY_2);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Guards! Guards!", Dialogues.ANNOYED);
								return true;
							case 6:
								player.getDialogue().endDialogue();
								player.fadeTeleport(new Position(2464, 3496, 3));
								player.setQuestStage(29, IN_JAIL);
								return true;
						}
						return false;
					case SEARCH_GLOUGHS_HOME:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Why do you look so shifty? Do you have something", "to say human?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("No, just, uh. Taking a look around.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Hrmph.", ANNOYED);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case TALK_TO_GLOUGH:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendStatement("The gnome is munching on a gnome delicacy.");
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Can I help you human? Can't you see I'm eating?!", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendStatement("The gnome continues to eat.");
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("The King asked me to inform you that the", "Daconia rocks have been taken!", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Surely not!", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Apparently a human took them from Hazelmere.", "Hazelmere believed him; he had the King's seal!", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("I should've known! The humans are going to invade!", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("N-never!", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Your type can't be trusted! I'll take care of this! Go", "back to the King.", ANGRY_1);
								player.getDialogue().endDialogue();
								player.setQuestStage(29, BACK_TO_NARNODE_2);
								return true;
						}
						return false;
				}
			case HAZELMERE:
				switch (player.getQuestStage(29)) {
					case TALK_TO_HAZELMERE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendStatement("The mage starts to speak but all you hear is:");
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Blah. Blah, blah, blah, blah... blah!", CONTENT);
								return true;
							case 3:
								if (player.getInventory().playerHasItem(BARK_SAMPLE)) {
									player.getDialogue().sendGiveItemNpc("You hand the bark sample to Hazelmere.", new Item(BARK_SAMPLE));
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Err, I can't understand a single thing you're saying.", "I meant to bring you a bark sample... but I guess", "I forgot it...", SAD);
									player.getDialogue().endDialogue();
									return true;
								}
							case 4:
								player.getDialogue().sendStatement("The mage carefully examines the sample.");
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Blah, blah... Daconia... blah, blah.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Can you write this down, and I'll try", "and translate it later?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Blah, blah?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendStatement("You make a writing motion. The mage scribbles something", "down on a scroll.");
								return true;
							case 9:
								player.getDialogue().sendGiveItemNpc("Hazelmere has given you the scroll.", new Item(HAZELMERES_SCROLL));
								player.getDialogue().endDialogue();
								player.getInventory().replaceItemWithItem(new Item(BARK_SAMPLE), new Item(HAZELMERES_SCROLL));
								player.setQuestStage(29, BACK_TO_NARNODE);
								return true;
						}
						return false;
				}
				return false;
			case KING_NARNODE_SHAREEN:
				switch (player.getQuestStage(29)) { //Dialogue per stage
					case HIDDEN_DACONIA_ROCK:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().playerHasItem(DACONIA_ROCK)) {
									player.getDialogue().sendNpcChat("Traveller, have you managed to find the Daconia?", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Hurry Traveller, find that Daconia rock!", "The Tree is still dying!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("Is this it?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Yes! Excellent, well done!", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendGiveItemNpc("You hand the King the Daconia rock.", new Item(DACONIA_ROCK));
								player.getInventory().removeItem(new Item(DACONIA_ROCK));
								return true;
							case 5:
								player.getDialogue().sendNpcChat("It's incredible, the Tree's health is improving already! I", "don't know what to say, we owe you so much!", HAPPY);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("To think Glough had me fooled all along!", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("All that matters now is that humans and gnomes", "can live together in peace!", HAPPY);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("I'll drink to that!", LAUGHING);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("From now on I vow to make this stronghold a", "welcoming place for all! I'll grant you access to all", "our facilities.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("Thank you!", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("I think!", LAUGHING);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("It should make your stay here easier. You can use the", "spirit tree to travel to farther places, as well as the", "gnome glider. I also give you access to our mine.", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendPlayerChat("Mine?", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("Very few know of the secret mine under the Grand", "Tree. If you push on the roots just to my north they", "will now seperate and let you pass.", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendPlayerChat("Strange.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("That's magic trees for you!", LAUGHING);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("All the best Traveller, and thanks again!", HAPPY);
								return true;
							case 18:
								player.getDialogue().sendPlayerChat("You too, your highness!", HAPPY);
								return true;
							case 19:
								player.getDialogue().dontCloseInterface();
								QuestHandler.completeQuest(player, 29);
								return true;
						}
						return false;
					case WARN_NARNODE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Traveller, you're wounded! What happened?", DISTRESSED);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("It's Glough! he set a Black Demon on me!", SAD);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("What?! Glough? With a demon?!", DISTRESSED);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Glough has a store of Daconia rocks further up the", "passage! He's been accessing the roots from a secret", "passage at his home.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Never! Not Glough! He's a good gnome at heart!", SAD);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Guards!", CONTENT);
								return true;
							case 7:
								player.getDialogue().setLastNpcTalk(163);
								player.getDialogue().sendNpcChat("Sire!", CONTENT);
								return true;
							case 8:
								player.getDialogue().setLastNpcTalk(KING_NARNODE_SHAREEN);
								player.getDialogue().sendNpcChat("Go and check out that passage!", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendStatement("...Time passes as the guards search...");
								return true;
							case 10:
								player.getDialogue().sendStatement("...");
								return true;
							case 11:
								player.getDialogue().sendStatement("...");
								return true;
							case 12:
								player.getDialogue().sendStatement("It seems the guards have returned.");
								return true;
							case 13:
								player.getDialogue().setLastNpcTalk(163);
								player.getDialogue().sendNpcChat("We found Glough hiding under a horde of Daconia", "rocks!", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("That's what I've been trying to tell you!", "Glough's been fooling you!", ANGRY_1);
								return true;
							case 15:
								player.getDialogue().setLastNpcTalk(KING_NARNODE_SHAREEN);
								player.getDialogue().sendNpcChat("I... I don't know what to say. How could I", "have been so blind?!", SAD);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("Guards! Call off the military training!", "The humans are not attacking!", CONTENT);
								return true;
							case 17:
								player.getDialogue().setLastNpcTalk(163);
								player.getDialogue().sendNpcChat("Yes sir!", HAPPY);
								return true;
							case 18:
								player.getDialogue().setLastNpcTalk(KING_NARNODE_SHAREEN);
								player.getDialogue().sendNpcChat("You have my full apologies Traveller!", "And my gratitude!", CONTENT);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("A reward will have to wait though, the tree is still dying!", "The guards are clearing Glough's rock supply now, but", "there must be more Daconia hidden somewhere in the", "roots! Help us search, we have little time!", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(29, HIDDEN_DACONIA_ROCK);
								return true;
						}
						return false;
					case TRAPDOOR_OPEN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I found a secret trapdoor at Glough's house.", "The sticks you found were the key to opening it.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Ah, well... I suppose a secret trapdoor is still", "not a crime, everyone likes to hide their valuables.", "If you're concerned, go see where it leads and report back.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case TWIGS_GET:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().ownsItem(TWIGS_T) || !player.getInventory().ownsItem(TWIGS_U) || !player.getInventory().ownsItem(TWIGS_Z) || !player.getInventory().ownsItem(TWIGS_O)) {
									player.getDialogue().sendPlayerChat("I seem to have lost one of the twigs you gave me.", SAD);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Let me know if you find anything new...", "Otherwise it's full steam ahead with raising an army.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Oh, clumsy Traveller. We made copies of the sticks for", "purposes of documentation. Which stick did you lose?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendOption("The one shaped like a 'Z'.", "The one shaped like an 'O'.", "The one shaped like a 'T'.", "The one shaped like an 'U'.", "Nevermind.");
								return true;
							case 4:
								switch (optionId) {
									case 1:
										if (!player.getInventory().ownsItem(TWIGS_Z)) {
											player.getDialogue().sendGiveItemNpc("King Narnode hands you a replica twig.", new Item(TWIGS_Z));
											player.getDialogue().endDialogue();
											player.getInventory().addItemOrDrop(new Item(TWIGS_Z));
											return true;
										} else {
											player.getDialogue().sendNpcChat("You're pulling my leg! You already have that twig!", "Did you forget it in the bank?", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										}
									case 2:
										if (!player.getInventory().ownsItem(TWIGS_O)) {
											player.getDialogue().sendGiveItemNpc("King Narnode hands you a replica twig.", new Item(TWIGS_O));
											player.getDialogue().endDialogue();
											player.getInventory().addItemOrDrop(new Item(TWIGS_O));
											return true;
										} else {
											player.getDialogue().sendNpcChat("You're pulling my leg! You already have that twig!", "Did you forget it in the bank?", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										}
									case 3:
										if (!player.getInventory().ownsItem(TWIGS_T)) {
											player.getDialogue().sendGiveItemNpc("King Narnode hands you a replica twig.", new Item(TWIGS_T));
											player.getDialogue().endDialogue();
											player.getInventory().addItemOrDrop(new Item(TWIGS_T));
											return true;
										} else {
											player.getDialogue().sendNpcChat("You're pulling my leg! You already have that twig!", "Did you forget it in the bank?", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										}
									case 4:
										if (!player.getInventory().ownsItem(TWIGS_U)) {
											player.getDialogue().sendGiveItemNpc("King Narnode hands you a replica twig.", new Item(TWIGS_U));
											player.getDialogue().endDialogue();
											player.getInventory().addItemOrDrop(new Item(TWIGS_U));
											return true;
										} else {
											player.getDialogue().sendNpcChat("You're pulling my leg! You already have that twig!", "Did you forget it in the bank?", CONTENT);
											player.getDialogue().endDialogue();
											return true;
										}
									case 5:
										player.getDialogue().sendPlayerChat("Erm. Nevermind.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
						}
						return false;
					case INVASION_PLANS_FOUND:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hi, your highness, did you think about what I said?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Look, if you're right about Glough I would have him", "arrested... But there's no reason for me to", "think he's lying.", CONTENT);
								return true;
							case 3:
								if (player.getInventory().playerHasItem(INVASION_PLANS)) {
									player.getDialogue().sendPlayerChat("Look, I found this at Glough's home...", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I guess I'll have to get concrete evidence to", "change you mind.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 4:
								player.getDialogue().sendGiveItemNpc("You show King Narnode the invasion plans.", new Item(INVASION_PLANS));
								return true;
							case 5:
								player.getDialogue().sendStatement("The King looks them over in horror.");
								return true;
							case 6:
								player.getDialogue().sendNpcChat("If these are to be believed then this is terrible!", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("...But it's not proof. Anyone could have made these.", "Traveller, I understand your concern, I had guards", "search Glough's house but they found nothing", "suspicious, just these odd twigs.", ANNOYED);
								return true;
							case 8:
								if (player.getInventory().getItemContainer().emptySlots() >= 3) {
									player.getDialogue().sendGiveItemNpc("The King shows you some twigs lashed together.", new Item(TWIGS_T));
									return true;
								} else {
									player.getDialogue().sendNpcChat("Hmm. Looks like you can't carry anything else. Put", "enough things down for 3 free slots and come back to me.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 9:
								player.getDialogue().sendNpcChat("On the other hand, if Glough is right about the humans", "we will need an army of gnomes to protect ourselves.", "So I've decided to allow Glough to raise a mighty", "gnome army.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendStatement("You supress a laugh at the thought of a 'mighty' gnome army.");
								return true;
							case 11:
								player.getDialogue().sendNpcChat("The Grand Tree's still slowly dying, if it is human", "sabotage we must respond!", SAD);
								player.getDialogue().endDialogue();
								player.setQuestStage(29, TWIGS_GET);
								player.getInventory().replaceItemWithItem(new Item(INVASION_PLANS), new Item(TWIGS_T));
								player.getInventory().addItem(new Item(TWIGS_Z));
								player.getInventory().addItem(new Item(TWIGS_O));
								player.getInventory().addItem(new Item(TWIGS_U));
								return true;

						}
						return false;
					case KEY_GET:
					case FIND_GLOUGHS_KEY:
					case ESCAPE_FROM_ALCATRAZ:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().playerHasItem(LUMBER_ORDER)) {
									player.getDialogue().sendNpcChat("Traveller, what are you doing here? The stronghold has", "been put on full alert! It's not safe for you here!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendPlayerChat("King Narnode, we need to talk!", ANGRY_1);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Traveller, what are you doing here? The stronghold has", "been put on full alert! It's not safe for you here!", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Your highness, I believe Glough is killing the trees in", "order to make a mass fleet of warships!", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("That's an absurd accusation!", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("His hatred for humanity is stronger than you know!", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("That's enough Traveller, you sound as paranoid as him!", "Traveller, please leave! It's bad enough having one", "human locked up.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case IN_JAIL:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I don't think you can trust Glough, your highness. He", "seems to have an unnatural hatred for humans.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("I know he can be a bit extreme at times. But he's the", "best tree guardian I have, he has made the gnomes", "paranoid about humans though.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("I'm afraid Glough has placed guards on the front gate", "to stop you escaping! Let my glider pilot fly you away", "until things calm down around here.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Well, okay.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("I'm sorry again Traveller!", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(29, ESCAPE_FROM_ALCATRAZ);
								return true;
						}
						return false;
					case JOURNAL_GET:
					case SEARCH_GLOUGHS_HOME:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("The prisoner said that Glough was paying him to", "retrieve the stones!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("HAH! Nonsense! Glough is my most trusted member of", "the guard!", LAUGHING);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case TALK_TO_PRISONER:
					case BACK_TO_NARNODE_2:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello, your highness. Have you any news on the", "Daconia stones?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("It's okay Traveller, thanks to Glough! He found a", "human sneaking around! He has three Daconia rocks", "on him.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Wow, that was a bit... quick.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Yes, Glough really knows what he's doing. The human", "has been detained until we know who else is involved.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Maybe Glough was right, maybe humans are out to get us...", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("I doubt it, can I speak to the prisoner?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Certainly. He's on the top level of the tree. Be careful,", "it's a long way down!", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(29, TALK_TO_PRISONER);
								return true;
						}
						return false;
					case BACK_TO_NARNODE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello again, your highness.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Hello Traveller, did you speak to Hazelmere?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Yes! I managed to find him.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Do you understand what he said?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("I think so...", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("So, what did he say?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendOption("King Narnode must be stopped, he is a madman!", "Praise be to the great Zamorak!", "Do you have any bread? I do like bread.", "The time has come to attack!", "None of the above.");
								return true;
							case 8:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("'King Narnode must be stopped, he is a madman!'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("'Praise be to the great Zamorak!'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("'Do you have any bread? I do like bread.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 4:
										player.getDialogue().sendPlayerChat("'The time has come to attack!'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 5:
										player.getDialogue().sendOption("The tree is fine, you have nothing to fear.", "You must come and see me!", "The tree needs watering as there has been drought.", "Grave danger lies ahead, only the bravest will linger.", "None of the above.");
										return true;
								}
							case 9:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("'The tree is fine, you have nothing to fear.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("'You must come and see me!'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("'The tree needs watering as there has been drought.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 4:
										player.getDialogue().sendPlayerChat("'Grave danger lies ahead, only the bravest will linger.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 5:
										player.getDialogue().sendOption("Time is of the essence! We must move quickly.", "A man came to me with the King's seal.", "There is no need for haste, just send a runner.", "You must act now or we will all die!", "None of the above.");
										return true;
								}
							case 10:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("'Time is of the essence! We must move quickly.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("'A man came to me with the King's seal.'", CONTENT);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("'There is no need for haste, just send a runner.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 4:
										player.getDialogue().sendPlayerChat("'You must act now or we will all die!'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 5:
										player.getDialogue().sendPlayerChat("Err, let me look at what Hazelmere wrote down again...", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 11:
								player.getDialogue().sendOption("I gave the man Daconia rocks.", "You must use force!", "Use a bucket of milk from a sacred cow.", "Take this banana to him, he will understand.", "None of the above.");
								return true;
							case 12:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("'I gave the man Daconia rocks.'", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("'You must use force!'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("'Use a bucket of milk from a sacred cow.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 4:
										player.getDialogue().sendPlayerChat("'Take this banana to him, he will understand.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 5:
										player.getDialogue().sendPlayerChat("Err, let me look at what Hazelmere wrote down again...", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 13:
								player.getDialogue().sendOption("All will be fine on the third night.", "You must wait till the second night.", "Nothing will help us now!", "And Daconia rocks will kill the tree!");
								return true;
							case 14:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("'All will be fine on the third night.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("'You must wait till the second night.'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("'Nothing will help us now!'", CONTENT);
										player.getDialogue().setNextChatId(25);
										return true;
									case 4:
										player.getDialogue().sendPlayerChat("'And Daconia rocks will kill the tree!'", CONTENT);
										return true;
								}
							case 15:
								player.getDialogue().sendNpcChat("Of course! I should've known! Someone must've forged", "my royal seal. Hazelmere thought I sent him for the", "Daconia stones!", DISTRESSED);
								return true;
							case 16:
								player.getDialogue().sendPlayerChat("What are Daconia stones?", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("Hazelmere created the Daconia stones. They are a", "safety measure, in case the tree grew out of control.", "They're the only thing that can kill the tree.", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendNpcChat("This is terrible! The stones must be recovered!", CONTENT);
								return true;
							case 19:
								player.getDialogue().sendPlayerChat("Can I help?", CONTENT);
								return true;
							case 20:
								player.getDialogue().sendNpcChat("First I must warn the tree guardians. Please, could", "you tell the chief tree guardian Glough. He lives in a", "tree house just in front of the Grand Tree.", CONTENT);
								return true;
							case 21:
								player.getDialogue().sendNpcChat("If he's not there he will be at his girlfriend Anita's place.", "Meet me back here once you've told him.", CONTENT);
								return true;
							case 22:
								player.getDialogue().sendPlayerChat("Ok! I'll be back soon.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(29, TALK_TO_GLOUGH);
								return true;
							case 25:
								player.getDialogue().sendNpcChat("That doesn't sound like Hazelmere at all...", "are you sure you understood what he said?", CONTENT);
								player.getDialogue().endDialogue();
								return true;

						}
						return false;
					case TALK_TO_HAZELMERE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().ownsItem(BARK_SAMPLE) && player.getGroundItems().isEmpty()) {
									player.getDialogue().sendGiveItemNpc("King Narnode Shareen hands you a bark sample.", new Item(BARK_SAMPLE));
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(BARK_SAMPLE));
									return true;
								} else if (!player.getInventory().ownsItem(TRANSLATION_BOOK) && player.getGroundItems().isEmpty()) {
									player.getDialogue().sendGiveItemNpc("King Narnode Shareen hands you the translation book.", new Item(TRANSLATION_BOOK));
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(TRANSLATION_BOOK));
									return true;
								} else {
									player.getDialogue().sendNpcChat("Did you find Hazelmere yet?", CONTENT);
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 0: //Starting the quest
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getPosition().getY() < 8000) {
									player.getDialogue().sendNpcChat("Welcome Traveller. I am King Narnode. It's nice to", "see an outsider.", CONTENT);
									player.getDialogue().setNextChatId(22);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("So, what is this place?", CONTENT);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("These, my friend, are the foundations of the stronghold.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("They look like roots to me.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Not just any roots Traveller! These were created by", "gnome mages eons ago, since then they have grown to", "become a might stronghold.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Impressive. What exactly is the problem?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("In the last two months our tree guardians have", "reported continuing deterioration of the Grand Tree's", "health. I've never seen this before! It could be the end", "for us all!", SAD);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("You mean the tree is ill?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("In effect, yes. Would you be willing to help us discover", "what is happening to the tree?", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendOption("I'm sorry, I don't want to get involved.", "I'd be happy to help!");
								return true;
							case 10:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I'm sorry, I don't want to get involved.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("I'd be happy to help!", CONTENT);
										return true;
								}
							case 11:
								player.getDialogue().sendNpcChat("Thank Guthix for your arrival!", CONTENT);
								player.getDialogue().setNextChatId(1);
								QuestHandler.startQuest(player, 29);
								return true;
							case 22:
								player.getDialogue().sendPlayerChat("Hi! It seems to be a busy settlement.", CONTENT);
								return true;
							case 23:
								player.getDialogue().sendNpcChat("For now.", CONTENT);
								return true;
							case 24:
								player.getDialogue().sendPlayerChat("You seem worried, what's up?", CONTENT);
								return true;
							case 25:
								player.getDialogue().sendNpcChat("Traveller, can I speak to you in strictest confidence?", CONTENT);
								return true;
							case 26:
								player.getDialogue().sendPlayerChat("Of course sire.", CONTENT);
								return true;
							case 27:
								player.getDialogue().sendNpcChat("Not here. Meet me downstairs, down the trapdoor", "just right over there.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("The first task is to find out what's killing the tree.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Do you have an idea?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("My top tree guardian, Glough, believes it's human", "sabotage. I'm not so sure! The only way to know for", "sure is to talk to Hazelmere.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Who's Hazelmere?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Hazelmere is one of the mages that created the Grand", "Tree! He is the only one that has survived from that", "time. Take this bark sample to him, he will be able to", "help!", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("The mage only talks in the old tongue,", "you'll need this translation book as well.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("How does it work?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("It's a translation book, you'll need it to translate what", "Hazelmere says. Do this carefully! His words are our", "only hope! you'll find his dwellings high upon a towering", "hill, on an island east of Yanille.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendGiveItemNpc("The king hands you a sample of bark...", "...and a strange translation book.", new Item(BARK_SAMPLE), new Item(TRANSLATION_BOOK));
								player.getDialogue().endDialogue();
								player.setQuestStage(29, TALK_TO_HAZELMERE);
								player.getInventory().addItemOrDrop(new Item(BARK_SAMPLE));
								player.getInventory().addItemOrDrop(new Item(TRANSLATION_BOOK));
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
