package com.rs2.model.content.consumables;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Food {
	/*
	 * HAVENT DONE YET
	 * 
	 * 
		THINSNAIL(5 + (int) (Math.random() * (2 + 1)), -1, new int[]{3369}),
		LEANSNAIL(6 + (int) (Math.random() * (2 + 1)), -1, new int[]{3371}), 
		FATSNAIL(7 + (int) (Math.random() * (2 + 1)), -1, new int[]{3373}), 
		SLIMYEEL(6 + (int) (Math.random() * (4 + 1)), -1, new int[]{3381}), 
		CAVEEEL(8 + (int) (Math.random() * (4 + 1)), -1, new int[]{5003, 5007}), 
		SPIDERONSTICK(7 + (int) (Math.random() * (11 + 1)), -1, new int[]{6293, 6295, 6297, 6299, 6303}),
	 */
	
	public static class FoodDef {
		public enum FoodType {
			DEFAULT, STAGES
		}
		public FoodType type = FoodType.DEFAULT;
		public int heal = 0;
		public int[] items;
		
		public FoodDef(FoodType type, int heal, int[] items)
		{
			this.type = type;
			this.heal = heal;
			this.items = items;
		}
		
		public int getNextItem(int itemId){
			if(type == FoodType.STAGES)
			{
				if(items.length > 1)
				{
					int index = Arrays.binarySearch(items, itemId);
					if(index >= 0 && (index+1) < items.length)
					{
						return items[index+1];
					}
				}
			}
			return -1;
		}
		
		public int getLastItem() {
			if(type == FoodType.STAGES)
			{
				if(items.length > 1)
				{
					return items[items.length-1];
				}
			}
			return -1;
		}
		
		public int getContainer(){
			if(type == FoodType.STAGES)
			{
				int id = getLastItem();
				if (isContainer(id))
				{
					return id;
				}
			}
			return -1;
		}
	}
	
	private static ArrayList<FoodDef> foodDefinitions = new ArrayList<FoodDef>();
	public static void init() throws IOException {
		foodDefinitions.clear();
		FileReader reader = new FileReader("./datajson/content/food.json");
		try
		{
			foodDefinitions = new Gson().fromJson(reader, new TypeToken<List<FoodDef>>(){}.getType());
			reader.close();
			System.out.println("Loaded " + foodDefinitions.size() + " food definitions json.");
		} catch (IOException e) {
			reader.close();
			System.out.println("failed to load food definitions json.");
		}
	}
	
	Player player;

	public Food(Player player) {
		this.player = player;
	}
	
	public boolean eatFood(int id, int slot) {
		if (player.isDead()) {
			return false;
		}

		if (RulesData.NO_FOOD.activated(player)) {
			player.getActionSender().sendMessage("Usage of foods have been disabled during this fight!");
			return true;
		}
		FoodDef def = forId(id);
		if(def == null)
			return false;
		int foodTimer = def.type != FoodDef.FoodType.STAGES ? 600 : 1800;
		if (player.getSkill().canDoAction(foodTimer) && player.getSkill().getLevel()[Skill.HITPOINTS] > 0) {
			player.getUpdateFlags().sendAnimation(829);
			player.getActionSender().sendSound(317, 0, 0);
			if (!player.getInventory().removeItemSlot(new Item(id, 1), slot)) {
				player.getInventory().removeItem(new Item(id, 1));
			}
			String name = ItemManager.getInstance().getItemName(id);
			player.getActionSender().sendMessage("You eat the " + name.toLowerCase() + ".");
			int newItem = def.getNextItem(id);
			if (newItem != -1) {
				player.getInventory().addItemOrDrop(new Item(newItem, 1));
			}
			int heal = def.heal;
			if (id == 1971) {
				heal = eatKebab();
			}
			eatEasterEgg(id);
			doOtherEffects(id);
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
	
	public static FoodDef forId(int itemId) {
		if (isContainer(itemId))
		{
			return null;
		}
		for (FoodDef def : foodDefinitions) {
			for(int item : def.items)
			{
				if(item == itemId)
				{
					return def;
				}
			}
		}
		return null;
	}
	
	public static boolean isContainer(int id){
		if (id == 1923 || id == 1925 || id == 1935 || id == 2313)
		{
			return true;
		}
		return false;
	}
}