package com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Crafting.LeatherMakingAction.LeatherMaking;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 19:49 To change
 * this template use File | Settings | File Templates.
 */
public class SnakeskinOne extends LeatherMaking {

	public SnakeskinOne(final Player player, final int used, final int used2, final int result, final int amount, final int manualAmount, final int level, final double experience) {
		super(player, used, used2, result, amount, manualAmount, level, experience);
	}

	public static SnakeskinOne create(final Player player, int buttonId, int manualAmount) {
		final SnakeskinOneLeatherMake snakeskinOneLeatherMake = SnakeskinOneLeatherMake.forId(buttonId);
		if (snakeskinOneLeatherMake == null || (snakeskinOneLeatherMake.getAmount() == 0 && manualAmount == 0))
			return null;
		return new SnakeskinOne(player, snakeskinOneLeatherMake.getUsed(), snakeskinOneLeatherMake.getUsed2(), snakeskinOneLeatherMake.getResult(), snakeskinOneLeatherMake.getAmount(), manualAmount, snakeskinOneLeatherMake.getLevel(), snakeskinOneLeatherMake.getExperience());
	}

	public static enum SnakeskinOneLeatherMake {
		BANDANA(34170, 6287, 5, 6326, 1, 5, 48, 45),
		BANDANA5(34169, 6287, 5, 6326, 5, 5, 48, 45),
		BANDANA10(34168, 6287, 5, 6326, 10, 5, 48, 45),
		BANDANAX(34167, 6287, 5, 6326, 0, 5, 48, 45),
		BOOTS(34174, 6287, 6, 6328, 1, 6, 45, 30),
		BOOTS5(34173, 6287, 6, 6328, 5, 6, 45, 30),
		BOOTS10(34172, 6287, 6, 6328, 10, 6, 45, 30),
		BOOTSX(34171, 6287, 6, 6328, 0, 6, 45, 30);

		private int buttonId;
		private int used;
		private int used2;
		private int result;
		private int amount;
		private int amountNeeded;
		private int level;
		private double experience;

		public static HashMap<Integer, SnakeskinOneLeatherMake> snakeskinOneLeatherItems = new HashMap<Integer, SnakeskinOneLeatherMake>();

		public static SnakeskinOneLeatherMake forId(int id) {
			if (snakeskinOneLeatherItems == null) {
				return null;
			}
			return snakeskinOneLeatherItems.get(id);
		}

		static {
			for (SnakeskinOneLeatherMake data : SnakeskinOneLeatherMake.values()) {
				snakeskinOneLeatherItems.put(data.buttonId, data);
			}
		}

		private SnakeskinOneLeatherMake(int buttonId, int used, int used2, int result, int amount, int amountNeeded, int level, double experience) {
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
