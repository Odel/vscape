package com.rs2.model.content;

import com.rs2.Constants;
import com.rs2.model.players.Player;

/**
 * By Mikey` of Rune-Server
 */

public class WalkInterfaces {

	public static void addWalkableInterfaces(Player player) {
		if (player.inWild())
			player.wildyWarned = true;
		sendGameInterface(player);
		sendMultiInterface(player);
		if (player.getAlchemistPlayground().isInAlchemistPlayGround())
			player.getAlchemistPlayground().loadInterfacePlayer();
		if (player.getEnchantingChamber().isInEnchantingChamber())
			player.getEnchantingChamber().loadInterfacePlayer();
		if (player.getCreatureGraveyard().isInCreatureGraveyard()) {
			player.getCreatureGraveyard().loadInterfacePlayer();
		}
	}

	public static void sendGameInterface(Player player) {
		if (player.inWild()) {
			if (changeWalkableInterface(player, 197)) {
				player.getActionSender().sendPlayerOption("Attack", 1, false);
			}
			player.getActionSender().sendString("@yel@Level: " + player.getWildernessLevel(), 199);
		} else if (player.inDuelArena()) {
			player.getActionSender().sendPlayerOption("Attack", 1, false);
			changeWalkableInterface(player, 201);
		} else if (player.isInDuelArea()) {
			player.getActionSender().sendPlayerOption("Challenge", 1, false);
			changeWalkableInterface(player, 201);
		} else if (player.inBarrows()) {
			player.getActionSender().sendString("Kill count: " + player.getKillCount(), 4536);
			if (changeWalkableInterface(player, 4535)) {
				player.getActionSender().sendPlayerOption("null", 1, false);
			}
		} else if (player.inWaterbirthIsland()) {
			if (changeWalkableInterface(player, 11877)) {
				player.getActionSender().sendPlayerOption("null", 1, false);
			}
		} else {
			if (changeWalkableInterface(player, -1)) {
				player.getActionSender().sendPlayerOption("null", 1, false);
			}
		}
	}

	public static void checkChickenOption(Player player) {
		if (player.getEquipment().getId(Constants.WEAPON) == 4566)
			player.getActionSender().sendPlayerOption("Whack", 5, false);
		else
			player.getActionSender().sendPlayerOption("null", 5, false);
	}

	public static void sendMultiInterface(Player player) {
		if (player.inMulti()) {
			player.getActionSender().sendMultiInterface(true);
		} else {
			player.getActionSender().sendMultiInterface(false);
		}
	}

	public static boolean changeWalkableInterface(Player player, int id) {
		if (player.getCurrentWalkableInterface() == id) {
			return false;
		} else {
			player.setCurrentWalkableInterface(id);
			player.getActionSender().sendWalkableInterface(id);
			return true;
		}
	}
}
