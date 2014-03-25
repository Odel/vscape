package com.rs2.model.objects.functions;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class MilkCow {

	public static void milkCow(final Player player) {
		if (!player.getInventory().playerHasItem(1925, 1)) {
			player.getActionSender().sendMessage("You need a bucket in order to milk this cow.");
			return;
		}
		player.getUpdateFlags().sendAnimation(2305);
		player.getInventory().removeItem(new Item(1925));
		player.getInventory().addItem(new Item(1927));
		player.getActionSender().sendMessage("You milk the cow.");
	}
}
