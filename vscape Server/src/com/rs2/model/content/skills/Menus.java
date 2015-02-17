package com.rs2.model.content.skills;

import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 13:55 To change
 * this template use File | Settings | File Templates.
 */
public class Menus {

	public static void sendSkillMenu(Player player, String type) {
		/* CRAFTING */

		if (type == "potteryUnfired") {
			display5Item(player, 1787, 1789, 1791, 5352, 4438, "Pot", "Pie Dish", "Bowl", "Plant pot", "Pot lid");
		} else if (type == "potteryFired") {
			display5Item(player, 1931, 2313, 1923, 5350, 4440, "Pot", "Pie Dish", "Bowl", "Plant pot", "Pot lid");
		} else if (type == "silverCrafting") {
			display4Item(player, 1714, 2961, 5525, 1720, "Unstrung symbol", "Silver sickle", "Tiara", "Unstrung Emblem");
		} else if (type == "spinning") {
			display4Item(player, 1737, 1779, 9436, 6051, "Wool", "Flax", "Sinew", "Magic tree");
		} else if (type == "glassMaking") {
			player.getActionSender().sendInterface(11462);
		} else if (type == "normalLeather") {
			player.getActionSender().sendInterface(2311);
		} else if (type == "hardLeather") {
			display1Item(player, 1131, "Hard leather body");
		} else if (type == "dramenBranch") {
			display1Item(player, 772, "Dramen staff");
		} else if (type == "greenLeather") {
			display3Item(player, 1065, 1099, 1135, "Vamb", "Chaps", "Body");
		} else if (type == "blueLeather") {
			display3Item(player, 2487, 2493, 2499, "Vamb", "Chaps", "Body");
		} else if (type == "redLeather") {
			display3Item(player, 2489, 2495, 2501, "Vamb", "Chaps", "Body");
		} else if (type == "blackLeather") {
			display3Item(player, 2491, 2497, 2503, "Vamb", "Chaps", "Body");
		} else if (type == "snakeskin1") {
			display2Item(player, 6326, 6328, "Bandana", "Boots");
		} else if (type == "snakeskin2") {
			display3Item(player, 6330, 6324, 6322, "Vamb", "Chaps", "Body");
		} else if (type == "weaving") {
			display3ItemSized(player, 3224, 5418, 5376, "Cloth", "Empty sack", "Basket");
		}

		/* FLETCHING */
		else if (type == "shaft") {
			display1Item(player, 53, "Headless arrows");
		} else if (type == "bronzeArrow") {
			display1Item(player, 882, "Bronze arrows");
		} else if (type == "ironArrow") {
			display1Item(player, 884, "Iron arrows");
		} else if (type == "steelArrow") {
			display1Item(player, 886, "Steel arrows");
		} else if (type == "mithrilArrow") {
			display1Item(player, 888, "Mithril arrows");
		} else if (type == "adamantArrow") {
			display1Item(player, 890, "Adamant arrows");
		} else if (type == "runeArrow") {
			display1Item(player, 892, "Rune arrows");
		}
		// dart
		else if (type == "bronzeDart") {
			display1Item(player, 806, "Bronze darts");
		} else if (type == "ironDart") {
			display1Item(player, 807, "Iron darts");
		} else if (type == "steelDart") {
			display1Item(player, 808, "Steel darts");
		} else if (type == "mithrilDart") {
			display1Item(player, 809, "Mithril darts");
		} else if (type == "adamantDart") {
			display1Item(player, 810, "Adamant darts");
		} else if (type == "runeDart") {
			display1Item(player, 811, "Rune darts");
		}
		// brutal arrows
		else if (type == "bronzeBrutalArrow") {
			display1Item(player, 4773, "Bronze brutal arrows");
		} else if (type == "ironBrutalArrow") {
			display1Item(player, 4778, "Iron brutal arrows");
		} else if (type == "steelBrutalArrow") {
			display1Item(player, 4783, "Black brutal arrows");
		} else if (type == "blackBrutalArrow") {
			display1Item(player, 4788, "Steel brutal arrows");
		} else if (type == "mithrilBrutalArrow") {
			display1Item(player, 4793, "Mithril brutal arrows");
		} else if (type == "adamantBrutalArrow") {
			display1Item(player, 4798, "Adamant brutal arrows");
		} else if (type == "runeBrutalArrow") {
			display1Item(player, 4803, "Rune brutal arrows");
		}
		// bow stringing

		else if (type == "shortBow") {
			display1Item(player, 841, "Shortbow");
		} else if (type == "longBow") {
			display1Item(player, 839, "Longbow");
		} else if (type == "oakShortBow") {
			display1Item(player, 843, "Oak shortbow");
		} else if (type == "oakLongBow") {
			display1Item(player, 845, "Oak longbow");
		} else if (type == "compositeOgre") {
			display1Item(player, 4827, "Composite Ogre bow");
		} else if (type == "willowShortBow") {
			display1Item(player, 849, "Willow shortbow");
		} else if (type == "willowLongBow") {
			display1Item(player, 849, "Willow longbow");
		}

		else if (type == "mapleShortBow") {
			display1Item(player, 853, "Maple shortbow");
		} else if (type == "mapleLongBow") {
			display1Item(player, 851, "Maple longbow");
		}

		else if (type == "yewShortBow") {
			display1Item(player, 857, "Yew shortbow");
		}

		else if (type == "yewLongBow") {
			display1Item(player, 855, "Yew longbow");
		}

		else if (type == "magicShortBow") {
			display1Item(player, 861, "Magic shortbow");
		}

		else if (type == "magicLongBow") {
			display1Item(player, 859, "Magic longbow");
		}
		// log cutting
		else if (type == "normalCutting") {
			display4Item(player, 52, 50, 48, 9440, "Arrow shafts", "Shortbow", "Longbow","Wooden Stock");
		} else if (type == "oakCutting") {
			display3Item(player, 54, 56, 9442, "Oak shortbow", "Oak longbow","Oak Stock");
		} else if (type == "acheyCutting") {
			display1Item(player, 4825, "Unstrung comp bow");
		} else if (type == "willowCutting") {
			display3Item(player, 60, 58, 9444, "Willow shortbow", "Willow longbow","Willow Stock");
		} else if (type == "teakCutting") {
			display1Item(player, 9446, "Teak Stock");
		} else if (type == "mapleCutting") {
			display3Item(player, 64, 62, 9448, "Maple shortbow", "Maple longbow","Maple Stock");
		} else if (type == "mahoganyCutting") {
			display1Item(player, 9450, "Mahogany Stock");
		} else if (type == "yewCutting") {
			display3Item(player, 68, 66, 9452, "Yew shortbow", "Yew longbow", "Yew Stock");
		} else if (type == "magicCutting") {
			display2Item(player, 72, 70, "Magic shortbow", "Magic longbow");
		}

		/* Cooking */
		else if (type == "dairyChurn") {
			display3Item(player, 2130, 6697, 1985, "Pot of cream", "Pat of butter", "Cheese");
		}

		player.setStatedInterface(type);

	}
	public static void display5Item(Player player, int i1, int i2, int i3, int i4, int i5, String s1, String s2, String s3, String s4, String s5) {

		player.getActionSender().sendItemOnInterface(8941, 120, i1);
		player.getActionSender().sendItemOnInterface(8942, 150, i2);
		player.getActionSender().sendItemOnInterface(8943, 150, i3);
		player.getActionSender().sendItemOnInterface(8944, 120, i4);
		player.getActionSender().sendItemOnInterface(8945, 150, i5);
		player.getActionSender().sendString(s1, 8949);
		player.getActionSender().sendString(s2, 8953);
		player.getActionSender().sendString(s3, 8957);
		player.getActionSender().sendString(s4, 8961);
		player.getActionSender().sendString(s5, 8965);
		player.getActionSender().sendChatInterface(8938);
	}
	public static void display4Item(Player player, int i1, int i2, int i3, int i4, String s1, String s2, String s3, String s4) {

		player.getActionSender().sendItemOnInterface(8902, 250, i1);
		player.getActionSender().sendItemOnInterface(8903, 150, i2);
		player.getActionSender().sendItemOnInterface(8904, 200, i3);
		player.getActionSender().sendItemOnInterface(8905, 250, i4);
		player.getActionSender().sendString(s1, 8909);
		player.getActionSender().sendString(s2, 8913);
		player.getActionSender().sendString(s3, 8917);
		player.getActionSender().sendString(s4, 8921);
		player.getActionSender().sendChatInterface(8899);
	}
	public static void display3Item(Player player, int i1, int i2, int i3, String s1, String s2, String s3) {

		player.getActionSender().sendString(s1, 8889);
		player.getActionSender().sendString(s2, 8893);
		player.getActionSender().sendString(s3, 8897);
		player.getActionSender().sendItemOnInterface(8883, 180, i1);
		player.getActionSender().sendItemOnInterface(8884, 180, i2);
		player.getActionSender().sendItemOnInterface(8885, 180, i3);
		player.getActionSender().sendChatInterface(8880);
	}
	public static void display3ItemSized(Player player, int i1, int i2, int i3, String s1, String s2, String s3) {

		player.getActionSender().sendString(s1, 8889);
		player.getActionSender().sendString(s2, 8893);
		player.getActionSender().sendString(s3, 8897);
		player.getActionSender().sendItemOnInterface(8883, 150, i1);
		player.getActionSender().sendItemOnInterface(8884, 100, i2);
		player.getActionSender().sendItemOnInterface(8885, 100, i3);
		player.getActionSender().sendChatInterface(8880);
	}
	public static void display2Item(Player player, int i1, int i2, String s1, String s2) {

		player.getActionSender().sendString(s1, 8874);
		player.getActionSender().sendString(s2, 8878);
		player.getActionSender().sendItemOnInterface(8869, 180, i1);
		player.getActionSender().sendItemOnInterface(8870, 180, i2);
		player.getActionSender().sendChatInterface(8866);
	}
	public static void display1Item(Player player, int i1, String s1) {

		player.getActionSender().sendString(s1, 2799);
		player.getActionSender().sendItemOnInterface(1746, 200, i1);
		player.getActionSender().sendChatInterface(4429);
	}

	public static String determineAorAn(String nextWord) {
		String[] c = {"a", "e", "i", "o", "u", "y"};
		for (String firstLetter : c) {
			if (nextWord.toLowerCase().startsWith(firstLetter)) {
				return "an";
			}
		}
		return "a";
	}
}
