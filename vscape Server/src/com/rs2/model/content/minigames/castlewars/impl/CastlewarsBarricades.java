package com.rs2.model.content.minigames.castlewars.impl;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.effect.impl.BurnEffect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class CastlewarsBarricades {
	 
    private final static int MAX_BARRICADES = 10;
    private static ArrayList<Npc> barricadesZammy = new ArrayList<Npc>(MAX_BARRICADES);
    private static ArrayList<Npc> barricadesSara = new ArrayList<Npc>(MAX_BARRICADES);
    private final static MinigameAreas.Area[] badArea = {
    	Castlewars.ZAMMY_SPAWN_ROOM,
    	Castlewars.SARA_SPAWN_ROOM,
    	new MinigameAreas.Area(new Position(2371, 3118, 0), new Position(2374, 3121, 0)), // zammy main door
    	new MinigameAreas.Area(new Position(2425, 3087, 0), new Position(2428, 3089, 0)), // sara main door
    	CastlewarsRocks.ZAMMY_ROCK_TOP,
    	CastlewarsRocks.ZAMMY_ROCK_SIDE,
    	CastlewarsRocks.SARA_ROCK_BOTTOM,
    	CastlewarsRocks.SARA_ROCK_SIDE
    };
    
    private final static Position[] badPos = {
    	new Position(2417, 3077, 0), //sara
    	new Position(2416, 3074, 0),
    	new Position(2414, 3073, 0),
    	new Position(2421, 3074, 0),
    	new Position(2430, 3081, 0),
    	new Position(2430, 9481, 0),
    	new Position(2399, 9500, 0),
    	new Position(2399, 3100, 0),
    	new Position(2420, 3080, 1),
    	new Position(2421, 3074, 1),
    	new Position(2422, 3076, 1),
    	new Position(2426, 3081, 1),
    	new Position(2427, 3081, 1),
    	new Position(2430, 3080, 2),
    	new Position(2425, 3077, 2),
    	new Position(2426, 3074, 3),
    	new Position(2400, 3107, 0),//zammy
    	new Position(2400, 9507, 0),
    	new Position(2369, 9526, 0),
    	new Position(2369, 3126, 0),
    	new Position(2378, 3133, 0),
    	new Position(2385, 3134, 0),
    	new Position(2382, 3130, 0),
    	new Position(2383, 3133, 0),
    	new Position(2380, 3130, 0),
    	new Position(2378, 3133, 1),
    	new Position(2377, 3131, 1),
    	new Position(2379, 3127, 1),
    	new Position(2373, 3126, 1),
    	new Position(2372, 3126, 1),
    	new Position(2369, 3127, 2),
    	new Position(2374, 3130, 2),
    	new Position(2373, 3133, 3),
    };
    
    private static boolean barricadeBadPos(final Player player)
    {
    	Position pos = player.getPosition();
    	for(MinigameAreas.Area area : badArea){
	    	if(MinigameAreas.isInArea(pos, area))
	    	{
	    		return true;
	    	}
    	}
    	for(Position position : badPos){
	    	if(pos.getX() == position.getX() && pos.getY() == position.getY() && pos.getZ() == position.getZ())
	    	{
	    		return true;
	    	}
    	}
    	return false;
    }
    
    public static boolean HandleItemOnBarricades(final Player player, int item, Npc npc)
    {
		if(isBarricade(npc)){
			if(!Castlewars.isInGame(player))
			{
				return false;
			}
			final int x = npc.getPosition().getX();
			final int y = npc.getPosition().getY();
			final int z = npc.getPosition().getZ();
			switch(item)
			{
				case 590 :
					if(SetBarricadeFire(player, npc, x, y, z))
					{
						return true;
					}
				return false;
				case 1929 :
					if(ExtinguishBarricadeFire(player, npc, x, y, z))
					{
						return true;
					}
				return false;
				case 4045 :
					if(ExplodeBarricade(player, npc, x, y, z))
					{
						return true;
					}
				return false;
			}
		}
		return false;
    }
    
    
    private static Npc spawnBarricade(int id, Position pos)
    {
    	new GameObject(4421, pos.getX(), pos.getY(), pos.getZ(), 0, 10, Constants.EMPTY_OBJECT, 99999);
    	Npc npc = new Npc(1532);
	    npc.setPosition(pos);
	    npc.setSpawnPosition(pos);
	    npc.setCurrentX(pos.getX());
	    npc.setCurrentY(pos.getY());
	    World.register(npc);
	    return npc;
    }
    
    public static void destroyBarricade(Npc npc){
    	try{
			GameObject barricade = ObjectHandler.getInstance().getObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
			if(barricade != null){
				ObjectHandler.getInstance().removeObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
			}
			GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
			if(empty != null){
				ObjectHandler.getInstance().removeObject(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
			}
			if(barricadesZammy.contains(npc))
			{
				barricadesZammy.remove(npc);
			}
			if(barricadesSara.contains(npc))
			{
				barricadesSara.remove(npc);
			}
    	}catch(Exception ex) { System.out.println("Problem destroying Castlewars barricade"); }
    }
    
    private static boolean isBarricade(Npc npc){
    	if(npc.inCwGame()){
			if(barricadesZammy.contains(npc))
			{
				return true;
			}
			if(barricadesSara.contains(npc))
			{
				return true;
			}
    	}
    	return false;
    }
    
    public static void PlaceBarricade(final Player player, int item, int slot){
		if(!player.inCwGame() || !Castlewars.isInGame(player))
		{
			return;
		}
		try{
			if(barricadeBadPos(player))
			{
				player.getActionSender().sendMessage("You cannot place a barricade here!");
				return;
			}
			final int x = player.getPosition().getX();
			final int y = player.getPosition().getY();
			final int z = player.getPosition().getZ();
			if(player.getCastlewarsTeam() == Castlewars.ZAMORAK){
				if(barricadesZammy.size() >= MAX_BARRICADES)
				{
					player.getActionSender().sendMessage("Your team has hit the barricade limit.");
					return;
				}
				GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, x, y, z);
				if(empty != null){
					ObjectHandler.getInstance().removeObject(x, y, z, 10);
				}
				GameObjectDef objectHere = SkillHandler.getObject(x, y, z);
				if(objectHere != null && objectHere.getType() != 22){
					player.getActionSender().sendMessage("You cannot place a barricade here!");
					return;
				}
				player.getInventory().removeItemSlot(new Item(4053,1), slot);
				barricadesZammy.add(spawnBarricade(1532,new Position(x,y,z)));
				return;
			}
			if(player.getCastlewarsTeam() == Castlewars.SARADOMIN){
				if(barricadesSara.size() >= MAX_BARRICADES)
				{
					player.getActionSender().sendMessage("Your team has hit the barricade limit.");
					return;
				}
				GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, x, y, z);
				if(empty != null){
					ObjectHandler.getInstance().removeObject(x, y, z, 10);
				}
				GameObjectDef objectHere = SkillHandler.getObject(x, y, z);
				if(objectHere != null && objectHere.getType() != 22){
					player.getActionSender().sendMessage("You cannot place a barricade here!");
					return;
				}
				player.getInventory().removeItemSlot(new Item(4053,1), slot);
				barricadesSara.add(spawnBarricade(1532,new Position(x,y,z)));
				return;
			}
		}catch(Exception ex) { System.out.println("Problem placing Castlewars barricade"); }
    }
    
    public static boolean SetBarricadeFire(final Player player, final Npc npc, final int x, final int y, final int z){
		try{
			if(isBarricade(npc)){
				if(npc.getNpcId() == 1533)
				{
					player.getActionSender().sendMessage("This barricade is already on fire.");
					return true;
				}
				if(npc.getNpcId() == 1532)
				{
					int time = 3 + Misc.random(6);
					player.getActionSender().sendMessage("You attempt to light the barricade on fire.");
					player.getUpdateFlags().sendAnimation(733);
					player.getActionSender().sendSound(375, 0, 0);
					final int task = player.getTask();
			        player.setSkilling(new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (!player.checkTask(task) || npc.getNpcId() == 1533) {
								container.stop();
								return;
							}
							if (Misc.random(2) == 0) {
								player.getActionSender().sendMessage("The fire catches and the barricade begins to burn.");
								npc.sendTransform(1533, 99999999);
								HitDef hitDef = new HitDef(null, HitType.BURN, Math.ceil(4.0)).setStartingHitDelay(-1).setUnblockable(true).setDoBlock(false);
								Hit hit = new Hit(npc, npc, hitDef);
								BurnEffect burn = new BurnEffect(5, 5);
								burn.initialize(hit);
								container.stop();
								return;
							}else{
								player.getActionSender().sendMessage("You fail to set the barricade on fire.");
								container.stop();
							}
						}
						@Override
						public void stop() {
							player.resetAnimation();
						}
					});
			        CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), time);
					return true;
				}
			}
		}catch(Exception ex) { System.out.println("Problem setting Castlewars barricade fire"); }
		return false;
    }
    
    public static boolean ExtinguishBarricadeFire(final Player player, final Npc npc, final int x, final int y, final int z){
		try{
			if(isBarricade(npc)){
				if(npc.getNpcId() == 1533)
				{
					if(!player.getInventory().removeItemSlot(new Item(1929,1), player.getSlot())){
						player.getInventory().removeItem(new Item(1929,1));
						player.getInventory().addItemOrDrop(new Item(1925,1));
					}else{
						player.getInventory().addItemToSlot(new Item(1925,1), player.getSlot());
					}
					npc.removeAllEffects();
					npc.sendTransform(1532, 99999999);
					player.getActionSender().sendMessage("You extinguish the fire.");
					return true;
				}
			}
		}catch(Exception ex) { System.out.println("Problem setting Castlewars barricade fire"); }
		return false;
    }
    
    private static boolean ExplodeBarricade(final Player player, final Npc npc, final int x, final int y, final int z){
		try{
			if(isBarricade(npc)){
				if(!player.getInventory().removeItemSlot(new Item(4045,1), player.getSlot())){
					player.getInventory().removeItem(new Item(4045,1));
				}
				npc.hit((npc.getMaxHp() / 2), HitType.BURN);
		    	player.getActionSender().sendSoundRadius(97, 0, 0, npc.getPosition(), 5);
		    	npc.getUpdateFlags().sendHighGraphic(346);
				player.getActionSender().sendMessage("You throw the potion at the barricade and it explodes.");
				return true;
			}
		}catch(Exception ex) { System.out.println("Problem Exploding Castlewars barricade"); }
		return false;
    }
    
    public static void RemoveBarricades()
    {
    	try{
	    	for(Npc npc : new ArrayList<Npc>(barricadesZammy))
	    	{
	    		if(npc != null)
	    		{
	    			GameObject barricade = ObjectHandler.getInstance().getObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
	    			if(barricade != null){
	    				ObjectHandler.getInstance().removeObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
	    			}
	    			GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
	    			if(empty != null){
	    				ObjectHandler.getInstance().removeObject(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
	    			}
	    			npc.removeAllEffects();
	    			npc.setVisible(false);
	    			Following.resetFollow(npc);
	    			World.unregister(npc);
	    			barricadesZammy.remove(npc);
	    		}
	    	}
	    	for(Npc npc : new ArrayList<Npc>(barricadesSara))
	    	{
	    		if(npc != null)
	    		{
	    			GameObject barricade = ObjectHandler.getInstance().getObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
	    			if(barricade != null){
	    				ObjectHandler.getInstance().removeObject(4421, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
	    			}
	    			GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ());
	    			if(empty != null){
	    				ObjectHandler.getInstance().removeObject(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 10);
	    			}
	    			npc.removeAllEffects();
	    			npc.setVisible(false);
	    			Following.resetFollow(npc);
	    			World.unregister(npc);
	    			barricadesSara.remove(npc);
	    		}
	    	}
	    	barricadesZammy.clear();
	    	barricadesSara.clear();
    	}catch(Exception ex) { System.out.println("Problem resetting Castlewars barricades"); }
    }
}
