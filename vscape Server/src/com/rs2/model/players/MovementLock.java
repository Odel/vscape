package com.rs2.model.players;

import com.rs2.model.Entity;

/**
*
*/
public interface MovementLock {

    public boolean forcesRun();
    public void start(Entity entity);
    public void end(Entity entity);

}
