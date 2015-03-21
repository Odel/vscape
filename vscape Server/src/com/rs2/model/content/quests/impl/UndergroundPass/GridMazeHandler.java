package com.rs2.model.content.quests.impl.UndergroundPass;

import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import java.util.ArrayList;

public class GridMazeHandler {

	public static final Position[][] SQUARES = new Position[5][5];
	public static final ArrayList<Position> SQUARES_LIST = new ArrayList<>();
	public static final Position GRID_FAIL = new Position(2395, 9560, 0);

	public static void initSquares() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Position p = new Position(2475 - (i * 2), 9681 - (j * 2), 0);
				SQUARES[i][j] = p;
				SQUARES_LIST.add(p);
			}
		}
	}

	public static void generatePositions(final Player player) {
		int originalRow = Misc.randomMinusOne(5);
		int middleColumn = Misc.randomMinusOne(3) + 1, middleRow = Misc.randomMinusOne(5);
		final Position start = SQUARES[0][originalRow], middle = SQUARES[middleColumn][middleRow];
		player.getQuestVars().getGridStart().setX(start.getX());
		player.getQuestVars().getGridStart().setY(start.getY());
		player.getQuestVars().getGridMiddle().setX(middle.getX());
		player.getQuestVars().getGridMiddle().setY(middle.getY());
	}

	public static ArrayList<Position> findRoute(final Position start, final Position middle) {
		if (start.equals(middle)) {
			return null;
		}
		ArrayList<Position> route = new ArrayList<>();
		int originalRow = (((start.getY() - 9681) / 2) * -1);
		int middleRow = (((middle.getY() - 9681) / 2) * -1), middleColumn = (((middle.getX() - 2475) / 2) * -1);
		int currentRow = originalRow, currentColumn = 0;
		Position currentPosition = start;
		route.add(start);
		while (!currentPosition.equals(middle)) {
			if (currentColumn < middleColumn) {
				currentPosition = SQUARES[++currentColumn][currentRow];
				route.add(currentPosition);
			} else {
				currentPosition = SQUARES[currentColumn][currentRow < middleRow ? ++currentRow : --currentRow];
				route.add(currentPosition);
			}
		}
		currentRow = middleRow;
		currentColumn = middleColumn;
		while (currentColumn < 4) {
			route.add(SQUARES[++currentColumn][currentRow]);
		}
		return route;
	}

	public static void startGridCheck(final Player player) {
		final ArrayList<Position> route = findRoute(player.getQuestVars().getGridStart(), player.getQuestVars().getGridMiddle());
		if (route == null) {
			return;
		}
		final ArrayList<Position> trap = SQUARES_LIST;
		for(Position p : route) {
			trap.remove(p);
		}
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer b) {
						if (!player.Area(2465, 2482, 9671, 9688)) {
							b.stop();
						} else {
							if (player.Area(2467, 2476, 9673, 9682)) {
								for (Position p : trap) {	
									if (player.Area(p.getX(), p.getX() + 1, p.getY(), p.getY() + 1)) {
										handleFail(player);
										b.stop();
										break;
									}
								}
							}
						}
					}

					@Override
					public void stop() {
					}
				}, 2);
			}
		}, 2);
	}

	public static void handleFail(final Player player) {
		player.getUpdateFlags().sendAnimation(771);
		player.getActionSender().sendMessage("It's a trap!");
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				b.stop();
			}

			@Override
			public void stop() {
				player.teleport(GRID_FAIL);
				player.getActionSender().sendMessage("You fall onto the spikes.");
				player.hit(15, HitType.NORMAL);
				player.getUpdateFlags().setForceChatMessage("Ouch!");
			}
		}, 2);
	}

}
