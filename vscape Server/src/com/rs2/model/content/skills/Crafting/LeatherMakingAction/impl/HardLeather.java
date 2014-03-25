package com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Crafting.LeatherMakingAction.LeatherMaking;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 21:49 To change
 * this template use File | Settings | File Templates.
 */
public class HardLeather extends LeatherMaking {

	public HardLeather(final Player player, final int used, final int used2, final int result, final int amount, final int manualAmount, final int level, final double experience) {
		super(player, used, used2, result, amount, manualAmount, level, experience);
	}

	public static HardLeather create(final Player player, int buttonId, int manualAmount) {
		final HardLeatherMake hardLeatherMake = HardLeatherMake.forId(buttonId);
		if (hardLeatherMake == null || (hardLeatherMake.getAmount() == 0 && manualAmount == 0))
			return null;
		return new HardLeather(player, hardLeatherMake.getUsed(), hardLeatherMake.getUsed2(), hardLeatherMake.getResult(), hardLeatherMake.getAmount(), manualAmount, hardLeatherMake.getLevel(), hardLeatherMake.getExperience());
	}

	public static enum HardLeatherMake {
		BODY(10239, 1743, 3, 1131, 1, 28, 35), BODY5(10238, 1743, 3, 1131, 5, 28, 35), BODY10(6212, 1743, 3, 1131, 27, 28, 35), BODYX(6211, 1743, 3, 1131, 0, 28, 35);

		private int buttonId;
		private int used;
		private int used2;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, HardLeatherMake> hardLeatherItems = new HashMap<Integer, HardLeatherMake>();

		public static HardLeatherMake forId(int id) {
			if (hardLeatherItems == null) {
				return null;
			}
			return hardLeatherItems.get(id);
		}

		static {
			for (HardLeatherMake data : HardLeatherMake.values()) {
				hardLeatherItems.put(data.buttonId, data);
			}
		}

		private HardLeatherMake(int buttonId, int used, int used2, int result, int amount, int level, double experience) {
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
