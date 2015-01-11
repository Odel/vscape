package com.rs2;

import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.weapon.AttackStyle;

import java.util.LinkedList;
import java.util.List;

public class Constants {

	public static final String SERVER_NAME = "vidyascape";

	/*
	public static final String[] Admins = {
		"Mr foxter", 
		"Mr telescope", 
		"Odel", 
		"Noiryx",
		"Quietess",
		"Bobster",
		"mod basher"
	};

	public static final String[] Mods = {
		"kampeao", 
		"dark skies"
	};*/

	//public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	public static final String GAME_DB_URL = "jdbc:mysql://127.0.0.1:3306/vidyascape";
	public static final String GAME_DB_USER = "vidyascape";
	public static final String GAME_DB_PASS = "videogamesarethebest579";

	public static final String FORUM_DB_URL = "";
	public static final String FORUM_DB_USER = "admin06_b00mah3";
	public static final String FORUM_DB_PASS = "O1xS?rx%qEGS";

	public static boolean SQL_ENABLED = true;
	public static int SQL_TYPE = 1;  //Defines what sql system to use: 1 is mysql and 2 is sqlite
	public static boolean SERVER_DEBUG = true;
	public static boolean DEVELOPER_MODE = true;
	public static boolean HIGHSCORES_ENABLED = false;

	public static boolean ADMINS_CAN_INTERACT = true;
	
    List<String> list1 = new LinkedList<String>();
	public static final int MAGIC_ID = -1;
	public static final byte WORLD_NUMBER = (byte)1;
    public static boolean RSA_CHECK = true;
    public static boolean SYSTEM_UPDATE = false;
	public static int CLIENT_VERSION = 318;
	public static final String TEST_VERSION = "1.4";
   	public static final double EXP_RATE = 2.25;
   	
   	public static final boolean MAC_CHECK = true;
   	public static final int MAX_CONNECTIONS_PER_MAC = 1;
   	public static final int MAX_CONNECTIONS_PER_IP = 3;
   	
   	public static final int START_X = 3222;
	public static final int START_Y = 3218;
	public static final int START_Z = 0;
	public static final int MAX_NPCS = 8192;
	public static final int MAX_NPC_ID = 6391;
	public static final int NPC_WALK_DISTANCE = 2;
	public static final int NPC_FOLLOW_DISTANCE = 10;
	public static final int MAX_ITEMS = 11790;
	public static final int MAX_ITEM_COUNT = Integer.MAX_VALUE;
	public static final int MAX_PLAYERS_AMOUNT = 1000;
	public static final int LOGIN_RESPONSE_OK = 2;
	public static final int LOGIN_RESPONSE_INVALID_CREDENTIALS = 3;
	public static final int LOGIN_RESPONSE_ACCOUNT_DISABLED = 4;
	public static final int LOGIN_RESPONSE_ACCOUNT_ONLINE = 5;
	public static final int LOGIN_RESPONSE_UPDATED = 6;
	public static final int LOGIN_RESPONSE_WORLD_FULL = 7;
	public static final int LOGIN_RESPONSE_LOGIN_SERVER_OFFLINE = 8;
	public static final int LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED = 9;
	public static final int LOGIN_RESPONSE_BAD_SESSION_ID = 10;
	public static final int LOGIN_RESPONSE_PLEASE_TRY_AGAIN = 11;
	public static final int LOGIN_RESPONSE_NEED_MEMBERS = 12;
	public static final int LOGIN_RESPONSE_COULD_NOT_COMPLETE_LOGIN = 13;
	public static final int LOGIN_RESPONSE_SERVER_BEING_UPDATED = 14;
	public static final int LOGIN_RESPONSE_LOGIN_ATTEMPTS_EXCEEDED = 16;
	public static final int LOGIN_RESPONSE_MEMBERS_ONLY_AREA = 17;
	public static final int HAT = 0;
	public static final int CAPE = 1;
	public static final int AMULET = 2;
	public static final int WEAPON = 3;
	public static final int CHEST = 4;
	public static final int SHIELD = 5;
	public static final int LEGS = 7;
	public static final int HANDS = 9;
	public static final int FEET = 10;
	public static final int RING = 12;
	public static final int ARROWS = 13;
	public static final int APPEARANCE_SLOT_CHEST = 0;
	public static final int APPEARANCE_SLOT_ARMS = 1;
	public static final int APPEARANCE_SLOT_LEGS = 2;
	public static final int APPEARANCE_SLOT_HEAD = 3;
	public static final int APPEARANCE_SLOT_HANDS = 4;
	public static final int APPEARANCE_SLOT_FEET = 5;
	public static final int APPEARANCE_SLOT_BEARD = 6;
	public static final int GENDER_MALE = 0;
	public static final int GENDER_FEMALE = 1;

	public static boolean AGILITY_ENABLED = true;
	public static boolean COOKING_ENABLED = true;
	public static boolean CRAFTING_ENABLED = true;
	public static boolean FARMING_ENABLED = true;
	public static boolean FIREMAKING_ENABLED = true;
	public static boolean FISHING_ENABLED = true;
	public static boolean FLETCHING_ENABLED = true;
	public static boolean HERBLORE_ENABLED = true;
	public static boolean MINING_ENABLED = true;
	public static boolean PRAYER_ENABLED = true;
	public static boolean RUNECRAFTING_ENABLED = true;
	public static boolean SLAYER_ENABLED = true;
	public static boolean SMITHING_ENABLED = true;
	public static boolean THIEVING_ENABLED = true;
	public static boolean WOODCUTTING_ENABLED = true;
	
	public static boolean FARMING_BUSHES_ENABLED = true;
	
	public static boolean DEGRADING_ENABLED = true;

    public static boolean DUELING_DISABLED = false;
    
    public static boolean CASTLEWARS_ENABLED = true;
    
    public static boolean STAFF_ONLY_YELL = false;

	// modern teleport coords
	public static final int VARROCK_X = 3213, VARROCK_Y = 3423;
	public static final int LUMBRIDGE_X = 3222, LUMBRIDGE_Y = 3218;
	public static final int FALADOR_X = 2964, FALADOR_Y = 3378;
	public static final int CAMELOT_X = 2757, CAMELOT_Y = 3479;
	public static final int ARDOUGNE_X = 2662, ARDOUGNE_Y = 3305;
	public static final int WATCH_TOWER_X = 2547, WATCH_TOWER_Y = 3112;
	public static final int TROLLHEIM_X = 2910, TROLLHEIM_Y = 3612;
	public static final int APE_ATOLL_X = 2797, APE_ATOLL_Y = 2799;

	// ancient teleport coords
	public static final int PADDEWWA_X = 3098, PADDEWWA_Y = 9884;
	public static final int SENNTISTEN_X = 3321, SENNTISTEN_Y = 3335;
	public static final int KHARYRLL_X = 3492, KHARYRLL_Y = 3471;
	public static final int LASSAR_X = 3001, LASSAR_Y = 3470;
	public static final int DAREEYAK_X = 2968, DAREEYAK_Y = 3699;
	public static final int CARRALLANGAR_X = 3156, CARRALLANGAR_Y = 3666;
	public static final int ANNAKARL_X = 3287, ANNAKARL_Y = 3886;
	public static final int GHORROCK_X = 2977, GHORROCK_Y = 3873;

	public static final int[] MOVEMENT_ANIMS = new int[] { 0x328, 0x333, 0x338 };

	public static AttackStyle MAGIC_STYLE = new AttackStyle(AttackType.MAGIC, AttackStyle.Mode.MAGIC, AttackStyle.Bonus.MAGIC);

	public static final String BONUS_NAME[] = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength", "Prayer" };

	public static final int[] TRADEABLE_ITEMS = {1458, 6332};
	public static final int[] UNTRADEABLE_ITEMS = {6529, 3690, 3691, 6125, 6126, 6127, 1590, 1591, 772};
	public static final int[] FUN_WEAPONS = { 4566, 1419, 2460, 2462, 2464, 2466, 2468, 2470, 2472, 2474, 2476, 8936, 8938 };

    public static final int[] MASKS = { 1053, 1055, 1057};
    
    public final static int[] SWEETS = {
		4558,
		4559,
		4560,
		4561,
		4562,
		4563,
		4564 
	};
	
	public static final int EMPTY_OBJECT = 6951;

	public static final int PACKET_LENGTHS[] = {
			0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 4, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			/*2 item examine*/0, 0, 2, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};

    public static final int[][][] COLOR_RANGES = new int[][][] { { { 0, 0, 0, 0, 0 },  { 11, 15, 15, 5, 7} }, { { 0, 0, 0, 0, 0 }, { 11, 15, 15, 5, 7} } };
    public static final int[][][] APPEARANCE_RANGES = new int[][][] { { { 18, 26, 36, 0, 33, 42, 10 }, { 25, 31, 40, 8, 34, 43, 17 } }, { { 56, 61, 70, 45, 67, 79, -1 }, { 60, 65, 77, 54, 68, 80, -1 } } };

    public static final String[] colorStrings = {"@red@","@gre@","@blu@","@yel@","@cya@","@mag@","@whi@","@bla@","@lre@","@dre@","@dbl@","@or1@","@or2@","@or3@","@gr1@","@gr2@","@gr3@","@str@","@end@"};
	public static final String[] bad = {":chalreq:", ":tradereq:", ":duelreq:", ":yell:"};
	public static final String[] bannedChars = {"!","$","%","^","&","*","+","=","@",":",";","~","#","<",">",",",".","?","\\","/","`","|","[","]","{","}","(",")","-","'","\""};
	
    public static final int SKILL_ATTACK = 0, SKILL_DEFENCE = 1,
            SKILL_STRENGTH = 2, SKILL_HP = 3, SKILL_RANGE = 4, SKILL_PRAY = 5,
            SKILL_MAGE = 6, SKILL_COOK = 7, SKILL_WC = 8, SKILL_FLETCH = 9,
            SKILL_FISH = 10, SKILL_FM = 11, SKILL_CRAFT = 12, SKILL_SMITH = 13,
            SKILL_MINE = 14, SKILL_HERB = 15, SKILL_AGILITY = 16,
            SKILL_THIEVE = 17, SKILL_SLAY = 18, SKILL_FARM = 19, SKILL_RC = 20;

	public static boolean UNLIMITED_RUN = true;
}
