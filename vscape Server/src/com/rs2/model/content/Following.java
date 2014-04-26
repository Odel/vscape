package com.rs2.model.content;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;
import com.rs2.util.clip.ClippedPathFinder;

public class Following {

    private Entity entity;

    public Following(Entity entity) {
        this.entity = entity;
    }

    /**
     * Direction coordinate modifiers
     */
    public final int[][] COORDINATE_MODIFIERS = {{-1, 1}, {0, 1}, {1, 1}, {-1, 0}, {1, 0}, {-1, -1}, {0, -1}, {1, -1}};

    public final int[][][] SECONDARY_COORDINATE_MODIFIERS = {{{0, 0}, {-1, 2}, {0, 0}, {-2, 1}, {0, 0}, {0, 0}, {0, 0}, {0, 0}}, {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}}, {{0, 0}, {1, 2}, {0, 0}, {0, 0}, {2, 1}, {0, 0}, {0, 0}, {0, 0}}, {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}}, {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}}, {{0, 0}, {0, 0}, {0, 0}, {-2, -1}, {0, 0}, {0, 0}, {-1, -2}, {0, 0}}, {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}}, {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {2, -1}, {0, 0}, {1, -2}, {0, 0}},};

    /**
     * The entity's follow tick.
     */
    public void followTick() {
        if (entity.getFollowingEntity() != null)
            followEntity();
    }

    /**
     * An entity following another entity.
     */
    public void followEntity() {
        Entity leader = entity.getFollowingEntity();
        if (leader == null || leader.isDead() || entity.isDead() || (!leader.getPosition().withinDistance(entity.getPosition(), 20))) {
            resetFollow(entity);
            CombatManager.resetCombat(entity);
            return;
        }
        if (entity.isDontFollow()) {
        	return;
        }
        entity.getUpdateFlags().faceEntity(leader.getFaceIndex());
        if (entity.isFrozen() || entity.isStunned()) {
            return;
        }
        if (entity.isPlayer() && leader.isNpc() && ((Npc) leader).isBoothBanker()) {
        	Player player = (Player) entity;
        	Position pos = ((Npc) leader).getCorrectStandPosition(2);
        	ClippedPathFinder.getPathFinder().findRoute(player, pos.getX(), pos.getY(), true, 0, 0);
        	return;
        }
        if (entity.isPlayer() && entity.getCombatingEntity() == null) {
        	Player player = (Player) entity;
        	if (leader.isPlayer() && entity.getInteractingEntity() == null) {
            	int x = leader.getPosition().getLastX();
            	int y = leader.getPosition().getLastY();
            	ClippedPathFinder.getPathFinder().findRoute(player, x, y, true, 0, 0);
        	} else {
        		meleeFollow(player, leader);
        	}
        } else if (entity.isPlayer()) {
        	Player player = (Player) entity;
        	if (entity.getFollowDistance() < 2 && leader.getSize() < 2) {
        		meleeFollow(player, leader);
        		return;
        	}
            if (entity.inEntity(leader)) {
            	stepAway();
            	return;
            }
			if (Following.withinRange(entity, leader)) {
				return;
			}
           ClippedPathFinder.getPathFinder().findRoute(player, leader.getPosition().getX(), leader.getPosition().getY(), true, 0, 0);
        } else if (entity.isNpc()) {
        	Npc npc = (Npc) entity;
            if (entity.inEntity(leader)) {
            	stepAway();
            	return;
            }
			if (Following.withinRange(entity, leader)) {
				return;
			}
        	followMethod(npc, leader);
        }
        /*else {
            Position followPos = leader.getPosition();
            Position loc = follower.getPosition();
            PathFinder pf = new StraightPathFinder();

            Path path = pf.findPath(follower, followPos, true);
            if (path != null) {
                follower.getMovementHandler().reset();
                while (!path.getPoints().isEmpty()) {
                    Point p = path.getPoints().poll();
                    if (p == null)
                        break;
                    follower.getMovementHandler().addToPath(new Position(p.getX(), p.getY(), loc.getZ()));
                }
                follower.getMovementHandler().finish();
           }
        }*/
    }

    public void meleeFollow(Player player, Entity leader) {
    	int x = player.getPosition().getX(), y = player.getPosition().getY();
    	int x2 = leader.getPosition().getX(), y2 = leader.getPosition().getY();
    	if (x > x2 && leader.canMove(1, 0)) {
    		ClippedPathFinder.getPathFinder().findRoute(player, x2 + 1, y2, true, 0, 0);
    	} else if (x < x2 && leader.canMove(-1, 0)) {
    		ClippedPathFinder.getPathFinder().findRoute(player, x2 - 1, y2, true, 0, 0);
        } else if (y < y2 && leader.canMove(0, -1)) {
        	ClippedPathFinder.getPathFinder().findRoute(player, x2, y2 - 1, true, 0, 0);
        } else {
        	if (leader.canMove(1, 0)) {
        		ClippedPathFinder.getPathFinder().findRoute(player, x2 + 1, y2, true, 0, 0);
        	} else if (leader.canMove(-1, 0)) {
        		ClippedPathFinder.getPathFinder().findRoute(player, x2 - 1, y2, true, 0, 0);
            } else if (leader.canMove(0, -1)) {
            	ClippedPathFinder.getPathFinder().findRoute(player, x2, y2 - 1, true, 0, 0);
            } else if (leader.canMove(0, 1)) {
            	ClippedPathFinder.getPathFinder().findRoute(player, x2, y2 + 1, true, 0, 0);
            }
        }
    }
    /**
     * Resets the follower.
     */
    public static void resetFollow(Entity follower) {
    	//follower.getMovementHandler().reset();
    	follower.getUpdateFlags().faceEntity(65535);
    	follower.setFollowingEntity(null);
    }

    public static boolean withinRange(Entity attacker, Entity victim) {   
        return withinRange(attacker, victim, attacker.getFollowDistance());
    }

	/**
	 * Checks if the attacker is within distance required for attacking.
	 */
	public static boolean withinRange(Entity attacker, Entity victim, int distance) {
		if (attacker.inEntity(victim)) {
			return false;
		}
		if (attacker.getCombatingEntity() != null && attacker.isPlayer() && distance == 1 && victim.getSize() < 2 && !victim.isMoving()) {
			if (attacker.getPosition().getX() != victim.getPosition().getX() && attacker.getPosition().getY() != victim.getPosition().getY()) {
				return false;
			}
		}
		if (!attacker.goodDistanceEntity(victim, distance)) {
			return false;
		}
		// if door support, skip clip check
		if (victim.isDoorSupport()) {
			return true;
		}
		return Misc.checkClip(attacker.getPosition(), victim.getPosition(), distance < 2);
	}

    public void stepAway() {
        entity.getMovementHandler().reset();
        int x = entity.getPosition().getX();
        int y = entity.getPosition().getY();
        Npc npc = entity.isNpc() ? (Npc) entity : null;
        if (entity.canMove(-1, 0) && (npc == null || !npc.walkIntoNpc(-1, 0))) {
            entity.walkTo(x - 1, y, true);
        } else if (entity.canMove(1, 0) && (npc == null || !npc.walkIntoNpc(1, 0))) {
            entity.walkTo(x + 1, y, true);
        } else if (entity.canMove(0, -1) && (npc == null || !npc.walkIntoNpc(0, -1))) {
            entity.walkTo(x, y - 1, true);
        } else if (entity.canMove(0, 1) && (npc == null || !npc.walkIntoNpc(0, 1))) {
            entity.walkTo(x, y + 1, true);
        }
    }
   
	/**
	 * Npc Following
	 */
	public void followMethod(Npc npc, Entity leader) {
		int followX = leader.getPosition().getX(), followY = leader.getPosition().getY();
		int moveX = 0, moveY = 0;
		if (followX != npc.getPosition().getX() && followY != npc.getPosition().getY()) {
			if (followX > npc.getPosition().getX() && followY > npc.getPosition().getY()) {
				if (npc.checkWalk(1, 1) && !npc.goodDistanceEntity(leader, 1)) {
					moveX = 1;
					moveY = 1;
				} else if (npc.checkWalk(1, 0)) {
					moveX = 1;
					moveY = 0;
				} else if (npc.checkWalk(0, 1)) {
					moveX = 0;
					moveY = 1;
				}
			} else if (followX > npc.getPosition().getX() && followY < npc.getPosition().getY()) {
				if (npc.checkWalk(1, -1) && !npc.goodDistanceEntity(leader, 1)) {
					moveX = 1;
					moveY = -1;
				} else if (npc.checkWalk(0, -1)) {
					moveX = 0;
					moveY = -1;
				} else if (npc.checkWalk(1, 0)) {
					moveX = 1;
					moveY = 0;
				}
			} else if (followX < npc.getPosition().getX() && followY > npc.getPosition().getY()) {
				if (npc.checkWalk(-1, 1) && !npc.goodDistanceEntity(leader, 1)) {
					moveX = -1;
					moveY = 1;
				} else if (npc.checkWalk(-1, 0)) {
					moveX = -1;
					moveY = 0;
				} else if (npc.checkWalk(0, 1)) {
					moveX = 0;
					moveY = 1;
				}
			} else if (followX < npc.getPosition().getX() && followY < npc.getPosition().getY()) {
				if (npc.checkWalk(-1, -1) && !npc.goodDistanceEntity(leader, 1)) {
					moveX = -1;
					moveY = -1;
				} else if (npc.checkWalk(0, -1)) {
					moveX = 0;
					moveY = -1;
				} else if (npc.checkWalk(-1, 0)) {
					moveX = -1;
					moveY = 0;
				}
			}
		} else if (followX != npc.getPosition().getX()) {
			if (followX > npc.getPosition().getX()) {
				if (npc.checkWalk(1, 0)) {
					moveX = 1;
					moveY = 0;
				}
			} else if (followX < npc.getPosition().getX()) {
				if (npc.checkWalk(-1, 0)) {
					moveX = -1;
					moveY = 0;
				}
			}
		} else if (followY != npc.getPosition().getY()) {
			if (followY > npc.getPosition().getY()) {
				if (npc.checkWalk(0, 1)) {
					moveX = 0;
					moveY = 1;
				}
			} else if (followY < npc.getPosition().getY()) {
				if (npc.checkWalk(0, -1)) {
					moveX = 0;
					moveY = -1;
				}
			}
		}
		if (moveX != 0 || moveY != 0) {
			if (!Misc.goodDistance(npc.getPosition(), npc.getSpawnPosition(), Constants.NPC_FOLLOW_DISTANCE)) {
				CombatManager.resetCombat(npc);
				npc.resetActions();
				npc.walkTo(npc.getSpawnPosition().getX(), npc.getSpawnPosition().getY(), true);
				return;
			}
			npc.walkTo(npc.getPosition().getX() + moveX, npc.getPosition().getY() + moveY, true);
		}
	}

	public static boolean standingDiagonal(Position pos1, Position pos2) {
		return pos1.getX() != pos2.getX() && pos1.getY() != pos2.getY();
	}
}
