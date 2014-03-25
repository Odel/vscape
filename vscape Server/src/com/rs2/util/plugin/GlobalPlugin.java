package com.rs2.util.plugin;

/**
 * Represents a global server plugin.
 * 
 * @author Tommo
 * 
 */
public abstract class GlobalPlugin extends AbstractPlugin {

	/**
	 * Creates a new global server plugin.
	 */
	public GlobalPlugin() {
		super(Type.GLOBAL);
	}

	/**
	 * Called every server tick (600ms).
	 */
	public void onTick() {
	}

}
