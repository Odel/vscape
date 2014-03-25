package com.rs2.model.content.skills.Fletching;

import java.util.HashMap;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class ArrowMaking {
	public static final int ARROW_COUNT = 15;

	public enum ArrowData {// you attach item 1 with item2
		ARROW_SHAFT(314, 52, 53, 1, 0.4), BRONZE_ARROW(39, 53, 882, 1, 1.3), IRON_ARROW(40, 53, 884, 15, 2.5), STEEL_ARROW(41, 53, 886, 30, 5), MITHRIL_ARROW(42, 53, 888, 45, 7.5), ADAMANT_ARROW(43, 53, 890, 60, 10), RUNE_ARROW(44, 53, 892, 75, 12.5),

		BRONZE_DART(819, 314, 806, 1, 1.8), IRON_DART(820, 314, 807, 22, 3.8), STEEL_DART(821, 314, 808, 37, 7.5), MITHRIL_DART(822, 314, 809, 52, 11.2), ADAMANT_DART(823, 314, 810, 67, 15), RUNE_DART(824, 314, 811, 81, 18.8),

		BRONZE_BRUTAL_ARROW(4819, 53, 4773, 7, 1.4), IRON_BRUTAL_ARROW(4820, 53, 4778, 18, 2.6), STEEL_BRUTAL_ARROW(1539, 53, 4783, 33, 5.1), BLACK_BRUTAL_ARROW(4821, 53, 4788, 38, 6.4), MITHRIL_BRUTAL_ARROW(4822, 53, 4793, 49, 7.5), ADAMANT_BRUTAL_ARROW(4823, 53, 4798, 62, 10.1), RUNE_BRUTAL_ARROW(4824, 53, 4803, 77, 12.5),

		PEARL_BOLT(46, 877, 880, 41, 3.2);

		private int item1;
		private int item2;
		private int finalItem;
		private int level;
		private double experience;

		@SuppressWarnings("unused")
		private static Map<String, ArrowData> arrow = new HashMap<String, ArrowData>();

		public static ArrowData forId(int itemUsed, int usedWith) {
			for (ArrowData arrowData : ArrowData.values()) {
				if ((arrowData.item1 == itemUsed && arrowData.item2 == usedWith) || (arrowData.item2 == itemUsed && arrowData.item1 == usedWith))
					return arrowData;
			}
			return null;
		}

		private ArrowData(int item1, int item2, int finalItem, int level, double experience) {
			this.item1 = item1;
			this.item2 = item2;
			this.finalItem = finalItem;
			this.level = level;
			this.experience = experience;
		}

		public int getItem1() {
			return item1;
		}

		public int getItem2() {
			return item2;
		}

		public int getFinalItem() {
			return finalItem;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}
	}

	public static boolean perform(final Player player, int itemUsed, int usedWith) {
		final ArrowData arrowData = ArrowData.forId(itemUsed, usedWith);
		if (arrowData == null)
			return false;
		if (!Constants.FLETCHING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		if (player.getSkill().getLevel()[Skill.FLETCHING] < arrowData.getLevel()) {
			player.getDialogue().sendStatement("You need a fletching level of " + arrowData.getLevel() + " to do this");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(arrowData.getItem1()) || !player.getInventory().getItemContainer().contains(arrowData.getItem2())) {
			player.getDialogue().sendStatement("You need " + ARROW_COUNT + " " + new Item(arrowData.getItem1()).getDefinition().getName().toLowerCase() + " and " + ARROW_COUNT + " " + new Item(arrowData.getItem2()).getDefinition().getName().toLowerCase() + "s to make this");
			return true;
		}
		int factor = 1;
		if (player.getStatedInterface().contains("brutal")) {
			if (!player.getInventory().getItemContainer().contains(2347)) {
				player.getDialogue().sendStatement("You need a hammer to make brutal arrows");
				return true;
			}
			factor = 4;
		}
		if (player.getInventory().getItemContainer().freeSlots() < 1 && !player.getInventory().playerHasItem(arrowData.getFinalItem())) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return true;
		}
		final int multiplier = factor;

		final int task = player.getTask();
		int count1 = player.getInventory().getItemContainer().getCount(arrowData.getItem1()) < ARROW_COUNT ? player.getInventory().getItemContainer().getCount(arrowData.getItem1()) : ARROW_COUNT;
		int count2 = player.getInventory().getItemContainer().getCount(arrowData.getItem2()) < ARROW_COUNT ? player.getInventory().getItemContainer().getCount(arrowData.getItem2()) : ARROW_COUNT;
		final int count = count1 < count2 ? count1 : count2;
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || !player.getInventory().playerHasItem(new Item(arrowData.getItem1(), count)) || !player.getInventory().playerHasItem(new Item(arrowData.getItem2(), count))) {
					container.stop();
					return;
				}
				player.getActionSender().sendMessage("You attach the " + new Item(arrowData.getItem1()).getDefinition().getName().toLowerCase() + " to " + count / multiplier + " " + new Item(arrowData.getItem2()).getDefinition().getName().toLowerCase() + "s.");
				player.getInventory().removeItem(new Item(arrowData.getItem1(), count));
				player.getInventory().removeItem(new Item(arrowData.getItem2(), count));
				player.getInventory().addItem(new Item(arrowData.getFinalItem(), count / multiplier));
				player.getSkill().addExp(Skill.FLETCHING, count / multiplier * (arrowData.getExperience()));
				container.stop();
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
		return true;
	}
}