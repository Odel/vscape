package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;

public class EasterEvent implements Quest {
	
    public static final int EVENT_COMPLETE = 10;
    
    public int dialogueStage = 0;
    private int reward[][] = {
    };
    
    private int expReward[][] = {
    };
    
    private static final int questPointReward = 0;

    public int getQuestID() {
        return 7;
    }

    public String getQuestName() {
        return "Easter Event";
    }
    
    public String getQuestSaveName()
    {
    	return "easter-event";
    }
    
    public boolean canDoQuest(Player player) {
        return true;
    }
    
    public void getReward(Player player) {

    }
    
    public void completeQuest(Player player) {
        player.setQuestStage(getQuestID(), EVENT_COMPLETE);
    }
    
    public void sendQuestRequirements(Player player)
    {
    }
    
    public void sendQuestInterface(Player player){
    }
    
    public void startQuest(Player player) {
    }
    
    public boolean questCompleted(Player player)
    {
    	int questStage = player.getQuestStage(getQuestID());
    	if(questStage >= EVENT_COMPLETE)
    	{
    		return true;
    	}
    	return false;
    }
    
    public void sendQuestTabStatus(Player player) {
    }
    
    public int getQuestPoints() {
        return 1;
    }

    public void clickObject(Player player, int object) {
    }
    
    public void showInterface(Player player){

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
