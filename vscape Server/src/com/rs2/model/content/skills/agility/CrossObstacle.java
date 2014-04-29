package com.rs2.model.content.skills.agility;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/21/12 Time: 3:49 PM To change
 * this template use File | Settings | File Templates.
 */
public class CrossObstacle {

	//no Z required to keep old shit working
	public static void setForceMovement(final Player player, final int x, final int y, final int speed1, final int speed2, final int time, final boolean stopPacket, final double xp, final int walkAnim) {
		setForceMovement(player, x, y, 0, speed1, speed2, time, stopPacket, xp, walkAnim);
	}
	
	public static void walkAcross(final Player player, final double xp, final int walkX, final int walkY, final int time, final int speed, final int startAnim) {
		walkAcross(player, xp, walkX, walkY, 0, time, speed, startAnim);
	}

	public static void walkAcross(final Player player, final double xp, final int walkX, final int walkY, final int changeZ, final int time, final int speed, final int startAnim) {
		if (startAnim > 0) {
			player.getUpdateFlags().sendAnimation(startAnim);
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
	        	player.getActionSender().animateObject(player.getClickX(), player.getClickY(), 3, 127);
				setForceMovement(player, walkX, walkY, changeZ, speed, speed + 1, time, true, xp, startAnim);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 2);
	}

	public static void setForceMovement(final Player player, final int x, final int y, final int z, final int speed1, final int speed2, final int time, final boolean stopPacket, final double xp, final int walkAnim) {
		if (stopPacket) {
			player.setStopPacket(true);
		}
		player.getMovementHandler().reset();
		if (walkAnim > 0) {
			player.setRunAnim(walkAnim);
			player.setWalkAnim(walkAnim);
			player.setAppearanceUpdateRequired(true);
		}
		int direction = 2;
		if (x > 0) {
			direction = 1;
		} else if (x < 0) {
			direction = 3;
		} else if (y > 0) {
			direction = 0;
		}
		//player.movePlayer(player.getPosition());
		final int endX = player.getPosition().getX() + x;
		final int endY = player.getPosition().getY() + y;
		final int endZ = player.getPosition().getZ() + z;
		final int dir = direction;
		player.isCrossingObstacle = true;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				//player.getUpdateFlags().sendForceMovement(player, x, y, speed1, speed2, dir);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 1);
		//player.getUpdateFlags().sendAnimation(endAnim);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getSkill().addExp(Skill.AGILITY, xp);
		        player.getUpdateFlags().setForceMovementUpdateRequired(false);
				if (stopPacket) {
					player.setStopPacket(false);
				}
				player.teleport(new Position(endX, endY, endZ));
				player.getUpdateFlags().resetForceMovement();
				player.setRunAnim(-1);
				player.setWalkAnim(-1);
				player.setAppearanceUpdateRequired(true);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, time + 1);
	}

}