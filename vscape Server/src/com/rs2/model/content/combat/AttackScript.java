package com.rs2.model.content.combat;

import com.rs2.model.Entity;
import com.rs2.model.tick.CycleEventContainer;

public abstract class AttackScript {

	private Entity attacker, victim;

	public AttackScript(Entity attacker, Entity victim) {
		this.attacker = attacker;
		this.victim = victim;
	}

	public Entity getAttacker() {
		return attacker;
	}

	public Entity getVictim() {
		return victim;
	}

	/**
	 * The distance required for the attack
	 * 
	 * @return the distance
	 */
	public abstract int distanceRequired();

	/**
	 * Execute the attack, remove items
	 * 
	 * @return the delay after the attack
	 */
	public abstract int execute(CycleEventContainer container);

	/**
	 * If the attack is usable If the player has runes/arrow/etc
	 * 
	 * @return usable response
	 */
	public abstract AttackUsableResponse.Type isUsable();

	public abstract void initialize();

}
