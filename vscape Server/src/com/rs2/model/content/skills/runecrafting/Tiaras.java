package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.content.skills.runecrafting.RunecraftAltars.Altar;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class Tiaras {

	public static boolean bindTiara(Player player, int itemId, int objectId) {
		final Altar altar = RunecraftAltars.getAltarByAltarId(itemId, objectId);
		if (altar == null) {
			return false;
		}
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		if (player.getInventory().playerHasItem(5525)) {
			player.getInventory().removeItem(new Item(5525, 1));
			player.getInventory().addItem(new Item(altar.getTiara(), 1));
			player.getActionSender().sendMessage("You bind the power of the talisman into the tiara.");
		}
		return true;
	}

	public static void handleTiara(Player player, int id) {
		//System.out.println(id);
		int[][] tiaras = {{5527, 1}, {5529, 2}, {5531, 4}, {5535, 8}, {5537, 16}, {5533, 31}, {5539, 64}, {5543, 128}, {5541, 256}, {5545, 512}, {5547, 1024}};
		for (int[] t : tiaras) {
			if (t[0] == id) {
				player.getActionSender().sendConfig(491, t[1]);
				//System.out.println(t[1]);
				return;
			}
		}
		player.getActionSender().sendConfig(491, 0);
	}

}
