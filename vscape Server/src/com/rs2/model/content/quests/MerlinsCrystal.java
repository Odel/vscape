package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;

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
    private int reward[][] = {
    };
    private int expReward[][] = {
    };
    
    private static final int questPointReward = 6;

    public int getQuestID() {
        return 11;
    }

    public String getQuestName() {
        return "Merlin's Crystal";
    }
    
    public String getQuestSaveName()
    {
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
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7368);
    }
    
    public void sendQuestRequirements(Player player) {
        String prefix = "";
        int questStage = player.getQuestStage(getQuestID());
        if (questStage == QUEST_STARTED) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
            player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
            
            player.getActionSender().sendString("King Arthur has told you about Merlin.", 8150);
	    player.getActionSender().sendString("He is imprisoned in a crystal in the southeast tower.", 8151);
	    player.getActionSender().sendString("The other knights may have some ideas on how to free him.", 8152);
        }
	else if (questStage == GAWAIN) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to King Arthur", 8147);
            player.getActionSender().sendString("@str@" + "found in Camelot's castle.", 8148);
            player.getActionSender().sendString("@str@" + "King Arthur has told you about Merlin.", 8150);
	    player.getActionSender().sendString("@str@" + "He is imprisoned in a crystal in the southeast tower.", 8151);
	    player.getActionSender().sendString("@str@" + "The other knights may have some ideas on how to free him.", 8152);
	    
	    player.getActionSender().sendString("Sir Gawain said that Morgan Le Faye could free Merlin.", 8154);
	    player.getActionSender().sendString("Another knight may know how to get into Keep Le Faye.", 8155);
        }
	else if (questStage == LANCELOT) {
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
        }
	else if (questStage == INTO_CRATE) {
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
        }
	else if (questStage == SLAY_MORDRED) {
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
        }
	else if (questStage == MORGAN) {
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
        }
	else if (questStage == EXCALIBUR) {
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
        }
	else if (questStage == EXCALIBUR_FAILED) {
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
        }
	else if (questStage == CHAOS_ALTAR) {
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
        }
	else if (questStage == SUMMON) {
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
        }
	else if (questStage == SMASH_CRYSTAL) {
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
        }
	else if (questStage == TALK_TO_ARTHUR) {
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
        }
        else if (questStage == QUEST_COMPLETE) {
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
            
            player.getActionSender().sendString("@red@You have completed this quest!", 8185);
        }
        else {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("To start the quest, you should talk to King Arthur", 8147);
            player.getActionSender().sendString("found in Camelot's castle.", 8148);
	    player.getActionSender().sendString("-You must be able to defeat a level 39 enemy.", 8150);
	    player.getActionSender().sendString("-(Optional)You must be able to defeat a level 92 enemy.", 8151);
        }
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 7368);
    }
    
    public boolean questCompleted(Player player)
    {
    	int questStage = player.getQuestStage(getQuestID());
    	if(questStage >= QUEST_COMPLETE)
    	{
    		return true;
    	}
    	return false;
    }
    
    public void sendQuestTabStatus(Player player) {
    	int questStage = player.getQuestStage(getQuestID());
    	sendQuestRequirements(player);
    	if ((questStage >= QUEST_STARTED)) {
    		player.getActionSender().sendString("@yel@"+getQuestName(), 7368);
    	} else if (questStage == QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 7368);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 7368);
    	}
    	
    }
    
    public int getQuestPoints() {
        return 4;
    }

    public void clickObject(Player player, int object) {
    }
    
    public void showInterface(Player player){
    	String prefix = "";
    	player.getActionSender().sendString(getQuestName(), 8144);
        player.getActionSender().sendString("To start the quest, you should talk to King Arthur", 8147);
        player.getActionSender().sendString("found in Camelot's castle.", 8148);
	player.getActionSender().sendString("-You must be able to defeat a level 39 enemy.", 8150);
	player.getActionSender().sendString("-(Optional)You must be able to defeat a level 92 enemy.", 8151);
        player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void dialogue(Player player, Npc npc){
    	Dialogues.startDialogue(player, 251);
    }
    
    public int getDialogueStage(Player player){
    	return dialogueStage;
    }
    
    public void setDialogueStage(int in){
    	dialogueStage = in;
    }
    
    public static boolean workerSpawned() {
	    for(Npc npc : World.getNpcs()) {
		if(npc == null) continue;
		if(npc.getNpcId() == 675) return true;
	    }
	    return false;
    }
    
    public static boolean beggarSpawned() {
	    for(Npc npc : World.getNpcs()) {
		if(npc == null) continue;
		if(npc.getNpcId() == 252) return true;
	    }
	    return false;
    }
    
    public static boolean merlinSpawned() {
	    for(Npc npc : World.getNpcs()) {
		if(npc == null) continue;
		if(npc.getNpcId() == 249) return true;
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
    
}