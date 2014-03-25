package com.rs2.cache;

import java.nio.ByteBuffer;

/**
 * Represents a single cache file.
 * 
 * @author Graham Edgecombe
 * 
 */
public class CacheFile {

	/**
	 * The cache id.
	 */
	private int cache;

	/**
	 * The file id.
	 */
	private int file;

	/**
	 * The file data.
	 */
	private ByteBuffer data;

	/**
	 * Creates a cache file.
	 * 
	 * @param cache
	 *            The cache id.
	 * @param file
	 *            The file id.
	 * @param data
	 *            The file data.
	 */
	public CacheFile(int cache, int file, ByteBuffer data) {
		this.cache = cache;
		this.file = file;
		this.data = data;
	}

	/**
	 * Gets the cache id.
	 * 
	 * @return The cache id.
	 */
	public int getCache() {
		return cache;
	}

	/**
	 * Gets the file id.
	 * 
	 * @return The file id.
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Gets the buffer.
	 * 
	 * @return The buffer.
	 */
	public ByteBuffer getBuffer() {
		return data;
	}

	/**
	 * Gets the buffer as a byte array.
	 * 
	 * @return The byte array.
	 */
	public byte[] getBytes() {
		byte[] bytes = new byte[data.limit()];
		data.position(0);
		data.get(bytes);
		return bytes;
	}

}
