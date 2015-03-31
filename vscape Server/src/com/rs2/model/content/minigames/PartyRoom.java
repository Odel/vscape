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
import com.rs2.model.content.Emotes.EMOTE;
import com.rs2.model.content.dialogue.DialogueManager;
import com.rs2.model.content.dialogue.Dialogues;
import static com.rs2.model.content.dialogue.Dialogues.DISTRESSED;
import static com.rs2.model.content.dialogue.Dialogues.EVIL;
import static com.rs2.model.content.dialogue.Dialogues.HAPPY;
import static com.rs2.model.content.dialogue.Dialogues.LONGER_LAUGHING;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.task.Task;
import com.rs2.task.TaskScheduler;
import com.rs2.util.Misc;

public class PartyRoom {

	private static final MinigameAreas.Area dropArea = new MinigameAreas.Area(2731, 2744, 3463, 3475, 0);
	
	private static final int DROP_CHEST_INTERFACE = 2156;
	public static final int DROP_CHEST_INVENTORY = 2273;
	public static final int DROP_CHEST_INVENTORY_DEPOSIT = 2274;
	
	private static final int MAX_CHEST_ITEMS = 215;
	public static final int MAX_CHEST_DEPOSIT_ITEMS = 8;
	
	public static Container CHEST_CONTAINER = new Container(Type.STANDARD, MAX_CHEST_ITEMS);
	
	private final static String[] knightSong = {
		"We're knights of the party room",
		"We dance round and round like a loon",
		"Quite often we like to sing",
		"Unfortunately we make a din",
		"We're knights of the party room",
		"Do you like our helmet plumes?",
		"Everyone's happy now we can move",
		"Like a party animal in the groove"
	};

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
			player.getActionSender().sendMessage("@red@This feature is disabled.", true);
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
		if(buttonId == 8198 && player.getStatedInterface().equals("dropPartyChest"))
		{
			if(!Constants.PARTY_ROOM_ENABLED){
				player.getActionSender().sendMessage("@red@This feature is disabled.", true);
				return true;
			}
			Container depositContainer = player.getPartyDeposit();
			if(depositContainer == null || depositContainer.size() <= 0)
				return false;
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
	
	private static long chestValue(){
		long value = 0;
		if(CHEST_CONTAINER != null)
		{
			Item[] chestItems = CHEST_CONTAINER.toArray();
			if(chestItems != null && chestItems.length > 0){
				for(int i = 0; i < chestItems.length; i++)
				{
					Item item = chestItems[i];
					if (item == null || !item.validItem()) {
						continue;
					}
					if(item.getId() != 995){
						if(item.getDefinition().getHighAlcValue() > 0)
						{
							value += (item.getCount() * item.getDefinition().getHighAlcValue());
						}
					}else{
						value += item.getCount();
					}
				}
			}
		}
		return value;
	}
	
	private static int dropDelay(){
		int delay = 16;
		long value = chestValue();
		if(value >= 0 && value < 50000){
			delay = 16;
		} else if(value >= 50000 && value < 150000){
			delay = 100;
		} else if(value >= 150000 && value < 1000000){
			delay = 500;
		} else if(value >= 1000000){
			delay = 1000;
		}
		return delay;
	}
	
	public static void dropItems() {
		if(droppingItems)
			return;
		droppingItems = true;
		Item[] chestItems = CHEST_CONTAINER.toArray();
		if(chestItems == null || chestItems.length <= 0)
			return;
		final List<Item> itemsToDropL = new ArrayList<Item>();
		itemsToDropL.clear();
		for(int i = 0; i < chestItems.length; i++)
		{
			Item item = chestItems[i];
			if (item == null || !item.validItem()) {
				continue;
			}
			if(item.getDefinition().isStackable() && item.getCount() > 10)
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
		World.submit(new Tick(3) {
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
					int rAmnt = Misc.random(1, 3);
					for(int i = 0; i < rAmnt; i++)
					{
						createBalloon(null);
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
		PartyBalloon pb;
		if(item != null && item.validItem())
		{
			pb = new PartyBalloon(dropPos.clone(), new Item(item.getId(), item.getCount()));
		}else{
			pb = new PartyBalloon(dropPos.clone(), null);
		}
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
				if(!Constants.PARTY_ROOM_ENABLED){  //can't use lever if Party Room disabled
					player.getActionSender().sendMessage("@red@This feature is disabled.", true);
					return true;
				}
				Dialogues.startDialogue(player, 66000);
			return true;
			case 2417:
				if(!Constants.PARTY_ROOM_ENABLED){  //can't open chest if Party Room disabled
					player.getActionSender().sendMessage("@red@This feature is disabled.", true);
					return true;
				}
				player.getUpdateFlags().sendAnimation(832);
				new GameObject(2418, 2729, 3470, 0, def.getFace(), def.getType(), 2417, 500);
			return true;
		}
		return false;
	}
	
	public static boolean handleDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		DialogueManager d = player.getDialogue();
		switch (id) {
			case 659:  //Party Pete
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("Hi! I'm Party Pete. Welcome to the Party Room!", HAPPY);
						return true;
					case 2:
						d.sendOption("So what is this room for?", "What's the big lever over there for?", "What's the gold chest for?", "I wanna party!");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								d.sendNpcChat("This room is for partying the night away!", HAPPY);
								d.setNextChatId(4);
								return true;
							case 2:
								d.sendNpcChat("Simple. With the lever you can do some fun stuff.", HAPPY);
								d.setNextChatId(8);
								return true;
							case 3:
								d.sendNpcChat("Any items that are in the chest will be dropped inside the", "balloons when you pull the lever!", HAPPY);
								d.setNextChatId(12);
								return true;
							case 4:
								d.sendNpcChat("I've won the Dance Trophy at the Kandarin Ball", "three years in a trot!", HAPPY);
								d.setNextChatId(14);
								return true;
						}
						break;
					case 4:
						d.sendPlayerChat("How do you have a party in " + Constants.SERVER_NAME + "?", DISTRESSED);
						return true;
					case 5:
						d.sendNpcChat("Get a few m8s round, get the beers in and have fun!", HAPPY);
						return true;
					case 6:
						d.sendNpcChat("Some players organise parties so keep an eye open!", HAPPY);
						return true;
					case 7:
						d.sendPlayerChat("Woop! Thanks Party Pete!", HAPPY);
						d.endDialogue();
						return true;
					case 8:
						d.sendPlayerChat("What kind of stuff?", DISTRESSED);
						return true;
					case 9:
						d.sendNpcChat("A balloon drop costs 1000 gold. For this you get a bunch of", "balloons dropped across the whole of the party room.  You", "can then have fun popping the balloons! If there are items", "in the Party Drop Chest they will be inside the balloons!", HAPPY);
						return true;
					case 10:
						d.sendNpcChat("For 500 gold you can summon the Party Room", "Knights who will dance for your delight.", HAPPY);
						return true;
					case 11:
						d.sendNpcChat("Their singing isn't a delight though!", LONGER_LAUGHING);
						d.endDialogue();
						return true;
					case 12:
						d.sendPlayerChat("Cool! Sounds like a fun way to do a drop party!");
						return true;
					case 13:
						d.sendNpcChat("Exactly!  A word of warning though.", "Any items that you put in the chest can't be", "taken out again and it costs 1000 gold pieces", "for each balloon drop.");
						d.endDialogue();
						return true;
					case 14:
						d.sendPlayerChat("Show me your moves Pete!");
						d.endDialogue();
						final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
						npc.getUpdateFlags().sendAnimation(866);
						return true;
				}
				break;
			case 661: //Megan Party Room upstairs
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("Hi! I'm Megan. Welcome to the Party Room!", HAPPY);
						return true;
					case 2:
						d.sendOption("One beer please Megan!", "Can you dance Megan?");
						return true;
					case 3:
						switch (optionId) {
							case 1:
								d.sendNpcChat("Coming right up sir!  That's two gold please.", HAPPY);
								if (!player.getInventory().playerHasItem(995, 2)) {
									d.setNextChatId(4);
								} else {
									d.setNextChatId(5);
								}
								return true;
							case 2:
								d.sendNpcChat("Can I dance?", HAPPY);
								d.setNextChatId(6);
								return true;
						}
						break;
					case 4:
						d.sendPlayerChat("I'm sorry I don't have enough money!", DISTRESSED);
						d.endDialogue();
						return true;
					case 5:
						if (player.getInventory().getItemContainer().freeSlots() < 1) {
							d.sendNpcChat("Looks like you don't have enough room", "in your inventory.");
							d.endDialogue();
							return true;
						}
						player.getInventory().removeItem(new Item(995, 2));
						d.sendGiveItemNpc("Here you go!", "", new Item(1917));
						player.getInventory().addItem(new Item(1917));
						d.endDialogue();
						return true;
					case 6:
						d.sendNpcChat("CAN I DANCE?", HAPPY);
						return true;
					case 7:
						d.sendPlayerChat("Dance with me Megan.", EVIL);
						final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
						npc.getUpdateFlags().sendAnimation(866);
						player.getEmotes().performEmote(EMOTE.DANCE);
						d.endDialogue();
						return true;
				}
				break;
			case 662: //Lucy Party Room upstairs
				switch (d.getChatId()) {
					case 1:
						d.sendNpcChat("Hi! I'm Megan. Welcome to the Party Room!", HAPPY);
						return true;
					case 2:
						d.sendPlayerChat("One beer please Lucy!");
						return true;
					case 3:
						d.sendNpcChat("Coming right up sir!  That's two gold please.", HAPPY);
						if (!player.getInventory().playerHasItem(995, 2)) {
							d.setNextChatId(4);
						} else {
							d.setNextChatId(5);
						}
						return true;
					case 4:
						d.sendPlayerChat("I'm sorry I don't have enough money!", DISTRESSED);
						d.endDialogue();
						return true;
					case 5:
						if (player.getInventory().getItemContainer().freeSlots() < 1) {
							d.sendNpcChat("Looks like you don't have enough room", "in your inventory.");
							d.endDialogue();
							return true;
						}
						player.getInventory().removeItem(new Item(995, 2));
						d.sendGiveItemNpc("Here you go!", new Item(1917));
						player.getInventory().addItem(new Item(1917));
						d.endDialogue();
						return true;
				}
				break;
			case 66000 :
				switch(d.getChatId()) {
					case 1 :
						d.sendOption("Drop Balloons (1000gp)", "Dancing Knights (500gp)");
					return true;
					case 2 :
						switch(optionId) {
							case 1 :
								if(droppingItems)
								{
									d.sendStatement("There is currently a drop in progress.");	
									d.endDialogue();
									return true;
								}
								if(dropTick)
								{
									d.sendStatement("Drop countdown is in progress!");
									d.endDialogue();
									return true;
								}
								if(CHEST_CONTAINER == null || CHEST_CONTAINER.size() <= 0)
								{
									d.sendStatement("The chest is empty.");
									d.endDialogue();
									return true;
								}
								if(player.getInventory().removeItem(new Item(995, 1000)))
								{
									dropTick = true;
									final Npc pete = partyPete();
									new TaskScheduler().schedule(new Task() {
										int ticks = dropDelay();
										@Override
										protected void execute() {
											ticks--;
											if(ticks >= 0)
											{
												if(pete != null)
												{
													int time = (int)(0.6 * ticks);
													pete.getUpdateFlags().sendForceMessage(""+time);
												}
											}
											if(ticks <= 0)
											{
												stop();
												dropItems();
												dropTick = false;
												return;
											}
										}
									});
									d.sendGiveItemNpc("Dropping Balloons in " + (int)(0.6 * dropDelay()) + " Seconds.", new Item(995, 1000));
								}else{
									d.sendStatement("You don't have enough gold to do this.");
								}
								d.endDialogue();
								return true;
							case 2 :
								if(dancingKnights != null)
								{
									d.sendStatement("The knights are already dancing!");
									d.endDialogue();
									return true;
								}
								if(player.getInventory().removeItem(new Item(995, 500)))
								{
									spawnDancingKnights();
									d.sendGiveItemNpc("The knights begin to dance and sing on the table.", new Item(995, 500));
								}else{
									d.sendStatement("You don't have enough gold to do this.");
								}
								d.endDialogue();
								return true;
						}
					break;
				}
			break;
		}
		return false;
	}
	
	public static boolean objectSecond(final Player player, GameObjectDef def){
		if(def == null)
			return false;
		int id = def.getId();
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
	
	private static Npc partyPete(){
		for(Npc npc : World.getNpcs())
		{
			if(npc == null || npc.isDead() || !npc.isVisible())
				continue;
			if(npc.getNpcId() == 659)
				return npc;
		}
		return null;
	}
	
	private static void spawnDancingKnights(){
		if(dancingKnights != null)
			return;
		dancingKnights = new Npc[6];
		int x = 2735;
		for(int i = 0; i < dancingKnights.length; i++)
		{
			Npc knight = new Npc(660);
			knight.setPosition(new Position(x, 3468, 0));
			knight.setSpawnPosition(new Position(x, 3468, 0));
			knight.setFace(0);
			World.register(knight);
			dancingKnights[i] = knight;
			x++;
		}
		World.submit(new Tick(6) {
			int songPos = 0;
		    @Override 
		    public void execute() {
		    	if(dancingKnights == null || songPos >= knightSong.length + 1)
		    	{
		    		stop();
		    		destroyDancingKnights();
		    		return;
		    	}
				if(dancingKnights != null && songPos < knightSong.length)
				{
					Npc knight = dancingKnights[Misc.random(dancingKnights.length-1)];
					if(knight == null)
						return;
					knight.getUpdateFlags().sendForceMessage(knightSong[songPos]);
				}
		    	songPos++;
		    }
		});
	}
	
	private static void destroyDancingKnights(){
		if(dancingKnights != null)
		{
			for(int i = 0; i < dancingKnights.length; i++)
			{
				Npc knight = dancingKnights[i];
				if(knight == null)
					continue;
				NpcLoader.destroyNpc(knight);
				dancingKnights[i] = null;
			}
			dancingKnights = null;
		}
	}
	
	private static boolean droppingItems = false;
	private static boolean dropTick = false;
	private static List<PartyBalloon> balloons;
	private static Npc[] dancingKnights;
	
	public static class PartyBalloon {
		public Position position;
		public Item item;
		private GroundItem groundItem;
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
				if(player != null)
				{
					groundItem = new GroundItem(new Item(item.getId(), item.getCount()), player, position.clone());
				} else {
					groundItem = new GroundItem(new Item(item.getId(), item.getCount()), position.clone(), false);
				}
				item = null;
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
					if(groundItem != null) {
						GroundItemManager.getManager().dropItem(groundItem);
					}
			    	stop();
			    }
			});
			return true;
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
