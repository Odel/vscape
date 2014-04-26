package com.rs2.model.tick;

/**
 * Represents a task that is executed in the future, once or periodically. Each
 * "tick" represents 0.6ms, or one game loop.
 * 
 * @author Michael Bull
 * 
 */
public abstract class Tick {

	/*
	 * What is the difference between remainingTicks and tickDelay?
	 * ============================================================
	 * 
	 * tickDelay = the delay in ticks between consecutive executions. e.g. a
	 * combat event may run at 3 ticks, which mean each 3*600 ms it is executed
	 * 
	 * remainingTicks = the number of ticks until this tickable next runs. e.g.
	 * the tick delay will be 1 if it'll be ran in the next cycle.
	 */

	/**
	 * The amount of ticks before this event executes.
	 */
	private int remainingTicks;

	/**
	 * The ticks to reset to once executed if the tickable still runs.
	 */
	private int tickDelay;

	/**
	 * The running flag.
	 */
	private boolean running = true;

	/**
	 * The immediate flag.
	 */
	private boolean immediate = true;

	/**
	 * Creates a tickable with the specified amount of ticks and is not immediate.
	 * 
	 * @param ticks
	 *            The amount of ticks.
	 */
	public Tick(int ticks) {
		this(ticks, false);
	}

	/**
	 * Creates a tickable with the specified amount of ticks and immediate flag.
	 * 
	 * @param ticks
	 *            The amount of ticks.
	 */
	public Tick(int ticks, boolean immediate) {
		this.remainingTicks = ticks;
		this.tickDelay = ticks;
		this.immediate = immediate;
	}

	/**
	 * Gets the tick delay.
	 * 
	 * @return The delay, in ticks.
	 */
	public int getTickDelay() {
		return tickDelay;
	}

	/**
	 * Gets the remaining ticks.
	 * 
	 * @return The remaining ticks.
	 */
	public int getRemainingTicks() {
		return remainingTicks;
	}

	/**
	 * Gets the immediate flag
	 * 
	 * @return If event should start immediately
	 */
	public boolean getImmediate() {
		return immediate;
	}

	/**
	 * Sets the tick delay.
	 * 
	 * @param ticks
	 *            The amount of ticks to set.
	 * @throws IllegalArgumentException
	 *             if the delay is negative.
	 */
	public void setTickDelay(int ticks) {
		if (ticks < 0) {
			throw new IllegalArgumentException("Tick amount must be positive.");
		}
		this.tickDelay = ticks;
	}

	/**
	 * Checks if the tick is running.
	 * 
	 * @return <code>true</code> if the tick is still running,
	 *         <code>false</code> if not.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Stops the tick from running in the future.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * The execute method is called when the tick is run. The general contract
	 * of the execute method is that it may take any action whatsoever.
	 */
	public abstract void execute();

	/**
	 * This is executed every cycle and handles the counters and such.
	 */
	public void cycle() {
		if (getImmediate()) {
			execute();
			immediate = false;
		} else if (remainingTicks-- <= 1) {
			if (isRunning()) {
				execute();
			}
			remainingTicks = tickDelay;
		}
	}

}
