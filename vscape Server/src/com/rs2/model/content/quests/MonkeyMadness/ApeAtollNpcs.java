package com.rs2.model.content.quests.MonkeyMadness;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.pf.Path;
import com.rs2.pf.PathFinder;
import com.rs2.pf.StraightPathFinder;
import com.rs2.util.Misc;

public class ApeAtollNpcs {

    private static final Npc monkeyAunt = new Npc(MonkeyMadness.MONKEYS_AUNT);
    private static final Npc monkeyGuard_1 = new Npc(MonkeyMadness.TREFAJI);
    private static final Npc monkeyGuard_2 = new Npc(MonkeyMadness.ABERAB);

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

	public void spawnNpc() {
	    npc.setPosition(new Position(spawnPosition.getX(), spawnPosition.getY() - 1, spawnPosition.getZ()));
	    npc.setSpawnPosition(this.spawnPosition);
	    npc.setWalkType(Npc.WalkType.STAND);
	    npc.setNeedsRespawn(false);
	    World.register(npc);
	}

	public void startBasicWalkCycle() {
	    final ApeAtollNpcData a = this;
	    CycleEventHandler.getInstance().addEvent(npc, new CycleEvent() {
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
	public void startGuardWalkCycle() {
	    final ApeAtollNpcData a = this;
	    CycleEventHandler.getInstance().addEvent(npc, new CycleEvent() {
		int count = 1;
		int patrolCount = 0;
		boolean reverse = false;

		@Override
		public void execute(CycleEventContainer b) {
		    if (count == 6 || count == -1) {
			patrolCount++;
			if(patrolCount == 4) {
			    b.stop();
			}
			reverse = !reverse;
		    }
		    if (a.walkPosForCount(count) != null) {
			Position pos = a.walkPosForCount(count);
			npc.setSpawnPosition(pos);
			npc.setWalkType(Npc.WalkType.WALK);
			if(count == 4 && reverse) {
			    npc.walkTo(new Position(pos.getX() - 1, pos.getY() + 1, 0), true);
			} else if(count == 3 && reverse) {
			    npc.walkTo(new Position(pos.getX(), pos.getY() - 1, 0), true);
			} else if(count == 2 && reverse) {
			    npc.walkTo(new Position(pos.getX() - 1, pos.getY(), 0), true);
			} else if (count == 0) {
			    if(!reverse) {
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
		    if(a.equals(ApeAtollNpcData.PRISON_GUARD_1)) {
			ApeAtollNpcData.PRISON_GUARD_2.startGuardWalkCycle();
			patrollingGuard = 2;
		    } else if(a.equals(ApeAtollNpcData.PRISON_GUARD_2)) {
			ApeAtollNpcData.PRISON_GUARD_1.startGuardWalkCycle();
			patrollingGuard = 1;
		    }
		}
	    }, 8);
	}
    }

    public static void init() {
	for (ApeAtollNpcData a : ApeAtollNpcData.values()) {
	    a.spawnNpc();
	    if(a.equals(ApeAtollNpcData.PRISON_GUARD_1)) {
		a.startGuardWalkCycle();
	    } else if(!a.equals(ApeAtollNpcData.PRISON_GUARD_2)) {
		a.startBasicWalkCycle();
	    }
	}
    }
    
    public static void startJailCheck(final Player player) {
	final Npc guard1 = ApeAtollNpcData.PRISON_GUARD_1.getNpc();
	final Npc guard2 = ApeAtollNpcData.PRISON_GUARD_2.getNpc();
	
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    Position toCheck = patrollingGuard == 1 ? guard1.getPosition() : guard2.getPosition();
	    Position other = patrollingGuard == 2 ? guard1.getPosition() : guard2.getPosition();
	    @Override
	    public void execute(CycleEventContainer b) {
		if(!MinigameAreas.isInArea(player.getPosition(), ApeAtoll.JAIL) || !player.onApeAtoll()) {
		    b.stop();
		}
		if(Misc.checkClip(player.getPosition(), toCheck, true) && Misc.goodDistance(player.getPosition(), toCheck, 2)) {
		    b.stop();
		    player.hit(7, HitType.NORMAL);
		    if(patrollingGuard == 1) {
			guard1.getUpdateFlags().sendAnimation(1402);
		    } else {
			guard2.getUpdateFlags().sendAnimation(1402);
		    }
		    jail(player);
		} else if(Misc.goodDistance(player.getPosition(), other, 1)) {
		    b.stop();
		    player.hit(7, HitType.NORMAL);
		    if(patrollingGuard == 2) {
			guard1.getUpdateFlags().sendAnimation(1402);
		    } else {
			guard2.getUpdateFlags().sendAnimation(1402);
		    }
		    jail(player);
		}
	    }

	    @Override
	    public void stop() {
	    }
	}, 1);
    }
    
    public static void jail(final Player player) {
	player.getUpdateFlags().sendAnimation(836);
	player.getMovementHandler().reset();
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		b.stop();
	    }
	    @Override
	    public void stop() {
		player.fadeTeleport(new Position(2773, 2794, 0));
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			b.stop();
		    }
		    @Override
		    public void stop() {
			player.getMMVars().setJailCheckRunning(false);
		    }
		}, 5);
	    }
	}, 2);
    }

}
