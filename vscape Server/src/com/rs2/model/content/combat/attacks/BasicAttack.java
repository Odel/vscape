package com.rs2.model.content.combat.attacks;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.AttackScript;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.AttackUsableResponse;
import com.rs2.model.content.combat.DistanceCheck;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.impl.PoisonEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.util.Misc;
import com.rs2.util.requirement.ExecutableRequirement;
import com.rs2.util.requirement.Requirement;

/**
 *
 */
public abstract class BasicAttack extends AttackScript {

	private Requirement[] requirements;
	private HitDef[] hits;
	private Graphic graphic;
	private int animation, attackDelay;

	public BasicAttack(Entity attacker, Entity victim) {
		super(attacker, victim);
		this.animation = -1;
		this.attackDelay = -1;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public int execute(CycleEventContainer container) {
		if (requirements != null) {
			for (Requirement requirement : requirements) {
				if (requirement instanceof ExecutableRequirement)
					((ExecutableRequirement) requirement).execute(getAttacker());
			}
		}
        /*if(getAttacker().isPlayer() && getVictim().isNpc()){
             Player player = (Player) getAttacker();
             player.getSlayer().needToFinishOffMonster(((Npc)getVictim()), true);
        }*/
		if (getAttacker().isPlayer() && getVictim().isPlayer()) {
			Player player = (Player) getAttacker();
			switch (new WeaponAttack(player, getVictim(), Weapon.getWeapon(new Item(player.getEquipment().getId(Constants.WEAPON)))).getAttackStyle().getAttackType()) {
				case MELEE :
					if (!(this instanceof SpellAttack) && RulesData.NO_MELEE.activated(player) && player.getEquipment().getItemContainer().get(Constants.WEAPON) != null) {
						player.getActionSender().sendMessage("Melee attacks have been disabled during this fight!");
						player.getTask();
						return attackDelay;
					}
					break;
				case RANGED :
					if (RulesData.NO_RANGED.activated(player)) {
						player.getActionSender().sendMessage("Ranged attacks have been disabled during this fight!");
						player.getTask();
						return attackDelay;
					}
					break;
			}
			if (animation != 1818) { // teleother
				((Player) getAttacker()).addPossibleSkull((Player)getVictim());
			}
		}
		if (graphic != null)
			getAttacker().getUpdateFlags().sendGraphic(graphic.getId(), graphic.getValue());

		if (getAttacker().isPlayer()) {
			Player player = (Player) getAttacker();
			if (player.transformNpc > 1) {
				animation = new Npc(player.transformNpc).getDefinition().getAttackAnim();
			}
		}
		if (animation != -1)
			getAttacker().getUpdateFlags().sendAnimation(animation);
		if (hits != null) {
			for (HitDef hitDef : getHits()) {
				if(hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() != AttackType.MAGIC)
				{
					if(getAttacker() != null && getAttacker().isNpc() && getVictim() != null && getVictim().isPlayer()){
			            Player player = (Player) getVictim();
			            player.getCombatSounds().npcAttackSound(((Npc) getAttacker()));
			        }
				}
				if (getAttacker().isNpc() && ((Npc)getAttacker()).getDefinition().isPoisonous() && Misc.random(3) == 0) {
					hitDef.addEffect(new PoisonEffect(6.0));
				}
				Hit hit = new Hit(getAttacker(), getVictim(), hitDef);
				if (getVictim().isNpc()) {
					Npc npc = (Npc) getVictim();
					if (npc.getCombatDef().getWeakness() != null)
						npc.getCombatDef().getWeakness().modifyHit(hit, this);
				}
				hit.initialize();
			}
		}
		return attackDelay;
	}

	@Override
	public AttackUsableResponse.Type isUsable() {
		Entity attacker = getAttacker();
		Entity victim = getVictim();
		if(attacker.isPlayer() && ((Player)attacker).getCastedSpell() != null && victim.isNpc() && ((Npc)victim).getNpcId() == 745) {
		    return AttackUsableResponse.Type.SUCCESS;
		}
		if (requirements != null) {
			for (Requirement requirement : requirements) {
				if (!requirement.meets(attacker)) {
					failedRequirement();
					return AttackUsableResponse.Type.FAIL;
				}
			}
		}
		int distanceRequired = distanceRequired() + DistanceCheck.extraDistance(attacker, victim);
		if (!Following.withinRange(attacker, victim, distanceRequired)) {
			return AttackUsableResponse.Type.WAIT;
		}
		return AttackUsableResponse.Type.SUCCESS;
		/*int distanceRequired = distanceRequired();
		// check if diagonal
		if (attacker.isPlayer() && distanceRequired == 1 && victim.getSize() < 2 && !victim.isMoving()) {
			if (attacker.getPosition().getX() != victim.getPosition().getX() && attacker.getPosition().getY() != victim.getPosition().getY()) {
				return AttackUsableResponse.Type.WAIT;
			}
		}
		// if attack is far away, make sure path is clear
		if (!Misc.checkClip(attacker.getPosition(), victim.getPosition(), distanceRequired == 1))
			return AttackUsableResponse.Type.WAIT;
		return AttackUsableResponse.Type.SUCCESS;*/
	}

	public void failedRequirement() {

	}

	public void setRequirements(Requirement[] requirements) {
		this.requirements = requirements;
	}

	public Requirement[] getRequirements() {
		return requirements;
	}

	public void setHits(HitDef[] hits) {
		this.hits = hits;
	}

	public HitDef[] getHits() {
		return hits;
	}

	public void setAttackDelay(int attackDelay) {
		this.attackDelay = attackDelay;
	}

	public void setGraphic(Graphic graphic) {
		this.graphic = graphic;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
	}
    
	@SuppressWarnings("rawtypes")
	public BasicAttack addEffect(Effect effect) {
        if (hits == null)
            return this;
        for (HitDef hit : hits)
            hit.addEffect(effect);
        return this;
    }

	public static BasicAttack meleeAttack(Entity attacker, Entity victim, final AttackStyle.Mode mode, final AttackStyle.Bonus bonus, final int damage, final int delay, final int animation) {
		return new BasicAttack(attacker, victim) {
			@Override
			public int distanceRequired() {
				return 1;
			}
			@Override
			public void initialize() {
				setHits(new HitDef[]{new HitDef(new AttackStyle(AttackType.MELEE, mode, bonus), HitType.NORMAL, damage).applyAccuracy().randomizeDamage()});
				setAnimation(animation);
				setAttackDelay(delay);
			}
		};
	}

	public static BasicAttack meleeAttack(Entity attacker, Entity victim, final AttackStyle.Mode mode, final int damage, Weapon weapon) {
		int attackStyleIndex = -1;
		for (int check = 0; check < weapon.getAttackAnimations().length; check++) {
			if (weapon.getWeaponInterface().getAttackStyles()[check].getMode() == mode) {
				attackStyleIndex = check;
				break;
			}
		}
		if (attackStyleIndex == -1)
			throw new IllegalArgumentException("That weapon does not contain an attack style with the given mode!");
		AttackStyle attackStyle = weapon.getWeaponInterface().getAttackStyles()[attackStyleIndex];
		return meleeAttack(attacker, victim, mode, attackStyle.getBonus(), damage, weapon.getAttackDelay(), weapon.getAttackAnimations()[attackStyleIndex]);
	}

		@SuppressWarnings("rawtypes")
	public static BasicAttack projectileAttack(Entity attacker, Entity victim, final AttackType attackType, final AttackStyle.Mode mode, final int damage, final int delay, final int animation, final Graphic startGfx, final Graphic endGfx, final int projectileId, final ProjectileTrajectory trajectory, final int addedHitDelay, final Effect effects) {
		final Entity attackerFinal = attacker;
		return new BasicAttack(attacker, victim) {
			@Override
			public int distanceRequired() {
				if(attackerFinal.isNpc() && ((Npc)attackerFinal).getNpcId() >= 1351 && ((Npc)attackerFinal).getNpcId() < 1357) {
				    return 5;
				}
				if (mode == AttackStyle.Mode.DRAGONFIRE)
					return 1;
				if (mode == AttackStyle.Mode.DRAGONFIRE_FAR)
					return 3;
				if (attackType == AttackType.MAGIC || mode == AttackStyle.Mode.MELEE_FAR)
					return 10;
				return mode == AttackStyle.Mode.LONGRANGE ? 10 : 8;
			}

			@Override
			public void initialize() {
				ProjectileDef projectileDef = trajectory != null && projectileId != -1 ? new ProjectileDef(projectileId, trajectory) : null;
				if(attackerFinal != null && attackerFinal.isNpc() && ((Npc)attackerFinal).getNpcId() >= 1351 && ((Npc)attackerFinal).getNpcId() < 1357) {
				    setHits(new HitDef[]{new HitDef(new AttackStyle(attackType, mode, attackType == AttackType.RANGED ? AttackStyle.Bonus.RANGED : AttackStyle.Bonus.MAGIC), HitType.NORMAL, damage).setProjectile(projectileDef).setStartingHitDelay(addedHitDelay).setHitGraphic(endGfx).applyAccuracy().randomizeDamage()
				    , new HitDef(new AttackStyle(attackType, mode, attackType == AttackType.RANGED ? AttackStyle.Bonus.RANGED : AttackStyle.Bonus.MAGIC), HitType.NORMAL, damage).setProjectile(projectileDef).setStartingHitDelay(addedHitDelay).setHitGraphic(endGfx).applyAccuracy().randomizeDamage()});
				}
				else {
				    setHits(new HitDef[]{new HitDef(new AttackStyle(attackType, mode, attackType == AttackType.RANGED ? AttackStyle.Bonus.RANGED : AttackStyle.Bonus.MAGIC), HitType.NORMAL, damage).setProjectile(projectileDef).setStartingHitDelay(addedHitDelay).setHitGraphic(endGfx).applyAccuracy().randomizeDamage()});
				}
				if (effects != null)
				    addEffect(effects);
				setAnimation(animation);
				setAttackDelay(delay);
				setGraphic(startGfx);
			}
		};
	}
	
	public static BasicAttack jadRangeAttack(final Entity attacker, final Entity victim, final AttackType attackType, final AttackStyle.Mode mode, final int damage, final int delay, final int animation, final Graphic startGfx, final Graphic endGfx, final int projectileId, final ProjectileTrajectory trajectory, final int addedHitDelay) {
		return new BasicAttack(attacker, victim) {
			@Override
			public int distanceRequired() {
				return 8;
			}

			@Override
			public void initialize() {
				ProjectileDef projectileDef = trajectory != null && projectileId != -1 ? new ProjectileDef(projectileId, trajectory) : null;
				setHits(new HitDef[]{new HitDef(new AttackStyle(attackType, mode, AttackStyle.Bonus.RANGED), HitType.NORMAL, damage).setProjectile(projectileDef).setStartingHitDelay(addedHitDelay).setHitGraphic(endGfx).applyAccuracy().randomizeDamage()});
				setAnimation(animation);
				setAttackDelay(delay);
			}
		};
	}
	
   public static BasicAttack projectileAttack(Entity attacker, Entity victim, final AttackType attackType, final AttackStyle.Mode mode, final int damage, final int delay, final int animation, final Graphic startGfx, final Graphic endGfx, final int projectileId, final ProjectileTrajectory trajectory) {
        return projectileAttack(attacker, victim, attackType, mode, damage,  delay, animation, startGfx, endGfx, projectileId, trajectory, 0, null);
    }
   /*
   public static BasicAttack projectileAttack(Entity attacker, Entity victim, final AttackType attackType, final AttackStyle.Mode mode, final int damage, final int delay, final int animation, final Graphic startGfx, final Graphic endGfx, final int projectileId, final ProjectileTrajectory trajectory, final int addedHitDelay) {
        return projectileAttack(attacker, victim, attackType, mode, damage,  delay, animation, startGfx, endGfx, projectileId, trajectory, addedHitDelay);
    }

	public static BasicAttack projectileAttack(Entity attacker, Entity victim, final AttackType attackType, final AttackStyle.Mode mode, final int damage, final int delay, final int animation, final Graphic startGfx, final Graphic endGfx, final int projectileId, final ProjectileTrajectory trajectory) {
		return projectileAttack(attacker, victim, attackType, mode, damage, delay, animation, startGfx, endGfx, projectileId, trajectory, 0);
	}

	@SuppressWarnings("rawtypes")
	public static BasicAttack projectileAttack(Entity attacker, Entity victim, final AttackType attackType, final AttackStyle.Mode mode, final int damage, final int delay, final int animation, final Graphic startGfx, final Graphic endGfx, final int projectileId, final ProjectileTrajectory trajectory, final Effect effect) {
		return projectileAttack(attacker, victim, attackType, mode, damage, delay, animation, startGfx, endGfx, projectileId, trajectory, 0).addEffect(effect);
	}
*/
	public static BasicAttack rangedAttack(Entity attacker, Entity victim, final AttackStyle.Mode mode, final int damage, Weapon weapon, RangedAmmo ammo) {
		if (weapon.getAmmoType() == null)
			throw new IllegalArgumentException("You cannot use weapons without ranged ammo types for npc ranged attacks!");
		int delay = weapon.getAttackDelay();
		if (mode == AttackStyle.Mode.RAPID)
			delay -= 1;
		int animation = -1;
		for (int style = 0; style < weapon.getWeaponInterface().getAttackStyles().length; style++) {
			if (weapon.getWeaponInterface().getAttackStyles()[style].getMode() == mode) {
				animation = weapon.getAttackAnimations()[style];
				break;
			}
		}
		if (animation == -1)
			throw new IllegalArgumentException("Animation not found, weapon must not contain valid attack style with the given mode!");
		Graphic startGfx = ammo.getGraphicId() != -1 ? new Graphic(ammo.getGraphicId(), weapon.getAmmoType().getGraphicHeight()) : null;
		return projectileAttack(attacker, victim, AttackType.RANGED, mode, damage, delay, animation, startGfx, null, ammo.getProjectileId(), weapon.getAmmoType().getProjectileTrajectory());
	}

	

	public static BasicAttack magicAttack(Entity attacker, Entity victim, Spell spell) {
		return new SpellAttack(attacker, victim, spell);
	}

	@SuppressWarnings("rawtypes")
	public static BasicAttack magicAttack(Entity attacker, Entity victim, Spell spell, Effect effect) {
		return new SpellAttack(attacker, victim, spell).addEffect(effect);
	}

}
