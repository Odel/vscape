package com.rs2.model.transport;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class JewelleryTeleport {

	public enum Jewellery {
		GLORY(new int[]{1712, 1710, 1708, 1706, 1704}),
		DUELLING(new int[]{2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566, -1}),
		GAMES(new int[]{3853, 3855, 3857, 3859, 3861, 3863, 3865, 3867, -1});

		int[] ids;

		private Jewellery(int[] ids) {
			this.ids = ids;
		}
		
		public int[] getIds() {
			return ids;
		}
	}

	public static void replaceItem(Player player) {
		if (player.getInventory().removeItem(new Item(player.getClickItem()))) {
			int item = findNextJewellery(player.getClickItem());
			if (item > 0) {
				player.getInventory().addItem(new Item(item));
			}
		}
	}

	public static void teleport(Player player, Position position) {
		player.getActionSender().removeInterfaces();
		if (!player.getInventory().playerHasItem(player.getClickItem())) {
			return;
		}
		if (!player.getTeleportation().attemptTeleportJewellery(position)) {
			return;
		}
		replaceItem(player);
	}

	public static int findNextJewellery(int id) {
		boolean getNext = false;
		for (Jewellery jewellery : Jewellery.values()) {
			for (int i : jewellery.getIds()) {
				if (getNext) {
					return i;
				}
				if (id == i) {
					getNext = true;
				}
			}
		}
		return 0;
	}

}
