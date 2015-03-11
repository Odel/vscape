package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.objects.GameObject;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import java.util.Random;

public class WaterfallQuest implements Quest {

	//Quest stages
	public static final int QUEST_STARTED = 1;
	public static final int SPOKE_TO_HUDON = 2;
	public static final int RESEARCH = 3;
	public static final int PEBBLE = 4;
	public static final int AMULET_AND_URN = 5;
	public static final int RUNES_ON_STAND = 6;
	public static final int AMULET_ON_STATUE = 7;
	public static final int URN_ON_CHALICE = 8;
	public static final int QUEST_COMPLETE = 9;
	//Items
	public static final int AIR_RUNE = 556;
	public static final int WATER_RUNE = 555;
	public static final int EARTH_RUNE = 557;
	public static final int BOOK_ON_BAXTORIAN = 292;
	public static final int GLARIAL_PEBBLE = 294;
	public static final int GLARIAL_AMULET = 295;
	public static final int GLARIAL_URN = 296;
	public static final int GLARIAL_URN_EMPTY = 297;
	public static final int KEY_G = 293;
	public static final int KEY_W = 298;
	public static final int DIAMOND = 1601;
	public static final int GOLD_BAR = 2357;
	public static final int MITHRIL_SEEDS = 299;
	//Positions
	public static final Position CRASH_LANDING = new Position(2512, 3481, 0);
	public static final Position DROP_DOWN = new Position(2511, 3463, 0);
	public static final Position WASHED_UP = new Position(2530, 3445, 0);
	public static final Position GOLRIE_CAVE = new Position(2532, 9555, 0);
	public static final Position GLARIAL_TOMB_ENTRANCE = new Position(2555, 9844, 0);
	public static final Position WATERFALL_ENTRANCE = new Position(2575, 9861, 0);
	public static final Position WATERFALL_RISEN_FLOOR = new Position(2603, 9914, 0);
	//Interfaces
	public static final int DOOR_INTERFACE = 10116;
	//Npcs
	public static final int ALMERA = 304;
	public static final int HUDON = 305;
	public static final int HADLEY = 302;
	public static final int GOLRIE = 306;
	//Objects
	public static final int LOG_RAFT = 1987;
	public static final int SWIM_RIVER = 10283;
	public static final int WATERFALL_ROCK = 1996;
	public static final int WATERFALL_DEAD_TREE = 2020;
	public static final int WATERFALL_BARREL = 2022;
	public static final int WATERFALL_LEDGE_DOOR = 2010;
	public static final int TOURIST_BOOKCASE = 1989;
	public static final int GOLRIE_LADDER = 5250;
	public static final int GOLRIE_CRATE = 1990;
	public static final int GOLRIE_DOOR = 1991;
	public static final int GLARIAL_TOMBSTONE = 1992;
	public static final int GLARIAL_TOMB = 1993;
	public static final int GLARIAL_CHEST_CLOSED = 1994;
	public static final int GLARIAL_CHEST_OPEN = 1995;
	public static final int WATERFALL_DUNGEON_DOOR_ENTRANCE = 2000;
	public static final int WATERFALL_DUNGEON_CRATE = 366;
	public static final int WATERFALL_DUNGEON_DOOR = 2002;
	public static final int WATERFALL_DUNGEON_PILLAR = 2004;
	public static final int WATERFALL_FIRST_PILLAR[] = {2562, 9910};
	public static final int WATERFALL_SECOND_PILLAR[] = {2562, 9912};
	public static final int WATERFALL_THIRD_PILLAR[] = {2562, 9914};
	public static final int WATERFALL_FOURTH_PILLAR[] = {2569, 9914};
	public static final int WATERFALL_FIFTH_PILLAR[] = {2569, 9912};
	public static final int WATERFALL_SIXTH_PILLAR[] = {2569, 9910};
	public static final int PILLAR_ARRAY[][] = {WATERFALL_FIRST_PILLAR, WATERFALL_SECOND_PILLAR, WATERFALL_THIRD_PILLAR, WATERFALL_FOURTH_PILLAR, WATERFALL_FIFTH_PILLAR, WATERFALL_SIXTH_PILLAR};
	public static final int STATUE_OF_GLARIAL = 2006;
	public static final int STATUE_OF_BAXTORIAN = 2005;
	public static final int CHALICE_OF_ETERNITY = 2014;

	public static final int PLACE_ANIM = 832;

	public int dialogueStage = 0; //Ignore

	private int reward[][] = { //Items in the form of {Id, #},
		{DIAMOND, 2},
		{GOLD_BAR, 2},
		{MITHRIL_SEEDS, 40}};

	private int expReward[][] = { //Exp in the form of {Skill.AGILITY, x},
		{Skill.STRENGTH, 13750},
		{Skill.ATTACK, 13750},}; //The 2.25 multiplier is added later, use vanilla values

	private static final int questPointReward = 1;

	public int getQuestID() { //Don't change
		return 31;
	}

	public String getQuestName() { //Don't change
		return "Waterfall Quest";
	}

	public String getQuestSaveName() { //Don't change
		return "waterfallquest";
	}

	public boolean canDoQuest(Player player) { //Use to check for strict auxiliary quest requirements
		return true;
	}

	public void getReward(Player player) { //Don't change
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		for (int[] expRewards : expReward) {
			player.getSkill().addExp(expRewards[0], (expRewards[1]));
		}
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	//End of quest reward scroll interface, tweak what's necessary
	public void completeQuest(Player player) {
		//If writing in exp, be sure to express it manually as 2.25 the vanilla reward
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Points", 12150);
		player.getActionSender().sendString("30937.5 Strength XP", 12151);
		player.getActionSender().sendString("30927.5 Attack XP", 12152);
		player.getActionSender().sendString("2 Diamonds", 12153);
		player.getActionSender().sendString("2 Gold Bars", 12154);
		player.getActionSender().sendString("40 Mithril Seeds", 12155);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		//Updates the players quest list to show the green complete quest
		player.getActionSender().sendString("@gre@" + getQuestName(), 7361);
	}

    //Here we send the quest log, with the text and then the line number in sendString(string, line number)
	//The line number is according to the interface, just add to it for the next line
	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case QUEST_STARTED:
				player.getActionSender().sendString("@str@" + "Talk to @dre@Almera @bla@ near the @dre@Baxtorian Falls @bla@to begin.", 8147);

				player.getActionSender().sendString("Almera has asked you to find and talk to her son, Hudon.", 8149);
				player.getActionSender().sendString("She said you could use the raft out back.", 8150);
				break;
			case SPOKE_TO_HUDON:
				player.getActionSender().sendString("@str@" + "Talk to Almera near the Baxtorian Falls..", 8147);
				player.getActionSender().sendString("@str@" + "I found Hudon. He refused to leave the falls.", 8149);

				player.getActionSender().sendString("Hudon mentioned a treasure, I should look into it.", 8151);
				player.getActionSender().sendString("There is a tourist center nearby, maybe I should go.", 8152);
				break;
			case RESEARCH:
				player.getActionSender().sendString("@str@" + "Talk to Almera near the Baxtorian Falls.", 8147);
				player.getActionSender().sendString("@str@" + "I found Hudon. He refused to leave the falls.", 8149);
				player.getActionSender().sendString("@str@" + "I went to the tourist center for info on the treasure.", 8151);
				player.getActionSender().sendString("@str@" + "I met the tourist guide and read a boring book.", 8153);

				player.getActionSender().sendString("There are two places I should investigate:", 8155);
				player.getActionSender().sendString("@dre@Golrie@bla@, under the @dre@Tree Gnome Village@bla@.", 8156);
				player.getActionSender().sendString("@dre@Glarial's tomb@bla@, north-east of the @dre@tourist center.", 8157);
				break;
			case PEBBLE:
				player.getActionSender().sendString("@str@" + "Talk to Almera near the Baxtorian Falls.", 8147);
				player.getActionSender().sendString("@str@" + "I found Hudon. He refused to leave the falls.", 8149);
				player.getActionSender().sendString("@str@" + "I went to the tourist center for info on the treasure.", 8151);
				player.getActionSender().sendString("@str@" + "I met the tourist guide and read a boring book.", 8153);

				player.getActionSender().sendString("There are two places I should investigate:", 8155);
				player.getActionSender().sendString("@str@" + "Golrie gave me Glarial's Pebble.", 8156);
				player.getActionSender().sendString("@dre@Glarial's tomb@bla@, north-east of the @dre@tourist center.", 8157);
				break;
			case AMULET_AND_URN:
			case RUNES_ON_STAND:
			case AMULET_ON_STATUE:
				player.getActionSender().sendString("@str@" + "Talk to Almera near the Baxtorian Falls.", 8147);
				player.getActionSender().sendString("@str@" + "I found Hudon. He refused to leave the falls.", 8149);
				player.getActionSender().sendString("@str@" + "I went to the tourist center for info on the treasure.", 8151);
				player.getActionSender().sendString("@str@" + "I met the tourist guide and read a boring book.", 8153);
				player.getActionSender().sendString("@str@" + "There are two places I should investigate:", 8155);
				player.getActionSender().sendString("@str@" + "Golrie gave me Glarial's Pebble.", 8156);
				player.getActionSender().sendString("@str@" + "Glarial's tomb, where I found her amulet and urn.", 8157);

				player.getActionSender().sendString("It's time I head to the Waterfall and find", 8159);
				player.getActionSender().sendString("the fabled treasure.", 8160);
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString("@str@" + "Talk to Almera near the Baxtorian Falls.", 8147);
				player.getActionSender().sendString("@str@" + "I found Hudon. He refused to leave the falls.", 8149);
				player.getActionSender().sendString("@str@" + "I went to the tourist center for info on the treasure.", 8151);
				player.getActionSender().sendString("@str@" + "I met the tourist guide and read a boring book.", 8153);
				player.getActionSender().sendString("@str@" + "There are two places I should investigate:", 8155);
				player.getActionSender().sendString("@str@" + "Golrie gave me Glarial's Pebble.", 8156);
				player.getActionSender().sendString("@str@" + "Glarial's tomb, where I found her amulet and urn.", 8157);
				player.getActionSender().sendString("@str@" + "I solved the mystery and got the treasure!", 8159);
				player.getActionSender().sendString("@red@" + "You have completed this quest!", 8161);
				break;
			default:
				player.getActionSender().sendString("Talk to @dre@Almera @bla@ near the @dre@Baxtorian Falls @bla@to begin.", 8147);
				break;
		}
	}

	public void sendQuestInterface(Player player) { //Don't change
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) { //Don't change
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7350);
	}

	public boolean questCompleted(Player player) //Don't change
	{
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= QUEST_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) { //Don't change
		int questStage = player.getQuestStage(getQuestID());
		if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
			player.getActionSender().sendString("@yel@" + getQuestName(), 7350); //These numbers correspond to the index of the quest in
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7350); //the quest list on the quest tab. I've listed them all
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7350); //in the player class, just search and then replace
			//also add the name while you're there @red@Quest Name
		}
	}

	public int getQuestPoints() { //Don't change
		return questPointReward;
	}

	public void showInterface(Player player) { //Don't change
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public void washOut(final Player player) {
		player.fadeTeleport(WASHED_UP);
		player.setQuestStage(31, AMULET_AND_URN);
		player.getActionSender().sendMessage("You've been washed out of the dungeon!");
		for (int i = 0; i < 6; i++) {
			player.getQuestVars().waterfallPillars[i][0] = false;
			player.getQuestVars().waterfallPillars[i][1] = false;
			player.getQuestVars().waterfallPillars[i][2] = false;
		}
	}

	public static void handleRopePull(final Player player) {
		if (player.getPosition().getX() == 2512 && player.getPosition().getY() == 3476) {
			player.getUpdateFlags().sendAnimation(774);
			player.getActionSender().sendMessage("You throw your rope around the rock...");
			player.getInventory().removeItem(new Item(954));
			final CacheObject g = ObjectLoader.object(2512, 3468, 0);
			new GameObject(1997, 2512, 3468, 0, g.getRotation(), g.getType(), WATERFALL_ROCK, 23);
			player.setStopPacket(true);
			player.setAppearanceUpdateRequired(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					player.setWalkAnim(776);
					player.setStandAnim(777);
					player.setAppearanceUpdateRequired(true);
					if (player.getPosition().getX() == 2512 && player.getPosition().getY() == 3469) {
						player.getActionSender().sendMessage("You carefully untie your rope.");
						player.setStandAnim(773);
						player.setWalkAnim(772);
						player.setAppearanceUpdateRequired(true);
						player.getInventory().addItem(new Item(954));
						player.getActionSender().walkToNoPacket(1, 0, true);
					} else if (player.getPosition().getX() == 2513 && player.getPosition().getY() == 3469) {
						player.getActionSender().walkToNoPacket(0, -1, true);
						player.getActionSender().sendMessage("You safely traversed the river.");
						b.stop();
					} else {
						player.getActionSender().walkToNoPacket(0, -1, true);
					}
				}

				@Override
				public void stop() {
					player.setStopPacket(false);
					player.setStandAnim(-1);
					player.setWalkAnim(-1);
					player.setAppearanceUpdateRequired(true);
				}
			}, 3);
		}
	}

	public int getPillarNumberForCoords(int x, int y) {
		int i = 0;
		for (int[] coords : PILLAR_ARRAY) {
			if (coords[0] == x && coords[1] == y) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public boolean hasPlacedRunes(Player player, int index) {
		return player.getQuestVars().waterfallPillars[index][0] && player.getQuestVars().waterfallPillars[index][1] && player.getQuestVars().waterfallPillars[index][2];
	}

	public boolean hasCompletedPillars(Player player) {
		return hasPlacedRunes(player, 0) && hasPlacedRunes(player, 1) && hasPlacedRunes(player, 2) && hasPlacedRunes(player, 3) && hasPlacedRunes(player, 4) && hasPlacedRunes(player, 5);
	}

	public boolean itemHandling(final Player player, int itemId) { //Inherited, will work without a call to it
		switch (itemId) {
			case BOOK_ON_BAXTORIAN:
				player.getDialogue().sendPlayerChat("Hmm... Fascinating. Maybe one day I'll learn to read.", SAD);
				player.getDialogue().endDialogue();
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) { //Inherited, will work without a call to it
		return false;
	}

	public static boolean doMiscItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case WATERFALL_ROCK:
				if (item == 954) {
					if (player.getPosition().getX() == 2512 && player.getPosition().getY() == 3476) {
						handleRopePull(player);
						return true;
					} else {
						player.walkTo(new Position(2512, 3476, 0), true);
						return true;
					}
				}
				return false;
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) { //Inherited, will work without a call to it
		switch (object) {
			case WATERFALL_ROCK:
				if (item == 954) {
					if (player.getPosition().getX() == 2512 && player.getPosition().getY() == 3476) {
						handleRopePull(player);
						return true;
					} else {
						try {
							player.walkTo(new Position(2512, 3476, 0), true);
							handleRopePull(player);
						} catch (Exception e) {

						}
						return true;
					}
				}
				return false;
			case GLARIAL_TOMBSTONE:
				if (item == GLARIAL_PEBBLE) {
					if (player.hasCombatEquipment() || player.hasRunes()) {
						player.getActionSender().sendMessage("A strong, yet peaceful force prevents you from touching the tomb.");
						return true;
					} else {
						player.getActionSender().sendMessage("You hear a loud grinding resound from within the tomb...");
						player.setStopPacket(true);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								player.getActionSender().sendMessage("The tombstone moves, revealing an entrance.  You climb down.");
								player.fadeTeleport(GLARIAL_TOMB_ENTRANCE);
								player.setStopPacket(false);
							}
						}, 1);
						return true;
					}
				}
				return false;
			case CHALICE_OF_ETERNITY:
				if (item == GLARIAL_URN) {
					player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
					player.getActionSender().sendMessage("You carefully pour the ashes into the Chalice.");
					player.setQuestStage(31, URN_ON_CHALICE);
					player.getInventory().removeItem(new Item(GLARIAL_URN));
					player.getInventory().addItem(new Item(GLARIAL_URN_EMPTY));
					return true;
				}
				return false;
			case STATUE_OF_BAXTORIAN:
				if ((player.getQuestStage(31) == RUNES_ON_STAND || player.getQuestStage(31) == AMULET_AND_URN) && item == GLARIAL_AMULET) {
					washOut(player);
					return true;
				}
				return false;
			case STATUE_OF_GLARIAL:
				if (player.getQuestStage(31) == RUNES_ON_STAND) {
					if (item == GLARIAL_AMULET) {
						player.setStopPacket(true);
						player.getActionSender().sendMessage("You place the Amulet on the statue of Glarial...");
						player.setQuestStage(31, AMULET_ON_STATUE);
						player.getActionSender().sendMessage("You hear a low rumble...");
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								player.getActionSender().sendMessage("And the ground raises to a hill beneath you!");
								player.teleport(WATERFALL_RISEN_FLOOR);
								player.setStopPacket(false);
							}
						}, 2);
						return true;
					}
					return false;
				}
				return false;
			case WATERFALL_DUNGEON_PILLAR:
				int pillarIndex = getPillarNumberForCoords(player.getClickX(), player.getClickY());
				if (player.getQuestStage(31) == AMULET_AND_URN && pillarIndex != -1) {
					if (item == AIR_RUNE && !player.getQuestVars().waterfallPillars[pillarIndex][0]) {
						player.getQuestVars().waterfallPillars[pillarIndex][0] = true;
						player.getActionSender().sendMessage("The Air Rune is absorbed into the pillar.");
						player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
						player.getInventory().removeItem(new Item(AIR_RUNE));
						if (hasCompletedPillars(player) && player.getQuestStage(31) == AMULET_AND_URN) {
							player.setQuestStage(31, RUNES_ON_STAND);
						}
						if (hasPlacedRunes(player, pillarIndex)) {
							player.getActionSender().sendMessage("You feel a slight change in the pillar.");
							return true;
						} else {
							return true;
						}
					} else if (item == WATER_RUNE && !player.getQuestVars().waterfallPillars[pillarIndex][1]) {
						player.getQuestVars().waterfallPillars[pillarIndex][1] = true;
						player.getActionSender().sendMessage("The Water Rune is absorbed into the pillar.");
						player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
						player.getInventory().removeItem(new Item(WATER_RUNE));
						if (hasCompletedPillars(player) && player.getQuestStage(31) == AMULET_AND_URN) {
							player.setQuestStage(31, RUNES_ON_STAND);
						}
						if (hasPlacedRunes(player, pillarIndex)) {
							player.getActionSender().sendMessage("You feel a slight change in the pillar.");
							return true;
						} else {
							return true;
						}
					} else if (item == EARTH_RUNE && !player.getQuestVars().waterfallPillars[pillarIndex][2]) {
						player.getQuestVars().waterfallPillars[pillarIndex][2] = true;
						player.getActionSender().sendMessage("The Earth Rune is absorbed into the pillar.");
						player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
						player.getInventory().removeItem(new Item(EARTH_RUNE));
						if (hasCompletedPillars(player) && player.getQuestStage(31) == AMULET_AND_URN) {
							player.setQuestStage(31, RUNES_ON_STAND);
						}
						if (hasPlacedRunes(player, pillarIndex)) {
							player.getActionSender().sendMessage("You feel a slight change in the pillar.");
							return true;
						} else {
							return true;
						}
					}
					return false;
				}
				return false;
		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) { //Inherited, will work without a call to it
		switch (object) {
			case WATERFALL_BARREL:
				if (player.getQuestStage(31) >= 1) {
					player.getActionSender().sendMessage("You climb in the barrel and rock yourself over the edge...");
					player.fadeTeleport(WASHED_UP);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.getActionSender().sendMessage("You end up on the shore of the lake.");
						}
					}, 5);
					return true;
				}
				return false;
			case LOG_RAFT:
				if (player.getQuestStage(31) >= 1) {
					player.getActionSender().sendMessage("You board the raft and push off downstream...");
					player.fadeTeleport(CRASH_LANDING);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.getActionSender().sendMessage("You crash into a mound of land.");
						}
					}, 5);
					return true;
				}
				return false;
			case WATERFALL_ROCK:
			case SWIM_RIVER:
				if (player.getPosition().getX() == 2512 & player.getPosition().getY() == 3476) {
					player.getActionSender().walkToNoPacket(0, -1, true);
					player.setStandAnim(773);
					player.getActionSender().sendMessage("You begin to swim across the river...");
					player.setStopPacket(true);
					player.setAppearanceUpdateRequired(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							player.setWalkAnim(772);
							player.setAppearanceUpdateRequired(true);
							if (player.getPosition().getX() == 2512 && player.getPosition().getY() == 3471) {
								if (33 >= (new Random().nextDouble() * 100)) {
									player.getActionSender().walkToNoPacket(1, -1, true);
									player.getActionSender().sendMessage("You hold strong against the current.");
								} else {
									player.getActionSender().sendMessage("The current washes you down the waterfall...");
									player.fadeTeleport2(WASHED_UP);
									player.tempBoolean = true;
									b.stop();
								}
							} else if (player.getPosition().getX() == 2513 && player.getPosition().getY() == 3469) {
								player.getActionSender().walkToNoPacket(0, -1, true);
								player.getActionSender().sendMessage("You manage to traverse the river.");
								b.stop();
							} else {
								player.getActionSender().walkToNoPacket(0, -1, true);
							}
						}

						@Override
						public void stop() {
							if (player.tempBoolean) {
								player.delayHit(5, HitType.NORMAL, 5);
								player.tempBoolean = false;
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										player.setStandAnim(-1);
										player.setWalkAnim(-1);
										player.setAppearanceUpdateRequired(true);
									}
								}, 3);
							} else {
								player.setStopPacket(false);
								player.setStandAnim(-1);
								player.setWalkAnim(-1);
								player.setAppearanceUpdateRequired(true);
							}
						}
					}, 3);
					return true;
				}
				return true;
			case WATERFALL_DEAD_TREE:
				player.teleport(DROP_DOWN);
				player.getActionSender().sendMessage("You climb down the ledge, using the tree as support.");
				return true;
			case TOURIST_BOOKCASE:
				if (player.getQuestStage(31) >= 2 && !player.getInventory().ownsItem(BOOK_ON_BAXTORIAN)) {
					player.getActionSender().sendMessage("You search the bookcase and find the 'Book on Baxtorian'");
					player.getInventory().addItemOrDrop(new Item(BOOK_ON_BAXTORIAN));
					return true;
				}
				return false;
			case GOLRIE_LADDER:
				if (player.getQuestStage(31) >= 3) {
					Ladders.climbLadder(player, GOLRIE_CAVE);
					return true;
				} else {
					player.getActionSender().sendMessage("Looks scary down there!");
					return true;
				}
			case GOLRIE_CRATE:
				if (player.getQuestStage(31) >= 3 && !player.getInventory().ownsItem(KEY_G)) {
					player.getActionSender().sendMessage("You search the crate and find a large key!");
					player.getInventory().addItemOrDrop(new Item(KEY_G));
					return true;
				}
				return false;
			case GOLRIE_DOOR:
				if (player.getInventory().ownsItem(KEY_G)) {
					player.getActionSender().sendMessage("You open the door and walk through.");
					player.getActionSender().walkTo(0, player.getPosition().getY() < 9576 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				} else {
					player.getActionSender().sendMessage("The door is locked tight.");
					return true;
				}
			case GLARIAL_TOMBSTONE:
				player.getDialogue().sendPlayerChat("'Here lies Queen Glarial.'", "Hmm, there's a pebble-shaped chunk missing on", "the side of the tombstone. Strange.", CONTENT);
				return true;
			case GLARIAL_TOMB:
				if (!player.getInventory().ownsItem(GLARIAL_URN)) {
					player.getActionSender().sendMessage("You find an urn filled with Queen Glarial's Ashes.");
					player.getInventory().addItemOrDrop(new Item(GLARIAL_URN));
					if (player.getInventory().ownsItem(GLARIAL_AMULET) && player.getQuestStage(31) == PEBBLE) {
						player.setQuestStage(31, AMULET_AND_URN);
					}
					return true;
				}
				return false;
			case GLARIAL_CHEST_CLOSED:
				player.getActionSender().sendMessage("The chest creaks open.");
				player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
				final CacheObject g = ObjectLoader.object(2530, 9844, 0);
				new GameObject(GLARIAL_CHEST_OPEN, 2530, 9844, 0, g.getRotation(), g.getType(), GLARIAL_CHEST_CLOSED, 30);
				return true;
			case GLARIAL_CHEST_OPEN:
				if (!player.getInventory().ownsItem(GLARIAL_AMULET)) {
					player.getActionSender().sendMessage("You find an amulet that belonged to the late Queen.");
					player.getInventory().addItemOrDrop(new Item(GLARIAL_AMULET));
					if (player.getInventory().playerHasItem(GLARIAL_URN) && player.getQuestStage(31) == PEBBLE) {
						player.setQuestStage(31, AMULET_AND_URN);
					}
					return true;
				}
				return false;
			case WATERFALL_LEDGE_DOOR:
				if (player.getEquipment().getId(Constants.AMULET) == GLARIAL_AMULET) {
					player.teleport(WATERFALL_ENTRANCE);
					player.getActionSender().sendMessage("You step through the barrier, and into the waterfall.");
					return true;
				} else {
					player.getActionSender().sendMessage("A strong barrier prevents you from entering.");
					return true;
				}
			case WATERFALL_DUNGEON_DOOR_ENTRANCE:
				player.teleport(DROP_DOWN);
				return true;
			case WATERFALL_DUNGEON_CRATE:
				if (!player.getInventory().playerHasItem(KEY_W) & player.Area(2580, 2600, 9880, 9890)) {
					player.getActionSender().sendMessage("You search the crate and find a key!");
					player.getInventory().addItemOrDrop(new Item(KEY_W));
					return true;
				}
				return false;
			case WATERFALL_DUNGEON_DOOR:
				if (player.getPosition().getX() < 2571) {
					if (player.getInventory().playerHasItem(KEY_W)) {
						player.getActionSender().sendMessage("You open the door and walk through.");
						if (player.getPosition().getY() < 9896) {
							player.getActionSender().walkTo(0, player.getPosition().getY() < 9894 ? 1 : -1, true);
						} else {
							player.getActionSender().walkTo(0, player.getPosition().getY() < 9902 ? 1 : -1, true);
						}
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						return true;
					} else {
						player.getActionSender().sendMessage("The door is locked tight.");
						return true;
					}
				} else {
					player.getActionSender().sendMessage("The door is locked tight.");
					return true;
				}
			case CHALICE_OF_ETERNITY:
				if (player.getQuestStage(31) == URN_ON_CHALICE) {
					player.setStopPacket(true);
					player.fadeTeleport2(new Position(2566, 9911, 0));
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							QuestHandler.completeQuest(player, 31);
						}
					}, 4);
					return true;
				} else {
					washOut(player);
					return true;
				}
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

	public static boolean playerAskedAllThree(final Player player) {
		return player.getQuestVars().waterfallOption1 && player.getQuestVars().waterfallOption2 && player.getQuestVars().waterfallOption3;
	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) { //Inherited
		switch (id) { //Npc ID
			case ALMERA:
				switch (player.getQuestStage(31)) { //Dialogue per stage
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I happened to find your stubborn son. He says", "he is just fine where he is and doesn't", "need any help.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("If you ask me, he does need help, that little rat.", "He's stuck halfway down the river searching for", "some 'treasure'. No such thing exists. I promise you.", "Do not encourage him or look for the treasure.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Erm, alright. Thank you for finding him", "and letting me know he's okay. I'll", "have to go reprimand him, and get him to return home.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Remember, this 'treasure' by the Waterfall is a hoax.", "Don't listen to what he says.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Okay, I got that the first time, thank you.", Dialogues.ANNOYED);
								player.getDialogue().endDialogue();
						}
						return false;
					case 0: //Starting the quest
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello.", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Ah, hello there. Nice to see an outsider for a change,", "are you busy? I have a problem.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendOption("I'm afraid I'm in a rush.", "How can I help?");
								return true;
							case 4:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I'm afraid I'm in a rush.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("How can I help?", CONTENT);
										return true;
								}
								return false;
							case 5:
								player.getDialogue().sendNpcChat("It's my son Hudon, he's always getting into trouble, the", "boy's convinced there's hidden treasure in the river and", "I'm a bit worried about his safety. The poor lad", "can't even swim.", DISTRESSED);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("I could go and take a look for you if you like?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Would you? You are kind. You can use the small raft", "out back if you wish, do be careful, the current", "down-stream is very strong.", CONTENT);
								player.getDialogue().endDialogue();
								QuestHandler.startQuest(player, 31);
								return true;
						}
						return false;
				}
				return false;
			case HUDON:
				switch (player.getQuestStage(31)) {
					case SPOKE_TO_HUDON:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("I told you I'm fine alone!!", ANGRY_1);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello son, are you okay? You need help?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("It looks like you need the help.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Your mom sent me to find you.", "Snot nosed brat...", ANGRY_1);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Don't play nice with me, I know you're looking for the", "treasure too.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Where is this treasure you talk of?", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Just because I'm small doesn't mean I'm dumb! If I", "told you, you would take it all for yourself.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Maybe I could help.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("I'm fine alone.", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(31, SPOKE_TO_HUDON);
								return true;
						}
						return false;
				}
				return false;
			case HADLEY:
				switch (player.getQuestStage(31)) {
					case SPOKE_TO_HUDON:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello there.", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Are you on holiday? If so you've come to the right", "place. I'm Hadley the tourist guide, anything you need", "to know just ask me, I know of the most majestic", "wildlife and scenery in /v/scape.", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("People come from miles around to fish in the clear lakes", "or to wander the beautiful hill sides.", HAPPY);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("It is quite pretty.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Surely pretty is an understatement kind sir. Beautiful,", "amazing, or possibly life-changing would be more suitable", "wording. Have you seen the Baxtorian waterfall?", "Named after the Elf king who was buried beneath.", HAPPY);
								return true;
							case 6:
								if (player.getQuestStage(31) >= 6) {
									player.getDialogue().sendPlayerChat("I have seen the Waterfall, it is a", "marvel.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendOption("Can you tell me what happened to the elf king?", "Where else is worth visiting around here?", "Is there treasure under the waterfall?");
									return true;
								}
							case 7:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Can you tell me what happened to the elf king?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Where else is worth visiting around here?", CONTENT);
										player.getDialogue().setNextChatId(13);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("Is there treasure under the waterfall?", CONTENT);
										player.getDialogue().setNextChatId(19);
										return true;
								}
							case 8:
								player.getDialogue().sendNpcChat("There are many myths about Baxtorian. One popular", "story is this, after defending his kingdom against the", "invading dark forces from the west, Baxtorian returned", "to find his wife Glarial had been captured by the", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("enemy!", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("This destroyed Baxtorian, after years of searching he", "became a recluse. In the secret home he had made for", "Glarial under the waterfall, he never came out and it is", "told that only Glarial could enter.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("What happened to him?", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Oh, I don't know. I believe we have some pages on him", "upstairs in our archives. If you wish to look at them", "please be careful, they're all pretty delicate.", CONTENT);
								player.getDialogue().setNextChatId(25);
								player.getQuestVars().waterfallOption1 = true;
								return true;
							case 13:
								player.getDialogue().sendNpcChat("There is a lovely spot for a picnic on the hill to the", "north east, there lies a monument to the deceased elven", "Queen Glarial. It really is quite pretty.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Who was Queen Glarial?", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Baxtorian's wife, the only person who could also enter", "the waterfall. She was queen when this land was", "inhabited by elven kind. Glarial was kidnapped while", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("Baxtorian was away, but they eventually recovered her", "body and brought her home to rest.", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendPlayerChat("That's sad.", SAD);
								return true;
							case 18:
								player.getDialogue().sendNpcChat("True, I believe there's some information about her", "upstairs, if you look at them please be careful.", CONTENT);
								player.getQuestVars().waterfallOption2 = true;
								player.getDialogue().setNextChatId(25);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("Ha ha... Another treasure hunter. Well if there is, no", "one's been able to get to it. They've been searching that", "river for decades, all to no avail.", LAUGHING);
								player.getQuestVars().waterfallOption3 = true;
								player.getDialogue().setNextChatId(25);
								return true;
							case 20:
								player.getDialogue().sendNpcChat("Enjoy your visit.", CONTENT);
								player.getDialogue().endDialogue();
								if (player.getQuestStage(31) == SPOKE_TO_HUDON) {
									player.setQuestStage(31, RESEARCH);
								}
								return true;
							case 25:
								if (playerAskedAllThree(player)) {
									player.getDialogue().sendPlayerChat("Thank you, have a nice day.", CONTENT);
									player.getDialogue().setNextChatId(20);
								} else {
									player.getDialogue().sendOption("Can you tell me what happened to the elf king?", "Where else is worth visiting around here?", "Is there treasure under the waterfall?");
									player.getDialogue().setNextChatId(7);
								}
								return true;
						}
						return false;
				}
				return false;
			case GOLRIE:
				switch (player.getQuestStage(31)) {
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getQuestStage(31) >= 3) {
									player.getDialogue().sendPlayerChat("Hello, is your name Golrie?", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("I'm quite busy, please go away.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("That's me. I've been stuck in here for weeks, those", "goblins are trying to steal my family's heirlooms. My", "grand-dad gave me all sorts of old junk.", HAPPY);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Do you mind if I have a look?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("No, of course not.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Hmmm...", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendPlayerChat("Hmmm...", HAPPY);
								return true;
							case 16:
								if (!player.getInventory().ownsItem(GLARIAL_PEBBLE)) {
									player.getDialogue().sendPlayerChat("AHA!", "...", "Could I take this old pebble?", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Eh, there's nothing here I need.", "Thanks anyways.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 17:
								player.getDialogue().sendNpcChat("Oh that, yes have it, it's just some old elven junk I", "believe.", CONTENT);
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(GLARIAL_PEBBLE));
								if (player.getQuestStage(31) == RESEARCH) {
									player.setQuestStage(31, PEBBLE);
								}
								return true;
						}
						return false;
				}
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
