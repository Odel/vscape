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

public class DruidicRitual implements Quest {

    public static final int QUEST_STARTED = 1;
    public static final int RAW_MEAT_GATHERING = 2;
    public static final int GATE_TOUCHED = 3;
    public static final int MEATS_DIPPED = 4;
    public static final int QUEST_COMPLETE = 5;
    
    public int dialogueStage = 0;
    private int reward[][] = {
    };
    private int expReward[][] = {
	{Skill.HERBLORE, 250}
    };
    
    private static final int questPointReward = 4;

    public int getQuestID() {
        return 10;
    }

    public String getQuestName() {
        return "Druidic Ritual";
    }
    
    public String getQuestSaveName()
    {
    	return "druidic-ritual";
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
        player.getActionSender().sendString("4 Quest Points", 12150);
        player.getActionSender().sendString((int)(expReward[0][1]*Constants.EXP_RATE) + " Herblore Experience", 12151);
        player.getActionSender().sendString("Easy access to the Herblore Skill", 12152);
        player.getActionSender().sendString("", 12154);
        player.getActionSender().sendString("", 12155);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7355);
    }
    
    public void sendQuestRequirements(Player player) {
        String prefix = "";
        int questStage = player.getQuestStage(getQuestID());
        if (questStage == QUEST_STARTED) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
            player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);
            
            player.getActionSender().sendString("Kaqemeex needs your help, he told you to speak", 8150);
	    player.getActionSender().sendString("with Sanfew in central Taverly.", 8151);
        }
	else if (questStage == RAW_MEAT_GATHERING) {
	    player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
            player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);
            player.getActionSender().sendString("@str@" + "Kaqemeex needs your help, he told you to speak", 8150);
	    player.getActionSender().sendString("@str@" + "with Sanfew in central Taverly.", 8151);
	    
            player.getActionSender().sendString("Sanfew has told you to gather some meat.", 8153);
	    player.getActionSender().sendString("He said you need:", 8154);
	    player.getActionSender().sendString("Raw Beef, Raw Rat Meat, Raw Bear, and Raw Chicken.", 8155);
	    player.getActionSender().sendString("You need to dip these into the Cauldron of Thunder.", 8156);
	    player.getActionSender().sendString("then return to Sanfew.", 8157);
        }
	else if (questStage == GATE_TOUCHED) {
	    player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
            player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);
            player.getActionSender().sendString("@str@" + "Kaqemeex needs your help, he told you to speak", 8150);
	    player.getActionSender().sendString("@str@" + "with Sanfew in central Taverly.", 8151);
	    
            player.getActionSender().sendString("Sanfew has told you to gather some meat.", 8153);
	    player.getActionSender().sendString("He said you need:", 8154);
	    player.getActionSender().sendString("Raw Beef, Raw Rat Meat, Raw Bear, and Raw Chicken.", 8155);
	    player.getActionSender().sendString("You need to dip these into the Cauldron of Thunder,", 8156);
	    player.getActionSender().sendString("then return to Sanfew.", 8157);
        }
	else if (questStage == MEATS_DIPPED) {
	    player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
            player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);
            player.getActionSender().sendString("@str@" + "Kaqemeex needs your help, he told you to speak", 8150);
	    player.getActionSender().sendString("@str@" + "with Sanfew in central Taverly.", 8151);
            player.getActionSender().sendString("@str@" + "Sanfew has told you to gather some meat.", 8153);
	    player.getActionSender().sendString("@str@" + "He said you need:", 8154);
	    player.getActionSender().sendString("@str@" + "Raw Beef, Raw Rat Meat, Raw Bear, and Raw Chicken.", 8155);
	    player.getActionSender().sendString("@str@" + "You need to dip these into the Cauldron of Thunder.", 8156);
	    player.getActionSender().sendString("@str@" + "then return to Sanfew.", 8157);
	    
	    player.getActionSender().sendString("Speak with Kaqemeex.", 8159);
        }
        else if (questStage == QUEST_COMPLETE) {
	    player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Kaqemeex", 8147);
            player.getActionSender().sendString("@str@" + "found in the druidic circle in Taverly.", 8148);
            player.getActionSender().sendString("@str@" + "Kaqemeex needs your help, he told you to speak", 8150);
	    player.getActionSender().sendString("@str@" + "with Sanfew in central Taverly.", 8151);
	    player.getActionSender().sendString("@str@" + "Sanfew has told you to gather some meat.", 8153);
	    player.getActionSender().sendString("@str@" + "He said you need:", 8154);
	    player.getActionSender().sendString("@str@" + "Raw Beef, Raw Rat Meat, Raw Bear, and Raw Chicken.", 8155);
	    player.getActionSender().sendString("@str@" + "You need to dip these into the Cauldron of Thunder.", 8156);
	    player.getActionSender().sendString("@str@" + "then return to Sanfew.", 8157);
	    player.getActionSender().sendString("@str@" + "Speak with Kaqemeex.", 8159);
	    
            player.getActionSender().sendString("@red@You have completed this quest!", 8161);
        }
        else {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString(prefix + "To start the quest, you should talk to Kaqemeex", 8147);
            player.getActionSender().sendString(prefix + "found in the druidic circle in Taverly.", 8148);
        }
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 7355);
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
    		player.getActionSender().sendString("@yel@"+getQuestName(), 7355);
    	} else if (questStage == QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 7355);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 7355);
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
        player.getActionSender().sendString(prefix + "To start the quest, you should talk to Kaqemeex", 8147);
        player.getActionSender().sendString(prefix + "found in the druidic circle in Taverly.", 8148);
        player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void dialogue(Player player, Npc npc){
    	Dialogues.startDialogue(player, 455);
    }
    
    public int getDialogueStage(Player player){
    	return dialogueStage;
    }
    
    public void setDialogueStage(int in){
    	dialogueStage = in;
    }
    
}