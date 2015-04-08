package com.rs2.model.content.quests.impl;

import com.rs2.Constants;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.ActionSender;
import com.rs2.util.Misc;

public class TempleOfIkov implements Quest {

	public static boolean ENABLED = false;
	public static final int questIndex = 7378;
	
	//Quest stages
	public static final int QUEST_NOT_STARTED = 0;
	public static final int QUEST_STARTED = 1;
	public static final int LEVER_PLACED = 2;
	public static final int FIRE_WARRIOR_KILLED = 3;
	public static final int WITCH_PAID = 4;
	public static final int GUARDIAN_TALKED_TO = 5;
	public static final int SIDE_CHOSEN = 6;
	public static final int QUEST_COMPLETE = 7;

	//Items
	public static final int OAK_LONGBOW = 56;
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
		player.getActionSender().sendItemOnInterface(12145, 250, OAK_LONGBOW); //zoom, then itemId
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
			case QUEST_STARTED:
				lastIndex = 5;
				break;
			case FIRE_WARRIOR_KILLED:
			case LEVER_PLACED:
				lastIndex = 7;
				break;
			case GUARDIAN_TALKED_TO:
			case WITCH_PAID:
				lastIndex = 9;
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
		a.sendQuestLogString("given me a pendant so I can enter the chamber of fear.", 5, this.getQuestID(), QUEST_STARTED);
		a.sendQuestLogString("Temple Guardian defeated.", 7, this.getQuestID(), LEVER_PLACED);
		a.sendQuestLogString("Staff chamber found.", 9, this.getQuestID(), WITCH_PAID);
		if (player.getQuestVars().getSidedWithLucien()) {
			a.sendQuestLogString("I must recover and return the Staff to Lucien.", 11, this.getQuestID(), SIDE_CHOSEN);
		} else {
			a.sendQuestLogString("I agreed to help the Guardians of Armadyl to", 11, this.getQuestID(), SIDE_CHOSEN);
			a.sendQuestLogString("defeat lucien and temporarily banish him from this plane.", 12, this.getQuestID(), SIDE_CHOSEN);
		}

		switch (questStage) {
			default:
				break;
			case 0:
				a.sendQuestLogString("I can start this quest at the @dre@Flying Horse Inn @bla@in @dre@Ardougne", 1);
				a.sendQuestLogString("by speaking to @dre@Lucien", 2);
				a.sendQuestLogString("@dre@Requirements:", 4);
				a.sendQuestLogString((player.getSkill().getLevel()[Skill.THIEVING] >= 42 ? "@str@" : "@dbl@") + "Level 42 thieving", 5);
				a.sendQuestLogString((player.getSkill().getLevel()[Skill.RANGED] >= 40 ? "@str@" : "@dbl@") + "Level 40 ranged", 6);
				break;
			case LEVER_PLACED:
				a.sendQuestLogString("I need to get some ice weaponary to be able", lastIndex);
				a.sendQuestLogString("to challenge the temple guardian.", lastIndex + 1);
				break;
			case FIRE_WARRIOR_KILLED:
				a.sendQuestLogString("To get deeper into the temple i need to pay", lastIndex + 1);
				a.sendQuestLogString("the witch 20 limpwurt roots.", lastIndex + 2);
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

	public static boolean itemPickupHandling(Player player, int itemId) {
		switch (itemId) {
			case STAFF_OF_ARMADYL:
				if (player.getQuestVars().getSidedWithGuardians()) {
					player.getActionSender().sendMessage("I have no need for this.");
					return true;
				} else {
					if (!player.getQuestVars().guardianKilled) {
						spawnGuardian(player);
						return true;
					} else {
						player.getQuestVars().setSidedWithLucien(true);
						player.setQuestStage(47, SIDE_CHOSEN);
						return false;
					}
				}
		}
		return false;
	}

	public static void spawnGuardian(final Player player) {
		//player.getActionSender().sendMessage("The door won't open.");
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
					player.getInventory().removeItem(new Item(LEVER));
					player.getActionSender().sendMessage("You insert the lever into the bracket and pull...");
					player.getActionSender().sendMessage("You hear a faint click nearby.");
					player.setQuestStage(this.getQuestID(), LEVER_PLACED);
				}
				return true;
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
					if (player.getQuestStage(47) < LEVER_PLACED) //TODO check if the performance increase is worth it
					{
						bridgeHandling(player);
					}
				} else {
					new GameObject(ICE_ARROW_GATE_LEFT, 2662, 9803, 0, 2, 0, ICE_ARROW_GATE_RIGHT, 2, false).addOriginalFace(3);
					new GameObject(ICE_ARROW_GATE_RIGHT, 2661, 9803, 0, 4, 0, ICE_ARROW_GATE_LEFT, 2, false).addOriginalFace(3);
					player.getActionSender().walkTo(0, player.getPosition().getY() < y ? 1 : -1, true);
				}
			}
		}, 1);
	}

	private void bridgeHandling(final Player player) {
		System.out.println("Starting bridge checking");
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			boolean eastOfBridge = true;

			@Override
			public void execute(CycleEventContainer b) {

				if (!player.Area(2634, 2673, 9815, 9870)) {
					b.stop();
				}
				if (player.getWeight() <= 0) {
					if (eastOfBridge) {
						for (int i = 0; i < EAST_BRIDGE_GAPS.length; i++) {
							if (player.getPosition().equals(EAST_BRIDGE_GAPS[i])) {
								player.getActionSender().walkTo(player.getPosition().getLastX() > 2649 ? -3 : 3, 0, true);
							}
						}
					} else {
						for (int i = 0; i < WEST_BRIDGE_GAPS.length; i++) {
							if (player.getPosition().equals(WEST_BRIDGE_GAPS[i])) {
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
			case STAIRCASE_TO_BOOTS:
				if (player.hasLightSource()) {
					player.fadeTeleport(BOOTS_STAIR_IN);
					return true;
				} else {
					player.fadeTeleport(BOOTS_DARK_STAIR_IN);
					player.getActionSender().sendMessage("It's pitch black in here!");
					return true;
				}
			case STAIRCASE_FROM_BOOTS:
				player.fadeTeleport(BOOTS_STAIR_OUT);
				return true;
			case GATE_OF_FEAR_LEFT:
			case GATE_OF_FEAR_RIGHT:
				if (x >= 2661 && x <= 2662 && y == 9815) {
					if (player.getEquipment().getId(Constants.AMULET) == PENDANT_OF_LUCIEN) {
						System.out.println("GateHandling");
						gateHandling(player, x, y, true);
					} else {
						player.getActionSender().sendMessage("As you reach to open the door a great terror overcomes you!");
					}
					return true;
				}
			case ICE_ARROW_GATE_LEFT:
			case ICE_ARROW_GATE_RIGHT:
				if (x >= 2661 && x <= 2662 && y == 9803) {
					if (player.getQuestStage(this.getQuestID()) >= LEVER_PLACED) {
						gateHandling(player, x, y, false);
					} else {
						player.getActionSender().sendMessage("The gate won't budge!");
					}
				}
				return true;
			case ICE_ARROW_CHEST:
				if (Misc.random(2) == 1) {
					player.getInventory().addItem(new Item(ICE_ARROW, 3));
				} else {
					player.getActionSender().sendMessage("You search the chest but find nothing.");
				}
				return true;
			case TRAP_LEVER_DOOR:
				if (player.getPosition().equals(new Position(2648, 9857, 0))) {
					if (player.getQuestVars().trapLeverPulled) {
						player.getActionSender().walkTo(0, 1, true);
						player.getActionSender().walkThroughDoor(object, x, y, 0);
					} else {
						player.getActionSender().sendMessage("The door won't open.");
					}
				}
				return true;
			case FIRE_WARRIOR_DOOR:
				if (x == 2646 && y == 9870) {
					if (player.getPosition().getY() <= 9870 && player.getQuestStage(this.getQuestID()) < FIRE_WARRIOR_KILLED) {
						startFireWarriorSequence(player);
					} else {
						player.getActionSender().walkThroughDoor(object, x, y, 0);
						player.getActionSender().walkTo(player.getPosition().getX() == x ? 0 : player.getPosition().getX() < x ? 1 : -1, player.getPosition().getY() <= 9870 ? 1 : -1, true);
					}
				}
				return true;
			case TRAP_LEVER:
				if (player.getQuestVars().trapLeverDisarmed) {
					player.getQuestVars().trapLeverPulled = true;
					player.getActionSender().sendMessage("You pull the lever down.");
				} else {
					player.getActionSender().sendMessage("The lever shocks you! You are unable to finish pulling it!");
				}
				return true;
			case MCGRUBBYGRUBBERS_DOOR:
				if (x == 2657 && y == 3496) {
					if (player.getPosition().equals(new Position(2657, 3497, 0))) {
						if (player.getInventory().playerHasItem(SHINY_KEY)) {
							player.getActionSender().walkTo(0, -1, true);
							player.getActionSender().walkThroughDoor(object, x, y, 0);
						} else {
							player.getActionSender().sendMessage("Door is locked.");
						}
					} else {
						player.getActionSender().walkTo(0, 1, true);
						player.getActionSender().walkThroughDoor(object, x, y, 0);
					}
				}
				return true;
			case TEMPLE_WALL:
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
			case LUCIENS_DOOR:
				if (x == 3176 && y == 3481) {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3481 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
				}
				return true;
		}
		return false;
	}

	public static void startFireWarriorSequence(final Player player) {
		player.getActionSender().sendMessage("The door won't open.");
		if (player.spawnedNpc != null || player.stopPlayerPacket()) {
			return;
		}
		player.setStopPacket(true);
		final Position spawnPos = new Position(2646, 9866, 0);
		final Npc warrior = new Npc(277);
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
				CombatManager.attack(warrior, player);
			}
		}, 2);
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {
			case TRAP_LEVER:
				if (player.getSkill().getLevel()[Skill.THIEVING] >= 42) {
					player.getDialogue().sendStatement("You find a trap on the lever! You disable the trap.");
					player.getQuestVars().trapLeverDisarmed = true;
				} else {
					player.getActionSender().sendMessage("You can't see any trap mechanisms.");
				}
		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {
		if (died.getPlayerOwner() != null && died.getPlayerOwner().equals(player) && died.getNpcId() == 277) {
			player.setQuestStage(this.getQuestID(), FIRE_WARRIOR_KILLED);
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

		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int count = 0;

			@Override
			public void execute(CycleEventContainer b) {
				count++;
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
			}
		}, 2);
	}

	public boolean sendDialogue(final Player player, final int id, int chatId, int optionId, int npcChatId) {
		if (!ENABLED) {
			return false;
		}
		DialogueManager d = player.getDialogue();
		switch (id) { //Npc ID
			case LUCIEN:
				switch (player.getQuestStage(this.getQuestID())) { //Dialogue per stage
					case QUEST_COMPLETE:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Thank you again!", Dialogues.HAPPY);
								d.endDialogue();
								return true;
						}
						return false;
					case QUEST_NOT_STARTED:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("I seek a hero to go on an important mission!");
								return true;
							case 2:
								d.sendOption("I'm a mighty hero!", "Yep, lots of heros about these days.");
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
								d.sendOption("Why can't you get it yourself?", "That sounds like a laugh!", "Oh no! Sounds far too dangerous!", "Whats the reward?!");
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
									d.sendPlayerChat("I've got you the limpwurt roots!");
									player.getInventory().removeItem(new Item(LIMPWURT_ROOT, 20));
									player.setQuestStage(this.getQuestID(), WITCH_PAID);
								} else {
									d.sendPlayerChat("Ugh. Fine");
									d.endDialogue();
								}
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
					case WITCH_PAID:
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Hold on tight!");
								return true;
							case 2:
								d.endDialogue();
								witchTele(player);
								return true;
						}
						return false;
				}
				return false;
			case GUARDIANS_OF_ARMADYL_MALE:
			case GUARDIANS_OF_ARMADYL_FEM:
				switch (player.getQuestStage(this.getQuestID())) {
					case WITCH_PAID:
						switch (d.getChatId()) {
							case 1:
								if (this.isWearingLucienPendant(player)) {
									d.sendNpcChat("Thou is a foul agent of Lucien! Such an agent must die!");
									d.endDialogue();
									CombatManager.attack(World.getNpcs()[player.getNpcClickIndex()], player);
								} else {
									d.sendNpcChat("Thou hast ventured deep into the tunnels, you have", "reached the temple of our master. it is many ages", "since a pilgrim has come here.");
								}
								return true;
							case 2:
								d.sendOption("I seek the Staff of Armadyl", "Out of my way fool!", "What are your kind and what are you doing here?");
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
								d.sendNpcChat("We are the Guardians of Armadyl. We have kept the", "temple safe for many ages. The evil in the dungeons", "seek what lies here. the Mahjarrat are the worst.");
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
								d.sendNpcChat("Armadyl is the god we serve. We have been charged", "with guarding his sacred artefacts until he requires them.");
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
								d.sendNpcChat("They are ancient and powerful beings! They are very", "evil! It is said that they once dominated this place of", "existance, Zamorak was supposedly of their blood. They", "are far fewer in number now.");
								return true;
							case 14:
								d.sendNpcChat("Some still have presence in this world in their liche", "forms. Mahjarrat such as Lucien and Azzanadra would", "become very powerful if they came into possession of the", "Staff of Armadyl.");
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
								d.sendOption("How dare you call me a fool!", "I just thought of something i must do!", "You're right it's time for my yearly bath.");
								return true;
							case 21:
								d.sendPlayerChat(d.tempStrings[optionId - 1]);
								switch (optionId) {
									case 1:
										d.setNextChatId(22);
										break;
									case 2:
										d.setNextChatId(23);
										break;
									case 3:
										d.setNextChatId(24);
										break;
								}
								return true;
							case 22:
								d.sendNpcChat("Thou is a foul agent of Lucien! Such an agent must die!");
								d.endDialogue();
								CombatManager.attack(World.getNpcs()[player.getNpcClickIndex()], player);
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
									d.sendNpcChat("Thou is a foul agent of Lucien! Such an agent must die!");
									d.endDialogue();
									CombatManager.attack(World.getNpcs()[player.getNpcClickIndex()], player);
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
								d.sendNpcChat("Thou is a foul agent of Lucien! Such an agent must die!");
								d.endDialogue();
								player.getQuestVars().setSidedWithLucien(true);
								player.setQuestStage(this.getQuestID(), SIDE_CHOSEN);
								CombatManager.attack(World.getNpcs()[player.getNpcClickIndex()], player);
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
							d.endDialogue();
							World.getNpcs()[player.getNpcClickIndex()].getUpdateFlags().setForceChatMessage("Thou is a foul agent of Lucien! Such an agent must die!");
							CombatManager.attack(World.getNpcs()[player.getNpcClickIndex()], player);
							return true;
						}
						switch (d.getChatId()) {
							case 1:
								d.sendNpcChat("Have you rid us of lucien yet?");
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
					case QUEST_COMPLETE:
						break;
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
									d.sendGiveItemNpc("You give Lucien the Staff of Armadyl", new Item(STAFF_OF_ARMADYL));
									player.getInventory().removeItem(new Item(STAFF_OF_ARMADYL));
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
								return true;
						}
						return false;
				}
				return false;
		}
		return false;
	}

}
