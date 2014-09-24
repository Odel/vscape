package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ShopManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class HeroesQuest implements Quest {
    //Quest stages
    public static final int QUEST_STARTED = 1;
    public static final int QUEST_COMPLETE = 9;
    //Items
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
    //Positions
    public static final Position UP_AT_LIGHTHOUSE = new Position(2510, 3644, 0);
    //Interfaces
    public static final int DOOR_INTERFACE = 10116;
    //Npcs
    public static final int ACHIETTIES = 796;
    public static final int ICE_QUEEN = 795;
    public static final int CHARLIE_THE_COOK = 794;
    public static final int ALFONSE_THE_WAITER = 793;
    public static final int GRIP = 792;
    public static final int SETH = 791;
    public static final int TROBERT = 790;
    public static final int GARV = 788;
    public static final int ENTRANA_FIREBIRD = 6108;
    public static final int GERRANT = 2720;
    public static final int KATRINE = ShieldOfArrav.KATRINE;
    public static final int STRAVEN = ShieldOfArrav.STRAVEN;
    //Objects
    public static final int BOOKSHELF = 4617;
    
    public int dialogueStage = 0;
    
    private int reward[][] = {
    };
    private int expReward[][] = {
	{Skill.ATTACK, 2825},
	{Skill.DEFENCE, 3075},
	{Skill.STRENGTH, 3025},
	{Skill.HITPOINTS, 2775},
	{Skill.RANGED, 1525},
	{Skill.COOKING, 2725},
	{Skill.WOODCUTTING, 1875},
	{Skill.FIREMAKING, 2725},
	{Skill.SMITHING, 2225},
	{Skill.MINING, 2575},
	{Skill.HERBLORE, 1825}
    };
    
    private static final int questPointReward = 1;

    public int getQuestID() {
        return 27;
    }

    public String getQuestName() {
        return "Heroes Quest";
    }
    
    public String getQuestSaveName()
    {
    	return "heroes";
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
        player.getActionSender().sendString("You have completed the " + getQuestName() + "!", 12144);
        player.getActionSender().sendString("You are rewarded: ", 12146);
        player.getActionSender().sendString("1 Quest Point", 12150);
        player.getActionSender().sendString("Access to the Heroes' guild", 12151);
        player.getActionSender().sendString("A total of 65,772 XP spread", 12152);
	player.getActionSender().sendString("over twelve skills.", 12153);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7363);
    }
    
    public void sendQuestRequirements(Player player) {
        String prefix = "";
        player.getActionSender().sendString(getQuestName(), 8144);
        int questStage = player.getQuestStage(getQuestID());
	switch(questStage) {
	    case QUEST_STARTED:
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("@str@" + "Talk to Achietties, outside the Heroes Guild to begin.", 8147);
	    
		player.getActionSender().sendString("x", 8149);
	    case QUEST_COMPLETE:
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("@str@" + "Talk to Achietties, outside the Heroes Guild to begin.", 8147);
	    
		player.getActionSender().sendString("@red@" + "You have completed this quest!", 8170);
	    default:
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to @dre@Achietties @bla@outside the @dre@Heroes Guild @bla@, north", 8147);
		player.getActionSender().sendString("of Taverly to begin this quest.", 8148);
		player.getActionSender().sendString("@dre@Requirements:", 8149);
		if (player.getQuestPoints() < 55) {
		    player.getActionSender().sendString("-55 Quest Points.", 8150);
		} else {
		    player.getActionSender().sendString("@str@-55 Quest Points.", 8150);
		}
		if (player.getSkill().getLevel()[Skill.COOKING] < 53) {
		    player.getActionSender().sendString("-53 Cooking.", 8151);
		} else {
		    player.getActionSender().sendString("@str@-53 Cooking.", 8151);
		}
		if (player.getSkill().getLevel()[Skill.FISHING] < 53) {
		    player.getActionSender().sendString("-53 Fishing.", 8152);
		} else {
		    player.getActionSender().sendString("@str@-53 Fishing.", 8152);
		}
		if (player.getSkill().getLevel()[Skill.HERBLORE] < 25) {
		    player.getActionSender().sendString("-25 Herblore.", 8153);
		} else {
		    player.getActionSender().sendString("@str@-25 Herblore.", 8153);
		}
		if (player.getSkill().getLevel()[Skill.MINING] < 50) {
		    player.getActionSender().sendString("-50 Mining.", 8154);
		} else {
		    player.getActionSender().sendString("@str@-50 Mining.", 8154);
		}
		if (player.getQuestStage(13) < ShieldOfArrav.QUEST_COMPLETE) {
		    player.getActionSender().sendString("-Completion of Shield of Arrav.", 8155);
		} else {
		    player.getActionSender().sendString("@str@-Completion of Shield of Arrav.", 8155);
		}
		if (player.getQuestStage(14) < LostCity.QUEST_COMPLETE) {
		    player.getActionSender().sendString("-Completion of Lost City.", 8156);
		} else {
		    player.getActionSender().sendString("@str@-Completion of Lost City.", 8156);
		}
		if (player.getQuestStage(15) < DragonSlayer.QUEST_COMPLETE) {
		    player.getActionSender().sendString("-Completion of Dragon Slayer.", 8157);
		} else {
		    player.getActionSender().sendString("@str@-Completion of Dragon Slayer.", 8157);
		}
		if (player.getQuestStage(11) < MerlinsCrystal.QUEST_COMPLETE) {
		    player.getActionSender().sendString("-Completion of Merlin's Crystal.", 8157);
		} else {
		    player.getActionSender().sendString("@str@-Completion of Merlin's Crystal.", 8157);
		}
		player.getActionSender().sendString("@dre@-I must be able to defeat a strong level 111 enemy.", 8158);
        }
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 7363);
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
    		player.getActionSender().sendString("@yel@"+getQuestName(), 7363);
    	} else if (questStage == QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 7363);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 7363);
    	}
    	
    }
    
    public int getQuestPoints() {
        return questPointReward;
    }

    public void showInterface(Player player){
    	String prefix = "";
        player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    	player.getActionSender().sendString(getQuestName(), 8144);
	player.getActionSender().sendString("Talk to @dre@Achietties @bla@outside the @dre@Heroes Guild @bla@, north", 8147);
	player.getActionSender().sendString("of Taverly to begin this quest.", 8148);
	player.getActionSender().sendString("@dre@Requirements:", 8149);
	if (player.getQuestPoints() < 55) {
	    player.getActionSender().sendString("-55 Quest Points.", 8150);
	} else {
	    player.getActionSender().sendString("@str@-55 Quest Points.", 8150);
	}
	if (player.getSkill().getLevel()[Skill.COOKING] < 53) {
	    player.getActionSender().sendString("-53 Cooking.", 8151);
	} else {
	    player.getActionSender().sendString("@str@-53 Cooking.", 8151);
	}
	if (player.getSkill().getLevel()[Skill.FISHING] < 53) {
	    player.getActionSender().sendString("-53 Fishing.", 8152);
	} else {
	    player.getActionSender().sendString("@str@-53 Fishing.", 8152);
	}
	if (player.getSkill().getLevel()[Skill.HERBLORE] < 25) {
	    player.getActionSender().sendString("-25 Herblore.", 8153);
	} else {
	    player.getActionSender().sendString("@str@-25 Herblore.", 8153);
	}
	if (player.getSkill().getLevel()[Skill.MINING] < 50) {
	    player.getActionSender().sendString("-50 Mining.", 8154);
	} else {
	    player.getActionSender().sendString("@str@-50 Mining.", 8154);
	}
	if (player.getQuestStage(13) < ShieldOfArrav.QUEST_COMPLETE) {
	    player.getActionSender().sendString("-Completion of Shield of Arrav.", 8155);
	} else {
	    player.getActionSender().sendString("@str@-Completion of Shield of Arrav.", 8155);
	}
	if (player.getQuestStage(14) < LostCity.QUEST_COMPLETE) {
	    player.getActionSender().sendString("-Completion of Lost City.", 8156);
	} else {
	    player.getActionSender().sendString("@str@-Completion of Lost City.", 8156);
	}
	if (player.getQuestStage(15) < DragonSlayer.QUEST_COMPLETE) {
	    player.getActionSender().sendString("-Completion of Dragon Slayer.", 8157);
	} else {
	    player.getActionSender().sendString("@str@-Completion of Dragon Slayer.", 8157);
	}
	if (player.getQuestStage(11) < MerlinsCrystal.QUEST_COMPLETE) {
	    player.getActionSender().sendString("-Completion of Merlin's Crystal.", 8157);
	} else {
	    player.getActionSender().sendString("@str@-Completion of Merlin's Crystal.", 8157);
	}
	player.getActionSender().sendString("@dre@-I must be able to defeat a strong level 111 enemy.", 8158);
    }
    
    public void dialogue(Player player, Npc npc){
    	Dialogues.startDialogue(player, ACHIETTIES);
    }
    
    public int getDialogueStage(Player player){
    	return dialogueStage;
    }
    
    public void setDialogueStage(int in){
    	dialogueStage = in;
    }
    
    public boolean itemHandling(final Player player, int itemId) {
	switch(itemId) {

	}
	return false;
    }
    
    public boolean itemOnItemHandling(Player player, int firstItem, int secondItem) { return false; }
    
    public boolean doItemOnObject(final Player player, int object, int item) {
	switch(object) {
	    
	}
	return false;
    }
    
    public boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch(object) {
	    
	}
	return false;
    }
    
    public static boolean doMiscObjectClicking(final Player player, int object, int x, int y) {
	switch (object) {
	    
	}
	return false;
    }
    
    public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) {
	    case ACHIETTIES:
		switch (player.getQuestStage(26)) {
		    case 0:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("x", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		    case QUEST_COMPLETE:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("x", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		    case QUEST_STARTED:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("x", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("x", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		}
	    return false;
	}
	return false;
    }

}
