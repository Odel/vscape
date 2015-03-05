package com.rs2.cache.object;


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
import com.rs2.model.content.quests.ChristmasEvent;
import com.rs2.model.objects.GameObject;
/*import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.GlobalObjectHandler;*/
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
			addObjects();
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
		
		ObjectHandler.getInstance().removeClip(2766, 2782, 0, 10, 0); //ape atoll spot
		ObjectHandler.getInstance().removeClip(2766, 2781, 0, 10, 0); //ape atoll spot
		ObjectHandler.getInstance().removeClip(2765, 2782, 0, 10, 0); //ape atoll spot
		ObjectHandler.getInstance().removeClip(2765, 2781, 0, 10, 0); //ape atoll spot
		ObjectHandler.getInstance().removeClip(2764, 2783, 0, 10, 0); //ape atoll
		ObjectHandler.getInstance().removeClip(2755, 2705, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(2742, 2763, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(2742, 2762, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(2760, 2761, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(2760, 2762, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(2761, 2762, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(2761, 2761, 0, 10, 0);
		
		ObjectHandler.getInstance().removeClip(3367, 9644, 0, 22, 0);
		ObjectHandler.getInstance().removeClip(3366, 9643, 0, 22, 0);
		ObjectHandler.getInstance().removeClip(3360, 9643, 0, 22, 0);
		ObjectHandler.getInstance().removeClip(3359, 9644, 0, 22, 0);
		ObjectHandler.getInstance().removeClip(3360, 9637, 0, 22, 0);
		ObjectHandler.getInstance().removeClip(3359, 9636, 0, 22, 0);
		ObjectHandler.getInstance().removeClip(3367, 9636, 0, 22, 0);
		ObjectHandler.getInstance().removeClip(3366, 9637, 0, 22, 0); //Mage arena enchanting chamber
		
		ObjectHandler.getInstance().removeClip(3363, 9630, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3366, 9630, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3371, 9630, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3371, 9646, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3366, 9646, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3363, 9646, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3358, 9646, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3358, 9630, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3356, 9628, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3356, 9648, 2, 22, 0);
		ObjectHandler.getInstance().removeClip(3373, 9628, 2, 22, 0); //MTA alchemist playground
		
		ObjectHandler.getInstance().removeClip(2559, 3267, 0, 10, 0);
		ObjectHandler.getInstance().removeClip(2559, 3268, 0, 10, 0);
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
		
		ObjectHandler.getInstance().addClip(4712, 2749, 2768, 0, 1, 22);
		ObjectHandler.getInstance().addClip(4712, 2764, 2769, 0, 1, 22);
		
		//ObjectHandler.getInstance().addDoorClip(2802, 2758, 0, 1);
		//ObjectHandler.getInstance().addDoorClip(2799, 2761, 0, 1); //Awowogei's palace
	}
	
	public static void addObjects() {
	    if(ChristmasEvent.CHRISTMAS_ENABLED) {
		//ObjectHandler.getInstance().placeObject(new GameObject(10659, 3195, 3437, 0, 0, 10, 0, 999999, true), true);
		//ObjectHandler.getInstance().placeObject(new GameObject(10665, 3194, 3440, 0, 1, 10, 0, 999999, true), true);
		//ObjectHandler.getInstance().placeObject(new GameObject(10665, 3192, 3438, 0, 2, 10, 0, 999999, true), true);
		new GameObject(10659, 3195, 3437, 0, 0, 10, 0, 999999, true);
		new GameObject(10665, 3194, 3440, 0, 1, 10, 0, 999999, true);
		new GameObject(10665, 3192, 3438, 0, 2, 10, 0, 999999, true);
		
		new GameObject(210, 2851, 3783, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2851, 3782, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2851, 3781, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2851, 3780, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2851, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2852, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2853, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2854, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2855, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2856, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2857, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2858, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2859, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2860, 3779, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2860, 3780, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2860, 3781, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2860, 3782, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2860, 3783, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2860, 3784, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2859, 3784, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2858, 3784, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2857, 3784, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2856, 3784, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2855, 3784, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2854, 3784, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2853, 3784, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2852, 3784, 0, 0, 10, 0, 999999, true);
		new GameObject(210, 2851, 3784, 0, 0, 10, 0, 999999, true);
	    }
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
