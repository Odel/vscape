package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.runecrafting.Runecrafting.Runes;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class RunecraftAltars {

	public enum Altar {
		AIR_ALTAR(1438, 5527, 2452, 2987, 3292, 2478, 2465, 2844, 4830), // air
		MIND_ALTAR(1448, 5529, 2453, 2982, 3512, 2479, 2466, 2782, 4841), // mind
		WATER_ALTAR(1444, 5531, 2454, 3183, 3165, 2480, 2467, 2712, 4836), // water
		EARTH_ALTAR(1440, 5535, 2455, 3304, 3474, 2481, 2468, 2654, 4841), // earth
		FIRE_ALTAR(1442, 5537, 2456, 3312, 3253, 2482, 2469, 2585, 4834), // fire
		BODY_ALTAR(1446, 5533, 2457, 3051, 3445, 2483, 2470, 2525, 4828), // body
		COSMIC_ALTAR(1454, 5539, 2458, 2407, 4379, 2484, 2471, 2138, 4833), // cosmic
		CHAOS_ALTAR(1452, 5543, 2461, 3062, 3591, 2487, 2474, 2267, 4842), // chaos
		NATURE_ALTAR(1462, 5541, 2460, 2868, 3017, 2486, 2473, 2396, 4841), // nature
		LAW_ALTAR(1458, 5545, 2459, 2859, 3379, 2485, 2472, 2464, 4827), // law
		DEATH_ALTAR(1456, 5547, 2462, 1862, 4639, 2488, 2475, 2207, 4834); // death
		// {1450, 5549, -1, -1, -1, -1, -1, -1, -1,}, // blood
		// {1460, 5551 - 1, -1, -1, -1, -1, -1, -1,}, // soul

		int talisman;
		int tiara;
		int ruinId;
		int xRuin;
		int yRuin;
		int altarId;
		int altarPortalId;
		int xAltar;
		int yAltar;

		private Altar(int talisman, int tiara, int ruinId, int xRuin, int yRuin, int altarId, int altarPortalId, int xAltar, int yAltar) {
			this.talisman = talisman;
			this.tiara = tiara;
			this.ruinId = ruinId;
			this.xRuin = xRuin;
			this.yRuin = yRuin;
			this.altarId = altarId;
			this.altarPortalId = altarPortalId;
			this.xAltar = xAltar;
			this.yAltar = yAltar;
		}
		
		public int getTalisman() {
			return talisman;
		}

		public int getTiara() {
			return tiara;
		}

		public int getRuinId() {
			return ruinId;
		}

		public int getXRuin() {
			return xRuin;
		}

		public int getYRuin() {
			return yRuin;
		}

		public int getAltarId() {
			return altarId;
		}

		public int getAltarPortalId() {
			return altarPortalId;
		}

		public int getXAltar() {
			return xAltar;
		}

		public int getYAltar() {
			return yAltar;
		}

	}

	public static Altar getAltarByTalismanId(int id) {
		for (Altar altar : Altar.values()) {
			if (id == altar.getTalisman()) {
				return altar;
			}
		}
		return null;
	}

	public static Altar getAltarRuinId(int id) {
		for (Altar altar : Altar.values()) {
			if (id == altar.getRuinId()) {
				return altar;
			}
		}
		return null;
	}

	public static Altar getAltarPortalId(int id) {
		for (Altar altar : Altar.values()) {
			if (id == altar.getAltarPortalId()) {
				return altar;
			}
		}
		return null;
	}

	public static Altar getAltarByRuinId(int talisman, int ruinId) {
		for (Altar altar : Altar.values()) {
			if (talisman == altar.getTalisman() && ruinId == altar.getRuinId()) {
				return altar;
			}
		}
		return null;
	}

	public static Altar getAltarByAltarId(int talisman, int altarId) {
		for (Altar altar : Altar.values()) {
			if (talisman == altar.getTalisman() && altarId == altar.getAltarId()) {
				return altar;
			}
		}
		return null;
	}

	public static boolean runecraftAltar(Player player, int objectId) {
		switch (objectId) {
		// Runecraft
		case 2482:
			Runecrafting.craftRunes(player, Runes.FIRE);
			return true;
		case 2480:
			Runecrafting.craftRunes(player, Runes.WATER);
			return true;
		case 2478:
			Runecrafting.craftRunes(player, Runes.AIR);
			return true;
		case 2481:
			Runecrafting.craftRunes(player, Runes.EARTH);
			return true;
		case 2479:
			Runecrafting.craftRunes(player, Runes.MIND);
			return true;
		case 2483:
			Runecrafting.craftRunes(player, Runes.BODY);
			return true;
		case 2488:
			Runecrafting.craftRunes(player, Runes.DEATH);
			return true;
		case 2486:
			Runecrafting.craftRunes(player, Runes.NATURE);
			return true;
		case 2487:
			Runecrafting.craftRunes(player, Runes.CHAOS);
			return true;
		case 2485:
			Runecrafting.craftRunes(player, Runes.LAW);
			return true;
		case 2484:
			Runecrafting.craftRunes(player, Runes.COSMIC);
			return true;
		case 7141: // blood rift
			player.getActionSender().sendMessage("This rune cannot be crafted.");
			//Runecrafting.craftRunes(player, Runes.BLOOD);
			return true;
			// case 2489 :
		case 7138: // soul rift
			player.getActionSender().sendMessage("This rune cannot be crafted.");
			//Runecrafting.craftRunes(player, Runes.SOUL);
			return true;
			// Abyss
		case 7133:
			player.getTeleportation().teleportObelisk(2400, 4835, 0);
			return true;
		case 7132:
			player.getTeleportation().teleportObelisk(2142, 4813, 0);
			return true;
		case 7129:
			player.getTeleportation().teleportObelisk(2574, 4849, 0);
			return true;
		case 7130:
			player.getTeleportation().teleportObelisk(2655, 4830, 0);
			return true;
		case 7131:
			player.getTeleportation().teleportObelisk(2523, 4826, 0);
			return true;
		case 7140:
			player.getTeleportation().teleportObelisk(2793, 4828, 0);
			return true;
		case 7139:
			player.getTeleportation().teleportObelisk(2841, 4829, 0);
			return true;
		case 7137:
			player.getTeleportation().teleportObelisk(2726, 4832, 0);
			return true;
		case 7136:
			player.getTeleportation().teleportObelisk(2208, 4830, 0);
			return true;
		case 7135:
			player.getTeleportation().teleportObelisk(2464, 4818, 0);
			return true;
		case 7134:
			player.getTeleportation().teleportObelisk(2281, 4837, 0);
			return true;
		default:
			return false;
		}
	}

	public static boolean clickRuin(Player player, int objectId) {
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		Altar ruinAltar = getAltarRuinId(objectId);
		if (ruinAltar != null) {
			if(!QuestHandler.questCompleted(player, 5))
			{
				player.getDialogue().sendStatement("You must complete Rune Mysteries","to access this skill.");
				return false;
			}
			player.sendTeleport(ruinAltar.getXAltar(), ruinAltar.getYAltar(), 0);
			player.getActionSender().sendMessage("You feel a powerful force take hold of you...");
			return true;
		}
		Altar portalAltar = getAltarPortalId(objectId);
		if (portalAltar != null) {
			if(!QuestHandler.questCompleted(player, 5))
			{
				player.getDialogue().sendStatement("You must complete Rune Mysteries","to access this skill.");
				return false;
			}
			if (objectId == 2471 && !player.hasKilledTreeSpirit()) {
				player.sendTeleport(3201, 3169, 0);
			} else if (objectId == 2472 && player.hasCombatEquipment()) {
				player.sendTeleport(3048, 3234, 0);
			} else {
				player.sendTeleport(portalAltar.getXRuin(), portalAltar.getYRuin(), 0);
			}
			player.getActionSender().sendMessage("You step through the portal...");
			return true;
		}
		return false;
	}

	public static boolean useTaliOnRuin(final Player player, final int itemId, int objectId) {
		final Altar altar = getAltarByRuinId(itemId, objectId);
		if (altar == null) {
			return false;
		}
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		if(!QuestHandler.questCompleted(player, 5))
		{
			player.getDialogue().sendStatement("You must complete Rune Mysteries","to access this skill.");
			return false;
		}
		player.getActionSender().sendMessage("You hold the " + ItemManager.getInstance().getItemName(altar.getTalisman()) + " towards the mysterious ruins.");
		player.getUpdateFlags().sendAnimation(827);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You feel a powerful force take hold of you...");
				player.sendTeleport(altar.getXAltar(), altar.getYAltar(), 0);
				container.stop();
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
		return true;
	}

}
