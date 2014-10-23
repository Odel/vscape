package com.rs2.model.content.skills.cooking;

import com.rs2.model.content.skills.Menus;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class SliceDiceHandler {
	
	public static final int KNIFE = 946;
	
	public static enum SliceDiceData {
		LEMON(2102, 2104, 1, 2106, 1),
		ORANGE(2108, 2110, 1, 2112, 1),
		PINEAPPLE(2114, 2116, 1, 2118, 4),
		LIME(2120, 2122, 1, 2124, 1);
		
		private int srcItem;
		private int dice;
		private int diceAmt;
		private int slice;
		private int sliceAmt;
		
		private SliceDiceData(int srcItem, int dice, int diceAmt,int slice, int sliceAmt) {
			this.srcItem = srcItem;
			this.dice = dice;
			this.diceAmt = diceAmt;
			this.slice = slice;
			this.sliceAmt = sliceAmt;
		}
		
		public static SliceDiceData forId(int itemId) {
			for (SliceDiceData sliceDiceData : SliceDiceData.values()) {
					if (sliceDiceData.srcItem == itemId)
						return sliceDiceData;
			}
			return null;
		}
		
		public int getSrc() {
			return srcItem;
		}
		
		public int getDice() {
			return dice;
		}
		
		public int getDiceAmount() {
			return diceAmt;
		}
		
		public int getSlice() {
			return slice;
		}
		
		public int getSliceAmount() {
			return sliceAmt;
		}
	}

	public static boolean handleInterface(final Player player, int selectedItemId, int usedItemId, final int slot) {
		final int itemId = selectedItemId != KNIFE ? selectedItemId : usedItemId;
		final SliceDiceData sliceDiceData = SliceDiceData.forId(itemId);
		if(sliceDiceData != null) {
			if((selectedItemId == sliceDiceData.getSrc() && usedItemId != KNIFE) || (selectedItemId == KNIFE && usedItemId != sliceDiceData.getSrc()))
			{
				return false;
			}
			player.setNewSkillTask();
			player.setStatedInterface("sliceDice");
			String diceName = new Item(sliceDiceData.getDice()).getDefinition().getName();
			String sliceName = new Item(sliceDiceData.getSlice()).getDefinition().getName();
			player.setTempInteger(sliceDiceData.getSrc());
			Menus.display2Item(player, sliceDiceData.getDice(), sliceDiceData.getSlice(), diceName, sliceName);
			return true;
		}
		return false;
	}
	
	private static void handleDice(final Player player, final int amount, final int itemId)
	{
		player.getActionSender().removeInterfaces();
		if(amount == 0)
			return;
		if(!player.getInventory().playerHasItem(KNIFE) || !player.getInventory().playerHasItem(itemId)){
			return;
		}
		final SliceDiceData sliceDiceData = SliceDiceData.forId(itemId);
		if(sliceDiceData == null)
			return;
		player.setSkilling(new CycleEvent() {
			int makeAmount = amount;
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkNewSkillTask() || !player.getInventory().playerHasItem(KNIFE) || !player.getInventory().playerHasItem(itemId) || makeAmount == 0  || player.getInventory().getItemContainer().freeSlots() < sliceDiceData.getDiceAmount()) {
					container.stop();
					return;
				}
				String srcName = new Item(sliceDiceData.getSrc()).getDefinition().getName();
				String diceName = new Item(sliceDiceData.getDice()).getDefinition().getName();
				player.getInventory().removeItem(new Item(sliceDiceData.getSrc()));
				player.getInventory().addItemOrDrop(new Item(sliceDiceData.getDice(), sliceDiceData.getDiceAmount()));
				player.getActionSender().sendMessage("You dice the "+srcName+" into some "+ diceName +".");
				makeAmount--;
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
	}
	
	private static void handleSlice(final Player player, final int amount, final int itemId)
	{
		player.getActionSender().removeInterfaces();
		if(amount == 0)
			return;
		if(!player.getInventory().playerHasItem(KNIFE) || !player.getInventory().playerHasItem(itemId)){
			return;
		}
		final SliceDiceData sliceDiceData = SliceDiceData.forId(itemId);
		if(sliceDiceData == null)
			return;
		player.setSkilling(new CycleEvent() {
			int makeAmount = amount;
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkNewSkillTask() || !player.getInventory().playerHasItem(KNIFE) || !player.getInventory().playerHasItem(itemId) || makeAmount == 0  || player.getInventory().getItemContainer().freeSlots() < sliceDiceData.getSliceAmount()) {
					container.stop();
					return;
				}
				String srcName = new Item(sliceDiceData.getSrc()).getDefinition().getName();
				String sliceName = new Item(sliceDiceData.getSlice()).getDefinition().getName();
				player.getInventory().removeItem(new Item(sliceDiceData.getSrc()));
				player.getInventory().addItemOrDrop(new Item(sliceDiceData.getSlice(), sliceDiceData.getSliceAmount()));	
				player.getActionSender().sendMessage("You slice the "+srcName+" into "+ sliceName + (sliceName.endsWith("s") ? ".":"s."));
				makeAmount--;
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
	}
	
	public static boolean handleButtons(Player player, int buttonId, int amount) {
		if(player.getStatedInterface() == "sliceDice")
		{
			final SliceDiceData sliceDiceData = SliceDiceData.forId(player.getTempInteger());
			if(sliceDiceData != null)
			{
				switch (buttonId) {
					case 34170:// 1
						handleDice(player, 1, sliceDiceData.getSrc());
						return true;
					case 34169:// 5
						handleDice(player, 5, sliceDiceData.getSrc());
						return true;
					case 34168:// 10
						handleDice(player, 10, sliceDiceData.getSrc());
						return true;
					case 34167:// x
						handleDice(player, amount, sliceDiceData.getSrc());
						return true;			
					case 34174:// 1
						handleSlice(player, 1, sliceDiceData.getSrc());
						return true;
					case 34173:// 5
						handleSlice(player, 5, sliceDiceData.getSrc());
						return true;
					case 34172:// 10
						handleSlice(player, 10, sliceDiceData.getSrc());
						return true;
					case 34171:// x
						handleSlice(player, amount, sliceDiceData.getSrc());
						return true;
				}
			}
		}
		return false;
	}
}
