package com.rs2.model.content.skills.cooking;

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
		CREAM1(34185, 1927, 2130, 1, 21, 18), 
		CREAM5(34184, 1927, 2130, 5, 21, 18), 
		CREAM10(34183, 1927, 2130, 10, 21, 18),
		CREAM_X(34182, 1927, 2130, -1, 21, 18),
		BUTTER1(34189, 1927, 6697, 1, 38, 40),
		BUTTER5(34188, 1927, 6697, 5, 38, 40),
		BUTTER10(34187, 1927, 6697, 10, 38, 40),
		BUTTER_X(34186, 1927, 6697, 10, 38, 40),
		CHEESE1(34193, 6697, 1985, -1, 48, 64),
		CHEESE5(34192, 6697, 1985, 5, 48, 64),
		CHEESE10(34191, 6697, 1985, 10, 48, 64),
		CHEESE_X(34190, 6697, 1985, -1, 48, 64);

		private int buttonId;
		private int item;
		private int result;
		private int amount;
		private int level;
		private double experience;
		
		public static ChurnData forId(int buttonId) {
			for (ChurnData churnData : ChurnData.values()) {
				if (buttonId == churnData.buttonId)
					return churnData;
			}
			return null;
		}

		private ChurnData(int buttonId, int item, int result, int amount,int level, double experience) {
			this.buttonId = buttonId;
			this.item = item;
			this.result = result;
			this.amount = amount;
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
		
		public int getAmount() {
			return amount;
		}

	}

	public static void churnItem(final Player player, final ChurnData churnData, final int amount) {
		if (player.getSkill().getLevel()[Skill.COOKING] < churnData.getLevel()) {
			player.getDialogue().sendStatement("You need a cooking level of " + churnData.getLevel() + " to make this.");
			return;
		}
		if (!player.getInventory().getItemContainer().contains(churnData.getItem())) {
			player.getDialogue().sendStatement("You need a " + new Item(churnData.getItem()).getDefinition().getName().toLowerCase() + " to make this.");
			return;
		}
		if(amount == -1) {
		    return;
		}
		player.getActionSender().removeInterfaces();
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			int makeAmount = amount;
			@Override
			public void execute(CycleEventContainer container) {

				if (!player.checkNewSkillTask() || !player.checkTask(task) || churnData == null || !player.getInventory().getItemContainer().contains(churnData.getItem()) || makeAmount == 0) {
					container.stop();
					return;
				}
				player.getActionSender().removeInterfaces();
				player.getUpdateFlags().sendAnimation(CHURN_ANIMATION);
				player.getInventory().removeItem(new Item(churnData.getItem()));
				if(churnData.getItem() == 1927){
					player.getInventory().addItemOrDrop(new Item(1925));
				}
				player.getInventory().addItemOrDrop(new Item(churnData.getResult()));
				player.getSkill().addExp(Skill.COOKING, churnData.getExperience());
				player.getActionSender().sendMessage("You make a " + ItemDefinition.forId(churnData.getResult()).getName().toLowerCase() + ".");
				makeAmount--;
				container.setTick(2);
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
	}
	
	public static boolean handleButtons(Player player, int buttonId, int amount) {
		if(player.getStatedInterface().equals("dairyChurn"))
		{
			final ChurnData churnData = ChurnData.forId(buttonId);
			if(churnData != null)
			{
				if(amount == 0)
				{
					amount = churnData.amount;
				}
				player.setNewSkillTask();
				churnItem(player,churnData,amount);
				return true;
			}
		}
		return false;
	}
}
