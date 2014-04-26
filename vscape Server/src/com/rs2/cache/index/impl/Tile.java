package com.rs2.cache.index.impl;

/**
 * An individual tile on a <code>TileMap</code>. Immutable.
 * 
 * @author Graham Edgecombe
 * 
 */
public class Tile {

	/**
	 * Constant values used by the bitmask.
	 */
	public static final int NORTH_TRAVERSAL_PERMITTED = 1, EAST_TRAVERSAL_PERMITTED = 2, SOUTH_TRAVERSAL_PERMITTED = 4, WEST_TRAVERSAL_PERMITTED = 8;

	/**
	 * A bitmask which determines which directions can be traversed.
	 */
	private final int traversalMask;

	/**
	 * Creates the tile.
	 * 
	 * @param traversalMask
	 *            The traversal bitmask.
	 */
	public Tile(int traversalMask) {
		this.traversalMask = traversalMask;
	}

	/**
	 * Gets the traversal bitmask.
	 * 
	 * @return The traversal bitmask.
	 */
	public int getTraversalMask() {
		return traversalMask;
	}

	/**
	 * Checks if northern traversal is permitted.
	 * 
	 * @return True if so, false if not.
	 */
	public boolean isNorthernTraversalPermitted() {
		return (traversalMask & NORTH_TRAVERSAL_PERMITTED) > 0;
	}

	/**
	 * Checks if eastern traversal is permitted.
	 * 
	 * @return True if so, false if not.
	 */
	public boolean isEasternTraversalPermitted() {
		return (traversalMask & EAST_TRAVERSAL_PERMITTED) > 0;
	}

	/**
	 * Checks if southern traversal is permitted.
	 * 
	 * @return True if so, false if not.
	 */
	public boolean isSouthernTraversalPermitted() {
		return (traversalMask & SOUTH_TRAVERSAL_PERMITTED) > 0;
	}

	/**
	 * Checks if western traversal is permitted.
	 * 
	 * @return True if so, false if not.
	 */
	public boolean isWesternTraversalPermitted() {
		return (traversalMask & WEST_TRAVERSAL_PERMITTED) > 0;
	}

}