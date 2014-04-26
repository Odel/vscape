package com.rs2.model.content.skills;

import java.util.Random;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;

public class SkillHandler {

	public static final Random r = new Random(System.currentTimeMillis());

	/**
	 * Skill Experience Multipliers
	 */
	public static final int[] EXP_RATE = { // Multiplier
	1 // ATTACK
			, 1 // DEFENCE
			, 1 // STRENGTH
			, 1 // HITPOINTS
			, 1 // RANGE
			, 1 // PRAYER
			, 1 // MAGIC
			, 1 // COOKING
			, 1 // WOODCUTTING
			, 1 // FLETCHING
			, 1 // FISHING
			, 1 // FIREMAKING
			, 1 // CRAFTING
			, 1 // SMITHING
			, 1 // MINING
			, 1 // HERBLORE
			, 1 // AGILITY
			, 1 // THIEVING
			, 1 // SLAYER
			, 1 // FARMING
			, 1 // RUNECRAFTING
	};

	public static final String[] skillNames = {"Attack", "Defence", "Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting"};

	public static String getLine() {
		return "";// "\\n\\n\\n\\n";
	}

	public static boolean hasRequiredLevel(final Player player, int skillId, int lvlReq, String event) {
		if (player.getSkill().getPlayerLevel(skillId) < lvlReq) {
			player.getDialogue().sendStatement("You need a " + skillNames[skillId] + " level of " + lvlReq + " to " + event + ".");
			return false;
		}
		return true;
	}

	public static boolean skillCheck(int level, int levelRequired, int itemBonus) {
		double chance = 0.0;
		double baseChance = Math.pow(10d-levelRequired/10d, 2d)/2d;/*levelRequired < 11 ? 15 : levelRequired < 51 ? 10 : 5*/
		chance = baseChance + ((level - levelRequired) / 2d) + (itemBonus / 10d);
		return chance >= (new Random().nextDouble() * 100.0);
	}

    public static boolean checkObject(int object, int x, int y, int h) {
        return getObject(object, x, y, h) != null;
    }

	public static GameObjectDef getObject(int object, int x, int y, int h) {
		final GameObject p = ObjectHandler.getInstance().getObject(object, x, y, h);
		if (p != null) {
			return p.getDef();
		}
		final CacheObject g = ObjectLoader.object(object, x, y, h);
		if (g != null) {
			return g.getDef();
		}
		return null;
	}

	public static int getFace(int object, int x, int y, int h) {
		final CacheObject g = ObjectLoader.object(object, x, y, h);
		if (g != null) {
			return g.getRotation();
		}
		return 0;
	}

	public static int getType(int object, int x, int y, int h) {
		final CacheObject g = ObjectLoader.object(object, x, y, h);
		if (g != null) {
			return g.getType();
		}
		return 10;
	}

	public static boolean doSpawnEvent(Player player) {
		if (player.getSpawnedNpc() != null) {
			return false;
		}
		return Misc.random(3000) == 0;
	}

}