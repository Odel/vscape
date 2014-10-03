package com.rs2.cache.object;

import com.rs2.Constants;
import java.io.IOException;
import java.util.logging.Logger;

import com.rs2.cache.Cache;
import com.rs2.cache.InvalidCacheException;
import com.rs2.cache.index.impl.MapIndex;
import com.rs2.cache.index.impl.StandardIndex;
import com.rs2.cache.map.LandscapeListener;
import com.rs2.cache.map.LandscapeParser;
import com.rs2.cache.obj.ObjectDefinitionListener;
import com.rs2.cache.region.Regions;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.GlobalObjectHandler;
import com.rs2.model.players.ObjectHandler;

/**
 * Manages all of the in-game objects.
 * 
 * @author Graham Edgecombe
 * 
 */
public class ObjectLoader implements LandscapeListener, ObjectDefinitionListener {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ObjectLoader.class.getName());

	/**
	 * The number of definitions loaded.
	 */
	private int definitionCount = 0;

	/**
	 * The count of objects loaded.
	 */
	private int objectCount = 0;

	/**
	 * Loads the objects in the map.
	 * 
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 * @throws com.rs2.cache.InvalidCacheException
	 *             if the cache is invalid.
	 */
	public void load() throws IOException, InvalidCacheException {
		Cache cache = Cache.getSingleton();
		try {
			logger.info("Loading definitions...");
			@SuppressWarnings("unused")
			StandardIndex[] defIndices = cache.getIndexTable().getObjectDefinitionIndices();
			//new ObjectDefinitionParser(cache, defIndices, this).parse();
			logger.info("Loaded " + definitionCount + " object definitions.");
			logger.info("Loading map...");
			MapIndex[] mapIndices = cache.getIndexTable().getMapIndices();
			for (MapIndex index : mapIndices) {
				new LandscapeParser(cache, index.getIdentifier(), this).parse();
			}
			logger.info("Loaded " + objectCount + " objects.");
			removeUnnecessaryClipping();
			addNecessaryClipping();
			//GlobalObjectHandler.createGlobalObject();
		} catch(Exception e) { 
            e.printStackTrace();
        } finally {
			cache.close();
		}

	}

	public static void removeUnnecessaryClipping() {
		ObjectHandler.getInstance().removeClip(3363, 9646, 2, 10, 0);
		ObjectHandler.getInstance().removeClip(3366, 9646, 2, 10, 0);
		ObjectHandler.getInstance().removeClip(3363, 9630, 2, 10, 0);
		ObjectHandler.getInstance().removeClip(3366, 9630, 2, 10, 0);

		ObjectHandler.getInstance().removeClip(3360, 9643, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3359, 9644, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3366, 9643, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3367, 9644, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3366, 9637, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3367, 9636, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3360, 9637, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3359, 9636, 0, 10, 0);
		
		ObjectHandler.getInstance().removeClip(2787, 3439, 0, 10, 0); //catherby oak tree
		ObjectHandler.getInstance().removeClip(2792, 3432, 0, 10, 0); //catherby fence
		
		ObjectHandler.getInstance().removeClip(3424, 9895, 0, 10, 0); //undernearth paterdomus temple
		ObjectHandler.getInstance().removeClip(3424, 9896, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3423, 9896, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3429, 9891, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(3428, 9891, 0, 10, 0);
		
		ObjectHandler.getInstance().removeClip(2910, 9802, 0, 10, 0); //blue dragon eggs
		ObjectHandler.getInstance().removeClip(3021, 3251, 0, 10, 0); //port sarim fence
		ObjectHandler.getInstance().removeClip(2984, 3227, 0, 10, 0); //Rimmington mine fence
		
		
	}
	
	public static void addNecessaryClipping() {
		ObjectHandler.getInstance().addDoorClip(3408, 3488, 0, 2); //paterdomus doors
		ObjectHandler.getInstance().addDoorClip(3408, 3489, 0, 2);
		ObjectHandler.getInstance().addDoorClip(3086, 9934, 0, 1); //obelisk of earth
		ObjectHandler.getInstance().addDoorClip(3087, 9934, 0, 1); //obelisk of earth
		ObjectHandler.getInstance().addDoorClip(3088, 9934, 0, 1); //obelisk of earth
		ObjectHandler.getInstance().addClip(2150, 3088, 9932, 0, 2, 10); //still obelisk of earth
		ObjectHandler.getInstance().addClip(2150, 3088, 9933, 0, 2, 10);
		ObjectHandler.getInstance().addClip(2150, 3088, 9934, 0, 2, 10);
		ObjectHandler.getInstance().addClip(2150, 3088, 9932, 0, 3, 10);
		ObjectHandler.getInstance().addClip(2150, 3087, 9932, 0, 3, 10);
		ObjectHandler.getInstance().addClip(2150, 3086, 9932, 0, 3, 10);
		ObjectHandler.getInstance().addClip(2150, 3086, 9932, 0, 0, 10); //still obelisk of earth
		ObjectHandler.getInstance().addClip(2150, 3086, 9933, 0, 0, 10);
		ObjectHandler.getInstance().addClip(2150, 3086, 9934, 0, 0, 10);
	}
	
	public static void addObjects() {
	    
	}

	@Override
	public void objectParsed(CacheObject obj) {
		objectCount++;
		World.getWorld().getRegionManager().getRegionByLocation(obj.getLocation()).getGameObjects().add(obj);
	}

	@Override
	public void objectDefinitionParsed(GameObjectData def) {
		definitionCount++;
		GameObjectData.addDefinition(def);
	}

	public static CacheObject object(int x, int y, int z) {
		final Position loc = new Position(x, y, z);
		Regions r = World.getWorld().getRegionManager().getRegionByLocation(loc);
		for (CacheObject go : r.getGameObjects()) {
			if (go.getLocation().equals(loc)) {
				return go;
			}
		}
		return null;
	}

	public static CacheObject object(int id, int x, int y, int z) {
		final Position loc = new Position(x, y, z);
		Regions r = World.getWorld().getRegionManager().getRegionByLocation(loc);
		for (CacheObject go : r.getGameObjects()) {
			if (go.getDef().getId() == id && go.getLocation().equals(loc)) {
				return go;
			}
		}
		return null;
	}

	public static CacheObject object(String name, int x, int y, int z) {
		final Position loc = new Position(x, y, z);
		Regions r = World.getWorld().getRegionManager().getRegionByLocation(loc);
		for (CacheObject go : r.getGameObjects()) {
			final String objectName = GameObjectData.forId(go.getDef().getId()) != null ? GameObjectData.forId(go.getDef().getId()).getName().toLowerCase() : "";
			if (objectName.contains(name.toLowerCase()) && go.getLocation().equals(loc)) {
				return go;
			}
		}
		return null;
	}

}
