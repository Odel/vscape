package com.rs2.model.content.skills.herblore;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
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
		boolean useItemIsPestle = false;
		if (useItem.getId() == PESTLE) {
			useItemIsPestle = true;
		}
		GrindableData g = GrindableData.getGrindableForId(!useItemIsPestle ? useItem.getId() : withItem.getId());
		if ((useItemIsPestle || withItem.getId() == PESTLE) && g != null) {
			int materialSlot = player.getInventory().getItemContainer().getSlotById(g.getFirstItem());
			if (!Constants.HERBLORE_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if(g.noReqs) {
				grindItem(player, g.getFirstItem(), g.getGrindedItem(), materialSlot);
				return true;
			} else {
				if(canGrindItem(player, g.getFirstItem())) {
					grindItem(player, g.getFirstItem(), g.getGrindedItem(), materialSlot);
				}
				return true;
			}
		}
		return false;
	}

	public static void grindItem(final Player player, final int material, final int product, final int materialSlot) {
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer p) {
				Item materialItem = new Item(material, 1);
				if (!player.getInventory().removeItemSlot(materialItem, materialSlot) && !player.getInventory().removeItem(materialItem)) {
					p.stop();
					return;
				}
				Item toRecieve = new Item(product);
				player.getUpdateFlags().sendAnimation(GRIND_ANIM);
				if (material == 4620) {
					player.getInventory().replaceItemWithItem(new Item(VIAL), toRecieve);
				} else {
					player.getInventory().addItemToSlot(toRecieve, materialSlot);
				}
				player.getActionSender().sendMessage("You grind the " + new Item(material).getDefinition().getName().toLowerCase() + ".");
				p.stop();
			}

			@Override
			public void stop() {
			}
		}, 2);
	}
}
