package com.rs2.model.content.minigames.castlewars;

import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.util.Misc;

public class CastlewarsExchange {
	
	private final static int INTERFACE_MAIN = 11367;
	private final static int INTERFACE_ITEM_CONTAINER = 12392; //24 slots
	
	private final static int TICKET_ID = 4067;
	
	private static Container stock = new Container(Type.ALWAYS_STACK, 24);
	
	public enum ExchangeItem {
		RDH(new Item(4071,1), 4),
		RDB(new Item(4069,1), 8),
		RDL(new Item(4070,1), 6),
		RDSH(new Item(4072,1), 6),
		RDSW(new Item(4068,1), 5),
		WDH(new Item(4506,1), 40),
		WDB(new Item(4504,1), 80),
		WDL(new Item(4505,1), 60),
		WDSH(new Item(4507,1), 60),
		WDSW(new Item(4503,1), 50),
		GDH(new Item(4511,1), 400),
		GDB(new Item(4509,1), 800),
		GDL(new Item(4510,1), 600),
		GDSH(new Item(4512,1), 600),
		GDSW(new Item(4508,1), 500),
		SARAHOOD(new Item(4513,1), 10),
		SARACLOAK(new Item(4514,1), 10),
		ZAMMYHOOD(new Item(4515,1), 10),
		ZAMMYCLOAK(new Item(4516,1), 10),
		SARABANNER(new Item(4037,1), 100),
		ZAMMYBANNER(new Item(4039,1), 100);
		
		private Item item;
		private int price;
		
		private ExchangeItem(Item item, int price)
		{
			this.item = item;
			this.price = price;
		}
		
		public static ExchangeItem forItemId(int id) {
	    	for (ExchangeItem exchangeItem : ExchangeItem.values()) {
				if (exchangeItem.item.getId() == id) {
			    	return exchangeItem;
				}
	    	}
	    	return null;
		}
		
		public Item getItem(){
			return item;
		}
		
		public int getPrice(){
			return price;
		}

	}
	
	static {
		for (ExchangeItem cei : ExchangeItem.values()) {
			stock.add(cei.item);
		}
	}
	
	public static void OpenInterface(final Player player) {
		Item[] exchangeItems = stock.toArray();
		player.getActionSender().sendInterface(INTERFACE_MAIN, 3822);
		player.getInventory().refresh(3823);
		player.getActionSender().sendUpdateItems(INTERFACE_ITEM_CONTAINER, exchangeItems);
		player.getAttributes().put("isShopping", Boolean.TRUE);
	}
	
	public static void BuyItem(Player player, int slot, int shopItem)
	{
		Container inventory = player.getInventory().getItemContainer();
		Item item = stock.get(slot);
		if (shopItem < 0 || item == null || !item.validItem()) {
			return;
		}
		if (shopItem != item.getId()) {
			return;
		}
		ExchangeItem exchangeItem = ExchangeItem.forItemId(item.getId());
		if(exchangeItem == null){
			return;
		}
		int value = exchangeItem.getPrice();
		int currency = TICKET_ID;
		int ticketAmount = inventory.getCount(currency);
		boolean canBuy = false;
       	if(ticketAmount >= value){
       		canBuy = true;
        }
        if(!canBuy){
			player.getActionSender().sendMessage("You do not have enough Tickets to buy this item.");
            return;
        }
        int freeSpace = inventory.freeSlots();
		if (player.getInventory().playerHasExactItem(currency, value)) {
			freeSpace++;
		}
		if ((!item.getDefinition().isStackable() || !player.getInventory().playerHasItem(shopItem)) && freeSpace < 1) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return;
		}
		player.getInventory().removeItem(new Item(currency, value));
		player.getInventory().addItem(new Item(shopItem, 1));
		player.getInventory().refresh(3823);
	}

	public static void getBuyValue(Player player, int id) {
		ExchangeItem exchangeItem = ExchangeItem.forItemId(id);
		if(exchangeItem == null){
			return;
		}
		int price = exchangeItem.getPrice();
		String currencyName = ItemManager.getInstance().getItemName(TICKET_ID);
		player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": currently costs " + Misc.formatNumber(price) + " " + currencyName + "s.", true);
	}

}
