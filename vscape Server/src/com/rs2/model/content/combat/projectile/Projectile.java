package com.rs2.model.content.combat.projectile;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;

/**
 *
 */
public class Projectile {

	private int lockOn, duration;
	private byte offsetX, offsetY;
	private ProjectileDef projectileDef;
	private Position start;
	private int sizeOffset;


	public Projectile(Position start, int sizeOffset, Position end, int lockOn, ProjectileDef projectileDef) {
		this.start = start;
		this.sizeOffset = sizeOffset;
		this.lockOn = lockOn;
		this.projectileDef = projectileDef;
		int distance = Misc.getDistance(start, end);
		int slowness = projectileDef.getProjectileTrajectory().getSlowness();
		this.duration = projectileDef.getProjectileTrajectory().getDelay() + slowness + distance * 5;
		this.offsetX = (byte) (end.getX() - start.getX());
		this.offsetY = (byte) (end.getY() - start.getY());
	}
    
    public Projectile(Entity source, Entity victim, Position end, ProjectileDef projectileDef) {
    	this(source.getPosition(), source.getSize(), end, (victim.isPlayer() ? -victim.getIndex() - 1 : victim.getIndex() + 1), projectileDef);
    }

	public Projectile(Entity source, Entity victim, ProjectileDef projectileDef) {
		this(source, victim, victim.getPosition(), projectileDef);
	}

	public void show() {
		if (projectileDef.getProjectileId() == -1)
			return;
		ProjectileTrajectory projectileTrajectory = projectileDef.getProjectileTrajectory();
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (start.isViewableFrom(player.getPosition()))
				player.getActionSender().sendProjectile(start, sizeOffset, lockOn, offsetX, offsetY, projectileDef.getProjectileId(), projectileTrajectory.getDelay(), duration, projectileTrajectory.getStartHeight(), projectileTrajectory.getEndHeight(), projectileTrajectory.getCurve());
		}
	}
}
