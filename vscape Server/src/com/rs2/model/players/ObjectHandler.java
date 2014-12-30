package com.rs2.model.players;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.firemaking.Firemaking;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Doors;
import com.rs2.model.objects.functions.DoubleDoors;
import com.rs2.model.players.item.Item;
import com.rs2.util.Benchmark;
import com.rs2.util.Benchmarks;
import com.rs2.util.Misc;
import com.rs2.util.clip.Rangable;
import com.rs2.util.clip.Region;

/**
 * @author Sanity
 */

public class ObjectHandler {

	public static ArrayList<GameObject> objects = new ArrayList<GameObject>();
	/**
	 * The instance of this class
	 */
	private static ObjectHandler instance;

	/**
	 * Returns the instance of this class
	 * 
	 * @return
	 */
	public static ObjectHandler getInstance() {
		if (ObjectHandler.instance == null) {
			ObjectHandler.instance = new ObjectHandler();
		}
		return ObjectHandler.instance;
	}

	public void tick() {
		Benchmark b = Benchmarks.getBenchmark("tickObjects");
		b.start();
		for (GameObject o : new ArrayList<GameObject>(objects)) {
			if(o == null)
			{
				continue;
			}
			if (o.tick > 0 && o.tick < 99999) {
				o.tick--;
			} else if (o.tick <= 0) {
					objects.remove(o);
					updateObject(o);
			}
		}
		b.stop();
	}

	public void updateObject(GameObject o) {
		if (o.getDef().getId() == 734) {
			addClip(o.newId, o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getFace(), o.getDef().getType());
		} else if (o.clip && o.getDef().getId() > 0 && o.getDef().getId() != Constants.EMPTY_OBJECT && !Firemaking.isFire(o.getDef().getId()) && !Doors.isDoor(o.getDef().getId(), o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ()) && !DoubleDoors.isDoor(o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ())) {
			addClip(o.newId, o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getFace(), o.getDef().getType());
		}
		if (Firemaking.isFire(o.getDef().getId())) {
            GroundItem item = new GroundItem(new Item(592), new Position(o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ()), false);
            GroundItemManager.getManager().dropItem(item);
		}
		for (Player players : World.getPlayers()) {
			if (players == null) {
				continue;
			}
			if (players.getPosition().getZ() == o.getDef().getPosition().getZ() && Misc.goodDistance(o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), players.getPosition().getX(), players.getPosition().getY(), 60)) {
				players.getActionSender().sendObject(o.newId, o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.faceOriginal, o.getDef().getType());
			}
		}
	}

	public void placeObject(GameObject o, boolean clip) {
		if (clip) {
			if (o.getDef().getId() > 0 && o.getDef().getId() != Constants.EMPTY_OBJECT && !Firemaking.isFire(o.getDef().getId())) {
				addClip(o.newId, o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getFace(), o.getDef().getType());
			} else {
				removeClip(o.newId, o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getFace(), o.getDef().getType());
			}
		}
		for (Player players : World.getPlayers()) {
			if (players == null) {
				continue;
			}
			if (players.getPosition().getZ() == o.getDef().getPosition().getZ() && Misc.goodDistance(o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), players.getPosition().getX(), players.getPosition().getY(), 60)) {
				players.getActionSender().sendObject(o.getDef().getId(), o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getFace(), o.getDef().getType());
			}
		}
	}

	public GameObject getObject(int x, int y, int height) {
		for (GameObject o : objects) {
			if (o.getDef().getPosition().getX() == x && o.getDef().getPosition().getY() == y && o.getDef().getPosition().getZ() == height) {
				return o;
			}
		}
		return null;
	}

	public GameObject getObject(int id, int x, int y, int height) {
		for (GameObject o : objects) {
			if (o.getDef().getId() == id && o.getDef().getPosition().getX() == x && o.getDef().getPosition().getY() == y && o.getDef().getPosition().getZ() == height) {
				return o;
			}
		}
		return null;
	}

	public GameObject getObjectDoor(int x, int y, int height) {
		for (GameObject o : objects) {
			if (o.getDef().getType() == 0 && o.getDef().getPosition().getX() == x && o.getDef().getPosition().getY() == y && o.getDef().getPosition().getZ() == height) {
				return o;
			}
		}
		return null;
	}

	public void removeObject(int x, int y, int height, int type) {
		GameObject o = getObject(x, y, height);
		if (o != null) {
		    if(height < 4) {
			removeClip(o.getDef().getId(), x, y, height, type, o.getDef().getFace());
		    }
			objects.remove(o);
			updateObject(o);
		}
	}

	public void removeObject(int id, int x, int y, int height, int type) {
		GameObject o = getObject(id, x, y, height);
		if (o != null) {
			removeClip(o.getDef().getId(), x, y, height, type, o.getDef().getFace());
			objects.remove(o);
			updateObject(o);
		}
	}

	public void loadObjects(Player c) {
		if (c == null) {
			return;
		}
		for (GameObject o : objects) {
			if (loadForPlayer(o, c)) {
				c.getActionSender().sendObject(o.getDef().getId(), o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ(), o.getDef().getFace(), o.getDef().getType());
			}
		}
	}

	/*
	 * public void updateDoorClip(int x, int y, int height, int face) {
	 * Region.updateDoorClip(x, y, height, face); Rangable.updateDoorClip(x, y,
	 * height, face); }
	 */

	public void updateDoorClip(int x, int y, int height, int oldFace, int newFace) {
		removeDoorClip(x, y, height, oldFace);
		addDoorClip(x, y, height, newFace);
	}

	public void addDoorClip(int x, int y, int height, int face) {
		Region.addDoorClip(x, y, height, face);
		Rangable.addDoorClip(x, y, height, face);
	}

	public void removeDoorClip(int x, int y, int height, int face) {
		Region.removeDoorClip(x, y, height, face);
		Rangable.removeDoorClip(x, y, height, face);
	}

	public void addClip(int id, int x, int y, int height, int face, int type) {
		Region.addObject(id, x, y, height, face, type, false);
		Rangable.addObject(id, x, y, height, face, type, false);
	}

	//Remove 1 square clip
	public void removeClip(int x, int y, int height, int type, int direction) {
		Region.removeObject(1, x, y, height, direction, type);
		Rangable.removeObject(1, x, y, height, direction, type);
	}

	public void removeClip(int id, int x, int y, int height, int type, int direction) {
		Region.removeObject(id, x, y, height, direction, type);
		Rangable.removeObject(id, x, y, height, direction, type);
	}

	public final int IN_USE_ID = 14825;

	public boolean isObelisk(int id) {
		for (int obeliskId : obeliskIds) {
			if (obeliskId == id) {
				return true;
			}
		}
		return false;
	}

	public int[] obeliskIds = {14829, 14830, 14827, 14828, 14826, 14831};
	public int[][] obeliskCoords = {{3154, 3618}, {3225, 3665}, {3033, 3730}, {3104, 3792}, {2978, 3864}, {3305, 3914}};
	public boolean[] activated = {false, false, false, false, false, false};

	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				new GameObject(14825, obeliskCoords[index][0], obeliskCoords[index][1], 0, 0, 10, obeliskId, 16);
				new GameObject(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1], 0, 0, 10, obeliskId, 16);
				new GameObject(14825, obeliskCoords[index][0], obeliskCoords[index][1] + 4, 0, 0, 10, obeliskId, 16);
				new GameObject(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1] + 4, 0, 0, 10, obeliskId, 16);
			}
		}
	}

	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id) {
				return j;
			}
		}
		return -1;
	}

	@SuppressWarnings("unused")
	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (Player c : World.getPlayers()) {
			if (c == null) {
				continue;
			}
			int xOffset = c.getPosition().getX() - obeliskCoords[port][0];
			int yOffset = c.getPosition().getY() - obeliskCoords[port][1];
			if (Misc.goodDistance(c.getPosition().getX(), c.getPosition().getY(), obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2, 1)) {
				// Magic.teleportObelisk(c, obeliskCoords[random][0] + xOffset,
				// obeliskCoords[random][1] + yOffset, 0);
			}
		}
	}

	public boolean loadForPlayer(GameObject o, Player c) {
		if (o == null || c == null) {
			return false;
		}
		return c.getPosition().getZ() == o.getDef().getPosition().getZ() && Misc.goodDistance(o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), c.getPosition().getX(), c.getPosition().getY(), 60);
	}

	public void addObject(GameObject o, boolean clip) {
		if (getObject(o.getDef().getPosition().getX(), o.getDef().getPosition().getY(), o.getDef().getPosition().getZ()) == null) {
			objects.add(o);
			placeObject(o, clip);
		}
	}

	// obId, obX, obY, obH, obWalkX, obWalkY, teleX, teleY, teleH
	public final static int[][] objectsMove = {{4415, 2425, 3074, 3, 2426, 3074, 2425, 3077, 2}, {4417, 2425, 3074, 2, 2425, 3077, 2426, 3074, 3}, {4415, 2430, 3081, 2, 2430, 3080, 2427, 3081, 1}, {4417, 2428, 3081, 1, 2427, 3081, 2430, 3080, 2}, {4415, 2419, 3080, 1, 2420, 3080, 2419, 3077, 0}, {4417, 2419, 3078, 0, 2419, 3077, 2420, 3080, 1}, {4418, 2380, 3127, 0, 2380, 3130, 2379, 3127, 1}, {4415, 2380, 3127, 1, 2379, 3127, 2380, 3130, 0}, {4418, 2369, 3126, 1, 2372, 3126, 2369, 3127, 2}, {4415, 2369, 3126, 2, 2369, 3127, 2372, 3126, 1}, {4418, 2374, 3131, 2, 2374, 3130, 2373, 3133, 3}, {4415, 2374, 3133, 3, 2373, 3133, 2374, 3130, 2},};

	public static void getObjectDetails(Player player, int objectId, int x, int y) {
		player.objectWalkX = player.objectWalkY = player.objectXSize = player.objectYSize = 0;
		switch (objectId) {
			/*
			 * case 312 : // farming objects case 313 : case 1161 : case 2646 :
			 * case 4675 : case 4677 : case 4678 : case 4679 : c.objectWalkX =
			 * c.objectX; c.objectWalkY = c.objectY; c.objectXSize =
			 * c.objectYSize = 0; break;
			 */
			case 4494: // slayer tower
			case 4496: // slayer tower
			case 4493: // slayer tower
			case 4495: // slayer tower
				player.objectXSize = 2;
				player.objectYSize = 4;
				return;
			case 1722 :
			case 1723 :
				player.objectXSize = 2;
				player.objectYSize = 3;
				return;
			case 1747 : // climb up ladder
			case 1750 :
			case 1755 :
			case 2148 :
			case 2609 :
			case 8744 :
			case 9319 :
				int face = SkillHandler.getFace(objectId, x, y, 0);
				player.objectWalkX = x + (face == 1 ? 1 : face == 3 ? -1 : 0);
				player.objectWalkY = y + (face == 0 ? 1 : face == 2 ? -1 : 0);
				return;
			case 3566 : // brimhaven swing
				player.objectWalkX = player.getPosition().getX() > 2768 ? 2769 : 2764;
				player.objectWalkY = player.getPosition().getX() > 2768 ? 9567 : 9569;
				return;
			case 2640 : // prayer guild altar
				player.objectXSize = 1;
				player.objectYSize = 2;
				return;
			case 3044 : // tut island furnace
				player.objectXSize = 5;
				player.objectYSize = 5;
				return;
			case 2309 : // wildy entrance
				player.objectWalkX = 2998;
				player.objectWalkY = 3916;
				return;
			case 2307 : // wildy exit
			case 2308 :
				player.objectWalkX = 2998;
				player.objectWalkY = 3931;
				return;
			case 2288 : // wildy tunnel
				player.objectWalkX = 3004;
				player.objectWalkY = 3937;
				return;
			case 2283 : // wildy swing
				player.objectWalkX = 3005;
				player.objectWalkY = 3953;
				player.objectYOffset = 2;
				return;
			case 2311 : // wildy stones
				player.objectWalkX = 3002;
				player.objectWalkY = 3960;
				return;
			case 2297 : // wildy log
				player.objectWalkX = 3002;
				player.objectWalkY = 3945;
				return;
			case 154 : // gnome tunnel
				player.objectWalkX = 2484;
				player.objectWalkY = 3430;
				return;
			case 4058 : // gnome tunnel
				player.objectWalkX = 2487;
				player.objectWalkY = 3430;
				return;
			case 5959 :
				player.moveToX = 2416;
				player.moveToY = 3074;
				break;
			case 1815 :
			case 5960 :
			case 1816 :
				player.objectXSize = player.objectYSize = 0;
				break;
			case 4467 :
			case 4468 :
			case 8959 :
				player.objectYOffset = 1;
				break;
			case 4465 :
			case 4466 :
				player.objectYOffset = -1;
				break;
			case 4381 :
				player.objectYOffset = 2;
				break;
			case 4382 :
				player.objectYOffset = -2;
				break;
			// castle wars objects
				
			case 4419 :
				if (player.rangableArea(player.getPosition().getX(), player.getPosition().getY())) {
					player.objectWalkX = 2416;
					player.objectWalkY = 3074;
					player.moveToX = 2417;
					player.moveToY = 3077;
				} else {
					player.objectWalkX = 2417;
					player.objectWalkY = 3077;
					player.moveToX = 2416;
					player.moveToY = 3074;
				}
				player.moveToH = 0;
				return;
			case 4420 :
				if (player.rangableArea(player.getPosition().getX(), player.getPosition().getY())) {
					player.objectWalkX = 2383;
					player.objectWalkY = 3133;
					player.moveToX = 2382;
					player.moveToY = 3130;
				} else {
					player.objectWalkX = 2382;
					player.objectWalkY = 3130;
					player.moveToX = 2383;
					player.moveToY = 3133;
				}
				player.moveToH = 0;
				return;
			case 2558 :
				if (player.getPosition().getX() > player.objectX && player.objectX == 3044) {
					player.objectXOffset = 1;
				}
				if (player.getPosition().getY() > player.objectY) {
					player.objectYOffset = 1;
				}
				if (player.getPosition().getX() < player.objectX && player.objectX == 3038) {
					player.objectXOffset = -1;
				}
				player.objectXSize = player.objectYSize = 0;
				break;
			case 4031 : // Shantay pass
			case 6706 : // torag
				player.objectXOffset = 2;
				break;
			case 6707 : // verac
				player.objectYOffset = 3;
				break;
			case 6823 :
				player.objectYOffset = 1;
				break;
			case 6772 :
				player.objectYOffset = 1;
				break;
			case 6705 : // karils
				player.objectYOffset = -1;
				break;
			case 6822 :
				player.objectYOffset = 1;
				break;
			case 6704 : // guthan
				player.objectYOffset = -1;
				break;
			case 6773 :
				player.objectXOffset = 1;
				player.objectYOffset = 1;
				break;
			case 6703 : // dharok
				player.objectXOffset = -1;
				break;
			case 6771 :
				player.objectXOffset = 1;
				player.objectYOffset = 1;
				break;
			case 6702 : // ahrim
				player.objectXOffset = -1;
				break;
			case 6821 :
				player.objectXOffset = 1;
				player.objectYOffset = 1;
				break;
		}
		for (int[] ob : objectsMove) {
			if (objectId == ob[0] && player.objectX == ob[1] && player.objectY == ob[2] && player.getPosition().getZ() == ob[3]) {
				player.objectWalkX = ob[4];
				player.objectWalkY = ob[5];
				player.moveToX = ob[6];
				player.moveToY = ob[7];
				player.moveToH = ob[8];
				return;
			}
		}
	}


}