package com.rs2.model.content.combat;

import com.rs2.model.Entity;
import com.rs2.model.tick.TickTimer;

/**
 *
 */
public class InCombatTick extends TickTimer {
	@SuppressWarnings("unused")
	private Entity entity, other;

	public InCombatTick(Entity entity, int ticks) {
		super(ticks);
		this.entity = entity;
	}

	public void update(Entity other, int ticks) {
		this.other = other;
		setWaitDuration(ticks);
		reset();
	}

	public Entity getOther() {
		return other;
	}
}
