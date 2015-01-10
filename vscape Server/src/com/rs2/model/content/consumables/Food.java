package com.rs2.model.content.consumables;

import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

import java.util.HashMap;
import java.util.Map;

public class Food {

	Player player;

	public Food(Player player) {
		this.player = player;
	}

	public enum FoodData {

		/*
		 * Health, New food ID, Food(s) Id(s)
		 */
		// Normal
		ONEHP(1, -1, new int[]{1971, 319, 1965, 1967, 2128, 1957, 1942, 2130}), 
		TWOHP(2, -1, new int[]{1963, 1985, 2126, 247, 2120, 2122, 2124, 2108, 2110, 2112, 7072, 1969, 1982, 10962, 2116, 2118, 2102, 2104, 2106, 2152, 4517}), 
		THREEHP(3, -1, new int[]{2142, 4293, 2140, 4291, 1973, 3151, 315, 1861, 5984}), 
		FOURHP(4, -1, new int[]{6701, 325, 403, 1977, 4012}), 
		FIVEHP(5, -1, new int[]{2309, 7062, 3228, 347, 7082, 7084, 7078, 4014}), 
		SIXHP(6, -1, new int[]{355, 6961, 6962, 6965}), 
		SEVENHP(7, -1, new int[]{2213, 2209, 2239, 339, 7223, 333}), 
		EIGHTHP(8, -1, new int[]{7064, 5972, 6883, 351, 2217, 2244, 2205, 2237}),
		NINEHP(9, -1, new int[]{329}), 
		TENHP(10, -1, new int[]{2878, 7228, 361}), 
		ELEVENHP(11, -1, new int[]{2259, 2149, 2277, 7066, 2255, 2281, 2253}), 
		TWELVENHP(12, -1, new int[]{379, 739, 2195, 2191}), 
		THIRTEEN(13, -1, new int[]{365}), 
		FOURTEEN(14, -1, new int[]{373, 2345, 6703, 7054}), 
		FIFTHTEEN(15, -1, new int[]{2185, 2187}), 
		SIXTEEN(16, -1, new int[]{7056, 6705, 7946}), 
		EIGHTTEEN(18, -1, new int[]{3144, 3147}), 
		NINETEEN(19, -1, new int[]{1883, 1885}), 
		TWENTY(20, -1, new int[]{7058, 385}), 
		TWENTYONE(21, -1, new int[]{397}), 
		TWENTYTWOHP(22, -1, new int[]{391, 7060}),
		
		SWEETS(2, -1, new int[]{4558, 4559, 4560, 4561, 4562, 4563, 4564}),
		
		TUNACORN(13, 1923, new int[]{7068}), 
		TUNABOWL(7, 1923, new int[]{7086}), 
		CORNBOWL(7, 1923, new int[]{7088}), 
		SWEETCORN(5, -1, new int[]{5988}), 
		
		// Other
		THINSNAIL(5 + (int) (Math.random() * (2 + 1)), -1, new int[]{3369}),
		LEANSNAIL(6 + (int) (Math.random() * (2 + 1)), -1, new int[]{3371}), 
		FATSNAIL(7 + (int) (Math.random() * (2 + 1)), -1, new int[]{3373}), 
		SLIMYEEL(6 + (int) (Math.random() * (4 + 1)), -1, new int[]{3381}), 
		CAVEEEL(8 + (int) (Math.random() * (4 + 1)), -1, new int[]{5003, 5007}), 
		SPIDERONSTICK(7 + (int) (Math.random() * (11 + 1)), -1, new int[]{6293, 6295, 6297, 6299, 6303}),

		// Pies
		REDBERRYFULL(5, 2333, new int[]{2325}), 
		REDBERRYHALF(5, 2313, new int[]{2333}), 
		MEATPIEFULL(6, 2331, new int[]{2327}), 
		MEATPIEHALF(5, 2313, new int[]{2331}), 
		FISHFULL(6, 7190, new int[]{7188}), 
		FISHHALF(6, 2313, new int[]{7190}), 
		GARDENFULL(6, 7180, new int[]{7178}), 
		GARDENHALF(6, 2313, new int[]{7180}), 
		APPLEFULL(7, 2335, new int[]{2323}), 
		APPLEHALF(7, 2313, new int[]{2335}), 
		ADMIRALFULL(8, 7200, new int[]{7198}),
		ADMIRALHALF(8, 2313, new int[]{7200}), 
		SUMMERFULL(8, 7220, new int[]{7218}),
		SUMMERLHALF(8, 2313, new int[]{7220}),
		WILDFULL(8, 7210, new int[]{7208}), 
		WILDHALF(8, 2313, new int[]{7210}),

		// Pizza
		PLAINFULL(7, 2291, new int[]{2289}), 
		PLAINHALF(7, -1, new int[]{2291}), 
		MEATFULL(8, 2295, new int[]{2293}), 
		MEATHALF(8, -1, new int[]{2295}), 
		ANCHOVYFULL(9, 2299, new int[]{2297}), 
		ANCHOVYHALF(9, -1, new int[]{2299}), 
		PINEAPPLEFULL(9, 2303, new int[]{2301}), 
		PINEAPPLEHALF(9, -1, new int[]{2303}),

		CHOCOMILK(4, 1925, new int[]{1977}), 
		JUG_OF_WINE(11, 1935, new int[]{1993}),

		// Cake
		CHOCOLATE(5, 1899, new int[]{1897}),
		CHOCOLATETWOTHIRD(5, 1901, new int[]{1899}), 
		CHOCOLATESLICE(5, -1, new int[]{1901}), 
		CAKE(4, 1893, new int[]{1891}), 
		CAKETWOTHIRD(4, 1895, new int[]{1893}), 
		CAKESLICE(4, -1, new int[]{1895}),
		
		// Stews
		STEW(11, 1923, new int[]{2003}),
		BANANA_STEW(11, 1923, new int[]{4016}),
		CURRY(19, 1923, new int[]{2011}),
		
		EASTEREGGS(10, -1,  new int[]{ 7928, 7929, 7930, 7931, 7932, 7933 });

		private static Map<Integer, FoodData> foods = new HashMap<Integer, FoodData>();

		public static FoodData forId(int id) {
			return foods.get(id);
		}

		static {
			for (FoodData food : FoodData.values()) {
				for (int id : food.foodId) {
					foods.put(id, food);
				}
			}
		}

		private int[] foodId;
		private int health;
		private int newId;

		private FoodData(int health, int newId, int[] foodId) {
			this.health = health;
			this.newId = newId;
			this.foodId = foodId;
		}

		public int getHealth() {
			return health;
		}

		public int getNewItemId() {
			return newId;
		}

		public int[] getFoodId() {
			return foodId;
		}
	}

	public boolean eatFood(int id, int slot) {
		if (player.isDead()) {
			return false;
		}

		if (RulesData.NO_FOOD.activated(player)) {
			player.getActionSender().sendMessage("Usage of foods have been disabled during this fight!");
			return true;
		}
		FoodData f = FoodData.forId(id);
		if (f == null)
			return false;
		int foodTimer = f.getNewItemId() != -1 ? 600 : 1800;
		if (player.getSkill().canDoAction(foodTimer) && player.getSkill().getLevel()[Skill.HITPOINTS] > 0) {
			player.getUpdateFlags().sendAnimation(829);
			player.getActionSender().sendSound(317, 0, 0);
			if (!player.getInventory().removeItemSlot(new Item(id, 1), slot)) {
				player.getInventory().removeItem(new Item(id, 1));
			}
			String name = ItemManager.getInstance().getItemName(id);
			player.getActionSender().sendMessage("You eat the " + name.toLowerCase() + ".");
			int heal = f.getHealth();
			if (id == 1971) {
				heal = eatKebab();
			}
			
			eatEasterEgg(id);
			
			doOtherEffects(id);
			
			if (f.getNewItemId() != -1)
				player.getInventory().addItem(new Item(f.getNewItemId()));
			player.heal(heal);
			player.getTask();
			player.getCombatDelayTick().setWaitDuration(player.getCombatDelayTick().getWaitDuration() + 2);
			if (id != 10476 && id != 1971) {
				CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (player.getSkill().getPlayerLevel(Skill.HITPOINTS) < player.getSkill().getPlayerLevel(Skill.HITPOINTS)) {
							player.getActionSender().sendMessage("It heals some health.");
						}
						container.stop();
					}
					@Override
					public void stop() {
					}
				}, 2);
			}
		}
        return true;
	}

	public int eatKebab() {
		if (player.getSkill().getLevel()[Skill.HITPOINTS] >= player.getSkill().getPlayerLevel(Skill.HITPOINTS)) {
			return 0;
		}
		int random = Misc.random(9);
		if (random == 0) {
			player.getActionSender().sendMessage("Wow, that was an amazing kebab! You feel really invigorated.");
			return 30;
		} else if (random == 1) {
			player.getActionSender().sendMessage("That kebab didn't seem to do a lot.");
			return 0;
		} else if (random < 5) {
			player.getActionSender().sendMessage("That was a good kebab. You feel a lot better.");
			return Misc.random(10) + 10;
		} else {
			player.getActionSender().sendMessage("It restores some life points.");
			return player.getSkill().getPlayerLevel(Skill.HITPOINTS) / 10;
		}
	}
	
	private void eatEasterEgg(int id)
	{
		switch(id)
		{
			case 7928:
				player.getActionSender().sendMessage("Mmm... chocolate");
				break;
			case 7929:
				player.getActionSender().sendMessage("Mmm... carrot");
				player.morphRabbit();
				break;
			case 7930:
				player.getActionSender().sendMessage("Mmm... coffee");
				break;
			case 7931:
				player.getActionSender().sendMessage("Mmm... orange");
				break;
			case 7932:
				player.getActionSender().sendMessage("Mmm... toffee");
				break;
			case 7933:
				player.getActionSender().sendMessage("Mmm... nougat");
				break;
		}
	}
	
	private void doOtherEffects(int itemId) {
		switch (itemId) {
			case 7180: // garden pie
			case 7178:
			    player.getActionSender().statEdit(Skill.FARMING, 3, true);
			    break;
			case 7188: //fish pie
			case 7190:
			    player.getActionSender().statEdit(Skill.FISHING, 3, true);
			    break;
			case 7198: //admiral pie
			case 7200:
			    player.getActionSender().statEdit(Skill.FISHING, 5, true);
			    break;
			case 7208: //wild pie
			case 7210:
			    player.getActionSender().statEdit(Skill.RANGED, 4, true);
			    player.getActionSender().statEdit(Skill.SLAYER, 5, true);
			    break;
			case 7218: //summer pie
			case 7220:
			    player.getActionSender().statEdit(Skill.AGILITY, 5, true);
			    break;
			default :
				break;
		}
	}

}