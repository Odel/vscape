package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class Tiaras {

	public enum Tiara {		
		AIR_ALTAR(1438, 5527, 2478, 1, 25), // air
		MIND_ALTAR(1448, 5529, 2479, 2, 27), // mind
		WATER_ALTAR(1444, 5531, 2480, 4, 30), // water
		EARTH_ALTAR(1440, 5535, 2481, 8, 33), // earth
		FIRE_ALTAR(1442, 5537, 2482, 16, 35), // fire
		BODY_ALTAR(1446, 5533, 2483, 31, 37), // body
		COSMIC_ALTAR(1454, 5539, 2484, 64, 40), // cosmic
		CHAOS_ALTAR(1452, 5543, 2487, 128, 42.5), // chaos
		NATURE_ALTAR(1462, 5541, 2486, 256, 45), // nature
		LAW_ALTAR(1458, 5545, 2485, 512, 47.5), // law
		DEATH_ALTAR(1456, 5547, 2488, 1024, 50); // death
		// {1450, 5549, -1, -1, -1, -1, -1, -1, -1,}, // blood
		// {1460, 5551 - 1, -1, -1, -1, -1, -1, -1,}, // soul

		int talisman;
		int tiara;
		int altarId;
		int configId;
		double exp;

		private Tiara(int talisman, int tiara, int altarId, int configId, double exp) {
			this.talisman = talisman;
			this.tiara = tiara;
			this.altarId = altarId;
			this.configId = configId;
			this.exp = exp;
		}
		
		public int getTalisman() {
			return talisman;
		}

		public int getTiara() {
			return tiara;
		}

		public int getAltarId() {
			return altarId;
		}
		
		public int getConfigId() {
			return configId;
		}
		
		public double getExp() {
			return exp;
		}
		
		public static Tiara getTiara(int tiaraId) {
			for (Tiara tiara : Tiara.values()) {
				if (tiaraId == tiara.getTiara()) {
					return tiara;
				}
			}
			return null;
		}
		
		public static Tiara getAltar(int altarId) {
			for (Tiara altar : Tiara.values()) {
				if (altarId == altar.getAltarId()) {
					return altar;
				}
			}
			return null;
		}
	}
	
	public static boolean bindTiara(Player player, int itemId, int objectId) {
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		final Tiara tiara = Tiara.getAltar(objectId);
		if (tiara == null) {
			return false;
		}
		if(!QuestHandler.questCompleted(player, 5))
		{
			player.getDialogue().sendStatement("You must complete Rune Mysteries","to access this skill.");
			return false;
		}
		if(player.carryingItem(5525) && player.carryingItem(tiara.getTalisman())){
			player.getUpdateFlags().sendAnimation(791);
			player.getInventory().removeItem(new Item(5525, 1));
			player.getInventory().removeItem(new Item(tiara.getTalisman(), 1));
			player.getInventory().addItem(new Item(tiara.getTiara(), 1));
			player.getActionSender().sendMessage("You bind the power of the talisman into the tiara.");
			player.getSkill().addExp(Skill.RUNECRAFTING, tiara.getExp());
			return true;
		}
		return false;
	}

	public static void handleTiara(Player player) {
		int id = player.getEquipment().getId(Constants.HAT);
		final Tiara tiara = Tiara.getTiara(id);
		if (tiara == null) {
			player.getActionSender().sendConfig(491, 0);
			return;
		}
		player.getActionSender().sendConfig(491, tiara.getConfigId());
	}

}
