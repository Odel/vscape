package com.rs2.util.plugin;

/**
 * Represents an abstract server plugin.
 * 
 * @author Tommo
 * 
 */
public abstract class AbstractPlugin {

	public static enum Type {
		ABSTRACT, GLOBAL, LOCAL
	}

	private Type type = Type.ABSTRACT;

	public AbstractPlugin(Type type) {
		this.type = type;
	}

	/**
	 * 
	 * @return The name of the plugin.
	 */
	public abstract String getName();

	/**
	 * 
	 * @return The author of the plugin.
	 */
	public abstract String getAuthor();

	/**
	 * 
	 * @return The plugin version.
	 */
	public abstract double getVersion();

	/**
	 * Called when a plugin is initiated.
	 */
	public void onCreate() {
	}

	/**
	 * Called when a plugin is shutdown.
	 */
	public void onDestroy() {
	}

	/**
	 * Resets the current plugin.
	 */
	public void reset() {
	}

	/**
	 * 
	 * @return The {@link com.rs2.util.plugin.AbstractPlugin.Type} of the
	 *         plugin.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Logs the specified message to local system output.
	 * 
	 * @param message
	 *            The message to log.
	 */
	public void log(String message) {
		System.out.println("[PLUGIN-" + getName() + "]: " + message);
	}

}
