package com.rs2.model.content.skills.herblore;

import java.util.HashMap;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 20/12/11 Time: 22:55 To change
 * this template use File | Settings | File Templates.
 */
public class Cleaning {

	public enum CleanData {// identify , clean, level, experience
		GUAM(199, 249, 1, 2.5), MARRENTILL(201, 251, 5, 3.8), TARROMIN(203, 253, 11, 5), HARRALANDER(205, 255, 20, 6.3), RANARR(207, 257, 25, 7.5), TOADFLAX(3049, 2998, 30, 8), IRIT(209, 259, 40, 8.8), AVANTOE(211, 261, 48, 10), KWUARM(213, 263, 54, 11.3), SNAPDRAGON(3051, 3000, 59, 11.8), CADANTINE(215, 265, 65, 12.5), LANTADYME(2485, 2481, 67, 13.1), DWARF_WEED(217, 267, 70, 13.8), TORSTOL(219, 269, 75, 15);

		private int identifyId;
		private int cleanId;
		private int level;
		private double experience;

		private static Map<Integer, CleanData> clean = new HashMap<Integer, CleanData>();

		public static CleanData forId(int id) {
			return clean.get(id);
		}

		static {
			for (CleanData data : CleanData.values()) {
				clean.put(data.identifyId, data);
			}
		}

		private CleanData(int identifyId, int cleanId, int level, double experience) {
			this.identifyId = identifyId;
			this.cleanId = cleanId;
			this.level = level;
			this.experience = experience;
		}

		public int getIdentifyId() {
			return identifyId;
		}

		public int getCleanId() {
			return cleanId;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}
	}

	public static boolean handleCleaning(Player player, int itemId, int itemSlot) {
		CleanData herbloring = CleanData.forId(itemId);
		if (herbloring != null) {
			if (!Constants.HERBLORE_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (player.getSkill().getLevel()[Skill.HERBLORE] < herbloring.getLevel()) {
				player.getDialogue().sendStatement("You need a herblore level of " + herbloring.getLevel() + " to identifiy this herb.");
				return true;
			}
			player.getSkill().addExp(Skill.HERBLORE, herbloring.getExperience());

			if (player.getInventory().removeItemSlot(new Item(itemId), itemSlot)) {
				player.getInventory().addItemToSlot(new Item(herbloring.getCleanId()), itemSlot);
			} else if (player.getInventory().removeItem(new Item(itemId))) {
				player.getInventory().addItem(new Item(herbloring.getCleanId()));
			}
			player.getActionSender().sendMessage("You identify the herb, it's a " + new Item(herbloring.getCleanId()).getDefinition().getName().toLowerCase().replace("clean", "") + "");
			return true;
		}
		return false;
	}
}
