package com.rs2.model.content.skills.Fletching.LogCuttingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Fletching.LogCuttingAction.LogCutting;
import com.rs2.model.players.Player;

public class MahoganyCut extends LogCutting {

	public MahoganyCut(final Player player, final int used, final int result, final int level, final double experience, final int amount, final int manualAmount) {
		super(player, used, result, level, experience, amount, manualAmount);
	}

	public static MahoganyCut create(final Player player, int buttonId, int manualAmount) {
		final MahoganyCutData mahoganyCutData = MahoganyCutData.forId(buttonId);
		if (mahoganyCutData == null || (mahoganyCutData.getAmount() == 0 && manualAmount == 0))
			return null;
		return new MahoganyCut(player, mahoganyCutData.getUsed(), mahoganyCutData.getResult(), mahoganyCutData.getLevel(), mahoganyCutData.getExperience(), mahoganyCutData.getAmount(), manualAmount);
	}

	public static enum MahoganyCutData {
		STOCK(10239, 6332, 9450, 1, 61, 41), STOCK5(10238, 6332, 9450, 5, 61, 41), STOCKX(6212, 6332, 9450, 0, 61, 41),STOCKALL(6211, 6332, 9450, 28, 61, 41);
		
		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, MahoganyCutData> mahoganyCutItems = new HashMap<Integer, MahoganyCutData>();

		public static MahoganyCutData forId(int id) {
			return mahoganyCutItems.get(id);
		}

		static {
			for (MahoganyCutData data : MahoganyCutData.values()) {
				mahoganyCutItems.put(data.buttonId, data);
			}
		}

		private MahoganyCutData(int buttonId, int used, int result, int amount, int level, double experience) {
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
