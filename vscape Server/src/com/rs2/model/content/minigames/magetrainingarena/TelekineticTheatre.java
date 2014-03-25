package com.rs2.model.content.minigames.magetrainingarena;

import com.rs2.model.Position;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 01/01/12 Time: 14:55 To change
 * this template use File | Settings | File Templates.
 */
public class TelekineticTheatre {

	/* setting up the maze teleport locations THERE ARE 10 DIFFERENT MAZE ADDED */

	private static final Position[] MAZE_POSITIONS = {new Position(3336, 9718, 0), new Position(3379, 9716, 0), new Position(3374, 9696, 0), new Position(3354, 9690, 0), new Position(3362, 9713, 1), new Position(3378, 9706, 1), new Position(3382, 9698, 1), new Position(3355, 9693, 1), new Position(3368, 9680, 2), new Position(3359, 9701, 2)};

	@SuppressWarnings("unused")
	private static final Position[] STATUE_POSITIONS = {new Position(3343, 9705, 0), new Position(3368, 9712, 0), new Position(3373, 9678, 0), new Position(3343, 9680, 0), new Position(3350, 9717, 1), new Position(3374, 9713, 1), new Position(3376, 9687, 1), new Position(3351, 9684, 1), new Position(3348, 9674, 2), new Position(3346, 9718, 2)};

	@SuppressWarnings("unused")
	private static final Position[] STATUE_ARRIVAL_POSITIONS = {new Position(3347, 9714, 0), new Position(3367, 9720, 0), new Position(3375, 9682, 0), new Position(3342, 9684, 0), new Position(3341, 9708, 1), new Position(3383, 9713, 1), new Position(3385, 9686, 1), new Position(3353, 9680, 1), new Position(3339, 9683, 2), new Position(3345, 9718, 2)};

	/* setting up the corners of each maze squares */

	private static final Position[] UPPER_LEFT_CORNER = {new Position(3337, 9715, 0), new Position(3365, 9721, 0), new Position(3368, 9683, 0), new Position(3337, 9685, 0), new Position(3340, 9718, 1), new Position(3373, 9723, 1), new Position(3375, 9687, 1), new Position(3345, 9690, 1), new Position(3338, 9684, 2), new Position(3342, 9719, 2)};
	private static final Position[] BOTTOM_LEFT_CORNER = {new Position(3337, 9704, 0), new Position(3365, 9710, 0), new Position(3368, 9672, 0), new Position(3337, 9674, 0), new Position(3340, 9707, 1), new Position(3373, 9712, 1), new Position(3375, 9676, 1), new Position(3345, 9679, 1), new Position(3338, 9673, 2), new Position(3342, 9708, 2)};
	private static final Position[] UPPER_RIGHT_CORNER = {new Position(3348, 9715, 0), new Position(3376, 9721, 0), new Position(3379, 9683, 0), new Position(3348, 9685, 0), new Position(3351, 9715, 1), new Position(3384, 9723, 1), new Position(3386, 9687, 1), new Position(3356, 9690, 1), new Position(3349, 9684, 2), new Position(3353, 9719, 2)};
	private static final Position[] BOTTOM_RIGHT_CORNER = {new Position(3348, 9704, 0), new Position(3376, 9710, 0), new Position(3379, 9672, 0), new Position(3348, 9674, 0), new Position(3351, 9707, 1), new Position(3384, 9712, 1), new Position(3386, 9676, 1), new Position(3356, 9679, 1), new Position(3349, 9673, 2), new Position(3353, 9708, 2)};

	/* setting up the center square position for camera angle */

	private static Position[] CENTER_SQUARE = new Position[10];

	private Player player;
	@SuppressWarnings("unused")
	private int pizzazPoint, mazeIndex;

	public TelekineticTheatre(Player player) {
		this.player = player;
	}

	/* loading the telekinetic theatre minigame */

	public static void loadTelekineticTheatre() {
		loadCenterSquares();
	}

	/* calculating the position of the center spot of the whole maze */

	private static void loadCenterSquares() {
		for (int i = 0; i < MAZE_POSITIONS.length - 1; i++) {
			CENTER_SQUARE[i] = getCenterPosition(BOTTOM_LEFT_CORNER[i], UPPER_RIGHT_CORNER[i]);
		}
	}

	/* putting the camera on the maze */

	public void loadCamera() {// todo this is just testing stage, need to
								// devellop
		player.getActionSender().stillCamera(CENTER_SQUARE[0].getX(), CENTER_SQUARE[0].getY(), 0001, 0001, 0001);
		player.getActionSender().sendMessage("" + CENTER_SQUARE[0]);
	}

	/* gets the center position with a square diagonal provided */

	private static Position getCenterPosition(Position p1, Position p2) {
		int x1 = p1.getX();
		int y1 = p1.getY();
		int x2 = p2.getX();
		int y2 = p2.getY();
		return new Position(((x1 + x2) / 2) + 1, ((y1 + y2) / 2) + 1);
	}

	/*
	 * gets the player angle compared to the guardian statue : left wall , right
	 * wall etc
	 */

	@SuppressWarnings("unused")
	private String getSidePlace(int mazeIndex) {
		int x = player.getPosition().getX();
		int y = player.getPosition().getY();

		if (x == BOTTOM_LEFT_CORNER[mazeIndex].getX()) {
			if (y > BOTTOM_LEFT_CORNER[mazeIndex].getY() && y < UPPER_LEFT_CORNER[mazeIndex].getY())
				return "left";
		}
		if (x == BOTTOM_RIGHT_CORNER[mazeIndex].getX()) {
			if (y > BOTTOM_RIGHT_CORNER[mazeIndex].getY() && y < UPPER_RIGHT_CORNER[mazeIndex].getY())
				return "right";
		}
		if (y == UPPER_LEFT_CORNER[mazeIndex].getY()) {
			if (x > UPPER_LEFT_CORNER[mazeIndex].getX() && x < UPPER_RIGHT_CORNER[mazeIndex].getX())
				return "upper";
		}
		if (y == BOTTOM_LEFT_CORNER[mazeIndex].getY()) {
			if (x > BOTTOM_LEFT_CORNER[mazeIndex].getX() && x < BOTTOM_RIGHT_CORNER[mazeIndex].getX())
				return "bottom";
		}
		return "";
	}

	public boolean isInTelekineticTheatre() {
		return false;
	}
}
