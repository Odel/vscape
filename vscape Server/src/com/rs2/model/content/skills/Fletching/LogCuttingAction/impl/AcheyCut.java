package com.rs2.model.content.skills.Fletching.LogCuttingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Fletching.LogCuttingAction.LogCutting;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 14:23 To change
 * this template use File | Settings | File Templates.
 */
public class AcheyCut extends LogCutting {

	public AcheyCut(final Player player, final int used, final int result, final int level, final double experience, final int amount, final int manualAmount) {
		super(player, used, result, level, experience, amount, manualAmount);
	}

	public static AcheyCut create(final Player player, int buttonId, int manualAmount) {
		final AcheyCutData acheyCutData = AcheyCutData.forId(buttonId);
		if (acheyCutData == null || (acheyCutData.getAmount() == 0 && manualAmount == 0))
			return null;
		return new AcheyCut(player, acheyCutData.getUsed(), acheyCutData.getResult(), acheyCutData.getLevel(), acheyCutData.getExperience(), acheyCutData.getAmount(), manualAmount);
	}

	public static enum AcheyCutData {
		COMPOSITE_OGRE(10239, 2862, 4825, 1, 30, 45), COMPOSITE_OGRE5(10238, 2862, 4825, 5, 30, 45), COMPOSITE_OGRE28(6211, 2862, 4825, 28, 30, 45), COMPOSITE_OGREX(6212, 2862, 4825, 0, 30, 45);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, AcheyCutData> acheyItems = new HashMap<Integer, AcheyCutData>();

		public static AcheyCutData forId(int id) {
			return acheyItems.get(id);
		}

		static {
			for (AcheyCutData data : AcheyCutData.values()) {
				acheyItems.put(data.buttonId, data);
			}
		}

		private AcheyCutData(int buttonId, int used, int result, int amount, int level, double experience) {
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
