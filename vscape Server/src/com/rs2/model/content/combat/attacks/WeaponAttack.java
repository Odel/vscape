package com.rs2.model.content.combat.attacks;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.AttackUsableResponse;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.effect.impl.PoisonEffect;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.combat.weapon.RangedAmmo;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.util.Misc;
import com.rs2.util.requirement.EquipmentRequirement;
import com.rs2.util.requirement.Requirement;

/**
 *
 */
public class WeaponAttack extends BasicAttack {

	private Weapon weapon;
	private int attackStyleIndex;
	private AttackStyle attackStyle;
	private RangedAmmo rangedAmmo;
	private SpecialType specialType;
	private boolean failedInitialize;
	private PoisonEffect poisonEffect;
	private int dropId;

	public static final PoisonEffect NORMAL_POISON_MELEE = new PoisonEffect(4.0), NORMAL_POISON_RANGED = new PoisonEffect(2.0), EXTRA_POISON_MELEE = new PoisonEffect(5.0), EXTRA_POISON_RANGED = new PoisonEffect(3.0), SUPER_POISON_MELEE = new PoisonEffect(6.0), SUPER_POISON_RANGED = new PoisonEffect(4.0), KARAMBWAN_PASTE = new PoisonEffect(6.0);

	public WeaponAttack(Player player, Entity victim, Weapon weapon) {
		super(player, victim);
		this.weapon = weapon;
		this.attackStyleIndex = ((Player) getAttacker()).getFightMode();
		setAttackStyle(weapon.getWeaponInterface().getAttackStyles()[attackStyleIndex]);
		this.dropId = -1;
	}

	public RangedAmmo getRangedAmmo() {
		return rangedAmmo;
	}

	public int generateAnimation() {
		return weapon.getAttackAnimations()[attackStyleIndex];
	}

	public double generateMaxHit() {
		return CombatManager.calculateMaxHit((Player) getAttacker(), this);
	}

	public void setAttackStyle(AttackStyle attackStyle) {
		this.attackStyle = attackStyle;
	}

	public AttackStyle getAttackStyle() {
		return attackStyle;
	}

	public int generateHitDelay() {
		int delayModifier = 0;
		AttackStyle style = getAttackStyle();
		if (style.getMode() == AttackStyle.Mode.RAPID)
			delayModifier = -1;
		return weapon.getAttackDelay() + delayModifier;
	}

	public Graphic generateGraphic() {
		if (rangedAmmo != null && weapon.getAmmoType() != null && weapon == Weapon.DARK_BOW && getAttacker().isPlayer() ) {
		    return new Graphic(RangedAmmo.getDarkBowPullGfx(rangedAmmo), 90);
		}
		else if (rangedAmmo != null && weapon.getAmmoType() != null && weapon != Weapon.DARK_BOW) {
			return new Graphic(rangedAmmo.getGraphicId(), weapon.getAmmoType().getGraphicHeight());
		}
		else
		    return null;
	}

	public final HitDef[] generateHits() {
		double maxDamage = generateMaxHit();
		ProjectileDef projectile = null;
		if(rangedAmmo != null && weapon == Weapon.DARK_BOW) {
		    int projectileId = rangedAmmo.getProjectileId();
		    ProjectileDef firstProjectile = new ProjectileDef(projectileId, ProjectileTrajectory.DOUBLE_ARROW1);
		    ProjectileDef secondProjectile = new ProjectileDef(projectileId, ProjectileTrajectory.DOUBLE_ARROW2);
		    HitDef[] hitDef = new HitDef[]{new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().applyAccuracy(1.0).setProjectile(firstProjectile).setCheckAccuracy(true), new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().applyAccuracy(1.0).setProjectile(secondProjectile).setCheckAccuracy(true)};
		    return hitDef;
		}
		else if(rangedAmmo != null && rangedAmmo == RangedAmmo.DRAGON_DART) {
		    projectile = new ProjectileDef(rangedAmmo.getProjectileId(), weapon.getAmmoType().getProjectileTrajectory());
		    HitDef hitDef = new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().applyAccuracy(1.1).setProjectile(projectile);
		    return new HitDef[]{hitDef};
		}
		else if (rangedAmmo != null && weapon != Weapon.DARK_BOW) { 
		    projectile = new ProjectileDef(rangedAmmo.getProjectileId(), weapon.getAmmoType().getProjectileTrajectory());
		    HitDef hitDef = new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().applyAccuracy().setProjectile(projectile);
		    return new HitDef[]{hitDef};
		}
		else {
		    HitDef hitDef = new HitDef(getAttackStyle(), HitType.NORMAL, maxDamage).randomizeDamage().applyAccuracy();
		    return new HitDef[]{hitDef};
		}
	}

	@Override
	public final void initialize() {
		Player player = ((Player) getAttacker());
		if (getAttackStyle().getAttackType() == AttackType.MELEE) {
			if (getVictim().isDoorSupport()) {
				failedInitialize = true;
				CombatManager.resetCombat(player);
				return;
			}
		}
		String weaponName = player.getEquippedWeapon().name().toLowerCase();
		if (getAttackStyle().getAttackType() == AttackType.RANGED && weapon != Weapon.CRYSTAL_BOW) {
		    if(player.inWarriorGuildArena() || player.inWarriorGuild()) {
			    failedInitialize = true;
			    player.getActionSender().sendMessage(CombatManager.WARRIORS_GUILD);
			    return;
		    }
		    if(getVictim().isNpc() && ((Npc)getVictim()).getNpcId() == 879) {
			failedInitialize = true;
			player.getActionSender().sendMessage("You must use Silverlight to fight Delrith!");
			return;
		    }
		    if(weapon == Weapon.DARK_BOW) {
			rangedAmmo = RangedAmmo.getRangedAmmo(player, weapon, true);
			if (rangedAmmo == null) {
				failedInitialize = true;
				return;
			}
			int ammoSlot = weapon.getAmmoType().getEquipmentSlot();
			dropId = player.getEquipment().getId(ammoSlot);
			setRequirements(new Requirement[]{new EquipmentRequirement(ammoSlot, dropId, 2, true) {
				@Override
				public String getFailMessage() {
					return CombatManager.NO_AMMO_MESSAGE;
				}
			}});
			poisonEffect = checkPoison(player, AttackType.RANGED, ammoSlot);
		    }
		    else {
			rangedAmmo = RangedAmmo.getRangedAmmo(player, weapon, true);
			if (rangedAmmo == null) {
				failedInitialize = true;
				return;
			}
			int ammoSlot = weapon.getAmmoType().getEquipmentSlot();
			dropId = player.getEquipment().getId(ammoSlot);
			setRequirements(new Requirement[]{new EquipmentRequirement(ammoSlot, dropId, 1, true) {
				@Override
				public String getFailMessage() {
					return CombatManager.NO_AMMO_MESSAGE;
				}
			}});
			poisonEffect = checkPoison(player, AttackType.RANGED, ammoSlot);
		    }
		} else if (getAttackStyle().getAttackType() == AttackType.MELEE){
			poisonEffect = checkPoison(player, AttackType.MELEE, Constants.WEAPON);
		} else if (weaponName.contains("crystal")) {
		    if(player.inWarriorGuildArena()) {
			    failedInitialize = true;
			    player.getActionSender().sendMessage(CombatManager.WARRIORS_GUILD);
			    return;
		    }
		    rangedAmmo = RangedAmmo.CRYSTAL_ARROW; //This allows the bow to ignore ammo
		}
		setHits(generateHits());
		setAttackDelay(generateHitDelay());
		setGraphic(generateGraphic());
		setAnimation(generateAnimation());
		failedInitialize = !canInitialize();
	}

	@Override
	public int distanceRequired() {
		switch (attackStyle.getAttackType()) {
			case RANGED :
				if (weapon == Weapon.LONG_BOW || weapon == Weapon.SPECIAL_BOW || weapon == Weapon.DARK_BOW) {
					return attackStyle.getMode() == AttackStyle.Mode.LONGRANGE ? 10 : 9;
				} else if (weapon == Weapon.SHORT_BOW || weapon == Weapon.CBOW || weapon == Weapon.CROSSBOW) {
					return attackStyle.getMode() == AttackStyle.Mode.LONGRANGE ? 9 : 7;
				} else if (weapon == Weapon.THROWING_KNIFE || weapon == Weapon.THROWING_AXE) {
					return attackStyle.getMode() == AttackStyle.Mode.LONGRANGE ? 6 : 4;
				} else if (weapon == Weapon.THROWING_DART) {
					return attackStyle.getMode() == AttackStyle.Mode.LONGRANGE ? 5 : 3;
				} else if (weapon == Weapon.JAVELIN) {
					return attackStyle.getMode() == AttackStyle.Mode.LONGRANGE ? 7 : 5;
				} else if (weapon == Weapon.CRYSTAL_BOW) {
					return 10;
				}
				return attackStyle.getMode() == AttackStyle.Mode.LONGRANGE ? 9 : 7;
			case MAGIC :
				if (attackStyle.getMode() == AttackStyle.Mode.DRAGONFIRE) {
					return 1;
				}
				return 10;
			case MELEE :
				return weapon == Weapon.HALBERD ? 2 : 1;
		}
		return 1;
	}

	public boolean canInitialize() {
		return true;
	}

	@Override
	public final AttackUsableResponse.Type isUsable() {
		if (failedInitialize)
			return AttackUsableResponse.Type.FAIL;
		SpecialType specialType = ((Player) getAttacker()).getSpecialType();
		boolean specialEnabled = ((Player) getAttacker()).isSpecialAttackActive();
		// System.out.println("Special Type: "+(specialType == null ? "null" :
		// specialType.name())+" Enabled? "+specialEnabled);
		if (specialType != null && specialEnabled) {
			Player player = ((Player) getAttacker());
			this.specialType = specialType;
			if (player.getSpecialAmount() < specialType.getEnergyRequired()) {
				player.getActionSender().sendMessage(CombatManager.NO_SPECIAL_ENERGY_MESSAGE);
				return AttackUsableResponse.Type.FAIL;
			}
			if (RulesData.NO_SPEC.activated(player)) {
				player.getActionSender().sendMessage("Special attacks have been disabled during this fight!");
				return AttackUsableResponse.Type.FAIL;
			}

		}
		return super.isUsable();
	}

	@Override
	public int execute(CycleEventContainer container) {
		Player player = ((Player) getAttacker());
		if (player.isSpecialAttackActive() && specialType != null) {
			player.setSpecialAmount(player.getSpecialAmount() - specialType.getEnergyRequired());
			player.updateSpecialBar();
			player.setSpecialAttackActive(false);
			player.updateSpecialBar();
		}
		if (getHits() != null) {
			for (HitDef hit : getHits()) {
				if (dropId != -1)
					hit.setDroppedItem(new Item(dropId));
				if (poisonEffect != null && Misc.random(3) == 0)
					hit.addEffect(poisonEffect);
			}
		}
		player.getCombatSounds().weaponSound(player);
		if(getAttacker() != null && getAttacker().isPlayer() && getVictim() != null && getVictim().isPlayer())
		{
			((Player) getVictim()).getCombatSounds().weaponSound(player);
		}
		/*if (attackStyle.getAttackType() == AttackType.RANGED) {
			player.getMovementPaused().setWaitDuration(2);
			player.getMovementPaused().reset();
		}*/
		return super.execute(container);
	}

	private PoisonEffect checkPoison(Player player, AttackType attackType, int slot) {
		// what u doin here fool?
		if (attackType == AttackType.MAGIC)
			return null;
		// else
		Item item = player.getEquipment().getItemContainer().get(slot);
		if (item == null)
			return null;
		ItemDefinition def = ItemDefinition.forId(item.getId());
		String name = def.getName().toLowerCase();
		if (name.contains("(kp)"))
			return KARAMBWAN_PASTE;
		else if (name.contains("(s)") || name.contains("(p++)") || name.contains("++"))
			return attackType == AttackType.MELEE ? SUPER_POISON_MELEE : SUPER_POISON_RANGED;
		else if (name.contains("(+)") || name.contains("(p+)"))
			return attackType == AttackType.MELEE ? EXTRA_POISON_MELEE : EXTRA_POISON_RANGED;
		else if (name.contains("(p)"))
			return attackType == AttackType.MELEE ? NORMAL_POISON_MELEE : NORMAL_POISON_RANGED;
		return null;
	}

}
