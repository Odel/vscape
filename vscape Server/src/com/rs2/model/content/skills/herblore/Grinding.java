package com.rs2.model.content.skills.herblore;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class Grinding {
	
	private static final int PESTLE = 233;
	public static final int GRIND_ANIM = 364;
	private static final int VIAL = 229;
	
	public enum GrindableData {
		CHOCOLATE_BAR(1973, 1975, true),
		BIRDS_NEST(5075, 6693, true),
		BLUE_DRAGON_SCALE(243, 241, true),
		DESERT_GOAT_HORN(9735, 9736, true),
		UNICORN_HORN(237, 235, true),
		BLACK_MUSHROOM(4620, 4622, false);
		
		private int firstItem;
		private int grindedItem;
		public boolean noReqs;
		
		GrindableData(int firstItem, int grindedItem, boolean noReqs) {
			this.firstItem = firstItem;
			this.grindedItem = grindedItem;
			this.noReqs = noReqs;
		}
		
		public static GrindableData getGrindableForId(int id) {
			for(GrindableData g : GrindableData.values()) {
				if(id == g.getFirstItem()) {
					return g;
				}
			}
			return null;
		}
		
		public int getFirstItem() {
			return this.firstItem;
		}
		
		public int getGrindedItem() {
			return this.grindedItem;
		}
	}
	
	public static boolean canGrindItem(final Player player, final int id) {
		//If grindable item has an extra req denoted by hasReq being false in enum.
		switch(id) {
			case 4620:
				boolean canGrind = player.getInventory().playerHasItem(VIAL);
				if(!canGrind) {
					player.getActionSender().sendMessage("You need a vial to put the ink from the mushroom in.");
				}
				return canGrind;
		}
		return false;
	}
	
	public static boolean createProduct(Player player, Item useItem, Item withItem, int slot, int usedSlot) {
		final int grindItem = useItem.getId() != PESTLE ? useItem.getId() : withItem.getId();
		int materialSlot = useItem.getId() != PESTLE ? slot : usedSlot;
		GrindableData grindable = GrindableData.getGrindableForId(grindItem);
		if (grindable != null) {
			if((useItem.getId() == grindable.getFirstItem() && withItem.getId() != PESTLE) || 
					(useItem.getId() == PESTLE && withItem.getId() != grindable.getFirstItem()))
			{
				return false;
			}
			if (!Constants.HERBLORE_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if(!grindable.noReqs)
			{
				if(!canGrindItem(player, grindable.getFirstItem())) {
					return true;
				}
			}
			grindItem(player, grindable.getFirstItem(), grindable.getGrindedItem(), materialSlot);
			return true;
		}
		return false;
	}
	
	public static void grindItem(final Player player, final int material, final int product, final int materialSlot){
		player.getUpdateFlags().sendAnimation(GRIND_ANIM);
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || !player.getInventory().playerHasItem(PESTLE) || !player.getInventory().playerHasItem(material)) {
					container.stop();
					return;
				}
				Item toRecieve = new Item(product, 1);
				if (material == 4620) {
					if(player.getInventory().removeItemSlot(new Item(material, 1), materialSlot) 
							&& player.getInventory().removeItem(new Item(VIAL, 1)))
					{
						player.getInventory().addItemToSlot(toRecieve, materialSlot);
						player.getActionSender().sendMessage("You Grind the mushroom filling a vial with black ink.");
						container.stop();
						return;
					}
				} else {
					player.getInventory().replaceItemWithItem(new Item(material, 1), toRecieve);
					String itemName = ItemManager.getInstance().getItemName(material);
					player.getActionSender().sendMessage("You Grind the "+itemName+" to dust.");
					container.stop();
					return;
				}
				container.stop();
				return;
			}

			@Override
			public void stop() {
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
	}
}
