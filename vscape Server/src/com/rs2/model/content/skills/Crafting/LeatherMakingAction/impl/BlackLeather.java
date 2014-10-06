package com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Crafting.LeatherMakingAction.LeatherMaking;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 19:49 To change
 * this template use File | Settings | File Templates.
 */
public class BlackLeather extends LeatherMaking {

	public BlackLeather(final Player player, final int used, final int used2, final int result, final int amount, final int manualAmount, final int level, final double experience) {
		super(player, used, used2, result, amount, manualAmount, level, experience);
	}

	public static BlackLeather create(final Player player, int buttonId, int manualAmount) {
		final BlackLeatherMake blackLeatherMake = BlackLeatherMake.forId(buttonId);
		if (blackLeatherMake == null || (blackLeatherMake.getAmount() == 0 && manualAmount == 0))
			return null;
		return new BlackLeather(player, blackLeatherMake.getUsed(), blackLeatherMake.getUsed2(), blackLeatherMake.getResult(), blackLeatherMake.getAmount(), manualAmount, blackLeatherMake.getLevel(), blackLeatherMake.getExperience());
	}

	public static enum BlackLeatherMake {
		VAMB(34185, 2509, 1, 2491, 1, 79, 86), VAMB5(34184, 2509, 1, 2491, 5, 79, 86), VAMB10(34183, 2509, 1, 2491, 10, 79, 86), VAMBX(34182, 2509, 1, 2491, 0, 79, 86), CHAPS(34189, 2509, 2, 2497, 1, 82, 172), CHAPS5(34188, 2509, 2, 2497, 5, 82, 172), CHAPS10(34187, 2509, 2, 2497, 10, 82, 172), CHAPSX(34186, 2509, 2, 2497, 0, 82, 172), BODY(34193, 2509, 3, 2503, 1, 84, 258), BODY5(34192, 2509, 3, 2503, 5, 84, 258), BODY10(34191, 2509, 3, 2503, 10, 84, 258), BODYX(34190, 2509, 3, 2503, 0, 84, 258);

		private int buttonId;
		private int used;
		private int used2;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, BlackLeatherMake> blackLeatherItems = new HashMap<Integer, BlackLeatherMake>();

		public static BlackLeatherMake forId(int id) {
			if (blackLeatherItems == null) {
				return null;
			}
			return blackLeatherItems.get(id);
		}

		static {
			for (BlackLeatherMake data : BlackLeatherMake.values()) {
				blackLeatherItems.put(data.buttonId, data);
			}
		}

		private BlackLeatherMake(int buttonId, int used, int used2, int result, int amount, int level, double experience) {
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
