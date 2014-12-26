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
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;

public class AlchemistPlayground {
    //todo green arrow for free alch

    private Player player;

    private int coinReward;
    private int alchPizazzPoint;
    private static int cycleSynchTicks;

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
    private static final int LEATHER_BOOTS = 6893;
    private static final int ADAMANT_KITE = 6894;
    private static final int ADAMANT_MED_HELM = 6895;
    private static final int EMERALD = 6896;
    private static final int RUNE_LONGSWORD = 6897;

    /* setting up the different objects */
    private static int RUNE_LONGSWORD_OBJECT;
    private static int EMERALD_OBJECT;
    private static int ADAMANT_MED_HELM_OBJECT;
    private static int ADAMANT_KITE_OBJECT;
    private static int LEATHER_BOOTS_OBJECT;

    /* Loading the minigame itself */
    public static void loadAlchemistPlayGround() {
	loadInitialVariables();
	launchRotateEvent();
    }
    public static boolean isAlchemistPlaygroundItem(int id) {
	return id == LEATHER_BOOTS || id == ADAMANT_KITE || id == ADAMANT_MED_HELM || id == EMERALD || id == RUNE_LONGSWORD;
    }
    /* the initial variable for each object and prices */
    private static void loadInitialVariables() {
	LEATHER_BOOTS_OBJECT = 10791;
	ADAMANT_KITE_OBJECT = 10789;
	ADAMANT_MED_HELM_OBJECT = 10787;
	EMERALD_OBJECT = 10785;
	RUNE_LONGSWORD_OBJECT = 10783;
	MageGameConstants.LEATHER_BOOTS_PRICE = 30;
	MageGameConstants.ADAMANT_KITE_PRICE = 15;
	MageGameConstants.ADAMANT_MED_HELM_PRICE = 8;
	MageGameConstants.EMERALD_PRICE = 5;
	MageGameConstants.RUNE_LONGSWORD_PRICE = 1;

    }

    /* the rotating object and price event */
    private static void launchRotateEvent() {
	World.submit(new Tick(70) { // 42 seconds
	    @Override
	    public void execute() {
		updatePrices();
		for(Player player : World.getPlayers()) {
		    if(player == null) continue;
		    if(player.inAlchemistPlayground()) {
			player.getAlchemistPlayground().rotateObjects();
		    }
		}
	    }
	});
    }

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
	player.getActionSender().sendWalkableInterface(-1);
	removeItems();
	player.teleport(MageGameConstants.LEAVING_POSITION);
	player.getActionSender().sendMessage("You've left the Alchemists' Playground.");
	saveVariables();
    }
    
    public void saveVariables() {
	player.setAlchemistPizazz(player.getAlchemistPizazz() + alchPizazzPoint);
	alchPizazzPoint = 0;
	coinReward = 0;
    }

    public void removeItems() {
	player.getInventory().removeItem(new Item(995, player.getInventory().getItemAmount(995)));
	player.getInventory().removeItem(new Item(LEATHER_BOOTS, player.getInventory().getItemAmount(LEATHER_BOOTS)));
	player.getInventory().removeItem(new Item(ADAMANT_KITE, player.getInventory().getItemAmount(ADAMANT_KITE)));
	player.getInventory().removeItem(new Item(ADAMANT_MED_HELM, player.getInventory().getItemAmount(ADAMANT_MED_HELM)));
	player.getInventory().removeItem(new Item(RUNE_LONGSWORD, player.getInventory().getItemAmount(RUNE_LONGSWORD)));
	player.getInventory().removeItem(new Item(EMERALD, player.getInventory().getItemAmount(EMERALD)));
    }

    /* rotating the item contained in each cupboard */
    private void rotateObjects() {
	RUNE_LONGSWORD_OBJECT = increaseObject(RUNE_LONGSWORD_OBJECT);
	EMERALD_OBJECT = increaseObject(EMERALD_OBJECT);
	ADAMANT_MED_HELM_OBJECT = increaseObject(ADAMANT_MED_HELM_OBJECT);
	ADAMANT_KITE_OBJECT = increaseObject(ADAMANT_KITE_OBJECT);
	LEATHER_BOOTS_OBJECT = increaseObject(LEATHER_BOOTS_OBJECT);
	Npc guardian = guardian();
	if (guardian != null) {
	    guardian().getUpdateFlags().sendForceMessage("The costs are changing!");
	}
    }

    /* increasing the object id for each items -used for rotating objects */
    private static int increaseObject(int objectId) {
	int i = objectId;
	if (i == 10797) {
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
	MageGameConstants.RUNE_LONGSWORD_PRICE = objects[0];
	MageGameConstants.EMERALD_PRICE = objects[1];
	MageGameConstants.ADAMANT_MED_HELM_PRICE = objects[2];
	MageGameConstants.ADAMANT_KITE_PRICE = objects[3];
	MageGameConstants.LEATHER_BOOTS_PRICE = objects[4];
    }

    public boolean isInAlchemistPlayGround() {
	return player.inAlchemistPlayground();
    }

    public void loadInterfacePlayer() {
	player.getActionSender().sendWalkableInterface(15892);
	player.getActionSender().sendString("" + (player.getAlchemistPizazz() + alchPizazzPoint), 15896);
	player.getActionSender().sendString("" + MageGameConstants.LEATHER_BOOTS_PRICE, 15902);
	player.getActionSender().sendString("" + MageGameConstants.ADAMANT_KITE_PRICE, 15903);																	// kiteshield
	player.getActionSender().sendString("" + MageGameConstants.ADAMANT_MED_HELM_PRICE, 15904);																	// med
	player.getActionSender().sendString("" + MageGameConstants.EMERALD_PRICE, 15905);
	player.getActionSender().sendString("" + MageGameConstants.RUNE_LONGSWORD_PRICE, 15906);
    }

    public boolean handleObjectClicking(int objectId, int objectX, int objectY) { // todo
	if (objectId == LEATHER_BOOTS_OBJECT || objectId == ADAMANT_KITE_OBJECT || objectId == ADAMANT_MED_HELM_OBJECT || objectId == EMERALD_OBJECT || objectId == RUNE_LONGSWORD_OBJECT) {
	    if (isInAlchemistPlayGround()) {
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
		    player.getActionSender().sendMessage("Not enough space in your inventory.");
		    return true;
		}
		int itemId = correspondingItem(objectId);
		player.getInventory().addItem(new Item(itemId));
		player.getUpdateFlags().sendAnimation(832, 0);
		if(itemId == LEATHER_BOOTS) {
		    player.getActionSender().sendMessage("You find some " + ItemManager.getInstance().getItemName(itemId).toLowerCase() + ".");
		} else if (itemId == RUNE_LONGSWORD) {
		    player.getActionSender().sendMessage("You find a " + ItemManager.getInstance().getItemName(itemId).toLowerCase() + ".");
		} else {
		    player.getActionSender().sendMessage("You find an " + ItemManager.getInstance().getItemName(itemId).toLowerCase() + ".");
		}
		return true;
	    }
	} else if (objectId == 10734) {
	    if (isInAlchemistPlayGround()) {
		reward();
	    }
	    return true;
	} else if (objectId == 10782) {
	    if (isInAlchemistPlayGround()) {
		exit();
		return true;
	    }
	    return false;
	} else if (objectId == 10780) {
	    enter();
	    return true;
	} else if (isInAlchemistPlayGround()) {
	    player.getActionSender().sendMessage("The cupboard is empty.");
	    return true;
	}
	return false;

    }

    /* handle the cupboard opening and closing 
    private void handleCupBoard(final int objectId, final int objectX, final int objectY) {
	new GameObject(objectId + 1, objectX, objectY, 0, objectId < 10791 ? 1 : 3, 10, objectId, 5);
    }*/

    /* get the corresponding item of an object */
    private static int correspondingItem(int objectId) {
	if (objectId == LEATHER_BOOTS_OBJECT) {
	    return LEATHER_BOOTS;
	}
	if (objectId == ADAMANT_KITE_OBJECT) {
	    return ADAMANT_KITE;
	}
	if (objectId == ADAMANT_MED_HELM_OBJECT) {
	    return ADAMANT_MED_HELM;
	}
	if (objectId == EMERALD_OBJECT) {
	    return EMERALD;
	}
	if (objectId == RUNE_LONGSWORD_OBJECT) {
	    return RUNE_LONGSWORD;
	}
	return -1;
    }

    /* get the corresponding price of an item */
    private static int correspondingPrice(int itemId) {
	if (itemId == LEATHER_BOOTS) {
	    return MageGameConstants.LEATHER_BOOTS_PRICE;
	}
	if (itemId == ADAMANT_KITE) {
	    return MageGameConstants.ADAMANT_KITE_PRICE;
	}
	if (itemId == ADAMANT_MED_HELM) {
	    return MageGameConstants.ADAMANT_MED_HELM_PRICE;
	}
	if (itemId == EMERALD) {
	    return MageGameConstants.EMERALD_PRICE;
	}
	if (itemId == RUNE_LONGSWORD) {
	    return MageGameConstants.RUNE_LONGSWORD_PRICE;
	}
	return 0;
    }

    /* handles the item alching during the minigame */
    public void alchItem(int itemId) {
	if (correspondingPrice(itemId) == 0) {
	    return;
	}
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
	if (alchPizazzPoint + reward > MageGameConstants.MAX_ALCHEMY_POINT) {
	    reward = MageGameConstants.MAX_ALCHEMY_POINT - alchPizazzPoint;
	}
	alchPizazzPoint += reward;
	coinReward += reward * 10;
	player.getInventory().removeItem(new Item(995, count));
	player.getBankManager().add(new Item(995, coinReward));
	player.getUpdateFlags().sendAnimation(832, 0);

	player.getActionSender().sendChatInterface(363);
	player.getDialogue().sendStatement("You've just deposited " + count + " coins, earning you " + reward + " Alchemist Pizazz", "Points and " + count * 4.5 + " magic XP. " + coinReward + " coin(s) have been deposited", "directly to your bank!");
    }

}
