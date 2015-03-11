package com.rs2.model.content;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.content.minigames.pestcontrol.*;
import com.rs2.model.content.quests.impl.ChristmasEvent.ChristmasEvent;
import com.rs2.model.content.quests.impl.GhostsAhoy.GhostsAhoy;

/**
 * By Mikey` of Rune-Server
 */

public class WalkInterfaces {

	public static void addWalkableInterfaces(Player player) {
		if (player.inWild())
			player.wildyWarned = true;
		sendGameInterface(player);
		sendMultiInterface(player);
		if (player.inAlchemistPlayground()) {
			player.getAlchemistPlayground().loadInterfacePlayer();
		}
		if (player.inEnchantingChamber()) {
			player.getEnchantingChamber().loadInterfacePlayer();
		}
		if (player.inCreatureGraveyard()) {
			player.getCreatureGraveyard().loadInterfacePlayer();
		}
		if(player.inTelekineticTheatre()) {
			player.getTelekineticTheatre().loadInterfacePlayer();
		}
	}

	//useful stuff - right click options for players such
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
		}else if(player.inPestControlLobbyArea())
		{
			changeWalkableInterface(player, 24126);
			PestControl.lobbyInterface(player);
		} 
		else if(player.inPestControlGameArea())
		{
			changeWalkableInterface(player, 24131);
			PestControl.gameInterface(player);
		} 
		else if(player.inCwLobby())
		{
			changeWalkableInterface(player, 11479);
			Castlewars.lobbyInterface(player);
		}
		else if(player.inCwGame())
		{
			player.getActionSender().sendPlayerOption("Attack", 1, false);
			changeWalkableInterface(player, 11344);
			Castlewars.gameInterface(player);
		}
		else if(player.getPosition().getX() > 3615 && player.getPosition().getX() < 3630
			&& player.getPosition().getY() > 3540 && player.getPosition().getY() < 3546
			&& player.getPosition().getZ() == 2) {
			changeWalkableInterface(player, 12278);
			if(!GhostsAhoy.lowWind) {
			    GhostsAhoy.handleWindSpeed(player);
			}
		}
		else if (player.inWaterbirthIsland() || player.Area(2754, 2814, 3833, 3873)) {
			if (changeWalkableInterface(player, 11877)) {
				player.getActionSender().sendPlayerOption("null", 1, false);
			}
		} 
		else {
			if (changeWalkableInterface(player, -1)) {
				player.getActionSender().sendPlayerOption("null", 1, false);
			}
		}
		
	}

	public static void checkChickenOption(Player player) {
		if (player.getEquipment().getId(Constants.WEAPON) == 4566)
			player.getActionSender().sendPlayerOption("Whack", 5, false);
		else if(player.getEquipment().getId(Constants.WEAPON) == ChristmasEvent.SNOWBALL_ITEM)
			player.getActionSender().sendPlayerOption("Pelt", 5, false);
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
