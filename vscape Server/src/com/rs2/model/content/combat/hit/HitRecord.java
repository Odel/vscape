package com.rs2.model.content.combat.hit;

import com.rs2.model.Entity;
import com.rs2.model.EntityRecord;
import com.rs2.model.tick.TickTimer;

/**
 *
 */
public class HitRecord extends EntityRecord {

	private int damage;
	private TickTimer expireTimer;

	// 5 minutes
	private static final int EXPIRE_TIME = 5 * 60 * 1000 / 600;

	public HitRecord(Entity attacker) {
		super(attacker);
		this.expireTimer = new TickTimer(EXPIRE_TIME);
	}

	public boolean expired() {
		return expireTimer.completed();
	}

	public void addDamage(int damage) {
		this.damage += damage;
		this.expireTimer.reset();
	}

	public int getDamage() {
		return damage;
	}

}
