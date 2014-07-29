package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.GlobalVariables;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.ANNOYED;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import static com.rs2.model.content.quests.PiratesTreasure.BANANA;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.prayer.Ectofungus;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import java.io.IOException;
import java.util.ArrayList;

public class GhostsAhoy implements Quest {

    public static final int QUEST_STARTED = 1;
    public static final int BACK_TO_VELORINA = 2;
    public static final int TO_CRONE = 3;
    public static final int CRONE_NEEDS_TEA = 4;
    public static final int CRONE_NEEDS_TEA_IN_CUP = 5;
    public static final int CRONE_NEEDS_MILKY_TEA = 6;
    public static final int CRONES_STORY = 7;
    public static final int ITEMS_FOR_ENCHANTMENT = 8;
    public static final int ITEMS_FOR_ENCHANTMENT_2 = 9;
    public static final int CAST_SPELL = 10;
    public static final int TELL_VELORINA = 11;
    public static final int QUEST_COMPLETE = 12;
    
    public static final int BUCKET = 1925;
    public static final int GHOSTSPEAK_AMULET = 552;
    public static final int ENCHANTED_GHOSTSPEAK = 4250;
    public static final int ECTOPHIAL = 4251;
    public static final int ECTOPHIAL_EMPTY = 4252;
    public static final int SIGNED_OAK_BOW = 4236;
    public static final int NETTLE_WATER = 4237;
    public static final int PUDDLE_OF_SLIME = 4238;
    public static final int NETTLE_TEA_BOWL = 4239;
    public static final int EMPTY_CUP = 1918;
    public static final int NETTLE_TEA_BOWL_MILKY = 4240;
    public static final int NETTLES = 4241;
    public static final int EMPTY_BOWL = 1923;
    public static final int NETTLE_TEA_CUP = 4242;
    public static final int NETTLE_TEA_CUP_MILKY = 4243;
    public static final int PORCELAIN_CUP = 4244;
    public static final int PORCELAIN_CUP_NETTLE = 4245;
    public static final int PORCELAIN_CUP_NETTLE_MILKY = 4246;
    public static final int NECROVARUS_ROBES = 4247;
    public static final int BOOK_OF_HARICANTO = 4248;
    public static final int TRANSLATION_MANUAL = 4249;
    public static final int MODEL_SHIP = 4253;
    public static final int MODEL_SHIP_SILK = 4254;
    public static final int CHEST_KEY = 4273;
    public static final int MAP_SCRAP = 4274;
    public static final int MAP_SCRAP_2 = 4275;
    public static final int MAP_SCRAP_3 = 4276;
    public static final int TREASURE_MAP = 4277;
    public static final int ECTOTOKEN = 4278;
    public static final int PETITION = 4283;
    public static final int BEDSHEET = 4284;
    public static final int ECTOPLASM_BEDSHEET = 4285;
    public static final int BONES = 526;
    public static final int POT = 1951;
    public static final int SPADE = 952;
    public static final int BUCKET_OF_MILK = 1927;
    public static final int NEEDLE = 1733;
    public static final int THREAD = 1734;
    public static final int SILK = 950;
    public static final int KNIFE = 946;
    public static final int BOWL_OF_WATER = 1921;
    public static final int OAK_LONGBOW = 845;
    public static final String[] DYES = {"red", "yellow", "blue", "orange", "green", "purple"};
    public static final int RED_DYE = 1763;
    public static final int YELLOW_DYE = 1765;
    public static final int BLUE_DYE = 1767;
    public static final int ORANGE_DYE = 1769;
    public static final int GREEN_DYE = 1771;
    public static final int PURPLE_DYE = 1773;
    
    public static final int BARRIER = 5259;
    public static final int SHIPS_LADDER_UP = 5265;
    public static final int SHIPS_LADDER_DOWN = 5266;
    public static final int MAST = 5274;
    public static final int NETTLES_1 = 5253;
    public static final int NETTLES_2 = 5254;
    public static final int NETTLES_3 = 5255;
    public static final int NETTLES_4 = 5256;
    public static final int NETTLES_5 = 5257;
    public static final int NETTLES_6 = 5258;
    public static final int NETTLES_7 = 1181;
    public static final int PIRATE_CAPTAIN = 5287;
    public static final int TREASURE_CHEST_1 = 5270;
    public static final int[] TREASURE_CHEST_1_OBJ = {3619, 3545};
    
    public static boolean lowWind = false;
    
    
    public static final Position ECTOFUNTUS_POS = new Position(3659, 3517, 0);
    
    public static final int MAP_INTERFACE = 12266;
    public static final int BLACK_INTERFACE_TEXT = 12283, STRING_ON_BLACK = 12285;
    
    
    public static final int VELORINA = 1683;
    public static final int NECROVARUS = 1684;
    public static final int ROBIN = 1694;
    public static final int OLD_CRONE = 1695;
    public static final int OLD_MAN = 1696;
    public static final int GHOST_GUARD = 1706;
    
    public int dialogueStage = 0;
    
    private int reward[][] = {
	{ECTOPHIAL, 1},
    };
    private int expReward[][] = {
	{Skill.PRAYER, 2400},
    };
    
    private static final int questPointReward = 2;

    public int getQuestID() {
        return 24;
    }

    public String getQuestName() {
        return "Ghosts Ahoy";
    }
    
    public String getQuestSaveName()
    {
    	return "ghosts-ahoy";
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
        player.getActionSender().sendString("You have completed: " + getQuestName(), 12144);
	player.getActionSender().sendString("You are rewarded: ", 12146);
        player.getActionSender().sendString("2 Quest Points", 12150);
        player.getActionSender().sendString("5400 Prayer XP", 12151);
	player.getActionSender().sendString("The Ectophial", 12152);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 12282);
    }
    
    public void sendQuestRequirements(Player player) {
        String prefix = "";
        int questStage = player.getQuestStage(getQuestID());
        if (questStage == QUEST_STARTED) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    
	    player.getActionSender().sendString("Velorina told me to try and plead with Necrovarus", 8149);
	    player.getActionSender().sendString("to allow the ghosts of Phasmatys to pass on.", 8150);
	}
	else if (questStage == BACK_TO_VELORINA) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8148);
	    
	    player.getActionSender().sendString("Necrovarus was not interested in negotiating. I should", 8150);
	    player.getActionSender().sendString("probably return to Velorina and see what else can be done.", 8151);
	}
	else if (questStage == TO_CRONE) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8148);
	    
	    player.getActionSender().sendString("Velorina told me of an old disciple who escaped the fate of", 8150);
	    player.getActionSender().sendString("Phasmatys. She said she can be found west of here, near", 8151);
	    player.getActionSender().sendString("the sea and in between two large castles.", 8152);
	}
	else if (questStage == CRONE_NEEDS_TEA) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8148);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8149);
	    
	    player.getActionSender().sendString("The Old Crone seems to not remember anything without some", 8151);
	    player.getActionSender().sendString("nettle tea. I should fetch that if I want any answers.", 8152);
	}
	else if (questStage == CRONE_NEEDS_TEA_IN_CUP) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8148);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8149);
	    
	    player.getActionSender().sendString("Apparently any old container wasn't good enough for the", 8151);
	    player.getActionSender().sendString("Crone, I should put some tea in her 'special' porcelain cup.", 8152);
	}
	else if (questStage == CRONE_NEEDS_MILKY_TEA) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8148);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8149);
	    
	    player.getActionSender().sendString("Even the special cup tea wasn't good enough. The Old Crone", 8151);
	    player.getActionSender().sendString("only drinks her nettle tea with milk...", 8152);
	}
	else if (questStage == CRONES_STORY) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8148);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8149);
	    
	    player.getActionSender().sendString("The Old Crone warmed up after some milky nettle tea.", 8151);
	    player.getActionSender().sendString("I then proceeded to walk away while she was talking.", 8152);
	}
	else if (questStage == ITEMS_FOR_ENCHANTMENT || questStage == ITEMS_FOR_ENCHANTMENT) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8148);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8149);
	    player.getActionSender().sendString("@str@" + "The Old Crone warmed up after some milky nettle tea.", 8150);
	    
	    player.getActionSender().sendString("The Old Crone told me I need 3 things to enchant an", 8152);
	    player.getActionSender().sendString("Amulet of Ghostspeak to be able to control Necrovarus:", 8153);
	    if(player.getInventory().ownsItem(NECROVARUS_ROBES)) {
		player.getActionSender().sendString("@str@-Necrovarus' old robes.", 8154);
	    }
	    else {
		player.getActionSender().sendString("@dre@-Necrovarus' old robes.", 8154);
	    }
	    if(player.getInventory().ownsItem(BOOK_OF_HARICANTO)) {
		player.getActionSender().sendString("@str@-A book of encantations written by one Haricanto.", 8155);
	    }
	    else {
		player.getActionSender().sendString("@dre@-A book of encantations written by one Haricanto.", 8155);
	    }
	    if(player.getInventory().ownsItem(TRANSLATION_MANUAL)) {
		player.getActionSender().sendString("@str@-A translation manual for the encantation book.", 8156);
	    }
	    else {
		player.getActionSender().sendString("@dre@-A translation manual for the encantation book.", 8156);
	    }
	    if(playerHasItems(player)) {
		player.getActionSender().sendString("I have all the items, I should bring them to the", 8157);
		player.getActionSender().sendString("Crone to get my Ghostspeak Amulet enchanted.", 8158);
	    }
	}
	else if (questStage == QUEST_COMPLETE) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    
	    player.getActionSender().sendString("@red@" + "You have completed this quest!", 8167);
	}
        else {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("Talk to @dre@Velorina @bla@in @dre@Port Phasmatys @bla@to begin this quest.", 8147);
	    player.getActionSender().sendString("@dre@Requirements:", 8148);
	    if(player.getSkill().getLevel()[Skill.AGILITY] < 25) {
		player.getActionSender().sendString("-Level 25 Agility.", 8150);
	    }
	    else {
		player.getActionSender().sendString("@str@-25 Agility.", 8150);
	    }
	    if(player.getSkill().getLevel()[Skill.COOKING] < 20) {
		player.getActionSender().sendString("-Level 20 Cooking.", 8151);
	    }
	    else {
		player.getActionSender().sendString("@str@-20 Cooking.", 8151);
	    }
	    if(player.getQuestStage(23) < 12) {
		player.getActionSender().sendString("-Access to Canifis.", 8152);
	    }
	    else {
		player.getActionSender().sendString("@str@ -Access to Canifis.", 8152);
	    }
	    player.getActionSender().sendString("-Ability to defeat a level 32 lobster.", 8153);
        }
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 12282);
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
    		player.getActionSender().sendString("@yel@"+getQuestName(), 12282);
    	} else if (questStage == QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 12282);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 12282);
    	}
    	
    }
    
    public int getQuestPoints() {
        return questPointReward;
    }
    public void clickObject(Player player, int object) {
    }

    public void showInterface(Player player){
    	String prefix = "";
    	player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("Talk to @dre@Velorina @bla@in @dre@Port Phasmatys @bla@to begin this quest.", 8147);
	    player.getActionSender().sendString("@dre@Requirements:", 8148);
	    if(player.getSkill().getLevel()[Skill.AGILITY] < 25) {
		player.getActionSender().sendString("-Level 25 Agility.", 8150);
	    }
	    else {
		player.getActionSender().sendString("@str@-25 Agility.", 8150);
	    }
	    if(player.getSkill().getLevel()[Skill.COOKING] < 20) {
		player.getActionSender().sendString("-Level 20 Cooking.", 8151);
	    }
	    else {
		player.getActionSender().sendString("@str@-20 Cooking.", 8151);
	    }
	    if(player.getQuestStage(23) < 12) {
		player.getActionSender().sendString("-Access to Canifis.", 8152);
	    }
	    else {
		player.getActionSender().sendString("@str@ -Access to Canifis.", 8152);
	    }
	    player.getActionSender().sendString("-Ability to defeat a level 32 lobster.", 8153);
	    player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void dialogue(Player player, Npc npc){
    	Dialogues.startDialogue(player, VELORINA);
    }
    
    public int getDialogueStage(Player player){
    	return dialogueStage;
    }
    
    public void setDialogueStage(int in){
    	dialogueStage = in;
    }
    public static boolean playerHasItems(final Player player) {
	return player.getInventory().ownsItem(NECROVARUS_ROBES) && player.getInventory().ownsItem(BOOK_OF_HARICANTO) && player.getInventory().ownsItem(TRANSLATION_MANUAL);
    }
    public static boolean playerHasItemsInInventory(final Player player) {
	return player.getInventory().playerHasItem(NECROVARUS_ROBES) && player.getInventory().playerHasItem(BOOK_OF_HARICANTO) && player.getInventory().playerHasItem(TRANSLATION_MANUAL);
    }
    public static void handleWindSpeed(final Player player) {
	if(Misc.random(10) == 2) {
	    lowWind = true;
	    player.getActionSender().sendString("Low", 12281);
	    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer b) {
		    lowWind = false;
		    b.stop();
		}

		@Override
		public void stop() {
		}
	    }, 6);
	} else {
	    player.getActionSender().sendString("High", 12281);
	}
    }
    
    public static boolean itemHandling(final Player player, int itemId) {
	switch(itemId) {
	    case MODEL_SHIP_SILK:
		player.getDialogue().sendStatement("The top half of the flag is " + player.getTopHalfFlag() + ".", "The bottom half of the flag is " + player.getBottomHalfFlag() + ".", "The skull emblem of the flag is " + player.getSkullFlag() + ".");
		return true;
	    case MODEL_SHIP:
		if (player.getInventory().playerHasItem(SILK)) {
		    if (player.getInventory().playerHasItem(THREAD) && player.getInventory().playerHasItem(NEEDLE) && player.getInventory().playerHasItem(KNIFE)) {
			player.getDialogue().sendGiveItemNpc("You replace the model ship's missing flag.", new Item(MODEL_SHIP_SILK));
			player.getInventory().removeItem(new Item(THREAD));
			player.getInventory().removeItem(new Item(SILK));
			player.getInventory().replaceItemWithItem(new Item(MODEL_SHIP), new Item(MODEL_SHIP_SILK));
			return true;
		    } else {
			player.getDialogue().sendStatement("You need a knife, needle, and some thread to cut", "and attach the sail to the model ship.");
			return true;
		    }
		} else {
		    player.getDialogue().sendPlayerChat("Hmm, I should probably add some sort of sail", "to this. Some silk would do.", CONTENT);
		    return true;
		}
	    case ECTOPHIAL:
		if(player.getTeleportation().attemptEctophialTeleport(ECTOFUNTUS_POS)) {
		    player.getInventory().replaceItemWithItem(new Item(ECTOPHIAL), new Item(ECTOPHIAL_EMPTY));
		    player.getActionSender().sendMessage("You empty the ectoplasm on the ground around your feet...");
		    player.getUpdateFlags().sendAnimation(714);
		    player.getUpdateFlags().sendHighGraphic(301);
		    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
			    player.getActionSender().sendMessage("...and the world changes around you.");
			    player.teleport(ECTOFUNTUS_POS);
			    b.stop();
			}

			@Override
			public void stop() {
			    player.setStopPacket(false);
			}
		    }, 4);
		    return true;
		}
		else {
		    return false;
		}
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
	switch(firstItem) {
	    case RED_DYE:
	    case BLUE_DYE:
	    case YELLOW_DYE:
	    case ORANGE_DYE:
	    case GREEN_DYE: 
	    case PURPLE_DYE:
		if(secondItem == MODEL_SHIP_SILK) {
		    Dialogues.startDialogue(player, 11575);
		    player.setTempInteger(firstItem);
		    return true;
		}
	}
	if(firstItem == SILK && secondItem == MODEL_SHIP) {
	    if(player.getInventory().playerHasItem(THREAD) && player.getInventory().playerHasItem(NEEDLE) && player.getInventory().playerHasItem(KNIFE) ) {
		player.getDialogue().sendGiveItemNpc("You replace the model ship's missing flag.", new Item(MODEL_SHIP_SILK));
		player.getInventory().removeItem(new Item(THREAD));
		player.getInventory().removeItem(new Item(SILK));
		player.getInventory().replaceItemWithItem(new Item(MODEL_SHIP), new Item(MODEL_SHIP_SILK));
		return true;
	    }
	    else {
		player.getDialogue().sendStatement("You need a knife, needle, and some thread to cut", "and attach the sail to the model ship.");
		return true;
	    }
	}
	if(firstItem == NETTLE_TEA_BOWL && secondItem == EMPTY_CUP) {
	    player.getActionSender().sendMessage("You pour the tea into the cup.");
	    player.getInventory().replaceItemWithItem(new Item(NETTLE_TEA_BOWL), new Item(EMPTY_BOWL));
	    player.getInventory().replaceItemWithItem(new Item(EMPTY_CUP), new Item(NETTLE_TEA_CUP));
	    return true;
	}
	else if(firstItem == BUCKET_OF_MILK && secondItem == NETTLE_TEA_BOWL) {
	    player.getActionSender().sendMessage("You pour your milk into the nettle tea bowl.");
	    player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_MILK), new Item(BUCKET));
	    player.getInventory().replaceItemWithItem(new Item(NETTLE_TEA_BOWL), new Item(NETTLE_TEA_BOWL_MILKY));
	    return true;
	}
	else if(firstItem == BUCKET_OF_MILK && secondItem == NETTLE_TEA_CUP) {
	    player.getActionSender().sendMessage("You pour your milk into the nettle tea cup.");
	    player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_MILK), new Item(BUCKET));
	    player.getInventory().replaceItemWithItem(new Item(NETTLE_TEA_CUP), new Item(NETTLE_TEA_CUP_MILKY));
	    return true;
	}
	else if(firstItem == BUCKET_OF_MILK && secondItem == PORCELAIN_CUP_NETTLE) {
	    player.getActionSender().sendMessage("You pour your milk into the Old Crone's special cup.");
	    player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_MILK), new Item(BUCKET));
	    player.getInventory().replaceItemWithItem(new Item(PORCELAIN_CUP_NETTLE), new Item(PORCELAIN_CUP_NETTLE_MILKY));
	    return true;
	}
	else if((firstItem == NETTLE_TEA_BOWL && secondItem == PORCELAIN_CUP) || (firstItem == NETTLE_TEA_CUP && secondItem == PORCELAIN_CUP)) {
	    player.getActionSender().sendMessage("You pour your tea into the Old Crone's special cup.");
	    player.getInventory().replaceItemWithItem(new Item(firstItem == NETTLE_TEA_BOWL ? NETTLE_TEA_BOWL : NETTLE_TEA_CUP), new Item(firstItem == NETTLE_TEA_BOWL ? EMPTY_BOWL : EMPTY_CUP));
	    player.getInventory().replaceItemWithItem(new Item(PORCELAIN_CUP), new Item(PORCELAIN_CUP_NETTLE));
	    return true;
	}
	else if((firstItem == NETTLE_TEA_BOWL_MILKY && secondItem == PORCELAIN_CUP) || (firstItem == NETTLE_TEA_CUP_MILKY && secondItem == PORCELAIN_CUP)) {
	    player.getActionSender().sendMessage("You pour your milky tea into the Old Crone's special cup.");
	    player.getInventory().replaceItemWithItem(new Item(firstItem == NETTLE_TEA_BOWL_MILKY ? NETTLE_TEA_BOWL_MILKY : NETTLE_TEA_CUP_MILKY), new Item(firstItem == NETTLE_TEA_BOWL_MILKY ? EMPTY_BOWL : EMPTY_CUP));
	    player.getInventory().replaceItemWithItem(new Item(PORCELAIN_CUP), new Item(PORCELAIN_CUP_NETTLE_MILKY));
	    return true;
	}
	return false;
    }
    
    public static boolean doItemOnObject(final Player player, int object, int item) {
	switch(object) {
	    case Ectofungus.ECTOFUNGUS:
		if(item == ECTOPHIAL_EMPTY) {
		    player.getUpdateFlags().sendAnimation(883);
		    player.getInventory().replaceItemWithItem(new Item(ECTOPHIAL_EMPTY), new Item(ECTOPHIAL));
		    player.getActionSender().sendMessage("You refill the ectophial from the Ectofuntus.");
		    return true;
		}
	}
	return false;
    }
    
    public static boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch(object) {
	    case PIRATE_CAPTAIN:
		player.getDialogue().sendStatement("The pirate captain ignore you and continues to stare lifelessly", "at nothing, as he has clearly been dead for some time.");
		return true;
	    case MAST:
		if(!lowWind) {
		    player.getDialogue().sendStatement("You see a tattered flag blowing in the wind.", "The wind is blowing too hard to make out any details.");
		    return true;
		} else {
		    int index1 = Misc.randomMinusOne(DYES.length);
		    int index2 = 0, index3 = 0;
		    if(player.getDesiredTopHalfFlag().equals("black")) {
			player.getDialogue().sendStatement("You see a tattered flag blowing in the wind.", "The top half of the flag is colored " + DYES[index1] + ".");
			player.setDesiredGhostsAhoyFlag("top", DYES[index1]);
			return true;
		    }
		    else if(!player.getDesiredTopHalfFlag().equals("black") && player.getDesiredBottomHalfFlag().equals("black")) {
			do {
			    index2 = Misc.randomMinusOne(DYES.length);
			} while (DYES[index2].equals(player.getDesiredTopHalfFlag()));
			player.getDialogue().sendStatement("You see a tattered flag blowing in the wind.", "The bottom half of the flag is colored " + DYES[index2] + ".");
			player.setDesiredGhostsAhoyFlag("bottom", DYES[index2]);
			return true;
		    }
		    else if(!player.getDesiredTopHalfFlag().equals("black") && !player.getDesiredBottomHalfFlag().equals("black") && player.getDesiredSkullFlag().equals("black")) {
			do {
			    index3 = Misc.randomMinusOne(DYES.length);
			} while (DYES[index3].equals(player.getDesiredBottomHalfFlag()));
			player.getDialogue().sendStatement("You see a tattered flag blowing in the wind.", "The skull emblem on the flag is colored " + DYES[index3] + ".");
			player.setDesiredGhostsAhoyFlag("skull", DYES[index3]);
			return true;
		    }
		    else if(!player.getDesiredTopHalfFlag().equals("black") && !player.getDesiredBottomHalfFlag().equals("black") && !player.getDesiredSkullFlag().equals("black")){
			player.getDialogue().sendStatement("You see a tattered flag blowing in the wind.",  "The top half of the flag is colored " + player.getDesiredTopHalfFlag() + ".", "The bottom half of the flag is colored " + player.getDesiredBottomHalfFlag() + ".","The skull emblem on the flag is colored " + player.getDesiredSkullFlag() + ".");
			return true;
		    }
		}
	    case NETTLES_1:
	    case NETTLES_2:
	    case NETTLES_3:
	    case NETTLES_4:
	    case NETTLES_5:
	    case NETTLES_6:
	    case NETTLES_7:
		if(player.getEquipment().getItemContainer().get(Constants.HANDS) != null 
		   && player.getEquipment().getItemContainer().get(Constants.HANDS).getDefinition().getName().toLowerCase().contains("gloves")) {
		    player.getUpdateFlags().sendAnimation(827);
		    player.getActionSender().sendMessage("You attempt to pick some nettles...");
		    player.setStopPacket(true);
		    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
			    player.getActionSender().sendMessage("You safely pick them with your gloves.");
			    player.getInventory().addItemOrDrop(new Item(NETTLES));
			    b.stop();
			}

			@Override
			public void stop() {
			    player.setStopPacket(false);
			}
		    }, 2);
		    return true;
		}
		else {
		    player.getUpdateFlags().sendAnimation(827);
		    player.getActionSender().sendMessage("You attempt to pick some nettles...");
		    player.setStopPacket(true);
		    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
			    player.getActionSender().sendMessage("You get stung terribly without gloves on!");
			    player.hit(2, HitType.NORMAL);
			    b.stop();
			}

			@Override
			public void stop() {
			    player.setStopPacket(false);
			}
		    }, 3);
		    return true;
		}
	    case SHIPS_LADDER_UP:
		if(x == 3615 && y == 3541) {
		    Ladders.climbLadder(player, new Position(3616, 3541, 2));
		    return true;
		}
		else {
		    Ladders.climbLadder(player, new Position(x < 3612 ? 3608 : 3614, 3543, 1));
		    return true;
		}
	    case SHIPS_LADDER_DOWN:
		if(x == 3615 && y == 3541) {
		    Ladders.climbLadder(player, new Position(3614, 3541, 1));
		    return true;
		}
		else {
		    Ladders.climbLadder(player, new Position(x < 3612 ? 3610 : 3612, 3543, 0));
		    return true;
		}
	    case BARRIER:
		if((player.getPosition().getX() > 3657 && player.getPosition().getY() > 3507) || (player.getPosition().getX() < 3653 && player.getPosition().getY() < 3490)) {
		    Dialogues.startDialogue(player, 11111);
		    return true;
		}
		else if(player.getPosition().getX() > 3657 && player.getPosition().getY() < 3508) {
		    player.getActionSender().walkTo(0, 2, true);
		    return true;
		}
		else if(player.getPosition().getX() > 3652 && player.getPosition().getY() < 3490) {
		    player.getActionSender().walkTo(-2, 0, true);
		    return true;
		}
		else {
		    return false;
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
	    case 11575: //the ship's flag
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendOption("Dye the top half of the flag.", "Dye the bottom half of the flag.", "Dye the flag's skull emblem.");
			//player.getDialogue().sendTitledOption("@dre@Which part of the flag do you want to dye?", "Top Half", "Bottom Half", "Skull Emblem");
			return true;
		    case 2:
			switch(optionId) {
			    case 1: //top half
				switch(player.getTempInteger()) {
				    case RED_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag red.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(RED_DYE));
					player.dyeGhostsAhoyFlag("top", "red");
					player.setTempInteger(0);
					return true;
				    case BLUE_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag blue.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(BLUE_DYE));
					player.dyeGhostsAhoyFlag("top", "blue");
					player.setTempInteger(0);
					return true;
				    case YELLOW_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag yellow.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(YELLOW_DYE));
					player.dyeGhostsAhoyFlag("top", "yellow");
					player.setTempInteger(0);
					return true;
				    case ORANGE_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag orange.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(ORANGE_DYE));
					player.dyeGhostsAhoyFlag("top", "orange");
					player.setTempInteger(0);
					return true;
				    case PURPLE_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag purple.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(PURPLE_DYE));
					player.dyeGhostsAhoyFlag("top", "purple");
					player.setTempInteger(0);
					return true;
				    case GREEN_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag green.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(GREEN_DYE));
					player.dyeGhostsAhoyFlag("top", "green");
					player.setTempInteger(0);
					return true;
				}
			    return false;
			    case 2: //bottom half
				switch(player.getTempInteger()) {
				    case RED_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag red.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(RED_DYE));
					player.dyeGhostsAhoyFlag("bottom", "red");
					player.setTempInteger(0);
					return true;
				    case BLUE_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag blue.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(BLUE_DYE));
					player.dyeGhostsAhoyFlag("bottom", "blue");
					player.setTempInteger(0);
					return true;
				    case YELLOW_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag yellow.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(YELLOW_DYE));
					player.dyeGhostsAhoyFlag("bottom", "yellow");
					player.setTempInteger(0);
					return true;
				    case ORANGE_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag orange.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(ORANGE_DYE));
					player.dyeGhostsAhoyFlag("bottom", "orange");
					player.setTempInteger(0);
					return true;
				    case PURPLE_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag purple.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(PURPLE_DYE));
					player.dyeGhostsAhoyFlag("bottom", "purple");
					player.setTempInteger(0);
					return true;
				    case GREEN_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag green.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(GREEN_DYE));
					player.dyeGhostsAhoyFlag("bottom", "green");
					player.setTempInteger(0);
					return true;
				}
			    return false;
			    case 3: //skull
				switch(player.getTempInteger()) {
				    case RED_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag red.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(RED_DYE));
					player.dyeGhostsAhoyFlag("skull", "red");
					player.setTempInteger(0);
					return true;
				    case BLUE_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag blue.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(BLUE_DYE));
					player.dyeGhostsAhoyFlag("skull", "blue");
					player.setTempInteger(0);
					return true;
				    case YELLOW_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag yellow.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(YELLOW_DYE));
					player.dyeGhostsAhoyFlag("skull", "yellow");
					player.setTempInteger(0);
					return true;
				    case ORANGE_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag orange.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(ORANGE_DYE));
					player.dyeGhostsAhoyFlag("skull", "orange");
					player.setTempInteger(0);
					return true;
				    case PURPLE_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag purple.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(PURPLE_DYE));
					player.dyeGhostsAhoyFlag("skull", "purple");
					player.setTempInteger(0);
					return true;
				    case GREEN_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag green.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(GREEN_DYE));
					player.dyeGhostsAhoyFlag("skull", "green");
					player.setTempInteger(0);
					return true;
				}
			    return false;
			}
		}
	    return false;
	    case 11111: //energy barrier
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().setLastNpcTalk(GHOST_GUARD);
			player.getDialogue().sendNpcChat("All visitors to Port Phasmatys must pay a toll charge", "of 2 Ecto-Tokens.", CONTENT);
			return true;
		    case 2:
			if(player.getInventory().playerHasItem(ECTOTOKEN, 2)) {
			    player.getDialogue().sendOption("Pay Toll (2 Ecto-Tokens)", "Nevermind.");
			    return true;
			}
			else {
			    player.getDialogue().sendOption("I don't have that many Ecto-tokens.", "Where can I get Ecto-tokens?");
			    player.getDialogue().setNextChatId(5);
			    return true;
			}
		    case 3:
			switch(optionId) {
			    case 1:
				player.getDialogue().setLastNpcTalk(GHOST_GUARD);
				player.getDialogue().sendNpcChat("Thank you.", CONTENT);
				player.getInventory().removeItem(new Item(ECTOTOKEN, 2));
				player.getDialogue().endDialogue();
				if(player.getPosition().getY() > 3500) {
				    player.getActionSender().walkTo(0, player.getPosition().getY() < 3508 ? 1 : -2, true);
				    return true;
				}
				else {
				   player.getActionSender().walkTo(player.getPosition().getX() < 3653 ? 2 : -1, 0, true);
				   return true; 
				}
			    case 2:
				player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 5:
			switch(optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("I don't have that many Ecto-tokens.", SAD);
				player.getDialogue().endDialogue();
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("Where can I get Ecto-tokens?", CONTENT);
				return true;
			}
		    case 6:
			player.getDialogue().setLastNpcTalk(GHOST_GUARD);
			player.getDialogue().sendNpcChat("You need to go to the Temple and earn some.", "Talk to the disciples - they will tell you how.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		}
	    return false;
	    case NECROVARUS:
		switch(player.getQuestStage(24)) {
		    case 1: //talk to meeeee
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("I must speak with you on behalf of Velorina.", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("You dare to speak that name in this place?!", ANGRY_1);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("She wants to pass...", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("Silence!! Or I will incinerate the flesh from your bones!", ANGRY_1);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("But, she...", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendNpcChat("Get out of my sight!! Or I promise you that", "you will regret your insolence for the rest of eternity!!", ANGRY_2);
				player.getDialogue().endDialogue();
				player.setQuestStage(24, BACK_TO_VELORINA);
				return true;
			}
		    return false;
		    case CAST_SPELL:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("You dare to face me again, you must be truly insane!!", ANGRY_2);
				return true;
			    case 2:
				if(player.getEquipment().getId(Constants.AMULET) == ENCHANTED_GHOSTSPEAK) {
				    player.getDialogue().sendPlayerChat("No, Necrovarus, I am not insane. With this enchanted", "amulet, I have the power to command you to do my will!", CONTENT);
				    return true;
				}
				else if(player.getEquipment().getId(Constants.AMULET) != ENCHANTED_GHOSTSPEAK && player.getInventory().playerHasItem(ENCHANTED_GHOSTSPEAK)) {
				    player.getDialogue().sendPlayerChat("No, Necrovarus, I am not insane! Err...", "Hold on...", CONTENT);
				    player.getDialogue().setNextChatId(13);
				    return true;
				}
				else if(player.getEquipment().getId(Constants.AMULET) != ENCHANTED_GHOSTSPEAK && !player.getInventory().playerHasItem(ENCHANTED_GHOSTSPEAK)) {
				    player.getDialogue().sendPlayerChat("No, Necrovarus, I am not insane! Err...", "Hold on...", CONTENT);
				    player.getDialogue().setNextChatId(15);
				    return true;
				}
			    case 3:
				player.getDialogue().sendStatement("A beam of intense green light radiates out from the", "amulet of ghostspeak, enveloping Necrovarus in it's power.", "His eyes become softer, and appear to stare into nothingness.");
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("I - will - let....", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("Carry on.", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendNpcChat("...any...", CONTENT);
				return true;
			    case 7:
				player.getDialogue().sendPlayerChat("Yes?", CONTENT);
				return true;
			    case 8:
				player.getDialogue().sendNpcChat("...any ghost who so wishes...", CONTENT);
				return true;
			    case 9:
				player.getDialogue().sendPlayerChat("I think we're almost getting there.", CONTENT);
				return true;
			    case 10:
				player.getDialogue().sendNpcChat("...pass into the next world.", CONTENT);
				return true;
			    case 11:
				player.getDialogue().sendPlayerChat("That's it. Now I don't have to deal with you", "any longer.", CONTENT);
				return true;
			    case 12:
				player.getDialogue().sendGiveItemNpc("The amulet of ghostspeak fades back to it's original state.", new Item(GHOSTSPEAK_AMULET));
				player.getDialogue().endDialogue();
				if(player.getEquipment().getItemContainer().get(Constants.AMULET) != null && player.getEquipment().getItemContainer().get(Constants.AMULET).getId() == ENCHANTED_GHOSTSPEAK) {
				    player.getEquipment().getItemContainer().set(Constants.AMULET, new Item(GHOSTSPEAK_AMULET));
				    player.getEquipment().refresh();
				    player.setQuestStage(24, TELL_VELORINA);
				}
			    return true;
			    case 13:
				player.getDialogue().sendStatement("You need to equip the enchanted ghostspeak to", "compel Necrovarus to do your will.");
				player.getDialogue().endDialogue();
				return true;
			    case 15:
				player.getDialogue().sendStatement("You need an enchanted amulet of ghostspeak to", "compel Necrovarus to do your will.");
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		}
	    return false;
	    case OLD_MAN:
		switch(player.getQuestStage(24)) {
		    case ITEMS_FOR_ENCHANTMENT: //start
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("What is your name old man?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("My name? I... I cannot remember my name...", "But I am a fierce pirate! Sailor of the seven seas!", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("Interesting.", CONTENT);
				if(!player.getInventory().playerHasItem(MODEL_SHIP_SILK)) {
				    player.getDialogue().endDialogue();
				}
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("Is this your model ship?", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendGiveItemNpc("You show the Old Man the model ship.", new Item(MODEL_SHIP_SILK));
				return true;
			    case 6:
				if(player.getTopHalfFlag().equals(player.getDesiredTopHalfFlag()) && player.getBottomHalfFlag().equals(player.getDesiredBottomHalfFlag()) && player.getSkullFlag().equals(player.getDesiredSkullFlag()) ) {
				    player.getDialogue().sendNpcChat("My word - so it is!!! I never thought I would", "see it again!! Where did you get it from?", HAPPY);
				    return true;
				}
				else if(!player.getTopHalfFlag().equals(player.getDesiredTopHalfFlag())) {
				    player.getDialogue().sendNpcChat("Hmmm, it looks similar, but the top of the", "flag is the wrong color, that surely isn't my", "model ship.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getBottomHalfFlag().equals(player.getDesiredBottomHalfFlag())) {
				    player.getDialogue().sendNpcChat("Hmmm, it looks similar, but the bottom of the", "flag is the wrong color, that surely isn't my", "model ship.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getSkullFlag().equals(player.getDesiredSkullFlag())) {
				    player.getDialogue().sendNpcChat("Hmmm, it looks similar, but the skull of the", "flag is the wrong color, that surely isn't my", "model ship.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("I don't recognize that at all...", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 7:
				player.getDialogue().sendPlayerChat("Your mother gave it to me to pass on to you.", CONTENT);
				return true;
			    case 8:
				player.getDialogue().sendNpcChat("My mother? She still lives??", HAPPY);
				return true;
			    case 9:
				player.getDialogue().sendPlayerChat("Yes, in a shack to the west of here.", CONTENT);
				return true;
			    case 10:
				player.getDialogue().sendNpcChat("After all these years...", CONTENT);
				return true;
			    case 11:
				player.getDialogue().sendNpcChat("Thank you adventurer, you have provided me with", "some hope in my life! Let me keep my old boat", "and I'll give you this key to an old pirate's chest.", CONTENT);
				return true;
			    case 12:
				player.getDialogue().sendPlayerChat("Sounds reasonable.", CONTENT);
				return true;
			    case 13:
				player.getDialogue().sendGiveItemNpc("You hand the Old Man his model ship.", "He gives you an old key in return.", new Item(MODEL_SHIP_SILK), new Item(CHEST_KEY));
				player.getDialogue().endDialogue();
				player.getInventory().replaceItemWithItem(new Item(MODEL_SHIP_SILK), new Item(CHEST_KEY));
				player.setQuestStage(24, ITEMS_FOR_ENCHANTMENT_2);
				return true;
			}
		    return false;
		case ITEMS_FOR_ENCHANTMENT_2: //start
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(!player.getInventory().ownsItem(CHEST_KEY)) {
				    player.getDialogue().sendPlayerChat("I seem to have misplaced that key you gave me.", CONTENT);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Thank you again adventurer, for bringing", "an old man some joy.", HAPPY);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				player.getDialogue().sendNpcChat("Luckily I have a copy, and this key is worth", "nothing to me compared to this model ship.", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendGiveItemNpc("The old man hands you a replacement key.", new Item(CHEST_KEY));
				player.getDialogue().endDialogue();
				player.getInventory().addItemOrDrop(new Item(CHEST_KEY));
				return true;
			}
		    return false;
		}
	    return false;
	    case VELORINA:
		switch(player.getQuestStage(24)) {
		    case 0: //start
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("Take pity on me, please - eternity stretches", "out before me and I am helpless in it's grasp.", SAD);
				return true;
			    case 2:
				player.getDialogue().sendOption("Why, what is the matter?", "Sorry, I'm scared of ghosts.");
				return true;
			    case 3:
				switch(optionId) {
				    case 1:
					player.getDialogue().sendPlayerChat("Why, what is the matter?", CONTENT);
					return true;
				    case 2:
					player.getDialogue().sendPlayerChat("Sorry, you're a little too spooky.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			    case 4:
				player.getDialogue().sendNpcChat("Oh, I'm sorry - I was just wailing out loud.", "I didn't mean to scare you.", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("No, that's okay. It takes more than a ghost", "to scare me. What is wrong?", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendNpcChat("Do you know the history of our town?", CONTENT);
				return true;
			    case 7:
				player.getDialogue().sendPlayerChat("Something about how you are all stuck", "in the mortal world as ghosts?", CONTENT);
				return true;
			    case 8:
				player.getDialogue().sendNpcChat("Would you help us obtain our release into the next world?", CONTENT);
				return true;
			    case 9:
				player.getDialogue().sendOption("Yes.", "No.");
				return true;
			    case 10:
				switch(optionId) {
				    case 1:
					player.getDialogue().sendPlayerChat("Yes, I'll help you!", HAPPY);
					return true;
				    case 2:
					player.getDialogue().sendPlayerChat("No, I have better things to do.", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			    case 11:
				player.getDialogue().sendNpcChat("Oh, thank you!", CONTENT);
				return true;
			    case 12:
				player.getDialogue().sendNpcChat("Necrovarus will not listen to those of us who", "are already dead. He might rethink his position", "if someone with a mortal soul pleaded our cause.", CONTENT);
				return true;
			    case 13:
				player.getDialogue().sendNpcChat("If he declines, there may yet be another way.", CONTENT);
				return true;
			    case 14:
				player.getDialogue().sendOption("I'll go talk to him.", "Necrovarus?");
				return true;
			    case 15:
				switch(optionId) {
				    case 1:
					player.getDialogue().sendPlayerChat("I'll go talk to him.", CONTENT);
					player.getDialogue().endDialogue();
					player.setQuestStage(24, 1);
					QuestHandler.getQuests()[24].startQuest(player);
					return true;
				    case 2:
					player.getDialogue().sendPlayerChat("Who is Necrovarus?", CONTENT);
					return true;
				}
			    case 16:
				player.getDialogue().sendNpcChat("Necrovarus is the powerful creator of the Ectofuntus.", "You can find him amongst his disciples there.", CONTENT);
				return true;
			    case 17:
				player.getDialogue().sendPlayerChat("Thanks, I'll go talk to him.", CONTENT);
				player.getDialogue().endDialogue();
				player.setQuestStage(24, QUEST_STARTED);
				QuestHandler.getQuests()[24].startQuest(player);
				return true;
			}
		    return false;
		    case 1: //talk to necrovarus
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("I sense that you have not yet spoken to Necrovarus...", SAD);
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		    case BACK_TO_VELORINA:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("I'm sorry, but Necrovarus will not let you go.", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("I feared as much.", "His spirit is a thing of fire and wrath.", SAD);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("You spoke of another way.", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("It is a small chance. During the building of the Ectofuntus", "one of Necrovarus' disciples spoke out against him.", "It is such a long time ago I cannot remember her name,", "although I knew her as a friend.", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("She fled before the Ectofuntus took control over us,", "but being a disciple of Necrovarus she would have privy", "to many of his darkest secrets. She may know of a way", "to aid us without Necrovarus.", CONTENT);
				return true;
			    case 6:
				 player.getDialogue().sendPlayerChat("Do you know where this woman can be found?", CONTENT);
				return true;
			    case 7:
				player.getDialogue().sendNpcChat("I have a vision of a small wooden shack, the land", "it was built on is the unholy soil of Morytania.", "I sense the sea is very close, and that there looms", "castles to the west and the east.", CONTENT);
				return true;
			    case 8:
				player.getDialogue().sendPlayerChat("I'll try and find her.", CONTENT);
				player.getDialogue().endDialogue();
				player.setQuestStage(24, TO_CRONE);
				return true;
			}
		    return false;
		    case TELL_VELORINA:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("You dont need to tell me " + player.getUsername() + ",", "I sensed the removal of Necrovarus' psychic barrier!", HAPPY);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("Only happy to help out.", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("Here, take this as a thank you for the service", "you have given us.", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendGiveItemNpc("Velorina brandishes a vial of bright green ectoplasm.", new Item(ECTOPHIAL));
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("This is an Ectophial. If you ever want to", "come back to Port Phastmatys, empty this on the floor", "beneath your feet, and you will instantly be teleported", "to the temple - the source of it's power.", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendNpcChat("Remember that once the Ectophial has been used,", "you need to refill it from the Ectofuntus.", "Thank you again adventurer.", HAPPY);
				return true;
			    case 7:
				player.setQuestStage(24, QUEST_COMPLETE);
				QuestHandler.completeQuest(player, 24);
				player.getDialogue().dontCloseInterface();
				return true;
			}
		    return false;
		}
	    return false;
	    case OLD_CRONE:
		switch(player.getQuestStage(24)) {
		    case TO_CRONE:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("Were you once a disciple of Necrovarus in the", "Temple of Phasmatys, old woman?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("I don't remember; I am so old and my memory only", "goes back so far...", SAD);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("Is there anything I can do to refresh your memory?", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("Yes, I would love some nice hot nettle tea.", CONTENT);
				return true;
			    case 5:
				if(player.getInventory().playerHasItem(NETTLE_TEA_BOWL) || player.getInventory().playerHasItem(NETTLE_TEA_CUP)) {
				    player.getDialogue().sendPlayerChat("Here's some nice tea for you, like you asked.", CONTENT);
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("I'm afraid I don't have any tea like that.", "I'll go fetch some.", CONTENT);
				    player.getDialogue().endDialogue();
				    player.setQuestStage(24, CRONE_NEEDS_TEA);
				    return true;
				}
			    case 6:
				player.getDialogue().sendStatement("You go to hand the tea to the Old Crone...");
				return true;
			    case 7:
				player.getDialogue().sendNpcChat("Ack! That tea isn't in my special cup!", "I only ever drink from my special cup!!", DISTRESSED);
				return true;
			    case 8:
				player.getDialogue().sendPlayerChat("I see... Can I have this special cup, then?", CONTENT);
				return true;
			    case 9:
				player.getDialogue().sendGiveItemNpc("The Old Crone hands you her 'special cup'.", new Item(PORCELAIN_CUP));
				player.getInventory().addItemOrDrop(new Item(PORCELAIN_CUP));
				player.getDialogue().endDialogue();
				player.setQuestStage(24, CRONE_NEEDS_TEA_IN_CUP);
				return true;
			}
		    return false;
		    case CRONE_NEEDS_TEA:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("Did you get me some nice hot nettle tea?", CONTENT);
				return true;
			    case 2:
				if(player.getInventory().playerHasItem(NETTLE_TEA_BOWL) || player.getInventory().playerHasItem(NETTLE_TEA_CUP)) {
				    player.getDialogue().sendPlayerChat("Here's some nice tea for you, like you asked.", CONTENT);
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("I'm afraid I don't have any tea like that.", "I'll go fetch some.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				player.getDialogue().sendStatement("You go to hand the tea to the Old Crone...");
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("Ack! That tea isn't in my special cup!", "I only ever drink from my special cup!!", DISTRESSED);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("I see... Can I have this special cup, then?", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendGiveItemNpc("The Old Crone hands you her 'special cup'.", new Item(PORCELAIN_CUP));
				player.getDialogue().endDialogue();
				player.getInventory().addItemOrDrop(new Item(PORCELAIN_CUP));
				player.setQuestStage(24, CRONE_NEEDS_TEA_IN_CUP);
				return true;
			}
		    return false;
		    case CRONE_NEEDS_TEA_IN_CUP:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(!player.getInventory().ownsItem(PORCELAIN_CUP)) {
				    player.getDialogue().sendPlayerChat("I'm afraid I lost your cup.", CONTENT);
				    player.getDialogue().setNextChatId(10);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Did you get me some tea in my special cup?", CONTENT);
				    return true;
				}
			    case 2:
				if(player.getInventory().playerHasItem(PORCELAIN_CUP_NETTLE)) {
				    player.getDialogue().sendPlayerChat("Here's that tea you wanted, in your special cup...", CONTENT);
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("I'm afraid I didn't fill your cup with tea yet.", "I'll go do that.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				player.getDialogue().sendStatement("You go to hand the cup of tea to the Old Crone...");
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("Oh no! It hasn't got any milk in it!", "I only drink tea with milk in it, I'm afraid.", DISTRESSED);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("Oh.", ANGRY_1);
				return true;
			    case 6:
				player.getDialogue().sendPlayerChat("...", ANGRY_1);
				return true;
			    case 7:
				player.getDialogue().sendPlayerChat("Alright. I'll go put some milk in your tea.", ANNOYED);
				player.getDialogue().endDialogue();
				player.setQuestStage(24, CRONE_NEEDS_MILKY_TEA);
				return true;
			    case 10:
				player.getDialogue().sendNpcChat("How foolish of you! I used to only have one", "special cup. But years ago another adventurer just like", "you lost that one, just as you have. So I have a few", "special cups made.", ANGRY_1);
				return true;
			    case 11:
				player.getDialogue().sendGiveItemNpc("The Old Crone hands you another 'special cup'.", new Item(PORCELAIN_CUP));
				player.getDialogue().endDialogue();
				player.getInventory().addItemOrDrop(new Item(PORCELAIN_CUP));
				return true;
			}
		    return false;
		    case CRONE_NEEDS_MILKY_TEA:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("Did you get me some milky tea in my", "special cup there dear?", CONTENT);
				return true;
			    case 2:
				if(player.getInventory().playerHasItem(PORCELAIN_CUP_NETTLE_MILKY)) {
				    player.getDialogue().sendPlayerChat("Here's that milky, delicious, hand-crafted, ill-gotten...", ANNOYED);
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("I'm sorry. I didn't get your precious milk yet.", ANNOYED);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				player.getDialogue().sendGiveItemNpc("You hand the Old Crone her very specific tea.", new Item(PORCELAIN_CUP_NETTLE_MILKY));
				player.getDialogue().setNextChatId(1);
				player.getInventory().removeItem(new Item(PORCELAIN_CUP_NETTLE_MILKY));
				player.setQuestStage(24, CRONES_STORY);
				return true;
			}
		    return false;
		    case CRONES_STORY:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("Ahhhhh... That's much better. Now, let me see... Yes,", "I was once a disciple of Necrovarus.", HAPPY);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("I have come from Velorina, she needs your help.", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("Velorina? That name sounds familiar...", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("Yes. Velorina was once a very good friend!", "It has been many years since we spoke last.", "How is she?", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("She's a ghost, I'm afraid.", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendNpcChat("They are all dead then. Even Velorina. I should have", "done something to stop what was happening, instead", "of running away.", SAD);
				return true;
			    case 7:
				player.getDialogue().sendPlayerChat("She and many others want to pass on into the", "next world, but Necrovarus will not let them. Do you", "know of any way to help them?", CONTENT);
				return true;
			    case 8:
				player.getDialogue().sendNpcChat("There might be one way...", CONTENT);
				return true;
			    case 9:
				player.getDialogue().sendNpcChat("Do you have such a thing as a ghostspeak amulet?", CONTENT);
				return true;
			    case 10:
				if(player.getEquipment().getId(Constants.AMULET) == GHOSTSPEAK_AMULET || player.getInventory().playerHasItem(GHOSTSPEAK_AMULET)) {
				    player.getDialogue().sendPlayerChat("I've got one right on me actually.", CONTENT);
				    player.getDialogue().setNextChatId(12);
				    return true;
				}
				else if(player.getQuestStage(2) > 2) {
				    player.getDialogue().sendPlayerChat("Not on me, but I know where to get one.", CONTENT);
				    player.getDialogue().setNextChatId(12);
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("I'm afraid I don't know what that is.", SAD);
				    return true;
				}
			    case 11:
				player.getDialogue().sendNpcChat("I'm sure you'll figure out where to get one.", "Anyways...", CONTENT);
				player.getDialogue().setNextChatId(13);
				return true;
			    case 12:
				player.getDialogue().sendNpcChat("Well, that's a stroke of luck!", CONTENT);
				return true;
			    case 13:
				player.getDialogue().sendNpcChat("There is an enchantment that I can perform on such", "an amulet that will give it the power of command over", "ghosts. It will only work once, but it will enable you", "to command Necrovarus to let the ghosts pass on.", CONTENT);
				return true;
			    case 14:
				player.getDialogue().sendPlayerChat("What do you need to perform the enchantment?", CONTENT);
				return true;
			    case 15:
				player.getDialogue().sendNpcChat("Necrovarus had a magical robe for which he must", "have no more use. Only these robes hold the power needed", "to perform the enchantment.", CONTENT);
				return true;
			    case 16:
				player.getDialogue().sendNpcChat("All his rituals came from a book written by an ancient", "sorcerer from the East called Haricanto. You will need to", "bring me this strange book as well.", CONTENT);
				return true;
			    case 17:
				player.getDialogue().sendNpcChat("However, I cannot read the strange letters of the eastern", "lands. I will need something to help me translate the book.", CONTENT);
				return true;
			    case 18:
				player.getDialogue().sendPlayerChat("While I'm out collecting these materials, is there", "anything else you might need?", CONTENT);
				return true;
			    case 19:
				player.getDialogue().sendNpcChat("I have lived here on my own for years, but not always.", "When I left Port Phasmatys, I took my son with me.", "He grew up to be a fine boy, always in love", "with the sea.", CONTENT);
				return true;
			    case 20:
				player.getDialogue().sendNpcChat("He was about twelve years old when he ran away", "with some pirates to be a cabin boy. I never", "saw him again.", CONTENT);
				return true;
			    case 21:
				player.getDialogue().sendPlayerChat("Was his name Jenkins per chance?", CONTENT);
				return true;
			    case 22:
				player.getDialogue().sendNpcChat("Err, no. It wasn't... If you see him, please give him this", "model ship. Tell him that his mother still loves him.", CONTENT);
				return true;
			    case 23:
				player.getDialogue().sendPlayerChat("Was this his boat?", CONTENT);
				return true;
			    case 24:
				player.getDialogue().sendNpcChat("Yes, he made it himself. It is a model of the very", "ship in which he sailed away. The paint has peeled", "off and it has lost it's flag, but I could never", "throw it away.", CONTENT);
				return true;
			    case 25:
				player.getDialogue().sendPlayerChat("If I find him, I'll be sure to pass it on.", CONTENT);
				return true;
			    case 26:
				if(player.getInventory().canAddItem(new Item(MODEL_SHIP))) {
				    player.getDialogue().sendGiveItemNpc("The Old Crone hands you a model ship.", new Item(MODEL_SHIP));
				    player.getDialogue().endDialogue();
				    player.getInventory().addItem(new Item(MODEL_SHIP));
				    player.setQuestStage(24, ITEMS_FOR_ENCHANTMENT);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Oh, you don't have room for this ship...", SAD);
				    player.getDialogue().endDialogue();
				    return true;
				}
			}
		    return false;
		    case ITEMS_FOR_ENCHANTMENT:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(!player.getInventory().ownsItem(MODEL_SHIP)) {
				    player.getDialogue().sendPlayerChat("I seem to have, erm, misplaced your son's model ship.", DISTRESSED);
				    return true;
				}
				else if(playerHasItemsInInventory(player)) {
				    player.getDialogue().sendPlayerChat("I found all the items for the enchantment.", CONTENT);
				    player.getDialogue().setNextChatId(5);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Go get all the items, hurry!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				player.getDialogue().sendNpcChat("You're lucky I have a keen eye. I found it", "while out on a stroll.", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendGiveItemNpc("The Old Crone hands you a model ship.", new Item(MODEL_SHIP));
				player.getDialogue().endDialogue();
				player.getInventory().addItem(new Item(MODEL_SHIP));
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("Good! Ah, the robes of Necrovarus...", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendNpcChat("The Book of Haricanto! I have no idea", "how you found this!", HAPPY);
				return true;
			    case 7:
				player.getDialogue().sendNpcChat("And the the translation manual - yes, this should", "do the job.", CONTENT);
				return true;
			    case 8:
				player.getDialogue().sendNpcChat("Wonderful, that's everything I need.", "I can perform the enchantment now.", CONTENT);
				return true;
			    case 9:
				if(player.getInventory().playerHasItem(GHOSTSPEAK_AMULET)) {
				    player.getDialogue().sendGiveItemNpc("The Old Crone takes the 3 items and begins to chant...", "The ghostspeak amulet begins to glow green.", new Item(NECROVARUS_ROBES), new Item(ENCHANTED_GHOSTSPEAK));
				    player.getDialogue().setNextChatId(1);
				    player.getInventory().replaceItemWithItem(new Item(GHOSTSPEAK_AMULET), new Item(ENCHANTED_GHOSTSPEAK));
				    player.getInventory().removeItem(new Item(NECROVARUS_ROBES));
				    player.getInventory().removeItem(new Item(BOOK_OF_HARICANTO));
				    player.getInventory().removeItem(new Item(TRANSLATION_MANUAL));
				    player.setQuestStage(24, CAST_SPELL);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Oh, you don't have a ghostspeak amulet for me", "to enchant. Come back with one as soon as possible!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			}
		    return false;
		    case CAST_SPELL:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("So this enchanted amulet can be used once to command", "any ghost to do my bidding?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendNpcChat("That's right, only once.", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("Hmm. Alright, I'll go take care of Necrovarus.", "Thank you for all the help.", HAPPY);
				player.getDialogue().endDialogue();
				return true;
			}
		    return false;
		}
	    return false;
	    case ROBIN:
		switch(player.getQuestStage(24)) {
		    case 0: //start
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(player.getInventory().playerHasItem(995, 50)) {
				    player.getRuneDraw().openGame(50);
				}
				else {
				    player.getDialogue().sendPlayerChat("I don't have that much money to bet.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				player.getDialogue().dontCloseInterface();
				return true;
			}
		    return false;
		}
	    return false;
	}
	return false;
    }

}