package com.rs2.model.objects.functions;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class CoalTruck {

	public static void depositCoal(Player player) {
		int coalAllowed = 120 - player.getCoalTruckAmount();
		if (coalAllowed == 0) {
			player.getActionSender().sendMessage("The coal truck is already full.");
			return;
		}
		int amount = player.getInventory().getItemAmount(453);
		if (amount == 0) {
			return;
		}
		int realAmount = coalAllowed < amount ? coalAllowed : amount;
		if (player.getInventory().removeItem(new Item(453, realAmount))) {
			player.setCoalTruckAmount(player.getCoalTruckAmount() + realAmount);
		}
	}

	public static void withdrawCoal(Player player) {
		if (player.getCoalTruckAmount() == 0) {
			player.getActionSender().sendMessage("There is no coal left in the truck.");
			return;
		}
		int amount = player.getInventory().getItemContainer().freeSlots();
		if (amount == 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return;
		}
		int realAmount = player.getCoalTruckAmount() < amount ? player.getCoalTruckAmount() : amount;
		player.getInventory().addItem(new Item(453, realAmount));
		player.setCoalTruckAmount(player.getCoalTruckAmount() - realAmount);
	}

	public static void checkCoal(Player player) {
		if (player.getCoalTruckAmount() == 0) {
			player.getActionSender().sendMessage("There is no coal left in the truck.");
		} else {
			player.getActionSender().sendMessage("The truck contains "+player.getCoalTruckAmount()+" pieces of coal.");
		}
	}
}
