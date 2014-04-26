package com.rs2.cache.region;

/**
 * Holds the x and y coordinate for a region.
 * 
 * @author Jinrake
 * 
 */
public class RegionCoordinates {

	/**
	 * X coordinate.
	 */
	private int x;

	/**
	 * Y coordinate.
	 */
	private int y;

	/**
	 * Creates the region coordinate.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public RegionCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x coordinate.
	 * 
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate.
	 * 
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RegionCoordinates other = (RegionCoordinates) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

}