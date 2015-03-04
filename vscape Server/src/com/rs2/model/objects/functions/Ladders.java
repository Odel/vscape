package com.rs2.model.objects.functions;

import com.rs2.model.Position;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 19/04/12 Time: 01:00 To change
 * this template use File | Settings | File Templates.
 */
public class Ladders {

	public static void climbLadder(final Player player, final Position pos) {
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(828/*method == "up" ? 828 : 827*/);
		player.getActionSender().removeInterfaces();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player.getPosition() != pos) {
					player.teleport(pos);
				}
				player.getUpdateFlags().sendAnimation(65535);
				container.stop();
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
	}
	
	public static void climbLadderDown(final Player player, final Position pos) {
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(827);
		player.getActionSender().removeInterfaces();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player.getPosition() != pos) {
					player.teleport(pos);
				}
				player.getUpdateFlags().sendAnimation(65535);
				container.stop();
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
	}

	public static void checkClimbLadder(Player player, String method) {
		GameObjectDef def = SkillHandler.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int z = player.getPosition().getZ();
		int height = method.equalsIgnoreCase("up") ? z + 1 : (z - 1 < 1 ? 0 : z - 1);
		climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), height));
	}

	public static void checkClimbStaircase(Player player, int xLength, int yLength, String method) {
		GameObjectDef def = SkillHandler.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int face = def.getFace();
		int z = player.getPosition().getZ();
		int height = method.equalsIgnoreCase("up") ? z + 1 : (z - 1 < 1 ? 0 : z - 1);
		int x = face == 1 ? (method.equalsIgnoreCase("up") ? xLength : -xLength) : (face == 3 ? (method.equalsIgnoreCase("up") ? -xLength : xLength) : 0);
		int y = face == 0 ? (method.equalsIgnoreCase("up") ? yLength : -yLength) : (face == 2 ? (method.equalsIgnoreCase("up") ? -yLength : yLength) : 0);
		player.teleport(new Position(player.getPosition().getX() + x, player.getPosition().getY() + y, height));
	}

	public static void checkClimbStaircaseBackwards(Player player, int xLength, int yLength, String method) {
		GameObjectDef def = SkillHandler.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int face = def.getFace();
		int z = player.getPosition().getZ();
		int height = method.equalsIgnoreCase("up") ? z + 1 : (z - 1 < 1 ? 0 : z - 1);
		int x = face == 1 ? (method.equalsIgnoreCase("up") ? -xLength : xLength) : (face == 3 ? (method.equalsIgnoreCase("up") ? xLength : -xLength) : 0);
		int y = face == 0 ? (method.equalsIgnoreCase("up") ? -yLength : yLength) : (face == 2 ? (method.equalsIgnoreCase("up") ? yLength : -yLength) : 0);
		player.teleport(new Position(player.getPosition().getX() + x, player.getPosition().getY() + y, height));
	}

	public static void checkClimbTallStaircase(Player player, String method) {
		GameObjectDef def = SkillHandler.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int face = def.getFace();
		int z = player.getPosition().getZ();
		int height = method.equalsIgnoreCase("up") ? z + 1 : (z - 1 < 1 ? 0 : z - 1);
		int x = (face == 0 ? 1 : face == 1 ? 1 : face == 2 ? -1 : -1) * (method.equalsIgnoreCase("up") ? 1 : -1);
		int y = (face == 0 ? 1 : face == 1 ? -1 : face == 2 ? -1 : 1) * (method.equalsIgnoreCase("up") ? 1 : -1);
		if (def.getId() == 1738 || def.getId() == 1739 || def.getId() == 1740) {
			x = y = 0;
		}
		player.teleport(new Position(player.getPosition().getX() + x, player.getPosition().getY() + y, height));
	}

	public static void checkClimbStaircaseDungeon(Player player, int xLength, int yLength, String method) {
		GameObjectDef def = SkillHandler.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int face = def.getFace();
		int x = face == 1 ? (method.equalsIgnoreCase("up") ? xLength : -xLength) : (face == 3 ? (method.equalsIgnoreCase("up") ? -xLength : xLength) : 0);
		int y = face == 0 ? (method.equalsIgnoreCase("up") ? yLength : -yLength) : (face == 2 ? (method.equalsIgnoreCase("up") ? -yLength : yLength) : 0);
		player.teleport(new Position(player.getPosition().getX() + x, player.getPosition().getY() + y + (method.equalsIgnoreCase("up") ? -6400 : 6400), 0));
	}

}
