package com.rs2.model.content.combat.effect;

import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.Hit;

/**
 *
 */
@SuppressWarnings("rawtypes")
public abstract class Effect<T extends EffectTick> {

	private boolean initialized;
	private T t;
	public final void initialize(Hit hit) {
		t = generateTick(hit.getAttacker(), hit.getVictim());
		if (t != null) {
			hit.getVictim().addEffect(t);
			World.getTickManager().submit(t);
		}
		onInit(hit, t);
		initialized = true;
	}

	public final void execute(Hit hit) {
		if (!initialized)
			return;
		onExecution(hit, t);
	}

	public abstract void onExecution(Hit hit, T tick);

	public abstract boolean isCompatible(Entity entity);

	public abstract void onInit(Hit hit, T tick);

	public abstract T generateTick(Entity attacker, Entity victim);

	@Override
	public abstract boolean equals(Object other);
}
