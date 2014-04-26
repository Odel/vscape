package com.rs2.model.content.combat;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;

public class DistanceCheck {

	/**
	 * Checks if the attacker is within distance required for attacking.
	 */
	public static int extraDistance(Entity attacker, Entity victim) {
		if (!victim.isMoving() || !attacker.isMoving()) {
			return 0;
		}
		if (victim.isRunning()) {
			return 2;
		}
		if (victim.isMoving()) {
			return 1;
		}
		return 0;
		/*boolean diagonal = attacker.getPosition().getX() != victim.getPosition().getX() && attacker.getPosition().getY() != victim.getPosition().getY();
		boolean running = victim.getSecondaryDirection() != -1;
		return running && diagonal ? 3 : diagonal || running ? 2 : 0;*/
	}

	/**
	 * Getting the distance needed for combat.
	 */
	public static int getDistanceForCombatType(Entity attacker) {
		Player player = null;
		if (attacker.isPlayer()) {
			player = (Player) attacker;
			determineAttackType(player);
		}
		switch (attacker.getAttackType()) {
			case MELEE :
				if (attacker.isPlayer()) {
					if (player.getEquippedWeapon() != null) {
						String weaponName = player.getEquippedWeapon().name().toLowerCase();
						if (weaponName.contains("halberd")) {
							return 2;
						}
					}
				}
				return 1;
			case RANGED :
				if (attacker.isPlayer()) {
					if (player.getEquippedWeapon() != null) {
						String weaponName = player.getEquippedWeapon().name().toLowerCase();
						if (weaponName.contains("longbow")) {
							return player.getFightMode() == 2 ? 11 : 10;
						} else {
							return player.getFightMode() == 2 ? 9 : 7;
						}
					}
				}
				return 7;
			case MAGIC :
				return 10;
			default :
				return 1;
		}
	}

	public static void determineAttackType(Player player) {
		if (player.getCastedSpell() != null || player.isAutoCasting()) { // Maging
			player.setAttackType(Entity.AttackTypes.MAGIC);
			return;
		}
		if (player.getEquippedWeapon() != null) {
			if (player.isUsingCrystalBow() || player.isUsingBow() || player.isUsingCross() || player.isUsingOtherRangedWeapon()) { // Ranging
				player.setAttackType(Entity.AttackTypes.RANGED);
				return;
			}
		}
		player.setAttackType(Entity.AttackTypes.MELEE);
	}

}
