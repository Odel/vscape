package com.rs2.util;

import java.nio.ByteBuffer;

public class NameUtil {

	public static final char VALID_CHARS[] = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[', ']', '|', '?', '/', '`'};

	public static String longToName(long l) {
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static String GetPlayerLevelColour(int playerLevel, int level) {
		int diff = playerLevel - level;
		if (diff < 0 && diff > -10)
			return "@gr2@" + level;
		else if (diff == 0)
			return "@yel@" + level;
		else if (diff > 0 && diff < 10)
			return "@ora@" + level;
		else if (diff > 0)
			return "@red@" + level;
		else
			return "@gre@" + level;
	}

	public static long nameToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z') {
				l += 1 + c - 65;
			} else if (c >= 'a' && c <= 'z') {
				l += 1 + c - 97;
			} else if (c >= '0' && c <= '9') {
				l += 27 + c - 48;
			}
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	public static String uppercaseFirstLetter(String str) {
		str = str.toLowerCase();
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}

	/**
	 * Formats a name for display.
	 * 
	 * @param s
	 *            The name.
	 * @return The formatted name.
	 */
	public static String formatName(String s) {
		return fixName(s.replace(" ", "_"));
	}

	/**
	 * Method that fixes capitalization in a name.
	 * 
	 * @param s
	 *            The name.
	 * @return The formatted name.
	 */
	private static String fixName(final String s) {
		if (s.length() > 0) {
			final char ac[] = s.toCharArray();
			for (int j = 0; j < ac.length; j++)
				if (ac[j] == '_') {
					ac[j] = ' ';
					if ((j + 1 < ac.length) && (ac[j + 1] >= 'a') && (ac[j + 1] <= 'z')) {
						ac[j + 1] = (char) ((ac[j + 1] + 65) - 97);
					}
				}

			if ((ac[0] >= 'a') && (ac[0] <= 'z')) {
				ac[0] = (char) ((ac[0] + 65) - 97);
			}
			return new String(ac);
		} else {
			return s;
		}
	}

	public static String getRS2String(ByteBuffer buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while (buf.hasRemaining() && (b = buf.get()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}
}
