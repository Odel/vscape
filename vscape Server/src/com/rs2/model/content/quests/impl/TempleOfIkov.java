package com.rs2.model.content.quests.impl;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;

public class TempleOfIkov implements Quest {
	public static boolean ENABLED = true;
	
	public static final int questIndex = 7378;
	//Quest stages
	public static final int QUEST_NOT_STARTED = 0,
				QUEST_STARTED = 1,
				LEVER_PLACED = 2,
				FIRE_WARRIOR_KILLED = 3,
				WITCH_NEEDS_ROOTS = 4,
				WITCH_PAID = 5,
				GUARDIAN_TALKED_TO = 6,
				SIDE_CHOSEN = 7,
				QUEST_COMPLETE = 8;

	//Items
	public static final int YEW_LONGBOW = 855;
	public static final int PENDANT_OF_LUCIEN = 86;
	public static final int LEVER = 83;
	public static final int ICE_ARROW = 78;
	public static final int LIMPWURT_ROOT = 225;
	public static final int SHINY_KEY = 85;
	public static final int ARMADYL_PENDANT = 87;
	public static final int STAFF_OF_ARMADYL = 84;

	//Positions
	public static final Position BOOTS_STAIR_IN = new Position(2641, 9763, 0);
	public static final Position BOOTS_DARK_STAIR_IN = new Position(2642, 9740, 0);
	public static final Position BOOTS_STAIR_OUT = new Position(2649, 9805, 0);
	public static final Position WITCH_TELEPORT = new Position(2664, 9877, 0);
	public static final Position[] EAST_BRIDGE_GAPS = {new Position(2650, 9829, 0), new Position(2650, 9828, 0)};
	public static final Position[] WEST_BRIDGE_GAPS = {new Position(2648, 9829, 0), new Position(2648, 9828, 0)};

	//Npcs
	public static final int LUCIEN = 273;
	public static final int LUCIEN_ATTACKABLE = 272;
	public static final int WINELDA = 276;
	public static final int GUARDIANS_OF_ARMADYL_MALE = 274;
	public static final int GUARDIANS_OF_ARMADYL_FEM = 275;
	public static final int FIRE_WARRIOR = 277;

	//Objects
	public static final int STAIRCASE_TO_BOOTS = 98;
	public static final int STAIRCASE_FROM_BOOTS = 96;
	public static final int ICE_ARROW_GATE_LEFT = 90;
	public static final int ICE_ARROW_GATE_RIGHT = 89;
	public static final int GATE_OF_FEAR_LEFT = 95;
	public static final int GATE_OF_FEAR_RIGHT = 94;
	public static final int LEVER_BRACKET = 86;
	public static final int ICE_ARROW_CHEST = 103;
	public static final int TRAP_LEVER = 91;
	public static final int FIRE_WARRIOR_DOOR = 93;
	public static final int TRAP_LEVER_DOOR = 92;
	public static final int MCGRUBBYGRUBBERS_DOOR = 99;
	public static final int TEMPLE_WALL = 1586;
	public static final int LUCIENS_DOOR = 102;
	
	public static final int PULL_LEVER_ANIM = 2140;
	
	public static final int[][] CHEST_COORDS = new int[][] { {2710, 9850}, {2719, 9838}, {2729, 9850}, {2747, 9848}, {2738, 9835}, {2745, 9821} };

	private int reward[][] = { //{itemId, count},
	};

	private int expReward[][] = {
		{Skill.RANGED, 10500},
		{Skill.FLETCHING, 8000}
	};

	private static final int questPointReward = 1;

	public int getQuestID() { //TODO Change
		return 47;
	}

	public String getQuestName() { //Change
		return "Temple of Ikov";
	}

	public String getQuestSaveName() { //Change
		return "templeofikov";
	}

	public boolean canDoQuest(final Player player) {
		return true;
	}

	public void getReward(final Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItemOrDrop(new Item(rewards[0], rewards[1]));
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				for (final int[] expRewards : expReward) {
					player.getSkill().addExp(expRewards[0], (expRewards[1]));
				}
			}
		}, 4);
		player.addQuestPoints(questPointReward);
		player.getActionSender().QPEdit(player.getQuestPoints());
	}

	public void completeQuest(Player player) {
		if (player.getQuestVars().getSidedWithGuardians()) {
			reloadLucien(player);
		}
		getReward(player);
		player.getActionSender().sendInterface(12140);
		player.getActionSender().sendItemOnInterface(12145, 250, YEW_LONGBOW); //zoom, then itemId
		player.getActionSender().sendString("You have completed " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("23625 Ranged XP", 12151);
		player.getActionSender().sendString("18000 Fletching XP", 12152);
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
		switch (questStage) {
			case LEVER_PLACED:
				lastIndex = 5;
				break;
			case WITCH_NEEDS_ROOTS:
				lastIndex = 7;
				break;
			case GUARDIAN_TALKED_TO:
				lastIndex = 10;
				break;
			case QUEST_COMPLETE:
				lastIndex = 26;
				break;
		}
		lastIndex++;

		ActionSender a = player.getActionSender();
		a.sendQuestLogString("Talk to Lucien in Ardougne to begin.", 1, this.getQuestID(), 0);
		a.sendQuestLogString("Lucien has asked me to retrieve the Staff of Armadyl from", 3, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("the Temple of Ikov. The entrance is near Hemenster. He has", 4, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("given me a pendant so I can enter the Chamber of Fear.", 5, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("I defeated the firey temple guardian with ice arrows.", 7, this.getQuestID(), FIRE_WARRIOR_KILLED);
		a.sendQuestLogString("The staff chamber seems to just be a little farther in", 9, this.getQuestID(), WITCH_PAID);
		a.sendQuestLogString("the temple. I should investigate.", 10, this.getQuestID(), WITCH_PAID);
		if (player.getQuestVars().getSidedWithLucien()) {
			a.sendQuestLogString("I must recover and return the Staff to Lucien.", 12, this.getQuestID(), SIDE_CHOSEN);
		} else {
			a.sendQuestLogString("I agreed to help the Guardians of Armadyl to", 12, this.getQuestID(), SIDE_CHOSEN);
			a.sendQuestLogString("defeat Lucien and temporarily banish him from this plane.", 13, this.getQuestID(), SIDE_CHOSEN);
		}
		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("I can start this quest at the @dre@Flying Horse Inn @bla@in @dre@Ardougne", 1);
				a.sendQuestLogString("by speaking to @dre@Lucien", 2);
				a.sendQuestLogString("@dre@Requirements:", 4);
				a.sendQuestLogString((player.getSkill().getLevel()[Skill.THIEVING] >= 42 ? "@str@" : "@dbl@") + "Level 42 Thieving", 5);
				a.sendQuestLogString((player.getSkill().getLevel()[Skill.RANGED] >= 40 ? "@str@" : "@dbl@") + "Level 40 Ranged", 6);
				break;
			case LEVER_PLACED:
				a.sendQuestLogString("I placed a lever into a bracket, it seemed to unlock", lastIndex + 1);
				a.sendQuestLogString("another area. Perhaps this area holds the key to", lastIndex + 2);
				a.sendQuestLogString("advancing in the temple.", lastIndex + 3);
				break;
			case WITCH_NEEDS_ROOTS:
				a.sendQuestLogString("To get deeper into the temple I need to pay", lastIndex + 1);
				a.sendQuestLogString("the Witch 20 Limpwurt roots.", lastIndex + 2);
				break;
			case GUARDIAN_TALKED_TO:
				a.sendQuestLogString("I need to decide between helping the Guardians and Lucien.", lastIndex + 1);
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
		if (ENABLED) {
			int questStage = player.getQuestStage(getQuestID());
			if ((questStage >= QUEST_STARTED) && (questStage < QUEST_COMPLETE)) {
				player.getActionSender().sendString("@yel@" + getQuestName(), questIndex);
			} else if (questStage == QUEST_COMPLETE) {
				player.getActionSender().sendString("@gre@" + getQuestName(), questIndex);
			} else {
				player.getActionSender().sendString("@red@" + getQuestName(), questIndex);
			}
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
	
	public static void guardianAttack(final Npc guardian, final Player player) {
		player.getDialogue().setLastNpcTalk(guardian.getNpcId());
		player.getDialogue().sendNpcChat("Thou art a foul agent of Lucien! Such an agent must die!");
		player.getDialogue().endDialogue();
		World.submit(new Tick(6) {
			@Override
			public void execute() {
				if(guardian != null)
					CombatManager.attack(guardian, player);
				this.stop();
			}
		});
	}

	public static boolean itemPickupHandling(Player player, int itemId) {
		switch (itemId) {
			case STAFF_OF_ARMADYL:
				if (player.getQuestVars().getSidedWithGuardians()) {
					player.getDialogue().sendPlayerChat("I have no need for that now.");
					player.getDialogue().endDialogue();
					return true;
				} else {
					if (!player.getQuestVars().guardianKilled) {
						spawnGuardian(player);
						return true;
					} else {
						if(player.getQuestStage(47) == WITCH_PAID || player.getQuestStage(47) == GUARDIAN_TALKED_TO) {
							player.getQuestVars().setSidedWithLucien(true);
							player.setQuestStage(47, SIDE_CHOSEN);
							return true;
						} else if(player.getQuestStage(47) >= SIDE_CHOSEN) {
							if(player.getInventory().ownsItem(STAFF_OF_ARMADYL)) {
								player.getDialogue().sendPlayerChat("I have no need for that now.");
								player.getDialogue().endDialogue();
								return true;
							}
						}
						return false;
					}
				}
		}
		return false;
	}

	public static void spawnGuardian(final Player player) {
		if (player.spawnedNpc != null || player.stopPlayerPacket()) {
			return;
		}
		final Position spawnPos = new Position(2637, 9910, 0);
		final Npc warrior = new Npc(GUARDIANS_OF_ARMADYL_FEM);
		NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, warrior, spawnPos, false, "That is not thine to take!");
		CombatManager.attack(warrior, player);
	}

	public static void reloadLucien(final Player player) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			Npc lucien = null;

			@Override
			public void execute(CycleEventContainer b) {
				for (Npc npc : World.getNpcs()) {
					if (npc != null && npc.getNpcId() == 272) {
						lucien = npc;
					}
				}
				if (lucien != null) {
					CombatManager.resetCombat(lucien);
					lucien.heal(lucien.getMaxHp());
					player.getNpcs().remove(lucien);
				}
				b.stop();
			}

			@Override
			public void stop() {
			}
		}, 2);
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case LEVER_BRACKET:
				if (item == LEVER) {
					player.getActionSender().sendObject(87, 2671, 9804, 0, 3, 6);
					player.getInventory().removeItem(new Item(LEVER));
					player.getActionSender().sendMessage("You insert the lever into the bracket.", true);
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
		if (npc.getNpcId() == LUCIEN_ATTACKABLE) {
			if (player.getQuestVars().getSidedWithGuardians()) {
				return false;
			} else {
				npc.getAttributes().put("canTakeDamage", Boolean.FALSE);
			}

		}
		return false;
	}
	
	public boolean doNpcSecondClicking(Player player, Npc npc) {
		return false;
	}

	private void gateHandling(final Player player, final int x, final int y, final boolean GATE_OF_FEAR) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer b) {
				if (player.getPosition().getY() == y || player.getPosition().getY() == (y - 1)) {
					if (player.getPosition().getX() > x) {
						player.getActionSender().walkTo(-1, 0, true);
					} else if (player.getPosition().getX() < x) {
						player.getActionSender().walkTo(1, 0, true);
					}
					if (player.getPosition().getX() == x) {
						b.stop();
					}
				}
			}

			@Override
			public void stop() {
				if (GATE_OF_FEAR) {
					new GameObject(GATE_OF_FEAR_LEFT, 2662, 9815, 0, 2, 0, GATE_OF_FEAR_RIGHT, 2, false).addOriginalFace(3);
					new GameObject(GATE_OF_FEAR_RIGHT, 2661, 9815, 0, 4, 0, GATE_OF_FEAR_LEFT, 2, false).addOriginalFace(3);
					player.getActionSender().walkTo(0, player.getPosition().getY() < y ? 1 : -1, true);
					bridgeHandling(player);
				} else {
					new GameObject(ICE_ARROW_GATE_LEFT, 2662, 9803, 0, 2, 0, ICE_ARROW_GATE_RIGHT, 2, false).addOriginalFace(3);
					new GameObject(ICE_ARROW_GATE_RIGHT, 2661, 9803, 0, 4, 0, ICE_ARROW_GATE_LEFT, 2, false).addOriginalFace(3);
					player.getActionSender().walkTo(0, player.getPosition().getY() < y ? 1 : -1, true);
				}
			}
		}, 1);
	}

	public static void bridgeHandling(final Player player) {
		if(player.getQuestStage(47) >= LEVER_PLACED) {
			return;
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			boolean eastOfBridge = true;

			@Override
			public void execute(CycleEventContainer b) {
				if (!player.Area(2634, 2673, 9815, 9858)) {
					b.stop();
				}
				if (player.getWeight() <= 0) {
					if (eastOfBridge) {
						for (Position p : EAST_BRIDGE_GAPS) {
							if (player.getPosition().equals(p)) {
								player.getActionSender().walkTo(player.getPosition().getLastX() > 2649 ? -3 : 3, 0, true);
							}
						}
					} else {
						for (Position p : WEST_BRIDGE_GAPS) {
							if (player.getPosition().equals(p)) {
								player.getActionSender().walkTo(player.getPosition().getLastX() > 2649 ? -3 : 3, 0, true);
							}
						}
					}
				}
				if (player.getPosition().getX() > 2650) {
					eastOfBridge = true;
				} else if (player.getPosition().getX() < 2648) {
					eastOfBridge = false;
				}
			}

			@Override
			public void stop() {
			}
		}, 1);
	}

	public boolean doObjectClicking(final Player player, final int object, final int x, final int y) {
		switch (object) {
			case 6278: //outside trapdoor south of fish guild
				if(x == 2637 && y == 3408) {
					player.getActionSender().sendMessage("You open the trapdoor and climb inside...");
					Ladders.climbLadderDown(player, new Position(2637, 9809, 0));
					return true;
				}
			return false;
			case 100: //inside fake trapdoors
				if(x == 2665 && (y == 9849 || y == 9855)) {
					player.getActionSender().sendMessage("You pull on the trapdoor...", true);
					player.getActionSender().sendTimedMessage("...It won't budge! It must be locked from the other side.", true, 3);
					return true;
				}
			return false;
			case 87:
				if(x == 2671 && y == 9804) {
					if (player.getQuestStage(this.getQuestID()) == QUEST_STARTED) {
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.getActionSender().sendMessage("You pull the lever...", true);
						player.getActionSender().sendTimedMessage("You hear the clunking of some hidden machinery.", true, 2);
						player.setQuestStage(this.getQuestID(), LEVER_PLACED);
						return true;
					}
				}
				return false;
			case STAIRCASE_TO_BOOTS:
				if (x == 2650 && y == 9804) {
					boolean condition = player.hasLightSource();
					player.fadeTeleport(condition ? BOOTS_STAIR_IN : BOOTS_DARK_STAIR_IN);
					if(!condition)
						player.getActionSender().sendTimedMessage("It's pitch black in here!", true, 5);
					return true;
				}
				return false;
			case STAIRCASE_FROM_BOOTS:
				if(x == 2638 && (y == 9763 || y == 9740)) {
					player.fadeTeleport(BOOTS_STAIR_OUT);
					return true;
				}
				return false;
			case GATE_OF_FEAR_LEFT:
			case GATE_OF_FEAR_RIGHT:
				if ((x == 2661 || x == 2662) && y == 9815) {
					if (player.getEquipment().getId(Constants.AMULET) == PENDANT_OF_LUCIEN) {
						gateHandling(player, x, y, true);
					} else {
						player.getActionSender().sendMessage("As you reach to open the door a great terror overcomes you!", true);
					}
					return true;
				}
				return false;
			case ICE_ARROW_GATE_LEFT:
			case ICE_ARROW_GATE_RIGHT:
				if ((x == 2661 || x == 2662) && y == 9803) {
					if (player.getQuestStage(this.getQuestID()) >= LEVER_PLACED) {
						gateHandling(player, x, y, false);
					} else {
						player.getActionSender().sendMessage("The gate won't budge!", true);
					}
					return true;
				}
				return false;
			case ICE_ARROW_CHEST:
				if (player.Area(2707, 2757, 9821, 9861)) {
					player.getUpdateFlags().sendAnimation(832);
					CacheObject o = ObjectLoader.object(object, x, y, 0);
					if(o != null) {
						new GameObject(104, x, y, 0, o.getRotation(), 10, 103, 999999, true);
					}
					return true;
				}
				return false;
			case ICE_ARROW_CHEST + 1: //Ice arrow chest open
				if (player.Area(2707, 2757, 9821, 9861)) {
					player.setStopPacket(true);
					player.getActionSender().sendMessage("You search the chest...", true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							b.stop();
						}

						@Override
						public void stop() {
							int[] coords = CHEST_COORDS[player.getQuestVars().iceArrowChestIndex];
							if (x == coords[0] && y == coords[1]) {
								int amount = Misc.random(4) + 2;
								Item arrows = new Item(ICE_ARROW, amount);
								player.getInventory().addItem(arrows);
								player.getActionSender().sendMessage("You find some ice arrows!", true);
								player.getDialogue().sendGiveItemNpc("You find some ice arrows!", arrows);
								player.getQuestVars().iceArrowChestIndex = Misc.randomMinusOne(CHEST_COORDS.length);
							} else {
								player.getActionSender().sendMessage("...but find nothing.", true);
							}
							player.setStopPacket(false);
						}
					}, 2);
					return true;
				}
				return false;
			case TRAP_LEVER_DOOR:
				if (x == 2648 && y == 9857) {
					if (player.getPosition().getY() < 9858) {
						if (player.getQuestVars().trapLeverPulled || player.getQuestStage(this.getQuestID()) >= FIRE_WARRIOR_KILLED) {
							player.getActionSender().walkTo(0, 1, true);
							player.getActionSender().walkThroughDoor(object, x, y, 0);
						} else {
							player.getActionSender().sendMessage("The door won't open.", true);
						}
					} else {
						player.getActionSender().walkTo(0, -1, true);
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						bridgeHandling(player);
					}
					return true;
				}
				return false;
			case FIRE_WARRIOR_DOOR:
				if (x == 2646 && y == 9870) {
					if (player.getPosition().getY() <= 9870 && player.getQuestStage(this.getQuestID()) < FIRE_WARRIOR_KILLED) {
						startFireWarriorSequence(player);
					} else {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(player.getPosition().getX() == x ? 0 : player.getPosition().getX() < x ? 1 : -1, player.getPosition().getY() <= 9870 ? 1 : -1, true);
					}
					return true;
				}
				return false;
			case TRAP_LEVER:
				if (x == 2665 && y == 9855) {
					if (player.getQuestVars().trapLeverDisarmed) {
						player.getQuestVars().trapLeverPulled = true;
						player.getUpdateFlags().sendAnimation(PULL_LEVER_ANIM);
						player.getActionSender().sendMessage("You pull the lever.", true);
					} else {
						player.getActionSender().sendMessage("The lever shocks you! You are unable to finish pulling it!", true);
						player.hit(2, HitType.NORMAL);
					}
					return true;
				}
				return false;
			case MCGRUBBYGRUBBERS_DOOR:
				if (x == 2657 && y == 3496) {
					if (player.getPosition().equals(new Position(2657, 3497, 0))) {
						if (player.getInventory().playerHasItem(SHINY_KEY)) {
							player.getActionSender().walkTo(0, -1, true);
							player.getActionSender().walkThroughDoor(object, x, y, 0);
						} else {
							player.getActionSender().sendMessage("This door is locked.", true);
						}
					} else {
						player.getActionSender().walkTo(0, 1, true);
						player.getActionSender().walkThroughDoor(object, x, y, 0);
					}
					return true;
				}
				return false;
			case TEMPLE_WALL:
				if (x == 2643 && y == 9892) {
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (player.getPosition().getY() == 9893 || player.getPosition().getY() == (9893 - 1)) {
								if (player.getPosition().getX() > 2643) {
									player.getActionSender().walkTo(-1, 0, true);
								} else if (player.getPosition().getX() < 2643) {
									player.getActionSender().walkTo(1, 0, true);
								}
								if (player.getPosition().getX() == 2643) {
									b.stop();
								}
							}
						}
						@Override
						public void stop() {
							player.getActionSender().walkTo(0, player.getPosition().getY() < 9893 ? 1 : -1, true);
							player.getActionSender().walkThroughDoor(object, x, y, 0);
						}
					}, 1);
					return true;
				}
				return false;
			case LUCIENS_DOOR:
				if (x == 3176 && y == 3481) {
					player.getActionSender().walkTo(player.getPosition().getX() == x ? 0 : player.getPosition().getX() < x ? -1 : 1, player.getPosition().getY() < 3481 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				}
				return false;	
		}
		return false;
	}

	public static void startFireWarriorSequence(final Player player) {
		player.getActionSender().sendMessage("The door won't open.", true);
		if (player.spawnedNpc != null || player.stopPlayerPacket()) {
			return;
		}
		player.setStopPacket(true);
		final Position spawnPos = new Position(2646, 9866, 0);
		final Npc warrior = new Npc(FIRE_WARRIOR);
		World.createStaticGraphic(new Graphic(86, 0), spawnPos);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;
			Position p = new Position(2646, 9870);

			@Override
			public void execute(CycleEventContainer b) {
				count++;
				switch (count) {
					case 1:
						NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, warrior, spawnPos, false, "You will not pass!");
						break;
					case 3:
						warrior.getUpdateFlags().setForceChatMessage("Amitus! Setitii!");
						warrior.getUpdateFlags().sendFaceToDirection(new Position(2646, 9871, 0));
						warrior.getUpdateFlags().setFaceToDirection(true);
						warrior.setFace(2);
						warrior.getUpdateFlags().sendAnimation(711);
						warrior.getUpdateFlags().sendGraphic(Graphic.highGraphic(126));
						int attackerX = spawnPos.getX(),
						 attackerY = spawnPos.getY();
						final int offsetX = (attackerY - p.getY()) * -1;
						final int offsetY = (attackerX - p.getX()) * -1;
						World.sendProjectile(spawnPos, offsetX, offsetY, 130, 43, 0, 120, 0, false);
						break;
					case 5:
						World.createStaticGraphic(Graphic.highGraphic(131), p);
						player.getUpdateFlags().sendAnimation(846);
						player.movePlayer(p.modifyY(p.getY() - 1));
						break;
					case 6:
						b.stop();
						break;
				}
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {
			case ICE_ARROW_CHEST + 1: //close ice arrow chest
				if (player.Area(2707, 2757, 9821, 9861)) {
					player.getUpdateFlags().sendAnimation(832);
					ObjectHandler.getInstance().removeObject(x, y, 0, 10);
					return true;
				}
				return false;
			case TRAP_LEVER:
				if (x == 2665 && y == 9855) {
					if (player.getSkill().getLevel()[Skill.THIEVING] >= 42) {
						player.getDialogue().sendStatement("You find a trap on the lever! You disable the trap.");
						player.getQuestVars().trapLeverDisarmed = true;
					} else {
						player.getDialogue().sendStatement("You need a Thieving level of 42 to disarm the trap here.");
					}
					return true;
				}
				return false;
		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {
		if (died.getPlayerOwner() != null && died.getPlayerOwner().equals(player) && died.getNpcId() == FIRE_WARRIOR) {
			if(player.getQuestStage(47) == LEVER_PLACED) {
				player.setQuestStage(this.getQuestID(), FIRE_WARRIOR_KILLED);
			}
		}
		if (died.getPlayerOwner() != null && died.getPlayerOwner().equals(player) && died.getNpcId() == GUARDIANS_OF_ARMADYL_FEM) {
			player.getQuestVars().guardianKilled = true;
		}
	}

	public boolean isWearingLucienPendant(Player player) {
		if (player.getEquipment().getId(Constants.AMULET) == PENDANT_OF_LUCIEN) {
			return true;
		}
		return false;
	}

	public void witchTele(final Player player) {
		player.getActionSender().removeInterfaces();
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;

			@Override
			public void execute(CycleEventContainer b) {
				count++;
				player.setStopPacket(true);
				switch (count) {
					case 1:
						player.transformNpc = 2370;
						player.setAppearanceUpdateRequired(true);
						World.createStaticGraphic(new Graphic(86, 0), player.getPosition());
						break;
					case 2:
						player.movePlayer(WITCH_TELEPORT);
						World.createStaticGraphic(new Graphic(86, 0), WITCH_TELEPORT);
						break;
					case 3:
						player.transformNpc = -1;
						player.setAppearanceUpdateRequired(true);
						b.stop();
						break;
				}
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		if (!ENABLED) {
			return false;
		}
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case FIRE_WARRIOR:
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("Who dares to enter the Temple of Ikov!", ANGRY_1);
						return true;
					case 2:
						d.sendOption("A mighty hero!", "A humble pilgrim.");
						return true;
					case 3:
						d.sendPlayerChat(d.tempStrings[optionId - 1]);
						if(optionId == 1)
							d.setNextChatId(5);
						return true;
					case 4:
						d.sendNpcChat("No pilgrims have been to this temple for hundreds", "of years. This temple is closed.", ANGRY_1);
						d.endDialogue();
						return true;
					case 5:
						d.sendNpcChat("Aargh! All you 'heroes' do is further harm this", "temple! Begone!", ANGRY_1);
						return true;
					case 6:
						d.endDialogue();
						player.getActionSender().removeInterfaces();
						Npc attacker = World.getNpcs()[player.getNpcClickIndex()];
						if(attacker != null && attacker.getPlayerOwner() != null && attacker.getPlayerOwner().equals(player)) {
							CombatManager.attack(attacker, player);
						}
						return true;
				}
			return false;
			case LUCIEN:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case QUEST_NOT_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("I seek a hero to go on an important mission!");
								return true;
							case 2:
								d.sendOption("I'm a mighty hero!", "Yep, lots of heroes about these days.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 1:
										d.setNextChatId(5);
										break;
								}
								return true;
							case 4:
								d.sendNpcChat("Well, if you see any be sure to point them", "in my direction.");
								d.endDialogue();
								return true;
							case 5:
								d.sendNpcChat("I require the Staff of Armadyl. It is in the deserted", "Temple of Ikov near Hemenster, north east of here.");
								return true;
							case 6:
								d.sendNpcChat("Take care hero! There is a dangerous monster", "somewhere in the temple!");
								return true;
							case 7:
								d.sendOption("Why can't you get it yourself?", "That sounds like a laugh!", "Oh no! Sounds far too dangerous!", "Whats the reward?");
								return true;
							case 8:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 1:
										d.setNextChatId(13);
										break;
									case 2:
										d.setNextChatId(15);
										break;
									case 3:
										d.setNextChatId(9);
										break;
									case 4:
										d.setNextChatId(10);
										break;
								}
								return true;
							case 9:
								d.sendNpcChat("Wimp! Call yourself a hero?!", "My daughter is more a hero than you!");
								d.endDialogue();
								return true;
							case 10:
								d.sendNpcChat("I see you are the mercenary type.");
								return true;
							case 11:
								d.sendPlayerChat("It's a living.");
								return true;
							case 12:
								d.sendNpcChat("I will reward you well if you bring me the staff."); //TODO should this chat end here or go back.
								d.setNextChatId(7);
								return true;
							case 13:
								d.sendNpcChat("The guardians of the Staff of Armadyl fear me!");
								return true;
							case 14:
								d.sendNpcChat("They have set up a magical barrier", "which even my power cannot overcome!");
								d.setNextChatId(7);
								return true;
							case 15:
								d.sendNpcChat("It's not as easy as it sounds. The monster can only be", "killed with a weapon of ice. There are many other", "dangers.");
								return true;
							case 16:
								d.sendPlayerChat("I'm up for it!");
								return true;
							case 17:
								d.sendNpcChat("Take this pendant. Without it you will not be able to", "enter the Chamber of Fear.");
								return true;
							case 18:
								Item pendant = new Item(PENDANT_OF_LUCIEN);
								if (player.getInventory().canAddItem(pendant)) {
									d.sendGiveItemNpc("Lucien has given you a pendant!", pendant);
									player.getInventory().addItem(pendant);
								} else {
									d.sendStatement("Your inventory is full!");
									d.endDialogue();
								}
								return true;
							case 19:
								d.sendNpcChat("I cannot stay here much longer.");
								return true;
							case 20:
								d.sendNpcChat("I will be in the forest north of Varrock. I have a small", "holding up there.");
								d.endDialogue();
								QuestHandler.startQuest(player, this.getQuestID());
								return true;
						}
						return false;
				}
				return false;
			case WINELDA:
				switch (player.getQuestStage(this.getQuestID())) {
					case WITCH_NEEDS_ROOTS:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Did you get me those Limpwurt roots yet?", "I need 20, you can't have forgotten!");
								return true;
							case 2:
								if (player.getInventory().playerHasItem(LIMPWURT_ROOT, 20)) {
									d.sendPlayerChat("I've got the Limpwurt roots!");
									d.setNextChatId(5);
									player.getInventory().removeItem(new Item(LIMPWURT_ROOT, 20));
									player.getQuestVars().setSidedWithLucien(false);
									player.getQuestVars().setSidedWithGuardians(false);
									player.setQuestStage(this.getQuestID(), WITCH_PAID);
								} else {
									d.sendPlayerChat("Not yet...", ANGRY_1);
									d.endDialogue();
								}
								return true;
						}
					return false;
					case FIRE_WARRIOR_KILLED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("You seem like you want to cross this gap.");
								return true;
							case 2:
								d.sendPlayerChat("How'd you know?");
								return true;
							case 3:
								d.sendNpcChat("Why else would you come here, hmm?", "No matter, if you wish to pass, bring me 20 limpwurt roots.");
								return true;
							case 4:
								if (player.getInventory().playerHasItem(LIMPWURT_ROOT, 20)) {
									d.sendPlayerChat("I've got some limpwurt roots!");
									player.getInventory().removeItem(new Item(LIMPWURT_ROOT, 20));
									player.setQuestStage(this.getQuestID(), WITCH_PAID);
								} else {
									d.sendPlayerChat("Ugh. Fine.");
									d.endDialogue();
									player.setQuestStage(this.getQuestID(), WITCH_NEEDS_ROOTS);
								}
								return true;
						}
						return false;
					case WITCH_PAID:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hold on tight!");
								return true;
							case 2:
								d.endDialogue();
								witchTele(player);
								return true;
							case 5:
								d.sendNpcChat("Good! Good! My potion is nearly ready! Bubble, bubble,", "toil and trouble!");
								return true;
							case 6:
								d.sendNpcChat("Now we shows them ours magic! Hold on tight!");
								return true;
							case 7:
								d.endDialogue();
								witchTele(player);
								return true;
						}
						return false;
				}
				return false;
			case GUARDIANS_OF_ARMADYL_MALE:
			case GUARDIANS_OF_ARMADYL_FEM:
				final Npc speaker = World.getNpcs()[player.getNpcClickIndex()];
				if(speaker != null && speaker.isAttacking()) {
					return false;
				}
				switch (player.getQuestStage(this.getQuestID())) {
					case WITCH_PAID:
						switch (d.getChatId()) {
							case 1:
								if (this.isWearingLucienPendant(player)) {
									guardianAttack(speaker, player);
								} else {
									d.sendNpcChat("Thou hast ventured deep into the tunnels, you have", "reached the temple of our master. It is many ages", "since a pilgrim has come here.");
								}
								return true;
							case 2:
								d.sendOption("I seek the Staff of Armadyl.", "Out of my way fool!", "What are your kind and what are you doing here?");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 1:
										d.setNextChatId(4);
										break;
									case 2:
										d.setNextChatId(5);
										break;
									case 3:
										d.setNextChatId(6);
										break;
								}
								return true;
							case 4:
								d.sendNpcChat("We cannot let you have it! If it's power were", "to be wielded by the wrong hands, the consequences", "would be catastrophic!");
								d.setNextChatId(2);
								return true;
							case 5:
								d.sendNpcChat("How rude!");
								d.endDialogue();
								return true;
							case 6:
								d.sendNpcChat("We are the Guardians of Armadyl. We have kept the", "temple safe for many ages. The evil in the dungeons", "seek what lies here. The Mahjarrat are the worst.");
								return true;
							case 7:
								d.sendOption("What is Armadyl?", "Who are the Mahjarrat?", "Wow! You must be really old!");
								return true;
							case 8:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 1:
										d.setNextChatId(9);
										break;
									case 2:
										d.setNextChatId(13);
										break;
									case 3:
										d.setNextChatId(12);
										break;
								}
								return true;
							case 9:
								d.sendNpcChat("Armadyl is the god we serve. We have been charged", "with guarding his sacred artifacts until he requires them.");
								return true;
							case 10:
								d.sendPlayerChat("Someone told me there were only three gods.", "Saradomin, Zamorak and Guthix.");
								return true;
							case 11:
								d.sendNpcChat("Saradominists. Bleh.", "They only acknowledge those three.", "There are at least twenty gods!");
								d.setNextChatId(7);
								return true;
							case 12:
								d.sendNpcChat("No! I am not old!", "My family has guarded the staff for many generations.");
								d.setNextChatId(7);
								return true;
							case 13:
								d.sendNpcChat("They are ancient and powerful beings! They are very", "evil! It is said that they once dominated this place of", "existence, Zamorak was supposedly of their blood. They", "are far fewer in number now.");
								return true;
							case 14:
								d.sendNpcChat("Some still have presence in this world in their lich", "forms. Mahjarrat such as Lucien and Azzanadra would", "become very powerful if they came into possession of the", "Staff of Armadyl.");
								return true;
							case 15:
								d.sendOption("Did you say Lucien?", "I hope you're doing a good job then!");
								return true;
							case 16:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 1:
										d.setNextChatId(18);
										break;
									case 2:
										d.setNextChatId(17);
										break;
								}
								return true;
							case 17:
								d.sendNpcChat("Yes, we have kept the staff safe for many centuries now.");
								d.endDialogue();
								return true;
							case 18:
								d.sendPlayerChat("It was Lucien that asked me to get the staff!");
								return true;
							case 19:
								d.sendNpcChat("You are a fool to be working for Lucien! Your soul", "must be cleansed to save you!");
								return true;
							case 20:
								d.sendOption("How dare you call me a fool!", "I just thought of something I must do!", "You're right it's time for my yearly bath.");
								return true;
							case 21:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 2:
										d.setNextChatId(23);
										break;
									case 3:
										d.setNextChatId(24);
										break;
								}
								return true;
							case 22:
								guardianAttack(speaker, player);
								return true;
							case 23:
								d.sendNpcChat("Hurry back quickly, before", "Lucien's corruption can take root!");
								d.endDialogue();
								return true;
							case 24:
								d.sendStatement("The guardian splashes holy water over you.");
								return true;
							case 25:
								d.sendNpcChat("You have been cleansed!");
								player.setQuestStage(this.getQuestID(), GUARDIAN_TALKED_TO);
								d.setNextChatId(1);
								return true;
						}
						return false;
					case GUARDIAN_TALKED_TO:
						switch (d.getChatId()) {
							case 1:
								if (this.isWearingLucienPendant(player)) {
									guardianAttack(speaker, player);
								} else {
									d.sendNpcChat("Lucien must not get hold of the staff! He would become", "too powerful!");
								}
								return true;
							case 2:
								d.sendNpcChat("Hast thou come across the undead necromancer? It", "was he that raised an army of the undead against", "Varrock a generation ago. If you know where he is", "you can help us defeat him.");
								return true;
							case 3:
								d.sendOption("Ok! I'll help!", "No! I shall not turn against my employer!", "I need time to think.");
								return true;
							case 4:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 1:
										d.setNextChatId(6);
										break;
									case 2:
										d.setNextChatId(5);
										break;
									case 3:
										d.endDialogue();
										break;
								}
								return true;
							case 5:
								guardianAttack(speaker, player);
								player.getQuestVars().setSidedWithLucien(true);
								player.setQuestStage(this.getQuestID(), SIDE_CHOSEN);
								return true;
							case 6:
								d.sendNpcChat("So he is close by?");
								return true;
							case 7:
								d.sendPlayerChat("Yes!");
								return true;
							case 8:
								d.sendNpcChat("He must be gaining in power again. If you can defeat", "him he will be banished from this plane for a while. You", "will need this pendant to attack him.");
								return true;
							case 9:
								Item pendant = new Item(ARMADYL_PENDANT);
								if (player.getInventory().canAddItem(pendant)) {
									d.sendGiveItemNpc("The guardian has given you a pendant!", pendant);
									player.getInventory().addItem(pendant);
									player.getQuestVars().setSidedWithGuardians(true);
									player.setQuestStage(this.getQuestID(), SIDE_CHOSEN);
								} else {
									d.sendStatement("Your inventory is full!");
								}
								d.endDialogue();
								return true;
							case 10:
								d.sendNpcChat("Return to me if you find out where he is located");
								d.endDialogue();
								return true;
						}
						return false;
					case SIDE_CHOSEN:
						if (player.getQuestVars().getSidedWithLucien()) {
							guardianAttack(speaker, player);
							return true;
						}
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Have you rid us of Lucien yet?");
								return true;
							case 2:
								d.sendPlayerChat("Not yet.");
								return true;
							case 3:
								d.sendNpcChat("Hurry friend! Time is against us!");
								d.endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case LUCIEN_ATTACKABLE:
				switch (player.getQuestStage(this.getQuestID())) {
					case QUEST_NOT_STARTED:
						return false;
					case QUEST_COMPLETE:
						d.sendNpcChat("Thank you again again adventurer...", "Muhahaha!", Dialogues.LAUGHING);
						d.endDialogue();
						return true;
					default:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Have you got the Staff of Armadyl yet?");
								return true;
							case 2:
								d.sendOption("Yes here it is.", "No not yet.");
								return true;
							case 3:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 1:
										d.setNextChatId(4);
										break;
									case 2:
										d.endDialogue();
										break;

								}
								return true;
							case 4:
								if (player.getInventory().ownsItem(STAFF_OF_ARMADYL)) {
									d.sendGiveItemNpc("You show Lucien the Staff of Armadyl.", new Item(STAFF_OF_ARMADYL));
								} else {
									d.sendNpcChat("Where is it then? Don't waste my time!");
									d.endDialogue();
								}
								return true;
							case 5:
								d.sendNpcChat("Muhahahahahahah!");
								return true;
							case 6:
								d.sendNpcChat("I can feel the power of the staff running through me! I", "will be more powerful and they shall bow down to me!");
								return true;
							case 7:
								d.sendNpcChat("I suppose you want your reward? I shall grant you", "much power!");
								return true;
							case 8:
								d.dontCloseInterface();
								QuestHandler.completeQuest(player, this.getQuestID());
								player.getInventory().removeItem(new Item(STAFF_OF_ARMADYL));
								return true;
						}
						return false;
				}
			case LUCIEN_ATTACKABLE + 20000:
				d.setLastNpcTalk(LUCIEN_ATTACKABLE);
				switch(d.getChatId()) {
					case 1:
						d.sendNpcChat("You have defeated me for now! I shall reappear in the", "North!");
						return true;
					case 2:
						d.dontCloseInterface();
						player.getQuestVars().setSidedWithGuardians(true);
						QuestHandler.completeQuest(player, 47);
						return true;
				}
			return false;
		}
		return false;
	}

}
