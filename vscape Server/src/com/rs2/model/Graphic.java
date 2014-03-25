package com.rs2.model;

public class Graphic {

	private int id, height, delay;

	public Graphic(int id, int height) {
		this.id = id;
		this.height = height;
		this.delay = 0;
	}

	public static Graphic highGraphic(int id) {
		return new Graphic(id, 100);
	}

	public static Graphic lowGraphic(int id) {
		return new Graphic(id, 0);
	}

	public Graphic setDelay(int delay) {
		this.delay = delay;
		return this;
	}

	public int getId() {
		return id;
	}

	public int getValue() {
		return (height << 16) + delay;
	}
}
