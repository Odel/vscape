package com.rs2.model.tick;

import java.util.LinkedList;
import java.util.List;

/**
 * A class that manages <code>Tickable</code>s for a specific
 * <code>GameEngine</code>.
 * 
 * @author Michael Bull
 * 
 */
public class TickManager {

	/**
	 * The list of tickables.
	 */
	private List<Tick> ticks = new LinkedList<Tick>();

	/**
	 * @return The tickables.
	 */
	public List<Tick> getTickables() {
		return ticks;
	}

	/**
	 * Submits a new tickable to the <code>GameEngine</code>.
	 * 
	 * @param tickable
	 *            The tickable to submit.
	 */
	public void submit(final Tick tick) {
		ticks.add(tick);
	}

}
