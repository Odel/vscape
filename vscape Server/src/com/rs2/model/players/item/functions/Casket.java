package com.rs2.model.players.item.functions;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class Casket {

	public static final int[] COMMON = {995, 1623, 1454};
	public static final int[] UNCOMMON = {1621, 1619, 1452};
	public static final int[] RARE = {1617, 985, 987, 1462};

	public static void openCasket(Player player) {
		int reward = 0;
		if (Misc.random(999) == 0) {
			reward = RARE[Misc.randomMinusOne(RARE.length)];
		} else if (Misc.random(99) == 0) {
			reward = UNCOMMON[Misc.randomMinusOne(UNCOMMON.length)];
		} else {
			reward = COMMON[Misc.randomMinusOne(COMMON.length)];
		}
		player.getInventory().addItem(new Item(reward, reward == 995 ? (Misc.random(2980) + 20) : 1));
		player.getActionSender().sendMessage("You open the casket and find some treasure inside.");
	}
}
