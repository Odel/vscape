package com.rs2.model.content.skills.herblore;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 20/12/11 Time: 23:07 To change
 * this template use File | Settings | File Templates.
 */
public class Coconut {
	public static boolean handleCoconut(Player player, Item itemUsed, Item usedWith) {
		if ((itemUsed.getId() == 2347 && usedWith.getId() == 5974) || (itemUsed.getId() == 5974 && usedWith.getId() == 2347)) {
			if (!Constants.HERBLORE_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			player.getActionSender().sendMessage("You crush the coconut with a hammer.");
			player.getInventory().removeItem(new Item(5974));
			player.getInventory().addItem(new Item(5976));
			return true;
		}
		if ((itemUsed.getId() == 229 && usedWith.getId() == 5976) || (itemUsed.getId() == 5976 && usedWith.getId() == 229)) {
			if (!Constants.HERBLORE_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			player.getInventory().removeItem(new Item(229));
			player.getInventory().addItem(new Item(5935));
			player.getInventory().removeItem(new Item(5976));
			player.getInventory().addItem(new Item(5978));
			player.getActionSender().sendMessage("You overturn the coconut into a vial.");
			return true;
		}
		return false;
	}
}
