package com.rs2.model.content.quests.impl.MonkeyMadness;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class ApeAtollNpcs {

    private static final Npc monkeyAunt = new Npc(MonkeyMadness.MONKEYS_AUNT);
    private static final Npc monkeyGuard_1 = new Npc(MonkeyMadness.TREFAJI);
    private static final Npc monkeyGuard_2 = new Npc(MonkeyMadness.ABERAB);
    
    public static final int SPIDER = 1473;
    public static final int SCORPION = 1477;
    public static final int JUNGLE_SPIDER = 1478;
    public static final int SNAKE = 1479;
    public static final int MONKEY_ARCHER_GATE = 1456;
    public static final int MONKEY_GUARD = 1459;
    public static final int MONKEY_GUARD_BLOCK = 1460;
    public static final int ZOMBIE_MONKEY_TUNNEL = 1465;
    public static final int ZOMBIE_MONKEY_LARGE = 1466;
    public static final int ZOMBIE_MONKEY_SMALL = 1467;
    public static final int SKELETON = 1471;
    public static Npc WAYMOTTIN = null;
    public static Npc BUNKWICKET = null;
    
    public static boolean rubbingWalls = false;
    
    private static int patrollingGuard = 1;

    public enum ApeAtollNpcData {
	MONKEYS_AUNT(monkeyAunt, new Position(2736, 2787, 0), new Position(2737, 2790, 0), new Position(2736, 2794, 0), new Position(2739, 2794, 0), new Position(2742, 2794), new Position(0, 0, 0), new Position(0, 0, 0), true),
	PRISON_GUARD_1(monkeyGuard_1, new Position(2768, 2804, 0), new Position(2768, 2798, 0), new Position(2771, 2798, 0), new Position(2770, 2802, 0), new Position(2773, 2801, 0), new Position(2772, 2796, 0), new Position(2769, 2796, 0), false),
	PRISON_GUARD_2(monkeyGuard_2, new Position(2767, 2802, 0), new Position(2768, 2798, 0), new Position(2771, 2798, 0), new Position(2770, 2802, 0), new Position(2773, 2801, 0), new Position(2772, 2796, 0), new Position(2769, 2796, 0), false);

	private Npc npc;
	private Position spawnPosition;
	private Position first;
	private Position second;
	private Position third;
	private Position fourth;
	private Position fifth;
	private Position end;
	private boolean basicCycle;

	ApeAtollNpcData(Npc npc, Position spawnPosition, Position first, Position second, Position third, Position fourth, Position fifth, Position end, boolean basicCycle) {
	    this.npc = npc;
	    this.spawnPosition = spawnPosition;
	    this.first = first;
	    this.second = second;
	    this.third = third;
	    this.fourth = fourth;
	    this.fifth = fifth;
	    this.end = end;
	    this.basicCycle = basicCycle;
	}
	
	ApeAtollNpcData(Npc npc, Position spawnPosition, Position face) {
	    this.npc = npc;
	    this.spawnPosition = spawnPosition;
	    this.first = face;
	}

	public static ApeAtollNpcData forNpcId(int npcId) {
	    for (ApeAtollNpcData a : ApeAtollNpcData.values()) {
		if (a.npc.getNpcId() == npcId) {
		    return a;
		}
	    }
	    return null;
	}

	public Npc getNpc() {
	    return this.npc;
	}
	
	public Position getSpawnPosition() {
	    return this.spawnPosition;
	}
	
	public Position getSecondPosition() {
	    return this.second;
	}

	public Position walkPosForCount(int count) {
	    switch (count) {
		case 0:
		    return this.spawnPosition.clone();
		case 1:
		    return this.first;
		case 2:
		    return this.second;
		case 3:
		    return this.third;
		case 4:
		    return this.fourth;
		case 5:
		    return this.fifth;
		case 6:
		    return this.end;
	    }
	    return null;
	}
    }
    
    public static void spawnApeAtollNpc(final ApeAtollNpcData a) {
	final Npc npc = a.getNpc();
	npc.setPosition(new Position(a.getSpawnPosition().getX(), a.getSecondPosition() == null ? a.getSpawnPosition().getY() : a.getSpawnPosition().getY() - 1, a.getSpawnPosition().getZ()));
	npc.setSpawnPosition(a.getSpawnPosition());
	npc.setWalkType(Npc.WalkType.STAND);
	npc.setNeedsRespawn(false);
	npc.setFollowingEntity(null);
	World.register(npc);
    }
    
    public static void startBasicWalkCycle(final ApeAtollNpcData a) {
	final Npc npc = a.getNpc();
	CycleEventHandler.getInstance().addEvent(a.getNpc(), new CycleEvent() {
	    int count = 1;
	    boolean reverse = false;

	    @Override
	    public void execute(CycleEventContainer b) {
		int max = a.basicCycle ? 4 : 6;
		if (count == max || count == -1) {
		    reverse = !reverse;
		}
		if (a.walkPosForCount(count) != null) {
		    Position pos = a.walkPosForCount(count);
		    npc.setSpawnPosition(pos);
		    npc.setWalkType(Npc.WalkType.WALK);
		    npc.walkTo(pos, true);
		    npc.getUpdateFlags().sendFaceToDirection(pos);
		    npc.getUpdateFlags().setFaceToDirection(true);
		}
		count = reverse ? count - 1 : count + 1;
	    }

	    @Override
	    public void stop() {
	    }
	}, 12);
    }

    public static void startGuardWalkCycle(final ApeAtollNpcData a) {
	final Npc npc = a.getNpc();
	CycleEventHandler.getInstance().addEvent(npc, new CycleEvent() {
	    int count = 1;
	    int patrolCount = 0;
	    boolean reverse = false;

	    @Override
	    public void execute(CycleEventContainer b) {
		if (count == 6 || count == -1) {
		    patrolCount++;
		    if (patrolCount == 4) {
			b.stop();
		    }
		    reverse = !reverse;
		}
		if (a.walkPosForCount(count) != null) {
		    Position pos = a.walkPosForCount(count);
		    npc.setSpawnPosition(pos);
		    npc.setWalkType(Npc.WalkType.WALK);
		    if (count == 4 && reverse) {
			npc.walkTo(new Position(pos.getX() - 1, pos.getY() + 1, 0), true);
		    } else if (count == 3 && reverse) {
			npc.walkTo(new Position(pos.getX(), pos.getY() - 1, 0), true);
		    } else if (count == 2 && reverse) {
			npc.walkTo(new Position(pos.getX() - 1, pos.getY(), 0), true);
		    } else if (count == 0) {
			if (!reverse) {
			    count++;
			}
		    } else {
			npc.walkTo(pos, true);
		    }
		    npc.getUpdateFlags().setFace(pos);
		    npc.getUpdateFlags().setEntityFaceUpdate(true);
		}
		count = reverse ? count - 1 : count + 1;
	    }

	    @Override
	    public void stop() {
		npc.walkTo(a.walkPosForCount(0), true);
		if (a.equals(ApeAtollNpcData.PRISON_GUARD_1)) {
		    startGuardWalkCycle(ApeAtollNpcData.PRISON_GUARD_2);
		    patrollingGuard = 2;
		} else if (a.equals(ApeAtollNpcData.PRISON_GUARD_2)) {
		    startGuardWalkCycle(ApeAtollNpcData.PRISON_GUARD_1);
		    patrollingGuard = 1;
		}
	    }
	}, 8);
    }
    
    public static void init() {
	for (ApeAtollNpcData a : ApeAtollNpcData.values()) {
	    spawnApeAtollNpc(a);
	    if(a.equals(ApeAtollNpcData.PRISON_GUARD_1)) {
		startGuardWalkCycle(a);
	    } else if(!a.equals(ApeAtollNpcData.PRISON_GUARD_2)) {
		startBasicWalkCycle(a);
	    }
	}
    }
    
    public enum FinalFightNpcs {
	GARKOR(1412, new Position(2726, 9172, 1)),
	LUMO(1414, new Position(2731, 9174, 1)),
	BUNKDO(1416, new Position(2725, 9172, 1)),
	CARADO(1418, new Position(2731, 9170, 1)),
	LUMBDO(1419, new Position(2728, 9176, 1)),
	KARAM(1422, new Position(2728, 9173, 1)),
	BUNKWICKET(1423, new Position(2727, 9176, 1)),
	WAYMOTTIN(1424, new Position(2734, 9177, 1)),
	ZOOKNOCK(1426, new Position(2733, 9168, 1));

	private int id;
	private Position spawnPos;
	
	FinalFightNpcs(int id, Position spawnPos) {
	    this.id = id;
	    this.spawnPos = spawnPos;
	}
	
	public int getId() {
	    return this.id;
	}
	
	public Position getPosition() {
	    return this.spawnPos;
	}
    }
    
    public static Npc spawnFinalFightNpc(final FinalFightNpcs f, int z, Player player) {
	    Npc npc = new Npc(f.getId());
	    npc.setPosition(f.getPosition().modifyZ(z));
	    npc.setSpawnPosition(f.getPosition().modifyZ(z));
	    npc.setWalkType(Npc.WalkType.WALK);
	    npc.setMaxWalk(new Position(f.getPosition().getX() + 15, f.getPosition().getY() + 15, z));
	    npc.setMinWalk(new Position(f.getPosition().getX() - 15, f.getPosition().getY() - 15, z));
	    npc.setNeedsRespawn(false);
	    World.register(npc);
	    return npc;
	}
    
    public static void startJailCheck(final Player player) {
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    Position toCheck = patrollingGuard == 1 ? monkeyGuard_1.getPosition() : monkeyGuard_2.getPosition();
	    Position other = patrollingGuard == 2 ? monkeyGuard_1.getPosition() : monkeyGuard_2.getPosition();
	    @Override
	    public void execute(CycleEventContainer b) {
		if(!MinigameAreas.isInArea(player.getPosition(), ApeAtoll.JAIL) || !player.onApeAtoll() || player.getMMVars().isMonkey()) {
		    b.stop();
		}
		if(Misc.checkClip(player.getPosition(), toCheck, true) && Misc.goodDistance(player.getPosition(), toCheck, 2)) {
		    b.stop();
		    player.hit(Misc.random(3) + 5, HitType.NORMAL);
		    if(toCheck == monkeyGuard_1.getPosition()) {
			monkeyGuard_1.getUpdateFlags().sendAnimation(1402);
		    } else {
			monkeyGuard_2.getUpdateFlags().sendAnimation(1402);
		    }
		    ApeAtoll.jail(player, false);
		} else if(Misc.goodDistance(player.getPosition(), other, 1)) {
		    b.stop();
		    player.hit(Misc.random(3) + 5, HitType.NORMAL);
		    if(other == monkeyGuard_1.getPosition()) {
			monkeyGuard_1.getUpdateFlags().sendAnimation(1402);
		    } else {
			monkeyGuard_2.getUpdateFlags().sendAnimation(1402);
		    }
		    ApeAtoll.jail(player, false);
		}
	    }

	    @Override
	    public void stop() {
	    }
	}, 1);
    }
    
    public static void rubWalls() {
	rubbingWalls = true;
	WAYMOTTIN.getUpdateFlags().sendAnimation(1414);
	BUNKWICKET.getUpdateFlags().sendAnimation(1414);
	World.submit(new Tick(100) {
	    @Override
	    public void execute() {
		this.stop();
		return;
	    }
	    
	    @Override
	    public void stop() {
		ApeAtollNpcs.rubbingWalls = false;
	    }
	});
    }
    
    public static boolean walkIntoNpc(final Player player, int dirX, int dirY) {
	for (Npc npc : World.getNpcs()) {
	    if (npc == null || (npc.getNpcId() != MONKEY_GUARD_BLOCK && npc.getNpcId() != 1461 && npc.getNpcId() != 1462)) {
		//DO NOTHING? WHAT A MADMAN
	    } else if (!player.isDead() && npc.getPosition().getZ() == player.getPosition().getZ() && !npc.isPet()) {
		if (Misc.goodDistance(player.getPosition().getX(), player.getPosition().getY(), npc.getPosition().getX(), npc.getPosition().getY(), npc.getSize())) {
		    if (npcInNpc(player, npc, dirX, dirY)) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public static boolean npcInNpc(Player player, Npc npc, int dirX, int dirY) {
	for (int x = player.getPosition().getX(); x < player.getPosition().getX() + player.getSize(); x++) {
	    for (int y = player.getPosition().getY(); y < player.getPosition().getY() + player.getSize(); y++) {
		for (int x2 = npc.getPosition().getX(); x2 < npc.getPosition().getX() + npc.getSize(); x2++) {
		    for (int y2 = npc.getPosition().getY(); y2 < npc.getPosition().getY() + npc.getSize(); y2++) {
			if (x2 == x + dirX && y2 == y + dirY) {
			    return true;
			}
		    }
		}
	    }
	}
	return false;
    }
    
     public static boolean isAggressiveNpc(int id) {
	return id == SNAKE || id == JUNGLE_SPIDER || id == SPIDER || id == SCORPION || id == MONKEY_ARCHER_GATE || id == MonkeyMadness.PADULAH
		|| id == ZOMBIE_MONKEY_TUNNEL || id == SKELETON || id == MONKEY_GUARD || id == MONKEY_GUARD_BLOCK || id == ZOMBIE_MONKEY_SMALL || id == ZOMBIE_MONKEY_LARGE;
    }
    
}
