package com.rs2.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import com.rs2.Server;
import com.rs2.model.players.Player;
import com.rs2.net.packet.PacketManager;

/**
 * A dedicated network reactor thread for low-latency network operations.
 * 
 * @author blakeman8192
 */
public class DedicatedReactor extends Thread {

	/** The singleton instance. */
	private static DedicatedReactor instance;

	/** The selector instance. */
	private final Selector selector;

	/**
	 * Instantiates a new {@code DedicatedReactor}.
	 * 
	 * @param selector
	 *            The selector to perform readiness selection with
	 */
	public DedicatedReactor(Selector selector) {
		this.selector = selector;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("DedicatedReactor");
		while (!Thread.interrupted()) {
			synchronized (DedicatedReactor.getInstance()) {
				// Guard lock - wait here for modifications to complete
			}
			try {
				selector.select();
				for (SelectionKey key : selector.selectedKeys()) {
					if (key.isValid() && key.isAcceptable()) {
						Server.accept(key);
					} else {
						Player player = (Player) key.attachment();
						if (key.isValid() && key.isReadable()) {
							PacketManager.handleIncomingData(player);
						}
						if (key.isValid() && key.isWritable()) {
							PacketManager.flushOutBuffer(player);
						}
					}
				}
				selector.selectedKeys().clear();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Gets the selector.
	 * 
	 * @return The selector
	 */
	public Selector getSelector() {
		return selector;
	}

	/**
	 * Gets the singleton instance.
	 * 
	 * @return The instance
	 */
	public static DedicatedReactor getInstance() {
		return instance;
	}

	/**
	 * Sets the singleton instance.
	 * 
	 * @param instance
	 *            The instance to set
	 * @throws IllegalStateException
	 *             If the instance is not null
	 */
	public static void setInstance(DedicatedReactor instance) {
		if (getInstance() != null) {
			throw new IllegalStateException("Instance already set");
		}
		DedicatedReactor.instance = instance;
	}

}
