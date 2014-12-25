package com.rs2.model.content.minigames.magetrainingarena;

import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 01/01/12 Time: 00:47 To change
 * this template use File | Settings | File Templates.
 */
public class EnchantingChamber {

    private static final Position[] ENTERING_POSITION = {new Position(3373, 9640, 0), new Position(3374, 9639, 0), new Position(3375, 9640, 0), new Position(3374, 9641, 0), new Position(3362, 9650, 0), new Position(3363, 9651, 0), new Position(3364, 9650, 0), new Position(3363, 9649, 0), new Position(3363, 9628, 0), new Position(3364, 9629, 0), new Position(3363, 9630, 0), new Position(3362, 9629, 0), new Position(3354, 9640, 0), new Position(3353, 9639, 0), new Position(3352, 9640, 0), new Position(3353, 9641, 0)};

    private Player player;

    private int tempPizzazPoint;
    private int enchantPizazzPoint;
    private int[] enchantSpellsUsed = new int[6];
    private static ArrayList<GroundItem> DRAGONSTONES = new ArrayList<>();

    public static Npc guardian() {
	for (Npc npc : World.getNpcs()) {
	    if (npc == null) {
		continue;
	    }
	    if (npc.getDefinition().getId() == 3100) {
		return npc;
	    }
	}
	return null;
    }

    public EnchantingChamber(Player player) {
	this.player = player;
    }

    private static final String[] color = {"red", "yellow", "green", "blue"};

    private static final int RED = 6901;
    private static final int YELLOW = 6899;
    private static final int GREEN = 6898;
    private static final int BLUE = 6900;
    private static final int ORB = 6902;
    private static final int DRAGONSTONE = 6903;
    
    private static final MinigameAreas.Area CHAMBER = new MinigameAreas.Area(3341, 3386, 9616, 9662, 0);

    private static String bonusItem = "red";

    static Random random = new Random();

    /* loading the enchanting chamber part */
    public static void loadEnchantingChamber() {
	//loadInitialVariables();
	loadEnchantingEvent();
    }

    private static void loadInitialVariables() {
	bonusItem = "red";
    }

    /* loading the enchanting chamber event */
    private static void loadEnchantingEvent() {
	World.submit(new Tick(40) { // 24 seconds
	    @Override
	    public void execute() {
		bonusItem = color[random.nextInt(color.length)];
		for (Player player : World.getPlayers()) {
		    if (player == null) {
			continue;
		    }
		    if (player.getEnchantingChamber().isInEnchantingChamber()) {
			player.getEnchantingChamber().showInterfaceComponent(bonusItem);
		    }
		}
		if (guardian() != null) {
		    guardian().getUpdateFlags().sendForceMessage("The color shape is now " + bonusItem + "!");
		}
	    }
	});
    }

    public boolean isInEnchantingChamber() {
	return player.inEnchantingChamber();
    }

    public void loadInterfacePlayer() {
	player.getActionSender().sendWalkableInterface(15917);
	player.getActionSender().sendString("" + (enchantPizazzPoint + tempPizzazPoint), 15921);
    }

    public void showInterfaceComponent(String bonusItem) {
	removeInterfaceComponent();
	if (bonusItem.equals("red")) {
	    player.getActionSender().sendInterfaceHidden(0, 15924);
	} else if (bonusItem.equals("yellow")) {
	    player.getActionSender().sendInterfaceHidden(0, 15922);
	} else if (bonusItem.equals("green")) {
	    player.getActionSender().sendInterfaceHidden(0, 15923);
	} else if (bonusItem.equals("blue")) {
	    player.getActionSender().sendInterfaceHidden(0, 15925);
	}
    }

    public void removeInterfaceComponent() {
	player.getActionSender().sendInterfaceHidden(1, 15924);
	player.getActionSender().sendInterfaceHidden(1, 15922);
	player.getActionSender().sendInterfaceHidden(1, 15923);
	player.getActionSender().sendInterfaceHidden(1, 15925);
    }

    public boolean handleObjectClicking(int objectId) {
	switch (objectId) {
	    case 10802:// red pile
	    case 10799:// yellow pile
	    case 10800:// green pile
	    case 10801:// blue pile
		addCorrespondingItem(objectId);
		return true;
	    case 10782:// exiting object
		if (isInEnchantingChamber()) {
		    exit();
		}
		return true;
	    case 10779:// entering object
		enter();
		return true;
	    case 10803:// deposit orb object
		depositOrbs();
		return true;
	}
	return false;
    }

    /* adding the corresponding item for the object id */
    private void addCorrespondingItem(final int objectId) {
	if (player.getInventory().getItemContainer().freeSlots() <= 0) {
	    player.getActionSender().sendMessage("Not enough space in your inventory.");
	    return;
	}
	player.getUpdateFlags().sendAnimation(832, 0);
	player.getInventory().addItem(new Item(correspondingItem(objectId)));
    }

    /* gets the correspondint item for an object id */
    private static int correspondingItem(int objectId) {
	switch (objectId) {
	    case 10802:// red pile
		return RED;
	    case 10799:// yellow pile
		return YELLOW;
	    case 10800:// green pile
		return GREEN;
	    case 10801:// blue pile
		return BLUE;
	}
	return 0;
    }

    private static String getStringById(int i) {
	if (i == RED) {
	    return "red";
	}
	if (i == YELLOW) {
	    return "yellow";
	}
	if (i == GREEN) {
	    return "green";
	}
	if (i == BLUE) {
	    return "blue";
	}
	return "";
    }

    private void enter() {
	if (player.getSkill().getLevel()[Skill.MAGIC] < MageGameConstants.ENCHANTING_LEVEL) {
	    player.getActionSender().sendMessage("You need a magic level of " + MageGameConstants.ENCHANTING_LEVEL + " to enter here.");
	    return;
	}
	player.getActionSender().sendMessage("You've entered the Enchantment Chamber.");
	int number = random.nextInt(ENTERING_POSITION.length);
	player.teleport(ENTERING_POSITION[number]);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		if(!isInEnchantingChamber()) {
		    b.stop();
		} else {
		    player.getActionSender().sendMessage("You hear the tinkle of a gem hit the floor.");
		    GroundItem drop = new GroundItem(new Item(DRAGONSTONE), player, player, MinigameAreas.randomPosition(CHAMBER));
		    GroundItemManager.getManager().dropItem(drop);
		    DRAGONSTONES.add(drop);
		}
	    }

	    @Override
	    public void stop() {
		if (!DRAGONSTONES.isEmpty()) {
		    for (GroundItem g : DRAGONSTONES) {
			if (g != null) {
			    GroundItemManager.getManager().destroyItem(g);
			}
		    }
		}
	    }
	}, 300);
    }

    public void exit() {
	player.getActionSender().sendMessage("You've left the Enchantment Chamber.");
	player.getActionSender().sendWalkableInterface(-1);
	player.teleport(MageGameConstants.LEAVING_POSITION);
	removeItems();
	enchantPizazzPoint += tempPizzazPoint;
	tempPizzazPoint = 0;
	player.getActionSender().sendWalkableInterface(-1);
	player.setEnchantingPizazz(player.getEnchantingPizazz() + enchantPizazzPoint);
	DRAGONSTONES.clear();
    }

    /* removing the minigame items */
    private void removeItems() {
	player.getInventory().removeItem(new Item(RED, player.getInventory().getItemAmount(RED)));
	player.getInventory().removeItem(new Item(YELLOW, player.getInventory().getItemAmount(YELLOW)));
	player.getInventory().removeItem(new Item(GREEN, player.getInventory().getItemAmount(GREEN)));
	player.getInventory().removeItem(new Item(BLUE, player.getInventory().getItemAmount(BLUE)));
	player.getInventory().removeItem(new Item(ORB, player.getInventory().getItemAmount(ORB)));
	player.getInventory().removeItem(new Item(DRAGONSTONE, player.getInventory().getItemAmount(DRAGONSTONE)));
    }

    /* getting the points given with the spell id */
    private static int getPointsBySpell(int spellId) {
	switch (spellId) {
	    case 1155:// lv 1
		return 2;
	    case 1165:// lv 2
		return 4;
	    case 1176:// lv 3
		return 6;
	    case 1180:// lv 4
		return 8;
	    case 1187:// lv 5
		return 10;
	    case 6003:// lv 6
		return 12;
	}
	return 0;
    }

    private static int getExtraPointsByIndex(int index) {
	switch (index) {
	    case 0:// lv 1
		return 1;
	    case 1:// lv 2
		return 2;
	    case 2:// lv 3
		return 3;
	    case 3:// lv 4
		return 4;
	    case 4:// lv 5
		return 5;
	    case 5:// lv 6
		return 6;
	}
	return 0;
    }

    /* getting the enchanting index */
    private static int getEnchantingIndex(int spellId) {
	return getPointsBySpell(spellId) / 2 - 1;
    }

    /* enchanting an item in the minigame */
    public void enchantItem(int spellId, int itemId) {
	/* checks if the item enchanted can be enchanted */
	if (itemId != RED && itemId != YELLOW && itemId != GREEN && itemId != BLUE && itemId != DRAGONSTONE) {
	    return;
	}
	if (itemId == DRAGONSTONE) {
	    /* check if the max point is reached or not */
	    if ((enchantPizazzPoint + tempPizzazPoint) <= MageGameConstants.MAX_ENCHANTING_POINT) {
		tempPizzazPoint += getPointsBySpell(spellId);
	    } else {
		tempPizzazPoint = MageGameConstants.MAX_ENCHANTING_POINT - enchantPizazzPoint;
	    }
	} else {
	    /* if the player is enchanting the item shown as bonus */
	    if (bonusItem.equals(getStringById(itemId)) && (enchantPizazzPoint + tempPizzazPoint) <= MageGameConstants.MAX_ENCHANTING_POINT) {
		player.getActionSender().sendMessage("You recieve 1 bonus point!");
		enchantPizazzPoint += 1;
	    }
	    /*
	     * extra points handling : win points depending on the enchanting
	     * point used: will be added when deposit orb
	     */
	    enchantSpellsUsed[getEnchantingIndex(spellId)] += 1;
	}
	/* standard enchanting methods */
	player.getInventory().removeItem(new Item(itemId));
	player.getInventory().addItem(new Item(ORB));
    }

    /* deposit the orbs into the hole */
    private void depositOrbs() {
	if (!player.getInventory().getItemContainer().contains(ORB)) {
	    player.getActionSender().sendMessage("You don't have any orbs to deposit.");
	    return;
	}
	int count = player.getInventory().getItemContainer().getCount(ORB);
	int extraPoints = 0;
	// checks if the max point is reached or not
	if ((enchantPizazzPoint + tempPizzazPoint) <= MageGameConstants.MAX_ENCHANTING_POINT) {
	    tempPizzazPoint += Math.floor(count / 10) * 10;
	    for (int i = 0; i < enchantSpellsUsed.length; i++) {
		while (enchantSpellsUsed[i] - 10 >= 0) {
		    enchantSpellsUsed[i] -= 10;
		    tempPizzazPoint += getExtraPointsByIndex(i);
		    extraPoints += getExtraPointsByIndex(i);
		}
	    }
	} else {
	    tempPizzazPoint = MageGameConstants.MAX_ENCHANTING_POINT - enchantPizazzPoint;
	}
	player.getDialogue().sendStatement("You've just deposited " + count + " orbs, earning you " + (int) (Math.floor(count / 10) * 10) + " Enchanting Pizazz", "Points and " + extraPoints + " extra points for the enchanting spell used.");
	resetEnchantingSpells();
	player.getInventory().removeItem(new Item(ORB, player.getInventory().getItemAmount(ORB)));
	player.getUpdateFlags().sendAnimation(832, 0);

    }

    /* reset the enchanting spells after depositing orbs */
    private void resetEnchantingSpells() {
	for (int i = 0; i < enchantSpellsUsed.length; i++) {
	    enchantSpellsUsed[i] = 0;
	}
    }

}
