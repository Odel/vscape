package com.rs2.model.tick;

import com.rs2.model.World;

/**
 *
 */
public class TickStopWatch {
    
    private int startTicks;
    public TickStopWatch() {
        reset();
    }
    
    public int elapsed() {
        return World.SERVER_TICKS-startTicks;
    }

    public void reset() {
        this.startTicks = World.SERVER_TICKS;
    }
}
