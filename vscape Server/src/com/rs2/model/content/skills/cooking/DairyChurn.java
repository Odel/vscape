package com.rs2.model.content.skills.cooking;

import java.util.HashMap;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 23/12/11 Time: 20:42 To change
 * this template use File | Settings | File Templates.
 */
public class DairyChurn {
	private static final int CHURN_ANIMATION = 894;

	public static enum ChurnData {
		CREAM(59238, new int[] { 1927 }, 2130, 21, 18), BUTTER(59239, new int[] { 1927, 2130 }, 6697, 38, 40), CHEESE(59240, new int[] { 1927, 2130, 6697 }, 1985, 48, 64);

		private int buttonId;
		private int[] used;
		private int result;
		private int level;
		private double experience;

		public static HashMap<Integer, ChurnData> churnItems = new HashMap<Integer, ChurnData>();

		public static ChurnData forId(int id) {
			return churnItems.get(id);
		}

		static {
			for (ChurnData data : ChurnData.values()) {
				churnItems.put(data.buttonId, data);
			}
		}

		private ChurnData(int buttonId, int[] used, int result, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.result = result;
			this.level = level;
			this.experience = experience;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int[] getUsed() {
			return used;
		}

		public int getResult() {
			return result;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}

	public static boolean churnItem(final Player player, int buttonId) {
		final ChurnData churnData = ChurnData.forId(buttonId);

		if (churnData == null)
			return false;
		if (player.getStatedInterface() == "dairyChurn") {
			if (player.getSkill().getLevel()[Skill.COOKING] < churnData.getLevel()) {
				player.getDialogue().sendStatement("You need a cooking level of " + churnData.getLevel() + " to make this.");
				return true;
			}

			player.getActionSender().removeInterfaces();

			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				boolean hasItems = true;

				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task)) {
						player.setTempInteger(0);
						container.stop();
						return;
					}
					hasItems = true;
					for (int i = 0; i <= churnData.getUsed().length - 1; i++) {
						if (!player.getInventory().getItemContainer().contains(churnData.getUsed()[i]))
							hasItems = false;
					}
					container.setTick(5);

					if (!hasItems) {
						player.getDialogue().sendStatement("You don't have the required items to use the churn.");
						container.stop();
						return;
					}
					player.getUpdateFlags().sendAnimation(CHURN_ANIMATION);
					player.getActionSender().sendMessage("You make a " + ItemDefinition.forId(churnData.getResult()).getName().toLowerCase() + ".");
					for (int i = 0; i < churnData.getUsed().length; i++)
						player.getInventory().removeItem(new Item(churnData.getUsed()[i]));
					player.getInventory().addItem(new Item(churnData.getResult()));
					player.getInventory().addItem(new Item(FlourRelated.BUCKET));
					player.getSkill().addExp(Skill.COOKING, churnData.getExperience());

				}

				@Override
				public void stop() {
					player.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
			return true;
		}
		return false;
	}
}
