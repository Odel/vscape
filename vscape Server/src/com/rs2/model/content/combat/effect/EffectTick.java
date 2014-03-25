package com.rs2.model.content.combat.effect;

import com.rs2.model.Entity;
import com.rs2.model.EntityRecord;
import com.rs2.model.tick.Tick;

/**
 *
 */
@SuppressWarnings("rawtypes")
public abstract class EffectTick<E extends Effect> extends Tick {

	private EntityRecord attacker, victim;
	private E effect;

	/**
	 * Creates a tickable with the specified amount of ticks.
	 * 
	 * @param ticks
	 *            The amount of ticks.
	 */
	public EffectTick(int ticks, E effect, Entity attacker, Entity victim) {
		super(ticks);
		this.effect = effect;
		this.attacker = new EntityRecord(attacker);
		this.victim = new EntityRecord(victim);
	}

	public E getEffect() {
		return effect;
	}

	public EntityRecord getAttacker() {
		return attacker;
	}

	public EntityRecord getVictim() {
		return victim;
	}

	@Override
	public void stop() {
		super.stop();
		if (getVictim().getEntity() != null)
			getVictim().getEntity().removeEffect(getEffect());
	}
}
