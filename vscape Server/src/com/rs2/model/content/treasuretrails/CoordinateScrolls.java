package com.rs2.model.content.treasuretrails;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 15/01/12 Time: 11:03 To change
 * this template use File | Settings | File Templates.
 */
public class CoordinateScrolls {

	/* the observatory center Position */

	public static final Position OBSERVATORY_Position = new Position(2440, 3161, 0);

	/* constants for mathematical use */

	public static final double ONE_MINUTE_TO_SQUARE = 1.875;

	public static final int ONE_DEGREE_TO_MINUTE = 60;

	/* set of constants for the tools */

	public static final int SEXTANT = 2574;
	public static final int WATCH = 2575;
	public static final int CHART = 2576;

	/* contains all the coordinate clues */

	public static enum CoordinateData {
		COORDINATE_1(2723, 25, 3, 23, 24, "north", "east", 3), //correct
		COORDINATE_2(2725, 25, 3, 17, 5, "north", "east", 3), //correct
		COORDINATE_3(2731, 24, 58, 18, 43, "north", "east", 3), //correct
		COORDINATE_4(2733, 24, 56, 22, 58, "north", "east", 3), //correct
		COORDINATE_5(2735, 24, 24, 26, 24, "north", "east", 3), //correct
		COORDINATE_6(2737, 22, 45, 26, 33, "north", "east", 3), //correct
		COORDINATE_7(2739, 22, 35, 19, 18, "north", "east", 3), //correct
		COORDINATE_8(2741, 22, 30, 3, 1, "north", "east", 2), //correct
		COORDINATE_9(2743, 21, 24, 17, 54, "north", "east", 3), //correct
		COORDINATE_10(2745, 20, 33, 15, 48, "north", "east", 3), //fixed //correct
		COORDINATE_11(2801, 20, 7, 18, 33, "north", "east", 3), //correct
		COORDINATE_12(2805, 20, 5, 21, 52, "north", "east", 3), //correct
		COORDINATE_13(2807, 19, 43, 25, 07, "north", "east", 3), //correct
		COORDINATE_14(2809, 18, 22, 16, 33, "north", "east", 3), //correct
//		COORDINATE_15(2811, 18, 3, 25, 16, "north", "east", 3), //wrong x y possibly no 06 reference
		COORDINATE_16(2813, 17, 50, 8, 30, "north", "east", 3), //correct
		COORDINATE_17(2815, 16, 43, 19, 13, "north", "east", 3), //correct
		COORDINATE_18(2819, 16, 35, 27, 1,"north", "east", 3), //correct
		COORDINATE_19(2817, 16, 20, 12, 45, "north", "east", 3), //correct for 06 (maybe)
//		COORDINATE_20(2821, 15, 48, 13, 52, "north", "east", 3), //area is correct but block is unwalkable
		COORDINATE_21(2823, 14, 54, 9, 13, "north", "east", 2), //correct
		COORDINATE_22(2825, 13, 46, 21, 1, "north", "east", 3), //correct
		COORDINATE_23(3526, 12, 48, 20, 20, "north", "east", 3), //correct
		COORDINATE_24(3528, 11, 41, 14, 58, "north", "east", 2), //correct for 06 (maybe)
		COORDINATE_25(3530, 11, 5, 0, 45, "north", "west", 2), //correct
		COORDINATE_26(3532, 11, 3, 31, 20, "north", "east", 2), //correct for 06
		COORDINATE_27(3534, 9, 48, 17, 39, "north", "east", 2), //correct
		COORDINATE_29(3536, 8, 33, 1, 39, "north", "east", 2), //correct
		COORDINATE_30(3538, 8, 26, 10, 28, "south", "east", 3), //correct
		COORDINATE_31(3540, 8, 5, 15, 56, "south", "east", 3), //correct
		COORDINATE_32(3542, 8, 3, 31, 16, "north", "east", 3), //correct
		COORDINATE_33(3544, 7, 43, 12, 26, "south", "east", 3), //correct 
		COORDINATE_34(3546, 7, 33, 15, 0, "north", "east", 2), //correct
		COORDINATE_35(3548, 7, 5, 30, 56, "north", "east", 2), //correct
		COORDINATE_36(3550, 6, 31, 1, 47, "north", "west", 2), //fixed //correct
		COORDINATE_37(3552, 6, 11, 15, 7, "south", "east", 3), //correct
		COORDINATE_38(3554, 6, 0, 21, 48, "south", "east", 3), //correct
		COORDINATE_39(3556, 5, 50, 10, 5, "south", "east", 3), //correct
		COORDINATE_40(3558, 5, 43, 23, 5, "north", "east", 2), //correct
		COORDINATE_41(3560, 5, 37, 31, 15, "north", "east", 3), //correct
		COORDINATE_42(3562, 5, 20, 4, 28, "south", "east", 2), //correct
		COORDINATE_43(3582, 4, 41, 3, 9, "north", "west", 3), //correct
		COORDINATE_44(3584, 4, 16, 16, 16, "south", "east", 3), //correct
		COORDINATE_45(3586, 4, 13, 12, 45, "north", "east", 2), //correct
		COORDINATE_46(3588, 4, 5, 4, 24, "south", "east", 3), //correct
		COORDINATE_47(3590, 4, 3, 3, 11, "south", "east", 3), //correct
		COORDINATE_48(3592, 4, 0, 12, 46, "south", "east", 2), //correct
		COORDINATE_49(3594, 3, 45, 22, 45, "south", "east", 3), //correct
		COORDINATE_50(7256, 3, 35, 13, 35, "south", "east", 2), //correct 
		COORDINATE_51(7258, 2, 50, 6, 20, "north", "east", 2), //correct
		COORDINATE_52(7260, 2, 48, 22, 30, "north", "east", 2), //correct
//		COORDINATE_53(7262, 2, 46, 29, 11, "north", "east", 2), //correct //duel arena
		COORDINATE_54(7264, 1, 35, 7, 28, "south", "east", 3), //correct
		COORDINATE_55(7266, 1, 26, 8, 1, "north", "east", 2), //correct
		COORDINATE_56(7305, 1, 24, 8, 6, "north", "west", 3), //fixed //moved west by 1 minute from 5 to 6 (should be 5 but spawns in a tree)
		COORDINATE_57(7307, 1, 18, 14, 15, "south", "east", 2), //correct
		COORDINATE_58(7309, 0, 31, 17, 43, "south", "east", 2), //correct
		COORDINATE_59(7311, 0, 30, 24, 16, "north", "east", 2), //correct
		COORDINATE_60(7313, 0, 20, 23, 15, "south", "east", 2), //correct
		COORDINATE_61(7315, 0, 18, 9, 28, "south", "east", 2), //correct
		COORDINATE_62(7317, 0, 13, 13, 58, "south", "east", 2), //fixed //correct
		COORDINATE_63(2747, 0, 5, 1, 13, "south", "east", 2), //correct
		COORDINATE_64(2803, 0, 0, 7, 13, "north", "west", 3), //correct for 06
		;
		private int clueId;
		private int degree1;
		private int minutes1;
		private int degree2;
		private int minutes2;
		private String hint1;
		private String hint2;
		private int level;

		private Position diggingPosition;

		private static Map<Integer, CoordinateData> clues = new HashMap<Integer, CoordinateData>();
		private static Map<Position, CoordinateData> positions = new HashMap<Position, CoordinateData>();

		public static CoordinateData forIdPosition(Position position) {
			for (int i = 0; i < CoordinateData.values().length; i++) {
				if (CoordinateData.values()[i].getDiggingPosition().equals(position)) {
					return CoordinateData.values()[i];
				}
			}
			return null;
		}

		public static CoordinateData forIdClue(int clueId) {
			return clues.get(clueId);
		}

		static {
			for (CoordinateData data : CoordinateData.values()) {
				data.diggingPosition = calculateDiggingPosition(data.degree1, data.minutes1, data.degree2, data.minutes2, data.hint1, data.hint2);
				clues.put(data.clueId, data);
				positions.put(data.diggingPosition, data);

			}
		}

		CoordinateData(int clueId, int degree1, int minutes1, int degree2, int minutes2, String hint1, String hint2, int level) {
			this.clueId = clueId;
			this.degree1 = degree1;
			this.minutes1 = minutes1;
			this.degree2 = degree2;
			this.minutes2 = minutes2;
			this.hint1 = hint1;
			this.hint2 = hint2;
			this.level = level;
		}

		public int getClueId() {
			return clueId;
		}

		public int getDegree1() {
			return degree1;
		}

		public int getMinutes1() {
			return minutes1;
		}

		public int getDegree2() {
			return degree2;
		}

		public int getMinutes2() {
			return minutes2;
		}

		public String getHint1() {
			return hint1;
		}

		public String getHint2() {
			return hint2;
		}

		public int getLevel() {
			return level;
		}

		public Position getDiggingPosition() {
			return diggingPosition;
		}
	}

	/* loading the clue scroll interfaces */

	public static boolean loadClueInterface(Player player, int itemId) {
		CoordinateData coordinateData = CoordinateData.forIdClue(itemId);
		if (coordinateData == null) {
			return false;
		}
		player.getActionSender().sendInterface(ClueScroll.CLUE_SCROLL_INTERFACE);
		player.getActionSender().sendString(putZeroToNumber(coordinateData.getDegree1()) + " degrees " + putZeroToNumber(coordinateData.getMinutes1()) + " minutes " + coordinateData.getHint1(), 6971);
		player.getActionSender().sendString(putZeroToNumber(coordinateData.getDegree2()) + " degrees " + putZeroToNumber(coordinateData.getMinutes2()) + " minutes " + coordinateData.getHint2(), 6972);
		return true;
	}

	/* handling digging */

	public static boolean digClue(Player player) {
		CoordinateData coordinateData = CoordinateData.forIdPosition(new Position(player.getPosition().getX(), player.getPosition().getY()));
		if (coordinateData == null) {
			return false;
		}
		if (!player.getInventory().playerHasItem(coordinateData.getClueId())) {
			return false;
		}
		/*if (!player.getInventory().getItemContainer().contains(CHART) || !player.getInventory().getItemContainer().contains(SEXTANT) || !player.getInventory().getItemContainer().contains(WATCH)) {
			player.getActionSender().sendMessage("You need a chart, sextant, and watch in order to find the clue.");
			player.getActionSender().sendMessage("You can get these from the observatory professor.");
			return false;
		}*/
		if (!player.hasKilledClueAttacker() && coordinateData.getLevel() == 3) {
			ClueScroll.spawnAttacker(player);
			player.getActionSender().sendMessage("You must kill the wizard before continuing the search!");
			return true;
		}
		player.setKilledClueAttacker(false);

		player.getInventory().removeItem(new Item(coordinateData.getClueId(), 1));
		switch (coordinateData.getLevel()) {
			case 1 :
				player.getInventory().addItemOrDrop(new Item(ClueScroll.CASKET_LV1, 1));
				break;
			case 2 :
				player.getInventory().addItemOrDrop(new Item(ClueScroll.CASKET_LV2, 1));
				break;
			case 3 :
				player.getInventory().addItemOrDrop(new Item(ClueScroll.CASKET_LV3, 1));
				break;
		}
		player.getDialogue().sendStatement("You've found a casket!", ClueScroll.CASKET_LV1);
		return true;
	}

	/* put a 0 next to the number if its under 10 */

	private static String putZeroToNumber(int number) {
		return number < 10 ? "0" + number : "" + number;
	}

	/* calculating the position of digging place with hint provided */

	public static Position calculateDiggingPosition(int degree1, int minutes1, int degree2, int minutes2, String firstHint, String secondHint) {
		int obsX = OBSERVATORY_Position.getX();
		int obsY = OBSERVATORY_Position.getY();

		/* first hint handling */

		if (firstHint == "north") {
			obsY += (int) Math.ceil(((degree1 * ONE_DEGREE_TO_MINUTE + minutes1) / ONE_MINUTE_TO_SQUARE));
		}
		if (firstHint == "south") {
			obsY -= (int) Math.ceil(((degree1 * ONE_DEGREE_TO_MINUTE + minutes1) / ONE_MINUTE_TO_SQUARE));
		}
		if (firstHint == "east") {
			obsX += (int) Math.ceil(((degree1 * ONE_DEGREE_TO_MINUTE + minutes1) / ONE_MINUTE_TO_SQUARE));
		}
		if (firstHint == "west") {
			obsX -= (int) Math.ceil(((degree1 * ONE_DEGREE_TO_MINUTE + minutes1) / ONE_MINUTE_TO_SQUARE));
		}

		/* second hint handling */

		if (secondHint == "north") {
			obsY += (int) Math.ceil(((degree2 * ONE_DEGREE_TO_MINUTE + minutes2) / ONE_MINUTE_TO_SQUARE));
		}
		if (secondHint == "south") {
			obsY -= (int) Math.ceil(((degree2 * ONE_DEGREE_TO_MINUTE + minutes2) / ONE_MINUTE_TO_SQUARE));
		}
		if (secondHint == "east") {
			obsX += (int) Math.ceil(((degree2 * ONE_DEGREE_TO_MINUTE + minutes2) / ONE_MINUTE_TO_SQUARE));
		}
		if (secondHint == "west") {
			obsX -= (int) ((degree2 * ONE_DEGREE_TO_MINUTE + minutes2) / ONE_MINUTE_TO_SQUARE);
		}

		return new Position(obsX, obsY);

	}

	/* gets the hint with coordinate provided */
	public static String[] calculateActualPosition(int x, int y) {
		int obsX = OBSERVATORY_Position.getX();
		int obsY = OBSERVATORY_Position.getY();
		int differenceX = x - obsX;
		int differenceY = y - obsY;
		double minutesX = Math.abs(differenceX) * ONE_MINUTE_TO_SQUARE;
		double minutesY = Math.abs(differenceY) * ONE_MINUTE_TO_SQUARE;
		int finalMinutesX = (int) Math.ceil(minutesX) % ONE_DEGREE_TO_MINUTE;
		int finalMinutesY = (int) Math.ceil(minutesY) % ONE_DEGREE_TO_MINUTE;
		int degreeX = (int) (minutesX / ONE_DEGREE_TO_MINUTE);
		int degreeY = (int) (minutesY / ONE_DEGREE_TO_MINUTE);
		/* setting the first strings */
		String XAxis = (differenceX < 0 ? "west" : "east");
		String YAxis = (differenceY < 0 ? "south" : "north");

		/* returning the final strings */
		return new String[]{degreeY + " degrees, " + finalMinutesY + " minutes " + YAxis, degreeX + " degrees, " + finalMinutesX + " minutes " + XAxis};
	}

	/* getting a random coordinate clue */

	public static int getRandomScroll(int level) {
		int pick = new Random().nextInt(CoordinateData.values().length);
		while (CoordinateData.values()[pick].getLevel() != level) {
			pick = new Random().nextInt(CoordinateData.values().length);
		}

		return CoordinateData.values()[pick].getClueId();
	}

}
