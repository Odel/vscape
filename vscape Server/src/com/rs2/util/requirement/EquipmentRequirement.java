package com.rs2.util.requirement;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 *
 */
public abstract class EquipmentRequirement extends ExecutableRequirement {

	private int slot, itemId, itemAmount;
	private boolean remove;

	public EquipmentRequirement(int slot, int itemId, int itemAmount, boolean remove) {
		this.slot = slot;
		this.itemId = itemId;
		this.itemAmount = itemAmount;
		this.remove = remove;
	}

	@Override
	public void execute(Entity entity) {
		if (!remove || !entity.isPlayer())
			return;
		Player player = (Player) entity;
		player.getEquipment().removeAmount(slot, itemAmount);
	}

	@Override
	boolean meetsRequirement(Entity entity) {
		if (!entity.isPlayer())
			return true;
		Player player = (Player) entity;
		Item item = player.getEquipment().getItemContainer().getItems()[slot];
		if (item == null)
			return false;
		return item.getId() == itemId && item.getCount() >= itemAmount;
	}

}
