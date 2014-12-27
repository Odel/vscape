package com.rs2.model.content.minigames.magetrainingarena;

import com.rs2.model.Position;
import com.rs2.model.World;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.BOTTOM_LEFT_CORNER;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.BOTTOM_RIGHT_CORNER;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.CENTER_SQUARE;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.EXIT_POSITIONS;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.MAZE_POSITIONS;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.STATUE_POSITIONS;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.UPPER_LEFT_CORNER;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.UPPER_RIGHT_CORNER;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class TelekineticTheatre {

    public static final int STATUE = 6888;

    public GroundItem STATUE_ITEM;
    public GameObject EXIT_OBJECT;
    public Npc GUARDIAN;
    private Player player;
    private int pizzazPoint;
    private int mazeIndex = 0;
    public int z;

    public TelekineticTheatre(Player player) {
	this.player = player;
    }

    /* loading the telekinetic theatre minigame */
    public static void loadTelekineticTheatre() {
	loadCenterSquares();
    }

    /* calculating the position of the center spot of the whole maze */
    private static void loadCenterSquares() {
	for (int i = 0; i < MAZE_POSITIONS.length - 1; i++) {
	    CENTER_SQUARE[i] = getCenterPosition(BOTTOM_LEFT_CORNER[i], UPPER_RIGHT_CORNER[i]);
	}
    }

    /* putting the camera on the maze */
    public void loadCamera() {
	player.getActionSender().resetCamera();
	//player.getActionSender().spinCamera(CENTER_SQUARE[mazeIndex].getX(), CENTER_SQUARE[mazeIndex].getY(), 0001, 0001, 0001);
	//player.getActionSender().stillCamera(CENTER_SQUARE[mazeIndex].getX(), CENTER_SQUARE[mazeIndex].getY(), 0001, 0001, 0001);
	//player.getActionSender().sendMessage("" + CENTER_SQUARE[mazeIndex]);
	//FUCK
    }

    public void enter() {
	if (player.getSkill().getLevel()[Skill.MAGIC] < MageGameConstants.TELEKINETIC_LEVEL) {
	    player.getActionSender().sendMessage("You need a magic level of " + MageGameConstants.TELEKINETIC_LEVEL + " to enter here.");
	    return;
	}
	this.mazeIndex = Misc.randomMinusOne(MAZE_POSITIONS.length);
	z = (player.getIndex() * 4) + MAZE_POSITIONS[mazeIndex].getZ();
	player.teleport(MAZE_POSITIONS[mazeIndex].modifyZ(z));
	player.getActionSender().sendMessage("You've entered the Telekinetic Theatre.");
	spawnGuardian();
	EXIT_OBJECT = new GameObject(10782, EXIT_POSITIONS[mazeIndex].getX(), EXIT_POSITIONS[mazeIndex].getY(), z, 0, 10, 0, 999999, false);
	GroundItem drop = new GroundItem(new Item(STATUE), player, player, STATUE_POSITIONS[mazeIndex].modifyZ(z));
	GroundItemManager.getManager().dropItem(drop);
	STATUE_ITEM = drop;
    }

    public void saveVariables() {
	player.setTelekineticPizazz(player.getTelekineticPizazz());
    }

    public void exit() {
	player.teleport(MageGameConstants.LEAVING_POSITION);
	player.getActionSender().sendMessage("You've left the Telekinetic Theatre.");
	player.getActionSender().sendWalkableInterface(-1);
	removeItems();
	saveVariables();
    }

    public void removeItems() {
	if (EXIT_OBJECT != null) {
	    ObjectHandler.getInstance().removeObject(EXIT_OBJECT.oX, EXIT_OBJECT.oY, z, 10);
	    EXIT_OBJECT = null;
	}
	if (STATUE_ITEM != null) {
	    GroundItemManager.getManager().destroyItem(STATUE_ITEM);
	    STATUE_ITEM = null;
	}
	if (GUARDIAN != null) {
	    System.out.println("guardian removed at " + GUARDIAN.getPosition());
	    NpcLoader.destroyNpc(GUARDIAN);
	    GUARDIAN = null;
	}
    }

    public void spawnGuardian() {
	Position spawningPosition = MageGameConstants.GUARDIAN_POSITIONS[mazeIndex].modifyZ(z);
	GUARDIAN = new Npc(3098);
	GUARDIAN.setPosition(spawningPosition);
	GUARDIAN.setSpawnPosition(spawningPosition);
	GUARDIAN.setWalkType(Npc.WalkType.WALK);
	GUARDIAN.setMaxWalk(new Position(spawningPosition.getX() + 5, spawningPosition.getY() + 5, z));
	GUARDIAN.setMaxWalk(new Position(spawningPosition.getX() - 5, spawningPosition.getY() - 5, z));
	GUARDIAN.setCurrentX(spawningPosition.getX());
	GUARDIAN.setCurrentY(spawningPosition.getY());
	GUARDIAN.setNeedsRespawn(false);
	World.register(GUARDIAN);
    }

    /* gets the center position with a square diagonal provided */
    private static Position getCenterPosition(Position p1, Position p2) {
	int x1 = p1.getX();
	int y1 = p1.getY();
	int x2 = p2.getX();
	int y2 = p2.getY();
	return new Position(((x1 + x2) / 2) + 1, ((y1 + y2) / 2) + 1);
    }

    @SuppressWarnings("unused")
    private String getDirectionToMoveStatue(int mazeIndex) {
	int x = player.getPosition().getX();
	int y = player.getPosition().getY();

	if (x == BOTTOM_LEFT_CORNER[mazeIndex].getX()) {
	    if (y > BOTTOM_LEFT_CORNER[mazeIndex].getY() && y < UPPER_LEFT_CORNER[mazeIndex].getY()) {
		return "WEST";
	    }
	}
	if (x == BOTTOM_RIGHT_CORNER[mazeIndex].getX()) {
	    if (y > BOTTOM_RIGHT_CORNER[mazeIndex].getY() && y < UPPER_RIGHT_CORNER[mazeIndex].getY()) {
		return "EAST";
	    }
	}
	if (y == UPPER_LEFT_CORNER[mazeIndex].getY()) {
	    if (x > UPPER_LEFT_CORNER[mazeIndex].getX() && x < UPPER_RIGHT_CORNER[mazeIndex].getX()) {
		return "NORTH";
	    }
	}
	if (y == BOTTOM_LEFT_CORNER[mazeIndex].getY()) {
	    if (x > BOTTOM_LEFT_CORNER[mazeIndex].getX() && x < BOTTOM_RIGHT_CORNER[mazeIndex].getX()) {
		return "SOUTH";
	    }
	}
	return "";
    }

    public boolean isInTelekineticTheatre() {
	return false;
    }

    public boolean handleObjectClicking(final int objectId, final int x, final int y, final int h) {
	if (objectId == 10778) {
	    enter();
	    return true;
	}
	if (objectId == 10782 && player.inTelekineticTheatre()) {
	    exit();
	    return true;
	}
	return false;
    }

}
