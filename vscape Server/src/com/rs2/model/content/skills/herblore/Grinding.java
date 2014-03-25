package com.rs2.model.content.skills.herblore;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 20/12/11 Time: 22:53 To change
 * this template use File | Settings | File Templates.
 */
public class Grinding {
	private static int PESTLE = 233;

	/**
	 * Grindable Item data.
	 * 
	 * @note orig, ground
	 */
	private static final int[][] GRINDABLES = {{237, 235}, // Unicorn Horn.
			{1973, 1975}, // Chocolate Bar.
			{5075, 6693}, // Bird's Nest.
			{243, 241} // Blue dragon scale
	};

	public static boolean createProduct(Player player, Item useItem, Item withItem, int slot, int usedSlot) {
		try {
			int orig = -1, product = -1;
			int first = useItem.getId(), second = withItem.getId();
			for (int[] data : GRINDABLES) {
				orig = data[0];
				product = data[1];
				if (first == orig && second == PESTLE || first == PESTLE && second == orig) {
                    int origSlot = player.getInventory().getItemContainer().get(slot).getId() == orig ? slot : usedSlot;
					if (!Constants.HERBLORE_ENABLED) {
						player.getActionSender().sendMessage("This skill is currently disabled.");
						return true;
					}
					grindItem(player, orig, product, origSlot);
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static void grindItem(final Player player, final int orig, final int product, final int origSlot) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer p) {
				if (!player.getInventory().removeItemSlot(new Item(orig, 1), origSlot) && !player.getInventory().removeItem(new Item(orig, 1))) {
                    p.stop();
                    return;
                }
				player.getInventory().addItem(new Item(product));
				// player.getActionSender().playSound(275, 1, 0);
				player.getActionSender().sendMessage("You grind the " + new Item(orig).getDefinition().getName() + ".");
				p.stop();
			}

			@Override
			public void stop() {
			}
		}, 2);
	}
}
