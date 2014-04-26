package com.rs2.model.content.treasuretrails;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 13/01/12 Time: 22:37 To change
 * this template use File | Settings | File Templates.
 */
public class DiggingScrolls {

	public static enum DigData {
		DIG1(new String[]{"Dig near some giant mushrooms ", " behind the GrandTree"}, 3618, new Position(2458, 3504), 1),
		DIG2(new String[]{"46 is my number, My body is", "the colour of burnt orange ", "and crawls among those with eight. ", "Three mouths I have, yet I cannot eat.","My blinking blue eye hides my grave"}, 7274, new Position(3170, 3885), 3),
		DIG3(new String[]{"As you desert this town, keep ", "that could ruin nearby rugs: ", "dig carefully around the ", "greenery"}, 3609, new Position(3397, 2915), 3),
		DIG4(new String[]{"By the town of the dead, ", "walk south down a rickety ", "bridge, then dig near the ", "slime-covered tree."}, 3611, new Position(3647, 3496), 3),
		DIG5(new String[]{"Come to the evil ", "ledge, Yew know yew ", "want to, And try not to ", "get stung."}, 7238, new Position(3088, 3469), 3),
		DIG6(new String[]{"Covered in shadows, ", "the centre of the ", "circle is where you will ", "find the answer."}, 7252, new Position(3489, 3289), 3),
		DIG7(new String[]{"I lie lonely and forgotten ", "in mid wilderness, ", "Where the dead rise ", "from their beds. Feel ", "free to quarrel and wind ", "me up, and dig while ", "you shoot their heads."}, 7270, new Position(3174, 3663), 3),
		DIG8(new String[]{"The beasts to my east ", "snap claws and tails. ", "The rest to my west can ", "slide and eat fish. The ", "northern are silly and ", "jump and wail. Dig by ", "my fire and make a ", "wish."}, 7250, new Position(2599, 3266), 3),

		;
		private String[] hints;
		private int clueId;
		private Position diggingPosition;
		private int level;

		private static Map<Integer, DigData> clues = new HashMap<Integer, DigData>();
		private static Map<Position, DigData> positions = new HashMap<Position, DigData>();

		public static DigData forIdPosition(Position position) {
			for (int i = 0; i < DigData.values().length; i++) {
				if (DigData.values()[i].getDiggingPosition().equals(position)) {
					return DigData.values()[i];
				}
			}
			return null;
		}

		public static DigData forIdClue(int clueId) {
			return clues.get(clueId);
		}

		static {
			for (DigData data : DigData.values()) {
				clues.put(data.clueId, data);
				positions.put(data.diggingPosition, data);
			}
		}

		DigData(String[] hints, int clueId, Position diggingPosition, int level) {
			this.hints = hints;
			this.clueId = clueId;
			this.diggingPosition = diggingPosition;
			this.level = level;
		}

		public String[] getHints() {
			return hints;
		}

		public int getClueId() {
			return clueId;
		}

		public Position getDiggingPosition() {
			return diggingPosition;
		}

		public int getLevel() {
			return level;
		}
	}

	public static boolean loadClueInterface(Player player, int itemId) {
		DigData digData = DigData.forIdClue(itemId);
		if (digData == null) {
			return false;
		}
		player.getActionSender().sendInterface(ClueScroll.CLUE_SCROLL_INTERFACE);
		for (int i = 0; i < digData.getHints().length; i++) {
			player.getActionSender().sendString(digData.getHints()[i], getChilds(digData.getHints())[i]);
		}
		return true;
	}

	public static boolean digClue(Player player) {
		DigData digData = DigData.forIdPosition(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
		if (digData == null) {
			return false;
		}
		if (!player.getInventory().playerHasItem(digData.getClueId())) {
			return false;
		}
		player.getInventory().removeItem(new Item(digData.getClueId(), 1));
		switch (digData.getLevel()) {
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

	/* put the right childs ids on the interface */

	public static int[] getChilds(String[] sentences) {
		switch (sentences.length) {
			case 1 :
				return new int[]{6971};
			case 2 :
				return new int[]{6971, 6972};
			case 3 :
				return new int[]{6970, 6971, 6972};
			case 4 :
				return new int[]{6970, 6971, 6972, 6973};
			case 5 :
				return new int[]{6969, 6970, 6971, 6972, 6973};
			case 6 :
				return new int[]{6969, 6970, 6971, 6972, 6973, 6974};
			case 7 :
				return new int[]{6968, 6969, 6970, 6971, 6972, 6973, 6974};
			case 8 :
				return new int[]{6968, 6969, 6970, 6971, 6972, 6973, 6974, 6975};
		}
		return null;
	}

	public static int getRandomScroll(int level) {
		int pick = new Random().nextInt(DigData.values().length);
		while (DigData.values()[pick].getLevel() != level) {
			pick = new Random().nextInt(DigData.values().length);
		}

		return DigData.values()[pick].getClueId();
	}

}
