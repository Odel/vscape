package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class TemplateQuest implements Quest {
    public static final int questIndex = -1; //Used in player's quest log interface, id is in Player.java //Change
    //Quest stages
    public static final int QUEST_STARTED = 1;
    public static final int QUEST_COMPLETE = 2;

    //Items
    public static final int ITEM = -1;
    
    //Positions
    public static final Position POSITION = new Position(0, 0, 0);

    //Interfaces
    public static final int INTERFACE = -1;

    //Npcs
    public static final int NPC = -1;

    //Objects
    public static final int OBJECT = -1;
    
    public int dialogueStage = 0;

    private int reward[][] = { //{itemId, count},

    };

    private int expReward[][] = { //{skillId, exp},
    };

    private static final int questPointReward = 1; //Change

    public int getQuestID() { //Change
	return -1;
    }

    public String getQuestName() { //Change
	return "Template";
    }

    public String getQuestSaveName() { //Change
	return "Template";
    }

    public boolean canDoQuest(final Player player) {
	return false;
    }

    public void getReward(Player player) {
	for (int[] rewards : reward) {
	    player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
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
	player.getActionSender().sendItemOnInterface(12145, 250, ITEM); //zoom, then itemId
	player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
	player.getActionSender().sendString("You are rewarded: ", 12146);
	player.getActionSender().sendString("1 Quest Point", 12150);
	player.getActionSender().sendString("", 12151);
	player.getActionSender().sendString("", 12152);
	player.getActionSender().sendString("", 12153);
	player.getActionSender().sendString("", 12154);
	player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
	player.getActionSender().sendString(" ", 12147);
	player.setQuestStage(getQuestID(), QUEST_COMPLETE);
	player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
    }

    public void sendQuestRequirements(Player player) {
	int questStage = player.getQuestStage(getQuestID());
	switch (questStage) {
	    case QUEST_STARTED:
		player.getActionSender().sendString("@str@" + "", 8147);
		//Change
		player.getActionSender().sendString("", 8149);
		break;
	    case QUEST_COMPLETE:
		player.getActionSender().sendString("@str@" + "", 8147);
		//Change
		player.getActionSender().sendString("@red@" + "You have completed this quest!", 8177);
		break;
	    default:
		player.getActionSender().sendString("Talk to @dre@ x @bla@in the @dre@ x @bla@to begin.", 8147);
		player.getActionSender().sendString("@dre@Requirements:", 8148);
		if (QuestHandler.questCompleted(player, -1)) {
		    player.getActionSender().sendString("@str@-Template Quest.", 8150);
		} else {
		    player.getActionSender().sendString("@dbl@-Template Quest.", 8150);
		}
		//Change
		break;
	}
    }

    public void sendQuestInterface(Player player) {
	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }

    public void startQuest(Player player) {
	player.setQuestStage(getQuestID(), QUEST_STARTED);
	player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
    }

    public boolean questCompleted(Player player) {
	int questStage = player.getQuestStage(getQuestID());
	if (questStage >= QUEST_COMPLETE) {
	    return true;
	}
	return false;
    }

    public void sendQuestTabStatus(Player player) {
	int questStage = player.getQuestStage(getQuestID());
	if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
	    player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
	} else if (questStage == QUEST_COMPLETE) {
	    player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	} else {
	    player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
	}
    }

    public int getQuestPoints() {
	return questPointReward;
    }

    public void showInterface(Player player) {
	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	player.getActionSender().sendString(getQuestName(), 8144);
    }

    public void dialogue(Player player, Npc npc) {
	//Don't even need this anymore really
    }

    public int getDialogueStage(Player player) {
	return dialogueStage;
    }

    public void setDialogueStage(int in) {
	dialogueStage = in;
    }
    
    public boolean itemHandling(final Player player, int itemId) {
	switch(itemId) {
	    
	}
	return false;
    }

    public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
	return false; 
    }
    
    public boolean doItemOnObject(final Player player, int object, int item) {
	return false;
    }

    public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
	return false;
    }

    public boolean doNpcClicking(Player player, Npc npc) {
	return false;
    }

    public boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch (object) {
	    case OBJECT: //Spare controls crate
		return true;
	}
	return false;
    }

    public static boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
	switch (object) {

	}
	return false;
    }

    public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
	DialogueManager d = player.getDialogue();
	switch (id) { //Npc ID
	    case NPC:
		switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
		    case QUEST_COMPLETE:
			switch (d.getChatId()) {
			    case 1:
				d.sendNpcChat("Thank you again!", Dialogues.HAPPY);
				d.endDialogue();
				return true;
			}
			return false;
		}
		return false;
	}
	return false;
    }

}
