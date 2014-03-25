package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.players.Player;

@SuppressWarnings("rawtypes")
public class DisableProtectionEffect extends Effect {

	private int disabledPrayersTicks;
	public DisableProtectionEffect(int ticks) {
		this.disabledPrayersTicks = ticks;
	}

	@Override
	public void onExecution(Hit hit, EffectTick tick) {
	}

	@Override
	public boolean isCompatible(Entity entity) {
		return true;
	}

	@Override
	public void onInit(Hit hit, EffectTick tick) {
		Entity victim = hit.getVictim();
		if (victim.isPlayer()) {
			((Player) victim).getActionSender().sendMessage("You have been injured!");
			// disable protect prayers
			((Player) victim).setStopProtectPrayer(System.currentTimeMillis() + disabledPrayersTicks);
			((Player) victim).getPrayer().unactivatePrayer(Prayer.PROTECT_FROM_MAGIC);
			((Player) victim).getPrayer().unactivatePrayer(Prayer.PROTECT_FROM_RANGED);
			((Player) victim).getPrayer().unactivatePrayer(Prayer.PROTECT_FROM_MELEE);
		}
	}

	@Override
	public EffectTick generateTick(Entity attacker, Entity victim) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof DisableProtectionEffect;
	}

}
