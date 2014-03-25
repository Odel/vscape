package com.rs2.model.content.treasuretrails;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 08/01/12 Time: 13:23 To change
 * this template use File | Settings | File Templates.
 */
public class SearchScrolls {

	/* the whole search clue data */
	public static enum SearchData {

		CRATE1(new String[]{"Search the crates in Canifis"}, 2709, new Position(3509, 3497, 0), -1, 832, 1),
		CRATE2(new String[]{"Search for a crate in a ", "building in Hemenster"}, 2701, new Position(2636, 3453, 0), -1, 832, 1),
		CRATE3(new String[]{"Search for a crate in Varrock", "Palace."}, 2702, new Position(3224, 3492, 0), -1, 832, 1),
		CRATE4(new String[]{"Search for a crate on the ", "ground floor of a building in", "Seers' Village"}, 2703, new Position(2699, 3470, 0), -1, 832, 1),
		CRATE5(new String[]{"Search for a crate on the ", "ground floor of a house in", "Seers' Village"}, 2704, new Position(2699, 3470, 0), -1, 832, 1),
		CRATE6(new String[]{"Search the crate in the left-", "hand tower of Lumbridge", "castle"}, 2705, new Position(3228, 3212, 1), -1, 832, 1),
		CRATE7(new String[]{"Search the crate near a cart ", "in Port Khazard."}, 2706, new Position(2660, 3149, 0), -1, 832, 1),
		CRATE8(new String[]{"Search the crates in a house ", "in Yanille that has a piano"}, 2707, new Position(2598, 3105, 0), -1, 832, 2),
		CRATE11(new String[]{"Search the crates in Draynor ", "Manor"}, 2710, new Position(3106, 3369, 2), -1, 832, 1),
		CRATE13(new String[]{"Search the crates in Horvik's ", "armoury"}, 2711, new Position(3228, 3433, 0), -1, 832, 1),
		CRATE14(new String[]{"Search the crates in", "Barbarian Village", "helmet shop"}, 2712, new Position(3073, 3430, 0), -1, 832, 1), 
		CRATE15(new String[]{"Search the crates in the ", "guard house of the northern ", "gate of East Ardougne."}, 2773, new Position(2645, 3338, 0), 832, -1, 1),
		CRATE16(new String[]{"Search the crates in the ", "northernmost house in Al ", "Kharid"}, 2774, new Position(3289, 3202, 0), 832, -1, 1), 
		CRATE17(new String[]{"Search the crates in the Port ", "Sarim fishing shop"}, 2776, new Position(3012, 3222, 0), -1, 832, 1),
		CRATE18(new String[]{"Search the crates in the shed ", "just north of east Ardougne"}, 2778, new Position(2617, 3347, 0), -1, 832, 1),
		CRATE19(new String[]{"Search the crates just ", "outside the Armour shop in ", "East Ardougne"}, 2780, new Position(2654, 3299, 0), -1, 832, 1),
		CRATE20(new String[]{"Search the crates near a cart ", "in Varrock."}, 2782, new Position(3226, 3452, 0), -1, 832, 1),
		CRATE12(new String[]{"Search the crates in East", "Ardougne's general store."}, 3493, new Position(2615, 3291, 0), -1, 832, 1),
		CRATE21(new String[]{"Search the crates inside ", "of houses in eastern part of", "Falador"}, 2855, new Position(3029, 3355, 0), -1, 832, 1),
		CRATE22(new String[]{"A crate found in the tower of a church is your next  ", "Position."}, 3494, new Position(2612, 3307, 2), -1, 832, 1),
		CRATE23(new String[]{"A town with a different sort of", "night-life is your destination. ", "Search for some crates in one", "of the houses."}, 3495, new Position(3498, 3507, 0), -1, 832, 2),

		BOXES1(new String[]{"Search the boxes in one of ", "the tents in Al Kharid"}, 2785, new Position(3308, 3206, 0), -1, 832, 1),
		BOXES2(new String[]{"Search the boxes in the ", "Goblin house near Lumbridge"}, 2786, new Position(3245, 3245, 0), -1, 832, 1),
		BOXES3(new String[]{"Search the boxes in the ", "house near the South ", "entrance of Varrock"}, 2788, new Position(3203, 3384, 0), -1, 832, 1),
		BOXES4(new String[]{"Search the boxes near a cart ", "in Varrock"}, 2790, new Position(3228, 3454, 0), -1, 832, 1),
		BOXES6(new String[]{"Search the boxes of", "Falador's general store"}, 2793, new Position(2955, 3390, 0), -1, 832, 1),
		BOXES7(new String[]{"Search the tents in the ", "imperial guard camp in", "Burthorpe for some boxes"}, 3491, new Position(2885, 3540, 0), -1, 832, 1),
		BOXES8(new String[]{"Search the boxes in a shop ", "in Taverley"}, 2708, new Position(2886, 3449, 0), -1, 832, 1),

		CHEST1(new String[]{"Search the chest in the Duke ", "of Lumbridge's bedroom"}, 2794, new Position(3209, 3218, 1), 378, 832, 1),
		CHEST2(new String[]{"Search the chest in the left-", "hand tower of Camelot castle"}, 2796, new Position(2748, 3495, 2), 378, 832, 1),
		CHEST3(new String[]{"Search the chests in the", "Dwarven Mine"}, 2797, new Position(3000, 9798, 0), 378, 832, 1),
		CHEST4(new String[]{"Search the chests upstairs in", "Al Kharid palace."}, 2799, new Position(3301, 3169, 1), 378, 832, 1),
		CHEST5(new String[]{"Search chests found in the", "upstairs of shops in Port", "Sarim"}, 2831, new Position(3016, 3205, 1), 378, 832, 1),
		CHEST6(new String[]{"Search through chests found ", "in the upstairs of houses in ", "eastern Falador."}, 2833, new Position(3041, 3364, 1), 378, 832, 1),

		DRAWERS1(new String[]{"Search the drawers above", "Varrock's shops."}, 2835, new Position(3206, 3419, 1), 351, 832, 1),
		DRAWERS3(new String[]{"Search the drawers in a ", "house in Draynor Village."}, 2839, new Position(3097, 3277, 0), 351, 832, 1),
		DRAWERS4(new String[]{"Search the drawers in", "Falador's chainmail shop"}, 2841, new Position(2969, 3311, 0), 349, 832, 1),
		DRAWERS5(new String[]{"Search the drawers in one of", "Gertrude's bedrooms"}, 2843, new Position(3156, 3406, 0), 349, 832, 1),
		DRAWERS7(new String[]{"Search the drawers in the ", "upstairs of a house in ", "Catherby."}, 2847, new Position(2809, 3451, 1), 351, 832, 1),
		DRAWERS8(new String[]{"Search the drawers in the ", "upstairs of the bank to the ", "east of Varrock"}, 2848, new Position(3250, 3420, 1), 349, 832, 1),
		DRAWERS9(new String[]{"Search the drawers of houses ", "in Burthorpe"}, 2849, new Position(2929, 3570, 0), 349, 832, 1),
		DRAWERS10(new String[]{"Search the drawers on the ", "first floor of a building ", "overlooking Ardougne's ", "market"}, 2851, new Position(2657, 3322, 1), 353, 832, 1),
		DRAWERS11(new String[]{"Search the drawers upstairs ", "in Falador's shield shop."}, 2853, new Position(2971, 3386, 1), 349, 832, 1),
		DRAWERS12(new String[]{"Search through some drawers ", "found in Taverley's houses."}, 2857, new Position(2894, 3418, 0), 351, 832, 1),
		DRAWERS13(new String[]{"Search through some drawers ", "in the upstairs of a house in ", "Rimmington."}, 2858, new Position(2970, 3214, 1), 353, 832, 1),
		DRAWERS14(new String[]{"Search the upstairs drawers ", "of a house in a village were ", "pirates are known to have a ", "good time."}, 3490, new Position(2809, 3165, 1), 349, 832, 2),
		DRAWERS15(new String[]{"Search upstairs in the ", "houses of Seers' Village for ", "some drawers."}, 3492, new Position(2716, 3471, 1), 348, 832, 1),

		/* hard clues */

		CRIPTICS1(new String[]{"A great view: watch the rapidly ", "drying hides get splashed. ", "Check the box you are sitting ", "on."}, 3506, new Position(2523, 3493, 1), 0, 832, 3),
		CRIPTICS2(new String[]{"Four blades I have, yet draw no ", "blood. Still I turn my prey to ", "powder. If you are brave, come ", "search my roof; It is there my ", "blades are louder."}, 7245, new Position(3166, 3309, 2), 0, 832, 3),
		CRIPTICS3(new String[]{"His head might be hollow, ", "but the crates nearby are ", "filled with surprises."}, 7249, new Position(3478, 3091, 0), 0, 832, 3),
		CRIPTICS4(new String[]{"If you look closely ", "enough, it seems that ", "the archers have lost ", "more than their needles"}, 3604, new Position(2671, 3415, 0), 0, 832, 3),
		CRIPTICS5(new String[]{"In a village made of ", "bamboo look for some ", "crates under one of the ", "houses."}, 3605, new Position(2800, 3074, 0), 0, 832, 3),
		CRIPTICS6(new String[]{"It seems to have ", "reached the end of the ", "line, and it's still empty."}, 3607, new Position(3041, 9820, 0), 0, 832, 3),
		CRIPTICS7(new String[]{"My home is grey, and ", "made of stone, A castle ", "with a search for a ", "meal, Hidden in some ", "drawers I am, Across ", "from a wooden wheel."}, 3610, new Position(3213, 3216, 1), 5619, 832, 3),
		CRIPTICS8(new String[]{"North of the best monkey ", "restaurant on Karamja, look ", "for the centre of the triangle ", "of boats and search there."}, 3612, new Position(2905, 3189, 0), 0, 832, 3),
		CRIPTICS9(new String[]{"Probably filled ", "with books on ", "magic"}, 3613, new Position(3096, 9571, 0), 0, 832, 3),
		CRIPTICS10(new String[]{"Read 'How to ", "breed scorpions' ", "by O.W.Thathurt"}, 3614, new Position(2702, 3409, 1), 0, 832, 3),
		CRIPTICS11(new String[]{"The cheapest water for ", "miles around, but they ", "react badly to religious ", "icons."}, 3615, new Position(3178, 2987, 0), 0, 832, 3),
		CRIPTICS12(new String[]{"The crate on the ", "first floor of a ", "church is your next ", "Position."}, 3616, new Position(2612, 3304, 1), 0, 832, 3),
		CRIPTICS13(new String[]{"The crate in the tower", "of a church is your next ", "Position."}, 3617, new Position(2612, 3305, 2), 0, 832, 3),
		CRIPTICS14(new String[]{"The owner of this crate ", "has a hunch that he put ", "more than fish inside."}, 7243, new Position(2770, 3172, 0), 0, 832, 3),
		CRIPTICS15(new String[]{"This crate clearly marks ", "the end of the line for ", "coal."}, 7247, new Position(2691, 3508, 0), 0, 832, 3),
		CRIPTICS16(new String[]{"This crate holds a better ", "reward than a broken ", "arrow"}, 7248, new Position(2671, 3437, 0), 0, 832, 3),
		CRIPTICS17(new String[]{"This crate is mine, all ", "mine, even if it is in the ", "middle of the desert."}, 7272, new Position(3289, 3022, 0), 7274, 832, 3),
		CRIPTICS18(new String[]{"This village has a ", "problem with cartloads ", "of the undead. Try ", "checking the bookcase ", "to find the answer."}, 7251, new Position(2833,2991, 0), 0, 832, 3),
		CRIPTICS19(new String[]{"Try not to let yourself be ", "dazzled when you ", "search these drawers."}, 7253, new Position(2561, 3323, 0), 351, 832, 3),
		CRIPTICS21(new String[]{"When no weapons are ", "at hand, then is the ", "time to reflect. In ", "Saradomin's name, ", "redemption draws ", "closer..."}, 7255, new Position(2818, 3351, 0), 351, 832, 3),
		CRIPTICS22(new String[]{"You have all of the ", "elements available to ", "solve this clue. ", "Fortunately you do ", "not have to go so far ", "as to stand in a draft."}, 7268, new Position(2569, 3118, 0), 0, 832, 3),
		CRIPTICS23(new String[]{"Throat mage seeks ", "companionship. Seek ", "answers inside my ", "furniture if interested."}, 7254, new Position(2668, 3238), 353, 832, 3),
		/* the clues that needs key */

		KEY_CRIPTICS1(new String[]{"Find a bar with a centre fountain ", "in its city. Go upstairs and get ", "changed."}, 7276, new Position(3229, 3400, 1), 0, 832, 3),
		KEY_CRIPTICS2(new String[]{"Go to the ", "village being ", "attacked by ", "trolls, search ", "the drawers ", "while you are ", "there."}, 7278, new Position(2921, 3577, 0), 351, 832, 3),
		KEY_CRIPTICS3(new String[]{"Go to this ", "building to be ", "illuminated, and ", "search the ", "drawers while ", "you're there."}, 7280, new Position(2512, 3641, 1), 351, 832, 3),
		KEY_CRIPTICS4(new String[]{"In a town where ", "everyone has perfect ", "vision, seek some ", "locked drawers in a ", "house that sits opposite ", "a workshop."}, 7282, new Position(2709, 3478, 0), 351, 832, 3),
		KEY_CRIPTICS5(new String[]{"In a town where thieves ", "steal from stalls, search ", "for some drawers ", "upstairs of a house near ", "the bank."}, 7284, new Position(2611, 3324, 1), 349, 832, 3),
		KEY_CRIPTICS6(new String[]{"In a town where wizards ", "are known to gather, ", "search upstairs in a ", "large house to the ", "north."}, 7296, new Position(2593, 3108, 1), 378, 832, 3),
		KEY_CRIPTICS7(new String[]{"In a town where the ", "guards are armed with ", "maces, search the ", "upstairs room of the ", "Public House."}, 7298, new Position(2574, 3326, 1), 349, 832, 3),
		KEY_CRIPTICS8(new String[]{"Probably filled ", "with wizards' ", "socks"}, 7300, new Position(3116, 9562, 0), 351, 832, 3),
		KEY_CRIPTICS9(new String[]{"The dead, red dragon ", "watches over this chest. ", "He must really dig the ", "view."}, 7301, new Position(3353, 3332, 0), 378, 832, 3), 
		KEY_CRIPTICS10(new String[]{"The socks in these ", "drawers are holier than ", "thine, according to the ", "tonsured owners."}, 7303, new Position(3056, 3497, 0), 351, 832, 3),
		KEY_CRIPTICS11(new String[]{"You'll need to look for ", "a town with a central ", "fountain. Look for a ", "locked chest in the ", "town's chapel."}, 7304, new Position(3256, 3487, 0), 378, 832, 3), ;
		private String[] hints;
		private int clueId;
		private Position objectPosition;
		private int newObject;
		private int emote;
		private int level;

		private static Map<Position, SearchData> objects = new HashMap<Position, SearchData>();
		private static Map<Integer, SearchData> clues = new HashMap<Integer, SearchData>();

		public static SearchData forIdObject(Position position) {
			for (int i = 0; i < SearchData.values().length; i++) {
				if (SearchData.values()[i].getObjectPosition().equals(position)) {
					return SearchData.values()[i];
				}
			}
			return null;
		}

		public static SearchData forIdClue(int clueId) {
			return clues.get(clueId);
		}

		static {
			for (SearchData data : SearchData.values()) {
				objects.put(data.objectPosition, data);
				clues.put(data.clueId, data);
			}
		}

		SearchData(String[] hints, int clueId, Position objectPosition, int newObject, int emote, int level) {
			this.hints = hints;
			this.clueId = clueId;
			this.objectPosition = objectPosition;
			this.newObject = newObject;
			this.emote = emote;
			this.level = level;
		}

		public String[] getHints() {
			return hints;
		}

		public int getClueId() {
			return clueId;
		}

		public Position getObjectPosition() {
			return objectPosition;
		}

		public int getNewObject() {
			return newObject;
		}

		public int getEmote() {
			return emote;
		}

		public int getLevel() {
			return level;
		}
	}

	/* loading clue scroll interface */

	public static boolean loadClueInterface(Player player, int itemId) {
		SearchData searchData = SearchData.forIdClue(itemId);
		if (searchData == null) {
			return false;
		}
		player.getActionSender().sendInterface(ClueScroll.CLUE_SCROLL_INTERFACE);
		for (int i = 0; i < searchData.getHints().length; i++) {
			player.getActionSender().sendString(searchData.getHints()[i], getChilds(searchData.getHints())[i]);
		}
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

	/* get random scroll */

	public static int getRandomScroll(int level) {
		int pick = new Random().nextInt(SearchData.values().length);
		while (SearchData.values()[pick].getLevel() != level) {
			pick = new Random().nextInt(SearchData.values().length);
		}

		return SearchData.values()[pick].getClueId();
	}

	/* handles the object clicking */
	public static boolean handleObject(final Player player, GameObjectDef p) {
		SearchData searchData = SearchData.forIdObject(new Position(p.getPosition().getX(), p.getPosition().getY(), player.getPosition().getZ()));
		if (searchData == null) {
			return false;
		}
		if (!player.getInventory().playerHasItem(searchData.getClueId()) || player.getPosition().getZ() != searchData.getObjectPosition().getZ()) {
			return false;
		}
		if (!KeyToClue.handleKey(player, searchData.getClueId())) {
			return true;
		}
		if (searchData.getNewObject() > 0) {
			new GameObject(searchData.getNewObject(), p.getPosition().getX(), p.getPosition().getY(), player.getPosition().getZ(), p.getFace(), p.getType(), p.getId(), 30);
		}
		player.getInventory().removeItem(new Item(searchData.getClueId(), 1));
		player.getUpdateFlags().sendAnimation(searchData.getEmote());
		ClueScroll.clueReward(player, searchData.getLevel(), "You've found another clue!", false, "You've found a casket!");
		return true;
	}

}
