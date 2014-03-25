package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.players.Player;

/**
 *
 */
public class PoisonEffect extends Effect<PoisonEffect.PoisonTick> {

	private double maxPoison;
	private boolean displayMessage;

	public PoisonEffect(double maxPoison) {
		this.maxPoison = maxPoison;
		this.displayMessage = true;
	}

	public PoisonEffect(double maxPoison, boolean displayMessage) {
		this.maxPoison = maxPoison;
		this.displayMessage = displayMessage;
	}

	@Override
	public void onExecution(Hit hit, PoisonTick tick) {

	}

	@Override
	public boolean isCompatible(Entity entity) {
		return entity.getPoisonImmunity().completed();
	}

	@Override
	public void onInit(Hit hit, PoisonTick tick) {
		Entity victim = hit.getVictim();
		if (victim.isPlayer() && displayMessage)
			((Player) victim).getActionSender().sendMessage("You've been poisoned.");
		victim.setPoisonDamage(maxPoison);
		tick.execute();
	}

	@Override
	public PoisonTick generateTick(Entity attacker, Entity victim) {
		return new PoisonTick(this, attacker, victim);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof PoisonEffect;
	}

	public class PoisonTick extends EffectTick<PoisonEffect> {

		private double poison;

		public PoisonTick(PoisonEffect effect, Entity attacker, Entity victim) {
			super(30, effect, attacker, victim);
			this.poison = effect.maxPoison;
		}

		@Override
		public void execute() {
            Entity attacker = getAttacker().getEntity();
            Entity victim = getVictim().getEntity();
			@SuppressWarnings("unused")
			PoisonEffect effect = getEffect(); 
			if (victim == null) {
				stop();
				return;
			}
			if (poison > 0) {
				HitDef hitDef = new HitDef(null, HitType.POISON, Math.ceil(poison)).setStartingHitDelay(1).setUnblockable(true).setDoBlock(false);
				Hit hit = new Hit(attacker, victim, hitDef);
				hit.initialize();
				poison -= .2;
				victim.setPoisonDamage(poison);
			}
			if (poison <= 0)
				stop();
		}

	}
}
