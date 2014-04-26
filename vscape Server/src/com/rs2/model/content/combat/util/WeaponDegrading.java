package com.rs2.model.content.combat.util;

import com.rs2.model.players.Player;
import com.rs2.model.players.container.equipment.Equipment;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 24/03/12 Time: 12:17 To change
 * this template use File | Settings | File Templates.
 */
public class WeaponDegrading {
	public static final String[] barrowsEquipments = new String[]{"ahrims", "dharoks", "torags", "guthans", "karils", "veracs"};
	@SuppressWarnings("unused")
	private static final String[] crystalItems = new String[]{"crystal"};

	public static void degradeEquipment(Player player) {
		for (int i = 0; i < Equipment.SIZE; i++)
			degradeSpecificEquipment(player, i, player.getEquipment().getItemContainer().get(i));
		player.getEquipment().refresh();
	}

	public static void degradeSpecificEquipment(Player player, int slot, Item equipment) {
		if (equipment == null)
			return;
		updateDegradingEquipment(player, slot, equipment);
		if (player.getEquipment().getItemContainer().get(slot).getTimer() >= 0)
			player.getEquipment().getItemContainer().get(slot).setTimer(player.getEquipment().getItemContainer().get(slot).getTimer() - 1);
	}

	public static int getFirstDegraded(int itemId) {
		switch (itemId) {
			case 4708 :
				return 4856;
			case 4710 :
				return 4862;
			case 4712 :
				return 4868;
			case 4714 :
				return 4874;
			case 4716 :
				return 4880;
			case 4718 :
				return 4886;
			case 4720 :
				return 4892;
			case 4722 :
				return 4898;
			case 4724 :
				return 4904;
			case 4726 :
				return 4910;
			case 4728 :
				return 4916;
			case 4730 :
				return 4922;
			case 4732 :
				return 4928;
			case 4734 :
				return 4934;
			case 4736 :
				return 4940;
			case 4738 :
				return 4946;
			case 4745 :
				return 4952;
			case 4747 :
				return 4958;
			case 4749 :
				return 4964;
			case 4751 :
				return 4970;
			case 4753 :
				return 4976;
			case 4755 :
				return 4982;
			case 4757 :
				return 4988;
			case 4759 :
				return 4994;
		}
		return itemId;
	}

	private static void updateDegradingEquipment(Player player, int slot, Item equipment) {
		for (String s : barrowsEquipments) {
			if (equipment.getDefinition().getName().toLowerCase().contains(s) && !(equipment.getId() >= 4856 && equipment.getId() <= 4999)) {
				player.getEquipment().getItemContainer().set(slot, new Item(getFirstDegraded(equipment.getId())));
				player.getEquipment().getItemContainer().get(slot).setTimer(5625);// lasting
																					// 15
																					// hours
			}
		}
		if (equipment.getId() >= 4856 && equipment.getId() <= 4999 && !equipment.getDefinition().getName().toLowerCase().contains(" 0")) {
			if (player.getEquipment().getItemContainer().get(slot).getTimer() == 0) {
				player.getEquipment().getItemContainer().set(slot, new Item(equipment.getId() + 1));
				if (!equipment.getDefinition().getName().toLowerCase().contains(" 25"))
					player.getEquipment().getItemContainer().get(slot).setTimer(5625);
				else
					player.getActionSender().sendMessage("Your barrow equipment has broke.");
			}
		}
		if (equipment.getDefinition().getName().toLowerCase().contains("crystal")) {
			if (equipment.getDefinition().getId() == 4212 || equipment.getDefinition().getId() == 4224) {
				player.getEquipment().getItemContainer().set(slot, new Item(equipment.getDefinition().getId() == 4212 ? 4214 : 4225));
				player.getEquipment().getItemContainer().get(slot).setTimer(250);// lasting
																					// 2500
																					// shots
			} else {
				if (player.getEquipment().getItemContainer().get(slot).getTimer() == 0) {
					if (equipment.getDefinition().getId() == 4223 || equipment.getDefinition().getId() == 4234)
						player.getEquipment().getItemContainer().remove(equipment);

					player.getEquipment().getItemContainer().set(slot, new Item(equipment.getId() + 1));
					player.getEquipment().getItemContainer().get(slot).setTimer(250);
				}
			}
		}
	}
}
