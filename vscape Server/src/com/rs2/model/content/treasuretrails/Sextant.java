package com.rs2.model.content.treasuretrails;

import com.rs2.model.players.Player;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 08/02/12 Time: 21:03 To change
 * this template use File | Settings | File Templates.
 */
public class Sextant {

	// main sextant datas
	public static final int SEXTANT_INTERFACE = 6946;
	public static final int SEXTANT_BAR_LENTH = 280;
	public static final int SEXTANT_ROTATION1 = 513;
	public static final int SEXTANT_BAR_ZOOM = 580;
	public static final int SEXTANT_GLOBAL_ZOOM = 555;

	// different sprites ids
	public static final int SEXTANT_BAR = 6957;
	public static final int SEXTANT_SUN = 6949;
	public static final int SEXTANT_ARC = 6956;
	public static final int SEXTANT_TELESCOPE = 6958;
	public static final int SEXTANT_LANDSCAPE = 6948;
	// converting runescape pixels to degree
	public static final double DEGREE_FACTOR = 5.7;
	// the limit put for rotating/moving sprites
	private static final double BAR_LIMIT_LEFT = 28;
	private static final double BAR_LIMIT_RIGHT = 32;
	private static final int GLOBAL_LIMIT_LEFT = 40;
	private static final int GLOBAL_LIMIT_RIGHT = 58;
	private static final int LANDSCAPE_LIMIT_UP = 70;
	private static final int LANDSCAPE_LIMIT_DOWN = 70;

	/* initialize the main variables when clicking the sextant */
	public static void initializeVariables(Player c) {
		c.sextantBarDegree = 0;
		c.sextantSunCoords = 0;
		c.rotationFactor = 0;
		c.sextantGlobalPiece = 0;
		c.sextantLandScapeCoords = 0;

	}

	/* calculate the starting state formulas */
	public static void calculateStartFormula(Player c) {
		// sextant bar randomly placed
		double randomBarDegree = -BAR_LIMIT_RIGHT + (c.rotationFactor / DEGREE_FACTOR) + Misc.random((int) (BAR_LIMIT_RIGHT + (c.rotationFactor / DEGREE_FACTOR) + BAR_LIMIT_LEFT + (c.rotationFactor / DEGREE_FACTOR)));
		// sextant sun randomly placed
		int limitUpSun = (int) (BAR_LIMIT_LEFT + (c.rotationFactor / DEGREE_FACTOR) - randomBarDegree);
		int limitDownSun = Math.abs((int) (randomBarDegree + BAR_LIMIT_RIGHT + (c.rotationFactor / DEGREE_FACTOR)));
		int incrementationSun = (Misc.random(1) == 0) ? Misc.random(limitDownSun) * 2 : -Misc.random(limitUpSun) * 2;
		// sextant landscape randomly placed
		double randomLandScapeDegree = -(GLOBAL_LIMIT_RIGHT / 5.7) + Misc.random((int) ((GLOBAL_LIMIT_RIGHT / 5.7) + (GLOBAL_LIMIT_LEFT / 5.7)));
		int limitUpLandScape = (int) ((GLOBAL_LIMIT_LEFT / 5.7) - randomLandScapeDegree);
		int limitDownLandScape = Math.abs((int) (randomLandScapeDegree + GLOBAL_LIMIT_RIGHT / 5.7));
		int incrementationLandScape = (Misc.random(1) == 0) ? Misc.random(limitDownLandScape) : -Misc.random(limitUpLandScape);

		// setting the player sextant variables
		c.sextantLandScapeCoords += incrementationLandScape * 5.7;
		c.sextantGlobalPiece += randomLandScapeDegree * 5.7;
		c.sextantSunCoords += incrementationSun;
		c.sextantBarDegree = randomBarDegree;

	}

	/* initialize a random state for the sextant */
	public static void initializeRandomSextantInterface(Player c) {
		initializeVariables(c);
		calculateStartFormula(c);
		c.getActionSender().sendInterface(SEXTANT_INTERFACE);
		updateSextant(c);
	}

	/* update the sextant global state */
	public static void updateSextant(Player c) {
		moveSextantBarWithFormula(c, c.sextantBarDegree);
		moveSunWithFormula(c);
		moveSextantGlobalPieceWithFormula(c);
		moveLandScapeWithFormula(c);
	}

	/* checks if everything matches to get the player position */
	public static boolean everythingMatches(Player c) {
		if (Math.abs(c.sextantLandScapeCoords) > 7) {
			c.getActionSender().sendMessage("You need to get the horizon in the middle of the eye piece.");
			return false;
		}
		if (c.sextantSunCoords != 0) {
			c.getActionSender().sendMessage("You need to get the sun in the middle of the eye piece.");
			return false;
		}
		if (!c.getInventory().getItemContainer().contains(CoordinateScrolls.SEXTANT) || !c.getInventory().getItemContainer().contains(CoordinateScrolls.CHART)) {
			c.getDialogue().sendStatement("You need a watch and navigator's chart to work out your Position.");
			return false;

		}
		return true;
	}

	/* moves the sextant bar by one degree */
	public static void moveSextantBar(Player c, boolean left) {
		if (left) {
			c.sextantBarDegree += 1;
		} else {
			c.sextantBarDegree -= 1;
		}
		boolean bool1 = c.sextantBarDegree > BAR_LIMIT_LEFT + (c.rotationFactor / DEGREE_FACTOR);
		boolean bool2 = c.sextantBarDegree < -BAR_LIMIT_RIGHT + (c.rotationFactor / DEGREE_FACTOR);
		if (!bool1 && !bool2)
			moveSun(c, left);
		if (bool1)
			c.sextantBarDegree = BAR_LIMIT_LEFT + (c.rotationFactor / DEGREE_FACTOR);
		if (bool2)
			c.sextantBarDegree = -BAR_LIMIT_RIGHT + (c.rotationFactor / DEGREE_FACTOR);
		updateSextant(c);

	}

	/* moves the sextant bar to a specific degree */
	public static void moveSextantBarWithFormula(Player c, double degreeGiven) {
		double degree = degreeGiven < -BAR_LIMIT_RIGHT + (c.rotationFactor / DEGREE_FACTOR) ? -BAR_LIMIT_RIGHT + (c.rotationFactor / DEGREE_FACTOR) : degreeGiven > BAR_LIMIT_LEFT + (c.rotationFactor / DEGREE_FACTOR) ? BAR_LIMIT_LEFT + (c.rotationFactor / DEGREE_FACTOR) : degreeGiven;
		int realDegree = degree > 0 ? (int) (degree * DEGREE_FACTOR) : 2047 - (int) (-degree * DEGREE_FACTOR);
		int x = (int) Math.round(Math.abs((SEXTANT_BAR_LENTH / 2) * Math.sin(degree * Math.PI / 180)));
		int y = (int) Math.floor(Math.abs((SEXTANT_BAR_LENTH / 2) * (1 - Math.cos(degree * Math.PI / 180))));
		c.getActionSender().sendFrame230(SEXTANT_BAR, SEXTANT_ROTATION1, realDegree, SEXTANT_BAR_ZOOM);

		c.getActionSender().moveInterface(degree > 0 ? -(x - c.rotationFactor / 2) : (x + c.rotationFactor / 2), -y, SEXTANT_BAR);

	}

	/* moves the arc and telescope by te degrees */
	public static void moveGlobalPiece(Player c, boolean left) {

		if (left) {
			c.sextantGlobalPiece += 7;

			if (c.sextantGlobalPiece <= GLOBAL_LIMIT_LEFT && c.sextantGlobalPiece >= -GLOBAL_LIMIT_RIGHT) {
				c.sextantBarDegree += (7 / DEGREE_FACTOR);
				c.rotationFactor += 7;

			}
		} else {
			c.sextantGlobalPiece -= 7;

			if (c.sextantGlobalPiece <= GLOBAL_LIMIT_LEFT && c.sextantGlobalPiece >= -GLOBAL_LIMIT_RIGHT) {
				c.sextantBarDegree -= (7 / DEGREE_FACTOR);
				c.rotationFactor -= 7;

			}

		}
		boolean bool1 = c.sextantGlobalPiece > GLOBAL_LIMIT_LEFT;
		boolean bool2 = c.sextantGlobalPiece < -GLOBAL_LIMIT_RIGHT;
		if (!bool1 && !bool2)
			moveLandScape(c, left);
		if (bool1)
			c.sextantGlobalPiece = GLOBAL_LIMIT_LEFT;
		if (bool2)
			c.sextantGlobalPiece = -GLOBAL_LIMIT_RIGHT;
		updateSextant(c);

	}

	/* moving the telescope + arc */
	private static void moveSextantGlobalPieceWithFormula(Player c) {
		int degree = c.sextantGlobalPiece < -GLOBAL_LIMIT_RIGHT ? -GLOBAL_LIMIT_RIGHT : c.sextantGlobalPiece > GLOBAL_LIMIT_LEFT ? GLOBAL_LIMIT_LEFT : c.sextantGlobalPiece;
		int realDegree = degree > 0 ? degree : 2047 - (-degree);
		c.getActionSender().sendFrame230(SEXTANT_TELESCOPE, SEXTANT_ROTATION1, realDegree, SEXTANT_GLOBAL_ZOOM);
		c.getActionSender().sendFrame230(SEXTANT_ARC, SEXTANT_ROTATION1, realDegree, SEXTANT_GLOBAL_ZOOM);

	}

	/* moving the landscape horizon */
	public static void moveLandScape(Player c, boolean up) {
		if (up)
			c.sextantLandScapeCoords += 7;
		else
			c.sextantLandScapeCoords -= 7;
		updateSextant(c);

		boolean bool1 = c.sextantLandScapeCoords > LANDSCAPE_LIMIT_DOWN;
		boolean bool2 = c.sextantLandScapeCoords < -LANDSCAPE_LIMIT_UP;
		if (bool1)
			c.sextantLandScapeCoords = LANDSCAPE_LIMIT_DOWN;
		if (bool2)
			c.sextantLandScapeCoords = -LANDSCAPE_LIMIT_UP;

	}

	/* moving the landscape horizon with a formula */
	public static void moveLandScapeWithFormula(Player c) {
		c.getActionSender().moveInterface(0, c.sextantLandScapeCoords, SEXTANT_LANDSCAPE);
	}

	/* moves the sun by two pixels */
	public static void moveSun(Player c, boolean up) {
		if (up)
			c.sextantSunCoords += 2;
		else
			c.sextantSunCoords -= 2;
		updateSextant(c);
	}

	/* moves the sun to a specific coordinate */
	public static void moveSunWithFormula(Player c) {
		c.getActionSender().moveInterface(0, c.sextantSunCoords, SEXTANT_SUN);
	}

	/* display the player position in degree and minutes */
	public static void displayPosition(Player c) {
		String resultX = CoordinateScrolls.calculateActualPosition(c.getPosition().getX(), c.getPosition().getY())[0];
		String resultY = CoordinateScrolls.calculateActualPosition(c.getPosition().getX(), c.getPosition().getY())[1];
		c.getDialogue().sendStatement(resultX, resultY);
		c.getActionSender().sendMessage("the sextant displays:");
		c.getActionSender().sendMessage(resultX);
		c.getActionSender().sendMessage(resultY);
	}

	/* handle the sextant buttons */
	public static boolean handleSextantButtons(Player c, int buttonId) {
		switch (buttonId) {
			case 27043 :
				moveSextantBar(c, true);
				return true;
			case 27042 :
				moveSextantBar(c, false);
				return true;
			case 27041 :
				moveGlobalPiece(c, true);
				return true;
			case 27040 :
				moveGlobalPiece(c, false);
				return true;
			case 27047 : // Sextant give Position
				if (everythingMatches(c))
					displayPosition(c);
				return true;
		}
		return false;
	}
}
