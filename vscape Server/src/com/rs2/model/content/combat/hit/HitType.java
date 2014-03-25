package com.rs2.model.content.combat.hit;

/**
 *
 */
public enum HitType {
	NORMAL(1), POISON(2), BURN(3), MISS(0);

	private int intValue;
	HitType(int value) {
		intValue = value;
	}

	public int toInteger() {
		return intValue;
	}
}
