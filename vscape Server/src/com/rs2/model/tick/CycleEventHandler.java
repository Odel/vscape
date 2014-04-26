package com.rs2.model.tick;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.rs2.model.Entity;
import com.rs2.util.Benchmark;
import com.rs2.util.Benchmarks;

/**
 * Handles all of our cycle based events
 * 
 * @author Stuart <RogueX>
 * 
 */
public class CycleEventHandler {

	/**
	 * The instance of this class
	 */
	private static CycleEventHandler instance;

	/**
	 * Returns the instance of this class
	 * 
	 * @return
	 */
	public static CycleEventHandler getInstance() {
		if (CycleEventHandler.instance == null) {
			CycleEventHandler.instance = new CycleEventHandler();
		}
		return CycleEventHandler.instance;
	}

	/**
	 * Holds all of our events currently being ran
	 */
	private Queue<CycleEventContainer> events;
    
    private Queue<CycleEventContainer> eventsToAdd;

	/**
	 * Creates a new instance of this class
	 */
	public CycleEventHandler() {
		events = new LinkedList<CycleEventContainer>();
        eventsToAdd = new LinkedList<CycleEventContainer>();
	}

	/**
	 * Add an event to the list
	 * 
	 * @param owner
	 * @param event
	 * @param cycles
	 * @return the container
	 */
	public CycleEventContainer addEvent(Entity owner, CycleEvent event, int cycles) {
        CycleEventContainer container = new CycleEventContainer(owner, event, cycles);
        if (owner.getIndex() == -1)
            return container;
		eventsToAdd.add(container);
		return container;
	}

	/**
	 * Execute and remove events
	 */
	public void tick() {
		//List<CycleEventContainer> remove = new ArrayList<CycleEventContainer>();
		Benchmark b = Benchmarks.getBenchmark("cycleEvents");
		b.start();
        events.addAll(eventsToAdd);
        eventsToAdd.clear();
		for (Iterator<CycleEventContainer> cycleEvents = events.iterator(); cycleEvents.hasNext();) {
            CycleEventContainer c = cycleEvents.next();
            if (c != null) {
                if (c.getOwner() == null || c.getOwner().getIndex() == -1)
                    cycleEvents.remove();
                else {
                    if (c.needsExecution()) {
                        c.execute();
                    }
                    if (!c.isRunning()) {
                        cycleEvents.remove();
                    }
                }
            }
        }
		b.stop();
	}

	/**
	 * Returns the amount of events currently running
	 * 
	 * @return amount
	 */
	public int getEventsCount() {
		return events.size();
	}

	/**
	 * Stops all events which are being ran by the given owner
	 * 
	 * @param owner
	 */
	/*
	All events stop when index == -1, which happens after unregister
	public void stopEvents(Entity owner) {
		for (CycleEventContainer c : events) {
			if (c.getOwner() == owner) {
				c.stop();
			}
		}
	} */

}