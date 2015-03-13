package com.rs2.model.region.music;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 17/04/12 Time: 23:55 To change
 * this template use File | Settings | File Templates.
 */
public class Music {
	
	public Music(int[] regions, String name, int song, int frame, int button) {
		this.regions = regions;
		this.name = name;
		this.song = song;
		this.frame = frame;
		this.button = button;
	}
	
	private final int[] regions;
	
	private final String name;
	
	private final int song;
	
	private final int frame;
	
	private final int button;
	
	public int[] getRegions() {
		return regions;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSong() {
		return song;
	}
	
	public int getFrame() {
		return frame;
	}

	public int getButton() {
		return button;
	}
}
