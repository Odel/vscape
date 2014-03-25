package com.rs2.model.content.skills.cooking;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 21:41 To change
 * this template use File | Settings | File Templates.
 */
public class FlourRelated {
	public static final int POT_OF_FLOUR = 1933;
	public static final int BUCKET_OF_WATER = 1929;
	public static final int BUCKET = 1925;

	public static boolean handleButton(Player player, int buttonId) {
			switch (buttonId) {
			case 53202:
				player.getInventory().removeItem(new Item(BUCKET_OF_WATER));
				player.getInventory().removeItem(new Item(POT_OF_FLOUR));
				player.getInventory().addItem(new Item(BUCKET));
				player.getActionSender().sendMessage("You put the water on the flour and make it into a bread dough");
				player.getInventory().addItem(new Item(2307));
				player.getActionSender().removeInterfaces();
				return true;
			case 53203:
				if (!player.getInventory().getItemContainer().contains(BUCKET_OF_WATER) || !player.getInventory().getItemContainer().contains(POT_OF_FLOUR))
					return true;
				player.getInventory().removeItem(new Item(BUCKET_OF_WATER));
				player.getInventory().removeItem(new Item(POT_OF_FLOUR));
				player.getInventory().addItem(new Item(BUCKET));
				player.getActionSender().sendMessage("You put the water on the flour and make it into a pastry dough");
				player.getInventory().addItem(new Item(1953));
				player.getActionSender().removeInterfaces();
				return true;
			case 53204:
				if (!player.getInventory().getItemContainer().contains(BUCKET_OF_WATER) || !player.getInventory().getItemContainer().contains(POT_OF_FLOUR))
					return true;
				player.getInventory().removeItem(new Item(BUCKET_OF_WATER));
				player.getInventory().removeItem(new Item(POT_OF_FLOUR));
				player.getInventory().addItem(new Item(BUCKET));
				player.getActionSender().sendMessage("You put the water on the flour and make it into a pizza base");
				player.getInventory().addItem(new Item(2283));
				return true;
			}
		return false;
	}
}
