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
		CREAM(59238, 1927, 2130, 21, 18), 
		BUTTER(59239, 2130, 6697, 38, 40),
		CHEESE(59240, 6697, 1985, 48, 64);

		private int buttonId;
		private int item;
		private int result;
		private int level;
		private double experience;
		
		public static ChurnData forId(int buttonId) {
			for (ChurnData churnData : ChurnData.values()) {
				if (buttonId == churnData.buttonId)
					return churnData;
			}
			return null;
		}

		private ChurnData(int buttonId, int item, int result, int level, double experience) {
			this.buttonId = buttonId;
			this.item = item;
			this.result = result;
			this.level = level;
			this.experience = experience;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getItem() {
			return item;
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
			player.getActionSender().removeInterfaces();
			if (player.getSkill().getLevel()[Skill.COOKING] < churnData.getLevel()) {
				player.getDialogue().sendStatement("You need a cooking level of " + churnData.getLevel() + " to make this.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(churnData.getItem())) {
				player.getDialogue().sendStatement("You need a " + new Item(churnData.getItem()).getDefinition().getName().toLowerCase() + " to make this");
				return true;
			}
		}
		
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {

				if (!player.checkTask(task) || !player.getInventory().getItemContainer().contains(churnData.getItem())) {
					container.stop();
					return;
				}
				player.getUpdateFlags().sendAnimation(CHURN_ANIMATION);
				player.getInventory().removeItem(new Item(churnData.getItem()));
				if(churnData.getItem() == 1927){
					player.getInventory().addItem(new Item(1925));		
				}
				if(churnData.getResult() == 6697){
					player.getInventory().removeItem(new Item(1927));		
					player.getInventory().addItem(new Item(1925));		
				}
				player.getInventory().addItem(new Item(churnData.getResult()));
				player.getSkill().addExp(Skill.COOKING, churnData.getExperience());
				player.getActionSender().sendMessage("You make a " + ItemDefinition.forId(churnData.getResult()).getName().toLowerCase() + ".");
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
