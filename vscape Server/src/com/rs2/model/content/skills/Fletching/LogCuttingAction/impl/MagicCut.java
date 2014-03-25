package com.rs2.model.content.skills.Fletching.LogCuttingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.Fletching.LogCuttingAction.LogCutting;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 14:20 To change
 * this template use File | Settings | File Templates.
 */
public class MagicCut extends LogCutting {

	public MagicCut(final Player player, final int used, final int result, final int level, final double experience, final int amount, final int manualAmount) {
		super(player, used, result, level, experience, amount, manualAmount);
	}

	public static MagicCut create(final Player player, int buttonId, int manualAmount) {
		final MagicCutData magicCutData = MagicCutData.forId(buttonId);
		if (magicCutData == null || (magicCutData.getAmount() == 0 && manualAmount == 0))
			return null;
		return new MagicCut(player, magicCutData.getUsed(), magicCutData.getResult(), magicCutData.getLevel(), magicCutData.getExperience(), magicCutData.getAmount(), manualAmount);
	}

	public static enum MagicCutData {
		SHORTBOW(34170, 1513, 72, 1, 80, 83.3), SHORTBOW5(34169, 1513, 72, 5, 65, 68.5), SHORTBOW10(34168, 1513, 72, 28, 65, 68.5), SHORTBOWX(34167, 1513, 72, 0, 65, 68.5), LONGBOW(34174, 1513, 70, 1, 85, 91.5), LONGBOW5(34173, 1513, 70, 5, 85, 91.5), LONGBOW10(34172, 1513, 70, 28, 85, 91.5), LONGBOWX(34171, 1513, 70, 0, 85, 91.5);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, MagicCutData> magicCutItems = new HashMap<Integer, MagicCutData>();

		public static MagicCutData forId(int id) {
			return magicCutItems.get(id);
		}

		static {
			for (MagicCutData data : MagicCutData.values()) {
				magicCutItems.put(data.buttonId, data);
			}
		}

		private MagicCutData(int buttonId, int used, int result, int amount, int level, double experience) {
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
