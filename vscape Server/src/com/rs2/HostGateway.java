package com.rs2;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A static gateway type class that is used to limit the maximum amount of
 * connections per host.
 * 
 * @author blakeman8192
 */
public class HostGateway {

	/** The maximum amount of connections per host. */
	public static final int MAX_CONNECTIONS_PER_HOST = 3;
    

	/** Used to keep track of hosts and their amount of connections. */
	private static ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();


	/**
	 * Checks the host into the gateway.
	 * 
	 * @param host
	 *            the host
	 * @return true if the host can connect, false if it has reached the maximum
	 *         amount of connections
	 */
	public static boolean enter(String host) {
        //server being updated
      

		Integer amount = map.putIfAbsent(host, 1);

		// If they've reached the connection limit, return false.
		if (amount != null && amount == MAX_CONNECTIONS_PER_HOST) {
			return false;
		}

        amount = amount == null ? 0 : amount;

		// Otherwise, replace the key with the next value if it was present.
		map.replace(host, amount + 1);
		return true;
	}

	/**
	 * Unchecks the host from the gateway.
	 * 
	 * @param host
	 *            the host
	 */
	public static void exit(String host) {
		Integer amount = map.get(host);
		if (amount == null) {
			return;
		}

		// Remove the host from the map if it's at 1 connection.
		if (amount == 1) {
			map.remove(host);
			return;
		}

		// Otherwise decrement the amount of connections stored.
		if (amount != null) {
			map.replace(host, amount - 1);
		}
	}

}
