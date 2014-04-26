package com.rs2.cache.region;

import java.util.HashMap;
import java.util.Map;

import com.rs2.model.Position;

/**
 * Manages the world regions.
 * 
 * @author Jinrake
 * 
 */
public class RegionManager {

	/**
	 * The region size.
	 */
	public static final int REGION_SIZE = 32;

	public Regions getRegionByLocation(Position location) {
		return getRegion(location.getX() / REGION_SIZE, location.getY() / REGION_SIZE);
	}

	/**
	 * The active (loaded) region map.
	 */
	private static Map<RegionCoordinates, Regions> activeRegions = new HashMap<RegionCoordinates, Regions>();

	/**
	 * Gets a region by its x and y coordinates.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @return The region.
	 */
	public static Regions getRegion(int x, int y) {
		RegionCoordinates key = new RegionCoordinates(x, y);
		if (activeRegions.containsKey(key)) {
			return activeRegions.get(key);
		} else {
			Regions region = new Regions(key);
			activeRegions.put(key, region);
			return region;
		}
	}

}
