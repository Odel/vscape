package com.rs2.model.objects.functions;

import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class AxeInLog {

	public static void pullAxeFromLog(Player player, int x, int y) {
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return;
		}
		player.getInventory().addItem(new Item(1351));
		player.getUpdateFlags().sendAnimation(832);
		player.getActionSender().sendMessage("You take the axe from the log.");
		new GameObject(5582, x, y, player.getPosition().getZ(), 2, 10, 5581, 100);
	}
}
