package com.rs2.model.objects.functions;

import com.rs2.Constants;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class CrystalChest {

	public static void openCrystalChest(Player player) {
		if (!player.getInventory().removeItem(new Item(989))) {
			return;
		}
		player.getUpdateFlags().sendAnimation(832);
		player.getActionSender().sendMessage("You unlock the chest with your key.");
		new GameObject(173, 2914, 3452, 0, 2, 10, 172, 2);
		player.getInventory().addItemOrDrop(new Item(1631));
		int chance = Misc.random(10000);
		if (chance < 3970) {
			player.getInventory().addItemOrDrop(new Item(995, 2000));
			player.getInventory().addItemOrDrop(new Item(1969));
		} else if (chance < 4743) {
			player.getInventory().addItemOrDrop(new Item(554, 50));
			player.getInventory().addItemOrDrop(new Item(555, 50));
			player.getInventory().addItemOrDrop(new Item(556, 50));
			player.getInventory().addItemOrDrop(new Item(557, 50));
			player.getInventory().addItemOrDrop(new Item(558, 50));
			player.getInventory().addItemOrDrop(new Item(559, 50));
			player.getInventory().addItemOrDrop(new Item(560, 10));
			player.getInventory().addItemOrDrop(new Item(561, 10));
			player.getInventory().addItemOrDrop(new Item(562, 10));
			player.getInventory().addItemOrDrop(new Item(563, 10));
			player.getInventory().addItemOrDrop(new Item(564, 10));
		} else if (chance < 5094) {
			player.getInventory().addItemOrDrop(new Item(2363, 3));
		} else if (chance < 5749) {
			player.getInventory().addItemOrDrop(new Item(454, 100));
		} else if (chance < 6024) {
			player.getInventory().addItemOrDrop(new Item(441, 150));
		} else if (chance < 6447) {
			player.getInventory().addItemOrDrop(new Item(1617, 2));
			player.getInventory().addItemOrDrop(new Item(1619, 2));
		} else if (chance < 7504) {
			player.getInventory().addItemOrDrop(new Item(995, 1000));
			player.getInventory().addItemOrDrop(new Item(371, 5));
		} else if (chance < 7830) {
			player.getInventory().addItemOrDrop(new Item(995, 750));
			player.getInventory().addItemOrDrop(new Item(Misc.random(1) == 0 ? 985 : 987));
		} else if (chance < 7936) {
			player.getInventory().addItemOrDrop(new Item(1183));
		} else if (chance < 7962) {
			player.getInventory().addItemOrDrop(new Item(player.getGender() == Constants.GENDER_MALE ? 1079 : 1093));
		}
	}
}
