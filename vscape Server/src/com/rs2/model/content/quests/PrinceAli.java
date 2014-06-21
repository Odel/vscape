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

public class PrinceAli implements Quest {

    public static final int QUEST_STARTED = 1;
    public static final int FIND_LEELA = 2;
    public static final int NEDS_WIG = 3;
    public static final int AGGIE = 4;
    public static final int AGGIE_NEEDS_SUPPLIES = 5;
    public static final int LADY_KELI_IMPRINT = 6;
    public static final int IMPRINT_TO_OSMAN = 7;
    public static final int GET_JOE_DRUNK = 8;
    public static final int TIPSY_JOE = 9;
    public static final int FREE_ALI = 10;
    public static final int ALI_NEEDS_DISGUISE = 11;
    public static final int RETURN_TO_HASSAN = 12;
    public static final int QUEST_COMPLETE = 13;
    
    public int dialogueStage = 0;
    private int reward[][] = {
        {995, 700}, //itemID, amount
    };
    private int expReward[][] = {
    };
    
    private static final int questPointReward = 3;

    public int getQuestID() {
        return 9;
    }

    public String getQuestName() {
        return "Prince Ali Rescue";
    }
    
    public String getQuestSaveName()
    {
    	return "prince-ali";
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
        player.getActionSender().sendString("3 Quest Points", 12150);
        player.getActionSender().sendString((int)(reward[0][1]) + " coins", 12151);
        player.getActionSender().sendString("Free Passage to Al-Kharid", 12153);
        player.getActionSender().sendString("", 12154);
        player.getActionSender().sendString("", 12155);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7342);
    }
    
    public void sendQuestRequirements(Player player) {
        String prefix = "";
        int questStage = player.getQuestStage(getQuestID());
        if (questStage == QUEST_STARTED) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
            
            player.getActionSender().sendString("Hassan has told you to talk to Osman.", 8150);
        }
        else if (questStage == FIND_LEELA) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    
	    player.getActionSender().sendString("Osman needs you to find Leela near Draynor.", 8151);

        }
	else if (questStage == NEDS_WIG) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
	    
	    player.getActionSender().sendString("You have a feeling Ned the wool man can make you a wig.", 8152);

        }
	else if (questStage == AGGIE) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
	    player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
	    
	    player.getActionSender().sendString("Speak with Aggie for skin paste and dye for the wig.", 8153);

        }
	else if (questStage == LADY_KELI_IMPRINT) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
	    player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
	    player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
	    
	    player.getActionSender().sendString("You now need to get a key imprint from Lady Keli near", 8154);
	    player.getActionSender().sendString("Draynor's prison.", 8155);

        }
	else if (questStage == IMPRINT_TO_OSMAN) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
	    player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
	    player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
	    player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
	    player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
	    
	    player.getActionSender().sendString("Take the key imprint to Osman.", 8156);
        }
	else if (questStage == GET_JOE_DRUNK) {
	    player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
	    player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
	    player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
	    player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
	    player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
	    player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);
	    
	    player.getActionSender().sendString("Find a way around Joe, the guard, to rescue Prince Ali.", 8157);
        }
	else if (questStage == FREE_ALI) {
	    player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
	    player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
	    player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
	    player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
	    player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
	    player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);
	    player.getActionSender().sendString("@str@" + "Find a way around Joe, the guard, to rescue Prince Ali.", 8156);
	    
	    player.getActionSender().sendString("Free Prince Ali by dealing with Lady Keli.", 8158);
        }
	else if (questStage == ALI_NEEDS_DISGUISE) {
	    player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
	    player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
	    player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
	    player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
	    player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
	    player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);
	    player.getActionSender().sendString("@str@" + "Find a way around Joe, the guard, to rescue Prince Ali.", 8156);
	    
	    player.getActionSender().sendString("Make sure you have a blonde wig (yellow dye), the skirt,", 8158);
	    player.getActionSender().sendString("the skin paste and the cell door key.", 8159);
        }
	else if (questStage == RETURN_TO_HASSAN) {
	    player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
	    player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
	    player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
	    player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
	    player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
	    player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);
	    player.getActionSender().sendString("@str@" + "Find a way around Joe, the guard, to rescue Prince Ali.", 8156);
	    player.getActionSender().sendString("@str@" + "Free Prince Ali by dealing with Lady Keli.", 8157);
	    
	    player.getActionSender().sendString("Return to Hassan.", 8159); 
        }
        else if (questStage == QUEST_COMPLETE) {
	    player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString("@str@" + "found in the Al-Kharid palace.", 8148);
	    player.getActionSender().sendString("@str@" + "Hassan has told you to talk to Osman.", 8149);
	    player.getActionSender().sendString("@str@" + "Osman told you to find Leela near Draynor.", 8150);
	    player.getActionSender().sendString("@str@" + "You have a feeling Ned the wool man can make you a wig.", 8151);
	    player.getActionSender().sendString("@str@" + "Speak with Aggie for skin paste and dye for the wig.", 8152);
	    player.getActionSender().sendString("@str@" + "You now need to get a key imprint from Lady Keli near", 8153);
	    player.getActionSender().sendString("@str@" + "Draynor's prison.", 8154);
	    player.getActionSender().sendString("@str@" + "Take the key imprint to Osman.", 8155);
	    player.getActionSender().sendString("@str@" + "Get Joe the cell guard drunk to rescue Prince Ali.", 8156);
	    player.getActionSender().sendString("@str@" + "Free Prince Ali by dealing with Lady Keli.", 8157);
	    player.getActionSender().sendString("@str@" + "Return to Hassan.", 8158);
	    
            player.getActionSender().sendString("@red@You have completed this quest!", 8160);
        }
        else {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString(prefix + "To start the quest, you should talk to Hassan", 8147);
            player.getActionSender().sendString(prefix + "found in the Al-Kharid palace.", 8148);
        }
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 7342);
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
    	if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
    		player.getActionSender().sendString("@yel@"+getQuestName(), 7342);
    	} else if (questStage == QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 7342);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 7342);
    	}
    	
    }
    
    public int getQuestPoints() {
        return 3;
    }

    public void clickObject(Player player, int object) {
    }
    
    public void showInterface(Player player){
    	String prefix = "";
    	player.getActionSender().sendString(getQuestName(), 8144);
        player.getActionSender().sendString(prefix + "To start the quest, you should talk to Hassan", 8147);
        player.getActionSender().sendString(prefix + "found in the Al-Kharid palace.", 8148);
        player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void dialogue(Player player, Npc npc){
    	Dialogues.startDialogue(player, 923);
    }
    
    public int getDialogueStage(Player player){
    	return dialogueStage;
    }
    
    public void setDialogueStage(int in){
    	dialogueStage = in;
    }
    
}