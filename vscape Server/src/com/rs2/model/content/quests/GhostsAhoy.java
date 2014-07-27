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
    public static final int NETTLE_TEA_BOWL_MILKY = 4240;
    public static final int NETTLES = 4241;
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
    public static final int BUCKET_OF_SLIME = 4286;
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
    public static final int[] DYES = {1763, 1765, 1767, 1769, 1771, 1773};
    public static final int RED_DYE = DYES[0];
    public static final int YELLOW_DYE = DYES[1];
    public static final int BLUE_DYE = DYES[2];
    public static final int ORANGE_DYE = DYES[3];
    public static final int GREEN_DYE = DYES[4];
    public static final int PURPLE_DYE = DYES[5];
    
    
    public static final int VELORINA = 1683;

    
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
	    
	    player.getActionSender().sendString("x", 8149);
	}
	else if (questStage == QUEST_COMPLETE) {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("@str@" + "Talk to Velorina in Port Phasmatys to begin this quest.", 8147);
	    
	    player.getActionSender().sendString("@red@" + "You have completed this quest!", 8167);
	}
        else {
            player.getActionSender().sendString(getQuestName(), 8144);
            player.getActionSender().sendString("Talk to @dre@Velorina @bla@in @dre@Port Phasmatys @bla@to begin this quest.", 8147);
	    player.getActionSender().sendString("@dre@Requirements:", 8149);
	    player.getActionSender().sendString("-Ability to defeat a level 32 lobster.", 8150);
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
            player.getActionSender().sendString("Talk to @dre@King Roald @bla@in the @dre@Varrock Palace @bla@to begin this quest.", 8147);
	    player.getActionSender().sendString("@dre@Requirements:", 8148);
	    player.getActionSender().sendString("-Ability to defeat a level 30 enemy", 8150);
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

    public static boolean itemHandling(Player player, int itemId) {
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
	    case VELORINA:
		switch(player.getQuestStage(23)) {
		    case 0: //start
			switch (player.getDialogue().getChatId()) {
			    case 1:
				player.getDialogue().sendPlayerChat("fag u", CONTENT);
				return true;
			}
		    return false;
		}
	    return false;
	}
	return false;
    }

}