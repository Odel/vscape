package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.PLAIN_EVIL;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.dialogue.Dialogues.startDialogue;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class MerlinsCrystal implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int GAWAIN = 2;
	public static final int LANCELOT = 3;
	public static final int INTO_CRATE = 4;
	public static final int SLAY_MORDRED = 5;
	public static final int MORGAN = 6;
	public static final int EXCALIBUR = 7;
	public static final int EXCALIBUR_FAILED = 8;
	public static final int CHAOS_ALTAR = 9;
	public static final int SUMMON = 10;
	public static final int SMASH_CRYSTAL = 11;
	public static final int TALK_TO_ARTHUR = 12;
	public static final int QUEST_COMPLETE = 13;

	public int dialogueStage = 0;
	private int reward[][] = {};
	private int expReward[][] = {};

	private static final int questPointReward = 6;

	public int getQuestID() {
		return 11;
	}

	public String getQuestName() {
		return "Merlin's Crystal";
	}

	public String getQuestSaveName() {
		return "merlins-crystal";
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
		player.getActionSender().sendString("6 Quest Points", 12150);
		player.getActionSender().sendString("Excaliber", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7368);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);

			player.getActionSender().sendString("King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("The other knights may have some ideas on how to free him.", 8152);
		} else if (questStage == GAWAIN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);

			player.getActionSender().sendString("Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("Another knight may know how to get into Keep Le Faye.", 8155);
		} else if (questStage == LANCELOT) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);

			player.getActionSender().sendString("Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("Someone who works the docks nearby may know something.", 8158);
		} else if (questStage == INTO_CRATE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);

			player.getActionSender().sendString("You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("However, you can't climb into a crate near Arhein...", 8161);
		} else if (questStage == SLAY_MORDRED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);
			player.getActionSender().sendString("@str@" + "You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("@str@" + "However, you can't climb into a crate near Arhein...", 8161);

			player.getActionSender().sendString("You need a way to get Morgan Le Faye's attention.", 8163);
			player.getActionSender().sendString("Attacking the head guard may do the trick.", 8164);
			player.getActionSender().sendString("He should be on the top floor.", 8165);
		} else if (questStage == MORGAN) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);
			player.getActionSender().sendString("@str@" + "You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("@str@" + "However, you can't climb into a crate near Arhein...", 8161);
			player.getActionSender().sendString("@str@" + "You need a way to get Morgan Le Faye's attention.", 8163);
			player.getActionSender().sendString("@str@" + "Attacking the head guard may do the trick.", 8164);
			player.getActionSender().sendString("@str@" + "He should be on the top floor.", 8165);

			player.getActionSender().sendString("Morgan Le Faye has told you how to free Merlin.", 8167);
			player.getActionSender().sendString("You need Bat Bones, a Black Candle, and Excalibur.", 8168);
			player.getActionSender().sendString("Morgan said to visit the lake southeast of Taverly.", 8169);
		} else if (questStage == EXCALIBUR) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);
			player.getActionSender().sendString("@str@" + "You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("@str@" + "However, you can't climb into a crate near Arhein...", 8161);
			player.getActionSender().sendString("@str@" + "You need a way to get Morgan Le Faye's attention.", 8163);
			player.getActionSender().sendString("@str@" + "Attacking the head guard may do the trick.", 8164);
			player.getActionSender().sendString("@str@" + "He should be on the top floor.", 8165);
			player.getActionSender().sendString("@str@" + "Morgan Le Faye has told you how to free Merlin.", 8167);
			player.getActionSender().sendString("@str@" + "You need Bat Bones, a Black Candle, and Excalibur.", 8168);
			player.getActionSender().sendString("@str@" + "Morgan said to visit the lake southeast of Taverly.", 8169);

			player.getActionSender().sendString("The Lady of the Lake needs you to go to Port Sarim.", 8171);
			player.getActionSender().sendString("She said something about being pure of heart.", 8172);
			player.getActionSender().sendString("The Jewellery Store will have her package waiting.", 8173);
		} else if (questStage == EXCALIBUR_FAILED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);
			player.getActionSender().sendString("@str@" + "You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("@str@" + "However, you can't climb into a crate near Arhein...", 8161);
			player.getActionSender().sendString("@str@" + "You need a way to get Morgan Le Faye's attention.", 8163);
			player.getActionSender().sendString("@str@" + "Attacking the head guard may do the trick.", 8164);
			player.getActionSender().sendString("@str@" + "He should be on the top floor.", 8165);
			player.getActionSender().sendString("@str@" + "Morgan Le Faye has told you how to free Merlin.", 8167);
			player.getActionSender().sendString("@str@" + "You need Bat Bones, a Black Candle, and Excalibur.", 8168);
			player.getActionSender().sendString("@str@" + "Morgan said to visit the lake southeast of Taverly.", 8169);
			player.getActionSender().sendString("@str@" + "The Lady of the Lake needs you to go to Port Sarim.", 8171);
			player.getActionSender().sendString("@str@" + "She said something about being pure of heart.", 8172);
			player.getActionSender().sendString("@str@" + "The Jewellery Store will have her package waiting.", 8173);

			player.getActionSender().sendString("You failed the Lady's test.", 8175);
			player.getActionSender().sendString("Return to her with some bread for Excalibur.", 8176);
		} else if (questStage == CHAOS_ALTAR) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);
			player.getActionSender().sendString("@str@" + "You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("@str@" + "However, you can't climb into a crate near Arhein...", 8161);
			player.getActionSender().sendString("@str@" + "You need a way to get Morgan Le Faye's attention.", 8163);
			player.getActionSender().sendString("@str@" + "Attacking the head guard may do the trick.", 8164);
			player.getActionSender().sendString("@str@" + "He should be on the top floor.", 8165);
			player.getActionSender().sendString("@str@" + "Morgan Le Faye has told you how to free Merlin.", 8167);
			player.getActionSender().sendString("@str@" + "You need Bat Bones, a Black Candle, and Excalibur.", 8168);
			player.getActionSender().sendString("@str@" + "Morgan said to visit the lake southeast of Taverly.", 8169);
			player.getActionSender().sendString("@str@" + "The Lady of the Lake needs you to go to Port Sarim.", 8171);
			player.getActionSender().sendString("@str@" + "She said something about being pure of heart.", 8172);
			player.getActionSender().sendString("@str@" + "The Jewellery Store will have her package waiting.", 8173);

			player.getActionSender().sendString("You should head to Varrock to find the Chaos Altar.", 8175);
			player.getActionSender().sendString("You need to memorize the words of the incantation.", 8176);
		} else if (questStage == SUMMON) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);
			player.getActionSender().sendString("@str@" + "You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("@str@" + "However, you can't climb into a crate near Arhein...", 8161);
			player.getActionSender().sendString("@str@" + "You need a way to get Morgan Le Faye's attention.", 8163);
			player.getActionSender().sendString("@str@" + "Attacking the head guard may do the trick.", 8164);
			player.getActionSender().sendString("@str@" + "He should be on the top floor.", 8165);
			player.getActionSender().sendString("@str@" + "Morgan Le Faye has told you how to free Merlin.", 8167);
			player.getActionSender().sendString("@str@" + "You need Bat Bones, a Black Candle, and Excalibur.", 8168);
			player.getActionSender().sendString("@str@" + "Morgan said to visit the lake southeast of Taverly.", 8169);
			player.getActionSender().sendString("@str@" + "The Lady of the Lake needs you to go to Port Sarim.", 8171);
			player.getActionSender().sendString("@str@" + "She said something about being pure of heart.", 8172);
			player.getActionSender().sendString("@str@" + "The Jewellery Store will have her package waiting.", 8173);
			player.getActionSender().sendString("@str@" + "You should head to Varrock to find the Chaos Altar.", 8175);
			player.getActionSender().sendString("@str@" + "You need to memorize the words of the incantation.", 8176);

			player.getActionSender().sendString("The incantation read: Snarthon Candtrick Termanto.", 8178);
			player.getActionSender().sendString("You need to summon and subdue Trantax the Mighty.", 8179);
			player.getActionSender().sendString("Morgan said the pentagram to do so is behind the castle.", 8180);
		} else if (questStage == SMASH_CRYSTAL) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);
			player.getActionSender().sendString("@str@" + "You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("@str@" + "However, you can't climb into a crate near Arhein...", 8161);
			player.getActionSender().sendString("@str@" + "You need a way to get Morgan Le Faye's attention.", 8163);
			player.getActionSender().sendString("@str@" + "Attacking the head guard may do the trick.", 8164);
			player.getActionSender().sendString("@str@" + "He should be on the top floor.", 8165);
			player.getActionSender().sendString("@str@" + "Morgan Le Faye has told you how to free Merlin.", 8167);
			player.getActionSender().sendString("@str@" + "You need Bat Bones, a Black Candle, and Excalibur.", 8168);
			player.getActionSender().sendString("@str@" + "Morgan said to visit the lake southeast of Taverly.", 8169);
			player.getActionSender().sendString("@str@" + "The Lady of the Lake needs you to go to Port Sarim.", 8171);
			player.getActionSender().sendString("@str@" + "She said something about being pure of heart.", 8172);
			player.getActionSender().sendString("@str@" + "The Jewellery Store will have her package waiting.", 8173);
			player.getActionSender().sendString("@str@" + "You should head to Varrock to find the Chaos Altar.", 8175);
			player.getActionSender().sendString("@str@" + "You need to memorize the words of the incantation.", 8176);
			player.getActionSender().sendString("@str@" + "The incantation read: Snarthon Candtrick Termanto.", 8178);
			player.getActionSender().sendString("@str@" + "You need to summon and subdue Trantax the Mighty.", 8179);
			player.getActionSender().sendString("@str@" + "Morgan said the pentagram to do so is behind the castle.", 8180);

			player.getActionSender().sendString("With the spirit subdued, you can smash Merlin's crystal.", 8182);
			player.getActionSender().sendString("It's stored in the southeast tower of the castle.", 8183);
		} else if (questStage == TALK_TO_ARTHUR) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);
			player.getActionSender().sendString("@str@" + "You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("@str@" + "However, you can't climb into a crate near Arhein...", 8161);
			player.getActionSender().sendString("@str@" + "You need a way to get Morgan Le Faye's attention.", 8163);
			player.getActionSender().sendString("@str@" + "Attacking the head guard may do the trick.", 8164);
			player.getActionSender().sendString("@str@" + "He should be on the top floor.", 8165);
			player.getActionSender().sendString("@str@" + "Morgan Le Faye has told you how to free Merlin.", 8167);
			player.getActionSender().sendString("@str@" + "You need Bat Bones, a Black Candle, and Excalibur.", 8168);
			player.getActionSender().sendString("@str@" + "Morgan said to visit the lake southeast of Taverly.", 8169);
			player.getActionSender().sendString("@str@" + "The Lady of the Lake needs you to go to Port Sarim.", 8171);
			player.getActionSender().sendString("@str@" + "She said something about being pure of heart.", 8172);
			player.getActionSender().sendString("@str@" + "The Jewellery Store will have her package waiting.", 8173);
			player.getActionSender().sendString("@str@" + "You should head to Varrock to find the Chaos Altar.", 8175);
			player.getActionSender().sendString("@str@" + "You need to memorize the words of the incantation.", 8176);
			player.getActionSender().sendString("@str@" + "The incantation read: Snarthon Candtrick Termanto.", 8178);
			player.getActionSender().sendString("@str@" + "You need to summon and subdue Trantax the Mighty.", 8179);
			player.getActionSender().sendString("@str@" + "Morgan said the pentagram to do so is behind the castle.", 8180);
			player.getActionSender().sendString("@str@" + "With the spirit subdued, you can smash Merlin's crystal.", 8182);
			player.getActionSender().sendString("@str@" + "King Arthur stored in the southeast tower of the castle.", 8183);

			player.getActionSender().sendString("Talk to King Arthur for your reward.", 8185);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
			player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
			player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
			player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
			player.getActionSender().sendString("@str@" + "Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
			player.getActionSender().sendString("@str@" + "Another knight may know how to get into Keep Le Faye.", 8155);
			player.getActionSender().sendString("@str@" + "Lancelot said the only way into the Keep is the sea.", 8157);
			player.getActionSender().sendString("@str@" + "Someone who works the docks nearby may know something.", 8158);
			player.getActionSender().sendString("@str@" + "You found Arhein and devised a way to hitch a ride.", 8160);
			player.getActionSender().sendString("@str@" + "However, you can't climb into a crate near Arhein...", 8161);
			player.getActionSender().sendString("@str@" + "You need a way to get Morgan Le Faye's attention.", 8163);
			player.getActionSender().sendString("@str@" + "Attacking the head guard may do the trick.", 8164);
			player.getActionSender().sendString("@str@" + "He should be on the top floor.", 8165);
			player.getActionSender().sendString("@str@" + "Morgan Le Faye has told you how to free Merlin.", 8167);
			player.getActionSender().sendString("@str@" + "You need Bat Bones, a Black Candle, and Excalibur.", 8168);
			player.getActionSender().sendString("@str@" + "Morgan said to visit the lake southeast of Taverly.", 8169);
			player.getActionSender().sendString("@str@" + "The Lady of the Lake needs you to go to Port Sarim.", 8171);
			player.getActionSender().sendString("@str@" + "She said something about being pure of heart.", 8172);
			player.getActionSender().sendString("@str@" + "The Jewellery Store will have her package waiting.", 8173);
			player.getActionSender().sendString("@str@" + "You should head to Varrock to find the Chaos Altar.", 8175);
			player.getActionSender().sendString("@str@" + "You need to memorize the words of the incantation.", 8176);
			player.getActionSender().sendString("@str@" + "The incantation read: Snarthon Candtrick Termanto.", 8178);
			player.getActionSender().sendString("@str@" + "You need to summon and subdue Trantax the Mighty.", 8179);
			player.getActionSender().sendString("@str@" + "Morgan said the pentagram to do so is behind the castle.", 8180);
			player.getActionSender().sendString("@str@" + "With the spirit subdued, you can now smash Merlin's crystal.", 8182);
			player.getActionSender().sendString("@str@" + "King Arthur has it stored in the southeast tower of the castle.", 8183);

			player.getActionSender().sendString("@red@ You have completed this quest!", 8185);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("To start the quest, you should talk to King Arthur", 8147);
			player.getActionSender().sendString("found in Camelot's castle.", 8148);
			player.getActionSender().sendString("-You must be able to defeat a level 39 enemy.", 8150);
			player.getActionSender().sendString("-(Optional)You must be able to defeat a level 92 enemy.", 8151);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7368);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7368);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7368);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7368);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("To start the quest, you should talk to King Arthur", 8147);
		player.getActionSender().sendString("found in Camelot's castle.", 8148);
		player.getActionSender().sendString("-You must be able to defeat a level 39 enemy.", 8150);
		player.getActionSender().sendString("-(Optional)You must be able to defeat a level 92 enemy.", 8151);
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public static boolean workerSpawned() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 675) {
				return true;
			}
		}
		return false;
	}

	public static boolean beggarSpawned() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 252) {
				return true;
			}
		}
		return false;
	}

	public static boolean merlinSpawned() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}
			if (npc.getNpcId() == 249) {
				return true;
			}
		}
		return false;
	}

	public static void summon(Player player) {
		player.getActionSender().walkTo(-1, 0, true);
		Npc npc = new Npc(238);
		npc.setPosition(new Position(2780, 3515, 0));
		npc.setSpawnPosition(new Position(2780, 3515, 0));
		World.register(npc);
		npc.setPlayerOwner(player.getIndex());
		Dialogues.startDialogue(player, 238);
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
		switch (object) {
			case 71:
				if (x == 2763 && y == 3402) {
					if (player.getQuestStage(11) < 6) {
						player.getDialogue().sendStatement("These doors are locked.");
						return true;
					} else {
						player.getActionSender().walkTo(player.getPosition().getX() == 2764 ? -1 : 1, 0, true);
						player.getActionSender().walkThroughDoubleDoor(71, 72, 2763, 3402, 2763, 3401, 0);
						return true;
					}
				}
			case 72:
				if (x == 2763 && y == 3401) {
					if (player.getQuestStage(11) < 6) {
						player.getDialogue().sendStatement("These doors are locked.");
						return true;
					} else {
						player.getActionSender().walkTo(player.getPosition().getX() == 2764 ? -1 : 1, 0, true);
						player.getActionSender().walkThroughDoubleDoor(71, 72, 2763, 3402, 2763, 3401, 0);
						return true;
					}
				}
			case 59:
				if (x == 3016 && y == 3246) {
					if (player.getQuestStage(11) == 7) {
						if (!MerlinsCrystal.beggarSpawned()) {
							Npc npc = new Npc(252);
							npc.setPosition(new Position(3016, 3253, 0));
							npc.setSpawnPosition(player.getPosition().clone());
							World.register(npc);
							npc.setFollowingEntity(player);
							npc.setPlayerOwner(player.getIndex());
						}
						Dialogues.startDialogue(player, 252);
						return true;
					} else {
						player.getActionSender().walkTo(player.getPosition().getX() > 3015 ? -1 : 1, 0, true);
						player.getActionSender().walkThroughDoor(59, 3016, 3246, 0);
						return true;
					}
				}
			case 62: //merlin's crystal prison
				if (player.getQuestStage(11) == 11) {
					if (player.getEquipment().getItemContainer().get(Constants.WEAPON).getId() == 35) {
						if (!MerlinsCrystal.merlinSpawned()) {
							Npc npc = new Npc(249);
							npc.setPosition(new Position(2763, 3512, 0));
							npc.setSpawnPosition(new Position(2763, 3512, 0));
							World.register(npc);
						}
						player.getUpdateFlags().sendAnimation(451);
						player.getActionSender().sendStillGraphic(new Graphic(287, 0), new Position(2767, 3494));
						player.getActionSender().sendStillGraphic(new Graphic(287, 0), new Position(2767, 3493));
						player.getActionSender().sendStillGraphic(new Graphic(287, 0), new Position(2768, 3494));
						player.getActionSender().sendStillGraphic(new Graphic(287, 0), new Position(2768, 3493));
						player.fadeTeleport(new Position(2764, 3512, 0));
						player.setQuestStage(11, 12);
					} else {
						player.getDialogue().sendStatement("You need to equip Excalibur first.");
					}
					return true;
				}
				return false;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, int x, int y) {
		switch (object) {
			case 63: //merlin's crystal crate
				if (player.getQuestStage(11) >= 4 && player.getPosition().clone() != new Position(2801, 3442)) {
					player.teleport(new Position(2801, 3442));
					player.getActionSender().sendMessage("You climb into the crate.");
					Dialogues.startDialogue(player, 10505);
				}
				return true;
			case 61: //chaos altar
				if (player.getQuestStage(11) == 9) {
					player.getDialogue().sendStatement("You find some words inscribed on the edge of the altar...", "They read: Snarthon Candtrick Termanto.");
					player.setQuestStage(11, 10);
					return true;
				}
				return false;
		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case 10505:
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.transformNpc = 152;
						player.setAppearanceUpdateRequired(true);
						player.getDialogue().sendStatement("You barely fit into the crate, and begin to wait.");
						player.getActionSender().sendWalkableInterface(8677);
						player.getActionSender().sendMapState(2);
						HitDef hitDef = new HitDef(null, HitType.NORMAL, 0).setStartingHitDelay(100000);
						Hit hit = new Hit(player, player, hitDef);
						BindingEffect bind = new BindingEffect(1000000);
						bind.initialize(hit);
						return true;
					case 2:
						player.getDialogue().sendStatement("You hear voices approaching.");
						return true;
					case 3:
						startDialogue(player, 675);
						return true;
				}
				break;
			//public static final int SHIPYARD_WORKER = 675;
			case 675: //shipyard worker / crate guy
				if (player.getQuestStage(11) == 4 || player.getQuestStage(11) == 5) {
					switch (player.getDialogue().getChatId()) {
						case 1:
							player.getDialogue().sendNpcChat("*Singing*", "Load 'em up, move 'em out, load 'em up...", CONTENT);
							return true;
						case 2:
							player.getDialogue().sendStatement("Your crate shakes a little. You hear the worker grunt.");
							return true;
						case 3:
							player.getDialogue().sendNpcChat("Woah, this one is a bit heavier than usual.", "Must be the boss man fuckin' with me.", LAUGHING);
							return true;
						case 4:
							player.getActionSender().sendMessage("You feel your crate being carried...and dropped off after a long voyage.");
							player.fadeTeleport(new Position(2779, 3400));
							player.setQuestStage(11, 5);
							player.getDialogue().endDialogue();
							player.transformNpc = -1;
							player.resetEffects();
							player.getActionSender().sendWalkableInterface(-1);
							player.getActionSender().sendMapState(0);
							return true;
					}
				}
				break;
			case 250: //lady of the lake
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(11) < 6) {
							player.getDialogue().sendStatement("The Lady doesn't seem to want to chat.");
							player.getDialogue().endDialogue();
							return true;
						} else if (player.getQuestStage(11) == 8) {
							player.getDialogue().sendNpcChat("Well, did you get my bread?", CONTENT);
							player.getDialogue().setNextChatId(20);
							return true;
						} else if (player.getQuestStage(11) >= 9) {
							player.getDialogue().sendNpcChat("Well, it seems you are pure of heart.", HAPPY);
							player.getDialogue().setNextChatId(15);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("Greetings fair maiden.", "I was sent to recover the sword Excalibur.", "Merlin the Great needs freeing!", CONTENT);
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("Ah, yes. Merlin. A wonderful soul.", "I do have the sword, but it is only", "to be carried by the pure of heart.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("Well, that would be me!", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("You young ones are always so eager.", "I need you to do me a favor first.", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendPlayerChat("Of course! What do I need to slay!", LAUGHING);
						return true;
					case 6:
						player.getDialogue().sendNpcChat("Nothing that gruesome, young one.", "I need you to pick up a parcel for me.", "It's being held in Port Sarim...", "...Go to the Jewellery Store to pick it up.", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendPlayerChat("I'll do that right away!", HAPPY);
						player.getDialogue().endDialogue();
						player.setQuestStage(11, 7);
						return true;
					case 9:
						for (Npc npc : World.getNpcs()) {
							if (npc == null) {
								continue;
							}
							if (npc.getNpcId() == 252) {
								npc.sendTransform(250, 1000);
							}
						}
						player.getDialogue().sendNpcChat("Well, it seems you are not very pure of heart.", ANNOYED);
						return true;
					case 10:
						player.getDialogue().sendNpcChat("Return to me personally in Taverly with the bread.", "Only then can you have Excalibur.", ANNOYED);
						for (Npc npc : World.getNpcs()) {
							if (npc == null) {
								continue;
							}
							if (npc.getNpcId() == 250 && Misc.goodDistance(player.getPosition().clone(), npc.getPosition().clone(), 5) && player.getPosition().getX() > 3005) {
								player.getActionSender().sendStillGraphic(86, npc.getPosition().clone(), 0);
								npc.setVisible(false);
								World.unregister(npc);
								break;
							}
						}
						player.getDialogue().endDialogue();
						return true;
					case 14:
						for (Npc npc : World.getNpcs()) {
							if (npc == null) {
								continue;
							}
							if (npc.getNpcId() == 252) {
								npc.sendTransform(250, 1000);
							}
						}
						player.getDialogue().sendNpcChat("Well, it seems you are pure of heart.", HAPPY);
						return true;
					case 15:
						if (!player.getInventory().ownsItem(35)) {
							player.getDialogue().sendNpcChat("Here is the sword Excalibur, wield it well.", CONTENT);
							player.getInventory().addItem(new Item(35));
							player.getDialogue().endDialogue();
							if (player.getQuestStage(11) == 9) {
								for (Npc npc : World.getNpcs()) {
									if (npc == null) {
										continue;
									}
									if (npc.getNpcId() == 250 && Misc.goodDistance(player.getPosition().clone(), npc.getPosition().clone(), 5) && player.getPosition().getX() > 3005) {
										player.getActionSender().sendStillGraphic(86, npc.getPosition().clone(), 0);
										npc.setVisible(false);
										World.unregister(npc);
										break;
									}
								}
							}
							return true;
						} else {
							player.getDialogue().sendNpcChat("You already have Excalibur.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
					case 20:
						if (player.getInventory().playerHasItem(2309)) {
							player.getDialogue().sendPlayerChat("Yes m'lady, here it is.", CONTENT);
							player.getInventory().removeItem(new Item(2309));
							player.getDialogue().setNextChatId(15);
							player.setQuestStage(11, 9);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("I'm afraid not...", SAD);
							player.getDialogue().endDialogue();
							return true;
						}
				}
				break;
			case 249: //merlin
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Well, that was quite the exit!", LAUGHING);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("Are you alright?!", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Of course lad, I can't thank you enough.", CONTENT);
						for (Npc npc : World.getNpcs()) {
							if (npc == null) {
								continue;
							}
							if (npc.getNpcId() == 249) {
								player.getActionSender().sendStillGraphic(86, npc.getPosition().clone(), 0);
								npc.setVisible(false);
								World.unregister(npc);
							}
						}
						return true;
					case 4:
						Dialogues.sendDialogue(player, 251, 2, 0);
						return true;
				}
				break;
			case 238: //thrantax the mighty spirit
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendOption("Snartahon Candytick Tormanto.", "Snarthon Candtrick Termanto.", "Snarfon Candletrick Tormento.", "Snorthol Sendtick Tomato.");
						return true;
					case 2:
						switch (optionId) {
							case 2:
								player.getDialogue().sendStatement("Thrantax fades, the incantation worked.");
								for (Npc npc : World.getNpcs()) {
									if (npc == null) {
										continue;
									}
									if (npc.getNpcId() == 238) {
										npc.setVisible(false);
										World.unregister(npc);
									}
								}
								player.getDialogue().endDialogue();
								if (player.getQuestStage(11) == 10) {
									player.setQuestStage(11, 11);
								}
								return true;
							case 1:
							case 3:
							case 4:
								for (Npc npc : World.getNpcs()) {
									if (npc == null) {
										continue;
									}
									if (npc.getNpcId() == 238) {
										CombatManager.attack(npc, player);
									}
								}
						}
				}
				break;
			case 252: //beggar
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("S'cuse me sir, could you spare some bread?", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendOption("Yes.", "No, you dirty beggar.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								if (player.getInventory().playerHasItem(2309)) {
									player.getDialogue().sendStatement("You hand the beggar a loaf of bread.");
									player.getInventory().removeItem(new Item(2309));
									if (player.getQuestStage(11) == 7) {
										player.setQuestStage(11, 9);
									}
									Dialogues.sendDialogue(player, 250, 14, 0);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I'm afraid I don't have any bread.", SAD);
									player.getDialogue().setNextChatId(15);
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("No, you dirty beggar.", ANGRY_1);
								if (player.getQuestStage(11) == 7) {
									player.setQuestStage(11, 8);
								}
								Dialogues.sendDialogue(player, 250, 9, 0);
								return true;
						}
					case 15:
						player.getDialogue().sendNpcChat("Well, tha's okay mista, I'll be waiting here...", SAD);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 248: //morgan le faye
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(11) == 5) {
							player.getDialogue().sendNpcChat("STOP! Please don't hurt my son!", CONTENT);
							return true;
						}
					case 2:
						player.getDialogue().sendPlayerChat("Erm... That's your son?", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendNpcChat("Yes, who else would I choose to guard my keep?!", ANGRY_2);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("If you tell me how to free Merlin", "I'll let him live.", PLAIN_EVIL);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("Yes, of course, anything for his life.", "You will need 3 primary things to free him.", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendNpcChat("First you need to obtain some bones.", "Bones of a bat.", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendNpcChat("Secondly, you'll need a black candle.", CONTENT);
						return true;
					case 8:
						player.getDialogue().sendNpcChat("And finally, err...", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendPlayerChat("What's the matter? Should I kill him?", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendNpcChat("NO! It's just that... Well...", "...The third thing you need is...", "...the legendary sword Excalibur.", CONTENT);
						return true;
					case 11:
						player.getDialogue().sendPlayerChat("Of course. It's never easy with you folk.", "Where do you suggest I find the sword?", CONTENT);
						return true;
					case 12:
						player.getDialogue().sendNpcChat("The ancient keeper of the blade can", "be found southeast of Taverly.", "On the peninsula jutting into the big lake.", CONTENT);
						return true;
					case 13:
						player.getDialogue().sendPlayerChat("And what do I do with all of these items?", CONTENT);
						return true;
					case 14:
						player.getDialogue().sendNpcChat("You'll need the bones and the candle to", "summon the spirit Thrantax the Mighty.", "The pentragram to summon him can be found", "northeast, inside the fence of Camelot Castle.", CONTENT);
						return true;
					case 15:
						player.getDialogue().sendNpcChat("You will then light the candle, and", "drop the bones on the pentagram.", "However, you will need to say a proper incantation.", CONTENT);
						return true;
					case 16:
						player.getDialogue().sendPlayerChat("Incantation?", CONTENT);
						return true;
					case 17:
						player.getDialogue().sendNpcChat("Yes, a failsafe in case anyone got close to", "figuring out how to free Merlin...", "...but I'll tell you where to find the words.", CONTENT);
						return true;
					case 18:
						player.getDialogue().sendPlayerChat("You had better make it quick...", "...Training in combat is my forte.", "And your son still has some lifepoints left.", CONTENT);
						return true;
					case 19:
						player.getDialogue().sendNpcChat("Yes, yes! The words can be found on the", "chaos altar in Varrock, look there.", "After you have compelled the spirit to free Merlin...", CONTENT);
						return true;
					case 20:
						player.getDialogue().sendNpcChat("Just smash the crystal Merlin is held in.", "Smash it with Excalibur only.", CONTENT);
						return true;
					case 21:
						player.getDialogue().sendPlayerChat("That seems to be all the information I need.", CONTENT);
						player.setQuestStage(11, 6);
						return true;
					case 22:
						player.getDialogue().sendPlayerChat("Your son shall live.", CONTENT);
						for (Npc npc : World.getNpcs()) {
							if (npc == null) {
								continue;
							}
							if (npc.getNpcId() == 247) {
								npc.setVisible(false);
								World.unregister(npc);
								NpcDefinition newNpc = NpcDefinition.forId(247);
								player.appendToAutoSpawn(newNpc);
							}
						}
						return true;
					case 23:
						player.getDialogue().sendNpcChat("Thank you, now leave this place!", CONTENT);
						player.getDialogue().endDialogue();
						for (Npc npc : World.getNpcs()) {
							if (npc == null) {
								continue;
							}
							if (npc.getNpcId() == 248) {
								player.getActionSender().sendStillGraphic(86, npc.getPosition().clone(), 0);
								npc.setVisible(false);
								World.unregister(npc);
							}
						}
						return true;
				}
				break;
			case 241: //useless knights
			case 242:
			case 243:
			case 244:
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(11) == 1) {
							player.getDialogue().sendPlayerChat("Do you have any ideas on how to free Merlin?", CONTENT);
							return true;
						} else if (player.getQuestStage(11) == 2) {
							player.getDialogue().sendPlayerChat("Do you have any ideas on how to get into Keep Le Faye?", CONTENT);
							player.getDialogue().setNextChatId(3);
							return true;
						} else {
							player.getDialogue().sendNpcChat("Begone! I have nothing to say to you.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("No, unfortunately not.", "I do hope we find a way to free him.", SAD);
						player.getDialogue().endDialogue();
						return true;
					case 3:
						player.getDialogue().sendNpcChat("The Keep is impenetrable!", "Or so I hear atleast.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 240: //gawain
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(11) != 1) {
							player.getDialogue().sendNpcChat("Begone! I have nothing to say to you.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						} else if (player.getQuestStage(11) == 1) {
							player.getDialogue().sendPlayerChat("Do you have any ideas on how to free Merlin?", CONTENT);
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("Hmm. Morgan Le Faye would surely know.", "However, her keep is nearly impossible to enter.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("Well then how do I gain entry to the keep?", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("I-I... don't k-know.", "Perhaps one of the other knights do...", SAD);
						player.getDialogue().endDialogue();
						player.setQuestStage(11, 2);
						return true;
				}
				break;
			case 239: //lancelot
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(11) != 2) {
							player.getDialogue().sendNpcChat("Begone! I have nothing to say to you.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						} else if (player.getQuestStage(11) == 2) {
							player.getDialogue().sendPlayerChat("Do you have any ideas on how to get into Keep Le Faye?", CONTENT);
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("Come closer, it's not safe to say aloud.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendStatement("You lean in close to Sir Lancelot.");
						return true;
					case 4:
						player.getDialogue().sendNpcChat("The sea is the secret to getting into the keep.", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(11, 3);
						return true;
				}
				break;
			case 563: //arhein on the dock of the bay
				switch (player.getDialogue().getChatId()) {
					case 1:
						if (player.getQuestStage(11) != 3) {
							return false;
						} else {
							player.getDialogue().sendPlayerChat("Hey! You have a ship!", "Can you take me to Keep Le Faye?", "...It's not far from here!", CONTENT);
							return true;
						}
					case 2:
						player.getDialogue().sendNpcChat("Fool! I only carry cargo!", "Why would I ever take you anywhere?", ANGRY_2);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("Perhaps a bit of gold could change your mind?", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendNpcChat("I told you once, Guthix help me if", "I have to tell you twice boy!", ANNOYED);
						return true;
					case 5:
						player.getDialogue().sendPlayerChat("Fine. May I ask about your cargo then?", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendNpcChat("I s'pose so... But no more about free rides!", CONTENT);
						return true;
					case 7:
						player.getDialogue().sendPlayerChat("I promise.", CONTENT);
						return true;
					case 8:
						player.getDialogue().sendNpcChat("Hrumph. I don't know what you want to know.", "It's all rather simple.", "I load my cargo into crates, like every", "other seaman around the land.", ANNOYED);
						return true;
					case 9:
						player.getDialogue().sendPlayerChat("Interesting... well, thank you!", "I don't care to hear any more.", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendStatement("Arhein mutters to himself while you inspect his crates.");
						return true;
					case 11:
						player.getDialogue().sendStatement("The crates seem large enough to fit a person.", "Maybe there is a suitable crate to hide in nearby.");
						player.getDialogue().endDialogue();
						player.setQuestStage(11, 4);
						return true;
				}
			case 251: //king arthur
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Welcome to my court, I am King Arthur.", CONTENT);
						return true;
					case 2:
						if (player.getQuestStage(11) < 1) {
							player.getDialogue().sendPlayerChat("I want to join the Knights of the Round Table!", CONTENT);
							return true;
						} else if (player.getQuestStage(11) == 12) {
							player.getDialogue().sendNpcChat("Thank you young traveller.", "As a token of my appreciation...", "...you can keep Excalibur.", HAPPY);
							player.getDialogue().setNextChatId(25);
							return true;
						} else if (player.getQuestStage(11) > 12) {
							player.getDialogue().sendNpcChat("Oh it's you! Thank you again!", HAPPY);
							player.getDialogue().endDialogue();
							return true;
						} else {
							player.getDialogue().sendNpcChat("Have you freed Merlin yet?", SAD);
							player.getDialogue().endDialogue();
							return true;
						}
					case 3:
						player.getDialogue().sendNpcChat("Ho ho! You're an eager lad.", "Why don't you complete a quest for me...", "...to prove your worth.", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("Anything for the mighty King Arthur.", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendNpcChat("Well, you see, we have a bit of a problem.", "My friend and mentor both, Merlin the Great...", "...has been trapped.", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendPlayerChat("Trapped? How? And by whom?", "I'll tear them apart!", ANGRY_1);
						return true;
					case 7:
						player.getDialogue().sendNpcChat("The trickster Morgan Le Faye imprisoned him", "inside a massive blue crystal.", "We managed to recover the crystal, and err...", "...stored it in the southeast tower.", CONTENT);
						return true;
					case 8:
						player.getDialogue().sendPlayerChat("Well let me destroy this crystal!", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("It's not that easy, I'm afraid.", "I've tried many ways to free him...", "...and all have failed.", SAD);
						return true;
					case 10:
						player.getDialogue().sendNpcChat("However, perhaps the other knights have some ideas.", "Go talk to them and find out.", CONTENT);
						return true;
					case 11:
						player.getDialogue().sendPlayerChat("Right away my lord!", CONTENT);
						player.getDialogue().endDialogue();
						player.setQuestStage(11, 1);
						QuestHandler.getQuests()[11].startQuest(player);
						return true;
					case 25:
						player.getDialogue().sendPlayerChat("Oh thank you!", HAPPY);
						return true;
					case 26:
						player.setQuestStage(11, 13);
						QuestHandler.completeQuest(player, 11);
						player.getDialogue().dontCloseInterface();
						return true;
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
