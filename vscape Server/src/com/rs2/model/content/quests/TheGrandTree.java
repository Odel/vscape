package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.quests.ErnestTheChicken.PULL_LEVER_ANIM;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class TheGrandTree implements Quest {
    //Quest stages
    public static final int QUEST_STARTED = 1;
    public static final int QUEST_COMPLETE = 10;
    //Items
    public static final int BARK_SAMPLE = 783;
    public static final int TRANSLATION_BOOK = 784;
    public static final int GLOUGHS_JOURNAL = 785;
    public static final int HAZELMERES_SCROLL = 786;
    public static final int LUMBER_ORDER = 787;
    public static final int GLOUGHS_KEY = 788;
    public static final int TWIGS_T = 789;
    public static final int TWIGS_U = 790;
    public static final int TWIGS_Z = 791;
    public static final int TWIGS_O = 792;
    public static final int DACONIA_ROCK = 793;
    public static final int INVASION_PLANS = 794;
    //Positions

    //Interfaces

    //Npcs
    public static final int HAZELMERE = 699;
    public static final int KING_NARNODE_SHAREEN = 670;
    public static final int GLOUGH = 671;
    public static final int ANITA = 672;
    public static final int CHARLIE = 673;
    public static final int FOREMAN = 674;
    public static final int SHIPYARD_WORKER = 675;
    public static final int FEMI = 676;
    public static final int BLACK_DEMON = 677;
    //Objects

    //Buttons
    
    public int dialogueStage = 0; //Ignore
    
    private int reward[][] = { //Items in the form of {Id, #},
    };
    private int expReward[][] = { //Exp in the form of {Skill.AGILITY, x},
	{Skill.AGILITY, 7900},
	{Skill.ATTACK, 18400},
    }; //The 2.25 multiplier is added later, use vanilla values
    
    private static final int questPointReward = 5;

    public int getQuestID() { //Don't change
        return 29;
    }

    public String getQuestName() { //Don't change
        return "The Grand Tree";
    }
    
    public String getQuestSaveName() { //Don't change
    	return "grandtree";
    }

    public boolean canDoQuest(Player player) { //Use to check for strict auxiliary quest requirements
	return true;
    }
    
    public void getReward(Player player) { //Don't change
        for (int[] rewards : reward) {
            player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
        }
        for (int[] expRewards : expReward) {
            player.getSkill().addExp(expRewards[0], (expRewards[1]));
        }
        player.addQuestPoints(questPointReward);
        player.getActionSender().QPEdit(player.getQuestPoints());
    }
    
    //End of quest reward scroll interface, tweak what's necessary
    public void completeQuest(Player player) {
	//If writing in exp, be sure to express it manually as 2.25 the vanilla reward
        getReward(player);
        player.getActionSender().sendInterface(12140);
        player.getActionSender().sendItemOnInterface(995, 200, 12142);
        player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
        player.getActionSender().sendString("You are rewarded: ", 12146);
        player.getActionSender().sendString("5 Quest Points", 12150);
        player.getActionSender().sendString("17775 Agility XP", 12151);
        player.getActionSender().sendString("41400 Attack XP", 12152);
	player.getActionSender().sendString("4837.5 Magic XP", 12153);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
	//Updates the players quest list to show the green complete quest
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7361);
    }
    //Here we send the quest log, with the text and then the line number in sendString(string, line number)
    //The line number is according to the interface, just add to it for the next line
    public void sendQuestRequirements(Player player) {
        int questStage = player.getQuestStage(getQuestID());
	switch(questStage) {
	    case QUEST_STARTED:
		player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);
	    
		player.getActionSender().sendString("x", 8149);
		break;
	    case QUEST_COMPLETE:
		player.getActionSender().sendString("@str@" + "Talk to King Narnode Shareen, in the Grand Tree.", 8147);

	    
		player.getActionSender().sendString("@red@" + "You have completed this quest!", 8167);
		break;
	    default:
		player.getActionSender().sendString("Talk to @dre@King Narnode Shareen @bla@in the @dre@Grand Tree @bla@ to begin.", 8147);
		player.getActionSender().sendString("@dre@Requirements:", 8148);
		if (player.getSkill().getLevel()[Skill.AGILITY] < 25) {
		    player.getActionSender().sendString("-25 Agility.", 8149);
		} else {
		    player.getActionSender().sendString("@str@-25 Agility.", 8149);
		}
		player.getActionSender().sendString("@dre@-I must be able to defeat a level 172 demon.", 8150);
		break;
        }
    }
    
    public void sendQuestInterface(Player player){ //Don't change
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) { //Don't change
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 7361);
    }
    
    public boolean questCompleted(Player player) //Don't change
    {
    	int questStage = player.getQuestStage(getQuestID());
    	if(questStage >= QUEST_COMPLETE)
    	{
    		return true;
    	}
    	return false;
    }
    
    public void sendQuestTabStatus(Player player) { //Don't change
    	int questStage = player.getQuestStage(getQuestID());
    	if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
    		player.getActionSender().sendString("@yel@"+getQuestName(), 7361); //These numbers correspond to the index of the quest in
    	} else if (questStage == QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 7361); //the quest list on the quest tab. I've listed them all
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 7361); //in the player class, just search and then replace
										   //also add the name while you're there @red@Quest Name
    	}
    }
    
    public int getQuestPoints() { //Don't change
        return questPointReward;
    }

    public void showInterface(Player player){ //Don't change
    	String prefix = "";
        player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    	player.getActionSender().sendString(getQuestName(), 8144);
    }
    
    public void dialogue(Player player, Npc npc){
    	Dialogues.startDialogue(player, KING_NARNODE_SHAREEN); //Arbitrary, doesn't really matter
    }
    
    public int getDialogueStage(Player player){ //Don't change
    	return dialogueStage;
    }
    
    public void setDialogueStage(int in){ //Don't change
    	dialogueStage = in;
    }
    
    public boolean itemHandling(final Player player, int itemId) { //Inherited, will work without a call to it
	switch(itemId) {

	}
	return false;
    }
    
    public boolean itemOnItemHandling(Player player, int firstItem, int secondItem) { //Inherited, will work without a call to it
	return false; 
    }
    
    public boolean doItemOnObject(final Player player, int object, int item) { //Inherited, will work without a call to it
	switch(object) {
	
	}
	return false;
    }
    
    public boolean doObjectClicking(final Player player, int object, int x, int y) { //Inherited, will work without a call to it
	switch(object) {
	    
	}
	return false;
    }
    
    public static boolean doObjectSecondClick(final Player player, int object, final int x, final int y) { //An example of a non-inherited method
	switch (object) {
	    //Will need to be called in WalkToActionHandler in the form if(TheGrandTree.doObjectSecondClick(blah, obj, x, y)) { break; } or what have you.
	}
	return false;
    }
    
    public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) { //Inherited
	switch(id) { //Npc ID
	    /*case KING_NARNODE_SHAREEN:
		switch (player.getQuestStage(29)) { //Dialogue per stage
		    case 0: //Starting the quest
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("x", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("y", CONTENT);
				return true;
			}
		    return false;
		    case QUEST_COMPLETE:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("x", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("y", CONTENT);
				return true;
			}
		    return false;
		    case QUEST_STARTED:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("How goes thy quest adventurer?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("y", CONTENT);
				return true;
			}
		    return false;
		}
	    return false;*/
	}
	return false;
    }

}
