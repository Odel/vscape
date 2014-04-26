package com.rs2.model.content.skills.herblore;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * @author
 * @Skill: Herblore
 * 
 * @tasks Herb cleaning - Done Making fin potions Making unf potions
 */
public class PotionMaking {// todo description items

	public static final int VIAL_OF_WATER = 227;
	public static final int COCONUT_MILK = 5935;
	private static final int UNFINISHED_ANIMATION = 363;

	public static boolean combineDose(Player c, int itemused1, int itemused2, int slot1, int slot2) {
		Item item1 = new Item(itemused1);
		Item item2 = new Item(itemused2);
		String itemName1 = item1.getDefinition().getName().toLowerCase();
		String itemName2 = item2.getDefinition().getName().toLowerCase();
		if (itemName1.contains("(4)") || itemName2.contains("(4)")) {
			return false;
		}
		try {
			if (itemName1.substring(0, itemName1.indexOf("(")).equalsIgnoreCase(itemName2.substring(0, itemName2.indexOf("(")))) {
				int amount1 = Integer.parseInt(itemName1.substring(itemName1.indexOf("(") + 1, itemName1.indexOf("(") + 2));
				int amount2 = Integer.parseInt(itemName2.substring(itemName2.indexOf("(") + 1, itemName2.indexOf("(") + 2));
				int totalAmount = amount1 + amount2;
				if (!c.getInventory().playerHasItem(item1.getId()) || !c.getInventory().playerHasItem(item2.getId())) {
					return false;
				}
				if (totalAmount > 4) {
					amount1 = 4;
					amount2 = totalAmount - 4;
					String item3 = itemName1.substring(0, itemName1.indexOf("(") + 1) + amount1 + ")";
					String item4 = itemName1.substring(0, itemName1.indexOf("(") + 1) + amount2 + ")";
					c.getInventory().removeItemSlot(slot1);
					c.getInventory().removeItemSlot(slot2);
					c.getInventory().addItemToSlot(new Item(ItemDefinition.getItemId(item3)), slot1);
					c.getInventory().addItemToSlot(new Item(ItemDefinition.getItemId(item4)), slot2);
					return true;
				} else {
					amount1 = totalAmount;
					String item3 = itemName1.substring(0, itemName1.indexOf("(") + 1) + amount1 + ")";
					c.getInventory().removeItemSlot(slot1);
					c.getInventory().removeItemSlot(slot2);
					c.getInventory().addItemToSlot(new Item(ItemDefinition.getItemId(item3)), slot1);
					c.getInventory().addItemToSlot(new Item(229, 1), slot2);
					return true;
				}
			}
			// player.getActionSender().playSound(477, 1, 0);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}

	/**
	 * Potion data.
	 * 
	 * @note herb, ingredient, unfinished, finished, exp, req
	 */
	private static final double[][] POTIONS = {
        {249, 221, 91, 121, 25, 0},             // Attack Potion.
        {251, 235, 93, 175, 37.5, 5},           // Anti-Poison.
        {253, 225, 95, 115, 50, 12},            // Strength Potion.
        {253, 592, 95, 3408, 50, 15},           // Serum 207
        {255, 223, 97, 127, 62.5, 22},          // Restore Potion.
        {255, 1581, 97, 1582, 80, 25},          // Blamish Oil.
        {255, 1975, 97, 3010, 67.5, 26},        // Energy Potion.
        {257, 239, 99, 133, 75, 30},            // Defence Potion.
        {2998, 2152, 3002, 3034, 80, 34},       // Agility Potion.
        {257, 231, 99, 139, 87.5, 38},          // Prayer Potion.
        {259, 221, 101, 145, 100, 45},          // Super Attack Potion.
        {259, 235, 101, 181, 106.3, 48},        // Super Anti-Poison.
        {261, 231, 103, 151, 112.5, 50},        // Fishing Potion.
        {261, 2970, 103, 3018, 117.5, 52},      // Super Energy Potion.
        {263, 225, 105, 157, 125, 55},          // Super Strength Potion.
        {263, 241, 105, 187, 137.5, 60},        // Weapon Poison.
        {3000, 223, 3004, 3026, 142.5, 63}, // Super Restore.
        {265, 239, 107, 163, 150, 66},          // Super Defence.
        {2998, 6049, 5942, 5945, 175, 68},      // antidote+ Potion.
        {267, 245, 109, 169, 162.5, 69},        // Ranging Potion.
        {2481, 241, 2483, 2454, 157.5, 72}, // Antifire.
        {6016, 223, 5936, 5937, 175, 73},       // weapon+ Potion.
        {2481, 3138, 2483, 3042, 172.5, 76},// Magic Potion.
        {269, 247, 111, 189, 175, 78},          // Zamorak Potion.
        {259, 6051, 101, 5954, 175, 79},        // antidote++ Potion.
        {2998, 6693, 3002, 6687, 175, 81},      // sara brew Potion.
        {2398, 6018, 5939, 5940, 175, 82},      // weapon++ Potion.                    
	};

	public static boolean createPotion(Player player, Item useItem, Item withItem, int slot, int slotUsed) {
		int container = VIAL_OF_WATER;
		int item = useItem.getId(), usedItem = withItem.getId(), herb = -1;
		double ingre = -1, unf = -1, fin = -1, exp = -1, req = -1;
		for (double[] data : POTIONS) {
			herb = (int) data[0];
			ingre = data[1];
			unf = data[2];
			fin = data[3];
			exp = data[4];
			req = data[5];
			if (herb == 6016 || ingre == 5106 || ingre == 6049) {
				container = COCONUT_MILK;
			} else {
				container = VIAL_OF_WATER;
			}
			if ((item == herb && usedItem == container) || (item == container && usedItem == herb)) {
				if (player.getSkill().getLevel()[Skill.HERBLORE] < req) {
					player.getDialogue().sendStatement("You need a Herblore level of " + (int) req + " in order to make this potion.");
					return true;
				}
				createUnfinished(player, herb, unf, item == container ? slot : slotUsed, item == container ? slotUsed : slot);
				return true;
			}
			if ((item == unf && usedItem == ingre) || (item == ingre && usedItem == unf)) {
				if (player.getSkill().getLevel()[Skill.HERBLORE] < req) {
					player.getDialogue().sendStatement("You need a Herblore level of " + (int) req + " in order to make this potion.");
					return true;
				}
				createFinished(player, ingre, unf, fin, exp, item == ingre ? slot : slotUsed, item == ingre ? slotUsed : slot);
				return true;
			}
		}
		return false;
	}

	public static void createUnfinished(final Player player, int herb, final double unf, final int slot, final int slotUsed) {
		final Item item = new Item(herb, 1);
		if (!Constants.HERBLORE_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		// player.getActionSender().playSound(270, 1, 0);
		player.getUpdateFlags().sendAnimation(UNFINISHED_ANIMATION);
		player.getActionSender().sendMessage("You put the " + item.getDefinition().getName().replace(" herb", "").toLowerCase() + " into the vial of water.");

        final int taskId = player.getTask();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
                if (!player.checkTask(taskId)) {
                    container.stop();
                    return;
                }
                int vialType = (item.getId() == 6016 || item.getId() == 2398  || item.getId() == 259) ? COCONUT_MILK : VIAL_OF_WATER;
				if (player.getInventory().playerHasItem(item) && player.getInventory().playerHasItem(vialType)) {
					player.getInventory().removeItem(item);
					player.getInventory().removeItem(new Item(vialType));
					player.getInventory().addItem(new Item((int) unf));
				}
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 1);
	}

	public static void createFinished(final Player player, final double ingre, final double unf, final double fin, final double exp, final int slot, final int slotUsed) {
		final Item item = new Item((int) ingre, 1);
		if (!Constants.HERBLORE_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		player.getUpdateFlags().sendAnimation(UNFINISHED_ANIMATION);
		player.getActionSender().sendMessage("You mix the " + item.getDefinition().getName().toLowerCase() + " into your potion");

        final int taskId = player.getTask();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
                if (!player.checkTask(taskId)) {
                    container.stop();
                    return;
                }
				if (player.getInventory().playerHasItem(item) && player.getInventory().playerHasItem(new Item((int) unf))) {
					player.getInventory().removeItem(item);
					player.getInventory().removeItem(new Item((int) unf));
					player.getInventory().addItem(new Item((int) fin));
				}
				player.getSkill().addExp(15, exp);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 2);
	}

	public static boolean emptyPotion(Player player, Item item, int slot) {
		String description = item.getDefinition().getDescription().toLowerCase();
		String name = item.getDefinition().getName().toLowerCase();
		if (description.contains("bucket") || description.contains("potion") || description.contains("dose") || description.contains("jug") || name.contains("jug") || description.contains("bowl") || description.contains("vial") || name.contains("flour") || description.contains("bucket") || description.contains("cup")) {
			player.getActionSender().sendMessage("You empty your " + name + ".");
			if (player.getInventory().removeItemSlot(item, slot)) {
				player.getInventory().addItemToSlot(new Item(getEmptyId(item)), slot);
			} else if (player.getInventory().removeItem(item)) {
				player.getInventory().addItem(new Item(getEmptyId(item)));
			}
			return true;
		}
		return false;
	}

	private static int getEmptyId(Item item) {
		String description = item.getDefinition().getDescription().toLowerCase();
		String name = item.getDefinition().getName().toLowerCase();
		if (description.contains("potion") || description.contains("vial") || description.contains("dose")) {
			return 229;
		}
		if (description.contains("bucket") || description.contains("compost")) {
			return 1925;
		}
		if (description.contains("bowl") || description.contains("curry")) {
			return 1923;
		}
		if (name.contains("jug") || description.contains("jug")) {
			return 1935;
		}
		if (name.contains("flour")) {
			return 1931;
		}
		if (description.contains("cup")) {
			return 1980;
		}
		return -1;
	}
}