package com.rs2.model.content.combat;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.attacks.SpellAttack;
import com.rs2.model.content.combat.attacks.WeaponAttack;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;

/**
 *
 */
public class DefaultCombatScript implements CombatScript {
	@Override
	public AttackScript[] generateAttacks(Entity attacker, Entity victim, int distance) {
		if (attacker.isPlayer()) {
			Player player = (Player) attacker;
			SpecialType specialType = player.getSpecialType();
			if (player.getCastedSpell() != null)
				return new AttackScript[]{new SpellAttack(attacker, victim, player.getCastedSpell())};
			else if (player.getAutoSpell() != null && player.isAutoCasting())
				return new AttackScript[]{new SpellAttack(attacker, victim, player.getAutoSpell())};
			if (specialType != null && player.isSpecialAttackActive())
				return new AttackScript[]{specialType.getSpecialAttack(player, victim, player.getEquippedWeapon())};
			return new AttackScript[]{new WeaponAttack(player, victim, player.getEquippedWeapon())};
		} else
			return ((Npc) attacker).getCombatDef().attackScripts(attacker, victim);
	}
}
