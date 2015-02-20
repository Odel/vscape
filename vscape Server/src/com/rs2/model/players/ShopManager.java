package com.rs2.model.players;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.LogHandler;
//import com.rs2.util.XStreamUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rs2.model.content.minigames.magetrainingarena.MageRewardHandling;

public class ShopManager {

	public static final int SIZE = 40;
	
    public static Random rand = new Random();

	private static List<Shop> shops = new ArrayList<Shop>(SIZE);

	public static void refresh(Player player, Shop shop) {
		Item[] shopItems = shop.getCurrentStock().toArray();
		player.getActionSender().sendUpdateItems(3900, shopItems);
	}

	public static void refreshAll(int shopId) {
		if(shopId > shops.size()) {
			return;
		}
		Shop shop = shops.get(shopId);
		for (Player players : World.getPlayers()) {
			if (players != null && players.getShopId() == shopId) {
				refresh(players, shop);
			}
		}
	}

	public static void openShop(Player player, int shopId) {
		if (shopId >= shops.toArray().length) {
			return;
		}
		Shop shop = shops.get(shopId);
		Item[] shopItems = shop.getCurrentStock().toArray();
		player.getActionSender().sendInterface(3824, 3822);
		player.getInventory().refresh(3823);
		player.getActionSender().sendUpdateItems(3900, shopItems);
		player.getActionSender().sendString(shop.getName(), 3901);
		player.setShopId(shopId);
		player.getAttributes().put("isShopping", Boolean.TRUE);
	}

	public static void buyItem(Player player, int slot, int shopItem, int amount) {
		Shop shop = shops.get(player.getShopId());
		Container inventory = player.getInventory().getItemContainer();
		Item item = shop.getCurrentStock().get(slot);
		int currency, value;
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			currency = shop.getCurrency();
		} else {
			currency = getCurrencyForShopType(player, shop);
		}
		if (amount < 1 || shopItem < 0 || item == null || !item.validItem()) {
			return;
		}
		if (shopItem != item.getId()) {
			return;
		}
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			value = ItemManager.getInstance().getItemValue(shopItem, "buyfromshop");
			if (shop.getCurrency() == 6529) {
				value *= 1.5;
				value = Math.round(value);
			}
            int finalAmount = 0;
            int currencyValue = inventory.getCount(currency);
            while(currencyValue >= value && finalAmount < amount){
                currencyValue -= value;
                finalAmount++;
            }
            if(finalAmount <= 0){
				player.getActionSender().sendMessage("You do not have enough " + ItemManager.getInstance().getItemName(currency) + " to buy this item.");
                return;
            }
            amount = finalAmount;
		} else {
			value = getSpecialShopPrice(player, shop, shopItem);
			if (currency < value * amount) {
				player.getActionSender().sendMessage("You do not have enough server points to buy this item.");
				return;
			}
		}
		if (shop.getCurrentStock().get(slot).getCount() < amount) {
			amount = shop.getCurrentStock().get(slot).getCount();
		}
		int freeSpace = inventory.freeSlots();
		if (player.getInventory().playerHasExactItem(currency, value)) {
			freeSpace++;
		}
		if ((!item.getDefinition().isStackable() || !player.getInventory().playerHasItem(shopItem)) && freeSpace < (!item.getDefinition().isStackable() ? amount : 1)) {
			amount = freeSpace;
			player.getActionSender().sendMessage("Not enough space in your inventory.");
		}
		if (shop.getCurrentStock().get(slot).getCount() < amount) {
			amount = shop.getCurrentStock().get(slot).getCount();
		}
		if (shop.isGeneralStore()) {
			if (shop.getCurrentStock().get(slot).getCount() == 0) {
				player.getActionSender().sendMessage("This item is out of stock.");
				return;
			}
		}
		for (int i = 0; i < amount; i++) {
			if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
				player.getInventory().removeItem(new Item(currency, value));
			} else {
				decreaseCurrencyForSpecialShop(player, shop, value);
			}
		}
		if (shop.getStock().contains(item.getId())) {
			shop.getCurrentStock().removeOrZero(new Item(item.getId(), amount));
		} else {
			shop.getCurrentStock().remove(new Item(item.getId(), amount));
		}
		player.getInventory().addItem(new Item(shopItem, amount));
		player.getInventory().refresh(3823);
		refreshAll(player.getShopId());
		if(value > 1000)
		LogHandler.logShop(player, shop.getName(), "Bought", new Item(shopItem, amount), value, ItemManager.getInstance().getItemName(currency));
	}

	public static void sellItem(Player player, int slot, int itemId, int amount) {
		Shop shop = shops.get(player.getShopId());
		Container inventory = player.getInventory().getItemContainer();
		Item item = inventory.get(slot);
		int currency = shop.getCurrency();
        if (!Constants.ADMINS_CAN_INTERACT && player.getStaffRights() >= 2) {
            player.getActionSender().sendMessage("This action is not allowed.");
            return;
        }
        if (item == null)
            return;
        if (item.getId() != itemId || !item.validItem()) {
        	return;
        }
	/*	if (currency == 6529) { // TODO: Fix sell prices
			player.getActionSender().sendMessage("You cannot sell items to this shop.");
			return;
		}*/
		if (shop.getCurrencyType() != Shop.CurrencyTypes.ITEM) {
			player.getActionSender().sendMessage("This shop can't buy anything.");
			return;
		}
		if (shop.getCurrentStock().freeSlots() < 1) {
			player.getActionSender().sendMessage("The shop is currently full!");
			return;
		}
        if (itemId == 995) {
            player.getActionSender().sendMessage("You cannot sell coins to the shop.");
            return;
        }
		if ((!shop.isGeneralStore() && !shop.getCurrentStock().contains(itemId)) || item.getDefinition().isUntradable()) {
			player.getActionSender().sendMessage("You cannot sell this item in this shop.");
			return;
		}
		int totalItems = inventory.getCount(itemId);
		if (amount < 1 || itemId < 0) {
			return;
		}
		if (itemId > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		if (!inventory.contains(itemId)) {
			return;
		}
		if (amount >= totalItems) {
			amount = totalItems;
		}
		int shopAmount = shop.getCurrentStock().getCount(itemId);
		player.getInventory().removeItem(new Item(itemId, amount));
		int price = ItemManager.getInstance().getItemValue(itemId, "selltoshop");
		
		int curStock = 0;
		int baseStock = 0;
		if(shop.getStock().getById(itemId) != null)
		{
			baseStock = shop.getStock().getById(itemId).getCount();
		}
		if(shop.getCurrentStock().getById(itemId) != null)
		{
			curStock = shop.getCurrentStock().getById(itemId).getCount();
		}
		int[] pricePerItem = new int[amount];
		for(int i = 1; i <= amount; i++)
		{
			int itemPrice = price;
			if(curStock >= 0 && baseStock == 0)
			{
				double diminish = diminishing_returns((curStock+i)/3d, 0.003d);
				if(diminish > 1)
				{
					itemPrice /= diminish;
				}
			}
			else if(curStock >= 0 && baseStock > 0)
			{
				double diminish = ((curStock+i)/baseStock);
				if(diminish > 1)
				{
					itemPrice /= diminish;
				}
			}
			if (shop.getCurrency() == 6529) {
				itemPrice *= 1.5d;
			}
			pricePerItem[i-1] = itemPrice;
		}
		int itemsAboveZero = 0;
		@SuppressWarnings("unused")
		int itemsZero = 0;
		for(int i = 0; i < amount; i++)
		{
			if(pricePerItem[i] > 0)
			{
				itemsAboveZero++;
			}
			if(pricePerItem[i] <= 0)
			{
				itemsZero++;
			}
		}
		int[] finalPricePerItem = new int[itemsAboveZero];
		for(int i = 0; i < itemsAboveZero; i++)
		{
			finalPricePerItem[i] = pricePerItem[i];
		}
		int avgPrice = calculateAverage(finalPricePerItem);
		int finalPrice = avgPrice * itemsAboveZero;
		
		if (finalPrice > 0) {
			player.getInventory().addItem(new Item(currency, finalPrice));
		}
		if (shop.isGeneralStore() && shopAmount < 1) {
			shop.getCurrentStock().add(new Item(itemId, amount));
		} else {
			shop.getCurrentStock().set(shop.getCurrentStock().getSlotById(itemId), new Item(itemId, shopAmount + amount));
		}
		player.getInventory().refresh(3823);
		refreshAll(player.getShopId());
		if(finalPrice > 1000)
		{
			LogHandler.logShop(player, shop.getName(), "Sold", new Item(itemId, amount), finalPrice, ItemManager.getInstance().getItemName(currency));
		}
	}
	
	private static int calculateAverage(int[] items) {
		  Integer sum = 0;
		  if(items.length > 0) {
		    for (Integer price : items) 
		    {
	    		sum += price;
		    }
		    return (int) sum.doubleValue() / items.length;
		  }
		  return sum;
	}
	
	private static double diminishing_returns(double val, double scale) {
	    if(val < 0)
	        return -diminishing_returns(-val, scale);
	    double mult = val / scale;
	    double trinum = (Math.sqrt(8.0 * mult + 1.0) - 1.0) / 2.0;
	    return trinum * scale;
	}

	public static void getBuyValue(Player player, int id) {
		Shop shop = shops.get(player.getShopId());
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			int price = ItemManager.getInstance().getItemValue(id, "buyfromshop");
			if (shop.getCurrency() == 6529) {
				price *= 1.5d;
				price = Math.round(price);
			}
			String currencyName = ItemManager.getInstance().getItemName(shop.getCurrency());
			player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": currently costs " + Misc.formatNumber(price) + " " + currencyName + ".", true);
		} else {
			int price = getSpecialShopPrice(player, shop, id);
			player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": currently costs " + price + " " + getCurrencyName(shop) + ".", true);
		}
	}

	public static void getSellValue(Player player, int id) {
		if (new Item(id).getDefinition().isUntradable()) {
			player.getActionSender().sendMessage("You cannot sell this item to this shop.");
			return;
		}
		Shop shop = shops.get(player.getShopId());
		int price = ItemManager.getInstance().getItemValue(id, "selltoshop");
		int baseStock = 0;
		if(shop.getStock().getById(id) != null)
		{
			baseStock = shop.getStock().getById(id).getCount();
		}
		int curStock = 0;
		if(shop.getCurrentStock().getById(id) != null)
		{
			curStock = shop.getCurrentStock().getById(id).getCount();
		}
		if(curStock >= 0 && baseStock == 0)
		{
			double diminish = diminishing_returns((curStock+1)/3d, 0.003d);
			if(diminish > 1)
			{
				price /= diminish;
			}
		}
		else if(curStock >= 0 && baseStock > 0)
		{
			double diminish = ((curStock+1)/baseStock);
			if(diminish > 1)
			{
				price /= diminish;
			}
		}
		if (shop.getCurrency() == 6529) {
			price *= 1.5d;
		}
		if(price <= 0)
		{
			price = 0;
		}
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM && shop.isGeneralStore()) {
			ItemManager.getInstance().getItemName(shop.getCurrency());
			player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": shop will buy for " + Misc.formatNumber(price) + " " + getCurrencyName(shop) + ".");
		} else {
			if ((!shop.getCurrentStock().contains(id)) || new Item(id).getDefinition().isUntradable()) {
				player.getActionSender().sendMessage("You cannot sell this item in this shop.");
				return;
			}
			String currencyName = ItemManager.getInstance().getItemName(shop.getCurrency());
			ItemManager.getInstance().getItemName(shop.getCurrency());
			player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": shop will buy for " + Misc.formatNumber(price) + " " + currencyName + ".");
		}
	}

	@SuppressWarnings("incomplete-switch")
	public static int getCurrencyForShopType(Player player, Shop shop) {
		switch (shop.getCurrencyType()) {
			case SERVER_POINTS :
				return player.getServerPoints();
		}
		return -1;
	}

	@SuppressWarnings("incomplete-switch")
	public static int getSpecialShopPrice(Player player, Shop shop, int buyId) {
		switch (shop.getCurrencyType()) {
			case SERVER_POINTS :
				switch (buyId) {
					case 4152 :
						return 10;
				}
				break;
		}
		return 0;
	}

	@SuppressWarnings("incomplete-switch")
	public static void decreaseCurrencyForSpecialShop(Player player, Shop shop, int value) {
		switch (shop.getCurrencyType()) {
			case SERVER_POINTS :
				player.decreaseServerPoints(value);
				break;
		}
	}

	@SuppressWarnings("incomplete-switch")
	public static String getCurrencyName(Shop shop) {
		switch (shop.getCurrencyType()) {
			case SERVER_POINTS :
				return "Server Points";
		}
		return "Coins";
	}

	public static void loadShops() throws IOException {
		//List<Shop> list = (List<Shop>) XStreamUtil.getxStream().fromXML(new FileInputStream("./data/content/shops.xml"));
		FileReader reader = new FileReader("./datajson/content/shops.json");
		try{
			if(shops != null && shops.size() > 0)
			{
				shops.clear();
			}
			List<Shop> list = new Gson().fromJson(reader, new TypeToken<List<Shop>>(){}.getType());
	        for (Shop shop : list) {
				Container stock = new Container(Type.ALWAYS_STACK, SIZE);
				Container currentStock = new Container(Type.ALWAYS_STACK, SIZE);
				shops.add(shop);
				for (Item item : shop.getItems()) {
					if (item != null) {
						stock.add(item);
						currentStock.add(item);
					}
				}
				shop.setStock(stock);
				shop.setCurrentStock(currentStock);
			}
	        reader.close();
			System.out.println("Loaded " + shops.size() + " shop definitions json.");
		} catch (IOException e) {
			reader.close();
			System.out.println("failed to load shop definitions json.");
		}
		// start shop process
		ShopManager.process();
	}

	public static void process() {
		World.submit(new Tick(30) {
			@Override
			public void execute() {
				int count = 0;
				for (Shop shop : shops) {
					for (Item item : shop.getCurrentStock().getItems()) {
						if (item == null) {
							continue;
						}
						if (shop.getStock().contains(item.getId())) {
							int minimum = shop.getStock().getById(item.getId()).getCount();
							if (item.getCount() < minimum) {
								shop.getCurrentStock().add(new Item(item.getId()));
							} else if (item.getCount() > minimum && item.getCount() > 0) {
								shop.getCurrentStock().removeOrZero(new Item(item.getId()));
							}
						} else {
							count = item.getCount();
							if(count == 1){
								if(rand.nextInt(9) == 0)
								{
									shop.getCurrentStock().remove(new Item(item.getId()));
								}
							}
							else if(count < 30 && count > 1)
							{
								for(int x = 0; x < ((count/20)+1); x = x+1) {
									shop.getCurrentStock().remove(new Item(item.getId()));
								}
							}
							else if(count < 100 && count >= 30)
							{
								for(int x = 0; x < ((count/15)+1); x = x+1) {
									shop.getCurrentStock().remove(new Item(item.getId()));
								}
							}
							else if(count >= 100)
							{
								for(int x = 0; x < ((count/10)+1); x = x+1) {
									shop.getCurrentStock().remove(new Item(item.getId()));
								}
							}
							//break;
						}
					}
					refreshAll(shop.getShopId());
				}
				for (Item item : MageRewardHandling.stock.toArray()) {
				    if (item == null) {
					continue;
				    }
				    if (item.getCount() < 100) {
					MageRewardHandling.stock.set(MageRewardHandling.stock.getSlotById(item.getId()), new Item(item.getId(), item.getCount() + 1));
					for(Player player : World.getPlayers()) {
					    if(player != null && (Boolean)player.getAttributes().get("isShopping")) {
						player.getActionSender().sendUpdateItems(MageRewardHandling.ITEM_GROUP, MageRewardHandling.stock.toArray());
					    }
					}
				    }
				}
			}
		});
	}

	public static void setShops(List<Shop> items) {
		ShopManager.shops = items;
	}

	public static List<Shop> getShops() {
		return shops;
	}

	public static class Shop {

		private int shopId;
		private String name;
		private Item[] items;
		private boolean isGeneralStore;
		private CurrencyTypes currencyType;
		private int currency;
		private Container stock;
		private Container currentStock;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setShopId(int shopId) {
			this.shopId = shopId;
		}

		public int getShopId() {
			return shopId;
		}

		public void setGeneralStore(boolean isGeneralStore) {
			this.isGeneralStore = isGeneralStore;
		}

		public boolean isGeneralStore() {
			return isGeneralStore;
		}

		public void setCurrency(int currency) {
			this.currency = currency;
		}

		public int getCurrency() {
			return currency;
		}

		public CurrencyTypes getCurrencyType() {
			return currencyType;
		}

		public void setItems(Item[] items) {
			this.items = items;
		}

		public Item[] getItems() {
			return items;
		}

		public void setStock(Container stock) {
			this.stock = stock;
		}

		public Container getStock() {
			return stock;
		}

		public void setCurrentStock(Container currentStock) {
			this.currentStock = currentStock;
		}

		public Container getCurrentStock() {
			return currentStock;
		}

		enum CurrencyTypes {
			ITEM, SERVER_POINTS
		}
	}

}
