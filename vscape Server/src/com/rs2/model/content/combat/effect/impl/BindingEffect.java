package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;

/**
 *  Freezes on initialization
 */
@SuppressWarnings("rawtypes")
public class BindingEffect extends Effect<EffectTick> {

	private int frozenTime;

	public BindingEffect(int frozenTime) {
		this.frozenTime = frozenTime;
	}

	@Override
	public void onExecution(Hit hit, EffectTick tick) {
	}

	@Override
	public boolean isCompatible(Entity entity) {
		return entity.getFrozenImmunity().completed();
	}

	@Override
	public void onInit(Hit hit, EffectTick tick) {
		Entity victim = hit.getVictim();
		victim.getMovementHandler().reset();
		victim.getFrozenTimer().setWaitDuration(frozenTime);
		victim.getFrozenTimer().reset();
		victim.getFrozenImmunity().setWaitDuration(frozenTime + 6);
		victim.getFrozenImmunity().reset();
	}

	@Override
	public EffectTick generateTick(Entity attacker, Entity victim) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof BindingEffect;
	}
    
    public int getFrozenTime() {
        return frozenTime;
    }
}
