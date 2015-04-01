package com.rs2.model.content.quests.impl;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;

import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;

public class TheGolem implements Quest {

	public static final int questIndex = 12839; //Used in player's quest log interface, id is in Player.java, Change
	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int FIXED_GOLEM = 2;
	public static final int READ_LETTER = 3;
	public static final int SPOKE_WITH_ELISSA = 4;
	public static final int READ_NOTES = 5;
	public static final int SPEAK_WITH_CURATOR = 6;
	public static final int PLACE_STATUETTE = 7;
	public static final int OPEN_HELL_DOOR = 8;
	public static final int ENTER_HELL = 9;
	public static final int SPEAK_TO_GOLEM = 10;
	public static final int USE_INSTRUMENT = 11;
	public static final int USE_CODE = 12;
	public static final int QUEST_COMPLETE = 13;

	//Items
	public static final int SOFT_CLAY = 1761;
	public static final int VIAL = 229;
	public static final int PESTLE_MORTAR = 233;
	public static final int KNIFE = 946;
	public static final int HAMMER = 2347;
	public static final int CHISEL = 1755;
	public static final int PAPYRUS = 970;
	public static final int PHOENIX_FEATHER = 4621;
	public static final int LETTER = 4615;
	public static final int VARMENS_NOTES = 4616;
	public static final int DISPLAY_CABINET_KEY = 4617;
	public static final int STATUETTE = 4618;
	public static final int STRANGE_IMPLIMENT = 4619;
	public static final int BLACK_MUSHROOM = 4620;
	public static final int BLACK_MUSHROOM_INK = 4622;
	public static final int PHEONIX_QUILL_PIN = 4623;
	public static final int GOLEM_PROGRAM = 4624;
	public static final int RUBY = 1603;
	public static final int EMERALD = 1605;
	public static final int SAPPHIRE = 1607;
	public static final int DIAMOND = 1601;

	//Positions
	public static final Position INSIDE_RUINS = new Position(2721, 4886, 0);
	public static final Position INNER_DEMON = new Position(2720, 4885, 2);
	public static final Position OUTSIDE_THRONEROOM = new Position(2722, 4911, 0);
	public static final Position OUTSIDE_RUINS = new Position(3491, 3090, 0);

	//Interfaces
	public static final int LETTER_INTERFACE = 13691;

	//Npcs
	public static final int GOLEM_BROKEN = 1908;
	public static final int GOLEM_DAMAGED = 1909;
	public static final int GOLEM = 1910;
	public static final int DESERT_PHOENIX = 1911;
	public static final int ELISSA = 1912;
	public static final int CURATOR = 646;

	//Objects
	public static final int STAIRS_DOWN = 6373;
	public static final int CRATE = 355;
	public static final int BOOKCASE = 2372;
	public static final int DISPLAY_CASE = 6293;
	public static final int DOOR_TO_HELL = 6310;
	public static final int THRONE = 6300;
	public static final int ALCOVE_1 = 6303;
	public static final int ALCOVE_2 = 6304;
	public static final int ALCOVE_3 = 6305;
	public static final int ALCOVE_4 = 6306;
	public static final int PORTAL = 6282;
	public static final int STAIRS_UP = 6372;
	public static final int GROUND_SHROOMS = 6311;

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = { //{skillId, exp},
		{Skill.CRAFTING, 1000}, {Skill.THIEVING, 1000},};

	private static final int questPointReward = 1; //Change

	public int getQuestID() { //Change
		return 46;
	}

	public String getQuestName() { //Change
		return "The Golem";
	}

	public String getQuestSaveName() { //Change
		return "thegolem";
	}

	public boolean canDoQuest(final Player player) {
		return player.getSkill().getLevel()[Skill.THIEVING] > 25 && player.getSkill().getLevel()[Skill.CRAFTING] > 25;
	}

	public void getReward(final Player player) {
		for (int[] rewards : this.reward) {
			player.getInventory().addItemOrDrop(
				new Item(rewards[0], rewards[1]));
		}
		for (final int[] expRewards : this.expReward) {
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
		this.getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, STATUETTE); //zoom, then itemId
		player.getActionSender().sendString(
			"You have completed " + this.getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("2250 Crafting XP", 12151);
		player.getActionSender().sendString("2250 Thieving XP", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString(
			"Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(this.getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + this.getQuestName(),
			questIndex);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(this.getQuestID());
		int lastIndex = 0;
		switch (questStage) {
			/**
			 * The last index of the text sent for that quest stage
			 * which persists through the entire quest, striked or
			 * not (i.e, shit sent below) Remember if there is no
			 * new persistent entry for that stage, stack the case
			 * with the last case that started the newest persistent
			 * entry, to keep lastIndex correct
         *
			 */
			case QUEST_STARTED:
				lastIndex = 3;
				break;
			case FIXED_GOLEM:
				lastIndex = 4;
				break;
			case READ_LETTER:
				lastIndex = 6;
				break;
			case SPOKE_WITH_ELISSA:
				lastIndex = 7;
				break;
			case READ_NOTES:
				lastIndex = 8;
				break;
			case SPEAK_WITH_CURATOR:
			case PLACE_STATUETTE:
			case OPEN_HELL_DOOR:
				lastIndex = 9;
				break;
			case ENTER_HELL:
				lastIndex = 10;
				break;
			case SPEAK_TO_GOLEM:
			case USE_INSTRUMENT:
				lastIndex = 12;
				break;
			case USE_CODE:
			case QUEST_COMPLETE:
				lastIndex = 15;
				break;
		}
		lastIndex++;

        //Quest log entries that persist past each stage, line which to send (1 = first, etc)
		//And the quest stage which it first appears, after that stage it'll @str@, strikethrough
		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to the golem in the ruins of Uzer to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("The golem is broken. I'm in the process of fixing it.", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I fixed the golem. The golem has asked me to help him.", 4, this.getQuestID(), FIXED_GOLEM);
		a.sendQuestLogString("I found a letter written by Elissa, the digsite worker.", 6, this.getQuestID(), READ_LETTER);
		a.sendQuestLogString("Elissa told me of Varmen's log book in the Exam Centre.", 7, this.getQuestID(), SPOKE_WITH_ELISSA);
		a.sendQuestLogString("The log book says the museum has the missing statuette.", 8, this.getQuestID(), READ_NOTES);
		a.sendQuestLogString("I've opened the portal! Let's see how tough this demon is.", 9, this.getQuestID(), OPEN_HELL_DOOR);
		a.sendQuestLogString("I found the demon's skeleton inside his throne room.", 10, this.getQuestID(), ENTER_HELL);
		a.sendQuestLogString("I told the golem, but he doesn't seem to understand.", 12, this.getQuestID(), SPEAK_TO_GOLEM);
		a.sendQuestLogString("I rewrote the golem's program according to the logbook.", 15, this.getQuestID(), USE_CODE);

		switch (questStage) { //Quest stages where you send additional information that is not stored
			//or striked out past that stage.
			default:
				break;
			case 0:
				//Not started quest journal shit, no need to use lastIndex
				a.sendQuestLogString(
					"Talk to @dre@the golem @bla@in the @dre@ruins of Uzer @bla@to begin.",
					1);
				a.sendQuestLogString("@dre@Requirements:", 3);
				if (player.getSkill().getLevel()[Skill.THIEVING] < 25) {
					a.sendQuestLogString("@dre@25 THIEVING", 5);
				} else {
					a.sendQuestLogString("@str@25 THIEVING", 5);
				}
				if (player.getSkill().getLevel()[Skill.CRAFTING] < 25) {
					a.sendQuestLogString("@dre@25 CRAFTING", 6);
				} else {
					a.sendQuestLogString("@str@25 CRAFTING", 6);
				}
				break;
			case FIXED_GOLEM:
				//Last index + index of new information (1 = first, etc)
				a.sendQuestLogString("He needs me to open the portal in the temple, but how?", lastIndex + 1);
				break;
			case ENTER_HELL:
				a.sendQuestLogString("I should tell the golem the good news.", lastIndex + 1);
				break;
			case SPEAK_TO_GOLEM:
				a.sendQuestLogString("The log explain how to prgram a golem.", lastIndex + 1);
				a.sendQuestLogString("Maybe I should give that a shot.", lastIndex + 2);
				break;
			case QUEST_COMPLETE:
				//Same here, first line after the last entry that persists for the whole quest
				a.sendQuestLogString(
					"@red@" + "You have completed this quest!",
					lastIndex + 1);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(this.getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + this.getQuestName(),
			questIndex);
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(this.getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
		int questStage = player.getQuestStage(this.getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + this.getQuestName(),
				questIndex);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + this.getQuestName(),
				questIndex);
		} else {
			player.getActionSender().sendString("@red@" + this.getQuestName(),
				questIndex);
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(this.getQuestName(), 8144);
	}

	public static final String[] varmensNotes = {"Septober 19:", "The nomads were right:",
		"there is a city here,", "probably buried for", "millennia and revealed by",
		"the random motions of", "the sand. The", "architecture is impressive",
		"even in ruin, and must", "once have been amazing.", "One puzzling factor is the",
		"pottery -- there are", "fragments all over the", "ruins, surely too much",
		"for a city even of this", "size. We have set up", "camp and will do a proper",
		"survey tomorrow.", "", "", "", "", "Septober 20:", "The meaning of the",
		"pottery was explained", "today in a most", "surprising manner. We",
		"found a mostly-intact clay", "statue buried up to its", "waist in sand and as",
		"soon as we dug it out, it", "started to walk around! It", "is a clay golem, built by",
		"the city's inhabitants and", "dormant all this time. Its", "head is badly damaged",
		"and it is", "uncommunicative, but its", "existence tells us that the", "city's inhabitants were",
		"expert magical craftsmen.", "The huge kilns in some", "of the buildings indicate",
		"that at some point before", "its destruction the whole", "city was converted to the",
		"manufacture of these", "golems.", "", "We have also examined", "the carvings on the large",
		"building in the centre.", "There are symbols", "depicting several of the",
		"ancient gods, including", "Saradomin, Zamorak, and", "Armadyl, but there is",
		"another prominent symbol", "that I cannot identify. As", "it seems we will need to",
		"be here for longer than I", "had thought, I have sent", "to Elissa for books on",
		"golems and religious", "symbols.", "", "Septober 21:", "As we examine the ruins",
		"one thing becomes", "increasingly clear: most", "of the damage was not", "due to weathering. The",
		"buildings were destroyed", "by force, as if torn down", "by giant hands.", "", "",
		"Septober 22:", "A breakthrough! We have", "found the staircase into", "the lower levels of the",
		"temple. This part has", "been untouched by the", "elements, and the", "carvings here are more",
		"intact, especially four", "beautiful statuettes in", "alcoves framing the large",
		"door. I have removed one", "of them. The door will", "not open. I am glad I",
		"sent for a book on", "symbols, as the", "unidentified symbol is", "even more prominent",
		"here, especially on the", "door.", "", "", "Septober 23:", "Our messenger returned",
		"with the books I asked for", "and a letter from Elissa.", "It is unfortunate that the",
		"museum will not be able", "to finance a full-scale", "excavation here as well as",
		"the one closer to Varrok,", "although I am of course", "pleased that the other city",
		"has been uncovered. But", "with the books I am able", "to piece together more of",
		"the story of the city.", "", "The unidentified symbol", "in the ruins is that of the",
		"demon Thammaron, who", "was Zamorak's chief", "lieutenant during the", "godwars of the Third",
		"Age. With that", "information I can say", "with confidence that these", "are the ruins of Uzer, an",
		"advanced human", "civilization said to have", "been destroyed towards", "the end of the Third Age",
		"(roughtly 2,500 years", "ago). It was allied with", "Saradomin and enjoyed",
		"his protection, as well as", "that of its own mages and", "warriors. Thammaron was",
		"able to open a portal from", "his own domain straight", "into the heart of the city,",
		"bypassing its defences.", "With Saradomin's help the", "army of Uzer was able to",
		"drive Thammaron back,", "but the record ends at", "that point and it has", "always been assumed that",
		"a later attack, either by", "Thammaron or by", "Zamorak's other forces,", "finished the city off.",
		"", "Examining the door", "again. I now see that it is", "exactly the sort of door",
		"that could be used to seal", "Thammaron's portal. I am", "suddenly glad I was not",
		"able to open it! I surmise", "that the army of golems", "was created in order to",
		"fight the demon, since", "Uzer's army had been", "wiped out and", "Saradomin's forces were",
		"increasing stretched.", "However, this approach", "evidently failed, since the",
		"city was eventually", "destroyed.", "", "The art of the", "construction of golems",
		"has been lost since the", "Third Age, and, although", "they are sometimes", "discovered lying dormant",
		"int he ground, no", "concerted effort has been", "made to regain it, thanks", "largely to the modern",
		"Saradomist Church's view", "of them as unnatural.", "This view is without", "foundation, as golems are",
		"neither good nor evil but", "follow instructions they", "are given to the letter",
		"and without imagination,", "indeed experiencing", "extreme discomfort for as",
		"long as a task assigned to", "them remains incomplete.", "Some golems were", "constructed to obey",
		"verbal instructions, but", "the main method of", "instruction was to place", "magical words into the",
		"golem's skull cavity.", "These were written on", "papyrus using a naturally", "occuring source of ink,",
		"and their magical power", "derived from the use of a", "phoenix tail feather as a",
		"pen. These would be used", "for long-term of", "important tasks, and", "would override any verbal", "instructions."};

	public static void assessConfigs(final Player player) {
		int count = 0, trueCount = 0, toSend = 0;
		for (int i : player.getQuestVars().getStatueStates()) {
			if (i == 1) {
				toSend += (1 << (10 + count));
				if (count != 3) {
					trueCount++;
				}
			} else if (i == 2) {
				toSend += (1 << 14);
				trueCount++;
			}
			count++;
		}

		if (player.getQuestVars().stolenThroneGems) {
			toSend += (1 << 16);
		}
		if (player.getQuestVars().stolenStatuette) {
			toSend += (1 << 17);
		}

		if (player.getQuestVars().statuesFacingCorrectly[0] && player.getQuestVars().statuesFacingCorrectly[1]
			&& player.getQuestVars().statuesFacingCorrectly[2] && player.getQuestVars().statuesFacingCorrectly[3]) {

			if (player.getQuestStage(46) == PLACE_STATUETTE) {
				player.getActionSender().sendMessage("The door slides open as you turn the statuette.");
				player.setQuestStage(46, OPEN_HELL_DOOR);
			}
		}
		
		player.getActionSender().sendConfig(437, toSend + (1 << trueCount));
	}

	public static void readLetter(final Player player) {
		ActionSender a = player.getActionSender();
		player.setStatedInterface("letter");
		a.sendInterface(LETTER_INTERFACE);
		a.sendString("", 13692);
		a.sendString("", 13693);
		a.sendString("@yel@Dearest Varmen,", 13695);
		a.sendString("@yel@I hope this finds you well. Here are some books", 13696);
		a.sendString("@yel@you asked for. There has been an exciting", 13697);
		a.sendString("@yel@development closer to home --another city from", 13698);
		a.sendString("@yel@the same period has been discovered east of", 13699);
		a.sendString("@yel@Varrok and we are starting a huge excavation", 13700);
		a.sendString("@yel@project here. I don't know if the museum will", 13701);
		a.sendString("@yel@be able to finance your expedition as well as", 13702);
		a.sendString("@yel@this one, so I fear your current trip will be", 13703);
		a.sendString("@yel@the last.", 13704);
		a.sendString("@yel@May Saradomin grant you a safe journey home.", 13705);
		a.sendString("@yel@Your loving Elissa.", 13706);
		a.sendString("", 13707);
		a.sendString("", 13708);
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case LETTER:
				if (player.getQuestStage(this.getQuestID()) >= FIXED_GOLEM) {
					readLetter(player);
					player.setQuestStage(this.getQuestID(), READ_LETTER);
				}
				return true;
			case VARMENS_NOTES:
				player.setQuestStage(this.getQuestID(), READ_NOTES);
				player.getBookHandler().initBook(varmensNotes, "The Ruins of Uzer");
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem,
		int secondItem, int firstSlot, int secondSlot) {
		if (player.getQuestStage(this.getQuestID()) >= READ_NOTES) {
			if ((firstItem == PESTLE_MORTAR && secondItem == BLACK_MUSHROOM) || (firstItem == BLACK_MUSHROOM && secondItem == PESTLE_MORTAR)) {
				if (player.getInventory().playerHasItem(VIAL)) {
					player.getInventory().removeItem(new Item(BLACK_MUSHROOM));
					player.getInventory().replaceItemWithItem(new Item(VIAL), new Item(BLACK_MUSHROOM_INK));
				}
			}

			if ((firstItem == BLACK_MUSHROOM_INK && secondItem == PHOENIX_FEATHER) || (firstItem == PHOENIX_FEATHER && secondItem == BLACK_MUSHROOM_INK)) {
				player.getInventory().replaceItemWithItem(new Item(PHOENIX_FEATHER), new Item(PHEONIX_QUILL_PIN));
			}

			if ((firstItem == PHEONIX_QUILL_PIN && secondItem == PAPYRUS) || (firstItem == PAPYRUS && secondItem == PHEONIX_QUILL_PIN)) {
				player.getInventory().replaceItemWithItem(new Item(PAPYRUS), new Item(GOLEM_PROGRAM));
			}
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case DISPLAY_CASE:
				if (item == DISPLAY_CABINET_KEY && player.getQuestStage(this.getQuestID()) >= SPEAK_WITH_CURATOR) {
					if (!player.getInventory().ownsItem(STATUETTE)) {
						player.getActionSender().sendMessage("You find the mysterious Statuette in the cabinet.");
						player.getInventory().addItemOrDrop(new Item(STATUETTE));
						player.getQuestVars().stolenStatuette = true;
						this.assessConfigs(player);
					}
				}
				return true;
			case THRONE:
				if ((item == CHISEL && player.getInventory().playerHasItem(HAMMER)) || (item == HAMMER && player.getInventory().playerHasItem(CHISEL))) {
					if (!player.getQuestVars().stolenThroneGems) {
						player.getQuestVars().stolenThroneGems = true;
						assessConfigs(player);
						player.getInventory().addItemOrDrop(new Item(RUBY, 2));
						player.getInventory().addItemOrDrop(new Item(EMERALD, 2));
						player.getInventory().addItemOrDrop(new Item(SAPPHIRE, 2));
						player.getInventory().addItemOrDrop(new Item(DIAMOND, 2));
					}
				} else if(item == CHISEL && !player.getInventory().playerHasItem(HAMMER)) {
					player.getDialogue().sendPlayerChat("I'll need a hammer to get these gems out.");
				}
				return true;
			case ALCOVE_4:
				if (item == STATUETTE && player.getQuestStage(this.getQuestID()) >= SPEAK_WITH_CURATOR) {
					player.setQuestStage(this.getQuestID(), PLACE_STATUETTE);
					player.getActionSender().sendMessage("You sit the statuette back in the alcove it came from.");
					player.getInventory().removeItem(new Item(STATUETTE));
					player.getQuestVars().placedStatuette = true;
					player.getQuestVars().statuesFacingCorrectly[3] = true;
					player.getQuestVars().setStatueStates(3, 2);
				}
				return true;
		}
		return false;
	}

	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		if (player.getQuestVars().clayAddedToGolem < 4) {
			if (itemId == SOFT_CLAY && npc.getNpcId() == GOLEM_BROKEN) {
				player.getQuestVars().clayAddedToGolem++;
				if (player.getQuestVars().clayAddedToGolem <= 3) {
					player.getDialogue().sendStatement("You apply some clay to the damaged golem.");
				}
				player.getInventory().removeItem(new Item(SOFT_CLAY));
				if (player.getQuestVars().clayAddedToGolem == 1) {
					QuestHandler.startQuest(player, this.getQuestID());
				} else if (player.getQuestVars().clayAddedToGolem == 4) {
					player.setQuestStage(this.getQuestID(), FIXED_GOLEM);
					player.getDialogue().sendStatement("You finish repairing the damaged golem.");
				}
				player.reloadRegion();
			}
		}

		if (player.getQuestStage(this.getQuestID()) == SPEAK_TO_GOLEM) {
			if (itemId == STRANGE_IMPLIMENT && npc.getNpcId() == GOLEM_BROKEN) {
				player.getActionSender().sendMessage("You insert the key and the golem's skull hinges open.");
				player.setQuestStage(this.getQuestID(), USE_INSTRUMENT);
			}
		}
		if (player.getQuestStage(this.getQuestID()) == USE_INSTRUMENT) {
			if (itemId == GOLEM_PROGRAM && npc.getNpcId() == GOLEM_BROKEN) {
				player.setQuestStage(this.getQuestID(), USE_CODE);
				Dialogues.startDialogue(player, 125501);
			}
		}
		return false;
	}

	public boolean doNpcClicking(Player player, Npc npc) {
		return false;
	}

	public boolean doNpcSecondClicking(final Player player, final Npc npc) {
		if (npc.getNpcId() == DESERT_PHOENIX && player.getQuestStage(this.getQuestID()) >= FIXED_GOLEM) {
			final boolean successful = Misc.random(player.getSkill().getLevel()[Skill.THIEVING]) > Misc.random(25);
			player.setStopPacket(true);
			player.getUpdateFlags().sendAnimation(881);
			player.getActionSender().sendMessage("You attemt to grab some Phoenix tail... feathers.");
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (successful) {
						if (!player.getInventory().ownsItem(PHOENIX_FEATHER)) {
							player.getActionSender().sendMessage("You manage to pick the " + npc.getDefinition().getName().toLowerCase() + "'s tail feather.");
							player.getInventory().addItemOrDrop(new Item(PHOENIX_FEATHER));
						} else {
							player.getActionSender().sendMessage("You already have the Phoenix's tail feather.");
						}
					} else {
						npc.getUpdateFlags().sendForceMessage("Get your hands off my ass.");
					}
					container.stop();
				}

				@Override
				public void stop() {
					player.setStopPacket(false);
				}
			}, 3);
		}
		if (npc.getNpcId() == CURATOR && player.getQuestStage(this.getQuestID()) >= SPEAK_WITH_CURATOR && player.getQuestStage(this.getQuestID()) < QUEST_COMPLETE) {
			final boolean successful = Misc.random(player.getSkill().getLevel()[Skill.THIEVING]) > Misc.random(25);
			player.setStopPacket(true);
			player.getUpdateFlags().sendAnimation(881);
			player.getActionSender().sendMessage("You attemt to pick the Curator's pockets.");
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (successful) {
						if (!player.getInventory().ownsItem(DISPLAY_CABINET_KEY)) {
							player.getActionSender().sendMessage("You manage to pick the " + npc.getDefinition().getName().toLowerCase() + "'s pockets.");
							player.getInventory().addItemOrDrop(new Item(DISPLAY_CABINET_KEY));
						} else {
							player.getActionSender().sendMessage("You already have the key.");
						}
					} else {
						npc.getUpdateFlags().sendForceMessage("I know what you're up to!");
					}
					container.stop();
				}

				@Override
				public void stop() {
					player.setStopPacket(false);
				}
			}, 3);
		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x,
		int y) {
		switch (object) {
			case GROUND_SHROOMS:
				player.getUpdateFlags().sendAnimation(832);
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("You find a banana, how strange.");
						player.getInventory().addItem(new Item(BLACK_MUSHROOM));
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 3);
				return true;
			case STAIRS_DOWN:
				player.fadeTeleport(INSIDE_RUINS);
				return true;
			case STAIRS_UP:
				player.fadeTeleport(OUTSIDE_RUINS);
				return true;
			case PORTAL:
				player.fadeTeleport(OUTSIDE_THRONEROOM);
				return true;
			case DOOR_TO_HELL:
				if (player.getQuestStage(this.getQuestID()) >= OPEN_HELL_DOOR) {
					player.getActionSender().sendMessage("You step into the portal...");
					player.fadeTeleport(INNER_DEMON);
					if (!player.getQuestVars().enteredHell) {
						player.getQuestVars().enteredHell = true;
						player.setQuestStage(this.getQuestID(), ENTER_HELL);
						player.getActionSender().sendMessage("This room is dominated by a colossal horned skeleton!");
					}
				}
				return true;
			case BOOKCASE:
				if (player.getQuestStage(this.getQuestID()) >= SPOKE_WITH_ELISSA) {
					if (!player.getInventory().ownsItem(VARMENS_NOTES)) {
						player.getDialogue().sendGiveItemNpc("You find Varmen's Expedition Notes.", new Item(VARMENS_NOTES));
						player.getInventory().addItemOrDrop(new Item(VARMENS_NOTES));
					}
				}
				return true;
			case ALCOVE_1:
				player.getActionSender().sendMessage("You turn the statuette in the alcove.");
				player.getQuestVars().setStatueStates(0, player.getQuestVars().getStatueStates()[0] == 1 ? 0 : 1);
				if (player.getQuestVars().getStatueStates()[0] == 0) {
					player.getQuestVars().statuesFacingCorrectly[0] = false;
				} else {
					player.getQuestVars().statuesFacingCorrectly[0] = true;
				}
				assessConfigs(player);
				return true;
			case ALCOVE_2:
				player.getActionSender().sendMessage("You turn the statuette in the alcove.");
				player.getQuestVars().setStatueStates(1, player.getQuestVars().getStatueStates()[1] == 1 ? 0 : 1);
				if (player.getQuestVars().getStatueStates()[1] == 0) {
					player.getQuestVars().statuesFacingCorrectly[1] = false;
				} else {
					player.getQuestVars().statuesFacingCorrectly[1] = true;
				}
				assessConfigs(player);
				return true;
			case ALCOVE_3:
				player.getActionSender().sendMessage("You turn the statuette in the alcove.");
				player.getQuestVars().setStatueStates(2, player.getQuestVars().getStatueStates()[2] == 1 ? 0 : 1);
				if (player.getQuestVars().getStatueStates()[2] == 0) {
					player.getQuestVars().statuesFacingCorrectly[2] = true;
				} else {
					player.getQuestVars().statuesFacingCorrectly[2] = false;
				}
				assessConfigs(player);
				return true;
			case ALCOVE_4:
				player.getActionSender().sendMessage("You turn the statuette in the alcove.");
				player.getQuestVars().setStatueStates(3, player.getQuestVars().getStatueStates()[3] == 1 ? 2 : 1);
				if (player.getQuestVars().getStatueStates()[3] == 1) {
					player.getQuestVars().statuesFacingCorrectly[3] = false;
				} else if (player.getQuestVars().getStatueStates()[3] == 2) {
					player.getQuestVars().statuesFacingCorrectly[3] = true;
				}
				assessConfigs(player);
				return true;
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object,
		final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(final Player player, final int id, int chatId,
		int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) {
			case GOLEM:
			case GOLEM_DAMAGED:
			case GOLEM_BROKEN:
				switch (player.getQuestStage(this.getQuestID())) {
					case 0:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Damage... Severe... task... incomplete...", CONTENT);
								return true;
							case 2:
								d.sendOption("Shall I try to repair you?", "I'm not going to find a conversation here!");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								if (optionId == 2) {
									d.endDialogue();
								}
								return true;
							case 4:
								d.sendNpcChat("Repairs... Needed...", CONTENT);
								return true;
							case 5:
								if (player.getInventory().playerHasItem(new Item(SOFT_CLAY))) {
									player.getQuestVars().clayAddedToGolem++;
									player.getInventory().removeItem(new Item(SOFT_CLAY));
									if (player.getQuestVars().clayAddedToGolem == 1) {
										QuestHandler.startQuest(player, this.getQuestID());
									}
									d.endDialogue();
								} else {
									d.sendPlayerChat("I don't have any clay though...", CONTENT);
									d.endDialogue();
								}
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								switch (player.getQuestVars().clayAddedToGolem) {
									case 1:
										d.sendNpcChat("Repairs... Needed...", CONTENT);
										return true;
									case 2:
										d.sendNpcChat("Damage... Severe...", CONTENT);
										return true;
									case 3:
										d.sendStatement("The golem is nearly whole again.");
										return true;
								}
								return false;
							case 2:
								if (player.getInventory().playerHasItem(new Item(SOFT_CLAY))) {
									player.getQuestVars().clayAddedToGolem++;
									player.getInventory().removeItem(new Item(SOFT_CLAY));
									if (player.getQuestVars().clayAddedToGolem < 4) {
										d.sendStatement("You apply some soft clay to mend the golem.");
										if (player.getQuestVars().clayAddedToGolem == 2) {
											player.reloadRegion();
										}
										d.endDialogue();
									} else {
										d.sendNpcChat("Damage repaired.", CONTENT);
										player.setQuestStage(this.getQuestID(), FIXED_GOLEM);
										player.reloadRegion();
									}
								}
								d.endDialogue();
								return true;
						}
						return false;
					case FIXED_GOLEM:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank you, my body has been repaired.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Now I must complete my task by defeating the great", "enemy.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("What enemy?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("A great demon. It broke through from its dimension to", "attack the city.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("The golem army was created to fight it. Many were", "destroyed, but we drove the demon back!", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("The demon is still wounded. you must open the portal", "so that I can strike the final blow and complete my", "task.", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("My task is incomplete. You must open the portal so I", "can defeat the great demon.", CONTENT);
								return true;
							case 8:
								d.sendOption("How do I open the portal?", "What makes you think you can defeat the demon?", "I'll get right on it.");
								return true;
							case 9:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								switch (optionId) {
									case 2:
										d.setNextChatId(12);
										break;
									case 3:
										d.endDialogue();
										return true;
								}
								return true;
							case 10:
								d.sendNpcChat("The four statuettes in the temple must be turned to the", "correct pattern.", CONTENT);
								return true;
							case 11:
								d.sendNpcChat("I do not know the pattern. Golems are not permitted to", "open the portal.", CONTENT);
								d.endDialogue();
								return true;
							case 12:
								d.sendNpcChat("If not I, then who else? No living being can destroy", "the demon. That is why the golems were created in the", "first place.", CONTENT);
								return true;
							case 13:
								d.sendNpcChat("Now that I am repaired, I will be able to destroy it", "easily!", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case ENTER_HELL:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("My task is incomplete. You must open the portal so", "I can defeat the great demon.", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("It's okay, the demon is dead!", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("The demon must be defeated...", CONTENT);
								return true;
							case 4:
								d.sendPlayerChat("No, you don't understand. I saw the demon's skeleton.", "It must have died from its wounds.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("Demon must be defeated! Task incomplete.", CONTENT);
								player.setQuestStage(this.getQuestID(), SPEAK_TO_GOLEM);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case ELISSA:
				if (player.getQuestStage(this.getQuestID()) >= READ_LETTER && player.getQuestStage(this.getQuestID()) < QUEST_COMPLETE) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Hello there.", CONTENT);
							return true;
						case 2:
							d.sendPlayerChat("I found a letter in the desert with your name on it.", CONTENT);
							return true;
						case 3:
							d.sendNpcChat("Ah, so you've found the ruines of Uzer.", CONTENT);
							return true;
						case 4:
							d.sendNpcChat("I wrote that letter to my late husband when he was", "exploring there.", CONTENT);
							return true;
						case 5:
							d.sendNpcChat("That was a great city as well, but the museum could", "only fund one excavation and this one was closer to", "home.", CONTENT);
							return true;
						case 6:
							d.sendNpcChat("If you're interested in his expedition, the notes he made", "are in the library in the Exam Centre.", CONTENT);
							player.setQuestStage(this.getQuestID(), SPOKE_WITH_ELISSA);
							d.endDialogue();
							return true;
					}
				}
				return false;
			case CURATOR:
				if (player.getQuestStage(this.getQuestID()) >= READ_NOTES && player.getQuestStage(this.getQuestID()) < QUEST_COMPLETE) {
					switch (d.getChatId()) {
						case 1:
							d.sendNpcChat("Welcome to the museum of Varrock.", CONTENT);
							return true;
						case 2:
							d.sendOption("Have any interesting news?", "Do you know where I could find any treasure?", "I'm looking for a statuette recovered from the city of Uzer.");
							return true;
						case 3:
							d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
							switch (optionId) {
								case 2:
									d.setNextChatId(5);
									break;
								case 3:
									d.setNextChatId(7);
									break;
							}
							return true;
						case 4:
							d.sendNpcChat("Did you know Pickles isn't actually a vegetable or a frog?", "He's not even green!", ANGRY_1);
							d.endDialogue();
							return true;
						case 5:
							d.sendNpcChat("I hear the desert south of Al Kharid is a veritable trove", "of unexplored mysteries.", CONTENT);
							return true;
						case 6:
							d.sendNpcChat("Maybe you could find something of value there? If you do", "be sure to bring it to me here at the museum!", LAUGHING);
							d.endDialogue();
							return true;
						case 7:
							d.sendNpcChat("Ah yes, a very impressive artifact. The people of that", "city were excellent sculptors.", CONTENT);
							return true;
						case 8:
							d.sendNpcChat("It's in the display case upstairs.", CONTENT);
							return true;
						case 9:
							d.sendPlayerChat("No, I need to take it away with me.", CONTENT);
							return true;
						case 10:
							d.sendNpcChat("What do you want it for?", CONTENT);
							player.setQuestStage(this.getQuestID(), SPEAK_WITH_CURATOR);
							return true;
						case 11:
							d.sendOption("I want to open the portal to the lair of an elder demon.", "Well I, er, just need it.");
							return true;
						case 12:
							d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
							if (optionId == 2) {
								d.setNextChatId(14);
							}
							return true;
						case 13:
							d.sendNpcChat("Good heavens! I'd never let you do such a dangerous", "thing.", ANGRY_1);
							d.endDialogue();
							return true;
						case 14:
							d.sendNpcChat("Well you can't have it! This museum never let's go of", "its treasures.", ANGRY_1);
							d.endDialogue();
							return true;
					}
				}
				return false;
			case 125501:
				d.setLastNpcTalk(GOLEM);
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("New Instructions...", "Updating program...", CONTENT);
						return true;
					case 2:
						d.sendNpcChat("Task Complete!", HAPPY);
						return true;
					case 3:
						d.sendNpcChat("Thank you, now my mind is at rest.", CONTENT);
						return true;
					case 4:
						d.dontCloseInterface();
						QuestHandler.completeQuest(player, this.getQuestID());
						return true;
				}
				return false;
		}
		return false;
	}

}
