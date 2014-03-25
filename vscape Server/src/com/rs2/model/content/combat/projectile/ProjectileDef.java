package com.rs2.model.content.combat.projectile;

/**
 *
 */
public class ProjectileDef {

	private ProjectileTrajectory projectileTrajectory;
	private int projectileId;

	public ProjectileDef(int projectileId, ProjectileTrajectory projectileTrajectory) {
		this.projectileId = projectileId;
		this.projectileTrajectory = projectileTrajectory;
	}

	public ProjectileTrajectory getProjectileTrajectory() {
		return projectileTrajectory;
	}

	public int getProjectileId() {
		return projectileId;
	}
}
