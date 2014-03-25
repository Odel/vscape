package com.rs2.cache.util;

import java.nio.ByteBuffer;

/**
 * A utility class for byte buffers.
 * 
 * @author Graham Edgecombe
 * 
 */
public class ByteBufferUtils {

	/**
	 * Gets a smart.
	 * 
	 * @param buf
	 *            The buffer.
	 * @return The smart.
	 */
	public static int getSmart(ByteBuffer buf) {
		int peek = buf.get(buf.position()) & 0xFF;
		if (peek < 128) {
			return buf.get() & 0xFF;
		} else {
			return (buf.getShort() & 0xFFFF) - 32768;
		}
	}

	/**
	 * Gets an RS2 string from the buffer.
	 *
	 */
    public static String getString(ByteBuffer buffer) {
        StringBuilder bldr = new StringBuilder();
        char c;
        while ((c = (char) buffer.get()) != 10) {
            bldr.append(c);
        }
        return bldr.toString();
    }

}
