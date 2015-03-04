package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.consumables.Food;
import com.rs2.model.content.consumables.Food.FoodDef;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;

import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

import java.util.ArrayList;

public class NatureSpirit implements Quest {

	public static final int questIndex = 8137; //Used in player's quest log interface, id is in Player.java //Change

	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int TALKED_TO_FILLIMAN = 2;
	public static final int FILLIMAN_NEEDS_JOURNAL = 3;
	public static final int SOMETHING_FROM_NATURE = 4;
	public static final int BLESSED = 5;
	public static final int ENTER_GROTTO = 6;
	public static final int TRANSFORMATION_COMPLETE = 7;
	public static final int BLESSED_SICKLE_GET = 8;
	public static final int GHASTS_SLAIN = 9;
	public static final int QUEST_COMPLETE = 10;

	//Items
	public static final int APPLE_PIE = 2323;
	public static final int MEAT_PIE = 2327;
	public static final int DRUID_POUCH_EMPTY = 2957;
	public static final int DRUID_POUCH = 2958;
	public static final int ROTTEN_FOOD = 2959;
	public static final int SILVER_SICKLE = 2961;
	public static final int SILVER_SICKLE_B = 2963;
	public static final int WASHING_BOWL = 2964;
	public static final int MIRROR = 2966;
	public static final int JOURNAL = 2967;
	public static final int DRUIDIC_SPELL = 2968;
	public static final int USED_SPELL = 2969;
	public static final int MORT_MYRE_FUNGUS = 2970;
	public static final int MORT_MYRE_STEM = 2972;
	public static final int MORT_MYRE_PEAR = 2974;
	public static final int SICKLE_MOULD = 2976;

	//Positions
	public static final Position POSITION = new Position(0, 0, 0);

	//Interfaces
	public static final int INTERFACE = -1;

	//Npcs
	public static final int DREZEL = 1049;
	public static final int FILLIMAN_TARLOCK = 1050;
	public static final int NATURE_SPIRIT = 1051;
	public static final int GHAST = 1052;
	public static final int GHAST_ATTACKABLE = 1053;
	public static final int ULIZIUS = 1054;

	//Objects
	public static final int GROTTO_BRIDGE = 3522;
	public static final int GROTTO_ENTRANCE = 3516;

	public int dialogueStage = 0;

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = { //{skillId, exp},
		{Skill.CRAFTING, 3000},
		{Skill.DEFENCE, 2000},
		{Skill.HITPOINTS, 2000}
	};

	private static final int questPointReward = 2; //Change

	public int getQuestID() { //Change
		return 37;
	}

	public String getQuestName() { //Change
		return "Nature Spirit";
	}

	public String getQuestSaveName() { //Change
		return "naturespirit";
	}

	public boolean canDoQuest(final Player player) {
		return QuestHandler.questCompleted(player, 2) && QuestHandler.questCompleted(player, 23);
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
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
		player.getActionSender().sendItemOnInterface(12145, 250, SILVER_SICKLE_B); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("2 Quest Points", 12150);
		player.getActionSender().sendString("6750 Crafting XP", 12151);
		player.getActionSender().sendString("4500 Defence XP", 12152);
		player.getActionSender().sendString("4500 Hitpoints XP", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case QUEST_STARTED:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);

				player.getActionSender().sendString("Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("He should be south in the swamp, I have food for him.", 8151);
				break;
			case TALKED_TO_FILLIMAN:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@str@" + "Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("@str@" + "He should be south in the swamp, I have food for him.", 8151);

				player.getActionSender().sendString("I found Filliman. Turns out he's dead. But, he won't", 8153);
				player.getActionSender().sendString("believe me when I tell him that. I need to make him", 8154);
				player.getActionSender().sendString("see that he is dead.", 8155);
				break;
			case FILLIMAN_NEEDS_JOURNAL:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@str@" + "Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("@str@" + "He should be south in the swamp, I have food for him.", 8151);
				player.getActionSender().sendString("@str@" + "I found Filliman. Turns out he's dead. But, he won't", 8153);
				player.getActionSender().sendString("@str@" + "believe me when I tell him that. I need to make him", 8154);
				player.getActionSender().sendString("@str@" + "see that he is dead.", 8155);

				player.getActionSender().sendString("I convinced Filliman he was dead by showing him his", 8157);
				player.getActionSender().sendString("reflection in a mirror. He now needs his journal", 8158);
				player.getActionSender().sendString("to figure things out. I should help him find it.", 8159);
				break;
			case SOMETHING_FROM_NATURE:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@str@" + "Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("@str@" + "He should be south in the swamp, I have food for him.", 8151);
				player.getActionSender().sendString("@str@" + "I found Filliman. Turns out he's dead. But, he won't", 8153);
				player.getActionSender().sendString("@str@" + "believe me when I tell him that. I need to make him", 8154);
				player.getActionSender().sendString("@str@" + "see that he is dead.", 8155);
				player.getActionSender().sendString("@str@" + "I convinced Filliman he was dead by showing him his", 8157);
				player.getActionSender().sendString("@str@" + "reflection in a mirror. He now needs his journal", 8158);
				player.getActionSender().sendString("@str@" + "to figure things out. I should help him find it.", 8159);

				player.getActionSender().sendString("I found Filliman's journal and there are things he", 8161);
				player.getActionSender().sendString("needs my help to find. The first is 'something from", 8162);
				player.getActionSender().sendString("nature' which he said he knew how to find. I need to", 8163);
				player.getActionSender().sendString("visit the Temple north of here to be blessed.", 8164);
				break;
			case BLESSED:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@str@" + "Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("@str@" + "He should be south in the swamp, I have food for him.", 8151);
				player.getActionSender().sendString("@str@" + "I found Filliman. Turns out he's dead. But, he won't", 8153);
				player.getActionSender().sendString("@str@" + "believe me when I tell him that. I need to make him", 8154);
				player.getActionSender().sendString("@str@" + "see that he is dead.", 8155);
				player.getActionSender().sendString("@str@" + "I convinced Filliman he was dead by showing him his", 8157);
				player.getActionSender().sendString("@str@" + "reflection in a mirror. He now needs his journal", 8158);
				player.getActionSender().sendString("@str@" + "to figure things out. I should help him find it.", 8159);
				player.getActionSender().sendString("@str@" + "I found Filliman's journal and there are things he", 8161);
				player.getActionSender().sendString("@str@" + "needs my help to find. The first is 'something from", 8162);
				player.getActionSender().sendString("@str@" + "nature' which he said he knew how to find. I need to", 8163);
				player.getActionSender().sendString("@str@" + "visit the Temple north of here to be blessed.", 8164);

				player.getActionSender().sendString("I was blessed by Drezel, I should see what Filliman", 8166);
				player.getActionSender().sendString("needs me to do to complete the Nature Spirit", 8167);
				player.getActionSender().sendString("transformation he is going to undergo.", 8168);
				break;
			case ENTER_GROTTO:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@str@" + "Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("@str@" + "He should be south in the swamp, I have food for him.", 8151);
				player.getActionSender().sendString("@str@" + "I found Filliman. Turns out he's dead. But, he won't", 8153);
				player.getActionSender().sendString("@str@" + "believe me when I tell him that. I need to make him", 8154);
				player.getActionSender().sendString("@str@" + "see that he is dead.", 8155);
				player.getActionSender().sendString("@str@" + "I convinced Filliman he was dead by showing him his", 8157);
				player.getActionSender().sendString("@str@" + "reflection in a mirror. He now needs his journal", 8158);
				player.getActionSender().sendString("@str@" + "to figure things out. I should help him find it.", 8159);
				player.getActionSender().sendString("@str@" + "I found Filliman's journal and there are things he", 8161);
				player.getActionSender().sendString("@str@" + "needs my help to find. The first is 'something from", 8162);
				player.getActionSender().sendString("@str@" + "nature' which he said he knew how to find. I need to", 8163);
				player.getActionSender().sendString("@str@" + "visit the Temple north of here to be blessed.", 8164);
				player.getActionSender().sendString("@str@" + "I was blessed by Drezel, I should see what Filliman", 8166);
				player.getActionSender().sendString("@str@" + "needs me to do to complete the Nature Spirit", 8167);
				player.getActionSender().sendString("@str@" + "transformation he is going to undergo.", 8168);

				player.getActionSender().sendString("I did it! I completed the Nature spell with the help of", 8170);
				player.getActionSender().sendString("Filliman. He told me to enter his grotto for the final", 8171);
				player.getActionSender().sendString("step of the Nature Spirit transformation.", 8172);
				break;
			case TRANSFORMATION_COMPLETE:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@str@" + "Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("@str@" + "He should be south in the swamp, I have food for him.", 8151);
				player.getActionSender().sendString("@str@" + "I found Filliman. Turns out he's dead. But, he won't", 8153);
				player.getActionSender().sendString("@str@" + "believe me when I tell him that. I need to make him", 8154);
				player.getActionSender().sendString("@str@" + "see that he is dead.", 8155);
				player.getActionSender().sendString("@str@" + "I convinced Filliman he was dead by showing him his", 8157);
				player.getActionSender().sendString("@str@" + "reflection in a mirror. He now needs his journal", 8158);
				player.getActionSender().sendString("@str@" + "to figure things out. I should help him find it.", 8159);
				player.getActionSender().sendString("@str@" + "I found Filliman's journal and there are things he", 8161);
				player.getActionSender().sendString("@str@" + "needs my help to find. The first is 'something from", 8162);
				player.getActionSender().sendString("@str@" + "nature' which he said he knew how to find. I need to", 8163);
				player.getActionSender().sendString("@str@" + "visit the Temple north of here to be blessed.", 8164);
				player.getActionSender().sendString("@str@" + "I was blessed by Drezel, I should see what Filliman", 8166);
				player.getActionSender().sendString("@str@" + "needs me to do to complete the Nature Spirit", 8167);
				player.getActionSender().sendString("@str@" + "transformation he is going to undergo.", 8168);

				player.getActionSender().sendString("Filliman is now fully transformed into a Nature spirit.", 8170);
				player.getActionSender().sendString("He asked me to bring him a silver sickle so that he can", 8171);
				player.getActionSender().sendString("bless it.", 8172);
				break;
			case BLESSED_SICKLE_GET:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@str@" + "Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("@str@" + "He should be south in the swamp, I have food for him.", 8151);
				player.getActionSender().sendString("@str@" + "I found Filliman. Turns out he's dead. But, he won't", 8153);
				player.getActionSender().sendString("@str@" + "believe me when I tell him that. I need to make him", 8154);
				player.getActionSender().sendString("@str@" + "see that he is dead.", 8155);
				player.getActionSender().sendString("@str@" + "I convinced Filliman he was dead by showing him his", 8157);
				player.getActionSender().sendString("@str@" + "reflection in a mirror. He now needs his journal", 8158);
				player.getActionSender().sendString("@str@" + "to figure things out. I should help him find it.", 8159);
				player.getActionSender().sendString("@str@" + "I found Filliman's journal and there are things he", 8161);
				player.getActionSender().sendString("@str@" + "needs my help to find. The first is 'something from", 8162);
				player.getActionSender().sendString("@str@" + "nature' which he said he knew how to find. I need to", 8163);
				player.getActionSender().sendString("@str@" + "visit the Temple north of here to be blessed.", 8164);
				player.getActionSender().sendString("@str@" + "I was blessed by Drezel, I should see what Filliman", 8166);
				player.getActionSender().sendString("@str@" + "needs me to do to complete the Nature Spirit", 8167);
				player.getActionSender().sendString("@str@" + "transformation he is going to undergo.", 8168);
				player.getActionSender().sendString("@str@" + "Filliman is now fully transformed into a Nature spirit.", 8170);

				player.getActionSender().sendString("The Nature spirit blessed my silver sickle and then", 8172);
				player.getActionSender().sendString("told me to use the Druid pouch to help kill 3 Ghasts in", 8173);
				player.getActionSender().sendString("Mort Myre to restore his Grotto to it's former glory.", 8174);
				break;
			case GHASTS_SLAIN:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@str@" + "Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("@str@" + "He should be south in the swamp, I have food for him.", 8151);
				player.getActionSender().sendString("@str@" + "I found Filliman. Turns out he's dead. But, he won't", 8153);
				player.getActionSender().sendString("@str@" + "believe me when I tell him that. I need to make him", 8154);
				player.getActionSender().sendString("@str@" + "see that he is dead.", 8155);
				player.getActionSender().sendString("@str@" + "I convinced Filliman he was dead by showing him his", 8157);
				player.getActionSender().sendString("@str@" + "reflection in a mirror. He now needs his journal", 8158);
				player.getActionSender().sendString("@str@" + "to figure things out. I should help him find it.", 8159);
				player.getActionSender().sendString("@str@" + "I found Filliman's journal and there are things he", 8161);
				player.getActionSender().sendString("@str@" + "needs my help to find. The first is 'something from", 8162);
				player.getActionSender().sendString("@str@" + "nature' which he said he knew how to find. I need to", 8163);
				player.getActionSender().sendString("@str@" + "visit the Temple north of here to be blessed.", 8164);
				player.getActionSender().sendString("@str@" + "I was blessed by Drezel, I should see what Filliman", 8166);
				player.getActionSender().sendString("@str@" + "needs me to do to complete the Nature Spirit", 8167);
				player.getActionSender().sendString("@str@" + "transformation he is going to undergo.", 8168);
				player.getActionSender().sendString("@str@" + "Filliman is now fully transformed into a Nature spirit.", 8170);
				player.getActionSender().sendString("@str@" + "The Nature spirit blessed my silver sickle and then", 8172);
				player.getActionSender().sendString("@str@" + "told me to use the Druid pouch to help kill 3 Ghasts in", 8173);
				player.getActionSender().sendString("@str@" + "Mort Myre to restore his Grotto to it's former glory.", 8174);

				player.getActionSender().sendString("I have slain all 3 Ghasts! I should return to the", 8176);
				player.getActionSender().sendString("Nature spirit.", 8177);
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString("@str@" + "You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("@str@" + "the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@str@" + "Drezel told me to seek out Filliman, a druid in Mort Myre.", 8150);
				player.getActionSender().sendString("@str@" + "He should be south in the swamp, I have food for him.", 8151);
				player.getActionSender().sendString("@str@" + "I found Filliman. Turns out he's dead. But, he won't", 8153);
				player.getActionSender().sendString("@str@" + "believe me when I tell him that. I need to make him", 8154);
				player.getActionSender().sendString("@str@" + "see that he is dead.", 8155);
				player.getActionSender().sendString("@str@" + "I convinced Filliman he was dead by showing him his", 8157);
				player.getActionSender().sendString("@str@" + "reflection in a mirror. He now needs his journal", 8158);
				player.getActionSender().sendString("@str@" + "to figure things out. I should help him find it.", 8159);
				player.getActionSender().sendString("@str@" + "I found Filliman's journal and there are things he", 8161);
				player.getActionSender().sendString("@str@" + "needs my help to find. The first is 'something from", 8162);
				player.getActionSender().sendString("@str@" + "nature' which he said he knew how to find. I need to", 8163);
				player.getActionSender().sendString("@str@" + "visit the Temple north of here to be blessed.", 8164);
				player.getActionSender().sendString("@str@" + "I was blessed by Drezel, I should see what Filliman", 8166);
				player.getActionSender().sendString("@str@" + "needs me to do to complete the Nature Spirit", 8167);
				player.getActionSender().sendString("@str@" + "transformation he is going to undergo.", 8168);
				player.getActionSender().sendString("@str@" + "Filliman is now fully transformed into a Nature spirit.", 8170);
				player.getActionSender().sendString("@str@" + "The Nature spirit blessed my silver sickle and then", 8172);
				player.getActionSender().sendString("@str@" + "told me to use the Druid pouch to help kill 3 Ghasts in", 8173);
				player.getActionSender().sendString("@str@" + "Mort Myre to restore his Grotto to it's former glory.", 8174);
				player.getActionSender().sendString("@str@" + "I have slain all 3 Ghasts! I should return to the", 8176);
				player.getActionSender().sendString("@str@" + "Nature spirit.", 8177);

				player.getActionSender().sendString("I helped Filliman Tarlock turn his grotto into an Altar", 8179);
				player.getActionSender().sendString("to Nature. I may use it to my benefit.", 8180);

				player.getActionSender().sendString("@red@" + "You have completed this quest!", 8182);
				break;
			default:
				player.getActionSender().sendString("You can begin this quest by talking to @dre@Drezel @bla@at", 8147); //default quest log to begin quest
				player.getActionSender().sendString("the mouth of the river @dre@Salve.", 8148);
				player.getActionSender().sendString("@dre@Requirements:", 8149);
				if (QuestHandler.questCompleted(player, 2)) {
					player.getActionSender().sendString("@str@-The Restless Ghost.", 8151);
				} else {
					player.getActionSender().sendString("@dbl@-The Restless Ghost.", 8151);
				}
				if (QuestHandler.questCompleted(player, 23)) {
					player.getActionSender().sendString("@str@-Priest In Peril.", 8152);
				} else {
					player.getActionSender().sendString("@dbl@-Priest In Peril.", 8152);
				}
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public static void handleSwampRot(final Player player) {
		if (player.Area(3434, 3449, 3331, 3344)) {
			player.getActionSender().sendMessage("The aura of Filliman's camp protects you from the swamp.");
		} else if (player.getInventory().playerHasItem(SILVER_SICKLE_B) || player.getEquipment().getId(Constants.WEAPON) == SILVER_SICKLE_B) {
			player.getUpdateFlags().sendGraphic(264);
			player.getActionSender().sendMessage("Your blessed sickle prevents the swamp from decaying you.");
		} else {
			if (player.getPosition().getZ() == 0 && !Misc.goodDistance(player.getPosition(), new Position(3508, 3439, 0), 7) && !player.isInCutscene()) { //Near Curpile Fyod dialogue for Myreque
				player.getUpdateFlags().sendGraphic(267);
				player.getActionSender().sendMessage("The swamp decays you.", true);
				player.hit(Misc.random(4), HitType.NORMAL);
			}
		}
	}

	public static FoodDef findSpoilableFood(final Player player) {
		for (Item i : player.getInventory().getItemContainer().toArray()) {
			if (i == null) {
				continue;
			}
			FoodDef food = Food.forId(i.getId());
			if(food != null)
			{
				player.setTempInteger(i.getId());
				return food;
			}
		}
		return null;
	}

	public static void handleSpoilFood(final Npc npc, final Player player) {
		FoodDef f = findSpoilableFood(player);
		if (player.getInventory().playerHasItem(new Item(DRUID_POUCH))) {
			if (player.getInventory().getItemAmount(DRUID_POUCH) == 1) {
				player.getInventory().replaceItemWithItem(new Item(DRUID_POUCH), new Item(DRUID_POUCH_EMPTY));
			} else {
				player.getInventory().removeItem(new Item(DRUID_POUCH, 1));
			}
			player.getActionSender().sendMessage("The druid pouch makes the Ghast visible.");
			npc.sendTransform(GHAST_ATTACKABLE, 500);
			/*
			 Npc temp = npc;
			 NpcLoader.destroyNpc(npc);
			 NpcLoader.spawnPlayerOwnedAttackNpc(player, new Npc(GHAST_ATTACKABLE), temp.getPosition(), true, null);
			 */
		} else {
			if (f != null) {

				if (Misc.random(4) == 1 && player.getSkill().getLevel()[Skill.PRAYER] > 0) {
					int set = player.getSkill().getLevel()[Skill.PRAYER] - Misc.random(4);
					player.getSkill().getLevel()[Skill.PRAYER] = set <= 0 ? 0 : set;
					player.getSkill().refresh(Skill.PRAYER);
				}
				if (Misc.random(3) == 1) {
					player.getInventory().replaceItemWithItem(new Item(player.getTempInteger()), new Item(ROTTEN_FOOD));
					player.getActionSender().sendMessage("You feel something attacking your backpack, and smell a terrible stench.");
					int container = f.getContainer();
					if (container != -1) {
						player.getInventory().addItemOrDrop(new Item(container, 1));
					}
				} else {
					player.getActionSender().sendMessage("An attacking Ghast just misses you.");
				}
			} else {
				player.hit(Misc.random(2), HitType.NORMAL);
			}
		}
	}

	public static Npc getDrezel() {
		for (Npc npc : World.getNpcs()) {
			if (npc != null && npc.getNpcId() == DREZEL) {
				return npc;
			}
		}
		return null;
	}

	public static boolean allFillimanOptions(final Player player) {
		return player.getQuestVars().fillimanOption1 && player.getQuestVars().fillimanOption2 && player.getQuestVars().fillimanOption3;
	}

	public static CacheObject findSuitableLog(final Position toCheck) {
		int x = toCheck.getX();
		int y = toCheck.getY();
		Position[] positionsToCheck = {new Position(x + 1, y), new Position(x - 1, y), new Position(x, y + 1), new Position(x, y - 1), new Position(x + 1, y + 1), new Position(x + 1, y - 1), new Position(x - 1, y + 1), new Position(x - 1, y - 1)};
		for (Position p : positionsToCheck) {
			CacheObject g = ObjectLoader.object("Rotting log", p.getX(), p.getY(), 0);
			GameObject o = ObjectHandler.getInstance().getObject(p.getX(), p.getY(), 0);
			if (o != null && o.getDef().getId() == 3509) {
				continue;
			}
			if (g != null && g.getDef().getId() == 3508) {
				return g;
			}
		}
		return null;
	}

	public static CacheObject findSuitableBranch(final Position toCheck) {
		int x = toCheck.getX();
		int y = toCheck.getY();
		Position[] positionsToCheck = {new Position(x + 1, y), new Position(x - 1, y), new Position(x, y + 1), new Position(x, y - 1), new Position(x + 1, y + 1), new Position(x + 1, y - 1), new Position(x - 1, y + 1), new Position(x - 1, y - 1)};
		for (Position p : positionsToCheck) {
			CacheObject g = ObjectLoader.object("Rotting branch", p.getX(), p.getY(), 0);
			GameObject o = ObjectHandler.getInstance().getObject(p.getX(), p.getY(), 0);
			if (o != null && o.getDef().getId() == 3511) {
				continue;
			}
			if (g != null && g.getDef().getId() == 3510) {
				return g;
			}
		}
		return null;
	}

	public static CacheObject findSuitableBush(final Position toCheck) {
		int x = toCheck.getX();
		int y = toCheck.getY();
		Position[] positionsToCheck = {new Position(x + 1, y), new Position(x - 1, y), new Position(x, y + 1), new Position(x, y - 1), new Position(x + 1, y + 1), new Position(x + 1, y - 1), new Position(x - 1, y + 1), new Position(x - 1, y - 1)};
		for (Position p : positionsToCheck) {
			CacheObject g = ObjectLoader.object("A small bush", p.getX(), p.getY(), 0);
			GameObject o = ObjectHandler.getInstance().getObject(p.getX(), p.getY(), 0);
			if (o != null && o.getDef().getId() == 3513) {
				continue;
			}
			if (g != null && g.getDef().getId() == 3512) {
				return g;
			}
		}
		return null;
	}

	public static void handleDruidicSpell(final Player player, final boolean isSickle) {
		if (isSickle && player.getSkill().getLevel()[Skill.PRAYER] == 0) {
			player.getActionSender().sendMessage("You need some prayer points to activate the power of the sickle.");
			return;
		}
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				int x = player.getPosition().getX();
				int y = player.getPosition().getY();
				int z = player.getPosition().getZ();
				player.getUpdateFlags().sendAnimation(811);
				player.getActionSender().createStillGfx(263, x + 1, y, z, 5);
				player.getActionSender().createStillGfx(263, x - 1, y, z, 5);
				player.getActionSender().createStillGfx(263, x, y + 1, z, 5);
				player.getActionSender().createStillGfx(263, x, y - 1, z, 5);
				player.getActionSender().createStillGfx(263, x + 1, y + 1, z, 5);
				player.getActionSender().createStillGfx(263, x + 1, y - 1, z, 5);
				player.getActionSender().createStillGfx(263, x - 1, y + 1, z, 5);
				player.getActionSender().createStillGfx(263, x - 1, y - 1, z, 5);
				if (!player.inMortMyreSwamp()) {
					player.getActionSender().sendMessage("This spell is only effective in the Mort Myre swamp.", true);
					b.stop();
					return;
				} else {
					player.getActionSender().sendMessage("You cast the spell in the swamp.", true);
				}
				final CacheObject log = findSuitableLog(player.getPosition());
				final CacheObject branch = findSuitableBranch(player.getPosition());
				final CacheObject bush = findSuitableBush(player.getPosition());
				if (log != null) {
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							ObjectHandler.getInstance().removeObject(log.getLocation().getX(), log.getLocation().getY(), 0, 10);
							new GameObject(3509, log.getLocation().getX(), log.getLocation().getY(), 0, log.getRotation(), 10, 0, 999999);
							ObjectHandler.getInstance().removeClip(log.getLocation().getX(), log.getLocation().getY(), 0, 10, 0);
						}
					}, 3);
				}
				if (branch != null) {
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							ObjectHandler.getInstance().removeObject(branch.getLocation().getX(), branch.getLocation().getY(), 0, 10);
							new GameObject(3511, branch.getLocation().getX(), branch.getLocation().getY(), 0, branch.getRotation(), 10, 0, 999999);
							ObjectHandler.getInstance().removeClip(branch.getLocation().getX(), branch.getLocation().getY(), 0, 10, 0);
						}
					}, 3);
				}
				if (bush != null) {
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							ObjectHandler.getInstance().removeObject(bush.getLocation().getX(), bush.getLocation().getX(), 0, 10);
							new GameObject(3513, bush.getLocation().getX(), bush.getLocation().getY(), 0, bush.getRotation(), 10, 0, 999999);
						}
					}, 3);
				}
				if (log == null && branch == null && bush == null) {
					player.getActionSender().sendMessage("There is no suitable material to be affected in this area.", true);
				} else {
					if (!isSickle) {
						if (player.getQuestStage(37) < BLESSED_SICKLE_GET) {
							if (log != null) {
								player.getInventory().replaceItemWithItem(new Item(DRUIDIC_SPELL), new Item(USED_SPELL));
							}
						} else {
							player.getInventory().replaceItemWithItem(new Item(DRUIDIC_SPELL), new Item(USED_SPELL));
						}
					}
				}
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
	}

	public void handleDeath(final Player player, final Npc died) {
		if (died.getNpcId() == GHAST_ATTACKABLE) {
			if (player.getQuestStage(37) == BLESSED_SICKLE_GET) {
				player.getQuestVars().setGhastsSlain(player.getQuestVars().getGhastsSlain() + 1);
				if (player.getQuestVars().getGhastsSlain() == 1) {
					player.getActionSender().sendMessage("That's one Ghast down. Two more to go");
				} else if (player.getQuestVars().getGhastsSlain() == 2) {
					player.getActionSender().sendMessage("That's two Ghasts, one more to kill.");
				} else if (player.getQuestVars().getGhastsSlain() == 3) {
					player.getActionSender().sendMessage("That's all 3 Ghasts. Return to the nature spirit.");
					player.setQuestStage(37, GHASTS_SLAIN);
				}
			}
		}
	}

	public boolean itemHandling(final Player player, final int itemId) {
		switch (itemId) {
			case DRUID_POUCH:
			case DRUID_POUCH_EMPTY:
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						ArrayList<Item> toAdd = new ArrayList<>();
						for (Item i : player.getInventory().getItemContainer().toArray()) {
							if (i == null) {
								continue;
							}
							if (i.getId() == MORT_MYRE_FUNGUS || i.getId() == MORT_MYRE_PEAR || i.getId() == MORT_MYRE_STEM) {
								toAdd.add(i);
							}
						}
						if (!toAdd.isEmpty()) {
							int count = 0;
							if (itemId == DRUID_POUCH_EMPTY) {
								player.getInventory().removeItem(toAdd.get(0));
								player.getInventory().replaceItemWithItem(new Item(DRUID_POUCH_EMPTY), new Item(DRUID_POUCH));
							} else {
								player.getInventory().replaceItemWithItem(toAdd.get(0), new Item(DRUID_POUCH));
							}
							toAdd.remove(0);
							count++;
							if (!toAdd.isEmpty()) {
								for (Item i : toAdd) {
									player.getInventory().replaceItemWithItem(i, new Item(DRUID_POUCH));
									count++;
								}
							}
							toAdd.clear();
							if (count == 1) {
								player.getActionSender().sendMessage("You add 1 natures harvest to your druid pouch.");
							} else {
								player.getActionSender().sendMessage("You add " + count + " natures harvests to your druid pouch.");
							}
							b.stop();
						} else {
							player.getActionSender().sendMessage("You have no suitable items to add to the pouch.");
							b.stop();
						}
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 2);
				return true;
			case DRUIDIC_SPELL:
				if (player.getQuestStage(37) >= BLESSED) {
					handleDruidicSpell(player, false);
				} else {
					player.getActionSender().sendMessage("You must be blessed in order to cast this druidic spell.");
				}
				return true;
			case JOURNAL:
				Dialogues.startDialogue(player, 10000 + JOURNAL);
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case 3521:
				if (item == SILVER_SICKLE) {
					player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
					player.getActionSender().sendMessage("You carefully dip your sickle in the altar of nature's water.");
					player.getInventory().replaceItemWithItem(new Item(SILVER_SICKLE), new Item(SILVER_SICKLE_B));
					return true;
				} else {
					return false;
				}
			case 3520: //inside grotto
				if (item == SILVER_SICKLE && player.getQuestStage(37) >= BLESSED_SICKLE_GET) {
					player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
					player.getActionSender().sendMessage("You carefully dip your sickle in the grotto's water.");
					player.getInventory().replaceItemWithItem(new Item(SILVER_SICKLE), new Item(SILVER_SICKLE_B));
					return true;
				} else {
					return false;
				}
			case 3527: //nature / left stone
				if (item == MORT_MYRE_FUNGUS && player.getQuestStage(37) == BLESSED) {
					player.getActionSender().sendMessage("The stone seems to absorb the fungus.");
					player.getUpdateFlags().sendAnimation(827);
					player.getInventory().removeItem(new Item(MORT_MYRE_FUNGUS));
					final GroundItem fungi = new GroundItem(new Item(MORT_MYRE_FUNGUS), player, player, new Position(3439, 3336, 0));
					GroundItemManager.getManager().dropItem(fungi);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							GroundItemManager.getManager().destroyItem(fungi);
							player.setStopPacket(false);
							player.getQuestVars().natureSpiritFungusPlaced = true;
						}
					}, 4);
					return true;
				} else {
					return false;
				}
			case 3529: //spell / right stone
				if ((item == DRUIDIC_SPELL || item == USED_SPELL) && player.getQuestStage(37) == BLESSED) {
					player.getActionSender().sendMessage("The stone seems to absorb the spell scroll.");
					player.getUpdateFlags().sendAnimation(827);
					player.getInventory().removeItem(new Item(item == DRUIDIC_SPELL ? DRUIDIC_SPELL : USED_SPELL));
					final GroundItem fungi = new GroundItem(new Item(item == DRUIDIC_SPELL ? DRUIDIC_SPELL : USED_SPELL), player, player, new Position(3441, 3336, 0));
					GroundItemManager.getManager().dropItem(fungi);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							GroundItemManager.getManager().destroyItem(fungi);
							player.setStopPacket(false);
							player.getQuestVars().natureSpiritSpellPlaced = true;
						}
					}, 4);
					return true;
				} else {
					return false;
				}
		}
		return false;
	}

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		if (npc.getNpcId() == FILLIMAN_TARLOCK) {
			if (itemId == JOURNAL) {
				if (player.getQuestStage(37) == QUEST_STARTED || player.getQuestStage(37) == TALKED_TO_FILLIMAN) {
					player.getDialogue().setLastNpcTalk(FILLIMAN_TARLOCK);
					player.getDialogue().sendNpcChat("Oh, keep hold of that, I may need it later.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				} else if (player.getQuestStage(37) == FILLIMAN_NEEDS_JOURNAL) {
					Dialogues.sendDialogue(player, FILLIMAN_TARLOCK, 20, 0);
					return true;
				}
				return false;
			}
			if (itemId == MIRROR) {
				if (player.getQuestStage(37) == TALKED_TO_FILLIMAN) {
					Dialogues.sendDialogue(player, FILLIMAN_TARLOCK, 5, 0);
				} else {
					player.getDialogue().setLastNpcTalk(FILLIMAN_TARLOCK);
					player.getDialogue().sendNpcChat("Oh, what a lovely mirror adventurer. Thank", "you for wordlessly touching me with it.", CONTENT);
					player.getDialogue().endDialogue();
				}
				return true;
			}
		}
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case 3517: //Search outside grotto
				player.getActionSender().sendMessage("It looks like a tree on a large rock with roots trailing to the ground.", true);
				return true;
			case 3520: //Search inside grotto
				Dialogues.startDialogue(player, player.getQuestStage(37) == ENTER_GROTTO ? FILLIMAN_TARLOCK : NATURE_SPIRIT);
				return true;
			case 3525:
			case 3526: //Grotto exit
				player.fadeTeleport(new Position(3440, 3337, 0));
				player.getActionSender().sendMessage("You prepare to exit the grotto.");
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						b.stop();
					}

					@Override
					public void stop() {
						player.getActionSender().sendMessage("You crawl back out of the grotto.");
					}
				}, 4);
				return true;
			case GROTTO_ENTRANCE:
				if (player.getQuestStage(37) == 1) {
					Dialogues.sendDialogue(player, FILLIMAN_TARLOCK, 50, 0);
				} else if (player.getQuestStage(37) >= 2 && player.getQuestStage(37) <= 5) {
					Dialogues.startDialogue(player, FILLIMAN_TARLOCK);
				} else {
					player.fadeTeleport(player.getQuestStage(37) < QUEST_COMPLETE ? new Position(3442, 9734, 0) : new Position(3442, 9734, 1));
					player.getActionSender().sendMessage("You prepare to enter the grotto.");
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							if (player.getQuestStage(37) == QUEST_COMPLETE) {
								player.getActionSender().sendMessage("You enter the beautiful Altar to Nature.");
							} else {
								player.getActionSender().sendMessage("You see a beautifully tended small grotto area.");
							}
						}
					}, 4);
				}
				return true;
			case GROTTO_BRIDGE:
				int targetY = player.getPosition().getY() < y ? 3 : -3;
				Agility.climbObstacle(player, x, y + targetY, player.getPosition().getZ(), 2750, 2, 0);
				return true;
			case 3507:
			case 3506: //Mort myre gate
				if (player.getPosition().getY() >= 3458) {
					player.getActionSender().sendMessage("You walk into the gloomy atmosphere of Mort Myre.", true);
				} else {
					player.getActionSender().sendMessage("Ulizius gives you a wink and a nod as you pass through the gate.", true);
				}
				player.getActionSender().walkThroughDoubleDoor(3506, 3507, 3444, 3458, 3443, 3458, 0);
				player.getActionSender().walkTo(0, player.getPosition().getY() < 3458 ? 1 : -1, true);
				return true;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {
			case 3513: //golden pear bush
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You pick a pear from the bush.", true);
						player.getUpdateFlags().sendAnimation(827);
						player.getInventory().addItemOrDrop(new Item(MORT_MYRE_PEAR));
						final CacheObject g = ObjectLoader.object(x, y, 0);
						if (g != null) {
							ObjectHandler.getInstance().removeObject(x, y, 0, 10);
							new GameObject(3512, x, y, 0, g.getRotation(), 10, 0, 999999);
						}
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 1);
				return true;
			case 3511: //budding branch
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You pick a stem from the branch.", true);
						player.getUpdateFlags().sendAnimation(827);
						player.getInventory().addItemOrDrop(new Item(MORT_MYRE_STEM));
						final CacheObject g = ObjectLoader.object(x, y, 0);
						if (g != null) {
							ObjectHandler.getInstance().removeObject(x, y, 0, 10);
							new GameObject(3510, x, y, 0, g.getRotation(), 10, 0, 999999);
							ObjectHandler.getInstance().removeClip(x, y, 0, 10, 0);
						}
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 1);
				return true;
			case 3509: //mushroom log
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You pick a mushroom from the log.", true);
						player.getUpdateFlags().sendAnimation(827);
						player.getInventory().addItemOrDrop(new Item(MORT_MYRE_FUNGUS));
						final CacheObject g = ObjectLoader.object(x, y, 0);
						if (g != null) {
							ObjectHandler.getInstance().removeObject(x, y, 0, 10);
							new GameObject(3508, x, y, 0, g.getRotation(), 10, 0, 999999);
							ObjectHandler.getInstance().removeClip(x, y, 0, 10, 0);
						}
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 1);
				return true;
			case 3517: //Search outside grotto
				if (!player.getInventory().playerHasItem(JOURNAL) && player.getQuestStage(37) < SOMETHING_FROM_NATURE) {
					player.getDialogue().sendStatement("You search the strange rock. You find a knot and", "inside of it you discover a small tome. The words on", "the front are a bit vague, but you make out the words", "'Tarlock' and 'journal'.");
					player.getInventory().addItem(new Item(JOURNAL));
					return true;
				}
		}
		return false;
	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case 35290: //right Stone
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("You search the stone and find that it has some sort of spirit symbol", "scratched into it.");
						d.endDialogue();
						return true;
				}
				return false;
			case 35280: //middle Stone
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("You search the stone and find that it has some sort of faith symbol", "scratched into it.");
						d.endDialogue();
						return true;
				}
				return false;
			case 35270: //left Stone
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("You search the stone and find that it has some sort of nature symbol", "scratched into it.");
						d.endDialogue();
						return true;
				}
				return false;
			case 10000 + JOURNAL:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("Most of the writing is pretty uninteresting, but", "something inside refers to a nature spirit. The", "requirements for which are...");
						return true;
					case 2:
						d.sendStatement("'Something from nature', 'something with faith' and", "'something of the spirit-to-become freely given'. It's all", "pretty vague.");
						d.endDialogue();
						return true;
				}
				return false;
			case NATURE_SPIRIT:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Welcome to my Altar to Nature! Thank you for", "your help friend! Don't forget to keep those Ghasts", "at bay!", HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
					case GHASTS_SLAIN:
						switch (d.getChatId()) {
							case 1:
								if (!NpcLoader.checkSpawn(player, NATURE_SPIRIT)) {
									NpcLoader.spawnNpc(player, new Npc(NATURE_SPIRIT), false, false);
								}
								if (player.getEquipment().getId(Constants.AMULET) != GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendNpcChat("Ahhrs OOooh ArhhhhAHHhhh...", DISTRESSED);
									d.endDialogue();
								} else {
									d.sendNpcChat("Hello again my friend, have you defeated three Ghasts", "as I asked you?", CONTENT);
								}
								return true;
							case 2:
								d.sendPlayerChat("Yes, I've killed all three and their spirits have been", "released!", HAPPY);
								return true;
							case 3:
								d.sendNpcChat("Many thanks my friend, you have completed your", "quest! I can now change this place into a holy", "sanctuary! And forever will it now be an Altar of", "Nature!", HAPPY);
								return true;
							case 4:
								player.fadeTeleport(new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										QuestHandler.completeQuest(player, 37);
										player.setStopPacket(false);
									}
								}, 5);
								return true;
						}
						return false;
					case BLESSED_SICKLE_GET:
						switch (d.getChatId()) {
							case 1:
								if (!NpcLoader.checkSpawn(player, NATURE_SPIRIT)) {
									NpcLoader.spawnNpc(player, new Npc(NATURE_SPIRIT), false, false);
								}
								if (player.getEquipment().getId(Constants.AMULET) != GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendNpcChat("Ahhrs OOooh ArhhhhAHHhhh...", DISTRESSED);
									d.endDialogue();
								} else {
									d.sendNpcChat("Now you can go forth and make the swamp bloom.", "Collect natures bounty to fill a druids pouch. So armed", "will the Ghasts be bound to you until, you flee or they", "are defeated.", CONTENT);
								}
								return true;
							case 2:
								d.sendNpcChat("Before I can make this grotto into an Altar of Nature,", "I need to be sure that the Ghasts will be kept at bay.", "Go forth into Mort Myre and slay three Ghasts. You'll", "be releasing their souls from Mort Myre.", CONTENT);
								if (player.getInventory().playerHasItem(DRUID_POUCH_EMPTY) || player.getInventory().playerHasItem(DRUID_POUCH)) {
									d.endDialogue();
								}
								return true;
							case 3:
								d.sendGiveItemNpc("The nature spirit gives you an empty pouch.", new Item(DRUID_POUCH_EMPTY));
								player.getInventory().addItemOrDrop(new Item(DRUID_POUCH_EMPTY));
								return true;
							case 4:
								d.sendNpcChat("You'll need this in order to collect together natures", "bounty. When it contains items, it will bind the Ghast to", "you until you flee or it is defeated.", CONTENT);
								d.endDialogue();
								return true;
							case 12:
								d.sendStatement("Your sickle has been blessed! @blu@If you lose the blessed", "@blu@sickle, you can bless a new sickle by dipping it in the", "@blu@grotto waters.");
								d.setNextChatId(1);
								return true;
						}
						return false;
					case TRANSFORMATION_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								if (!NpcLoader.checkSpawn(player, NATURE_SPIRIT)) {
									NpcLoader.spawnNpc(player, new Npc(NATURE_SPIRIT), false, false);
								}
								if (player.getEquipment().getId(Constants.AMULET) != GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendNpcChat("Ahhrs OOooh ArhhhhAHHhhh...", DISTRESSED);
									d.endDialogue();
								} else {
									d.sendNpcChat("Hmmm, good, the transformation is complete. Now, my", "friend, in return for your assistance, I will help you to", "kill the Ghasts. First bring to me a silver sickle so that I", "can bless it for you.", CONTENT);
								}
								return true;
							case 2:
								if (!player.getInventory().playerHasItem(SILVER_SICKLE)) {
									d.sendPlayerChat("A silver sickle? What's that?", CONTENT);
								} else {
									d.sendPlayerChat("Yes, here it is. What are you going to do with it?", CONTENT);
									d.setNextChatId(10);
								}
								return true;
							case 3:
								d.sendNpcChat("The sickle is the symbol and weapon of the Druid, you", "need to construct one of silver so that I can bless it,", "with its powers you will be able to defeat the Ghasts of", "Mort Myre.", CONTENT);
								return true;
							case 4:
								d.sendOption("Where would I get a silver sickle?", "What will you do to the silver sickle?", "How can a blessed sickle help me to defeat the Ghasts?", "Ok, thanks.");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Where would I get a silver sickle?", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("What will you do to the silver sickle?", CONTENT);
										d.setNextChatId(7);
										return true;
									case 3:
										d.sendPlayerChat("How can a blessed sickle help me to defeat the Ghasts?", CONTENT);
										d.setNextChatId(8);
										return true;
									case 4:
										d.sendPlayerChat("Ok, thanks.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 6:
								d.sendNpcChat("Use your imagination adventurer, you can craft", "one yourself, or buy one from another adventurer.", CONTENT);
								d.setNextChatId(4);
								return true;
							case 7:
								d.sendNpcChat("I will bless the sickle, allowing it to attack", "Ghasts and cast the Bloom spell without the spell", "being written on a piece of parchment.", CONTENT);
								d.setNextChatId(4);
								return true;
							case 8:
								d.sendNpcChat("The holiness of the sickle will be used to cause the Ghast", "to become rooted in the physical world, and able to be", "attacked. It will also help their poor souls rest.", CONTENT);
								d.setNextChatId(4);
								return true;
							case 10:
								d.sendNpcChat("My friend, I will bless it for you and you will then be", "able to accomplish great things. Now then, I must cast", "the enchantment. You can bless a new sickle by dipping", "it in the holy water of the grotto.", CONTENT);
								return true;
							case 11:
								d.endDialogue();
								player.getActionSender().removeInterfaces();
								final Npc filliman = player.getSpawnedNpc();
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									int count = 0;

									@Override
									public void execute(CycleEventContainer b) {
										if (count == 1) {
											b.stop();
										} else {
											if (filliman != null) {
												filliman.getUpdateFlags().sendAnimation(811);
												Position[] positions = {new Position(3441, 9741, 0), new Position(3442, 9741, 0), new Position(3442, 9740, 0), new Position(3441, 9740, 0)};
												for (Position p : positions) {
													int attackerX = p.getX(), attackerY = p.getY();
													int victimX = player.getPosition().getX(), victimY = player.getPosition().getY();
													final int offsetX = (attackerY - victimY) * -1;
													final int offsetY = (attackerX - victimX) * -1;
													World.sendProjectile(p, offsetX, offsetY, 268, 30, 45, 100, player.getIndex() - 1, true);
												}
												CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
													@Override
													public void execute(CycleEventContainer b) {
														b.stop();
													}

													@Override
													public void stop() {
														player.getInventory().replaceItemWithItem(new Item(SILVER_SICKLE), new Item(SILVER_SICKLE_B));
														player.setQuestStage(37, BLESSED_SICKLE_GET);
													}
												}, 3);
											} else {
												return;
											}
											count++;
										}
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										Dialogues.sendDialogue(player, NATURE_SPIRIT, 12, 0);
									}
								}, 4);
								return true;
						}
						return false;
				}
				return false;
			case FILLIMAN_TARLOCK:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case ENTER_GROTTO:
						switch (d.getChatId()) {
							case 1:
								if (!NpcLoader.checkSpawn(player, FILLIMAN_TARLOCK) && !NpcLoader.checkSpawn(player, NATURE_SPIRIT)) {
									NpcLoader.spawnNpc(player, new Npc(FILLIMAN_TARLOCK), false, false);
								}
								if (player.getEquipment().getId(Constants.AMULET) != GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendNpcChat("Ahhrs OOooh ArhhhhAHHhhh...", DISTRESSED);
									d.endDialogue();
								} else {
									if (player.getPosition().getY() < 9000) {
										d.sendNpcChat("Aha, everything seems to be in place! You can come", "through now into the grotto for the final section of my", "transformation.", HAPPY);
										d.endDialogue();
									} else {
										d.sendNpcChat("Well, hello there again, I was just enjoying the grotto.", "Many thanks for your help, I couldn't have become a", "Spirit of nature without you.", HAPPY);
									}
								}
								return true;
							case 2:
								d.sendNpcChat("I must complete the transformation now. Just stand", "there and watch the show, apparently it's quite good!", HAPPY);
								return true;
							case 3:
								d.endDialogue();
								player.getActionSender().removeInterfaces();
								final Npc filliman = player.getSpawnedNpc();
								if (filliman != null) {
									Following.resetFollow(filliman);
									filliman.walkTo(new Position(3441, 9737, 0), true);
								}
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									int count = 0;

									@Override
									public void execute(CycleEventContainer b) {
										if (count == 1) {
											b.stop();
										} else {
											if (filliman != null) {
												filliman.getUpdateFlags().sendAnimation(811);
												int attackerX = player.getPosition().getX(), attackerY = player.getPosition().getY();
												int victimX = filliman.getPosition().getX(), victimY = filliman.getPosition().getY();
												final int offsetX = (attackerY - victimY) * -1;
												final int offsetY = (attackerX - victimX) * -1;
												Position[] positions = {new Position(3439, 9741, 0), new Position(3444, 9741, 0), new Position(3438, 9738, 0), new Position(3438, 9736, 0), new Position(3445, 9737, 0), new Position(3445, 9736, 0), new Position(3441, 9735, 0)};
												for (Position p : positions) {
													World.sendProjectile(p, offsetX, offsetY, 268, 10, 43, 100, filliman.getIndex() + 1, false);
												}
												CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
													@Override
													public void execute(CycleEventContainer b) {
														b.stop();
													}

													@Override
													public void stop() {
														filliman.getUpdateFlags().sendGraphic(266);
														filliman.sendTransform(NATURE_SPIRIT, 10000);
														player.setQuestStage(37, TRANSFORMATION_COMPLETE);
													}
												}, 3);
											} else {
												return;
											}
											count++;
										}
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										Dialogues.startDialogue(player, NATURE_SPIRIT);
									}
								}, 4);
								return true;
						}
						return false;
					case BLESSED:
						switch (d.getChatId()) {
							case 1:
								if (!NpcLoader.checkSpawn(player, FILLIMAN_TARLOCK)) {
									NpcLoader.spawnNpc(player, new Npc(FILLIMAN_TARLOCK), false, false);
								}
								if (player.getEquipment().getId(Constants.AMULET) != GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendNpcChat("Ahhrs OOooh ArhhhhAHHhhh...", DISTRESSED);
									d.endDialogue();
								} else {
									if (!player.getQuestVars().showedFillimanFungus) {
										d.sendNpcChat("Did you manage to get something from nature?", CONTENT);
										d.setNextChatId(5);
									} else if (player.getQuestVars().showedFillimanFungus) {
										d.sendNpcChat("Hello again! I don't suppose you've found out what the", "other components of the Nature spell are have you?", CONTENT);
										d.setNextChatId(8);
									} else {
										d.sendPlayerChat("Hello, I've been blessed but I don't know", "what to do now.", CONTENT);
									}
								}
								return true;
							case 2:
								d.sendNpcChat("Well, you need to bring 'something from nature',", "'something with faith', and 'something of the spirit-to-", "become freely given'.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Yeah, but what does that mean?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Hmm, it is a conundrum, however, if you use that", "Bloom spell I gave you, you should be able to get", "something from nature. Once you have that, we may", "be able to puzzle the rest out.", CONTENT);
								d.endDialogue();
								return true;
							case 5:
								if (player.getInventory().playerHasItem(MORT_MYRE_FUNGUS)) {
									d.sendGiveItemNpc("You show the fungus to Filliman.", new Item(MORT_MYRE_FUNGUS));
								} else {
									d.sendPlayerChat("I'm afraid not...", SAD);
									d.endDialogue();
								}
								player.getQuestVars().showedFillimanFungus = true;
								return true;
							case 6:
								d.sendPlayerChat("Yes, I have a fungus here that I picked.", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Wonderful, the mushroom represents 'something from", "nature'. Now we need to work out what the other", "components of the spell are!", HAPPY);
								return true;
							case 8:
								d.sendOption("What are the things that are needed?", "What should I do when I have those things?", "I think I've solved the puzzle!", "Could I have another bloom scroll please?", "Ok, thanks.");
								return true;
							case 9:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("What are the things that are needed?", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("What should I do when I have those things?", CONTENT);
										d.setNextChatId(13);
										return true;
									case 3:
										d.sendPlayerChat("I think I've solved the puzzle!", CONTENT);
										d.setNextChatId(16);
										return true;
									case 4:
										d.sendPlayerChat("Could I have another bloom scroll please?", CONTENT);
										d.setNextChatId(19);
										return true;
									case 5:
										d.sendPlayerChat("Ok, thanks.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 10:
								d.sendNpcChat("The three things are: 'Something from faith', 'something", "from nature', and 'something of the spirit-to-become", "freely given'.", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("Ok, and 'something from nature' is the mushroom from", "the bloom spell you gave me?", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("Yes, that's correct, that seems right to me.", CONTENT);
								d.setNextChatId(8);
								return true;
							case 13:
								d.sendNpcChat("Ah yes, I looked this up. It says... 'to arrange upon", "three rocks around the spirit-to-become...'. Then I must", "cast a spell. As you can see, I've already placed the", "rocks. I must have planned to do this before I died!", CONTENT);
								return true;
							case 14:
								d.sendPlayerChat("Can we just place the components on any rock?", CONTENT);
								return true;
							case 15:
								d.sendNpcChat("Well, the only thing the journal says is that 'something", "with faith stands south of the spirit-to-become', but I'm", "so confused now I don't really know what that means.", "Oh, if only I had all my faculties!", SAD);
								d.setNextChatId(8);
								return true;
							case 16:
								d.sendNpcChat("Oh really... Have you placed all the items on the stones?", "Ok, well, lets try! @blu@~ The druid attempts to caste a spell. ~", HAPPY);
								return true;
							case 17:
								d.endDialogue();
								player.getActionSender().removeInterfaces();
								final Npc filliman = player.getSpawnedNpc();
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									int count = 0;
									boolean success = false;

									@Override
									public void execute(CycleEventContainer b) {
										if (count == 1) {
											b.stop();
										} else {
											if (filliman != null) {
												filliman.getUpdateFlags().sendAnimation(811);
												if (player.getQuestVars().natureSpiritFungusPlaced && player.getQuestVars().natureSpiritSpellPlaced && player.getPosition().getX() == 3440 && player.getPosition().getY() == 3335) {
													success = true;
													int attackerX = player.getPosition().getX(), attackerY = player.getPosition().getY();
													int victimX = filliman.getPosition().getX(), victimY = filliman.getPosition().getY();
													final int offsetX = (attackerY - victimY) * -1;
													final int offsetY = (attackerX - victimX) * -1;
													World.sendProjectile(new Position(3438, 3336, 0), offsetX, offsetY, 268, 10, 43, 100, filliman.getIndex() + 1, false);
													World.sendProjectile(new Position(3440, 3334, 0), offsetX, offsetY, 268, 10, 43, 100, filliman.getIndex() + 1, false);
													World.sendProjectile(new Position(3442, 3336, 0), offsetX, offsetY, 268, 10, 43, 100, filliman.getIndex() + 1, false);
													CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
														@Override
														public void execute(CycleEventContainer b) {
															b.stop();
														}

														@Override
														public void stop() {
															filliman.getUpdateFlags().sendGraphic(266);
														}
													}, 3);
												}
											}
											count++;
										}
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										Dialogues.sendDialogue(player, FILLIMAN_TARLOCK, success ? 25 : 18, 0);
									}
								}, 4);
								return true;
							case 18:
								d.sendNpcChat("Hmm, something still doesn't seem right. I think we", "need something more before we can continue.", SAD);
								d.endDialogue();
								return true;
							case 19:
								d.sendNpcChat("Sure, but please look after this one.", CONTENT);
								return true;
							case 20:
								d.sendGiveItemNpc("Filliman Tarlock's spirit hands you a bloom spell.", new Item(DRUIDIC_SPELL));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(DRUIDIC_SPELL));
								return true;
							case 25:
								d.sendNpcChat("Aha, everything seems to be in place! You can come", "through now into the grotto for the final section of my", "transformation.", HAPPY);
								d.endDialogue();
								player.setQuestStage(37, ENTER_GROTTO);
								return true;
						}
						return false;
					case SOMETHING_FROM_NATURE:
						switch (d.getChatId()) {
							case 1:
								if (!NpcLoader.checkSpawn(player, FILLIMAN_TARLOCK)) {
									NpcLoader.spawnNpc(player, new Npc(FILLIMAN_TARLOCK), false, false);
								}
								if (player.getEquipment().getId(Constants.AMULET) != GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendNpcChat("Ahhrs OOooh ArhhhhAHHhhh...", DISTRESSED);
									d.endDialogue();
								} else {
									d.sendPlayerChat("Hello again!", CONTENT);
								}
								return true;
							case 2:
								if (!player.getInventory().playerHasItem(DRUIDIC_SPELL)) {
									d.sendPlayerChat("I err, lost the spell.", SAD);
									return true;
								} else {
									d.sendNpcChat("Hm? What are you still doing here? Go get", "blessed in the Temple north of here!", "Hurry adventurer!", CONTENT);
									d.endDialogue();
									return true;
								}
							case 3:
								d.sendNpcChat("Luckily for you, I have another.", CONTENT);
								return true;
							case 4:
								d.sendGiveItemNpc("The druid produces a small sheet of papyrus.", new Item(DRUIDIC_SPELL));
								return true;
							case 5:
								d.sendNpcChat("This spell needs to be cast in the swamp after you have", "been blessed. I'm afraid you'll need to go to the temple", "to the North and ask a member of the clergy to bless", "you.", CONTENT);
								return true;
							case 6:
								d.sendPlayerChat("Blessed, what does that do?", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("It is required if you're to cast this druid spell. Once", "you've cast the spell, you should find something from", "nature. Bring it back to me and then we'll try to figure", "out the other things we need.", CONTENT);
								return true;
							case 8:
								d.sendStatement("Filliman hands you the spell.");
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(DRUIDIC_SPELL));
								return true;
						}
						return false;
					case FILLIMAN_NEEDS_JOURNAL:
						switch (d.getChatId()) {
							case 1:
								if (!NpcLoader.checkSpawn(player, FILLIMAN_TARLOCK)) {
									NpcLoader.spawnNpc(player, new Npc(FILLIMAN_TARLOCK), false, false);
								}
								if (player.getEquipment().getId(Constants.AMULET) != GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendNpcChat("Ahhrs OOooh ArhhhhAHHhhh...", DISTRESSED);
									d.endDialogue();
								} else {
									d.sendPlayerChat("Hello again!", CONTENT);
								}
								return true;
							case 2:
								d.sendNpcChat("Oh, hello... Sorry, you've caught me at a bad time, it's", "just that I've had a sign you see and I need to find", "my journal.", CONTENT);
								return true;
							case 3:
								if (!player.getInventory().playerHasItem(JOURNAL)) {
									d.sendPlayerChat("Where did you put it?", CONTENT);
								} else {
									d.sendPlayerChat("I have a journal right here Filliman.", CONTENT);
									d.setNextChatId(20);
								}
								return true;
							case 4:
								d.sendNpcChat("Well, if I knew that, I wouldn't still be looking for it.", "However, I do remember something about a knot?", "Perhaps I was meant to tie a knot or something?", SAD);
								d.endDialogue();
								return true;
							case 20:
								d.sendGiveItemNpc("You give the journal to Filliman Tarlock.", new Item(JOURNAL));
								return true;
							case 21:
								d.sendPlayerChat("I found this, maybe you can use it?", CONTENT);
								return true;
							case 22:
								d.sendNpcChat("My journal! That should help to collect my thoughts.", HAPPY);
								return true;
							case 23:
								d.sendStatement("~ The spirit starts leafing through the journal. ~", "~ He seems quite distant as he regards the pages. ~", "~ After some time the druid faces you again. ~");
								return true;
							case 24:
								d.sendNpcChat("It's all coming back to me now. It looks like I came to", "a violent and bitter end but that's not important now. I", "just have to figure out what I am going to do now?", CONTENT);
								return true;
							case 25:
								d.sendOption("Being dead, what options do you think you have?", "So, what's your plan?", "Well, good luck with that.", "How can I help?", "Ok, thanks.");
								return true;
							case 26:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Being dead, what options do you think you have? I'm", "not trying to be rude or anything, but it's not like you", "have many options is it? I mean, it's either up or down", "for you isn't it?", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("So, what's your plan?", CONTENT);
										d.setNextChatId(28);
										return true;
									case 3:
										d.sendPlayerChat("Well, good luck with that.", CONTENT);
										d.setNextChatId(29);
										return true;
									case 4:
										d.sendPlayerChat("How can I help?", CONTENT);
										d.setNextChatId(30);
										return true;
									case 5:
										d.sendPlayerChat("Ok, thanks.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 27:
								d.sendNpcChat("Hmm, well, you're a poetic one aren't you. Your", "material world logic stands you in good stead... If", "you're standing in the material world...", CONTENT);
								d.setNextChatId(25);
								return true;
							case 28:
								d.sendNpcChat("In my former incarnation I was Filliman Tarlock, a", "great druid of some power. I spent many years in this", "place, which was once a forest and I would wish to", "protect it as a nature spirit.", CONTENT);
								d.setNextChatId(25);
								return true;
							case 29:
								d.sendNpcChat("Won't you help me to become a nature spirit? I could", "really use your help!", CONTENT);
								d.setNextChatId(25);
								return true;
							case 30:
								d.sendNpcChat("Will you help me to become a nature spirit? The", "directions for becoming one are a bit vague, I need", "three things but I know how to get one of them.", "Perhaps you can help collect the rest?", CONTENT);
								return true;
							case 31:
								d.sendPlayerChat("I might be interested, what's involved?", CONTENT);
								return true;
							case 32:
								d.sendNpcChat("Well the book says, that I need, and I quote:", "'Something with faith', 'something from nature' and", "'something of the spirit-to-become freely given'. Hmm, I", "know how to get something from nature.", CONTENT);
								return true;
							case 33:
								d.sendPlayerChat("Well, that does seem a bit vague.", CONTENT);
								return true;
							case 34:
								d.sendNpcChat("Hmm, it does and I could understand if you didn't", "want to help. However, if you could perhaps at least get", "the item from nature, that would be a start. Perhaps we", "can figure out the rest as we go along.", CONTENT);
								return true;
							case 35:
								d.sendGiveItemNpc("The druid produces a small sheet of papyrus.", new Item(DRUIDIC_SPELL));
								return true;
							case 36:
								d.sendNpcChat("This spell needs to be cast in the swamp after you have", "been blessed. I'm afraid you'll need to go to the temple", "to the North and ask a member of the clergy to bless", "you.", CONTENT);
								return true;
							case 37:
								d.sendPlayerChat("Blessed, what does that do?", CONTENT);
								return true;
							case 38:
								d.sendNpcChat("It is required if you're to cast this druid spell. Once", "you've cast the spell, you should find something from", "nature. Bring it back to me and then we'll try to figure", "out the other things we need.", CONTENT);
								return true;
							case 39:
								d.sendStatement("Filliman hands you the spell.");
								d.endDialogue();
								player.getInventory().replaceItemWithItem(new Item(JOURNAL), new Item(DRUIDIC_SPELL));
								player.setQuestStage(37, SOMETHING_FROM_NATURE);
								return true;
						}
						return false;
					case TALKED_TO_FILLIMAN:
						switch (d.getChatId()) {
							case 1:
								if (!NpcLoader.checkSpawn(player, FILLIMAN_TARLOCK)) {
									NpcLoader.spawnNpc(player, new Npc(FILLIMAN_TARLOCK), false, false);
								}
								if (player.getEquipment().getId(Constants.AMULET) != GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendNpcChat("Ahhrs OOooh ArhhhhAHHhhh...", DISTRESSED);
									d.endDialogue();
								} else {
									d.sendPlayerChat("Hello again!", CONTENT);
								}
								return true;
							case 2:
								d.sendNpcChat("Oh, hello there, do you still think I'm dead? It's hard to", "see how I could be dead when I'm still in the world.", "I can see everything quite clearly. And nothing of what", "you say reflects the truth.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Yes, I do think you're dead and I'll prove it somehow.", CONTENT);
								if (!player.getInventory().playerHasItem(MIRROR)) {
									d.endDialogue();
								}
								return true;
							case 4:
								d.sendPlayerChat("Hmm...", CONTENT);
								return true;
							case 5:
								d.sendStatement("You use the mirror on the spirit of the dead", "Filliman Tarlock.");
								return true;
							case 6:
								d.sendPlayerChat("Here take a look at this, perhaps you can see that", "you're utterly transparent now!", CONTENT);
								return true;
							case 7:
								d.sendStatement("The spirit of Filliman reaches forwards and takes", "the mirror.");
								return true;
							case 8:
								d.sendNpcChat("Well, that is the most peculiar thing I've ever", "experienced. This mirror must somehow be", "dysfunctional. Strange how well it reflects the stagnant", "swamp behind me, but there is nothing of my own", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("visage apparent.", CONTENT);
								return true;
							case 10:
								d.sendPlayerChat("That's because you're dead! Dead as a door nail...", "Deader in fact... You bear a remarkable resemblance", "to worm bait! Err... No offense...", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("I think you may be right my friend, though I still feel", "very much alive. It is strange how I still come to be", "here and yet I've not turned into a Ghast.", Dialogues.CALM);
								return true;
							case 12:
								d.sendNpcChat("It must be a sign... Yes a sign... I must try to find", "out what it means. Now, where did I put my journal?", DISTRESSED);
								d.endDialogue();
								player.getInventory().removeItem(new Item(MIRROR));
								player.setQuestStage(37, FILLIMAN_NEEDS_JOURNAL);
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								if (player.getEquipment().getId(Constants.AMULET) == GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendNpcChat("Cannot wake up... Where am I?", SAD);
								} else {
									d.sendNpcChat("OOohohOOOOOOHHhh....", SAD);
								}
								return true;
							case 2:
								if (player.getEquipment().getId(Constants.AMULET) == GhostsAhoy.GHOSTSPEAK_AMULET) {
									d.sendPlayerChat("Huh? What's this?", CONTENT);
								} else {
									d.sendStatement("You need to be wearing a Ghostspeak Amulet to understand this ghost.");
									d.endDialogue();
								}
								return true;
							case 3:
								d.sendNpcChat("What did I write down now? Put it in the knot hole.", "Save me...", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("What are you talking about?", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("OH! I understand you! At last, someone who doesn't", "just mumble. I understand what you're saying!", HAPPY);
								return true;
							case 6:
								d.sendOption("I'm wearing an amulet of ghost speak!", "How long have you been a ghost?", "What's it like being a ghost?", "Do you believe in skeletons?", "Ok, thanks.");
								return true;
							case 7:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("I'm wearing an amulet of ghost speak!", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("How long have you been a ghost?", CONTENT);
										d.setNextChatId(15);
										return true;
									case 3:
										d.sendPlayerChat("What's it like being a ghost?", CONTENT);
										d.setNextChatId(18);
										return true;
									case 4:
										d.sendPlayerChat("Do you believe in skeletons?", CONTENT);
										d.setNextChatId(21);
										return true;
									case 5:
										d.sendPlayerChat("Ok, thanks.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 8:
								d.sendNpcChat("Why you poor fellow, have you passed away and you", "want to send a message back to a loved one?", CONTENT);
								return true;
							case 9:
								d.sendPlayerChat("Err... Not exactly...", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("You have come to haunt my dreams until I pass on", "your message to a dearly loved one. I understand.", "Pray, tell me who you would like me to pass a", "message on to?", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("Ermm, you don't understand... It's just that...", CONTENT);
								return true;
							case 12:
								d.sendNpcChat("Yes!", HAPPY);
								return true;
							case 13:
								d.sendPlayerChat("Well, please don't be upsert or anything...", "But you're the ghost!", CONTENT);
								return true;
							case 14:
								d.sendNpcChat("Don't be silly now! That in no way reflects the truth!", DISTRESSED);
								player.getQuestVars().fillimanOption1 = true;
								if (allFillimanOptions(player)) {
									d.setNextChatId(25);
								} else {
									d.setNextChatId(6);
								}
								return true;
							case 15:
								d.sendNpcChat("What?! Don't be preposterous! I'm not a ghost! How", "could you say something like that?", DISTRESSED);
								return true;
							case 16:
								d.sendPlayerChat("But it's true, you're a ghost... at least that is", "to say, you're sort of not alive anymore.", CONTENT);
								return true;
							case 17:
								d.sendNpcChat("Don't be silly, I can see you, I can see that tree. If I", "were dead, I wouldn't be able to see anything... What", "you say just doesn't reflect the truth. You'll have to try", "harder to pull one over on me!", CONTENT);
								player.getQuestVars().fillimanOption2 = true;
								if (allFillimanOptions(player)) {
									d.setNextChatId(25);
								} else {
									d.setNextChatId(6);
								}
								return true;
							case 18:
								d.sendNpcChat("Oh, it's quite... Oh... Trying to catch me out were you!", "Anyone can clearly see that I am not a ghost!", ANGRY_1);
								return true;
							case 19:
								d.sendPlayerChat("But you are a ghost, look at yourself! I can see", "straight through you! You're as dead as this swamp!", "Err.. No offense or anything...", CONTENT);
								return true;
							case 20:
								d.sendNpcChat("No I won't take offense because I'm not dead and I'm", "afraid you'll have to come up with some pretty", "conclusive proof before I believe it. What a strange", "dream this is.", CONTENT);
								player.getQuestVars().fillimanOption3 = true;
								if (allFillimanOptions(player)) {
									d.setNextChatId(25);
								} else {
									d.setNextChatId(6);
								}
								return true;
							case 21:
								d.sendNpcChat("Those scary things with all the bones?", "Heavens no!", CONTENT);
								return true;
							case 22:
								d.sendPlayerChat("But skeletons are quite real. You're lucky you", "didn't become one yourself, being dead and all.", CONTENT);
								return true;
							case 23:
								d.sendNpcChat("Gah! I'm not dead! These skeletons you speak of", "don't exist anyways! I could never become one!", "You can't fool me into thinking I'm dead to satiate", "whatever strange desire you have for me to be dead.", ANGRY_2);
								d.setNextChatId(6);
								return true;
							case 25:
								d.sendPlayerChat("I think that's all I need to know.", CONTENT);
								d.endDialogue();
								player.setQuestStage(37, TALKED_TO_FILLIMAN);
								return true;
							case 50:
								d.sendStatement("A shifting apparition appears in front of you.");
								d.setNextChatId(1);
								if (!NpcLoader.checkSpawn(player, FILLIMAN_TARLOCK)) {
									NpcLoader.spawnNpc(player, new Npc(FILLIMAN_TARLOCK), false, false);
								}
								return true;
						}
						return false;
				}
				return false;
			case DREZEL:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case SOMETHING_FROM_NATURE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Greetings again adventurer. How go your travels in", "Morytania? Is it as evil as I have heard?", Dialogues.HAPPY);
								return true;
							case 2:
								d.sendPlayerChat("Hello again! I'm helping Filliman, he plans to become a", "nature spirit. I have a spell to cast but first I need to", "be blessed. Can you bless me?", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("But you haven't sneezed!", Dialogues.LONGER_LAUGHING);
								return true;
							case 4:
								d.sendPlayerChat("Ha ha. Very funny.", ANGRY_1);
								return true;
							case 5:
								d.sendPlayerChat("Can you bless me or not?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Very well my friend, prepare yourself for the blessings", "of Saradomin. Here we go!", CONTENT);
								return true;
							case 7:
								d.endDialogue();
								player.getActionSender().removeInterfaces();
								final Npc drezel = getDrezel();
								drezel.getUpdateFlags().setForceChatMessage("Ashustru, blessidium, adverturasi, fidum!");
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									int count = 0;

									@Override
									public void execute(CycleEventContainer b) {
										if (count == 1) {
											b.stop();
										} else {
											drezel.getUpdateFlags().faceEntity(player.getFaceIndex());
											drezel.getUpdateFlags().sendAnimation(811);
											player.getUpdateFlags().sendAnimation(645);
											player.getUpdateFlags().sendGraphic(263);
											count++;
										}
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										Dialogues.sendDialogue(player, DREZEL, 8, 0);
									}
								}, 3);
								return true;
							case 8:
								d.sendNpcChat("There you go my friend, you're now blessed. It's", "funny, now I look at you, there seems to be something", "of the faith about you. Anyway, good luck with your", "quest!", HAPPY);
								return true;
							case 9:
								d.sendPlayerChat("Many thanks!", CONTENT);
								d.endDialogue();
								player.setQuestStage(37, BLESSED);
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Please take that food to Filliman, he'll probably appreciate", "a bit of cooked food. Now, he's never revealed where he", "lives in the swamps but I guess he'd be to the south.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case 0:
						switch (d.getChatId()) {
							case 1:
								if (this.canDoQuest(player)) {
									d.sendNpcChat("Greetings again adventurer. How go your travels in", "Morytania? Is it as evil as I have heard?", Dialogues.HAPPY);
									return true;
								} else {
									return false;
								}
							case 2:
								d.sendOption("Well, I'm going to look around a bit more.", "Is there anything else interesting to do around here?");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Well, I'm going to look around a bit more.", CONTENT);
										d.endDialogue();
										return true;
									case 2:
										d.sendPlayerChat("Is there anything else interesting to do around here?", CONTENT);
										d.setNextChatId(30);
										return true;
								}
							case 30:
								d.sendNpcChat("Well, not a great deal... but there is something you", "could do for me if you're interested. Though it is quite", "dangerous.", CONTENT);
								d.setNextChatId(4);
								return true;
							case 4:
								d.sendOption("Sorry, not interested...", "Well, what is it, I may be able to help?");
								return true;
							case 5:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Sorry, not interested...", CONTENT);
										d.endDialogue();
										return true;
									case 2:
										d.sendPlayerChat("Well, what is it, I may be able to help?", CONTENT);
										return true;
								}
							case 6:
								d.sendNpcChat("There's a man called Filliman who lives in Mort Myre,", "I wonder if you could look for him? The swamps of", "Mort Myre are dangerous though, they're infested with", "Ghasts!", CONTENT);
								return true;
							case 7:
								d.sendOption("Who is this Filliman?", "Where's Mort Myre?", "What's a Ghast?", "Yes, I'll go and look for him.", "Sorry, I don't think I can help.");
								return true;
							case 8:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Who is this Filliman?", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Where's Mort Myre?", CONTENT);
										d.setNextChatId(12);
										return true;
									case 3:
										d.sendPlayerChat("What's a Ghast?", CONTENT);
										d.setNextChatId(15);
										return true;
									case 4:
										d.sendPlayerChat("Yes, I'll go and look for him.", CONTENT);
										d.setNextChatId(20);
										return true;
									case 5:
										d.sendPlayerChat("Sorry, I don't think I can help.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 9:
								d.sendNpcChat("Filliman Tarlock is his full name and he's a Druid. He", "lives in Mort Myre much like a hermit, but there's", "many a traveller who he's helped.", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("Most people that come this way tell stories of when they", "were lost and paths that just seemed to 'open up' before", "them! I think it was Filliman Tarlock helping out.", HAPPY);
								d.setNextChatId(7);
								return true;
							case 12:
								d.sendNpcChat("Mort Myre is a decayed and dangerous swamp to the", "south. It was once a beautiful forest but has since", "become filled with vile emanations from within", "Morytania.", CONTENT);
								return true;
							case 13:
								d.sendNpcChat("The swamp decays everything. We put a fence around", "it to stop unwary travellers going in. Anyone who dies", "in the swamp is forever cursed to haunt it as a Ghast.", "Ghasts attack travellers, turning food to rotten filth.", CONTENT);
								d.setNextChatId(7);
								return true;
							case 15:
								d.sendNpcChat("A Ghast is a poor soul who died in Mort Myre.", "They're undead of a special class, they're untouchable", "as far as I'm aware!", CONTENT);
								return true;
							case 16:
								d.sendNpcChat("Filliman knew how to tackle them, but I've not heard", "from him in a long time. Ghasts, when they attack, will", "devour any food you have. If you have no food, they'll", "draw their nourishment from you!", CONTENT);
								d.setNextChatId(7);
								return true;
							case 20:
								d.sendNpcChat("That's great, but it is very dangerous. Are you sure", "you want to do this?", CONTENT);
								return true;
							case 21:
								d.sendOption("Yes, I'm sure.", "Sorry, I don't think I can help.");
								return true;
							case 22:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Yes, I'm sure.", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Sorry, I don't think I can help.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 23:
								d.sendNpcChat("That's great! Many thanks! Now then, please be aware", "of the Ghasts, you cannot attack them, only Filliman", "knew how to take them on.", HAPPY);
								return true;
							case 24:
								d.sendNpcChat("Just run from them if you can. If you start to get", "lost, try to make your way back to the temple.", CONTENT);
								return true;
							case 25:
								d.sendGiveItemNpc("The cleric hands you some food.", "", new Item(APPLE_PIE), new Item(APPLE_PIE));
								if (!player.getInventory().playerHasItem(new Item(APPLE_PIE, 3))) {
									player.getInventory().addItemOrDrop(new Item(APPLE_PIE, 3));
								}
								if (!player.getInventory().playerHasItem(new Item(MEAT_PIE, 3))) {
									player.getInventory().addItemOrDrop(new Item(MEAT_PIE, 3));
								}
								return true;
							case 26:
								d.sendNpcChat("Please take this food to Filliman, he'll probably appreciate", "a bit of cooked food. Now, he's never revealed where he", "lives in the swamps but I guess he'd be to the south,", "search for him will you?", CONTENT);
								return true;
							case 27:
								d.sendPlayerChat("I'll do my very best, don't worry, if he's in there and", "he's still alive I'll definitely find him.", CONTENT);
								d.endDialogue();
								QuestHandler.startQuest(player, 37);
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
