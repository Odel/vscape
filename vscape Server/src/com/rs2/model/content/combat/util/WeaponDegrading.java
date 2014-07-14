package com.rs2.model.content.combat.util;

import com.rs2.Constants;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.equipment.Equipment;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.util.Misc;
import java.util.ArrayList;

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
		if (player.getEquipment().getItemContainer().get(slot).getTimer() >= 0) {
		}	 //if time to degrade
	}
	public static int getBarrowsPlayerArraySlot(Item item) {
	    String itemName = ItemDefinition.forId(item.getId()).getName().toLowerCase();
	    if (itemName.contains("ahrim")) {
		if (itemName.contains("staff")) return 3;
		if (itemName.contains("skirt")) return 2;
		if (itemName.contains("top")) return 1;
		if (itemName.contains("hood")) return 0;

	    }
	    if (itemName.contains("guthan")) {
		if (itemName.contains("spear")) return 15;
		if (itemName.contains("skirt")) return 14;
		if (itemName.contains("body")) return 13;
		if (itemName.contains("helm")) return 12;
	    }
	    if (itemName.contains("torag")) {
		if (itemName.contains("hammers")) return 11;
		if (itemName.contains("legs")) return 10;
		if (itemName.contains("body")) return 9;
		if (itemName.contains("helm")) return 8;
	    }
	    if (itemName.contains("dharok")) {
		if (itemName.contains("axe")) return 7;
		if (itemName.contains("legs")) return 6;
		if (itemName.contains("body")) return 5;
		if (itemName.contains("helm")) return 4;
	    }
	    if (itemName.contains("verac")) {
		if (itemName.contains("flail")) return 23;
		if (itemName.contains("skirt")) return 22;
		if (itemName.contains("top") || itemName.contains("brassard")) return 21;
		if (itemName.contains("helm")) return 20;
	    }
	    if (itemName.contains("karil")) {
		if (itemName.contains("bow")) return 19;
		if (itemName.contains("skirt")) return 18;
		if (itemName.contains("top")) return 17;
		if (itemName.contains("coif")) return 16;
	    }
	    return -1;
	}
	public static int getBarrowsEquipSlot(Item item) {
	    String itemName = ItemDefinition.forId(item.getId()).getName().toLowerCase();
	    if (itemName.contains("ahrim")) {
		if (itemName.contains("staff")) return Constants.WEAPON;
		if (itemName.contains("skirt")) return Constants.LEGS;
		if (itemName.contains("top")) return Constants.CHEST;
		if (itemName.contains("hood")) return Constants.HAT;

	    }
	    if (itemName.contains("guthan")) {
		if (itemName.contains("spear")) return Constants.WEAPON;
		if (itemName.contains("skirt")) return Constants.LEGS;
		if (itemName.contains("body")) return Constants.CHEST;
		if (itemName.contains("helm")) return Constants.HAT;
	    }
	    if (itemName.contains("torag")) {
		if (itemName.contains("hammers")) return Constants.WEAPON;
		if (itemName.contains("legs")) return Constants.LEGS;
		if (itemName.contains("body")) return Constants.CHEST;
		if (itemName.contains("helm")) return Constants.HAT;
	    }
	    if (itemName.contains("dharok")) {
		if (itemName.contains("axe")) return Constants.WEAPON;
		if (itemName.contains("legs")) return Constants.LEGS;
		if (itemName.contains("body")) return Constants.CHEST;
		if (itemName.contains("helm")) return Constants.HAT;
	    }
	    if (itemName.contains("verac")) {
		if (itemName.contains("flail")) return Constants.WEAPON;
		if (itemName.contains("skirt")) return Constants.LEGS;
		if (itemName.contains("top") || itemName.contains("brassard")) return Constants.CHEST;
		if (itemName.contains("helm")) return Constants.HAT;
	    }
	    if (itemName.contains("karil")) {
		if (itemName.contains("bow")) return Constants.WEAPON;
		if (itemName.contains("skirt")) return Constants.LEGS;
		if (itemName.contains("top")) return Constants.CHEST;
		if (itemName.contains("coif")) return Constants.HAT;
	    }
	    return -1;
	}
	
	public static void handlePlayerHit(final Player player) {
	    Item equipped = player.getEquipment().getItemContainer().get(Constants.WEAPON);
	    BarrowsItems item = BarrowsItems.getBarrowsItem(equipped);
	    if (item == null) {
		return;
	    }
	    if (item.getEquipSlot() == Constants.WEAPON) {
		if (equipped.getId() == item.getOriginalId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 0) {
		    player.getActionSender().sendMessage("Your barrows equipment has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFirstDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getFirstDegradeId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 250) {
		    player.getActionSender().sendMessage("Your barrows equipment has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSecondDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getSecondDegradeId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 500) {
		    player.getActionSender().sendMessage("Your barrows equipment has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getThirdDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getThirdDegradeId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 750) {
		    player.getActionSender().sendMessage("Your barrows equipment has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFourthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getFourthDegradeId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 1000) {
		    player.getActionSender().sendMessage("Your barrows equipment has broken.");
		    if (player.getInventory().canAddItem(new Item(item.getBrokenId()))) {
			player.getInventory().addItem(new Item(item.getBrokenId()));
		    } else {
			GroundItem dropItem = new GroundItem(new Item(item.getBrokenId()), player, player.getPosition().clone());
			GroundItemManager.getManager().dropItem(dropItem);
		    }
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), null);
		    player.getEquipment().refresh();
		} else {
		    player.setBarrowsHits(item.getPlayerArraySlot(), player.getBarrowsHits()[item.getPlayerArraySlot()] + 1);
		}
	    } 
	}
	public static void handleNpcHit(final Player player) {
	    BarrowsItems item = getRandomEquipped(player);
	    if (item == null) {
		return;
	    }
	    Item equipped = player.getEquipment().getItemContainer().get(item.getEquipSlot());
	    if (equipped.getId() == item.getOriginalId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 0) {
		player.getActionSender().sendMessage("Your barrows equipment has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFirstDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getFirstDegradeId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 250) {
		player.getActionSender().sendMessage("Your barrows equipment has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSecondDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getSecondDegradeId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 500) {
		player.getActionSender().sendMessage("Your barrows equipment has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getThirdDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getThirdDegradeId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 750) {
		player.getActionSender().sendMessage("Your barrows equipment has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFourthDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getFourthDegradeId() && player.getBarrowsHits()[item.getPlayerArraySlot()] == 1000) {
		player.getActionSender().sendMessage("Your barrows equipment has broken.");
		if (player.getInventory().canAddItem(new Item(item.getBrokenId()))) {
		    player.getInventory().addItem(new Item(item.getBrokenId()));
		} else {
		    GroundItem dropItem = new GroundItem(new Item(item.getBrokenId()), player, player.getPosition().clone());
		    GroundItemManager.getManager().dropItem(dropItem);
		}
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), null);
		player.getEquipment().refresh();
	    } else {
		player.setBarrowsHits(item.getPlayerArraySlot(), player.getBarrowsHits()[item.getPlayerArraySlot()] + 1);
	    }
	}
	private static BarrowsItems getRandomEquipped(final Player player) {
	    ArrayList<BarrowsItems> equippedBarrows = new ArrayList<BarrowsItems>();
	    for(int x = 0; x < 14; x++) {
		Item equipped = player.getEquipment().getItemContainer().get(x);
		BarrowsItems item = BarrowsItems.getBarrowsItem(equipped);
		if(item == null) continue;
		if(item.getEquipSlot() == Constants.WEAPON) continue;
		else {
		    equippedBarrows.add(item);
		}
	    }
	    if(equippedBarrows.isEmpty()) {
		return null;
	    }
	    else if(equippedBarrows.size() == 1) {
		return equippedBarrows.get(0);
	    }
	    else {
		return equippedBarrows.get(Misc.random(equippedBarrows.size()));
	    }
	}
	private static void updateDegradingEquipment(Player player, int slot, Item equipment) {
		/*for (String s : barrowsEquipments) {
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
					player.getActionSender().sendMessage("Your barrows equipment has broken.");
			}
		} */
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
