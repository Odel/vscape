package com.rs2.model.content.minigames.castlewars;

import java.util.ArrayList;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/26/12 Time: 2:51 PM To change
 * this template use File | Settings | File Templates.
 */
public class GlobalGameHandling {

	public static boolean isInZammyLobby(Player player) {
		return MinigameAreas.isInArea(player.getPosition(), new MinigameAreas.Area(new Position(2410, 9510, 0), new Position(2427, 9533, 0)));
	}

	public static boolean isInSaradominLobby(Player player) {
		return MinigameAreas.isInArea(player.getPosition(), new MinigameAreas.Area(new Position(2362, 9473, 0), new Position(2392, 9499, 0)));
	}

	public static ArrayList<Player> getZammyLobbyPlayers() {
		ArrayList<Player> array = new ArrayList<Player>();
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (isInZammyLobby(player))
				array.add(player);
		}
		return array;
	}

	public static ArrayList<Player> getSaradominLobbyPlayers() {
		ArrayList<Player> array = new ArrayList<Player>();
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (isInSaradominLobby(player))
				array.add(player);
		}
		return array;
	}

	public static void sendAllPlayersToGame() {
		ArrayList<Player> list = getSaradominLobbyPlayers();
		ArrayList<Player> list2 = getZammyLobbyPlayers();
		for (int i = 0; i < list.size(); i++)
			list.get(i).teleport(MinigameAreas.randomPosition(new MinigameAreas.Area(new Position(2423, 3072, 1), new Position(2431, 3080, 1))));

		for (int i = 0; i < list2.size(); i++)
			list2.get(i).teleport(MinigameAreas.randomPosition(new MinigameAreas.Area(new Position(2368, 3127, 1), new Position(2376, 3135, 1))));
	}

	public static void sendPlayerToLobby(Player player, String team) {
		if (team == "zamorak")
			player.teleport(MinigameAreas.randomPosition(new MinigameAreas.Area(new Position(2417, 9517, 0), new Position(2428, 9531, 0))));
		else
			player.teleport(MinigameAreas.randomPosition(new MinigameAreas.Area(new Position(2372, 9483, 0), new Position(2388, 9492, 0))));
	}

	public static boolean inCwArea(Player player) {
		return (MinigameAreas.isInArea(player.getPosition(), new MinigameAreas.Area(new Position(2368, 9479, 0), new Position(2431, 9535, 0))) || MinigameAreas.isInArea(player.getPosition(), new MinigameAreas.Area(new Position(2368, 3072, 0), new Position(2431, 3135, 0)))) && !isInSaradominLobby(player) && !isInZammyLobby(player);
	}

	public static boolean inCwSafe(Player player, String team) {
		if (team == "saradomin")
			return MinigameAreas.isInArea(player.getPosition(), new MinigameAreas.Area(new Position(2423, 3072, 0), new Position(2431, 3080, 0)));
		else
			return MinigameAreas.isInArea(player.getPosition(), new MinigameAreas.Area(new Position(2368, 3127, 0), new Position(2376, 3135, 0)));
	}

	public static String getTeam(Player player) {
		return null;

	}

}
