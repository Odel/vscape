package com.rs2.model.content.combat.projectile;

/**
 *
 */
public class ProjectileTrajectory {

	public static ProjectileTrajectory ARROW = new ProjectileTrajectory(44, 3, 43, 31, 15);
	public static ProjectileTrajectory KNIFE = new ProjectileTrajectory(33, 3, 45, 37, 5);
	public static ProjectileTrajectory DART = KNIFE.clone().setDelay(40).setSlowness(2);
	public static ProjectileTrajectory SPELL = new ProjectileTrajectory(50, 6, 45, 30, 15);
	public static ProjectileTrajectory JAD_SPELL = new ProjectileTrajectory(50, 25, 100, 26, 15);
	public static ProjectileTrajectory JAD_RANGE = new ProjectileTrajectory(50, 50, 60, 26, 15);
	public static ProjectileTrajectory FISHING_EXPLOSION = new ProjectileTrajectory(50, 6, 45, 0, 15);
	// hitDelayMilli = (delay*18)+
	// the max delay (100) takes 1800 milliseconds to fire
	// at max slowness (100) and min distance (1) it takes 1800 milliseconds
	// at max slowness (100) and max distance (10) it takes 3600 milliseconds
	// at min slowness (0) and min distance (1) it takes 0 milliseconds
	// at min slowness (0) and max distance(10) it takes 600 milliseconds x

	private int delay, slowness, startHeight, endHeight, curve;

	public ProjectileTrajectory(int delay, int slowness, int startHeight, int endHeight, int curve) {
		this.delay = delay;
		this.slowness = slowness;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.curve = curve;
	}

	public int getDelay() {
		return delay;
	}

	public int getSlowness() {
		return slowness;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public int getCurve() {
		return curve;
	}

	public ProjectileTrajectory setDelay(int delay) {
		this.delay = delay;
		return this;
	}

	public ProjectileTrajectory setSlowness(int slowness) {
		this.slowness = slowness;
		return this;
	}

	@Override
	public ProjectileTrajectory clone() {
		return new ProjectileTrajectory(delay, slowness, startHeight, endHeight, curve);
	}

	public ProjectileTrajectory setStartHeight(int startHeight) {
		this.startHeight = startHeight;
		return this;
	}

	public ProjectileTrajectory setEndHeight(int endHeight) {
		this.endHeight = endHeight;
		return this;
	}
}
