package com.rs2.pf;

/**
 * A class which stores a grid of tiles and manages which directions can or
 * cannot be traversed.
 * 
 * @author Graham Edgecombe
 * 
 */
public class TileMap {

	/**
	 * A tile in which traversal in any direction is permitted.
	 */
	public static final Tile EMPTY_TILE = new Tile(Tile.NORTH_TRAVERSAL_PERMITTED | Tile.EAST_TRAVERSAL_PERMITTED | Tile.SOUTH_TRAVERSAL_PERMITTED | Tile.WEST_TRAVERSAL_PERMITTED);

	/**
	 * A tile in which traversal in no directions is permitted.
	 */
	public static final Tile SOLID_TILE = new Tile(0);

	/**
	 * A two dimensional array of tiles.
	 */
	private final Tile[][] tiles;

	/**
	 * The width of the tile map.
	 */
	private final int width;

	/**
	 * The height of the tile map.
	 */
	private final int height;

	/**
	 * Creates the tile map with the specified initial grid of tiles.
	 * 
	 * @param tiles
	 *            The tiles array.
	 * @throws IllegalArgumentException
	 *             if the width is zero or the heights are zero or differ.
	 */
	public TileMap(Tile[][] tiles) {
		this.tiles = tiles;
		width = tiles.length;
		if (width == 0) {
			throw new IllegalArgumentException("Width is zero.");
		}
		int possibleHeight = tiles[0].length;
		if (possibleHeight == 0) {
			throw new IllegalArgumentException("Height is zero.");
		}
		for (int x = 0; x < width; x++) {
			if (tiles[x].length != possibleHeight) {
				throw new IllegalArgumentException("The heights differ.");
			}
		}
		height = possibleHeight;
	}

	/**
	 * Creates a blank tile map with the specified width and height. The map is
	 * filled with <code>EMPTY_TILE</code>s.
	 * 
	 * @param width
	 *            The width of the tile map.
	 * @param height
	 *            The height of the tile map.
	 */
	public TileMap(int width, int height) {
		this(width, height, EMPTY_TILE);
	}

	/**
	 * Creates and fills a map with the specified width, height and default
	 * tile.
	 * 
	 * @param width
	 *            The width of the tile map.
	 * @param height
	 *            The height of the tile map.
	 * @param defaultTile
	 *            The default tile, which the map is filled with.
	 */
	public TileMap(int width, int height, Tile defaultTile) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < width; y++) {
				tiles[x][y] = defaultTile;
			}
		}
	}

	/**
	 * Checks the bounds of the specified coordinates.
	 * 
	 * @param x
	 *            X coordinate.
	 * @param y
	 *            Y coordinate.
	 * @throw IllegalArgumentException if either coordinate is out of bounds.
	 */
	private void checkBounds(int x, int y) {
		if (x < 0 || x >= width) {
			throw new IllegalArgumentException("X coordinate out of permitted range.");
		}
		if (y < 0 || y >= height) {
			throw new IllegalArgumentException("Y coordinate out of permitted range.");
		}
	}

	/**
	 * Gets the width of the tile map.
	 * 
	 * @return The width of the tile map.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the tile map.
	 * 
	 * @return The height of the tile map.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets a tile.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @return The tile at the coordinates.
	 * @throws IllegalArgumentException
	 *             if the coordinates are out of bounds.
	 */
	public Tile getTile(int x, int y) {
		checkBounds(x, y);
		return tiles[x][y];
	}

	/**
	 * Sets a tile.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param tile
	 *            The new tile.
	 * @throws IllegalArgumentException
	 *             if the coordinates are out of bounds.
	 */
	public void setTile(int x, int y, Tile tile) {
		checkBounds(x, y);
		tiles[x][y] = tile;
	}

}
