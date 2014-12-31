package com.rs2.model.content.minigames.magetrainingarena;

import com.rs2.model.Position;
import com.rs2.model.World;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.BOTTOM_LEFT_CORNER;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.BOTTOM_RIGHT_CORNER;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.CENTER_SQUARE;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.EXIT_POSITIONS;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.MAZE_POSITIONS;
import static com.rs2.model.content.minigames.magetrainingarena.MageGameConstants.STATUE_ARRIVAL_POSITIONS;
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
import com.rs2.model.players.Player.LoginStages;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import java.util.ArrayList;

public class TelekineticTheatre {

    public static final int STATUE = 6888;

    private Player player;
    public GroundItem STATUE_ITEM;
    public GameObject EXIT_OBJECT;
    public Npc GUARDIAN;
    public Npc MAZE_GUARDIAN;
    public ArrayList<Integer> mazeIndexesCompleted = new ArrayList<>();
    
    private int pizazzPoints;
    private int mazeIndex = 0;
    private int mazesCompleted = 0;
    public int z;
    public boolean mazeComplete = false;

    public TelekineticTheatre(Player player) {
	this.player = player;
    }

    public static void loadTelekineticTheatre() {
	loadCenterSquares();
    }

    private static void loadCenterSquares() {
	for (int i = 0; i < MAZE_POSITIONS.length - 1; i++) {
	    CENTER_SQUARE[i] = getCenterPosition(BOTTOM_LEFT_CORNER[i], UPPER_RIGHT_CORNER[i]);
	}
    }

    public void loadCamera() {
	player.getActionSender().resetCamera();
	//player.getActionSender().spinCamera(CENTER_SQUARE[mazeIndex].getX(), CENTER_SQUARE[mazeIndex].getY(), 0001, 0001, 0001);
	//player.getActionSender().stillCamera(CENTER_SQUARE[mazeIndex].getX(), CENTER_SQUARE[mazeIndex].getY(), 0001, 0001, 0001);
	//player.getActionSender().sendMessage("" + CENTER_SQUARE[mazeIndex]);
	//FUCK
    }
    
    public void resetStatue() {
	GroundItemManager.getManager().destroyItem(STATUE_ITEM);
	STATUE_ITEM = new GroundItem(new Item(STATUE), player, player, STATUE_POSITIONS[mazeIndex].modifyZ(z));
	GroundItemManager.getManager().dropItem(STATUE_ITEM);
    }

    public void enter() {
	if (player.getSkill().getLevel()[Skill.MAGIC] < MageGameConstants.TELEKINETIC_LEVEL) {
	    player.getActionSender().sendMessage("You need a magic level of " + MageGameConstants.TELEKINETIC_LEVEL + " to enter here.");
	    return;
	}
	loadInterfacePlayer();
	if(mazeIndexesCompleted.size() >= 10) {
	    mazeIndexesCompleted.clear();
	    this.mazeIndex = Misc.randomMinusOne(MAZE_POSITIONS.length);
	} else {
	    int index = Misc.randomMinusOne(MAZE_POSITIONS.length);
	    while(mazeIndexesCompleted.contains(index)) {
		index = Misc.randomMinusOne(MAZE_POSITIONS.length);
	    }
	    this.mazeIndex = index;
	}
	z = (player.getIndex() * 4) + MAZE_POSITIONS[mazeIndex].getZ();
	player.teleport(MAZE_POSITIONS[mazeIndex].modifyZ(z));
	player.getActionSender().sendMessage("You've entered the Telekinetic Theatre.");
	spawnGuardian();
	EXIT_OBJECT = new GameObject(10782, EXIT_POSITIONS[mazeIndex].getX(), EXIT_POSITIONS[mazeIndex].getY(), z, 0, 10, 0, 999999, false);
	STATUE_ITEM = new GroundItem(new Item(STATUE), player, player, STATUE_POSITIONS[mazeIndex].modifyZ(z));
	GroundItemManager.getManager().dropItem(STATUE_ITEM);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		if(!player.inTelekineticTheatre() || mazeComplete) {
		    b.stop();
		} else {
		    if(STATUE_ITEM == null) {
			STATUE_ITEM = new GroundItem(new Item(STATUE), player, player, STATUE_POSITIONS[mazeIndex].modifyZ(z));
			GroundItemManager.getManager().dropItem(STATUE_ITEM);
		    } else if(!GroundItemManager.getManager().itemExists(player, STATUE_ITEM)) {
			GroundItemManager.getManager().dropItem(STATUE_ITEM);
		    }
		}
	    }

	    @Override
	    public void stop() {
	    }
	}, 2);
    }

    public void saveVariables() {
	player.setTelekineticPizazz(player.getTelekineticPizazz() + pizazzPoints);
	player.setTelekineticMazesCompleted(player.getTelekineticMazesCompleted() + mazesCompleted);
	pizazzPoints = 0;
	mazesCompleted = 0;
	if(!mazeComplete) {
	    mazeIndexesCompleted.clear();
	}
	mazeComplete = false;
    }

    public void exit() {
	player.teleport(MageGameConstants.LEAVING_POSITION);
	player.getActionSender().sendMessage("You've left the Telekinetic Theatre.");
	player.getActionSender().sendWalkableInterface(-1);
	removeItems();
	saveVariables();
	player.setTelekineticMazesCompleted(0);
    }
    
    public void newMaze() {
	removeItems();
	saveVariables();
	enter();
    }

    public void removeItems() {
	if (player != null && player.getLoginStage().equals(LoginStages.LOGGED_IN)) {
	    if (EXIT_OBJECT != null) {
		ObjectHandler.getInstance().removeObject(EXIT_OBJECT.oX, EXIT_OBJECT.oY, z, 10);
		EXIT_OBJECT = null;
	    }
	    if (STATUE_ITEM != null) {
		GroundItemManager.getManager().destroyItem(STATUE_ITEM);
		STATUE_ITEM = null;
	    }
	    if (GUARDIAN != null) {
		NpcLoader.destroyNpc(GUARDIAN);
		GUARDIAN = null;
	    }
	    if (MAZE_GUARDIAN != null) {
		NpcLoader.destroyNpc(MAZE_GUARDIAN);
		MAZE_GUARDIAN = null;
	    }
	}
    }
    
    public void loadInterfacePlayer() {
	player.getActionSender().sendWalkableInterface(15962);
	player.getActionSender().sendString("" + (player.getTelekineticPizazz() + pizazzPoints), 15966);
	player.getActionSender().sendString("" + (player.getTelekineticMazesCompleted() + mazesCompleted), 15968);
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
    
    public void spawnMazeGuardian() {
	Position spawningPosition = STATUE_ARRIVAL_POSITIONS[mazeIndex].modifyZ(z);
	MAZE_GUARDIAN = new Npc(3102);
	MAZE_GUARDIAN.setPosition(spawningPosition);
	MAZE_GUARDIAN.setSpawnPosition(spawningPosition);
	MAZE_GUARDIAN.setWalkType(Npc.WalkType.STAND);
	MAZE_GUARDIAN.setCurrentX(spawningPosition.getX());
	MAZE_GUARDIAN.setCurrentY(spawningPosition.getY());
	MAZE_GUARDIAN.setNeedsRespawn(false);
	World.register(MAZE_GUARDIAN);
    }

    /* gets the center position with a square diagonal provided */
    private static Position getCenterPosition(Position p1, Position p2) {
	int x1 = p1.getX();
	int y1 = p1.getY();
	int x2 = p2.getX();
	int y2 = p2.getY();
	return new Position(((x1 + x2) / 2) + 1, ((y1 + y2) / 2) + 1);
    }

    private String getDirectionToMoveStatue() {
	int x = player.getPosition().getX();
	int y = player.getPosition().getY();

	if (x == BOTTOM_LEFT_CORNER[mazeIndex].getX() || x + 1 == BOTTOM_LEFT_CORNER[mazeIndex].getX()) {
	    if (y > BOTTOM_LEFT_CORNER[mazeIndex].getY() && y < UPPER_LEFT_CORNER[mazeIndex].getY()) {
		return "WEST";
	    }
	}
	if (x == BOTTOM_RIGHT_CORNER[mazeIndex].getX() || x - 1 == BOTTOM_RIGHT_CORNER[mazeIndex].getX()) {
	    if (y > BOTTOM_RIGHT_CORNER[mazeIndex].getY() && y < UPPER_RIGHT_CORNER[mazeIndex].getY()) {
		return "EAST";
	    }
	}
	if (y == UPPER_LEFT_CORNER[mazeIndex].getY() || y - 1 == UPPER_LEFT_CORNER[mazeIndex].getY()) {
	    if (x > UPPER_LEFT_CORNER[mazeIndex].getX() && x < UPPER_RIGHT_CORNER[mazeIndex].getX()) {
		return "NORTH";
	    }
	}
	if (y == BOTTOM_LEFT_CORNER[mazeIndex].getY() || y + 1 == BOTTOM_LEFT_CORNER[mazeIndex].getY()) {
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
    
    public void handleTelegrab(final Position itemPos) {
	String direction = getDirectionToMoveStatue();
	if(direction.equals("")) {
	    player.getDialogue().sendStatement("You need to get to a closer, more appropriate spot to move the Statue.");
	} else {
	    moveStatue(direction, itemPos);
	}
    }
    
    public void moveStatue(String direction, Position startPos) {
	GroundItemManager.getManager().destroyItem(STATUE_ITEM);
	switch(direction) {
	    case "WEST":
		Position endW = findEndPosition(-1, 0, startPos);
		STATUE_ITEM = new GroundItem(new Item(STATUE), player, player, endW);
		GroundItemManager.getManager().dropItem(STATUE_ITEM);
		break;
	    case "EAST":
		Position endE = findEndPosition(1, 0, startPos);
		STATUE_ITEM = new GroundItem(new Item(STATUE), player, player, endE);
		GroundItemManager.getManager().dropItem(STATUE_ITEM);
		break;
	    case "NORTH":
		Position endN = findEndPosition(0, 1, startPos);
		STATUE_ITEM = new GroundItem(new Item(STATUE), player, player, endN);
		GroundItemManager.getManager().dropItem(STATUE_ITEM);
		break;
	    case "SOUTH":
		Position endS = findEndPosition(0, -1, startPos);
		STATUE_ITEM = new GroundItem(new Item(STATUE), player, player, endS);
		GroundItemManager.getManager().dropItem(STATUE_ITEM);
		break;
	}
	if (STATUE_ITEM.getPosition().equals(STATUE_ARRIVAL_POSITIONS[mazeIndex].modifyZ(z))) {
	    reward();
	}
    }
    
    public Position findEndPosition(int x, int y, Position startPos) {
	Position currentPos = startPos;
	Position checkPos = new Position(currentPos.getX() + x, currentPos.getY() + y, currentPos.getZ());
	while (Misc.checkClip(currentPos, checkPos, true)) {
	    currentPos = checkPos;
	    checkPos = new Position(currentPos.getX() + x, currentPos.getY() + y, currentPos.getZ());
	}
	return currentPos;
    }
    
    public void reward() {
	mazeComplete = true;
	mazeIndexesCompleted.add(mazeIndex);
	if(((player.getTelekineticMazesCompleted() + mazesCompleted) + 1)%5 == 0) {
	    player.setTelekineticMazesCompleted(0);
	    mazesCompleted = 0;
	    pizazzPoints += 8;
	    player.getInventory().addItemOrDrop(new Item(563, 10));
	    player.getSkill().addExp(Skill.MAGIC, 1000);
	} else  {
	    mazesCompleted++;
	    pizazzPoints += 2;
	}
	loadInterfacePlayer();
	GroundItemManager.getManager().destroyItem(STATUE_ITEM);
	STATUE_ITEM = null;
	spawnMazeGuardian();
    }

}
