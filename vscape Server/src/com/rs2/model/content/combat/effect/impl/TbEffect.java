package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.players.Player;

/**
 *  Freezes on initialization
 */
@SuppressWarnings("rawtypes")
public class TbEffect extends Effect<EffectTick> {

	private int teleblockTime;

	public TbEffect(int teleblockTime) {
		this.teleblockTime = teleblockTime;
	}

	@Override
	public void onExecution(Hit hit, EffectTick tick) {
	}

	@Override
	public boolean isCompatible(Entity entity) {
		return !entity.isTeleblocked();
	}

	@Override
	public void onInit(Hit hit, EffectTick tick) {
		Entity victim = hit.getVictim();
		Entity attacker = hit.getAttacker();
        if(victim.isPlayer()) ((Player)victim).getActionSender().sendMessage("You have been teleblocked!");
		if (victim.isProtectingFromCombat(AttackType.MAGIC, attacker)) {
			victim.getTeleblockTimer().setWaitDuration(teleblockTime / 2);
		} else {
			victim.getTeleblockTimer().setWaitDuration(teleblockTime / 2);
		}
		victim.getTeleblockTimer().reset();
	}

	@Override
	public EffectTick generateTick(Entity attacker, Entity victim) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof TbEffect;
	}

}
