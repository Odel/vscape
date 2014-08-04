package com.rs2.model.content.skills.cooking;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 21:29 To change
 * this template use File | Settings | File Templates.
 */
public class OneIngredients {

	public static enum MixItemData {
		PIE_SHELL(1953, 0, 2313, 2315, 1, 0, true, 0), 
		REDBERRY_PIE(1951, 0, 2315, 2321, 10, 78, true, 0), 
		APPLE_PIE(1955, 0, 2315, 2317, 30, 130, true, 0),
		MEAT_PIE(2142, 0, 2315, 2319, 20, 110, true, 0),
		MEAT_PIE_CHICKEN(2140, 0, 2315, 2319, 20, 110, true, 0),
		CURRY(2007, 0, 1921, 2009, 60, 280, true, 7496), 
		CURRY_LEAF(5970, 3, 1921, 2009, 60, 280, true, 0),

		MEAT_PIZZA(2142, 0, 2289, 2293, 45, 169, true, 0), 
		ANCHOVIE_PIZZA(319, 0, 2289, 2297, 45, 169, true, 0), 
		PINEAPPLE_PIZZA(2116, 0, 2289, 2301, 65, 195, true, 0), 
		PINEAPPLE_PIZZA2(2118, 0, 2289, 2301, 65, 195, true, 0),

		CHOCOLATE_CAKE(1973, 0, 1891, 1897, 50, 210, true, 0),
		CHOCOLATE_CAKE2(1975, 0, 1891, 1897, 50, 210, true, 0), 
		CHOCOLATE_MILK(1975, 0, 1927, 1977, 4, 0, true, 0), 
		CAKE1(2516, 0, 1887, 1889, 40, 0, true, 0), 
		CAKE2(1927, 0, 1887, 1889, 40, 0, true, 0), 
		CAKE3(1944, 0, 1887, 1889, 40, 0, true, 0),

		SPICY_SAUCE(1550, 0, 1923, 7074, 9, 25, true, 0), 
		SPICY_SAUCE2(2169, 0, 7074, 7072, 9, 25, true, 0), 
		CON_CARNE(2142, 0, 7072, 7062, 11, 55, true, 0), 
		SCRAMBLED_EGG(1944, 0, 1923, 7076, 13, 50, true, 0), 
		SCRAMBLED_EGG_AND_TOMATO(1982, 0, 7078, 7064, 23, 50, true, 0), 
		FRIED_ONION(1957, 0, 1923, 1871, 42, 60, true, 0), 
		FRIED_MUSHROOM(6004, 0, 1923, 7080, 46, 60, true, 0),
		MUSHROOM_AND_ONION(7082, 0, 7084, 7066, 57, 120, false, 1923),

		POTATO_WITH_BUTTER(6697, 0, 6701, 6703, 39, 40, true, 0), 
		CHILLI_POTATO(7062, 0, 6703, 7054, 41, 15, true, 1923), 
		CHEESE_POTATO(1985, 0, 6703, 6705, 47, 40, true, 0), 
		EGG_POTATO(7064, 0, 6703, 7056, 51, 45, true, 1923), 
		MUSHROOM_AND_ONION_POTATO(7066, 0, 6703, 7058, 64, 55, true, 1923), 
		TUNA_POTATO(7068, 0, 6703, 7060, 68, 10, true, 1923),
		
		WINE(1987, 0, 1937, 1993, 35, 309.5, false, 0), 
		NETTLE(4241, 0, 1921, 4237, 20, 52, false, 0), 
		NETTLE_TEA(4237, 0, 4242, 4239, 20, 52, false, 1923);

		private int primaryIngredient;
		private int amount;
		private int recipient;
		private int result;
		private int level;
		private double experience;
		private boolean hasRecipient;
		private int emptyOne;

		public static MixItemData forIdItem(int itemUsed, int withItem) {
			for (MixItemData mixItemData : MixItemData.values()) {
				if ((mixItemData.primaryIngredient == itemUsed && mixItemData.recipient == withItem) || (mixItemData.primaryIngredient == withItem && mixItemData.recipient == itemUsed))
					return mixItemData;

			}
			return null;
		}

		private MixItemData(int primaryIngredient, int amount, int recipient, int result, int level, double experience, boolean hasRecipient, int emptyOne) {
			this.primaryIngredient = primaryIngredient;
			this.amount = amount;
			this.recipient = recipient;
			this.result = result;
			this.level = level;
			this.experience = experience;
			this.hasRecipient = hasRecipient;
			this.emptyOne = emptyOne;
		}

		public int getPrimaryIngredient() {
			return primaryIngredient;
		}

		public int getAmount() {
			return amount;
		}

		public int getRecipient() {
			return recipient;
		}

		public int getResult() {
			return result;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

		public boolean hasRecipient() {
			return hasRecipient;
		}

		public int getEmptyOne() {
			return emptyOne;
		}

	}

	public static boolean mixItems(Player player, int itemUsed, int withItem, int itemUsedSlot, int withItemSlot) {
		MixItemData rightData = MixItemData.forIdItem(itemUsed, withItem);
		if (rightData == null)
			return false;
		player.getActionSender().removeInterfaces();
		if (!Constants.COOKING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (player.getSkill().getLevel()[Skill.COOKING] < rightData.getLevel()) {
			player.getDialogue().sendStatement("You need a cooking level of " + rightData.getLevel() + " to do this.");
			return true;
		}

		int amount = rightData.getAmount() == 0 ? 1 : rightData.getAmount();
		if (player.getInventory().getItemAmount(rightData.getPrimaryIngredient()) < amount) {
			player.getDialogue().sendStatement("You need " + amount + " " + ItemDefinition.forId(rightData.getPrimaryIngredient()).getName().toLowerCase() + " to do this");
			return true;
		}

		if ((rightData.getResult() == 1871 || rightData.getResult() == 7080 || rightData.getResult() == 7074) && !player.getInventory().getItemContainer().contains(946)) {
			player.getActionSender().sendMessage("You need a knife for that.");
			return true;
		}
		if (rightData.getResult() == 1889)// making cake
			if (!player.getInventory().getItemContainer().contains(2516) || !player.getInventory().getItemContainer().contains(1927) || !player.getInventory().getItemContainer().contains(1944))
				return true;
			else {
				handleCake(player);
				return true;
			}

		if (rightData.getResult() == 7066)
			player.getInventory().addItem(new Item(1923));

		if (rightData.hasRecipient())
			player.getActionSender().sendMessage("You put the " + ItemDefinition.forId(rightData.getPrimaryIngredient()).getName().toLowerCase() + " into the " + ItemDefinition.forId(rightData.getRecipient()).getName().toLowerCase() + " and make a " + ItemDefinition.forId(rightData.getResult()).getName().toLowerCase() + ".");
		else
			player.getActionSender().sendMessage("You mix the " + ItemDefinition.forId(rightData.getPrimaryIngredient()).getName().toLowerCase() + " with the " + ItemDefinition.forId(rightData.getRecipient()).getName().toLowerCase() + " and make a " + ItemDefinition.forId(rightData.getResult()).getName().toLowerCase() + ".");

		if (rightData.getResult() != 1889)
			player.getInventory().removeItem(new Item(rightData.getPrimaryIngredient(), amount));
		player.getInventory().removeItem(new Item(rightData.getRecipient()));
		player.getInventory().addItem(new Item(rightData.getResult()));
		//player.getInventory().addItemToSlot(new Item(rightData.getResult()), itemUsed == rightData.getRecipient() ? itemUsedSlot : withItemSlot);
		player.getSkill().addExp(Skill.COOKING, rightData.getExperience());
		/* empty recipients */
		if (rightData.getEmptyOne() != 0)
			player.getInventory().addItem(new Item(rightData.getEmptyOne()));
		return true;
	}

	public static void handleCake(Player player) {
		player.getInventory().removeItem(new Item(2516));
		player.getInventory().removeItem(new Item(1927));
		player.getInventory().removeItem(new Item(1944));
		player.getInventory().removeItem(new Item(1887));
		player.getInventory().addItem(new Item(3727));
		player.getInventory().addItem(new Item(1931));
		player.getInventory().addItem(new Item(1889));
		player.getActionSender().sendMessage("You mix the ingredients together and make a cake.");
	}
}
