package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;

public class WitchsPotion implements Quest
{
    public static final int QUEST_STARTED = 1;
    public static final int DRINK_POTION = 2;
    public static final int QUEST_COMPLETE = 3;
    
    public int dialogueStage = 0;
    private int reward[][] = {
    };
    
    private int expReward[][] = {
    		{Skill.MAGIC, 325} // skill ID, exp amount
    };
    
    private static final int questPointReward = 1;

    public int getQuestID() {
        return 6;
    }

    public String getQuestName() {
        return "Witch's Potion";
    }
    
    public String getQuestSaveName()
    {
    	return "witchs-potion";
    }
    
    public boolean canDoQuest(Player player) {
        return true;
    }
    
    public void getReward(Player player) {
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
        player.getActionSender().sendString((int)(expReward[0][1]*Constants.EXP_RATE) + " Magic Experience", 12151);
        player.getActionSender().sendString("", 12152);
        player.getActionSender().sendString("", 12153);
        player.getActionSender().sendString("", 12154);
        player.getActionSender().sendString("", 12155);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7348);
    }
    
    public void sendQuestRequirements(Player player)
    {
        String prefix = "";
        int questStage = player.getQuestStage(getQuestID());
        switch(questStage)
        {
        	case 1:
        		player.getActionSender().sendString(getQuestName(), 8144);
        		player.getActionSender().sendString("@str@Talk to Hetty in her house in Rimmington,", 8147);
        		player.getActionSender().sendString("@str@south of Falador and west of Port Sarim.", 8148);

                player.getActionSender().sendString("Hetty has asked you to get the following items:", 8150);
    			if(player.carryingItem(2146)){
    				player.getActionSender().sendString("@str@Burnt Meat", 8151);
    			}else{
    				player.getActionSender().sendString("Burnt Meat", 8151);
    			}
    			if(player.carryingItem(221)){
    				player.getActionSender().sendString("@str@Eye of newt", 8152);
    			}else{
    				player.getActionSender().sendString("Eye of newt", 8152);
    			}
    			if(player.carryingItem(1957)){
    				player.getActionSender().sendString("@str@Onion", 8153);
    			}else{
    				player.getActionSender().sendString("Onion", 8153);
    			}
    			if(player.carryingItem(300)){
    				player.getActionSender().sendString("@str@Rat's tail", 8154);
    			}else{
    				player.getActionSender().sendString("Rat's tail", 8154);
    			}
    			if(player.carryingItem(2146) && player.carryingItem(221) && player.carryingItem(1957) && player.carryingItem(300))
    			{
    				player.getActionSender().sendString("@str@Hetty has asked you to get the following items:", 8150);
    				player.getActionSender().sendString("You need to give Hetty all the items", 8155);
    			}
    			else
    			{
    				player.getActionSender().sendString("@str@Hetty has asked you to get the following items:", 8150);
    				player.getActionSender().sendString("", 8155);
    			}
        	break;
        	case 2:
        		player.getActionSender().sendString(getQuestName(), 8144);
        		player.getActionSender().sendString("@str@Talk to Hetty in her house in Rimmington,", 8147);
        		player.getActionSender().sendString("@str@south of Falador and west of Port Sarim.", 8148);
                player.getActionSender().sendString("@str@Hetty has asked you to get the following items:", 8150);
                player.getActionSender().sendString("@str@Burnt Meat", 8151);
                player.getActionSender().sendString("@str@Eye of newt", 8152);
                player.getActionSender().sendString("@str@Onion", 8153);
                player.getActionSender().sendString("@str@Rat's tail", 8154);
                player.getActionSender().sendString("@str@You gave Hetty all the items", 8155);
                player.getActionSender().sendString("I need to drink the potion!", 8156);
        	break;
        	case 3:
        		player.getActionSender().sendString(getQuestName(), 8144);
        		player.getActionSender().sendString("@str@Talk to Hetty in her house in Rimmington,", 8147);
        		player.getActionSender().sendString("@str@south of Falador and west of Port Sarim.", 8148);
                player.getActionSender().sendString("@str@Hetty has asked you to get the following items:", 8150);
                player.getActionSender().sendString("@str@Burnt Meat", 8151);
                player.getActionSender().sendString("@str@Eye of newt", 8152);
                player.getActionSender().sendString("@str@Onion", 8153);
                player.getActionSender().sendString("@str@Rat's tail", 8154);
                player.getActionSender().sendString("@str@You gave Hetty all the items", 8155);
                player.getActionSender().sendString("@str@I need to drink the potion!", 8156);
                player.getActionSender().sendString("@red@You have completed this quest!", 8157);
        	break;
        	default:
                player.getActionSender().sendString(getQuestName(), 8144);
                player.getActionSender().sendString(prefix + "Talk to Hetty in her house in Rimmington,", 8147);
                player.getActionSender().sendString(prefix + "south of Falador and west of Port Sarim.", 8148);
        }
        
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 7348);
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
    		player.getActionSender().sendString("@yel@"+getQuestName(), 7348);
    	} else if (questStage >= QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 7348);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 7348);
    	}
    	
    }
    
    public int getQuestPoints() {
        return questPointReward;
    }
    
    public void showInterface(Player player){
    	String prefix = "";
    	player.getActionSender().sendString(getQuestName(), 8144);
        player.getActionSender().sendString(prefix + "Talk to Hetty in her house in Rimmington,", 8147);
        player.getActionSender().sendString(prefix + "south of Falador and west of Port Sarim.", 8148);
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
    
    public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot)  { return false; }
    
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
