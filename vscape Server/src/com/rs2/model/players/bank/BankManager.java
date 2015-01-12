package com.rs2.model.players.bank;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.content.BankPin;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;

public class BankManager {
	
	public static final int SIZE = 352; // 352
	public static final int tabSize = 9; //(8 + 1) 8 tabs 1 main
	public static final int slotModifier = 6;
	private ArrayList<Container> itemContainers = new ArrayList<Container>(tabSize);

	public static final int DEFAULT_BANK_INTERFACE = 5292;
	
	private Player player;
	private int currentTab = 0; // 0 = main
	
	public BankManager(final Player player)
	{
		this.player = player;
		itemContainers.add(new Container(Type.ALWAYS_STACK, SIZE));
	}
	
	public void openDepositBox() {
		Item[] inventory = player.getInventory().getItemContainer().toArray();
		player.getActionSender().sendUpdateItems(7423, inventory);
		player.getActionSender().sendInterface(4465, 197);
		//player.getAttributes().put("isBanking", Boolean.TRUE);
	}
	
	public void openBank() {
		if (player.getBankPin().hasBankPin()) {
			if (!player.getBankPin().isBankPinVerified()) {
				player.getBankPin().startPinInterface(BankPin.PinInterfaceStatus.VERIFYING);
				return;
			}
		} else if (!player.isBankWarning()) {
			player.getActionSender().sendMessage("You do not have a bank pin, it is highly recommended you get one.", true);
			player.setBankWarning(true);
		}

		Item[] inventory = player.getInventory().getItemContainer().toArray();
		Item[] bank = tabContainer(currentTab).toArray();
		player.getActionSender().sendUpdateItems(5064, inventory);
		player.getActionSender().sendInterface(5292, 5063);
		player.getActionSender().sendUpdateItems(5382, bank);
		player.getAttributes().put("isBanking", Boolean.TRUE);
		player.setStatedInterface("banking");
		updateBankInterface();
	}
	
	public void updateBankInterface()
	{
		updateTabs();
		updateBankTotal();
	}
	
	public void updateTabs(){
		for(int i = 0; i < tabSize-1; i++)
		{
			player.getActionSender().sendUpdateItem(new Item(65535), 0, 50013+i, 1);
		}
		if(getUsedTabs() > 1)
		{
			for(int i = 1; i < getUsedTabs(); i++)
			{
				Container tab = tabContainer(i);
				if(tab != null && tab.size() > 0) {
					if(tab.get(0) != null){
						if(tab.get(0) != null){
							int id = tab.get(0).getId();
							if(id == 995)
							{
								id = getStackableModel(id, tab.get(0).getCount());
							}
							player.getActionSender().sendUpdateItem(new Item(id), 0, 50013+(i-1), 1);
						}
					}
				}
			}
		}
		player.getActionSender().sendString(Integer.toString(getUsedTabs()-1), 50022);
		player.getActionSender().sendString(Integer.toString(currentTab), 50023);
		player.getActionSender().sendString("1", 50021);
	}
	
	public void refreshTabContainer()
	{
		Item[] bankItems = tabContainer(currentTab).toArray();
		player.getInventory().refresh(5064);
		player.getInventory().refresh(7423);
		player.getActionSender().sendUpdateItems(5382, bankItems);
		player.getInventory().refresh();
	}
	
    public int getStackableModel(int id, int count) {
		int model = id;
		if (model == 995) {
			if (count > 9999) {
				model = 1004;
			} else if (count > 999) {
				model = 1003;
			} else if (count > 249) {
				model = 1002;
			} else if (count > 99) {
				model = 1001;
			}  else if (count > 24) {
				model = 1000;
			} else if (count > 4) {
				model = 999;
			} else if (count > 3) {
				model = 998;
			} else if (count > 2) {
				model = 997;
			} else if (count > 1) {
				model = 996;
			}
		}
		return model;
	}

	public void updateBankTotal(){
		player.getActionSender().sendString(Integer.toString(totalUsedSlots()), 50011);
		player.getActionSender().sendString(Integer.toString(((SIZE*tabSize) / slotModifier)), 50012);
	}
	
	public void bankItem(int slot, int bankItem, int bankAmount) {
	    RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("banking")) {
            return;
        }
        if(!((Boolean) player.getAttributes().get("isBanking")))
        {
        	return;
        }
		Item inventoryItem = player.getInventory().getItemContainer().get(slot);
		if (inventoryItem == null || inventoryItem.getId() != bankItem || !inventoryItem.validItem()) {
			return;
		}
		int amount = player.getInventory().getItemContainer().getCount(bankItem);
		boolean isNote = inventoryItem.getDefinition().isNoted();
		if (inventoryItem.getDefinition().getId() > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.", true);
			return;
		}
		int transferId = isNote ? inventoryItem.getDefinition().getNormalId() : inventoryItem.getDefinition().getId();
		Container tabContainer = tabContainer(currentTab);
		if(!tabContainer.contains(transferId))
		{
			int tabContains = tabsContain(transferId);
			if(tabContains >= 0)
			{
				tabContainer = tabContainer(tabContains);
				selectTab(tabContains, false);
			}else{
				if (!roomForItems()) {
					updateBankTotal();
					refreshTabContainer();	
					player.getActionSender().sendMessage("You don't have enough space in your bank account.", true);
					return;
				}
				int freeSlot = tabFreeSlot(currentTab);
				if (freeSlot == -1) {
					if(getUsedTabs() < tabSize){
						addTab();
						tabContainer = tabContainer(getUsedTabs()-1);
						selectTab(getUsedTabs()-1, false);
					}else{
						updateBankTotal();
						refreshTabContainer();	
						player.getActionSender().sendMessage("This bank tab is full.", true);
						return;
					}
				}
			}
		}
		if (amount > bankAmount) {
			amount = bankAmount;
		}
		if (!inventoryItem.getDefinition().isStackable()) {
			for (int i = 0; i < amount; i++) {
				player.getInventory().removeItemSlot(new Item(bankItem, amount), slot);
			}
		} else {
			player.getInventory().removeItem(new Item(bankItem, amount));
		}
		int bankCount = tabContainer.getCount(bankItem);
		if (bankCount == 0) {
			tabContainer.add(new Item(transferId, amount));
			if(tabContainer.size() == 1)
				updateTabs();
		} else {
			tabContainer.set(tabContainer.getSlotById(transferId), new Item(transferId, bankCount + amount));
			if(bankItem == 995)
			{
				updateTabs();
			}
		}
		updateBankTotal();
		refreshTabContainer();
	}
	
	public void bankAll()
	{
		RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("banking")) {
            return;
        }
        if(!((Boolean) player.getAttributes().get("isBanking")))
        {
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
					player.getActionSender().sendMessage("This item is not supported yet.", true);
					return;
				}
				int transferId = isNote ? item.getDefinition().getNormalId() : item.getDefinition().getId();
				Container tabContainer = tabContainer(currentTab);
				if(!tabContainer.contains(transferId))
				{
					int tabContains = tabsContain(transferId);
					if(tabContains >= 0)
					{
						tabContainer = tabContainer(tabContains);
					}else{
						if (!roomForItems()) {
							updateBankTotal();
							refreshTabContainer();	
							player.getActionSender().sendMessage("You don't have enough space in your bank account.", true);
							return;
						}
						int freeSlot = tabFreeSlot(currentTab);
						if (freeSlot == -1) {
							int nextFreeTab = -1;
							for(int i = 0; i < getUsedTabs(); i++)
							{
								int freeSlots = tabFreeSlot(i);
								if(freeSlots != -1)
								{
									nextFreeTab = i;
									tabContainer = tabContainer(i);
									break;
								}
							}
							if(nextFreeTab == -1)
							{
								if(getUsedTabs() < tabSize){
									addTab();
									tabContainer = tabContainer(getUsedTabs()-1);
									selectTab(getUsedTabs()-1, false);
								}else{
									updateBankTotal();
									refreshTabContainer();	
									player.getActionSender().sendMessage("This bank tab is full.", true);
									return;
								}
							}
						}
					}
				}
				if (!item.getDefinition().isStackable()) {
					for (int i = 0; i < amount; i++) {
						player.getInventory().removeItemSlot(new Item(itemId, amount), slot);
					}
				} else {
					player.getInventory().removeItem(new Item(itemId, amount));
				}
				int bankCount = tabContainer.getCount(itemId);
				if (bankCount == 0) {
					tabContainer.add(new Item(transferId, amount));
					if(tabContainer.size() == 1)
						updateTabs();
				} else {
					tabContainer.set(tabContainer.getSlotById(transferId), new Item(transferId, bankCount + amount));
					if(itemId == 995)
					{
						updateTabs();
					}
				}
			}
		} catch (Exception e) {	
		} finally {		
		}
		updateBankTotal();
		refreshTabContainer();	
	}
	
	public void withdrawItemAll(int slot, int bankItem) {
		RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("banking")) {
            return;
        }
        if(!((Boolean) player.getAttributes().get("isBanking")))
        {
        	return;
        }
		Container tabContainer = tabContainer(currentTab);
		int amount = tabContainer.getCount(bankItem);
		withdrawItem(slot, bankItem, amount);
	}
	
	public void withdrawItem(int slot, int bankItem, int bankAmount) {
		RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("banking")) {
            return;
        }
        if(!((Boolean) player.getAttributes().get("isBanking")))
        {
        	return;
        }
		Item item = new Item(bankItem);
		Container tabContainer = tabContainer(currentTab);
		if(tabContainer == null)
			return;
		boolean withdrawAsNote = player.isWithdrawAsNote();
		boolean isNoteable = item.getDefinition().isNoteable();
		int notedid = item.getDefinition().getNotedId();
		int inBankAmount = tabContainer.getCount(bankItem);
		if (bankAmount < 1 || bankItem < 0 || tabContainer.get(slot) == null || tabContainer.get(slot).getId() != item.getId() || !item.validItem()) {
			return;
		}
		if (bankItem > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.", true);
			return;
		}
		if (inBankAmount < bankAmount) {
			bankAmount = inBankAmount;
		}
		if (withdrawAsNote && !isNoteable) {
			player.getActionSender().sendMessage("This item cannot be withdrawn as a note.", true);
			withdrawAsNote = false;
		}
		int count = 0;
		if (!withdrawAsNote || !isNoteable) {
			count = player.getInventory().addItemCount(new Item(bankItem, bankAmount));
		} else if (withdrawAsNote) {
			count = player.getInventory().addItemCount(new Item(notedid, bankAmount));
		}
		tabContainer.remove(new Item(bankItem, count), slot);
		tabContainer.shift();
		
		if(currentTab != 0){
			if(tabContainer.size() <= 0)
			{
				tabContainer.clear();
				removeTab(currentTab);
				selectTab(0, false);
			}
		}
		if(slot == 0)
		{
			updateTabs();
		}
		updateBankTotal();
		refreshTabContainer();
	}
	
	public void toTab(int tab, int from)
	{
	    RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("banking")) {
            return;
        }
        if(!((Boolean) player.getAttributes().get("isBanking")))
        {
        	return;
        }
		if (tab == currentTab || tab > getUsedTabs() || tab > tabSize)
			return;
		Container fromTab = itemContainers.get(currentTab);
		if(fromTab == null || fromTab.size() <= 1 && tab > (getUsedTabs()-1))
		{
			return;
		}
		if(fromTab.size() > 1 && tab == getUsedTabs()){
			addTab();
		}
		Container toTab = itemContainers.get(tab); 
		if(toTab == null)
			return;
		int freeSlot = toTab.freeSlot();
		if (freeSlot == -1) {
			player.getActionSender().sendMessage("That bank tab is already full.", true);
			return;
		}
		final int id = fromTab.get(from).getId();
		final int amount = fromTab.get(from).getCount();
		fromTab.remove(new Item(id, amount), from);
		fromTab.shift();
		final int bankCount = toTab.getCount(id);
		if (bankCount == 0) {
			tabContainer(tab).add(new Item(id, amount));
		} else {
			tabContainer(tab).set(tabContainer(tab).getSlotById(id), new Item(id, bankCount + amount));
		}
		if(fromTab.size() <= 0)
		{
			fromTab.clear();
			if(currentTab != 0){
				removeTab(currentTab);
			}
		}
		if(tab > (getUsedTabs()-1))
		{
			tab = tab-1;
		}
		selectTab(tab, false);
	}
	
	public void handleBankOptions(int fromSlot, int toSlot) {
		RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("banking")) {
            return;
        }
        if(!((Boolean) player.getAttributes().get("isBanking")))
        {
        	return;
        }
		Container tabContainer = tabContainer(currentTab);
		if(tabContainer == null){
            return;
        }
        if (toSlot >= tabContainer.getCapacity() || fromSlot >= tabContainer.getCapacity()){
            return;
        }
		if (player.getBankOptions().equals(BankOptions.SWAP_ITEM)) {
			tabContainer.swap(fromSlot, toSlot);
		} else if (player.getBankOptions().equals(BankOptions.INSERT_ITEM)) {
			tabContainer.insert(fromSlot, toSlot);
		}
		tabContainer.shift();
		updateTabs();
		refreshTabContainer();
	}
	
	public boolean tabButtons(int buttonId){
		switch(buttonId)
		{
			case 195082 :
				selectTab(0, true);
			return true;
			case 195083 :
				selectTab(1, true);
			return true;
			case 195084 :
				selectTab(2, true);
			return true;
			case 195085 :
				selectTab(3, true);
			return true;
			case 195086 :
				selectTab(4, true);
			return true;
			case 195087 :
				selectTab(5, true);
			return true;
			case 195088 :
				selectTab(6, true);
			return true;
			case 195089 :
				selectTab(7, true);
			return true;
			case 195090 :
				selectTab(8, true);
			return true;
		}
		return false;
	}
	
	//Tab methods
	private void addTab()
	{
		if(getUsedTabs() < tabSize){
			itemContainers.add(new Container(Type.ALWAYS_STACK, SIZE));
			updateTabs();
		}
	}
	
	public void addTabs(int usedTabs)
	{
		for(int i = 1; i < usedTabs; i++) {
			itemContainers.add(new Container(Type.ALWAYS_STACK, SIZE));
		}
	}
	
	private void removeTab(int tab)
	{
		if(tab != 0 && getUsedTabs() > 1){
			itemContainers.remove(tab);
			updateBankInterface();
		}
	}
	
	private void selectTab(int tab, boolean buttonClick)
	{
		if(buttonClick)
		{
			RSInterface inter = RSInterface.forId(DEFAULT_BANK_INTERFACE);
	        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("banking")) {
	            return;
	        }
	        if(!((Boolean) player.getAttributes().get("isBanking")))
	        {
	        	return;
	        }
		}
		if(buttonClick && tab == currentTab)
			return;
		if(tab < getUsedTabs()){
			if(itemContainers.get(tab) != null)
			{
				currentTab = tab;
				updateTabs();
				refreshTabContainer();
			}
		}
	}
	
	public int getUsedTabs()
	{
		return itemContainers.size();
	}
	
	//Tab Container Methods
	public Container tabContainer()
	{
		return itemContainers.get(currentTab);
	}
	
	public Container tabContainer(int tab)
	{
		return itemContainers.get(tab);
	}
	
	private boolean tabContains(int tab, int itemId)
	{
		for (Item item : tabContainer(tab).getItems()) {
			if (item == null)
				continue;
			if (item.getId() == itemId)
				return true;
		}
		return false;
	}
	
	//Container Item Methods
	public void add(Item item){
		int containerId = tabsContain(item.getId());
		if(containerId >= 0)
		{
			tabContainer(containerId).add(item);
		}else{
			if(!roomForItems())
				return;
			int freeSlot = tabFreeSlot(0);
			if (freeSlot == -1) {
				int nextFreeTab = -1;
				for(int i = 0; i < getUsedTabs(); i++)
				{
					int freeSlots = tabFreeSlot(i);
					if(freeSlots != -1)
					{
						nextFreeTab = i;
						break;
					}
				}
				if(nextFreeTab == -1)
				{
					if(getUsedTabs() < tabSize){
						addTab();
						tabContainer(getUsedTabs()-1).add(item);
						return;
					}else{
						return;
					}
				}else{
					tabContainer(nextFreeTab).add(item);
					return;
				}
			}else{
				tabContainer(0).add(item);
			}
		}
	}
	
	public void add(Item item, int tab){
		int containerId = tabsContain(item.getId());
		if(containerId >= 0)
		{
			tabContainer(containerId).add(item);
		}else{
			if(!roomForItems())
				return;
			int freeSlot = tabFreeSlot(tab);
			if (freeSlot == -1) {
				int nextFreeTab = -1;
				for(int i = 0; i < getUsedTabs(); i++)
				{
					int freeSlots = tabFreeSlot(i);
					if(freeSlots != -1)
					{
						nextFreeTab = i;
						break;
					}
				}
				if(nextFreeTab == -1)
				{
					if(getUsedTabs() < tabSize){
						addTab();
						tabContainer(getUsedTabs()-1).add(item);
						return;
					}else{
						return;
					}
				}else{
					tabContainer(nextFreeTab).add(item);
					return;
				}
			}else{
				tabContainer(tab).add(item);
			}
		}
	}
	
	public void remove(Item item){
		int containerId = tabsContain(item.getId());
		if(containerId >= 0)
		{
			tabContainer(containerId).remove(item);
			if(containerId != 0){
				if(tabContainer(containerId).size() <= 0)
				{
					removeTab(containerId);
				}
			}
		}
	}
	
	public void remove(Item item, int tab, int slot){
		if(tabContains(tab, item.getId()))
		{
			tabContainer(tab).remove(item, slot);
			if(tab != 0){
				if(tabContainer(tab).size() <= 0)
				{
					removeTab(tab);
				}
			}
		}
	}
	
	//Contains Methods
	private int tabsContain(int itemId)
	{
		for(int i = 0; i < getUsedTabs(); i++){
			if(tabContains(i, itemId))
			{
				return i;
			}
		}
		return -1;
	}
	
	public boolean ownsItem(String string) {
		for(int i = 0; i < getUsedTabs(); i++){
			for (Item item : tabContainer(i).getItems()) {
				if (item == null)
					continue;
				if (item.getDefinition().getName().toLowerCase().contains(string))
					return true;
			}
		}
		return false;
	}
	
	public boolean ownsItem(int id) {
		for(int i = 0; i < getUsedTabs(); i++){
			for (Item item : tabContainer(i).getItems()) {
				if (item == null)
					continue;
				if (item.getId() == id)
					return true;
			}
		}
		return false;
	}
	
	//Free Room methods
	public boolean roomForItem(Item item)
	{
		int containerId = tabsContain(item.getId());
		if(containerId >= 0)
		{
			return true;
		}else{
			if(!roomForItems()){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public boolean roomForItems()
	{
		return totalUsedSlots() < ((SIZE*tabSize) / slotModifier);
	}
	
	// Slot Methods
	private int tabFreeSlot(int tab)
	{
		return tabContainer(tab).freeSlot();
	}
	
	private int totalUsedSlots()
	{
		int totalSize = 0;
		for(int i = 0; i < getUsedTabs(); i++){
			totalSize += tabContainer(i).size();
		}
		return totalSize;
	}
	
}
