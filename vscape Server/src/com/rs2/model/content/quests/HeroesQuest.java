package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
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
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ShopManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class HeroesQuest implements Quest {
    //Quest stages
    public static final int QUEST_STARTED = 1;
    public static final int SLIME_GOTTEN = 2;
    public static final int QUEST_COMPLETE = 9;
    //Items
    public static final int HAMMER = 2347;
    public static final int CANDLESTICKS = 1577;
    public static final int ARMBAND = 1579;
    public static final int ICE_GLOVES = 1580;
    public static final int BLAMISH_SNAIL_SLIME = 1581;
    public static final int BLAMISH_OIL = 1582;
    public static final int FIREBIRD_FEATHER = 1583;
    public static final int ID_PAPERS = 1584;
    public static final int OILY_FISHING_ROD = 1585;
    public static final int MISC_KEY = 1586;
    public static final int DUSTY_KEY = 1590;
    public static final int JAIL_KEY = 1591;
    public static final int RAW_LAVA_EEL = 2148;
    public static final int LAVA_EEL = 2149;
    public static final int SAMPLE_BOTTLE = 3377;
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
    public static final int MASTER_FISHER = 308;
    public static final int ROACHEY = 592;
    public static final int HARRY = 576;
    public static final int KATRINE = ShieldOfArrav.KATRINE;
    public static final int STRAVEN = ShieldOfArrav.STRAVEN;
    //Objects
    public static final int GUILD_DOOR_2 = 2624;
    public static final int GUILD_DOOR_1 = 2625;
    public static final int ROCKSLIDE = 2634;
    
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
        return player.getQuestStage(13) >= ShieldOfArrav.QUEST_COMPLETE && player.getQuestStage(14) >= LostCity.QUEST_COMPLETE && player.getQuestStage(15) >= DragonSlayer.QUEST_COMPLETE && player.getQuestStage(11) >= MerlinsCrystal.QUEST_COMPLETE && player.getQuestPoints() >= 50;
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
	    case SLIME_GOTTEN:
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("@str@" + "Talk to Achietties, outside the Heroes Guild to begin.", 8147);
	    
		player.getActionSender().sendString("Achietties will let me into the Heroes' Guild if I can get:", 8149);
		if(player.getInventory().ownsItem(FIREBIRD_FEATHER)) {
		    player.getActionSender().sendString("@str@-An Entranan Firebird feather.", 8150);
		}
		else {
		    player.getActionSender().sendString("-An Entranan Firebird feather (I should check on Entrana.)", 8150);
		}
		if(player.getInventory().ownsItem(LAVA_EEL)) {
		    player.getActionSender().sendString("@str@-A cooked lava eel.", 8151);
		}
		else {
		    player.getActionSender().sendString("-A cooked lava eel (I should talk to a fishing expert.)", 8151);
		}
		if(player.getInventory().ownsItem(ARMBAND)) {
		    player.getActionSender().sendString("@str@-A Master Thieves Armband.", 8152);
		}
		else {
		    player.getActionSender().sendString("-A Master Thieves Armband (I should talk to my gang's leader.)", 8152);
		}
		break;
	    case QUEST_COMPLETE:
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("@str@" + "Talk to Achietties, outside the Heroes Guild to begin.", 8147);
	    
		player.getActionSender().sendString("@red@" + "You have completed this quest!", 8170);
		break;
	    default:
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to @dre@Achietties @bla@outside the @dre@Heroes Guild @bla@, north", 8147);
		player.getActionSender().sendString("of Taverly to begin this quest.", 8148);
		player.getActionSender().sendString("@dre@Requirements:", 8149);
		if (player.getQuestPoints() < 50) {
		    player.getActionSender().sendString("-50 Quest Points.", 8150);
		} else {
		    player.getActionSender().sendString("@str@-50 Quest Points.", 8150);
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
		break;
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
	if (player.getQuestPoints() < 50) {
	    player.getActionSender().sendString("-50 Quest Points.", 8150);
	} else {
	    player.getActionSender().sendString("@str@-50 Quest Points.", 8150);
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
    
    public static boolean itemPickupHandling(Player player, int itemId) {
	if (itemId == FIREBIRD_FEATHER) {
	    if ((player.getQuestStage(27) == QUEST_STARTED || player.getQuestStage(27) == SLIME_GOTTEN) && !player.getInventory().ownsItem(FIREBIRD_FEATHER)) {
		if (player.getEquipment().getId(Constants.HANDS) != ICE_GLOVES) {
		    player.getDialogue().sendPlayerChat("I'd better not touch this, it appears extremely hot.", "Perhaps there is a way to grab it...", CONTENT);
		    return true;
		} else {
		    player.getActionSender().sendMessage("You pickup the feather carefully with your ice gloves.");
		    return false;
		}
	    } else {
		player.getDialogue().sendPlayerChat("I think I'll burn my hands getting that!", "I have no need for it either...", CONTENT);
		return true;
	    }
	} else {
	    return false;
	}
    }
    
    public boolean itemOnItemHandling(Player player, int firstItem, int secondItem) { return false; }
    
    public boolean doItemOnObject(final Player player, int object, int item) {
	switch(object) {
	    
	}
	return false;
    }
    
    public boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch(object) {
	    case ROCKSLIDE:
		player.getActionSender().sendMessage("You examine the rock for ores...");
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			player.getActionSender().sendMessage("...it appears to be a rockslide, devoid of ore. Strange.");
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.setStopPacket(false);
		    }
		}, 4);
		return true;
	    case GUILD_DOOR_1:
	    case GUILD_DOOR_2:
		if(player.getQuestStage(27) < QUEST_COMPLETE) {
		    player.getActionSender().sendMessage("You are not a part of the Heroes' Guild.");
		    return true;
		}
		else {
		    player.getActionSender().walkTo(player.getPosition().getX() > 2901 ? -1 : 1, 0, true);
		    player.getActionSender().walkThroughDoubleDoor(GUILD_DOOR_2, GUILD_DOOR_1, 2902, 3510, 2902, 3511, 0);
		    return true;
		}
	}
	return false;
    }
    
    public static boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
	switch (object) {
	    case ROCKSLIDE:
		final CacheObject g = ObjectLoader.object(x, y, player.getPosition().getZ());
		boolean canDoIt = true;
		if (Tools.getTool(player, Skill.MINING) == null) {
		    player.getActionSender().sendMessage("You need a pickaxe to mine here.");
		    canDoIt = false;
		}
		if (player.getSkill().getLevel()[Skill.MINING] < 50) {
		    player.getDialogue().sendStatement("You need atleast 50 Mining to clear this.");
		    canDoIt = false;
		}
		if (!canDoIt) {
		    break;
		}
		player.getActionSender().sendMessage("You attempt to mine your way through...");
		player.getUpdateFlags().sendAnimation(Tools.getTool(player, Skill.MINING).getAnimation());
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			    if (Misc.random(1) == 0) {
				player.getActionSender().sendMessage("...and manage to break through the rock.");
				new GameObject(Constants.EMPTY_OBJECT, x, y, player.getPosition().getZ(), g.getRotation(), g.getType(), ROCKSLIDE, 10);
				player.getActionSender().walkTo(player.getPosition().getX() > 2837 ? -3 : 3, 0, true);
				b.stop();

			    } else {
				player.getActionSender().sendMessage("...but fail to break-up the rock.");
				b.stop();
			    }
		    }

		    @Override
		    public void stop() {
			player.getUpdateFlags().sendAnimation(-1);
			player.setStopPacket(false);
		    }
		}, 3);
	}
	return false;
    }
    
    public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) {
	    case GERRANT:
	    case ROACHEY:
	    case MASTER_FISHER:
	    case HARRY:
		switch (player.getQuestStage(27)) {
		    case SLIME_GOTTEN:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(!player.getInventory().ownsItem(BLAMISH_SNAIL_SLIME)) {
				    player.getDialogue().sendPlayerChat("I lost my snail slime...", CONTENT);
				    return true;
				}
				else {
				    return false;
				}
			    case 2:
				player.getDialogue().sendNpcChat("I'm afraid that's all I had... You can", "try getting a bottle from Canifis and using it directly", "on a blamish snail in Mort Myre.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		    case QUEST_STARTED:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("I want to find out how to catch a lava eel.", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("Lava eels eh? That's a ticky one that is, you'll need a", "lava-proof fishing line. The method for making this would", "be to take an ordinary fishing rod, and then cover it", "with the fire-proof Blamish Oil.", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("You know... thinking about it... I may have a jar of", "Blamish Slime around here somewhere... Now where did", "I put it...?", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendStatement("....");
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("Aha! here it is! Take the slime, mix it with some", "Harralander and water and you'll have the Blamish Oil", "you need for treating your fishing rod.", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendGiveItemNpc("You get handed a strange slimy bottle.", new Item(BLAMISH_SNAIL_SLIME));
				player.getDialogue().endDialogue();
				player.getInventory().addItemOrDrop(new Item(BLAMISH_SNAIL_SLIME));
				player.setQuestStage(27, SLIME_GOTTEN);
				return true;
			}
		    return false;
		}
	    return false;
	    case ACHIETTIES:
		switch (player.getQuestStage(27)) {
		    case 0:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("Greetings. Welcome to the Heroes' Guild.", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("Only the greatest heroes of this land may gain", "entrance to this guild.", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("I'm a hero! May I apply to join?", HAPPY);
				return true;
			    case 4:
				if(!canDoQuest(player)) {
				    player.getDialogue().sendNpcChat("Hmm, I'm afraid you're not quite the heroic type.", "There's a requisite to enter this guild.", CONTENT);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Well, you have a lot of quest points, and you have done", "all of the required quests, so you may now begin the", "tasks to meet the entry requirements for membership in", "the Heroes' Guild. The three items required", CONTENT);
				    player.getDialogue().setNextChatId(6);
				    return true;
				}
			    case 5:
				player.getDialogue().sendStatement("You must have atleast 50 Quest Points and have completed:", "Shield of Arrav, Dragon Slayer, Merlin's Crystal and Lost City,", "to become a member of the Heroes' Guild.");
				player.getDialogue().endDialogue();
				return true;
			    case 6:
				player.getDialogue().sendNpcChat("for entrance are: An Entranan Firebird feather, a", "Master Thieves' armband, and a cooked Lava Eel.", CONTENT);
				return true;
			    case 7:
				player.getDialogue().sendOption("Any hints on getting the armband?", "Any hints on getting the feather?", "Any hints on getting the eel?", "I'll start looking for all those things then.");
				return true;
			    case 8:
				switch(optionId) {
				    case 1:
					player.getDialogue().sendPlayerChat("Any hints on getting the thieves armband?", CONTENT);
					return true;
				    case 2:
					player.getDialogue().sendPlayerChat("Any hints on getting the feather?", CONTENT);
					player.getDialogue().setNextChatId(13);
					return true;
				    case 3:
					player.getDialogue().sendPlayerChat("Any hints on getting the eel?", CONTENT);
					player.getDialogue().setNextChatId(14);
					return true;
				    case 4:
					player.getDialogue().sendPlayerChat("I'll start looking for all those things then.", CONTENT);
					player.getDialogue().setNextChatId(15);
					return true;
				}
			    return false;
			    case 9:
				player.getDialogue().sendNpcChat("Connections you made in the Varrock gangs may be able", "to assist you. Which gang are you a part of?", CONTENT);
				return true;
			    case 10:
				if(player.isPhoenixGang() && !player.isBlackArmGang()) {
				    player.getDialogue().sendPlayerChat("I'm a member of the Phoenix Gang.", CONTENT);
				    return true;
				}
				else if(!player.isPhoenixGang() && player.isBlackArmGang()) {
				    player.getDialogue().sendPlayerChat("I'm a member of the Black Arm Gang.", CONTENT);
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("Erm, I'm not quite sure. The gang may have", "forgotten about me, it's been a while.", CONTENT);
				    player.getDialogue().setNextChatId(12);
				    return true;
				}
			    case 11:
				player.getDialogue().sendNpcChat("Very good, the leader of your gang can point you", "in the right direction.", CONTENT);
				player.getDialogue().setNextChatId(7);
				return true;
			    case 12:
				player.getDialogue().sendNpcChat("You had better re-affirm your membership in the", "gang you chose originally. The leader of that", "gang then will be able to assist you.", CONTENT);
				player.getDialogue().setNextChatId(7);
				return true;
			    case 13:
				player.getDialogue().sendNpcChat("Not really - other than Entranan firebirds tend to live", "on Entrana and burn at molten degrees.", CONTENT);
				player.getDialogue().setNextChatId(7);
				return true;
			    case 14:
				player.getDialogue().sendNpcChat("Maybe go and find someone who makes his living", "off of fishing?", CONTENT);
				player.getDialogue().setNextChatId(7);
				return true;
			    case 15:
				player.getDialogue().sendNpcChat("Good luck with that adventurer.", CONTENT);
				player.getDialogue().endDialogue();
				QuestHandler.startQuest(player, 27);
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
				player.getDialogue().sendNpcChat("How goes thy quest adventurer?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendOption("I'm a little confused, could you help?", "It's going just fine, thanks.");
				return true;
			    case 3:
				switch(optionId) {
				    case 1:
					player.getDialogue().sendPlayerChat("I'm a little confused, could you help?", CONTENT);
					return true;
				    case 2:
					player.getDialogue().sendPlayerChat("It's going just fine, thanks.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			    case 4:
				player.getDialogue().sendNpcChat("I may be able to, what do you need help with?", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendOption("Any hints on getting the armband?", "Any hints on getting the feather?", "Any hints on getting the eel?", "Nevermind.");
				return true;
			    case 6:
				switch(optionId) {
				    case 1:
					player.getDialogue().sendPlayerChat("Any hints on getting the thieves armband?", CONTENT);
					player.getDialogue().setNextChatId(9);
					return true;
				    case 2:
					player.getDialogue().sendPlayerChat("Any hints on getting the feather?", CONTENT);
					player.getDialogue().setNextChatId(13);
					return true;
				    case 3:
					player.getDialogue().sendPlayerChat("Any hints on getting the eel?", CONTENT);
					player.getDialogue().setNextChatId(14);
					return true;
				    case 4:
					player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			    return false;
			    case 9:
				player.getDialogue().sendNpcChat("Connections you made in the Varrock gangs may be able", "to assist you. Which gang are you a part of?", CONTENT);
				return true;
			    case 10:
				if(player.isPhoenixGang() && !player.isBlackArmGang()) {
				    player.getDialogue().sendPlayerChat("I'm a member of the Phoenix Gang.", CONTENT);
				    return true;
				}
				else if(!player.isPhoenixGang() && player.isBlackArmGang()) {
				    player.getDialogue().sendPlayerChat("I'm a member of the Black Arm Gang.", CONTENT);
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("Erm, I'm not quite sure. The gang may have", "forgotten about me, it's been a while.", CONTENT);
				    player.getDialogue().setNextChatId(12);
				    return true;
				}
			    case 11:
				player.getDialogue().sendNpcChat("Very good, the leader of your gang can point you", "in the right direction.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			    case 12:
				player.getDialogue().sendNpcChat("You had better re-affirm your membership in the", "gang you chose originally. The leader of that", "gang then will be able to assist you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			    case 13:
				player.getDialogue().sendNpcChat("Not really - other than Entranan firebirds tend to live", "on Entrana and burn at molten degrees.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			    case 14:
				player.getDialogue().sendNpcChat("Maybe go and find someone who makes his living", "off of fishing?", CONTENT);
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
