package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
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
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.minigames.castlewars.*;
import com.rs2.model.content.minigames.duelarena.GlobalDuelRecorder;
import com.rs2.model.content.minigames.pestcontrol.*;
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
import com.rs2.model.content.skills.Woodcutting.Canoe;
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
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.transport.Sailing;

public class WalkToActionHandler {

	private static Actions actions = Actions.OBJECT_FIRST_CLICK;
	private int test;
	
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
		final int z = player.getClickZ();
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
					if (id == 2142 || id == 2297 || id == 2311 || id == 2294 || id == 2295 || id == 2296 || id == 9293  || id == 9328 || id == 2834 || id == 9330 || id == 9322 || id == 9324 || id == 2332 || id == 3933 || (id == 3203 || id == 4616 || id == 4615) || (id == 2213 && x == 3513) || (id == 356 && y == 3507) || GameObjectData.forId(id).getName().toLowerCase().contains("gangplank")) { //exceptions
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
			/*	if (id == 4031)
					player.getActionSender().walkTo(0, player.getPosition().getY() > y ? -2 : 2, true);
					*/
				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));

				if (Barrows.barrowsObject(player, id)) {
					this.stop();
					return;
				}
		        if (Barrows.handleObjectClicking(player, id, x, y)) {
                    this.stop();
                    return;
                }
				if (player.getSlayer().handleObjects(id, x, y)) {
					this.stop();
					return;
				}
				if (ObeliskTick.clickObelisk(id)) {
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
					Prayer.rechargePrayer(player);
					this.stop();
					return;
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
				if (id == 2551 || id == 2550 || id == 2556 || id == 2558 || id == 2557 || id == 2554) {
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
				if(Canoe.canoeStation(player, id))
				{
					this.stop();
					return;
				}
				if(Canoe.useCanoe(player, id))
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
				if (objectName.contains("crate")) {
					player.getActionSender().sendMessage("You search the crate...");
					player.getUpdateFlags().sendAnimation(832);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (Misc.random(99) == 0) {
								Item[] rewards = {new Item(995, 10), new Item(686), new Item(687), new Item(689), new Item(690), new Item(697), new Item(1059), new Item(1061)};
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
				case 2114 : // coal truck
					CoalTruck.withdrawCoal(player);
					break;
				case 2693 : // shantay bank chest
				case 4483 : // castle wars bank chest
					BankManager.openBank(player);
					break;
				case 3193 : // closed bank chest
					player.getUpdateFlags().sendAnimation(832);
					new GameObject(3194, x, y, z, def.getFace(), def.getType(), id, 500);
					break;
				case 1293 : // spirit tree
				case 1294 :
					Dialogues.startDialogue(player, 10011);
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
				case 11844: //falador wall shortcut
				    if(player.getPosition().getX() ==  2934)
					Agility.climbOver(player, 2936, 3355, 11, 5);
				    else if(player.getPosition().getX() ==  2936)
					Agility.climbOver(player, 2934, 3355, 11, 5); 
				break;
				case 9301:
				case 9302: //watchtower tunnel
				    if(y == 3111)
					Agility.crossObstacle(player, 2575, 3107, 844, 6, 16, 7);
				    else if(y == 3108)
					Agility.crossObstacle(player, 2575, 3112, 844, 6, 16, 7);
				break;
				case 2296: //coal trucks log
				    if(x == 2599)
					Agility.crossLog(player, 2603, 3477, 8, 20, 10);
				    else if(x == 2602)
					Agility.crossLog(player, 2598, 3477, 8, 20, 10);
				break;
				case 2332: //karamja log
				    if(x == 2909)
					Agility.crossLog(player, 2906, 3049, 8, 20, 5);
				    else if(x == 2907)
					Agility.crossLog(player, 2910, 3049, 6, 8, 5);
				break;
				case 9311:
				case 9312: //varrock underwall tunnel
				    if(x == 3189)
					Agility.crossObstacle(player, 3193, 3493, 844, 6, 21, 8);
				    else if(x == 3192)
					Agility.crossObstacle(player, 3188, 3493, 844, 6, 21, 8);
				break;
				case 9309:
				case 9310: //fally underwall tunnel
				    if(y == 3312)
					Agility.crossObstacle(player, 2948, 3309, 844, 6, 26, 8);
				    else if(y == 3310)
					Agility.crossObstacle(player, 2948, 3313, 844, 6, 26, 8);
				break;
				case 9315: //jumping rocks to champs guild from draynor manor
				    if(player.getPosition().getX() == 3149)
					Agility.crossObstacle(player, 3150, 3363, 769, 5, 29, 9);
				    else if(player.getPosition().getX() == 3150)
					Agility.crossObstacle(player, 3151, 3363, 769, 5, 29, 9);
				    else if(player.getPosition().getX() == 3151 && x == 3152)
					Agility.crossObstacle(player, 3152, 3363, 769, 5, 29, 9);
				    else if(player.getPosition().getX() == 3151 && x == 3150)
					Agility.crossObstacle(player, 3150, 3363, 769, 5, 29, 9);
				    else if(player.getPosition().getX() == 3152 && x == 3153)
					Agility.crossObstacle(player, 3153, 3363, 769, 5, 29, 9);
				    else if(player.getPosition().getX() == 3152 && x == 3151)
					Agility.crossObstacle(player, 3151, 3363, 769, 5, 29, 9);
				    else if(player.getPosition().getX() == 3153)
					Agility.crossObstacle(player, 3152, 3363, 769, 5, 29, 9);
				    else if(player.getPosition().getX() == 3154)
					Agility.crossObstacle(player, 3153, 3363, 769, 5, 29, 9);
				break;
				case 9328: //ardy log cross
				    if(player.getPosition().getX() == 2602) //x == 2601)
					Agility.crossLog(player, 2602, 3336, 6, 32, 10);
				break;
				case 9330: //ardy log cross
				    if(player.getPosition().getX() == 2598) //x == 2599)
					Agility.crossLog(player, 2598, 3336, 6, 32, 10);
				break;
				case 9316:
				case 9317: //grand tree nw rocks
				    if(x == 2487)
					Agility.crossObstacle(player, 2489, 3521, 844, 10, 36, 13);
				    else if(x == 2489)
					Agility.crossObstacle(player, 2486, 3515, 844, 10, 36, 13);
				break;
				case 9331:
				case 9332: //ak mine rockclimb
				    if(x == 3303)
					Agility.crossObstacle(player, 3306, 3315, 844, 6, 37, 13);
				    else if(x == 3305)
					Agility.crossObstacle(player, 3302, 3315, 820, 6, 37, 13);
				break;
				case 9325: //dwarf mine crevice
				    if(x == 3029)
					Agility.crossObstacle(player, 3035, 9806, 844, 8, 41, 15);
				    else if(x == 3034)
					Agility.crossObstacle(player, 3028, 9806, 844, 8, 41, 15);
				break;
				case 9322: 
				case 9324: //frem log cross
				    if(y == 3595)
					Agility.crossLog(player, 2722, 3592, 6, 47, 20);
				    else if(y == 3593)
					Agility.crossLog(player, 2722, 3596, 6, 47, 20);
				break;
				case 9295: //moss giant pipe
				    if(x == 3150)
					Agility.crawlPipe(player, 3155, 9906, 6, 48, 20);
				    else if(x == 3153)
					Agility.crawlPipe(player, 3149, 9906, 6, 48, 20);
				break;
				case 9319:
				case 9320: //slayer tower spike chains
				    if(x == 3422 && z == 0)
					Agility.climbBranch(player, 3423, 3550, 1, 59, 25);
				    else if(x == 3422 && z == 1)
					Agility.climbBranch(player, 3423, 3550, 0, 59, 25);
				    else if(x == 3447 && z == 1)
					Agility.climbBranch(player, 3448, 3576, 2, 71, 47);
				    else if(x == 3447 && z == 2)
					Agility.climbBranch(player, 3448, 3576, 1, 71, 47);
				break;
				case 9321: //slayer dungeon crevice 61
				    if(x == 2734)
					Agility.crossObstacle(player, 2730, 10008, 844, 6, 61, 35);
				    else if(x == 2731)
					Agility.crossObstacle(player, 2735, 10008, 844, 6, 61, 35);
				break;
				case 9334:
				case 9335:
				case 9336:
				case 9337: //canifis/morytania shortcut 64
				    if(x == 3424 && player.getPosition().getX() == 3423)
					Agility.crossObstacle(player, 3424, 3476, 756, 4, 64, 35);
				    else if(x == 3424 && player.getPosition().getX() == 3424)
					Agility.crossObstacle(player, 3423, 3476, 756, 4, 64, 35);
				    else if(x == 3425 && y == 3476)
					Agility.crossObstacle(player, 3427, 3476, 820, 6, 64, 35);
				    else if(x == 3426)
					Agility.crossObstacle(player, 3424, 3476, 820, 6, 64, 35);
				    else if(x == 3425 && player.getPosition().getY() == 3483)
					Agility.crossObstacle(player, 3425, 3484, 756, 4, 64, 35);
				    else if(x == 3425 && player.getPosition().getY() == 3484)
					Agility.crossObstacle(player, 3425, 3483, 756, 4, 64, 35);
				break;
				case 9293: //taverly dung blue drag pipe
				    if(x == 2887)
					Agility.crawlPipe(player, 2892, 9799, 6, 70, 47);
				    else if(player.getPosition().getX() == 2891)
					Agility.crawlPipe(player, 2886, 9799, 6, 70, 47);
				    break;
				case 2333:
				case 2334:
				case 2335: //shilo rock jump 74
				    if(player.getPosition().getY() == 2948)
					Agility.crossObstacle(player, 2925, 2949, 769, 5, 74, 50);
				    else if(player.getPosition().getY() == 2950)
					Agility.crossObstacle(player, 2925, 2949, 769, 5, 74, 50);
				    else if(player.getPosition().getY() == 2947)
					Agility.crossObstacle(player, 2925, 2948, 769, 5, 74, 50);
				    else if(player.getPosition().getY() == 2949 && y == 2948)
					Agility.crossObstacle(player, 2925, 2948, 769, 5, 74, 50);
				    else if(player.getPosition().getY() == 2949 && y == 2950)
					Agility.crossObstacle(player, 2925, 2950, 769, 5, 74, 50);
				    else if(player.getPosition().getY() == 2951)
					Agility.crossObstacle(player, 2925, 2950, 769, 5, 74, 50);
				break;
				case 9326: //slayer dungeon 81 floor
				    if (x == 2774 && player.getPosition().getX() == 2775)
					Agility.crossObstacle(player, 2773, 10003, 769, 5, 81, 56);
				    else if(x == 2774 && player.getPosition().getX() == 2773)
					Agility.crossObstacle(player, 2775, 10003, 769, 5, 81, 56);
				    else if(x == 2769 && player.getPosition().getX() == 2770)
					Agility.crossObstacle(player, 2768, 10002, 769, 5, 81, 56);
				    else if(x == 2769 && player.getPosition().getX() == 2768 )
					Agility.crossObstacle(player, 2770, 10002, 769, 5, 81, 56);
				    break;
				case 1805 : // champions guild
					player.getActionSender().walkTo(0, player.getPosition().getY() > 3362 ? -1 : 1, true);
					player.getActionSender().walkThroughDoor(id, x, y, z);
					break;
				case 4624 : // burthorpe staircase
					player.teleport(new Position(2208, 4938));
					break;
				case 5959 : // magebank lever
					player.teleport(new Position(2539, 4712));
					break;
				case 9706 : //mage arena lever
				case 9707 :
					if(player.getCombatingEntity() != null)
					    player.getActionSender().sendMessage("You cannot use this in combat!");
					else if(player.getCombatingEntity() == null && y == 3956)
					    player.teleport(new Position(3106, 3952, 0));
					else if(player.getCombatingEntity() == null && y == 3952)
					    player.teleport(new Position(3105, 3956, 0));
				break;
				case 2878: //magebank pool - cadillac
				    if(player.getMageArenaStage() >= 2) {
					player.getActionSender().sendMessage("The world starts to shimmer around you...");
					player.teleport(new Position(2509, 4690));
					break;
				    }
				    else {
					player.getActionSender().sendMessage("The water feels cool, yet seems to repel your presence.");
					break;
				    }
				case 2879: //magebank pool2 - cadillac
				    if(player.getMageArenaStage() >= 2) {
					player.getActionSender().sendMessage("The world starts to shimmer around you...");
					player.teleport(new Position(2541, 4718));
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
					player.teleport(new Position(2561, 3311)); //3153 3923
					break;
				case 1814 : // ardy lever
					player.teleport(new Position(3153, 3923)); //3153 3923
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
				case 4622 : // games room staircase
					player.teleport(new Position(2899, 3565));
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
				case 1967 :
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3492 ? 2 : -2, true);
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
					//player.movePlayer(player.getPosition());
					player.getUpdateFlags().sendAnimation(754);
					CrossObstacle.setForceMovement(player, player.getPosition().getX() < 2662 ? 1 : -1, 0, 1, 80, 2, true, 0, 0);
					break;
				case 2186 :
					//player.movePlayer(player.getPosition());
					player.getUpdateFlags().sendAnimation(754);
					CrossObstacle.setForceMovement(player, 0, player.getPosition().getY() < 3161 ? 1 : -1, 1, 80, 2, true, 0, 0);
					break;
				case 5259 : // port phays entrance
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3508 ? 1 : -1, true);
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
					Ladders.climbLadder(player, new Position(2899, 4449, 0));
					break;
				case 9398: // deposit box
					BankManager.openDepositBox(player);
					break;
				case 10596: // enter icy cavern
					player.teleport(new Position(3056, 9555));
					break;
				case 10595: // exit icy cavern
					player.teleport(new Position(3056, 9562));
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
				case 5088:
					if (x > player.getPosition().getX() && y == player.getPosition().getY())
						player.getActionSender().walkTo(-5, 0, true);
					else if (x < player.getPosition().getX() && y == player.getPosition().getY())
						player.getActionSender().walkTo(5, 0, true);
					break;
				case 6552:
					player.getUpdateFlags().sendAnimation(645);
					if (player.getMagicBookType() == SpellBook.MODERN) {
						player.getActionSender().sendMessage(" You feel a strange wisdom fill your mid...");
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
					break;
				case 1744: // gnome tree stairs
					if (x == 2445 && y == 3434) {
						player.teleport(new Position(2445, 3436, 0));
					}
					if (x == 2445 && y == 3415) {
						player.teleport(new Position(2445, 3413, 0));
					}
					break;
				case 2406: // zanaris shed door
					if (player.getEquipment().getId(Constants.WEAPON) == 772) {
						if (player.getTeleportation().attemptTeleportJewellery(new Position(2452, 4473, 0))) {
							player.getActionSender().sendMessage("You are suddenly teleported away.");
						}
					} else {
						player.getActionSender().sendMessage("The door seems to be locked.");
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
					if (player.getSkill().getLevel()[5] < 31) {
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
				case 2606: // wall in karamja dungeon
					player.getActionSender().walkThroughDoor(id, x, y, 0);
					if (player.getPosition().getY() < 9600) {
						player.getActionSender().walkTo(0, 1, true);
					} else {
						player.getActionSender().walkTo(0, -1, true);
					}
					break;
				case 3443: // holy barrier to canifis
					player.teleport(new Position(3423, 3485, 0));
					player.getActionSender().sendMessage("You step through the holy barrier and appear in Canifis.");
					break;
				case 6434: // open trapdoor
					TrapDoor.handleTrapdoor(player, id, 6435, def);
					break;
				case 1568: // open trapdoor
					TrapDoor.handleTrapdoor(player, id, 1570, def);
					break;
				case 1570: // climb down trapdoor
				case 5947: // climb into lumby swamp
				case 6435: // climb down trapdoor
				case 882: // climb down manhole
				case 1754: // climb down ladder
				case 9472: // climb down port sarim dungeon
				case 1759: // taverly dungeon entrance
				case 11867: // dwarven mine entrance
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
					case 844:
						player.teleport(new Position(2684, 3322));
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
					MilkCow.milkCow(player);
					break;
				case 2718: // operate hopper
					FlourMill.operateHopper(player);
					break;
				case 1782: // grain bin
					FlourMill.takeFromBin(player);
					break;
				case 2609: // crandor tunnel
					Ladders.climbLadder(player, new Position(2834, 9657, 0));
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
					Ladders.checkClimbLadder(player, "up");
					break;
				case 8746:
				case 9559:
				case 11741:
				case 9560:
				case 12966:// flour mill staircase down
				case 1746: // climb down a height ladder
				case 1749:
				case 11742:
					Ladders.checkClimbLadder(player, "down");
					break;
				case 2148:// wizard tower ladder to sedridor
					Ladders.climbLadder(player, new Position(3105, 3162, 0));
					break;
				case 881: // open manhold
					TrapDoor.handleTrapdoor(player, id, 882, def);
					break;
				case 883: // close manhold
					TrapDoor.handleTrapdoor(player, id, 881, def);
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
					player.sendTeleport(2480, 5175, 0);
					break;
				case 9359: // exit tzhaar cave
					player.sendTeleport(2862, 9571, 0);
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
				case 1765: // climb into KBD cave
					Ladders.climbLadder(player, new Position(2257, 4695, 0));
					break;
				case 1738: // staircase up
                    if(x == 2673 && y == 3300) {
                        player.teleport(new Position(2675, 3300, 1));
                    }else{
                    	Ladders.checkClimbTallStaircase(player, "up");
                    }
					break;
				case 1740: // staircase down
					Ladders.checkClimbTallStaircase(player, "down");
					break;
				case 1739: // staircase mid
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
					Dialogues.startDialogue(player, 494);
					break;
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
					player.teleport(new Position(2709, 3498));
					break;
				case 3389:
					break;
				case 3390: //Odd looking wall
				case 3391:
					break;
				case 3406: //Water Wheel lever in Elemental Workshop
					player.getActionSender().animateObject(2719, 9907, 0, 472);
					break;
				case 3409: //Bellows in Elemental Workshop

					break;
		        //case 1747:
		        //case 1757:
				case 4411:
	            case 4415:
	            case 4417:
	            case 4418:
	            case 4419:
	            case 4420:
	            case 4469:
	            case 4470:
	            case 4911:
	            case 4912:
	            case 4437:
	            case 6281:
	            case 6280:
	            case 4472:
	            case 4471:
	            case 4406:
	            case 4407:
	            case 4458:
	            case 4902:
	            case 4903:
	            case 4900:
	            case 4901:
	            case 4461:
	            case 4463:
	            case 4464:
	            case 4377:
	            case 4378:
	            case 4408: //guthix portal
	            case 4388: //zammy portal
	            case 4387: //sara portal
	            case 4390: //zammy portal out of lobby
	            case 4389: //sara portal out of lobby
	                CastlewarsObjects.handleObject(player, id, x, y);
				break;
	            /*case 1568:
	                if (x== 3097 && y == 3468) {
						//c.getPA().movePlayer(3097, 9868, 0);
	                } else {
	                    CastleWarObjects.handleObject(player, x, y, z);
	                }
	                break;*/
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
		final int z = player.getClickZ();
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
				switch (player.getClickId()) {
				case 2114 : // coal truck
					CoalTruck.checkCoal(player);
					break;
				case 3194 : // opened bank chest
					BankManager.openBank(player);
					break;
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
				case 1739: // staircase mid
				case 12537:
					Ladders.checkClimbTallStaircase(player, "up");
					break;
				case 1748: // stairs mid
				case 8745:
				case 12965:// flour mill
				case 2884: // gnome tree
					Ladders.checkClimbLadder(player, "up");
					break;
				case 14921: // furnace
				case 11666:
				case 9390:
				case 2781:
				case 3044:
					Smelting.smeltInterface(player);
					// player.getSmithing().setUpSmelting();or
					break;
				case 2644:
					Menus.sendSkillMenu(player, "spinning");
					break;
				case 12121: // entrana bank
				case 2213:
				case 11758:
				case 14367: //pc bank booth
					BankManager.openBank(player);
					break;
				case 8717:
					Menus.sendSkillMenu(player, "weaving");
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
		final int z = player.getClickZ();
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
				case 1739: //staircase
				case 12537:
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
		final int z = player.getClickZ();
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
		for (int[] element : Pets.PET_IDS) {
			if (player.getClickId() == element[1]) {
				player.getPets().unregisterPet();
				return;
			}
		}
		if (npc.getPlayerOwner() != null && (npc.getPlayerOwner() != player || npc.getCombatingEntity() != null)) {
			player.getActionSender().sendMessage("This npc is not interested in talking with you right now.");
			return;
		}
		npc.setInteractingEntity(player);
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (npc.isBoothBanker()) {
					if (npc.getCorrectStandPosition(player.getPosition(), 2)) {
						npc.getUpdateFlags().faceEntity(player.getFaceIndex());
						player.setInteractingEntity(npc);
						player.getUpdateFlags().faceEntity(npc.getFaceIndex());
						Dialogues.startDialogue(player, player.getClickId());
						Following.resetFollow(player);
						this.stop();
					}
					return;
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
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
					return;
				}
				Following.resetFollow(player);
				npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				player.setInteractingEntity(npc);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				if (npc.getPlayerOwner() != null && npc.getPlayerOwner() != player) {
					player.getActionSender().sendMessage(npc.getDefinition().getName() + " is not interested in interacting with you right now.");
					this.stop();
					return;
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
					if(player.getClickId() != 553)
					Dialogues.sendDialogue(player, 10008, 1, 0, player.getClickId());
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
						npc.getUpdateFlags().faceEntity(player.getFaceIndex());
						player.setInteractingEntity(npc);
						player.getUpdateFlags().faceEntity(npc.getFaceIndex());
						Dialogues.startDialogue(player, player.getClickId());
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
					if (npc.getCorrectStandPosition(player.getPosition(), 2)) {
						npc.getUpdateFlags().faceEntity(player.getFaceIndex());
						player.setInteractingEntity(npc);
						player.getUpdateFlags().faceEntity(npc.getFaceIndex());
						BankManager.openBank(player);
						Following.resetFollow(player);
						this.stop();
					}
					return;
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
					return;
				}
				Following.resetFollow(player);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				if (ThieveNpcs.handleThieveNpc(player, npc)) {
					this.stop();
					return;
				}
				npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				player.setInteractingEntity(npc);
				if (Shops.openShop(player, npc.getNpcId())) {
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
					npc.getUpdateFlags().faceEntity(player.getFaceIndex());
					player.setInteractingEntity(npc);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					BankManager.openBank(player);
					Following.resetFollow(player);
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
				case 844:
				case 462:
				case 171:
					Runecrafting.teleportRunecraft(player, npc);
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
					BankManager.openBank(player);
					break;
				case 3781: //pc squire
					Sailing.sailShip(player, Sailing.ShipRoute.PORT_SARIM_TO_PEST_CONTROL);
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
				switch (player.getClickId()) {
				case 553:
					Runecrafting.teleportRunecraft(player, npc);
					break;
				case 70:
				case 1596:
				case 1597:
				case 1598:
				case 1599:
					ShopManager.openShop(player, 166);
					break;
				// case 958 :
				// ShopManager.openShop(player, 164);
				// break;
				case 2258:
					Abyss.teleportToAbyss(player, npc);
					break;
				case 836: //SHANTAY Buy pass
					Item SHANTAY_PASS = new Item(1854);
					int SHANTAY_PASS_PRICE = SHANTAY_PASS.getDefinition().getPrice();
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
					BankManager.openBank(player);
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
				case 494:
					BankManager.openBank(player);
					break;
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
		final CacheObject obj = ObjectLoader.object(id, x, y, z);
		if (obj == null && ObjectHandler.getInstance().getObject(x, y, z) == null)
			return;
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
				// smithing

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
				if (Tiaras.bindTiara(player, item, id)) {
					this.stop();
					return;
				}
				if (MixingRunes.combineRunes(player, item, id)) {
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
				case 733: // slash web
					Webs.slashWeb(player, x, y, item);
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
					    SmithBars.smithInterface(player, item);
					break;
				case 2714: // grain hopper
					FlourMill.putFlourInHopper(player);
					break;
				case 2638: // glory / skills fountain
					if (item == 1704 || item == 1706 || item == 1708 || item == 1710) {
						if (player.getInventory().playerHasItem(item)) 
						{
							player.getActionSender().sendMessage("You dip your amulet into the fountain...");
							player.getUpdateFlags().sendAnimation(827, 0);
							player.getInventory().addItemToSlot(new Item(1712, 1), player.getSlot());
						}
					}
					if (item == 11113 || item == 11111 || item == 11109 || item == 11107 ) {
						if (player.getInventory().playerHasItem(item)) 
						{
							player.getActionSender().sendMessage("You dip your amulet into the fountain...");
							player.getUpdateFlags().sendAnimation(827, 0);
							player.getInventory().addItemToSlot(new Item(11105, 1), player.getSlot());
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
					break;
				case 2645:// pile of sand
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
				case 3413:
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
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
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
				case 954:
				    if(player.getQuestStage(9) == 10 && player.getClickId() == 919) {
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

	private static boolean canInteractWithObject(Player player, Position objectPos, GameObjectDef def) {
		if (def.getId() == 2638) {
			return true;
		}
		if(def.getId() == 2142) {
		    return true;
		}
		Rangable.removeObjectAndClip(def.getId(), def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ(), def.getFace(), def.getType());
		boolean canInteract = Misc.checkClip(player.getPosition(), objectPos, false);
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
