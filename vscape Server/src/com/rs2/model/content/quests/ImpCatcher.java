package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;

/**
 * @date 1-jun-2011
 * @author Satan666
 */
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
    
    public String getQuestSaveName()
    {
    	return "imp-catcher";
    }

    /**
     * Does the user meet the requirements if yes return true.
     * @param player
     * @return
     */
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
        player.getActionSender().sendString((int)(expReward[0][1]*Constants.EXP_RATE) + " Magic Experience", 12151);
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
        }
        else if (questStage == QUEST_COMPLETE) {
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
            player.getActionSender().sendString("@str@" + "Wizard Mizgog in The Wizard's Tower.", 8148);
            
            player.getActionSender().sendString("@str@ Cook has asked you to get the following items:", 8150);
            player.getActionSender().sendString("@str@" + "Red Bead", 8151);
            player.getActionSender().sendString("@str@" + "Black Bead", 8152);
            player.getActionSender().sendString("@str@" + "White Bead & Yellow Bead", 8153);
            
            player.getActionSender().sendString("@str@" + "You were rewarded for finding all the beads.", 8154);
            
            player.getActionSender().sendString("@red@You have completed this quest!", 8155);
        }
        else {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString(prefix + "To start the quest, you should talk with", 8147);
            player.getActionSender().sendString(prefix + "Wizard Mizgog in The Wizard's Tower.", 8148);
        }
    }

    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@Imp Catcher", 7340);
    }
    
    public void sendQuestTabStatus(Player player) {
    	int questStage = player.getQuestStage(getQuestID());
    	sendQuestRequirements(player);
    	if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
    		player.getActionSender().sendString("@yel@Imp Catcher", 7340);
    	} else if (questStage >= QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@Imp Catcher", 7340);
    	} else {
    		player.getActionSender().sendString("@red@Imp Catcher", 7340);
    	}
    	
    }

    public int getQuestPoints() {
        return 1;
    }

    public void clickObject(Player player, int object) {
    }
    
    public void showInterface(Player player){
    	String prefix = "";
    	player.getActionSender().sendString(getQuestName(), 8144);
        player.getActionSender().sendString(prefix + "To start the quest, you should talk with", 8147);
        player.getActionSender().sendString(prefix + "Wizard Mizgog in The Wizard's Tower.", 8148);
        player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void dialogue(Player player, Npc npc){
    	Dialogues.startDialogue(player, 278);
    }
    
    public int getDialogueStage(Player player){
    	return dialogueStage;
    }
    public void setDialogueStage(int in){
    	dialogueStage = in;
    }
}