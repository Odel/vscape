package com.rs2.model.content.quests;

import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LAUGHING;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ShopManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class HorrorFromTheDeep implements Quest {

	public static final int QUEST_STARTED = 1;
	public static final int KEY_TO_LARRISSA = 2;
	public static final int DOOR_UNLOCKED = 3;
	public static final int DAGANNOTH_DOOR_UNLOCKED = 4;
	public static final int CHILD_DAG_DEAD = 5;
	public static final int MOTHER_DEAD = 6;
	public static final int TALK_TO_JOSSIK = 7;
	public static final int FINISH_UP_QUEST = 8;
	public static final int QUEST_COMPLETE = 9;

	public static final int HAMMER = 2347;
	public static final int FIRE_RUNE = 554;
	public static final int WATER_RUNE = 555;
	public static final int AIR_RUNE = 556;
	public static final int EARTH_RUNE = 557;
	public static final int MOLTEN_GLASS = 1775;
	public static final int TINDERBOX = 590;
	public static final int SWAMP_TAR = 1939;
	public static final int STEEL_NAILS = 1539;
	public static final int PLANK = 960;
	public static final int LIGHTHOUSE_KEY = 3848;
	public static final int JOURNAL = 3845;
	public static final int DIARY = 3846;
	public static final int MANUAL = 3847;
	public static final int RUSTY_CASKET = 3849;
	public static final int DAMAGED_SARA_BOOK = 3839;
	public static final int SARA_BOOK = 3840;
	public static final int DAMAGED_ZAM_BOOK = 3841;
	public static final int ZAMORAK_BOOK = 3842;
	public static final int DAMAGED_GUTH_BOOK = 3843;
	public static final int GUTHIX_BOOK = 3844;

	public static final Position UP_AT_LIGHTHOUSE = new Position(2510, 3644, 0);
	public static final Position UP_FROM_BOSS_CAVE = new Position(2515, 4629, 1);
	public static final Position DOWN_IN_BOSS_CAVE = new Position(2515, 10008, 0);
	public static final Position DOWN_IN_BOSS_CAVE_POST_QUEST = new Position(2515, 4632, 0);
	public static final Position DOWN_IN_CAVE_POST_QUEST = new Position(2519, 4619, 1);
	public static final Position DOWN_IN_CAVE = new Position(2519, 9995, 1);

	public static final int DOOR_INTERFACE = 10116;

	public static final int GUNNJORN = 607;
	public static final int LARRISSA = 1336;
	public static final int LARRISSA_2 = 1337;
	public static final int JOSSIK = 1334;
	public static final int CHILD_DAGANNOTH = 1347;
	public static final int SITTING_JOSSIK = 1335;
	public static final int WHITE_MOTHER = 1351;
	public static final int BLUE_MOTHER = 1352;
	public static final int RED_MOTHER = 1353;
	public static final int BROWN_MOTHER = 1354;
	public static final int GREEN_MOTHER = 1355;
	public static final int ORANGE_MOTHER = 1356;
	public static final int[] MOTHER_COLORS = {WHITE_MOTHER, BLUE_MOTHER, RED_MOTHER, BROWN_MOTHER, GREEN_MOTHER, ORANGE_MOTHER};
	public static final String[] CRIES = {"Krrrrrrk", "Ksssrsrkrsk", "Srrkkrkrkrk", "Sssssssrrrk", "Chkhkhkhkhk"};

	public static final int BOOKSHELF = 4617;
	public static final int LIGHTHOUSE_DOOR = 4577;
	public static final int LADDER_UP_TO_WALL = 4413;
	public static final int LADDER_DOWN_TO_CAVE = 4383;
	public static final int LADDER_DOWN_TO_BOSS_CAVE = 4485;
	public static final int LADDER_UP_TO_LIGHTHOUSE = 4412;
	public static final int STUDY_WALL = 4544;
	public static final int STUDY_WALL_2 = 4543;
	public static final int OPEN_WALL = 4546;
	public static final int OPEN_WALL_2 = 4545;
	public static final int FIRST_ROCK_TO_SHORE = 4550;
	public static final int SHORE_TO_FIRST_ROCK = 4551;
	public static final int SECOND_ROCK_FROM_FIRST = 4553;
	public static final int FIRST_ROCK_FROM_SECOND = 4552;
	public static final int THIRD_ROCK_FROM_SECOND = 4555;
	public static final int SECOND_ROCK_FROM_THIRD = 4554;
	public static final int FOURTH_ROCK_FROM_THIRD = 4557;
	public static final int THIRD_ROCK_FROM_FOURTH = 4556;
	public static final int FOURTH_ROCK_TO_SHORE = 4559;
	public static final int SHORE_TO_FOURTH_ROCK = 4558;

	public int dialogueStage = 0;

	private int reward[][] = {};
	private int expReward[][] = {
		{Skill.MAGIC, 4662},
		{Skill.STRENGTH, 4662},
		{Skill.RANGED, 4662},};

	private static final int questPointReward = 2;

	public int getQuestID() {
		return 26;
	}

	public String getQuestName() {
		return "Horror From The Deep";
	}

	public String getQuestSaveName() {
		return "horror-deep";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {
		for (int[] rewards : reward) {
			player.getInventory().addItem(new Item(rewards[0], rewards[1]));
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
		player.getActionSender().sendItemOnInterface(995, 200, 12142);
		player.getActionSender().sendString("You have survived the " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("2 Quest Points", 12150);
		player.getActionSender().sendString("10489.5 XP in each of: Ranged,", 12151);
		player.getActionSender().sendString("Magic, and Strength", 12152);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 10135);
	}

	public void sendQuestRequirements(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		int questStage = player.getQuestStage(getQuestID());
		if (questStage == QUEST_STARTED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);

			player.getActionSender().sendString("Larrissa expressed her concerns about her boyfriend.", 8149);
			player.getActionSender().sendString("I need to find Larrissa's cousin who has a lighthouse key.", 8150);
			player.getActionSender().sendString("Larrissa said his name is Gunnjorn and he loves Agility.", 8151);
		} else if (questStage == KEY_TO_LARRISSA) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);
			player.getActionSender().sendString("@str@" + "Larrissa expressed her concerns about her boyfriend.", 8149);
			player.getActionSender().sendString("@str@" + "I need to find Larrissa's cousin who has a lighthouse key.", 8150);

			player.getActionSender().sendString("I found Gunnjorn at the Barbarian Outpost. He gave me the", 8152);
			player.getActionSender().sendString("spare lighthouse key. I should return to Larrissa.", 8153);
		} else if (questStage == DOOR_UNLOCKED || questStage == DAGANNOTH_DOOR_UNLOCKED) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);
			player.getActionSender().sendString("@str@" + "Larrissa expressed her concerns about her boyfriend.", 8149);
			player.getActionSender().sendString("@str@" + "I need to find Larrissa's cousin who has a lighthouse key.", 8150);
			player.getActionSender().sendString("@str@" + "I found Gunnjorn at the Barbarian Outpost.", 8152);

			player.getActionSender().sendString("I need to search the lighthouse for clues as to where", 8154);
			player.getActionSender().sendString("Jossik could be.", 8155);
		} else if (questStage == CHILD_DAG_DEAD) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);
			player.getActionSender().sendString("@str@" + "Larrissa expressed her concerns about her boyfriend.", 8149);
			player.getActionSender().sendString("@str@" + "I need to find Larrissa's cousin who has a lighthouse key.", 8150);
			player.getActionSender().sendString("@str@" + "I found Gunnjorn at the Barbarian Outpost.", 8152);
			player.getActionSender().sendString("@str@" + "I need to search the lighthouse for clues as to where", 8154);
			player.getActionSender().sendString("@str@" + "Jossik could be.", 8155);

			player.getActionSender().sendString("I found Jossik injured beneath the lighthouse.", 8157);
			player.getActionSender().sendString("I need to defend him from the dagannoths to rescue him!", 8158);
		} else if (questStage == MOTHER_DEAD) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);
			player.getActionSender().sendString("@str@" + "Larrissa expressed her concerns about her boyfriend.", 8149);
			player.getActionSender().sendString("@str@" + "I need to find Larrissa's cousin who has a lighthouse key.", 8150);
			player.getActionSender().sendString("@str@" + "I found Gunnjorn at the Barbarian Outpost.", 8152);
			player.getActionSender().sendString("@str@" + "I need to search the lighthouse for clues as to where", 8154);
			player.getActionSender().sendString("@str@" + "Jossik could be.", 8155);
			player.getActionSender().sendString("@str@" + "I found Jossik injured beneath the lighthouse.", 8157);

			player.getActionSender().sendString("I defeated the dagannoth mother. I should get", 8159);
			player.getActionSender().sendString("Jossik out of here.", 8160);
		} else if (questStage == TALK_TO_JOSSIK) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);
			player.getActionSender().sendString("@str@" + "Larrissa expressed her concerns about her boyfriend.", 8149);
			player.getActionSender().sendString("@str@" + "I need to find Larrissa's cousin who has a lighthouse key.", 8150);
			player.getActionSender().sendString("@str@" + "I found Gunnjorn at the Barbarian Outpost.", 8152);
			player.getActionSender().sendString("@str@" + "I need to search the lighthouse for clues as to where", 8154);
			player.getActionSender().sendString("@str@" + "Jossik could be.", 8155);
			player.getActionSender().sendString("@str@" + "I found Jossik injured beneath the lighthouse.", 8157);
			player.getActionSender().sendString("@str@" + "I defeated the dagannoth mother.", 8159);

			player.getActionSender().sendString("Jossik managed to get to a safe area of the cave.", 8161);
			player.getActionSender().sendString("I should talk to him about this casket.", 8162);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("@str@" + "Talk to Larrissa, outside the Lighthouse to begin.", 8147);

			player.getActionSender().sendString("@red@" + "You have completed this quest!", 8170);
		} else {
			player.getActionSender().sendString(getQuestName(), 8144);
			player.getActionSender().sendString("Talk to @dre@Larrissa @bla@outside the @dre@Lighthouse @bla@, north", 8147);
			player.getActionSender().sendString("of the Barbarian Outpost to begin this quest.", 8148);
			player.getActionSender().sendString("@dre@Requirements:", 8149);
			if (player.getSkill().getLevel()[Skill.AGILITY] < 35) {
				player.getActionSender().sendString("-35 Agility.", 8150);
			} else {
				player.getActionSender().sendString("@str@-35 Agility.", 8150);
			}
			player.getActionSender().sendString("@dre@-I must be able to defeat strong level 100 enemies.", 8151);
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 10135);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 10135);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 10135);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 10135);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to @dre@Larrissa @bla@outside the @dre@Lighthouse @bla@, north", 8147);
		player.getActionSender().sendString("of the Barbarian Outpost to begin this quest.", 8148);
		player.getActionSender().sendString("@dre@Requirements:", 8149);
		if (player.getSkill().getLevel()[Skill.AGILITY] < 35) {
			player.getActionSender().sendString("-35 Agility.", 8150);
		} else {
			player.getActionSender().sendString("@str@-35 Agility.", 8150);
		}
		player.getActionSender().sendString("@dre@-I must be able to defeat strong level 100 enemies.", 8151);
	}

	public static boolean isAirSpell(int graphic) {
		switch (graphic) {
			case 92:
			case 119:
			case 134:
			case 160:
				return true;
		}
		return false;
	}

	public static boolean isWaterSpell(int graphic) {
		switch (graphic) {
			case 95:
			case 122:
			case 137:
			case 163:
				return true;
		}
		return false;
	}

	public static boolean isEarthSpell(int graphic) {
		switch (graphic) {
			case 98:
			case 125:
			case 140:
			case 166:
				return true;
		}
		return false;
	}

	public static boolean isFireSpell(int graphic) {
		switch (graphic) {
			case 101:
			case 128:
			case 131:
			case 157:
				return true;
		}
		return false;
	}

	public static boolean motherSpawned(final Player player) {
		for (int i = 1348; i < 1357; i++) {
			if (NpcLoader.checkSpawn(player, i)) {
				return true;
			}
		}
		return false;
	}

	public static void spawnEncounter(final Player player, final int npcId) {
		final Npc npc = new Npc(npcId);
		NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, npc, new Position(2504, 10021, 0), true, null);
		npc.sendTransform(npcId - 3, 20);
		npc.setFollowingEntity(player);
		player.setFollowingEntity(npc);
		player.walkTo(new Position(2512, 10020, 0), true);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				npc.getMovementHandler().setCanWalk(true);
				npc.walkTo(new Position(2506, 10021, 0), false);
				b.stop();
			}

			@Override
			public void stop() {
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						npc.getMovementHandler().setCanWalk(true);
						npc.walkTo(new Position(2508, 10021, 0), false);
						b.stop();
					}

					@Override
					public void stop() {
						npc.sendTransform(npcId - 1, 20);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								npc.getMovementHandler().setCanWalk(true);
								npc.walkTo(new Position(2512, 10019, 0), false);
								b.stop();
							}

							@Override
							public void stop() {
								npc.sendTransform(npcId, 20000);
								CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer b) {
										npc.getMovementHandler().setCanWalk(true);
										npc.setPlayerOwner(player.getIndex());
										npc.walkTo(new Position(2512, 10019, 0), false);
										b.stop();
									}

									@Override
									public void stop() {
										npc.walkTo(new Position(2512, 10019, 0), false);
										CombatManager.attack(npc, player);
										player.setStopPacket(false);
										if (npc.getNpcId() == WHITE_MOTHER) {
											CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
												@Override
												public void execute(CycleEventContainer b) {
													if (npc.isDead() || npc.getCurrentHp() == 0 || player.getQuestStage(26) >= 6 || !Misc.goodDistance(player.getPosition(), npc.getPosition(), 30) || !npc.isAttacking()) {
														b.stop();
														return;
													}
													int color = MOTHER_COLORS[Misc.randomMinusOne(MOTHER_COLORS.length)];
													int hp = npc.getCurrentHp();
													do {
														color = MOTHER_COLORS[Misc.randomMinusOne(MOTHER_COLORS.length)];
													} while (color == npc.getTransformId());
													if (color == npc.getTransformId()) {
														do {
															color = MOTHER_COLORS[Misc.randomMinusOne(MOTHER_COLORS.length)];
														} while (color == npc.getTransformId());
													}
													npc.getUpdateFlags().sendForceMessage(CRIES[Misc.randomMinusOne(CRIES.length)]);
													switch (color) {
														case WHITE_MOTHER:
															player.getActionSender().sendMessage("The Dagannoth changes to white...");
															break;
														case BLUE_MOTHER:
															player.getActionSender().sendMessage("The Dagannoth changes to blue...");
															break;
														case RED_MOTHER:
															player.getActionSender().sendMessage("The Dagannoth changes to red...");
															break;
														case ORANGE_MOTHER:
															player.getActionSender().sendMessage("The Dagannoth changes to orange...");
															break;
														case BROWN_MOTHER:
															player.getActionSender().sendMessage("The Dagannoth changes to brown...");
															break;
														case GREEN_MOTHER:
															player.getActionSender().sendMessage("The Dagannoth changes to green...");
															break;
													}
													npc.sendTransform(color, 20);
													npc.setCurrentHp(hp);
												}

												@Override
												public void stop() {
													NpcLoader.destroyNpc(npc);
												}
											}, 15);
										}
									}
								}, 3);
							}
						}, 3);
					}
				}, 3);
			}
		}, 6);
	}

	public static boolean allItemsInDoor(final Player player) {
		return player.getQuestVars().hasPlacedAirRune() && player.getQuestVars().hasPlacedFireRune() && player.getQuestVars().hasPlacedEarthRune() && player.getQuestVars().hasPlacedWaterRune() && player.getQuestVars().hasPlacedSword() && player.getQuestVars().hasPlacedArrow();
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {
			case DIARY:
				Dialogues.startDialogue(player, 13789);
				return true;
			case MANUAL:
				Dialogues.startDialogue(player, 13787);
				return true;
			case JOURNAL:
				Dialogues.startDialogue(player, 13788);
				return true;
		}
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {
			case OPEN_WALL:
			case OPEN_WALL_2:
			case STUDY_WALL:
			case STUDY_WALL_2:
				switch (player.getQuestStage(26)) {
					case 3:
						switch (item) {
							default:
								Item used = new Item(item);
								if (used.getDefinition().getName().contains("sword")) {
									Dialogues.startDialogue(player, 88978);
									player.setTempInteger(item);
									return true;
								} else if (used.getDefinition().getName().contains("arrow")) {
									Dialogues.startDialogue(player, 88979);
									player.setTempInteger(item);
									return true;
								} else {
									return false;
								}
							case FIRE_RUNE:
								player.getInventory().removeItem(new Item(FIRE_RUNE, 1));
								player.getQuestVars().setPlacedFireRune(true);
								player.getActionSender().sendMessage("You place the fire rune into the slot in the door.");
								if (allItemsInDoor(player)) {
									player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
									player.setQuestStage(26, 4);
								}
								return true;
							case WATER_RUNE:
								player.getInventory().removeItem(new Item(WATER_RUNE, 1));
								player.getQuestVars().setPlacedWaterRune(true);
								player.getActionSender().sendMessage("You place the water rune into the slot in the door.");
								if (allItemsInDoor(player)) {
									player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
									player.setQuestStage(26, 4);
								}
								return true;
							case EARTH_RUNE:
								player.getInventory().removeItem(new Item(EARTH_RUNE, 1));
								player.getQuestVars().setPlacedEarthRune(true);
								player.getActionSender().sendMessage("You place the earth rune into the slot in the door.");
								if (allItemsInDoor(player)) {
									player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
									player.setQuestStage(26, 4);
								}
								return true;
							case AIR_RUNE:
								player.getInventory().removeItem(new Item(AIR_RUNE, 1));
								player.getQuestVars().setPlacedAirRune(true);
								player.getActionSender().sendMessage("You place the air rune into the slot in the door.");
								if (allItemsInDoor(player)) {
									player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
									player.setQuestStage(26, 4);
								}
								return true;
						}
				}
			case LIGHTHOUSE_DOOR:
				if (item == LIGHTHOUSE_KEY && player.getQuestStage(26) == 2) {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3636 ? 1 : -1, true);
					//player.getActionSender().walkThroughDoor(object, 2509, 3636, 0); door is broke as fuk
					player.getActionSender().sendMessage("You unlock the lighthouse front door.");
					player.setQuestStage(26, 3);
					player.getInventory().removeItem(new Item(LIGHTHOUSE_KEY));
					return true;
				}
				return true;
		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case BOOKSHELF:
				Dialogues.startDialogue(player, 97979);
				return true;
			case 4570: //lighthouse top level ladder
				Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
				return true;
			case 4569: //lighthouse level 1 ladder
				Dialogues.startDialogue(player, 14567);
				return true;
			case 4568: //lighthouse ladder up
				Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
				return true;
			case LADDER_UP_TO_WALL:
				if (player.getQuestStage(26) < 7) {
					player.getActionSender().sendMessage("Jossik needs my help!");
					return true;
				} else {
					Ladders.climbLadder(player, UP_FROM_BOSS_CAVE);
					return true;
				}
			case LADDER_UP_TO_LIGHTHOUSE:
				Ladders.climbLadder(player, UP_AT_LIGHTHOUSE);
				return true;
			case LADDER_DOWN_TO_CAVE:
				if (player.getQuestStage(26) >= 7) {
					Ladders.climbLadder(player, DOWN_IN_CAVE_POST_QUEST);
					return true;
				} else {
					Ladders.climbLadder(player, DOWN_IN_CAVE);
					return true;
				}
			case LADDER_DOWN_TO_BOSS_CAVE:
				if (player.getQuestStage(26) >= 7) {
					Ladders.climbLadder(player, DOWN_IN_BOSS_CAVE_POST_QUEST);
					return true;
				} else {
					Ladders.climbLadder(player, DOWN_IN_BOSS_CAVE);
					return true;
				}
			case OPEN_WALL:
			case OPEN_WALL_2:
				if (player.getQuestStage(26) < 4) {
					player.getActionSender().sendMessage("This door is mysteriously locked.");
					return true;
				} else {
					player.getActionSender().walkTo(0, player.getPosition().getY() < y ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 1);
					return true;
				}
			case STUDY_WALL:
			case STUDY_WALL_2:
				player.getActionSender().sendInterface(DOOR_INTERFACE);
				return true;
			case LIGHTHOUSE_DOOR:
				switch (player.getQuestStage(26)) {
					case 0:
					case 1:
					case 2:
						player.getActionSender().sendMessage("This door is locked firmly.");
						return true;
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
						player.getActionSender().walkTo(0, player.getPosition().getY() < 3636 ? 1 : -1, true);
						//player.getActionSender().walkThroughDoor(object, x, y, 0);
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

	public static boolean doMiscObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case FIRST_ROCK_TO_SHORE:
			case SHORE_TO_FIRST_ROCK:
			case SECOND_ROCK_FROM_FIRST:
			case FIRST_ROCK_FROM_SECOND:
			case THIRD_ROCK_FROM_SECOND:
			case SECOND_ROCK_FROM_THIRD:
			case FOURTH_ROCK_FROM_THIRD:
			case THIRD_ROCK_FROM_FOURTH:
			case FOURTH_ROCK_TO_SHORE:
			case SHORE_TO_FOURTH_ROCK:
				if (Misc.random(69) == 5 && Misc.goodDistance(player.getPosition().clone(), new Position(x, y, 0), 2)) {
					player.getActionSender().sendMessage("You slip while jumping across the rock...");
					player.fadeTeleport(new Position(2518, 3594, 0));
					player.getActionSender().sendMessage("...You find yourself washed up on shore.");
					return true;
				} else {
					if (Misc.goodDistance(player.getPosition().clone(), new Position(x, y, 0), 2)) {
						Agility.jumpRock(player, x, y, 769, 2, 0, 0);
						return true;
					} else {
						player.walkTo(x, y, false);
						return true;
					}
				}
		}
		return false;
	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case 13787: //manual dialogue
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("'The Light-o-Matic 2000 lighthouse model.'", "Oh boy, this seems boring.", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("'Swamp tar... Planks.... Oil.....'", "Blah blah, what a bunch of nonsense. I definitely", "don't need anything out of this book.", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendStatement("You throw the useless manual away.");
						player.getInventory().removeItem(new Item(MANUAL));
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 13788: //journal dialogue
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("'The daily journal of Jossik'.", "Well, fantastic, this might tell me where he's gone.", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendPlayerChat("'The light seemed to turn a bit faster today.", "I oiled the gears yesterday, perhaps it was too much.", "If only Silas were here, he might know.'", CONTENT);
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("Hm. That's the last entry, it's completely normal.", "This doesn't tell me anything about Jossik's whereabouts.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case 13789: //diary dialogue
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("This diary is rather old, hmm... I wonder", "what it contains...", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendStatement("Dust and crumbled pages fall out of the book.");
						return true;
					case 3:
						player.getDialogue().sendPlayerChat("There's not much here that is legible...", CONTENT);
						return true;
					case 4:
						player.getDialogue().sendPlayerChat("'Ancient dwellers... sharp ... cunning...'", "There's some sort of drawing with slots on it, hmm...", "'cavern door... willing to risk ... may never...'", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendPlayerChat("Not sure what that is all about.", "It seems related to the lighthouse though.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				return false;
			case JOSSIK:
				switch (player.getQuestStage(26)) {
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("You know, it isn't too bad down here.", "It's quiet and right under the lighthouse!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendOption("That's nice.", "I have a question about my god book.");
								return true;
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("That's nice.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("I have a question about my god book.", CONTENT);
										return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("Alright! Go ahead, ask away!", CONTENT);
								return true;
							case 5:
								if (player.getLostGodBook() != -1 && !player.getInventory().ownsItem(player.getLostGodBook())) {
									player.getDialogue().sendOption("Would it be possible to exchange my book?", "Found any more information on the books?", "I lost my book...");
									return true;
								} else {
									player.getDialogue().sendOption("Would it be possible to exchange my book?", "Found any more information on the books?");
									return true;
								}
							case 6:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Would it be possible to exchange my book?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Found any more information on the books?", CONTENT);
										player.getDialogue().setNextChatId(22);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("I lost my book...", SAD);
										player.getDialogue().setNextChatId(20);
										return true;
								}
							case 7:
								player.getDialogue().sendNpcChat("I wouldn't see why not... But I would have to", "have another book to exchange with you!", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("And do you have another book...?", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Fortune is with you adventurer!", "I do, I do, I do... Yes.", LAUGHING);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("I found a stash of damaged books deep in this cave.", "I risked my life again, but it was worth it!", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendNpcChat("I can either exchange your book with one of mine,", "or sell you another for 100,000 gold.", "But remember, the books I have are damaged and empty.", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendOption("I'd like to exchange my book.", "I'll buy a book.");
								return true;
							case 13:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I'd like to exchange my book.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("I'll buy a book.", CONTENT);
										player.getDialogue().setNextChatId(30);
										return true;
								}
							case 14:
								if (!player.getInventory().playerHasItem(new Item(player.getGodBook()))) {
									player.getDialogue().sendNpcChat("Oh, I'm afraid you don't have your book.", "Come back when you have it.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendNpcChat("Alright, which book do you want in exchange?", CONTENT);
									return true;
								}
							case 15:
								player.getDialogue().sendOption("Saradomin.", "Zamorak.", "Guthix.");
								return true;
							case 16:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Saradomin.", CONTENT);
										player.setTempInteger(DAMAGED_SARA_BOOK);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Zamorak.", CONTENT);
										player.setTempInteger(DAMAGED_ZAM_BOOK);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("Guthix.", CONTENT);
										player.setTempInteger(DAMAGED_GUTH_BOOK);
										return true;
								}
							case 17:
								player.getDialogue().sendNpcChat("Alright, here you are!", CONTENT);
								player.getDialogue().endDialogue();
								player.getInventory().replaceItemWithItem(new Item(player.getGodBook()), new Item(player.getTempInteger()));
								player.setGodBook(player.getTempInteger());
								player.setTempInteger(0);
								return true;
							case 20:
								if (player.getInventory().canAddItem(new Item(player.getLostGodBook()))) {
									player.getDialogue().sendNpcChat("Well, luckily for you my wife has quite the travels!", "She told me she came across a book on the ground.", "I believe this one is yours.", CONTENT);
									player.getDialogue().endDialogue();
									player.getInventory().addItemOrDrop(new Item(player.getLostGodBook()));
									player.setLostGodBook(-1);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Oh, you don't have room for a new book!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 22:
								player.getDialogue().sendNpcChat("Eh, not so much. What I told you before about them", "is still all I know. I'm on the hunt for more", "answers though.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 30:
								player.getDialogue().dontCloseInterface();
								ShopManager.openShop(player, 167);
								return true;
						}
						return false;
					case FINISH_UP_QUEST:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("So what do I do with this...'book'?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Well, it looks to be incomplete. If you can find the", "missing pages it should have it's full power again!", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("And what power is that?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Well, it's a holy book of a god!", "It'll grant you a nice bonus to Prayer.", "You should also be able to preach from the book to others.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendPlayerChat("Hmm, interesting. Thank you Jossik.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("No, thank you for saving my life in that cave!", "I owe you big time!", CONTENT);
								return true;
							case 7:
								player.getDialogue().dontCloseInterface();
								QuestHandler.completeQuest(player, 26);
								return true;
						}
						return false;
					case TALK_TO_JOSSIK:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Oh, good, you made it to safety!", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("It seems I was not as injured as I thought...", "paralyzed by fear I suppose. I must thank you for", "all your help!", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Now, about that casket you found on that monster's", "corpse...", CONTENT);
								return true;
							case 4:
								if (!player.getInventory().ownsItem(RUSTY_CASKET)) {
									player.getDialogue().sendPlayerChat("Oh, it seems I didn't grab the casket...", SAD);
									player.getDialogue().setNextChatId(17);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I have it here. You said you might be able to tell me", "something about it...?", CONTENT);
									return true;
								}
							case 5:
								player.getDialogue().sendNpcChat("I can indeed! Here, let me have a closer look...", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Yes! There is something written on it!", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("It is very faint however...", "Can you read it?", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("It says...", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendOption("Saradomin", "Zamorak", "Guthix");
								return true;
							case 10:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("'Saradomin'.", CONTENT);
										player.setTempInteger(DAMAGED_SARA_BOOK);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("'Zamorak'.", CONTENT);
										player.setTempInteger(DAMAGED_ZAM_BOOK);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("'Guthix'.", CONTENT);
										player.setTempInteger(DAMAGED_GUTH_BOOK);
										return true;
								}
							case 11:
								player.getDialogue().sendNpcChat("Are you sure?", CONTENT);
								return true;
							case 12:
								player.getDialogue().sendOption("Yes.", "Perhaps I need to take another look.");
								return true;
							case 13:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I'm sure.", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Perhaps I need to take another look.", CONTENT);
										player.getDialogue().setNextChatId(10);
										return true;
								}
							case 14:
								player.getDialogue().sendNpcChat("I think you're right!", "Let's see what's inside...", CONTENT);
								return true;
							case 15:
								player.setGodBook(player.getTempInteger());
								player.setTempInteger(0);
								Item item = new Item(player.getGodBook());
								player.getInventory().replaceItemWithItem(new Item(RUSTY_CASKET), item);
								player.getDialogue().sendNpcChat(item.getDefinition().getDescription() + ".. Wow!", "I thought these had all vanished!", HAPPY);
								player.getDialogue().setNextChatId(1);
								player.setQuestStage(26, 8);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("Lucky for you, I grabbed it before I left.", "You're bold to leave something so curious behind!", "Let me take a look at it...", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						return false;
				}
				return false;
			case SITTING_JOSSIK:
				switch (player.getQuestStage(26)) {
					case TALK_TO_JOSSIK:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Meet me up past that strange door.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case MOTHER_DEAD:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Okay, it's dead, let's get out of here!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Yes, quickly, the mother might be dead, but it's", "children are not!", DISTRESSED);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Follow me to the upper level, I may be able to", "help you with that casket that dropped.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("It looks... familiar somehow...", CONTENT);
								player.getDialogue().endDialogue();
								player.setQuestStage(26, 7);
								return true;
						}
						return false;
					case CHILD_DAG_DEAD:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (motherSpawned(player)) {
									player.getDialogue().sendNpcChat("What are you doing talking to me?!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Okay, now that the creature's dead we can get you", "out of here.", HAPPY);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("No... you do not understand...", SAD);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("That was not the creature that attacked me...", DISTRESSED);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("That was one of it's babies...", DISTRESSED);
								return true;
							case 5:
								player.getDialogue().endDialogue();
								spawnEncounter(player, WHITE_MOTHER);
								break;
						}
						return false;
					case DAGANNOTH_DOOR_UNLOCKED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("*cough*", "Please... please help me...", "I think my leg is broken, and those creatures will be", "back any minute now!", SAD);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I guess you're Jossik then...", "What creatures are you talking about?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("I... I do not know.", "I've never seen their like before!", DISTRESSED);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("I was searching for information about my uncle Silas,", "who vanished mysteriously from this lighthouse many", "months ago. I found the secret of that strange wall, and", "discovered that I could use it as a door, but when I", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("came down here...I was attacked by...", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendStatement("Jossik shudders violently.");
								return true;
							case 7:
								player.getDialogue().sendNpcChat("...I do not know what they are, but they are very", "strong. They hurt me badly enough to trap me here,", "and I have been fearing for my life ever since!", SAD);
								return true;
							case 8:
								player.getDialogue().sendPlayerChat("Don't worry, I'm here now. Larrissa was", "worried about you and asked for my help.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("I'll go back upstairs and let her know that I've found", "you and that you're still alive, and then we can work", "out some way of getting you out of here, okay?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("NO! No, you can't leave me now!", "Look! They're coming again! Do something!!", DISTRESSED);
								return true;
							case 11:
								player.getDialogue().sendPlayerChat("Okay, I'll keep them at bay, hold on...", CONTENT);
								return true;
							case 12:
								player.getDialogue().endDialogue();
								spawnEncounter(player, CHILD_DAGANNOTH);
								break;
						}
						return false;
				}
				return false;
			case 88979: //arrow in wall
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("I don't think I'll get this back...", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendOption("Continue anyways.", "Don't place the item in the wall.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								Item item = new Item(player.getTempInteger());
								player.getInventory().removeItem(item);
								player.getQuestVars().setPlacedArrow(true);
								player.getActionSender().sendMessage("You place the " + item.getDefinition().getName() + " into the slot in the door.");
								if (allItemsInDoor(player)) {
									player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
									player.setQuestStage(26, 4);
								}
								player.setTempInteger(0);
								break;
							case 2:
								player.getDialogue().endDialogue();
								player.setTempInteger(0);
								break;
						}
				}
				return false;
			case 88978: //sword in wall
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendPlayerChat("I don't think I'll get this back...", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendOption("Continue anyways.", "Don't place the item in the wall.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								Item item = new Item(player.getTempInteger());
								player.getInventory().removeItem(item);
								player.getQuestVars().setPlacedSword(true);
								player.getActionSender().sendMessage("You place the " + item.getDefinition().getName() + " into the slot in the door.");
								if (allItemsInDoor(player)) {
									player.getActionSender().sendMessage("You hear the sound of something moving into place in the wall.");
									player.setQuestStage(26, 4);
								}
								player.setTempInteger(0);
								break;
							case 2:
								player.getDialogue().endDialogue();
								player.setTempInteger(0);
								break;
						}
				}
				return false;
			case 97979: //bookshelf
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendStatement("There are three books here that look important...", "What would you like to do?");
						return true;
					case 2:
						player.getDialogue().sendOption("Take the Manual.", "Take the Diary.", "Take the Journal.", "Take all three books.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								if (player.getInventory().ownsItem(MANUAL)) {
									player.getDialogue().sendStatement("You already have the manual.");
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getInventory().addItemOrDrop(new Item(MANUAL));
									break;
								}
							case 2:
								if (player.getInventory().ownsItem(DIARY)) {
									player.getDialogue().sendStatement("You already have the diary.");
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getInventory().addItemOrDrop(new Item(DIARY));
									break;
								}
							case 3:
								if (player.getInventory().ownsItem(JOURNAL)) {
									player.getDialogue().sendStatement("You already have the journal.");
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getInventory().addItemOrDrop(new Item(JOURNAL));
									break;
								}
							case 4:
								if (player.getInventory().ownsItem(MANUAL)) {
									player.getActionSender().sendMessage("You already have the manual.");
								} else if (!player.getInventory().ownsItem(MANUAL)) {
									player.getInventory().addItemOrDrop(new Item(MANUAL));
								}
								if (player.getInventory().ownsItem(DIARY)) {
									player.getActionSender().sendMessage("You already have the diary.");
								} else if (!player.getInventory().ownsItem(DIARY)) {
									player.getInventory().addItemOrDrop(new Item(DIARY));
								}
								if (player.getInventory().ownsItem(JOURNAL)) {
									player.getActionSender().sendMessage("You already have the journal.");
								} else if (!player.getInventory().ownsItem(JOURNAL)) {
									player.getInventory().addItemOrDrop(new Item(JOURNAL));
								}
								break;
						}
				}
				return false;
			case 14567: //lighthouse ladder
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendOption("Climb ladder up.", "Climb ladder down.", "Do nothing.");
						return true;
					case 2:
						switch (optionId) {
							case 1:
								Ladders.climbLadder(player, new Position(2505, 3641, 2));
								player.getDialogue().endDialogue();
								break;
							case 2:
								Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
								player.getDialogue().endDialogue();
								break;
							case 3:
								player.getDialogue().endDialogue();
								break;
						}
				}
				return false;
			case GUNNJORN:
				switch (player.getQuestStage(26)) {
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Welcome to my obstacle course. Have fun, but", "remember this isn't a child's playground. People have", "died here.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case KEY_TO_LARRISSA:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getInventory().ownsItem(LIGHTHOUSE_KEY)) {
									player.getDialogue().sendNpcChat("Welcome to my obstacle course. Have fun, but", "remember this isn't a child's playground. People have", "died here.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Erm, I seem to have lost the key you gave me.", SAD);
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("Lucky for you, one of my barbarian scouts found it.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendGiveItemNpc("Gunnjorn hands you a key.", new Item(LIGHTHOUSE_KEY));
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(LIGHTHOUSE_KEY));
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Welcome to my obstacle course. Have fun, but", "remember this isn't a child's playground. People have", "died here.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("You're Gunnjorn, aren't you?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Why, indeed I am. I own this here agility course.", "It can be very dangerous for adventurers like you.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Yeah, that's great. I understand you have a cousin", "named Larrissa who gave you a key...?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Yes, she did! How do you know of this? She said she", "probably wouldn't need it, but gave it to me for safe", "keeping just in case.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Well, something has happened at the lighthouse, and she", "has been locked out. I need you to give me her key.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Sure, give Larrissa my regards.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendGiveItemNpc("Gunnjorn hands you a key.", new Item(LIGHTHOUSE_KEY));
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(LIGHTHOUSE_KEY));
								player.setQuestStage(26, KEY_TO_LARRISSA);
								return true;
						}
						return false;
				}
			case LARRISSA:
				switch (player.getQuestStage(26)) {
					case 3:
					case 4:
					case 5:
					case 6:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Oh, I'm so worried about Jossik... Find him", "quickly please!!", DISTRESSED);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 7:
					case 8:
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Oh, thank you so much! Jossik is safe!", "Now if only I could get him to leave that cave...", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Where is Gunnjorn again?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("I'm not sure, but he was always a fan of Agility.", "He left Rellekka to pursue his love of it.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case KEY_TO_LARRISSA:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("I found Gunnjorn and the key.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Fantastic! Go unlock the lighthouse and search for clues!", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Hello there.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Oh, thank Guthix! I am in such a worry... please", "help me!", DISTRESSED);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("With what?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Oh... it is terrible... horrible... My boyfriend lives here", "in this lighthouse, but I haven't seen him the last few", "days! I think something terrible has happened!", DISTRESSED);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("He is nowhere to be found and the front door is locked", "up tight! He would NEVER do that!", DISTRESSED);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("Maybe he just went on vacation or something?", "Must be pretty boring living in a lighthouse.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("That is terribly irresponsible! He is far too thoughtful", "for that! He would never leave it unattended! He would", "also never leave without telling me!", DISTRESSED);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("Please, I know something terrible has happened to him", "I can sense it! Please... please help me adventurer!", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("But how can I help?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Well, we have to get inside to see where he has gone!", "If you could go and visit my cousin and get the", "spare key I left him, I will be eternally grateful!", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendOption("Okay, I'll help!", "Sorry, I'm just passing through, I can't help you.");
								return true;
							case 12:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Okay, I'll help!", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Sorry, I'm just passing through, I can't help you.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 13:
								player.getDialogue().sendNpcChat("OH! THANK YOU SO MUCH! I know my darling", "would never have left without telling me where he's gone!", HAPPY);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Where is your cousin at?", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("My cousin was always interested in agility. He left our", "home in Rellekka many moons ago, so that he could", "pursue his interest.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("I don't know exactly where he has gone, but I am sure", "he went somewhere to practice his agility. If you see", "him, his name is Gunnjorn. Mention my name, he will", "recognize it.", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendPlayerChat("I'll see what I can do to find him.", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendNpcChat("Thank you so much!", HAPPY);
								player.getDialogue().endDialogue();
								QuestHandler.startQuest(player, 26);
								return true;
						}
						return false;
				}
				return false;
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
