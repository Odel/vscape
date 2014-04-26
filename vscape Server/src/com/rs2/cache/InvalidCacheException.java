package com.rs2.cache;

/**
 * An exception which is thrown when a cache is invalid (i.e. corrupt or not a
 * cache).
 * 
 * @author Graham Edgecombe
 * 
 */
public class InvalidCacheException extends Exception {

	/**
	 * The serial version unique id.
	 */
	private static final long serialVersionUID = 6238347561608460220L;

	/**
	 * Creates the invalid cache exception.
	 * 
	 * @param s
	 *            The reason.
	 */
	public InvalidCacheException(String s) {
		super(s);
	}

	/**
	 * Creates the invalid cache exception.
	 * 
	 * @param ex
	 *            The parent exception.
	 */
	public InvalidCacheException(Exception ex) {
		super(ex);
	}

}
