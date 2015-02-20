package com.rs2.model.content.minigames.barrows;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;
import java.util.ArrayList;

public class Barrows {
    public Player player;
    public ArrayList<Npc> npcsSpawned = new ArrayList<>();
    private boolean northSpawn = true;
    private boolean westSpawn = true;
    private boolean eastSpawn = true;
    private boolean southSpawn = true;
    private boolean northWestSpawn = true;
    private boolean northEastSpawn = true;
    private boolean southWestSpawn = true;
    private boolean southEastSpawn = true;

    private static final int[] Items = {4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 7462, 7462};
    private static final int[][] Stackables = {{4740, 5}, {558, 25}, {562, 11}, {560, 5}, {565, 2}, {995, 55}};
    private static final int[] Monsters = {2031, 2033, 2034, 2035, 2037};
    
    private static final MinigameAreas.Area CENTER = new MinigameAreas.Area(3546, 3557, 9689, 9700, 0);
    private static final MinigameAreas.Area NORTH = new MinigameAreas.Area(3546, 3557, 9706, 9717, 0);
    private static final MinigameAreas.Area NORTHWEST = new MinigameAreas.Area(3529, 3540, 9706, 9717, 0);
    private static final MinigameAreas.Area NORTHEAST = new MinigameAreas.Area(3563, 3574, 9706, 9717, 0);
    private static final MinigameAreas.Area WEST = new MinigameAreas.Area(3529, 3540, 9689, 9700, 0);
    private static final MinigameAreas.Area EAST = new MinigameAreas.Area(3563, 3574, 9689, 9700, 0);
    private static final MinigameAreas.Area SOUTH = new MinigameAreas.Area(3546, 3557, 9672, 9683, 0);
    private static final MinigameAreas.Area SOUTHWEST = new MinigameAreas.Area(3529, 3540, 9672, 9683, 0);
    private static final MinigameAreas.Area SOUTHEAST = new MinigameAreas.Area(3563, 3574, 9672, 9683, 0);
    public static final MinigameAreas.Area[] AREAS = {NORTH, WEST, EAST, SOUTH, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST, CENTER};

    public Barrows(Player player) {
	this.player = player;
    }

    public enum BarrowsBrother {
	AHRIM(0, 6821, 2025),
	DHAROK(1, 6771, 2026),
	GUTHAN(2, 6773, 2027),
	KARIL(3, 6822, 2028),
	TORAG(4, 6772, 2029),
	VERAC(5, 6823, 2030);

	private int index;
	private int objectId;
	private int npcId;

	BarrowsBrother(int index, int objectId, int npcId) {
	    this.index = index;
	    this.objectId = objectId;
	    this.npcId = npcId;
	}

	public static BarrowsBrother forCryptId(int objectId) {
	    for (BarrowsBrother brother : BarrowsBrother.values()) {
		if (objectId == brother.objectId) {
		    return brother;
		}
	    }
	    return null;
	}

	public static BarrowsBrother forNpcId(int npcId) {
	    for (BarrowsBrother brother : BarrowsBrother.values()) {
		if (npcId == brother.npcId) {
		    return brother;
		}
	    }
	    return null;
	}

	public static BarrowsBrother forIndex(int index) {
	    for (BarrowsBrother brother : BarrowsBrother.values()) {
		if (index == brother.index) {
		    return brother;
		}
	    }
	    return null;
	}
    }

    public static int random(int range) {
	if (range == 6 || range < 0) {
	    int number = (int) Math.ceil(Math.random() * 20);
	    return number == 1 ? 0 : number;
	} else if (range > 0) {
	    int number = (int) Math.ceil(Math.random() * (range - 2));
	    return number == 1 ? 0 : number;

	} else {
	    return -1;
	}
    }

    public boolean handleBarrowsObject(Player player, int objectId) {
	switch (objectId) {
	    case 10284: // chest
		if (player.getKillCount() < 1) {
		    player.getActionSender().sendMessage("You search the chest but don't find anything.");
		    player.getTeleportation().teleport(3565, 3298, 0, true);
		    return true;
		} else {
		    BarrowsBrother brother = BarrowsBrother.forIndex(player.getRandomGrave());
		    if (brother != null) {
			if (NpcLoader.checkSpawn(player, brother.npcId)) {
			    player.getActionSender().sendMessage("You must kill the the brother before searching this.");
			    return true;
			}
			if (player.getBarrowsNpcDead(player.getRandomGrave())) {
			    getReward(player);
			    return true;
			}
			if (player.getSpawnedNpc() == null || player.getSpawnedNpc().getNpcId() != brother.npcId && !player.getBarrowsNpcDead(brother.index)) {
			    NpcLoader.spawnNpc(player, new Npc(brother.npcId), true, true);
			    return true;
			}
		    }
		}
		return true;
	    case 6823:
	    case 6772:
	    case 6821:
	    case 6771:
	    case 6773:
	    case 6822:
		BarrowsBrother brother = BarrowsBrother.forCryptId(objectId);
		if (brother != null) {
		    if (brother.index == player.getRandomGrave()) {
			Dialogues.startDialogue(player, 10001);
			return true;
		    }
		    if (NpcLoader.checkSpawn(player, brother.npcId)) {
			player.getActionSender().sendMessage("You must kill the the brother before searching this.");
			return true;
		    }
		    if (player.getBarrowsNpcDead()[brother.index]) {
			player.getActionSender().sendMessage("You have already searched this sarcophagus.");
			return true;
		    }
		    if (player.getSpawnedNpc() == null || player.getSpawnedNpc().getNpcId() != brother.npcId && !player.getBarrowsNpcDead()[brother.index]) {
			NpcLoader.spawnNpc(player, new Npc(brother.npcId), true, true);
		    }
		    if (brother.index != player.getRandomGrave()) {
			player.getActionSender().sendMessage("You don't find anything.");
			return true;
		    }
		}
		return true;
	    case 6707: // verac stairs
		player.teleport(new Position(3556, 3298, 0));
		return true;
	    case 6706: // torag stairs
		player.teleport(new Position(3553, 3283, 0));
		break;
	    case 6705: // karil stairs
		player.teleport(new Position(3565, 3276, 0));
		return true;
	    case 6704: // guthan stairs
		player.teleport(new Position(3578, 3284, 0));
		return true;
	    case 6703: // dharok stairs
		player.teleport(new Position(3574, 3298, 0));
		return true;
	    case 6702: // ahrim stairs
		player.teleport(new Position(3565, 3290, 0));
		return true;
	}
	return false;
    }

    public static boolean digCrypt(Player player) {
	if (player.Area(3553, 3561, 3294, 3301)) {
	    player.teleport(new Position(3578, 9706, 3));
	    player.getActionSender().sendMessage("You've broken into a crypt!");
	    player.getActionSender().sendMapState(2);
	    return true;
	} else if (player.Area(3550, 3557, 3278, 3287)) {
	    player.teleport(new Position(3568, 9683, 3));
	    player.getActionSender().sendMessage("You've broken into a crypt!");
	    player.getActionSender().sendMapState(2);
	    return true;
	} else if (player.Area(3561, 3568, 3285, 3292)) {
	    player.teleport(new Position(3557, 9703, 3));
	    player.getActionSender().sendMessage("You've broken into a crypt!");
	    player.getActionSender().sendMapState(2);
	    return true;
	} else if (player.Area(3570, 3579, 3293, 3302)) {
	    player.teleport(new Position(3556, 9718, 3));
	    player.getActionSender().sendMessage("You've broken into a crypt!");
	    player.getActionSender().sendMapState(2);
	    return true;
	} else if (player.Area(3571, 3582, 3278, 3285)) {
	    player.teleport(new Position(3534, 9704, 3));
	    player.getActionSender().sendMessage("You've broken into a crypt!");
	    player.getActionSender().sendMapState(2);
	    return true;
	} else if (player.Area(3562, 3569, 3273, 3279)) {
	    player.teleport(new Position(3546, 9684, 3));
	    player.getActionSender().sendMessage("You've broken into a crypt!");
	    player.getActionSender().sendMapState(2);
	    return true;
	}
	return false;
    }

    public void getReward(Player player) {
	final int number = Misc.randomMinusOne(Stackables.length);
	final int rune = Stackables[number][0];
	final int amount = Stackables[number][1];
	final int reward = Items[Misc.randomMinusOne(Items.length)];
	int kills = player.getKillCount();
	if (kills < 1) {
	    player.getActionSender().sendMessage("You can only loot the chest after killing at least 1 brother.");
	    return;
	}
	boolean getBarrows = random(126 - (kills * 8)) == 0;
	boolean getBarrows2 = (Misc.random(24) == 0 && brotherKillCount(player) == 6);
	if (getBarrows || getBarrows2) {
	    if (player.getInventory().getItemContainer().freeSlots() == 1) {
		if (!player.getInventory().playerHasItem(rune)) {
		    player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
		    return;
		}
	    } else if (player.getInventory().getItemContainer().freeSlots() < 1) {
		player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
		return;
	    }
	    player.getInventory().addItem(new Item(rune, Misc.random(amount * kills) + 1));
	    player.getInventory().addItem(new Item(reward, 1));
	} else {
	    final int number2 = Misc.randomMinusOne(Stackables.length);
	    final int rune2 = Stackables[number2][0];
	    final int amount2 = Stackables[number2][1];
	    if (player.getInventory().getItemContainer().freeSlots() < 1) {
		if (!player.getInventory().playerHasItem(rune) || !player.getInventory().playerHasItem(rune2)) {
		    player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
		    return;
		}
	    }
	    if (player.getInventory().getItemContainer().freeSlots() == 1) {
		if (!player.getInventory().playerHasItem(rune) && !player.getInventory().playerHasItem(rune2)) {
		    player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
		    return;
		}
	    }
	    player.getInventory().addItem(new Item(rune, Misc.random(amount * kills) + 1));
	    player.getInventory().addItem(new Item(rune2, Misc.random(amount2 * kills) + 1));
	}
	player.getUpdateFlags().sendAnimation(714);
	player.getUpdateFlags().sendHighGraphic(301);
	player.getTeleportation().teleport(3565, 3298, 0, true);
	player.getActionSender().sendMessage("You grab the loot and teleport away.");
	resetBarrows(player);
	return;
    }

    public BarrowsBrother getBrotherForArea(MinigameAreas.Area area) {
	if (area == SOUTHEAST) {
	    return BarrowsBrother.VERAC;
	} else if (area == SOUTH) {
	    return BarrowsBrother.KARIL;
	} else if (area == WEST) {
	    return BarrowsBrother.DHAROK;
	} else if (area == EAST) {
	    return BarrowsBrother.GUTHAN;
	} else if (area == NORTH) {
	    return BarrowsBrother.AHRIM;
	} else if (area == NORTHWEST) {
	    return BarrowsBrother.TORAG;
	} else {
	    return null;
	}
    }

    public boolean getSpawnBoolForAreaIndex(int index) {
	switch (index) {
	    case 0:
		return northSpawn;
	    case 1:
		return westSpawn;
	    case 2:
		return eastSpawn;
	    case 3:
		return southSpawn;
	    case 4:
		return northWestSpawn;
	    case 5:
		return northEastSpawn;
	    case 6:
		return southWestSpawn;
	    case 7:
		return southEastSpawn;
	}
	return false;
    }

    public void setSpawnBoolFalseForAreaIndex(int index) {
	switch (index) {
	    case 0:
		northSpawn = false;
		return;
	    case 1:
		westSpawn = false;
		return;
	    case 2:
		eastSpawn = false;
		return;
	    case 3:
		southSpawn = false;
		return;
	    case 4:
		northWestSpawn = false;
		return;
	    case 5:
		northEastSpawn = false;
		return;
	    case 6:
		southWestSpawn = false;
		return;
	    case 7:
		southEastSpawn = false;
		return;
	}
    }

    public boolean isEWDoor(int id) {
	switch (id) {
	    case 6717:
	    case 6719:
	    case 6721:
	    case 6723:
	    case 6724:
	    case 6725:
	    case 6729:
	    case 6730:
	    case 6736:
	    case 6738:
	    case 6740:
	    case 6742:
	    case 6743:
	    case 6744:
	    case 6748:
	    case 6749:
		return true;
	}
	return false;
    }

    public boolean isNSDoor(int id) {
	switch (id) {
	    case 6716:
	    case 6718:
	    case 6720:
	    case 6722:
	    case 6726:
	    case 6727:
	    case 6728:
	    case 6731:
	    case 6735:
	    case 6737:
	    case 6739:
	    case 6741:
	    case 6745:
	    case 6746:
	    case 6747:
	    case 6750:
		return true;
	}
	return false;
    }
    public static boolean inBarrowsCrypts(Entity entity) {
	return entity.Area(3523, 3581, 9666, 9724);
    }
    
    public boolean handleBarrowsDoors(Player player, int objectId, int x, int y) {
	final int spawn1 = Monsters[Misc.randomMinusOne(Monsters.length)];
	final int spawn2 = Monsters[Misc.randomMinusOne(Monsters.length)];
	final int spawn3 = Monsters[Misc.randomMinusOne(Monsters.length)];

	if (player.Area(3523, 3581, 9666, 9724) && (isEWDoor(objectId) || isNSDoor(objectId))) {
	    for (int i = 0; i < 9; ++i) {
		MinigameAreas.Area area = AREAS[i];
		int playerCoord = isEWDoor(objectId) ? player.getPosition().getX() : player.getPosition().getY();
		int areaCoord = isEWDoor(objectId) ? area.getNorthEastCorner().getX() : area.getNorthEastCorner().getY();
		if (MinigameAreas.isInArea(player.getPosition(), area)) {
		    int walk = playerCoord > areaCoord - 5 ? 1 : -1;
		    player.getActionSender().walkTo(isEWDoor(objectId) ? walk : 0, isNSDoor(objectId) ? walk : 0, true);
		    player.getActionSender().walkThroughDoor(objectId, x, y, 0);
		    return true;
		} else if (!MinigameAreas.isInArea(player.getPosition(), area) && MinigameAreas.isInArea(player.getPosition(), area.enlargeNew(1))) {
		    int walk = playerCoord > areaCoord ? -1 : 1;
		    player.getActionSender().walkTo(isEWDoor(objectId) ? walk : 0, isNSDoor(objectId) ? walk : 0, true);
		    player.getActionSender().walkThroughDoor(objectId, x, y, 0);
		    if (area != CENTER && getSpawnBoolForAreaIndex(i)) {
			BarrowsBrother brother = getBrotherForArea(area);
			if (brother != null && !player.getBarrowsNpcDead()[brother.index]) {
			    spawn(player, spawn1, area, i, false);
			    spawn(player, spawn2, area, i, false);
			    NpcLoader.spawnPlayerOwnedAttackNpc(player, new Npc(brother.npcId), area.center(), false, null);
			    setSpawnBoolFalseForAreaIndex(i);
			    return true;
			} else {
			    spawn(player, spawn1, area, i, true);
			    spawn(player, spawn2, area, i, true);
			    spawn(player, spawn3, area, i, true);
			    setSpawnBoolFalseForAreaIndex(i);
			    return true;
			}
		    }
		}
	    }
	    return true;
	} else {
	    return false;
	}
    }

    public void spawn(Player player, int npcId, MinigameAreas.Area area, int areaIndex, boolean attack) {
	Position position = MinigameAreas.randomPosition(area);
	Npc npc = NpcLoader.spawnBasicNpc(player, npcId, position, attack);
	npcsSpawned.add(npc);
    }

    private static int brotherKillCount(Player player) {
	int brotherKillCount = 0;
	for (boolean kill : player.getBarrowsNpcDead()) {
	    if (kill) {
		brotherKillCount++;
	    }
	}
	return brotherKillCount;
    }

    public void resetBarrows(Player player) {
	for (int x = 0; x < 6; x++) {
	    player.setBarrowsNpcDead(x, false);
	}
	player.setKillCount(0);
	player.setRandomGrave(Misc.random(5));
	player.getActionSender().resetCamera();
	northSpawn = true;
	westSpawn = true;
	eastSpawn = true;
	southSpawn = true;
	northWestSpawn = true;
	northEastSpawn = true;
	southWestSpawn = true;
	southEastSpawn = true;
	for(Npc npc : npcsSpawned) {
	    NpcLoader.destroyNpc(npc);
	}
    }

    public void handleDeath(Npc npc) {
		BarrowsBrother brother = BarrowsBrother.forNpcId(npc.getNpcId());
		if (brother != null) {
		    player.setBarrowsNpcDead(brother.index, true);
		    if (player.getSpawnedNpc() == npc) {
		    	player.setSpawnedNpc(null);
		    }
		    player.setKillCount(player.getKillCount() + 1);
		}
		if (monsterDeath(player, npc)) {
		    player.setKillCount(player.getKillCount() + 1);
		}
		player.getActionSender().sendString("Kill count: " + player.getKillCount(), 4536);
    }

    public static boolean monsterDeath(Player player, Npc npc) {
	if (npc.getNpcId() == 2031) {
	    return true;
	} else if (npc.getNpcId() == 2033) {
	    return true;
	} else if (npc.getNpcId() == 2034) {
	    return true;
	} else if (npc.getNpcId() == 2035) {
	    return true;
	} else if (npc.getNpcId() == 2037) {
	    return true;
	} else {
	    return false;
	}
    }
}
