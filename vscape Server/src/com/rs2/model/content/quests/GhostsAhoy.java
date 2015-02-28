package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
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
import com.rs2.model.content.skills.Menus;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.content.skills.prayer.Ectofuntus;
import static com.rs2.model.content.skills.prayer.Ectofuntus.BUCKET_OF_SLIME;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

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
    public static final int EMPTY_CUP = 1980;
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
    public static final int BONE_KEY = 4272;
    public static final int ECTOPLASM_BEDSHEET = 4285;
    public static final int BONES = 526;
    public static final int POT = 1931;
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
    public static final int NECROVARUS_DOOR = 5244;
    public static final int NECROVARUS_COFFIN = 5278;
    public static final int NETTLES_1 = 5253;
    public static final int NETTLES_2 = 5254;
    public static final int NETTLES_3 = 5255;
    public static final int NETTLES_4 = 5256;
    public static final int NETTLES_5 = 5257;
    public static final int NETTLES_6 = 5258;
    public static final int NETTLES_7 = 1181;
    public static final int PIRATE_CAPTAIN = 5287;
    public static final int JUMPING_ROCKS = 5269;
    public static final int TREASURE_CHEST_1 = 5270;
    public static final int TREASURE_CHEST_2 = 5272;
    public static final int[] TREASURE_CHEST_1_OBJ = {3619, 3545};
    public static final int GANGPLANK_TO_ROCKS = 5285;
    public static final int[] GANGPLANK_TO_ROCKS_OBJ = {3605, 3546};
    public static final int GANGPLANK_TO_SHIP = 5286;
    public static final int[] GANGPLANK_TO_SHIP_OBJ = {3605, 3547};
    
    public static boolean lowWind = false;
    
    public static final Position ECTOFUNTUS_POS = new Position(3659, 3517, 0);
    public static final Position OVER_GANGPLANK = new Position(3605, 3548, 0);
    public static final Position ON_SHIP = new Position(3605, 3545, 1);
    public static final Position BACK_FROM_DRAGONTOOTH = new Position(3702, 3487, 0);
    public static final Position ON_DRAGONTOOTH = new Position(3793, 3560, 0);
    public static final Position TREASURE_LOCATION = new Position(3803, 3530, 0);
    public static final Position LOBSTER_SPAWN = new Position(3616, 3543, 0);
    
    public static final int MAP_INTERFACE = 12266;
    public static final int BLACK_INTERFACE_TEXT = 12283, STRING_ON_BLACK = 12285;
    
    public static final int GIANT_LOBSTER = 1693;
    public static final int VELORINA = 1683;
    public static final int NECROVARUS = 1684;
    public static final int GRAVINGAS = 1685;
    public static final int ROBIN = 1694;
    public static final int OLD_CRONE = 1695;
    public static final int OLD_MAN = 1696;
    public static final int GHOST_GUARD = 1706;
    public static final int GHOST_CAPTAIN = 1704;
    public static final int GHOST_CAPTAIN_2 = 1705;
    public static final int AK_HARANU = 1688;
    public static final int INNKEEPER = 1700;
    public static final int DISCIPLE = 1686;
    public static final int PATCHY = 4359;
    
    public static final Position UP_TO_BONEGRINDER = new Position(3666, 3522, 1);
    public static final Position DOWN_FROM_BONEGRINDER = new Position(3666, 3517, 0);
    public static final Position DOWN_AT_SLIME = new Position(3683, 9888, 0);
    public static final Position UP_FROM_SLIME = new Position(3687, 9888, 1);
    public static final Position DOWN_FROM_FIRST_LEVEL = new Position(3675, 9888, 1);
    public static final Position UP_FROM_FIRST_LEVEL = new Position(3671, 9888, 2);
    public static final Position DOWN_FROM_SECOND_LEVEL = new Position(3688, 9888, 2);
    public static final Position UP_FROM_SECOND_LEVEL = new Position(3692, 9888, 3);
    public static final Position UP_FROM_FIRST_LEVEL_SHORTCUT = new Position(3670, 9888, 3);
    public static final Position DOWN_FROM_TRAPDOOR = new Position(3669, 9888, 3);
    public static final Position UP_FROM_LADDER = new Position(3654, 3519, 0);

    public static final int X = 0, Y = 1;
    public static final int[] ECTOFUNTUS_OBJ = {3658, 3518};
    public static final int[] TRAPDOOR_OBJ = {3653, 3519};
    public static final int[] STAIRS_UP_OBJ = {3666, 3518};
    public static final int[] STAIRS_DOWN_OBJ = {3666, 3520};
    public static final int[] STAIRS_UP_TO_SECOND_OBJ = {3689, 9887};
    public static final int[] STAIRS_UP_TO_FIRST_OBJ = {3672, 9887};
    public static final int[] STAIRS_UP_FROM_SLIME_OBJ = {3684, 9887};
    public static final int[] STAIRS_DOWN_TO_SECOND_OBJ = {3689, 9887};
    public static final int[] STAIRS_DOWN_TO_FIRST_OBJ = {3672, 9887};
    public static final int[] STAIRS_DOWN_TO_SLIME_OBJ = {3684, 9887};
    public static final int[] LADDER_UP_OBJ = {3668, 9888};
    public static final int[] BONEGRINDER_OBJ = {3659, 3525};
    public static final int[] BONEGRINDER_BIN_OBJ = {3658, 3525};

    public static final int ECTOFUNTUS = 5282;
    public static final int TRAPDOOR = 5267;
    public static final int STAIRS_UP = 5280;
    public static final int STAIRS_DOWN = 5281;
    public static final int STAIRS_UP_SLIME = 5262;
    public static final int STAIRS_DOWN_SLIME = 5263;
    public static final int LADDER_UP = 5264;
    public static final int JUMP_DOWN = 9308;
    public static final int LOADER = 11162;
    public static final int BONEGRINDER = 11163;
    public static final int BONEGRINDER_BIN = 11164;
    public static final int SLIME = 5461;
    public static final int SLIME_2 = 5462;
    
    public static final String[] GHOST_VILLAGER_SAYINGS = {"What a beautiful day for spooking.", "Well... atleast I'm not a skeleton...", "We all have pain, we all have sorrow...", "I used to be a trumpet player before I died, you know.", "OoooOOOOOooooOOOoo!", "Go away mortal, I'm very busy."};
    
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
        player.getActionSender().sendString(getQuestName(), 8144);
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
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8149);
	    
	    player.getActionSender().sendString("Necrovarus was not interested in negotiating. I should", 8151);
	    player.getActionSender().sendString("probably return to Velorina and see what else can be done.", 8152);
	}
	else if (questStage == TO_CRONE) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8149);
	    
	    player.getActionSender().sendString("Velorina told me of an old disciple who escaped the fate of", 8151);
	    player.getActionSender().sendString("Phasmatys. She said she can be found west of here, near", 8152);
	    player.getActionSender().sendString("the sea and in between two large castles.", 8153);
	}
	else if (questStage == CRONE_NEEDS_TEA) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8149);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8151);
	    
	    player.getActionSender().sendString("The Old Crone seems to not remember anything without some", 8153);
	    player.getActionSender().sendString("nettle tea. I should fetch that if I want any answers.", 8154);
	}
	else if (questStage == CRONE_NEEDS_TEA_IN_CUP) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8149);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8151);
	    
	    player.getActionSender().sendString("Apparently any old container wasn't good enough for the", 8153);
	    player.getActionSender().sendString("Crone, I should put some tea in her 'special' porcelain cup.", 8154);
	}
	else if (questStage == CRONE_NEEDS_MILKY_TEA) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8149);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8151);
	    
	    player.getActionSender().sendString("Even the special cup tea wasn't good enough. The Old Crone", 8153);
	    player.getActionSender().sendString("only drinks her nettle tea with milk...", 8154);
	}
	else if (questStage == CRONES_STORY) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8149);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8151);
	    
	    player.getActionSender().sendString("The Old Crone warmed up after some milky nettle tea.", 8153);
	    player.getActionSender().sendString("I then proceeded to walk away while she was talking.", 8154);
	}
	else if (questStage == ITEMS_FOR_ENCHANTMENT || questStage == ITEMS_FOR_ENCHANTMENT_2) {
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
	else if (questStage == CAST_SPELL) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8149);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8151);
	    player.getActionSender().sendString("@str@" + "The Old Crone warmed up after some milky nettle tea.", 8153);
	    
	    player.getActionSender().sendString("The Old Crone enchanted my Ghostspeak Amulet with", 8155);
	    player.getActionSender().sendString("the items I painstakingly gathered. All I need to", 8156);
	    player.getActionSender().sendString("do now is command Necrovarus to do my bidding.", 8157);
	}
	else if (questStage == TELL_VELORINA) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8149);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8151);
	    player.getActionSender().sendString("@str@" + "The Old Crone warmed up after some milky nettle tea.", 8153);
	    player.getActionSender().sendString("@str@" + "She then enchanted my Ghostspeak Amulet after I", 8155);
	    player.getActionSender().sendString("@str@" + "painstakingly gathered the items for the enchantment", 8156);
	    
	    player.getActionSender().sendString("The spell worked! Necrovarus was forced to agree to", 8158);
	    player.getActionSender().sendString("let the denizens of Port Phastmatys to pass on!", 8159);
	    player.getActionSender().sendString("I should go tell Velorina the good news.", 8160);
	}
	else if (questStage == QUEST_COMPLETE) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    player.getActionSender().sendString("@str@" + "Velorina told me to try and plead with Necrovarus.", 8149);
	    player.getActionSender().sendString("@str@" + "Velorina told me of an old disciple who escaped Phasmatys.", 8151);
	    player.getActionSender().sendString("@str@" + "The Old Crone warmed up after some milky nettle tea.", 8153);
	    player.getActionSender().sendString("@str@" + "She then enchanted my Ghostspeak Amulet after I.", 8155);
	    player.getActionSender().sendString("@str@" + "painstakingly gathered the items for the enchantment", 8156);
	    
	    player.getActionSender().sendString("@str@" + "The spell worked! Necrovarus was forced to agree to", 8158);
	    player.getActionSender().sendString("@str@" + "let the denizens of Port Phastmatys to pass on!", 8159);
	    
	    player.getActionSender().sendString("@red@" + "You have completed this quest!", 8161);
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

    public void showInterface(Player player){
    	player.getActionSender().sendString(getQuestName(), 8144);
        player.getActionSender().sendString(getQuestName(), 8144);
        player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
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
    
    public boolean itemHandling(final Player player, int itemId) {
	switch(itemId) {
	    case PETITION:
		player.getPetition().count();
		return true;
	    case TREASURE_MAP:
		player.getActionSender().sendInterface(MAP_INTERFACE);
		return true;
	    case MODEL_SHIP_SILK:
		player.getDialogue().sendStatement("The top half of the flag is " + player.getQuestVars().getTopHalfFlag() + ".", "The bottom half of the flag is " + player.getQuestVars().getBottomHalfFlag() + ".", "The skull emblem of the flag is " + player.getQuestVars().getSkullFlag() + ".");
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
		    return true;
		}
	}
	return false;
    }
     
    public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot)  {
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
	    case MAP_SCRAP:
		switch(secondItem) {
		    case MAP_SCRAP_2:
		    case MAP_SCRAP_3:
			if(player.getInventory().playerHasItem(MAP_SCRAP_2) && player.getInventory().playerHasItem(MAP_SCRAP_3)) {
			    player.getActionSender().sendMessage("You fit all the map pieces together.");
			    player.getInventory().replaceItemWithItem(new Item(MAP_SCRAP), new Item(TREASURE_MAP));
			    player.getInventory().removeItem(new Item(MAP_SCRAP_2));
			    player.getInventory().removeItem(new Item(MAP_SCRAP_3));
			    return true;
			}
		}
	    return false;
	    case MAP_SCRAP_2:
		switch(secondItem) {
		    case MAP_SCRAP:
		    case MAP_SCRAP_3:
			if(player.getInventory().playerHasItem(MAP_SCRAP) && player.getInventory().playerHasItem(MAP_SCRAP_3)) {
			    player.getActionSender().sendMessage("You fit all the map pieces together.");
			    player.getInventory().replaceItemWithItem(new Item(MAP_SCRAP), new Item(TREASURE_MAP));
			    player.getInventory().removeItem(new Item(MAP_SCRAP_2));
			    player.getInventory().removeItem(new Item(MAP_SCRAP_3));
			    return true;
			}
		}
	    return false;
	    case MAP_SCRAP_3:
		switch(secondItem) {
		    case MAP_SCRAP:
		    case MAP_SCRAP_2:
			if(player.getInventory().playerHasItem(MAP_SCRAP) && player.getInventory().playerHasItem(MAP_SCRAP_2)) {
			    player.getActionSender().sendMessage("You fit all the map pieces together.");
			    player.getInventory().replaceItemWithItem(new Item(MAP_SCRAP), new Item(TREASURE_MAP));
			    player.getInventory().removeItem(new Item(MAP_SCRAP_2));
			    player.getInventory().removeItem(new Item(MAP_SCRAP_3));
			    return true;
			}
		}
	    return false;
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
    
    public boolean doItemOnObject(final Player player, final int object, final int item) {
	switch(object) {
	    case ECTOFUNTUS:
		if(item == ECTOPHIAL_EMPTY) {
		    player.getUpdateFlags().sendAnimation(883);
		    player.getInventory().replaceItemWithItem(new Item(ECTOPHIAL_EMPTY), new Item(ECTOPHIAL));
		    player.getActionSender().sendMessage("You refill the ectophial from the Ectofuntus.");
		    return true;
		}
	    case SLIME:
	    case SLIME_2:
		if (item == GhostsAhoy.BEDSHEET) {
		    player.getUpdateFlags().sendAnimation(827);
		    player.getActionSender().sendMessage("You dip the bedsheet into the ectoplasm, and it comes out dripping with green slime.");
		    player.getInventory().replaceItemWithItem(new Item(GhostsAhoy.BEDSHEET), new Item(GhostsAhoy.ECTOPLASM_BEDSHEET));
		    return true;
		}
		if (item == BUCKET) {
		    player.setStatedInterface("Ectoplasm");
		    Menus.display1Item(player, BUCKET_OF_SLIME, "");
		    return true;
		}
		return false;
	    case LOADER:
		final Ectofuntus.BonemealData bone = Ectofuntus.BonemealData.forBoneId(item);
		if (bone == null) {
		    player.getActionSender().sendMessage("Those aren't bones!");
		    return true;
		}
		if (player.getEctofuntus().boneType != null && player.getEctofuntus().boneType.boneId != item) {
		    player.getActionSender().sendMessage("You can only process one type of bone at a time. Empty the bin if need be.");
		    return true;
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			if(player.getEctofuntus().boneType == null) {
			    player.getEctofuntus().boneType = Ectofuntus.BonemealData.forBoneId(item);
			}
			player.getActionSender().sendMessage("You add some " + new Item(bone.boneId).getDefinition().getName() + " to the loader.");
			player.getInventory().removeItem(new Item(bone.boneId));
			player.getEctofuntus().getBonesInLoader().add(bone);
			player.getUpdateFlags().sendAnimation(1649);
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.setStopPacket(false);
		    }
		}, 1);
		return true;
	}
	return false;
    }
    
    public boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch(object) {
	    case 5268: //Ectofuntus ladder
		if(x == 3653 && y == 3519) {
		    player.teleport(new Position(3669, 9888, 3));
		}
		return true;
	    case NECROVARUS_COFFIN:
		if(x == 3659 && y == 3513) {
		    if(!player.getInventory().ownsItem(NECROVARUS_ROBES)) {
			player.getDialogue().sendGiveItemNpc("You take the Robes of Necrovarus from his remains.", new Item(NECROVARUS_ROBES));
			player.getInventory().addItemOrDrop(new Item(NECROVARUS_ROBES));
			return true;
		    }
		    else {
			player.getActionSender().sendMessage("The remains of Necrovarus are bare without his robes.");
			return true;
		    }
		}
	    return false;
	    case NECROVARUS_DOOR:
		if(x == 3656 && y == 3514) {
		    if(player.getInventory().playerHasItem(BONE_KEY)) {
			player.getActionSender().sendMessage("The door creaks open when the key is turned.");
			player.getActionSender().walkTo(player.getPosition().getX() < 3656 ? 1 : -1, 0, true);
			player.getActionSender().walkThroughDoor(object, x, y, 1);
			return true;
		    }
		    else {
			player.getDialogue().setLastNpcTalk(DISCIPLE);
			player.getDialogue().sendNpcChat("Inside that room is a coffin, inside which lie the mortal", "remains of our most glorious master, Necrovarus.", "None may enter!", ANNOYED);
			return true;
		    }
		}
	    return false;
	    case GANGPLANK_TO_ROCKS:
		if(x == GANGPLANK_TO_ROCKS_OBJ[0] && y == GANGPLANK_TO_ROCKS_OBJ[1]) {
		    player.teleport(OVER_GANGPLANK);
		    return true;
		}
	    return false;
	    case TREASURE_CHEST_1:
		if (x == TREASURE_CHEST_1_OBJ[0] && y == TREASURE_CHEST_1_OBJ[1]) {
		    if (player.getInventory().playerHasItem(CHEST_KEY) && !player.getInventory().ownsItem(MAP_SCRAP) && !player.getInventory().ownsItem(TREASURE_MAP)) {
			player.getUpdateFlags().sendAnimation(832);
			player.getDialogue().sendGiveItemNpc("You unlock the chest and find a scrap piece of a map.", new Item(MAP_SCRAP));
			player.getInventory().addItem(new Item(MAP_SCRAP));
			return true;
		    } else if (player.getInventory().playerHasItem(CHEST_KEY) && player.getInventory().ownsItem(MAP_SCRAP) && !player.getInventory().ownsItem(TREASURE_MAP)) {
			player.getDialogue().sendPlayerChat("I already got this piece of the map.", "Perhaps there are other pieces around nearby.", CONTENT);
			return true;
		    } else if (player.getInventory().ownsItem(TREASURE_MAP)) {
			player.getDialogue().sendPlayerChat("I already completed the map. I need to figure", "out where it goes instead of wasting time.", CONTENT);
			return true;
		    } else {
			player.getActionSender().sendMessage("This chest is locked tightly.");
			return true;
		    }
		} else {
		    player.getActionSender().sendMessage("This chest is locked tightly.");
		    return true;
		}
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
		    if(player.getQuestVars().getDesiredTopHalfFlag().equals("black")) {
			player.getDialogue().sendStatement("You see a tattered flag blowing in the wind.", "The top half of the flag is colored " + DYES[index1] + ".");
			player.getQuestVars().setDesiredGhostsAhoyFlag("top", DYES[index1]);
			return true;
		    }
		    else if(!player.getQuestVars().getDesiredTopHalfFlag().equals("black") && player.getQuestVars().getDesiredBottomHalfFlag().equals("black")) {
			do {
			    index2 = Misc.randomMinusOne(DYES.length);
			} while (DYES[index2].equals(player.getQuestVars().getDesiredTopHalfFlag()));
			player.getDialogue().sendStatement("You see a tattered flag blowing in the wind.", "The bottom half of the flag is colored " + DYES[index2] + ".");
			player.getQuestVars().setDesiredGhostsAhoyFlag("bottom", DYES[index2]);
			return true;
		    }
		    else if(!player.getQuestVars().getDesiredTopHalfFlag().equals("black") && !player.getQuestVars().getDesiredBottomHalfFlag().equals("black") && player.getQuestVars().getDesiredSkullFlag().equals("black")) {
			do {
			    index3 = Misc.randomMinusOne(DYES.length);
			} while (DYES[index3].equals(player.getQuestVars().getDesiredBottomHalfFlag()));
			player.getDialogue().sendStatement("You see a tattered flag blowing in the wind.", "The skull emblem on the flag is colored " + DYES[index3] + ".");
			player.getQuestVars().setDesiredGhostsAhoyFlag("skull", DYES[index3]);
			return true;
		    }
		    else if(!player.getQuestVars().getDesiredTopHalfFlag().equals("black") && !player.getQuestVars().getDesiredBottomHalfFlag().equals("black") && !player.getQuestVars().getDesiredSkullFlag().equals("black")){
			player.getDialogue().sendStatement("You see a tattered flag blowing in the wind.",  "The top half of the flag is colored " + player.getQuestVars().getDesiredTopHalfFlag() + ".", "The bottom half of the flag is colored " + player.getQuestVars().getDesiredBottomHalfFlag() + ".","The skull emblem on the flag is colored " + player.getQuestVars().getDesiredSkullFlag() + ".");
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
		    if(player.getQuestStage(24) < TELL_VELORINA) {
			Dialogues.startDialogue(player, 11111);
			return true;
		    }
		    else {
			player.getActionSender().sendMessage("The guards give you a wink and a nod as you pass through the barrier.", true);
			if (player.getPosition().getY() > 3500) {
			    player.getActionSender().walkTo(0, player.getPosition().getY() < 3508 ? 1 : -2, true);
			    return true;
			} else {
			    player.getActionSender().walkTo(player.getPosition().getX() < 3653 ? 2 : -1, 0, true);
			    return true;
			}
		    }
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
	    case STAIRS_UP:
		if (x == STAIRS_UP_OBJ[X] && y == STAIRS_UP_OBJ[Y]) {
		    player.teleport(UP_TO_BONEGRINDER);
		    return true;
		}
	    case STAIRS_DOWN:
		if (x == STAIRS_DOWN_OBJ[X] && y == STAIRS_DOWN_OBJ[Y]) {
		    player.teleport(DOWN_FROM_BONEGRINDER);
		    return true;
		}
	    case ECTOFUNTUS:
		if (x == ECTOFUNTUS_OBJ[X] && y == ECTOFUNTUS_OBJ[Y]) {
		    player.getEctofuntus().worship();
		    return true;
		}
	    case STAIRS_UP_SLIME:
		if (x == STAIRS_UP_FROM_SLIME_OBJ[X]) {
		    player.teleport(UP_FROM_SLIME);
		    return true;
		} else if (x == STAIRS_UP_TO_FIRST_OBJ[X]) {
		    player.teleport(UP_FROM_FIRST_LEVEL);
		    return true;
		} else if (x == STAIRS_UP_TO_SECOND_OBJ[X]) {
		    player.teleport(UP_FROM_SECOND_LEVEL);
		    return true;
		}
	    case STAIRS_DOWN_SLIME:
		if (x == STAIRS_DOWN_TO_SLIME_OBJ[X]) {
		    player.teleport(DOWN_AT_SLIME);
		    return true;
		} else if (x == STAIRS_DOWN_TO_FIRST_OBJ[X]) {
		    player.teleport(DOWN_FROM_FIRST_LEVEL);
		    return true;
		} else if (x == STAIRS_DOWN_TO_SECOND_OBJ[X]) {
		    player.teleport(DOWN_FROM_SECOND_LEVEL);
		    return true;
		}
	    case LADDER_UP:
		if (x == LADDER_UP_OBJ[X]) {
		    Ladders.climbLadder(player, UP_FROM_LADDER);
		    return true;
		}
	    case BONEGRINDER:
		if (player.getEctofuntus().getBonesInLoader().isEmpty()) {
		    player.getActionSender().sendMessage("You haven't added any bones to the loader!");
		    return true;
		} else {
		    player.getUpdateFlags().sendAnimation(1648);
		    player.getActionSender().sendMessage("You grind the bones.");
		    for (Ectofuntus.BonemealData bone : player.getEctofuntus().getBonesInLoader()) {
			player.getEctofuntus().getBonemealInBin().add(bone);
		    }
		    player.getEctofuntus().getBonesInLoader().clear();
		    return true;
		}
	    case BONEGRINDER_BIN:
		player.getUpdateFlags().setFace(new Position(x, y + 1, 1));
		if (!player.getInventory().playerHasItem(POT)) {
		    player.getActionSender().sendMessage("You don't have any pots to collect bonemeal with!");
		    return true;
		}
		if (player.getEctofuntus().getBonemealInBin().isEmpty()) {
		    player.getActionSender().sendMessage("The bin is empty.");
		    return true;
		} else if (!player.getEctofuntus().getBonemealInBin().isEmpty()) {
		    player.setStopPacket(true);
		    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int index = player.getEctofuntus().getBonemealInBin().size() - 1;
			@Override
			public void execute(CycleEventContainer b) {
			    player.getUpdateFlags().reset();
			    if (index < 0) {
				b.stop();
			    } else {
				if (!player.getInventory().playerHasItem(POT)) {
				    player.getActionSender().sendMessage("You don't have enough pots to collect the rest of the bonemeal!");
				    b.stop();
				} else {
				    player.getInventory().replaceItemWithItem(new Item(POT), new Item(player.getEctofuntus().getBonemealInBin().get(index).bonemealId));
				    player.getUpdateFlags().sendAnimation(1650);
				    index--;
				}
			    }
			}

			@Override
			public void stop() {
			    player.setStopPacket(false);
			    if (index < 0) {
				player.getEctofuntus().getBonemealInBin().clear();
				player.getEctofuntus().boneType = null;
				player.getActionSender().sendMessage("You finish collecting the bonemeal from the bin.");
			    } else {
				for (int i = player.getEctofuntus().getBonemealInBin().size() - 1; i > index; i--) {
				    player.getEctofuntus().getBonemealInBin().remove(i);
				}
			    }
			}
		    }, 2);
		    return true;
		}
	    return false;
	}
	return false;
    }
	
    public static boolean doObjectSecondClick(final Player player, int object, int x, int y) {
	switch (object) {
	    case BONEGRINDER:
		if (x == BONEGRINDER_OBJ[X] && y == BONEGRINDER_OBJ[Y]) {
		    player.getActionSender().sendMessage("" + player.getEctofuntus().getBonesAdded());
		    return true;
		}
	}
	return false;
    }
    
    public static boolean doMiscObjectClicking(final Player player, int object, int x, int y) {
	switch (object) {
	    case JUMPING_ROCKS: //first rock
		if(player.transformNpc == 1707 || player.transformNpc == 1708) {
		    player.getDialogue().sendPlayerChat("I should probably take this off in order", "to jump these rocks carefully.", CONTENT);
		    return true;
		}
		if(player.getSkill().getLevel()[Skill.AGILITY] < 25) {
		    player.getDialogue().sendStatement("You need 25 agility to cross this obstacle.");
		    return true;
		}
		int playerX = player.getPosition().getX();
		int playerY = player.getPosition().getY();
		if (x == 3602 && y == 3550) {
		    if (playerX == 3604 && playerY == 3550) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3604 && y == 3550) {
		    if (playerX == 3602 && playerY == 3550) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3597 && y == 3552) {
		    if (playerX == 3599 && playerY == 3552) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3599 && y == 3552) {
		    if (playerX == 3597 && playerY == 3552) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3595 && y == 3556) {
		    if (playerX == 3595 && playerY == 3554) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3595 && y == 3554) {
		    if (playerX == 3595 && playerY == 3556) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3597 && y == 3561) {
		    if (playerX == 3597 && playerY == 3559) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3597 && y == 3559) {
		    if (playerX == 3597 && playerY == 3561) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3601 && y == 3564) {
		    if (playerX == 3599 && playerY == 3564) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3599 && y == 3564) {
		    if (playerX == 3601 && playerY == 3564) {
			Agility.jumpRock(player, x, y, 769, 2, 0, 0);
			return true;
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		}
	    return true;
	    case TREASURE_CHEST_2:
		if (x == 3606 && y == 3564) {
		    if (Misc.goodDistance(player.getPosition().clone(), new Position(x, y), 2)) {
			if (!player.getInventory().ownsItem(MAP_SCRAP_3) && !player.getInventory().ownsItem(TREASURE_MAP)) {
			    player.getUpdateFlags().sendAnimation(832);
			    player.getDialogue().sendGiveItemNpc("You find a piece of a map.", new Item(MAP_SCRAP_3));
			    player.getInventory().addItem(new Item(MAP_SCRAP_3));
			    return true;
			} else if(player.getInventory().ownsItem(TREASURE_MAP)) {
			    player.getDialogue().sendPlayerChat("I already completed the map. I need to figure", "out where it goes instead of wasting time.", CONTENT);
			    return true;
			} else {
			    player.getDialogue().sendPlayerChat("I already got this piece of the map.", "Perhaps there are other pieces around nearby.", CONTENT);
			    return true;
			}
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else if (x == 3618 && y == 3542) {
		    if (Misc.goodDistance(player.getPosition().clone(), new Position(x, y), 2)) {
			if (!player.getQuestVars().lobsterSpawnedAndDead() && player.getSpawnedNpc() == null) {
			    player.getActionSender().sendMessage("You are attacked by a giant lobster!");
			    NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, new Npc(GIANT_LOBSTER), LOBSTER_SPAWN, true, null);
			    return true;
			} else if(player.getQuestVars().lobsterSpawnedAndDead()) {
			    if (!player.getInventory().ownsItem(MAP_SCRAP_2) && !player.getInventory().ownsItem(TREASURE_MAP)) {
				player.getUpdateFlags().sendAnimation(832);
				player.getDialogue().sendGiveItemNpc("You find a piece of a map.", new Item(MAP_SCRAP_2));
				player.getInventory().addItem(new Item(MAP_SCRAP_2));
				return true;
			    } else if(player.getInventory().ownsItem(TREASURE_MAP)) {
				player.getDialogue().sendPlayerChat("I already completed the map. I need to figure", "out where it goes instead of wasting time.", CONTENT);
				return true;
			    } else {
				player.getDialogue().sendPlayerChat("I already got this piece of the map.", "Perhaps there are other pieces around nearby.", CONTENT);
				return true;
			    }
			} else {
			    player.getActionSender().sendMessage("You are under attack by a giant lobster! Now is not the time to click this!");
			    return true;
			}
		    } else {
			player.walkTo(x, y, false);
			return true;
		    }
		} else {
		    player.getActionSender().sendMessage("This chest is locked firmly.");
		    return true;
		}
	    case GANGPLANK_TO_SHIP:
		if (x == GANGPLANK_TO_SHIP_OBJ[0] && y == GANGPLANK_TO_SHIP_OBJ[1]) {
		    if (player.getPosition().getX() == x && player.getPosition().getY() == y + 1) {
			player.teleport(ON_SHIP);
			return true;
		    } else {
			    player.walkTo(x, y, false);
			return true;
		    }
		}
	    return false;
	}
	return false;
    }
    
    public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) {
		case GhostsAhoyPetition.GHOST_VILLAGER:
			if(player.getQuestStage(this.getQuestID()) > ITEMS_FOR_ENCHANTMENT_2) {
				player.getDialogue().sendNpcChat(GHOST_VILLAGER_SAYINGS[Misc.randomMinusOne(GHOST_VILLAGER_SAYINGS.length)], CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
	    case 1686: //ghost disciple
		switch (player.getDialogue().getChatId()) {
		    case 1:
			if (player.getEctoWorshipCount() > 0) {
			    player.getDialogue().sendPlayerChat("I've worshipped the Ectofuntus, I'd like", "tokens in exchange for the power it has received.", CONTENT);
			    return true;
			} else {
			    player.getDialogue().sendPlayerChat("What is this strange fountain?", CONTENT);
			    player.getDialogue().setNextChatId(5);
			    return true;
			}
		    case 2:
			player.getDialogue().sendNpcChat("Yes, good work adventurer. The Ectofuntus is mighty!", "Here are some Ectotokens in reward.", CONTENT);
			return true;
		    case 3:
			player.getDialogue().sendGiveItemNpc("The disciple hands you some Ectotokens.", new Item(4278));
			player.getDialogue().endDialogue();
			player.getInventory().addItemOrDrop(new Item(ECTOTOKEN, player.getEctoWorshipCount() > 12 ? 60 : player.getEctoWorshipCount() * 5));
			player.setEctoWorshipCount(0);
			return true;
		    case 5:
			player.getDialogue().sendNpcChat("This is the Ectofuntus, the most marvellous", "creation of Necrovarus, our glorious leader.", HAPPY);
			return true;
		    case 6:
			player.getDialogue().sendOption("What is the Ectofuntus for?", "Where do I get ectoplasm from?", "How do I grind bones?", "How do I receive Ecto-tokens?", "Thank you for your time.");
			return true;
		    case 7:
			switch (optionId) {
			    case 1:
				player.getDialogue().sendPlayerChat("What is the Ectofuntus for?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("Where do I get ectoplasm from?", CONTENT);
				player.getDialogue().setNextChatId(17);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("How do I grind bones?", CONTENT);
				player.getDialogue().setNextChatId(20);
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("How do I recieve Ecto-tokens?", CONTENT);
				player.getDialogue().setNextChatId(24);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("Thank you for your time.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 8:
			player.getDialogue().sendNpcChat("It provides the power to keep us ghosts", "from passing over into the next plane of", "existence.", CONTENT);
			return true;
		    case 9:
			player.getDialogue().sendPlayerChat("And how does it work?", CONTENT);
			return true;
		    case 10:
			player.getDialogue().sendNpcChat("You have to pour a bucket of ectoplasm and", "a pot of ground bones into the Ectofuntus,", " and then worship at it. A unit of unholy", "power will then be created.", CONTENT);
			return true;
		    case 11:
			player.getDialogue().sendPlayerChat("Can you do it yourself?", CONTENT);
			return true;
		    case 12:
			player.getDialogue().sendNpcChat("No, we must rely upon the living, as the", "worship of the undead no longer holds any", "inherent power.", CONTENT);
			return true;
		    case 13:
			player.getDialogue().sendPlayerChat("Why would people waste their time helping you out?", CONTENT);
			return true;
		    case 14:
			player.getDialogue().sendNpcChat("For every unit of power produced, we will", "give you five Ecto-tokens. These tokens can be used", "in Port Phasmatys to purchase various services,", "not least of which includes access through the main gates.", CONTENT);
			return true;
		    case 15:
			player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		    case 17:
			player.getDialogue().sendNpcChat("Necrovarus sensed the power bubbling beneath our feet", "and we delved long and deep beneath Port Phasmatys,", "until we found a pool of natural ectoplasm. You may", "find it by using the trapdoor over there.", CONTENT);
			return true;
		    case 18:
			player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		    case 20:
			player.getDialogue().sendNpcChat("There is a bone grinding machine upstairs. Put bones", "of any type into the machine's hopper, and then turn", "the handle when you have loaded all your bones.", CONTENT);
			return true;
		    case 21:
			player.getDialogue().sendNpcChat("Necrovarus, in his mighty power, enchanted the bin to allow", " you to seperate all the bonemeal into pots instantaneously.", CONTENT);
			return true;
		    case 22:
			player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		    case 24:
			player.getDialogue().sendNpcChat("We disciples keep track of how many units", "of power have been produced. Just talk to us", "once you have generated some and we will reimburse you.", CONTENT);
			return true;
		    case 25:
			player.getDialogue().sendPlayerChat("How do I produce units of power?", CONTENT);
			return true;
		    case 26:
			player.getDialogue().sendNpcChat("You have to pour a bucket of ectoplasm and", "a pot of ground bones into the Ectofuntus,", " and then worship at it. A unit of unholy", "power will then be created.", CONTENT);
			return true;
		    case 27:
			player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		}
		return false;
	    case AK_HARANU:
		switch (player.getQuestStage(24)) {
		    case ITEMS_FOR_ENCHANTMENT:
		    case ITEMS_FOR_ENCHANTMENT_2:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(player.getInventory().playerHasItem(SIGNED_OAK_BOW) && !player.getInventory().playerHasItem(TRANSLATION_MANUAL)) {
				    player.getDialogue().sendPlayerChat("I have your signed longbow for you.", CONTENT);
				    player.getDialogue().setNextChatId(10);
				    return true;
				}
				else if(player.getInventory().playerHasItem(TRANSLATION_MANUAL)) {
				    player.getDialogue().sendNpcChat("Thank you again adventurer, my family will", "be quite proud of this bow.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("It's nice to see a human face around here.", CONTENT);
				    return true;
				}
			    case 2:
				player.getDialogue().sendNpcChat("My name is Ak-Haranu. I am a trader.", "I come from far across the sea in the East!", ANGRY_1);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("You come from the lands of the East? Do you", "have anything that can help me translate a book", "that is scribed in your native language?", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("Ak-Haranu may help you, a translation manual I", "do have. Very good for reading the Eastern language.", ANGRY_1);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("How much do you want for it?", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendNpcChat("Ak-Haranu does not want money for this book,", "as money is such a small thing. But, there may be some-", "thing you can do for Ak-Haranu. Robin, master bowman", "refuses to sign anything for Ak-Haranu!", ANGRY_1);
				return true;
			    case 7:
				player.getDialogue().sendNpcChat("If you could get Robin to sign an oak long-bow", "for Ak-Haranu, then Ak-Haranu will trade you book.", CONTENT);
				return true;
			    case 8:
				player.getDialogue().sendPlayerChat("Alright, I'll go get a bow signed for you.", "Don't have an aneurysm while I'm away.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			    case 10:
				player.getDialogue().sendNpcChat("Ah, can it be true? Robin finally", "signed a signature?", CONTENT);
				return true;
			    case 11:
				player.getDialogue().sendPlayerChat("He was, erm, (cough) happy, to oblige.", CONTENT);
				return true;
			    case 12:
				player.getDialogue().sendNpcChat("May honor be bestowed upon you and your", "family!", HAPPY);
				return true;
			    case 13:
				player.getDialogue().sendGiveItemNpc("You hand Ak-Haranu the signed longbow...", "He hands you a worn translation manual in return.", new Item(SIGNED_OAK_BOW), new Item(TRANSLATION_MANUAL));
				player.getDialogue().endDialogue();
				player.getInventory().replaceItemWithItem(new Item(SIGNED_OAK_BOW), new Item(TRANSLATION_MANUAL));
				return true;
			}
		    return false;
		}
	    return false;
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
					player.getQuestVars().dyeGhostsAhoyFlag("top", "red");
					player.setTempInteger(0);
					return true;
				    case BLUE_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag blue.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(BLUE_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("top", "blue");
					player.setTempInteger(0);
					return true;
				    case YELLOW_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag yellow.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(YELLOW_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("top", "yellow");
					player.setTempInteger(0);
					return true;
				    case ORANGE_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag orange.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(ORANGE_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("top", "orange");
					player.setTempInteger(0);
					return true;
				    case PURPLE_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag purple.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(PURPLE_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("top", "purple");
					player.setTempInteger(0);
					return true;
				    case GREEN_DYE:
					player.getDialogue().sendStatement("You carefully dye the top half of the silk flag green.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(GREEN_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("top", "green");
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
					player.getQuestVars().dyeGhostsAhoyFlag("bottom", "red");
					player.setTempInteger(0);
					return true;
				    case BLUE_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag blue.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(BLUE_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("bottom", "blue");
					player.setTempInteger(0);
					return true;
				    case YELLOW_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag yellow.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(YELLOW_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("bottom", "yellow");
					player.setTempInteger(0);
					return true;
				    case ORANGE_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag orange.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(ORANGE_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("bottom", "orange");
					player.setTempInteger(0);
					return true;
				    case PURPLE_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag purple.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(PURPLE_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("bottom", "purple");
					player.setTempInteger(0);
					return true;
				    case GREEN_DYE:
					player.getDialogue().sendStatement("You carefully dye the bottom half of the silk flag green.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(GREEN_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("bottom", "green");
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
					player.getQuestVars().dyeGhostsAhoyFlag("skull", "red");
					player.setTempInteger(0);
					return true;
				    case BLUE_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag blue.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(BLUE_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("skull", "blue");
					player.setTempInteger(0);
					return true;
				    case YELLOW_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag yellow.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(YELLOW_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("skull", "yellow");
					player.setTempInteger(0);
					return true;
				    case ORANGE_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag orange.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(ORANGE_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("skull", "orange");
					player.setTempInteger(0);
					return true;
				    case PURPLE_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag purple.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(PURPLE_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("skull", "purple");
					player.setTempInteger(0);
					return true;
				    case GREEN_DYE:
					player.getDialogue().sendStatement("You carefully dye the skull of the silk flag green.");
					player.getDialogue().endDialogue();
					player.getInventory().removeItem(new Item(GREEN_DYE));
					player.getQuestVars().dyeGhostsAhoyFlag("skull", "green");
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
	    case GRAVINGAS:
		switch(player.getQuestStage(24)) {
		    case ITEMS_FOR_ENCHANTMENT:
		    case ITEMS_FOR_ENCHANTMENT_2:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(player.transformNpc == 1708) {
				    if(player.getInventory().playerHasItem(PETITION)) {
					player.getDialogue().sendNpcChat("Go get the 10 signatures and show it to Necrovarus!", CONTENT);
					player.getDialogue().endDialogue();
					return true;
				    }
				    else {
					player.getDialogue().sendNpcChat("Fellow ghost! Will you join with me and protest", "against the evil desires of Necrovarus and his disciples?", CONTENT);
					player.getDialogue().setNextChatId(4);
					return true;
				    }
				}
				else if(player.transformNpc == 1707) {
				    player.getDialogue().sendNpcChat("What kind of ghost are you? A potato ghost? Hah!", "You surely can't help me.", CONTENT);
				    player.getDialogue().setNextChatId(3);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Shoo, mortal. I need the coalition of ghosts,", "not the aid of the smelly living." , ANNOYED);
				    return true;
				}
			    case 2:
				player.getDialogue().sendStatement("You'll need to convince Gravingas you are a ghost to help him.", "Perhaps disguising as a ghost will do the trick.");
				player.getDialogue().endDialogue();
				return true;
			    case 3:
				player.getDialogue().sendStatement("Gravingas seemed to believe you were a ghost, but not", "one from Port Phastmatys.");
				player.getDialogue().endDialogue();
				return true;
			    case 4:
				player.getDialogue().sendPlayerChat("OOooOOoh, Yessss, after hearing Velorina's story", "I willlll be hAaAaaAAppy to help out!", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("I'm not sure why you're speaking like that...", "But any voice helps. Take this petition form, try to get", "10 signatures from the townsfolk. Then you can present", "the petition to Necrovarus!", CONTENT);
				return true;
			    case 6:
				if(player.getInventory().canAddItem(new Item(PETITION))) {
				    player.getDialogue().sendGiveItemNpc("Gravingas hands you a petition form.", new Item(PETITION));
				    player.getDialogue().endDialogue();
				    player.getInventory().addItem(new Item(PETITION));
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Oh, you don't have room in your inventory.", SAD);
				    player.getDialogue().endDialogue();
				    return true;
				}
			}
		    return false;
		}
	    return false;
	    case NECROVARUS:
		switch(player.getQuestStage(24)) {
		    case QUEST_STARTED: //talk to meeeee
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
		    case ITEMS_FOR_ENCHANTMENT:
		    case ITEMS_FOR_ENCHANTMENT_2:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(player.getPetition().signatures() == 10) {
				    player.getDialogue().sendPlayerChat("Necrovarus, I am presenting you with a petition", "that has been signed by 10 citizens of Port Phasmatys.", CONTENT);
				    player.getDialogue().setNextChatId(4);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("FOOL! How dare you speak to me again?!", ANGRY_2);
				    return true;
				}
			    case 2:
				player.getDialogue().sendStatement("In his rage you see a key float from Necrovarus' robes.", "Perhaps if you made him really angry he'll lose control", "of the key.");
				player.getDialogue().endDialogue();
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("A petition you say? Continue, mortal.", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendPlayerChat("It says that the citizens of Port Phasmatys should have", "the right to choose whether they pass over into the", "next world or not, and not have this decided", " by the powers that be on their behalf.", CONTENT);
				return true;
			    case 6:
				player.getDialogue().sendNpcChat("I see.", CONTENT);
				return true;
			    case 7:
				player.getDialogue().sendPlayerChat("So you will let them pass over if they wish?", CONTENT);
				return true;
			    case 8:
				player.getDialogue().sendNpcChat("Oh yes.", CONTENT);
				return true;
			    case 9:
				player.getDialogue().sendPlayerChat("Really?", CONTENT);
				return true;
			    case 10:
				player.getDialogue().sendNpcChat("NO!!!! Get out of my sight before I burn", "every ounce of FLESH from your BONES!!!!!", ANGRY_2);
				return true;
			    case 11:
				player.getDialogue().sendStatement("The petition turns to ash in your hands.", "You see a key drop out from Necrovarus' robes in his rage.");
				player.getDialogue().endDialogue();
				player.getPetition().clearSignatures();
				player.getInventory().removeItem(new Item(PETITION));
				GroundItemManager.getManager().dropItem(new GroundItem(new Item(BONE_KEY), player, new Position(player.getClickX(), player.getClickY(), player.getPosition().getZ())));
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
				    player.setAppearanceUpdateRequired(true);
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
		    case ITEMS_FOR_ENCHANTMENT:
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
				if(player.getQuestVars().getTopHalfFlag().equals(player.getQuestVars().getDesiredTopHalfFlag()) && player.getQuestVars().getBottomHalfFlag().equals(player.getQuestVars().getDesiredBottomHalfFlag()) && player.getQuestVars().getSkullFlag().equals(player.getQuestVars().getDesiredSkullFlag()) ) {
				    player.getDialogue().sendNpcChat("My word - so it is!!! I never thought I would", "see it again!! Where did you get it from?", HAPPY);
				    return true;
				}
				else if(!player.getQuestVars().getTopHalfFlag().equals(player.getQuestVars().getDesiredTopHalfFlag())) {
				    player.getDialogue().sendNpcChat("Hmmm, it looks similar, but the top of the", "flag is the wrong color, that surely isn't my", "model ship.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getQuestVars().getBottomHalfFlag().equals(player.getQuestVars().getDesiredBottomHalfFlag())) {
				    player.getDialogue().sendNpcChat("Hmmm, it looks similar, but the bottom of the", "flag is the wrong color, that surely isn't my", "model ship.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getQuestVars().getSkullFlag().equals(player.getQuestVars().getDesiredSkullFlag())) {
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
				if(player.getQuestStage(24) == ITEMS_FOR_ENCHANTMENT) {
				    player.setQuestStage(24, ITEMS_FOR_ENCHANTMENT_2);
				}
				return true;
			}
		    return false;
		case ITEMS_FOR_ENCHANTMENT_2:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				
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
		    case QUEST_COMPLETE:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendNpcChat("Thank you so much again adventurer. We are,", "in your debt.", HAPPY);
				return true;
			    case 2:
				if(!player.getInventory().ownsItem(ECTOPHIAL)) {
				    player.getDialogue().sendPlayerChat("I seem to have lost my Ectophial...", SAD);
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("Only happy to help.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 3:
				player.getDialogue().sendNpcChat("And I've found another one, your lucky day.", CONTENT);
				return true;
			    case 4:
				player.getDialogue().sendGiveItemNpc("Velorina hands you a vial of bright green ectoplasm.", new Item(ECTOPHIAL));
				player.getDialogue().endDialogue();
				player.getInventory().addItem(new Item(ECTOPHIAL));
				return true;
			}
		    return false;
		}
	    return false;
	    case GHOST_CAPTAIN_2:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendPlayerChat("I'd like to return to Port Phasmatys now.", CONTENT);
			return true;
		    case 2:
			player.getDialogue().sendNpcChat("Sure thing.", CONTENT);
			return true;
		    case 3:
			final Player toTele = player;
			player.setStopPacket(true);
			player.getActionSender().sendInterface(BLACK_INTERFACE_TEXT);
			player.getActionSender().sendString("After a long boat trip...", STRING_ON_BLACK);
			player.getDialogue().dontCloseInterface();
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				b.stop();
			    }

			    @Override
			    public void stop() {
				toTele.teleport(BACK_FROM_DRAGONTOOTH);
				toTele.getActionSender().sendInterface(BLACK_INTERFACE_TEXT);
				toTele.getActionSender().sendString("...you arrive back at Port Phasmatys.", STRING_ON_BLACK);
				toTele.setStopPacket(false);
			    }
			}, 10);
			return true;
		}
	    return false;
	    case GHOST_CAPTAIN:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendPlayerChat("Where do you sail to, in such a small boat?", CONTENT);
			return true;
		    case 2:
			player.getDialogue().sendNpcChat("I can take you to Dragontooth island, which", "is a most pleasant island in the sea between", "Morytania and the lands of the east.", CONTENT);
			return true;
		    case 3:
			if(player.getInventory().playerHasItem(TREASURE_MAP)) {
			    player.getDialogue().sendPlayerChat("Does the island look anything like this,", "by chance?", CONTENT);
			    return true;
			}
			else {
			    player.getDialogue().sendPlayerChat("Hmm. Sounds lovely, but I don't have the", "time to vacation right now.", CONTENT);
			    player.getDialogue().endDialogue();
			    return true;
			}
		    case 4:
			player.getDialogue().sendGiveItemNpc("You show the captain the treasure map.", new Item(TREASURE_MAP));
			return true;
		    case 5:
			player.getDialogue().sendNpcChat("The island looks exactly like that... strange.", "Where did you find that?", CONTENT);
			return true;
		    case 6:
			player.getDialogue().sendPlayerChat("Nevermind about that, how much does the", "roundtrip cost?", CONTENT);
			return true;
		    case 7:
			player.getDialogue().sendNpcChat("It'll cost you 25 Ecto-tokens.", CONTENT);
			return true;
		    case 8:
			player.getDialogue().sendOption("Okay, here you go. (25 Ecto-tokens)", "Nevermind.");
			return true;
		    case 9:
			switch(optionId) {
			    case 1:
				if(player.getInventory().playerHasItem(ECTOTOKEN, 25)) {
				    final Player toTele = player;
				    player.setStopPacket(true);
				    player.getInventory().removeItem(new Item(ECTOTOKEN, 25));
				    player.getActionSender().sendInterface(BLACK_INTERFACE_TEXT);
				    player.getActionSender().sendString("After a long boat trip...", STRING_ON_BLACK);
				    player.getDialogue().dontCloseInterface();
				    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
					    b.stop();
					}

					@Override
					public void stop() {
					    toTele.teleport(ON_DRAGONTOOTH);
					    toTele.getActionSender().sendInterface(BLACK_INTERFACE_TEXT);
					    toTele.getActionSender().sendString("...you arrive on Dragontooth Island.", STRING_ON_BLACK);
					    toTele.setStopPacket(false);
					}
				    }, 10);
				    return true;
				}
				else {
				    player.getDialogue().sendPlayerChat("Oh, I got too eager. I don't have that", "many Ecto-tokens.", SAD);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				player.getDialogue().sendPlayerChat("Nevermind, I don't want to go yet.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		}
	    return false;
	    case PATCHY:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendNpcChat("'Ello there adventurer!", HAPPY);
			return true;
		    case 2:
			player.getDialogue().sendPlayerChat("Hello.", CONTENT);
			return true;
		    case 3:
			player.getDialogue().sendNpcChat("What are ye looking for today?", CONTENT);
			return true;
		    case 4:
			player.getDialogue().sendPlayerChat("Nothing, I was just admiring your outfit.", "Where did you get it?", CONTENT);
			return true;
		    case 5:
			player.getDialogue().sendNpcChat("Why, I made it me-self!", HAPPY);
			return true;
		    case 6:
			player.getDialogue().sendPlayerChat("You're awfully talented with a needle then...", "What else can you do?", CONTENT);
			return true;
		    case 7:
			player.getDialogue().sendNpcChat("Well, I can sew you some pirate garb together!", CONTENT);
			return true;
		    case 8:
			player.getDialogue().sendPlayerChat("Eh. The pirate outfit can be found anywhere.", "What else can you offer me?", "I've got gold I'm willing to spend.", CONTENT);
			return true;
		    case 9:
			player.getDialogue().sendStatement("Patchy's eyes light up at the sound of the word gold.");
			return true;
		    case 10:
			player.getDialogue().sendNpcChat("Gold ye say? Lean in close laddy, for I do", "have something to offer...", CONTENT);
			return true;
		    case 11:
			player.getDialogue().sendNpcChat("Eyepatches! Ha! It's my finest work!", LAUGHING);
			return true;
		    case 12:
			player.getDialogue().sendPlayerChat("Erm, eyepatches aren't special.", CONTENT);
			return true;
		    case 13:
			player.getDialogue().sendNpcChat("Ah yes, by themselves laddy, that they aren't!", "I can sew an eyepatch onto ye pirate garb!", HAPPY);
			return true;
		    case 14:
			player.getDialogue().sendPlayerChat("Really? What can you attach an eyepatch to?", CONTENT);
			return true;
		    case 15:
			player.getDialogue().sendNpcChat("Bandanas and pirate hats are me specialty.", "Would you like me to sew on for ye?", CONTENT);
			return true;
		    case 16:
			player.getDialogue().sendOption("Yes!", "No.");
			return true;
		    case 17:
			switch(optionId) {
			    case 1:
				player.getDialogue().sendNpcChat("What piece would ye like sown?", CONTENT);
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 18:
			player.getDialogue().sendOption("White Bandana (650 gold)", "Red Bandana (750 gold)", "Blue Bandana (500 gold)" , "Brown Bandana (1000 gold)", "Pirate Hat(5000 gold)");
			return true;
		    case 19:
			switch(optionId) {
			    case 1:
				if(!player.getInventory().playerHasItem(995, 650)) {
				    player.getDialogue().sendPlayerChat("Erm, it seems I'm a little low on funds...", SAD);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getInventory().playerHasItem(1025)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have an eyepatch for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getInventory().playerHasItem(7112)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have that bandana for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(player.getInventory().playerHasItem(7112) && player.getInventory().playerHasItem(1025) && player.getInventory().playerHasItem(995, 650)) {
				    player.getDialogue().sendGiveItemNpc("You hand Patchy the supplies for the sewing.", "After a few quick motions he is done sewing them together.", new Item(1025), new Item(8924));
				    player.getDialogue().endDialogue();
				    player.getInventory().replaceItemWithItem(new Item(7112), new Item(8924));
				    player.getInventory().removeItem(new Item(1025));
				    player.getInventory().removeItem(new Item(995, 650));
				    return true;
				}
			    return false;
			    case 2:
				if(!player.getInventory().playerHasItem(995, 750)) {
				    player.getDialogue().sendPlayerChat("Erm, it seems I'm a little low on funds...", SAD);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getInventory().playerHasItem(1025)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have an eyepatch for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getInventory().playerHasItem(7124)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have that bandana for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(player.getInventory().playerHasItem(7124) && player.getInventory().playerHasItem(1025) && player.getInventory().playerHasItem(995, 750)) {
				    player.getDialogue().sendGiveItemNpc("You hand Patchy the supplies for the sewing.", "After a few quick motions he is done sewing them together.", new Item(1025), new Item(8925));
				    player.getDialogue().endDialogue();
				    player.getInventory().replaceItemWithItem(new Item(7124), new Item(8925));
				    player.getInventory().removeItem(new Item(1025));
				    player.getInventory().removeItem(new Item(995, 750));
				    return true;
				}
			    return false;
			    case 3:
				if(!player.getInventory().playerHasItem(995, 500)) {
				    player.getDialogue().sendPlayerChat("Erm, it seems I'm a little low on funds...", SAD);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getInventory().playerHasItem(1025)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have an eyepatch for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(!player.getInventory().playerHasItem(7130)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have that bandana for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(player.getInventory().playerHasItem(7130) && player.getInventory().playerHasItem(1025) && player.getInventory().playerHasItem(995, 500)) {
				    player.getDialogue().sendGiveItemNpc("You hand Patchy the supplies for the sewing.", "After a few quick motions he is done sewing them together.", new Item(1025), new Item(8926));
				    player.getDialogue().endDialogue();
				    player.getInventory().replaceItemWithItem(new Item(7130), new Item(8926));
				    player.getInventory().removeItem(new Item(1025));
				    player.getInventory().removeItem(new Item(995, 500));
				    return true;
				}
			    return false;
			    case 4:
				if(!player.getInventory().playerHasItem(995, 1000)) {
				    player.getDialogue().sendPlayerChat("Erm, it seems I'm a little low on funds...", SAD);
				    player.getDialogue().endDialogue();
				    return true;
				}
				if(!player.getInventory().playerHasItem(1025)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have an eyepatch for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				if(!player.getInventory().playerHasItem(7136)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have that bandana for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(player.getInventory().playerHasItem(7136) && player.getInventory().playerHasItem(1025) && player.getInventory().playerHasItem(995, 1000)) {
				    player.getDialogue().sendGiveItemNpc("You hand Patchy the supplies for the sewing.", "After a few quick motions he is done sewing them together.", new Item(1025), new Item(8927));
				    player.getDialogue().endDialogue();
				    player.getInventory().replaceItemWithItem(new Item(7136), new Item(8927));
				    player.getInventory().removeItem(new Item(1025));
				    player.getInventory().removeItem(new Item(995, 1000));
				    return true;
				}
			    return false;
			    case 5:
				if(!player.getInventory().playerHasItem(995, 5000)) {
				    player.getDialogue().sendPlayerChat("Erm, it seems I'm a little low on funds...", SAD);
				    player.getDialogue().endDialogue();
				    return true;
				}
				if(!player.getInventory().playerHasItem(1025)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have an eyepatch for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				if(!player.getInventory().playerHasItem(2651)) {
				    player.getDialogue().sendNpcChat("I'm afraid you don't have a pirate hat for me, matey!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
				else if(player.getInventory().playerHasItem(2651) && player.getInventory().playerHasItem(1025) && player.getInventory().playerHasItem(995, 5000)) {
				    player.getDialogue().sendGiveItemNpc("You hand Patchy the supplies for the sewing.", "After a few quick motions he is done sewing them together.", new Item(1025), new Item(8928));
				    player.getDialogue().endDialogue();
				    player.getInventory().replaceItemWithItem(new Item(2651), new Item(8928));
				    player.getInventory().removeItem(new Item(1025));
				    player.getInventory().removeItem(new Item(995, 5000));
				    return true;
				}
			    return false;
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
				if(!player.getInventory().ownsItem(PORCELAIN_CUP) && !player.getInventory().ownsItem(PORCELAIN_CUP_NETTLE) && !player.getInventory().ownsItem(PORCELAIN_CUP_NETTLE_MILKY)) {
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
				if(!player.getInventory().ownsItem(PORCELAIN_CUP) && !player.getInventory().ownsItem(PORCELAIN_CUP_NETTLE) && !player.getInventory().ownsItem(PORCELAIN_CUP_NETTLE_MILKY)) {
				    player.getDialogue().sendPlayerChat("I'm afraid I lost your cup.", CONTENT);
				    player.getDialogue().setNextChatId(10);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Did you get me some milky tea in my", "special cup there dear?", CONTENT);
				    return true;
				}
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
			}
		    return false;
		    case ITEMS_FOR_ENCHANTMENT_2:
			switch (player.getDialogue().getChatId()) {
			    case 1:
				if(playerHasItemsInInventory(player)) {
				    player.getDialogue().sendPlayerChat("I found all the items for the enchantment.", CONTENT);
				    return true;
				}
				else {
				    player.getDialogue().sendNpcChat("Go get all the items, hurry!", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				player.getDialogue().sendNpcChat("Good! Ah, the robes of Necrovarus...", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendNpcChat("The Book of Haricanto! I have no idea", "how you found this!", HAPPY);
				return true;
			    case 4:
				player.getDialogue().sendNpcChat("And the the translation manual - yes, this should", "do the job.", CONTENT);
				return true;
			    case 5:
				player.getDialogue().sendNpcChat("Wonderful, that's everything I need.", "I can perform the enchantment now.", CONTENT);
				return true;
			    case 6:
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
	    case INNKEEPER:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			if(player.getQuestStage(24) >= ITEMS_FOR_ENCHANTMENT && !player.getInventory().ownsItem(BEDSHEET)) {
			    player.getDialogue().sendPlayerChat("Do you have any sort of job for me?", CONTENT);
			    return true;
			}
			else {
			    return false;
			}
		    case 2:
			player.getDialogue().sendNpcChat("Yes, actually, I do. We have a very famous Master", "Bowman named Robin staying with us at the moment.", "Could you take him some clean bed linen for me?", CONTENT);
			return true;
		    case 3:
			player.getDialogue().sendGiveItemNpc("The Innkeeper hands you a bedsheet.", new Item(BEDSHEET));
			player.getDialogue().endDialogue();
			player.getInventory().addItemOrDrop(new Item(BEDSHEET));
			return true;
		}
	    return true;
	    case ROBIN:
		switch (player.getDialogue().getChatId()) {
		    case 1:
			if(!player.getInventory().ownsItem(SIGNED_OAK_BOW) && !player.getQuestVars().getRuneDrawWins()[0] && (player.getQuestStage(24) == ITEMS_FOR_ENCHANTMENT || player.getQuestStage(24) == ITEMS_FOR_ENCHANTMENT_2) ) {
			    player.getDialogue().sendPlayerChat("Would you sign an oak longbow for me?", CONTENT);
			    return true;
			}
			else if(player.getQuestVars().getRuneDrawWins()[0] && !player.getQuestVars().getRuneDrawWins()[1]) {
			    if(player.getQuestVars().justWonRuneDraw()) {
				player.getDialogue().sendPlayerChat("So are you going to pay up then?", "You still owe me 25 gold coins.", CONTENT);
				player.getDialogue().setNextChatId(15);
				return true;
			    }
			    else {
				player.getDialogue().sendPlayerChat("I may have lost, but...", "You still owe me 25 gold coins.", CONTENT);
				player.getDialogue().setNextChatId(15);
				return true;
			    }
			}
			else if(player.getQuestVars().getRuneDrawWins()[0] && player.getQuestVars().getRuneDrawWins()[1] && !player.getQuestVars().getRuneDrawWins()[2]) {
			    if(player.getQuestVars().justWonRuneDraw()) {
				player.getDialogue().sendPlayerChat("So are you going to pay up this time?", "You still owe me 75 gold coins!", ANNOYED);
				player.getDialogue().setNextChatId(20);
				return true;
			    }
			    else {
				player.getDialogue().sendPlayerChat("I may have lost this last time, but, ", "you owe me 75 gold coins!", ANNOYED);
				player.getDialogue().setNextChatId(20);
				return true;
			    }
			}
			else if(player.getQuestVars().getRuneDrawWins()[0] && player.getQuestVars().getRuneDrawWins()[1] && player.getQuestVars().getRuneDrawWins()[2]) {
			    player.getDialogue().sendPlayerChat("I've had enough of you not paying up - you owe me", "100 gold now. I'm going to tell the ghosts what you're", "doing.", ANGRY_1);
			    player.getDialogue().setNextChatId(25);
			    return true;
			}
			else if (player.getQuestStage(24) < ITEMS_FOR_ENCHANTMENT && player.getQuestStage(24) >= QUEST_COMPLETE) {
			    player.getDialogue().sendNpcChat("Care for a game of RuneDraw? No bets involved!", "However...If you're here for some sort of signature,", "you can just turn around and leave.", CONTENT);
			    player.getDialogue().setNextChatId(30);
			    return true;
			}
			else {
			    return false;
			}
		    case 2:
			player.getDialogue().sendNpcChat("I'm sorry. I don't sign autographs.", CONTENT);
			return true;
		    case 3:
			player.getDialogue().sendNpcChat("While you're here, though, why don't you", "have a game of RuneDraw with me? If you've got", "25 gold pieces, I've got a bag of runes we can use.", CONTENT);
			return true;
		    case 4:
			player.getDialogue().sendOption("Yes, I'll give you a game. (25 gold)", "How do you play RuneDraw?", "No, gambling is wrong.");
			return true;
		    case 5:
			switch(optionId) {
			    case 1:
				if (player.getInventory().playerHasItem(995, 25)) {
				    player.getRuneDraw().openGame(25, true);
				    player.getDialogue().dontCloseInterface();
				    return true;
				} else {
				    player.getDialogue().sendPlayerChat("I don't have that much money to bet.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				player.getDialogue().sendPlayerChat("How do you play RuneDraw?", CONTENT);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("No, gambling is wrong.", ANNOYED);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 6:
			player.getDialogue().sendNpcChat("Two players take turns to draw a rune from a bag,", "which contains various runes. Each rune has a", "different value: an air rune is worth one point, up to", "a Nature rune which is worth nine points.", CONTENT);
			return true;
		    case 7:
			player.getDialogue().sendNpcChat("If a player draws the Death rune then the game", "is over, and they have lost.", CONTENT);
			return true;
		    case 8:
			player.getDialogue().sendNpcChat("A player can choose to hold once per game if they wish.", "The goal is to get as close to 21 points without going over.", "If you go over, or see that Death rune, you'll lose.", CONTENT);
			return true;
		    case 9:
			player.getDialogue().sendPlayerChat("Sounds easy enough.", CONTENT);
			return true;
		    case 10:
			player.getDialogue().sendNpcChat("So, what do you say, care to wager 25 gold?", CONTENT);
			return true;
		    case 11:
			player.getDialogue().sendOption("Yes, I'll play. (25 gold)", "No, thank you.");
			return true;
		    case 12:
			switch(optionId) {
			    case 1:
				if (player.getInventory().playerHasItem(995, 25)) {
				    player.getRuneDraw().openGame(25, true);
				    player.getDialogue().dontCloseInterface();
				    return true;
				} else {
				    player.getDialogue().sendPlayerChat("I don't have that much money to bet.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 15:
			player.getDialogue().sendNpcChat("Err, how about another game? Double or nothing?", "I'll pay you back with the winnings.", CONTENT);
			return true;
		    case 16:
			player.getDialogue().sendOption("Double or nothing! (50 gold)", "How do you play RuneDraw again?", "No, you won't make a gambler of me.");
			return true;
		    case 17:
			switch(optionId) {
			    case 1:
				if (player.getInventory().playerHasItem(995, 50)) {
				    player.getRuneDraw().openGame(50, true);
				    player.getDialogue().dontCloseInterface();
				    return true;
				} else {
				    player.getDialogue().sendPlayerChat("I don't have that much money to bet.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				player.getDialogue().sendPlayerChat("How do you play RuneDraw again?", CONTENT);
				player.getDialogue().setNextChatId(35);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("No, you won't make a gambler of me.", ANNOYED);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 20:
			player.getDialogue().sendNpcChat("Err, how about one last game? 25 more gold?", "I'll pay you back with all the winnings I'll get!", LAUGHING);
			return true;
		    case 21:
			player.getDialogue().sendOption("Fine, one last game. (25 coins)", "How do you play RuneDraw again?", "No.");
			return true;
		    case 22:
			switch(optionId) {
			    case 1:
				if (player.getInventory().playerHasItem(995, 25)) {
				    player.getRuneDraw().openGame(25, true);
				    player.getDialogue().dontCloseInterface();
				    return true;
				} else {
				    player.getDialogue().sendPlayerChat("I don't have that much money to bet.", CONTENT);
				    player.getDialogue().endDialogue();
				    return true;
				}
			    case 2:
				player.getDialogue().sendPlayerChat("How do you play RuneDraw again?", CONTENT);
				player.getDialogue().setNextChatId(35);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("No, enough is enough, Robin.", ANGRY_2);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 25:
			player.getDialogue().sendNpcChat("Please don't do that!!! They will suck the life from", "my bones!!!", DISTRESSED);
			return true;
		    case 26:
			player.getDialogue().sendPlayerChat("How about you signing my longbow then?", CONTENT);
			return true;
		    case 27:
			player.getDialogue().sendNpcChat("Yes! Anything!!", CONTENT);
			return true;
		    case 28:
			if(player.getInventory().playerHasItem(OAK_LONGBOW)) {
			    player.getDialogue().sendGiveItemNpc("Robin signs the oak longbow for you.", new Item(OAK_LONGBOW));
			    player.getDialogue().endDialogue();
			    player.getInventory().replaceItemWithItem(new Item(OAK_LONGBOW), new Item(SIGNED_OAK_BOW));
			    player.getQuestVars().setRuneDrawWins(0, false);
			    player.getQuestVars().setRuneDrawWins(1, false);
			    player.getQuestVars().setRuneDrawWins(2, false);
			    return true;
			}
			else {
			    player.getDialogue().sendPlayerChat("Let me go get the bow then.", CONTENT);
			    player.getDialogue().endDialogue();
			    return true;
			}
		    case 30:
			player.getDialogue().sendOption("Yes, I'll give you a game.", "How do you play RuneDraw?", "No, gambling is wrong.");
			return true;
		    case 31:
			switch (optionId) {
			    case 1:
				player.getRuneDraw().openGame();
				player.getDialogue().dontCloseInterface();
				return true;
			    case 2:
				player.getDialogue().sendPlayerChat("How do you play RuneDraw?", CONTENT);
				player.getDialogue().setNextChatId(35);
				return true;
			    case 3:
				player.getDialogue().sendPlayerChat("No, gambling is wrong.", ANNOYED);
				player.getDialogue().endDialogue();
				return true;
			}
		    case 35:
			player.getDialogue().sendNpcChat("Two players take turns to draw a rune from a bag,", "which contains various runes. Each rune has a", "different value: an air rune is worth one point, up to", "a Nature rune which is worth nine points.", CONTENT);
			return true;
		    case 36:
			player.getDialogue().sendNpcChat("If a player draws the Death rune then the game", "is over, and they have lost.", CONTENT);
			return true;
		    case 37:
			player.getDialogue().sendNpcChat("A player can choose to hold once per game if they wish.", "The goal is to get as close to 21 points without going over.", "If you go over, or see that Death rune, you'll lose.", CONTENT);
			return true;
		    case 38:
			player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
			player.getDialogue().endDialogue();
			return true;
		}
		return false;
	}
	return false;
    }

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