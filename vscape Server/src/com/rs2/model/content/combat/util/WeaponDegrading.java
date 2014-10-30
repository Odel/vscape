package com.rs2.model.content.combat.util;

import com.rs2.Constants;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.equipment.Equipment;
import com.rs2.model.players.item.Item;
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
	
	public static void handlePlayerHit(final Player player) {
	    Item equipped = player.getEquipment().getItemContainer().get(Constants.WEAPON);
	    Degradeables item = Degradeables.getDegradeableItem(equipped);
	    if (item == null || !Constants.DEGRADING_ENABLED) {
		return;
	    }
	    if (item.getEquipSlot() == Constants.WEAPON) {
		if (equipped.getId() == item.getOriginalId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == 0) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFirstDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getFirstDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSecondDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getSecondDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 2) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getThirdDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getThirdDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 3) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFourthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getFourthDegradeId() && item.getFifthDegradeId() == -1 && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 4) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has broken.");
		    player.setDegradeableHits(item.getPlayerArraySlot(), 0);
		    if (player.getInventory().canAddItem(new Item(item.getBrokenId()))) {
			player.getInventory().addItem(new Item(item.getBrokenId()));
		    } else {
			GroundItem dropItem = new GroundItem(new Item(item.getBrokenId()), player, player.getPosition().clone());
			GroundItemManager.getManager().dropItem(dropItem);
		    }
		    player.getEquipment().unequip(item.getEquipSlot());
		    player.getInventory().removeItem(equipped);
		    player.getEquipment().refresh();
		    player.setAppearanceUpdateRequired(true);
		} else if (equipped.getId() == item.getFourthDegradeId() && item.getFifthDegradeId() != -1 && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 4) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFifthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getFifthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 5) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSixthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getSixthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 6) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSeventhDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getSeventhDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 7) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getEigthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getEigthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 8) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getNinthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getNinthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 9) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		    player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getTenthDegradeId()));
		    player.getEquipment().refresh();
		} else if (equipped.getId() == item.getTenthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 10) {
		    player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has broken.");
		    player.setDegradeableHits(item.getPlayerArraySlot(), 0);
		    if (player.getInventory().canAddItem(new Item(item.getBrokenId()))) {
			player.getInventory().addItem(new Item(item.getBrokenId()));
		    } else {
			GroundItem dropItem = new GroundItem(new Item(item.getBrokenId()), player, player.getPosition().clone());
			GroundItemManager.getManager().dropItem(dropItem);
		    }
		    player.getEquipment().unequip(item.getEquipSlot());
		    player.getInventory().removeItem(equipped);
		    player.getEquipment().refresh();
		    player.setAppearanceUpdateRequired(true);
		} else {
		    player.setDegradeableHits(item.getPlayerArraySlot(), player.getDegradeableHits()[item.getPlayerArraySlot()] + 1);
		}
	    } 
	}
	public static void handleNpcHit(final Player player) {
	    Degradeables item = getRandomEquipped(player);
	    if (item == null || !Constants.DEGRADING_ENABLED) {
		return;
	    }
	    Item equipped = player.getEquipment().getItemContainer().get(item.getEquipSlot());
	    if (equipped.getId() == item.getOriginalId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == 0) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFirstDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getFirstDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getSecondDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getSecondDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 2) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getThirdDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getThirdDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 3) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has degraded.");
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), new Item(item.getFourthDegradeId()));
		player.getEquipment().refresh();
	    } else if (equipped.getId() == item.getFourthDegradeId() && player.getDegradeableHits()[item.getPlayerArraySlot()] == Degradeables.DEGRADE_HITS * 4) {
		player.getActionSender().sendMessage("Your " + equipped.getDefinition().getName().toLowerCase() + " has broken.");
		if (player.getInventory().canAddItem(new Item(item.getBrokenId()))) {
		    player.getInventory().addItem(new Item(item.getBrokenId()));
		} else {
		    GroundItem dropItem = new GroundItem(new Item(item.getBrokenId()), player, player.getPosition().clone());
		    GroundItemManager.getManager().dropItem(dropItem);
		}
		player.getEquipment().getItemContainer().set(item.getEquipSlot(), null);
		player.getEquipment().refresh();
	    } else {
		player.setDegradeableHits(item.getPlayerArraySlot(), player.getDegradeableHits()[item.getPlayerArraySlot()] + 1);
	    }
	}
	private static Degradeables getRandomEquipped(final Player player) {
	    ArrayList<Degradeables> equippedDegradeables = new ArrayList<Degradeables>();
	    for(int x = 0; x < 14; x++) {
		Item equipped = player.getEquipment().getItemContainer().get(x);
		Degradeables item = Degradeables.getDegradeableItem(equipped);
		if(item == null) continue;
		if(item.getEquipSlot() == Constants.WEAPON) continue;
		else {
		    equippedDegradeables.add(item);
		}
	    }
	    if(equippedDegradeables.isEmpty()) {
		return null;
	    }
	    else if(equippedDegradeables.size() == 1) {
		return equippedDegradeables.get(0);
	    }
	    else {
		return equippedDegradeables.get(Misc.randomMinusOne(equippedDegradeables.size()));
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
	    /*
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
		    */
	}
}
