package com.rs2.model.transport;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class JewelleryTeleport {

	public enum Jewellery {
		GLORY(new int[]{1712, 1710, 1708, 1706, 1704}),
		COMBATBRACELET(new int[]{11118, 11120, 11122, 11124, 11126}),
		DUELLING(new int[]{2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566, -1}),
		GAMES(new int[]{3853, 3855, 3857, 3859, 3861, 3863, 3865, 3867, -1}),
		SKILLS(new int[]{11105, 11107, 11109, 11111, 11113});

		int[] ids;

		private Jewellery(int[] ids) {
			this.ids = ids;
		}
		
		public int[] getIds() {
			return ids;
		}
	}

	public static void replaceItem(Player player) {
		if(player.getEquipmentOperate()) {
			if(player.getEquipment().getId(player.getSlot()) == player.getClickItem())
			{
				int item = findNextJewellery(player.getClickItem());
				if (item > 0) {
					player.getEquipment().replaceEquipment(item, player.getSlot());
				}else{
					player.getEquipment().removeAmount(player.getSlot(), 1);
				}
			}
		}else{
			if (player.getInventory().removeItem(new Item(player.getClickItem()))) {
				int item = findNextJewellery(player.getClickItem());
				if (item > 0) {
					player.getInventory().addItem(new Item(item));
				}
			}
		}
		player.setEquipmentOperate(false);
	}

	public static void teleport(Player player, Position position) {
		player.getActionSender().removeInterfaces();
		/*if(!player.getEquipment().getItemContainer().contains(player.getClickItem()) &&
		!player.getInventory().playerHasItem(player.getClickItem()))
		{
			return;
		}*/
		if ((player.getEquipmentOperate() && player.getEquipment().getId(player.getSlot()) != player.getClickItem()) || (!player.getEquipmentOperate() && !player.getInventory().playerHasItem(player.getClickItem()))) {
			player.setEquipmentOperate(false);
			return;
		}
		if (!player.getTeleportation().attemptTeleportJewellery(position)) {
			player.setEquipmentOperate(false);
			return;
		}
		replaceItem(player);
	}

	public static int findNextJewellery(int id) {
		boolean getNext = false;
		for (Jewellery jewellery : Jewellery.values()) {
			for (int i : jewellery.getIds()) {
				if (getNext) {
					return i;
				}
				if (id == i) {
					getNext = true;
				}
			}
		}
		return 0;
	}

}
