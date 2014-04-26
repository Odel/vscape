package com.rs2.model.content.skills.Crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class PotteryMaking {

	public static final int SOFT_CLAY = 1761;
	private static final int POTTERY_ANIM_UNFIRED = 894;
	private static final int POTTERY_ANIM_FIRED = 896;

	public static enum Pottery {
		POT(34245, 1761, 1787, 1931, 1, 1, 6.3, 6.3), POT5(34244, 1761, 1787, 1931, 5, 1, 6.3, 6.3), POT10(34243, 1761, 1787, 1931, 10, 1, 6.3, 6.3), POTX(34242, 1761, 1787, 1931, 0, 1, 6.3, 6.3), DISH(34249, 1761, 1789, 2313, 1, 7, 15, 10), DISH5(34248, 1761, 1789, 2313, 5, 7, 15, 10), DISH10(34247, 1761, 1789, 2313, 10, 7, 15, 10), DISHX(34246, 1761, 1789, 2313, 0, 7, 15, 10), BOWL(34253, 1761, 1791, 1923, 1, 8, 18, 15), BOWL5(34252, 1761, 1791, 1923, 5, 8, 18, 15), BOWL10(34251, 1761, 1791, 1923, 10, 8, 18, 15), BOWLX(34250, 1761, 1791, 1923, 0, 8, 18, 15), PLANT(35001, 1761, 5352, 5350, 1, 19, 20, 17.5), PLANT5(35000, 1761, 5352, 5350, 5, 19, 20, 17.5), PLANT10(34999, 1761, 5352, 5350, 10, 19, 20, 17.5), PLANTX(34998, 1761, 5352, 5350, 0, 19, 20, 17.5), LID(35005, 1761, 4438, 4440, 1, 25, 20, 20), LID5(35004, 1761, 4438, 4440, 5, 25, 20, 20), LID10(35003, 1761, 4438, 4440, 10, 25, 20, 20), LIDX(35002, 1761, 4438, 4440, 0, 25, 20, 20);

		private int buttonId;
		private int used;
		private int resultUnfired;
		private int resultFired;
		private int amount;
		private int level;
		private double experienceUnfired;
		private double experienceFired;

		public static HashMap<Integer, Pottery> potteryItems = new HashMap<Integer, Pottery>();

		public static Pottery forId(int id) {
			return potteryItems.get(id);
		}

		static {
			for (Pottery data : Pottery.values()) {
				potteryItems.put(data.buttonId, data);
			}
		}

		private Pottery(int buttonId, int used, int resultUnfired, int resultFired, int amount, int level, double experienceUnfired, double experienceFired) {
			this.buttonId = buttonId;
			this.used = used;
			this.resultUnfired = resultUnfired;
			this.resultFired = resultFired;
			this.amount = amount;
			this.level = level;
			this.experienceUnfired = experienceUnfired;
			this.experienceFired = experienceFired;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getUsed() {
			return used;
		}

		public int getResultUnfired() {
			return resultUnfired;
		}

		public int getResultFired() {
			return resultFired;
		}

		public int getAmount() {
			return amount;
		}

		public int getLevel() {
			return level;
		}

		public double getExperienceUnfired() {
			return experienceUnfired;
		}

		public double getExperienceFired() {
			return experienceFired;
		}
	}
	public static boolean makePottery(final Player player, int buttonId, final int amount) {
		/* first step : making unfired */
		final Pottery pottery = Pottery.forId(buttonId);
		if (pottery == null || (pottery.getAmount() == 0 && amount == 0)) {
			return false;
		}
		if (pottery.getUsed() == SOFT_CLAY && player.getStatedInterface() == "potteryUnfired") {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(SOFT_CLAY)) {
				player.getDialogue().sendStatement("You need soft clay to do this.");
				return true;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < pottery.getLevel()) {
				player.getDialogue().sendStatement("You need a level of " + pottery.getLevel() + " to make this.");
				return true;
			}
		    player.getActionSender().removeInterfaces();
			player.getUpdateFlags().sendAnimation(POTTERY_ANIM_UNFIRED);
			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				int amnt = pottery.getAmount() != 0 ? pottery.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task) || amnt == 0 || !player.getInventory().getItemContainer().contains(SOFT_CLAY)) {
						container.stop();
						return;
					}
					container.setTick(3);
					player.getUpdateFlags().sendAnimation(POTTERY_ANIM_UNFIRED);
					player.getActionSender().sendMessage("You make the soft clay into " + Menus.determineAorAn(new Item(pottery.getResultUnfired()).getDefinition().getName().toLowerCase()) + " " + new Item(pottery.getResultUnfired()).getDefinition().getName().toLowerCase() + ".");
					player.getInventory().removeItem(new Item(SOFT_CLAY));
					player.getInventory().addItem(new Item(pottery.getResultUnfired()));
					player.getSkill().addExp(Skill.CRAFTING, pottery.getExperienceUnfired());
					amnt--;

				}

				@Override
				public void stop() {
					player.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
			return true;

		}
		/* Second step making fired */
		else if (player.getStatedInterface() == "potteryFired") {

			if (!player.getInventory().getItemContainer().contains(pottery.getResultUnfired())) {
				player.getDialogue().sendStatement("You need an " + new Item(pottery.getResultUnfired()).getDefinition().getName().toLowerCase() + " to do this.");
				return true;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < pottery.getLevel()) {
				player.getActionSender().sendMessage("You need a crafting level of " + pottery.getLevel() + " to make this.");
				return true;
			}
		    player.getActionSender().removeInterfaces();
			player.getUpdateFlags().sendAnimation(POTTERY_ANIM_FIRED);
			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				int amnt = pottery.getAmount() != 0 ? pottery.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task) || amnt == 0 || !player.getInventory().getItemContainer().contains(pottery.getResultUnfired())) {
						container.stop();
						return;
					}
					player.getUpdateFlags().sendAnimation(POTTERY_ANIM_FIRED);
					player.getActionSender().sendMessage("You put a " + new Item(pottery.getResultUnfired()).getDefinition().getName() + " in the oven.");
					player.getActionSender().sendMessage("You retrieve the " + new Item(pottery.getResultFired()).getDefinition().getName() + " from the oven.");
					player.getInventory().removeItem(new Item(pottery.getResultUnfired()));
					player.getInventory().addItem(new Item(pottery.getResultFired()));
					player.getSkill().addExp(Skill.CRAFTING, pottery.getExperienceFired());
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