package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;

public class Biohazard implements Quest {

	public static final int questIndex = 7352;
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int QUEST_COMPLETE = 2;

	//Items
	public static final int ETHENEA = 415;
	public static final int LIQUID_HONEY = 416;
	public static final int SULFURIC_BROLINE = 417;
	public static final int PLAGUE_SAMPLE = 418;
	public static final int TOUCH_PAPER = 419;
	public static final int DISTILLATOR = 420;
	public static final int LATHAS_AMULET = 421;
	public static final int BIRD_FEED = 422;
	public static final int KEY = 423;
	public static final int PIGEON_CAGE_FULL = 424;
	public static final int PIGEON_CAGE_EMPTY = 425;
	public static final int PRIEST_GOWN_TOP = 426;
	public static final int PRIEST_GOWN_BOTTOM = 428;
	public static final int DOCTORS_GOWN = 430;

	//Positions
	public static final Position POSITION = new Position(0, 0, 0);

	//Interfaces
	public static final int INTERFACE = -1;

	//Npcs
	public static final int ELENA = 3209;
	public static final int KING_LATHAS = 364;
	public static final int JERICO = 366;
	public static final int CHEMIST = 367;
	public static final int GUARD = 368;
	public static final int NURSE_SARAH = 373;
	public static final int DA_VINCI = 336;
	public static final int CHANCY = 338;
	public static final int HOPS_DRINKING = 340;
	public static final int HOPS = 341;
	public static final int GUIDORS_WIFE = 342;
	public static final int GUIDOR = 343;
	public static final int GUARD_1 = 344;
	public static final int GUARD_2 = 345;
	public static final int GUARD_3 = 346;
	public static final int KILRON = 349;
	public static final int OMART = 350;

	//Objects
	public static final int ELENAS_DOOR = 2054;

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = { //{skillId, exp},
		{Skill.THIEVING, 1250}
	};

	private static final int questPointReward = 3; //Change

	public int getQuestID() { //Change
		return 40;
	}

	public String getQuestName() { //Change
		return "Biohazard";
	}

	public String getQuestSaveName() { //Change
		return "biohazard";
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
		player.getActionSender().sendItemOnInterface(12145, 250, DISTILLATOR); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("2812.5 Thieving XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
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
			case QUEST_STARTED:
				lastIndex = 5;
				break;
			case QUEST_COMPLETE:
				lastIndex = 26;
				break;
		}
		lastIndex++;
		
		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Elena in East Ardougne to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Elena told me to talk to her father's old friend", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("Jerico in order to try and gain access to West", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("Ardougne again", 5, this.getQuestID(), QUEST_STARTED);
		
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@Elena @bla@in @dre@East Ardougne @bla@to begin.", 1);
				a.sendQuestLogString("@dre@Requirements:", 3);
				a.sendQuestLogString((QuestHandler.questCompleted(player, 39) ? "@str@" : "@dbl@") + "Plague City", 4);
				break;
			case QUEST_STARTED:
				a.sendQuestLogString("Additional information", lastIndex + 1);
				break;
			case QUEST_COMPLETE:
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
		int pX = player.getPosition().getX();
		int pY = player.getPosition().getY();
		switch (object) {
			case ELENAS_DOOR:
				player.getActionSender().walkThroughDoor(object, x, y, 0);
				player.getActionSender().walkTo(pX == x ? 0 : pX < x ? 1 : -1, pY < 3339 ? 1 : -1, true);
				player.reloadRegion();
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
			case ELENA:
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
