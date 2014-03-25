package com.rs2.model.content;

import com.rs2.model.players.Player;

/**
 * By Mikey` of Rune-Server
 */
public class Emotes {

	private Player player;

	public Emotes(Player player) {
		this.player = player;
	}

	private static final int[][] EMOTES = {{168, 855}, {169, 856}, {162, 857}, {164, 858}, {165, 859}, {161, 860}, {170, 861}, {171, 862}, {163, 863}, {167, 864}, {172, 865}, {166, 866}, {52050, 2105}, {52051, 2106}, {52052, 2107}, {52053, 2108}, {52054, 2109}, {52055, 2110}, {52056, 2111}, {52057, 2112}, {52058, 2113}, {43092, 0x558}, {2155, 1131}, {25103, 1130}, {25106, 1129}, {2154, 1128}, {52071, 2127}, {52072, 2128}, {59062, 2836}, {72032, 3544}, {72033, 3543}};

	public void performEmote(int emoteId) {
		player.getUpdateFlags().sendAnimation(emoteId, 0);
	}

	public boolean activateEmoteButton(int buttonId) {
		for (int[] element : EMOTES) {
			if (buttonId == element[0]) {
				performEmote(element[1]);
				return true;
			}
		}
		return false;
	}

}
