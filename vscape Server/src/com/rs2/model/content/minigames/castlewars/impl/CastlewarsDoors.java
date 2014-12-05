package com.rs2.model.content.minigames.castlewars.impl;

import com.rs2.model.Position;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class CastlewarsDoors {
    
	public enum SideDoorData {
		ZAMMY(0, new Position(2384,3134,0), 4467, 4468, 2),
		SARA(1, new Position(2415,3073,0), 4465, 4466, 0);
		
		public int teamIndex;
		public Position position;
		public int lockedId;
		public int unlockedId;
		public int face;
		
		private SideDoorData(int teamIndex, Position position, int lockedId, int unlockedId, int face)
		{
			this.teamIndex = teamIndex;
			this.position = position;
			this.lockedId = lockedId;
			this.unlockedId = unlockedId;
			this.face = face;
		}
		
		public static SideDoorData forTeam(int team) {
	    	for (SideDoorData sideDoorData : SideDoorData.values()) {
				if (sideDoorData.teamIndex == team) {
			    	return sideDoorData;
				}
	    	}
	    	return null;
		}
		
		public static SideDoorData forPosition(Position pos) {
			for (SideDoorData sideDoorData : SideDoorData.values()) {
				if (sideDoorData.position.getX() == pos.getX() && sideDoorData.position.getY() == pos.getY() &&
						sideDoorData.position.getZ() == pos.getZ()) {
			    	return sideDoorData;
				}
	    	}
	    	return null;
		}
	}
	private static boolean[] doorUnlocked = {
		false, //zammy
		false //sara
	};
    private static void setSideDoorUnlocked(int team, boolean val)
    {
    	doorUnlocked[team] = val;
    }
    public static boolean getSideDoorUnlocked(int team)
    {
    	return doorUnlocked[team];
    }
    
    public static boolean HandleDoors(final Player player, int objectId, int x, int y, int z)
    {
		if(!Castlewars.isInGame(player))
		{
			return false;
		}
		int playerTeam = player.getCastlewarsTeam();
		switch(objectId)
		{
			case 4465: //sara side door locked
				switch(playerTeam)
				{
					case Castlewars.ZAMORAK:
						UnlockDoor(player, Castlewars.SARADOMIN);
					return true;
					case Castlewars.SARADOMIN:
		            	player.getActionSender().walkTo(player.getPosition().getX() >= 2415 ? -1 : 1, 0, true);
		            	player.getActionSender().walkThroughDoor(objectId, x, y, z);
					return true;
				}
			return false;
			case 4466: //sara side door unlocked
				switch(playerTeam)
				{
					case Castlewars.SARADOMIN:
						LockDoor(player, Castlewars.SARADOMIN);
					return true;
				}
			return false;
			case 4467: //zammy side door locked
				switch(playerTeam)
				{
					case Castlewars.SARADOMIN:
						UnlockDoor(player, Castlewars.ZAMORAK);
					return true;
					case Castlewars.ZAMORAK:
		            	player.getActionSender().walkTo(player.getPosition().getX() <= 2384 ? 1 : -1, 0, true);
		            	player.getActionSender().walkThroughDoor(objectId, x, y, z);
					return true;
				}
			return false;
			case 4468: //zammy side door unlocked
				switch(playerTeam)
				{
					case Castlewars.ZAMORAK:
						LockDoor(player, Castlewars.ZAMORAK);
					return true;
				}
			return false;
		}
    	return false;
    }
    
    private static void LockDoor(final Player player, int team)
    {
		if(!Castlewars.isInGame(player))
		{
			return;
		}
		try{
			switch(team)
			{
				case Castlewars.ZAMORAK:
	            	if(!getSideDoorUnlocked(Castlewars.ZAMORAK)){
	            		return;
	            	}
					SideDoorData sideDoorDataz = SideDoorData.forTeam(Castlewars.ZAMORAK);
	                player.getActionSender().sendMessage("You lock the door.");
					if(ObjectHandler.getInstance().getObject(sideDoorDataz.unlockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ()) != null){
						ObjectHandler.getInstance().removeObject(sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), 0);
					}
					new GameObject(sideDoorDataz.lockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), sideDoorDataz.face, 0, sideDoorDataz.lockedId, 99999);
					setSideDoorUnlocked(Castlewars.ZAMORAK, false);
				return;
				case Castlewars.SARADOMIN:
	            	if(!getSideDoorUnlocked(Castlewars.SARADOMIN)){
	            		return;
	            	}
					SideDoorData sideDoorDataS = SideDoorData.forTeam(Castlewars.SARADOMIN);
	                player.getActionSender().sendMessage("You lock the door.");
					if(ObjectHandler.getInstance().getObject(sideDoorDataS.unlockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ()) != null){
						ObjectHandler.getInstance().removeObject(sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), 0);
					}
					new GameObject(sideDoorDataS.lockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), sideDoorDataS.face, 0, sideDoorDataS.lockedId, 99999);
					setSideDoorUnlocked(Castlewars.SARADOMIN, false);
				return;
			}
	    }catch(Exception ex) { System.out.println("Problem locking Castlewars side door"); }
    }
    
    private static void UnlockDoor(final Player player, int team)
    {
		if(!Castlewars.isInGame(player))
		{
			return;
		}
		try{
			if(team == Castlewars.ZAMORAK){
            	if(getSideDoorUnlocked(Castlewars.ZAMORAK)){
            		return;
            	}
    			int time = 3 + Misc.random(6);
    			player.getActionSender().sendMessage("You attempt to picklock the door.");
    			player.getUpdateFlags().sendAnimation(881);
    			final int task = player.getTask();
    	        player.setSkilling(new CycleEvent() {
    				@Override
    				public void execute(CycleEventContainer container) {
    					if (!player.checkTask(task) || getSideDoorUnlocked(Castlewars.ZAMORAK)) {
    						container.stop();
    						return;
    					}
    					if (Misc.random(2) == 0) {
    						SideDoorData sideDoorDataz = SideDoorData.forTeam(Castlewars.ZAMORAK);
    		                player.getActionSender().sendMessage("You manage to pick the door lock.");
    						if(ObjectHandler.getInstance().getObject(sideDoorDataz.lockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ()) != null){
    							ObjectHandler.getInstance().removeObject(sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), 0);
    						}
    						ObjectHandler.getInstance().removeClip(sideDoorDataz.lockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), 0, sideDoorDataz.face);
    						new GameObject(sideDoorDataz.unlockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), sideDoorDataz.face-1, 0, sideDoorDataz.unlockedId, 99999, false);
    						setSideDoorUnlocked(Castlewars.ZAMORAK, true);
    						container.stop();
    						return;
    					}else{
    						player.getActionSender().sendMessage("You fail to pick the lock.");
    						container.stop();
    					}
    				}
    				@Override
    				public void stop() {
    					player.resetAnimation();
    				}
    			});
    	        CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), time);
    	        return;
			}
			if(team == Castlewars.SARADOMIN){
            	if(getSideDoorUnlocked(Castlewars.SARADOMIN)){
            		return;
            	}
    			int time = 3 + Misc.random(6);
    			player.getActionSender().sendMessage("You attempt to picklock the door.");
    			player.getUpdateFlags().sendAnimation(881);
    			final int task = player.getTask();
    	        player.setSkilling(new CycleEvent() {
    				@Override
    				public void execute(CycleEventContainer container) {
    					if (!player.checkTask(task) || getSideDoorUnlocked(Castlewars.SARADOMIN)) {
    						container.stop();
    						return;
    					}
    					if (Misc.random(2) == 0) {
    						SideDoorData sideDoorDataS = SideDoorData.forTeam(Castlewars.SARADOMIN);
    		                player.getActionSender().sendMessage("You manage to pick the door lock.");
    						if(ObjectHandler.getInstance().getObject(sideDoorDataS.lockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ()) != null){
    							ObjectHandler.getInstance().removeObject(sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), 0);
    						}
    						ObjectHandler.getInstance().removeClip(sideDoorDataS.lockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), 0, sideDoorDataS.face);
    						new GameObject(sideDoorDataS.unlockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), sideDoorDataS.face-1, 0, sideDoorDataS.unlockedId, 99999, false);
    						setSideDoorUnlocked(Castlewars.SARADOMIN, true);
    						container.stop();
    						return;
    					}else{
    						player.getActionSender().sendMessage("You fail to pick the lock.");
    						container.stop();
    					}
    				}
    				@Override
    				public void stop() {
    					player.resetAnimation();
    				}
    			});
    	        CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), time);
    	        return;
			}
	    }catch(Exception ex) { System.out.println("Problem unlocking Castlewars side door"); }
    }
    
    private static void ResetSideDoors()
    {
	    try{
	    	if(getSideDoorUnlocked(Castlewars.ZAMORAK)){
	    		SideDoorData sideDoorDataz = SideDoorData.forTeam(Castlewars.ZAMORAK);
				if(ObjectHandler.getInstance().getObject(sideDoorDataz.unlockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ()) != null){
					ObjectHandler.getInstance().removeObject(sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), 0);
				}
				if(ObjectHandler.getInstance().getObject(sideDoorDataz.lockedId, sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ()) != null){
					ObjectHandler.getInstance().removeObject(sideDoorDataz.position.getX(), sideDoorDataz.position.getY(), sideDoorDataz.position.getZ(), 0);
				}
				setSideDoorUnlocked(Castlewars.ZAMORAK, false);
	    	}
	    	if(getSideDoorUnlocked(Castlewars.SARADOMIN)){
	    		SideDoorData sideDoorDataS = SideDoorData.forTeam(Castlewars.SARADOMIN);
				if(ObjectHandler.getInstance().getObject(sideDoorDataS.unlockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ()) != null){
					ObjectHandler.getInstance().removeObject(sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), 0);
				}
				if(ObjectHandler.getInstance().getObject(sideDoorDataS.lockedId, sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ()) != null){
					ObjectHandler.getInstance().removeObject(sideDoorDataS.position.getX(), sideDoorDataS.position.getY(), sideDoorDataS.position.getZ(), 0);
				}
				setSideDoorUnlocked(Castlewars.SARADOMIN, false);
	    	}
	    }catch(Exception ex) { System.out.println("Problem resetting Castlewars side doors"); }
    }
    //main door
    
    public static void ResetDoors()
    {
    	ResetSideDoors();
    	//ResetMainDoors();
    }
}
