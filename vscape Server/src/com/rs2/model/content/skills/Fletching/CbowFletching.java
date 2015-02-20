package com.rs2.model.content.skills.Fletching;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class CbowFletching {

	private static final int ATTACH_LIMBS = 713;
	private static final int STRING_BOW = 713;
	
	public enum AttachData {// you attach item 1 with item2
		BRONZE(9440, 9420, 9454, 9, 12),
		BLURITE(9442, 9422, 9456, 24, 32),
		IRON(9444, 9423, 9457, 39, 44), 
		STEEL(9446, 9425, 9459, 46, 54), 
		MITHRIL(9448, 9427, 9461, 54, 64), 
		ADAMANT(9450, 9429, 9463, 61, 82), 
		RUNITE(9452, 9431, 9465, 69, 100);

		private int item1;
		private int item2;
		private int finalItem;
		private int level;
		private double experience;

		public static AttachData forId(int itemId, int usedWith) {
			for (AttachData attachData : AttachData.values()) {
				if ((attachData.item1 == itemId && attachData.item2 == usedWith) || (attachData.item2 == itemId && attachData.item1 == usedWith))
					return attachData;
			}
			return null;
		}

		private AttachData(int item1, int item2, int finalItem, int level, double experience) {
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
	
	public enum UnstrungData {// you attach item 1 with item2
		BRONZE(9454, 9438, 9174, 9, 6), 
		BLURITE(9456, 9438, 9176, 24, 15),
		IRON(9457, 9438, 9177, 39, 22), 
		STEEL(9459, 9438, 9179, 46, 27), 
		MITHRIL(9461, 9438, 9181, 54, 32), 
		ADAMANT(9463, 9438, 9183, 61, 41), 
		RUNITE(9465, 9438, 9185, 69, 50);

		private int item1;
		private int item2;
		private int finalItem;
		private int level;
		private double experience;

		public static UnstrungData forId(int itemId, int usedWith) {
			for (UnstrungData unstrungData : UnstrungData.values()) {
				if ((unstrungData.item1 == itemId && unstrungData.item2 == usedWith) || (unstrungData.item2 == itemId && unstrungData.item1 == usedWith))
					return unstrungData;
			}
			return null;
		}

		private UnstrungData(int item1, int item2, int finalItem, int level, double experience) {
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
	
	public static boolean attachLimbs(final Player player, int itemUsed, int usedWith) {
		final AttachData attachData = AttachData.forId(itemUsed, usedWith);
		if (attachData == null)
			return false;
		if (!Constants.FLETCHING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		if (player.getSkill().getLevel()[Skill.FLETCHING] < attachData.getLevel()) {
			player.getDialogue().sendStatement("You need a fletching level of " + attachData.getLevel() + " to do this");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(attachData.getItem1()) || !player.getInventory().getItemContainer().contains(attachData.getItem2())) {
			player.getDialogue().sendStatement("You need a " + new Item(attachData.getItem1()).getDefinition().getName().toLowerCase() + " and a " + new Item(attachData.getItem2()).getDefinition().getName().toLowerCase() + " to make this");
			return true;
		}
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {

				if (!player.checkTask(task) || !player.getInventory().getItemContainer().contains(attachData.getItem1()) || !player.getInventory().getItemContainer().contains(attachData.getItem2())) {
					container.stop();
					return;
				}
				player.getUpdateFlags().sendAnimation(ATTACH_LIMBS);
				String item1Name = new Item(attachData.getItem1()).getDefinition().getName();
				String item2Name = new Item(attachData.getItem2()).getDefinition().getName();
				String finalName = new Item(attachData.getFinalItem()).getDefinition().getName();
				player.getActionSender().sendMessage("You attach the "+item2Name+" to the "+item1Name+".");
				player.getInventory().removeItem(new Item(attachData.getItem1()));
				player.getInventory().removeItem(new Item(attachData.getItem2()));
				player.getInventory().addItem(new Item(attachData.getFinalItem()));
				player.getSkill().addExp(Skill.FLETCHING, attachData.getExperience());
				player.getActionSender().sendMessage("You make a "+finalName+".");
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
	
	public static boolean stringCbow(final Player player, int itemUsed, int usedWith) {
		final UnstrungData unstrungData = UnstrungData.forId(itemUsed, usedWith);
		if (unstrungData == null)
			return false;
		if (!Constants.FLETCHING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		if (player.getSkill().getLevel()[Skill.FLETCHING] < unstrungData.getLevel()) {
			player.getDialogue().sendStatement("You need a fletching level of " + unstrungData.getLevel() + " to do this");
			return true;
		}
		if (!player.getInventory().getItemContainer().contains(unstrungData.getItem1()) || !player.getInventory().getItemContainer().contains(unstrungData.getItem2())) {
			player.getDialogue().sendStatement("You need a " + new Item(unstrungData.getItem1()).getDefinition().getName().toLowerCase() + " and a " + new Item(unstrungData.getItem2()).getDefinition().getName().toLowerCase() + " to make this");
			return true;
		}
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {

				if (!player.checkTask(task) || !player.getInventory().getItemContainer().contains(unstrungData.getItem1()) || !player.getInventory().getItemContainer().contains(unstrungData.getItem2())) {
					container.stop();
					return;
				}
				player.getUpdateFlags().sendAnimation(STRING_BOW);
				player.getActionSender().sendSound(2606, 0, 0);
				String item1Name = new Item(unstrungData.getItem1()).getDefinition().getName();
				String finalName = new Item(unstrungData.getFinalItem()).getDefinition().getName();
				player.getActionSender().sendMessage("You add a string to the "+item1Name+".");
				player.getInventory().removeItem(new Item(unstrungData.getItem1()));
				player.getInventory().removeItem(new Item(unstrungData.getItem2()));
				player.getInventory().addItem(new Item(unstrungData.getFinalItem()));
				player.getSkill().addExp(Skill.FLETCHING, unstrungData.getExperience());
				player.getActionSender().sendMessage("You make a "+finalName+".");
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
