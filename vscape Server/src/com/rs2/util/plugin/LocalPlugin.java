package com.rs2.util.plugin;

import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;

/**
 * Represents a local plugin. Each player in the game server has a unique
 * instance of the running plugin allowing for manipulation of per-player data.
 * 
 * @author Tommo
 * 
 */
public abstract class LocalPlugin extends AbstractPlugin {

	/**
	 * The player this instance of the plugin belongs to.
	 */
	private Player player;

	/**
	 * Creates a new local plugin instance for the specified player.
	 * 
	 * @param player
	 *            The player.
	 */
	public LocalPlugin() {
		super(Type.LOCAL);
		onCreate();
	}

	/**
	 * Called every 600ms.
	 */
	public void onTick() {
	}

	/**
	 * 
	 * @return The player this plugin belongs to.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player this local plugin belongs to.
	 * 
	 * @param player
	 *            The player.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Called when a packet arrives.
	 * 
	 * @param packet
	 *            The received packet.
	 * @return Return TRUE if this packet should be processed elsewhere, return
	 *         FALSE if this packet should not be processed elsewhere.
	 */
	public boolean onPacketArrival(Packet packet) {
		return true;
	}

}
