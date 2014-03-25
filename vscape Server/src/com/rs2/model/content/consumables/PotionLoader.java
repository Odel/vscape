package com.rs2.model.content.consumables;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.rs2.util.XStreamUtil;

/**
 * By Mikey` of Rune-Server
 */
public class PotionLoader {

	@SuppressWarnings("unchecked")
	public static void loadPotionDefinitions() throws FileNotFoundException, IOException {
		System.out.println("Loading potion definitions...");
		List<PotionDefinition> list = (List<PotionDefinition>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/combat/potiondef.xml"));
		int count = 0;
		for (PotionDefinition def : list) {
			Potion.getPotionDefinitions()[count] = def;
			Potion.potionCount++;
			count++;
		}
		System.out.println("Loaded " + list.size() + " potion definitions.");
	}

	public static class PotionDefinition {

		private String potionName;
		private PotionTypes potionType;
		private int[] potionIds;
		private int[] affectedStats;
		private int[] statAddons;
		private double[] statModifiers;

		public String getPotionName() {
			return potionName;
		}

		public int[] getPotionIds() {
			return potionIds;
		}

		public int[] getAffectedStats() {
			return affectedStats;
		}

		public int[] getStatAddons() {
			return statAddons;
		}

		public double[] getStatModifiers() {
			return statModifiers;
		}

		public PotionTypes getPotionType() {
			return potionType;
		}

		enum PotionTypes {
			BOOST, RESTORE
		}

	}
}
