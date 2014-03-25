package com.rs2.model.content.combat;

import com.rs2.model.Entity;

/**
 *
 */
public interface CombatScript {

	public static final CombatScript DEFAULT_COMBAT_SCRIPT = new DefaultCombatScript();

	public AttackScript[] generateAttacks(Entity attacker, Entity victim, int distance);

}
