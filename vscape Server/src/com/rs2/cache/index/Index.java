package com.rs2.cache.index;

/**
 * An index points to a file in the cache.
 * 
 * @author Graham Edgecombe
 * 
 */
public abstract class Index {

	/**
	 * The identifier.
	 */
	private int identifier;

	/**
	 * Creates the index.
	 * 
	 * @param identifier
	 *            The identifier.
	 */
	public Index(int identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the identifier.
	 * 
	 * @return The identifier.
	 */
	public int getIdentifier() {
		return identifier;
	}

}
