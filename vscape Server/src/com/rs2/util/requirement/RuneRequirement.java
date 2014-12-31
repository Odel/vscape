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
	private int itemAmount;

	public RuneRequirement(int itemId, int itemAmount) {
		super(itemId, itemAmount);
		this.runeId = itemId;
		this.itemAmount = itemAmount;
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
		if(entity.isPlayer() && swapForComboRunes((Player)entity, runeId) == 0) {
			return super.meetsRequirement(entity);
		} else if (entity.isPlayer() && swapForComboRunes((Player)entity, runeId) != 0) {
			((Player)entity).getInventory().removeItem(new Item(swapForComboRunes((Player)entity, runeId), itemAmount));
			return true;
		}
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
	
	private static int swapForComboRunes(Player player, int runeId) {
	    switch(runeId) {
		default:
		    return 0;
		case 556: //Air rune
		    if(player.getInventory().playerHasItem(4696)) {
			return 4696; //Dust
		    } else if(player.getInventory().playerHasItem(4695)) {
			return 4695; //Mist
		    } else if(player.getInventory().playerHasItem(4697)) {
			return 4697; //Smoke
		    }
		return 0;
		case 555: //Water rune
		    if(player.getInventory().playerHasItem(4695)) {
			return 4695; //Mist
		    } else if(player.getInventory().playerHasItem(4698)) {
			return 4698; //Mud
		    } else if (player.getInventory().playerHasItem(4694)) {
			return 4694; //Steam
		    }
		return 0;
		case 557: //Earth rune
		    if(player.getInventory().playerHasItem(4696)) {
			return 4696; //Dust
		    } else if(player.getInventory().playerHasItem(4698)) {
			return 4698; //Mud
		    } else if(player.getInventory().playerHasItem(4699)) {
			return 4699; //Lava
		    }
		return 0;
		case 554: //Fire rune
		    if(player.getInventory().playerHasItem(4697)) {
			return 4697; //Smoke
		    } else if (player.getInventory().playerHasItem(4694)) {
			return 4694; //Steam
		    } else if(player.getInventory().playerHasItem(4699)) {
			return 4699; //Lava
		    }
		return 0;
	    }
	}

}
