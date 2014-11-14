package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.*;

public class DoricsQuest implements Quest {

    public static final int QUEST_STARTED = 1;
    public static final int NEEDS_ITEMS = 2;
    public static final int QUEST_COMPLETE = 3;
    
    public int dialogueStage = 0;
    private int reward[][] = {
        {995, 180}, //itemID, amount
    };
    private int expReward[][] = {
        {Skill.MINING, 1300} // skill ID, exp amount
    };
    private static final int questPointReward = 1;

    public int getQuestID() {
        return 3;
    }

    public String getQuestName() {
        return "Doric's Quest";
    }
    
    public String getQuestSaveName()
    {
    	return "dorics-quest";
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
        player.getActionSender().sendString((int)(reward[0][1]) + " coins", 12151);
        player.getActionSender().sendString((int)(expReward[0][1]*Constants.EXP_RATE) + " Mining Experience", 12152);
        player.getActionSender().sendString("Use of Doric's Anvils", 12153);
        player.getActionSender().sendString("", 12154);
        player.getActionSender().sendString("", 12155);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7336);
    }
    
    public void sendQuestRequirements(Player player) {
        String prefix = "";
        int questStage = player.getQuestStage(getQuestID());
        if (questStage == QUEST_STARTED) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Doric", 8147);
            player.getActionSender().sendString("@str@" + "found in North West of Falador.", 8148);
            
            player.getActionSender().sendString("Doric has asked you to get the following items:", 8150);
            player.getActionSender().sendString("6 clay", 8151);
            player.getActionSender().sendString("4 copper ore", 8152);
            player.getActionSender().sendString("2 iron ore", 8153);
        }
        else if (questStage == NEEDS_ITEMS) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Doric", 8147);
            player.getActionSender().sendString("@str@" + "found in North West of Falador.", 8148);

			int clayCount = player.getInventory().getItemAmount(434);
			int copperCount = player.getInventory().getItemAmount(436);
			int ironCount = player.getInventory().getItemAmount(440);
			if(player.carryingItem(434) && clayCount >= 6){
				player.getActionSender().sendString("@str@" + "6 clay", 8151);
			}
			else{
				player.getActionSender().sendString("6 clay", 8151);
			}
			if(player.carryingItem(436) && copperCount >= 4){
				player.getActionSender().sendString("@str@" + "4 copper ore", 8152);
			}
			else{
				player.getActionSender().sendString("4 copper ore", 8152);
			}
			if(player.carryingItem(440) && ironCount >= 2){
				player.getActionSender().sendString("@str@" + "2 iron ore", 8153);
			}
			else{
				player.getActionSender().sendString("2 iron ore", 8153);
			}
			if(clayCount >= 6 && copperCount >= 4 && ironCount >= 2)
			{
				player.getActionSender().sendString("@str@" + "Doric has asked you to get the following items:", 8150);
				player.getActionSender().sendString("You need to give Doric all the items", 8154);
			}
			else
			{
				player.getActionSender().sendString("Doric has asked you to get the following items:", 8150);
				player.getActionSender().sendString("", 8154);
			}
        }
        else if (questStage == QUEST_COMPLETE) {
            player.getActionSender().sendString("@str@" + "To start the quest, you should talk to Doric", 8147);
            player.getActionSender().sendString("@str@" + "found in North West of Falador.", 8148);
            
            player.getActionSender().sendString("@str@" + "Doric has asked you to get the following items:", 8150);
            player.getActionSender().sendString("@str@" + "6 clay", 8151);
            player.getActionSender().sendString("@str@" + "4 copper ore", 8152);
            player.getActionSender().sendString("@str@" + "2 iron ore", 8153);
            
            player.getActionSender().sendString("@str@" + "You brought Doric all the items", 8154);
            
            player.getActionSender().sendString("@red@You have completed this quest!", 8155);
        }
        else {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString(prefix + "To start the quest, you should talk to Doric", 8147);
            player.getActionSender().sendString(prefix + "found in North West of Falador.", 8148);
        }
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 7336);
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
    		player.getActionSender().sendString("@yel@"+getQuestName(), 7336);
    	} else if (questStage >= QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 7336);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 7336);
    	}
    	
    }
    
    public int getQuestPoints() {
        return questPointReward;
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
    
    public boolean itemHandling(final Player player, int itemId) { return false; }
    
    public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) { return false; }
    
    public boolean doItemOnObject(final Player player, int object, int item) { return false; }
    
    public boolean doObjectClicking(final Player player, int object, int x, int y) { return false; }
    
    public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) { return false; }

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
