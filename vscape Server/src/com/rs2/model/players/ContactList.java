package com.rs2.model.players;

import java.util.LinkedList;
import java.util.List;

import com.rs2.model.World;

/**
 *
 */
public class ContactList {

	private List<Long> friends, ignores;
	private Player player;

	public ContactList(Player player) {
		this.player = player;
		this.friends = new LinkedList<Long>();
		this.ignores = new LinkedList<Long>();
	}

	public void addFriend(long username) {
		if (ignores.contains(username)) {
			player.getActionSender().sendMessage("You must remove this contact from your ignore list.");
			return;
		}
		friends.add(username);
	}

	public void addIgnore(long username) {
		if (friends.contains(username)) {
			player.getActionSender().sendMessage("You must remove this contact from your friend list.");
			return;
		}
		ignores.add(username);
	}

	public void sendPrivateMessage(long toPlayer, int messageSize, byte[] message) {
		Player other = World.getPlayerByName(toPlayer);
		if (other == null) {
			player.getActionSender().sendMessage("An error has occurred.");
			return;
		}
	}

}
