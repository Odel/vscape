package com.rs2.util;

/**
 * A simple class used to benchmark time.
 * 
 * @author Blake Beaupain
 */
public class Benchmark {

	/** The start time. */
	private long startTime;

    
    private long duration = 0;

	/**
	 * Starts the benchmark.
	 */
	public void start() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * Stops the benchmark.
	 */
	public void stop() {
		duration += System.currentTimeMillis()-startTime;
	}

    public void reset() {
        duration = 0;
    }

	/**
	 * Gets the benchmark time, in milliseconds.
	 * 
	 * @return The benchmark time
	 */
	public long getTime() {
        return duration;
	}

}
