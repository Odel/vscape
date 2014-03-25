package com.rs2.model.content.minigames.duelarena;

import com.rs2.model.Position;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.players.Player;
import com.rs2.util.clip.Region;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/24/12 Time: 1:07 PM To change
 * this template use File | Settings | File Templates.
 */
public class DuelAreas {

	@SuppressWarnings("unused")
	private Player player;

	public DuelAreas(Player player) {
		this.player = player;
	}

    public static final MinigameAreas.Area[] OBSTACLE_AREA = new MinigameAreas.Area[]{new MinigameAreas.Area(new Position(3367, 3246, 0), new Position(3385, 3256, 0)), new MinigameAreas.Area(new Position(3336, 3227, 0), new Position(3355, 3237, 0)), new MinigameAreas.Area(new Position(3367, 3208, 0), new Position(3386, 3218, 0))};
    public static final MinigameAreas.Area[] NORMAL_AREA = new MinigameAreas.Area[]{new MinigameAreas.Area(new Position(3337, 3245, 0), new Position(3355, 3256, 0)), new MinigameAreas.Area(new Position(3366, 3227, 0), new Position(3386, 3237, 0)), new MinigameAreas.Area(new Position(3337, 3207, 0), new Position(3354, 3218, 0))};

	public MinigameAreas.Area[] randomPositionSpawn(boolean obstacles) {
		if (obstacles)
			return OBSTACLE_AREA;
		else
			return NORMAL_AREA;
	}


	public Position getNextToPlayerPosition(Position pos) {
		int x = pos.getX();
		int y = pos.getY();
		Position up = new Position(x, y + 1);
		Position down = new Position(x, y - 1);
		Position left = new Position(x - 1, y);
		Position right = new Position(x + 1, y);

		if (Region.getClipping(up.getX(), up.getY(), 0) == 0)
			return up;
		else if (Region.getClipping(down.getX(), down.getY(), 0) == 0)
			return down;
		else if (Region.getClipping(left.getX(), left.getY(), 0) == 0)
			return left;
		else
			return right;

	}

	public Position getRandomDuelPosition() {
		return MinigameAreas.randomPosition(new MinigameAreas.Area(new Position(3356, 3269, 0), new Position(3379, 3280)));
	}

	public Position getRandomArenaPosition(boolean obstacles, int index) {
		return MinigameAreas.randomPosition(randomPositionSpawn(obstacles)[index]);
	}

}
