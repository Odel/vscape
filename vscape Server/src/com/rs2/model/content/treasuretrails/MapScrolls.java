package com.rs2.model.content.treasuretrails;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 04/01/12 Time: 16:08 To change
 * this template use File | Settings | File Templates.
 */
public class MapScrolls {

	/* contains the whole map clue */

	public static enum MapCluesData {
		BLACK_FORTERESS(2713, 9507, new Position(3026, 3628), true, 2),
		GALAHAD(2716, 9108, new Position(2612, 3482), false, 1),
		CHAMPION_GUILD(2719, 6994, new Position(3166, 3360), false, 2),
		FALADOR_ROCK(2722, 7271, new Position(3043, 3399), false, 2),
		DRAYNOR(2827, 7113, new Position(3092, 3226), false, 2),
		GOBLIN_VILLAGE(2829, 9454, new Position(2459, 3179), true, 3),
		NECROMANCER(3516, 9632, new Position(2650, 3230), false, 3),
		LUMBERYARD(3518, 7221, new Position(3309, 3503), true, 1),
		VARROCK_MINE(3520, 7045, new Position(3290, 3373), false, 1),
		YANILLE(3522, 9043, new Position(2616, 3077), false, 1),
		RIMMINGTON(3524, 9839, new Position(2924, 3209), false, 1),
		HOBGOBLIN(3525, 4305, new Position(2906, 3294), false, 2),
		MCGRUBOR(3596, 9196, new Position(2658, 3488), true, 2),
		CLOCK_TOWER(3599, 9720, new Position(2565, 3248), true, 1),
		//WEST_ARDOUGNE(3601, 9359, new Position(2488, 3308), false, 1), Disabled until plague city or w/e
		WIZARD_TOWER(3602, 9275, new Position(3109, 3153), false, 2),
		MORT_TON(7236, 17774, new Position(3434, 3265), false, 3),
		CHAOS_ALTAR(7239, 17888, new Position(2454, 3230), false, 3),
		WILDERNESS_AGILITY(7241, 17620, new Position(3021, 3912), false, 2),
		MISCELLANIA(7286, 17687, new Position(2535, 3865), false, 1),
		CAMELOT(7288, 18055, new Position(2666, 3562), false, 3),
		LEGEND_GUILD(7290, 17634, new Position(2723, 3339), false, 3),
		FALADOR_STATUE(7292, 17537, new Position(2970, 3414), false, 2),
		RELLEKA(7294, 17907, new Position(2579, 3597), false, 2),
		CRAFTING_GUILD_EAST(3598, 7162, new Position(2702, 3428), false, 1);

		private int clueId;
		private int interfaceId;
		private Position finalPosition;
		private boolean isCrate;
		private int level;

		private static Map<Position, MapCluesData> positions = new HashMap<Position, MapCluesData>();
		private static Map<Integer, MapCluesData> clues = new HashMap<Integer, MapCluesData>();

		public static MapCluesData forIdPosition(Position position) {
			for (int i = 0; i < MapCluesData.values().length; i++) {
				if (MapCluesData.values()[i].getFinalPosition().equals(position)) {
					return MapCluesData.values()[i];
				}
			}
			return null;
		}
		public static MapCluesData forIdClue(int clueId) {
			return clues.get(clueId);
		}

		static {
			for (MapCluesData data : MapCluesData.values()) {
				positions.put(data.finalPosition, data);
				clues.put(data.clueId, data);
			}
		}

		MapCluesData(int clueId, int interfaceId, Position finalPosition, boolean crate, int level) {
			this.clueId = clueId;
			this.interfaceId = interfaceId;
			this.finalPosition = finalPosition;
			isCrate = crate;
			this.level = level;
		}

		public int getClueId() {
			return clueId;
		}

		public int getInterfaceId() {
			return interfaceId;
		}

		public Position getFinalPosition() {
			return finalPosition;
		}

		public boolean isCrate() {
			return isCrate;
		}

		public int getLevel() {
			return level;
		}
	}

	/* loading the clue interface */

	public static boolean loadClueInterface(Player player, int itemId) {
		MapCluesData mapCluesData = MapCluesData.forIdClue(itemId);
		if (mapCluesData == null) {
			return false;
		}
		if (mapCluesData.getInterfaceId() < 0) {
			return false;
		}
		player.getActionSender().sendInterface(mapCluesData.getInterfaceId());
		return true;
	}

	/* handles the digging clue method */

	public static boolean digClue(Player player) {
		MapCluesData mapCluesData = MapCluesData.forIdPosition(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
		if (mapCluesData == null) {
			return false;
		}
		if (!player.getInventory().playerHasItem(mapCluesData.getClueId())) {
			return false;
		}
		player.getInventory().removeItem(new Item(mapCluesData.getClueId(), 1));
		switch (mapCluesData.getLevel()) {
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

	/* getting a random clue scroll */

	public static int getRandomScroll(int level) {
		int pick = new Random().nextInt(MapCluesData.values().length);
		while (MapCluesData.values()[pick].getLevel() != level) {
			pick = new Random().nextInt(MapCluesData.values().length);
		}

		return MapCluesData.values()[pick].getClueId();
	}

	/* handle crate clicking for some of the clues */

	public static boolean handleCrate(Player player, int objectX, int objectY) {
		MapCluesData mapCluesData = MapCluesData.forIdPosition(new Position(objectX, objectY));
		if (mapCluesData == null) {
			return false;
		}
		if (!mapCluesData.isCrate()) {
			return false;
		}
		if (!player.getInventory().playerHasItem(mapCluesData.getClueId())) {
			return false;
		}
		player.getInventory().removeItem(new Item(mapCluesData.getClueId(), 1));
		player.getUpdateFlags().sendAnimation(832);
		ClueScroll.clueReward(player, mapCluesData.getLevel(), "You've found another clue!", false, "");
		return true;
	}

}