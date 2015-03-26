package com.rs2.model.content.quests.impl.UndergroundPass;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.combat.weapon.RangedAmmoType;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.impl.Quest;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;
import static org.jruby.ext.bigdecimal.RubyBigDecimal.mode;

public class UndergroundPass implements Quest {

	public static final int questIndex = 9927;
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int QUEST_COMPLETE = 2;

	//Items
	public static final int ROCK = 1480;
	public static final int ORB_OF_LIGHT = 1481;
	public static final int ORB_OF_LIGHT_2 = 1482;
	public static final int ORB_OF_LIGHT_3 = 1483;
	public static final int ORB_OF_LIGHT_4 = 1484;
	public static final int DAMP_CLOTH = 1485;
	public static final int PIECE_OF_RAILING = 1486;
	public static final int UNICORN_HORN = 1487;
	public static final int PALADINS_BADGE = 1488;
	public static final int PALADINS_BADGE_2 = 1489;
	public static final int PALADINS_BADGE_3 = 1490;
	public static final int WITCHS_CAT = 1491;
	public static final int DOLL_OF_IBAN = 1492;
	public static final int OLD_JOURNAL = 1493;
	public static final int HISTORY_OF_IBAN = 1494;
	public static final int KLANKS_GAUNTLETS = 1495;
	public static final int IBANS_DOVE = 1496;
	public static final int AMULET_OF_OTHANIAN = 1497;
	public static final int AMULET_OF_DOOMION = 1498;
	public static final int AMULET_OF_HOLTHION = 1499;
	public static final int IBANS_SHADOW = 1500;
	public static final int DWARF_BREW = 1501;
	public static final int IBANS_ASHES = 1502;

	//Positions
	//KALRAG new Position(2357, 9911, 0)
	// cave with teleport icon? new Position(2357, 9911, 0)
	public static final Position PASS_ENTRANCE_POS = new Position(2495, 9716, 0);
	public static final Position PASS_EXIT_POS = new Position(2435, 3314, 0);

	//Interfaces
	public static final int INTERFACE = -1;

	//Npcs
	public static final int KING_LATHAS = -1;
	public static final int KOFTIK_NO_HOOD = 972;
	public static final int KOFTIK = 973;
	public static final int KOFTIK_2 = 974;
	public static final int KOFTIK_3 = 975;
	public static final int KOFTIK_4 = 976;
	public static final int BOULDER = 986;
	public static final int SIR_JERRO = 988;
	public static final int SIR_CARL = 989;
	public static final int SIR_HARRY = 990;
	public static final int HALF_SOULLESS = 991;
	public static final int KARDIA = 992;
	public static final int WITCHS_CAT_ITEM = 993;
	public static final int NILOOF = 994;
	public static final int KLANK = 995;
	public static final int KAMEN = 996;
	public static final int KALRAG = 997;
	public static final int OTHAINIAN = 998;
	public static final int DOOMION = 999;
	public static final int HOLTHION = 1000;
	public static final int DARK_MAGE = 1001;
	public static final int DISCIPLE_OF_IBAN = 1002;
	public static final int LORD_IBAN = 1003;
	/*
	979 Slave combat lvl: 0
980 Slave combat lvl: 0
981 Slave combat lvl: 0
982 Slave combat lvl: 0
983 Slave combat lvl: 0
984 Slave combat lvl: 0
985 Slave combat lvl: 0
	*/

	//Objects
	public static final int PASS_ENTRANCE = 3213;
	public static final int PASS_EXIT = 3214;
	public static final int GUIDE_ROPE = 3340;
	
	public static final String[] ibanWhispers = {"Blood, pain and hate.", "Death is only the beginning.", "Kill, maim... murder.", "I'll swallow your soul.", "The power of the gods could be yours.", "Hear me...", "Iban will save you... He'll save us all.", "I will release you...", "Make them all pay!", "Join us!", "I see you adventurer... you can't hide."};

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = {
		{Skill.AGILITY, 3000},
		{Skill.ATTACK, 3000}
	};

	private static final int questPointReward = 5;

	public int getQuestID() { //Change
		return 44;
	}

	public String getQuestName() { //Change
		return "Underground Pass";
	}

	public String getQuestSaveName() { //Change
		return "undergroundpass";
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
		player.getActionSender().sendItemOnInterface(12145, 250, DOLL_OF_IBAN); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("5 Quest Points", 12150);
		player.getActionSender().sendString("6750 Agility XP", 12151);
		player.getActionSender().sendString("6750 Attack XP", 12152);
		player.getActionSender().sendString("Iban Staff", 12153);
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
				lastIndex = 4;
				break;
			case QUEST_COMPLETE:
				lastIndex = 26;
				break;
		}
		lastIndex++;

		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to King Lathas in the Ardougne Castle to begin.", 1, this.getQuestID(), 0);
		
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@King Lathas @bla@in the @dre@Ardougne Castle @bla@to begin.", 1);
				a.sendQuestLogString("@dre@Requirements:", 3);
				a.sendQuestLogString((QuestHandler.questCompleted(player, QuestHandler.getQuestId("Biohazard")) ? "@str@" : "@dbl@") + "-Biohazard", 5);
				a.sendQuestLogString((player.getSkill().getPlayerLevel(Skill.RANGED) >= 25 ? "@str@" : "@dbl@") + "-25 Range", 6);
				break;
			case QUEST_STARTED:
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
		/*
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
		}
		*/
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}
	
	public static void startIbanWhispers(final Player player) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				if(!player.inUndergroundPass())
					b.stop();
				else
					player.getActionSender().sendMessage(ibanWhispers[Misc.randomMinusOne(ibanWhispers.length)]);
			}
			@Override
			public void stop() {
			}
		}, 120);
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
		switch(object) {
			case 3230: //flat rock trap
				if(item == 960) { //plank
					PassObjectHandling.handleDisarmTrap(player, 3231, 827, null);
					return true;
				}
			return false;
			case 2275: //rock swing
			case 2276:
				if(item == 954) { //rope
					PassObjectHandling.handleRopeSwing(player, object);
					return true;
				}
			return false;
		}
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
			case 3234:
				if(player.inUndergroundPass()) {
					Dialogues.startDialogue(player, 32340);
					return true;
				}
			return false;
			case 3339:
				if(x == 2382 && y == 9668) {
					Dialogues.startDialogue(player, ORB_OF_LIGHT + 10000);
					return true;
				}
			return false;
			case 3230:
				PassObjectHandling.handleDisarmTrap(player, object, 2244, null);
				return true;
			case 2274: //rope swing
				PassObjectHandling.handleRopeSwing(player, object);
				return true;
			case 3337:
				if(x == 2466 && y == 9672) {
					PassObjectHandling.handlePortcullis(player);
					return true;
				}
				return false;
			case 3241: //lever
				if (x == 2436 && y == 9716) {
					PassObjectHandling.returnOverBridge(player);
					return true;
				}
				return false;
			case GUIDE_ROPE:
				if (player.getPosition().getY() < 9718) {
					player.getActionSender().sendMessage("You can't get a clear shot from here.");
				} else {
					PassObjectHandling.shootBridgeRope(player, x, y);
				}
				return true;
			case 3295:
			case 3296:
			case 3297:
			case 3298:
				PassObjectHandling.readTablets(player, object);
				return true;
			case PASS_ENTRANCE:
				if(x == 2433 && y == 3313) {
					player.getUpdateFlags().sendAnimation(844);
					//player.fadeTeleport(PASS_ENTRANCE_POS);
					startIbanWhispers(player);
					return true;
				}
				return false;
			case PASS_EXIT:
				if(x == 2496 && y == 9713) {
					player.getUpdateFlags().sendAnimation(844);
					player.fadeTeleport(PASS_EXIT_POS);
					return true;
				}
				return false;
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
			case 32340:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("The markings appear to be holes in the wall... It's a trap!");
						return true;
					case 2:
						d.sendOption("Try and disarm the trap.", "Leave it alone.");
						return true;
					case 3:
						player.getActionSender().removeInterfaces();
						d.endDialogue();
						if (optionId == 1) {
							PassObjectHandling.handleDisarmTrap(player, 3234, 2246, null);
						}
						return true;	
				}
			return false;
			case ORB_OF_LIGHT + 10000:
				switch (d.getChatId()) {
					case 1:
						d.sendStatement("The rock appears to move slightly as you touch it... It's a trap!");
						return true;
					case 2:
						d.sendOption("Try and disarm the trap.", "Leave it alone.");
						return true;
					case 3:
						player.getActionSender().removeInterfaces();
						d.endDialogue();
						if (optionId == 1) {
							PassObjectHandling.handleDisarmTrap(player, 3361, 2244, null);
						}
						return true;	
				}
			return false;
			case KING_LATHAS:
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
