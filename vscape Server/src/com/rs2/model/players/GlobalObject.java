package com.rs2.model.players;

import com.rs2.model.Position;

public class GlobalObject {

	private int id;
	private Position position;
	private int face;
	private int type;
	
	public GlobalObject() {
	    this.id = 0;
	    this.position = null;
	    this.face = 0;
	    this.type = 0;
	}
	
	public GlobalObject(int id, Position position, int face, int type) {
		this.id = id;
		this.position = position;
		this.face = face;
		this.type = type;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public void setFace(int face) {
		this.face = face;
	}

	public int getFace() {
		return face;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

}