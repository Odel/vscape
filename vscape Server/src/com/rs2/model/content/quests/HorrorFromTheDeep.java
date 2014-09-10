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
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class HorrorFromTheDeep implements Quest {

    public static final int QUEST_STARTED = 1;
    public static final int KEY_TO_LARRISSA = 2;
    public static final int DOOR_UNLOCKED = 3;
    public static final int DAGANNOTH_DOOR_UNLOCKED = 4;
    public static final int QUEST_COMPLETE = 23;
    
    public static final int HAMMER = 2347;
    public static final int FIRE_RUNE = 554;
    public static final int WATER_RUNE = 555;
    public static final int AIR_RUNE = 556;
    public static final int EARTH_RUNE = 557;
    public static final int MOLTEN_GLASS = 1775;
    public static final int TINDERBOX = 590;
    public static final int SWAMP_TAR = 1939;
    public static final int STEEL_NAILS = 1539;
    public static final int PLANK = 960;
    public static final int LIGHTHOUSE_KEY = 3848;
    public static final int JOURNAL = 3845;
    public static final int DIARY = 3846;
    public static final int MANUAL = 3847;
    
    public static final Position UP_AT_LIGHTHOUSE = new Position(2510, 3644, 0);
    public static final Position UP_FROM_BOSS_CAVE = new Position(2515, 4629, 1);
    public static final Position DOWN_IN_BOSS_CAVE = new Position(2515, 10008, 0);
    public static final Position DOWN_IN_BOSS_CAVE_POST_QUEST = new Position(2515, 4632, 0);
    public static final Position DOWN_IN_CAVE_POST_QUEST = new Position(2519, 4619, 1);
    public static final Position DOWN_IN_CAVE = new Position(2519, 9995, 1);
    
    public static final int DOOR_INTERFACE = 10116;
    
    public static final int GUNNJORN = 607;
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
    
    public static final int BOOKSHELF = 4617;
    public static final int LIGHTHOUSE_DOOR = 4577;
    public static final int LADDER_UP_TO_WALL = 4413;
    public static final int LADDER_DOWN_TO_CAVE = 4383;
    public static final int LADDER_DOWN_TO_BOSS_CAVE = 4485;
    public static final int LADDER_UP_TO_LIGHTHOUSE = 4412;
    public static final int STUDY_WALL = 4544;
    public static final int STUDY_WALL_2 = 4543;
    public static final int OPEN_WALL = 4546;
    public static final int OPEN_WALL_2 = 4545;
    public static final int FIRST_ROCK_TO_SHORE = 4550;
    public static final int SHORE_TO_FIRST_ROCK = 4551;
    public static final int SECOND_ROCK_FROM_FIRST = 4553;
    public static final int FIRST_ROCK_FROM_SECOND = 4552;
    public static final int THIRD_ROCK_FROM_SECOND = 4555;
    public static final int SECOND_ROCK_FROM_THIRD = 4554;
    public static final int FOURTH_ROCK_FROM_THIRD = 4557;
    public static final int THIRD_ROCK_FROM_FOURTH = 4556;
    public static final int FOURTH_ROCK_TO_SHORE = 4559;
    public static final int SHORE_TO_FOURTH_ROCK = 4558;
    
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
	    
	    player.getActionSender().sendString("Larrissa expressed her concerns about her boyfriend.", 8149);
	    player.getActionSender().sendString("I need to find Larrissa's cousin who has a lighthouse key.", 8150);
	    player.getActionSender().sendString("Larrissa said his name is Gunnjorn and he loves Agility.", 8151);
	}
	else if (questStage == KEY_TO_LARRISSA) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);
	    player.getActionSender().sendString("@str@" + "Larrissa expressed her concerns about her boyfriend.", 8149);
	    player.getActionSender().sendString("@str@" + "I need to find Larrissa's cousin who has a lighthouse key.", 8150);
	    
	    player.getActionSender().sendString("I found Gunnjorn at the Barbarian Outpost. He gave me the", 8152);
	    player.getActionSender().sendString("spare lighthouse key. I should return to Larrissa.", 8153);
	}
	else if (questStage == DOOR_UNLOCKED || questStage == DAGANNOTH_DOOR_UNLOCKED) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);
	    player.getActionSender().sendString("@str@" + "Larrissa expressed her concerns about her boyfriend.", 8149);
	    player.getActionSender().sendString("@str@" + "I need to find Larrissa's cousin who has a lighthouse key.", 8150);
	    player.getActionSender().sendString("@str@" + "I found Gunnjorn at the Barbarian Outpost.", 8152);
	    
	    player.getActionSender().sendString("I need to search the lighthouse for clues as to where", 8154);
	    player.getActionSender().sendString("Jossik could be.", 8155);
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
	    player.getActionSender().sendString("@dre@-I must be able to defeat strong level 100 enemies.", 8151);
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
	player.getActionSender().sendString("@dre@-I must be able to defeat strong level 100 enemies.", 8151);
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
    public static boolean allItemsInDoor(final Player player) {
	return player.hasPlacedAirRune() && player.hasPlacedFireRune() && player.hasPlacedEarthRune() && player.hasPlacedWaterRune() && player.hasPlacedSword() && player.hasPlacedArrow();
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
	    case OPEN_WALL:
	    case OPEN_WALL_2:
	    case STUDY_WALL:
	    case STUDY_WALL_2:
		switch(player.getQuestStage(26)) {
		    case 3:
			switch(item) {
			    default:
				Item used = new Item(item);
				if(used.getDefinition().getName().contains("sword")) {
				    Dialogues.startDialogue(player, 88978);
				    player.setTempInteger(item);
				    return true;
				}
				else if(used.getDefinition().getName().contains("arrow")) {
				    Dialogues.startDialogue(player, 88979);
				    player.setTempInteger(item);
				    return true;
				}
				else {
				    return false;
				}
			    case FIRE_RUNE:
				player.getInventory().removeItem(new Item(FIRE_RUNE, 1));
				player.setPlacedFireRune(true);
				player.getActionSender().sendMessage("You place the fire rune into the slot in the door.");
				if (allItemsInDoor(player)) {
				    player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
				    player.setQuestStage(26, 4);
				}
				return true;
			    case WATER_RUNE:
				player.getInventory().removeItem(new Item(WATER_RUNE, 1));
				player.setPlacedWaterRune(true);
				player.getActionSender().sendMessage("You place the water rune into the slot in the door.");
				if (allItemsInDoor(player)) {
				    player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
				    player.setQuestStage(26, 4);
				}
				return true;
			    case EARTH_RUNE:
				player.getInventory().removeItem(new Item(EARTH_RUNE, 1));
				player.setPlacedEarthRune(true);
				player.getActionSender().sendMessage("You place the earth rune into the slot in the door.");
				if (allItemsInDoor(player)) {
				    player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
				    player.setQuestStage(26, 4);
				}
				return true;
			    case AIR_RUNE:
				player.getInventory().removeItem(new Item(AIR_RUNE, 1));
				player.setPlacedAirRune(true);
				player.getActionSender().sendMessage("You place the air rune into the slot in the door.");
				if (allItemsInDoor(player)) {
				    player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
				    player.setQuestStage(26, 4);
				}
				return true;
			}
		}    
	    case LIGHTHOUSE_DOOR:
		if(item == LIGHTHOUSE_KEY && player.getQuestStage(26) == 2) {
		    player.getActionSender().walkTo(0, player.getPosition().getY() < 3636 ? 1 : -1, true);
		    //player.getActionSender().walkThroughDoor(object, 2509, 3636, 0); door is broke as fuk
		    player.getActionSender().sendMessage("You unlock the lighthouse front door.");
		    player.setQuestStage(26, 3);
		    player.getInventory().removeItem(new Item(LIGHTHOUSE_KEY));
		    return true;
		}
	    return true;
	}
	return false;
    }
    public static boolean doMiscObjectClicking(final Player player, int object, int x, int y) {
	switch (object) {
	    case FIRST_ROCK_TO_SHORE:
	    case SHORE_TO_FIRST_ROCK:
	    case SECOND_ROCK_FROM_FIRST:
	    case FIRST_ROCK_FROM_SECOND:
	    case THIRD_ROCK_FROM_SECOND:
	    case SECOND_ROCK_FROM_THIRD:
	    case FOURTH_ROCK_FROM_THIRD:
	    case THIRD_ROCK_FROM_FOURTH:
	    case FOURTH_ROCK_TO_SHORE:
	    case SHORE_TO_FOURTH_ROCK:
		if (Misc.random(69) == 5) {
		    player.getActionSender().sendMessage("You slip while jumping across the rock...");
		    player.fadeTeleport(new Position(2518, 3594, 0));
		    player.getActionSender().sendMessage("...You find yourself washed up on shore.");
		    return true;
		} else {
		    if (Misc.goodDistance(player.getPosition().clone(), new Position(x, y, 0), 2)) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		}
	}
	return false;
    }
    public static boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch(object) {
	    case BOOKSHELF:
		Dialogues.startDialogue(player, 97979);
		return true;
	    case 4570: //lighthouse top level ladder
		Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
		return true;
	    case 4569: //lighthouse level 1 ladder
		Dialogues.startDialogue(player, 14567);
		return true;
	    case 4568: //lighthouse ladder up
		Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
		return true;
	    case LADDER_UP_TO_WALL:
		Ladders.climbLadder(player, UP_FROM_BOSS_CAVE);
		return true;
	    case LADDER_UP_TO_LIGHTHOUSE:
		Ladders.climbLadder(player, UP_AT_LIGHTHOUSE);
		return true;
	    case LADDER_DOWN_TO_CAVE:
		Ladders.climbLadder(player, DOWN_IN_CAVE);
		return true;
	    case LADDER_DOWN_TO_BOSS_CAVE:
		if(QuestHandler.questCompleted(player, 26)) {
		    Ladders.climbLadder(player, DOWN_IN_BOSS_CAVE_POST_QUEST);
		    return true;
		}
		else {
		    Ladders.climbLadder(player, DOWN_IN_BOSS_CAVE);
		    return true;
		}
	    case OPEN_WALL:
	    case OPEN_WALL_2:
		if(player.getQuestStage(26) < 4) {
		    player.getActionSender().sendMessage("This door is mysteriously locked.");
		    return true;
		}
		else {
		    player.getActionSender().walkTo(0, player.getPosition().getY() < y ? 1 : -1, true);
		    player.getActionSender().walkThroughDoor(object, x, y, 1);
		    return true;
		}
	    case STUDY_WALL:
	    case STUDY_WALL_2:
		player.getActionSender().sendInterface(DOOR_INTERFACE);
		return true;
	    case LIGHTHOUSE_DOOR:
		switch(player.getQuestStage(26)) {
		    case 0:
		    case 1:
		    case 2:
			player.getActionSender().sendMessage("This door is locked firmly.");
			return true;
		    case 3:
			player.getActionSender().walkTo(0, player.getPosition().getY() < 3636 ? 1 : -1, true);
			//player.getActionSender().walkThroughDoor(object, x, y, 0);
			return true;
		}
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
	    case SITTING_JOSSIK:
		switch (player.getQuestStage(26)) {
		    case DAGANNOTH_DOOR_UNLOCKED:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("*cough*", "Please... please help me...", "I think my leg is broken, and those creatures will be", "back any minute now!", SAD);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("I guess you're Jossik then...", "What creatures are you talking about?", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("I... I do not know.", "I've never seen their like before!", DISTRESSED);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("I was searching for information about my uncle Silas,", "who vanished mysteriously from this lighthouse many", "months ago. I found the secret of that strange wall, and", "discovered that I could use it as a door, but when I", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("came down here...I was attacked by...", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendStatement("Jossik shudders violently.");
				return true;
			    case 7:
				player.getDialogue().sendNpcChat("...I do not know what they are, but they are very", "strong. They hurt me badly enough to trap me here,", "and I have been fearing for my life ever since!", SAD);
				return true;
			    case 8:
				player.getDialogue().sendPlayerChat("Don't worry, I'm here now. Larrissa was", "worried about you and asked for my help.", CONTENT);
				return true;
			    case 9:
				player.getDialogue().sendPlayerChat("I'll go back upstairs and let her know that I've found", "you and that you're still alive, and then we can work", "out some way of getting you out of here, okay?", CONTENT);
				return true;
			    case 10:
				player.getDialogue().sendNpcChat("NO! No, you can't leave me now!", "Look! They're coming again! Do something!!", DISTRESSED);
				return true;
			    case 11:
				//encounter
			}
		    return false;
		}
	    return false;
	    case 88979: //arrow in wall
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendPlayerChat("I don't think I'll get this back...", CONTENT);
			return true;
		    case 2:
			player.getDialogue().sendOption("Continue anyways.", "Don't place the item in the wall.");
			return true;
		    case 3:
			switch(optionId) {
			    case 1:
				Item item = new Item(player.getTempInteger());
				player.getInventory().removeItem(item);
				player.setPlacedArrow(true);
				player.getActionSender().sendMessage("You place the " + item.getDefinition().getName() + " into the slot in the door.");
				if (allItemsInDoor(player)) {
				    player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
				    player.setQuestStage(26, 4);
				}
				player.setTempInteger(0);
				break;
			    case 2:
				player.getDialogue().endDialogue();
				player.setTempInteger(0);
				break;
			}
		}
	    return false;
	    case 88978: //sword in wall
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendPlayerChat("I don't think I'll get this back...", CONTENT);
			return true;
		    case 2:
			player.getDialogue().sendOption("Continue anyways.", "Don't place the item in the wall.");
			return true;
		    case 3:
			switch(optionId) {
			    case 1:
				Item item = new Item(player.getTempInteger());
				player.getInventory().removeItem(item);
				player.setPlacedSword(true);
				player.getActionSender().sendMessage("You place the " + item.getDefinition().getName() + " into the slot in the door.");
				if (allItemsInDoor(player)) {
				    player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
				    player.setQuestStage(26, 4);
				}
				player.setTempInteger(0);
				break;
			    case 2:
				player.getDialogue().endDialogue();
				player.setTempInteger(0);
				break;
			}
		}
	    return false;
	    case 97979: //bookshelf
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendStatement("There are three books here that look important...", "What would you like to do?");
			return true;
		    case 2:
			player.getDialogue().sendOption("Take the Manual.", "Take the Diary.", "Take the Journal.", "Take all three books.");
			return true;
		    case 3:
			switch(optionId) {
			    case 1:
				if(player.getInventory().ownsItem(MANUAL)) {
				    player.getDialogue().sendStatement("You already have the manual.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else {
				    player.getInventory().addItemOrDrop(new Item(MANUAL));
				    break;
				}
			    case 2:
				if(player.getInventory().ownsItem(DIARY)) {
				    player.getDialogue().sendStatement("You already have the diary.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else {
				    player.getInventory().addItemOrDrop(new Item(DIARY));
				    break;
				}
			    case 3:
				if(player.getInventory().ownsItem(JOURNAL)) {
				    player.getDialogue().sendStatement("You already have the journal.");
				    player.getDialogue().endDialogue();
				    return true;
				}
				else {
				    player.getInventory().addItemOrDrop(new Item(JOURNAL));
				    break;
				}
			    case 4:
				if(player.getInventory().ownsItem(MANUAL)) {
				    player.getActionSender().sendMessage("You already have the manual.");
				}
				else if(!player.getInventory().ownsItem(MANUAL)) {
				    player.getInventory().addItemOrDrop(new Item(MANUAL));
				}
				if(player.getInventory().ownsItem(DIARY)) {
				    player.getActionSender().sendMessage("You already have the diary.");
				}
				else if(!player.getInventory().ownsItem(DIARY)) {
				    player.getInventory().addItemOrDrop(new Item(DIARY));
				}
				if(player.getInventory().ownsItem(JOURNAL)) {
				    player.getActionSender().sendMessage("You already have the journal.");
				}
				else if(!player.getInventory().ownsItem(JOURNAL)){
				    player.getInventory().addItemOrDrop(new Item(JOURNAL));
				}
				break;
			}
		}
	    return false;
	    case 14567: //lighthouse ladder
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendOption("Climb ladder up.", "Climb ladder down.", "Do nothing.");
			return true;
		    case 2:
			switch(optionId) {
			    case 1:
				Ladders.climbLadder(player, new Position(2505, 3641, 2));
				player.getDialogue().endDialogue();
				break;
			    case 2:
				Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
				player.getDialogue().endDialogue();
				break;
			    case 3:
				player.getDialogue().endDialogue();
				break;
			}
		}
	    return false;
	    case GUNNJORN:
		switch (player.getQuestStage(26)) {
		    default:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("Welcome to my obstacle course. Have fun, but", "remember this isn't a child's playground. People have", "died here.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		    case KEY_TO_LARRISSA:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(player.getInventory().ownsItem(LIGHTHOUSE_KEY)) {
				    player.getDialogue().sendNpcChat("Welcome to my obstacle course. Have fun, but", "remember this isn't a child's playground. People have", "died here.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("Erm, I seem to have lost the key you gave me.", SAD);
				    return true;
				}
			    case 2:
				player.getDialogue().sendNpcChat("Lucky for you, one of my barbarian scouts found it.", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendGiveItemNpc("Gunnjorn hands you a key.", new Item(LIGHTHOUSE_KEY));
				player.getDialogue().endDialogue();
				player.getInventory().addItemOrDrop(new Item(LIGHTHOUSE_KEY));
				return true;
			}
		    return false;
		    case QUEST_STARTED:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("Welcome to my obstacle course. Have fun, but", "remember this isn't a child's playground. People have", "died here.", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("You're Gunnjorn, aren't you?", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("Why, indeed I am. I own this here agility course.", "It can be very dangerous for adventurers like you.", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("Yeah, that's great. I understand you have a cousin", "named Larrissa who gave you a key...?", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("Yes, she did! How do you know of this? She said she", "probably wouldn't need it, but gave it to me for safe", "keeping just in case.", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendPlayerChat("Well, something has happened at the lighthouse, and she", "has been locked out. I need you to give me her key.", CONTENT);
				return true;
			    case 7:
				player.getDialogue().sendNpcChat("Sure, give Larrissa my regards.", CONTENT);
				return true;
			    case 8:
				player.getDialogue().sendGiveItemNpc("Gunnjorn hands you a key.", new Item(LIGHTHOUSE_KEY));
				player.getDialogue().endDialogue();
				player.getInventory().addItemOrDrop(new Item(LIGHTHOUSE_KEY));
				player.setQuestStage(26, KEY_TO_LARRISSA);
				return true;
			}
		    return false;
		}
	    case LARRISSA:
		switch (player.getQuestStage(26)) {
		    case QUEST_COMPLETE:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("x", CONTENT);
				return true;
			}
		    return false;
		    case QUEST_STARTED:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("Where is Gunnjorn again?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("I'm not sure, but he was always a fan of Agility.", "He left Rellekka to pursue his love of it.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		    case KEY_TO_LARRISSA:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("I found Gunnjorn and the key.", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("Fantastic! Go unlock the lighthouse and search for clues!", HAPPY);
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		    case 0:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("Hello there.", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("Oh, thank Guthix! I am in such a worry... please", "help me!", DISTRESSED);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("With what?", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("Oh... it is terrible... horrible... My boyfriend lives here", "in this lighthouse, but I haven't seen him the last few", "days! I think something terrible has happened!", DISTRESSED);
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("He is nowhere to be found and the front door is locked", "up tight! He would NEVER do that!", DISTRESSED);
				return true;
			    case 6:
				player.getDialogue().sendPlayerChat("Maybe he just went on vacation or something?", "Must be pretty boring living in a lighthouse.", CONTENT);
				return true;
			    case 7:
				player.getDialogue().sendNpcChat("That is terribly irresponsible! He is far too thoughtful", "for that! He would never leave it unattended! He would", "also never leave without telling me!", DISTRESSED);
				return true;
			    case 8:
				player.getDialogue().sendNpcChat("Please, I know something terrible has happened to him", "I can sense it! Please... please help me adventurer!", CONTENT);
				return true;
			    case 9:
				player.getDialogue().sendPlayerChat("But how can I help?", CONTENT);
				return true;
			    case 11:
				player.getDialogue().sendNpcChat("Well, we have to get inside to see where he has gone!", "If you could go and visit my cousin and get the", "spare key I left him, I will be eternally grateful!", CONTENT);
				return true;
			    case 12:
				player.getDialogue().sendOption("Okay, I'll help!", "Sorry, I'm just passing through, I can't help you.");
				return true;
			    case 13:
				switch(optionId) {
				    case 1:
					player.getDialogue().sendPlayerChat("Okay, I'll help!", CONTENT);
					return true;
				    case 2:
					player.getDialogue().sendPlayerChat("Sorry, I'm just passing through, I can't help you.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			    case 14:
				player.getDialogue().sendNpcChat("OH! THANK YOU SO MUCH! I know my darling", "would never have left without telling me where he's gone!", HAPPY);
				return true;
			    case 15:
				player.getDialogue().sendPlayerChat("Where is your cousin at?", CONTENT);
				return true;
			    case 16:
				player.getDialogue().sendNpcChat("My cousin was always interested in agility. He left our", "home in Rellekka many moons ago, so that he could", "pursue his interest.", CONTENT);
				return true;
			    case 17:
				player.getDialogue().sendNpcChat("I don't know exactly where he has gone, but I am sure", "he went somewhere to practice his agility. If you see", "him, his name is Gunnjorn. Mention my name, he will", "recognize it.", CONTENT);
				return true;
			    case 18:
				player.getDialogue().sendPlayerChat("I'll see what I can do to find him.", CONTENT);
				return true;
			    case 19:
				player.getDialogue().sendNpcChat("Thank you so much!", HAPPY);
				player.getDialogue().endDialogue();
				QuestHandler.startQuest(player, 26);
				return true;
			}
		    return false;
		}
	    return false;
	}
	return false;
    }

}
