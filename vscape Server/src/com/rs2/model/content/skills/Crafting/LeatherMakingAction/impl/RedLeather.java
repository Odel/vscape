package com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Crafting.LeatherMakingAction.LeatherMaking;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 19:48 To change
 * this template use File | Settings | File Templates.
 */
public class RedLeather extends LeatherMaking {

	public RedLeather(final Player player, final int used, final int used2, final int result, final int amount, final int manualAmount, final int level, final double experience) {
		super(player, used, used2, result, amount, manualAmount, level, experience);
	}

	public static RedLeather create(final Player player, int buttonId, int manualAmount) {
		final RedLeatherMake redLeatherMake = RedLeatherMake.forId(buttonId);
		if (redLeatherMake == null || (redLeatherMake.getAmount() == 0 && manualAmount == 0))
			return null;
		return new RedLeather(player, redLeatherMake.getUsed(), redLeatherMake.getUsed2(), redLeatherMake.getResult(), redLeatherMake.getAmount(), manualAmount, redLeatherMake.getLevel(), redLeatherMake.getExperience());
	}

	public static enum RedLeatherMake {
		VAMB(34185, 2507, 1, 2489, 1, 73, 86), VAMB5(34184, 2507, 1, 2489, 5, 73, 86), VAMB10(34183, 2507, 1, 2489, 10, 73, 86), VAMBX(34182, 2507, 1, 2489, 0, 73, 86), CHAPS(34189, 2507, 2, 2495, 1, 75, 172), CHAPS5(34188, 2507, 2, 2495, 5, 75, 172), CHAPS10(34187, 2507, 2, 2495, 10, 75, 172), CHAPSX(34186, 2507, 2, 2495, 0, 75, 172), BODY(34193, 2507, 3, 2501, 1, 77, 258), BODY5(34192, 2507, 3, 2501, 5, 77, 258), BODY10(34191, 2507, 3, 2501, 10, 77, 258), BODYX(34190, 2507, 3, 2501, 0, 77, 258);

		private int buttonId;
		private int used;
		private int used2;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, RedLeatherMake> redLeatherItems = new HashMap<Integer, RedLeatherMake>();

		public static RedLeatherMake forId(int id) {
			if (redLeatherItems == null) {
				return null;
			}
			return redLeatherItems.get(id);
		}

		static {
			for (RedLeatherMake data : RedLeatherMake.values()) {
				redLeatherItems.put(data.buttonId, data);
			}
		}

		private RedLeatherMake(int buttonId, int used, int used2, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.used2 = used2;
			this.result = result;
			this.amount = amount;
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

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}

}
