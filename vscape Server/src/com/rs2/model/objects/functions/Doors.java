package com.rs2.model.objects.functions;

import java.util.ArrayList;
import java.util.List;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 30/03/12 Time: 21:10 To change
 * this template use File | Settings | File Templates.
 */
public class Doors {

	private static List<Doors> doors = new ArrayList<Doors>();

	public Doors(int door, int x, int y, int z) {
		this.doorId = door;
		this.originalId = door;
		this.doorX = x;
		this.doorY = y;
		this.originalX = x;
		this.originalY = y;
		this.doorZ = z;
		int face1 = SkillHandler.getFace(door, x, y, z);
		this.originalFace = face1;
		this.currentFace = face1;
		int type1 = SkillHandler.getType(door, x, y, z);
		this.type = type1;
		this.open = alreadyOpen(door);
	}

	private static Doors getDoor(int id, int x, int y, int z) {
		for (Doors d : doors) {
			if (d.doorX == x && d.doorY == y && d.doorZ == z) {
				return d;
			}
		}
		Doors door = new Doors(id, x, y, z);
		doors.add(door);
		return door;
	}

	public static boolean isDoor(int id, int x, int y, int z) {
		return getDoor(id, x, y, z) != null;
	}

	public static boolean handleDoor(int id, int x, int y, int z) {
		final String objectName = GameObjectData.forId(id) != null ? GameObjectData.forId(id).getName().toLowerCase() : "";
		if (!objectName.contains("fence") && !objectName.contains("gate") && !objectName.contains("door") || objectName.contains("trapdoor") || objectName.contains("tree") || objectName.contains("guild") ) {
			return false;
		}
		/* Special Doors */
		if (id == 3944 || id == 3945 || id == 2559 || id == 2537 || id == 883 || id == 1805 || id == 2881 || id == 2882 || id == 2883 || id == 2623 || id == 2112 || id == 1804 || id == 2266 || id == 2406 || id == 2407 || id == 2631 || id == 2623 || id == 8958 || id == 8959 || id == 8960 || id == 1589 || id == 1590 || id == 190 || id == 4577 || id == 10721) {
			return false;
		}
		if(id == 2025 && x == 2611 && y == 3394)//fishing guild
		{
			return false;
		}
		if((id == 2307 || id == 2308) && x >= 2997 && x <= 2998 && y == 3931)
		{
			return false;
		}
		if((id == 52 || id == 53) && x >= 2649 && x <= 2650 && y == 3470)
		{
			return false;
		}
		if(id == 4423 || id == 4424 || id == 4427 || id == 4428 || id == 4465 || id == 4466 || id == 4467 || id == 4468)
		{
			return false;
		}
		if(id == 9300)
		{
			return false;
		}
		Doors d = getDoor(id, x, y, z);
		if (d == null) {
			return false;
		}
		GameObject g = ObjectHandler.getInstance().getObject(id, x, y, z);
		if (g != null) {
			if (d.type == 9) {
				ObjectHandler.getInstance().removeClip(x, y, z, 9, g.getDef().getFace());
			} else {
				ObjectHandler.getInstance().removeDoorClip(x, y, z, g.getDef().getFace());
			}
		}
		CacheObject c = ObjectLoader.object(id, x, y, z);
		if (c != null) {
			if (d.type == 9) {
				ObjectHandler.getInstance().removeClip(x, y, z, 9, c.getRotation());
			} else {
				ObjectHandler.getInstance().removeDoorClip(x, y, z, c.getRotation());
			}
		}
		int xAdjustment = 0, yAdjustment = 0;
		if (d.type == 0) {
			if (!d.open) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					xAdjustment = -1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					yAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					xAdjustment = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					yAdjustment = -1;
				}
			} else if (d.open) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					yAdjustment = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					xAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					yAdjustment = -1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					xAdjustment = -1;
				}
			}
		} else if (d.type == 9) {
			if (!d.open) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					xAdjustment = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					xAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					xAdjustment = -1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					xAdjustment = -1;
				}
			} else if (d.open) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					xAdjustment = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					xAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					xAdjustment = -1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					xAdjustment = -1;
				}
			}
		}
		if (xAdjustment != 0 || yAdjustment != 0) {
			ObjectHandler.getInstance().removeObject(d.doorX, d.doorY, d.doorZ, 0);
			new GameObject(Constants.EMPTY_OBJECT, d.doorX, d.doorY, d.doorZ, 0, d.type, Constants.EMPTY_OBJECT, 999999999);
		}
		if (d.doorX == d.originalX && d.doorY == d.originalY) {
			d.doorX += xAdjustment;
			d.doorY += yAdjustment;
		} else {
			ObjectHandler.getInstance().removeObject(d.doorX, d.doorY, d.doorZ, 0);
			new GameObject(Constants.EMPTY_OBJECT, d.doorX, d.doorY, d.doorZ, 0, d.type, Constants.EMPTY_OBJECT, 999999999);
			d.doorX = d.originalX;
			d.doorY = d.originalY;
		}
		if (id == 1513) {
			d.doorId = 1534;
		} else if (id == 1512 || id == 22 || id == 2550 || id == 2551 || id == 2556 || id == 2558 || id == 2557 || id == 3014) {
			d.doorId = id;
		} else if (d.doorId == d.originalId) {
			if (!d.open) {
				d.doorId += 1;
			} else if (d.open) {
				d.doorId -= 1;
			}
		} else if (d.doorId != d.originalId) {
			if (!d.open) {
				d.doorId -= 1;
			} else if (d.open) {
				d.doorId += 1;
			}
		}
		int newFace = getNextFace(d);
		ObjectHandler.getInstance().removeObject(d.doorX, d.doorY, d.doorZ, 0);
		new GameObject(d.doorId, d.doorX, d.doorY, d.doorZ, newFace, d.type, Constants.EMPTY_OBJECT, 999999);
		return true;
	}

	public static int getNextFace(Doors d) {
		int f = d.originalFace;
		if (d.type == 0) {
			if (!d.open) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 2;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 3;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 0;
				} else if (d.originalFace != d.currentFace) {
					f = d.originalFace;
				}
			} else if (d.open) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 3;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 0;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 2;
				} else if (d.originalFace != d.currentFace) {
					f = d.originalFace;
				}
			}
		} else if (d.type == 9) {
			if (!d.open) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 3;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 2;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 0;
				} else if (d.originalFace != d.currentFace) {
					f = d.originalFace;
				}
			} else if (d.open) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 3;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 0;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 2;
				} else if (d.originalFace != d.currentFace) {
					f = d.originalFace;
				}
			}
		}
		d.currentFace = f;
		return f;
	}

	public static void handlePassThroughDoor(final Player player, final int id, final int x, final int y, final int z, final int destX, final int destY) {
		Doors doors = getDoor(id, x, y, z);
		if (doors == null) {
			player.setStopPacket(false);
			return;
		}
		handleDoor(id, x, y, z);
		final Doors door = doors;
		player.getActionSender().walkTo(destX, destY, false);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				handleDoor(door.doorId, door.doorX, door.doorY, door.doorZ);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, 2);
	}

	private boolean alreadyOpen(int id) {
		for (int i = 0; i < openDoors.length; i++) {
			if (openDoors[i] == id) {
				return true;
			}
		}
		return false;
	}

	private int doorId;
	private int originalId;
	private int doorX;
	private int doorY;
	private int originalX;
	private int originalY;
	private int doorZ;
	private int originalFace;
	private int currentFace;
	private int type;
	private boolean open;

	private static int[] openDoors = {1504, 1514, 1517, 1520, 1531, 1534, 2033, 2035, 2037, 2998, 3271, 4468, 4697, 6101, 6103, 6105, 6107, 6109, 6111, 6113, 6115, 6976, 6978, 8696, 8819, 10261, 10263, 10265, 11708, 11710, 11712, 11715, 11994, 12445, 13002,};
}
