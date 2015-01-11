package com.rs2.model.content.quests.MonkeyMadness;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.skills.thieving.ThieveOther;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.objects.functions.TrapDoor;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class ApeAtoll {

    private static final int[] MAIN_COORDS = {2755, 2784};

    public static final int PYRE_TRAPDOOR = 4879;
    public static final int PYRE_TRAPDOOR_OPEN = 4880;
    public static final int DENTURES_TRAPDOOR = 4712;
    public static final int DENTURES_TRAPDOOR_OPEN = 4713;
    public static final int DENTURES_ROPE_WEST = 4889;
    public static final int DENTURES_ROPE_EAST = 4728;
    public static final int PYRE_ROPE = 4881;
    public static final int GORILLA_STATUE = 4859;
    public static final int TEMPLE_STAIRS_DOWN = 4755;
    public static final int TEMPLE_STAIRS_UP = 4756;
    public static final int BANANA_TREE = 4749;
    public static final int PINEAPPLE_PLANT = 4827;
    public static final int DUNGEON_LADDER_UP = 4781;
    public static final int DUNGEON_LADDER_DOWN = 4780;
    public static final int BAMBOO_LADDER = 4743;
    public static final int BAMBOO_LADDER_DOWN = 4744;
    public static final int END_OF_BRIDGE = 4745;
    
    public static final Position INSIDE_VILLAGE = new Position(2515,3162,0);
    public static final Position APE_ATOLL_LANDING = new Position(2805, 2707, 0);
    public static final Position JUNGLE_DEMON_POS = new Position(2715, 9161, 1);
    public static final Position HANGAR = new Position(2649, 4518, 0);
    public static final Position BELOW_PYRES = new Position(2807, 9201, 0);
    public static final Position UP_FROM_PYRES = new Position(2806, 2785, 0);
    public static final Position START_OF_DUNGEON = new Position(2764, 9103, 0);
    public static final Position END_OF_DUNGEON = new Position(2805, 9142, 0);
    public static final Position CRASH_ISLAND = new Position(2892, 2727, 0);
    
    public static final MinigameAreas.Area JAIL = new MinigameAreas.Area(2764, 2776, 2792, 2804, 0);
    public static final MinigameAreas.Area DUNGEON = new MinigameAreas.Area(2682, 2822, 9087, 9155, 0);
    public static final MinigameAreas.Area END_DUNGEON = new MinigameAreas.Area(2800, 2810, 9139, 9149, 0);

    public enum GreeGreeData {
	SMALL_NINJA_GREEGREE(4024, 3179, 1480, 1386, 1380, 1381),
	MEDIUM_NINJA_GREEGREE(4025, 3180, 1481, 1386, 1380, 1381),
	GORILLA_GREEGREE(4026, 3181, 1482, 1401, 1399, 1400),
	BEARED_GORILLA_GREEGREE(4027, 3182, 1483, 1401, 1399, 1400),
	MYSTERIOUS_GREEGREE(4028, -1, 1484, 1401, 1399, 1400),
	SMALL_ZOMBIE_GREEGREE(4029, 3186, 1485, 1386, 1382, -1),
	LARGE_ZOMBIE_GREEGREE(4030, 3185, 1486, 1386, 1382, -1),
	MONKEY_GREEGREE(4031, 3183, 1487, 222, 219, 219);

	private int itemId;
	private int bonesId;
	private int transformId;
	private int standAnim;
	private int walkAnim;
	private int runAnim;

	GreeGreeData(int itemId, int bonesId, int transformId, int standAnim, int walkAnim, int runAnim) {
	    this.itemId = itemId;
	    this.bonesId = bonesId;
	    this.transformId = transformId;
	    this.standAnim = standAnim;
	    this.walkAnim = walkAnim;
	    this.runAnim = runAnim;
	}

	public static GreeGreeData forItemId(int itemId) {
	    for (GreeGreeData g : GreeGreeData.values()) {
		if (itemId != -1 && g.itemId == itemId) {
		    return g;
		}
	    }
	    return null;
	}

	public static int talismanForBones(int itemId) {
	    for (GreeGreeData g : GreeGreeData.values()) {
		if (g.bonesId != -1 && g.bonesId == itemId) {
		    return g.itemId;
		}
	    }
	    return -1;
	}

	public int getItemId() {
	    return this.itemId;
	}

	public int getBonesId() {
	    return this.bonesId;
	}

	public int getTransformId() {
	    return this.transformId;
	}

	public int getStandAnim() {
	    return this.standAnim;
	}

	public int getWalkAnim() {
	    return this.walkAnim;
	}
	
	public int getRunAnim() {
	    return this.runAnim;
	}

    }
    
    public enum DungeonSpikesData {
	SPIKES_1(new Position(2793, 9115, 0)),
	SPIKES_2(new Position(2809, 9111, 0)),
	SPIKES_3(new Position(2740, 9135, 0)),
	SPIKES_4(new Position(2715, 9137, 0)),
	SPIKES_5(new Position(2712, 9136, 0)),
	SPIKES_6(new Position(2708, 9136, 0)),
	SPIKES_7(new Position(2709, 9134, 0)),
	SPIKES_8(new Position(2715, 9134, 0)),
	SPIKES_9(new Position(2717, 9133, 0)),
	SPIKES_10(new Position(2712, 9133, 0)),
	SPIKES_11(new Position(2708, 9132, 0)),
	SPIKES_12(new Position(2711, 9131, 0)),
	SPIKES_13(new Position(2713, 9130, 0)),
	SPIKES_14(new Position(2716, 9131, 0)),
	SPIKES_15(new Position(2719, 9130, 0)),
	SPIKES_16(new Position(2739, 9127, 0)),
	SPIKES_17(new Position(2741, 9125, 0)),
	SPIKES_18(new Position(2743, 9123, 0)),
	SPIKES_19(new Position(2737, 9123, 0)),
	SPIKES_20(new Position(2735, 9122, 0)),
	SPIKES_21(new Position(2740, 9122, 0)),
	SPIKES_22(new Position(2742, 9121, 0)),
	SPIKES_23(new Position(2736, 9120, 0)),
	SPIKES_24(new Position(2739, 9119, 0)),
	SPIKES_25(new Position(2738, 9117, 0)),
	SPIKES_26(new Position(2715, 9119, 0)),
	SPIKES_27(new Position(2710, 9114, 0)),
	SPIKES_28(new Position(2707, 9113, 0)),
	SPIKES_29(new Position(2704, 9113, 0)),
	SPIKES_30(new Position(2703, 9108, 0)),
	SPIKES_31(new Position(2706, 9109, 0)),
	SPIKES_32(new Position(2709, 9110, 0)),
	SPIKES_33(new Position(2711, 9111, 0)),
	SPIKES_34(new Position(2713, 9109, 0)),
	SPIKES_35(new Position(2713, 9107, 0)),
	SPIKES_36(new Position(2710, 9106, 0)),
	SPIKES_37(new Position(2706, 9106, 0)),
	SPIKES_38(new Position(2703, 9106, 0)),
	SPIKES_39(new Position(2705, 9102, 0)),
	SPIKES_40(new Position(2707, 9103, 0)),
	SPIKES_41(new Position(2711, 9104, 0)),
	SPIKES_42(new Position(2733, 9105, 0)),
	SPIKES_43(new Position(2740, 9105, 0)),
	SPIKES_44(new Position(2742, 9107, 0)),
	SPIKES_45(new Position(2745, 9106, 0)),
	SPIKES_46(new Position(2745, 9104, 0)),
	SPIKES_47(new Position(2742, 9103, 0)),
	SPIKES_48(new Position(2746, 9101, 0)),
	SPIKES_49(new Position(2743, 9100, 0)),
	SPIKES_50(new Position(2746, 9099, 0)),
	SPIKES_51(new Position(2744, 9098, 0)),
	SPIKES_52(new Position(2742, 9097, 0)),
	SPIKES_53(new Position(2740, 9098, 0)),
	SPIKES_54(new Position(2725, 9097, 0)),
	SPIKES_55(new Position(2691, 9115, 0)),
	SPIKES_56(new Position(2701, 9142, 0));
	
	private Position position;
	
	DungeonSpikesData(Position pos) {
	    this.position = pos;
	}
	
	public static boolean onSpikes(Position p) {
	    for(DungeonSpikesData d : DungeonSpikesData.values()) {
		if(d.position.equals(p)) {
		    return true;
		}
	    }
	    return false;
	}
    }
    public static void startGuardDestroyCheck(final Player player) {
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		if(player != null && player.getMMVars().guardCalled != null) {
		    if(!player.onApeAtoll() || !Misc.goodDistance(player.getPosition(), player.getMMVars().guardCalled.getPosition(), 15))
			b.stop();
		}
	    }

	    @Override
	    public void stop() {
		NpcLoader.destroyNpc(player.getMMVars().guardCalled);
		player.getMMVars().guardCalled = null;
	    }
	}, 1);
    }
    
    public static void jail(final Player player, boolean guards) {
	final Npc npc = new Npc(1442 + Misc.random(4));
	if(guards) {
	    NpcLoader.spawnNpc(player, npc, false, false);
	    player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 0);
	    npc.getUpdateFlags().setForceChatMessage("Ook.");
	    npc.setFollowingEntity(player);
	    npc.setInteractingEntity(player);
	    npc.setPlayerOwner(player.getIndex());
	    player.getMMVars().guardCalled = npc;
	    startGuardDestroyCheck(player);
	}
	if(player.getMMVars().isMonkey()) {
	    return;
	}
	if(!guards) {
	    player.getUpdateFlags().sendAnimation(836);
	    player.setStopPacket(true);
	}
	player.getMMVars().inProcessOfBeingJailed = true;
	player.getMovementHandler().reset();
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		b.stop();
	    }
	    @Override
	    public void stop() {
		if(player.getMMVars().isMonkey()) {
		    Following.resetFollow(npc);
		    npc.setInteractingEntity(null);
		    Npc karam = new Npc(MonkeyMadness.KARAM_2);
		    NpcLoader.spawnNpc(player, karam, false, false);
		    player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 0);
		    karam.getUpdateFlags().setForceChatMessage("I'll show this guard the meaning of 'going ape shit'.");
		    CombatManager.attack(karam, npc);
		    player.getMMVars().inProcessOfBeingJailed = false;
		    karam.setPlayerOwner(player.getIndex());
		    return;
		}
		if(!player.onApeAtoll()) {
		    player.getMMVars().inProcessOfBeingJailed = false;
		    return;
		}
		player.fadeTeleport(new Position(2773, 2794, 0));
		if(player.getMMVars().guardCalled != null) {
		    NpcLoader.destroyNpc(player.getMMVars().guardCalled);
		    player.getMMVars().guardCalled = null;
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			b.stop();
		    }
		    @Override
		    public void stop() {
			if(player.getMMVars().firstTimeJail() && player.getQuestStage(36) < MonkeyMadness.FUCK_THE_DEMON) {
			    player.getMMVars().setFirstTimeJail(false);
			    Dialogues.startDialogue(player, MonkeyMadness.LUMO);
			} else if(!player.getMMVars().canHideInGrass()) {
			    Dialogues.startDialogue(player, MonkeyMadness.CARADO);
			}
			player.setStopPacket(false);
			player.getMMVars().setJailCheckRunning(false);
			player.getMMVars().inProcessOfBeingJailed = false;
		    }
		}, 5);
	    }
	}, guards ? 8 : 2);
    }
    
    public static void runDungeon(final Player player) {
	player.getMMVars().setDungeonRunning(true);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    int rocksTimer = 0;
	    @Override
	    public void execute(CycleEventContainer b) {
		if(!MinigameAreas.isInArea(player.getPosition(), DUNGEON)) {
		    b.stop();
		}
		if(rocksTimer > 2 && MinigameAreas.isInArea(player.getPosition(), END_DUNGEON) && !ApeAtollNpcs.rubbingWalls) {
		    ApeAtollNpcs.rubWalls();
		}
		if(Misc.random(15) == 1 && !MinigameAreas.isInArea(player.getPosition(), END_DUNGEON) && rocksTimer >= 15) {
		    rocksTimer = 0;
		    fallingRocks(player);
		}
		if(player.getMMVars().hitBySpikesHere != null && !player.getMMVars().hitBySpikesHere.equals(player.getPosition().clone())) {
		    player.getMMVars().hitBySpikes = false;
		    player.getMMVars().hitBySpikesHere = null;
		}
		if(DungeonSpikesData.onSpikes(player.getPosition()) && !player.getMMVars().hitBySpikes) {
		    player.getMMVars().hitBySpikes = true;
		    player.getMMVars().hitBySpikesHere = player.getPosition().clone();
		    floorSpikes(player, player.getPosition());
		}
		rocksTimer++;
	    }
	    @Override
	    public void stop() {
		player.getMMVars().setDungeonRunning(false);
		player.getMMVars().hitBySpikes = false;
		player.getMMVars().hitBySpikesHere = null;
	    }
	}, 1);
    }
    
    public static void floorSpikes(final Player player, final Position p) {
	player.hit(Misc.random(7), HitType.NORMAL);
	player.getActionSender().animateObject(p.getX(), p.getY(), p.getZ(), 1111);
	player.getUpdateFlags().sendAnimation(player.getBlockAnimation());
	player.getActionSender().sendMessage("You are injured by the floor spikes!");
    }
    
    public static void fallingRocks(final Player player) {
	if (!player.getMMVars().isMonkey()) {
	    player.getActionSender().shakeScreen(2, 10, 10, 4);
	    player.getUpdateFlags().sendGraphic(60);
	    player.getUpdateFlags().sendAnimation(player.getBlockAnimation());
	    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		int count = 1;

		@Override
		public void execute(CycleEventContainer b) {
		    if (count == 1) {
			player.hit(Misc.random(4), HitType.NORMAL);
		    }
		    if (count >= 2) {
			b.stop();
		    }
		    count++;
		}

		@Override
		public void stop() {
		    player.getActionSender().resetCamera();
		}
	    }, 1);
	}
    }
    
    public static boolean hiddenInGrass(final Player player) {
	CacheObject g = ObjectLoader.object("Jungle Grass", player.getPosition().getX(), player.getPosition().getY(), 0);
	return g != null && g.getDef().getId() >= 4812 && g.getDef().getId() <= 4814;
    }
    
    public static boolean handleGreeGreeEquip(final Player player, int itemId) {
	if (GreeGreeData.forItemId(itemId) != null) {
	    if (player.onApeAtoll() || player.Area(2591, 2639, 3264, 3288)) {
		player.getUpdateFlags().sendGraphic(160);
		player.getEquipment().equip(player.getSlot());
		GreeGreeData g = GreeGreeData.forItemId(itemId);
		player.transformNpc = g.getTransformId();
		player.setStandAnim(g.getStandAnim());
		player.setWalkAnim(g.getWalkAnim());
		if(g.getRunAnim() == -1) {
		    player.getMovementHandler().setRunToggled(false);
		} else {
		    player.setRunAnim(g.getRunAnim());
		    player.getMovementHandler().setRunToggled(true);
		}
		player.getUpdateFlags().setUpdateRequired(true);
		player.getMMVars().setIsMonkey(true);
		return true;
	    } else {
		player.getActionSender().sendMessage("You must be on Ape Atoll to feel the effects of the greegree.");
		return true;
	    }
	}
	return false;
    }
    
    public static void handleGreeGree(final Player player, GreeGreeData g) {
	if (player.onApeAtoll() || player.Area(2591, 2639, 3264, 3288)) {
	    player.transformNpc = g.getTransformId();
	    player.setStandAnim(g.getStandAnim());
	    player.setWalkAnim(g.getWalkAnim());
	    if (g.getRunAnim() == -1) {
		player.getMovementHandler().setRunToggled(false);
	    } else {
		player.setRunAnim(g.getRunAnim());
		player.getMovementHandler().setRunToggled(true);
	    }
	    player.getUpdateFlags().setUpdateRequired(true);
	    player.getMMVars().setIsMonkey(true);
	} else {
	    player.getActionSender().sendMessage("You must be on Ape Atoll to feel the effects of the greegree.");
	    player.transformNpc = -1;
	    player.setStandAnim(-1);
	    player.setWalkAnim(-1);
	    player.setRunAnim(-1);
	    player.getActionSender().sendSideBarInterfaces();
	    player.setAppearanceUpdateRequired(true);
	    player.getMMVars().setIsMonkey(false);
	    player.getMovementHandler().setRunToggled(true);
	}
    }

    public static boolean doObjectFirstClick(final Player player, final int object, final int x, final int y) {
	switch (object) {
	    case 1413: //empty pineapple
		player.getActionSender().sendMessage("This plant has no fruit left!");
		return true;
	    case 1412:
	    case 1411:
	    case 1410:
	    case 1409:
	    case 1408: //karamja pineapple plants
	    case PINEAPPLE_PLANT:
		final GameObjectDef p = SkillHandler.getObject(object, x, y, player.getPosition().getZ());
		player.getActionSender().sendMessage("You pick the pineapple off the plant.");
		player.getUpdateFlags().sendAnimation(832);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			player.getInventory().addItem(new Item(2114));
			ObjectHandler.getInstance().removeObject(x, y, player.getPosition().getZ(), 10);
			new GameObject(1413, x, y, player.getPosition().getZ(), p.getFace(), 10, object, 999999);
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.setStopPacket(false);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			    @Override
			    public void execute(CycleEventContainer b) {
				b.stop();
			    }

			    @Override
			    public void stop() {
				ObjectHandler.getInstance().removeObject(x, y, player.getPosition().getZ(), 10);
				new GameObject(object, x, y, player.getPosition().getZ(), p.getFace(), 10, 0, 999999);
			    }
			}, 20);
		    }
		}, 2);
		return true;
	    case 4787:
	    case 4788:
		return true;
	    case 4799:
	    case 4800: //jail cell doors
		GameObjectDef d = SkillHandler.getObject(object, x, y, player.getPosition().getZ());
		if (d.getFace() == 0) {
		    ThieveOther.pickLock(player, new Position(x, y, player.getPosition().getZ()), object, 0, 0, player.getPosition().getX() >= x ? -1 : 1, 0);
		} else if (d.getFace() == 1) {
		    ThieveOther.pickLock(player, new Position(x, y, player.getPosition().getZ()), object, 0, 0, 0, player.getPosition().getY() > y ? -1 : 1);
		}
		return true;
	    case BAMBOO_LADDER:
		Ladders.climbLadder(player, new Position(2803, 2733, 2));
		return true;
	    case BAMBOO_LADDER_DOWN:
		Ladders.climbLadder(player, new Position(2803, 2735, 0));
		return true;
	    case END_OF_BRIDGE:
		player.getActionSender().sendMessage("You jump off the edge of the bridge...");
		player.fadeTeleport(new Position(2803, 2725, 0));
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			player.getActionSender().sendMessage("...and wind up mildy bruised.");
			player.hit(2, HitType.NORMAL);
			b.stop();
		    }

		    @Override
		    public void stop() {
		    }
		}, 5);
		return true;
	    case BANANA_TREE:
		player.getActionSender().sendMessage("You search the banana tree...");
		player.getUpdateFlags().sendAnimation(832);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			player.getActionSender().sendMessage("You find a banana, how strange.");
			player.getInventory().addItem(new Item(1963));
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.setStopPacket(false);
		    }
		}, 2);
		return true;
	    case 4772:
	    case 4773:
	    case 4774:
	    case 4775:
		if (x == 2781 && y == 2783) {
		    Ladders.climbLadder(player, new Position(2781, 2784, 2));
		    return true;
		} else {
		    GameObjectDef g = SkillHandler.getObject(object, x, y, player.getPosition().getZ());
		    int r = g.getFace();
		    Ladders.climbLadder(player, new Position(r == 2 ? x + 1 : r == 0 ? x - 1 : x, r == 1 ? y + 1 : r == 3 ? y - 1 : y, player.getPosition().getZ() + 1));
		}
		return true;
	    case 4776:
	    case 4777:
	    case 4778:
	    case 4779:
		GameObjectDef g2 = SkillHandler.getObject(object, x, y, player.getPosition().getZ());
		int r2 = g2.getFace();
		Ladders.climbLadder(player, new Position(r2 == 0 ? x + 1 : r2 == 2 ? x - 1 : x, r2 == 3 ? y + 1 : r2 == 1 ? y - 1 : y, player.getPosition().getZ() - 1));
		return true;
	    case DUNGEON_LADDER_DOWN:
		//TO DO, a warning here? not sure how the quest goes
		Ladders.climbLadder(player, START_OF_DUNGEON);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			b.stop();
		    }
		    @Override
		    public void stop() {
			runDungeon(player);
		    }
		}, 3);
		return true;
	    case DUNGEON_LADDER_UP:
		Ladders.climbLadder(player, new Position(2764, 2703, 0));
		return true;
	    case TEMPLE_STAIRS_DOWN:
		player.teleport(new Position(x, y - 2, 0));
		return true;
	    case TEMPLE_STAIRS_UP:
		player.teleport(new Position(x, y + 3, 1));
		return true;
	    case GORILLA_STATUE:
		Prayer.rechargePrayer(player);
		return true;
	    case PYRE_TRAPDOOR:
	    case DENTURES_TRAPDOOR:
		GameObjectDef def = SkillHandler.getObject(object, x, y, player.getPosition().getZ());
		if(x != 2749) {
		    TrapDoor.handleTrapdoor(player, object, object == PYRE_TRAPDOOR ? PYRE_TRAPDOOR_OPEN : DENTURES_TRAPDOOR_OPEN, def);
		} else {
		    player.getActionSender().sendMessage("The trapdoor is locked from this side.");
		}
		return true;
	    case DENTURES_TRAPDOOR_OPEN:
		Ladders.climbLadder(player, new Position(2797, 9169, 0));
		return true;
	    case DENTURES_ROPE_EAST:
		Ladders.climbLadder(player, new Position(2764, 2770, 0));
		return true;
	    case DENTURES_ROPE_WEST:
		Ladders.climbLadder(player, new Position(2749, 2769, 0));
		return true;
	    case PYRE_TRAPDOOR_OPEN:
		Ladders.climbLadder(player, BELOW_PYRES);
		return true;
	    case PYRE_ROPE:
		Ladders.climbLadder(player, UP_FROM_PYRES);
		return true;
	}
	return false;
    }
}
