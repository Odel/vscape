package com.rs2.model.content.combat;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.rs2.model.Entity;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 *  TODO: on hit execution, setInCombatTick
 */
public class CombatCycleEvent extends CycleEvent {

	private Entity attacker, victim;
	private int taskId;

	public CombatCycleEvent(Entity attacker, Entity victim) {
		this.attacker = attacker;
		this.victim = victim;
		this.taskId = attacker.getTask();
	}

	@Override
	public void execute(CycleEventContainer container) {
		try {
			// check if attacker executes a different task
			if (!attacker.checkTask(taskId) || attacker.getCombatingEntity() == null) {
				container.stop();
				return;
			}
			// check if attacker can attack victim
			CanAttackResponse canAttackResponse = canAttack(attacker, victim);
			if (canAttackResponse != CanAttackResponse.SUCCESS) {
				container.stop();
				CombatManager.resetCombat(attacker);
				if (attacker.isPlayer()) {
					switch (canAttackResponse) {
						case CANT_ATTACK :
							((Player) attacker).getActionSender().sendMessage("You can't attack that npc from here.");
							break;
						case WILD_LEVEL :
							((Player) attacker).getActionSender().sendMessage("Your level difference is too great!");
							((Player) attacker).getActionSender().sendMessage("You need to move deeper into the Wilderness.");
							CombatManager.resetCombat((Player) attacker);
							break;
						case ALREADY_IN_COMBAT :
							((Player) attacker).getActionSender().sendMessage("You are already under attack!");
							break;
						case INCORRECT_DUEL_OPPONENT :
							((Player) attacker).getActionSender().sendMessage("That player is not your opponent!");
							break;
						case NOT_IN_COMBAT_AREA :
							((Player) attacker).getActionSender().sendMessage("That player is not in the wilderness!");
							break;
					}
				}
				return;
			}
			// check if victim can attack attacker
			canAttackResponse = canAttack(victim, attacker);
			if (canAttackResponse != CanAttackResponse.SUCCESS) {
				container.stop();
				CombatManager.resetCombat(attacker);
				if (attacker.isPlayer()) {
					switch (canAttackResponse) {
						case WILD_LEVEL :
							((Player) attacker).getActionSender().sendMessage("Your level difference is too great!");
							((Player) attacker).getActionSender().sendMessage("You need to move deeper into the Wilderness.");
							CombatManager.resetCombat((Player) attacker);
							break;
						case ALREADY_IN_COMBAT :
							((Player) attacker).getActionSender().sendMessage("That " + (victim.isPlayer() ? "player" : "monster") + " is already in combat!");
							break;
						case INCORRECT_DUEL_OPPONENT :
							((Player) attacker).getActionSender().sendMessage("That player is not your opponent!");
							break;

					}
				}
				return;
			}
			/* ORDER IS SUPER IMPORTANT HERE, DON'T ADD ANYTHING AFTER */
			int distance = Misc.getDistance(attacker.getPosition(), victim.getPosition());
			List<AttackUsableResponse> attacksInDistance = new LinkedList<AttackUsableResponse>();
			int possibleAttacks = attacker.fillUsableAttacks(attacksInDistance, victim, distance);
			AttackScript possibleAttackScript = null;
			boolean attackDelayExpired = attacker.getCombatDelayTick().completed();
			// randomize attacks
			if (attackDelayExpired)
				Collections.shuffle(attacksInDistance);
			for (AttackUsableResponse attackUsableResponse : attacksInDistance) {
				AttackUsableResponse.Type response = attackUsableResponse.getType();
                if (attacker.isStunned())
                    response = AttackUsableResponse.Type.WAIT;
                attacker.getUpdateFlags().faceEntity(victim.getFaceIndex());
				if (response == AttackUsableResponse.Type.WAIT) {
					possibleAttackScript = attackUsableResponse.getScript();
				} else if (response == AttackUsableResponse.Type.SUCCESS) {
					if (!attacker.isPlayer()) {
						attacker.setFollowDistance(attackUsableResponse.getScript().distanceRequired());
					}
					if (attackDelayExpired) {
						int delay = attackUsableResponse.getScript().execute(container);
						if (delay == -1) {
							CombatManager.resetCombat(attacker);
							container.stop();
							return;
						}
						victim.getInCombatTick().update(attacker, 17);
						victim.getPjTimer().update(attacker, 8);
						attacker.setCombatDelay(delay);
					}
					return;
				}
			}
			if (possibleAttackScript == null) {
				if (possibleAttacks <= attacksInDistance.size()) {
					CombatManager.resetCombat(attacker);
					container.stop();
					//attacker.getMovementHandler().reset();
				}
				 attacker.getMovementHandler().reset();
				// else if there are more possible attacks, wait to move closer
			} else {
				if (!attacker.isPlayer()) {
	                attacker.setFollowDistance(possibleAttackScript.distanceRequired());
				}
				// TODO:keep following until closer
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
	}

	public static CanAttackResponse canAttack(Entity attacker, Entity victim) {
		// check if victim nulled or dead
		if (victim == null || victim.isDead()) {
			return CanAttackResponse.FAIL;
		}
		// check if victim is in sight of attacker
		if ((victim.isPlayer() && !((Player) victim).isLoggedIn()) || !victim.getPosition().isViewableFrom(attacker.getPosition())) {
			return CanAttackResponse.FAIL;
		}
		// check if npc attackable
		if (victim.getMaxHp() < 1) {
			return CanAttackResponse.FAIL;
		}
		// check if door-support
        if (victim.isNpc()) {
    		if (((Npc) victim).getNpcId() == 2440) {
    			if (attacker.getPosition().getY() < 10141) {
    				return CanAttackResponse.CANT_ATTACK;
    			}
    		} else if (((Npc) victim).getNpcId() == 2443) {
    			if (attacker.getPosition().getX() < 2543) {
    				return CanAttackResponse.CANT_ATTACK;
    			}
    		} else if (((Npc) victim).getNpcId() == 2446) {
    			if (attacker.getPosition().getY() > 10145) {
    				return CanAttackResponse.CANT_ATTACK;
    			}
    		}
        }
		// check if single combat and already in combat
		if (!attacker.inMulti() || !victim.inMulti()) {
			if (attacker.getPjTimer().getOther() != null && !attacker.getPjTimer().completed() && attacker.getPjTimer().getOther() != victim) {
				return CanAttackResponse.ALREADY_IN_COMBAT;
			}
			/*if (attacker.getInCombatTick().getOther() != null && !attacker.getInCombatTick().getOther().isDead() && !attacker.getInCombatTick().completed() && attacker.getInCombatTick().getOther() != victim) {
			}*/
		}
		boolean bothPlayers = attacker.isPlayer() && victim.isPlayer();
		// if they are both players, need to do additional checks
		if (bothPlayers) {
			if (attacker.inDuelArena() && ((Player) attacker).getDuelMainData().getOpponent() != victim) {
				// check if in duel with victim, else return false
				return CanAttackResponse.INCORRECT_DUEL_OPPONENT;
			} else if (attacker.inWild()) {
				// check if in wild range, else return false
				int attackerWildLevel = attacker.getWildernessLevel();
				int attackerCombatLevel = ((Player) attacker).getCombatLevel();
				int victimCombatLevel = ((Player) victim).getCombatLevel();
				if (attackerWildLevel < Math.abs(attackerCombatLevel - victimCombatLevel)) {
					return CanAttackResponse.WILD_LEVEL;
				}
			} else if (attacker.isPlayer() && !attacker.inDuelArena())
				return CanAttackResponse.NOT_IN_COMBAT_AREA;
		}
		return CanAttackResponse.SUCCESS;
	}

	public static void startCombat(Entity attacker, Entity victim) {
		CombatCycleEvent combatEvent = new CombatCycleEvent(attacker, victim);
		attacker.setCombatingEntity(victim);
        attacker.getUpdateFlags().faceEntity(victim.getFaceIndex());
    	attacker.getUpdateFlags().sendFaceToDirection(victim.getPosition());
    	attacker.setFollowingEntity(victim);
		attacker.setSkilling(combatEvent);
		if (attacker.isNpc()) {
			Npc npc = (Npc) attacker;
			if (npc.getTransformTimer() < 1 && npc.isTransformOnAggression() > 0) {
				npc.sendTransform(npc.isTransformOnAggression(), 999999);
			}
		}
		if (attacker.isPlayer()) {
			Player player = (Player) attacker;
			if (player.transformNpc == 2626 || (player.transformNpc >= 3689 && player.transformNpc <= 3694)) {
				player.transformNpc = -1;
				player.getActionSender().sendSideBarInterfaces();
				player.setAppearanceUpdateRequired(true);
			}
		}
		//attacker.setFollowDistance(DistanceCheck.getDistanceForCombatType(attacker));
		//if (Following.withinRange(attacker, victim)) {
        //    System.out.println("WITHIN RANGE!");
		//	attacker.getMovementHandler().reset();
		//}
		CycleEventHandler.getInstance().addEvent(attacker, attacker.getSkilling(), 1);
	}

	public enum CanAttackResponse {
		CANT_ATTACK, WILD_LEVEL, NOT_IN_COMBAT_AREA, ALREADY_IN_COMBAT, INCORRECT_DUEL_OPPONENT, FAIL, SUCCESS
	}
}
