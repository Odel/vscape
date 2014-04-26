package com.rs2.model.tick;

import com.rs2.model.World;

/**
 *
 */
public class TickTimer {

	private int startTicks;
	private int waitDuration;

	public TickTimer(int waitDuration) {
		setWaitDuration(waitDuration);
		reset();
	}

	public int getWaitDuration() {
		return waitDuration;
	}

	public void setWaitDuration(int waitDuration) {
		this.waitDuration = waitDuration;
	}

	public void reset() {
		this.startTicks = World.SERVER_TICKS;
	}

	public boolean completed() {
		return World.SERVER_TICKS - startTicks >= waitDuration;
	}

    public int ticksRemaining() {
        return waitDuration - (World.SERVER_TICKS - startTicks);

    }
}
