package com.rs2.util;

import com.rs2.model.Position;

/**
 *
 */
public class Area {
    
    private int bottomX, bottomY, topX, topY;
    private byte z;
    
    private Area(int bottomX, int bottomY, int topX, int topY, byte z) {
        this.bottomX = bottomX;
        this.bottomY = bottomY;
        this.topX = topX;
        this.topY = topY;
        this.z = z;
    }
    
    public Position[] calculateAllPositions() {
        Position[] positions = new Position[((topX-bottomX)+1)*((topY-bottomY)+1)];
        int current = 0;
        for (int x = bottomX; x <= topX; x++) {
            for (int y = bottomY; y <= topY; y++) {
                positions[current++] = new Position(x, y, z);
            }
        }
        return positions;
    }
    
    public static Area areaFromCenter(Position pos, int radius) {
        int bottomX = pos.getX()-radius;
        int bottomY = pos.getY()-radius;
        int topX = pos.getX()+radius;
        int topY = pos.getY()+radius;
        return new Area(bottomX, bottomY, topX, topY, (byte)pos.getZ());
    }

    public static Area areaFromCorner(Position pos, int width, int height) {
        int bottomX = pos.getX();
        int bottomY = pos.getY();
        int topX = pos.getX()+width;
        int topY = pos.getY()+height;
        return new Area(bottomX, bottomY, topX, topY, (byte)pos.getZ());
    }
    
    public boolean contains(Position pos) {
        int x = pos.getX(); 
        int y = pos.getY();
        return x > bottomX && x < topX && y > bottomY && y < topY; //pos.getZ() == z && 
    }
}
