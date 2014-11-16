package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class DesertHeat {
	
	//TODO OTHER DESERT SHOPS
	
	private final int KNIFE = 946;
	
	private final int baseDehydrateTime = 90;
	private final int dehydrateDmgTime = 60;
	private final int cycleInterval = 10;
	
	private final int[] waterSkins = {1823,1825,1827,1829};
	private final int[] waterSkinsFillable = {1825,1827,1829,1831};
	public enum WaterSkin {
		ZERO(1831, -1, false, 1829),
		ONE(1829, 1831, true, 1827),
		TWO(1827, 1829, true, 1825),
		THREE(1825, 1827, true, 1823),
		FOUR(1823, 1825, true, -1);
		
		private int id;
		private int drankId;
		private boolean drinkable;
		private int fillId;
		
		private WaterSkin(int id, int drankId, boolean drinkable, int fillId)
		{
			this.id = id;
			this.drankId = drankId;
			this.drinkable = drinkable;
			this.fillId = fillId;
		}
		
		public static WaterSkin forId(int itemId) {
			for (WaterSkin waterSkin : WaterSkin.values()) {
				if (waterSkin.id == itemId)
					return waterSkin;
			}
			return null;
		}
		
		public int getId() {
			return id;
		}

		public int getDrankId() {
			return drankId;
		}
		
		public int getFillId() {
			return fillId;
		}
		
		public boolean getDrinkable() {
			return drinkable;
		}
	}
	
	private Player player;
	public DesertHeat(Player player)
	{
		this.player = player;
	}

	private void ProcessDesertHeat(){
		if(desertHeatRunning || !player.inDesert())
			return;
		desertHeatRunning = true;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if(!desertHeatRunning || !player.inDesert() || player.isDead()){
					ResetDesertHeat();
					container.stop();
					return;
				}
				int finalDehydrateTime = baseDehydrateTime + DesertEquipment();
				if(!dehydrated){
					if(curDehydrateTime < finalDehydrateTime)
					{
						curDehydrateTime += cycleInterval;
						if(curDehydrateTime >= finalDehydrateTime)
						{
							if(!DrinkWater()){
								player.hit(1, HitType.NORMAL);
								dehydrated = true;
							}
							curDehydrateTime = 0;
						}
					}
				}else{
					if(curDehydrateDmgTime < dehydrateDmgTime)
					{
						curDehydrateDmgTime += cycleInterval;
						if(curDehydrateDmgTime >= dehydrateDmgTime)
						{
							if(!DrinkWater()){
								player.hit(1, HitType.NORMAL);
							}
							curDehydrateDmgTime = 0;
						}
					}
				}
			}

			@Override
			public void stop() {

			}
		}, cycleInterval);
	}
	
	public void CheckDesertHeat(){
		if(!desertHeatRunning){
			if(player.inDesert())
			{
				ProcessDesertHeat();
			}
		}
	}
	
	private void ResetDesertHeat(){
		desertHeatRunning = false;
		ResetDehydrated();
	}
	
	private void ResetDehydrated(){
		curDehydrateDmgTime = 0;
		curDehydrateTime = 0;
		dehydrated = false;	
	}
	
	private boolean DrinkWater(){
		for(int wsid : waterSkins)
		{
			if(player.getInventory().playerHasItem(wsid))
			{
				WaterSkin waterSkin = WaterSkin.forId(wsid);
				if(waterSkin != null)
				{
					if(waterSkin.drinkable)
					{
						player.getActionSender().sendMessage("You drink the "+new Item(waterSkin.getId()).getDefinition().getName()+".");
						if(waterSkin.getDrankId() != -1){
							player.getInventory().removeItem(new Item(waterSkin.getId()));
							player.getInventory().addItem(new Item(waterSkin.getDrankId()));
						}
						ResetDehydrated();
						return true;
					}
				}
			}
		}
		if(!dehydrated){
			player.getActionSender().sendMessage("@red@You don't have any Water to drink!");
		}
		return false;
	}
	
	private boolean hasFillableWaterSkin(){
		for(int wsfid : waterSkinsFillable)
		{
			if(player.getInventory().playerHasItem(wsfid))
			{
				WaterSkin waterSkin = WaterSkin.forId(wsfid);
				if(waterSkin != null)
				{
					if(waterSkin.getFillId() != -1)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean CutCactus(final int id, int x, int y, int z, int face) {
		if(id == 2670){
			if (!player.getInventory().getItemContainer().contains(KNIFE)) {
				player.getDialogue().sendStatement("You need a knife to do this.");
				player.getDialogue().endDialogue();
				return false;
			}
			if(!hasFillableWaterSkin())
			{
				player.getActionSender().sendMessage("You don't have any waterskins to fill with water.");
				return false;
			}
			for(int wsfid : waterSkinsFillable)
			{
				if(player.getInventory().playerHasItem(wsfid))
				{
					WaterSkin waterSkin = WaterSkin.forId(wsfid);
					if(waterSkin != null)
					{
						if(waterSkin.getFillId() != -1)
						{
							player.getActionSender().sendMessage("You cut the cactus open with the Knife and fill the "+new Item(waterSkin.getId()).getDefinition().getName()+" with one dose.");
							if(waterSkin.getFillId() != -1){
								player.getInventory().removeItem(new Item(waterSkin.getId()));
								player.getInventory().addItem(new Item(waterSkin.getFillId()));
							}
							player.getSkill().addExp(Skill.WOODCUTTING, 10d);
							new GameObject(2671, x, y, z, face, 10, 2670, 60);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private int DesertEquipment(){
		int dehydrateTimeModifer = 0;
		int[] slots = {Constants.HAT, Constants.CHEST, Constants.LEGS, Constants.FEET, Constants.HANDS};
		slotLoop : for(int slot : slots){
			int itemId = player.getEquipment().getId(slot);
			if(itemId <= 0)
				continue slotLoop;
			switch(itemId)
			{
				case 1833 :
				case 1835 :
				case 6384 :
				case 6386 :
				case 6390 :
					dehydrateTimeModifer += 12;
				break;
				case 1837 :
				case 6388 :
					dehydrateTimeModifer += 6;
				break;
			}
			ItemDefinition itemDef = new Item(itemId).getDefinition();
			if(itemDef != null){
				for(int bonus : itemDef.getBonuses())
				{
					if(bonus > 0)
					{
						switch(slot)
						{
							case Constants.HAT :
							case Constants.FEET :
							case Constants.HANDS :
								dehydrateTimeModifer -= 6;
								continue slotLoop;
							case Constants.CHEST :
								dehydrateTimeModifer -= 24;
								continue slotLoop;
							case Constants.LEGS :
								dehydrateTimeModifer -= 18;
								continue slotLoop;
						}
					}
				}
			}
		}
		return dehydrateTimeModifer;
	}
	
	public void setDehydrateTime(int time)
	{
		curDehydrateTime = time;
	}
	
	public int getDehydrateTime()
	{
		return curDehydrateTime;
	}
	
	public void setDehydrateDmgTime(int time)
	{
		curDehydrateDmgTime = time;
	}
	
	public int getDehydrateDmgTime()
	{
		return curDehydrateDmgTime;
	}
	
	public void setDehydrated(boolean state)
	{
		dehydrated = state;
	}
	
	public boolean getDehydrated()
	{
		return dehydrated;
	}
	
	private boolean desertHeatRunning = false;
	private int curDehydrateTime = 0;
	private int curDehydrateDmgTime = 0;
	private boolean dehydrated = false;
}
