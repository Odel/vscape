package com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Crafting.LeatherMakingAction.LeatherMaking;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 19:49 To change
 * this template use File | Settings | File Templates.
 */
public class SnakeskinTwo extends LeatherMaking {

	public SnakeskinTwo(final Player player, final int used, final int used2, final int result, final int amount, final int manualAmount, final int level, final double experience) {
		super(player, used, used2, result, amount, manualAmount, level, experience);
	}

	public static SnakeskinTwo create(final Player player, int buttonId, int manualAmount) {
		final SnakeskinTwoLeatherMake snakeskinTwoLeatherMake = SnakeskinTwoLeatherMake.forId(buttonId);
		if (snakeskinTwoLeatherMake == null || (snakeskinTwoLeatherMake.getAmount() == 0 && manualAmount == 0))
			return null;
		return new SnakeskinTwo(player, snakeskinTwoLeatherMake.getUsed(), snakeskinTwoLeatherMake.getUsed2(), snakeskinTwoLeatherMake.getResult(), snakeskinTwoLeatherMake.getAmount(), manualAmount, snakeskinTwoLeatherMake.getLevel(), snakeskinTwoLeatherMake.getExperience());
	}

	public static enum SnakeskinTwoLeatherMake {
		VAMB(34185, 6289, 8, 6330, 1, 8, 47, 35),
		VAMB5(34184, 6289, 8, 6330, 5, 8, 47, 35),
		VAMB10(34183, 6289, 8, 6330, 10, 8, 47, 35),
		VAMBX(34182, 6289, 8, 6330, 0, 8, 47, 35),
		CHAPS(34189, 6289, 12, 6324, 1, 12, 51, 50), 
		CHAPS5(34188, 6289, 12, 6324, 5, 12, 51, 50),
		CHAPS10(34187, 6289, 12, 6324, 10, 12, 51, 50),
		CHAPSX(34186, 6289, 12, 6324, 0, 12, 51, 50),
		BODY(34193, 6289, 15, 6322, 1, 15, 53, 55),
		BODY5(34192, 6289, 15, 6322, 5, 15, 53, 55),
		BODY10(34191, 6289, 15, 6322, 10, 15, 53, 55),
		BODYX(34190, 6289, 15, 6322, 0, 15, 53, 55);

		private int buttonId;
		private int used;
		private int used2;
		private int result;
		private int amount;
		private int amountNeeded;
		private int level;
		private double experience;

		public static HashMap<Integer, SnakeskinTwoLeatherMake> snakeskinTwoLeatherItems = new HashMap<Integer, SnakeskinTwoLeatherMake>();

		public static SnakeskinTwoLeatherMake forId(int id) {
			if (snakeskinTwoLeatherItems == null) {
				return null;
			}
			return snakeskinTwoLeatherItems.get(id);
		}

		static {
			for (SnakeskinTwoLeatherMake data : SnakeskinTwoLeatherMake.values()) {
				snakeskinTwoLeatherItems.put(data.buttonId, data);
			}
		}

		private SnakeskinTwoLeatherMake(int buttonId, int used, int used2, int result, int amount, int amountNeeded, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.used2 = used2;
			this.result = result;
			this.amount = amount;
			this.amountNeeded = amountNeeded;
			this.level = level;
			this.experience = experience;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getUsed() {
			return used;
		}

		public int getUsed2() {
			return used2;
		}

		public int getResult() {
			return result;
		}

		public int getAmount() {
			return amount;
		}

		public int getAmountNeeded() {
			return amountNeeded;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}

}
