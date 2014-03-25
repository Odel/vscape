package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;

public class Pouches {

	public static final int[][] POUCHES = {{5509, 3, 1}, {5510, 6, 25}, {5512, 9, 50}, {5514, 12, 75}};

	public static boolean fillEssencePouch(Player player, int itemId) {
		int level = player.getSkill().getPlayerLevel(Skill.RUNECRAFTING);
		for (int i = 0; i < POUCHES.length; i++) {
			if (itemId == POUCHES[i][0]) {
				if (!Constants.RUNECRAFTING_ENABLED) {
					player.getActionSender().sendMessage("This skill is currently disabled.");
					return false;
				}
				if (level < POUCHES[i][2]) {
					player.getActionSender().sendMessage("You need " + POUCHES[i][2] + " Runecrafting to use this pouch.");
					return true;
				}
				int amount = player.getInventory().getItemContainer().getCount(7936);
				if (player.getPouchData(i) < POUCHES[i][1]) {
					if (amount > 0) {
						int spaceAvailable = POUCHES[i][1] - player.getPouchData(i);
						int fillAmount = 0;
						for (int i2 = 0; i2 < spaceAvailable; i2++) {
							if (amount > 0 && player.getPouchData(i) <= POUCHES[i][1]) {
								amount--;
								fillAmount++;
								player.setPouchData(i, player.getPouchData(i) + 1);
							}
						}
						player.getInventory().removeItem(new Item(7936, fillAmount));
					} else {
						player.getActionSender().sendMessage("You don't have any Pure essence.");
					}
				} else {
					player.getActionSender().sendMessage("Your " + ItemManager.getInstance().getItemName(itemId) + " is full.");
				}
				return true;
			}
		}
		return false;
	}

	public static void emptyEssencePouch(Player player, int itemId) {
		for (int i = 0; i < POUCHES.length; i++) {
			if (itemId == POUCHES[i][0]) {
				if (!Constants.RUNECRAFTING_ENABLED) {
					player.getActionSender().sendMessage("This skill is currently disabled.");
					return;
				}
				if (player.getPouchData(i) > 0) {
					if (player.getInventory().getItemContainer().freeSlots() >= player.getPouchData(i)) {
						player.getInventory().addItem(new Item(7936, player.getPouchData(i)));
						player.setPouchData(i, 0);
						return;
					} else {
						player.getActionSender().sendMessage("Not enough space in your inventory.");
					}
				} else {
					player.getActionSender().sendMessage("Your " + ItemManager.getInstance().getItemName(itemId) + " is empty.");
					return;
				}
			}
		}
	}

	public static void checkEssencePouch(Player player, int itemId) {
		for (int i = 0; i < POUCHES.length; i++) {
			if (itemId == POUCHES[i][0]) {
				if (!Constants.RUNECRAFTING_ENABLED) {
					player.getActionSender().sendMessage("This skill is currently disabled.");
					return;
				}
				if (player.getPouchData(i) > 0) {
					player.getActionSender().sendMessage("Your " + ItemManager.getInstance().getItemName(itemId) + " contains " + player.getPouchData(i) + " Pure essence.");
					return;
				} else {
					player.getActionSender().sendMessage("Your " + ItemManager.getInstance().getItemName(itemId) + " is empty.");
					return;
				}
			}
		}
	}
}
