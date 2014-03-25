package com.rs2.model.content.skills.Crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 17:17 To change
 * this template use File | Settings | File Templates.
 */
public class Weaving {
	// 3224 5418 5376
	public static final int WEAVE = 895;

	public static enum WeaveItem {
		CLOTH(34185, 1759, 3224, 1, 4, 10, 12), CLOTH5(34184, 1759, 3224, 5, 4, 10, 12), CLOTH10(34183, 1759, 3224, 10, 4, 10, 12), CLOTHX(34182, 1759, 3224, 0, 4, 10, 12), SACKS(34189, 5931, 5418, 1, 4, 21, 38), SACKS5(34188, 5931, 5418, 5, 4, 21, 38), SACKS10(34187, 5931, 5418, 10, 4, 21, 38), SACKSX(34186, 5931, 5418, 0, 4, 21, 38), BASKET(34193, 5933, 5376, 1, 6, 36, 56), BASKET5(34192, 5933, 5376, 5, 6, 36, 56), BASKET10(34191, 5933, 5376, 10, 6, 36, 56), BASKETX(34190, 5933, 5376, 0, 6, 36, 56);

		private int buttonId;
		private int used2;
		private int result;
		private int amount;
		private int amountNeeded;
		private int level;
		private double experience;

		public static HashMap<Integer, WeaveItem> weavingItems = new HashMap<Integer, WeaveItem>();

		public static WeaveItem forId(int id) {
			return weavingItems.get(id);
		}

		static {
			for (WeaveItem data : WeaveItem.values()) {
				weavingItems.put(data.buttonId, data);
			}
		}

		private WeaveItem(int buttonId, int used2, int result, int amount, int amountNeeded, int level, double experience) {
			this.buttonId = buttonId;
			this.used2 = used2;
			this.result = result;
			this.amount = amount;
			this.amountNeeded = amountNeeded;
			this.level = level;
			this.experience = experience;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getUsed2() {
			return used2;
		}

		public int getResult() {
			return result;
		}

		public int getAmount() {
			return amount;
		}

		public int getAmountNeeded() {
			return amountNeeded;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}
	public static boolean weave(final Player player, int buttonId, final int amount) {
		final WeaveItem weaveItem = WeaveItem.forId(buttonId);
		if (weaveItem == null || (weaveItem.getAmount() == 0 && amount == 0))
			return false;

		if (player.getStatedInterface() == "weaving") {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (player.getInventory().getItemAmount(weaveItem.getUsed2()) < weaveItem.getAmountNeeded()) {
				player.getDialogue().sendStatement("You need " + weaveItem.getAmountNeeded() + " " + new Item(weaveItem.getUsed2()).getDefinition().getName().toLowerCase() + "s to do this.");
				return true;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < weaveItem.getLevel()) {
				player.getDialogue().sendStatement("You need a crafting level of " + weaveItem.getLevel() + " to make this.");
				return true;
			}
		    player.getActionSender().removeInterfaces();
			player.getUpdateFlags().sendAnimation(WEAVE);

			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				int amnt = weaveItem.getAmount() != 0 ? weaveItem.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task) || amnt == 0 || player.getInventory().getItemAmount(weaveItem.getUsed2()) < weaveItem.getAmountNeeded()) {
						container.stop();
						return;
					}
					player.getUpdateFlags().sendAnimation(WEAVE);
					player.getActionSender().sendMessage("You weave your materials into " + new Item(weaveItem.getResult()).getDefinition().getName().toLowerCase() + "s.");
					player.getInventory().removeItem(new Item(weaveItem.getUsed2(), weaveItem.getAmountNeeded()));
					player.getInventory().addItem(new Item(weaveItem.getResult()));
					player.getSkill().addExp(Skill.CRAFTING, weaveItem.getExperience());
					amnt--;
				}

				@Override
				public void stop() {
					player.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
			return true;
		}
		return false;
	}
}
