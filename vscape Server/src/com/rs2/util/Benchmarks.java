package com.rs2.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Static map collection of benchmarks.
 * 
 * @author Blake Beaupain
 */
public class Benchmarks {

	/** The benchmark map. */
	private static Map<String, Benchmark> benchmarkMap = new HashMap<String, Benchmark>();

	/**
	 * Gets a benchmark.
	 * 
	 * @param key
	 *            The benchmark key
	 * @return The benchmark
	 */
	public static Benchmark getBenchmark(String key) {
		Benchmark benchmark = benchmarkMap.get(key);
		if (benchmark == null) {
			benchmarkMap.put(key, benchmark = new Benchmark());
		}
		return benchmark;
	}

    public static void resetAll() {
        for (Entry<String, Benchmark> entry : benchmarkMap.entrySet()) {
            entry.getValue().reset();
        }
    }

	/**
	 * Prints all mapped benchmarks.
	 */
	public static void printAll() {
		for (Entry<String, Benchmark> entry : benchmarkMap.entrySet()) {
			System.out.println("[Benchmark] " + entry.getKey() + " took " + entry.getValue().getTime() + "ms");
		}
	}

}
