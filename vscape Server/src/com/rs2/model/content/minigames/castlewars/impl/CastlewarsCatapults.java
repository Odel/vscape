package com.rs2.model.content.minigames.castlewars.impl;

import java.util.ArrayList;

import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class CastlewarsCatapults {

	public enum CatapultData {
		ZAMMY(0, new Position(2384,3117,0), 4381, 4904, 4385, 1),
		SARA(1, new Position(2413,3088,0), 4382, 4905, 4386, 3);
		
		public int teamIndex;
		public Position position;
		public int normalId;
		public int burningId;
		public int brokenId;
		public int face;
		
		private CatapultData(int teamIndex, Position position, int normalId, int burningId, int brokenId, int face)
		{
			this.teamIndex = teamIndex;
			this.position = position;
			this.normalId = normalId;
			this.burningId = burningId;
			this.brokenId = brokenId;
			this.face = face;
		}
		
		public static CatapultData forTeam(int team) {
			for (CatapultData catapultData : CatapultData.values()) {
				if (catapultData.teamIndex == team) {
			    	return catapultData;
				}
	    	}
	    	return null;
		}
		
		public static CatapultData forCatapultId(int catapultId) {
			for (CatapultData catapultData : CatapultData.values()) {
				if (catapultData.normalId == catapultId) {
			    	return catapultData;
				}
	    	}
	    	return null;
		}
		
		public static CatapultData forPosition(Position pos) {
			for (CatapultData catapultData : CatapultData.values()) {
				if (catapultData.position.getX() == pos.getX() && catapultData.position.getY() == pos.getY() &&
						catapultData.position.getZ() == pos.getZ()) {
			    	return catapultData;
				}
	    	}
	    	return null;
		}
	}
	private static long[] lastFireTime = {
		10000, //zammy
		10000 //sara
	};
    private static void setLastFireTime(int team, long val)
    {
    	lastFireTime[team] = val;
    }
    
	private static boolean[] onFire = {
		false, //zammy
		false //sara
	};
    private static void setOnFire(int team, boolean val)
    {
    	onFire[team] = val;
    }
    public static boolean getOnFire(int team)
    {
    	return onFire[team];
    }
	private static boolean[] destroyed = {
		false, //zammy
		false //sara
	};
    private static void setDestroyed(int team, boolean val)
    {
    	destroyed[team] = val;
    }
    public static boolean getDestroyed(int team)
    {
    	return destroyed[team];
    }
	
	public static boolean OperateCatapult(final Player player, int catapultId, int x, int y, int z){
		if(!Castlewars.isInGame(player))
		{
			return false;
		}
		if(!player.getInventory().playerHasItem(4043))
		{
			player.getActionSender().sendMessage("You don't have any ammo to operate this catapult.");
			return true;
		}
		switch(catapultId)
		{
			case 4381 : //zammy
                if (player.getCastlewarsTeam() == 1) {
                    player.getActionSender().sendMessage("You cannot operate this teams catapult.");
                    return true;
                }
            	CatapultInterface.openInterface(player, Castlewars.ZAMORAK);
        	return true;
			case 4382 : //sara
                if (player.getCastlewarsTeam() == 0) {
                    player.getActionSender().sendMessage("You cannot operate this teams catapult.");
                    return true;
                }                	
            	CatapultInterface.openInterface(player, Castlewars.SARADOMIN);
        	return true;
		}
		return false;
	}
	
	public static boolean canFire(int team){
		return (System.currentTimeMillis() - lastFireTime[team]) >= 10000;
	}
	
	public static void fireCatapult(final Player player, final int team, final int x, final int y)
	{
		if(getOnFire(team))
		{
			player.getActionSender().sendMessage("This catapult is on fire and cannot be fired.");
			return;
		}
		if(getDestroyed(team))
		{
			player.getActionSender().sendMessage("This catapult has been destroyed and cannot fire.");
			return;
		}
		if(!player.getInventory().playerHasItem(4043))
		{
			player.getActionSender().sendMessage("You don't have any ammo to operate this catapult.");
			return;
		}
		if(!canFire(team))
		{
	        long seconds = (((((System.currentTimeMillis() - lastFireTime[team])) * -1 + 10000) / 1000)+1);  
	        player.getActionSender().sendMessage("This catapult will be ready to fire in " + seconds + " seconds!");
	        return;
		}
		try{
			final CatapultData cd = CatapultData.forTeam(team);
			if(cd != null)
			{
				int directionX = (team == 0 ? 1 : -1);
				int directionY = (team == 0 ? -1 : 1);
				int baseX = (cd.position.getX() + (team == 0 ? 4 : -2));
				int baseY = (cd.position.getY() + (team == 0 ? -2 : 4));
				int fireX = baseX + (((int)Math.floor(x/1.75)) * directionX);
				int fireY = baseY + (((int)Math.floor(y/1.75)) * directionY);
				final Position firePosition = new Position(fireX, fireY, 0);
				player.getInventory().removeItem(new Item(4043,1));
				setLastFireTime(team, System.currentTimeMillis());
		        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		            @Override
		            public void execute(CycleEventContainer container) {
		    			World.createStaticGraphic(Graphic.lowGraphic(287), firePosition);
		    			int damage = Misc.random(2, 10);
		    		    for (Player playerZ : new ArrayList<Player>(Castlewars.zammyGamePlayers)) {
		    				if(playerZ == null) {
		    				    continue; 
		    				}
		    				if(playerZ.isDead()) {
		    				    continue; 
		    				}
		                	if(Misc.getDistance(firePosition, playerZ.getPosition()) <= 1){
		                		playerZ.hit(damage, HitType.NORMAL);
		                	}
		    		    }
		    		    for (Player playerS : new ArrayList<Player>(Castlewars.saraGamePlayers)) {
		    				if(playerS == null) {
		    				    continue; 
		    				}
		    				if(playerS.isDead()) {
		    				    continue; 
		    				}
		                	if(Misc.getDistance(firePosition, playerS.getPosition()) <= 1){
		                		playerS.hit(damage, HitType.NORMAL);
		                	}
		    		    }
		                container.stop();
		            }
		            @Override
		            public void stop(){
		            }
		        }, 2);
			}
		}catch(Exception ex) { System.out.println("Problem firing Castlewars catapult"); }
	}
	
	public static boolean handleItemOnCatapult(final Player player, final int item, final int object, final int x, final int y, final int z)
	{
		if(!Castlewars.isInGame(player))
		{
			return false;
		}
		switch(object)
		{
			case 4381 :
				if(item == 590){
	                if (player.getCastlewarsTeam() == Castlewars.ZAMORAK) {
	                    player.getActionSender().sendMessage("You cannot set fire to your own catapult.");
	                    return true;
	                }
	                if(SetCatapultOnFire(player, Castlewars.ZAMORAK))
	                {
	                	return true;
	                }
				}
			return false;
			case 4382 :
				if(item == 590){
	                if (player.getCastlewarsTeam() == Castlewars.SARADOMIN) {
	                    player.getActionSender().sendMessage("You cannot set fire to your own catapult.");
	                    return true;
	                }
	                if(SetCatapultOnFire(player, Castlewars.SARADOMIN))
	                {
	                	return true;
	                }
				}
			return false;
		}
		return false;
	}
	
	private static boolean SetCatapultOnFire(final Player player, final int team){
		if(getOnFire(team) || getDestroyed(team))
		{
			return false;
		}
		try{
			final CatapultData cd = CatapultData.forTeam(team);
			if(cd != null) {
				int time = 3 + Misc.random(6);
				player.getActionSender().sendMessage("You attempt to light the catapult on fire.");
				player.getUpdateFlags().sendAnimation(733);
				player.getActionSender().sendSound(375, 0, 0);
				final int task = player.getTask();
		        player.setSkilling(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (!player.checkTask(task) || getOnFire(team) || getDestroyed(team)) {
							container.stop();
							return;
						}
						if (Misc.random(2) == 0) {
							player.getActionSender().sendMessage("The fire catches and the catapult begins to burn.");
			    			GameObject normalObj = ObjectHandler.getInstance().getObject(cd.normalId, cd.position.getX(), cd.position.getY(), cd.position.getZ());
			    			if(normalObj != null){
			    				ObjectHandler.getInstance().removeObject(cd.normalId, cd.position.getX(), cd.position.getY(), cd.position.getZ(), 11);
			    			}
			    	    	new GameObject(cd.burningId, cd.position.getX(), cd.position.getY(), cd.position.getZ(), cd.face, 11, cd.burningId, 9999999);
			    	    	setOnFire(team, true);
			    	    	World.submit(new Tick(33) {
			    			    @Override 
			    			    public void execute() {
			    			    	DestroyCatapult(team);
			    			    	this.stop();
			    			    	return;
			    			    }
			    			});
							container.stop();
							return;
						}else{
							player.getActionSender().sendMessage("You fail to set the catapult on fire.");
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
		}catch(Exception ex) { System.out.println("Problem setting Castlewars catapult fire"); }
		return false;
	}
	
	private static void DestroyCatapult(final int team){
		if(getDestroyed(team))
		{
			return;
		}
		try{
	    	setOnFire(team, false);
	    	setDestroyed(team, true);
			final CatapultData cd = CatapultData.forTeam(team);
			if(cd != null) {
				GameObject burningObj = ObjectHandler.getInstance().getObject(cd.burningId, cd.position.getX(), cd.position.getY(), cd.position.getZ());
				if(burningObj != null){
					ObjectHandler.getInstance().removeObject(cd.burningId, cd.position.getX(), cd.position.getY(), cd.position.getZ(), 11);
				}
    	    	new GameObject(cd.brokenId, cd.position.getX(), cd.position.getY(), cd.position.getZ(), cd.face, 11, cd.brokenId, 9999999);
			}
		}catch(Exception ex) { System.out.println("Problem destroying Castlewars catapult"); }
	}
	
	public static boolean HandleRepair(final Player player, final int object){
		if(!Castlewars.isInGame(player))
		{
			return false;
		}
		switch(object)
		{
			case 4385 :
                if (player.getCastlewarsTeam() == Castlewars.SARADOMIN) {
                    player.getActionSender().sendMessage("You cannot repair the enemies catapult!");
                    return true;
                }
                if(RepairCatapult(player, Castlewars.ZAMORAK))
                {
                	return true;
                }
			return false;
			case 4386 :
                if (player.getCastlewarsTeam() == Castlewars.ZAMORAK) {
                    player.getActionSender().sendMessage("You cannot repair the enemies catapult!");
                    return true;
                }
                if(RepairCatapult(player, Castlewars.SARADOMIN))
                {
                	return true;
                }
			return false;
		}
		return false;
	}
	
	private static boolean RepairCatapult(final Player player, final int team){
		if(!getDestroyed(team))
		{
			return false;
		}
		if(!player.getInventory().playerHasItem(4051))
		{
            player.getActionSender().sendMessage("You don't have a toolkit to repair the catapult.");
            return true;
		}
		try{
			final CatapultData cd = CatapultData.forTeam(team);
			if(cd != null) {
				int time = 2 + Misc.random(2);
				player.getActionSender().sendMessage("You attempt to repair the catapult.");
				player.getUpdateFlags().sendAnimation(733);
				final int task = player.getTask();
		        player.setSkilling(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (!player.checkTask(task) || !getDestroyed(team)) {
							container.stop();
							return;
						}
						if (Misc.random(2) == 0) {
							player.getActionSender().sendMessage("You manage to repair the catapult.");
							player.getInventory().removeItem(new Item(4051,1));
							GameObject destoyedObj = ObjectHandler.getInstance().getObject(cd.brokenId, cd.position.getX(), cd.position.getY(), cd.position.getZ());
							if(destoyedObj != null){
								ObjectHandler.getInstance().removeObject(cd.brokenId, cd.position.getX(), cd.position.getY(), cd.position.getZ(), 11);
							}
					    	new GameObject(cd.normalId, cd.position.getX(), cd.position.getY(), cd.position.getZ(), cd.face, 11, cd.normalId, 9999999);
			    	    	setDestroyed(team, false);
							container.stop();
							return;
						}else{
							player.getActionSender().sendMessage("You fail to repair the catapult.");
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
		}catch(Exception ex) { System.out.println("Problem repairing Castlewars catapult"); }
		return false;
	}
	
	public static void ResetCatapults(){
		try{
			setLastFireTime(Castlewars.ZAMORAK, 10000);
			setLastFireTime(Castlewars.SARADOMIN, 10000);
			setOnFire(Castlewars.ZAMORAK, false);
			setOnFire(Castlewars.SARADOMIN, false);
			setDestroyed(Castlewars.ZAMORAK, false);
			setDestroyed(Castlewars.SARADOMIN, false);
			final CatapultData cdZ = CatapultData.forTeam(Castlewars.ZAMORAK);
			GameObject normalObj = ObjectHandler.getInstance().getObject(cdZ.normalId, cdZ.position.getX(), cdZ.position.getY(), cdZ.position.getZ());
			if(normalObj != null){
				ObjectHandler.getInstance().removeObject(cdZ.normalId, cdZ.position.getX(), cdZ.position.getY(), cdZ.position.getZ(), 11);
			}
			GameObject burningObj = ObjectHandler.getInstance().getObject(cdZ.burningId, cdZ.position.getX(), cdZ.position.getY(), cdZ.position.getZ());
			if(burningObj != null){
				ObjectHandler.getInstance().removeObject(cdZ.burningId, cdZ.position.getX(), cdZ.position.getY(), cdZ.position.getZ(), 11);
			}
			GameObject destoyedObj = ObjectHandler.getInstance().getObject(cdZ.brokenId, cdZ.position.getX(), cdZ.position.getY(), cdZ.position.getZ());
			if(destoyedObj != null){
				ObjectHandler.getInstance().removeObject(cdZ.brokenId, cdZ.position.getX(), cdZ.position.getY(), cdZ.position.getZ(), 11);
			}
			final CatapultData cdS = CatapultData.forTeam(Castlewars.SARADOMIN);
			GameObject normalObjS = ObjectHandler.getInstance().getObject(cdS.normalId, cdS.position.getX(), cdS.position.getY(), cdS.position.getZ());
			if(normalObjS != null){
				ObjectHandler.getInstance().removeObject(cdS.normalId, cdS.position.getX(), cdS.position.getY(), cdS.position.getZ(), 11);
			}
			GameObject burningObjS = ObjectHandler.getInstance().getObject(cdS.burningId, cdS.position.getX(), cdS.position.getY(), cdS.position.getZ());
			if(burningObjS != null){
				ObjectHandler.getInstance().removeObject(cdS.burningId, cdS.position.getX(), cdS.position.getY(), cdS.position.getZ(), 11);
			}
			GameObject destoyedObjS = ObjectHandler.getInstance().getObject(cdS.brokenId, cdS.position.getX(), cdS.position.getY(), cdS.position.getZ());
			if(destoyedObjS != null){
				ObjectHandler.getInstance().removeObject(cdS.brokenId, cdS.position.getX(), cdS.position.getY(), cdS.position.getZ(), 11);
			}
		}catch(Exception ex) { System.out.println("Problem resetting Castlewars catapults"); }
	}
}
