package com.rs2.model.content.skills.Fletching;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 12:50 To change
 * this template use File | Settings | File Templates.
 */
public class BowStringing {
	private static final int STRING_BOW = 713;

	public enum LoadData {// you attach item 1 with item2
		SHORT_BOW(50, 1777, 841, 5, 5), LONG_BOW(48, 1777, 839, 10, 10), OAK_SHORT_BOW(54, 1777, 843, 20, 16.5), OAK_LONG_BOW(56, 1777, 845, 25, 25), COMPOSITE_BOW(4825, 1777, 4827, 30, 45), WILLOW_SHORT_BOW(60, 1777, 849, 35, 33.3), WILLOW_LONG_BOW(58, 1777, 847, 40, 41.5), MAPLE_SHORT_BOW(64, 1777, 853, 50, 50), MAPLE_LONG_BOW(62, 1777, 851, 55, 58.3), YEW_SHORT_BOW(68, 1777, 857, 65, 68.5), YEW_LONG_BOW(66, 1777, 855, 70, 75), MAGIC_SHORT_BOW(72, 1777, 861, 80, 83.3), MAGIC_LONG_BOW(70,
				1777, 859, 85, 91.5);

		private int item1;
		private int item2;
		private int finalItem;
		private int level;
		private double experience;

		public static LoadData forId(int itemId, int usedWith) {
			for (LoadData loadData : LoadData.values()) {
				if ((loadData.item1 == itemId && loadData.item2 == usedWith) || (loadData.item2 == itemId && loadData.item1 == usedWith))
					return loadData;
			}
			return null;
		}

		private LoadData(int item1, int item2, int finalItem, int level, double experience) {
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
		final LoadData loadData = LoadData.forId(itemUsed, usedWith);
		if (loadData == null)
			return false;
		if (!Constants.FLETCHING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		if (player.getSkill().getLevel()[Skill.FLETCHING] < loadData.getLevel()) {
			player.getDialogue().sendStatement("You need a fletching level of " + loadData.getLevel() + " to do this");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(loadData.getItem1()) || !player.getInventory().getItemContainer().contains(loadData.getItem2())) {
			player.getDialogue().sendStatement("You need a " + new Item(loadData.getItem1()).getDefinition().getName().toLowerCase() + " and a " + new Item(loadData.getItem2()).getDefinition().getName().toLowerCase() + " to make this");
			return true;
		}

		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {

				if (!player.checkTask(task) || !player.getInventory().getItemContainer().contains(loadData.getItem1()) || !player.getInventory().getItemContainer().contains(loadData.getItem2())) {
					container.stop();
					return;
				}
				player.getUpdateFlags().sendAnimation(STRING_BOW);
				player.getActionSender().sendSound(2606, 0, 0);
				player.getActionSender().sendMessage("You add a string to the bow.");
				player.getInventory().removeItem(new Item(loadData.getItem1()));
				player.getInventory().removeItem(new Item(loadData.getItem2()));
				player.getInventory().addItem(new Item(loadData.getFinalItem()));
				player.getSkill().addExp(Skill.FLETCHING, loadData.getExperience());
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
