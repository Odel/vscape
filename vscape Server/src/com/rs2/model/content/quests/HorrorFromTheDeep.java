package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class HorrorFromTheDeep implements Quest {

    public static final int QUEST_STARTED = 1;
    public static final int QUEST_COMPLETE = 23;
    
    public static final int HAMMER = 2347;
    public static final int MOLTEN_GLASS = 1775;
    public static final int TINDERBOX = 590;
    public static final int SWAMP_TAR = 1939;
    public static final int STEEL_NAILS = 1539;
    public static final int PLANK = 960;
    
    public static final Position LIGHTHOUSE = new Position(2510, 3634, 0);
    public static final Position DOWN_IN_CAVE = new Position(2515, 4632, 0);
    
    public static final int DOOR_INTERFACE = 10116;
    
    public static final int LARRISSA = 1336;
    public static final int LARRISSA_2 = 1337;
    public static final int JOSSIK = 1334;
    public static final int CHILD_DAGANNOTH = 1347;
    public static final int SITTING_JOSSIK = 1335;
    public static final int WHITE_MOTHER = 1351;
    public static final int BLUE_MOTHER = 1352;
    public static final int RED_MOTHER = 1353;
    public static final int BROWN_MOTHER = 1354;
    public static final int GREEN_MOTHER = 1355;
    public static final int ORANGE_MOTHER = 1356;
    
    public static final int LIGHTHOUSE_DOOR = 4577;
    
    public int dialogueStage = 0;
    
    private int reward[][] = {
    };
    private int expReward[][] = {
	{Skill.MAGIC, 4662},
	{Skill.STRENGTH, 4662},
	{Skill.RANGED, 4662},
    };
    
    private static final int questPointReward = 2;

    public int getQuestID() {
        return 26;
    }

    public String getQuestName() {
        return "Horror From The Deep";
    }
    
    public String getQuestSaveName()
    {
    	return "horror-deep";
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
        player.getActionSender().sendItemOnInterface(995, 200, 12142);
        player.getActionSender().sendString("You have survived the " + getQuestName() + "!", 12144);
        player.getActionSender().sendString("You are rewarded: ", 12146);
        player.getActionSender().sendString("2 Quest Points", 12150);
        player.getActionSender().sendString("10489.5 XP in each of: Ranged,", 12151);
        player.getActionSender().sendString("Magic, and Strength", 12152);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 10135);
    }
    
    public void sendQuestRequirements(Player player) {
        String prefix = "";
        player.getActionSender().sendString(getQuestName(), 8144);
        int questStage = player.getQuestStage(getQuestID());
        if (questStage == QUEST_STARTED) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);
	    
	    player.getActionSender().sendString("x", 8149);
	}
	else if (questStage == QUEST_COMPLETE) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);
	    
	    player.getActionSender().sendString("@red@" + "You have completed this quest!", 8170);
	}
        else {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("Talk to @dre@Larrissa @bla@outside the @dre@Lighthouse @bla@, north", 8147);
	    player.getActionSender().sendString("of the Barbarian Outpost to begin this quest.", 8148);
	    player.getActionSender().sendString("@dre@Requirements:", 8149);
	    if (player.getSkill().getLevel()[Skill.AGILITY] < 35) {
		player.getActionSender().sendString("-35 Agility.", 8150);
	    } else {
		player.getActionSender().sendString("@str@-35 Agility.", 8150);
	    }
        }
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 10135);
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
    		player.getActionSender().sendString("@yel@"+getQuestName(), 10135);
    	} else if (questStage == QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 10135);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 10135);
    	}
    	
    }
    
    public int getQuestPoints() {
        return questPointReward;
    }
    public void clickObject(Player player, int object) {
    }

    public void showInterface(Player player){
    	String prefix = "";
        player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    	player.getActionSender().sendString(getQuestName(), 8144);
        player.getActionSender().sendString("Talk to @dre@Larrissa @bla@outside the @dre@Lighthouse @bla@, north", 8147);
	player.getActionSender().sendString("of the Barbarian Outpost to begin this quest.", 8148);
	player.getActionSender().sendString("@dre@Requirements:", 8149);
	if (player.getSkill().getLevel()[Skill.AGILITY] < 35) {
	    player.getActionSender().sendString("-35 Agility.", 8150);
	} else {
	    player.getActionSender().sendString("@str@-35 Agility.", 8150);
	}
    }
    
    public void dialogue(Player player, Npc npc){
    	Dialogues.startDialogue(player, LARRISSA);
    }
    
    public int getDialogueStage(Player player){
    	return dialogueStage;
    }
    
    public void setDialogueStage(int in){
    	dialogueStage = in;
    }
    
    public static boolean itemHandling(final Player player, int itemId) {
	switch(itemId) {
	}
	return false;
    }
    
    public static void handleDeath(final Player player, Npc npc) {
	
    }
    
    public static boolean handleNpcClick(Player player, int npcId) {
	switch(npcId) {

	}
	return false;
    }
    
    public static boolean itemPickupHandling(Player player, int itemId) {
	//
	return false;
    }
    
    public static boolean itemOnItemHandling(Player player, int firstItem, int secondItem) {

	 return false;
    }
    
    public static boolean doItemOnObject(final Player player, int object, int item) {
	switch(object) {

	}
	return false;
    }
    public static boolean doMiscObjectClicking(final Player player, int object, int x, int y) {
	switch (object) {
	    
	}
	return false;
    }
    public static boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch(object) {
	    
	}
	return false;
    }
    
     public static boolean doObjectSecondClicking(final Player player, int object, int x, int y) {
	switch(object) {
	    
	}
	return false;
    }
    
    public static void handleDrops(Player player, Npc npc) {
	GroundItem drop = new GroundItem(new Item(0), player, npc.getPosition().clone());
	GroundItemManager.getManager().dropItem(drop);
	return;
    }
    
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) {
	    case LARRISSA:
		switch (player.getQuestStage(25)) {
		    case QUEST_COMPLETE:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("x", CONTENT);
				return true;
			}
		    return false;
		    case 0:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("x", CONTENT);
				return true;
			}
		    return false;
		}
	    return false;
	}
	return false;
    }

}
