package com.rs2.cache.map;

import com.rs2.cache.Cache;

/**
 * A class which parses map files in the game cache.
 * 
 * @author Graham Edgecombe
 * 
 */
public class MapParser {

	/**
	 * The cache.
	 */
	@SuppressWarnings("unused")
	private Cache cache;

	/**
	 * The area id.
	 */
	@SuppressWarnings("unused")
	private int area;

	/**
	 * Creates the map parser.
	 * 
	 * @param cache
	 *            The cache.
	 * @param area
	 *            The area id.
	 * @param listener
	 *            The listener.
	 */
	public MapParser(Cache cache, int area) {
		this.cache = cache;
		this.area = area;
	}

	/**
	 * Parses the map file.
	 */
	public void parse() {

	}

}
