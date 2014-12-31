package com.rs2.util.requirement;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 *
 */
public abstract class RuneRequirement extends InventoryRequirement {

	private int runeId;

	public RuneRequirement(int itemId, int itemAmount) {
		super(itemId, itemAmount);
		this.runeId = itemId;
	}

	@Override
	public void execute(Entity entity) {
		if (!entity.isPlayer() || isUsingStaff(entity, runeId))
			return;
		super.execute(entity);
	}

	@Override
	boolean meetsRequirement(Entity entity) {
		if (!entity.isPlayer() || isUsingStaff(entity, runeId))
			return true;
		return super.meetsRequirement(entity);
	}

	private static boolean isUsingStaff(Entity entity, int runeId) {
		if (!entity.isPlayer())
			return true;
		Player player = (Player) entity;
		Item weapon = player.getEquipment().getItemContainer().get(Constants.WEAPON);
		if (weapon == null)
			return false;
		String weaponName = ItemDefinition.forId(weapon.getId()).getName().toLowerCase();
		if (!weaponName.contains("staff"))
			return false;
		switch (runeId) {
			case 554 :
				return weaponName.contains("fire") || weaponName.contains("lava");
			case 555 :
				return weaponName.contains("water") || weaponName.contains("mud");
			case 556 :
				return weaponName.contains("air");
			case 557 :
				return weaponName.contains("earth") || weaponName.contains("lava") || weaponName.contains("mud");
		}
		return false;
	}

}
