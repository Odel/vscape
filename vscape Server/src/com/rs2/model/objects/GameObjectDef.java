package com.rs2.model.objects;

import com.rs2.model.Position;

public class GameObjectDef {
    
    private int id, type, face;
    private Position position;

    public GameObjectDef(int id, int type, int face, Position position) {
        this.id = id;
        this.type = type;
        this.face = face;
        this.position = position;
    }
    
    public int getId() {
        return id;
    }
    
    public int getType() {
        return type;
    }
    
    public int getFace() {
        return face;
    }

    public Position getPosition() {
        return position;
    }
}
