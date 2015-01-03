package com.rs2.model.content.combat.weapon;

import static com.rs2.model.content.combat.weapon.RangedAmmo.ARROW_AMMO;
import static com.rs2.model.content.combat.weapon.RangedAmmo.DARK_BOW_AMMO;
import static com.rs2.model.content.combat.weapon.RangedAmmo.BRUTAL_AMMO;
import static com.rs2.model.content.combat.weapon.RangedAmmo.DART_AMMO;
import static com.rs2.model.content.combat.weapon.RangedAmmo.JAVELIN_AMMO;
import static com.rs2.model.content.combat.weapon.RangedAmmo.KNIFE_AMMO;
import static com.rs2.model.content.combat.weapon.RangedAmmo.OGRE_ARROW;
import static com.rs2.model.content.combat.weapon.RangedAmmo.THROWNAXE_AMMO;

import com.rs2.Constants;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;

/**
 *
 */
public enum RangedAmmoType {
	ARROW(Constants.ARROWS, 90, ProjectileTrajectory.ARROW, ARROW_AMMO), MONKEY_ARROW(Constants.ARROWS, 0, ProjectileTrajectory.ARROW, new RangedAmmo[]{RangedAmmo.MONKEY_ARROW}), DARK_BOW(Constants.ARROWS, 90, ProjectileTrajectory.ARROW, DARK_BOW_AMMO), CRYSTAL(Constants.ARROWS, 90, ProjectileTrajectory.CRYSTAL, new RangedAmmo[] {RangedAmmo.CRYSTAL_ARROW} ), OBBY_RING(Constants.WEAPON, 90, ProjectileTrajectory.ARROW, new RangedAmmo[]{RangedAmmo.TOKTZ}), KNIFE(Constants.WEAPON, 90, ProjectileTrajectory.KNIFE, KNIFE_AMMO), JAVELIN(Constants.WEAPON, 90, ProjectileTrajectory.ARROW, JAVELIN_AMMO), DART(Constants.WEAPON, 90, ProjectileTrajectory.DART, DART_AMMO), OGRE(Constants.ARROWS, 90, ProjectileTrajectory.ARROW, new RangedAmmo[]{OGRE_ARROW}), BRUTAL(Constants.ARROWS, 90, ProjectileTrajectory.ARROW, BRUTAL_AMMO), THROWNAXE(Constants.WEAPON, 90, ProjectileTrajectory.ARROW, THROWNAXE_AMMO), BOLTS(Constants.ARROWS, 90, ProjectileTrajectory.ARROW, RangedAmmo.BOLT_AMMO), KARILS_BOLT(Constants.ARROWS, 90, ProjectileTrajectory.ARROW, new RangedAmmo[]{RangedAmmo.BOLT_RACK});

	private final ProjectileTrajectory projectileTrajectory;
	private final RangedAmmo[] rangedAmmos;
	private final int equipmentSlot, graphicHeight;

	RangedAmmoType(int equipmentSlot, int graphicHeight, ProjectileTrajectory projectileTrajectory, RangedAmmo[] rangedAmmos) {
		this.equipmentSlot = equipmentSlot;
		this.graphicHeight = graphicHeight;
		this.projectileTrajectory = projectileTrajectory;
		this.rangedAmmos = rangedAmmos;
	}

	public ProjectileTrajectory getProjectileTrajectory() {
		return projectileTrajectory;
	}

	public RangedAmmo[] getRangedAmmos() {
		return rangedAmmos;
	}

	public int getEquipmentSlot() {
		return equipmentSlot;
	}

	public int getGraphicHeight() {
		return graphicHeight;
	}
}