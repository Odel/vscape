package com.rs2.model.content.minigames.duelarena;

import com.rs2.model.content.WalkInterfaces;
import com.rs2.model.players.Player;
import com.rs2.util.NameUtil;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/22/12 Time: 10:14 PM To change
 * this template use File | Settings | File Templates.
 */
public class PlayerInteraction {

	private Player player;
	private boolean accepted = false;

	public PlayerInteraction(Player player) {
		this.player = player;
	}

	public void requestDuel(Player otherPlayer) {
		if (otherPlayer.getLastPersonChallenged() != player) {
			player.getActionSender().sendMessage("Sending duel request...");
			otherPlayer.getActionSender().sendMessage(NameUtil.formatName(player.getUsername()) + ":duelreq:");
			player.setLastPersonChallenged(otherPlayer);
		} else {
			player.getDuelMainData().setOpponent(otherPlayer);
			otherPlayer.getDuelMainData().setOpponent(player);
			otherPlayer.getDuelInterfaces().openDuelInterface();
			player.getDuelInterfaces().openDuelInterface();
			player.setLastPersonChallenged(null);
			otherPlayer.setLastPersonChallenged(null);
		}
	}

	public void endDuelInteraction(boolean reAdd) {
		if (reAdd) {
			for (int i = 0; i < player.getDuelMainData().getItemStaked().size(); i++)
				player.getInventory().addItem(player.getDuelMainData().getItemStaked().get(i));
		}
		setAccepted(false);
		player.getDuelMainData().clearArrays();
		player.getDuelMainData().setStartedDuel(false);
		player.getDuelMainData().setOpponent(null);
		player.setLastPersonChallenged(null);
		player.getActionSender().removeInterfaces();
		player.getDuelInterfaces().resetDuelRules();
        WalkInterfaces.addWalkableInterfaces(player);
	}

	public void acceptDuel() {
		if (player.getDuelMainData().getOpponent() == null || !player.getDuelMainData().getOpponent().isLoggedIn() || player.getDuelMainData().getOpponent().getDuelInteraction() == null){
			endDuelInteraction(true);
            return;
        }
		setAccepted(true);
		if (player.getStatedInterface() == "duel") {
			if (!player.getDuelInterfaces().canStakeItems()) {
				player.getDuelMainData().getOpponent().getDuelInteraction().setAccepted(false);
				player.getDuelInteraction().setAccepted(false);
				return;
			}
			if (player.getDuelMainData().getOpponent().getDuelInteraction().isAccepted()) {
				player.getDuelInterfaces().openDuelConfirmInterface();
				player.getDuelMainData().getOpponent().getDuelInterfaces().openDuelConfirmInterface();
			}
		} else if (player.getStatedInterface() == "duel2") {
			if (player.getDuelMainData().getOpponent().getDuelInteraction().isAccepted()) {
				player.getDuelMainData().sendIntoDuel();
			}
		}
		player.getDuelMainData().getOpponent().getDuelInterfaces().updateAcceptString();
		player.getDuelInterfaces().updateAcceptString();
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
}
