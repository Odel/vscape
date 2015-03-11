package com.rs2.model;

import com.rs2.util.Misc;

/**
 * Represents the position of a player or NPC.
 * 
 * @author blakeman8192
 */
public class Position {

	private int x;
	private int y;
	private int z;
	private int lastX;
	private int lastY;
	private int lastZ;
	
	public Position() {
	    this.x = 0;
	    this.y = 0;
	    this.z = 0;
	    this.lastX = 0;
	    this.lastY = 0;
	    this.lastZ = 0;
	}
	/**
	 * Creates a new Position with the specified coordinates. The Z coordinate
	 * is set to 0.
	 * 
	 * @param x
	 *            the X coordinate
	 * @param y
	 *            the Y coordinate
	 */
	public Position(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * Creates a new Position with the specified coordinates.
	 * 
	 * @param x
	 *            the X coordinate
	 * @param y
	 *            the Y coordinate
	 * @param z
	 *            the Z coordinate
	 */
	public Position(int x, int y, int z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	public Position(Position copy) {
		this.setX(copy.getX());
		this.setY(copy.getY());
		this.setZ(copy.getZ());
	}

	@Override
	public String toString() {
		return "Position(" + x + ", " + y + ", " + z + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Position) {
			Position p = (Position) other;
			return x == p.x && y == p.y && z == p.z;
		}
		return false;
	}

	/**
	 * Sets this position as the other position. <b>Please use this method
	 * instead of player.setPosition(other)</b> because of reference conflicts
	 * (if the other position gets modified, so will the players).
	 * 
	 * @param other
	 *            the other position
	 */
	public void setAs(Position other) {
		this.x = other.x;
		this.y = other.y;
		this.lastX = other.lastX;
		this.lastY = other.lastY;
		this.z = other.z;
		this.lastZ = other.z;
	}

	/**
	 * Moves the position.
	 * 
	 * @param amountX
	 *            the amount of X coordinates
	 * @param amountY
	 *            the amount of Y coordinates
	 */
	public void move(int amountX, int amountY) {
	//	setLastX(getX());
	//	setLastY(getY());
		setX(getX() + amountX);
		setY(getY() + amountY);
	}

	/**
	 * Sets the X coordinate.
	 * 
	 * @param x
	 *            the X coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	public Position modifyX(int x) {
		this.x = x;
		return new Position(this);
	}

	/**
	 * Gets the X coordinate.
	 * 
	 * @return the X coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the Y coordinate.
	 * 
	 * @param y
	 *            the Y coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	public Position modifyY(int y) {
		this.y = y;
		return new Position(this);
	}

	/**
	 * Gets the Y coordinate.
	 * 
	 * @return the Y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the Z coordinate.
	 * 
	 * @param z
	 *            the Z coordinate
	 */
	public void setZ(int z) {
		this.z = z;
	}
	
	public Position modifyZ(int z) {
		this.z = z;
		return new Position(this);
	}

	/**
	 * Gets the Z coordinate.
	 * 
	 * @return the Z coordinate.
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Sets the X coordinate.
	 * 
	 * @param x
	 *            the X coordinate
	 */
	public void setLastX(int x) {
		lastX = x;
	}

	/**
	 * Gets the X coordinate.
	 * 
	 * @return the X coordinate
	 */
	public int getLastX() {
		return lastX;
	}

	/**
	 * Sets the Y coordinate.
	 * 
	 * @param y
	 *            the Y coordinate
	 */
	public void setLastY(int y) {
		lastY = y;
	}

	/**
	 * Gets the Y coordinate.
	 * 
	 * @return the Y coordinate
	 */
	public int getLastY() {
		return lastY;
	}
	
	/**
	 * Sets the Z coordinate.
	 * 
	 * @param z
	 *            the Z coordinate
	 */
	public void setLastZ(int z) {
		lastZ = z;
	}

	/**
	 * Gets the Z coordinate.
	 * 
	 * @return the Z coordinate
	 */
	public int getLastZ() {
		return lastZ;
	}

	/**
	 * Gets the X coordinate of the region containing this Position.
	 * 
	 * @return the region X coordinate
	 */
	public int getRegionX() {
		return (x >> 3) - 6;
	}

	/**
	 * Gets the Y coordinate of the region containing this Position.
	 * 
	 * @return the region Y coordinate
	 */
	public int getRegionY() {
		return (y >> 3) - 6;
	}

	/**
	 * Gets the local X coordinate relative to the base Position.
	 * 
	 * @param base
	 *            the base Position
	 * @return the local X coordinate
	 */
	public int getLocalX(Position base) {
		return x - 8 * base.getRegionX();
	}

	/**
	 * Gets the local Y coordinate relative to the base Position.
	 * 
	 * @param base
	 *            the base Position.
	 * @return the local Y coordinate.
	 */
	public int getLocalY(Position base) {
		return y - 8 * base.getRegionY();
	}

	/**
	 * Gets the local X coordinate relative to this Position.
	 * 
	 * @return the local X coordinate
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the local Y coordinate relative to this Position.
	 * 
	 * @return the local Y coordinate.=
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Checks if this position is viewable from the other position.
	 * 
	 * @param other
	 *            the other position
	 * @return true if it is viewable, false otherwise
	 */
	public boolean isViewableFrom(Position other) {
		if (this.getZ() != other.getZ()) {
			return false;
		}
		Position p = Misc.delta(this, other);
		return p.getX() <= 14 && p.getX() >= -15 && p.getY() <= 14 && p.getY() >= -15;
	}

	/**
	 * Checks if this position is viewable from the other position.
	 * 
	 * @param other
	 *            the other position
	 * @return true if it is viewable, false otherwise
	 */
	public boolean withinDistance(Position other, int distance) {
		if (this.getZ() != other.getZ()) {
			return false;
		}
		return Misc.goodDistance(this, other, distance);
	}

	public boolean nextToWildy(int x, int y) {
		return Area(2942, 3391, 3517, 3965, x, y) || Area(2942, 3391, 9917, 10365, x, y);
	}

	public boolean Area(int x, int x1, int y, int y1, int x2, int y2) {
		return x2 >= x && x2 <= x1 && y2 >= y && y2 <= y1;
	}

	public boolean isInMultiWild() {
		return (x >= 3029 && x <= 3374 && y >= 3759 && y <= 3903) || (x >= 2250 && x <= 2280 && y >= 4670 && y <= 4720) || (x >= 3198 && x <= 3380 && y >= 3904 && y <= 3970) || (x >= 3191 && x <= 3326 && y >= 3510 && y <= 3759) || (x >= 2987 && x <= 3006 && y >= 3912 && y <= 3937) || (x >= 2245 && x <= 2295 && y >= 4675 && y <= 4720) || (x >= 2450 && x <= 3520 && y >= 9450 && y <= 9550) || (x >= 3006 && x <= 3071 && y >= 3602 && y <= 3710) || (x >= 3134 && x <= 3192 && y >= 3519 && y <= 3646) || inPestControlGame();
	}

	public boolean inPestControlBoat() {
		return (x >= 2660 && x <= 2663 && y >= 2638 && y <= 2643);
	}

	public boolean inPestControlGame() {
		return (x >= 2624 && x <= 2690 && y >= 2550 && y <= 2625);
	}

	public boolean multiZone() {
		return x >= 3287 && x <= 3298 && y >= 3167 && y <= 3178 || x >= 3070 && x <= 3095 && y >= 3405 && y <= 3448 || x >= 2961 && x <= 2981 && y >= 3330 && y <= 3354 || x >= 2510 && x <= 2537 && y >= 4632 && y <= 4660 || x >= 3012 && x <= 3066 && y >= 4805 && y <= 4858 || x >= 2794 && x <= 2813 && y >= 9281 && y <= 9305 || x >= 3546 && x <= 3557 && y >= 9689 && y <= 9700 || x >= 2708 && x <= 2729 && y >= 9801 && y <= 9829 || x >= 3450 && x <= 3525 && y >= 9470 && y <= 9535 || x >= 3207 && x <= 3395 && y >= 3904 && y <= 3903 || x >= 3006 && x <= 3072 && y >= 3611 && y <= 3712 || x >= 3149 && x <= 3395 && y >= 3520 && y <= 4000 || x >= 2365 && x <= 2420 && y >= 5065 && y <= 5120 || x >= 2890 && x <= 2935 && y >= 4425 && y <= 4470 || x >= 2250 && x <= 2290 && y >= 4675 && y <= 4715
				|| x >= 2690 && x <= 2825 && y >= 2680 && y <= 2810;
	}

	/**
	 * Creates a new location based on this location.
	 * 
	 * @param diffX
	 *            X difference.
	 * @param diffY
	 *            Y difference.
	 * @param diffZ
	 *            Z difference.
	 * @return The new location.
	 */
	public Position transform(int diffX, int diffY, int diffZ) {
		return new Position(x + diffX, y + diffY, z + diffZ);
	}

	@Override
	public Position clone() {
		return new Position(x, y, z);
	}

	public Position getActualLocation(int biggestSize) {
		if (biggestSize == 1) {
			return this;
		}
		int bigSize = (int) Math.floor(biggestSize / 2);
		return transform(bigSize, bigSize, 0);
	}

	/**
	 * Determines whether or not this position is directly connectable in a
	 * straight line to another.
	 * 
	 * @param other
	 *            The other
	 * @return true if connectable, false otherwise
	 */
	public boolean isConnectable(Position other) {
		int deltaX = x - other.getX();
		int deltaY = y - other.getY();
		return Math.abs(deltaX) == Math.abs(deltaY) || deltaX == 0 || deltaY == 0;
	}

    public boolean isPerpendicularTo(Position other) {
        Position delta = Misc.delta(this, other);
        return delta.getX() != delta.getY() && delta.getX() == 0 || delta.getY() == 0;
    }
}
