package com.rs2.model.content.minigames.castlewars.impl;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class CastlewarsBattlements {
	   
    private final static int CLIMBING_ROPE_ITEM = 4047;
    private final static int CLIMBING_ROPE_OBJECT = 4444; //type 4 seems best
    
    private static ArrayList<GameObject> climbingRopes = new ArrayList<GameObject>();
    
    public static boolean HandleItemOnBattlement(final Player player, int objectId, int x, int y, int z){
    	if(!Castlewars.isInGame(player))
		{
			return false;
		}
    	if(PlaceClimbingRope(player, objectId, x, y, z))
    	{
    		return true;
    	}
    	return false;
    }
    
    private static boolean canPlaceRope(final Player player){
    	for(GameObject rope : new ArrayList<GameObject>(climbingRopes))
    	{
    		if(rope == null)
    			continue;
    		GameObjectDef def = rope.getDef();
    		if(def.getPosition().getX() == player.getPosition().getX() && def.getPosition().getY() == player.getPosition().getY()
    				&& def.getPosition().getZ() == player.getPosition().getZ())
    		{
    			return false;
    		}
    	}
    	return true;
    }
    
    private static boolean PlaceClimbingRope(final Player player, int objectId, int x, int y, int z){
    	try{
			switch(player.getCastlewarsTeam())
			{
				case Castlewars.ZAMORAK :
					if(objectId == 4447)
					{
						player.getActionSender().sendMessage("You Cannot place a climbing rope on your own battlements!");
						return true;
					}
				break;
				case Castlewars.SARADOMIN :
					if(objectId == 4446)
					{
						player.getActionSender().sendMessage("You Cannot place a climbing rope on your own battlements!");
						return true;
					}
				break;
			}
	    	if(!canPlaceRope(player))
	    	{
	    		player.getActionSender().sendMessage("There is already a climbing rope here!");
	    		return true;
	    	}
			GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			if(empty != null){
				ObjectHandler.getInstance().removeObject(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), 10);
			}
			GameObjectDef objectHere = SkillHandler.getObject(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			if(objectHere != null && objectHere.getType() != 22){
				player.getActionSender().sendMessage("You cannot place a climbing rope here!");
				return true;
			}
			final CacheObject bobj = ObjectLoader.object(x, y, z);
			if (bobj != null) {
				int face = bobj.getRotation();
				int ropeFace = face + 2;
				switch(objectId)
				{
					case 4446 :
						ropeFace = face + 2;
					break;
					case 4447 :
						ropeFace = face - 2;
					break;
				}
				if(!player.getInventory().removeItemSlot(new Item(CLIMBING_ROPE_ITEM,1), player.getSlot())){
					player.getInventory().removeItem(new Item(CLIMBING_ROPE_ITEM,1));
				}
				player.getActionSender().sendMessage("You manage to throw the climbing rope over the battlement wall.");
				climbingRopes.add(new GameObject(CLIMBING_ROPE_OBJECT, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), ropeFace, 4, Constants.EMPTY_OBJECT, 999999));
				return true;
			}
    	}catch(Exception ex) { System.out.println("Problem placing Castlewars rope"); }
    	return false;
    }
    
    public static void RemoveClimbingRopes()
    {
    	try{
	    	for(GameObject obj : new ArrayList<GameObject>(climbingRopes))
	    	{
	    		if(obj != null)
	    		{
	    			GameObjectDef def = obj.getDef();
					ObjectHandler.getInstance().removeObject(def.getId(), def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ(), 4);
	    		}
	    	}
	    	climbingRopes.clear();
    	}catch(Exception ex) { System.out.println("Problem resetting Castlewars ropes"); }
    }
}
