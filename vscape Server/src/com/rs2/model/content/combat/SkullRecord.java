package com.rs2.model.content.combat;

import com.rs2.model.Entity;
import com.rs2.model.EntityRecord;
import com.rs2.model.tick.TickTimer;

/**
 *
 */
public class SkullRecord extends EntityRecord {

	public static final int PK_EXPIRE_TIME = 2000;
	public static final int ABYSS_EXPIRE_TIME = 600;

	private TickTimer expireTimer;

	public SkullRecord(Entity victim, int timer) {
		super(victim);
		this.expireTimer = new TickTimer(timer);
	}

	public boolean expired() {
		return expireTimer.completed();
	}

	public void refresh() {
		this.expireTimer.reset();
	}

	public int ticksRemaining() {
		return this.expireTimer.ticksRemaining();
	}
}
