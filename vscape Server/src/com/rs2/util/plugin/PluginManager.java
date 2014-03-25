package com.rs2.util.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.rs2.model.players.Player;
import com.rs2.util.Benchmark;
import com.rs2.util.Benchmarks;

/**
 * Manages all plugins for the server.
 * 
 * @author Tommo
 * 
 */
public class PluginManager {

	/**
	 * A linked-list of class objects representing the loaded local plugins.
	 */
	private static List<Class<LocalPlugin>> localPlugins = new LinkedList<Class<LocalPlugin>>();

	/**
	 * A linked-list of currently loaded global plugins.
	 */
	private static List<GlobalPlugin> globalPlugins = new LinkedList<GlobalPlugin>();

	/**
	 * Loads plugins located in the com.rs2.util.plugin.impl package.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void loadPlugins() {
		try {
			System.out.println("Loading plugins..");
			Class[] pluginClasses = getClasses("com.rs2.util.plugin.impl");

			for (Class s : pluginClasses) {
				AbstractPlugin p = (AbstractPlugin) s.newInstance();
				if (p.getType() == AbstractPlugin.Type.GLOBAL) {
					register((GlobalPlugin) p);
				} else if (p.getType() == AbstractPlugin.Type.LOCAL) {
					register(s);
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all local plugins for the specified player.
	 * 
	 * @param p
	 *            The player.
	 */
	public static void loadLocalPlugins(Player p) {
		synchronized (localPlugins) {
			Iterator<Class<LocalPlugin>> iter = localPlugins.iterator();

			while (iter.hasNext()) {
				try {
					LocalPlugin lp = iter.next().newInstance();
					lp.setPlayer(p);
					p.addPlugin(lp);
				} catch (InstantiationException e) {
					e.printStackTrace();
					System.out.println("Error instantiating local plugin for " + p.getUsername());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					System.out.println("Error instantiating local plugin for " + p.getUsername());
				}
			}
		}
	}

	/**
	 * Called every server cycle (600ms). Calls onTick() in the plugin.
	 */
	public static void tick() {
		Benchmark b = Benchmarks.getBenchmark("tickPlugins");
		b.start();
		synchronized (globalPlugins) {
			Iterator<GlobalPlugin> iter = globalPlugins.iterator();

			while (iter.hasNext()) {
				iter.next().onTick();
			}
		}
		b.stop();
	}

	/**
	 * Powers the plug-in's tick (so long as requested in plugin)
	 */
	public static void reset() {
		synchronized (globalPlugins) {
			Iterator<GlobalPlugin> iter = globalPlugins.iterator();

			while (iter.hasNext()) {
				iter.next().reset();
			}
		}
	}

	/**
	 * Loops through the plugins and calls onDestroy().
	 */
	public static void close() {
		synchronized (globalPlugins) {
			Iterator<GlobalPlugin> iter = globalPlugins.iterator();

			while (iter.hasNext()) {
				iter.next().onDestroy();
			}
		}
	}

	/**
	 * Registers a new local plugin.
	 * 
	 * @param plugin
	 *            The plugin to register.
	 */
	private static void register(Class<LocalPlugin> plugin) {
		synchronized (localPlugins) {
			System.out.println("Loaded local plugin: " + plugin.getSimpleName());
			localPlugins.add(plugin);
		}
	}

	/**
	 * Registers a new global plugin.
	 * 
	 * @param plugin
	 *            The plugin to register.
	 */
	private static void register(GlobalPlugin plugin) {
		synchronized (localPlugins) {
			System.out.println("Loaded global plugin: " + plugin.getName() + " v" + plugin.getVersion() + " by " + plugin.getAuthor());
			plugin.onCreate();
			globalPlugins.add(plugin);
		}
	}

	/**
	 * Returns all of the classes found in the specified package.
	 * 
	 * @param packageName
	 *            The package name
	 * @return An array of generic class objects - the plugin classes.
	 * @throws ClassNotFoundException
	 * @throws java.io.IOException
	 */
	@SuppressWarnings("rawtypes")
	private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Returns a list of classes found in the specified directory.
	 * 
	 * @param directory
	 *            The directory.
	 * @param packageName
	 *            The package name.
	 * @return A list of found classes.
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

}
