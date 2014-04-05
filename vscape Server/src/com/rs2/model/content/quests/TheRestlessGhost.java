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


public class TheRestlessGhost implements Quest {

    public static final int QUEST_STARTED = 1;
    public static final int ITEMS_GIVEN = 2;
    public static final int QUEST_NEARING_COMPLETION = 3;
    public static final int QUEST_COMPLETE = 4;

    
    public int dialogueStage = 0;
    private int reward[][] = {
        {995, 0}, //itemID, amount
    };
    private int expReward[][] = {
        {Skill.PRAYER, 1125} // skill ID, exp amount
    };
    private static final int questPointReward = 1;

    public int getQuestID() {
        return 2;
    }

    public String getQuestName() {
        return "The Restless Ghost";
    }
    
    public String getQuestSaveName()
    {
    	return "restless-ghost";
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
        player.getActionSender().sendString("1 Quest Point", 12150);
        player.getActionSender().sendString((int)(expReward[0][1]*Constants.EXP_RATE) + " Prayer Experience", 12151);
        player.getActionSender().sendString("", 12152);
        player.getActionSender().sendString("", 12153);
        player.getActionSender().sendString("", 12154);
        player.getActionSender().sendString("", 12155);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@The Restless Ghost", 7337);
    }

    public void sendQuestRequirements(Player player) {
        String prefix = "";
        int questStage = player.getQuestStage(getQuestID());
        if (questStage == QUEST_STARTED) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
            player.getActionSender().sendString("@str@" + "Father Aereck at the church in Lumbridge.", 8148);
            
            player.getActionSender().sendString("Father Aereck told me to talk to Father Urhney who", 8150);
            player.getActionSender().sendString("can be found in his shack inside the swamp.", 8151);
            player.getActionSender().sendString("Apparently he can give me a bit of information", 8152);
            player.getActionSender().sendString("on how to get rid of the ghost.", 8153);
        }
        else if (questStage == ITEMS_GIVEN) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
            player.getActionSender().sendString("@str@" + "Father Aereck at the church in Lumbridge.", 8148);
            
            player.getActionSender().sendString("@str@" + "Father Aereck told me to talk to Father Urhney who", 8150);
            player.getActionSender().sendString("@str@" + "can be found in his shack inside the swamp.", 8151);
            player.getActionSender().sendString("@str@" + "Apparently he can give me a bit of information", 8152);
            player.getActionSender().sendString("@str@" + "on how to get rid of the ghost.", 8153);
            
            player.getActionSender().sendString("Father Urhney gave me an Amulet of Ghostspeak.", 8154);
        }
        else if (questStage == QUEST_NEARING_COMPLETION) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
            player.getActionSender().sendString("@str@" + "Father Aereck at the church in Lumbridge.", 8148);
            
            player.getActionSender().sendString("@str@" + "Father Aereck told me to talk to Father Urhney who", 8150);
            player.getActionSender().sendString("@str@" + "can be found in his shack inside the swamp.", 8151);
            player.getActionSender().sendString("@str@" + "Apparently he can give me a bit of information", 8152);
            player.getActionSender().sendString("@str@" + "on how to get rid of the ghost.", 8153);
            
            player.getActionSender().sendString("I talked to the ghost, he asked me to get back his skull.", 8154);
        }
        else if (questStage == QUEST_COMPLETE) {
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk with", 8147);
            player.getActionSender().sendString("@str@" + "Father Aereck at the church in Lumbridge.", 8148);
            
            player.getActionSender().sendString("@str@" + "Father Aereck told me to talk to Father Urhney who", 8150);
            player.getActionSender().sendString("@str@" + "can be found in his shack inside the swamp.", 8151);
            player.getActionSender().sendString("@str@" + "Apparently he can give me a bit of information", 8152);
            player.getActionSender().sendString("@str@" + "on how to get rid of the ghost.", 8153);
            
            player.getActionSender().sendString("@str@" + "I've helped the Ghost return to his sleep.", 8154);
            
            player.getActionSender().sendString("@red@You have completed this quest!", 8155);
        }
        else {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString(prefix + "To start the quest, you should talk with", 8147);
            player.getActionSender().sendString(prefix + "Father Aereck at the church in Lumbridge.", 8148);
        }
    }

    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@The Restless Ghost", 7337);
    }
    
    public void sendQuestTabStatus(Player player) {
    	int questStage = player.getQuestStage(getQuestID());
    	sendQuestRequirements(player);
    	if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
    		player.getActionSender().sendString("@yel@The Restless Ghost", 7337);
    	} else if (questStage >= QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@The Restless Ghost", 7337);
    	} else {
    		player.getActionSender().sendString("@red@The Restless Ghost", 7337);
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
        player.getActionSender().sendString(prefix + "Father Aereck in Lumbridge Church.", 8148);
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
