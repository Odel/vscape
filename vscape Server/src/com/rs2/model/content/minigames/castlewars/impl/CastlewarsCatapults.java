package com.rs2.model.content.minigames.castlewars.impl;

import com.rs2.model.Position;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.players.Player;
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
	
	public static void ResetCatapults(){
		try{
		}catch(Exception ex) { System.out.println("Problem resetting Castlewars catapults"); }
	}
}
