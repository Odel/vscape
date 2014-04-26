package com.rs2.model.content.skills.Fletching.LogCuttingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Fletching.LogCuttingAction.LogCutting;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 14:21 To change
 * this template use File | Settings | File Templates. Cleaned & Sorted / fixed
 * bugs by Johan
 */
public class OakCut extends LogCutting {

	public OakCut(final Player player, final int used, final int result, final int level, final double experience, final int amount, final int manualAmount) {
		super(player, used, result, level, experience, amount, manualAmount);
	}

	public static OakCut create(final Player player, int buttonId, int manualAmount) {
		final OakCutData oakCutData = OakCutData.forId(buttonId);
		if (oakCutData == null || (oakCutData.getAmount() == 0 && manualAmount == 0))
			return null;
		return new OakCut(player, oakCutData.getUsed(), oakCutData.getResult(), oakCutData.getLevel(), oakCutData.getExperience(), oakCutData.getAmount(), manualAmount);
	}

	public static enum OakCutData {
		SHORTBOW(34185, 1521, 54, 1, 20, 16.5), SHORTBOW5(34184, 1521, 54, 5, 20, 16.5), SHORTBOW10(34183, 1521, 54, 10, 20, 16.5), SHORTBOWX(34182, 1521, 54, 0, 20, 16.5), LONGBOW(34189, 1521, 56, 1, 25, 25), LONGBOW5(34188, 1521, 56, 5, 25, 25), LONGBOW10(34187, 1521, 56, 10, 25, 25), LONGBOWX(34186, 1521, 56, 0, 25, 25),
		STOCK(34193, 1521, 9442, 1, 24, 32), STOCK5(34192, 1521, 9442, 5, 24, 32), STOCK10(34191, 1521, 9442, 10, 24, 32), STOCKX(34190, 1521, 9442, 0, 24, 32);
		
		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, OakCutData> oakCutItems = new HashMap<Integer, OakCutData>();

		public static OakCutData forId(int id) {
			return oakCutItems.get(id);
		}

		static {
			for (OakCutData data : OakCutData.values()) {
				oakCutItems.put(data.buttonId, data);
			}
		}

		private OakCutData(int buttonId, int used, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
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