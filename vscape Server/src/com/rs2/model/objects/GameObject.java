package com.rs2.model.objects;

import com.rs2.model.Position;
import com.rs2.model.players.ObjectHandler;

public class GameObject {

    GameObjectDef def;
	public int faceOriginal, oX, oY;
	public int newId;
	public int tick;
	public boolean clip;

	public GameObject(int ID, int X, int Y, int Height, int Face, int Type, int NewId, int Tick) {
		GameObject p = ObjectHandler.getInstance().getObject(X, Y, Height);
		if (p != null && ID == p.def.getId()) {
			return;
		}
        def = new GameObjectDef(ID, Type, Face, new Position(X, Y, Height));
		faceOriginal = def.getFace();
		newId = NewId;
		tick = Tick;
		clip = true;
		ObjectHandler.getInstance().addObject(this, clip);
	}

	public GameObject(int ID, int X, int Y, int Height, int Face, int Type, int NewId, int Tick, boolean c) {
		GameObject p = ObjectHandler.getInstance().getObject(X, Y, Height);
		if (p != null && ID == p.def.getId()) {
			return;
		}
        def = new GameObjectDef(ID, Type, Face, new Position(X, Y, Height));
		faceOriginal = def.getFace();
		newId = NewId;
		tick = Tick;
		clip = c;
		ObjectHandler.getInstance().addObject(this, clip);
	}

	public GameObject(int ID, int X, int Y, int Height, int Face, int Type, int NewId, int Tick, int originalFace, int originalX, int originalY) {
		GameObject p = ObjectHandler.getInstance().getObject(X, Y, Height);
		if (p != null && ID == p.def.getId()) {
			return;
			//ObjectHandler.getInstance().removeObject(X, Y, Height, Type);
		}
        def = new GameObjectDef(ID, Type, Face, new Position(X, Y, Height));
		oX = originalX;
		oY = originalY;
		faceOriginal = originalFace;
		newId = NewId;
		tick = Tick;
		clip = true;
		ObjectHandler.getInstance().addObject(this, clip);
	}

	public GameObject(int ID, int X, int Y, int Height, int Face, int Type, int NewId, int Tick, int originalFace, int originalX, int originalY, boolean c) {
		GameObject p = ObjectHandler.getInstance().getObject(X, Y, Height);
		if (p != null && ID == p.def.getId()) {
			return;
			//ObjectHandler.getInstance().removeObject(X, Y, Height, Type);
		}
        def = new GameObjectDef(ID, Type, Face, new Position(X, Y, Height));
		oX = originalX;
		oY = originalY;
		faceOriginal = originalFace;
		newId = NewId;
		tick = Tick;
		clip = c;
		ObjectHandler.getInstance().addObject(this, clip);
	}
    
    public GameObjectDef getDef() {
        return def;
    }
    
	public void addOriginalFace(int face) {
		faceOriginal = face;
	}

	public void addOriginalInts(int x, int y, int face) {
		oX = x;
		oY = y;
		faceOriginal = face;
	}

}