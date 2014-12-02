package com.rs2.model.content.combat.effect.impl;

import com.rs2.model.Entity;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.EffectTick;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;

public class BurnEffect extends Effect<BurnEffect.BurnTick> {

	private double maxDamage;
	private int tick = 10;

	public BurnEffect(double maxDamage)
	{
		this.maxDamage = maxDamage;
	}
	
	public BurnEffect(double maxDamage, int tick)
	{
		this.maxDamage = maxDamage;
		this.tick = tick;
	}
	
	@Override
	public void onExecution(Hit hit, BurnTick tick) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCompatible(Entity entity) {
		return true;
	}

	@Override
	public void onInit(Hit hit, BurnTick tick) {
		tick.execute();
	}

	@Override
	public BurnTick generateTick(Entity attacker, Entity victim) {
		return new BurnTick(this, attacker, victim);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof BurnEffect;
	}
	
	public class BurnTick extends EffectTick<BurnEffect> {

		private double damage;

		public BurnTick(BurnEffect effect, Entity attacker, Entity victim) {
			super(effect.tick, effect, attacker, victim);
			this.damage = effect.maxDamage;
		}

		@Override
		public void execute() {
            Entity attacker = getAttacker().getEntity();
            Entity victim = getVictim().getEntity();
			@SuppressWarnings("unused")
			BurnEffect effect = getEffect(); 
			if (victim == null) {
				stop();
				return;
			}
			if (victim.isDead()) {
				stop();
				return;
			}
			if (damage > 0) {
				HitDef hitDef = new HitDef(null, HitType.BURN, Math.ceil(damage)).setStartingHitDelay(-1).setUnblockable(true).setDoBlock(false);
				Hit hit = new Hit(attacker, victim, hitDef);
				hit.initialize();
			}
			if(damage <= 0)
			{
				stop();
			}
		}

	}
}
