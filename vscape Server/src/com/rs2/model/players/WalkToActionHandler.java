package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.cache.object.GameObjectData;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.Pets;
import com.rs2.model.content.Shops;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dungeons.Abyss;
import com.rs2.model.content.minigames.warriorsguild.WarriorsGuild;
import com.rs2.model.content.minigames.castlewars.*;
import com.rs2.model.content.minigames.castlewars.impl.CastlewarsBarricades;
import com.rs2.model.content.minigames.duelarena.GlobalDuelRecorder;
import com.rs2.model.content.minigames.fightcaves.FightCaves;
import com.rs2.model.content.minigames.magetrainingarena.MageRewardHandling;
import com.rs2.model.content.minigames.pestcontrol.*;
import com.rs2.model.content.quests.impl.GhostsAhoy.GhostsAhoy;
import com.rs2.model.content.quests.impl.HeroesQuest;
import com.rs2.model.content.quests.impl.InSearchOfTheMyreque;
import com.rs2.model.content.quests.impl.MonkeyMadness.ApeAtoll;
import com.rs2.model.content.quests.impl.PiratesTreasure;

import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.Crafting.GemCrafting;
import com.rs2.model.content.skills.Crafting.GlassMaking;
import com.rs2.model.content.skills.Crafting.PotteryMaking;
import com.rs2.model.content.skills.Crafting.SilverCrafting;
import com.rs2.model.content.skills.Crafting.Tanning;
import com.rs2.model.content.skills.agility.ShortcutHandler;
import com.rs2.model.content.skills.agility.CrossObstacle;
import com.rs2.model.content.skills.farming.Farming;
import com.rs2.model.content.skills.fishing.Fishing;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.content.skills.mining.MineEssence;
import com.rs2.model.content.skills.mining.MineOre;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.skills.runecrafting.MixingRunes;
import com.rs2.model.content.skills.runecrafting.RunecraftAltars;
import com.rs2.model.content.skills.runecrafting.Runecrafting;
import com.rs2.model.content.skills.runecrafting.Tiaras;
import com.rs2.model.content.skills.smithing.DragonShieldSmith;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.content.skills.smithing.SmithBars;
import com.rs2.model.content.skills.thieving.ThieveNpcs;
import com.rs2.model.content.skills.thieving.ThieveOther;
import com.rs2.model.content.skills.thieving.ThieveStalls;
import com.rs2.model.content.skills.Woodcutting.ChopTree;
import com.rs2.model.content.skills.Woodcutting.ChopTree.Tree;
import com.rs2.model.content.treasuretrails.AnagramsScrolls;
import com.rs2.model.content.treasuretrails.MapScrolls;
import com.rs2.model.content.treasuretrails.SearchScrolls;
import com.rs2.model.content.treasuretrails.SpeakToScrolls;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcActions;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.objects.functions.AxeInLog;
import com.rs2.model.objects.functions.CoalTruck;
import com.rs2.model.objects.functions.CrystalChest;
import com.rs2.model.objects.functions.Doors;
import com.rs2.model.objects.functions.DoubleDoors;
import com.rs2.model.objects.functions.FlourMill;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.objects.functions.MilkCow;
import com.rs2.model.objects.functions.ObeliskTick;
import com.rs2.model.objects.functions.PickableObjects;
import com.rs2.model.objects.functions.TrapDoor;
import com.rs2.model.objects.functions.Webs;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.clip.Rangable;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.quests.impl.Quest;
import com.rs2.model.content.quests.impl.TheGrandTree;
import com.rs2.model.content.quests.impl.UndergroundPass.PassObjectHandling;
import com.rs2.model.content.randomevents.SpawnEvent;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.content.skills.firemaking.BarbarianSpirits;
import com.rs2.model.content.skills.smithing.DragonfireShieldSmithing;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.transport.Sailing;
import com.rs2.model.transport.Travel;
import com.rs2.util.clip.ClippedPathFinder;

import java.util.Random;

public class WalkToActionHandler {

	private static Actions actions = Actions.OBJECT_FIRST_CLICK;
	
	public static void doActions(Player player) {
		switch (actions) {
		case OBJECT_FIRST_CLICK:
			doObjectFirstClick(player);
			break;
		case OBJECT_SECOND_CLICK:
			doObjectSecondClick(player);
			break;
		case OBJECT_THIRD_CLICK:
			doObjectThirdClick(player);
			break;
		case OBJECT_FOURTH_CLICK:
			doObjectFourthClick(player);
			break;
		case NPC_FIRST_CLICK:
			doNpcFirstClick(player);
			break;
		case NPC_SECOND_CLICK:
			doNpcSecondClick(player);
			break;
		case NPC_THIRD_CLICK:
			doNpcThirdClick(player);
			break;
		case NPC_FOURTH_CLICK:
			doNpcFourthClick(player);
			break;
		case ITEM_ON_OBJECT:
			doItemOnObject(player);
			break;
		case ITEM_ON_NPC:
			doItemOnNpc(player);
			break;
		}
	}

	public static void doObjectFirstClick(final Player player) {
		final int id = player.getClickId();
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ()%4;
		final String objectName = GameObjectData.forId(id) != null ? GameObjectData.forId(id).getName().toLowerCase() : "";
		final int task = player.getTask();
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					if (id == 2142 || id == 2297 || id == 4879 || (id >= 7272 && id <= 7287)  || id == 5003 || id == 4766 || id == 5002 || id == 3522 || id == 4880 || id == 4881 || id == 5015 || id == 2311 || id == 2294 || id == 2295 || id == 2296 || id == 2022 || id == 9293  || id == 9328 || id == 2834 || id == 9330 || id == 9322 || id == 9324 || id == 2332 || id == 3931 || id == 3932 || id == 3933 || (id == 3203 || id == 4616 || id == 4615) || (id == 2213 && x == 3513) || (id == 356 && y == 3507) || GameObjectData.forId(id).getName().toLowerCase().contains("gangplank") || (id >= 14227 && id <= 14231)) { //exceptions
						def = new GameObjectDef(id, 10, 0, new Position(x, y, z));
					} else if (id == 4381 || id == 4382 || id == 4385 || id == 4386) { //exceptions
						def = new GameObjectDef(id, 11, 0, new Position(x, y, z));
					} else if (id == 4765) {
						def = new GameObjectDef(id, 0, 0, new Position(x, y, z));
					} else if (id == 4766) {
						def = new GameObjectDef(id, 9, 0, new Position(x, y, z));
					} else {
						return;
					}
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				
				if(id != 1729 && id != 2290 && id != 3340) {
				    if (objectPosition == null) {
					return;
				    }
				}
				if(id != 1729 && id != 2290) {
				    if (!canInteractWithObject(player, objectPosition, def)) {
					stop();
					return;
				    }
				} else if(!canInteractWithObject(player, def.getPosition(), def)) {
					stop();
					return;
				}
			/*	if (id == 4031)
					player.getActionSender().walkTo(0, player.getPosition().getY() > y ? -2 : 2, true);
					*/
				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));

				if (player.getBarrows().handleBarrowsObject(player, id)) {
					this.stop();
					return;
				}
				if (player.getBarrows().handleBarrowsDoors(player, id, x, y)) {
					this.stop();
					return;
				}
				if (PickableObjects.pickObject(player, id, x, y)) {
					this.stop();
					return;
				}
				if (player.getSlayer().handleObjects(id, x, y)) {
					this.stop();
					return;
				}
				for(Quest q : QuestHandler.getQuests()) {
				    if(q.doObjectClicking(player, id, x, y)) {
					this.stop();
					return;
				    }
				}
				if(ApeAtoll.doObjectFirstClick(player, id, x, y)) {
					this.stop();
					return;
				}
				if(PassObjectHandling.doObstacleClicking(player, id, x, y)) {
					this.stop();
					return;
				}
				if(player.getRandomHandler().getCurrentEvent() != null) {
					if(player.getRandomHandler().getCurrentEvent().doObjectClicking(id, x, y, z)) {
						this.stop();
						return;
					}
				}
				if (ObeliskTick.clickObelisk(id)) {
					this.stop();
					return;
				}
				if(player.getMultiCannon().objectFirstClick(id, x, y, z)) {
					this.stop();
				    return;
				}
				if(player.getDesertHeat().CutCactus(id, x, y, z, def.getFace())) {
					this.stop();
				    return;
				}
				if(CastlewarsObjects.handleObject(player, id, x, y, z))
				{
					this.stop();
					return;
				}
				if (player.getNewComersSide().handleObjectClicking(id, new Position(x, y, z))) {
					this.stop();
					return;
				}
				if (Abyss.handleObstacle(player, id, x, y)) {
					this.stop();
					return;
				}
				if (RunecraftAltars.runecraftAltar(player, id) || RunecraftAltars.clickRuin(player, id)) {
					this.stop();
					return;
				}
				if (object.getName().toLowerCase().contains("altar") && id != 2640 && id != 6552) {
				    if(id == 412 && x == 2571 && y == 9499) { //Yanille dungeon trap chaos altar
					player.getActionSender().sendMessage("The altar moves and you fall into a pit below!");
					player.fadeTeleport(new Position(2580, 9575, 0));
					this.stop();
					return;
				    } else if(id == 3521) { //Altar to nature / nature altar
					Prayer.rechargePrayerGuild(player);
					this.stop();
					return; 
				    } else {
					Prayer.rechargePrayer(player);
					this.stop();
					return;
				    }
				}
				if (player.getAlchemistPlayground().handleObjectClicking(id, x, y)) {
					this.stop();
					return;
				}
				if (player.getEnchantingChamber().handleObjectClicking(id)) {
					this.stop();
					return;
				}
				if (player.getCreatureGraveyard().handleObjectClicking(id, x, y, z)) {
					this.stop();
					return;
				}
				if (player.getTelekineticTheatre().handleObjectClicking(id, x, y, z)) {
					this.stop();
					return;
				}
				if (PestControl.handleObjectClicking(player, id, x, y)) {
					this.stop();
					return;
				}
				if (WarriorsGuild.handleObjectClicking(player, id, x, y)) {
					this.stop();
					return;
				}
				if (id == 10093) {
					Menus.sendSkillMenu(player, "dairyChurn");
					this.stop();
					return;
				}
				if (id == 2551 || id == 2550 || id == 2556 || id == 2558 || id == 2557 || id == 2554 || id == 2559) {
					player.getActionSender().sendMessage("This door is locked.");
					this.stop();
					return;
				}
				if (id == 2566) {
					player.getActionSender().sendMessage("This chest is locked.");
					this.stop();
					return;
				}
				if (MapScrolls.handleCrate(player, x, y)) {
					this.stop();
					return;
				}
				if (SearchScrolls.handleObject(player, def)) {
					this.stop();
					return;
				}
				if (Farming.harvest(player, x, y)) {
					this.stop();
					return;
				}
				/*
				 * if (Woodcutting.isTree(id)) { if
				 * (player.getWoodcutting().canCut(id, x, y)) {
				 * player.getWoodcutting().cut(id, x, y); } this.stop(); return;
				 * }
				 */
				if (Tree.getTree(id) != null) {
					ChopTree.handle(player, id, x, y);
					this.stop();
					return;
				}
				if(player.getCanoe().objectClick(id, x, y, z))
				{
					this.stop();
					return;
				}
				if (ShortcutHandler.handleShortcut(player, id, x, y)) {
					this.stop();
					return;
				}
				if (player.getAgilityCourses().handleCourse(id, x, y)) {
					this.stop();
					return;
				}
				if (MineOre.miningRocks(id)) {
					if (player.getMining().canMine(id)) {
						player.getMining().startMining(id, x, y);
					}
					this.stop();
					return;
				}

				if (player.getCompost().handleObjectClick(id, x, y)) {
					this.stop();
					return;
				}
				if (DoubleDoors.handleDoubleDoor(id, x, y, z)) {
					this.stop();
					return;
				}
				if (!player.getNewComersSide().isInTutorialIslandStage()) {
					if (Doors.handleDoor(id, x, y, z)) {
						this.stop();
						return;
					}
				}
				if (ThieveOther.handleObjectClick(player, id, x, y)) {
					this.stop();
					return;
				}
				/*if (objectName.contains("gate") && id != 2882 && id != 2883 && id != 2623) {
					// Gates.handleGate(id, x, y, z);
					final int face = SkillHandler.getFace(id, x, y, z);
					final int type = SkillHandler.getType(id, x, y, z);
					ObjectHandler.getInstance().removeClip(id, x, y, z, type, face);
					new GameObject(Constants.EMPTY_OBJECT, x, y, z, face, type, id, 999999);
					this.stop();
					return;
				}*/
				if (objectName.contains("hay")) {
					player.getActionSender().sendMessage("You search the hay bales...");
					player.getUpdateFlags().sendAnimation(832);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (Misc.random(99) == 0) {
								if (Misc.random(1) == 0) {
									player.getDialogue().sendPlayerChat("Wow! A needle!", "Now what are the chances of finding that?", Dialogues.HAPPY);
									player.getDialogue().endDialogue();
									player.getInventory().addItem(new Item(1733));
								} else {
									player.hit(1, HitType.NORMAL);
									player.getDialogue().sendPlayerChat("Ow! There's something sharp in there!", Dialogues.SAD);
									player.getDialogue().endDialogue();
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
					this.stop();
					return;
				}
				if (objectName.contains("crate") && id != 3398) {
					player.getActionSender().sendMessage("You search the crate...");
					player.getUpdateFlags().sendAnimation(832);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (Misc.random(99) == 0) {
								Item[] rewards = {new Item(995, 10), new Item(686), new Item(687), new Item(688), new Item(689), new Item(690), new Item(697), new Item(1059), new Item(1061)};
								Item reward = rewards[Misc.randomMinusOne(rewards.length)];
								player.getInventory().addItem(reward);
								player.getActionSender().sendMessage("You find some "+reward.getDefinition().getName().toLowerCase()+"!");
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
					this.stop();
					return;
				}
				if (objectName.contains("bookcase")) {
					player.getActionSender().sendMessage("You search the books...");
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							player.getActionSender().sendMessage("None of them look very interesting.");
							b.stop();
						}
						@Override
						public void stop() {
							player.setStopPacket(false);
						}
					}, 2);
					this.stop();
					return;
				}
				switch (id) {
				case 52 :
				case 53 :
					if(x >= 2649 && x <= 2650 && y == 3470)
					{
						player.getActionSender().sendMessage("The gate is locked.");
					}
					break;
				case 10638: //Entrana altar
				    Prayer.rechargePrayer(player);
				    this.stop();
				    return;
				case 377:
				    if(player.getInventory().playerHasItem(993) && Misc.goodDistance(player.getPosition(), new Position(2561, 9507, 0), 2)) {
					 player.getUpdateFlags().sendAnimation(TheGrandTree.PLACE_ANIM);
					 player.getActionSender().sendMessage("You open the sinister looking chest with your key.");
					 player.getInventory().replaceItemWithItem(new Item(993), new Item(205));
					 player.getInventory().addItem(new Item(205));
					 player.getInventory().addItem(new Item(207, 3));
					 player.getInventory().addItem(new Item(209));
					 player.getInventory().addItem(new Item(211));
					 player.getInventory().addItem(new Item(213));
					 player.getInventory().addItem(new Item(219));
				    } else {
					player.getActionSender().sendMessage("The sinister looking chest is locked.");
				    }
				    break;
				case 2994 :
				case 2993 :
				case 2992 :
				case 2991 :
				case 2990 :
				case 2989 :
				case 2013 :
				case 58 :
					player.getUpdateFlags().sendAnimation(830);
					player.getActionSender().sendMessage("You dig in amongst the vines.");
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							player.getActionSender().sendMessage("You find a red vine worm.");
							player.getInventory().addItem(new Item(25));
							b.stop();
						}
						@Override
						public void stop() {
							player.setStopPacket(false);
						}
					}, 2);
					break;
				/*case 54 : //dwarf tunnel shortcut left (under)
					if(x == 2820 && y == 9883)
					{
						player.teleport(new Position(2820, 3486, 0));
					}
					break;
				case 55 : //dwarf tunnel shortcut left (overworld)
					if(x == 2820 && y == 3484)
					{
						player.teleport(new Position(2820, 9882, 0));
					}
					break;
				case 56 : //dwarf tunnel shortcut right (under)
					if(x == 2876 && y == 9880)
					{
						player.teleport(new Position(2876, 3482, 0));
					}
					break;
				case 57 : //dwarf tunnel shortcut right (overworld)
					if(x == 2876 && y == 3480)
					{
						player.teleport(new Position(2876, 9879, 0));
					}
					break;*/
				case 6836 :
						player.getRandomHandler().getPillory().openInterface();
					break;
				case 7527:
					if(x == 3063 && y == 3282) {
						final Position toBe = new Position(3063, player.getPosition().getY() < 3283 ? 3284 : 3281, 0);
						final int toWalk = player.getPosition().getY() < 3283 ? 1 : -1;
						player.getUpdateFlags().sendFaceToDirection(toBe);
						player.getUpdateFlags().setFaceToDirection(true);
						player.getUpdateFlags().setUpdateRequired(true);
						player.setStopPacket(true);
						player.getActionSender().walkTo(player.getPosition().getX() == x ? 0 : player.getPosition().getX() < x ? 1 : -1, toWalk, true);
						player.getUpdateFlags().sendAnimation(839);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer b) {
								b.stop();
							}

							@Override
							public void stop() {
								player.teleport(toBe);
								player.setStopPacket(false);
							}
						}, 3);
					}
				break;
				case 2303: //Yanille dungeon balancing ledge
				    Agility.crossLedge(player, 2580, player.getPosition().getY() < 9520 ? 9520 : 9512, player.getPosition().getY() < 9520 ? 3 : 1, 10, 40, 25);
				    break;
				case 2316: //Yanille dungeon staircase from spiders
				    player.fadeTeleport(new Position(2579, 9520, 0));
				    break;
				case 1728: //Yanille dungeon stairs to Salarin
				    player.fadeTeleport(new Position(2620, 9565, 0));
				    break;
				case 1729: //Yanille dungeon stairs back from Salarin
				    player.fadeTeleport(new Position(2620, 9496, 0));
				    break;
				case 2290:
				    Agility.crawlPipe(player, player.getPosition().getX() > 2572 ? 2572 : 2578, y, 6, 49, 35);
				    break;
				case 2321: //Yanille dungeon monkey bars
				    Agility.crossMonkeyBars(player, player.getPosition().getX(), player.getPosition().getY() < 9495 ? 9495 : 9488, 57, 30);
				    break;
				case 2317: //Yanille dungeon rubble level 67
				    if(player.getSkill().getLevel()[Skill.AGILITY] < 67) {
					player.getDialogue().sendStatement("You need an Agility level of 67 to climb this obstacle.");
					break;
				    } else {
					Ladders.climbLadder(player, new Position(2614, 9504, 0));
					break;
				    }
				case 2318: //Yanille dungeon pile of rubble
				    player.fadeTeleport(new Position(2616, 9571, 0));
				    break;
				case 2114 : // coal truck
					CoalTruck.withdrawCoal(player);
					break;
				case 2693 : // shantay bank chest
				case 4483 : // castle wars bank chest
				    if(player.getFightCavesWave() > 0 ) {
					player.getActionSender().sendMessage("You cannot bank with a Fight Caves wave saved! Use ::resetcaves if needed.");
					break;
				    }
				    else {
				    	player.getBankManager().openBank();
					break;
				    }
				case 11888: //Dark wizard tower bottom floor stairs up
				    Ladders.climbLadder(player, new Position(2908, 3336, 1));
				    break;
				case 11890: //Dark wizard tower top floor stairs down
				    Ladders.climbLadder(player, new Position(2908, 3336, 1));
				    break;
				case 10595: //skeletal wyvern exit
				    player.teleport(new Position(3056, 9562, 0));
				    break;
				case 10596: //skeletal wyvern cavern entrance
				    if(!player.wyvernWarned()) {
					player.getDialogue().sendStatement("STOP! The creatures in this cave are VERY dangerous. Make sure", "you are ready before entering!");
					player.setWyvernWarned(true);
					break;
				    } else {
					player.teleport(new Position(3056, 9555, 0));
					break;
				    }
				case 1600:
				case 1601:
				    if(player.getSkill().getLevel()[Skill.MAGIC] < 66) {
					player.getDialogue().sendStatement("You need a Magic level of 66 to enter the Magic Guild.");
					break;
				    }
				    else {
					if(x < 2587) {
					    player.getActionSender().walkTo(player.getPosition().getX() < 2585 ? 1 : -1, 0, true);
					    player.getActionSender().walkThroughDoubleDoor(1600, 1601, 2584, 3088, 2584, 3087, 0);
					    break;
					}
					else {
					    player.getActionSender().walkTo(player.getPosition().getX() > 2596 ? -1 : 1, 0, true);
					    player.getActionSender().walkThroughDoubleDoor(1600, 1601, 2597, 3087, 2597, 3088, 0);
					    break;
					}
				    }
				case 5098: //brimhaven stairs back down
				case 5096:
				    if(player.getPosition().getY() > 9585) {
					player.teleport(new Position(2650, 9591, 0));
					break;
				    }
				    else if(player.getPosition().getY() < 9520) {
					player.teleport(new Position(2636, 9517, 0));
					break;
				    }
				case 3205: //ladder down
					if(x == 2766 && y == 3121) {
						Ladders.climbLadder(player, new Position(2767, 3121, 0)); //General store karamja
						break;
					}
				case 8972: //freaky forester portal
					if(x == 2611 && y == 4776 && player.getRandomHandler().getCurrentEvent() != player.getRandomHandler().getFreakyForester()) {
						for(Item item : player.getInventory().getItemContainer().getItems()) {
							if(item == null) continue;
							if(item.getId() == 6179) player.getInventory().removeItem(new Item(6179));
							if(item.getId() == 6178) player.getInventory().removeItem(new Item(6178));
					    }
						player.teleport(player.getLastPosition());
						break;
					}
				case 5097:
				case 5094: //north brimhaven dungeon stairs
					Dialogues.startDialogue(player, 2725);
					break;
				case 3193 : // closed bank chest
					player.getUpdateFlags().sendAnimation(832);
					new GameObject(3194, x, y, z, def.getFace(), def.getType(), id, 500);
					break;
				case 1317: //spirit tree south ardougne && northwest varrock
				    if(QuestHandler.questCompleted(player, 33)) {
					Dialogues.startDialogue(player, 10022);
				    } else {
					player.getDialogue().sendStatement("You must complete Tree Gnome Village to use this.");
					player.getDialogue().endDialogue();
				    }
				    break;
				case 1293 : // spirit tree the grand tree
				    if(QuestHandler.questCompleted(player, 33)) {
					Dialogues.startDialogue(player, 10011);
				    } else {
					player.getDialogue().sendStatement("You must complete Tree Gnome Village to use this.");
					player.getDialogue().endDialogue();
				    }
				    break;
				case 1294 : //spirit tree tree gnome village
				    if(QuestHandler.questCompleted(player, 33)) {
					Dialogues.startDialogue(player, 10023);
				    } else {
					player.getDialogue().sendStatement("You must complete Tree Gnome Village to use this.");
					player.getDialogue().endDialogue();
				    }
				    break;
				case 9356: //fight caves entrance
				    if(x == 2437) {
					FightCaves.enterCave(player);
					break;
				    }
				break;
				case 10771: //Mage training arena stairs up east
				    player.teleport(new Position(3369, 3307, 1));
				    break;
				case 10775: //Mage training arena stairs up west
				    player.teleport(new Position(3357, 3307, 1));
				    break;
				case 10773: //Mage training arena stairs down east
				    player.teleport(new Position(3366, 3306, 0));
				    break;
				case 10776: //Mage training arena stairs down west
				    player.teleport(new Position(3360, 3306, 0));
				    break;
				case 10721: //Mage training arena entrance
				    player.getActionSender().walkTo(0, player.getPosition().getY() > 3298 ? -2 : 2, true);
				    break;
				case 2143:
				case 2144:
				    if(player.getQuestStage(10) == 2) {
					Dialogues.startDialogue(player, 10999);
				    }
				    else if(player.getQuestStage(10) >= 3) {
					player.getActionSender().walkTo(player.getPosition().getX() < 2889 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoubleDoor(2144, 2143, 2889, 9830, 2889, 9831, 0);
				    }
				break;
				case 3782: //death plateau doors west
				case 3783:
				    if(x == 2897) {
					player.getActionSender().walkTo(player.getPosition().getX() < 2897 ? 1 : -1, 0, true);
				    }
				break;
				case 5167: //experiment entrance
				    if(x == 3578 && y == 3527)
					player.teleport(new Position(3577, 9927));
				break;
				case 1528: //curtain
				    if(x == 3182 && y == 2984) {
					player.getActionSender().walkTo(player.getPosition().getX() < 3183 ? 1 : -1, 0, true);
					
					//ObjectHandler.getInstance().removeClip(1528, x, y, 0, 0, 0);
					//new GameObject(Constants.EMPTY_OBJECT, x, y, 0, 0, 0, 1528, 999999, false);
					//new GameObject(1529, x+1, y, 0, 0, 0, 1528, 999999, false);
					//ObjectHandler.getInstance().removeClip(1529, x-, y, 0, 0, 0);
					
				    }
				    else if(x == 3172 && y == 2977) {
					player.getActionSender().walkTo(0, player.getPosition().getY() < 2977 ? 1 : -1, true);
					//ObjectHandler.getInstance().removeClip(1528, x, y, z, 0, 0);
					//new GameObject(Constants.EMPTY_OBJECT, x, y, 0, 0, 0, 1528, 999999, false);
					//new GameObject(1529, x, y-1, 0, 1, 0, 1528, 999999, false);
				    }
				    break;
				case 4624 : // burthorpe staircase
					if(x == 2899 && y == 3566) {
						player.teleport(new Position(2206, 4934, 1));
					}
					break;
				case 4620: //burthorpe games staircase down
					if(x == 2207 && y == 4935) {
						player.teleport(new Position(2207, 4938, 0));
					}
					break;
				case 4622 : // games room staircase
					if(x == 2207 && y == 4935) {
						player.teleport(new Position(2207, 4934, 1));
					}
					break;
				case 4627: //games room stairs up to castle
					if(x == 2205 && y == 4935) {
						player.teleport(new Position(2899, 3565, 0));
					}
					break;
				case 4626: //burthorpe castle stairs up
					if(x == 2897 && y == 3566) {
						player.teleport(new Position(2898, 3569, 1));
					}
					break;
				case 4625: //burthorpe castle stairs down
					if(x == 2897 && y == 3567) {
						player.teleport(new Position(2897, 3565, 0));
					}
					break;
				case 5959 : // magebank lever
					if(!player.isTeleblocked()){
						player.teleport(new Position(2539, 4712));
					}else{
						player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
					}
					break;
				case 9706 : //magebank lever
				case 9707 :
					if(!player.isTeleblocked()){
						if(player.getCombatingEntity() != null)
						    player.getActionSender().sendMessage("You cannot use this in combat!");
						else if(player.getCombatingEntity() == null && y == 3956)
						    player.teleport(new Position(3106, 3952, 0));
						else if(player.getCombatingEntity() == null && y == 3952)
						    player.teleport(new Position(3105, 3956, 0));
					}else{
						player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
					}
				break;
				case 2878: //magebank pool - cadillac
				    if(player.getMageArenaStage() >= 2) {
					player.getActionSender().sendMessage("The world starts to shimmer around you...");
					player.fadeTeleport(new Position(2509, 4690));
					break;
				    }
				    else {
					player.getActionSender().sendMessage("The water feels cool, yet seems to repel your presence.");
					break;
				    }
				case 2879: //magebank pool2 - cadillac
				    if(player.getMageArenaStage() >= 2) {
					player.getActionSender().sendMessage("The world starts to shimmer around you...");
					player.fadeTeleport(new Position(2541, 4718));
					break;
				    }
				    else {
					player.getActionSender().sendMessage("The water feels cool, yet seems to repel your presence.");
					break;
				    }
				case 2873: //sara statue - cadillac
				    if(player.getMageArenaStage() >= 2) {
					if (player.getCanHaveGodCape()) {
						if(player.getInventory().canAddItem(new Item(2412))) {
						    player.getUpdateFlags().sendAnimation(645);
						    player.getActionSender().sendMessage("Saradomin rewards you with a cape.");
						    player.getInventory().addItem(new Item(2412));
						    player.setCanHaveGodCape(false);
						}
						else
						    player.getActionSender().sendMessage("Your inventory is full!");
					} else {
						player.getActionSender().sendMessage("You already have a God Cape.");
					}
				    }
				    else
					player.getActionSender().sendMessage("You cannot obtain a God Cape yet.");
				break;
				case 2874: //zammy statue - cadillac
					if(player.getMageArenaStage() >= 2) {
					if (player.getCanHaveGodCape()) {
						if(player.getInventory().canAddItem(new Item(2414))) {
						    player.getUpdateFlags().sendAnimation(645);
						    player.getActionSender().sendMessage("Zamorak rewards you with a cape.");
						    player.getInventory().addItem(new Item(2414));
						    player.setCanHaveGodCape(false);
						}
						else
						    player.getActionSender().sendMessage("Your inventory is full!");
					} else {
						player.getActionSender().sendMessage("You already have a God Cape.");
					}
				    }
				    else
					player.getActionSender().sendMessage("You cannot obtain a God Cape yet.");
				break;
				case 2875: //guthix statue - cadillac
					if(player.getMageArenaStage() >= 2) {
					if (player.getCanHaveGodCape()) {
						if(player.getInventory().canAddItem(new Item(2413))) {
						    player.getUpdateFlags().sendAnimation(645);
						    player.getActionSender().sendMessage("Guthix rewards you with a cape.");
						    player.getInventory().addItem(new Item(2413));
						    player.setCanHaveGodCape(false);
						}
						else
						    player.getActionSender().sendMessage("Your inventory is full!");
					} else {
						player.getActionSender().sendMessage("You already have a God Cape.");
					}
				    }
				    else
					player.getActionSender().sendMessage("You cannot obtain a God Cape yet.");
				break;
				case 1815 : // magebank lever near webs
					if(!player.isTeleblocked()){
						player.teleport(new Position(2561, 3311)); //3153 3923
					}else{
						player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
					}
					break;
				case 1814 : // ardy lever
					if(!player.isTeleblocked()){
						player.teleport(new Position(3153, 3923)); //3153 3923
					}else{
						player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
					}
					break;
				case 5960 :
					player.teleport(new Position(3091, 3956));
					break;
                                case 5492 : //ham entry
                                        player.teleport(new Position(3149, 9652));
                                        break;
                                case 5493: //ham ladder exit
                                        Ladders.climbLadder(player, new Position(3166, 3251, 0));
                                        break;
                                case 2833: //Watchtower ladder ground
                                        Ladders.climbLadder(player, new Position(2544, 3112, 1));
                                        break;
                                case 2796: //Watchtower first floor ladder up
                                        Ladders.climbLadder(player, new Position(2549, 3112, 2));
                                        break;
                                case 2797: //Watchtower second floor down
                                        Ladders.climbLadder(player, new Position(2549, 3112, 1));
                                        break;
				case 2834 : // battlements
					/*if(player.getPosition().getX() ==  2566) {
					    //player.getUpdateFlags().sendAnimation(839);
					    //player.getActionSender().walkTo(-2, 0, true);
					    Agility.climbOver(player, 2569, 3022, 1, 0);
					}
					else if(player.getPosition().getX() ==  2568) {
					    //player.getUpdateFlags().sendAnimation(2750);
					    //CrossObstacle.setForceMovement(player, 2, 0, 1, 80, 2, true, 0, 0);
					    Agility.climbOver(player, 2566, 3022, 1, 0);
					} */

					if (player.getPosition().getX() > 2567) {
						player.getUpdateFlags().sendAnimation(839);
						player.getActionSender().walkTo(-2, 0, true);
					} else {
						//player.movePlayer(player.getPosition());
						player.getUpdateFlags().sendAnimation(2750);
						CrossObstacle.setForceMovement(player, 2, 0, 1, 80, 2, true, 0, 0);
					}
					break;
				case 1968 :
				case 1967 : //grand tree doors
				    player.setStopPacket(true);
				    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
					    player.getActionSender().walkTo(0, player.getPosition().getY() < 3492 ? 2 : -2, true);
					    new GameObject(1970, 2466, 3492, 0, 2, 10, 1968, 1);
					    new GameObject(1969, 2464, 3492, 0, 0, 10, 1967, 1);
					    b.stop();
					}

					@Override
					public void stop() {
					    player.setStopPacket(false);
					}
				    }, 2);
				    break;
				case 4616 : //lighthouse bridge
				case 4615 :
					//player.movePlayer(player.getPosition());
					if (player.getPosition().getX() > 2597) {
					player.teleport(new Position(2596, 3608, 0));
					} else {
					player.teleport(new Position(2598, 3608, 0));
					}
					break;
				case 51 : // McGrubors
					if(player.getPosition().getY() == 3500) {
						Agility.crossObstacle(player, player.getPosition().getX() < 2662 ? 2662 : 2661, 3500, 754, 2, 0, 0);
					    player.getActionSender().sendMessage("You squeeze through the fence.");
					}
					break;	
				case 2186 :
					//player.movePlayer(player.getPosition());
					player.getUpdateFlags().sendAnimation(754);
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3161 ? 1 : -1, true);
					//CrossObstacle.setForceMovement(player, 0, player.getPosition().getY() < 3161 ? 1 : -1, 1, 80, 2, true, 0, 0);
					break;
				case 2266 :
					if (player.getPosition().getY() > 2963) {
						player.getActionSender().walkTo(0, -1, true);
						player.getActionSender().walkThroughDoor(id, x, y, z);
					} else {
						Dialogues.startDialogue(player, 513);
					}
					break;
				case 10177 :
					Dialogues.startDialogue(player, 10005);
					break;
				case 8960 :
				case 8959 :
				case 8958 :
					Player p1 = null;
					Player p2 = null;
					for (Player players : World.getPlayers()) {
						if (players == null) {
							continue;
						}
						if (players.getPosition().getX() == 2490 && players.getPosition().getY() == y) {
							p1 = players;
						}
						if (players.getPosition().getX() == 2490 && players.getPosition().getY() == y + 2) {
							p2 = players;
						}
					}
					if (p1 != null && p2 != null) {
						new GameObject(Constants.EMPTY_OBJECT, x, y, z, def.getFace(), def.getType(), id, 50);
					} else {
						player.getActionSender().sendMessage("The door is locked.");
					}
					break;
				case 8966 : // daggnoth cave exit
					player.teleport(new Position(2523, 3739, 0));
					break;
				case 8929 : // daggnoth cave entrance
					player.teleport(new Position(2442, 10146, 0));
					break;
				case 8930 :
					player.teleport(new Position(2545, 10143, 0));
					break;
				case 10193:
					Ladders.climbLadder(player, new Position(2545, 10143, 0));
					break;
				case 10195:
					Ladders.climbLadder(player, new Position(1809, 4405, 2));
					break;
				case 10196:
					Ladders.climbLadder(player, new Position(1807, 4405, 3));
					break;
				case 10197:
					Ladders.climbLadder(player, new Position(1823, 4404, 2));
					break;
				case 10198:
					Ladders.climbLadder(player, new Position(1825, 4404, 3));
					break;
				case 10199:
					Ladders.climbLadder(player, new Position(1834, 4388, 2));
					break;
				case 10200:
					Ladders.climbLadder(player, new Position(1834, 4390, 3));
					break;
				case 10201:
					Ladders.climbLadder(player, new Position(1811, 4394, 1));
					break;
				case 10202:
					Ladders.climbLadder(player, new Position(1812, 4394, 2));
					break;
				case 10203:
					Ladders.climbLadder(player, new Position(1799, 4386, 2));
					break;
				case 10204:
					Ladders.climbLadder(player, new Position(1799, 4388, 1));
					break;
				case 10205:
					Ladders.climbLadder(player, new Position(1796, 4382, 1));
					break;
				case 10206:
					Ladders.climbLadder(player, new Position(1796, 4382, 2));
					break;
				case 10207:
					Ladders.climbLadder(player, new Position(1800, 4369, 2));
					break;
				case 10208:
					Ladders.climbLadder(player, new Position(1802, 4370, 1));
					break;
				case 10209:
					Ladders.climbLadder(player, new Position(1827, 4362, 1));
					break;
				case 10210:
					Ladders.climbLadder(player, new Position(1825, 4362, 2));
					break;
				case 10211:
					Ladders.climbLadder(player, new Position(1863, 4373, 2));
					break;
				case 10212:
					Ladders.climbLadder(player, new Position(1863, 4371, 1));
					break;
				case 10213:
					Ladders.climbLadder(player, new Position(1864, 4389, 1));
					break;
				case 10214:
					Ladders.climbLadder(player, new Position(1864, 4387, 2));
					break;
				case 10215:
					Ladders.climbLadder(player, new Position(1890, 4407, 0));
					break;
				case 10216:
					Ladders.climbLadder(player, new Position(1890, 4406, 1));
					break;
				case 10217:
					Ladders.climbLadder(player, new Position(1957, 4373, 1));
					break;
				case 10218:
					Ladders.climbLadder(player, new Position(1957, 4371, 0));
					break;
				case 10219:
					Ladders.climbLadder(player, new Position(1824, 4379, 3));
					break;
				case 10220:
					Ladders.climbLadder(player, new Position(1824, 4381, 2));
					break;
				case 10221:
					Ladders.climbLadder(player, new Position(1838, 4375, 2));
					break;
				case 10222:
					Ladders.climbLadder(player, new Position(1838, 4377, 3));
					break;
				case 10223:
					Ladders.climbLadder(player, new Position(1850, 4386, 1));
					break;
				case 10224:
					Ladders.climbLadder(player, new Position(1850, 4387, 2));
					break;
				case 10225:
					Ladders.climbLadder(player, new Position(1932, 4378, 1));
					break;
				case 10226:
					Ladders.climbLadder(player, new Position(1932, 4380, 2));
					break;
				case 10227:
					if (x == 1961 && y == 4392)
						Ladders.climbLadder(player, new Position(1961, 4392, 2));
					else
						Ladders.climbLadder(player, new Position(1932, 4377, 1));
					break;
				case 10228:
					Ladders.climbLadder(player, new Position(1961, 4393, 3));
					break;
				case 10229:
					Ladders.climbLadder(player, new Position(1912, 4367, 0));
					break;
				case 10230:
					Ladders.climbLadder(player, new Position(2900, 4449, 0));
					break;
				case 9398: // deposit box
					player.getBankManager().openDepositBox();
					break;
				case 5949: // lumby jump
					if (player.getPosition().getY() > y) {
						player.teleport(new Position(3221, 9552));
					} else {
						player.teleport(new Position(3221, 9556));
					}
					break;
				case 2623: // taverly dungeon door
					if (!player.getInventory().playerHasItem(1590) && player.getPosition().getX() > 2923) {
						player.getActionSender().sendMessage("The door is locked, you need a dusty key to open it.");
					} else {
						player.getActionSender().walkThroughDoor(id, x, y, 0);
						player.getActionSender().walkTo(player.getPosition().getX() > 2923 ? -1 : 1, 0, true);
					}
					break;
				case 2631: // jailer door
					if (!player.getInventory().playerHasItem(1591) && player.getPosition().getY() > 9689) {
						player.getActionSender().sendMessage("The door is locked, you need a jail key to open it.");
					} else {
						player.getActionSender().walkThroughDoor(id, x, y, 0);
						player.getActionSender().walkTo(0, player.getPosition().getY() > 9689 ? -1 : 1, true);
					}
					break;
				case 5083:
					if (player.isBrimhavenDungeonOpen()) {
						player.fadeTeleport(new Position(2713, 9564));
						player.setBrimhavenDungeonOpen(false);
					} else {
						Dialogues.startDialogue(player, 1595);
					}
					break;
				case 5084:
					player.teleport(new Position(2744, 3152));
					break;
				case 4499:
					player.fadeTeleport(new Position(2808, 10002));
					player.getActionSender().sendMessage("You enter the cave.");
					break;
				case 4500:
					player.teleport(new Position(2796, 3615));
					break;
				case 5100:
					if (player.getPosition().getY() < y)
						player.teleport(new Position(2655, 9573));
					// player.getActionSender().walkTo(0, 7, true);
					else
						player.teleport(new Position(2655, 9566));
					// player.getActionSender().walkTo(0, -7, true);
					break;
				case 5111:
					player.teleport(new Position(2649, 9562));
					break;
				case 5110:
					player.teleport(new Position(2647, 9557));
					break;
				case 6552:
					player.getUpdateFlags().sendAnimation(645);
					if (player.getMagicBookType() == SpellBook.MODERN) {
						player.getActionSender().sendMessage("You feel a strange wisdom fill your mind...");
						player.getActionSender().sendSidebarInterface(6, 12855);
						player.setMagicBookType(SpellBook.ANCIENT);
					} else {
						player.getActionSender().sendMessage("You feel a strange drain upon your memory...");
						player.getActionSender().sendSidebarInterface(6, 1151);
						player.setMagicBookType(SpellBook.MODERN);
					}
					break;
				case 6481:
					player.getActionSender().sendMessage("You sneak into the back of the pyramid...");
					player.fadeTeleport(new Position(3229, 9310));
					break;
				case 6515:
					player.getActionSender().sendMessage("You search the sarcophagus, and sneak into it...");
					player.getActionSender().sendInterface(8677);

					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							player.teleport(new Position(3233, 2887));
							container.stop();
						}

						@Override
						public void stop() {

						}
					}, 4);

					break;
				case 1742: // gnome tree stairs
					if (x == 2445 && y == 3434) {
						player.teleport(new Position(2445, 3433, 1));
					}
					if (x == 2444 && y == 3414) {
						player.teleport(new Position(2445, 3416, 1));
					}
					if(x == 2475 && y == 3400) {
					    Ladders.climbLadder(player, new Position(2475, 3399, 1));
					}
					if(x == 2488 && y == 3407) {
					    Ladders.climbLadder(player, new Position(2489, 3409, 1));
					}
					if(x == 2485 && y == 3402) {
					    Ladders.climbLadder(player, new Position(2485, 3401, 1));
					}
					if(x == 2479 && y == 3408) {
					    Ladders.climbLadder(player, new Position(2479, 3407, 1));
					}
					break;
				case 1744: // gnome tree stairs
					if (x == 2445 && y == 3434) {
						player.teleport(new Position(2445, 3436, 0));
					}
					if (x == 2445 && y == 3415) {
						player.teleport(new Position(2445, 3413, 0));
					}
					if (x == 2475 && y == 3400) {
						player.teleport(new Position(2474, 3400, 0));
					}
					if (x == 2489 && y == 3408) {
						player.teleport(new Position(2490, 3408, 0));
					}
					if (x == 2485 && y == 3402) {
						player.teleport(new Position(2484, 3402, 0));
					}
					if (x == 2479 && y == 3408) {
						player.teleport(new Position(2478, 3408, 0));
					}
					break;
				case 10640: //altar for skull
					
					
					break;
				case 2407: // entrana dungeon door
					if (x == 2874 && y == 9750) {
						player.setStopPacket(true);
						player.getActionSender().sendMessage("You feel the world around you dissolve...");
						player.getActionSender().walkThroughDoor(id, x, y, 0);
						CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {
								player.teleport(new Position(3071, 3609));
								player.setStopPacket(false);
								container.stop();
							}

							@Override
							public void stop() {
							}
						}, 3);
					}
					break;
				case 2408: // entrana dungeon
					Dialogues.startDialogue(player, 656);
					break;
				case 2640: // prayer guild altar
					Prayer.rechargePrayerGuild(player);
					break;
				case 2641: // prayer guild ladder
					if (player.getSkill().getPlayerLevel(Skill.PRAYER) < 31) {
						player.getDialogue().sendStatement("You need a Prayer level of 31 to enter the Prayer guild.");
					} else {
						Ladders.checkClimbLadder(player, "up");
					}
					break;
				case 1804: // brass key door
					if (player.getPosition().getY() < 3450 && !player.getInventory().playerHasItem(983)) {
						player.getActionSender().sendMessage("You need a brass key to enter here.");
					} else {
						player.getActionSender().walkThroughDoor(id, x, y, 0);
						player.getActionSender().walkTo(0, player.getPosition().getY() < 3450 ? 1 : -1, true);
					}
					break;
				case 733: // slash web
					Webs.slashWeb(player, x, y, player.getEquipment().getId(Constants.WEAPON));
					break;
				case 6434: // open trapdoor
					TrapDoor.handleTrapdoor(player, id, 6435, def);
					break;
				case 1568: // open trapdoor
					TrapDoor.handleTrapdoor(player, id, 1570, def);
					break;
				case GhostsAhoy.TRAPDOOR:
					TrapDoor.handleTrapdoor(player, id, 5268, def);
					break;
				case InSearchOfTheMyreque.TRAPDOOR:
					TrapDoor.handleTrapdoor(player, id, 6435, def);
					break;
				case 1570: // climb down trapdoor
				case 5947: // climb into lumby swamp
				case 6435: // climb down trapdoor
				case 882: // climb down manhole
				case 1754: // climb down ladder
				case 9472: // climb down port sarim dungeon
				case 1759: // taverly dungeon entrance
				case 11867: // dwarven mine entrance
				case 14758: // wilderness agility spike room
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY() + 6400));
					break;
				case 1755: // exit stairs
				case 2405:
				case 6436: // climb up ladder
				case 5946: // climb out of lumby swamp
				case 1757: // climb up ladder
					if (x == 3097 && y == 9867) { // up to edgeville
						Ladders.climbLadder(player, new Position(3096, 3468, 0));
					} else {
						Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY() - 6400));
					}
					break;
				case 3432: // open canifis trapdoor
					TrapDoor.handleTrapdoor(player, id, 3433, def);
					break;
				case 3433: // climb down canifis trapdoor
					Ladders.climbLadder(player, new Position(3440, 9887, 0));
					break;
				case 2492: // portal out of rune mines
					switch (player.getRunecraftNpc()) {
					case 462:
						player.teleport(new Position(2591, 3086));
						break;
					case 553:
						player.teleport(new Position(3253, 3401));
						break;
					case 300:
						player.teleport(new Position(3101, 9571));
						break;
					case 2328:
						player.teleport(new Position(2684, 3322));
						break;
					case 171:
						player.teleport(new Position(2409, 3422));
						break;
					}
					break;
				case 3044: // tutorial furnace
					if (player.getNewComersSide().isInTutorialIslandStage()) {
						player.getDialogue().sendStatement("This is a furnace for smelting metal. To use it simply click on the", "ore you wish to smelt then click on the furnace you would like to", "use.");
						player.setClickId(0);
					}
					break;
				case 3039: // range
					player.getDialogue().sendStatement("To cook something you need to use the item you would like to cook", "on the cooking range. Do this by clicking the item in your inventory", "and then clicking the range.");
					player.setClickId(0);
					break;
				case 3566: // brimhaven swing
					int walkX = player.getPosition().getX() > 2768 ? -5 : 5;
					// player.teleport(new Position(player.objectWalkX,
					// player.objectWalkY, 3));
					CrossObstacle.walkAcross(player, 50, walkX, 0, 2, 30, 751);
					break;
				/*case 5492: // H.A.M. trapdoor
					
					  if (player.isHamTrapDoor()) { new GameObject(5491, x, y,
					  z, 0, 10, 5492, 999999); }
					 
					break; */
				case 5581: // take axe from log
					AxeInLog.pullAxeFromLog(player, x, y);
					break;
				case 8689: // Milk cow
				case 12111: // milk cow zanaris
					MilkCow.milkCow(player);
					break;
				case 2718: // operate hopper
				case 2720: //operate hopper zanaris
					FlourMill.operateHopper(player);
					break;
				case 1782: // grain bin
				case 1781: //grain bin zanaris
					FlourMill.takeFromBin(player);
					break;
				case 2609: // crandor tunnel
					player.fadeTeleport(new Position(2834, 9657, 0));
					break;
				case 2610: // karamja rope
					Ladders.climbLadder(player, new Position(2833, 3257, 0));
					break;
				case 2147:// wizard tower ladder to sedridor
					Ladders.climbLadder(player, new Position(3104, 9576, 0));
					break;
				case 11511 : // draynor manor middle to top
				case 12536:// wizard tower ladder to upstairs up to height 1
				case 11732:// fally staircase
				case 11729:// fally staircase
					Ladders.checkClimbTallStaircase(player, "up");
					break;
				case 11733:// fally staircase to height 0
				case 11731:// fally staircase
				case 9584 : // draynor manor top floor
				case 12538:// wizard tower staircase down to height 1
					Ladders.checkClimbTallStaircase(player, "down");
					break;
				case 11736:// fally staircase
				case 11734:// fally staircase
				case 1722: // staircase up
				case 1725 : // varrock in bottom to middle
				case 9470: // rimmington staircase
					Ladders.checkClimbStaircase(player, 4, 4, "up");
					break;
				case 4493: // slayer tower
				case 4495: // slayer tower
				case 11498 : // draynor manor bottom to middle
					Ladders.checkClimbStaircase(player, 5, 5, "up");
					break;
				case 1723: // staircase down
				case 1726 : // varrock inn middle to bottom
				case 11737:// fally staircase
				case 11735:// fally staircase
				case 9471: // rimmington staircase
					Ladders.checkClimbStaircase(player, 4, 4, "down");
					break;
				case 4494: // slayer tower
				case 4496: // slayer tower
				case 11499 : // draynor manor middle to bottom
					Ladders.checkClimbStaircase(player, 5, 5, "down");
					break;
				case 7057 : // single staircase
					Ladders.checkClimbStaircaseBackwards(player, 4, 4, "up");
					break;
				case 7056 : // single staircase
					Ladders.checkClimbStaircaseBackwards(player, 4, 4, "down");
					break;
				case 8744:
				case 1747:
				case 1750:
				case 9558:
				case 11739:
				case 12964:// flour mill ladder to upstairs
				case 12112: //zanaris flour mill ladder up
					if(id == 11739 && x == 3050 && y == 3355)
					{
						Ladders.climbLadder(player, new Position(3050, 3354, 2));
					}else{
						Ladders.checkClimbLadder(player, "up");
					}
					break;
				case 8746:
				case 9559:
				case 11741:
				case 9560:
				case 12966:// flour mill staircase down
				case 12113: //zanaris flourmill ladder down
				case 1746: // climb down a height ladder
				case 1749:
				case 11742:
					Ladders.checkClimbLadder(player, "down");
					break;
				case 2148:// wizard tower ladder to sedridor
					Ladders.climbLadder(player, new Position(3105, 3162, 0));
					break;
				case 881: // open manhole
					TrapDoor.handleTrapdoor(player, id, 882, def);
					break;
				case 883: // close manhole
					TrapDoor.handleTrapdoor(player, id, 881, def);
					break;
				case 2545: // open manhole west ard
					TrapDoor.handleTrapdoor(player, id, 2544, def);
					break;
				case 2543: // close manhole west ard
					TrapDoor.handleTrapdoor(player, id, 2545, def);
					break;
				case 2112: // Mining guild door entrance
					if (player.getPosition().getY() > 9756) {
						if (SkillHandler.hasRequiredLevel(player, Skill.MINING, 60, "enter the Mining Guild")) {
							player.getActionSender().walkThroughDoor(id, x, y, z);
							player.getActionSender().walkTo(0, -1, true);
						}
					} else {
						player.getActionSender().walkThroughDoor(id, x, y, z);
						player.getActionSender().walkTo(0, 1, true);
					}
					break;
				case 2113: // Mining guild ladder entrance
					if (SkillHandler.hasRequiredLevel(player, Skill.MINING, 60, "enter the Mining Guild")) {
						Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY() + 6400, 0));
					}
					break;
				case 1733:
					Ladders.checkClimbStaircaseDungeon(player, 4, 4, "down");
					break;
				case 1734:
					Ladders.checkClimbStaircaseDungeon(player, 4, 4, "up");
					break;
				case 492: // climb down karamja volcano
					Ladders.climbLadder(player, new Position(2857, 9569, 0));
					break;
				case 1764: // climb up karamja volcano
					Ladders.climbLadder(player, new Position(2856, 3167, 0));
					break;
				case 9358: // enter tzhaar cave
				    if(player.getInventory().playerHasItem(new Item(431))) { //karamjan rum
					for(Item item : player.getInventory().getItemContainer().getItems()) {
					    if(item == null) continue;
					    if(item.getId() == 431) {
						player.hit(5, HitType.NORMAL);
						player.getInventory().removeItem(new Item(431));
					    }
					}
					player.getUpdateFlags().sendGraphic(287);
					player.getActionSender().sendMessage("Your Karamjan rum explodes from the heat near the Tzhaar caves.");
					break;
				    }
				    else {
					player.fadeTeleport(new Position(2480, 5175, 0));
					break;
				    }
				case 9359: // exit tzhaar cave
					player.fadeTeleport(new Position(2862, 9571, 0));
					break;
				case 3828: // climb into kalphite tunnel
					Ladders.climbLadder(player, new Position(3484, 9509, 2));
					break;
				case 3829: // climb out of kalphite tunnel
					Ladders.climbLadder(player, new Position(3229, 3108, 0));
					break;
				case 3831: // climb into kalphite boss
					Ladders.climbLadder(player, new Position(3507, 9494, 0));
					break;
				case 3832: // climb out of kalphite boss
					Ladders.climbLadder(player, new Position(3509, 9499, 2));
					break;
				case 10168:
					int height = z > 1 ? 1 : 0;
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), height));
					break;
				case 10167:
					int height2 = z < 1 ? 1 : 2;
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), height2));
					break;
				case 1765: //Lava dungeon maze - to KBD lever
					Ladders.climbLadder(player, new Position(3069, 10255, 0));
					break;
				case 1766: //Lava dungeon maze - back up from KBD lever
					Ladders.climbLadder(player, new Position(3017, 3847, 0));
					break;
				case 1767: //Lava dungeon maze - to black dragons
					Ladders.climbLadder(player, new Position(3017, 10249, 0));
					break;
				case 1768: //Lava dungeon maze - back up from black dragons
					Ladders.climbLadder(player, new Position(3069, 3855, 0));
					break;
				case 1816: //Lava dungeon maze - lever to KBD
				    player.getUpdateFlags().sendAnimation(2140);
				    player.fadeTeleport(new Position(2271, 4680, 0));
				    break;
				case 1817: //KBD lever to Lava dungeon maze
				    player.getUpdateFlags().sendAnimation(2140);
				    player.fadeTeleport(new Position(3067, 10253, 0));
				    break;
				case 14735: //Rogue castle bottom floor staircase
				    Ladders.climbLadder(player, new Position(3282, 3936, 1));
				    break;
				case 1738: // staircase up
				    if(x == 2673 && y == 3300) {
					player.teleport(new Position(2675, 3300, 1));
				    } else {
					Ladders.checkClimbTallStaircase(player, "up");
				    }
					break;
				case 14737: //Rogue castle top staircase
					Ladders.climbLadder(player, new Position(3282, 3936, 2));
					break;
				case 1740: // staircase down
					Ladders.checkClimbTallStaircase(player, "down");
					break;
				case 14736: //Rogue castle staircase
					Dialogues.startDialogue(player, 14736);
					break;
				case 1739: // staircase mid
				case 11889: //Dark wizard tower mid stairs
				case 12537:
					Dialogues.startDialogue(player, 10007);
					break;
				case 1748: // stairs mid
				case 8745:
				case 12965:// flour mill
				case 2884:
					Dialogues.startDialogue(player, 10006);
					break;
				case 2881: // prince ali prison door
					Dialogues.startDialogue(player, 920);
					break;
				case 2882: // alkharid gate
				case 2883:
				    if(player.getQuestStage(9) >= 12) {
					player.getActionSender().walkTo(player.getPosition().getX() < 3268 ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoubleDoor(2882, 2883, 3268, 3227, 3268, 3228, 0);
					if(player.getPosition().getX() == 3268 && player.getPets().getPet() != null) {
					    int petId = player.getPets().getPet().getNpcId();
					    int itemId = player.getPets().getPetItemId();
					    player.getPets().unregisterPet();
					    player.getPets().registerPet(itemId, petId);
					}
					if(player.getPosition().getX() == 3267 && player.getPets().getPet() != null) {
					    int petId = player.getPets().getPet().getNpcId();
					    int itemId = player.getPets().getPetItemId();
					    player.getPets().unregisterPet();
					    player.getPets().registerPet(itemId, petId);
					}
					player.getActionSender().sendMessage("The guards give you a nod and a wink as you pass through the gate.");
					break;
				    }
				    else {
					Dialogues.startDialogue(player, 9999);
					break;
				    }
				case 3203:
					Dialogues.startDialogue(player, 10010);
					break;
				case 3192:
					GlobalDuelRecorder.displayScoreBoardInterface(player);
					break;
				case 2213: //bank booth
				case 11758:
				case 14367: //pc bank booth
				case 5276: //port phasmatys bank booth
				case 10517: //Nardah bank booth
				    if(player.getFightCavesWave() > 0 ) {
					player.getActionSender().sendMessage("You cannot bank with a Fight Caves wave saved! Use ::resetcaves if needed.");
					break;
				    }
				    else {
					Dialogues.startDialogue(player, 494);
					break;
				    }
				case 2491: // mine rune/pure ess
					MineEssence.startMiningEss(player);
					break;
				case 2643:
				case 4308:
				case 11601: // fire pottery
					Menus.sendSkillMenu(player, "potteryFired");
					break;
				case 3415: //stairs down to elemental workshop
					player.teleport(new Position(2716, 9888));
					break;
				case 3416: //stairs up from elemental workshop
					player.fadeTeleport(new Position(2709, 3495));
					break;
				case 4031: //shantay pass
					if(player.getPosition().getY() < 3116)
					{
						player.getActionSender().walkTo(0, player.getPosition().getY() > y ? -2 : 2, true);
						player.getActionSender().walkThroughDoor(id, x, y, z);
					}
					else
					{
						if(player.getInventory().playerHasItem(1854))
						{
							player.getInventory().removeItem(new Item(1854));
							player.getActionSender().walkTo(0, player.getPosition().getY() > y ? -2 : 2, true);
							player.getActionSender().walkThroughDoor(id, x, y, z);
						}
						else
						{
							player.getActionSender().sendMessage("You don't have a shantay pass!");
						}
					}
					break;
				//Tirannwn
				case 3944: // Arandar double doors because fixing double doors right now
				case 3945:
					if(id == 3944){
						player.getActionSender().walkThroughDoubleDoor(3944, 3945, x, y, 2384, 3334, 0);
					}else if(id == 3945){
						player.getActionSender().walkThroughDoubleDoor(3945, 3944, x, y, 2386, 3334, 0);
					}
					player.getActionSender().walkTo(0, player.getPosition().getY() > y ? -2 : 2, true);
					break;
				case 2024: //HETTYS CAULDRON
					if(player.getQuestStage(6) == 2)
					{
						player.getDialogue().sendStatement("You drink from the cauldron, it tastes horrible! You feel yourself","imbued with power.");
						player.getDialogue().endDialogue();
						player.setQuestStage(6, 3);
						QuestHandler.completeQuest(player,6);
					}
					break;
				case 2647: //crafting guild door
					if(player.getPosition().getY()+1 > y)
					{
						if (!SkillHandler.hasRequiredLevel(player, Skill.CRAFTING, 40, "enter the Crafting Guild")) {
							break;
						}
						if(player.getEquipment().getId(Constants.CHEST) != 1757 && !player.getSkillCapeHandler().wearingCape(Constants.SKILL_CRAFT))
						{
							player.getDialogue().sendStatement("You need to wear a Brown Apron to enter this guild.");
							break;
						}
					}
					player.getActionSender().walkThroughDoor(2647, x, y, z);
					player.getActionSender().walkTo(0, player.getPosition().getY()+1 > y ? -1 : 1, true);
					break;
				case 9582:// craftguild staircase up
					Ladders.checkClimbTallStaircase(player, "up");
					break;
				case 2514: //range guild door
					if(player.getPosition().getX()-1 < x)
					{
						if (player.getSkill().getLevel()[4] < 40) {
						    player.getDialogue().sendStatement("You need a Ranged level of 40 to enter the Range guild.");
						    break;
						}
					}
					player.getActionSender().walkTo(player.getPosition().getX()-1 < x ? 2 : -2, 0, true);
					break;
				case 2025: // fishing guild
					if(player.getPosition().getY()-1 < y)
					{
						if (player.getSkill().getLevel()[10] < 68) {
						    player.getDialogue().sendStatement("You need a Fishing level of 68 to enter the Fishing guild.");
						    break;
						}
					}
					player.getActionSender().walkThroughDoor(2025, x, y, z);
					player.getActionSender().walkTo(0, player.getPosition().getY()+1 > y ? -1 : 1, true);
					break;
				case 190: // gnome door
					player.getActionSender().walkThroughDoor(190, x, y, z);
					player.getActionSender().walkTo(0, player.getPosition().getY() > y ? -4 : 4, true);
					break;
				case 14922: // Piscatoris hole
					if(x == 2344 && y == 3651){
						player.teleport(new Position(2344,3655));
					}else{
						player.teleport(new Position(2344,3650));
					}
					break;
				case 14973: // Piscatoris seaweed nets
					player.getActionSender().sendMessage("You search the nets...");
					player.getUpdateFlags().sendAnimation(832);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (Misc.random(5) == 0) {
								if (player.getInventory().getItemContainer().freeSlots() > 0){
									player.getActionSender().sendMessage("You find some seaweed");
									player.getInventory().addItem(new Item(401));
								}else{
									player.getActionSender().sendMessage("Not enough space in your inventory.");
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
					this.stop();
					return;
				case 2307:
				case 2308:
					if(x >= 2997 && x <= 2998 && y == 3931)
					{
						if (!SkillHandler.hasRequiredLevel(player, Skill.AGILITY, 52, "enter this course.")) {
							break;
						}
						player.getActionSender().walkThroughDoubleDoor(2307, 2308, 2998, 3931, 2997, 3931, 0);
						player.getActionSender().walkTo(0, player.getPosition().getY()+1 > y ? -1 : 1, true);
					}
					break;
				case 245:
					if (x == 3019 && y == 3959 || x == 3017 && y == 3959) {
						Ladders.checkClimbStaircase(player, 0, 2, "up");
					}
					if (x == 3046 && y == 3227 || x == 3046 && y == 3231) {
						Ladders.checkClimbStaircase(player, 2, 0, "up");
					}
					break;
				case 246:
					if (x == 3019 && y == 3959 || x == 3017 && y == 3959) {
						Ladders.checkClimbStaircaseBackwards(player, 0, 2, "down");
					}
					if (x == 3046 && y == 3227 || x == 3046 && y == 3231) {
						Ladders.checkClimbStaircaseBackwards(player, 2, 0, "down");
					}
					break;
				case 272:
					Ladders.checkClimbLadder(player, "up");
					break;
				case 273:
					Ladders.checkClimbLadder(player, "down");
					break;
				case 10527:
				case 10529:
					player.getActionSender().walkThroughDoubleDoor(10527, 10529, 3426, 3555, 3427, 3555, 1);
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3556 ? 1 : -1, true);
					break;
				default:
					player.getActionSender().sendMessage("Nothing interesting happens.");
					break;
				}
				this.stop();
			}
		});
	}

	protected static void test(long l) {
		// TODO Auto-generated method stub
		
	}

	public static void doObjectSecondClick(final Player player) {
		final int id = player.getClickId();
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ()%4;
		final int task = player.getTask();
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					if (id == 2213 && x == 3513) { //exception
						def = new GameObjectDef(id, 10, 0, new Position(x, y, z));
					} else {
						return;
					}
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (objectPosition == null)
					return;
				if (!canInteractWithObject(player, objectPosition, def)) {
					stop();
					return;
				}
				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));
				for (Quest q : QuestHandler.getQuests()) {
					if (q.doObjectSecondClick(player, id, x, y)) {
						this.stop();
						return;
					}
				}
				if (ThieveOther.handleObjectClick2(player, id, x, y)) {
					this.stop();
					return;
				}
				/* Thieving */
				if (ThieveStalls.handleThievingStall(player, id, x, y)) {
					this.stop();
					return;
				}
				if (Farming.inspectObject(player, x, y)) {
					this.stop();
					return;
				}
				if (player.getMining().prospect(id)) {
					this.stop();
					return;
				}
				if (PickableObjects.pickObject(player, id, x, y)) {
					this.stop();
					return;
				}
				if(player.getMultiCannon().objectSecondClick(id, x, y, z)) {
					this.stop();
				    return;
				}
				switch (player.getClickId()) {
				case 2114 : // coal truck
					CoalTruck.checkCoal(player);
					break;
				case 3194 : // opened bank chest
				    if(player.getFightCavesWave() > 0 ) {
					player.getActionSender().sendMessage("You cannot bank with a Fight Caves wave saved! Use ::resetcaves if needed.");
					break;
				    }
				    else {
				    	player.getBankManager().openBank();
					break;
				    }
				case 8930: //waterbirth snow cave to daggnoth
					Ladders.climbLadder(player, new Position(2545, 10143, 0));
					break;
				case 10177 : //daggnoth to waterbirth snow cave
					Ladders.climbLadder(player, new Position(2544, 3741, 0));
					break;
				case 3433: // close canifis trapdoor
					TrapDoor.handleTrapdoor(player, id, 3432, def);
					break;
				case 1570: // close trapdoor
					TrapDoor.handleTrapdoor(player, id, 1568, def);
					break;
				case 14736: //Rogue castle staircase up
				    if (player.getPosition().getZ() == 1) {
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 2));
					break;
				    } else if (player.getPosition().getZ() == 2) {
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 3));
					break;
				    }
				break;
				case 1739: // staircase mid
				
				case 12537:
					Ladders.checkClimbTallStaircase(player, "up");
					break;
				case 4569: //lighthouse level 1 ladder:
					Ladders.climbLadder(player, new Position(2505, 3641, 2));
					break;
				case 1748: // stairs mid
				case 8745:
				case 12965:// flour mill
				case 2884: // gnome tree
				case 11889: //Dark wizard tower mid stairs up
					Ladders.checkClimbLadder(player, "up");
					break;
				case 14921: // furnace
				case 11666:
				case 9390:
				case 2781:
				case 3044:
				case 12100:
					Smelting.smeltInterface(player);
					// player.getSmithing().setUpSmelting();or
					break;
				case 2559: //Yanille dungeon picklock door
				    ThieveOther.pickLock(player, new Position(2601, 9482, 0), 2559, 82, 75, 0, player.getPosition().getY() < 9482 ? 1 : -1);
				    break;
				case 2644:
					Menus.sendSkillMenu(player, "spinning");
					break;
				case 12121: // entrana bank
				case 2213:
				case 11758:
				case 14367: //pc bank booth
				case 5276: //port phasmatys bank booth
				case 10517: //Nardah bank booth
				    if(player.getFightCavesWave() > 0 ) {
					player.getActionSender().sendMessage("You cannot bank with a Fight Caves wave saved! Use ::resetcaves if needed.");
					break;
				    }
				    else {
				    	player.getBankManager().openBank();
					break;
				    }
				case 8717:
					Menus.sendSkillMenu(player, "weaving");
					break;
				case 12111: // milk cow zanaris
					MilkCow.milkCow(player);
					break;
				}
				this.stop();
			}
		});
	}

	public static void doObjectThirdClick(final Player player) {
		final int id = player.getClickId();
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ()%4;
		final int task = player.getTask();

		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					return;
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (objectPosition == null)
					return;
				if (!canInteractWithObject(player, objectPosition, def)) {
					stop();
					return;
				}
				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));
				
				if (PestControl.handleBarricadeClicking(player, id, x, y)) {
					this.stop();
					return;
				}
				
				switch (player.getClickId()) {
				case 3194 : // opened bank chest
					final GameObject p = ObjectHandler.getInstance().getObject(id, x, y, z);
					if (p != null) {
						player.getUpdateFlags().sendAnimation(832);
						ObjectHandler.getInstance().removeObject(x, y, z, def.getType());
					}
					break;
				case 10177: // Dagganoth ladder 1st level
					Ladders.climbLadder(player, new Position(1798, 4407, 3));
					break;
				case 4569: //lighthouse level 1 ladder:
				    Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
				    break;
				case 14736: //Rogue castle staircase
				    if (player.getPosition().getZ() == 1) {
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
					break;
				    } else if (player.getPosition().getZ() == 2) {
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), 1));
					break;
				    }
				    break;
				case 1739: //staircase
				case 12537:
				case 11889: //Dark wizard tower mid stairs down
					Ladders.checkClimbTallStaircase(player, "down");
					break;
				case 1748: // stairs mid
				case 8745:
				case 12965:// flour mill
				case 2884: // gnome tree
					Ladders.checkClimbLadder(player, "down");
					break;
				default:
					player.getActionSender().sendMessage("Nothing interesting happens.");
					break;
				}
				this.stop();
			}
		});
	}

	public static void doObjectFourthClick(final Player player) {
		final int id = player.getClickId();
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ()%4;
		final int task = player.getTask();
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					return;
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (objectPosition == null)
					return;
				if (!canInteractWithObject(player, objectPosition, def)) {
					stop();
					return;
				}
				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));

				if (Farming.guide(player, x, y)) {
					this.stop();
					return;
				}
				switch (player.getClickId()) {

				}
				player.getActionSender().sendMessage("Nothing interesting happens.");
				this.stop();
			}
		});
	}

	private static void doNpcFirstClick(final Player player) {
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		if(player.getCat().unregisterCat(npc))
		{
			return;
		}
		for (int[] element : Pets.PET_IDS) {
			if (player.getClickId() == element[1]) {
				player.getPets().unregisterPet();
				return;
			}
		}
		if (npc.getNpcId() == 43 || npc.getNpcId() == 1765) {
		    if(Misc.goodDistance(player.getPosition(), npc.getPosition(), 2)) {
			player.setInteractingEntity(npc);
			NpcActions.shearSheep(player);
			return;
		    } else {
			player.walkTo(npc.getPosition(), true);
			return;
		    }
		}
		if (npc.getPlayerOwner() != null && (npc.getPlayerOwner() != player || npc.getCombatingEntity() != null)) {
			player.getActionSender().sendMessage("This npc is not interested in talking with you right now.");
			return;
		}
		if(npc.canHaveInteractingEntity()) {
		    npc.setInteractingEntity(player);
		}
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (npc.isBoothBanker()) {
				    if(Misc.goodDistance(player.getPosition(), new Position(npc.getUpdateFlags().getFace().getX(), npc.getUpdateFlags().getFace().getY(), player.getPosition().getZ()), 1)) {
					player.setInteractingEntity(npc);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					Dialogues.startDialogue(player, player.getClickId());
					Following.resetFollow(player);
					this.stop();
					return;
				    } else {
					ClippedPathFinder.getPathFinder().findRoute(player, player.getClickX(), player.getClickY(), true, 0, 0);
					return;
				    }
				}
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				final Fishing.FishingSpot spot = Fishing.FishingSpot.getSpot(npc.getDefinition().getId(), 1);
				if (player.getFishing().fish(spot, npc.getPosition())) {
					Following.resetFollow(player);
					player.setInteractingEntity(npc);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					this.stop();
					return;
				}
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true))
				{
				    if(npc.getNpcId() != 2290 && npc.getNpcId() != 2287 && npc.getNpcId() != 2289 && !npc.isBoothBanker() && npc.getNpcId() != 1461 && npc.getNpcId() != 1462 && npc.getNpcId() != 1469 && npc.getNpcId() != 1436) { //Exceptions
					return;
				    }
				}
				Following.resetFollow(player);
				npc.getNpcId();
				if(npc.canHaveInteractingEntity()) {
				    npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				}
				player.setInteractingEntity(npc);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				if (npc.getPlayerOwner() != null && npc.getPlayerOwner() != player) {
					player.getActionSender().sendMessage(npc.getDefinition().getName() + " is not interested in interacting with you right now.");
					this.stop();
					return;
				}
				for(Quest q : QuestHandler.getQuests()) {
				    if(q.doNpcClicking(player, npc)) {
				    	this.stop();
				    	return;
				    }
				}
				if (player.getSlayer().doNpcSpecialEffect(npc)) {
					this.stop();
					return;
				}
				if (AnagramsScrolls.handleNpc(player, player.getClickId())) {
					this.stop();
					return;
				}
				if (SpeakToScrolls.handleNpc(player, player.getClickId())) {
					this.stop();
					return;
				}

				if (player.getNewComersSide().isInTutorialIslandStage() && player.getNewComersSide().sendGiveItemsInstructor()) {
					this.stop();
					return;
				}
				if (player.getNewComersSide().isInTutorialIslandStage() && player.getNewComersSide().sendDialogue()) {
					this.stop();
					return;
				}
				if (Dialogues.startDialogue(player, player.getClickId())) {
					this.stop();
					return;
				}
				if (Shops.findShop(player, player.getClickId()) > 0) {
					if(player.getClickId() != 553 || player.getClickId() != 4359)
					    Dialogues.sendDialogue(player, 10008, 1, 0, player.getClickId());
					else if(player.getClickId() == 4359) {
					    Dialogues.sendDialogue(player, 10777, 1, 0, player.getClickId());
					}
					this.stop();
					return;
				}
				switch (player.getClickId()) {
					case 166 :
					case 494 :
					case 495 :
					case 496 :
					case 499 :
					case 2619:
					case 4446 : //fairy bankers
					case 1702: //ghost banker
					case 3046: //Nardah banker
					case 2271: //Rogues den banker
						npc.getUpdateFlags().faceEntity(player.getFaceIndex());
						player.setInteractingEntity(npc);
						player.getUpdateFlags().faceEntity(npc.getFaceIndex());
						if(player.getFightCavesWave() > 0 ) {
						    player.getActionSender().sendMessage("You cannot bank with a Fight Caves wave saved! Use ::resetcaves if needed.");
						    break;
						}
						else {
						    Dialogues.startDialogue(player, player.getClickId());
						}
						Following.resetFollow(player);
						break;
				}
				player.getActionSender().sendMessage("This npc is not interested in talking with you right now.");
				this.stop();
			}
		});
	}

	private static void doNpcSecondClick(final Player player) {
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (npc.isBoothBanker()) {
				    if(Misc.goodDistance(player.getPosition(), new Position(npc.getUpdateFlags().getFace().getX(), npc.getUpdateFlags().getFace().getY(), player.getPosition().getZ()), 1)) {
					player.setInteractingEntity(npc);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					player.getBankManager().openBank();
					Following.resetFollow(player);
					this.stop();
					return;
				    } else {
					ClippedPathFinder.getPathFinder().findRoute(player, player.getClickX(), player.getClickY(), true, 0, 0);
					return;
				    }
				}
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				final Fishing.FishingSpot spot = Fishing.FishingSpot.getSpot(npc.getDefinition().getId(), 2);
				if (player.getFishing().fish(spot, npc.getPosition())) {
					Following.resetFollow(player);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					this.stop();
					return;
				}
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
				    if(npc.getNpcId() != 1436) { //Exceptions
					return;
				    }
				}
				Following.resetFollow(player);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				if (ThieveNpcs.handleThieveNpc(player, npc)) {
					this.stop();
					return;
				}
				if(!npc.isBoothBanker()) {
				    npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				}
				player.setInteractingEntity(npc);
				if (Shops.openShop(player, npc.getNpcId())) {
					this.stop();
					return;
				}
				if(PiratesTreasure.handleNpcClick(player, npc.getNpcId())) {
				    this.stop();
				    return;
				}
				switch (player.getClickId()) {
				case 166 :
				case 494 :
				case 495 :
				case 496 :
				case 499 :
				case 902 : //magebank banker
				case 4296 : //warriors guild banker jade
				case 2619:
				case 1702: //ghost banker
				case 3046: //Nardah banker
				case 2271: //Rogues den banker
					npc.getUpdateFlags().faceEntity(player.getFaceIndex());
					player.setInteractingEntity(npc);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					player.getBankManager().openBank();
					Following.resetFollow(player);
					break;
				case PestControlRewardHandler.EXP_LADY:
					PestControlRewardHandler.openInterface(player);
					break;
				case 2437:
					Dialogues.sendDialogue(player, 2437, 4, 0);
					break;
				case 1595:
					Dialogues.sendDialogue(player, 1595, 3, 1);
					break;
                                case 34:
                                    ThieveNpcs.handleThieveNpc(player, npc);
                                    break;
                                case 1714:
                                    ThieveNpcs.handleThieveNpc(player, npc);
                                    break;
                                case 1715:
                                    ThieveNpcs.handleThieveNpc(player, npc);
                                    break;
				case 2824:
				case 804:
				case 1041:
					Tanning.tanningInterface(player);
					break;
				case 300:
				case 2328:
				case 462:
				case 171:
					Runecrafting.teleportRunecraft(player, npc);
					break;
				case 510: //hajedy
				    Travel.startTravel(player, Travel.Route.BRIMHAVEN_TO_SHILO);
				    break;
				case 511: //vigroy
				    Travel.startTravel(player, Travel.Route.SHILO_TO_BRIMHAVEN);
				    break;
				case 657: //entrana monk
				    Sailing.sailShip(player, Sailing.ShipRoute.ENTRANA_TO_PORT_SARIM, player.getClickId());
				    break;
				case 2728:
				case 2729:
				    Sailing.sailShip(player, Sailing.ShipRoute.PORT_SARIM_TO_ENTRANA, player.getClickId());
				    break;
				case 960:
				case 961:
				case 962:
					player.getDuelMainData().healPlayer();
					break;
				case 3021:
					player.getFarmingTools().loadInterfaces();
					break;
				case 958:
					if(player.getFightCavesWave() > 0 ) {
					    player.getActionSender().sendMessage("You cannot bank with a Fight Caves wave saved! Use ::resetcaves if needed.");
					    break;
					}
					else {
						player.getBankManager().openBank();
					    break;
					}
				case 3781: //pc squire
					Sailing.sailShip(player, Sailing.ShipRoute.PORT_SARIM_TO_PEST_CONTROL, player.getClickId());
					player.getDialogue().dontCloseInterface();	
					break;

				}
				this.stop();
			}
		});
	}

	private static void doNpcThirdClick(final Player player) {
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
					return;
				}

				Following.resetFollow(player);
				Npc npc = World.getNpcs()[player.getNpcClickIndex()];
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				if(player.getCat().interact(npc))
				{
					this.stop();
					return;
				}
				switch (player.getClickId()) {
				case 1526:
					CastlewarsExchange.OpenInterface(player);
					break;
				case 553:
					Runecrafting.teleportRunecraft(player, npc);
					break;
				case 510: //hajedy
				    Travel.startTravel(player, Travel.Route.BRIMHAVEN_TO_SHILO);
				    break;
				case 70:
				case 1596:
				case 1597:
				case 1598:
				case 1599:
					ShopManager.openShop(player, 166);
					break; // formally known as the rape shop
				// case 958 :
				// ShopManager.openShop(player, 164);
				// break;
				case 2258:
					Abyss.teleportToAbyss(player, npc);
					break;
				case 3103: //MTA Rewards Guardian
					MageRewardHandling.openInterface(player);
					break;
				case 836: //SHANTAY Buy pass
					Item SHANTAY_PASS = new Item(1854);
					int SHANTAY_PASS_PRICE = SHANTAY_PASS.getDefinition().getPrice();
					if(player.getInventory().getItemContainer().freeSlots() <= 0) {
						player.getActionSender().sendMessage("Your inventory is full.");
						break;
					}
					if(player.getInventory().playerHasItem(995, SHANTAY_PASS_PRICE))
					{
						player.getInventory().addItem(SHANTAY_PASS);
						player.getInventory().removeItem(new Item(995,SHANTAY_PASS_PRICE));
						player.getActionSender().sendMessage("You bought a Shantay pass for "+SHANTAY_PASS_PRICE+" coins.");
					}
					else
					{
						player.getActionSender().sendMessage("You need "+SHANTAY_PASS_PRICE+" coins to buy a Shantay pass.");
					}
					break;
				case 3824 :
					npc.getUpdateFlags().faceEntity(player.getFaceIndex());
					player.setInteractingEntity(npc);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					if(player.getFightCavesWave() > 0 ) {
					    player.getActionSender().sendMessage("You cannot bank with a Fight Caves wave saved! Use ::resetcaves if needed.");
					    break;
					}
					else {
						player.getBankManager().openBank();
					}
					Following.resetFollow(player);
					break;
				}
				this.stop();
			}
		});
	}

	private static void doNpcFourthClick(final Player player) {
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
					return;
				}
				Following.resetFollow(player);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				switch (player.getClickId()) {
				case 510: //hajedy
				    Travel.startTravel(player, Travel.Route.BRIMHAVEN_TO_SHILO);
				    break;
				case 494:
				case 3046: //Nardah banker
				    if(player.getFightCavesWave() > 0 ) {
					player.getActionSender().sendMessage("You cannot bank with a Fight Caves wave saved! Use ::resetcaves if needed.");
					break;
				    }
				    else {
				    	player.getBankManager().openBank();
					break;
				    }
				}
				this.stop();
			}
		});
		/*
		 * player.setWalkAction(new WalkActions(player, x - 5, y - 5, sizeX + 5,
		 * sizeY + 5, player.getClickId()) {
		 * 
		 * @Override public void execute() { if
		 * (!player.getMovementHandler().walkToAction(new Position(x, y), 1)) {
		 * return; } Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		 * player.getUpdateFlags().faceEntity(npc.getFaceIndex());
		 * npc.getUpdateFlags().faceEntity(player.getFaceIndex()); switch
		 * (player.getClickId()) { case 494 : BankManager.openBank(player);
		 * break; } actions = null; } });
		 */
	}

	private static void doItemOnObject(final Player player) {
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ();
		final int id = player.getClickId();
		final int item = player.getClickItem();
		final int task = player.getTask();
	/*	final CacheObject obj = ObjectLoader.object(id, x, y, z);
		if (obj == null && ObjectHandler.getInstance().getObject(x, y, z) == null)
			return;*/
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) {
					if (id == 4446 || id == 4447) { //exceptions
						def = new GameObjectDef(id, 10, 0, new Position(x, y, z));
					} else if (id == 4381 || id == 4382) { //exceptions
						def = new GameObjectDef(id, 11, 0, new Position(x, y, z));
					} else {
						return;
					}
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (id != 2638) {
					if (objectPosition == null)
						return;
					if (!canInteractWithObject(player, objectPosition, def)) {
						stop();
						return;
					}
				}

				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));

				/*
				 * if(obj != null){
				 * if(obj.getDefinition().getName().toLowerCase(
				 * ).contains("table")){
				 * if(player.getInventory().getItemContainer().contains(item)){
				 * player.getUpdateFlags().sendAnimation(832);
				 * player.getInventory().removeItem(new Item(item)); Item
				 * itemDropped = new Item(item); if
				 * (itemDropped.getDefinition().isStackable()) {
				 * itemDropped.setCount
				 * (player.getInventory().getItemContainer().
				 * getCount(itemDropped.getId())); } else {
				 * itemDropped.setCount(1); } if
				 * (!player.getInventory().getItemContainer
				 * ().contains(itemDropped.getId())) { this.stop(); return; } if
				 * (
				 * player.getInventory().getItemContainer().contains(itemDropped
				 * .getId())) {
				 * ItemManager.getInstance().createGroundItem(player, new
				 * Item(item, itemDropped.getCount()), new Position(x, y, z)); }
				 * } } }
				 */
				/* cooking */
				if (player.getCooking().handleInterface(item, id, x, y)) {
					this.stop();
					return;
				}
				if (player.getFillHandler().handleInterface(item, id, x, y)) {
					this.stop();
					return;
				}
				for(Quest q : QuestHandler.getQuests()) {
				    if(q.doItemOnObject(player, id, item)) {
					this.stop();
					return;
				    }
				}
				if (id == 3044 && player.getNewComersSide().isInTutorialIslandStage() && (item == 438 || item == 436)) {
					Smelting.oreOnFurnace(player, item);
					// player.getSmithing().startSmelting(1, 0);
					this.stop();
					return;
				}
				// farming
				if (Farming.prepareCrop(player, item, id, x, y)) {
					this.stop();
					return;
				}

				if (RunecraftAltars.useTaliOnRuin(player, item, id)) {
					this.stop();
					return;
				}
				if (RunecraftAltars.useTiaraOnRuin(player, item, id)) {
					this.stop();
					return;
				}
				if (Tiaras.bindTiara(player, item, id)) {
					this.stop();
					return;
				}
				if (MixingRunes.combineRunes(player, item, id)) {
					this.stop();
					return;
				}
				if(player.getMultiCannon().itemOnObject(item, player.getSlot(), id, x, y, z)) {
					this.stop();
					return;
				}
				if(BarbarianSpirits.handleItemOnObject(player, id, item, x, y)) {
					this.stop();
					return;
				}
				if(CastlewarsObjects.handleItemOnObject(player, item, player.getSlot(), id, x, y, z)) {
					this.stop();
					return;
				}
				if (item >= 3422 && item <= 3428 && id == 4090) {
					player.getInventory().removeItem(new Item(item));
					player.getInventory().addItem(new Item(item + 8));
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You put the olive oil on the fire, and turn it into sacred oil.");
					this.stop();
					return;
				}

				switch (id) {
				case 2114 : // coal truck
					if (item == 453) {
						CoalTruck.depositCoal(player);
					}
					break;
				case 172: // crystal chest
					if (item == 989) {
						CrystalChest.openCrystalChest(player);
					}
					break;
				case 2644: //Spinning wheel
				    if (item == 1737 || item == 1779 || item == 9436 || item == 6051) {
					Menus.sendSkillMenu(player, "spinning");
					break;
				    }
				case 733: // slash web
					Webs.slashWeb(player, x, y, item);
					break;
				case 1781: //grain bin zanaris
				    if(item == 1931)
					FlourMill.takeFromBin(player);
				break;
				case 12093: //evil chicken shrine
				    if((item >= 5076 && item <= 5078)) {
					    player.getActionSender().sendMessage("You offer the egg to the Evil Chicken...");
					    player.getUpdateFlags().sendAnimation(645);
					    player.setStopPacket(true);
					    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
						    if(20 >= (new Random().nextDouble() * 100.0)) {
							if(player.getSpawnedNpc() == null) {
							    player.getInventory().removeItem(new Item(item));
							    Npc chicken = new Npc(SpawnEvent.addValue(player.getCombatLevel()) + 2463);
							    NpcLoader.spawnPlayerOwnedSpecificLocationNpc(player, chicken, new Position(2452, 4476, 0), false, "Bwukkkk!");
							    player.setSpawnedNpc(chicken);
							    CombatManager.attack(chicken, player);
							    player.getActionSender().sendMessage("...The Evil Chicken refuses your offering!");
							    b.stop();
							} else {
							    player.getActionSender().sendMessage("...The Evil Chicken isn't available at the moment...");
							    b.stop();
							}
						    } else {
							player.getInventory().removeItem(new Item(item));
							double reward = new Random().nextDouble() * 2000.0;
							player.getInventory().addItem(new Item(314, ((int)reward > 500 ? (int)reward : 525)));
							player.getActionSender().sendMessage("...The Evil Chicken blesses your offering.");
							b.stop();
						    }
						}

						@Override
						public void stop() {
						    player.setStopPacket(false);
						}
					    }, 3);
					}
				break;
				case 2782:
				case 2783: // anvil
					if(id == 2782)
					{
						if(!QuestHandler.questCompleted(player, 3))
						{
							player.getDialogue().sendStatement("You don't have permission to use this.");
							break;
						}
					}
					if(item == 2366 || item == 2368)
					{
						if(DragonShieldSmith.canSmith(player))
						{
							Dialogues.startDialogue(player, 10013);
						}
						break;
					}
					if(item == 1540 || item == 11286)
					{
						if(DragonfireShieldSmithing.canSmith(player))
						{
							Dialogues.startDialogue(player, 10604);
						}
						break;
					}
					    SmithBars.smithInterface(player, item);
					break;
				case 2714: // grain hopper
				case 2716: //grain hopper zanaris
					FlourMill.putFlourInHopper(player);
					break;
				case 2638: // glory / skills fountain
					if (item == 1704 || item == 1706 || item == 1708 || item == 1710) {
						if (player.getInventory().playerHasItem(item)) 
						{
							player.getActionSender().sendMessage("You dip your amulet into the fountain...");
							player.getUpdateFlags().sendAnimation(827, 0);
							player.getInventory().replaceItemWithItem(new Item(item), new Item(1712, 1)); //old code ate glories, oops.
						}
					}
					break;
				case 3827: //kq
				case 3830:
				    if(item == 954 && player.getPosition().getZ() == 0) {
					player.getActionSender().sendMessage("As you are tying the rope, you fall down the hole!");
					player.teleport(new Position(3484, 9509, 2));
				    }
				    else if(item == 954 && player.getPosition().getZ() == 2) {
					player.getActionSender().sendMessage("As you are tying the rope, you fall down the hole!");
					player.teleport(new Position(3507, 9494, 0));
				    }
				    else {
					player.getActionSender().sendMessage("Maybe I should use a rope instead.");
				    }
				break;
				case 2142:
				    if(player.getQuestStage(10) > 2) {
				    if(item == 2132) {
					player.getActionSender().sendMessage("You slowly dip the beef into the cauldron.");
					player.getInventory().removeItem(new Item(2132));
					player.getInventory().addItem(new Item(522));
					break;
				    }
				    if(item == 2134) {
					player.getActionSender().sendMessage("You slowly dip the rat meat into the cauldron.");
					player.getInventory().removeItem(new Item(2134));
					player.getInventory().addItem(new Item(523));
					break;
				    }
				    if(item == 2136) {
					player.getActionSender().sendMessage("You slowly dip the bear meat into the cauldron.");
					player.getInventory().removeItem(new Item(2136));
					player.getInventory().addItem(new Item(524));
					break;
				    }
				    if(item == 2138) {
					player.getActionSender().sendMessage("You slowly dip your chicken into the cauldron.");
					player.getInventory().removeItem(new Item(2138));
					player.getInventory().addItem(new Item(525));
					break;
				    }
				    }
				break;
				case 2939: // legends totem
					if (item == 11126 || item == 11124 || item == 11122 || item == 11120) {
						if (player.getInventory().playerHasItem(item)) 
						{
							player.getActionSender().sendMessage("You charge the bracelet at the totem...");
							player.getUpdateFlags().sendAnimation(827, 0);
							player.getInventory().addItemToSlot(new Item(11118, 1), player.getSlot());
						}
					}
					if (item == 11113 || item == 11111 || item == 11109 || item == 11107 ) {
						if (player.getInventory().playerHasItem(item)) 
						{
							player.getActionSender().sendMessage("You charge your necklace at the totem...");
							player.getUpdateFlags().sendAnimation(827, 0);
							player.getInventory().addItemToSlot(new Item(11105, 1), player.getSlot());
						}
					}
					break;
				case 2645:// pile of sand
				case 10814: //yanille sand
				case 4373: //relleka sand
					if (item == GlassMaking.BUCKET) {
						GlassMaking.fillWithSand(player);
					}
					break;
				/*
				 * Furnaces.
				 */
				case 14921:
				case 9390:
				case 2781:
				case 2785:
				case 2966:
				case 3044:
				case 3294:
				//case 3413: //elemental workshop furnace
				case 4304:
				case 4305:
				case 6189:
				case 6190:
				case 11009:
				case 11010:
				case 11666:
				case 12100:
				case 12809:
					if (item == GlassMaking.BUCKET_OF_SAND) {
						GlassMaking.makeMoltenGlass(player);
					} else if (item == GemCrafting.GOLD_BAR) {
						GemCrafting.openInterface(player);
					} else if (item == SilverCrafting.SILVER_BAR) {
						Menus.sendSkillMenu(player, "silverCrafting");
					} else if (ItemManager.getInstance().getItemName(item).toLowerCase().endsWith("ore") && item != 668) {
						Smelting.smeltInterface(player);
						// player.getSmithing().setUpSmelting();
					} else if(item == 668) {
					    Dialogues.startDialogue(player, 10200);
					}
					break;
				case 2642:// pottery unfire
					if (item == PotteryMaking.SOFT_CLAY) {
						Menus.sendSkillMenu(player, "potteryUnfired");
					}
					break;
				case 2643:
				case 4308:
				case 11601: // fire pottery
					Menus.sendSkillMenu(player, "potteryFired");
					break;
				default:
					player.getActionSender().sendMessage("Nothing interesting happens.");
					break;
				}
				this.stop();
			}
		});
	}

	private static void doItemOnNpc(final Player player) {
		// final int x = player.getClickX();
		// final int y = player.getClickY();
		final int item = player.getClickItem();
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if ((!player.goodDistanceEntity(npc, 1) && item != 10501) || player.inEntity(npc)) {
					return;
				}
				if(!npc.isBarricade()){
					if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
						return;
					}
				}
				
				if(CastlewarsBarricades.HandleItemOnBarricades(player, item, npc)){
					this.stop();
					return;
				}
				
				for(Quest q : QuestHandler.getQuests()) {
				    if(q.doItemOnNpc(player, item, npc)) {
				    	this.stop();
				    	return;
				    }
				}
			/*	if (FamilyCrest.doItemOnNpc(player, item, npc)) {
					this.stop();
					return;
				}*/
				if (player.getCat().itemOnNpc(item, npc)) {
					this.stop();
					return;
				}
				player.getSlayer().finishOffMonster(npc, item);
				switch (player.getClickId()) { // npc ids
				case 3021:
					if (player.getFarmingTools().noteItem(item)) {
						this.stop();
						return;
					}
					break;
				}
				switch (item) { // item ids
				    case 3377:
					if (npc.getDefinition().getName().toLowerCase().contains("blamish snail")) {
					    player.getInventory().replaceItemWithItem(new Item(item), new Item(HeroesQuest.BLAMISH_SNAIL_SLIME));
					    player.getActionSender().sendMessage("You carefully collect some blamish snail slime...");
					    break;
					}
				    case 954:
					if (player.getQuestStage(9) == 10 && player.getClickId() == 919) {
					    player.getInventory().removeItem(new Item(954));
					    player.getActionSender().sendMessage("You tie up Lady Keli.");
					    npc.setDead(true);
					    CombatManager.startDeath(npc);
					    player.setQuestStage(9, 11);
					    this.stop();
					    return;
					}
				    case 1735:
					if (player.getClickId() == 43 || player.getClickId() == 1765) {
					    NpcActions.shearSheep(player);
					    this.stop();
					    return;
					}
					break;
				}
				this.stop();
			}
		});
	}
    public static boolean doMiscObjectClicking(final Player player, int object, int x, int y) {
	switch (object) {
	    case 5088: //brimhaven dungeon log
	    case 5090:
		if (!Misc.goodDistance(player.getPosition().clone(), new Position(x, y, player.getPosition().getZ()), 2)) {
		    player.walkTo(x, y, false);
		    return true;
		} else {
		    if (x == 2683) {
			Agility.crossLog(player, 2687, 9506, 6, 0, 0);
		    } else if (x == 2686) {
			Agility.crossLog(player, 2682, 9506, 6, 0, 0);
		    }
		    return true;
		}
	    case 9328: //ardy log cross
	    case 9330:
		if (!Misc.goodDistance(player.getPosition().clone(), new Position(x, y, player.getPosition().getZ()), 2)) {
		    player.walkTo(x, y, false);
		    return true;
		} else {
		    if (x == 2599) {
			Agility.crossLog(player, 2602, 3336, 6, 32, 10);
		    } else if (x == 2601) {
			Agility.crossLog(player, 2598, 3336, 6, 32, 10);
		    }
		    return true;
		}
	}
	return false;
    }
	private static boolean canInteractWithObject(Player player, Position objectPos, GameObjectDef def) {
		if(def.getId() == 2638 || def.getId() == 2142 || def.getId() == 3340 || def.getId() == 4446 || def.getId() == 4447 || def.getId() == 5015 || def.getId() == 10782 || (def.getId() >= 7272 && def.getId() <= 7287) || def.getId() == 4765 || def.getId() == 4766)
		{
			return true;
		}
		if(def.getId() == 1729 || def.getId() == 3213 || def.getId() == 3214) {
			return Misc.goodDistance(player.getPosition(), def.getPosition(), 4);
		}
		if(def.getId() == 2290) {
			return Misc.goodDistance(player.getPosition(), def.getPosition(), 3);
		}
		if(def.getId() == 5061 || def.getId() == 5060) {
			return Misc.goodDistance(player.getPosition(), objectPos, 2);
		}
		if(def.getId() == 5003 || (def.getId() >= 137 && def.getId() <= 145)) {
			return Misc.goodDistance(player.getPosition(), new Position(objectPos.getX(), objectPos.getY(), player.getPosition().getZ()), 1);
		}
		if(def.getId() == 5002) {
		    return Misc.goodDistance(player.getPosition().clone().modifyZ(player.getPosition().getZ()%4), def.getPosition(), 1);
		}
		Rangable.removeObjectAndClip(def.getId(), def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ(), def.getFace(), def.getType());
		boolean canInteract = Misc.checkClip(player.getPosition().clone().modifyZ(player.getPosition().getZ()%4), objectPos, false);
		Rangable.addObject(def.getId(), def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ(), def.getFace(), def.getType(), true);
		return canInteract;
	}

	public static void setActions(Actions actions) {
		WalkToActionHandler.actions = actions;
	}

	public static Actions getActions() {
		return actions;
	}

	public static enum Actions {

		OBJECT_FIRST_CLICK, OBJECT_SECOND_CLICK, OBJECT_THIRD_CLICK, OBJECT_FOURTH_CLICK, ITEM_ON_OBJECT,

		NPC_FIRST_CLICK, ITEM_ON_NPC, NPC_SECOND_CLICK, NPC_THIRD_CLICK, NPC_FOURTH_CLICK
	}

}
