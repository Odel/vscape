package com.rs2.model.content.skills.cooking;

import java.util.HashMap;

import com.rs2.model.content.skills.Menus;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class DoughHandler {
	
	public static final int POT_OF_FLOUR = 1933;
	public static final int BUCKET_OF_WATER = 1929;
	public static final int BUCKET = 1925;
	public static final int POT = 1931;
	
	public static enum DoughData {
		BREAD(34185, 2307, 1), BREAD5(34184, 2307, 5), BREAD10(34183, 2307, 10), BREADX(34182, 2307, 0),
		PASTRY(34189, 1953, 1), PASTRY5(34188, 1953, 5), PASTRY10(34187, 1953, 10), PASTRYX(34186, 1953, 0),
		PIZZABASE(34193, 2283, 1), PIZZABASE5(34192, 2283, 5), PIZZABASE10(34191, 2283, 10), PIZZABASEX(34190, 2283, 0);
		
		private int buttonId;
		private int result;
		private int amount;
		
		public static HashMap<Integer, DoughData> doughItems = new HashMap<Integer, DoughData>();

		public static DoughData forId(int id) {
			return doughItems.get(id);
		}

		static {
			for (DoughData data : DoughData.values()) {
				doughItems.put(data.buttonId, data);
			}
		}

		private DoughData(int buttonId, int result, int amount) {
			this.buttonId = buttonId;
			this.result = result;
			this.amount = amount;
		}

		public int getButtonId() {
			return buttonId;
		}
		
		public int getResult() {
			return result;
		}

		public int getAmount() {
			return amount;
		}
	}
	
	public static boolean handleInterface(Player player, int itemUsed, int withItem, int itemUsedSlot, int withItemSlot) {
		if ((itemUsed == BUCKET_OF_WATER && withItem == POT_OF_FLOUR) || (itemUsed == POT_OF_FLOUR && withItem == BUCKET_OF_WATER)) {
			if (player.getNewComersSide().isInTutorialIslandStage()) {
				player.getInventory().removeItem(new Item(1933));
				player.getInventory().removeItem(new Item(1929));
				player.getInventory().addItem(new Item(2307));
				player.getInventory().addItem(new Item(1925));
				player.getInventory().addItem(new Item(1931));
				if (player.getNewComersSide().getTutorialIslandStage() == 17)
					player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
				return true;
			}
			player.setNewSkillTask();
			player.setStatedInterface("flour");
			Menus.display3Item(player, 2307, 1953, 2283, "Bread Dough", "Pastry Dough", "Pizza Base");
			return true;
		}
		return false;
	}
	
	public static void handleDoughAction(final Player player,final DoughData dough, final int amount) {
		player.getActionSender().removeInterfaces();
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			int makeAmount = amount;
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkNewSkillTask() || !player.checkTask(task) || dough == null || !player.getInventory().getItemContainer().contains(BUCKET_OF_WATER) || !player.getInventory().getItemContainer().contains(POT_OF_FLOUR) || makeAmount == 0) {
					container.stop();
					return;
				}
				player.getActionSender().sendMessage("You put the water on the flour and make it into a "+new Item(dough.getResult()).getDefinition().getName().toLowerCase() +".");
				player.getInventory().removeItem(new Item(BUCKET_OF_WATER));
				player.getInventory().removeItem(new Item(POT_OF_FLOUR));
				player.getInventory().addItemOrDrop(new Item(BUCKET));
				player.getInventory().addItemOrDrop(new Item(POT));
				player.getInventory().addItemOrDrop(new Item(dough.getResult()));	
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
		if(player.getStatedInterface() == "flour")
		{
			DoughData dough = DoughData.forId(buttonId);
			if(dough != null)
			{
				if(amount == 0)
				{
					amount = dough.getAmount();
				}
				handleDoughAction(player,dough,amount);
				return true;
			}
		}
		return false;
	}
}
