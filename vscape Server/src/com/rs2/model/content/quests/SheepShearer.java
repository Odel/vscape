package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class SheepShearer implements Quest
{
    public static final int QUEST_STARTED = 1;
    public static final int QUEST_STARTEDB = 2;
    public static final int ITEMS_GIVEN = 3;
    public static final int QUEST_COMPLETE = 4;
    
    public int dialogueStage = 0;
    private int reward[][] = {
		 {995, 60} //itemID, amount
    };
    
    private int expReward[][] = {
		{Skill.CRAFTING, 160} // skill ID, exp amount
    };
    
    private static final int questPointReward = 1;

    public int getQuestID() {
        return 8;
    }

    public String getQuestName() {
        return "Sheep Shearer";
    }
    
    public String getQuestSaveName()
    {
    	return "sheep-shearer";
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
        player.getActionSender().sendString(questPointReward + " Quest Point", 12150);
        player.getActionSender().sendString((int)(expReward[0][1]*Constants.EXP_RATE) + " Crafting Experience", 12151);
        player.getActionSender().sendString("60 Coins", 12152);
        player.getActionSender().sendString("", 12153);
        player.getActionSender().sendString("", 12154);
        player.getActionSender().sendString("", 12155);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7344);
    }
    
    public void sendQuestRequirements(Player player)
    {
        int questStage = player.getQuestStage(getQuestID());
        player.getActionSender().sendString(getQuestName(), 8144);
        switch(questStage)
        {
        	case 1:
        	case 2:
                player.getActionSender().sendString("@str@I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
                player.getActionSender().sendString("@str@@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);
                
    			int woolCount = player.getInventory().getItemAmount(1759);
    			if(player.carryingItem(1759) && woolCount >= 20){
    				player.getActionSender().sendString("@str@Fred has asked me to get the following items:", 8150);
    				player.getActionSender().sendString("@str@20 Balls of wool", 8151);
    				player.getActionSender().sendString("I need to give Fred the balls of wool", 8153);
    			}else{
    				player.getActionSender().sendString("Fred has asked me to get the following items:", 8150);
    				player.getActionSender().sendString("20 Balls of wool", 8151);
    				player.getActionSender().sendString("", 8153);
    			}
        	break;
        	case 3:
                player.getActionSender().sendString("@str@I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
                player.getActionSender().sendString("@str@@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);
                
				player.getActionSender().sendString("@str@Fred has asked me to get the following items:", 8150);
				player.getActionSender().sendString("@str@20 Balls of wool", 8151);
				
				player.getActionSender().sendString("@str@I need to give Fred the balls of wool", 8153);
				
				player.getActionSender().sendString("Maybe i can collect my reward now.", 8154);
        	break;
        	case 4:
                player.getActionSender().sendString("@str@I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
                player.getActionSender().sendString("@str@@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);
                
				player.getActionSender().sendString("@str@Fred has asked me to get the following items:", 8150);
				player.getActionSender().sendString("@str@20 Balls of wool", 8151);
				
				player.getActionSender().sendString("@str@I need to give Fred the balls of wool", 8153);
				
				player.getActionSender().sendString("@red@You have completed this quest!", 8155);
        	break;
        	default:
                player.getActionSender().sendString("I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
                player.getActionSender().sendString("@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);
        }
        
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 7344);
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
    		player.getActionSender().sendString("@yel@"+getQuestName(), 7344);
    	} else if (questStage >= QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 7344);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 7344);
    	}
    	
    }
    
    public int getQuestPoints() {
        return questPointReward;
    }
    
    public void showInterface(Player player){
    	player.getActionSender().sendString(getQuestName(), 8144);
    	player.getActionSender().sendString("I can start this quest by speaking to @dre@Farmer Fred@bla@ at his", 8147);
        player.getActionSender().sendString("@dre@farm@bla@ just a little way @dre@North West of Lumbridge", 8148);
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
    
    public boolean itemOnItemHandling(Player player, int firstItem, int secondItem) { return false; }
    
    public boolean doItemOnObject(final Player player, int object, int item) { return false; }
    
    public boolean doObjectClicking(final Player player, int object, int x, int y) { return false; }
    
    public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) { return false; }
    
}