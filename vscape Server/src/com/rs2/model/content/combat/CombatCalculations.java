package com.rs2.model.content.combat;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.attacks.WeaponAttack;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.quests.impl.DemonSlayer;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import java.util.Random;

public class CombatCalculations {
	public static final Random r = new Random(System.currentTimeMillis());

	public static boolean isAccurateHit(double chance) {
		return r.nextDouble() <= chance;
	}
	
	public static double calculateMaxHit(Player player, WeaponAttack weaponAttack) {
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
		double damage = 0;
		if (attackStyle.getAttackType() == AttackType.MELEE) {
			damage = calculateMaxMeleeHit(player, weaponAttack);
		} else if (attackStyle.getAttackType() == AttackType.RANGED && weaponAttack.getRangedAmmo() != null) {
			damage = calculateMaxRangedHit(player, weaponAttack);
		}
		return damage;
	}

	public static double calculateMaxRangedHit(Player player, WeaponAttack weaponAttack) {
		int rangedLevel = player.getSkill().getLevel()[Skill.RANGED];
		double styleBonus = 0;
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
		if (attackStyle.getMode() == AttackStyle.Mode.RANGED_ACCURATE) {
			styleBonus = 3;
		} else if (attackStyle.getMode() == AttackStyle.Mode.LONGRANGE) {
			styleBonus = 1;
		} else if (player.hasFullVoidRange()) {
			rangedLevel = (int) styleBonus + (int) (rangedLevel * 1.1);
		}
		if (player.getIsUsingPrayer()[Prayer.PrayerData.SHARP_EYE.getIndex()]) {
			rangedLevel *= 1.05;
		} else if (player.getIsUsingPrayer()[Prayer.PrayerData.HAWK_EYE.getIndex()]) {
			rangedLevel *= 1.1;
		} else if (player.getIsUsingPrayer()[Prayer.PrayerData.EAGLE_EYE.getIndex()]) {
			rangedLevel *= 1.15;
		}
		rangedLevel += styleBonus;
		double rangedStrength = weaponAttack.getRangedAmmo().getRangeStrength();
		double maxHit = (rangedLevel + rangedStrength / 8 + rangedLevel * rangedStrength * Math.pow(64, -1) + 14) / 10;
		if (player.hasFullVoidRange()) {
			maxHit = maxHit * 1.1;
		}
		return (int) Math.floor(maxHit);
	}

	public static double calculateMaxMeleeHit(Player player, WeaponAttack weaponAttack) {
		double strengthLevel = player.getSkill().getLevel()[Skill.STRENGTH];
		if (player.getIsUsingPrayer()[Prayer.PrayerData.BURST_OF_STRENGTH.getIndex()]) {
			strengthLevel *= 1.05;
		} else if (player.getIsUsingPrayer()[Prayer.PrayerData.SUPERHUMAN_STRENGTH.getIndex()]) {
			strengthLevel *= 1.1;
		} else if (player.getIsUsingPrayer()[Prayer.PrayerData.ULTIMATE_STRENGTH.getIndex()]) {
			strengthLevel *= 1.15;
		}
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
		int styleBonus = 0;
		if (attackStyle.getMode() == AttackStyle.Mode.AGGRESSIVE) {
			styleBonus = 3;
		} else if (attackStyle.getMode() == AttackStyle.Mode.CONTROLLED) {
			styleBonus = 1;
		}
		int effectiveStrengthDamage = (int) (strengthLevel + styleBonus);
		double baseDamage = 5 + (effectiveStrengthDamage + 8) * (player.getBonus(10) + 64) / 64; //10 = str bonus
		if (player.hasFullVoidMelee()) {
			baseDamage = baseDamage * 1.1;
		} else if (player.hasFullDharok()) {
			double hpLost = player.getMaxHp() - player.getCurrentHp();
			baseDamage += baseDamage * hpLost * 0.01;
		} else if (DemonSlayer.fightingDemon(player)) {
			baseDamage = baseDamage * 1.75;
		}
		int maxHit = (int) Math.floor(baseDamage);
		return (int) Math.floor(maxHit / 10);
	}

	public static double getChance(Entity attacker, double attack, double defence) {
		double A = Math.floor(attack);
		double D = Math.floor(defence);
		double chance = A < D ? (A - 1.0) / (2.0 * D) : 1.0 - (D + 1.0) / (2.0 * A);
		chance = chance > 0.9999 ? 0.9999 : chance < 0.0001 ? 0.0001 : chance;
		if (attacker.isPlayer() && ((Player) attacker).hasFullVoidMage()) {
			chance = chance * 1.3 >= 1.0 ? 1.0 : chance * 1.3;
		}
		return chance;
	}

	private static double getEffectiveAccuracy(Entity attacker, AttackStyle attackStyle) {
		double attackBonus = attacker.getBonus(attackStyle.getBonus().toInteger());
		double baseAttack = attacker.getBaseAttackLevel(attackStyle.getAttackType());
		if (attackStyle.getAttackType() == AttackType.MELEE && attacker.isPlayer()) {
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[Prayer.PrayerData.CLARITY_OF_THOUGHT.getIndex()]) {
				baseAttack *= 1.05;
			} else if (player.getIsUsingPrayer()[Prayer.PrayerData.IMPROVED_REFLEXES.getIndex()]) {
				baseAttack *= 1.1;
			} else if (player.getIsUsingPrayer()[Prayer.PrayerData.INCREDIBLE_REFLEXES.getIndex()]) {
				baseAttack *= 1.15;
			} else if (player.hasFullVoidMelee()) {
				baseAttack *= 1.1;
			}
		} else if (attackStyle.getAttackType() == AttackType.RANGED && attacker.isPlayer()) {
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[Prayer.PrayerData.SHARP_EYE.getIndex()]) {
				baseAttack *= 1.05;
			} else if (player.getIsUsingPrayer()[Prayer.PrayerData.HAWK_EYE.getIndex()]) {
				baseAttack *= 1.1;
			} else if (player.getIsUsingPrayer()[Prayer.PrayerData.EAGLE_EYE.getIndex()]) {
				baseAttack *= 1.15;
			}
		} else if (attackStyle.getAttackType() == AttackType.MAGIC && attacker.isPlayer()) {
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[Prayer.PrayerData.MYSTIC_WILL.getIndex()]) {
				baseAttack *= 1.05;
			} else if (player.getIsUsingPrayer()[Prayer.PrayerData.MYSTIC_LORE.getIndex()]) {
				baseAttack *= 1.1;
			} else if (player.getIsUsingPrayer()[Prayer.PrayerData.MYSTIC_MIGHT.getIndex()]) {
				baseAttack *= 1.15;
			}
		}
		return Math.floor(baseAttack + attackBonus) + 8;
	}

	private static double getEffectiveDefence(Entity victim, AttackStyle attackStyle) {
		double baseDefence = victim.getBaseDefenceLevel(attackStyle.getAttackType());
		if ((attackStyle.getMode() == AttackStyle.Mode.MAGIC && victim.isPlayer()) || (attackStyle.getAttackType() == AttackType.MAGIC && victim.isPlayer())) {
			Player player = (Player) victim;
			return player.getBonus(8);
		}
		if (attackStyle.getAttackType() == AttackType.MELEE && victim.isPlayer()) {
			Player player = (Player) victim;
			if (player.getIsUsingPrayer()[Prayer.PrayerData.THICK_SKIN.getIndex()]) {
				baseDefence *= 1.05;
			} else if (player.getIsUsingPrayer()[Prayer.PrayerData.ROCK_SKIN.getIndex()]) {
				baseDefence *= 1.1;
			} else if (player.getIsUsingPrayer()[Prayer.PrayerData.STEEL_SKIN.getIndex()]) {
				baseDefence *= 1.15;
			}
		} else if (attackStyle.getAttackType() == AttackType.MAGIC && victim.isNpc()) {
			Npc npc = (Npc) victim;
			return npc.getDefinition().getDefenceMage();
		}
		return Math.floor(baseDefence) + 8;
	}

	public static double getDefenceRoll(Entity victim, HitDef hitDef) {
		AttackStyle attackStyle = hitDef.getAttackStyle();
		//if (victim.isNpc())
		//    return victim.getBonus(attackStyle.getBonus().toInteger() + AttackStyle.Bonus.values().length);

		double effectiveDefence = getEffectiveDefence(victim, attackStyle);
		if (victim.isPlayer() && attackStyle.getAttackType() != AttackType.MAGIC && attackStyle.getMode() != AttackStyle.Mode.MAGIC) {
			effectiveDefence += victim.getBonus(attackStyle.getBonus().toInteger() + AttackStyle.Bonus.values().length);
		}
		int styleBonusDefence = 0;
		if (victim.isPlayer()) {
			Player pVictim = ((Player) victim);
			if (attackStyle.getAttackType() == AttackType.MAGIC) {
				int level = pVictim.getSkill().getLevel()[Skill.MAGIC];
				effectiveDefence = (int) (Math.floor(level * 0.125) + Math.floor(effectiveDefence * 0.875));
				styleBonusDefence = 19;
			} else if (attackStyle.getAttackType() == AttackType.MELEE && attackStyle.getMode() == AttackStyle.Mode.MAGIC) {
				int magicLevel = pVictim.getSkill().getLevel()[Skill.MAGIC];
				int defenseLevel = pVictim.getSkill().getLevel()[Skill.DEFENCE];
				effectiveDefence = (int) (Math.floor(magicLevel * 0.0875) + Math.floor(defenseLevel * 0.0375) + Math.floor(effectiveDefence * 0.875));
				styleBonusDefence = 64;
			} else {
				AttackStyle defenceStyle = pVictim.getEquippedWeapon().getWeaponInterface().getAttackStyles()[pVictim.getFightMode()];
				if (defenceStyle.getMode() == AttackStyle.Mode.DEFENSIVE || defenceStyle.getMode() == AttackStyle.Mode.LONGRANGE) {
					styleBonusDefence += 3;
				} else if (defenceStyle.getMode() == AttackStyle.Mode.CONTROLLED) {
					styleBonusDefence += 1;
				}
			}
		}
		effectiveDefence *= (1 + (styleBonusDefence) / 64);
		if (hitDef.getSpecialEffect() == 11) { //Verac set effect
			effectiveDefence *= 0.75;
		}
		if (effectiveDefence < 0) {
			effectiveDefence = 0;
		}
		return effectiveDefence;
	}

	public static double getAttackRoll(Entity attacker, HitDef hitDef) {
		AttackStyle attackStyle = hitDef.getAttackStyle();
		//if (attacker.isNpc())
		//    return attacker.getBonus(attackStyle.getBonus().toInteger());*/

		double specAccuracy = hitDef.getSpecAccuracy();
		double effectiveAccuracy = getEffectiveAccuracy(attacker, attackStyle);

		int styleBonusAttack = 0;
		if (attackStyle.getMode() == AttackStyle.Mode.MELEE_ACCURATE || attackStyle.getMode() == AttackStyle.Mode.RANGED_ACCURATE) {
			styleBonusAttack = 3;
		} else if (attackStyle.getMode() == AttackStyle.Mode.CONTROLLED) {
			styleBonusAttack = 1;
		} else if (attackStyle.getMode() == AttackStyle.Mode.MAGIC && attacker.isPlayer()) {
			Player player = (Player) attacker;
			styleBonusAttack = (int) ((player.getSkill().getLevel()[Skill.MAGIC] + player.getBonus(3)) / 82.5);
			specAccuracy = 1;
		}
		effectiveAccuracy *= (1 + (styleBonusAttack) / 64);
		return (int) (effectiveAccuracy * specAccuracy);
	}
}
