package com.rs2.util.requirement;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 *
 */
public abstract class InventoryRequirement extends ExecutableRequirement {

	private int itemId, itemAmount;

	public InventoryRequirement(int itemId, int itemAmount) {
		this.itemId = itemId;
		this.itemAmount = itemAmount;
	}

	@Override
	public void execute(Entity entity) {
		if (!entity.isPlayer())
			return;
		Player player = (Player) entity;
		player.getInventory().removeItem(new Item(itemId, itemAmount));
	}

	@Override
	boolean meetsRequirement(Entity entity) {
		if (!entity.isPlayer())
			return true;
		Player player = (Player) entity;
		return player.getInventory().getItemAmount(itemId) >= itemAmount;
	}
}
