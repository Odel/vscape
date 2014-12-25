package com.rs2.model.content.minigames.magetrainingarena;

import java.util.Random;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.Tick;

public class AlchemistPlayground {
    //todo green arrow for free alch

    private Player player;

    private int coinReward;
    private int alchPizzazPoint;

    public static Npc guardian() {
	for (Npc npc : World.getNpcs()) {
	    if (npc == null) {
		continue;
	    }
	    if (npc.getDefinition().getId() == 3099) {
		return npc;
	    }
	}
	return null;
    }

    public AlchemistPlayground(Player player) {
	this.player = player;
    }

    /* the default locations */
    private static final Position[] ENTERING_POSITION = {new Position(3363, 9623, 2), new Position(3366, 9623, 2)};

    /* the price table */
    private static int[] price = {30, 15, 8, 5, 1};

    /* the random instance */
    static Random random = new Random();

    /* set of constants determining the different item ids */
    private static final int boots = 6893;
    private static final int adamantKite = 6894;
    private static final int adamantMedHelm = 6895;
    private static final int emerald = 6896;
    private static final int runeLongSword = 6897;

    /* setting up the different objects */
    private static int runeLongSwordObject;
    private static int emeraldObject;
    private static int adamantMedHelmObject;
    private static int adamantKiteObject;
    private static int bootsObject;

    /* Setting up the different prices */
    private static int runeLongSwordPrice;
    private static int emeraldPrice;
    private static int adamantMedHelmPrice;
    private static int adamantKitePrice;
    private static int bootsPrice;

    /* Loading the minigame itself */
    public static void loadAlchemistPlayGround() {
	loadInitialVariables();
	launchRotateEvent();
    }
    public static boolean isAlchemistPlaygroundItem(int id) {
	return id == boots || id == adamantKite || id == adamantMedHelm || id == emerald || id == runeLongSword;
    }
    /* the initial variable for each object and prices */
    private static void loadInitialVariables() {
	bootsObject = 10791;
	adamantKiteObject = 10789;
	adamantMedHelmObject = 10787;
	emeraldObject = 10785;
	runeLongSwordObject = 10783;
	bootsPrice = 30;
	adamantKitePrice = 15;
	adamantMedHelmPrice = 8;
	emeraldPrice = 5;
	runeLongSwordPrice = 1;

    }

    /* the rotating object and price event */
    private static void launchRotateEvent() { // todo make this an event instead
	// of a tick on your server
	World.submit(new Tick(70) { // 42 seconds
	    @Override
	    public void execute() {
		rotateObjects();
		updatePrices();
	    }
	});
    }

    /* the enter and exit for the minigame */
    private void enter() {
	int number = random.nextInt(ENTERING_POSITION.length);
	if (player.getSkill().getLevel()[Skill.MAGIC] < MageGameConstants.ALCHEMIST_LEVEL) {
	    player.getActionSender().sendMessage("You need a magic level of " + MageGameConstants.ALCHEMIST_LEVEL + " to enter here.");
	    return;
	}
	if (player.getInventory().getItemContainer().contains(995)) {
	    player.getActionSender().sendMessage("You can't take coins in the Alchemists' Playground.");
	    return;
	}
	player.teleport(ENTERING_POSITION[number]);
	player.getActionSender().sendMessage("You've entered the Alchemists' Playground.");

    }

    public void exit() {
	int count = player.getInventory().getItemContainer().getCount(995);
	player.getActionSender().sendWalkableInterface(-1);
	removeItems(player);
	player.getInventory().removeItem(new Item(995, count));
	if (coinReward != 0) {
	    player.getInventory().addItem(new Item(995, coinReward));
	}
	player.teleport(MageGameConstants.LEAVING_POSITION);
	player.getActionSender().sendMessage("You've left the Alchemists' Playground " + (coinReward != 0 ? "and you get some coins as reward" : "") + ".");
	coinReward = 0;

    }

    /* removing player minigame items */
    private static void removeItems(Player player) {
	player.getInventory().removeItem(new Item(boots, 28));
	player.getInventory().removeItem(new Item(adamantKite, 28));
	player.getInventory().removeItem(new Item(adamantMedHelm, 28));
	player.getInventory().removeItem(new Item(runeLongSword, 28));
	player.getInventory().removeItem(new Item(emerald, 28));
    }

    /* rotating the item contained in each cupboard */
    private static void rotateObjects() {
	runeLongSwordObject = increaseObject(runeLongSwordObject);
	emeraldObject = increaseObject(emeraldObject);
	adamantMedHelmObject = increaseObject(adamantMedHelmObject);
	adamantKiteObject = increaseObject(adamantKiteObject);
	bootsObject = increaseObject(bootsObject);
	Npc guardian = guardian();
	if (guardian != null) {
	    guardian().getUpdateFlags().sendForceMessage("The costs are changing!");
	}
    }

    /* increasing the object id for each items -used for rotating objects */
    private static int increaseObject(int objectId) {
	int i = objectId;
	if (i == 10197) {
	    i = 10783;
	} else {
	    i += 2;
	}
	return i;
    }

    /* updating the item prices mathematically :D */
    private static void updatePrices() {
	int[] objects = new int[5];
	int i = random.nextInt(4);// objects.length-1
	// looping in order to browse all items
	for (int j = 0; j < 5; j++) {
	    objects[i] = price[j];
	    // increasing the index i
	    if (i <= 0) {
		i += 4;
	    } else {
		i--;
	    }
	}
	setPrices(objects);
    }

    /* setting prices for objects */
    private static void setPrices(int[] objects) {
	runeLongSwordPrice = objects[0];
	emeraldPrice = objects[1];
	adamantMedHelmPrice = objects[2];
	adamantKitePrice = objects[3];
	bootsPrice = objects[4];
    }

    public boolean isInAlchemistPlayGround() {
	return player.inAlchemistPlayground();
    }

    /* loading the interface for a single player */
    public void loadInterfacePlayer() {
	player.getActionSender().sendWalkableInterface(15892);
	player.getActionSender().sendString("" + alchPizzazPoint, 15896);// pizzaz
	player.getActionSender().sendString("" + bootsPrice, 15902);// leatherBoot
	player.getActionSender().sendString("" + adamantKitePrice, 15903);// adamant																	// kiteshield
	player.getActionSender().sendString("" + adamantMedHelmPrice, 15904);// adamant																	// med
	player.getActionSender().sendString("" + emeraldPrice, 15905);// emerald
	player.getActionSender().sendString("" + runeLongSwordPrice, 15906);// rune
    }

    public boolean handleObjectClicking(int objectId, int objectX, int objectY) { // todo
	if (objectId == bootsObject || objectId == adamantKiteObject || objectId == adamantMedHelmObject || objectId == emeraldObject || objectId == runeLongSwordObject) {
	    if (isInAlchemistPlayGround()) {
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
		    player.getActionSender().sendMessage("Not enough space in your inventory.");
		    return true;
		}
		int itemId = correspondingItem(objectId);
		player.getInventory().addItem(new Item(itemId));
		player.getUpdateFlags().sendAnimation(832, 0);
		player.getActionSender().sendMessage("You found : " + ItemManager.getInstance().getItemName(itemId) + ".");
		handleCupBoard(objectId, objectX, objectY);
		return true;
	    }
	} else if (objectId == 10734) {// coin collector
	    if (isInAlchemistPlayGround()) {
		reward();
	    }
	    return true;
	} else if (objectId == 10782) {// leaving alchemist playground
	    if (isInAlchemistPlayGround()) {
		exit();
	    }
	    return false;
	} else if (objectId == 10780) {// entering alchemist playground
	    enter();
	    return true;
	} else if (isInAlchemistPlayGround()) {
	    player.getActionSender().sendMessage("The cupboard is empty.");
	    return true;
	}
	return false;

    }

    /* handle the cupboard opening and closing */
    private void handleCupBoard(final int objectId, final int objectX, final int objectY) {
	new GameObject(objectId + 1, objectX, objectY, 0, objectId < 10791 ? 1 : 3, 10, objectId, 5);
    }

    /* get the corresponding item of an object */
    private static int correspondingItem(int objectId) {
	if (objectId == bootsObject) {
	    return boots;
	}
	if (objectId == adamantKiteObject) {
	    return adamantKite;
	}
	if (objectId == adamantMedHelmObject) {
	    return adamantMedHelm;
	}
	if (objectId == emeraldObject) {
	    return emerald;
	}
	if (objectId == runeLongSwordObject) {
	    return runeLongSword;
	}

	return -1;
    }

    /* get the corresponding price of an item */
    private static int correspondingPrice(int itemId) {
	if (itemId == boots) {
	    return bootsPrice;
	}
	if (itemId == adamantKite) {
	    return adamantKitePrice;
	}
	if (itemId == adamantMedHelm) {
	    return adamantMedHelmPrice;
	}
	if (itemId == emerald) {
	    return emeraldPrice;
	}
	if (itemId == runeLongSword) {
	    return runeLongSwordPrice;
	}
	return 0;
    }

    /* handles the item alching during the minigame */
    public void alchItem(int itemId) {
	if (correspondingPrice(itemId) == 0) {
	    return;
	}
	player.getInventory().removeItem(new Item(itemId));
	player.getInventory().addItem(new Item(995, correspondingPrice(itemId)));
    }

    /* rewarding the player in the coins collector */
    private void reward() {
	if (!player.getInventory().getItemContainer().contains(995)) {
	    player.getActionSender().sendMessage("You don't have any coins to deposit.");
	    return;
	}
	int count = player.getInventory().getItemContainer().getCount(995);
	if (count > 12000) {
	    count = 12000; // cannot deposit more than 12k coins
	}
	player.getSkill().addExp(Skill.MAGIC, count * 2);
	int reward = (int) Math.floor(count / 100);
	if (alchPizzazPoint + reward > MageGameConstants.MAX_ALCHEMY_POINT) {
	    reward = MageGameConstants.MAX_ALCHEMY_POINT - alchPizzazPoint;
	}
	alchPizzazPoint += reward;
	coinReward += reward * 10;
	player.getInventory().removeItem(new Item(995, count));
	player.getUpdateFlags().sendAnimation(832, 0);

	player.getActionSender().sendChatInterface(363);
	player.getActionSender().sendString("You've just deposited " + count + " coins, earning you " + reward + " Alchemist Pizazz", 364);
	player.getActionSender().sendString("Points and " + count * 2 + " magic XP. So far you're taking " + coinReward + " coins as a", 365);
	player.getActionSender().sendString("reward when you leave!", 366);
    }

}
