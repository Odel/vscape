package com.rs2.model.content.skills.cooking;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 23/12/11 Time: 01:30 To change
 * this template use File | Settings | File Templates.
 */
public class TwoIngredients {
	public static enum MixItemData {
		STEW(2142, 1942, 1921, 1999, 2001, 25, 117, true, 0, 0), STEW1(1942, 2142, 1921, 1997, 2001, 25, 117, true, 0, 0), PLAIN_PIZZA(1982, 1985, 2283, 2285, 2287, 35, 143, true, 0, 0), TUNE_AND_CORN(361, 5988, 1923, 7086, 7068, 67, 204, true, 0, 0), TUNE_AND_CORN2(5988, 361, 1923, 7088, 7068, 67, 204, true, 0, 0),

		;

		private int primaryIngredient;
		private int secondIngredient;
		private int recipient;
		private int firstStage;
		private int result;
		private int level;
		private double experience;
		private boolean hasRecipient;
		private int emptyOne;
		private int emptyTwo;

		public static HashMap<Integer, MixItemData> firstItems = new HashMap<Integer, MixItemData>();
		public static HashMap<Integer, MixItemData> firstStages = new HashMap<Integer, MixItemData>();

		public static MixItemData forIdPrimary(int id) {
			return firstItems.get(id);
		}

		public static MixItemData forIdFirstStage(int id) {
			return firstStages.get(id);
		}

		static {
			for (MixItemData data : MixItemData.values()) {
				firstItems.put(data.primaryIngredient, data);
				firstStages.put(data.firstStage, data);
			}
		}

		private MixItemData(int primaryIngredient, int secondIngredient, int recipient, int firstStage, int result, int level, double experience, boolean hasRecipient, int emptyOne, int emptyTwo) {
			this.primaryIngredient = primaryIngredient;
			this.secondIngredient = secondIngredient;
			this.recipient = recipient;
			this.firstStage = firstStage;
			this.result = result;
			this.level = level;
			this.experience = experience;
			this.hasRecipient = hasRecipient;
			this.emptyOne = emptyOne;
			this.emptyTwo = emptyTwo;
		}

		public int getPrimaryIngredient() {
			return primaryIngredient;
		}

		public int getSecondIngredient() {
			return secondIngredient;
		}

		public int getFirstStage() {
			return firstStage;
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

		public int getEmptyTwo() {
			return emptyTwo;
		}

	}

	public static boolean mixItems(Player player, int itemUsed, int withItem, int itemUsedSlot, int withItemSlot) {
		MixItemData firstIngredient = MixItemData.forIdPrimary(itemUsed) != null ? MixItemData.forIdPrimary(itemUsed) : MixItemData.forIdPrimary(withItem);
		MixItemData firstStage = MixItemData.forIdFirstStage(itemUsed) != null ? MixItemData.forIdFirstStage(itemUsed) : MixItemData.forIdFirstStage(withItem);
		int ingredient = -1;
		int recipient = -1;
		int result = -1;
		MixItemData rightData = null;

		if (firstIngredient != null && (itemUsed == firstIngredient.getRecipient() || withItem == firstIngredient.getRecipient())) {
			ingredient = firstIngredient.getPrimaryIngredient();
			recipient = firstIngredient.getRecipient();
			result = firstIngredient.getFirstStage();
			rightData = firstIngredient;
		}
		if (firstStage != null && (itemUsed == firstStage.getFirstStage() || withItem == firstStage.getFirstStage()) && (itemUsed == firstStage.getSecondIngredient() || withItem == firstStage.getSecondIngredient())) {

			ingredient = firstStage.getSecondIngredient();
			recipient = firstStage.getFirstStage();
			result = firstStage.getResult();
			rightData = firstStage;
		}

		if (rightData == null)
			return false;
		player.getActionSender().removeInterfaces();
		if (!Constants.COOKING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if (player.getSkill().getLevel()[Skill.COOKING] < rightData.getLevel()) {
			player.getActionSender().sendMessage("You need a cooking level of " + rightData.getLevel() + " to do this.");
			return true;
		}
		if (rightData.getResult() == 7068 && !player.getInventory().getItemContainer().contains(946)) {
			player.getActionSender().sendMessage("You need a knife for that.");
			return true;
		}
		if (rightData.hasRecipient())
			player.getActionSender().sendMessage("You put the " + ItemDefinition.forId(ingredient).getName().toLowerCase() + " into the " + ItemDefinition.forId(recipient).getName().toLowerCase() + " and make a " + ItemDefinition.forId(result).getName().toLowerCase() + ".");
		else
			player.getActionSender().sendMessage("You mix the " + ItemDefinition.forId(ingredient).getName().toLowerCase() + " with the " + ItemDefinition.forId(recipient).getName().toLowerCase() + " and make a " + ItemDefinition.forId(result).getName().toLowerCase() + ".");

		player.getInventory().removeItem(new Item(itemUsed));
		player.getInventory().removeItem(new Item(withItem));
		player.getInventory().addItemToSlot(new Item(result), itemUsed == recipient ? itemUsedSlot : withItemSlot);
		player.getSkill().addExp(Skill.COOKING, rightData.getExperience());
		/* empty recipients */
		if (rightData.getEmptyOne() != 0 && rightData == firstIngredient)
			player.getInventory().addItem(new Item(rightData.getEmptyOne()));
		if (rightData.getEmptyTwo() != 0 && rightData == firstStage)
			player.getInventory().addItem(new Item(rightData.getEmptyTwo()));
		return true;
	}
}
