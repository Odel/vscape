package com.rs2.model.npcs;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.AttackScript;
import com.rs2.model.content.combat.attacks.SpellAttack;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.skills.magic.Spell;

/**
 *
 */
public enum NpcWeakness {

	CRUMBLE_UNDEAD {
		@Override
		public void modifyHit(Hit hit, AttackScript attackScript) {
			Entity attacker = hit.getAttacker();
			if (attacker == null || !attacker.isPlayer())
				return;
			if (attackScript instanceof SpellAttack) {
				SpellAttack spellAttack = (SpellAttack) attackScript;
				if (spellAttack.getSpell() == Spell.CRUMBLE_UNDEAD) {
					hit.setDamage(hit.getDamage() + 10);
				}
			}
		}
	};

	public abstract void modifyHit(Hit hit, AttackScript attackScript);

}
