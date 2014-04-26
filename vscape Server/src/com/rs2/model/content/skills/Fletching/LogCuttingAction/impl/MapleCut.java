package com.rs2.model.content.skills.Fletching.LogCuttingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Fletching.LogCuttingAction.LogCutting;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 14:21 To change
 * this template use File | Settings | File Templates.
 */
public class MapleCut extends LogCutting {

	public MapleCut(final Player player, final int used, final int result, final int level, final double experience, final int amount, final int manualAmount) {
		super(player, used, result, level, experience, amount, manualAmount);
	}

	public static MapleCut create(final Player player, int buttonId, int manualAmount) {
		final MapleCutData mapleCutData = MapleCutData.forId(buttonId);
		if (mapleCutData == null || (mapleCutData.getAmount() == 0 && manualAmount == 0))
			return null;
		return new MapleCut(player, mapleCutData.getUsed(), mapleCutData.getResult(), mapleCutData.getLevel(), mapleCutData.getExperience(), mapleCutData.getAmount(), manualAmount);
	}
	public static enum MapleCutData {
		SHORTBOW(34185, 1517, 64, 1, 50, 50), SHORTBOW5(34184, 1517, 64, 5, 50, 50), SHORTBOW10(34183, 1517, 64, 10, 50, 50), SHORTBOWX(34182, 1517, 64, 0, 50, 50), LONGBOW(34189, 1517, 62, 1, 55, 58.3), LONGBOW5(34188, 1517, 62, 5, 55, 58.3), LONGBOW10(34187, 1517, 62, 10, 55, 58.3), LONGBOWX(34186, 1517, 62, 0, 55, 58.3),
		STOCK(34193, 1517, 9448, 1, 54, 32), STOCK5(34192, 1517, 9448, 5, 54, 32), STOCK10(34191, 1517, 9448, 10, 54, 32), STOCKX(34190, 1517, 9448, 0, 54, 32);
		
		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, MapleCutData> mapleCutItems = new HashMap<Integer, MapleCutData>();

		public static MapleCutData forId(int id) {
			return mapleCutItems.get(id);
		}

		static {
			for (MapleCutData data : MapleCutData.values()) {
				mapleCutItems.put(data.buttonId, data);
			}
		}

		private MapleCutData(int buttonId, int used, int result, int amount, int level, double experience) {
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
