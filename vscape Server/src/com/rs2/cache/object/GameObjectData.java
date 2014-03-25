package com.rs2.cache.object;

import java.io.IOException;
import java.util.logging.Logger;

import com.rs2.util.clip.ObjectDef;

/**
 * Represents a single type of object.
 * 
 * @author Graham Edgecombe
 * 
 */
public class GameObjectData {

	/**
	 * The maximum number of object definitions
	 */
	public static final int MAX_DEFINITIONS = 25374;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(GameObjectData.class.getName());

	public static void init() throws IOException {
		/*logger.info("Loading definitions...");
		List<GameObjectData> defs = (List<GameObjectData>) XStreamUtil.getxStream().fromXML(new FileInputStream("data/content/objectDefinitions.xml"));
		definitions = new GameObjectData[defs.size()];
		for (int i = 0; i < defs.size(); i++) { // Max items in the 377 cache..
												// (Others only matter for newly
												// added maps)
			GameObjectData def = defs.get(i);
			definitions[def.getId()] = def;
		}
		logger.info("Loaded " + defs.size() + " object definitions.");*/
	}

	/**
	 * The definitions array.
	 */
	private static GameObjectData[] definitions = new GameObjectData[MAX_DEFINITIONS];

	/**
	 * Adds a definition. TODO better way?
	 * 
	 * @param def
	 *            The definition.
	 */
	public static void addDefinition(GameObjectData def) {
		definitions[def.getId()] = def;
	}

	/**
	 * Gets an object definition by its id.
	 * 
	 * @param id
	 *            The id.
	 * @return The definition.
	 */
	public static GameObjectData forId(int id) {
		if (id < definitions.length) {
			if (definitions[id] == null) {
                ObjectDef def = ObjectDef.getObjectDef(id);
                if (def == null)
                    definitions[id] = produce(id);
                else {
                    String description = def.description == null ? "" : new String(def.description);
                    GameObjectData obj = new GameObjectData(id, def.name, description, def.xLength, def.yLength, def.isSolid, def.isWalkable, def.hasActions, def.boolean64, def.walkType);
                    if (id == 2781)
                        obj.sizeX = 3;
                    definitions[id] = obj;
                } 
            }
            return definitions[id];
		}
        return null;
	}
    
    private static GameObjectData produce(int id) {
        return new GameObjectData(id, "Object: #" + id, "Its an object!", 1, 1, false, false, false, true, 2);
    }

	/**
	 * The id.
	 */
	private int id;

	/**
	 * The name.
	 */
	private String name;

	/**
	 * The description.
	 */
	private String desc;

	/**
	 * X size.
	 */
	private int sizeX;

	/**
	 * Y size.
	 */
	private int sizeY;

	/**
	 * Solid flag.
	 */
	private boolean solid;

	/**
	 * Walkable flag.
	 */
	private boolean walkable;

	/**
	 * 'Has actions' flag.
	 */
	private boolean hasActions;

    private boolean unknown;
    
    private boolean rangeAble;

    private boolean canShootThru;

	/**
	 * Creates the definition.
	 * 
	 * @param id
	 *            The id.
	 * @param name
	 *            The name of the object.
	 * @param desc
	 *            The description of the object.
	 * @param sizeX
	 *            The x size of the object.
	 * @param sizeY
	 *            The y size of the object.
	 * @param isSolid
	 *            Solid flag.
	 * @param isWalkable
	 *            Walkable flag.
	 * @param hasActions
	 *            Flag which indicates if this object has any actions.
	 */
	public GameObjectData(int id, String name, String desc, int sizeX, int sizeY, boolean isSolid, boolean isWalkable, boolean hasActions, boolean unknown, int walkType) {
		this.id = id;
		this.name = name;
        if (name == null)
            this.name = "";
		this.desc = desc;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.solid = isSolid;
		this.walkable = isWalkable;
		this.hasActions = hasActions;
        this.unknown = unknown;
        this.rangeAble = walkType <= 1 || (walkType == 2 && !solid);
        this.canShootThru = setRangeAble();
	}

    public boolean unknown() {
        return unknown;
    }

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return desc;
	}

	/**
	 * Gets the x size.
	 * 
	 * @return The x size.
	 */
	public int getSizeX() {
		return sizeX;
	}

	/**
	 * Gets the y size.
	 * 
	 * @return The y size.
	 */
	public int getSizeY() {
		return sizeY;
	}

	public int getSizeX(int rotation) {
		if (rotation == 1 || rotation == 3) {
			return sizeY;
		} else {
			return sizeX;
		}
	}

	public int getSizeY(int rotation) {
		if (rotation == 1 || rotation == 3) {
			return sizeX;
		} else {
			return sizeY;
		}
	}

	/**
	 * Checks if this object is solid.
	 * 
	 * @return The solid flag.
	 */
	public boolean isSolid() {
		return solid;
	}

	/**
	 * Checks if this object is walkable.
	 * 
	 * @return The walkable flag.
	 */
	public boolean isWalkable() {
		return walkable;
	}

	/**
	 * Checks if this object has any actions.
	 * 
	 * @return A flag indicating that this object has some actions.
	 */
	public boolean hasActions() {
		return hasActions;
	}

	public int getBiggestSize() {
		if (sizeY > sizeX) {
			return sizeY;
		}
		return sizeX;
	}
    
    public boolean isRangeAble() {
        return rangeAble;
    }

    public boolean canShootThru() {
        return canShootThru;
    }

    public boolean setRangeAble() {
        int[] rangableObjects = {14462, 14464, 14465, 14466, 14467, 14468, 14470, 14502, 11754, 3007, 980, 997, 4262, 14437, 14438, 4437, 4439, 3487, 3457};
        for (int i : rangableObjects) {
            if (i == id) {
                return true;
            }
        }
        if (name != null) {
            final String name1 = name.toLowerCase();
            String[] rangables = {"fungus", "mushroom", "sarcophagus", "counter", "plant", "altar", "pew", "log", "stump", "stool", "sign", "cart", "chest", "rock", "bush", "hedge", "chair", "table", "crate", "barrel", "box", "skeleton", "corpse", "vent", "stone", "rockslide"};
            for (String i : rangables) {
                if (name1.contains(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*private void setRangeAble() {
        int[] rangableObjects = {11754, 3007, 980, 997, 4262, 14437, 14438, 4437, 4439, 3487, 3457};
        for (int i : rangableObjects) {
            if (i == id) {
                rangeAble = true;
            }
        }
        if (name != null) {
            final String name1 = name.toLowerCase();
            String[] rangables = {"sarcophagus", "counter", "plant", "altar", "pew", "log", "stump", "stool", "sign", "cart", "chest", "rock", "bush", "hedge", "chair", "table", "crate", "barrel", "box", "skeleton", "corpse", "vent", "stone", "rockslide"};
            for (String i : rangables) {
                if (name1.contains(i)) {
                    rangeAble = true;
                }
            }
        }
    }  */

}
