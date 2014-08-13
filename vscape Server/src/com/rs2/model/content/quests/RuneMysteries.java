package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

public class RuneMysteries implements Quest {
	
    public static final int QUEST_STARTED = 1;
    public static final int GIVEN_ITEMS = 2;
    public static final int DELIVER_ITEMS = 3;
    public static final int GIVEN_ITEMSB = 4;
    public static final int NPC_THINKING = 5;
    public static final int DELIVER_ITEMSB = 6;
    public static final int QUEST_COMPLETE = 7;
    
    public int dialogueStage = 0;
    private int reward[][] = {
        {995, 180}, //itemID, amount
    };
    
    private int expReward[][] = {
    };
    
    private static final int questPointReward = 1;

    public int getQuestID() {
        return 5;
    }

    public String getQuestName() {
        return "Rune Mysteries";
    }
    
    public String getQuestSaveName()
    {
    	return "rune-mysteries";
    }
    
    public boolean canDoQuest(Player player) {
        return true;
    }
    
    public void getReward(Player player) {
        player.addQuestPoints(questPointReward);
        player.getActionSender().QPEdit(player.getQuestPoints());
    }
    
    public void completeQuest(Player player) {
        getReward(player);
        player.getActionSender().sendInterface(12140);
        player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
        player.getActionSender().sendString(questPointReward + " Quest Point", 12150);
        player.getActionSender().sendString("Runecrafting Skill", 12151);
        player.getActionSender().sendString("Air Talisman", 12152);
        player.getActionSender().sendString("", 12153);
        player.getActionSender().sendString("", 12154);
        player.getActionSender().sendString("", 12155);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7335);
    }
    
    public void sendQuestRequirements(Player player)
    {
        String prefix = "";
        int questStage = player.getQuestStage(getQuestID());
        switch(questStage)
        {
        	case 1:
                player.getActionSender().sendString(getQuestName(), 8144);
                player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
                player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);
                
                player.getActionSender().sendString("Duke Horacio has asked you to take the Air Talisman he", 8150);
                player.getActionSender().sendString("found to the head wizard at Wizards' Tower.", 8151);
        		break;
        	case 2:
                player.getActionSender().sendString(getQuestName(), 8144);
                player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
                player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);
                
                player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
                player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);
                
                player.getActionSender().sendString("I need to speak to the head wizard again...", 8153);
        		break;
        	case 3:
                player.getActionSender().sendString(getQuestName(), 8144);
                player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
                player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);
                
                player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
                player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);
                
                player.getActionSender().sendString("The head wizard has asked me to take his research notes to", 8153);
                player.getActionSender().sendString("Aubury's rune shop in varrock.", 8154);
        		break;
        	case 4:
                player.getActionSender().sendString(getQuestName(), 8144);
                player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
                player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);
                
                player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
                player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);
                
                player.getActionSender().sendString("@str@" + "The head wizard has asked me to take his research notes to", 8153);
                player.getActionSender().sendString("@str@" + "Aubury's rune shop in varrock.", 8154);
                
                player.getActionSender().sendString("I should speak to Aubury again...", 8156);
        		break;
        	case 5:
                player.getActionSender().sendString(getQuestName(), 8144);
                player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
                player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);
                
                player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
                player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);
                
                player.getActionSender().sendString("@str@" + "The head wizard has asked me to take his research notes to", 8153);
                player.getActionSender().sendString("@str@" + "Aubury's rune shop in varrock.", 8154);
                
                player.getActionSender().sendString("Aubury told me I should speak to him again after hes done...", 8156);
        		break;
        	case 6:
                player.getActionSender().sendString(getQuestName(), 8144);
                player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
                player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);
                
                player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
                player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);
                
                player.getActionSender().sendString("@str@" + "The head wizard has asked me to take his research notes to", 8153);
                player.getActionSender().sendString("@str@" + "Aubury's rune shop in varrock.", 8154);
                
                player.getActionSender().sendString("Aubury gave me some notes and told me to take them to the", 8156);
                player.getActionSender().sendString("head wizard at Wizards' Tower.", 8157);
        		break;
        	case 7:
                player.getActionSender().sendString(getQuestName(), 8144);
                player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Duke Horacio", 8147);
                player.getActionSender().sendString("@str@" + "found in Lumbridge Castle.", 8148);
                
                player.getActionSender().sendString("@str@" + "Duke Horacio has asked you to take the Air Talisman he", 8150);
                player.getActionSender().sendString("@str@" + "found to the head wizard at Wizards' Tower.", 8151);
                
                player.getActionSender().sendString("@str@" + "The head wizard has asked me to take his research notes to", 8153);
                player.getActionSender().sendString("@str@" + "Aubury's rune shop in varrock.", 8154);
                
                player.getActionSender().sendString("@str@" + "Aubury gave me some notes and told me to take them to the", 8156);
                player.getActionSender().sendString("@str@" + "head wizard at Wizards' Tower.", 8157);
                
                player.getActionSender().sendString("@red@You have completed this quest!", 8159);
        		break;
        	default:
                player.getActionSender().sendString(getQuestName(), 8144);
                player.getActionSender().sendString(prefix + "To start the quest, you should talk to Duke Horacio", 8147);
                player.getActionSender().sendString(prefix + "found in Lumbridge Castle.", 8148);
        }
        
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 7335);
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
    	if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
    		player.getActionSender().sendString("@yel@"+getQuestName(), 7335);
    	} else if (questStage >= QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 7335);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 7335);
    	}
    	
    }
    
    public int getQuestPoints() {
        return questPointReward;
    }

    public void clickObject(Player player, int object) {
    }
    
    public void showInterface(Player player){
    	String prefix = "";
    	player.getActionSender().sendString(getQuestName(), 8144);
        player.getActionSender().sendString(prefix + "To start the quest, you should talk to Doric", 8147);
        player.getActionSender().sendString(prefix + "found in North West of Falador.", 8148);
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
