package com.rs2.model.content.quests;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_1;
import static com.rs2.model.content.dialogue.Dialogues.ANGRY_2;
import static com.rs2.model.content.dialogue.Dialogues.CONTENT;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.SAD;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.objects.GameObject;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class HeroesQuest implements Quest {

	//Quest stages

	public static final int QUEST_STARTED = 1;
	public static final int CANDLESTICKS_GOTTEN = 2;
	public static final int ARMBAND_GOTTEN = 3;
	public static final int QUEST_COMPLETE = 9;
	//Items
	public static final int HAMMER = 2347;
	public static final int CANDLESTICK = 1577;
	public static final int ARMBAND = 1579;
	public static final int ICE_GLOVES = 1580;
	public static final int BLAMISH_SNAIL_SLIME = 1581;
	public static final int BLAMISH_OIL = 1582;
	public static final int FIREBIRD_FEATHER = 1583;
	public static final int ID_PAPERS = 1584;
	public static final int OILY_FISHING_ROD = 1585;
	public static final int MISC_KEY = 1586;
	public static final int CANDLESTICKS_KEY = 9722;
	public static final int DUSTY_KEY = 1590;
	public static final int JAIL_KEY = 1591;
	public static final int RAW_LAVA_EEL = 2148;
	public static final int LAVA_EEL = 2149;
	public static final int SAMPLE_BOTTLE = 3377;
	public static final int TINDERBOX = 590;
	public static final int HARRALANDER_POTION = 97;
	public static final int FISHING_ROD = 307;
	//Positions
	public static final Position UP_AT_LIGHTHOUSE = new Position(2510, 3644, 0);
	//Interfaces
	public static final int DOOR_INTERFACE = 10116;
	//Npcs
	public static final int ACHIETTIES = 796;
	public static final int ICE_QUEEN = 795;
	public static final int CHARLIE_THE_COOK = 794;
	public static final int ALFONSE_THE_WAITER = 793;
	public static final int GRIP = 792;
	public static final int SETH = 791;
	public static final int TROBERT = 790;
	public static final int GARV = 788;
	public static final int GRUBOR = 789;
	public static final int GRUBOR_DOOR = 78999;
	public static final int ENTRANA_FIREBIRD = 6108;
	public static final int GERRANT = 558;
	public static final int MASTER_FISHER = 308;
	public static final int ROACHEY = 592;
	public static final int HARRY = 576;
	public static final int KATRINE = ShieldOfArrav.KATRINE;
	public static final int STRAVEN = ShieldOfArrav.STRAVEN;
	//Objects
	public static final int GUILD_DOOR_2 = 2624;
	public static final int GUILD_DOOR_1 = 2625;
	public static final int ROCKSLIDE = 2634;

	public int dialogueStage = 0;

	private int reward[][] = {};
	private int expReward[][] = {
		{Skill.ATTACK, 2825},
		{Skill.DEFENCE, 3075},
		{Skill.STRENGTH, 3025},
		{Skill.HITPOINTS, 2775},
		{Skill.RANGED, 1525},
		{Skill.COOKING, 2725},
		{Skill.WOODCUTTING, 1875},
		{Skill.FIREMAKING, 2725},
		{Skill.SMITHING, 2225},
		{Skill.MINING, 2575},
		{Skill.HERBLORE, 1825}
	};

	private static final int questPointReward = 1;

	public int getQuestID() {
		return 27;
	}

	public String getQuestName() {
		return "Heroes Quest";
	}

	public String getQuestSaveName() {
		return "heroes";
	}

	public boolean canDoQuest(Player player) {
		return player.getQuestStage(13) >= ShieldOfArrav.QUEST_COMPLETE && player.getQuestStage(14) >= LostCity.QUEST_COMPLETE && player.getQuestStage(15) >= DragonSlayer.QUEST_COMPLETE && player.getQuestStage(11) >= MerlinsCrystal.QUEST_COMPLETE && player.getQuestPoints() >= 50;
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
		player.getActionSender().sendString("You have completed the " + getQuestName() + "!", 12144);
		player.getActionSender().sendString("You are rewarded: ", 12146);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("Access to the Heroes' guild", 12151);
		player.getActionSender().sendString("A total of 65,772 XP spread", 12152);
		player.getActionSender().sendString("over twelve skills.", 12153);
		player.getActionSender().sendString("Quest points: " + player.getQuestPoints(), 12146);
		player.getActionSender().sendString(" ", 12147);
		player.setQuestStage(getQuestID(), QUEST_COMPLETE);
		player.getActionSender().sendString("@gre@" + getQuestName(), 7363);
	}

	public void sendQuestRequirements(Player player) {
		player.getActionSender().sendString(getQuestName(), 8144);
		int questStage = player.getQuestStage(getQuestID());
		switch (questStage) {
			case QUEST_STARTED:
			case CANDLESTICKS_GOTTEN:
			case ARMBAND_GOTTEN:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@" + "Talk to Achietties, outside the Heroes Guild to begin.", 8147);

				player.getActionSender().sendString("Achietties will let me into the Heroes' Guild if I can get:", 8149);
				if (player.getInventory().ownsItem(FIREBIRD_FEATHER)) {
					player.getActionSender().sendString("@str@-An Entranan Firebird feather.", 8150);
				} else {
					player.getActionSender().sendString("-An Entranan Firebird feather(I should check on Entrana.)", 8150);
				}
				if (player.getInventory().ownsItem(LAVA_EEL)) {
					player.getActionSender().sendString("@str@-A cooked lava eel.", 8151);
				} else {
					player.getActionSender().sendString("-A cooked lava eel(I should talk to a fishing expert.)", 8151);
				}
				if (player.getInventory().ownsItem(ARMBAND)) {
					player.getActionSender().sendString("@str@-A Master Thieves Armband.", 8152);
				} else {
					player.getActionSender().sendString("-Master Thieves band(I should talk to my gang's leader.)", 8152);
				}
				break;
			case QUEST_COMPLETE:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("@str@" + "Talk to Achietties, outside the Heroes Guild to begin.", 8147);
				player.getActionSender().sendString("Achietties will let me into the Heroes' Guild if I can get:", 8149);
				player.getActionSender().sendString("@str@-An Entranan Firebird feather.", 8150);
				player.getActionSender().sendString("@str@-A cooked lava eel.", 8151);
				player.getActionSender().sendString("@str@-A Master Thieves Armband.", 8152);
				player.getActionSender().sendString("@red@" + "You have completed this quest!", 8154);
				break;
			default:
				player.getActionSender().sendString(getQuestName(), 8144);
				player.getActionSender().sendString("Talk to @dre@Achietties @bla@outside the @dre@Heroes Guild @bla@, north", 8147);
				player.getActionSender().sendString("of Taverly to begin this quest.", 8148);
				player.getActionSender().sendString("@dre@Requirements:", 8149);
				if (player.getQuestPoints() < 50) {
					player.getActionSender().sendString("-50 Quest Points.", 8150);
				} else {
					player.getActionSender().sendString("@str@-50 Quest Points.", 8150);
				}
				if (player.getSkill().getLevel()[Skill.COOKING] < 53) {
					player.getActionSender().sendString("-53 Cooking.", 8151);
				} else {
					player.getActionSender().sendString("@str@-53 Cooking.", 8151);
				}
				if (player.getSkill().getLevel()[Skill.FISHING] < 53) {
					player.getActionSender().sendString("-53 Fishing.", 8152);
				} else {
					player.getActionSender().sendString("@str@-53 Fishing.", 8152);
				}
				if (player.getSkill().getLevel()[Skill.HERBLORE] < 25) {
					player.getActionSender().sendString("-25 Herblore.", 8153);
				} else {
					player.getActionSender().sendString("@str@-25 Herblore.", 8153);
				}
				if (player.getSkill().getLevel()[Skill.MINING] < 50) {
					player.getActionSender().sendString("-50 Mining.", 8154);
				} else {
					player.getActionSender().sendString("@str@-50 Mining.", 8154);
				}
				if (player.getQuestStage(13) < ShieldOfArrav.QUEST_COMPLETE) {
					player.getActionSender().sendString("-Completion of Shield of Arrav.", 8155);
				} else {
					player.getActionSender().sendString("@str@-Completion of Shield of Arrav.", 8155);
				}
				if (player.getQuestStage(14) < LostCity.QUEST_COMPLETE) {
					player.getActionSender().sendString("-Completion of Lost City.", 8156);
				} else {
					player.getActionSender().sendString("@str@-Completion of Lost City.", 8156);
				}
				if (player.getQuestStage(15) < DragonSlayer.QUEST_COMPLETE) {
					player.getActionSender().sendString("-Completion of Dragon Slayer.", 8157);
				} else {
					player.getActionSender().sendString("@str@-Completion of Dragon Slayer.", 8157);
				}
				if (player.getQuestStage(11) < MerlinsCrystal.QUEST_COMPLETE) {
					player.getActionSender().sendString("-Completion of Merlin's Crystal.", 8157);
				} else {
					player.getActionSender().sendString("@str@-Completion of Merlin's Crystal.", 8157);
				}
				player.getActionSender().sendString("@dre@-I must be able to defeat a strong level 111 enemy.", 8158);
				break;
		}
	}

	public void sendQuestInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
	}

	public void startQuest(Player player) {
		player.setQuestStage(getQuestID(), QUEST_STARTED);
		player.getActionSender().sendString("@yel@" + getQuestName(), 7363);
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
			player.getActionSender().sendString("@yel@" + getQuestName(), 7363);
		} else if (questStage == QUEST_COMPLETE) {
			player.getActionSender().sendString("@gre@" + getQuestName(), 7363);
		} else {
			player.getActionSender().sendString("@red@" + getQuestName(), 7363);
		}

	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {
		player.getActionSender().sendInterface(QuestHandler.QUEST_INTERFACE);
		player.getActionSender().sendString(getQuestName(), 8144);
		player.getActionSender().sendString("Talk to @dre@Achietties @bla@outside the @dre@Heroes Guild @bla@, north", 8147);
		player.getActionSender().sendString("of Taverly to begin this quest.", 8148);
		player.getActionSender().sendString("@dre@Requirements:", 8149);
		if (player.getQuestPoints() < 50) {
			player.getActionSender().sendString("-50 Quest Points.", 8150);
		} else {
			player.getActionSender().sendString("@str@-50 Quest Points.", 8150);
		}
		if (player.getSkill().getLevel()[Skill.COOKING] < 53) {
			player.getActionSender().sendString("-53 Cooking.", 8151);
		} else {
			player.getActionSender().sendString("@str@-53 Cooking.", 8151);
		}
		if (player.getSkill().getLevel()[Skill.FISHING] < 53) {
			player.getActionSender().sendString("-53 Fishing.", 8152);
		} else {
			player.getActionSender().sendString("@str@-53 Fishing.", 8152);
		}
		if (player.getSkill().getLevel()[Skill.HERBLORE] < 25) {
			player.getActionSender().sendString("-25 Herblore.", 8153);
		} else {
			player.getActionSender().sendString("@str@-25 Herblore.", 8153);
		}
		if (player.getSkill().getLevel()[Skill.MINING] < 50) {
			player.getActionSender().sendString("-50 Mining.", 8154);
		} else {
			player.getActionSender().sendString("@str@-50 Mining.", 8154);
		}
		if (player.getQuestStage(13) < ShieldOfArrav.QUEST_COMPLETE) {
			player.getActionSender().sendString("-Completion of Shield of Arrav.", 8155);
		} else {
			player.getActionSender().sendString("@str@-Completion of Shield of Arrav.", 8155);
		}
		if (player.getQuestStage(14) < LostCity.QUEST_COMPLETE) {
			player.getActionSender().sendString("-Completion of Lost City.", 8156);
		} else {
			player.getActionSender().sendString("@str@-Completion of Lost City.", 8156);
		}
		if (player.getQuestStage(15) < DragonSlayer.QUEST_COMPLETE) {
			player.getActionSender().sendString("-Completion of Dragon Slayer.", 8157);
		} else {
			player.getActionSender().sendString("@str@-Completion of Dragon Slayer.", 8157);
		}
		if (player.getQuestStage(11) < MerlinsCrystal.QUEST_COMPLETE) {
			player.getActionSender().sendString("-Completion of Merlin's Crystal.", 8157);
		} else {
			player.getActionSender().sendString("@str@-Completion of Merlin's Crystal.", 8157);
		}
		player.getActionSender().sendString("@dre@-I must be able to defeat a strong level 111 enemy.", 8158);
	}

	public boolean itemHandling(final Player player, int itemId) {
		switch (itemId) {

		}
		return false;
	}

	public static boolean hasAllItems(final Player player) {
		return player.getInventory().playerHasItem(ARMBAND) && player.getInventory().playerHasItem(FIREBIRD_FEATHER) && player.getInventory().playerHasItem(LAVA_EEL);
	}

	public static boolean blackKnightsGear(final Player player) {
		return player.getEquipment().getId(Constants.HAT) == 1165 && player.getEquipment().getId(Constants.CHEST) == 1125 && player.getEquipment().getId(Constants.LEGS) == 1077;
	}

	public static void handleShootGrip(final Player player, final Npc npc) {
		if (player.getQuestVars().isPhoenixGang()) {
			if (player.getPosition().equals(new Position(2780, 3198, 0)) && Misc.goodDistance(player.getPosition(), npc.getPosition(), 5) && npc.getPosition().getY() > 3195) {
				int attackerX = player.getPosition().getX(), attackerY = player.getPosition().getY();
				int victimX = npc.getPosition().getX(), victimY = npc.getPosition().getY();
				final int offsetX = (attackerY - victimY) * -1;
				final int offsetY = (attackerX - victimX) * -1;
				RangedAmmo ammo = RangedAmmo.getArrowForEquipped(player.getEquipment().getId(Constants.ARROWS));
				Item equipped = new Item(player.getEquipment().getId(Constants.WEAPON));
				if (ammo == null && !equipped.getDefinition().getName().toLowerCase().contains("crystal bow")) {
					player.getActionSender().sendMessage("You must use one of the standard arrows / bolts to hit Grip!");
					return;
				} else {
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					player.getUpdateFlags().sendAnimation(426);
					if (equipped.getDefinition().getName().toLowerCase().contains("crystal bow")) {
						World.sendProjectile(player.getPosition(), offsetX, offsetY, RangedAmmo.CRYSTAL_ARROW.getProjectileId(), 43, 40, 70, npc.getIndex(), false);
					} else {
						World.sendProjectile(player.getPosition(), offsetX, offsetY, ammo.getProjectileId(), 43, 40, 70, npc.getIndex(), false);
					}
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							npc.getUpdateFlags().setForceChatMessage("Urgggh...");
							npc.hit(npc.getCurrentHp(), HitType.NORMAL);
							if (player.getQuestStage(27) < ARMBAND_GOTTEN) {
								player.getQuestVars().setShotGrip(true);
							}
							b.stop();
						}

						@Override
						public void stop() {
							player.setStopPacket(false);
							for (Player players : World.getPlayers()) {
								if (players == null) {
									continue;
								}
								if (Misc.goodDistance(players.getPosition(), npc.getPosition(), 10) && players.getQuestVars().isBlackArmGang() && players.getQuestStage(27) >= 1) {
									players.getUpdateFlags().faceEntity(npc.getFaceIndex());
									players.getActionSender().sendMessage("You find a key on Grip's corpse.");
									players.getInventory().addItemOrDrop(new Item(CANDLESTICKS_KEY));
								}
							}
						}
					}, 3);
					return;
				}
			} else {
				player.walkTo(new Position(2780, 3198, 0), true);
				return;
			}
		} else if (Misc.goodDistance(player.getPosition().clone(), npc.getPosition().clone(), 2)) {
			player.getDialogue().setLastNpcTalk(npc.getNpcId());
			player.getDialogue().sendNpcChat("What are you doing so close to me?", Dialogues.CONTENT);
			player.getDialogue().endDialogue();
			return;
		}
	}

	public static void handleGripDeath(final Player player, final Npc died) {
		/*
		 if(died.getNpcId() == GRIP) {
		 for(Player players : World.getPlayers()) {
		 if(players == null) continue;
		 if(Misc.goodDistance(players.getPosition(), died.getPosition(), 10) && players.getQuestVars().isBlackArmGang() && (players.getQuestStage(27) == 1 || players.getQuestStage(27) == 2)) {
		 players.walkTo(died.getPosition().clone(), true);
		 players.getActionSender().sendMessage("You find a key on Grip's corpse.");
		 players.getInventory().addItemOrDrop(new Item(CANDLESTICKS_KEY));
		 }
		 }
		 }*/
	}

	public static boolean itemPickupHandling(Player player, int itemId) {
		if (itemId == FIREBIRD_FEATHER) {
			if ((player.getQuestStage(27) == QUEST_STARTED || player.getQuestStage(27) == CANDLESTICKS_GOTTEN || player.getQuestStage(27) == ARMBAND_GOTTEN) && !player.getInventory().ownsItem(FIREBIRD_FEATHER)) {
				if (player.getEquipment().getId(Constants.HANDS) != ICE_GLOVES) {
					player.getDialogue().sendPlayerChat("I'd better not touch this, it appears extremely hot.", "Perhaps there is a way to grab it...", CONTENT);
					return true;
				} else {
					player.getActionSender().sendMessage("You pickup the feather carefully with your ice gloves.");
					return false;
				}
			} else {
				player.getDialogue().sendPlayerChat("I think I'll burn my hands getting that!", "I have no need for it either...", CONTENT);
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		if ((firstItem == HARRALANDER_POTION && secondItem == BLAMISH_SNAIL_SLIME) || (firstItem == BLAMISH_SNAIL_SLIME && secondItem == HARRALANDER_POTION)) {
			if (player.getSkill().getLevel()[Skill.HERBLORE] < 25) {
				player.getDialogue().sendStatement("You need a Herblore level of 25 to make this potion.");
				return true;
			} else {
				player.getInventory().replaceItemWithItem(new Item(BLAMISH_SNAIL_SLIME), new Item(SAMPLE_BOTTLE));
				player.getInventory().replaceItemWithItem(new Item(HARRALANDER_POTION), new Item(BLAMISH_OIL));
				player.getActionSender().sendMessage("You carefully mix the blamish slime and harralander solution.");
				return true;
			}
		} else if ((firstItem == BLAMISH_OIL && secondItem == FISHING_ROD) || (firstItem == FISHING_ROD && secondItem == BLAMISH_OIL)) {
			player.getInventory().replaceItemWithItem(new Item(BLAMISH_OIL), new Item(229)); //vial
			player.getInventory().replaceItemWithItem(new Item(FISHING_ROD), new Item(OILY_FISHING_ROD));
			player.getActionSender().sendMessage("You lubricate the rod with oil.");
			return true;
		}
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		switch (object) {

		}
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		switch (object) {
			case 2635: //I SAID DONT TOUCH THE FUCKING CUPBOARDS
				for (Npc npc : World.getNpcs()) {
					if (npc == null) {
						continue;
					}
					if (npc.getNpcId() == GRIP) {
						npc.getUpdateFlags().setForceChatMessage("Hey! Get away from there!");
						if (!Misc.checkClip(npc.getPosition().clone(), new Position(2777, 3197, 0), true)) {
							npc.walkTo(new Position(2777, 3194, 0), true);
						} else {
							npc.walkTo(new Position(2777, 3197, 0), true);
						}
						return true;
					}
				}
			case 2627: //mansion door
				if (!player.spokeToGarv() && player.getPosition().getY() < 3188) {
					Dialogues.startDialogue(player, GARV);
					return true;
				} else {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3188 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				}
			case 2632: //candlesticks chest
				if (player.getQuestStage(27) == QUEST_STARTED && player.getInventory().canAddItem(new Item(CANDLESTICK, 2))) {
					player.getDialogue().sendStatement("You find two candlesticks in the chest. So that will be one for you,", "and one for the person who killed Grip for you.");
					player.getInventory().addItem(new Item(CANDLESTICK, 2));
					player.setQuestStage(27, CANDLESTICKS_GOTTEN);
					return true;
				} else if (player.getQuestStage(27) == QUEST_STARTED && !player.getInventory().canAddItem(new Item(CANDLESTICK, 2))) {
					player.getDialogue().sendStatement("You find two candlesticks, but you don't have room for them.");
					return true;
				} else if (player.getQuestStage(27) >= QUEST_COMPLETE && !player.getInventory().ownsItem(CANDLESTICK)) {
					player.getDialogue().sendStatement("You find a candlestick in the chest. Must be to assist", "the person who just killed Grip.");
					player.getInventory().addItemOrDrop(new Item(CANDLESTICK));
					return true;
				} else {
					return false;
				}
			case 2626: //locked grubor door
				if (!player.spokeToGrubor()) {
					Dialogues.startDialogue(player, GRUBOR_DOOR);
					return true;
				} else {
					player.getActionSender().walkTo(player.getPosition().getX() < 2811 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				}
			case 2621: //locked door to candlesticks
				if (!player.getInventory().playerHasItem(CANDLESTICKS_KEY) && player.getPosition().getX() < 2764) {
					player.getActionSender().sendMessage("This door is locked.");
					return true;
				} else if (player.getInventory().playerHasItem(CANDLESTICKS_KEY) && player.getPosition().getX() < 2764) {
					player.getActionSender().sendMessage("You open the door with Grip's key.");
					player.getInventory().removeItem(new Item(CANDLESTICKS_KEY));
					player.getActionSender().walkTo(player.getPosition().getX() < 2764 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				} else {
					player.getActionSender().walkTo(player.getPosition().getX() < 2764 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				}
			case 2622: //locked door to range room
				if (!player.getInventory().playerHasItem(MISC_KEY)) {
					player.getActionSender().sendMessage("This door is locked.");
					return true;
				} else {
					player.getActionSender().sendMessage("You open the door with your key.");
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3197 ? 1 : -1, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				}
			case 2628: //chef's door
				player.getActionSender().walkTo(0, player.getPosition().getY() < 3190 ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(object, x, y, 0);
				return true;
			case 2629: //chef's "wall"
				if (player.spokeToCharlie() || (player.getQuestStage(27) >= QUEST_COMPLETE && player.getQuestVars().isPhoenixGang())) {
					player.getActionSender().walkTo(player.getPosition().getX() < 2787 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(object, x, y, 0);
					return true;
				} else {
					player.getDialogue().sendNpcChat("Get away from that wall!!", ANGRY_2);
					return true;
				}
			case ROCKSLIDE:
				player.getActionSender().sendMessage("You examine the rock for ores...");
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						player.getActionSender().sendMessage("...it appears to be a rockslide, devoid of ore. Strange.");
						b.stop();
					}

					@Override
					public void stop() {
						player.setStopPacket(false);
					}
				}, 4);
				return true;
			case GUILD_DOOR_1:
			case GUILD_DOOR_2:
				if (player.getQuestStage(27) < QUEST_COMPLETE) {
					player.getActionSender().sendMessage("You are not a part of the Heroes' Guild.");
					return true;
				} else {
					player.getActionSender().walkTo(player.getPosition().getX() > 2901 ? -1 : 1, 0, true);
					player.getActionSender().walkThroughDoubleDoor(GUILD_DOOR_2, GUILD_DOOR_1, 2902, 3510, 2902, 3511, 0);
					return true;
				}
		}
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {
			case ROCKSLIDE:
				final CacheObject g = ObjectLoader.object(x, y, player.getPosition().getZ());
				boolean canDoIt = true;
				if (Tools.getTool(player, Skill.MINING) == null) {
					player.getActionSender().sendMessage("You need a pickaxe to mine here.");
					canDoIt = false;
				}
				if (player.getSkill().getLevel()[Skill.MINING] < 50) {
					player.getDialogue().sendStatement("You need atleast 50 Mining to clear this.");
					canDoIt = false;
				}
				if (!canDoIt) {
					break;
				}
				player.getActionSender().sendMessage("You attempt to mine your way through...");
				player.getUpdateFlags().sendAnimation(Tools.getTool(player, Skill.MINING).getAnimation());
				player.setStopPacket(true);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						if (Misc.random(1) == 0) {
							player.getActionSender().sendMessage("...and manage to break through the rock.");
							new GameObject(Constants.EMPTY_OBJECT, x, y, player.getPosition().getZ(), g.getRotation(), g.getType(), ROCKSLIDE, 10);
							player.getActionSender().walkTo(player.getPosition().getX() > 2837 ? -3 : 3, 0, true);
							b.stop();

						} else {
							player.getActionSender().sendMessage("...but fail to break-up the rock.");
							b.stop();
						}
					}

					@Override
					public void stop() {
						player.getUpdateFlags().sendAnimation(-1);
						player.setStopPacket(false);
					}
				}, 3);
		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		switch (id) {
			case GERRANT:
			case ROACHEY:
			case MASTER_FISHER:
			case HARRY:
				switch (player.getQuestStage(27)) {
					case QUEST_STARTED:
					case CANDLESTICKS_GOTTEN:
					case ARMBAND_GOTTEN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().ownsItem(BLAMISH_SNAIL_SLIME) && !player.getInventory().ownsItem(OILY_FISHING_ROD) && !player.getInventory().ownsItem(BLAMISH_OIL) && player.getQuestVars().givenSnailSlime()) {
									player.getDialogue().sendPlayerChat("I lost my snail slime...", CONTENT);
									player.getDialogue().setNextChatId(10);
									return true;
								} else if (!player.getQuestVars().givenSnailSlime()) {
									player.getDialogue().sendPlayerChat("I want to find out how to catch a lava eel.", CONTENT);
									return true;
								} else {
									return false;
								}
							case 2:
								player.getDialogue().sendNpcChat("Lava eels eh? That's a ticky one that is, you'll need a", "lava-proof fishing line. The method for making this would", "be to take an ordinary fishing rod, and then cover it", "with the fire-proof Blamish Oil.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("You know... thinking about it... I may have a jar of", "Blamish Slime around here somewhere... Now where did", "I put it...?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendStatement("....");
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Aha! here it is! Take the slime, mix it with some", "Harralander and water and you'll have the Blamish Oil", "you need for treating your fishing rod.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendGiveItemNpc("You get handed a strange slimy bottle.", new Item(BLAMISH_SNAIL_SLIME));
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(BLAMISH_SNAIL_SLIME));
								return true;
							case 10:
								player.getDialogue().sendNpcChat("I'm afraid that's all I had... You can", "try getting a bottle from Canifis and using it directly", "on a blamish snail in Mort Myre.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case CHARLIE_THE_COOK:
				switch (player.getQuestStage(27)) {
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hey! What the hell are you doing back here?!", ANGRY_1);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Just exploring...", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Hey! What the hell are you doing back here?!", ANGRY_1);
								return true;
							case 2:
								if (player.spokeToAlfonse() && player.getQuestVars().isPhoenixGang() && !player.getQuestVars().isBlackArmGang()) {
									player.getDialogue().sendPlayerChat("I'm looking for a gherkin...", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Just exploring...", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("Aaaaah... a fellow Phoenix! So, tell me compadre... what", "brings you to sunny Brimhaven?", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("I want to steal Scarface Pete's candlesticks.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Ah yes, of course. The candlesticks. Well, I have to be", "honest with you compadre, we haven't made much", "progress in that task ourselves so far. We can however", "offer", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("a little assistance. The setting up of this restaurant was", "the start of things; we have a secret door out the back", "out here that leads through the back of Mr Olbors'", "garden.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Now, at the other side of Mr Olbors' garden, is an old", "side entrance to Scarface Pete's manion. It seems to", "have been blocked off from the rest of the mansion", "some years ago.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("and we can't seem to find a way through. We're positive", "this is the key to entering the house undetected", "however, and I promise to let you know if we find", "anything there.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Mind if I check it out for myself?", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Not at all! The more minds we have working on the", "problem, the quicker we get that loot!", CONTENT);
								player.getDialogue().endDialogue();
								player.setSpokeToCharlie(true);
								return true;
						}
						return false;
				}
			case GRUBOR_DOOR:
				switch (player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().setLastNpcTalk(GRUBOR);
						player.getDialogue().sendNpcChat("Yes? What do you want?", CONTENT);
						return true;
					case 2:
						player.getDialogue().sendOption("Rabbit's foot.", "Four leaved clover.", "Lucky horseshoe.", "Black cat.");
						return true;
					case 3:
						switch (optionId) {
							case 1:
							case 3:
							case 4:
								player.getDialogue().setLastNpcTalk(GRUBOR);
								player.getDialogue().sendNpcChat("Please go away.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Four leaved clover.", CONTENT);
								return true;
						}
					case 4:
						player.getDialogue().setLastNpcTalk(GRUBOR);
						player.getDialogue().sendNpcChat("Oh, you're one of the gang are you? Ok, hold up a", "second, I'll just let you in through here.", CONTENT);
						return true;
					case 5:
						player.getDialogue().sendStatement("You hear the door being unbarred from the inside.");
						player.getDialogue().endDialogue();
						player.setSpokeToGrubor(true);
						return true;
				}
				return false;
			case GRIP:
				switch (player.getQuestStage(27)) {
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getQuestVars().isBlackArmGang()) {
									player.getDialogue().sendPlayerChat("Hi there. I am Hartigen. Reporting for duty as your", "new deputy sir.", CONTENT);
									return true;
								} else {
									return false;
								}
							case 2:
								player.getDialogue().sendNpcChat("Ah good, at last. You took your time getting here! Now", "let me see...", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("I'll get your hours and duty roster sorted out in a", "while. Oh, and do you have your ID papers with you?", "Internal security is almost as important as external", "security for a guard.", CONTENT);
								return true;
							case 4:
								if (player.getInventory().playerHasItem(ID_PAPERS)) {
									player.getDialogue().sendPlayerChat("Right here sir!", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("I, err... hold on...", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 5:
								player.getDialogue().sendGiveItemNpc("You show Grip your ID papers.", new Item(ID_PAPERS));
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("So, what do my duties invlove?", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("You'll be assigned specific duties as they are required", "and when they become necessary. Just so you know, if", "anything happens to me...", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("...you'll need to take over as head guard here. You'll find", "an important key to the treasure room inside my jacket.", "Although, I doubt anything bad will happen to me.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendPlayerChat("Well, anything I can do now?", CONTENT);
								return true;
							case 10:
								if (!player.getInventory().ownsItem(MISC_KEY)) {
									player.getDialogue().sendNpcChat("You can try and figure out where this key goes.", "I can't figure it out for the life of me.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Nothing I can think of right away.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 11:
								player.getDialogue().sendGiveItemNpc("Grip hands you a key.", new Item(MISC_KEY));
								player.getInventory().addItemOrDrop(new Item(MISC_KEY));
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("Anything else I should know about?", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Yes, stay away from the cupboards please. They contain", "important documents on Scarface Pete's business and need not", "be disorganized.", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("Alright, thanks Grip. I'll go patrol the grounds or something.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendPlayerChat("Grip, I lost the key you gave me...", SAD);
								return true;
							case 2:
								player.getDialogue().sendGiveItemNpc("Grip hands you a key.", new Item(MISC_KEY));
								player.getInventory().addItemOrDrop(new Item(MISC_KEY));
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Try not to lose it this time. Remember to", "stay away from the cupboards as well.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case GARV:
				switch (player.getQuestStage(27)) {
					default:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("What do you want?", CONTENT);
								return true;
							case 2:
								if (player.getQuestVars().givenIdPapers() && player.getInventory().playerHasItem(ID_PAPERS)) {
									player.getDialogue().sendPlayerChat("Hi. I'm Hartigen. I've come to work here.", CONTENT);
									player.getDialogue().setNextChatId(5);
									return true;
								} else if (player.getQuestStage(27) == QUEST_COMPLETE) {
									player.getDialogue().sendPlayerChat("It's me, Hartigen.", CONTENT);
									player.getDialogue().setNextChatId(10);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Can I go in there?", CONTENT);
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("No, 'in there' is private.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 5:
								if (blackKnightsGear(player)) {
									player.getDialogue().sendNpcChat("I assume you have your ID papers then?", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Hartigen the Black Knight? I don't think so. He doesn't", "dress like that.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 6:
								player.getDialogue().sendGiveItemNpc("You show Garv your ID papers.", new Item(ID_PAPERS));
								return true;
							case 7:
								player.getDialogue().sendNpcChat("You'd better come in then. Grip will want to talk to", "you.", CONTENT);
								player.getDialogue().endDialogue();
								player.setSpokeToGarv(true);
								return true;
							case 10:
								if (blackKnightsGear(player)) {
									player.getDialogue().sendNpcChat("Ah, of course, welcome back.", CONTENT);
									player.getDialogue().endDialogue();
									player.setSpokeToGarv(true);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Ah, of course, I didn't recognize you without", "your Black Knight uniform on. You need to be in uniform", "to be able to enter, sir. It's policy to avoid infiltration.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
						}
						return false;
				}
			case TROBERT:
				switch (player.getQuestStage(27)) {
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.spokeToGrubor() && player.getQuestVars().isBlackArmGang() && !player.getQuestVars().givenIdPapers()) {
									player.getDialogue().sendNpcChat("Hi. Welcome to our Brimhaven headquarters. I'm", "Trobert and I'm in charge here.", CONTENT);
									return true;
								} else if (player.getQuestVars().givenIdPapers() && player.spokeToGrubor() && player.getQuestVars().isBlackArmGang()) {
									player.getDialogue().sendNpcChat("Godspeed on those candlesticks.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								} else if (player.getQuestVars().givenIdPapers() && !player.getInventory().playerHasItem(ID_PAPERS) && player.spokeToGrubor() && player.getQuestVars().isBlackArmGang()) {
									player.getDialogue().sendPlayerChat("I seem to have, erm, lost the ID papers.", SAD);
									player.getDialogue().setNextChatId(10);
									return true;
								} else {
									player.getDialogue().sendNpcChat("WHAT THE FUCK?! HOW DID YOU GET IN HERE??", ANGRY_2);
									player.getDialogue().setNextChatId(15);
									return true;
								}
							case 2:
								player.getDialogue().sendPlayerChat("So, you can help me get Scarface Pete's candlesticks?", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Well, we have made some progress there. We know that", "one of the only keys to Pete's treasure room is carried", "by Grip, the head guard, so we thought it might be good", "to get close to him somehow.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Grip was taking on a new deputy called Hartigen, an", "Asgarnian Black Knight, who was deserting the Black", "Knight Fortress and seeking new employment here on", "Brimhaven.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("We managed to waylay him on the journey here, and", "steal his ID papers. Now all we need is to find", "somebody willing to impersonate him and take the", "deputy role to get that key for us.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendPlayerChat("I volunteer to undertake that mission.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("Good good. Well, Here's the ID papers, take them and", "introduce yourself to the guards at Scarface Pete's", "mansion, we'll have that treasure in no time.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendGiveItemNpc("Trobert hands you some ID papers.", new Item(ID_PAPERS));
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(ID_PAPERS));
								player.getQuestVars().setGivenIdPapers(true);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Luckily for you, we made copies.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendGiveItemNpc("Trobert hands you some ID papers.", new Item(ID_PAPERS));
								player.getDialogue().endDialogue();
								player.getInventory().addItemOrDrop(new Item(ID_PAPERS));
								return true;
							case 15:
								player.fadeTeleport(new Position(2810, 3170, 0));
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case GRUBOR:
				switch (player.getQuestStage(27)) {
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.spokeToGrubor()) {
									player.getDialogue().sendNpcChat("Talk to Trobert, he's the boss man.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("WHAT THE FUCK?! HOW DID YOU GET IN HERE??", ANGRY_2);
									player.getDialogue().setNextChatId(15);
									return true;
								}
							case 15:
								player.fadeTeleport(new Position(2810, 3170, 0));
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
				}
				return false;
			case ALFONSE_THE_WAITER:
				switch (player.getQuestStage(27)) {
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Welcome to the Shrimp and Parrot.", "Would you like to order, sir?", CONTENT);
								return true;
							case 2:
								if (player.getQuestVars().isPhoenixGang() && !player.getQuestVars().isBlackArmGang()) {
									player.getDialogue().sendPlayerChat("Do you sell Gherkins?", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("No, thank you.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 3:
								player.getDialogue().sendNpcChat("Hmmmm, Gherkins eh? Ask Charlie the cook, round the", "back. He may have some 'gherkins' for you!", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendStatement("Alfonse winks at you.");
								player.getDialogue().endDialogue();
								player.setSpokeToAlfonse(true);
								return true;
						}
						return false;
				}
				return false;
			case KATRINE:
				switch (player.getQuestStage(13)) {
					case 12:
						if (!player.getQuestVars().isPhoenixGang() && !player.getQuestVars().isBlackArmGang()) {
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendPlayerChat("Hello, Katrine.", CONTENT);
									player.getDialogue().setNextChatId(25);
									return true;
								case 25:
									player.getDialogue().sendNpcChat("Hmm, your face looks familiar. Who are you again?", CONTENT);
									return true;
								case 26:
									player.getDialogue().sendOption("I'm a loyal member of your Black Arm Gang.", "A nobody, I'm just poking around.");
									return true;
								case 27:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("I'm a loyal member of your Black Arm Gang.", CONTENT);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("A nobody, I'm just poking around. See you later.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
									}
								case 28:
									player.getDialogue().sendNpcChat("Ah, yes, that's right. Welcome back.", CONTENT);
									return true;
								case 29:
									player.getDialogue().sendStatement("You have been recognized as a member of the Black Arm Gang.");
									player.getDialogue().endDialogue();
									player.getQuestVars().joinBlackArmGang(true);
									return true;
							}
						}
				}
				switch (player.getQuestStage(27)) {
					case ARMBAND_GOTTEN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().ownsItem(ARMBAND)) {
									player.getDialogue().sendPlayerChat("I seem to have lost my armband.", SAD);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Hello again, 'Master Thief', heh. Feels good,", "doesn't it?", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("You adventurers, always losing things.", "Here's another.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendGiveItemNpc("Katrine hands you a Master Thief armband", new Item(ARMBAND));
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
					case CANDLESTICKS_GOTTEN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getQuestVars().isBlackArmGang() && !player.getInventory().ownsItem(CANDLESTICK)) {
									player.getDialogue().sendPlayerChat("How would I go about getting a Master Thief", "armband?", CONTENT);
									return true;
								} else if (player.getQuestVars().isBlackArmGang() && player.getInventory().playerHasItem(CANDLESTICK) && player.getQuestStage(27) == CANDLESTICKS_GOTTEN) {
									player.getDialogue().sendPlayerChat("I have retrieved a candlestick!", HAPPY);
									player.getDialogue().setNextChatId(12);
									return true;
								} else {
									return false;
								}
							case 2:
								player.getDialogue().sendNpcChat("Master thief? Ain't we the ambitious one!", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Well, you're gonna have to do something pretty", "amazing.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("Anything you can suggest?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Well, some of the MOST coveted prizes right", "now are in the pirate town of Brimhaven,", "on Karamja.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("The pirate leader Scarface Pete has a pair of extremely", "valuable candlesticks.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendNpcChat("His security is VERY good.", CONTENT);
								return true;
							case 8:
								player.getDialogue().sendNpcChat("We, of course, have gang members in a town like", "Brimhaven who may be able to help you.", CONTENT);
								return true;
							case 9:
								player.getDialogue().sendNpcChat("Visit our hideout in the alleyway on palm street.", CONTENT);
								return true;
							case 10:
								player.getDialogue().sendNpcChat("To get in you will need to tell them the secret password", "'four leafed clover'", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 12:
								player.getDialogue().sendNpcChat("Hmmm. Not bad, not bad. Let's see it, make sure it's", "genuine.", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendGiveItemNpc("You show Katrine the candlestick.", new Item(CANDLESTICK));
								return true;
							case 14:
								player.getDialogue().sendPlayerChat("So is this enough to get me a Master Thief", "armband?", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Hmm...", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendNpcChat("I dunno...", CONTENT);
								return true;
							case 17:
								player.getDialogue().sendNpcChat("Ah, what the heck. I suppose I'm in a generous mood", "today.", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendGiveItemNpc("Katrine hands you a Master Thief armband.", new Item(ARMBAND));
								player.getDialogue().endDialogue();
								player.getInventory().replaceItemWithItem(new Item(CANDLESTICK), new Item(ARMBAND));
								player.setQuestStage(27, ARMBAND_GOTTEN);
								return true;
						}
						return false;
				}
				return false;
			case STRAVEN:
				switch (player.getQuestStage(13)) {
					case 12:
						if (!player.getQuestVars().isPhoenixGang() && !player.getQuestVars().isBlackArmGang()) {
							switch (player.getDialogue().getChatId()) {
								case 1:
									player.getDialogue().sendPlayerChat("Hello, Straven.", CONTENT);
									player.getDialogue().setNextChatId(25);
									return true;
								case 25:
									player.getDialogue().sendNpcChat("Hmm, your face looks familiar. Who are you again?", CONTENT);
									return true;
								case 26:
									player.getDialogue().sendOption("I'm a loyal member of your Phoenix Gang.", "A nobody, I'm just poking around.");
									return true;
								case 27:
									switch (optionId) {
										case 1:
											player.getDialogue().sendPlayerChat("I'm a loyal member of your Phoenix Gang.", CONTENT);
											return true;
										case 2:
											player.getDialogue().sendPlayerChat("A nobody, I'm just poking around. See you later.", CONTENT);
											player.getDialogue().endDialogue();
											return true;
									}
								case 28:
									player.getDialogue().sendNpcChat("Ah, yes, that's right. Welcome back.", CONTENT);
									return true;
								case 29:
									player.getDialogue().sendStatement("You have been recognized as a member of the Phoenix Gang.");
									player.getQuestVars().joinPhoenixGang(true);
									player.getDialogue().endDialogue();
									return true;
							}
						}
				}
				switch (player.getQuestStage(27)) {
					case ARMBAND_GOTTEN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (!player.getInventory().ownsItem(ARMBAND)) {
									player.getDialogue().sendPlayerChat("I seem to have lost my armband.", SAD);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Hello again, 'Master Thief', heh. Feels good,", "doesn't it?", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
							case 2:
								player.getDialogue().sendNpcChat("You adventurers, always losing things.", "Here's another.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendGiveItemNpc("Straven hands you a Master Thief armband.", new Item(ARMBAND));
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
						switch (player.getDialogue().getChatId()) {
							case 1:
								if (player.getQuestVars().isPhoenixGang() && !player.getInventory().ownsItem(1577)) {
									player.getDialogue().sendPlayerChat("How would I go about getting a Master Thief", "armband?", CONTENT);
									return true;
								} else if (player.getQuestVars().isPhoenixGang() && player.getInventory().playerHasItem(CANDLESTICK) && player.getQuestVars().hasShotGrip()) {
									player.getDialogue().sendPlayerChat("I have retrieved a candlestick!", HAPPY);
									player.getDialogue().setNextChatId(10);
									return true;
								} else {
									return false;
								}
							case 2:
								player.getDialogue().sendNpcChat("Ooh... tricky stuff. Took me YEARS to get that rank.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("Well, what some of the more aspiring thieves in our", "gang are working on right now is to steal some very", "valuable candlesticks from Scarface Pete - the pirate", "leader on Karamja.", CONTENT);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("His security is excellent, and the target very valuable so", "that might be enough to get you the rank.", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendNpcChat("Go talk to our man Alfonse, the waiter in the Shrimp", "and Parrot.", CONTENT);
								return true;
							case 6:
								player.getDialogue().sendNpcChat("Use the secret word 'gherkin' to show you're one of us.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 10:
								player.getDialogue().sendNpcChat("Hmmm. Not bad, not bad. Let's see it, make sure it's", "genuine.", CONTENT);
								return true;
							case 11:
								player.getDialogue().sendGiveItemNpc("You show Straven the candlestick.", new Item(CANDLESTICK));
								return true;
							case 12:
								player.getDialogue().sendPlayerChat("So is this enough to get me a Master Thief", "armband?", CONTENT);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Hmm...", CONTENT);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("I dunno...", CONTENT);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Ah, what the heck. I suppose I'm in a generous mood", "today.", CONTENT);
								return true;
							case 16:
								player.getDialogue().sendGiveItemNpc("Straven hands you a Master Thief armband.", new Item(ARMBAND));
								player.getDialogue().endDialogue();
								player.getInventory().replaceItemWithItem(new Item(CANDLESTICK), new Item(ARMBAND));
								player.setQuestStage(27, ARMBAND_GOTTEN);
								player.getQuestVars().setShotGrip(false);
								return true;
						}
						return false;
				}
				return false;
			case ACHIETTIES:
				switch (player.getQuestStage(27)) {
					case 0:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Greetings. Welcome to the Heroes' Guild.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Only the greatest heroes of this land may gain", "entrance to this guild.", CONTENT);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I'm a hero! May I apply to join?", HAPPY);
								return true;
							case 4:
								if (!canDoQuest(player)) {
									player.getDialogue().sendNpcChat("Hmm, I'm afraid you're not quite the heroic type.", "There's a requisite to enter this guild.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendNpcChat("Well, you have a lot of quest points, and you have done", "all of the required quests, so you may now begin the", "tasks to meet the entry requirements for membership in", "the Heroes' Guild. The three items required", CONTENT);
									player.getDialogue().setNextChatId(6);
									return true;
								}
							case 5:
								player.getDialogue().sendStatement("You must have atleast 50 Quest Points and have completed:", "Shield of Arrav, Dragon Slayer, Merlin's Crystal and Lost City,", "to become a member of the Heroes' Guild.");
								player.getDialogue().endDialogue();
								return true;
							case 6:
								player.getDialogue().sendNpcChat("for entrance are: An Entranan Firebird feather, a", "Master Thieves' armband, and a cooked Lava Eel.", CONTENT);
								return true;
							case 7:
								player.getDialogue().sendOption("Any hints on getting the armband?", "Any hints on getting the feather?", "Any hints on getting the eel?", "I'll start looking for all those things then.");
								return true;
							case 8:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Any hints on getting the thieves armband?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Any hints on getting the feather?", CONTENT);
										player.getDialogue().setNextChatId(13);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("Any hints on getting the eel?", CONTENT);
										player.getDialogue().setNextChatId(14);
										return true;
									case 4:
										player.getDialogue().sendPlayerChat("I'll start looking for all those things then.", CONTENT);
										player.getDialogue().setNextChatId(15);
										return true;
								}
								return false;
							case 9:
								player.getDialogue().sendNpcChat("Connections you made in the Varrock gangs may be able", "to assist you. Which gang are you a part of?", CONTENT);
								return true;
							case 10:
								if (player.getQuestVars().isPhoenixGang() && !player.getQuestVars().isBlackArmGang()) {
									player.getDialogue().sendPlayerChat("I'm a member of the Phoenix Gang.", CONTENT);
									return true;
								} else if (!player.getQuestVars().isPhoenixGang() && player.getQuestVars().isBlackArmGang()) {
									player.getDialogue().sendPlayerChat("I'm a member of the Black Arm Gang.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Erm, I'm not quite sure. The gang may have", "forgotten about me, it's been a while.", CONTENT);
									player.getDialogue().setNextChatId(12);
									return true;
								}
							case 11:
								player.getDialogue().sendNpcChat("Very good, the leader of your gang can point you", "in the right direction.", CONTENT);
								player.getDialogue().setNextChatId(7);
								return true;
							case 12:
								player.getDialogue().sendNpcChat("You had better re-affirm your membership in the", "gang you chose originally. The leader of that", "gang then will be able to assist you.", CONTENT);
								player.getDialogue().setNextChatId(7);
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Not really - other than Entranan firebirds tend to live", "on Entrana and burn at molten degrees.", CONTENT);
								player.getDialogue().setNextChatId(7);
								return true;
							case 14:
								player.getDialogue().sendNpcChat("Maybe go and find someone who makes his living", "off of fishing?", CONTENT);
								player.getDialogue().setNextChatId(7);
								return true;
							case 15:
								player.getDialogue().sendNpcChat("Good luck with that adventurer.", CONTENT);
								player.getDialogue().endDialogue();
								QuestHandler.startQuest(player, 27);
								return true;
						}
						return false;
					case QUEST_COMPLETE:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("Welcome back to the Heroes Guild!", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						return false;
					case QUEST_STARTED:
					case CANDLESTICKS_GOTTEN:
					case ARMBAND_GOTTEN:
						switch (player.getDialogue().getChatId()) {
							case 1:
								player.getDialogue().sendNpcChat("How goes thy quest adventurer?", CONTENT);
								return true;
							case 2:
								if (hasAllItems(player)) {
									player.getDialogue().sendPlayerChat("I have all the required items.", CONTENT);
									player.getDialogue().setNextChatId(17);
									return true;
								} else {
									player.getDialogue().sendOption("I'm a little confused, could you help?", "It's going just fine, thanks.");
									return true;
								}
							case 3:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("I'm a little confused, could you help?", CONTENT);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("It's going just fine, thanks.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
							case 4:
								player.getDialogue().sendNpcChat("I may be able to, what do you need help with?", CONTENT);
								return true;
							case 5:
								player.getDialogue().sendOption("Any hints on getting the armband?", "Any hints on getting the feather?", "Any hints on getting the eel?", "Nevermind.");
								return true;
							case 6:
								switch (optionId) {
									case 1:
										player.getDialogue().sendPlayerChat("Any hints on getting the thieves armband?", CONTENT);
										player.getDialogue().setNextChatId(9);
										return true;
									case 2:
										player.getDialogue().sendPlayerChat("Any hints on getting the feather?", CONTENT);
										player.getDialogue().setNextChatId(13);
										return true;
									case 3:
										player.getDialogue().sendPlayerChat("Any hints on getting the eel?", CONTENT);
										player.getDialogue().setNextChatId(14);
										return true;
									case 4:
										player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
										player.getDialogue().endDialogue();
										return true;
								}
								return false;
							case 9:
								player.getDialogue().sendNpcChat("Connections you made in the Varrock gangs may be able", "to assist you. Which gang are you a part of?", CONTENT);
								return true;
							case 10:
								if (player.getQuestVars().isPhoenixGang() && !player.getQuestVars().isBlackArmGang()) {
									player.getDialogue().sendPlayerChat("I'm a member of the Phoenix Gang.", CONTENT);
									return true;
								} else if (!player.getQuestVars().isPhoenixGang() && player.getQuestVars().isBlackArmGang()) {
									player.getDialogue().sendPlayerChat("I'm a member of the Black Arm Gang.", CONTENT);
									return true;
								} else {
									player.getDialogue().sendPlayerChat("Erm, I'm not quite sure. The gang may have", "forgotten about me, it's been a while.", CONTENT);
									player.getDialogue().setNextChatId(12);
									return true;
								}
							case 11:
								player.getDialogue().sendNpcChat("Very good, the leader of your gang can point you", "in the right direction.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 12:
								player.getDialogue().sendNpcChat("You had better re-affirm your membership in the", "gang you chose originally. The leader of that", "gang then will be able to assist you.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 13:
								player.getDialogue().sendNpcChat("Not really - other than Entranan firebirds tend to live", "on Entrana and burn at molten degrees.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 14:
								player.getDialogue().sendNpcChat("Maybe go and find someone who makes his living", "off of fishing?", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 17:
								player.getDialogue().sendNpcChat("I see that you have. Well done; Now, to complete the", "quest, and gain entry to the Heroes' Guild in your final", "task all that you have to do is...", CONTENT);
								return true;
							case 18:
								player.getDialogue().sendPlayerChat("W-what? What do you mean? There's MORE???", DISTRESSED);
								return true;
							case 19:
								player.getDialogue().sendNpcChat("I'm sorry. I was just having a little fun with you. Just", "some Heroes' Guild humor there. What I really meant was", CONTENT);
								return true;
							case 20:
								player.getDialogue().sendNpcChat("Congratulations! You have completed the Heroes' Guild", "entry requirements! You will find the door now open", "for you! Enter, Hero! And take this reward!", CONTENT);
								return true;
							case 21:
								player.getInventory().removeItem(new Item(FIREBIRD_FEATHER));
								player.getInventory().removeItem(new Item(ARMBAND));
								player.getInventory().removeItem(new Item(LAVA_EEL));
								QuestHandler.completeQuest(player, 27);
								player.getDialogue().dontCloseInterface();
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
