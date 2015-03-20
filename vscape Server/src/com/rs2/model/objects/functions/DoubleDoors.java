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

/**
 * Created by IntelliJ IDEA. User: vayken Date: 13/04/12 Time: 00:17 To change
 * this template use File | Settings | File Templates.
 */
public class DoubleDoors {

	private static List<DoubleDoors> doors = new ArrayList<DoubleDoors>();

	private static DoubleDoors getDoor(int x, int y, int z) {
		for (DoubleDoors d : doors) {
			if (d.x == x && d.y == y && d.z == z) {
				return d;
			}
		}
		CacheObject c = ObjectLoader.object("door", x, y, z);
		if (c == null) {
			c = ObjectLoader.object("gate", x, y, z);
			if (c == null) {
				c = ObjectLoader.object("fence", x, y, z);
				if (c == null) {
					return null;
				}
			}
		}
		DoubleDoors door = new DoubleDoors(c.getDef().getId(), x, y, z);
		doors.add(door);
		return door;
	}

	public static boolean isDoor(int x, int y, int z) {
		return getDoor( x, y, z) != null;
	}

	public static boolean handleDoubleDoor(int id, int x, int y, int z) {
		final String objectName = GameObjectData.forId(id) != null ? GameObjectData.forId(id).getName().toLowerCase() : "";
		if (!objectName.contains("fence") && !objectName.contains("gate") && !objectName.contains("door") || objectName.contains("trapdoor") || objectName.contains("tree")) {
			return false;
		}
		if ( id == 10527 || id == 10529 || id == 3507 || id == 3506 || id == 2882 || id == 2883 || id == 1589 || id == 1590 || id == 190 || id == 1600 || id == 1601) {
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
		DoubleDoors doorClicked = getDoor(x, y, z);
		if (doorClicked == null) {
			return false;
		}
		/*if (doorClicked.doorId > 12000) {
			return true; // nearly all of these are not opened
		}*/
		if (!doorClicked.open) {
			if (doorClicked.originalFace == 0) {
				DoubleDoors lowerDoor = getDoor(x, y - 1, z);
				DoubleDoors upperDoor = getDoor(x, y + 1, z);
				if (lowerDoor != null) {
					changeLeftDoor(lowerDoor, false);
					changeRightDoor(doorClicked, false);
				} else if (upperDoor != null) {
					changeLeftDoor(doorClicked, false);
					changeRightDoor(upperDoor, false);
				} else {
					return false;
				}
			} else if (doorClicked.originalFace == 1) {
				DoubleDoors westDoor = getDoor(x - 1, y, z);
				DoubleDoors eastDoor = getDoor(x + 1, y, z);
				if (westDoor != null) {
					changeLeftDoor(westDoor, false);
					changeRightDoor(doorClicked, false);
				} else if (eastDoor != null) {
					changeLeftDoor(doorClicked, false);
					changeRightDoor(eastDoor, false);
				} else {
					return false;
				}
			} else if (doorClicked.originalFace == 2) {
				DoubleDoors lowerDoor = getDoor(x, y + 1, z);
				DoubleDoors upperDoor = getDoor(x, y - 1, z);
				if (lowerDoor != null) {
					changeLeftDoor(lowerDoor, false);
					changeRightDoor(doorClicked, false);
				} else if (upperDoor != null) {
					changeLeftDoor(doorClicked, false);
					changeRightDoor(upperDoor, false);
				} else {
					return false;
				}
			} else if (doorClicked.originalFace == 3) {
				DoubleDoors westDoor = getDoor(x - 1, y, z);
				DoubleDoors eastDoor = getDoor(x + 1, y, z);
				if (westDoor != null) {
					changeLeftDoor(westDoor, false);
					changeRightDoor(doorClicked, false);
				} else if (eastDoor != null) {
					changeLeftDoor(doorClicked, false);
					changeRightDoor(eastDoor, false);
				} else {
					return false;
				}
			}
		} else if (doorClicked.open) {
			if (doorClicked.originalFace == 0) {
				DoubleDoors westDoor = getDoor(x - 1, y, z);
				DoubleDoors upperDoor = getDoor(x + 1, y, z);
				if (westDoor != null) {
					changeLeftDoor(westDoor, false);
					changeRightDoor(doorClicked, false);
				} else if (upperDoor != null) {
					changeLeftDoor(doorClicked, false);
					changeRightDoor(upperDoor, false);
				} else {
					return false;
				}
			} else if (doorClicked.originalFace == 1) {
				DoubleDoors northDoor = getDoor(x, y + 1, z);
				DoubleDoors southDoor = getDoor(x, y - 1, z);
				if (northDoor != null) {
					changeLeftDoor(northDoor, false);
					changeRightDoor(doorClicked, false);
				} else if (southDoor != null) {
					changeLeftDoor(doorClicked, false);
					changeRightDoor(southDoor, false);
				} else {
					return false;
				}
			} else if (doorClicked.originalFace == 2) {
				DoubleDoors westDoor = getDoor(x - 1, y, z);
				DoubleDoors eastDoor = getDoor(x, y - 1, z);
				if (westDoor != null) {
					changeLeftDoor(westDoor, false);
					changeRightDoor(doorClicked, false);
				} else if (eastDoor != null) {
					changeLeftDoor(doorClicked, false);
					changeRightDoor(eastDoor, false);
				} else {
					return false;
				}
			} else if (doorClicked.originalFace == 3) {
				DoubleDoors northDoor = getDoor(x, y + 1, z);
				DoubleDoors southDoor = getDoor(x, y - 1, z);
				if (northDoor != null) {
					changeLeftDoor(northDoor, false);
					changeRightDoor(doorClicked, false);
				} else if (southDoor != null) {
					changeLeftDoor(doorClicked, false);
					changeRightDoor(southDoor, false);
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static void changeLeftDoor(DoubleDoors d, boolean changeId) {
		int xAdjustment = 0, yAdjustment = 0;
		GameObject g = ObjectHandler.getInstance().getObject(d.doorId, d.x, d.y, d.z);
		if (g != null) {
			ObjectHandler.getInstance().removeDoorClip(d.x, d.y, d.z, g.getDef().getFace());
		}
		CacheObject c = ObjectLoader.object(d.doorId, d.x, d.y, d.z);
		if (c != null) {
			ObjectHandler.getInstance().removeDoorClip(d.x, d.y, d.z, c.getRotation());
		}
		if (!d.open) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				xAdjustment = -1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				yAdjustment = 1;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				xAdjustment = +1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				yAdjustment = -1;
			}
		} else if (d.open) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				yAdjustment = -1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				xAdjustment = -1;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				xAdjustment = -1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				xAdjustment = -1;
			}
		}
		if (xAdjustment != 0 || yAdjustment != 0) {
		    ObjectHandler.getInstance().removeObject(d.x, d.y, d.z, 0);
			new GameObject(Constants.EMPTY_OBJECT, d.x, d.y, d.z, 0, 0, Constants.EMPTY_OBJECT, 999999999);
		}
		if (d.x == d.originalX && d.y == d.originalY) {
			d.x += xAdjustment;
			d.y += yAdjustment;
		} else {
		    ObjectHandler.getInstance().removeObject(d.x, d.y, d.z, 0);
			new GameObject(Constants.EMPTY_OBJECT, d.x, d.y, d.z, 0, 0, Constants.EMPTY_OBJECT, 999999999);
			d.x = d.originalX;
			d.y = d.originalY;
		}
		if (changeId) {
			if (d.doorId == d.originalId) {
				if (!d.open) {
					d.doorId += 1;
				} else if (d.open) {
					d.doorId -= 1;
				}
			}
		}
		if (d.doorId != d.originalId) {
			if (!d.open) {
				d.doorId = d.originalId;
			} else if (d.open) {
				d.doorId = d.originalId;
			}
		}
		ObjectHandler.getInstance().removeObject(d.x, d.y, d.z, 0);
		new GameObject(d.doorId, d.x, d.y, d.z, getNextLeftFace(d), 0, Constants.EMPTY_OBJECT, 999999999);
	}

	private static int getNextLeftFace(DoubleDoors d) {
		int f = d.originalFace;
		if (!d.open) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				f = 3;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				f = 0;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				f = 1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				f = 0;
			} else if (d.originalFace != d.currentFace) {
				f = d.originalFace;
			}
		} else if (d.open) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				f = 1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				f = 2;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				f = 1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				f = 2;
			} else if (d.originalFace != d.currentFace) {
				f = d.originalFace;
			}
		}
		d.currentFace = f;
		return f;
	}

	public static void changeRightDoor(DoubleDoors d, boolean changeId) {
		int xAdjustment = 0, yAdjustment = 0;
		GameObject g = ObjectHandler.getInstance().getObject(d.doorId, d.x, d.y, d.z);
		if (g != null) {
			ObjectHandler.getInstance().removeDoorClip(d.x, d.y, d.z, g.getDef().getFace());
		}
		CacheObject c = ObjectLoader.object(d.doorId, d.x, d.y, d.z);
		if (c != null) {
			ObjectHandler.getInstance().removeDoorClip(d.x, d.y, d.z, c.getRotation());
		}
		if (!d.open) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				xAdjustment = -1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				yAdjustment = 1;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				xAdjustment = +1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				yAdjustment = -1;
			}
		} else if (d.open) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				xAdjustment = 1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				xAdjustment = -1;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				yAdjustment = -1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				xAdjustment = -1;
			}
		}
		if (xAdjustment != 0 || yAdjustment != 0) {
		    ObjectHandler.getInstance().removeObject(d.x, d.y, d.z, 0);
			new GameObject(Constants.EMPTY_OBJECT, d.x, d.y, d.z, 0, 0, Constants.EMPTY_OBJECT, 999999999);
		}
		if (d.x == d.originalX && d.y == d.originalY) {
			d.x += xAdjustment;
			d.y += yAdjustment;
		} else {
		    ObjectHandler.getInstance().removeObject(d.x, d.y, d.z, 0);
			new GameObject(Constants.EMPTY_OBJECT, d.x, d.y, d.z, 0, 0, Constants.EMPTY_OBJECT, 999999999);
			d.x = d.originalX;
			d.y = d.originalY;
		}
		if (changeId) {
			if (d.doorId == d.originalId) {
				if (!d.open) {
					d.doorId += 1;
				} else if (d.open) {
					d.doorId -= 1;
				}
			} else if (d.doorId != d.originalId) {
				if (!d.open) {
					d.doorId = d.originalId;
				} else if (d.open) {
					d.doorId = d.originalId;
				}
			}
		}
		ObjectHandler.getInstance().removeObject(d.x, d.y, d.z, 0);
		new GameObject(d.doorId, d.x, d.y, d.z, getNextRightFace(d), 0, Constants.EMPTY_OBJECT, 999999999);
	}

	private static int getNextRightFace(DoubleDoors d) {
		int f = d.originalFace;
		if (!d.open) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				f = 1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				f = 2;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				f = 3;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				f = 2;
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
		d.currentFace = f;
		return f;
	}

	private int doorId;
	private int originalId;
	private boolean open;
	private int x;
	private int y;
	private int z;
	private int originalX;
	private int originalY;
	private int currentFace;
	private int originalFace;

	public boolean isOpenDoor(int id) {
		for (int i = 0; i < openDoors.length; i++) {
			if (id == openDoors[i] || id + 3 == openDoors[i]) {
				return true;
			}
		}
		return false;
	}

	// Have not found any others yet. Maybe only 1 type of double
	// doors exist to operate.
	private static int[] openDoors = {1520, 1517};

	public DoubleDoors(int id, int x, int y, int z) {
		this.doorId = id;
		this.originalId = id;
		this.open = isOpenDoor(id);
		this.x = x;
		this.originalX = x;
		this.y = y;
		this.z = z;
		this.originalY = y;
		this.currentFace = SkillHandler.getFace(id, x, y, z);
		this.originalFace = this.currentFace;
	}

}
