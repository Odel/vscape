package com.rs2.model.content.skills.farming;

import com.rs2.model.Position;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class MithrilSeeds {

	public static final int[] flowerObjects = {2980, 2981, 2982, 2983, 2984, 2985, 2986, 2987, 2988};
	public static final int[] flowerItems = {2460, 2462, 2464, 2466, 2468, 2470, 2472, 2474, 2476};

	public static void plantMithrilSeed(Player player) {
		int flower = flowerObjects[Misc.randomMinusOne(flowerObjects.length)];
		int x = player.getPosition().getX();
		int y = player.getPosition().getY();
		GameObject p = ObjectHandler.getInstance().getObject(x, y, player.getPosition().getZ());
		if (p != null) {
			player.getActionSender().sendMessage("You can't plant a flower here.");
			return;
		}
		if (player.getInventory().getItemContainer().contains(299)) {
			player.getInventory().removeItem(new Item(299));
		} else {
			return;
		}
		player.getUpdateFlags().sendAnimation(827);
		new GameObject(flower, x, y, player.getPosition().getZ(), 0, 10, -1, 500);
		if (player.canMove(-1, 0)) {
			player.getActionSender().walkTo(-1, 0, false);
		} else {
			player.getActionSender().walkTo(1, 0, false);
        }
		player.getUpdateFlags().sendFaceToDirection(new Position(x, y));
	}
}
