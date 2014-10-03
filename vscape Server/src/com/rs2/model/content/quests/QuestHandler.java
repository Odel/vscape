package com.rs2.model.content.quests;

//import com.rs2.model.content.DialogueManager;
//import com.rs2.model.content.DialogueManager.Dialogue;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.quests.CooksAssistant;
import com.rs2.model.players.item.*;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.PlayerSave;

/**
 * @date 1-jun-2011
 * @author Satan666
 */
public class QuestHandler {

    public static final int[] QUEST_IDS = {
        8145, 8146, 8147, 8148, 8149, 8150, 8151, 8152, 8153, 8154,
        8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163, 8164,
        8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174,
        8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184,
        8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194,
        8195, 12144, 12150, 12151, 12152, 12153, 12154, 12155, 12146
    };
    public static final int QUEST_INTERFACE = 8134;
    
    private static Quest[] quests = {
    	new CooksAssistant(),
    	new TheKnightsSword(),
    	new TheRestlessGhost(),
    	new DoricsQuest(),
    	new ImpCatcher(),
    	new RuneMysteries(),
    	new WitchsPotion(),
    	new EasterEvent(),
    	new SheepShearer(),
	new PrinceAli(),
	new DruidicRitual(),
	new MerlinsCrystal(),
	new ElementalWorkshop(),
	new ShieldOfArrav(),
	new LostCity(),
	new DragonSlayer(),
	new RomeoAndJuliet(),
	new DemonSlayer(),
	new BlackKnightsFortress(),
	new GoblinDiplomacy(),
	new PiratesTreasure(),
	new VampireSlayer(),
	new ErnestTheChicken(),
	new PriestInPeril(),
	new GhostsAhoy(),
	new AnimalMagnetism(),
	new HorrorFromTheDeep(),
	new HeroesQuest(),
	new FamilyCrest()
    };
    
    public static void init() {
        System.out.println("Loading quests...");
        System.out.println("Loaded " + quests.length + " quests.");
    }
    
    public static void initPlayer(Player player){
    	player.setQuestsLength(quests.length);
    	for(Quest q : quests)
    	{
    		player.setQuestStage(q.getQuestID(), 0);
    	}
        player.sendQuestTab(); //Makes the quest log empty except implemented quests
        PlayerSave.loadQuests(player); //loads quest progress from Username.txt, sets variables
    }
    
    public static Quest[] getQuests(){
    	return quests;
    }

    public static int getTotalQuests() {
        return quests.length;
    }

    public static void startQuest(Player player, int questID) {
        Quest quest = quests[questID];
        if (quest == null) {
            return;
        }
        if (!quest.canDoQuest(player)) {
            player.getActionSender().removeInterfaces();
            player.getActionSender().sendMessage("You can't do this quest yet.");
            return;
        }
        if (player.getQuestStage(quest.getQuestID()) == 0) {
            resetInterface(player);
            quest.startQuest(player);
        }
    }
    
    public static boolean questCompleted(Player player, int questID) {
        Quest quest = quests[questID];
        if (quest == null) {
            return false;
        }
        if(quest.questCompleted(player))
        {
        	return true;
        }
        return false;
    }

    public static void completeQuest(Player player, int questID) {
        Quest quest = quests[questID];
        if (quest == null) {
            return;
        }
        resetInterface(player);
        quest.completeQuest(player);
    }

    public static boolean handleQuestButtons(Player player, int button) {
        switch (button) {
		case 28165: //Cooks Assistant
			showInterface(player,quests[0]);
			return true;
        	case 28178: //The Knights Sword
        		showInterface(player,quests[1]);
        		return true;
        	case 28169: //The Restless Ghost
        		showInterface(player,quests[2]);
        		return true;
        	case 28168: //Doric's quest
        		showInterface(player,quests[3]);
        		return true;
        	case 28172: //Imp Catcher
        		showInterface(player,quests[4]);
        		return true;
        	case 28167: //Rune mysteries
        		showInterface(player,quests[5]);
        		return true;
        	case 28180: //witchs potion
        		showInterface(player,quests[6]);
        		return true;
        	case 28176: //Sheep Shearer
        		showInterface(player,quests[8]);
        		return true;
		case 28174: //Prince ali
        		showInterface(player,quests[9]);
        		return true;
		case 28187: //Druidic Ritual
			showInterface(player,quests[10]);
        		return true;
		case 28200: //Merlin's Crystal
			showInterface(player,quests[11]);
        		return true;
		case 29035: //Elemental Workshop
			showInterface(player,quests[12]);
        		return true;
		case 28177: //Shield of Arrav
			showInterface(player,quests[13]);
        		return true;
		case 28199: //Lost City
			showInterface(player,quests[14]);
        		return true;
		case 28215: //Dragon Slayer
			showInterface(player,quests[15]);
        		return true;
		case 28175: //Romeo and Juliet
			showInterface(player,quests[16]);
        		return true;
		case 28166: //Demon Slayer
			showInterface(player,quests[17]);
        		return true;
		case 28164: //Black Knight's Fortress
			showInterface(player,quests[18]);
        		return true;
		case 28170: //Goblin Diplomacy
			showInterface(player,quests[19]);
        		return true;
		case 28173: //Pirate's treasure
			showInterface(player,quests[20]);
        		return true;
		case 28179: //Vampire Slayer
			showInterface(player,quests[21]);
        		return true;
		case 28171: //Ernest the Chicken
			showInterface(player,quests[22]);
        		return true;
		case 31179: //Priest in Peril
			showInterface(player,quests[23]);
        		return true;
		case 47250: //Ghosts Ahoy
			showInterface(player,quests[24]);
        		return true;
		case 49228: //Animal Magnetism
			showInterface(player,quests[25]);
        		return true;
		case 39151: //Horror From The Deep
			showInterface(player,quests[26]);
        		return true;
		case 28195: //Heroes Quest
			showInterface(player,quests[27]);
        		return true;
		case 28189: //Family Crest
			showInterface(player,quests[28]);
        		return true;

        }
        return false;
    }
    
    public static void showInterface(Player player, Quest quest)
    {
        if (quest != null) 
        {
	    quest.showInterface(player);
	    quest.sendQuestTabStatus(player);
            resetInterface(player);
            quest.sendQuestRequirements(player);

        }
    }

    public static void resetInterface(Player player) {
        for (int i = 0; i < QUEST_IDS.length; i++) {
            player.getActionSender().sendString("", QUEST_IDS[i]);
        }
    }

}