package com.rs2.model.content.quests.impl;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;

import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
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
	public static final int TALKED_TO_JERICO = 2;
	public static final int DISTRACT_WATCHTOWER = 3;
	public static final int OVER_WALL = 4;
	public static final int STEW_POISONED = 5;
	public static final int DISTILLATOR_GET = 6;
	public static final int NEED_TOUCH_PAPER = 7;
	public static final int TOUCH_PAPER_GET = 8;
	public static final int EXPERIMENT_SUCCESS = 9;
	public static final int TALK_TO_LATHAS = 10;
	public static final int QUEST_COMPLETE = 11;

	//Items
	public static final int ETHENEA = 415;
	public static final int LIQUID_HONEY = 416;
	public static final int SULPHURIC_BROLINE = 417;
	public static final int PLAGUE_SAMPLE = 418;
	public static final int TOUCH_PAPER = 419;
	public static final int DISTILLATOR = 420;
	public static final int BIRD_FEED = 422;
	public static final int KEY = 423;
	public static final int PIGEON_CAGE_FULL = 424;
	public static final int PIGEON_CAGE_EMPTY = 425;
	public static final int PRIEST_GOWN_TOP = 426;
	public static final int PRIEST_GOWN_BOTTOM = 428;
	public static final int DOCTORS_GOWN = 430;
	public static final int ROTTEN_APPLE = 1984;

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
	public static final int HOPS = 340;
	public static final int HOPS_VARROCK = 341;
	public static final int GUIDORS_WIFE = 342;
	public static final int GUIDOR = 343;
	public static final int GUARD_1 = 344;
	public static final int GUARD_2 = 345;
	public static final int GUARD_3 = 346;
	public static final int KILRON = 349;
	public static final int OMART = 350;

	//Objects
	public static final int ELENAS_DOOR = 2054;
	public static final int JERICO_CUPBOARD = 2056;
	public static final int JERICO_CUPBOARD_OPEN = 2057;
	public static final int WATCHTOWER = 2067;
	public static final int ROPE_LADDER = 2065;
	public static final int MOURNER_DOOR = 2036;
	public static final int MOURNER_FENCE = 2068;
	public static final int MOURNER_GATE_1 = 2058;
	public static final int MOURNER_GATE_2 = 2060;
	public static final int CRATE = 2064;
	public static final int CAULDRON = 2043;
	public static final int GATE = 2050;
	public static final int GUIDORS_DOOR = 2032;
	public static final int ARDOUGNE_DOOR_LEFT = 8738;
	public static final int ARDOUGNE_DOOR_RIGHT = 8739;

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
			case TALKED_TO_JERICO:
				lastIndex = 8;
				break;
			case DISTRACT_WATCHTOWER:
				lastIndex = 11;
				break;
			case OVER_WALL:
				lastIndex = 14;
				break;
			case STEW_POISONED:
				lastIndex = 18;
				break;
			case DISTILLATOR_GET:
				lastIndex = 20;
				break;
			case NEED_TOUCH_PAPER:
				lastIndex = 25;
				break;
			case TOUCH_PAPER_GET:
				lastIndex = 29;
				break;
			case EXPERIMENT_SUCCESS:
				lastIndex = 33;
				break;
			case QUEST_COMPLETE:
				lastIndex = 35;
				break;
		}
		lastIndex++;
		
		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Elena in East Ardougne to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Elena told me to talk to her father's old friend", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("Jerico in order to try and gain access to West", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("Ardougne again.", 5, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I talked to Jerico, he said to seek out his", 7, this.getQuestID(), TALKED_TO_JERICO);
		a.sendQuestLogString("assistant Omart on the south end of the city wall.", 8, this.getQuestID(), TALKED_TO_JERICO);
		a.sendQuestLogString("Omart can get me over the wall, but only with the", 10, this.getQuestID(), DISTRACT_WATCHTOWER);
		a.sendQuestLogString("mourners at the nearby watchtower distracted.", 11, this.getQuestID(), DISTRACT_WATCHTOWER);
		a.sendQuestLogString("I managed to get over the West Ardougne wall with", 13, this.getQuestID(), OVER_WALL);
		a.sendQuestLogString("Omart's help.", 14, this.getQuestID(), OVER_WALL);
		a.sendQuestLogString("I snuck in the back of the mourner's main building", 16, this.getQuestID(), STEW_POISONED);
		a.sendQuestLogString("and poisoned their stew with a rotten apple. This", 17, this.getQuestID(), STEW_POISONED);
		a.sendQuestLogString("should allow me to pretend to be a doctor to get in.", 18, this.getQuestID(), STEW_POISONED);
		a.sendQuestLogString("I found Elena's distillator.", 20, this.getQuestID(), DISTILLATOR_GET);
		a.sendQuestLogString("Elena's test on the plague sample seemed to fail.", 22, this.getQuestID(), NEED_TOUCH_PAPER);
		a.sendQuestLogString("She has asked me to help her make it work, starting", 23, this.getQuestID(), NEED_TOUCH_PAPER);
		a.sendQuestLogString("by getting another piece of 'touch paper' from the", 24, this.getQuestID(), NEED_TOUCH_PAPER);
		a.sendQuestLogString("Chemist in Rimmington.", 25, this.getQuestID(), NEED_TOUCH_PAPER);
		a.sendQuestLogString("I got the touch paper from the chemist. I need", 27, this.getQuestID(), TOUCH_PAPER_GET);
		a.sendQuestLogString("to deliver it, the plague sample, and the vials", 28, this.getQuestID(), TOUCH_PAPER_GET);
		a.sendQuestLogString("to Guidor in East Varrock.", 29, this.getQuestID(), TOUCH_PAPER_GET);
		a.sendQuestLogString("I finally met with Guidor, and he did the experiment.", 31, this.getQuestID(), EXPERIMENT_SUCCESS);
		a.sendQuestLogString("He says the plague sample is fraudulous, as if there", 32, this.getQuestID(), EXPERIMENT_SUCCESS);
		a.sendQuestLogString("was never any plague!", 33, this.getQuestID(), EXPERIMENT_SUCCESS);
		a.sendQuestLogString("Elena told me to go see King Lathas.", 35, this.getQuestID(), TALK_TO_LATHAS);
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("Talk to @dre@Elena @bla@in @dre@East Ardougne @bla@to begin.", 1);
				a.sendQuestLogString("@dre@Requirements:", 3);
				a.sendQuestLogString((QuestHandler.questCompleted(player, 39) ? "@str@" : "@dbl@") + "Plague City", 4);
				break;
			case DISTRACT_WATCHTOWER:
				a.sendQuestLogString("I should figure this out for myself, or ask", lastIndex + 1);
				a.sendQuestLogString("Jerico for ideas on how to accomplish this.", lastIndex + 2);
				break;
			case OVER_WALL:
				a.sendQuestLogString("I should investigate and find where", lastIndex + 1);
				a.sendQuestLogString("the mourners took Elena's distillator. I imagine it's", lastIndex + 2);
				a.sendQuestLogString("in the locked building where they all gather.", lastIndex + 3);
				break;
			case DISTILLATOR_GET:
				a.sendQuestLogString("I should return the distillator to Elena and", lastIndex + 1);
				a.sendQuestLogString("see what she'd have me do next.", lastIndex + 2);
				break;
			case NEED_TOUCH_PAPER:
				a.sendQuestLogString("Elena mentioned that the plague sample is very", lastIndex + 1);
				a.sendQuestLogString("fragile. I should avoid any form of teleport", lastIndex + 2);
				a.sendQuestLogString("and combat.", lastIndex + 3);
				break;
			case TOUCH_PAPER_GET:
				a.sendQuestLogString("The chemist mentioned the guards are doing spot", lastIndex + 1);
				a.sendQuestLogString("checks at the gate into East Varrock, I need to", lastIndex + 2);
				a.sendQuestLogString("give the vials to the chemist's apprentices or", lastIndex + 3);
				a.sendQuestLogString("find some other way to smuggle them in.", lastIndex + 4);
				break;
			case EXPERIMENT_SUCCESS:
				a.sendQuestLogString("I should return to Elena and ask her about this.", lastIndex + 1);
				break;
			case QUEST_COMPLETE:
				a.sendQuestLogString("That plague was a hoax! King Lathas sealed off", lastIndex + 1);
				a.sendQuestLogString("West Ardougne to protect us from an evil unknown...", lastIndex + 2);
				a.sendQuestLogString("@red@" + "You have completed this quest!", lastIndex + 4);
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
	
	public static boolean releasePigeons(final Player player) {
		if (player.getQuestStage(40) != DISTRACT_WATCHTOWER) {
			player.getDialogue().sendStatement("The pigeons refuse to leave the cage.");
			return false;
		}
		int x = player.getPosition().getX(), y = player.getPosition().getY();
		if (((x >= 2559 && x <= 2563 && y == 3300) || (x == 2564 && y >= 3301 && y <= 3306) || player.getPosition().equals(new Position(2563, 3301, 0))) && player.getQuestVars().birdSeedThrown) {
			player.setStopPacket(true);
			player.getActionSender().sendMessage("The pigeons fly towards the watchtower.");
			int attackerX = player.getPosition().getX(), attackerY = player.getPosition().getY();
			Position[] positions = {new Position(2561, 3305, 0), new Position(2561, 3303, 0), new Position(2559, 3303, 0)};
			for (Position p : positions) {
				int victimX = p.getX(), victimY = p.getY();
				int offsetX = (attackerY - victimY) * -1;
				int offsetY = (attackerX - victimX) * -1;
				World.sendProjectile(player.getPosition(), offsetX, offsetY, 72, 10, 43, 100, 0, false);
			}
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}

				@Override
				public void stop() {
					player.setStopPacket(false);
					for (Npc npc : player.getNpcs()) {
						if (npc != null && npc.getNpcId() == 348) {
							npc.getUpdateFlags().sendAnimation(npc.getDefinition().getBlockAnim());
						}
					}
					player.getActionSender().sendMessage("The mourners are frantically trying to scare the pigeons away.");
					player.getQuestVars().watchtowerDistracted = true;
				}
			}, 3);
			return true;
		} else {
			if (player.getQuestVars().birdSeedThrown) {
				player.getActionSender().sendMessage("You need to get closer to the watchtower to release the birds.");
			} else {
				player.getDialogue().sendStatement("The pigeons refuse to leave the cage.");
			}
			return false;
		}

	}
	
	public static void climbRopeLadder(final Player player) {
		player.getActionSender().removeInterfaces();
		Following.resetFollow(World.getNpcs()[World.getNpcIndex(KILRON)]);
		Following.resetFollow(World.getNpcs()[World.getNpcIndex(OMART)]);
		player.getActionSender().sendMessage("You climb up the rope ladder...");
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getActionSender().sendMessage("...and drop down on the other side.");
				player.teleport(new Position(player.getPosition().getX() > 2558 ? 2556 : 2559, 3267, 0));
				ObjectHandler.getInstance().removeObject(2557, 3267, 0, 10);
				ObjectHandler.getInstance().removeObject(2558, 3267, 0, 10);
				new GameObject(ROPE_LADDER + 1, 2557, 3267, 0, 3, 10, ROPE_LADDER, 999999);
				new GameObject(ROPE_LADDER + 1, 2558, 3267, 0, 1, 10, ROPE_LADDER, 999999);
				ObjectHandler.getInstance().removeClip(2559, 3267, 0, 10, 0);
				ObjectHandler.getInstance().removeClip(2559, 3268, 0, 10, 0);
				ObjectHandler.getInstance().removeClip(2559, 3269, 0, 10, 0);
			}
		}, 3);
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case ROTTEN_APPLE:
				player.getInventory().removeItem(new Item(ROTTEN_APPLE));
				player.getUpdateFlags().setForceChatMessage("Yuck!");
				player.getActionSender().sendMessage("It's rotten, you spit it out.");
				return true;
			case PIGEON_CAGE_FULL:
				if(releasePigeons(player)) {
					player.getInventory().replaceItemWithItem(new Item(PIGEON_CAGE_FULL), new Item(PIGEON_CAGE_EMPTY));
				}
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch(object) {
			case CAULDRON:
				if(item == ROTTEN_APPLE && player.getQuestStage(this.getQuestID()) == OVER_WALL) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You place the rotten apple in the cauldron...");
					player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
					player.getInventory().removeItem(new Item(ROTTEN_APPLE));
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							player.setQuestStage(40, STEW_POISONED);
							player.getActionSender().sendMessage("and it quickly dissolves in the stew.");
						}
					}, 4);
					return true;
				}
			return false;
			case WATCHTOWER:
				if(item == BIRD_FEED && player.getQuestStage(this.getQuestID()) == DISTRACT_WATCHTOWER) {
					player.getActionSender().sendMessage("You steathily throw a handful of feed onto the watchtower.");
					player.getInventory().removeItem(new Item(BIRD_FEED));
					player.getQuestVars().birdSeedThrown = true;
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

	public boolean doObjectClicking(final Player player, final int object, final int x, final int y) {
		int pX = player.getPosition().getX();
		int pY = player.getPosition().getY();
		switch (object) {
			case ARDOUGNE_DOOR_LEFT:
			case ARDOUGNE_DOOR_RIGHT:
				if (QuestHandler.questCompleted(player, 40)) {
					player.getActionSender().sendMessage("You pull on the large wooden doors...");
					new GameObject(ARDOUGNE_DOOR_LEFT, 2557, 3299, 0, 3, 10, ARDOUGNE_DOOR_RIGHT, 3, false).addOriginalFace(2);
					new GameObject(ARDOUGNE_DOOR_RIGHT, 2557, 3300, 0, 1, 10, ARDOUGNE_DOOR_LEFT, 3, false).addOriginalFace(2);
					new GameObject(ARDOUGNE_DOOR_RIGHT, 2558, 3299, 0, 3, 10, ARDOUGNE_DOOR_LEFT, 3, false).addOriginalFace(0);
					new GameObject(ARDOUGNE_DOOR_LEFT, 2558, 3300, 0, 1, 10, ARDOUGNE_DOOR_RIGHT, 3, false).addOriginalFace(0);
					player.getActionSender().walkTo(pX > 2558 ? -3 : 3, 0, true);
					player.getActionSender().sendMessage("...You open them and walk through.");
				} else {
					player.getDialogue().setLastNpcTalk(348);
					player.getDialogue().sendNpcChat("Oi! Get away from there!", ANGRY_1);
					player.getDialogue().endDialogue();
				}
				return true;
			case GUIDORS_DOOR:
				if(x == 3282 && y == 3382) {
					if(pX < 3283) {
						if(player.getEquipment().getId(Constants.CHEST) == PRIEST_GOWN_TOP && player.getEquipment().getId(Constants.LEGS) == PRIEST_GOWN_BOTTOM) {
							player.getActionSender().sendMessage("Guidor's wife allows you to go in.");
							player.getActionSender().walkThroughDoor(object, x, y, 0);
							player.getActionSender().walkTo(1, pY == y ? 0 : pY < y ? 1 : -1, true);
						} else {
							Dialogues.startDialogue(player, GUIDORS_WIFE);
						}
					} else {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(-1, pY == y ? 0 : pY < y ? 1 : -1, true);
					}
					return true;
				}
			return false;
			case GATE:
			case GATE + 1:
				if((x == 3264 && y == 3405) || (x == 3264 && y == 3406)) {
					if(pX < 3264) {
						Dialogues.startDialogue(player, GATE+10000);
					} else {
						player.getActionSender().walkThroughGateEW(GATE + 1, GATE, 3264, 3406, 3264, 3405, 0, true);
						player.getActionSender().walkTo(-1, 0, true);	
					}
					return true;
				}
			return false;
			case CRATE:
				if(x == 2554 && y == 3327) {
					player.getActionSender().sendMessage("You search the crate...");
					player.getUpdateFlags().sendAnimation(832);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (!player.getInventory().ownsItem(DISTILLATOR)) {
								player.getActionSender().sendMessage("and find Elena's distillator.");
								player.getInventory().addItemOrDrop(new Item(DISTILLATOR));
								if(player.getQuestStage(40) == STEW_POISONED) {
									player.setQuestStage(40, DISTILLATOR_GET);
								}
							} else {
								player.getActionSender().sendMessage("You find nothing of interest.");
							}
							b.stop();
						}
						@Override
						public void stop() {
							player.setStopPacket(false);
						}
					}, 2);
					return true;
				}
			return false;
			case MOURNER_GATE_1:
			case MOURNER_GATE_2:
				if(x == 2551 && (y == 3326 || y == 3325)) {
					if(pX < 2552) {
						if(player.getInventory().playerHasItem(KEY)) {
							player.getInventory().removeItem(new Item(KEY));
							player.getActionSender().sendMessage("You unlock the gates and walk through them.");
							player.getActionSender().walkThroughDoubleDoor(MOURNER_GATE_1, MOURNER_GATE_2, 2551, 3326, 2551, 3325, 1);
							player.getActionSender().walkTo(1, 0, true);
						} else {
							player.getDialogue().sendStatement("These gates are locked firmly.");
						}
					} else {
						player.getActionSender().walkThroughDoubleDoor(MOURNER_GATE_1, MOURNER_GATE_2, 2551, 3326, 2551, 3325, 1);
						player.getActionSender().walkTo(-1, 0, true);
					}
					return true;
				}
			return false;
			case MOURNER_FENCE:
				if(x == 2541 && y == 3331) {
					if (player.getPosition().getX() < 2542) {
						Agility.crossObstacle(player, 2542, 3331, 756, 2, 0, 0);
						player.getActionSender().sendMessage("You squeeze through the fence.");
					} else {
						Agility.crossObstacle(player, 2541, 3331, 754, 2, 0, 0);
						player.getActionSender().sendMessage("You squeeze through the fence.");
					}
					return true;
				}
				return false;
			case MOURNER_DOOR:
				if (x == 2551 && y == 3328) {
					player.getActionSender().sendMessage("This door is locked.");
					return true;
				}
				if (x == 2551 && y == 3320) {
					if (player.getPosition().getY() < 3321) {
						if (player.getQuestStage(40) < STEW_POISONED) {
							player.getDialogue().setLastNpcTalk(357);
							player.getDialogue().sendNpcChat("Sorry, no one is allowed in at this point in time.");
							player.getDialogue().endDialogue();
						} else {
							Dialogues.startDialogue(player, MOURNER_DOOR + 10000);
						}
					} else {
						player.getActionSender().walkThroughDoor(MOURNER_DOOR, 2551, 3320, 0);
						player.getActionSender().walkTo(player.getPosition().getX() == 2551 ? 0 : player.getPosition().getX() < 2551 ? 1 : -1, -1, true);
					}
					return true;
				}
			return false;
			case ROPE_LADDER:
				if(player.getQuestStage(40) >= OVER_WALL) {
					climbRopeLadder(player);
					return true;
				}
				return false;
			case WATCHTOWER:
				if(player.getQuestVars().watchtowerDistracted) {
					player.getDialogue().sendStatement("The mourner guards look terribly distracted by pigeons.");
				} else {
					player.getDialogue().sendStatement("The mourner guards look ever vigilant.");
				}
				return true;
			case JERICO_CUPBOARD_OPEN:
				if(x == 2611 && y == 3326) {
					Dialogues.startDialogue(player, JERICO_CUPBOARD_OPEN + 10000);
					return true;
				}
			return true;
			case JERICO_CUPBOARD:
				if(x == 2611 && y == 3326) {
					player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
					ObjectHandler.getInstance().removeObject(x, y, 0, 10);
					new GameObject(JERICO_CUPBOARD_OPEN, x, y, 0, 1, 10, object, 999999);
					return true;
				}
			return false;
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
			case JERICO_CUPBOARD_OPEN:
				player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
				ObjectHandler.getInstance().removeObject(x, y, 0, 10);
				new GameObject(JERICO_CUPBOARD, x, y, 0, 1, 10, object, 999999);
				return true;
		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case KING_LATHAS:
				switch (player.getQuestStage(40)) {
					case TALK_TO_LATHAS:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("I assume that you are the King of East Ardougne?");
								return true;
							case 2:
								d.sendNpcChat("You assume correctly, but where do you get such", "impertinence.", ANGRY_1);
								return true;
							case 3:
								d.sendPlayerChat("I get it from finding out that the plague is a hoax.");
								return true;
							case 4:
								d.sendNpcChat("A hoax? I've never heard such a ridiculous thing...", ANGRY_1);
								return true;
							case 5:
								d.sendPlayerChat("I have evidence, from Guidor of Varrock.");
								return true;
							case 6:
								d.sendNpcChat("Ah... I see. Well, then you are right about the plague.", "But I did it for the good of my people.");
								return true;
							case 7:
								d.sendPlayerChat("When is it ever good to lie to people like that?");
								return true;
							case 8:
								d.sendNpcChat("When it protects them from a far greater danger, a", "fear too big to fathom.");
								return true;
							case 9:
								d.sendOption("I don't understand...", "Well I've wasted enough of my time here.");
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 11:
								d.sendNpcChat("Their King, Tyras, journeyed out to the West on a", "voyage of discovery. But he was captured by the Dark", "Lord.");
								return true;
							case 12:
								d.sendNpcChat("The Dark Lord agreed to spare his life, but only on", "one condition... That he would drink from the Chalice of", "Eternity.");
								return true;
							case 13:
								d.sendPlayerChat("So what happened?");
								return true;
							case 14:
								d.sendNpcChat("The chalice corrupted him. He joined forces with the", "Dark Lord, the embodiment of pure evil, banished all", "those years ago...", SAD);
								return true;
							case 15:
								d.sendNpcChat("And so I erected this wall, not just to protect my", "people, but to protect all the people of /v/scape.");
								return true;
							case 16:
								d.sendNpcChat("Now, with the King of West Ardougne, the Dark Lord", "has an ally on the inside.");
								return true;
							case 17:
								d.sendNpcChat("So, I'm sorry that I lied about the plague. I just hope", "that you can understand my reasons.");
								return true;
							case 18:
								d.sendPlayerChat("Well, at least I know now.", "But what can we do about it?");
								return true;
							case 19:
								d.sendNpcChat("Nothing at the moment, I'm waiting for my scouts to", "come back. They will tell us how we can get through", "the mountains.");
								return true;
							case 20:
								d.sendNpcChat("When this happens, can I count on your support?");
								return true;
							case 21:
								d.sendPlayerChat("Absolutely!");
								return true;
							case 22:
								d.sendNpcChat("Thank the gods! I give you permission to use my", "training area.");
								return true;
							case 23:
								d.sendNpcChat("It's located just to the north west of Ardougne, there", "you can prepare for the challenge ahead.");
								return true;
							case 24:
								d.sendPlayerChat("Ok. There's just one thing I don't understand, how", "do you know so much about King Tyras?");
								return true;
							case 25:
								d.sendNpcChat("How could I not? He was my brother.");
								return true;
							case 26:
								d.dontCloseInterface();
								QuestHandler.completeQuest(player, 40);
								return true;
						}
					return false;
				}
			return false;
			case GUIDOR:
				switch (d.getChatId()) {
					case 1:
						if(player.getQuestStage(40) == EXPERIMENT_SUCCESS) {
							d.sendPlayerChat("What should I do?");
							d.setNextChatId(25);
						} else {
							d.sendPlayerChat("Hello, you must be Guidor. I understand that you are", "unwell.");
						}
						return true;
					case 2:
						d.sendNpcChat("Is my wife asking priests to visit me now? I'm a man", "of science for god's sake.", ANGRY_1);
						return true;
					case 3:
						d.sendNpcChat("Ever since she heard rumors of a plague carrier", "travelling from Ardougne she's kept me under house", "arrest.");
						if(player.getQuestStage(40) < TOUCH_PAPER_GET)
							d.endDialogue();
						return true;
					case 4:
						d.sendNpcChat("Of course she means well, and I am quite frail now...", "So what brings you here?");
						return true;
					case 5:
						d.sendOption("I've come to ask your assistance in stopping a plague.", "I was just going to bless your room and I've done that now.");
						return true;
					case 6:
						if(optionId == 1) {
							d.sendPlayerChat(d.tempStrings[optionId - 1]);
						} else {
							d.sendPlayerChat("I was just going to bless your room,", "and I've done that now.");
							d.endDialogue();
						}
						return true;
					case 7:
						d.sendNpcChat("So you're the plague carrier!", DISTRESSED);
						return true;
					case 8:
						d.sendPlayerChat("I've been sent by your old pupil Elena, she's trying", "to halt the virus.");
						return true;
					case 9:
						d.sendNpcChat("Elena, eh?");
						return true;
					case 10:
						d.sendPlayerChat("Yes, she wants you to analyze it. You might be the", "only one who can help.");
						return true;
					case 11:
						d.sendNpcChat("Right then, sounds like we'd better get to work!");
						return true;
					case 12:
						if(player.getInventory().playerHasItem(PLAGUE_SAMPLE)) {
							d.sendPlayerChat("I have the plague sample.");
						} else {
							d.sendPlayerChat("Well, we could, but it seems I've forgotten", "the plague sample. I'll have to come back.");
							d.endDialogue();
						}
						return true;
					case 13:
						d.sendNpcChat("Now I'll be needing some liquid honey, some sulphuric", "broline, and then...");
						return true;
					case 14:
						d.sendPlayerChat("...some ethenea?");
						return true;
					case 15:
						d.sendNpcChat("Indeed!");
						return true;
					case 16:
						if(player.getInventory().playerHasItem(ETHENEA) && player.getInventory().playerHasItem(LIQUID_HONEY) && player.getInventory().playerHasItem(SULPHURIC_BROLINE)) {
							if(player.getInventory().playerHasItem(TOUCH_PAPER)) {
								d.sendNpcChat("Now I'll just apply these to the sample and... I don't", "get it... the touch paper has remained the same.");
								player.getInventory().removeItem(new Item(ETHENEA));
								player.getInventory().removeItem(new Item(LIQUID_HONEY));
								player.getInventory().removeItem(new Item(SULPHURIC_BROLINE));
								player.getInventory().removeItem(new Item(PLAGUE_SAMPLE));
								player.getInventory().removeItem(new Item(TOUCH_PAPER));
								player.setQuestStage(40, EXPERIMENT_SUCCESS);
							} else {
								d.sendNpcChat("Oh, well, you have the chemicals... but", "you're missing the touch paper! I need it", "to determine the result of the test. You can get", "some from Rimmington's chemist.");
								d.endDialogue();
							}
						} else {
							d.sendNpcChat("Oh, you don't seem to have all the chemicals", "needed, please come back when you do.");
							d.endDialogue();
						}
						return true;
					case 17:
						d.sendPlayerChat("That's why Elena wanted you to do it, because she", "wasn't sure what was happening.");
						return true;
					case 18:
						d.sendNpcChat("Well that's just it, nothing has happened.");
						return true;
					case 19:
						d.sendNpcChat("I don't know what this sample is, but it certainly isn't", "toxic.");
						return true;
					case 20:
						d.sendPlayerChat("So what about the plague?");
						return true;
					case 21:
						d.sendNpcChat("Don't you understand? There is no plague!", ANGRY_1);
						return true;
					case 22:
						d.sendNpcChat("I'm very sorry, I can see that you've worked very", "hard for this... but it seems that someone has been", "lying to you.");
						return true;
					case 23:
						d.sendNpcChat("The only question is...", "...why?");
						d.endDialogue();
						return true;
					case 25:
						d.sendNpcChat("I suggest you return to Elena, see what", "is really going behind all this 'plague'", "nonsense. Best of luck adventurer.");
						d.endDialogue();
						return true;
				}
			return false;
			case GUIDORS_WIFE:
				switch (d.getChatId()) {
					case 1:
						if(player.getEquipment().getId(Constants.CHEST) == PRIEST_GOWN_TOP && player.getEquipment().getId(Constants.LEGS) == PRIEST_GOWN_BOTTOM) {
							d.sendNpcChat("Oh, thank heavens you are here! Go", "on in and see my husband! Quickly!", DISTRESSED);
							d.endDialogue();
						} else {
							d.sendNpcChat("Oh, my poor husband! I just know he's", "been exposed to this plague they say is", "being carried around!", SAD);
						}
						return true;
					case 2:
						d.sendPlayerChat("What?");
						return true;
					case 3:
						d.sendNpcChat("He's been infected by the plague! I'm", "waiting for the priest to get here, only he", "can enter that room!", SAD);
						return true;
					case 4:
						d.sendPlayerChat("So, your husband does have the plague?", "Is he sick?");
						return true;
					case 5:
						d.sendNpcChat("Well, not quite... But surely he will", "get it! All he does it talk about it, and", "when I heard there was someone in the area", "carrying the plague, I locked him up!", SAD);
						return true;
					case 6:
						d.sendPlayerChat("Sounds a bit fanatical to me. Whatever, I'll", "leave you to it.");
						d.endDialogue();
						return true;
				}
			return false;
			case GATE + 10000:
				d.setLastNpcTalk(368);
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("Halt. I need to conduct a search on you. There have", "been reports of someone bringing a virus into Varrock.");
						return true;
					case 2:
						d.sendStatement("The guard searches you.");
						return true;
					case 3:
						if (!player.getInventory().playerHasItem(ETHENEA) && !player.getInventory().playerHasItem(LIQUID_HONEY) && !player.getInventory().playerHasItem(SULPHURIC_BROLINE)) {
							d.sendNpcChat("You may now pass.");
							
						} else {
							d.sendNpcChat("You have dangerous chemicals in your backpack.", "Chemicals related directly to plague testing.", "I'll be taking these off your hands.");
							d.endDialogue();
							if(player.getInventory().playerHasItem(ETHENEA))
								player.getInventory().removeItem(new Item(ETHENEA));
							if(player.getInventory().playerHasItem(LIQUID_HONEY))
								player.getInventory().removeItem(new Item(LIQUID_HONEY));
							if(player.getInventory().playerHasItem(SULPHURIC_BROLINE))
								player.getInventory().removeItem(new Item(SULPHURIC_BROLINE));
						}
						return true;
					case 4:
						d.endDialogue();
						player.getActionSender().removeInterfaces();
						player.getActionSender().walkThroughGateEW(GATE + 1, GATE, 3264, 3406, 3264, 3405, 0, true);
						player.getActionSender().walkTo(1, 0, true);
						return true;
				}
			return false;
			case HOPS_VARROCK:
				switch (player.getQuestStage(40)) {
					case TOUCH_PAPER_GET:
						switch (d.getChatId()) {
							case 1:
								if (player.getQuestVars().getVialGivenToHops() != 0) {
									d.sendPlayerChat("Hello, how was your journey?");
									d.setNextChatId(7);
								} else {
									d.sendPlayerChat("Hello, how was your journey?");
									d.setNextChatId(20);
								}
							return true;
							case 7:
								if(player.getQuestVars().getVialGivenToHops() == 3) {
									d.sendNpcChat("Pretty thirst-inducing actually...");
								} else {
									d.sendNpcChat("About that... You see, the chemical you handed me", "ended up being very thirst quenching. The", "journey was long and the thirst was so bad, I", "opened up the vial and drank it....", SAD);
									d.endDialogue();
									player.getQuestVars().setVialGivenToHops(0);
								}
								return true;
							case 8:
								d.sendPlayerChat("Please tell me you haven't drunk the contents...");
								return true;
							case 9:
								d.sendNpcChat("Oh the gods no! What do you take me for!", ANGRY_1);
								return true;
							case 10:
								d.sendNpcChat("Here's your vial anyway.");
								return true;
							case 11:
								if(player.getInventory().canAddItem(new Item(SULPHURIC_BROLINE))) {
									d.sendGiveItemNpc("Hops hands you the sulphuric broline.", new Item(SULPHURIC_BROLINE));
									player.getInventory().addItem(new Item(SULPHURIC_BROLINE));
									player.getQuestVars().setVialGivenToHops(0);
								} else {
									d.sendNpcChat("You don't seem to have the inventory space.", "Come back when you do, I'll still have", "this here sulphur stuff.");
									d.endDialogue();
								}
								return true;
							case 12:
								d.sendNpcChat("Next time give me something more palatable... I couldn't", "even try to drink that substance without getting", "sick to my stomach...", SAD);
								return true;
							case 13:
								d.sendPlayerChat("That was the idea.");
								d.endDialogue();
								return true;
							case 20:
								d.sendNpcChat("Eh? What journey?");
								return true;
							case 21:
								d.sendPlayerChat("Er, did I not give you a vial earlier near", "Rimmington?");
								return true;
							case 22:
								d.sendNpcChat("I certainly don't remember anything like that.");
								d.endDialogue();
								return true;
						}
					return false;
				}
			return false;
			case HOPS:
				switch (player.getQuestStage(40)) {
					case TOUCH_PAPER_GET:
						switch (d.getChatId()) {
							case 1:
								if(player.getQuestVars().getVialGivenToHops() == 0) {
									d.sendPlayerChat("Hi, I've got something for you to take to Varrock.");
								} else {
									d.sendOption("Could I have that vial I gave you back?", "I'll see you later in the Dancing Donkey Inn.");
									d.setNextChatId(10);
								}	
								return true;
							case 2:
								d.sendNpcChat("Sounds like pretty thirsty work.");
								return true;
							case 3:
								d.sendPlayerChat("Well, there's an Inn in Varrock if you're desperate.");
								return true;
							case 4:
								d.sendNpcChat("Don't worry, I'm a pretty resourceful fellow you know.");
								return true;
							case 5:
								d.sendOption("Here's the vial of ethenea.", "Here's the vial of liquid honey.", "Here's the vial of sulphuric broline.");
								return true;
							case 6:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								player.setTempInteger(optionId);
								return true;
							case 7:
								switch(player.getTempInteger()) {
									case 1:
										if(player.getInventory().playerHasItem(ETHENEA)) {
											d.sendGiveItemNpc("You hand the vial to Hops.", new Item(ETHENEA));
											player.getInventory().removeItem(new Item(ETHENEA));
											player.getQuestVars().setVialGivenToHops(1);
										} else {
											d.sendPlayerChat("Er, hold on, I don't seem to have the vial", "with me.", SAD);
											d.endDialogue();
										}
										return true;
									case 2:
										if(player.getInventory().playerHasItem(LIQUID_HONEY)) {
											d.sendGiveItemNpc("You hand the vial to Hops.", new Item(LIQUID_HONEY));
											player.getInventory().removeItem(new Item(LIQUID_HONEY));
											player.getQuestVars().setVialGivenToHops(2);
										} else {
											d.sendPlayerChat("Er, hold on, I don't seem to have the vial", "with me.", SAD);
											d.endDialogue();
										}
										return true;
									case 3:
										if(player.getInventory().playerHasItem(SULPHURIC_BROLINE)) {
											d.sendGiveItemNpc("You hand the vial to Hops.", new Item(SULPHURIC_BROLINE));
											player.getInventory().removeItem(new Item(SULPHURIC_BROLINE));
											player.getQuestVars().setVialGivenToHops(3);
										} else {
											d.sendPlayerChat("Er, hold on, I don't seem to have the vial", "with me.", SAD);
											d.endDialogue();
										}
										return true;
								}
							case 8:
								d.sendPlayerChat("Right. I'll see you later in the Dancing Donkey Inn.");
								d.endDialogue();
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 11:
								d.sendNpcChat("Sure.");
								return true;
							case 12:
								int vial = player.getQuestVars().getVialGivenToHops();
								int vialItem = vial == 1 ? ETHENEA : vial == 2 ? LIQUID_HONEY : SULPHURIC_BROLINE;
								d.sendGiveItemNpc("Hops hands you the vial back.", new Item(vialItem));
								d.endDialogue();
								player.getQuestVars().setVialGivenToHops(0);
								player.getInventory().addItemOrDrop(new Item(vialItem));
								return true;
								
								
						}
					return false;
				}
			return false;
			case DA_VINCI:
				switch (player.getQuestStage(40)) {
					case TOUCH_PAPER_GET:
						switch (d.getChatId()) {
							case 1:
								if (player.getPosition().getX() > 3200) {
									if (player.getQuestVars().getVialGivenToDaVinci() != 0) {
										d.sendNpcChat("Hello again. I hope your journey was as pleasant", "as mine.");
										d.setNextChatId(10);
									} else {
										d.sendPlayerChat("Hi, thanks for doing that.");
										d.setNextChatId(20);
									}
								} else {
									if (player.getQuestVars().getVialGivenToDaVinci() == 0) {
										d.sendPlayerChat("Hello, I hear you're an errand boy for the chemist.");
									} else {
										d.sendOption("Could I have that vial I gave you back?", "I'll see you later in the Dancing Donkey Inn.");
										d.setNextChatId(25);
									}
								}
								return true;
							case 2:
								d.sendNpcChat("Well that's my job yes. But I don't necessarily define", "my identity in such black and white terms.");
								return true;
							case 3:
								d.sendPlayerChat("Good for you. Now can you take a vial to Varrock for", "me?");
								return true;
							case 4:
								d.sendNpcChat("Go on then.");
								return true;
							case 5:
								d.sendOption("Here's the vial of ethenea.", "Here's the vial of liquid honey.", "Here's the vial of sulphuric broline.");
								return true;
							case 6:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								player.setTempInteger(optionId);
								return true;
							case 7:
								switch(player.getTempInteger()) {
									case 1:
										if(player.getInventory().playerHasItem(ETHENEA)) {
											d.sendGiveItemNpc("You hand the vial to Da Vinci.", new Item(ETHENEA));
											player.getInventory().removeItem(new Item(ETHENEA));
											player.getQuestVars().setVialGivenToDaVinci(1);
										} else {
											d.sendPlayerChat("Er, hold on, I don't seem to have the vial", "with me.", SAD);
											d.endDialogue();
										}
										return true;
									case 2:
										if(player.getInventory().playerHasItem(LIQUID_HONEY)) {
											d.sendGiveItemNpc("You hand the vial to Da Vinci.", new Item(LIQUID_HONEY));
											player.getInventory().removeItem(new Item(LIQUID_HONEY));
											player.getQuestVars().setVialGivenToDaVinci(2);
										} else {
											d.sendPlayerChat("Er, hold on, I don't seem to have the vial", "with me.", SAD);
											d.endDialogue();
										}
										return true;
									case 3:
										if(player.getInventory().playerHasItem(SULPHURIC_BROLINE)) {
											d.sendGiveItemNpc("You hand the vial to Da Vinci.", new Item(SULPHURIC_BROLINE));
											player.getInventory().removeItem(new Item(SULPHURIC_BROLINE));
											player.getQuestVars().setVialGivenToDaVinci(3);
										} else {
											d.sendPlayerChat("Er, hold on, I don't seem to have the vial", "with me.", SAD);
											d.endDialogue();
										}
										return true;
								}
							case 8:
								d.sendPlayerChat("Right. I'll see you later in the Dancing Donkey Inn.");
								d.endDialogue();
								return true;
							case 10:
								d.sendPlayerChat("I suppose the weather is quite nice.");
								return true;
							case 11:
								d.sendNpcChat("That it is.");
								return true;
							case 12:
								d.sendPlayerChat("Thanks, you've been a big help.");
								return true;
							case 13:
								if(player.getQuestVars().getVialGivenToDaVinci() == 1) {
									d.sendNpcChat("No problem.");
								} else {
									d.sendNpcChat("About that... You see, the chemical you handed me", "ended up being very colorful. I might have er, used it", "all to paint with. You'll have to find someone else", "to transport that chemical. It's far too artsy!");
									d.endDialogue();
									player.getQuestVars().setVialGivenToDaVinci(0);
								}
								return true;
							case 14:
								if(player.getInventory().canAddItem(new Item(ETHENEA))) {
									d.sendGiveItemNpc("Da Vinci hands you the ethenea.", new Item(ETHENEA));
									d.endDialogue();
									player.getInventory().addItem(new Item(ETHENEA));
									player.getQuestVars().setVialGivenToDaVinci(0);
								} else {
									d.sendNpcChat("You don't seem to have the inventory space.", "Come back when you do, I'll still have", "this here ethenea stuff.");
									d.endDialogue();
								}
								return true;
							case 20:
								d.sendNpcChat("Eh? Doing what?");
								return true;
							case 21:
								d.sendPlayerChat("Er, did I not give you a vial earlier near", "Rimmington?");
								return true;
							case 22:
								d.sendNpcChat("I certainly don't remember anything like that.");
								d.endDialogue();
								return true;
							case 25:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 26:
								d.sendNpcChat("Sure.");
								return true;
							case 27:
								int vial = player.getQuestVars().getVialGivenToDaVinci();
								int vialItem = vial == 1 ? ETHENEA : vial == 2 ? LIQUID_HONEY : SULPHURIC_BROLINE;
								d.sendGiveItemNpc("Da Vinci hands you the vial back.", new Item(vialItem));
								d.endDialogue();
								player.getQuestVars().setVialGivenToDaVinci(0);
								player.getInventory().addItemOrDrop(new Item(vialItem));
								return true;
								
						}
					return false;
				}
			return false;
			case CHANCY:
				switch (player.getQuestStage(40)) {
					case TOUCH_PAPER_GET:
						switch (d.getChatId()) {
							case 1:
								if (player.getPosition().getX() > 3200) {
									if (player.getQuestVars().getVialGivenToChancy() != 0) {
										d.sendPlayerChat("Hi, thanks for doing that.");
										d.setNextChatId(10);
									} else {
										d.sendPlayerChat("Hi, thanks for doing that.");
										d.setNextChatId(20);
									}
								} else {
									if (player.getQuestVars().getVialGivenToChancy() == 0) {
										d.sendPlayerChat("Hello, I've got a vial for you to take to Varrock.");
									} else {
										d.sendOption("Could I have that vial I gave you back?", "I'll see you later in the Dancing Donkey Inn.");
										d.setNextChatId(25);
									}
								}
								return true;
							case 2:
								d.sendNpcChat("Tssch... that chemist asks for a lot for the wages he", "pays.");
								return true;
							case 3:
								d.sendPlayerChat("Maybe you should ask him for more money.");
								return true;
							case 4:
								d.sendNpcChat("Nah... I just use my initiative here and there.");
								return true;
							case 5:
								d.sendOption("Here's the vial of ethenea.", "Here's the vial of liquid honey.", "Here's the vial of sulphuric broline.");
								return true;
							case 6:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								player.setTempInteger(optionId);
								return true;
							case 7:
								switch(player.getTempInteger()) {
									case 1:
										if(player.getInventory().playerHasItem(ETHENEA)) {
											d.sendGiveItemNpc("You hand the vial to Chancy.", new Item(ETHENEA));
											player.getInventory().removeItem(new Item(ETHENEA));
											player.getQuestVars().setVialGivenToChancy(1);
										} else {
											d.sendPlayerChat("Er, hold on, I don't seem to have the vial", "with me.", SAD);
											d.endDialogue();
										}
										return true;
									case 2:
										if(player.getInventory().playerHasItem(LIQUID_HONEY)) {
											d.sendGiveItemNpc("You hand the vial to Chancy.", new Item(LIQUID_HONEY));
											player.getInventory().removeItem(new Item(LIQUID_HONEY));
											player.getQuestVars().setVialGivenToChancy(2);
										} else {
											d.sendPlayerChat("Er, hold on, I don't seem to have the vial", "with me.", SAD);
											d.endDialogue();
										}
										return true;
									case 3:
										if(player.getInventory().playerHasItem(SULPHURIC_BROLINE)) {
											d.sendGiveItemNpc("You hand the vial to Chancy.", new Item(SULPHURIC_BROLINE));
											player.getInventory().removeItem(new Item(SULPHURIC_BROLINE));
											player.getQuestVars().setVialGivenToChancy(3);
										} else {
											d.sendPlayerChat("Er, hold on, I don't seem to have the vial", "with me.", SAD);
											d.endDialogue();
										}
										return true;
								}
							case 8:
								d.sendPlayerChat("Right. I'll see you later in the Dancing Donkey Inn.");
								d.endDialogue();
								return true;
							case 10:
								if(player.getQuestVars().getVialGivenToChancy() == 2) {
									d.sendNpcChat("No problem.");
								} else {
									d.sendNpcChat("About that... You see, the chemical you handed me", "ended up being very valuable. I might have er, gambled", "it away. I'm sorry. You'll have to get another I'm", "afraid.");
									d.endDialogue();
									player.getQuestVars().setVialGivenToChancy(0);
								}
								return true;
							case 11:
								if(player.getInventory().canAddItem(new Item(LIQUID_HONEY))) {
									d.sendGiveItemNpc("Chancy hands you the liquid honey.", new Item(LIQUID_HONEY));
									player.getInventory().addItem(new Item(LIQUID_HONEY));
									player.getQuestVars().setVialGivenToChancy(0);
								} else {
									d.sendNpcChat("You don't seem to have the inventory space.", "Come back when you do, I'll still have", "this here honey stuff.");
									d.endDialogue();
									
								}
								return true;
							case 12:
								d.sendNpcChat("Next time give me something more valuable... I couldn't", "get anything for this on the black market.", SAD);
								return true;
							case 13:
								d.sendPlayerChat("That was the idea.");
								d.endDialogue();
								return true;
							case 20:
								d.sendNpcChat("Eh? Doing what?");
								return true;
							case 21:
								d.sendPlayerChat("Er, did I not give you a vial earlier near", "Rimmington?");
								return true;
							case 22:
								d.sendNpcChat("I certainly don't remember anything like that.");
								d.endDialogue();
								return true;
							case 25:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 26:
								d.sendNpcChat("Sure.");
								return true;
							case 27:
								int vial = player.getQuestVars().getVialGivenToChancy();
								int vialItem = vial == 1 ? ETHENEA : vial == 2 ? LIQUID_HONEY : SULPHURIC_BROLINE;
								d.sendGiveItemNpc("Chancy hands you the vial back.", new Item(vialItem));
								d.endDialogue();
								player.getQuestVars().setVialGivenToChancy(0);
								player.getInventory().addItemOrDrop(new Item(vialItem));
								return true;
						}
					return false;
				}
			return false;
			case CHEMIST:
				switch (player.getQuestStage(40)) {
					case TOUCH_PAPER_GET:
						switch (d.getChatId()) {
							case 1:
								if(!player.getInventory().ownsItem(TOUCH_PAPER)) {
									d.sendGiveItemNpc("The chemist gives you some touch paper.", new Item(TOUCH_PAPER));
									d.endDialogue();
									player.getInventory().addItemOrDrop(new Item(TOUCH_PAPER));
								} else {
									d.sendPlayerChat("What do I need to know again, about the", "vials?");
								}
								return true;
							case 2:
								d.sendNpcChat("They're doing spot checks in Varrock because", "of a rumor that someone is carrying a sample of", "the plague. It's a pharmaceutical disaster!", DISTRESSED);
								return true;
							case 3:
								d.sendPlayerChat("Ah, that's right...", "So am I going to be ok carrying these three", "vials with me?");
								return true;
							case 4:
								d.sendNpcChat("With touch paper as well? You're asking for trouble.", "You'd better use my errand boys, outside. Give them", "a vial each.");
								return true;
							case 5:
								d.sendNpcChat("They're not the most reliable people in the world. One's", "a painter, one's a gambler, and one's a drunk. Still if", "you pay peanuts you'll get monkeys, right?");
								return true;
							case 6:
								d.sendNpcChat("It's better than entering Varrock with half a laboratory", "in your napsack.");
								return true;
							case 7:
								d.sendPlayerChat("Ok, thanks for your help.");
								d.endDialogue();
								return true;
						}
					return false;
					case NEED_TOUCH_PAPER:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Sorry, I'm afraid we're just closing now. You'll have to", "come back another time.");
								return true;
							case 2:
								d.sendOption("This can't wait, I'm carrying a plague sample.", "It's ok, I'm Elena's friend.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 1)
									d.setNextChatId(20);
								return true;
							case 4:
								d.sendNpcChat("Oh, well that's different then. Must be pretty important", "to come all this way.");
								return true;
							case 5:
								d.sendNpcChat("How's everyone doing there anyway? Wasn't there", "some plague scare?");
								return true;
							case 6:
								d.sendOption("I need some more touch paper for this plague sample.", "I just need some touch paper for a guy called Guidor.");
								return true;
							case 7:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 1)
									d.setNextChatId(20);
								return true;
							case 8:
								d.sendNpcChat("Guidor? This one's on me then... the poor guy. Sorry", "for the interrogation.");
								return true;
							case 9:
								d.sendNpcChat("It's just that there've been rumors of a man travelling", "with the plague on him.");
								return true;
							case 10:
								d.sendNpcChat("They're even doing spot checks in Varrock! It's a", "pharmaceutical disaster!", DISTRESSED);
								return true;
							case 11:
								d.sendPlayerChat("Oh, right... so am I going to be ok carrying these three", "vials with me?");
								return true;
							case 12:
								d.sendNpcChat("With touch paper as well? You're asking for trouble.", "You'd better use my errand boys, outside. Give them", "a vial each.");
								return true;
							case 13:
								d.sendNpcChat("They're not the most reliable people in the world. One's", "a painter, one's a gambler, and one's a drunk. Still if", "you pay peanuts you'll get monkeys, right?");
								return true;
							case 14:
								d.sendNpcChat("It's better than entering Varrock with half a laboratory", "in your napsack.");
								return true;
							case 15:
								d.sendPlayerChat("Ok, thanks for your help. I know Elena appreciates it.");
								return true;
							case 16:
								d.sendNpcChat("Yes, well don't stand around here gassing. You'd better", "hurry if you want to see Guidor... He won't be around", "for much longer.");
								return true;
							case 17:
								d.sendGiveItemNpc("The chemist gives you some touch paper.", new Item(TOUCH_PAPER));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(TOUCH_PAPER));
								player.setQuestStage(40, TOUCH_PAPER_GET);
								return true;
							case 20:
								if(player.getInventory().playerHasItem(PLAGUE_SAMPLE)) {
									d.sendNpcChat("Plague sample?! What? Give that to me right now!", ANGRY_1);
									d.endDialogue();
									player.getInventory().removeItem(new Item(PLAGUE_SAMPLE));
								} else {
									d.sendNpcChat("What are you talking about? You're not", "carrying a plague sample? Are you trying to", "make a fool out of me?!", ANGRY_1);
									d.endDialogue();
								}
								return true;
						}
					return false;
				}
			return false;
			case NURSE_SARAH:
				switch (player.getQuestStage(40)) {
					default:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello nurse.");
								return true;
							case 2:
								d.sendNpcChat("Oh, hello there.");
								return true;
							case 3:
								d.sendNpcChat("I'm afraid I can't stop and talk, there are", "many plague victims that need my attention.", SAD);
								d.endDialogue();
								return true;
						}
					return false;
					case STEW_POISONED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello nurse.");
								return true;
							case 2:
								d.sendNpcChat("Oh, hello there.");
								return true;
							case 3:
								d.sendNpcChat("I'm afraid I can't stop and talk, a group of mourners", "have become ill with food poisoning. I need to go over", "and see what I can do.");
								return true;
							case 4:
								d.sendPlayerChat("Hmmm, strange that!", Dialogues.EVIL_LAUGH_SHORT);
								d.endDialogue();
								return true;
						}
						return false;
				}
			case MOURNER_DOOR + 10000:
				d.setLastNpcTalk(357);
				switch (player.getQuestStage(this.getQuestID())) {
					case STEW_POISONED:
						switch (d.getChatId()) {
							case 1:
								if(player.getEquipment().getId(Constants.CHEST) == DOCTORS_GOWN) {
									d.sendNpcChat("In you go doc.");
									d.setNextChatId(5);
								} else {
									d.sendNpcChat("Stay away from there.");
								}
								return true;
							case 2:
								d.sendPlayerChat("Why?");
								return true;
							case 3:
								d.sendNpcChat("Several mourners are ill with food poisoning, we're", "waiting for a doctor.");
								d.endDialogue();
								return true;
							case 5:
								d.endDialogue();
								player.getActionSender().removeInterfaces();
								player.setStopPacket(true);
								if (player.getPosition().equals(new Position(2551, 3320, 0))) {
									player.getActionSender().walkThroughDoor(MOURNER_DOOR, 2551, 3320, 0);
									player.getActionSender().walkTo(player.getPosition().getX() == 2551 ? 0 : player.getPosition().getX() < 2551 ? 1 : -1, 1, true);
								} else {
									CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
										int count = 0;
										@Override
										public void execute(CycleEventContainer b) {
											count++;
											player.walkTo(new Position(2551, 3320, 0), true);
											if (player.getPosition().equals(new Position(2551, 3320, 0))) {
												player.getActionSender().walkThroughDoor(MOURNER_DOOR, 2551, 3320, 0);
												player.getActionSender().walkTo(player.getPosition().getX() == 2551 ? 0 : player.getPosition().getX() < 2551 ? 1 : -1, 1, true);
											}
											if (count >= 3) {
												b.stop();
											}
										}

										@Override
										public void stop() {
											player.setStopPacket(false);
										}
									}, 1);
								}
								return true;
						}
					return false;
				}
			return false;
			case KILRON:
				switch (player.getQuestStage(this.getQuestID())) {
					case TALK_TO_LATHAS:
					case EXPERIMENT_SUCCESS:
					case TOUCH_PAPER_GET:
					case NEED_TOUCH_PAPER:
					case DISTILLATOR_GET:
					case STEW_POISONED:
					case OVER_WALL:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Do you wish to climb back over?");
								return true;
							case 2:
								d.sendOption("Yes.", "Not right now.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 4:
								if (player.getEquipment().getId(Constants.HAT) != PlagueCity.GAS_MASK && player.getQuestStage(40) < EXPERIMENT_SUCCESS) {
									d.sendNpcChat("You should probably wear your gas mask", "before we let you over, adventurer.");
									d.endDialogue();
								} else {
									player.getActionSender().removeInterfaces();
									player.getActionSender().sendMessage("Kilron calls to his associate.");
									World.getNpcs()[World.getNpcIndex(KILRON)].getUpdateFlags().sendFaceToDirection(new Position(2559, 3266, 0));
									player.setStopPacket(true);
									CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
										@Override
										public void execute(CycleEventContainer b) {
											b.stop();
										}

										@Override
										public void stop() {
											player.setStopPacket(false);
											Dialogues.sendDialogue(player, KILRON, 5, 0);
										}
									}, 5);
								}
								return true;
							case 5:
								d.sendNpcChat("Omart!");
								World.getNpcs()[World.getNpcIndex(OMART)].setFollowingEntity(World.getNpcs()[World.getNpcIndex(KILRON)]);
								return true;
							case 6:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("He throws one end of the rope ladder over the wall.");
								ObjectHandler.getInstance().removeObject(2557, 3267, 0, 10);
								ObjectHandler.getInstance().removeObject(2558, 3267, 0, 10);
								new GameObject(ROPE_LADDER, 2557, 3267, 0, 3, 10, 2066, 999999);
								new GameObject(ROPE_LADDER, 2558, 3267, 0, 1, 10, 2066, 999999);
								ObjectHandler.getInstance().removeClip(2559, 3267, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3268, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3269, 0, 10, 0);
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										Biohazard.climbRopeLadder(player);
									}
								}, 5);
								return true;
						}
					return false;
				}
			case OMART:
				switch (player.getQuestStage(this.getQuestID())) {
					case TALK_TO_LATHAS:
					case EXPERIMENT_SUCCESS:
					case TOUCH_PAPER_GET:
					case NEED_TOUCH_PAPER:
					case DISTILLATOR_GET:
					case STEW_POISONED:
					case OVER_WALL:
						switch(d.getChatId()) {
							case 1:
								d.sendNpcChat("Do you wish to climb back over?");
								return true;
							case 2:
								d.sendOption("Yes.", "Not right now.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 4:
								if (player.getEquipment().getId(Constants.HAT) != PlagueCity.GAS_MASK && player.getQuestStage(40) < EXPERIMENT_SUCCESS) {
									d.sendNpcChat("You should probably wear your gas mask", "before we let you over, adventurer.");
									d.endDialogue();
								} else {
									player.getActionSender().removeInterfaces();
									player.getActionSender().sendMessage("Omart calls to his associate.");
									World.getNpcs()[World.getNpcIndex(OMART)].getUpdateFlags().sendFaceToDirection(new Position(2556, 3266, 0));
									player.setStopPacket(true);
									CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
										@Override
										public void execute(CycleEventContainer b) {
											b.stop();
										}

										@Override
										public void stop() {
											player.setStopPacket(false);
											Dialogues.sendDialogue(player, OMART, 5, 0);
										}
									}, 5);
								}
								return true;
							case 5:
								d.sendNpcChat("Kilron!");
								World.getNpcs()[World.getNpcIndex(KILRON)].setFollowingEntity(World.getNpcs()[World.getNpcIndex(OMART)]);
								return true;
							case 6:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("He throws one end of the rope ladder over the wall.");
								ObjectHandler.getInstance().removeObject(2557, 3267, 0, 10);
								ObjectHandler.getInstance().removeObject(2558, 3267, 0, 10);
								new GameObject(ROPE_LADDER, 2557, 3267, 0, 3, 10, 2066, 999999);
								new GameObject(ROPE_LADDER, 2558, 3267, 0, 1, 10, 2066, 999999);
								ObjectHandler.getInstance().removeClip(2559, 3267, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3268, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3269, 0, 10, 0);
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										Biohazard.climbRopeLadder(player);
									}
								}, 5);
								return true;
							case 8:
								d.sendNpcChat("You must go now traveller.");
								return true;
							case 9:
								d.sendOption("Ok, lets do it.", "I'll be back soon.");
								return true;
							case 10:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2) {
									d.endDialogue();
								}
								return true;
							case 11:
								d.endDialogue();
								climbRopeLadder(player);
								return true;
						}
					return false;
					case DISTRACT_WATCHTOWER:
						switch (d.getChatId()) {
							case 1:
								if(player.getQuestVars().watchtowerDistracted) {
									d.sendNpcChat("Well done, the guards are having real trouble with those", "birds. You must go now, it's your only choice.");
								} else {
									d.sendNpcChat("We still need to distract the guards in the", "watchtower.");
									d.setNextChatId(10);
								}
								return true;
							case 2:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("Omart calls to his associate.");
								World.getNpcs()[World.getNpcIndex(OMART)].getUpdateFlags().sendFaceToDirection(new Position(2556, 3266, 0));
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										Dialogues.sendDialogue(player, OMART, 3, 0);
									}
								}, 5);
								return true;
							case 3:
								d.sendNpcChat("Kilron!");
								World.getNpcs()[World.getNpcIndex(KILRON)].setFollowingEntity(World.getNpcs()[World.getNpcIndex(OMART)]);
								return true;
							case 4:
								player.getActionSender().removeInterfaces();
								player.getActionSender().sendMessage("He throws one end of the rope ladder over the wall.");
								player.setQuestStage(40, OVER_WALL);
								ObjectHandler.getInstance().removeObject(2557, 3267, 0, 10);
								ObjectHandler.getInstance().removeObject(2558, 3267, 0, 10);
								new GameObject(ROPE_LADDER, 2557, 3267, 0, 3, 10, 2066, 999999);
								new GameObject(ROPE_LADDER, 2558, 3267, 0, 1, 10, 2066, 999999);
								ObjectHandler.getInstance().removeClip(2559, 3267, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3268, 0, 10, 0);
								ObjectHandler.getInstance().removeClip(2559, 3269, 0, 10, 0);
								player.setStopPacket(true);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										b.stop();
									}

									@Override
									public void stop() {
										player.setStopPacket(false);
										World.getNpcs()[World.getNpcIndex(KILRON)].setFollowingEntity(null);
										Dialogues.sendDialogue(player, OMART, 8, 0);
									}
								}, 5);
								return true;
							case 10:
								d.sendPlayerChat("How?");
								return true;
							case 11:
								d.sendNpcChat("Try asking Jerico, if he's not too busy with his pigeons.", "I'll be waiting here for you.");
								d.endDialogue();
								return true;
						}
					return false;
					case TALKED_TO_JERICO:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Omart, Jerico said you might be able to help me.");
								return true;
							case 2:
								d.sendNpcChat("He informed me of your problem traveller. I would be", "glad to help, I have a rope ladder and my associate,", "Kilron, is waiting on the other side.");
								return true;
							case 3:
								d.sendPlayerChat("Good stuff.");
								return true;
							case 4:
								d.sendNpcChat("Unfortunately, we can't risk it with the watch tower so", "close. So first we need to distract the guards in the", "tower.");
								return true;
							case 5:
								d.sendPlayerChat("How?");
								return true;
							case 6:
								d.sendNpcChat("Try asking Jerico, if he's not too busy with his pigeons.", "I'll be waiting here for you.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), DISTRACT_WATCHTOWER);
								return true;
						}
					return false;
				}
			return false;
			case JERICO_CUPBOARD_OPEN + 10000:
				switch(d.getChatId()) {
					case 1:
						d.sendGiveItemNpc("The cupboard is full of birdfeed.", new Item(BIRD_FEED));
						player.getInventory().addItem(new Item(BIRD_FEED));
						return true;
					case 2:
						d.sendPlayerChat("Mmm, birdfeed! Now what could I do with that?");
						d.endDialogue();
						return true;
				}
			return false;
			case JERICO:
				switch (player.getQuestStage(this.getQuestID())) {
					case DISTRACT_WATCHTOWER:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Jerico, I need help distracted the mourners", "on the watchtower so that Omart can throw the", "rope ladder over the wall without getting caught.");
								return true;
							case 2:
								d.sendNpcChat("Hmm. Tricky, tricky...", "Let me think for a moment.");
								return true;
							case 3:
								d.sendStatement("Jerico stares off into the distance.");
								return true;
							case 4:
								d.sendStatement("Jerico starts to count on his fingers, mumbling", "softly to himself.");
								return true;
							case 5:
								d.sendNpcChat("Ah! I've got it. It's rather simple,", "really. Grab a pigeon cage from around the rear", "of my house. Then go into my cupboard inside and", "grab a pack of bird feed. Once you have");
								return true;
							case 6:
								d.sendNpcChat("both of these, head back to the watchtower.", "Once there, throw the feed onto the tower itself. Do", "it carefully! You don't want the mourners to notice. After", "you throw the feed, release the pigeons near");
								return true;
							case 7:
								d.sendNpcChat("the tower. Knowing my birds, they'll go crazy for", "the feed, and swarm the tower. The guards will be", "too busy fending them off to notice anything but", "how majestic my birds are!", LAUGHING);
								return true;
							case 8:
								d.sendPlayerChat("Bird feed, pigeons... got it. Thanks Jerico.");
								d.endDialogue();
								return true;
						}
					return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendPlayerChat("Hello Jerico.");
								return true;
							case 2:
								d.sendNpcChat("Hello, I've been expecting you. Elena tells me you need", "to cross the wall.");
								return true;
							case 3:
								d.sendPlayerChat("That's right.");
								return true;
							case 4:
								d.sendNpcChat("My messenger pigeons help me communicate with", "friends over the wall.");
								return true;
							case 5:
								d.sendNpcChat("I have arranged for two friends to aid you with a rope", "ladder. Omart is waiting for you at the south end of the", "wall.");
								return true;
							case 6:
								d.sendNpcChat("But be careful, if the mourners catch you the", "punishment will be severe.");
								return true;
							case 7:
								d.sendPlayerChat("Thanks Jerico.");
								d.endDialogue();
								player.setQuestStage(this.getQuestID(), TALKED_TO_JERICO);
								return true;
						}
					return false;
				}
			return false;
			case ELENA:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case EXPERIMENT_SUCCESS:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("You're back! So what did Guidor say?");
								return true;
							case 2:
								d.sendPlayerChat("Nothing.");
								return true;
							case 3:
								d.sendNpcChat("What?");
								return true;
							case 4:
								d.sendPlayerChat("He said that there is no plague.");
								return true;
							case 5:
								d.sendNpcChat("So what, this thing has all been a big hoax?", DISTRESSED);
								return true;
							case 6:
								d.sendPlayerChat("Or maybe we're about to uncover something huge.");
								return true;
							case 7:
								d.sendNpcChat("Then I think this thing may be bigger than both of us.");
								return true;
							case 8:
								d.sendPlayerChat("What do you mean?");
								return true;
							case 9:
								d.sendNpcChat("I mean you need to go right to the top... You need to", "see the King of East Ardougne!");
								d.endDialogue();
								player.setQuestStage(40, TALK_TO_LATHAS);
								return true;
						}
					return false;
					case TOUCH_PAPER_GET:
					case NEED_TOUCH_PAPER:
						switch (d.getChatId()) {
							case 1:
								if (player.getInventory().ownsItem(PLAGUE_SAMPLE)) {
									if (player.getInventory().ownsItem(ETHENEA) && player.getInventory().ownsItem(LIQUID_HONEY) && player.getInventory().ownsItem(SULPHURIC_BROLINE)) {
										d.sendNpcChat("First, you'll need some more touch paper. Go and", "see the chemist in Rimmington.");
										d.setNextChatId(12);
									} else {
										d.sendPlayerChat("I, er, lost some of the vials...", SAD);
									}
								} else {
									d.sendGiveItemNpc("Elena hands you a sample of the plague.", new Item(PLAGUE_SAMPLE));
									d.endDialogue();
									player.getInventory().addItemOrDrop(new Item(PLAGUE_SAMPLE));
								}
								return true;
							case 2:
								d.sendNpcChat("Sigh... I knew this was bound to happen.", "Not to worry, I have replacements.");
								return true;
							case 3:
								d.sendNpcChat("Which vial do you need?");
								return true;
							case 4:
								d.sendOption("The ethenea.", "The liquid honey.", "The sulphuric broline.");
								return true;
							case 5:
								switch(optionId) {
									case 1:
										if(player.getInventory().ownsItem(ETHENEA)) {
											d.sendPlayerChat("Hold on, maybe I don't need a replacement", "ethenea, let me look through my items again.");
											d.endDialogue();
										} else {
											d.sendPlayerChat(d.tempStrings[0]);
										}
										return true;
									case 2:
										if(player.getInventory().ownsItem(LIQUID_HONEY)) {
											d.sendPlayerChat("Hold on, maybe I don't need a replacement of", "the honey, let me look through my items again.");
											d.endDialogue();
										} else {
											d.sendPlayerChat(d.tempStrings[1]);
											d.setNextChatId(7);
										}
										return true;
									case 3:
										if(player.getInventory().ownsItem(SULPHURIC_BROLINE)) {
											d.sendPlayerChat("Hold on, maybe I don't need a replacement of", "the sulphuric broline, let me look through", "my items again.");
											d.endDialogue();
										} else {
											d.sendPlayerChat(d.tempStrings[1]);
											d.setNextChatId(8);
										}
										return true;
								}
							case 6:
								d.sendGiveItemNpc("Elena hands you a vial of ethenea.", new Item(ETHENEA));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(ETHENEA));
								return true;
							case 7:
								d.sendGiveItemNpc("Elena hands you a vial of liquid honey.", new Item(LIQUID_HONEY));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(LIQUID_HONEY));
								return true;
							case 8:
								d.sendGiveItemNpc("Elena hands you a vial of sulphuric broline.", new Item(SULPHURIC_BROLINE));
								d.endDialogue();
								player.getInventory().addItemOrDrop(new Item(SULPHURIC_BROLINE));
								return true;
							case 11:
								d.sendNpcChat("But first you'll need some more touch paper. Go and", "see the chemist in Rimmington.");
								return true;
							case 12:
								d.sendNpcChat("Just don't get into any fights, and be careful who you", "speak to.");
								return true;
							case 13:
								d.sendNpcChat("The plague sample is fragile, and plague carriers", "don't tend to be too popular.");
								d.endDialogue();
								return true;
						}
						return false;
					case DISTILLATOR_GET:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("So, have you managed to retrieve my distillator?");
								return true;
							case 2:
								if(player.getInventory().playerHasItem(DISTILLATOR)) {
									d.sendPlayerChat("Yes, here it is!");
								} else {
									d.sendPlayerChat("Not yet I'm afraid...", SAD);
									d.endDialogue();
								}
								return true;
							case 3:
								d.sendNpcChat("You have? That's great! Now can you pass me those", "reaction agents please?", HAPPY);
								return true;
							case 4:
								d.sendPlayerChat("Those look pretty fancy.");
								return true;
							case 5:
								d.sendNpcChat("Well, yes and no. The liquid honey isn't worth much,", "but the others are. Especially this colorless ethenea. Be", "careful with the sulphuric broline, it's highly poisonous.");
								return true;
							case 6:
								d.sendPlayerChat("You're not kidding, I can smell it from here!");
								return true;
							case 7:
								d.sendStatement("You hand Elena the distillator and an assortment of vials.", "Elena puts the agents through the distillator.");
								return true;
							case 8:
								d.sendNpcChat("I don't understand... the touch paper hasn't changed", "color at all...", DISTRESSED);
								return true;
							case 9:
								d.sendNpcChat("You'll need to go and see my old mentor Guidor. He", "lives in Varrock. Take these vials and this sample to", "him.");
								return true;
							case 10:
								if(player.getInventory().getItemContainer().freeSlots() >= 3) {
									d.sendGiveItemNpc("Elena gives you three vials and a sample", "in a tin container.", new Item(ETHENEA), new Item(PLAGUE_SAMPLE));
									player.getInventory().replaceItemWithItem(new Item(DISTILLATOR), new Item(PLAGUE_SAMPLE));
									player.getInventory().addItem(new Item(ETHENEA));
									player.getInventory().addItem(new Item(LIQUID_HONEY));
									player.getInventory().addItem(new Item(SULPHURIC_BROLINE));
									player.setQuestStage(40, NEED_TOUCH_PAPER);
								} else {
									d.sendNpcChat("Oh, you don't have room for the vials. Make", "room for 3 items and come back to me.");
									d.endDialogue();
								}
								return true;
						}
					return false;
					case TALKED_TO_JERICO:;
					case DISTRACT_WATCHTOWER:
					case OVER_WALL:
					case STEW_POISONED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("So, have you managed to retrieve my distillator?");
								return true;
							case 2:
								d.sendPlayerChat("Not yet I'm afraid...", SAD);
								d.endDialogue();
								return true;
						}
					return false;
					case QUEST_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Have you found Jerico yet?");
								return true;
							case 2:
								d.sendOption("Where can I find him at again?", "No, not yet.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 4:
								d.sendNpcChat("My father's friend is in communication with", "West Ardougne. He might be able to help us, he lives", "next to the chapel.");
								d.endDialogue();
								return true;
						}
					return false;
					case 0:
						switch (d.getChatId()) {
							case 1:
								if(!QuestHandler.questCompleted(player, 39)) {
									return false;
								} else {
									d.sendPlayerChat("Good day to you, Elena.");
									return true;
								}
							case 2:
								d.sendNpcChat("You too, thanks for freeing me.", HAPPY);
								return true;
							case 3:
								d.sendNpcChat("It's just a shame the mourners confiscated my", "equipment.", SAD);
								return true;
							case 4:
								d.sendPlayerChat("What did they take?");
								return true;
							case 5:
								d.sendNpcChat("My distillator, I can't test any plague samples without it.", "They're holding it in the mourner quarters", "in West Ardougne.", SAD);
								return true;
							case 6:
								d.sendNpcChat("I must somehow retrieve that distillator if I am to find", "a cure for this awful affliction.");
								return true;
							case 7:
								d.sendOption("I'll try to retrieve it for you.", "Well, good luck.");
								return true;
							case 8:
								d.sendPlayerChat(d.tempStrings[optionId - 1], CONTENT);
								if(optionId == 2)
									d.endDialogue();
								return true;
							case 9:
								d.sendNpcChat("I was hoping you would say that. Unfortunately they", "discovered the tunnel and filled it in. We need another", "way over the wall.");
								return true;
							case 10:
								d.sendPlayerChat("Any ideas?");
								return true;
							case 11:
								d.sendNpcChat("My father's friend Jerico is in communication with", "West Ardougne. He might be able to help us, he lives", "next to the chapel.");
								return true;
							case 12:
								d.sendPlayerChat("I'll see what I can do.");
								d.endDialogue();
								QuestHandler.startQuest(player, this.getQuestID());
								return true;
						}
					return false;
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
