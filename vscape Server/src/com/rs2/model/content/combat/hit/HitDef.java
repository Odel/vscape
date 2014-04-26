package com.rs2.model.content.combat.hit;

import java.util.LinkedList;
import java.util.List;

import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.combat.weapon.AttackStyle.Mode;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

/**
 *
 */
public class HitDef {

	private int hitAnimation;
	private int damage;
	private int victimAnimation;
	private HitType hitType;
	private AttackStyle attackStyle;
	private Graphic hitGraphic;
	private ProjectileDef projectileDef;
	private int startingHitDelay, specialEffect;
	private boolean checkAccuracy, randomizeDamage, unblockable;
    private byte radius;
    private boolean firstHit;
    private boolean block;

	@SuppressWarnings("rawtypes")
	private List<Effect> effects = new LinkedList<Effect>();
	private Item dropItem;
	private int damageDelay;
	private double specAccuracy = 1.0;

	public HitDef(AttackStyle attackStyle, HitType hitType, int damage) {
		this.attackStyle = attackStyle;
		this.hitType = hitType;
		this.damage = damage;
		this.hitAnimation = -1;
		this.hitGraphic = null;
		this.startingHitDelay = 0;
		this.damageDelay = 0;
		this.unblockable = false;
		this.firstHit = true;
		this.block = true;
	}

	@Override
	public HitDef clone() {
		HitDef clone = new HitDef(attackStyle, hitType, damage);
		clone.hitAnimation = hitAnimation;
		clone.hitGraphic = hitGraphic;
		clone.projectileDef = projectileDef;
		clone.startingHitDelay = startingHitDelay;
		clone.dropItem = dropItem;
		clone.damageDelay = damageDelay; 
        clone.radius = radius;
        clone.victimAnimation = victimAnimation;
        clone.unblockable = unblockable;
        clone.firstHit = firstHit;
        clone.block = block;
		if (shouldCheckAccuracy())
			clone.applyAccuracy(getSpecAccuracy());
		if (shouldRandomizeDamage())
			clone.randomizeDamage();
		if (getEffects() != null && getEffects().size() > 0) {
	        clone.addEffects(getEffects().toArray(new Effect[getEffects().size()]));
		}
		return clone;
	}

	public HitDef(AttackStyle attackStyle, HitType hitType, double damage) {
		this(attackStyle, hitType, (int) Math.floor(damage));
	}

	public HitDef setDamageDelay(int damageDelay) {
		this.damageDelay = damageDelay;
		return this;
	}

	public int getDamageDelay() {
		return damageDelay;
	}

	@SuppressWarnings("rawtypes")
	public HitDef addEffects(Effect[] addedEffects) {
		if (addedEffects != null) {
			for (Effect effect : addedEffects) {
				if (effect == null)
					continue;
				this.effects.add(effect);
			}
		}
		return this;
	}
    
    @SuppressWarnings("rawtypes")
	public HitDef addEffect(Effect effect) {
        this.effects.add(effect);
        return this;
    }

	@SuppressWarnings("rawtypes")
	public List<Effect> getEffects() {
		return effects;
	}

	public HitDef removeEffects() {
        this.effects = null;
        return this;
    }

	public HitDef randomizeDamage() {
		if (damage == -1)
			return this;
		setRandomizeDamage(true);
		return this;
	}

	public HitDef applyAccuracy(double accuracy) {
		if (attackStyle == null)
			return this;
		setCheckAccuracy(true);
		this.checkAccuracy = true;
		setSpecAccuracy(accuracy);
		return this;
	}

	public HitDef setSpecialEffect(int effect) {
		this.specialEffect = effect;
		return this;
	}

	public HitDef setDroppedItem(Item droppedItem) {
		this.dropItem = droppedItem;
		return this;
	}

	public HitDef applyAccuracy() {
		return applyAccuracy(1);
	}

	public HitDef setUnblockable(boolean unblockable) {
		this.unblockable = unblockable;
		return this;
	}

	public boolean isUnblockable() {
		if (getAttackStyle() != null && (getAttackStyle().getMode() == Mode.DRAGONFIRE || getAttackStyle().getMode() == Mode.DRAGONFIRE_FAR)) {
			return true;
		}
		return unblockable;
	}

	public HitDef setHitGraphic(Graphic g) {
		this.hitGraphic = g;
		return this;
	}

	public HitDef setStartingHitDelay(int startingHitDelay) {
		this.startingHitDelay = startingHitDelay;
		return this;
	}

	public HitDef setProjectile(ProjectileDef projectileDef) {
		this.projectileDef = projectileDef;
		return this;
	}

	public AttackStyle getAttackStyle() {
		return attackStyle;
	}

	public ProjectileDef getProjectileDef() {
		return projectileDef;
	}

	public Graphic getHitGraphic() {
		return hitGraphic;
	}

	public Item getDropItem() {
		return dropItem;
	}

	public int getHitAnimation() {
		return hitAnimation;
	}

	public int getDamage() {
		return damage;
	}

	public HitType getHitType() {
		return hitType;
	}

	public int getStartingHitDelay() {
		return startingHitDelay;
	}

	public boolean shouldCheckAccuracy() {
		return checkAccuracy;
	}

	public int getSpecialEffect() {
		return this.specialEffect;
	}

	public int calculateHitDelay(Position start, Position end) {
		int hitDelay = startingHitDelay;
		if (start != null && projectileDef != null) {
			int distance = Misc.getDistance(start, end);
			ProjectileTrajectory trajectory = projectileDef.getProjectileTrajectory();
			double delay = trajectory.getDelay() + trajectory.getSlowness() + distance * 5d;
			delay = Math.ceil((delay * 12d) / 600d);
			if (distance > 1)
				delay += 1;
			hitDelay += (int) delay;
		} else
			hitDelay += 1;
		return hitDelay;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setSpecAccuracy(double accuracy) {
		this.specAccuracy = accuracy;
	}

	public double getSpecAccuracy() {
		return specAccuracy;
	}

	public HitDef setCheckAccuracy(boolean checkAccuracy) {
		this.checkAccuracy = checkAccuracy;
		return this;
	}

	public boolean shouldRandomizeDamage() {
		return randomizeDamage;
	}

	public HitDef setRandomizeDamage(boolean randomizeDamage) {
		this.randomizeDamage = randomizeDamage;
		return this;
	}

	public HitDef reduceDamagePercentage(double percent) {
		this.damage = (int) (this.damage * percent);
		return this;
	}

    public HitDef setRadius(byte radius) {
        this.radius = radius;
        return this;
    }
    
    public byte getRadius() {
        return radius;
    }

	public HitDef setVictimAnimation(int anim) {
		this.victimAnimation = anim;
		return this;
	}

	public int getVictimAnimation() {
		return victimAnimation;
	}

	/**
	 * @param firstHit the firstHit to set
	 */
	public HitDef setFirstHit(boolean firstHit) {
		this.firstHit = firstHit;
		return this;
	}

	/**
	 * @return the firstHit
	 */
	public boolean isFirstHit() {
		return firstHit;
	}

	/**
	 * @param block the block to set
	 */
	public HitDef setDoBlock(boolean block) {
		this.block = block;
		return this;
	}

	/**
	 * @return the block
	 */
	public boolean doBlockAnim() {
		return block;
	}

}
