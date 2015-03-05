package com.rs2.model.content.minigames.magetrainingarena;

import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;

public class MageRewardHandling {

    public static final int INTERFACE = 15944;
    public static final int ITEM_GROUP = 15948;

    public static final int TELEKINETIC = 15955;
    public static final int ENCHANTING = 15956;
    public static final int ALCHEMY = 15957;
    public static final int GRAVEYARD = 15958;

    public static Container stock = new Container(Container.Type.ALWAYS_STACK, 24);

    public static final Item[] REWARD_ITEMS = {new Item(6908, 100), new Item(6910, 100), new Item(6912, 100), new Item(6914, 100), new Item(6918, 100), new Item(6916, 100), new Item(6924, 100), new Item(6920, 100), new Item(6922, 100), new Item(6889, 100), new Item(6926, 100), new Item(4695, 100), new Item(4696, 100), new Item(4698, 100), new Item(4697, 100), new Item(4694, 100), new Item(4699, 100), new Item(564, 100), new Item(562, 100), new Item(561, 100), new Item(560, 100), new Item(563, 100), new Item(566, 100), new Item(565, 100)};

    static {
	for (RewardItem r : RewardItem.values()) {
	    stock.add(new Item(r.itemId, 100));
	}
    }

    public static void openInterface(final Player player) {
	player.getActionSender().sendInterface(INTERFACE, 3822);
	player.getInventory().refresh(3823);
	player.getActionSender().sendUpdateItems(ITEM_GROUP, stock.toArray());
	player.getAttributes().put("isShopping", Boolean.TRUE);
	player.getActionSender().sendString("" + player.getTelekineticPizazz(), TELEKINETIC);
	player.getActionSender().sendString("" + player.getEnchantingPizazz(), ENCHANTING);
	player.getActionSender().sendString("" + player.getAlchemistPizazz(), ALCHEMY);
	player.getActionSender().sendString("" + player.getGraveyardPizazz(), GRAVEYARD);
    }

    public static void getBuyValue(Player player, int id) {
	RewardItem item = RewardItem.forId(id);
	if (item == null) {
	    return;
	}
	//int price = item.getPrice();
	player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": currently costs " + item.getTelePoints() + " Telekinetic, " + item.getAlchPoints() + " Alchemist, " + item.getEnchantPoints() + " Enchanting,", true);
	player.getActionSender().sendMessage("and " + item.getGraveyardPoints() + " Graveyard Pizazz Point(s).", true);
    }

    public static void buyItem(Player player, int slot, int shopItem) {
	Container inventory = player.getInventory().getItemContainer();
	Item item = stock.get(slot);
	if (shopItem < 0 || item == null || !item.validItem()) {
	    return;
	}
	if (shopItem != item.getId()) {
	    return;
	}
	RewardItem rewardItem = RewardItem.forId(item.getId());
	if (rewardItem == null) {
	    return;
	}
	int telePizazz = rewardItem.getTelePoints();
	int alchPizazz = rewardItem.getAlchPoints();
	int enchantPizazz = rewardItem.getEnchantPoints();
	int graveyardPizazz = rewardItem.getGraveyardPoints();
	boolean canBuy = false;
	if (player.getTelekineticPizazz() >= telePizazz && player.getAlchemistPizazz() >= alchPizazz && player.getEnchantingPizazz() >= enchantPizazz && player.getGraveyardPizazz() >= graveyardPizazz) {
	    canBuy = true;
	}
	if (!canBuy) {
	    player.getActionSender().sendMessage("You do not have enough Pizazz Points to purchase this item.");
	    return;
	}
	if(shopItem == 6926) {
	    player.getActionSender().sendMessage("You have unlocked the spell Bones to Peaches.");
	    player.setBonesToPeachesEnabled(true);
	} else if (shopItem == 6910 && !player.getInventory().playerHasItem(6908)) {
	    player.getActionSender().sendMessage("You need to have the Beginner wand in order to purchase this.");
	    return;
	} else if (shopItem == 6912 && !player.getInventory().playerHasItem(6910)) {
	    player.getActionSender().sendMessage("You need to have the Apprentice wand in order to purchase this.");
	    return;
	} else if (shopItem == 6914 && !player.getInventory().playerHasItem(6912)) {
	    player.getActionSender().sendMessage("You need to have the Teacher wand in order to purchase this.");
	    return;
	}
	int freeSpace = inventory.freeSlots();
	if ((!item.getDefinition().isStackable() || !player.getInventory().playerHasItem(shopItem)) && freeSpace < 1) {
	    player.getActionSender().sendMessage("Not enough space in your inventory.");
	    return;
	}
	if(shopItem != 6926) {
	    player.getInventory().addItem(new Item(shopItem, 1));
	}
	if(shopItem == 6910) {
		player.getInventory().removeItem(new Item(6908, 1));
	} else if(shopItem == 6912) {
		player.getInventory().removeItem(new Item(6910, 1));
	} else if(shopItem == 6914) {
		player.getInventory().removeItem(new Item(6912, 1));
	}
	stock.set(slot, new Item(shopItem, item.getCount() - 1));
	player.getInventory().refresh(3823);
	player.getActionSender().sendUpdateItems(ITEM_GROUP, stock.toArray());
	player.setTelekineticPizazz(player.getTelekineticPizazz() - telePizazz);
	player.setAlchemistPizazz(player.getAlchemistPizazz() - alchPizazz);
	player.setEnchantingPizazz(player.getEnchantingPizazz() - enchantPizazz);
	player.setGraveyardPizazz(player.getGraveyardPizazz() - graveyardPizazz);
	player.getActionSender().sendString("" + player.getTelekineticPizazz(), TELEKINETIC);
	player.getActionSender().sendString("" + player.getEnchantingPizazz(), ENCHANTING);
	player.getActionSender().sendString("" + player.getAlchemistPizazz(), ALCHEMY);
	player.getActionSender().sendString("" + player.getGraveyardPizazz(), GRAVEYARD);
    }

    public enum RewardItem {
	
	BEGINNERS_WAND(6908, 30, 30, 300, 30),
	APPRENTICE_WAND(6910, 60, 60, 600, 60),
	TEACHER_WAND(6912, 150, 200, 1500, 150),
	MASTER_WAND(6914, 240, 240, 2400, 240),
	INFINITY_HAT(6918, 350, 400, 3000, 350),
	INFINITY_TOP(6916, 400, 450, 4000, 400),
	INFINITY_BOTTOMS(6924, 450, 500, 5000, 450),
	INFINITY_BOOTS(6920, 120, 120, 1200, 120),
	INFINITY_GLOVES(6922, 175, 225, 1500, 175),
	MAGES_BOOK(6889, 500, 550, 6000, 500),
	BONES_TO_PEACHES(6926, 200, 300, 2000, 200),
	MIST_RUNE(4695, 1, 1, 15, 1),
	DUST_RUNE(4696, 1, 1, 15, 1),
	MUD_RUNE(4698, 1, 1, 15, 1),
	SMOKE_RUNE(4697, 1, 1, 15, 1),
	STEAM_RUNE(4694, 1, 1, 15, 1),
	LAVA_RUNE(4699, 1, 1, 15, 1),
	COSMIC_RUNE(564, 0, 0, 5, 0),
	CHAOS_RUNE(562, 0, 1, 5, 1),
	NATURE_RUNE(561, 0, 1, 0, 1),
	DEATH_RUNE(560, 2, 1, 20, 1),
	LAW_RUNE(563, 2, 0, 0, 0),
	SOUL_RUNE(566, 2, 2, 25, 2),
	BLOOD_RUNE(565, 2, 2, 25, 2);

	public int itemId;
	public int telePoints;
	public int alchPoints;
	public int enchantPoints;
	public int graveyardPoints;

	RewardItem(int id, int tele, int alch, int enchant, int grave) {
	    this.itemId = id;
	    this.telePoints = tele;
	    this.alchPoints = alch;
	    this.enchantPoints = enchant;
	    this.graveyardPoints = grave;
	}

	public static RewardItem forId(int itemId) {
	    for (RewardItem r : RewardItem.values()) {
		if (r.itemId == itemId) {
		    return r;
		}
	    }
	    return null;
	}

	public int getTelePoints() {
	    return this.telePoints;
	}

	public int getAlchPoints() {
	    return this.alchPoints;
	}

	public int getEnchantPoints() {
	    return this.enchantPoints;
	}

	public int getGraveyardPoints() {
	    return this.graveyardPoints;
	}
    }

}
