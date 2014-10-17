package com.rs2.model.content.minigames;

import com.rs2.model.Position;
import com.rs2.util.Misc;
import com.rs2.util.clip.Region;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/27/12 Time: 11:41 AM To change
 * this template use File | Settings | File Templates.
 */
public class MinigameAreas {

	public static boolean isInArea(Position position, Area area) {
		return position.getZ() == area.getSouthWestCorner().getZ() && position.getX() >= area.getSouthWestCorner().getX() && position.getY() >= area.getSouthWestCorner().getY() && position.getX() <= area.getNorthEastCorner().getX() && position.getY() <= area.getNorthEastCorner().getY();
	}

	public static boolean isInGlobalArea(Position position, Area area) {
		return position.getX() > area.getSouthWestCorner().getX() && position.getY() > area.getSouthWestCorner().getY() && position.getX() < area.getNorthEastCorner().getX() && position.getY() < area.getNorthEastCorner().getY();
	}

	public static Position randomPosition(Area area) {
		Position finalPosition = new Position(area.getSouthWestCorner().getX() + Misc.random(area.getNorthEastCorner().getX() - area.getSouthWestCorner().getX()), area.getSouthWestCorner().getY() + Misc.random(area.getNorthEastCorner().getY() - area.getSouthWestCorner().getY()), area.getSouthWestCorner().getZ());
		while (Region.getClipping(finalPosition.getX(), finalPosition.getY(), finalPosition.getZ()) != 0)
			finalPosition = new Position(area.getSouthWestCorner().getX() + Misc.random(area.getNorthEastCorner().getX() - area.getSouthWestCorner().getX()), area.getSouthWestCorner().getY() + Misc.random(area.getNorthEastCorner().getY() - area.getSouthWestCorner().getY()), area.getSouthWestCorner().getZ());
		return finalPosition;

	}


	public static class Area {

		private Position southWestCorner;
		private Position northEastCorner;

		public Area(Position southWestCorner, Position northEastCorner) {
			this.southWestCorner = southWestCorner;
			this.northEastCorner = northEastCorner;
		}
		public Area(int x1, int x2, int y1, int y2, int z) {
		    Position swCorner = new Position(x1, y1, z);
		    Position neCorner = new Position(x2, y2, z);
		    this.southWestCorner = swCorner;
		    this.northEastCorner = neCorner;
		}
		public Position getSouthWestCorner() {
			return southWestCorner;
		}
		public Position getNorthEastCorner() {
			return northEastCorner;
		}
		public Position center() {
		    return new Position(((northEastCorner.getX() - southWestCorner.getX()) / 2) + southWestCorner.getX(), ((northEastCorner.getY() - southWestCorner.getY()) / 2) + southWestCorner.getY(), southWestCorner.getZ());
		}
		public Area enlarge(int distance){
			this.southWestCorner = new Position(southWestCorner.getX() - distance, southWestCorner.getY() - distance, southWestCorner.getZ());
			this.northEastCorner = new Position(northEastCorner.getX() + distance, northEastCorner.getY() + distance, northEastCorner.getZ());
			return this;
		}
		public Area enlargeNew(int distance) {
			Area a = new Area(this.southWestCorner, this.northEastCorner);
			a.southWestCorner = new Position(a.southWestCorner.getX() - distance, a.southWestCorner.getY() - distance, southWestCorner.getZ());
			a.northEastCorner = new Position(a.northEastCorner.getX() + distance, a.northEastCorner.getY() + distance, northEastCorner.getZ());
			return a;
		}
	}
}
