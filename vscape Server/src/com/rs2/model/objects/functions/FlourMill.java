package com.rs2.model.objects.functions;

import com.rs2.GlobalVariables;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class FlourMill {

	public static void putFlourInHopper(Player player) {
		int amount = GlobalVariables.getGrainBin() + GlobalVariables.getGrainHopper();
		if (amount > 30) {
			player.getActionSender().sendMessage("The hopper or bin is already full.");
			return;
		}
		if (!player.getInventory().playerHasItem(1947)) {
			return;
		}
		player.getInventory().removeItem(new Item(1947, 1));
		player.getUpdateFlags().sendAnimation(832);
		GlobalVariables.setGrainHopper(GlobalVariables.getGrainHopper() + 1);
		player.getActionSender().sendMessage("You place the grain into the hopper.");
		if (GlobalVariables.getGrainHopper() < 30) {
			player.getActionSender().sendMessage("The hopper is now holding " + GlobalVariables.getGrainHopper() + " pieces of grain.");
		} else {
			player.getActionSender().sendMessage("The hopper is now full with 30 pieces of grain.");
		}
	}

	public static void operateHopper(Player player) {
		if (GlobalVariables.getGrainHopper() < 1) {
			player.getActionSender().sendMessage("There is no grain in the hopper.");
			return;
		}
		if (GlobalVariables.getGrainBin() > 29) {
			player.getActionSender().sendMessage("The grain bin is already full.");
			return;
		}
		int amount = GlobalVariables.getGrainBin() + GlobalVariables.getGrainHopper();
		if (amount > 30) {
			amount = 30 - GlobalVariables.getGrainBin();
		}
		player.getUpdateFlags().sendAnimation(832);
		GlobalVariables.setGrainBin(amount);
		GlobalVariables.setGrainHopper(0);
		player.getActionSender().sendMessage("The grain in the hopper slides down the chute.");
		if (ObjectHandler.getInstance().getObject(1782, 3166, 3306, 0) == null) {
			new GameObject(1782, 3166, 3306, 0, 0, 10, 1781, 999999);
		}
	}

	public static void takeFromBin(Player player) {
		if (GlobalVariables.getGrainBin() < 1) {
			player.getActionSender().sendMessage("There is no grain in the bin.");
			return;
		}
		if (!player.getInventory().playerHasItem(1931)) {
			player.getActionSender().sendMessage("You need an empty pot in order to take flour from here.");
			return;
		}
		player.getUpdateFlags().sendAnimation(832);
		player.getInventory().removeItem(new Item(1931, 1));
		player.getInventory().addItem(new Item(1933, 1));
		GlobalVariables.setGrainBin(GlobalVariables.getGrainBin() - 1);
		player.getActionSender().sendMessage("You take some flour from the bin.");
		if (GlobalVariables.getGrainBin() > 0) {
			player.getActionSender().sendMessage("There is enough grain left in the bin to fill " + GlobalVariables.getGrainBin() + " pot" + (GlobalVariables.getGrainBin() > 1 ? "s" : "") + ".");
		} else {
			player.getActionSender().sendMessage("The grain bin is now empty.");
			if (ObjectHandler.getInstance().getObject(1782, 3166, 3306, 0) != null) {
				ObjectHandler.getInstance().removeObject(3166, 3306, 0, 10);
			}
		}
	}
}
