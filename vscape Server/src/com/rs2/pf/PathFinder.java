package com.rs2.pf;

import com.rs2.model.Entity;
import com.rs2.model.Position;

/**
 *
 */
public interface PathFinder {
    
    public Path findPath(Entity entity, Position end, boolean clipped);
}
