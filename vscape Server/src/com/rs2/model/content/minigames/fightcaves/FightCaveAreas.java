package com.rs2.model.content.minigames.fightcaves;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rs2.model.Position;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 5/17/12 Time: 7:01 PM To change
 * this template use File | Settings | File Templates.
 */
public class FightCaveAreas {
	public static final MinigameAreas.Area NORTH_WEST = new MinigameAreas.Area(new Position(2378, 5103, 0), new Position(2382, 5108, 0));
	public static final MinigameAreas.Area CENTER = new MinigameAreas.Area(new Position(2396, 5085, 0), new Position(2402, 5090, 0));
	public static final MinigameAreas.Area EAST = new MinigameAreas.Area(new Position(2415, 5079, 0), new Position(2420, 5079, 0));
	public static final MinigameAreas.Area SOUTH = new MinigameAreas.Area(new Position(2396, 5069, 0), new Position(2403, 5074, 0));
	public static final MinigameAreas.Area SOUTH_WEST = new MinigameAreas.Area(new Position(2377, 5067, 0), new Position(2383, 5072, 0));

	public static final Position NORTH_WEST_CENTER = new Position(2381, 5105, 0);
	public static final Position CENTER_CENTER = new Position(2398, 5087, 0);
	public static final Position EAST_CENTER = new Position(2418, 5081, 0);
	public static final Position SOUTH_CENTER = new Position(2400, 5074, 0);
	public static final Position SOUTH_WEST_CENTER = new Position(2381, 5070, 0);

	public static Map<Position, MinigameAreas.Area> bindedAreas = new HashMap<Position, MinigameAreas.Area>();

	public static MinigameAreas.Area getClosestArea(Position position) {
		int[] distances = {Misc.getDistance(position, NORTH_WEST_CENTER), Misc.getDistance(position, CENTER_CENTER), Misc.getDistance(position, EAST_CENTER), Misc.getDistance(position, SOUTH_CENTER), Misc.getDistance(position, SOUTH_WEST_CENTER)};
		int correctIndex = 0;
		int distance = distances[0];
		for (int i = 0; i < distances.length; i++) {
			if (distance > distances[i]) {
				distance = distances[i];
				correctIndex = i;
			}
		}
		switch (correctIndex) {
			case 0 :
				return NORTH_WEST;
			case 1 :
				return CENTER;
			case 2 :
				return EAST;
			case 3 :
				return SOUTH;
		}
		return SOUTH_WEST;
	}

    public static boolean isInFightCaves(Player player){
        return true;
    }

    public static MinigameAreas.Area getRandomSpawningArea(Position playerPosition){
        ArrayList<MinigameAreas.Area> areas = new ArrayList<MinigameAreas.Area>();
        areas.add(NORTH_WEST);
        areas.add(CENTER);
        areas.add(EAST);
        areas.add(SOUTH);
        areas.add(SOUTH_WEST);
        areas.remove(getClosestArea(playerPosition));
        return areas.get(Misc.random(areas.size() - 1));
    }
}
