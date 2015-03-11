package com.rs2.model.content.quests.DeathPlateau;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.quests.Quest;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;

public class DeathPlateau implements Quest {

	public static final int questIndex = 8438; //Used in player's quest log interface, id is in Player.java, Change
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int QUEST_COMPLETE = 2;

	//Items
	public static final int STEEL_CLAWS = 3097;
	public static final int COMBINATION = 3102;
	public static final int IOU = 3103;
	public static final int SECRET_WAY_MAP = 3104;
	public static final int CLIMBING_BOOTS = 3105;
	public static final int SPIKED_BOOTS = 3107;
	public static final int RED_BALL = 3109;
	public static final int BLUE_BALL = 3110;
	public static final int YELLOW_BALL = 3111;
	public static final int PURPLE_BALL = 3112;
	public static final int GREEN_BALL = 3113;
	public static final int CERTIFICATE = 3114;

	//Positions
	public static final Position POSITION = new Position(0, 0, 0);

	//Interfaces
	public static final int DICE_INTERFACE = 6675;

	//Npcs
	public static final int DENULTH = 1060;
	public static final int SERGEANT_1 = 1061;
	public static final int SERGEANT_2 = 1062;
	public static final int SOLDIER_1 = 1063;
	public static final int SOLDIER_2 = 1064;
	public static final int SOLDIER_3 = 1065;
	public static final int SOLDIER_4 = 1066;
	public static final int SOLDIER_5 = 1067;
	public static final int SOLDIER_6 = 1068;
	public static final int SOLDIER_INJURED = 1069;
	public static final int SABA = 1070;
	public static final int TENZING = 1071;
	public static final int EADBURG = 1072;
	public static final int HAROLD = 1078;
	public static final int TOSTIG = 1079;
	public static final int EOHRIC = 1080;
	public static final int SERVANT = 1081;
	public static final int DUNSTAN = 1082;

	//Objects
	public static final int OBJECT = -1;

	private int reward[][] = { //{itemId, count},
		{STEEL_CLAWS, 1}
	};

	private int expReward[][] = { //{skillId, exp},
		{Skill.ATTACK, 3000}
	};

	private static final int questPointReward = 1; //Change

	public int getQuestID() { //Change
		return 42;
	}

	public String getQuestName() { //Change
		return "Death Plateau";
	}

	public String getQuestSaveName() { //Change
		return "deathplat";
	}

	public boolean canDoQuest(final Player player) {
		return false;
	}

	public void getReward(final Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (final int[] expRewards : expReward) {
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.getSkill().addExp(expRewards[0], (expRewards[1]));
				}
			}, 4);
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, STEEL_CLAWS); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("6750 Attack XP", 12151);
		player.getActionSender().sendString("Steel claws", 12152);
		player.getActionSender().sendString("Ability to make claws", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		int lastIndex = 0;
		switch(questStage) { 
			/**The last index of the text sent for that quest stage which persists
			through the entire quest, striked or not (i.e, shit sent below)
			Remember if there is no new persistent entry for that stage, stack the case with
			the last case that started the newest persistent entry, to keep lastIndex correct **/
			case QUEST_STARTED:
				lastIndex = 4;
				break;
			case QUEST_COMPLETE:
				lastIndex = 26;
				break;
		}
		lastIndex++;
		
		//Quest log entries that persist past each stage, line which to send (1 = first, etc)
		//And the quest stage which it first appears, after that stage it'll @str@, strikethrough
		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Denulth in Burthorpe to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("____ told me to do ______", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I should do this.", 4, this.getQuestID(), QUEST_STARTED);
		
		switch (questStage) { //Quest stages where you send additional information that is not stored
			//or striked out past that stage.
			default:
				break;
			case 0:
				//Not started quest journal shit, no need to use lastIndex
				a.sendQuestLogString("Talk to @dre@Denulth @bla@in @dre@Burthorpe @bla@to begin.", 1);
				break;
			case QUEST_STARTED:
				//Last index + index of new information (1 = first, etc)
				a.sendQuestLogString("Additional information", lastIndex + 1);
				break;
			case QUEST_COMPLETE:
				//Same here, first line after the last entry that persists for the whole quest
				a.sendQuestLogString("@red@" + "You have completed this quest!", lastIndex + 1);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {

		}
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
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case OBJECT:
				return true;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case DENULTH:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank you again!", Dialogues.HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
