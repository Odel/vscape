package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.model.content.BankPin;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;
import com.rs2.cache.interfaces.RSInterface;

public class BankManager {

	public static final int SIZE = 352;
	private Container itemContainer = new Container(Type.STANDARD, SIZE);
	public static final int DEFAULT_BANK_INTERFACE = 5292;
	private Player player;

	public static void openBank(Player player) {
		if (player.getBankPin().hasBankPin()) {
			if (!player.getBankPin().isBankPinVerified()) {
				player.getBankPin().startPinInterface(BankPin.PinInterfaceStatus.VERIFYING);
				return;
			}
		} else if (!player.isBankWarning()) {
			player.getActionSender().sendMessage("You do not have a bank pin, it is highly recommended you get one.");
			player.setBankWarning(true);
		}
		Item[] inventory = player.getInventory().getItemContainer().toArray();
		Item[] bank = player.getBank().toArray();
		player.getActionSender().sendUpdateItems(5064, inventory);
		player.getActionSender().sendInterface(5292, 5063);
		player.getActionSender().sendUpdateItems(5382, bank);
		player.getAttributes().put("isBanking", Boolean.TRUE);
	}

	public static void openDepositBox(Player player) {
		Item[] inventory = player.getInventory().getItemContainer().toArray();
		player.getActionSender().sendUpdateItems(7423, inventory);
		player.getActionSender().sendInterface(4465, 197);
		player.getAttributes().put("isBanking", Boolean.TRUE);
	}

	public static void bankItem(Player player, int slot, int bankItem, int bankAmount) {
	    RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
        if (!player.hasInterfaceOpen(inter)) {
            return;
        }
		Item inventoryItem = player.getInventory().getItemContainer().get(slot);
		if (inventoryItem == null || inventoryItem.getId() != bankItem || !inventoryItem.validItem()) {
			return;
		}
		int amount = player.getInventory().getItemContainer().getCount(bankItem);
		boolean isNote = inventoryItem.getDefinition().isNoted();
		if (inventoryItem.getDefinition().getId() > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		int freeSlot = player.getBank().freeSlot();
		if (freeSlot == -1) {
			player.getActionSender().sendMessage("You don't have enough space in your bank account.");
			return;
		}
		if (amount > bankAmount) {
			amount = bankAmount;
		}
		if (!inventoryItem.getDefinition().isStackable()) {
			for (int i = 0; i < amount; i++) {
				player.getInventory().removeItem(new Item(bankItem, 1));
			}
		} else {
			player.getInventory().removeItem(new Item(bankItem, amount));
		}
		int bankCount = player.getBank().getCount(bankItem);
		int transferId = isNote ? inventoryItem.getDefinition().getNormalId() : inventoryItem.getDefinition().getId();
		if (bankCount == 0) {
			player.getBank().add(new Item(transferId, amount));
		} else {
			player.getBank().set(player.getBank().getSlotById(transferId), new Item(transferId, bankCount + amount));
		}
		Item[] bankItems = player.getBank().toArray();
		player.getInventory().refresh(5064);
		player.getInventory().refresh(7423);
		player.getActionSender().sendUpdateItems(5382, bankItems);
	}
	
	public static void bankAll(Player player)
	{
		RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
        if (!player.hasInterfaceOpen(inter)) {
            return;
        }
		try
		{
			Container container = player.getInventory().getItemContainer();
			Item[] items = container.getItems();
			for (Item item : items) {
				if(item == null ||!item.validItem())
					continue;
				int itemId = item.getId();
				int slot = container.getSlotById(itemId);
				int amount = item.getCount();
				boolean isNote = item.getDefinition().isNoted();
				if (item.getDefinition().getId() > Constants.MAX_ITEMS) {
					player.getActionSender().sendMessage("This item is not supported yet.");
					return;
				}
				int freeSlot = player.getBank().freeSlot();
				if (freeSlot == -1) {
					player.getActionSender().sendMessage("You don't have enough space in your bank account.");
					return;
				}
				if (!item.getDefinition().isStackable()) {
					for (int i = 0; i < amount; i++) {
						player.getInventory().removeItem(new Item(itemId, 1));
					}
				} else {
					player.getInventory().removeItem(new Item(itemId, amount));
				}
				int bankCount = player.getBank().getCount(itemId);
				int transferId = isNote ? item.getDefinition().getNormalId() : item.getDefinition().getId();
				if (bankCount == 0) {
					player.getBank().add(new Item(transferId, amount));
				} else {
					player.getBank().set(player.getBank().getSlotById(transferId), new Item(transferId, bankCount + amount));
				}
			}
		} catch (Exception e) {	
		} finally {		
		}
		Item[] bankItems = player.getBank().toArray();
		player.getInventory().refresh(5064);
		player.getInventory().refresh(7423);
		player.getActionSender().sendUpdateItems(5382, bankItems);
		player.getInventory().refresh();	
	}
	
	public void refresh() {
		Item[] items = itemContainer.toArray();
		player.getActionSender().sendUpdateItems(DEFAULT_BANK_INTERFACE, items);
	}
	
	public static void withdrawItem(Player player, int slot, int bankItem, int bankAmount) {
		RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
        if (!player.hasInterfaceOpen(inter)) {
            return;
        }
		Item item = new Item(bankItem);
		boolean withdrawAsNote = player.isWithdrawAsNote();
		boolean isNoteable = item.getDefinition().isNoteable();
		int notedid = item.getDefinition().getNotedId();
		int inBankAmount = player.getBank().getCount(bankItem);
		if (bankAmount < 1 || bankItem < 0 || player.getBank().get(slot) == null || player.getBank().get(slot).getId() != item.getId() || !item.validItem()) {
			return;
		}
		if (bankItem > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		if (inBankAmount < bankAmount) {
			bankAmount = inBankAmount;
		}
		if (withdrawAsNote && !isNoteable) {
			player.getActionSender().sendMessage("This item cannot be withdrawn as a note.");
			withdrawAsNote = false;
		}
		int count = 0;
		if (!withdrawAsNote || !isNoteable) {
			count = player.getInventory().addItemCount(new Item(bankItem, bankAmount));
		} else if (withdrawAsNote) {
			count = player.getInventory().addItemCount(new Item(notedid, bankAmount));
		}
		player.getBank().remove(new Item(bankItem, count), slot);
		player.getBank().shift();
		Item[] bankItems = player.getBank().toArray();
		player.getInventory().refresh(5064);
		player.getActionSender().sendUpdateItems(5382, bankItems);
	}
	
	public void setItemContainer(Container itemContainer) {
		this.itemContainer = itemContainer;
	}

	public Container getItemContainer() {
		return itemContainer;
	}
	
	public static void handleBankOptions(Player player, int fromSlot, int toSlot) {
        if (toSlot >= player.getBank().getCapacity() || fromSlot >= player.getBank().getCapacity())
            return;
		if (player.getBankOptions().equals(BankOptions.SWAP_ITEM)) {
			player.getBank().swap(fromSlot, toSlot);
		} else if (player.getBankOptions().equals(BankOptions.INSERT_ITEM)) {
			player.getBank().insert(fromSlot, toSlot);
		}
		Item[] bankItems = player.getBank().toArray();
		player.getActionSender().sendUpdateItems(5382, bankItems);
	}

}
