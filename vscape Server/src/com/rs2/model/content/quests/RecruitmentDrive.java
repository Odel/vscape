package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.GlobalVariables;
import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.World;
import com.rs2.model.objects.GameObject;
import com.rs2.model.content.dialogue.DialogueManager;

import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;

import com.rs2.util.Misc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.ShopManager;

public class RecruitmentDrive implements Quest {

	//Quest stages

	public static final int QUEST_STARTED = 1;
	public static final int TRIALS_STARTED = 2;
	public static final int FIRST_ROOM_COMPLETE = 3;
	public static final int SECOND_ROOM_COMPLETE = 4;
	public static final int THIRD_ROOM_COMPLETE = 5;
	public static final int FOURTH_ROOM_COMPLETE = 6;
	public static final int TRIALS_COMPLETE = 7;
	public static final int QUEST_COMPLETE = 8;

	//Items
	public static final int INITIATE_SALLET = 5574;
	public static final int INITIATE_HAUBERK = 5575;
	public static final int INITIATE_CUISSE = 5576;
	public static final int CUPRIC_SULFATE = 5577;
	public static final int ACETIC_ACID = 5578;
	public static final int GYPSUM = 5579;
	public static final int SODIUM_CHLORIDE = 5580;
	public static final int NITROUS_OXIDE = 5581;
	public static final int VIAL_OF_LIQUID = 5582;
	public static final int TIN_ORE_POWDER = 5583;
	public static final int CUPRIC_ORE_POWDER = 5584;
	public static final int BRONZE_KEY = 5585;
	public static final int METAL_SPADE = 5586;
	public static final int METAL_SPADE_NO_HANDLE = 5587;
	public static final int ALCHEMICAL_NOTES = 5588;
	public static final int MYSTERY_MIXTURE_CUPRIC_SULFATE = 5589;
	public static final int MYSTERY_MIXTURE_LUMPY = 5590;
	public static final int MYSTERY_MIXTURE_RUINED = 5591;
	public static final int EMPTY_TIN = 5592;
	public static final int TIN_WHITE_MIXTURE = 5593;
	public static final int TIN_KEY_IMPRESSION_EMPTY = 5594;
	public static final int TIN_KEY_IMPRESSION_STAGE_1 = 5595;
	public static final int TIN_KEY_IMPRESSION_STAGE_2 = 5596;
	public static final int TIN_KEY_IMPRESSION_STAGE_3 = 5597;
	public static final int TIN_WITH_KEY = 5598;
	public static final int TIN_STRANGE_CONCOCTION = 5599;
	public static final int TIN_WITH_VIAL_CONTENTS = 5600;
	public static final int CHISEL = 5601;
	public static final int BRONZE_WIRE = 5602;
	public static final int SHEARS = 5603;
	public static final int MAGNET = 5604;
	public static final int KNIFE = 5605;
	public static final int MAKEOVER_VOUCHER = 5606;
	public static final int GRAIN = 5607;
	public static final int FOX = 5608;
	public static final int CHICKEN = 5609;
	public static final int HOURGLASS = 5610;
	//Positions
	public static final Position FIRST_ROOM = new Position(2441, 4956, 0);
	public static final Position SECOND_ROOM = new Position(2456, 4964, 0);
	public static final Position THIRD_ROOM = new Position(2489, 4972, 0);
	public static final Position FOURTH_ROOM = new Position(2472, 4956, 0);
	public static final Position FIFTH_ROOM = new Position(2451, 4936, 0);
	//Interfaces
	public static final int COMBINATION_LOCK_INTERFACE = 671;
	//Npcs
	public static final int SIR_AMIK_VARZE = 608;
	public static final int SIR_SPISHYUS = 2282;
	public static final int LADY_TABLE = 2283;
	public static final int SIR_KUAM_FERENTSE = 2284;
	public static final int SIR_LEYE = 2285;
	public static final int SIR_TINLEY = 2286;
	public static final int SIR_REN_ITCHOOD = 2287;
	public static final int MISS_CHEEVERS = 2288;
	public static final int MISS_HYNN_TERPRETT = 2289;
	public static final int SIR_TIFFY_CASHIEN = 2290;

	//Objects
	public static final int EXIT_PORTAL = 7321;
	public static final int EXIT_PORTAL_2 = 7315;
	public static final int EXIT_PORTAL_3 = 7272;
	public static final int EXIT_PORTAL_4 = 7218;
	public static final int EXIT_PORTAL_5 = 7352;
	public static final int FIRST_ROOM_PORTAL = 7322;
	public static final int COMBINATION_LOCK_DOOR = 7323;
	public static final int SECOND_ROOM_DOOR = 7317;
	public static final int SECOND_ROOM_PORTAL = 7316;
	public static final int CHICKEN_OBJ = 7279;
	public static final int GRAIN_OBJ = 7282;
	public static final int FOX_OBJ = 7275;
	public static final int THIRD_ROOM_PORTAL = 7273;
	public static final int THIRD_ROOM_DOOR = 7274;
	public static final int FOURTH_ROOM_DOOR = 7320;
	public static final int FOURTH_ROOM_PORTAL = 7319;
	public static final int FIFTH_ROOM_DOOR = 7354;
	public static final int FIFTH_ROOM_PORTAL = 7353;

	public static String[] RIDDLE_ANSWERS = {"TIME", "MEAT", "LAST", "BITE", "FISH"};

	public int dialogueStage = 0;

	private int reward[][] = {
		{INITIATE_SALLET, 1},};
	private int expReward[][] = {
		{Skill.PRAYER, 1000},
		{Skill.HERBLORE, 1000},
		{Skill.AGILITY, 1000}
	};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 35;
	}

	public String getQuestName() {
		return "Recruitment Drive";
	}

	public String getQuestSaveName() {
		return "recruitment-drive";
	}

	public boolean canDoQuest(Player player) {
		return true;
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
		player.getActionSender().sendItemOnInterface(12145, 275, INITIATE_SALLET);
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("2250 Prayer, Herblore and", 12151);
		player.getActionSender().sendString("Agility XP", 12152);
		player.getActionSender().sendString("Gaze of Saradomin", 12153);
		player.getActionSender().sendString("Temple Knight's Initiate Helm", 12154);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7361);
	}

	public void sendQuestRequirements(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case QUEST_STARTED:
				player.getActionSender().sendString("@str@" + "Talk to Sir Amik Varze, in the White Knights' Castle.", 8147);

				player.getActionSender().sendString("Sir Amik Varze told me to meet with Sir Tiffy Cashien", 8149);
				player.getActionSender().sendString("in Falador Park about the Temple Knights initiation.", 8150);
				break;
			case TRIALS_STARTED:
			case FIRST_ROOM_COMPLETE:
			case SECOND_ROOM_COMPLETE:
			case THIRD_ROOM_COMPLETE:
			case FOURTH_ROOM_COMPLETE:
				player.getActionSender().sendString("@str@" + "Talk to Sir Amik Varze, in the White Knights' Castle.", 8147);
				player.getActionSender().sendString("@str@" + "Sir Amik Varze told me to meet with Sir Tiffy Cashien", 8149);
				player.getActionSender().sendString("@str@" + "in Falador Park about the Temple Knights initiation.", 8150);

				player.getActionSender().sendString("Apparently my Temple Knight initiation are trials of", 8152);
				player.getActionSender().sendString("some sort. I should complete all my trials. Sir Tiffy", 8153);
				player.getActionSender().sendString("Cashien is able to show me back to the Training Grounds", 8154);
				player.getActionSender().sendString("should I have to start the trials over.", 8155);
				break;
			case TRIALS_COMPLETE:
				player.getActionSender().sendString("@str@" + "Talk to Sir Amik Varze, in the White Knights' Castle.", 8147);
				player.getActionSender().sendString("@str@" + "Sir Amik Varze told me to meet with Sir Tiffy Cashien", 8149);
				player.getActionSender().sendString("@str@" + "in Falador Park about the Temple Knights initiation.", 8150);
				player.getActionSender().sendString("@str@" + "Apparently my Temple Knight initiation are trials of", 8152);
				player.getActionSender().sendString("@str@" + "some sort. I should complete all my trials.", 8153);

				player.getActionSender().sendString("I completed all the Temple Knight trials! I should", 8155);
				player.getActionSender().sendString("return to Sir Tiffy Cashien for my reward.", 8156);
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString("@str@" + "Talk to Sir Amik Varze, in the White Knights' Castle.", 8147);
				player.getActionSender().sendString("@str@" + "Sir Amik Varze told me to meet with Sir Tiffy Cashien", 8149);
				player.getActionSender().sendString("@str@" + "in Falador Park about the Temple Knights initiation.", 8150);
				player.getActionSender().sendString("@str@" + "Apparently my Temple Knight initiation are trials of", 8152);
				player.getActionSender().sendString("@str@" + "some sort. I should complete all my trials.", 8153);
				player.getActionSender().sendString("@str@" + "I completed all the Temple Knight trials! I should", 8155);
				player.getActionSender().sendString("@str@" + "return to Sir Tiffy Cashien for my reward.", 8156);

				player.getActionSender().sendString("@red@" + "You have completed this quest!", 8158);
				break;
			default:
				player.getActionSender().sendString("Talk to @dre@Sir Amik Varze @bla@in the White Knights' Castle @bla@ to begin.", 8147);
				player.getActionSender().sendString("@dre@Requirements:", 8149);
				if (QuestHandler.questCompleted(player, 18)) {
					player.getActionSender().sendString("@str@-Completion of Black Knight's Fortress.", 8150);
				} else {
					player.getActionSender().sendString("@dre@-Completion of Black Knight's Fortress.", 8150);
				}
				player.getActionSender().sendString("@dre@-I must be able to defeat a level 20 monster with no items.", 8151);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 668);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 668);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 668);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 668);
		}
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
	}

	public static void enterTrainingGrounds(final Player player) {
		player.fadeTeleport(FIRST_ROOM);
		player.getQuestVars().templeKnightRiddleAnswer = RIDDLE_ANSWERS[Misc.randomMinusOne(RIDDLE_ANSWERS.length)];
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.getActionSender().sendMapState(2);
				if (player.getQuestStage(35) == TRIALS_STARTED) {
					Dialogues.startDialogue(player, SIR_REN_ITCHOOD);
				}
			}
		}, 5);
	}

	public static void exitTrainingGrounds(final Player player) {
		player.setQuestStage(35, TRIALS_STARTED);
		player.getInventory().removeItem(new Item(CHICKEN, player.getInventory().getItemAmount(CHICKEN)));
		player.getInventory().removeItem(new Item(FOX, player.getInventory().getItemAmount(FOX)));
		player.getInventory().removeItem(new Item(GRAIN, player.getInventory().getItemAmount(GRAIN)));
		player.getQuestVars().foxRight = true;
		player.getQuestVars().chickenRight = true;
		player.getQuestVars().grainRight = true;
		player.getQuestVars().foxLeft = false;
		player.getQuestVars().chickenLeft = false;
		player.getQuestVars().grainLeft = false;
		player.getQuestVars().receivedPacket = false;
		player.fadeTeleport(new Position(Constants.FALADOR_X, Constants.FALADOR_Y, 0));
	}

	public static void finishTrainingGrounds(final Player player) {
		player.fadeTeleport(new Position(2997, 3375, 0));
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				Dialogues.startDialogue(player, SIR_TIFFY_CASHIEN);
			}
		}, 5);
	}

	@SuppressWarnings("unused")
	public static void enterThirdRoom(final Player player) {
		boolean occupied = false;
		for (Player p : World.getPlayers()) {
			if (p == null) {
				continue;
			}
			if (p.getPosition().getZ() == player.getIndex() * 4) {
				occupied = true;
			}
		}
		if (!occupied) {
			player.teleport(THIRD_ROOM.modifyZ(player.getIndex() * 4));
		} else {
			player.teleport(THIRD_ROOM.modifyZ(player.getIndex() * 16));
		}
		int z = player.getPosition().getZ();
		NpcLoader.spawnNpc(SIR_SPISHYUS, 2488, 4973, z, true, true);
		CacheObject chicken = ObjectLoader.object(2487, 4974, 0);
		ObjectHandler.getInstance().removeObject(2487, 4974, z, 10);
		GameObject o = new GameObject(CHICKEN_OBJ, chicken.getLocation().getX(), chicken.getLocation().getY(), z, chicken.getRotation(), chicken.getType(), 0, 999999, false);
		CacheObject grain = ObjectLoader.object(2486, 4974, 0);
		ObjectHandler.getInstance().removeObject(2486, 4974, z, 10);
		GameObject o2 = new GameObject(GRAIN_OBJ, grain.getLocation().getX(), grain.getLocation().getY(), z, grain.getRotation(), grain.getType(), 0, 999999, false);
		CacheObject fox = ObjectLoader.object(2485, 4974, 0);
		ObjectHandler.getInstance().removeObject(2485, 4974, z, 10);
		GameObject o3 = new GameObject(FOX_OBJ, fox.getLocation().getX(), fox.getLocation().getY(), z, fox.getRotation(), fox.getType(), 0, 999999, false);
		GameObject o4 = new GameObject(7286, 2483, 4972, z, 0, 10, 0, 999999, false);
		GameObject o5 = new GameObject(7287, 2477, 4972, z, 0, 10, 0, 999999, false);
		GameObject o6 = new GameObject(7274, 2477, 4972, z, 2, 0, 0, 999999, false);
		CacheObject exit = ObjectLoader.object(2487, 4974, 0);
		GameObject o7 = new GameObject(EXIT_PORTAL_3, exit.getLocation().getX(), exit.getLocation().getY(), z, exit.getRotation(), exit.getType(), 0, 999999, false);
		CacheObject portal = ObjectLoader.object(2487, 4974, 0);
		GameObject o8 = new GameObject(THIRD_ROOM_PORTAL, portal.getLocation().getX(), portal.getLocation().getY(), z, portal.getRotation(), portal.getType(), 0, 999999, false);
		ObjectHandler.getInstance().removeObject(2473, 4970, z, 10);
		ObjectHandler.getInstance().removeObject(2474, 4970, z, 10);
		ObjectHandler.getInstance().removeObject(2475, 4970, z, 10);
	}

	public static void openComboLockInterface(final Player player) {
		player.getActionSender().sendInterface(671);
		player.getActionSender().sendString("A", 13884);
		player.getActionSender().sendString("A", 13885);
		player.getActionSender().sendString("A", 13886);
		player.getActionSender().sendString("A", 13887);
		player.getQuestVars().comboLockLetter1 = 1;
		player.getQuestVars().comboLockLetter2 = 1;
		player.getQuestVars().comboLockLetter3 = 1;
		player.getQuestVars().comboLockLetter4 = 1;
	}

	public static boolean comboLockCorrect(final Player player) {
		String concat = GlobalVariables.ALPHABET.get(player.getQuestVars().comboLockLetter1);
		concat = concat.concat(GlobalVariables.ALPHABET.get(player.getQuestVars().comboLockLetter2));
		concat = concat.concat(GlobalVariables.ALPHABET.get(player.getQuestVars().comboLockLetter3));
		concat = concat.concat(GlobalVariables.ALPHABET.get(player.getQuestVars().comboLockLetter4));
		return concat.equals(player.getQuestVars().templeKnightRiddleAnswer);
	}

	public void handleDeath(final Player player, Npc npc) {
		if (npc.getNpcId() == SIR_LEYE) {
			player.getDialogue().setLastNpcTalk(SIR_KUAM_FERENTSE);
			player.getDialogue().sendNpcChat("Well done. You may advance to your next task.", CONTENT);
			player.setQuestStage(35, SECOND_ROOM_COMPLETE);
		}
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
		return false;
	}

	public boolean doObjectClicking(final Player player, final int object, int x, int y) {
		switch (object) {
			case FIFTH_ROOM_PORTAL:
				if (player.getQuestStage(35) >= TRIALS_COMPLETE) {
					finishTrainingGrounds(player);
					return true;
				}
			case FOURTH_ROOM_PORTAL:
				if (player.getQuestStage(35) >= FOURTH_ROOM_COMPLETE) {
					player.teleport(FIFTH_ROOM);
					return true;
				}
			case THIRD_ROOM_PORTAL:
				if (player.getQuestStage(35) >= THIRD_ROOM_COMPLETE) {
					player.teleport(FOURTH_ROOM);
					return true;
				}
			case SECOND_ROOM_PORTAL:
				if (player.getQuestStage(35) >= SECOND_ROOM_COMPLETE) {
					enterThirdRoom(player);
					return true;
				}
			case FIRST_ROOM_PORTAL:
				if (player.getQuestStage(35) >= FIRST_ROOM_COMPLETE) {
					player.teleport(SECOND_ROOM);
					return true;
				}
			case COMBINATION_LOCK_DOOR:
				if (player.getQuestStage(35) >= FIRST_ROOM_COMPLETE) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(player.getPosition().getX() < 2447 ? 1 : -1, player.getPosition().getY() == 4956 ? 0 : player.getPosition().getY() > 4956 ? -1 : 1, true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.teleport(SECOND_ROOM);
						}
					}, 3);
					return true;
				} else {
					openComboLockInterface(player);
				}
				return true;
			case SECOND_ROOM_DOOR:
				if (player.getQuestStage(35) >= SECOND_ROOM_COMPLETE) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(player.getPosition().getX() < 2464 ? 1 : -1, player.getPosition().getY() == 4963 ? 0 : player.getPosition().getY() > 4963 ? -1 : 1, true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							enterThirdRoom(player);
						}
					}, 3);
				} else {
					player.getDialogue().setLastNpcTalk(SIR_KUAM_FERENTSE);
					player.getDialogue().sendNpcChat("Not so fast " + player.getUsername() + ".", "You aren't done with my task yet.", CONTENT);
				}
				return true;
			case THIRD_ROOM_DOOR:
				if (player.getQuestStage(35) >= THIRD_ROOM_COMPLETE) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(-1, player.getPosition().getY() == 4972 ? 0 : player.getPosition().getY() > 4972 ? -1 : 1, true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.teleport(FOURTH_ROOM);
						}
					}, 3);
				} else {
					player.getActionSender().sendMessage("This room's trial is not yet complete.");
				}
				return true;
			case FOURTH_ROOM_DOOR:
				if (player.getQuestStage(35) >= FOURTH_ROOM_COMPLETE) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(1, player.getPosition().getY() == 4956 ? 0 : player.getPosition().getY() > 4956 ? -1 : 1, true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.teleport(FIFTH_ROOM);
						}
					}, 3);
				} else {
					player.getDialogue().setLastNpcTalk(SIR_TINLEY);
					player.getDialogue().sendNpcChat("You move too soon. You have not completed", "my trial yet.", CONTENT);
				}
				return true;
			case FIFTH_ROOM_DOOR:
				if (player.getQuestStage(35) >= TRIALS_COMPLETE) {
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					player.getActionSender().walkTo(player.getPosition().getX() == 2452 ? 0 : player.getPosition().getX() > 2452 ? -1 : 1, 1, true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							finishTrainingGrounds(player);
						}
					}, 3);
				} else {
					player.getDialogue().setLastNpcTalk(MISS_HYNN_TERPRETT);
					player.getDialogue().sendNpcChat("Not so fast! You're almost done, but not done with", "my trial!", CONTENT);
				}
				return true;
			case EXIT_PORTAL:
			case EXIT_PORTAL_2:
			case EXIT_PORTAL_3:
			case EXIT_PORTAL_4:
			case EXIT_PORTAL_5:
				if (player.inTempleKnightsTraining()) {
					exitTrainingGrounds(player);
					return true;
				}

			case 7286: //Bridge 1
			case 7287: //Bridge 2
				player.setEnergy(0);
				player.getActionSender().walkTo(object == 7287 ? 4 : -4, 0, true);
				player.getActionSender().sendMessage("You carefully walk across the rickety bridge...");
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					int count = 0;

					@Override
					public void execute(CycleEventContainer b) {
						if (player.getWeight() > 6) {
							player.getActionSender().sendMessage("The bridge broke under your weight!");
							exitTrainingGrounds(player);
							b.stop();
						} else if (count == 2) {
							b.stop();
						} else {
							player.setEnergy(0);
							player.getActionSender().walkTo(object == 7287 ? 3 : -3, 0, true);
							count++;
						}
					}

					@Override
					public void stop() {
						player.setEnergy(100);
						player.getActionSender().sendEnergy();
						player.setStopPacket(false);
					}
				}, 4);
				return true;
			case FOX_OBJ:
				if (player.getInventory().getItemContainer().freeSlots() == 0) {
					player.getActionSender().sendMessage("Your inventory is full.");
					return true;
				}
				if (player.getQuestStage(35) == SECOND_ROOM_COMPLETE) {
					ObjectHandler.getInstance().removeObject(x, y, player.getPosition().getZ(), 10);
					CacheObject fox = ObjectLoader.object(2485, 4974, 0);
					new GameObject(Constants.EMPTY_OBJECT, x, y, player.getPosition().getZ(), fox.getRotation(), 10, 0, 999999, false);
					player.getInventory().addItem(new Item(FOX));
					player.getActionSender().sendMessage("You gently pick up the fox.");
					if (player.getPosition().getX() > 2483) {
						player.getQuestVars().foxRight = false;
					} else {
						player.getQuestVars().foxLeft = false;
					}
					return true;
				}
				return false;
			case CHICKEN_OBJ:
				if (player.getInventory().getItemContainer().freeSlots() == 0) {
					player.getActionSender().sendMessage("Your inventory is full.");
					return true;
				}
				if (player.getQuestStage(35) == SECOND_ROOM_COMPLETE) {
					ObjectHandler.getInstance().removeObject(x, y, player.getPosition().getZ(), 10);
					CacheObject chicken = ObjectLoader.object(2487, 4974, 0);
					new GameObject(Constants.EMPTY_OBJECT, x, y, player.getPosition().getZ(), chicken.getRotation(), 10, 0, 999999, false);
					player.getInventory().addItem(new Item(CHICKEN));
					player.getActionSender().sendMessage("You gently pick up the chicken.");
					if (player.getPosition().getX() > 2483) {
						player.getQuestVars().chickenRight = false;
					} else {
						player.getQuestVars().chickenLeft = false;
					}
					return true;
				}
				return false;
			case GRAIN_OBJ:
				if (player.getInventory().getItemContainer().freeSlots() == 0) {
					player.getActionSender().sendMessage("Your inventory is full.");
					return true;
				}
				if (player.getQuestStage(35) == SECOND_ROOM_COMPLETE) {
					ObjectHandler.getInstance().removeObject(x, y, player.getPosition().getZ(), 10);
					CacheObject grain = ObjectLoader.object(2486, 4974, 0);
					new GameObject(Constants.EMPTY_OBJECT, x, y, player.getPosition().getZ(), grain.getRotation(), 10, 0, 999999, false);
					player.getInventory().addItem(new Item(GRAIN));
					player.getActionSender().sendMessage("You pick up the sack of grain.");
					if (player.getPosition().getX() > 2483) {
						player.getQuestVars().grainRight = false;
					} else {
						player.getQuestVars().grainLeft = false;
					}
					return true;
				}
				return false;
		}
		return false;
	}

	public static void handleDropItem(final Player player, final Item item) {
		int z = player.getPosition().getZ();
		switch (item.getId()) {
			case CHICKEN:
				CacheObject chicken = ObjectLoader.object(2487, 4974, 0);
				if (player.getPosition().getX() > 2483) {
					ObjectHandler.getInstance().removeObject(chicken.getLocation().getX(), chicken.getLocation().getY(), z, 10);
					new GameObject(CHICKEN_OBJ, chicken.getLocation().getX(), chicken.getLocation().getY(), z, chicken.getRotation(), 10, 0, 999999, false);
					player.getQuestVars().chickenRight = true;
				} else {
					ObjectHandler.getInstance().removeObject(2473, 4970, z, 10);
					new GameObject(CHICKEN_OBJ, 2473, 4970, z, 3, 10, 0, 999999, false);
					player.getQuestVars().chickenLeft = true;
				}
				break;
			case GRAIN:
				CacheObject grain = ObjectLoader.object(2486, 4974, 0);
				if (player.getPosition().getX() > 2483) {
					ObjectHandler.getInstance().removeObject(grain.getLocation().getX(), grain.getLocation().getY(), z, 10);
					new GameObject(GRAIN_OBJ, grain.getLocation().getX(), grain.getLocation().getY(), z, grain.getRotation(), 10, 0, 999999, false);
					player.getQuestVars().grainRight = true;
				} else {
					ObjectHandler.getInstance().removeObject(2474, 4970, z, 10);
					new GameObject(GRAIN_OBJ, 2474, 4970, z, 2, 10, 0, 999999, false);
					player.getQuestVars().grainLeft = true;
				}
				break;
			case FOX:
				CacheObject fox = ObjectLoader.object(2485, 4974, 0);
				if (player.getPosition().getX() > 2483) {
					ObjectHandler.getInstance().removeObject(fox.getLocation().getX(), fox.getLocation().getY(), z, 10);
					new GameObject(FOX_OBJ, fox.getLocation().getX(), fox.getLocation().getY(), z, fox.getRotation(), 10, 0, 999999, false);
					player.getQuestVars().foxRight = true;
				} else {
					ObjectHandler.getInstance().removeObject(2475, 4970, z, 10);
					new GameObject(FOX_OBJ, 2475, 4970, z, 2, 10, 0, 999999, false);
					player.getQuestVars().foxLeft = true;
				}
				break;
		}
		if (player.getQuestVars().foxRight && player.getQuestVars().chickenRight && !player.getQuestVars().grainRight) {
			player.getActionSender().sendMessage("The fox has eaten the chicken!");
			player.getQuestVars().chickenRight = false;
			ObjectHandler.getInstance().removeObject(2487, 4974, player.getPosition().getZ(), 10);
			new GameObject(Constants.EMPTY_OBJECT, 2487, 4974, player.getPosition().getZ(), 0, 10, 0, 999999, false);
		} else if (player.getQuestVars().chickenRight && player.getQuestVars().grainRight && !player.getQuestVars().foxRight) {
			player.getActionSender().sendMessage("The chicken has eaten the grain!");
			player.getQuestVars().grainRight = false;
			ObjectHandler.getInstance().removeObject(2486, 4974, player.getPosition().getZ(), 10);
			new GameObject(Constants.EMPTY_OBJECT, 2486, 4974, player.getPosition().getZ(), 0, 10, 0, 999999, false);
		} else if (player.getQuestVars().foxLeft && player.getQuestVars().chickenLeft && !player.getQuestVars().grainLeft) {
			player.getActionSender().sendMessage("The fox has eaten the chicken!");
			player.getQuestVars().chickenLeft = false;
			ObjectHandler.getInstance().removeObject(2473, 4970, player.getPosition().getZ(), 10);
			new GameObject(Constants.EMPTY_OBJECT, 2473, 4970, player.getPosition().getZ(), 0, 10, 0, 999999, false);
		} else if (player.getQuestVars().chickenLeft && player.getQuestVars().grainLeft && !player.getQuestVars().foxLeft) {
			player.getActionSender().sendMessage("The chicken has eaten the grain!");
			player.getQuestVars().grainLeft = false;
			ObjectHandler.getInstance().removeObject(2474, 4970, player.getPosition().getZ(), 10);
			new GameObject(Constants.EMPTY_OBJECT, 2474, 4970, player.getPosition().getZ(), 0, 10, 0, 999999, false);
		}
		if (player.getQuestVars().foxLeft && player.getQuestVars().chickenLeft && player.getQuestVars().grainLeft && !player.getQuestVars().foxRight && !player.getQuestVars().chickenRight && !player.getQuestVars().grainRight) {
			player.setQuestStage(35, THIRD_ROOM_COMPLETE);
			Dialogues.startDialogue(player, SIR_SPISHYUS);
		}
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public static boolean buttonHandling(final Player player, int buttonId) {
		switch (buttonId) {
			case 54064: //Combo lock 1 L
			case 54065: //Combo lock 1 R
				int change1 = buttonId == 54065 ? 1 : -1;
				if (player.getQuestVars().comboLockLetter1 == 1 && change1 == -1) {
					player.getQuestVars().comboLockLetter1 = 26;
					player.getActionSender().sendString("Z", 13884);
					return true;
				} else if (player.getQuestVars().comboLockLetter1 == 26 && change1 == 1) {
					player.getQuestVars().comboLockLetter1 = 1;
					player.getActionSender().sendString("A", 13884);
					return true;
				} else {
					player.getQuestVars().comboLockLetter1 += change1;
					player.getActionSender().sendString(GlobalVariables.ALPHABET.get(player.getQuestVars().comboLockLetter1), 13884);
				}
				return true;
			case 54066: //Combo lock 2 L
			case 54067: //Combo lock 2 R
				int change2 = buttonId == 54067 ? 1 : -1;
				if (player.getQuestVars().comboLockLetter2 == 1 && change2 == -1) {
					player.getQuestVars().comboLockLetter2 = 26;
					player.getActionSender().sendString("Z", 13885);
					return true;
				} else if (player.getQuestVars().comboLockLetter2 == 26 && change2 == 1) {
					player.getQuestVars().comboLockLetter2 = 1;
					player.getActionSender().sendString("A", 13885);
					return true;
				} else {
					player.getQuestVars().comboLockLetter2 += change2;
					player.getActionSender().sendString(GlobalVariables.ALPHABET.get(player.getQuestVars().comboLockLetter2), 13885);
				}
				return true;
			case 54068: //Combo lock 3 L
			case 54069: //Combo lock 3 R
				int change3 = buttonId == 54069 ? 1 : -1;
				if (player.getQuestVars().comboLockLetter3 == 1 && change3 == -1) {
					player.getQuestVars().comboLockLetter3 = 26;
					player.getActionSender().sendString("Z", 13886);
					return true;
				} else if (player.getQuestVars().comboLockLetter3 == 26 && change3 == 1) {
					player.getQuestVars().comboLockLetter3 = 1;
					player.getActionSender().sendString("A", 13886);
					return true;
				} else {
					player.getQuestVars().comboLockLetter3 += change3;
					player.getActionSender().sendString(GlobalVariables.ALPHABET.get(player.getQuestVars().comboLockLetter3), 13886);
				}
				return true;
			case 54070: //Combo lock 4 L
			case 54071: //Combo lock 4 R
				int change4 = buttonId == 54071 ? 1 : -1;
				if (player.getQuestVars().comboLockLetter4 == 1 && change4 == -1) {
					player.getQuestVars().comboLockLetter4 = 26;
					player.getActionSender().sendString("Z", 13887);
					return true;
				} else if (player.getQuestVars().comboLockLetter4 == 26 && change4 == 1) {
					player.getQuestVars().comboLockLetter4 = 1;
					player.getActionSender().sendString("A", 13887);
					return true;
				} else {
					player.getQuestVars().comboLockLetter4 += change4;
					player.getActionSender().sendString(GlobalVariables.ALPHABET.get(player.getQuestVars().comboLockLetter4), 13887);
				}
				return true;
			case 54074: //Enter combo
				if (comboLockCorrect(player)) {
					player.setQuestStage(35, FIRST_ROOM_COMPLETE);
					Dialogues.startDialogue(player, SIR_REN_ITCHOOD);
				} else {
					player.getDialogue().sendStatement("The lock refuses to budge.");

				}
				return true;
		}
		return false;
	}

	public boolean sendDialogue(final Player player, int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case MISS_HYNN_TERPRETT:
				switch (player.getQuestStage(35)) {
					case TRIALS_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Congratulations " + player.getUsername() + ". You've completed", "all the trials successfully. Step through the door", "behind me and recieve your reward.", HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
					case FOURTH_ROOM_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Greetings, " + player.getUsername() + ".", "I am here to test your wits with a simple riddle.", CONTENT);
								d.setNextChatId(2 + Misc.random(4));
								return true;
							case 2:
								d.sendNpcChat("Here is my riddle:", "If you were being sentenced to death, what would you", "rather choose?", CONTENT);
								d.setNextChatId(8);
								return true;
							case 3:
								d.sendNpcChat("Here is my riddle: I have both a husband and a daughter.", "My husband is four times older than my daughter.", "In twenty years time, he will be twice as", "old as my daughter. How old is my daughter now?", CONTENT);
								d.setNextChatId(9);
								return true;
							case 4:
								d.sendNpcChat("I dropped 4 identical stones into 4 identical buckets, each", "containing an equal amount of water. The first bucket", "was at 32 degrees Fahrenheit, the second 33, the third 34", "and the fourth 35. Which bucket's stone hit the bottom last?", CONTENT);
								d.setNextChatId(10);
								return true;
							case 5:
								d.sendNpcChat("Here is my riddle: Counting the creatures and humans", " in /v/scape you get about a million inhabitants. If you", " multiply the fingers on everything's left hand by a", " million, how many would you get?", CONTENT);
								d.setNextChatId(11);
								return true;
							case 6:
								d.sendNpcChat("Here is my riddle:", "Which of the following statments is true?", CONTENT);
								return true;
							case 7:
								d.sendOption("The number of false statements here is one.", "The number of false statements here is two.", "The number of false statements here is three.", "The number of false statements here is four.");
								d.setNextChatId(12);
								return true;
							case 8:
								d.sendOption("Being thrown into a lake of acid.", "Being burned over a fire.", "Being fed to wolves that haven't eaten in 30 days.", "Being thrown off a castle turret.");
								d.setNextChatId(12);
								return true;
							case 9:
								d.sendOption("Your daughter is 24 years old.", "Your daughter is 12 years old.", "Your daughter is 10 years old.", "Your daughter is 8 years old.");
								d.setNextChatId(12);
								return true;
							case 10:
								d.sendOption("Bucket A (32 degrees)", "Bucket B (33 degrees)", "Bucket C (34 degrees)", "Bucket D (35 degrees)");
								d.setNextChatId(13);
								return true;
							case 11:
								d.sendOption("1 billion.", "5 million.", "Zero.", "None of the above.");
								d.setNextChatId(12);
								return true;
							case 12:
								switch (optionId) {
									case 1:
									case 2:
									case 4:
										d.sendNpcChat("I'm sorry, you've answered the riddle incorrectly.", "Please, do try again.", CONTENT);
										d.endDialogue();
										return true;
									case 3:
										d.sendNpcChat("Congratulations " + player.getUsername() + ". You've completed", "all the trials successfully. Step through the door", "behind me and recieve your reward.", HAPPY);
										player.setQuestStage(35, TRIALS_COMPLETE);
										d.endDialogue();
										return true;
								}
							case 13:
								switch (optionId) {
									case 1:
										d.sendNpcChat("Congratulations " + player.getUsername() + ". You've completed", "all the trials successfully. Step through the door", "behind me and recieve your reward.", HAPPY);
										player.setQuestStage(35, TRIALS_COMPLETE);
										d.endDialogue();
										return true;
									case 2:
									case 3:
									case 4:
										d.sendNpcChat("I'm sorry, you've answered the riddle incorrectly.", "Please, do try again.", CONTENT);
										d.endDialogue();
										return true;
								}
						}
						return false;
				}
				return false;
			case SIR_TINLEY:
				switch (player.getQuestStage(35)) {
					case FOURTH_ROOM_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Excellent work, " + player.getUsername() + ".", "Please step through the portal to meet your next", "challenge.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case THIRD_ROOM_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Ah, welcome " + player.getUsername() + ".", "I have but one clue for you to pass this room's puzzle:", "'Patience'.", CONTENT);
								player.getQuestVars().receivedPacket = false;
								return true;
							case 2:
								player.getActionSender().removeInterfaces();
								d.endDialogue();
								player.getQuestVars().receivedPacket = false;
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									int count = 0;

									@Override
									public void execute(CycleEventContainer b) {
										if (count > 10 && player.getQuestVars().receivedPacket) {
											b.stop();
										} else if (count == 15) {
											player.setQuestStage(35, FOURTH_ROOM_COMPLETE);
											Dialogues.startDialogue(player, SIR_TINLEY);
											b.stop();
										} else {
											count++;
										}
									}

									@Override
									public void stop() {
									}
								}, 1);
								return true;
						}
						return false;
				}
				return false;
			case SIR_SPISHYUS:
				switch (player.getQuestStage(35)) {
					case THIRD_ROOM_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Congratulations, you have solved the puzzle.", "Continue on to the next room for your next trial.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case SECOND_ROOM_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Ah, welcome " + player.getUsername() + ".", CONTENT);
								return true;
							case 2:
								d.sendPlayerChat("Hello there.", "What I am supposed to be doing in this room?", CONTENT);
								return true;
							case 3:
								d.sendNpcChat("Well, your task is to take this fox, this chicken and this", "bag of grain across that bridge there to the other side", "of the room.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("When you have done this, your task is complete.", CONTENT);
								return true;
							case 5:
								d.sendPlayerChat("Is that it?", CONTENT);
								return true;
							case 6:
								d.sendNpcChat("Well, it is not quite as simple as that may sound.", CONTENT);
								return true;
							case 7:
								d.sendNpcChat("Firstly, you may only carry one of the objects across", "the room at a time, for the bridge is old and fragile.", CONTENT);
								return true;
							case 8:
								d.sendNpcChat("Secondly, the fox wants to eat the chicken, and the", "chicken wants to eat the grain.", "Should you ever leave the fox unattended with the", "chicken, or the grain unattended with the chicken, then", CONTENT);
								return true;
							case 9:
								d.sendNpcChat("one of them will be eaten, and you will be unable to", "complete the test.", CONTENT);
								return true;
							case 10:
								d.sendPlayerChat("Okay, I'll see what I can do.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case SIR_KUAM_FERENTSE:
				switch (player.getQuestStage(35)) {
					case SECOND_ROOM_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Well done. You may advance to your next task.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case FIRST_ROOM_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Ah, " + player.getUsername() + ", you're finally here.", "Your task for this room is to defeat Sir Leye.", "He has been blessed by Saradomin to be undefeatable", "by any man, so it should be quite the challenge for you.", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("If you are having problems, remember;", "A true warrior uses his wits as much as his brawn.", "Fight smarter, not harder.", CONTENT);
								return true;
							case 3:
								player.getActionSender().removeInterfaces();
								d.endDialogue();
								NpcLoader.spawnPlayerOwnedAttackNpc(player, new Npc(SIR_LEYE), new Position(2458, 4961, 0), true, "No man can defeat me!");
								return true;
						}
						return false;
				}
				return false;
			case SIR_REN_ITCHOOD:
				switch (player.getQuestStage(35)) {
					case FIRST_ROOM_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Your wit is sharp, your brains quite clear;", "you solved my puzzle with no fear.", "At puzzles I rank you quite the best,", "now enter the portal for your next test.", CONTENT);
								d.endDialogue();
								return true;
						}
						return false;
					case TRIALS_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Greetings friend, and welcome here,", "you'll find my puzzle not so clear.", "Hidden amongst my words, it's true,", "the password for the door as a clue.", CONTENT);
								return true;
							case 2:
								d.sendOption("Can I have a clue for the door?", "Can I have a different clue for the door?");
								return true;
							case 3:
								switch (optionId) {
									case 1:
									case 2:
										if (optionId == 2) {
											player.getQuestVars().templeKnightRiddleAnswer = RIDDLE_ANSWERS[Misc.randomMinusOne(RIDDLE_ANSWERS.length)];
										}
										switch (player.getQuestVars().templeKnightRiddleAnswer) {
											case "TIME":
												d.sendNpcChat("The riddle of mine may confuse,", "I am quite sure of that.", "Maybe you should closely peruse", "Every word I have spat?", CONTENT);
												d.endDialogue();
												return true;
											case "MEAT":
												d.sendNpcChat("More than weeds, I have not for you", "Except the things I say today.", "Aware are you, this is a clue?", "Take note of what I say!", CONTENT);
												d.endDialogue();
												return true;
											case "LAST":
												d.sendNpcChat("Look closely at the words I speak;", "And study closely every part.", "See for yourself the word you seek", "Trapped for you if you're smart.", CONTENT);
												d.endDialogue();
												return true;
											case "BITE":
												d.sendNpcChat("Betrayed by words the answer is", "In that what I say is the key", "There is no more help after this", "Especially no more from me.", CONTENT);
												d.endDialogue();
												return true;
											case "FISH":
												d.sendNpcChat("Feel the aching of your mind", "In puzzlement, confused.", "See the clue hidden behind", "His words, as you perused.", CONTENT);
												d.endDialogue();
												return true;
										}

								}
						}
						return false;
				}
				return false;
			case SIR_TIFFY_CASHIEN:
				switch (player.getQuestStage(35)) {
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("What ho!", "Jolly good show on the old training grounds!", "What becometh of you today?", HAPPY);
								return true;
							case 2:
								d.sendOption("Can you explain the Gaze of Saradomin to me?", "Can I buy some armor?", "Can I switch respawns please?", "Goodbye.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Can you explain the Gaze of Saradomin to me?", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Can I buy some armor?", CONTENT);
										d.setNextChatId(15);
										return true;
									case 3:
										d.sendPlayerChat("Can I switch respawns please?", CONTENT);
										d.setNextChatId(8);
										return true;
									case 4:
										d.sendPlayerChat("Goodbye.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 4:
								d.sendNpcChat("Ahh, the great Gaze of Saradomin. A power", "that all Temple Knights gain upon completion of", "their trials.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("It is a jolly old power, it brings peace to", "the presider. It allows you to respawn within the", "confines of the White Knight Castle as a reward for", "outstanding duty.", CONTENT);
								return true;
							case 6:
								d.sendOption("That sounds useful, I'd like to switch respawns.", "Ah, interesting, but I'm not interested.");
								return true;
							case 7:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("That sounds useful, I'd like to switch respawns.", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Ah, interesting, but I'm not interested. Thank you.", CONTENT);
										d.endDialogue();
										return true;
								}
								return true;
							case 8:
								d.sendNpcChat("Sure thing my dear child, stand still while I", "focus Saradomin's energies...", CONTENT);
								return true;
							case 9:
								d.sendStatement("Sir Tiffy Cashien waves his hands around you...");
								return true;
							case 10:
								if (!player.getQuestVars().isGazeOfSaradomin()) {
									d.sendStatement("Your respawn point is now the White Knight Castle.");
									d.endDialogue();
									player.getQuestVars().setGazeOfSaradomin(true);
									return true;
								} else {
									d.sendStatement("Your respawn point is now Lumbridge Castle.");
									d.endDialogue();
									player.getQuestVars().setGazeOfSaradomin(false);
									return true;
								}
							case 15:
								d.sendNpcChat("Of course! I can sell you up to Initiate level", "items at the moment I'm afraid.", CONTENT);
								return true;
							case 16:
								ShopManager.openShop(player, 202);
								d.dontCloseInterface();
								return true;
						}
						return false;
					case TRIALS_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Oh, jolly well done!", "Your performance will need to be evaluated by Sir Vey", "personally, but I don't think it's going too far ahead of", "myself to welcome you to the team!", HAPPY);
								return true;
							case 2:
								QuestHandler.completeQuest(player, 35);
								d.dontCloseInterface();
								return true;
						}
						return false;
					case FOURTH_ROOM_COMPLETE:
					case THIRD_ROOM_COMPLETE:
					case SECOND_ROOM_COMPLETE:
					case FIRST_ROOM_COMPLETE:
					case TRIALS_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendNpcChat("Ah " + player.getUsername() + "!", "I didn't expect to see you back here so soon!", "Surely your trials are not over yet?", "Do you need shown back to the training grounds?", CONTENT);
								return true;
							case 2:
								d.sendOption("Yes.", "Not yet, actually.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Yes, I do.", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("Not yet, actually.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 4:
								d.sendNpcChat("Alright! I'll show you the way...", CONTENT);
								return true;
							case 5:
								enterTrainingGrounds(player);
								d.endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Sir Amik Varze sent me to meet you here for", "some sort of testing...?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("Ah, " + player.getUsername() + "!", "Amik told me all about you, dont-cha-know!", "Spiffing job you did with the old Black Knights there,", "absolutely first class.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("...Thanks. I think.", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Well, a top-notch filly like yourself is just the right sort", "we've been looking for for our organization.", CONTENT);
								return true;
							case 5:
								d.sendNpcChat("So, are you ready for testing?", CONTENT);
								return true;
							case 6:
								d.sendOption("Yes, let's go!", "Not quite yet.");
								return true;
							case 7:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Yeah, this sounds right up my alley.", "Let's go!", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("I don't think I'm quite ready yet.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 8:
								d.sendNpcChat("Jolly good show!", "Now, the training grounds location is a secret so...", CONTENT);
								return true;
							case 9:
								enterTrainingGrounds(player);
								player.setQuestStage(35, TRIALS_STARTED);
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case SIR_AMIK_VARZE:
				switch (player.getQuestStage(35)) { //Dialogue per stage
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Who should I be talking to again?", CONTENT);
								return true;
							case 2:
								d.sendNpcChat("You are to meet Sir Tiffy Cashien in Falador Park for", "testing immediately.", CONTENT);
								return true;
							case 3:
								d.sendPlayerChat("Ah, alright. Thank you.", CONTENT);
								d.endDialogue();
								return true;
						}
						return true;
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								d.sendPlayerChat("Hello Sir Amik!", HAPPY);
								return true;
							case 2:
								d.sendNpcChat("Hello, friend!", HAPPY);
								return true;
							case 3:
								d.sendPlayerChat("Do you have any other quests for me to do?", CONTENT);
								return true;
							case 4:
								d.sendNpcChat("Quests, eh?", "Well, I don't have anything on the go at the moment,", "but there is an organization that is always looking for", "capable adventurers to assist them.", CONTENT);
								return true;
							case 5:
								if (QuestHandler.questCompleted(player, 18)) {
									d.sendNpcChat("Your excellent work sorting out those Black Knights", "means I will happily write you a letter of", "recommendation.", CONTENT);
									return true;
								} else {
									d.sendNpcChat("But unfortunately you're not experienced enough, come back", "a bit later when you've taken care of some Black Knights.", CONTENT);
									d.endDialogue();
									return true;
								}
							case 6:
								d.sendNpcChat("Would you like me to put your name forward to", "them?", CONTENT);
								return true;
							case 7:
								d.sendOption("Yes please.", "No thanks.");
								return true;
							case 8:
								switch (optionId) {
									case 1:
										d.sendPlayerChat("Yes please.", CONTENT);
										return true;
									case 2:
										d.sendPlayerChat("No thanks.", CONTENT);
										d.endDialogue();
										return true;
								}
							case 9:
								d.sendNpcChat("Erm, well, this is a little embarrassing, I already HAVE", "put you forward as a potential member.", CONTENT);
								return true;
							case 10:
								d.sendNpcChat("They are called the Temple Knights, and you are to", "meet Sir Tiffy Cashien in Falador Park for testing", "immediately.", CONTENT);
								return true;
							case 11:
								d.sendPlayerChat("Alright, I'll go do that then.", CONTENT);
								d.endDialogue();
								QuestHandler.startQuest(player, 35);
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
