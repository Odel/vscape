package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;

/**
 *
 */
@SuppressWarnings("rawtypes")
public class LeechEffect extends Effect {

	private double leechAmount;

	public LeechEffect(double leechAmount) {
		this.leechAmount = leechAmount;
	}

	@Override
	public void onExecution(Hit hit, EffectTick tick) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public boolean isCompatible(Entity entity) {
		return true;
	}

	@Override
	public void onInit(Hit hit, EffectTick tick) {
		int leech = (int) (hit.getDamage() * leechAmount);
		hit.getAttacker().heal(leech);
	}

	@Override
	public EffectTick generateTick(Entity attacker, Entity victim) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof LeechEffect;
	}
}
