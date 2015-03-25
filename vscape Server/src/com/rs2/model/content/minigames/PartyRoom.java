package com.rs2.model.content.minigames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class PartyRoom {

	private static final MinigameAreas.Area dropArea = new MinigameAreas.Area(2731, 2744, 3463, 3475, 0);
	
	private static final int DROP_CHEST_INTERFACE = 2156;
	public static final int DROP_CHEST_INVENTORY = 2273;
	public static final int DROP_CHEST_INVENTORY_DEPOSIT = 2274;
	
	private static final int MAX_CHEST_ITEMS = 215;
	public static final int MAX_CHEST_DEPOSIT_ITEMS = 8;
	
	public static Container CHEST_CONTAINER = new Container(Type.STANDARD, MAX_CHEST_ITEMS);

	public static void OpenChestInterface(final Player player){
		Item[] inventoryItems = player.getInventory().getItemContainer().toArray();
		Item[] chestItems = CHEST_CONTAINER.toArray();
		Item[] depositItems = player.getPartyDeposit().toArray();
		player.getActionSender().sendUpdateItems(DROP_CHEST_INVENTORY_DEPOSIT, depositItems);
		player.getActionSender().sendUpdateItems(3322, inventoryItems);
		player.getActionSender().sendUpdateItems(DROP_CHEST_INVENTORY, chestItems);
		player.getActionSender().sendInterface(DROP_CHEST_INTERFACE, 3321);
		player.setStatedInterface("dropPartyChest");
	}
	
	public static void Deposit(final Player player, final int slot, final int depItem, int depAmount){
		if(!Constants.PARTY_ROOM_ENABLED){
			player.getActionSender().sendMessage("@red@This feature is disabked.", true);
			return;
		}
	    RSInterface inter = RSInterface.forId(DROP_CHEST_INTERFACE);
        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("dropPartyChest")) {
            return;
        }
		Container depositContainer = player.getPartyDeposit();
		Item inventoryItem = player.getInventory().getItemContainer().get(slot);
		if (depAmount < 1 || depItem < 0 || inventoryItem == null || inventoryItem.getId() != depItem || !inventoryItem.validItem()) {
			return;
		}
		int amount = player.getInventory().getItemContainer().getCount(depItem);
		if (inventoryItem.getDefinition().getId() > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		if (inventoryItem.getDefinition().isUntradable()) {
			player.getActionSender().sendMessage("This item is untradable.", true);
			return;
		}
		if (amount > depAmount) {
			amount = depAmount;
		}
		Item depositItem = new Item(depItem, amount);
		if (depositContainer.freeSlots() <= 0 && !depositContainer.hasRoomFor(depositItem)) {
			player.getActionSender().sendMessage("You cannot deposit anymore items.", true);
			return;
		}
		if (!depositItem.getDefinition().isStackable()) {
			if (amount > depositContainer.freeSlots()) {
				amount = depositContainer.freeSlots();
			}
		}
		if (depositItem.getDefinition().isStackable()) {
			if (!player.getInventory().removeItemSlot(new Item(depItem, amount), slot)) {
				player.getInventory().removeItem(new Item(depItem, amount));
			}
		} else {
			for (int i = 0; i < amount; i++) {
				player.getInventory().removeItem(new Item(depItem, 1));
			}
		}
		int depositAmount = depositContainer.getCount(depItem);
		if (depositAmount > 0 && depositItem.getDefinition().isStackable()) {
			depositContainer.set(depositContainer.getSlotById(depositItem.getId()), new Item(depItem, depositAmount + amount));
		} else {
			depositContainer.add(new Item(depositItem.getId(), amount));
		}
		Item[] depositItems = depositContainer.toArray();
		player.getInventory().refresh(3322);
		player.getActionSender().sendUpdateItems(DROP_CHEST_INVENTORY_DEPOSIT, depositItems);
	}
	
	public static void Withdraw(final Player player, final int slot, final int withdrawItem, int withdrawAmount){
	    RSInterface inter = RSInterface.forId(DROP_CHEST_INTERFACE);
        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("dropPartyChest")) {
            return;
        }
		Container depositContainer = player.getPartyDeposit();
		Item depositItem = depositContainer.get(slot);
		if (withdrawAmount < 1 || withdrawItem < 0 || depositItem == null || depositItem.getId() != withdrawItem || !depositItem.validItem()) {
			return;
		}
		if (withdrawItem > Constants.MAX_ITEMS) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		int amount = depositContainer.getCount(withdrawItem);
		if (amount > withdrawAmount) {
			amount = withdrawAmount;
		}
		int count = player.getInventory().addItemCount(new Item(withdrawItem, amount));
		depositContainer.remove(new Item(withdrawItem, count), slot);
		depositContainer.shift();
		Item[] depositItems = depositContainer.toArray();
		player.getInventory().refresh(3322);
		player.getActionSender().sendUpdateItems(DROP_CHEST_INVENTORY_DEPOSIT, depositItems);
	}
	
	public static void CancelOffer(final Player player)
	{
		Container depositContainer = player.getPartyDeposit();
		if(depositContainer == null || depositContainer.size() <= 0)
			return;
		player.getActionSender().removeInterfaces();
		for (int i = 0; i < MAX_CHEST_DEPOSIT_ITEMS; i++) {
			if (depositContainer.get(i) != null) {
				Item item = depositContainer.get(i);
				if (item != null) {
					depositContainer.remove(item);
					player.getInventory().addItem(item);
				}
			}
		}
		depositContainer.clear();
	}
	
	public static boolean AcceptOffer(final Player player, int buttonId)
	{
		if(!Constants.PARTY_ROOM_ENABLED){
			player.getActionSender().sendMessage("@red@This feature is disabked.", true);
			return true;
		}
		Container depositContainer = player.getPartyDeposit();
		if(depositContainer == null || depositContainer.size() <= 0)
			return false;
		if(buttonId == 8198 && player.getStatedInterface().equals("dropPartyChest"))
		{
			if(droppingItems)
			{
				player.getActionSender().sendMessage("There is currently a drop in progress.", true);
				return true;
			}
			if (CHEST_CONTAINER.freeSlot() == -1 || CHEST_CONTAINER.freeSlots() <= 0) {
				player.getActionSender().sendMessage("The Party chest is full.", true);
				return true;
			}
			for (int i = 0; i < MAX_CHEST_DEPOSIT_ITEMS; i++) {
				if (depositContainer.get(i) != null) {
					Item item = depositContainer.get(i);
					if (item != null) {
						if (CHEST_CONTAINER.freeSlot() == -1 || CHEST_CONTAINER.freeSlots() <= 0) {
							player.getActionSender().sendMessage("The Party chest is full.", true);
							break;
						}
						depositContainer.remove(item);
						CHEST_CONTAINER.set(CHEST_CONTAINER.freeSlot(), item);
					}			
				}
			}
			if(depositContainer.size() <= 0) {
				depositContainer.clear();
			}
			Item[] depositItems = depositContainer.toArray();
			player.getInventory().refresh(3322);
			player.getActionSender().sendUpdateItems(DROP_CHEST_INVENTORY_DEPOSIT, depositItems);
			RefreshChest();
			return true;
		}
		return false;
	}
	
	private static void RefreshChest() {
		Item[] chestItems = CHEST_CONTAINER.toArray();
		for (Player player : World.getPlayers()) {
			if (player != null && player.getStatedInterface().equals("dropPartyChest")){
				player.getActionSender().sendUpdateItems(DROP_CHEST_INVENTORY, chestItems);
			}
		}
	}
	
	public static void dropItems() {
		if(droppingItems)
			return;
		droppingItems = true;
		Item[] chestItems = CHEST_CONTAINER.toArray();
		final List<Item> itemsToDropL = new ArrayList<Item>();
		itemsToDropL.clear();
		for(int i = 0; i < chestItems.length; i++)
		{
			Item item = chestItems[i];
			if (item == null || !item.validItem()) {
				continue;
			}
			if(item.getDefinition().isStackable() && item.getCount() > 100)
			{
				int count = item.getCount();
				while(count > 0)
				{
					int amount = Misc.random(1, count);
					count -= amount;
					if(amount > 0)
					{
						itemsToDropL.add(new Item(item.getId(), amount));
					}
				}
			}else{
				itemsToDropL.add(new Item(item.getId(), item.getCount()));
			}
		}
		Collections.shuffle(itemsToDropL, new Random(System.nanoTime()));
		CHEST_CONTAINER.clear();
		RefreshChest();
		balloons = new ArrayList<PartyBalloon>();
		balloons.clear();
		World.submit(new Tick(5) {
			int tries = 0;
		    @Override 
		    public void execute() {
		    	Iterator<Item> dropItemsIter = itemsToDropL.iterator();
		    	if(dropItemsIter.hasNext() && droppingItems)
		    	{
		    		Item item = dropItemsIter.next();
					if (item == null || !item.validItem()) {
						return;
					}
					if(createBalloon(item))
					{
						dropItemsIter.remove();
					}
		    	}else{
			    	if(!droppingItems || balloons == null || (balloons != null && balloons.size() <= 0))
			    	{
				    	if(balloons != null)
				    	{
				    		balloons.clear();
				    	}
			    		balloons = null;
			    		droppingItems = false;
			    		stop();
			    		return;
			    	}else{
			    		tries++;
			    		if(tries > 20)
			    		{
			    			for(Iterator<PartyBalloon> balloonsIter = balloons.iterator(); balloonsIter.hasNext();){
			    				PartyBalloon pb = balloonsIter.next();
			    				if(pb != null)
			    				{
			    					if(pb.popBalloon(null))
			    					{
			    						balloonsIter.remove();
			    					}
			    				}
			    			}
			    		}
			    	}
		    	}
		    }
		});
	}
	
	private static boolean createBalloon(Item item) {
		Position dropPos = getDropPosition();
		if(balloons == null || dropPos == null)
			return false;
		PartyBalloon pb = new PartyBalloon(dropPos.clone(), new Item(item.getId(), item.getCount()));
		pb.dropBalloon();
		balloons.add(pb);
		return true;
	}
	
	private static Position getDropPosition(){
		Position dropPos = MinigameAreas.randomPosition(dropArea);
		for(PartyBalloon pb : balloons.toArray(new PartyBalloon[balloons.size()]))
		{
			if(pb == null)
				continue;
			if(pb.position.getX() == dropPos.getX() && pb.position.getY() == dropPos.getY())
			{
				return null;
			}
		}
		return dropPos;
	}
	
	private static PartyBalloon getBalloon(int id, int x, int y, int z){
		for(PartyBalloon pb : balloons.toArray(new PartyBalloon[balloons.size()]))
		{
			if(pb == null)
				continue;
			if(pb.position.getX() == x && pb.position.getY() == y && pb.position.getZ() == z)
			{
				return pb;
			}
		}
		return null;
	}
	
	public static boolean objectFirst(final Player player, GameObjectDef def){
		if(def == null)
			return false;
		int id = def.getId();
		int x = def.getPosition().getX();
		int y = def.getPosition().getY();
		int z = def.getPosition().getZ();
		if(balloons != null && balloons.size() > 0)
		{
			PartyBalloon pb = getBalloon(id, x, y, z);
			if(pb != null)
			{
				if(balloons.contains(pb)) {
					if(pb.popBalloon(player))
					{
						player.getUpdateFlags().sendAnimation(794);
						balloons.remove(pb);
					}
				}
				return true;
			}
		}
		switch(id)
		{
			case 2416 :
				if(!Constants.PARTY_ROOM_ENABLED){
					player.getActionSender().sendMessage("@red@This feature is disabked.", true);
					return true;
				}
			return true;
			case 2417:
				player.getUpdateFlags().sendAnimation(832);
				new GameObject(2418, 2729, 3470, 0, def.getFace(), def.getType(), 2417, 500);
			return true;
		}
		return false;
	}
	
	public static boolean objectSecond(final Player player, GameObjectDef def){
		if(def == null)
			return false;
		int id = def.getId();
		int x = def.getPosition().getX();
		int y = def.getPosition().getY();
		int z = def.getPosition().getZ();
		switch(id)
		{
			case 2418:
				OpenChestInterface(player);
			return true;
		}
		return false;
	}
	
	public static boolean objectThird(final Player player, GameObjectDef def){
		if(def == null)
			return false;
		int id = def.getId();
		int x = def.getPosition().getX();
		int y = def.getPosition().getY();
		int z = def.getPosition().getZ();
		switch(id)
		{
			case 2418:
				player.getUpdateFlags().sendAnimation(832);
				closeChest();
			return true;
		}
		return false;
	}
	
	private static void closeChest(){
		if(ObjectHandler.getInstance().getObject(2418, 2729, 3470, 0) != null)
		{
			ObjectHandler.getInstance().removeObject(2729, 3470, 0, 10);
		}
	}
	
	private static boolean droppingItems = false;
	private static List<PartyBalloon> balloons;
	
	public static class PartyBalloon {
		public Position position;
		public Item item;
		public GameObject balloonObj;
		public int balloonIndex;
		
		public enum Stage {
			NONE, FALLING, ONGROUND, POPPED
		};
		public Stage stage = Stage.FALLING;
		
		public PartyBalloon(Position position, Item item) {
			this.position = position;
			this.item = item;
		}
		
		public boolean popBalloon(final Player player)
		{
			if(stage.equals(Stage.FALLING) || stage.equals(Stage.POPPED))
				return false;
			if(balloonObj == null)
				return false;
			GameObjectDef balloonDef = balloonObj.getDef();
			if(balloonDef == null)
				return false;
			stage = Stage.POPPED;
			GameObject bObj = ObjectHandler.getInstance().getObject(balloonDef.getId(), balloonDef.getPosition().getX(), balloonDef.getPosition().getY(), balloonDef.getPosition().getZ());
			if(bObj != null)
			{
				ObjectHandler.getInstance().removeObject(balloonDef.getPosition().getX(), balloonDef.getPosition().getY(), balloonDef.getPosition().getZ(), balloonDef.getType());
			}
			if(item != null && item.validItem())
			{
				final GroundItem groundItem;
				if(player != null)
				{
					groundItem = new GroundItem(new Item(item.getId(), item.getCount()), player, position.clone());
				} else {
					groundItem = new GroundItem(new Item(item.getId(), item.getCount()), position.clone(), false);

				}
				balloonObj = new GameObject(balloons[balloonIndex][1], position.getX(), position.getY(), position.getZ(), 0, 10, -1, 999999, true);
				World.submit(new Tick(1) {
				    @Override 
				    public void execute() {
						GameObjectDef balloonDefP = balloonObj.getDef();
						if(balloonDefP != null)
						{
							GameObject bObj = ObjectHandler.getInstance().getObject(balloonDefP.getId(), balloonDefP.getPosition().getX(), balloonDefP.getPosition().getY(), balloonDefP.getPosition().getZ());
							if(bObj != null)
							{
								ObjectHandler.getInstance().removeObject(balloonDefP.getPosition().getX(), balloonDefP.getPosition().getY(), balloonDefP.getPosition().getZ(), balloonDefP.getType());
							}
						}
						balloonObj = null;
						item = null;
						if(groundItem != null) {
							GroundItemManager.getManager().dropItem(groundItem);
						}
				    	stop();
				    }
				});
				return true;
			}
			return false;
		}
		
		public void dropBalloon()
		{
			int balloonId = randomBalloon();
			balloonObj = new GameObject(balloonId, position.getX(), position.getY(), position.getZ(), 0, 10, -1, 999999, true);
			stage = Stage.FALLING;
			World.submit(new Tick(4) {
			    @Override 
			    public void execute() {
			    	stage = Stage.ONGROUND;
			    	stop();
			    }
			});
		}
		
		private int randomBalloon(){
			balloonIndex = Misc.randomMinusOne(balloons.length);
			return balloons[balloonIndex][0];
		}
		
		private static int[][] balloons = {{115, 123}, {116, 124}, {117, 125}, {118, 126}, {119, 127}, {120, 128}, {121, 129}, {122, 130}};
	}
}
