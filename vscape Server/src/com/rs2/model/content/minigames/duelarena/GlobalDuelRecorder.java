package com.rs2.model.content.minigames.duelarena;

import java.util.ArrayList;

import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/24/12 Time: 3:05 PM To change
 * this template use File | Settings | File Templates.
 */
public class GlobalDuelRecorder {

	public static ArrayList<String> duelList = new ArrayList<String>();

	public static void addDuelToList(Player winner, Player loser) {
		if (duelList.size() >= 50)
			duelList.remove(0);
		duelList.add(winner.getUsername() + " (" + winner.getCombatLevel() + ") beat " + loser.getUsername() + " (" + loser.getCombatLevel() + ")");
	}

	public static void displayScoreBoardInterface(Player player) {
		cleanUpScoreboard(player);
		if (duelList.size() == 0) {
			player.getActionSender().sendString("No duel have been started yet.", 6402);
		} else {
			for (int i = 6402; i < 6412; i++)
				if (duelList.size() - 1 - (i - 6402) >= 0)
					player.getActionSender().sendString(duelList.get(duelList.size() - 1 - i), i);
			for (int i = 8578; i < 8618; i++)
				if (duelList.size() - 10 - (i - 8578) >= 0)
					player.getActionSender().sendString(duelList.get(duelList.size() - 10 - (i - 8578)), i);
		}
		player.getActionSender().sendInterface(DuelMainData.DUEL_ARENA_SCOREBOARD);
	}

	public static void cleanUpScoreboard(Player player) {
		for (int i = 6402; i < 6412; i++)
			player.getActionSender().sendString("", i);
		for (int i = 8578; i < 8618; i++)
			player.getActionSender().sendString("", i);
	}
}
