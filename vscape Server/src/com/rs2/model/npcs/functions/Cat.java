package com.rs2.model.npcs.functions;

import java.util.Arrays;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class Cat {
/*	
	private final int[] catItems = {
		1555,1556,1557,1558,1559,1560,
		1561,1562,1563,1564,1565,1566,
		1567,1568,1569,1570,1571,1572
	};
*/	
	private final int[] validFood = {
		315,317,319,321,325,327,329,331,
		333,335,337,338,339,341,345,347,
		349,351,353,355,359,361,363,365,
		371,373,377,379,383,385,389,391,
		395,397,1552,1927,7944,7946
	};
	
	private Player player;
	
	public Cat(final Player player)
	{
		this.player = player;
	}
	
	public static enum CatData {
		CAT1(1555, 1561, 1567, 761, 768, 774),
		CAT2(1556, 1562, 1568, 762, 769, 775),
		CAT3(1557, 1563, 1569, 763, 770, 776),
		CAT4(1558, 1564, 1570, 764, 771, 777),
		CAT5(1559, 1565, 1571, 765, 772, 778),
		CAT6(1560, 1566, 1572, 766, 773, 779);
		
		private int kittenItemId;
		private int adultItemId;
		private int overgrownItemId;
		private int kittenNpcId;
		private int adultNpcId;
		private int overgrownNpcId;
		
		private CatData(int kittenItemId, int adultItemId, int overgrownItemId,
				int kittenNpcId, int adultNpcId, int overgrownNpcId) {
			this.kittenItemId = kittenItemId;
			this.adultItemId = adultItemId;
			this.overgrownItemId = overgrownItemId;
			this.kittenNpcId = kittenNpcId;
			this.adultNpcId = adultNpcId;
			this.overgrownNpcId = overgrownNpcId;
		}
		
		public static CatData forItemId(int item) {
			for (CatData catData : CatData.values()) {
				if (catData.kittenItemId == item)
					return catData;
				if (catData.adultItemId == item)
					return catData;
				if (catData.overgrownItemId == item)
					return catData;
			}
			return null;
		}
		
		public static CatData forNpcId(int npc) {
			for (CatData catData : CatData.values()) {
				if (catData.kittenNpcId == npc)
					return catData;
				if (catData.adultNpcId == npc)
					return catData;
				if (catData.overgrownNpcId == npc)
					return catData;
			}
			return null;
		}
		
		public int npcIdForItem(int item)
		{
			if (kittenItemId == item)
				return kittenNpcId;
			if (adultItemId == item)
				return adultNpcId;
			if (overgrownItemId == item)
				return overgrownNpcId;
			return -1;
		}
		
		public int itemIdForStage(int stage)
		{
			if (stage == 0)
				return kittenItemId;
			if (stage == 1)
				return adultItemId;
			if (stage == 2)
				return overgrownItemId;
			return -1;
		}
		
		public int npcIdForStage(int stage)
		{
			if (stage == 0)
				return kittenNpcId;
			if (stage == 1)
				return adultNpcId;
			if (stage == 2)
				return overgrownNpcId;
			return -1;
		}
		
		public int getKittenItemId() {
			return kittenItemId;
		}
		public int getKittenNpcId() {
			return kittenNpcId;
		}
	}
	
	public boolean registerCat(int itemID) {
		if(!hasCat() || cat != null)
			return false;
		if(itemID != catItemId)
			return false;
		CatData catData = CatData.forItemId(catItemId);
		if(catData == null)
			return false;
		if(catData.npcIdForItem(catItemId) == -1)
			return false;
		player.getInventory().removeItem(new Item(catItemId, 1));
		registerCatNpc(catData.npcIdForItem(catItemId));
		catActive = true;
		if(growthStage < 2){
			processCat();
		}
		return true;
	}
	
	public void registerCatNpc(int npcId) {
		cat = new Npc(npcId);
		cat.setPosition(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
		cat.setSpawnPosition(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
		World.register(cat);      
		cat.setFollowingEntity(player);
		cat.setPlayerOwner(player.getIndex());
		cat.setAsPet(true);
		catActive = true;
	}
	
	public boolean unregisterCat() {
		if(!hasCat())
			return false;
		if (cat == null)
			return false;
		if(player.getInventory().canAddItem(new Item(catItemId)) || player.getBankManager().roomForItem(new Item(catItemId))) {
			cat.setVisible(false);
            World.unregister(cat);
            Following.resetFollow(cat);
            cat = null;
            player.getActionSender().sendMessage("You pick up your cat.");
            player.getInventory().addItemOrBank(new Item(catItemId, 1));
            catActive = false;
		}else if(!player.getInventory().canAddItem(new Item(catItemId,1)) && !player.getBankManager().roomForItem(new Item(catItemId,1))) {
			RunAway(true);
		}
		return true;
	}
	
	public boolean unregisterCat(Npc npc) {
		if(!hasCat())
			return false;
		if (cat == null)
			return false;
		if(!ownsCatNpc(npc))
			return false;
		unregisterCat();
		return true;
	}
	
	public void processCat(){
		if(!hasCat() || cat == null)
			return;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if(!hasCat() || !catActive || growthStage == 2){
					container.stop();
					return;
				}
				if(cat != null && catActive)
				{
					if(cat.getFollowingEntity() != null && cat.getFollowingEntity() == player)
					{
						if(growthStage < 2){
							if(growthTime < maxGrowthTime)
							{
								growthTime++;
							}
							if(growthTime >= maxGrowthTime)
							{
								Grow();
							}
						}
						if(growthStage >= 2){
							container.stop();
							return;
						}
						if(growthStage == 0){
							if(hungerTime < maxHungerTime)
							{
								hungerTime++;
							}
							int hungerPercentage = (int)Misc.toPercent(hungerTime, maxHungerTime);
							if(hungerPercentage > 0 && !hungry)
							{
								hungry = true;
							}
							if(hungerPercentage >= 83 && !hungerMessage)
							{
								player.getDialogue().sendStatement("Your pet is hungry. Use an item from your inventory on your pet to","attempt to feed it. Each pet has a specific food it likes to eat.");
								hungerMessage = true;
							}
							if(hungerPercentage >= 93 && !hungerMessage2)
							{
								player.getDialogue().sendStatement("@red@Your pet is very hungry. Use an item from your inventory on your","@red@ pet to try to feed it. If you have no suitable food, pick your pet up to","@red@stop it getting hungerier.");
								hungerMessage2 = true;
							}
							if(hungerPercentage >= 100)
							{
								RunAway(false);
								container.stop();
								return;
							}
						}
					}
				}
			}
			@Override
			public void stop() {
				
			}
		}, 60);
	}
	
	public void Grow(){
		if(!hasCat() || cat == null || !catActive)
			return;
		CatData catData = CatData.forItemId(catItemId);
		if(catData == null)
			return;
		if(catData.itemIdForStage(growthStage+1) == -1)
			return;
		if(catData.npcIdForStage(growthStage+1) == -1)
			return;
		ResetHunger();
		growthTime = 0;
		growthStage++;
		catItemId = catData.itemIdForStage(growthStage);
		cat.sendTransform(catData.npcIdForStage(growthStage), 999999);
		cat.getUpdateFlags().setForceChatMessage("Meow!");
	}
	
	public boolean Feed(int itemId){
		if(!hasCat() || cat == null  || !catActive)
			return false;
		if(growthStage > 0)
			return false;
		if(!player.getInventory().playerHasItem(itemId))
			return false;
		if(Arrays.binarySearch(validFood, itemId) <= -1){
			return false;
		}
		if(!hungry){
			player.getActionSender().sendMessage("Your kitten is not hungry right now.");
			return true;
		}
		String itemName = new Item(itemId).getDefinition().getName();
		player.getActionSender().sendMessage("You feed the kitten "+(itemName.endsWith("s") ? "some" : "a") + " " + itemName);
		player.getInventory().removeItemSlot(new Item(itemId,1), player.getSlot());
		if(itemId == 1927){
			player.getInventory().addItemToSlot(new Item(1925,1), player.getSlot());
		}
		ResetHunger();
		cat.getUpdateFlags().setForceChatMessage("Meow!");
		return true;
	}
	
	public boolean itemOnNpc(int item, Npc npc){
		if(!hasCat() || cat == null)
			return false;
		if(!ownsCatNpc(npc))
			return false;
		if(cat != npc)
			return false;
		if(Feed(item))
		{
			return true;
		}
		return false;
	}
	
	public boolean interact(Npc npc)
	{
		if(!hasCat() || cat == null)
			return false;
		if(!ownsCatNpc(npc))
			return false;
		if(cat != npc)
			return false;
		//TODO INTERACT
		if(Dialogues.startDialogue(player, 100000)){
			return true;
		}
		return false;
	}
	
	public void RunAway(boolean shooAway) {
		if(cat != null) {
			cat.getUpdateFlags().setForceChatMessage("Meow!..");
			cat.getFollowing().stepAway();
			cat.setVisible(false);
            World.unregister(cat);
            Following.resetFollow(cat);
            cat = null;
			catActive = false;
			ResetHunger();
			catItemId = 0;
			growthStage = 0;
			growthTime = 0;
			attentionTime = 0;
			ratsCaught = 0;
			if(!shooAway){
				player.getDialogue().sendStatement("@red@Your kitten has run off to look for food!");
			}else{
				player.getUpdateFlags().setForceChatMessage("Shoo!");
				player.getDialogue().sendStatement("@red@Your cat has ran away!");
			}
		}
	}
	
	public void resetCat() {
		cat = null;
		catActive = false;
		ResetHunger();
		catItemId = 0;
		growthStage = 0;
		growthTime = 0;
		attentionTime = 0;
		ratsCaught = 0;
	}

	public void ResetHunger(){
		hungerTime = 0;
		hungry = false;
		hungerMessage = false;
		hungerMessage2 = false;
	}
	
	/*public void initChecks(){
		if(player.getQuestStage(32) <= 0){
			for(int item : catItems){
				if(player.hasItem(item))
				{
		            cat = null;
					catActive = false;
					ResetHunger();
					catItemId = 0;
					growthStage = 0;
					growthTime = 0;
					attentionTime = 0;
					ratsCaught = 0;
					if(player.getInventory().playerHasItem(item))
					{
						player.getInventory().removeItem(new Item(item));
					}
					if(player.getBankManager().ownsItem(item))
					{
						player.getBankManager().remove(new Item(item));
					}
				}
			}
		}
	}*/
	
	public Npc catNpc(){
		return cat;
	}
	
	public boolean ownsCatNpc(Npc npc){
		return (cat != null && npc.getPlayerOwner() != null && npc.getPlayerOwner() == player);
	}
	
	public boolean hasCat(){
		if(catItemId > 0){
			if(cat != null && cat.getPlayerOwner() != null && cat.getPlayerOwner() == player)
			{
				return true;
			}else{
				if(player.getInventory().ownsItem(catItemId) || player.getBankManager().ownsItem(catItemId))
				{
					return true;
				}else{
					catItemId = 0;
				}
			}
		}
		return false;
	}
	
	public void setCatItem(int itemId)
	{
		catItemId = itemId;
	}
	
	public int getCatItem()
	{
		return catItemId;
	}
	
	public void setGrowthStage(int stage)
	{
		growthStage = stage;
	}
	
	public int getGrowthStage()
	{
		return growthStage;
	}
	
	public void setGrowthTime(int time)
	{
		growthTime = time;
	}
	
	public int getGrowthTime()
	{
		return growthTime;
	}
	
	public void setHungerTime(int time)
	{
		hungerTime = time;
		if(hungerTime > 0 && !hungry && growthStage == 0)
		{
			hungry = true;
		}
	}
	
	public int getHungerTime()
	{
		return hungerTime;
	}
	
	public void setAttentionTime(int time)
	{
		attentionTime = time;
	}
		
	public int getAttentionTime()
	{
		return attentionTime;
	}
	
	public void setRatsCaught(int caught)
	{
		ratsCaught = caught;
	}
	
	public int getRatsCaught()
	{
		return ratsCaught;
	}
	
	private Npc cat;
	private int catItemId = 0;
	private boolean catActive = false;
	private int growthStage = 0; // 0 - kitten 1 - adult 2 - overgrown
	private int growthTime = 0;
	private final int maxGrowthTime = 90;
	private int hungerTime = 0;
	private final int maxHungerTime = 30;
	private boolean hungry = false;
	private boolean hungerMessage = false, hungerMessage2 = false;
	private int attentionTime = 0;
	//private final int maxAttentionTime = 20;
	private int ratsCaught = 0;
}
