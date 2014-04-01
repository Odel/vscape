package com.rs2.model.content.quests;

//import com.rs2.model.content.DialogueManager;
//import com.rs2.model.content.DialogueManager.Dialogue;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.quests.CooksAssistant;
import com.rs2.model.players.item.*;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.PlayerSave;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
    //private static Map<String, Quest> quests = new HashMap<String, Quest>();
    private static Quest[] quests = new Quest[1];
    public static Quest cook = new CooksAssistant();
    
    
    public static void init(Player player) {
        //System.out.println("Loading quests...");
        quests[0]=cook;
        player.setQuestStage(0, 0); //initialize cooks assistant
        PlayerSave.loadQuests(player);
        quests[0].sendQuestTabStatus(player);
        System.out.println("Loaded " + quests.length + " quests.");
    }
    
    public static Quest[] getQuests(){
    	return quests;
    }

    public static int getTotalQuests() {
        return quests.length;
    }

    public static void startQuest(Player player, int questID) {
    	System.out.println("quest starting");
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

    public static void completeQuest(Player player, int questID) {
        Quest quest = quests[questID];
        if (quest == null) {
            return;
        }
        resetInterface(player);
        quest.completeQuest(player);
    }

    public static void handleQuestButtons(Player player, int button) {
        Quest quest = null;
        switch (button) {
            case 28165:
                quest = quests[0];


                quest.showInterface(player);
                break;
        	case 28178:
        		quest = quests[1];
        		quest.showInterface(player);
        		break;
        }

        if (quest != null) {
            resetInterface(player);
            quest.sendQuestRequirements(player);
        }
    }

    public static void resetInterface(Player player) {
        for (int i = 0; i < QUEST_IDS.length; i++) {
            player.getActionSender().sendString("", QUEST_IDS[i]);
        }
    }

    public static void sendQuestTabOnLogin(Player player) {
        /*String prefix = "";
        int questStage = 0;
        for (int i = 0; i < quests.size(); i++) {
            questStage = player.getQuestStage()[i];
            switch (i) {
                case 0:
                    prefix = (questStage >= CooksAssistant.QUEST_STARTED && questStage < CooksAssistant.QUEST_COMPLETE) ? "@yel@"
                            : questStage == CooksAssistant.QUEST_COMPLETE ? "@gre@" : "";
                    player.getActionSender().sendString(prefix + "Cook's Assistant", 7333);
            }
        }*/
    }

    public static void handleQuest(Player player, int dialogueId) {
        /*for (int i = 0; i < DialogueManager.getDialogues().size(); i++) {
            Dialogue dia = DialogueManager.getDialogues().get(i);
            if (dia.getDialogueId() == dialogueId) {
                switch (dia.getDialogueId()) {
                    case 15:
                        QuestHandler.startQuest(player, "cookassist");
                        break;
                    case 20:
                        if (player.getInventory().getItemContainer().contains(1944)
                                && player.getInventory().getItemContainer().contains(1927)
                                && player.getInventory().getItemContainer().contains(1933)) {
                            player.setQuestStage(0, CooksAssistant.ITEMS_GIVEN);
                            player.getInventory().removeItem(new Item(1927, 1));
                            player.getInventory().removeItem(new Item(1933, 1));
                            player.getInventory().removeItem(new Item(1944, 1));
                            DialogueManager.showDialogue(player, 21);
                        } else {
                            DialogueManager.showDialogue(player, 27);
                        }
                        break;
                    case 26:
                        QuestHandler.completeQuest(player, "cookassist");
                        break;
                }
            }
        }*/
    }
}