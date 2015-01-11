package com.rs2.model.players.container.inventory;

import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;

public class Inventory {

	public static final int DEFAULT_INVENTORY_INTERFACE = 3214;
	public static final int SIZE = 28;

	private Player player;
	private Container itemContainer = new Container(Type.STANDARD, SIZE);

	public Inventory(Player player) {
		this.player = player;
	}

	public void sendInventoryOnLogin() {
		refresh();
	}

	public void refresh() {
		Item[] inv = itemContainer.toArray();
		player.getActionSender().sendUpdateItems(DEFAULT_INVENTORY_INTERFACE, inv);
	}

	public void refresh(int inventoryId) {
		Item[] inv = itemContainer.toArray();
		player.getActionSender().sendUpdateItems(inventoryId, inv);
	}

	public int addItemCount(Item item) {
		if (item == null) {
			return 0;
		}
		if (itemContainer.freeSlots() <= 0 && !itemContainer.hasRoomFor(item)) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return 0;
		}
		int amount = item.getCount();
		if (!item.getDefinition().isStackable()) {
			if (amount > itemContainer.freeSlots()) {
				amount = itemContainer.freeSlots();
			}
		}
		itemContainer.add(new Item(item.getId(), amount));
		refresh();
		player.getEquipment().updateWeight();
		return amount;
	}

	public void addItemOrDrop(Item item) {
		if (!addItem(item)) {
            GroundItemManager.getManager().dropItem(new GroundItem(item, player));
		}
	}
	
	public void addItemOrBank(Item item) {
		if (!addItem(item)) {
			if (player.getBankManager().roomForItem(item)) {
				player.getBankManager().add(item);
			} else {
				GroundItemManager.getManager().dropItem(new GroundItem(item, player));
			}
		}
	}

	public boolean addItem(Item item) {
		if (item == null || !item.validItem()) {
			return false;
		}
		if (itemContainer.freeSlots() <= 0 && !itemContainer.hasRoomFor(item)) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return false;
		}
		int amount = item.getCount();
		if (!item.getDefinition().isStackable()) {
			if (amount > itemContainer.freeSlots()) {
				amount = itemContainer.freeSlots();
			}
		}
		itemContainer.add(new Item(item.getId(), amount));
		refresh();
		player.getEquipment().updateWeight();
		return true;
	}

	public boolean canAddItem(Item item) {
		if (item == null) {
			return false;
		}
		if (itemContainer.freeSlots() <= 0 && !itemContainer.hasRoomFor(item)) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	/**
	 * Checks if the container contains the specified item.
	 * 
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean ownsItem(String string) {
		for (Item item : itemContainer.getItems()) {
			if (item == null)
				continue;
			if (item.getDefinition().getName().toLowerCase().contains(string))
				return true;
		}
		if(player.getBankManager().ownsItem(string))
		{
			return true;
		}
		return false;
	}
	/**
	 * Checks if the container contains the specified item.
	 * 
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean ownsItem(int id) {
		for (Item item : itemContainer.getItems()) {
			if (item == null)
				continue;
			if (item.getId() == id)
				return true;
		}
		if(player.getBankManager().ownsItem(id))
		{
			return true;
		}
		for (Item item : player.getEquipment().getItemContainer().getItems()) {
			if (item == null)
				continue;
			if (item.getId() == id)
				return true;
		}
		return false;
	}

	public void set(int slot, Item item) {
		removeItemSlot(slot);
		addItemToSlot(item, slot);

	}
	public void addItemToSlot(Item item, int slot) {
		if (item == null || !item.validItem()) {
			return;
		}
		if (item.getDefinition().isStackable() && itemContainer.getSlotById(item.getId()) > -1) {
			int index = itemContainer.getSlotById(item.getId());
			Item currentItem = itemContainer.get(index);
			itemContainer.set(index, new Item(item.getId(), item.getCount() + currentItem.getCount()));
			refresh();
			return;
		}
		if (itemContainer.freeSlot() == -1) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return;
		}
		itemContainer.set(slot, item);
		refresh();
	}
/*	public boolean removeAllOfItem(Item item) {
		if (item == null || !item.validItem()) {
			return false;
		}
		if (!playerHasItem(item)) {
			return false;
		}
		for(Item i : itemContainer.getItems()) {
		    itemContainer.remove(item);
		    refresh();
		    player.getEquipment().updateWeight();
		}
		return true;
	}*/
	public boolean removeItem(Item item) {
		if (item == null || !item.validItem()) {
			return false;
		}
		if (!playerHasItem(item)) {
			return false;
		}
		itemContainer.remove(item);
		refresh();
		player.getEquipment().updateWeight();
		return true;
	}

	/**
	 * Removes an item and adds an item - I'd recommend using this since it
	 * checks for you having the item before it gives it to you. - Should help
	 * combat duping.
	 */
	public void replaceItemWithItem(Item oldItem, Item newItem) {
		removeItem(oldItem);
		addItem(newItem);
	}

	public boolean removeItemSlot(int slot) {
		if (slot == -1) {
			return false;
		}
		if (itemContainer.get(slot) == null) {
			return false;
		}
		itemContainer.remove(itemContainer.get(slot), slot);
		return true;
	}

	public boolean removeItemSlot(Item item, int slot) {
		if (item == null || item.getId() == -1) {
			return false;
		}
		if (slot == -1) {
			return false;
		}
		if (itemContainer.get(slot) == null) {
			return false;
		}
		if (!itemContainer.contains(item.getId())) {
			return false;
		}
		int items = itemContainer.remove(item, slot);
		refresh();
		return items > 0;
	}

	public void swap(int fromSlot, int toSlot) {
		itemContainer.swap(fromSlot, toSlot);
		refresh();
	}

	public void setItemContainer(Container itemContainer) {
		this.itemContainer = itemContainer;
	}

	public Container getItemContainer() {
		return itemContainer;
	}
    
    public boolean playerHasItem(Item item) {
        return playerHasItem(item.getId(), item.getCount());
    }

	public boolean playerHasItem(int id) {
		return getItemContainer().getSlotById(id) > -1;
	}

	public boolean playerHasItem(int id, int amount) {
		if (!playerHasItem(id))
			return false;
		return getItemContainer().getCount(id) >= amount;
	}

	public boolean playerHasExactItem(int id, int amount) {
		if (!playerHasItem(id))
			return false;
		return getItemContainer().getCount(id) == amount;
	}

	public int getItemAmount(int itemId) {
		return player.getInventory().getItemContainer().getCount(itemId);
	}

}
