package com.rs2.model.content.quests.MonkeyMadness;

import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Graphic;
import com.rs2.model.objects.GameObject;
import com.rs2.model.World;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.util.Misc;
import com.rs2.model.content.skills.agility.Agility;

public class MonkeyMadness implements Quest {
    
    //Quest stages
    public static final int QUEST_STARTED = 1;
    public static final int QUEST_COMPLETE = 100;
    
    //Items
    public static final int DRAGONSTONE = 1615;
    public static final int BANANA = 1963;
    public static final int ROYAL_SEAL = 4004;
    public static final int NARNODES_ORDERS = 4005;
    public static final int MONKEY_DENTURES = 4006;
    public static final int ENCHANTED_BAR = 4007;
    public static final int EYE_OF_GNOME = 4008;
    public static final int EYE_OF_GNOME_2 = 4009; //Will this be needed?
    public static final int MONKEY_MAGIC = 4010;
    public static final int MONKEY_NUTS = 4012;
    public static final int MONKEY_BAR = 4014;
    public static final int BANANA_STEW = 4016;
    public static final int MONKEY_WRENCH = 4018;
    public static final int MONKEYSPEAK_AMULET_MOULD = 4020;
    public static final int MONKEYSPEAK_AMULET = 4021;
    public static final int MONKEYSPEAK_AMULET_U = 4022;
    public static final int MONKEY_TALISMAN = 4023;
    public static final int MONKEY = 4033;
    public static final int MONKEY_SKULL = 4034;
    public static final int TENTH_SQUAD_SIGIL = 4035;

    //Interfaces
    public static final int[] GLIDER_PUZZLE = {3904, 3906, 3908, 3910, 3912, 3914, 3916, 3918, 3920, 3922, 3924, 3926, 3928, 3930, 3932, 3934, 3936, 3938, 3940, 3942, 3944, 3946, 3948, 3950, -1};
    public static final int PUZZLE_INTERFACE = 11126;
    
    //Npcs
    public static final int HAZELMERE = 669;
    public static final int KING_NARNODE = 670;
    public static final int GLOUGH = 671;
    public static final int ANITA = 672;
    public static final int DAERO = 1407;
    public static final int WAYDAR = 1408;
    public static final int WAYDAR_2 = 1409;
    public static final int WAYDAR_3 = 1410;
    public static final int GARKOR = 1411;
    public static final int GARKOR_2 = 1412;
    public static final int LUMO = 1413;
    public static final int LUMO_2 = 1414;
    public static final int BUNKDO = 1415;
    public static final int BUNKDO_2 = 1416;
    public static final int CARADO = 1417;
    public static final int CARADO_2 = 1418;
    public static final int LUMBDO = 1419;
    public static final int KARAM = 1420;
    public static final int KARAM_2 = 1421;
    public static final int KARAM_3 = 1422;
    public static final int BUNKWICKET = 1423;
    public static final int WAYMOTTIN = 1424;
    public static final int ZOOKNOCK = 1425;
    public static final int ZOOKNOCK_2 = 1426;
    public static final int GLO_CARANOCK = 1427;
    public static final int GLO_CARANOCK_2 = 1428;
    public static final int DUGOPUL = 1429;
    public static final int SALENAB = 1430;
    public static final int TREFAJI = 1431;
    public static final int ABERAB = 1432;
    public static final int SOLIHIB = 1433;
    public static final int DAGA = 1434;
    public static final int TUTAB = 1435;
    public static final int IFABA = 1436;
    public static final int HAMAB = 1437;
    public static final int HAFUBA = 1438;
    public static final int DENADU = 1439;
    public static final int LOFU = 1440;
    public static final int KRUK = 1441;
    public static final int DUKE = 1442;
    public static final int OIPUIS = 1443;
    public static final int UYORO = 1444;
    public static final int OUHAI = 1445;
    public static final int UODAI = 1446;
    public static final int PADULAH = 1447;
    public static final int AWOWOGEI = 1448;
    public static final int UWOGO = 1449;
    public static final int MURUWOI = 1450;
    public static final int SLEEPING_MONKEY = 1451;
    public static final int MONKEY_CHILD = 1452;
    public static final int MONKEYS_UNCLE = 1453;
    public static final int MONKEYS_AUNT = 1454;
    public static final int ELDER_GUARD = 1461;
    public static final int ELDER_GUARD_2 = 1462;
    public static final int BONZARA = 1468;
    public static final int MONKEY_MINDER = 1469;
    public static final int FOREMAN = 1470;
    public static final int JUNGLE_DEMON = 1472;
    
    public int dialogueStage = 0;
    
    private int reward[][] = {
	{DRAGONSTONE, 3},
	{995, 100000} //Lovely money!
    };
    
    private int expReward[][] = {
	//to do
    };
    
    private static final int questPointReward = 3;

    public int getQuestID() {
        return 34;
    }

    public String getQuestName() {
        return "Monkey Madness";
    }
    
    public String getQuestSaveName() {
    	return "monkeymadness";
    }

    public boolean canDoQuest(final Player player) {
	return QuestHandler.questCompleted(player, 33) && QuestHandler.questCompleted(player, 29);
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
        player.getActionSender().sendItemOnInterface(995, 200, 12142);
        player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
        player.getActionSender().sendString("You are rewarded: ", 12146);
        player.getActionSender().sendString("3 Quest Points", 12150);
        player.getActionSender().sendString("100,000 coins", 12151);
        player.getActionSender().sendString("3 dragonstones", 12152);
        player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
        player.getActionSender().sendString(" ", 12147);
        player.setQuestStage(getQuestID(), QUEST_COMPLETE);
        player.getActionSender().sendString("@gre@"+ getQuestName(), 7361);
    }
    
    public void sendQuestRequirements(Player player) {
        int questStage = player.getQuestStage(getQuestID());
	switch(questStage) {
	    case QUEST_STARTED:
		player.getActionSender().sendString("@str@" + "x", 8147);
		
		player.getActionSender().sendString("x", 8149);
		break;
	    case QUEST_COMPLETE:
		player.getActionSender().sendString("@str@" + "x", 8147);
		
		player.getActionSender().sendString("@red@" + "You have completed this quest!", 8156);
		break;
	    default:
		player.getActionSender().sendString("Talk to @dre@x @bla@in the blah @bla@ to begin.", 8147);
		player.getActionSender().sendString("@dre@Requirements:", 8148);
		if(QuestHandler.questCompleted(player, 33)) {
		    player.getActionSender().sendString("@str@-Tree Gnome Village.", 8150);
		} else {
		    player.getActionSender().sendString("@dbl@-Tree Gnome Village.", 8150);
		}
		if(QuestHandler.questCompleted(player, 29)) {
		    player.getActionSender().sendString("@str@-The Grand Tree.", 8151);
		} else {
		    player.getActionSender().sendString("@dbl@-The Grand Tree.", 8151);
		}
		player.getActionSender().sendString("-Ability to defeat a level 195 Jungle Demon.", 8152);
		break;
        }
    }
    
    public void sendQuestInterface(Player player){
    	player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    }
    
    public void startQuest(Player player) {
        player.setQuestStage(getQuestID(), QUEST_STARTED);
        player.getActionSender().sendString("@yel@"+getQuestName(), 11132);
    }
    
    public boolean questCompleted(Player player) {
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
    		player.getActionSender().sendString("@yel@"+getQuestName(), 11132);
    	} else if (questStage == QUEST_COMPLETE) {
    		player.getActionSender().sendString("@gre@"+getQuestName(), 11132);
    	} else {
    		player.getActionSender().sendString("@red@"+getQuestName(), 11132);
    	}
    }
    
    public int getQuestPoints() {
        return questPointReward;
    }

    public void showInterface(Player player){
    	String prefix = "";
        player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
    	player.getActionSender().sendString(getQuestName(), 8144);
    }
    
    public void dialogue(Player player, Npc npc){
    	//Don't even need this anymore really
    }
    
    public int getDialogueStage(Player player){
    	return dialogueStage;
    }
    
    public void setDialogueStage(int in){
    	dialogueStage = in;
    }
    
    public static void openGliderPuzzle(final Player player) {
	player.getPuzzle().initPuzzle(6661234);
	player.getPuzzle().loadClueInterface(6661234);
    }
    
    public static boolean isHiddenInGrass(final Player player) {
	int objectId = 0;
	if(SkillHandler.getObject(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()) != null) {
	    objectId = SkillHandler.getObject(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()).getId();
	}
	if(objectId >= 4812 && objectId <= 4815) {
	    return true;
	}
	return false;
    }
    
    public static void handleDeath(final Player player, Npc npc) {

    }
    
    public boolean itemHandling(final Player player, int itemId) {
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
	switch(npc.getNpcId()) {
	    case TREFAJI:
	    case ABERAB:
		if(!player.getMMVars().isMonkey()) {
		    ApeAtoll.jail(player);
		}
	    return true;
	}
	return false;
    }
    
    public boolean doObjectClicking(final Player player, int object, int x, int y) {
	switch(object) {
           
	}
	return false;
    }
    
    public static boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
	switch (object) {

	}
	return false;
    }
    
    
    public boolean sendDialogue(final Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) { //Npc ID
	    case KING_NARNODE:
		if(!QuestHandler.questCompleted(player, 29)) { //Grand Tree
		    return false;
		}
		switch (player.getQuestStage(34)) { //Dialogue per stage
		    case 0: //Starting the quest
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("Hello.", CONTENT); //oh SHIT SON it is about to GO DOWN
				return true;
			}
		    return false;
		}
            return false;
	}
	return false;
    }

}