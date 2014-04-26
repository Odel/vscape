package com.rs2.model.players;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.rs2.model.Position;
import com.rs2.model.objects.GameObject;
import com.rs2.util.XStreamUtil;

public class GlobalObjectHandler {

	private static List<GlobalObject> objects = new ArrayList<GlobalObject>();

	@SuppressWarnings("unchecked")
	public static void loadObjects() throws FileNotFoundException {
		System.out.println("Loading global objects...");
		List<GlobalObject> list = (List<GlobalObject>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/objects.xml"));
		for (GlobalObject object : list) {
			objects.add(object);
		}
		System.out.println("Loaded " + list.size() + " objects.");
	}

	/**
	 * Creates an object for everyone online.
	 * 
	 */
	public static void createGlobalObject() {
		for (GlobalObject object : objects) {
			new GameObject(object.getId(), object.getPosition().getX(), object.getPosition().getY(), object.getPosition().getZ(), object.getFace(), object.getType(), -1, 99999);
		}
	}

	/**
	 * Creates an object just for yourself. Specified the object id, position,
	 * face, and type
	 * 
	 * @param player
	 */
	public static void createObject(Player player, int id, Position position, int face, int type) {
		new GameObject(id, position.getX(), position.getY(), position.getZ(), face, type, -1, 99999);
	}

	public static void setObjects(List<GlobalObject> objects) {
		GlobalObjectHandler.objects = objects;
	}

	public static List<GlobalObject> getObjects() {
		return objects;
	}

}