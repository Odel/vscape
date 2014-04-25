package com.rs2.model.content.skills.Fletching.LogCuttingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Fletching.LogCuttingAction.LogCutting;
import com.rs2.model.players.Player;

public class TeakCut extends LogCutting {

	public TeakCut(final Player player, final int used, final int result, final int level, final double experience, final int amount, final int manualAmount) {
		super(player, used, result, level, experience, amount, manualAmount);
	}

	public static TeakCut create(final Player player, int buttonId, int manualAmount) {
		final TeakCutData teakCutData = TeakCutData.forId(buttonId);
		if (teakCutData == null || (teakCutData.getAmount() == 0 && manualAmount == 0))
			return null;
		return new TeakCut(player, teakCutData.getUsed(), teakCutData.getResult(), teakCutData.getLevel(), teakCutData.getExperience(), teakCutData.getAmount(), manualAmount);
	}

	public static enum TeakCutData {
		STOCK(10239, 6333, 9446, 1, 46, 54), STOCK5(10238, 6333, 9446, 5, 46, 54), STOCKX(6212, 6333, 9446, 0, 46, 54),STOCKALL(6211, 6333, 9446, 28, 46, 54);
		
		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, TeakCutData> teakCutItems = new HashMap<Integer, TeakCutData>();

		public static TeakCutData forId(int id) {
			return teakCutItems.get(id);
		}

		static {
			for (TeakCutData data : TeakCutData.values()) {
				teakCutItems.put(data.buttonId, data);
			}
		}

		private TeakCutData(int buttonId, int used, int result, int amount, int level, double experience) {
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
