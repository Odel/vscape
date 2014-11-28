package com.rs2.model.content.quests.MonkeyMadness;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.skills.thieving.ThieveOther;
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
    
    public static final MinigameAreas.Area JAIL = new MinigameAreas.Area(2764, 2776, 2792, 2804, 0);

    public enum GreeGreeData {

	SMALL_NINJA_GREEGREE(4024, 3179, 1480, 1386, 1380),
	MEDIUM_NINJA_GREEGREE(4025, 3180, 1481, 1386, 1380),
	GORILLA_GREEGREE(4026, 3181, 1482, 1401, 1399),
	BEARED_GORILLA_GREEGREE(4027, 3182, 1483, 1401, 1399),
	MYSTERIOUS_GREEGREE(4028, -1, 1484, 1401, 1399), //bonesId?
	SMALL_ZOMBIE_GREEGREE(4029, 3185, 1485, 1386, 1382),
	LARGE_ZOMBIE_GREEGREE(4030, 3186, 1486, 1386, 1382),
	MONKEY_GREEGREE(4031, 3183, 1487, 222, 219);

	private int itemId;
	private int bonesId;
	private int transformId;
	private int standAnim;
	private int walkAnim;

	GreeGreeData(int itemId, int bonesId, int transformId, int standAnim, int walkAnim) {
	    this.itemId = itemId;
	    this.bonesId = bonesId;
	    this.transformId = transformId;
	    this.standAnim = standAnim;
	    this.walkAnim = walkAnim;
	}

	public static GreeGreeData forItemId(int itemId) {
	    for (GreeGreeData g : GreeGreeData.values()) {
		if (g.itemId == itemId) {
		    return g;
		}
	    }
	    return null;
	}

	public static int talismanForBones(int itemId) {
	    for (GreeGreeData g : GreeGreeData.values()) {
		if (g.bonesId == itemId) {
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

    }

    public static boolean handleGreeGreeEquip(final Player player, int itemId) {
	if (GreeGreeData.forItemId(itemId) != null) {
	    if (player.onApeAtoll()) {
		player.getUpdateFlags().sendGraphic(160);
		player.getEquipment().equip(player.getSlot());
		GreeGreeData g = GreeGreeData.forItemId(itemId);
		player.transformNpc = g.getTransformId();
		player.setStandAnim(g.getStandAnim());
		player.setWalkAnim(g.getWalkAnim());
		player.setRunAnim(g.getWalkAnim());
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
		}, 3);
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
		Ladders.climbLadder(player, MonkeyMadness.START_OF_DUNGEON);
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
		    player.getActionSender().sendMessage("The trapdoor is locked from this side?");
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
		Ladders.climbLadder(player, MonkeyMadness.BELOW_PYRES);
		return true;
	    case PYRE_ROPE:
		Ladders.climbLadder(player, MonkeyMadness.UP_FROM_PYRES);
		return true;
	}
	return false;
    }
}
