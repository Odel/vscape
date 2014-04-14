package com.rs2.model.content.quests;

//import com.rs2.model.content.DialogueManager;
//import com.rs2.model.content.DialogueManager.Dialogue;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.quests.CooksAssistant;
import com.rs2.model.players.item.*;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.PlayerSave;

/**
 * @date 1-jun-2011
 * @author Satan666
 */
public class QuestHandler {

    public static final int[] QUEST_IDS = {
        8145, 8146, 8147, 8148, 8149, 8150, 8151, 8152, 8153, 8154,
        8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163, 8164,
        8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174,
        8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184,
        8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194,
        8195, 12144, 12150, 12151, 12152, 12153, 12154, 12155, 12146
    };
    public static final int QUEST_INTERFACE = 8134;
    
    private static Quest[] quests = {
    	new CooksAssistant(),
    	new TheKnightsSword(),
    	new TheRestlessGhost(),
    	new DoricsQuest(),
    	new ImpCatcher(),
    	new RuneMysteries()
    };
    
    public static void init() {
        System.out.println("Loading quests...");
        System.out.println("Loaded " + quests.length + " quests.");
    }
    
    public static void initPlayer(Player player){
    	player.setQuestsLength(quests.length);
    	for(Quest q : quests)
    	{
    		player.setQuestStage(q.getQuestID(), 0);
    	}

        player.sendQuestTab(); //Makes the quest log empty except implemented quests
        PlayerSave.loadQuests(player); //loads quest progress from Username.txt, sets variables
    	
        for(Quest q : quests)
    	{
    		q.sendQuestTabStatus(player);
    	}
    }
    
    public static Quest[] getQuests(){
    	return quests;
    }

    public static int getTotalQuests() {
        return quests.length;
    }

    public static void startQuest(Player player, int questID) {
        Quest quest = quests[questID];
        if (quest == null) {
            return;
        }
        if (!quest.canDoQuest(player)) {
            player.getActionSender().removeInterfaces();
            player.getActionSender().sendMessage("You can't do this quest yet.");
            return;
        }
        if (player.getQuestStage(quest.getQuestID()) == 0) {
            resetInterface(player);
            quest.startQuest(player);
        }
    }
    
    public static boolean questCompleted(Player player, int questID) {
        Quest quest = quests[questID];
        if (quest == null) {
            return false;
        }
        if(quest.questCompleted(player))
        {
        	return true;
        }
        return false;
    }

    public static void completeQuest(Player player, int questID) {
        Quest quest = quests[questID];
        if (quest == null) {
            return;
        }
        resetInterface(player);
        quest.completeQuest(player);
    }

    public static boolean handleQuestButtons(Player player, int button) {
        switch (button) {
            case 28165: //Cooks Assistant Button
                showInterface(player,quests[0]);
                return true;
        	case 28178: //The Knights Sword Button
        		showInterface(player,quests[1]);
        		return true;
        	case 28169: //The Restless Ghost
        		showInterface(player,quests[2]);
        		return true;
        	case 28168: //Doric's quest
        		showInterface(player,quests[3]);
        		return true;
        	case 28172: //Imp Catcher
        		showInterface(player,quests[4]);
        		return true;
        	case 28167: //Rune mysteries
        		showInterface(player,quests[5]);
        		return true;
        }
        return false;
    }
    
    public static void showInterface(Player player, Quest quest)
    {
        if (quest != null) 
        {
        	quest.showInterface(player);
            resetInterface(player);
            quest.sendQuestRequirements(player);

        }
    }

    public static void resetInterface(Player player) {
        for (int i = 0; i < QUEST_IDS.length; i++) {
            player.getActionSender().sendString("", QUEST_IDS[i]);
        }
    }

}