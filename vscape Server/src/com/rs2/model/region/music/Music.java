package com.rs2.model.region.music;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 17/04/12 Time: 23:55 To change
 * this template use File | Settings | File Templates.
 */
public class Music {
	public Music(int music, int swX, int swY, int neX, int neY) {
		this.music = music;
		this.swX = swX;
		this.swY = swY;
		this.neX = neX;
		this.neY = neY;
	}

	public int music, swX, swY, neX, neY;
}
