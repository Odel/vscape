package com.rs2.model.region;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.rs2.cache.object.CacheObject;

/**
 * Represents a single region.
 * 
 * @author Jinrake
 * 
 */
public class Regions {

	/**
	 * The region coordinates.
	 */
	private RegionCoordinates coordinate;

	/**
	 * Creates a region.
	 * 
	 * @param coordinate
	 *            The coordinate.
	 */
	public Regions(RegionCoordinates coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * Gets the region coordinates.
	 * 
	 * @return The region coordinates.
	 */
	public RegionCoordinates getCoordinates() {
		return coordinate;
	}

	/**
	 * A list of objects in this region.
	 */
	private List<CacheObject> objects = new LinkedList<CacheObject>();

	/**
	 * Gets the list of objects.
	 * 
	 * @return The list of objects.
	 */
	public Collection<CacheObject> getGameObjects() {
		return objects;
	}

}
