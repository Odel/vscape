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
		if (itemName1.contains("waterskin") || itemName2.contains("waterskin")) {
			return false;
		}
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
	
	public enum PotionData {
		ATTACK(VIAL_OF_WATER, 249, 221, 91, 121, 0, 25),
		ANTIPOISON(VIAL_OF_WATER, 251, 235, 93, 175, 5, 37.5),
		STRENGTH(VIAL_OF_WATER, 253, 225, 95, 115, 12, 50),
		SERUM207(VIAL_OF_WATER, 253, 592, 95, 3408, 15, 50),
		RESTORE(VIAL_OF_WATER, 255, 223, 97, 127, 22, 62.5),
		BLAMISHOIL(VIAL_OF_WATER, 255, 1581, 97, 1582, 25, 80),
		ENERGY(VIAL_OF_WATER, 255, 1975, 97, 3010, 26, 67.5),
		DEFENCE(VIAL_OF_WATER, 257, 239, 99, 133, 30, 75),
		AGILITY(VIAL_OF_WATER, 2998, 2152, 3002, 3034, 34, 80),
		COMBAT(VIAL_OF_WATER, 255, 9736, 97, 9741, 36, 84),
		PRAYER(VIAL_OF_WATER, 257, 231, 99, 139, 38, 87.5),
		SUPERATTACK(VIAL_OF_WATER, 259, 221, 101, 145, 45, 100),
		SUPERANTIPOISON(VIAL_OF_WATER, 259, 235, 101, 181, 48, 106.3),
		FISHING(VIAL_OF_WATER, 261, 231, 103, 151, 50, 112.5),
		SUPERENERGY(VIAL_OF_WATER, 261, 2970, 103, 3010, 52, 117.5),
		SUPERSTRENGTH(VIAL_OF_WATER, 263, 225, 105, 157, 55, 125),
		WEAPONPOISON(VIAL_OF_WATER, 263, 241, 105, 187, 60, 137.5),
		SUPERRESTORE(VIAL_OF_WATER, 3000, 223, 3004, 3026, 63, 142.5),
		SUPERDEFENCE(VIAL_OF_WATER, 265, 239, 107, 163, 66, 150),
		ANTIDOTEPLUS(COCONUT_MILK, 2998, 6049, 5942, 5945, 68, 175),
		ANTIFIRE(VIAL_OF_WATER, 2481, 241, 2483, 2454, 69, 157.5),
		RANGING(VIAL_OF_WATER, 267, 245, 109, 169, 72, 162.5),
		WEAPONPLUS(COCONUT_MILK, 6016, 223, 5936, 5937, 73, 175),
		MAGIC(VIAL_OF_WATER, 2481, 3138, 2483, 3042, 76, 172.5),
		ZAMORAK(VIAL_OF_WATER, 269, 247, 111, 189, 78, 175),
		ANTIDOTEPLUSPLUS(COCONUT_MILK, 259, 6051, 101, 5954, 79, 175),
		SARABREW(VIAL_OF_WATER, 2998, 6693, 3002, 6687, 81, 175),
		WEAPONPLUSPLUS(COCONUT_MILK, 2398, 6018, 5939, 5940, 82, 175);
		
		private int vial;
		private int herb;
		private int ingredient2;
		private int unfItem;
		private int finalItem;
		private int level;
		private double experience;

		private PotionData(int vial, int herb, int ingredient2, int unfItem, int finalItem, int level, double experience) {
			this.vial = vial;
			this.herb = herb;
			this.ingredient2 = ingredient2;
			this.unfItem = unfItem;
			this.finalItem = finalItem;
			this.level = level;
			this.experience = experience;
		}
		
		public int getVial() {
			return vial;
		}
		
		public int getHerb() {
			return herb;
		}

		public int getIngredient() {
			return ingredient2;
		}
		
		public int getUnfished() {
			return unfItem;
		}

		public int getFinalItem() {
			return finalItem;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}
		
		public static PotionData forId(int itemId, int usedWith) {
			for (PotionData potionData : PotionData.values()) {
				if ((potionData.herb == itemId && potionData.vial == usedWith) || (potionData.vial == itemId && potionData.herb == usedWith)
						|| (potionData.ingredient2 == itemId && potionData.unfItem == usedWith) || (potionData.unfItem == itemId && potionData.ingredient2 == usedWith))
					return potionData;
			}
			return null;
		}
	}

	public static boolean createPotion(Player player, Item useItem, Item withItem, int slot, int slotUsed) {
		final PotionData potionData = PotionData.forId(useItem.getId(), withItem.getId());
		if (potionData == null)
			return false;
		int item = useItem.getId(), usedItem = withItem.getId();
		int vial = potionData.getVial();
		int herb = potionData.getHerb();
		int ingredient = potionData.getIngredient();
		int unf = potionData.getUnfished();
		int finished = potionData.getFinalItem();
		int lvl = potionData.getLevel();
		double exp = potionData.getExperience();
		if ((item == herb && usedItem == vial) || (item == vial && usedItem == herb)) {
			if (player.getSkill().getLevel()[Skill.HERBLORE] < lvl) {
				player.getDialogue().sendStatement("You need a Herblore level of " + (int) lvl + " in order to make this potion.");
				return true;
			}
			createUnfinished(player, herb, unf, vial);
			return true;
		}
		if ((item == unf && usedItem == ingredient) || (item == ingredient && usedItem == unf)) {
			if (player.getSkill().getLevel()[Skill.HERBLORE] < lvl) {
				player.getDialogue().sendStatement("You need a Herblore level of " + (int) lvl + " in order to make this potion.");
				return true;
			}
			createFinished(player, ingredient, unf, finished, exp, item == ingredient ? slot : slotUsed, item == ingredient ? slotUsed : slot);
			return true;
		}
		return false;
	}

	public static void createUnfinished(final Player player, int herb, final int unf, final int vial) {
		final Item item = new Item(herb, 1);
		if (!Constants.HERBLORE_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		player.getUpdateFlags().sendAnimation(UNFINISHED_ANIMATION);
		final int taskId = player.getTask();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
                if (!player.checkTask(taskId)) {
                    container.stop();
                    return;
                }
				if (!player.getInventory().playerHasItem(item) || !player.getInventory().playerHasItem(vial)) {
                    container.stop();
                    return;
				}
				player.getInventory().removeItem(item);
				player.getInventory().removeItem(new Item(vial));
				player.getInventory().addItem(new Item(unf));
				player.getActionSender().sendMessage("You put the " + item.getDefinition().getName().replace(" herb", "").toLowerCase() + " into the "+ new Item(vial).getDefinition().getName() + ".");
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