package com.rs2.model.content.minigames.castlewars.impl;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.content.minigames.castlewars.Castlewars;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;

public class CastlewarsRocks {
	
	
	private static final MinigameAreas.Area ZAMMY_CAVE_TOP = new MinigameAreas.Area(new Position(2399, 9511, 0), new Position(2402, 9514, 0));
	private static final MinigameAreas.Area ZAMMY_CAVE_SIDE = new MinigameAreas.Area(new Position(2390, 9500, 0), new Position(2393, 9503, 0));
	private static final MinigameAreas.Area SARA_CAVE_BOTTOM = new MinigameAreas.Area(new Position(2400, 9493, 0), new Position(2403, 9496, 0));
	private static final MinigameAreas.Area SARA_CAVE_SIDE = new MinigameAreas.Area(new Position(2408, 9502, 0), new Position(2411, 9505, 0));
	
	public static final MinigameAreas.Area ZAMMY_ROCK_TOP = new MinigameAreas.Area(new Position(2400, 9512, 0), new Position(2401, 9513, 0));
	public static final MinigameAreas.Area ZAMMY_ROCK_SIDE = new MinigameAreas.Area(new Position(2391, 9501, 0), new Position(2392, 9502, 0));
	public static final MinigameAreas.Area SARA_ROCK_BOTTOM = new MinigameAreas.Area(new Position(2401, 9494, 0), new Position(2402, 9495, 0));
	public static final MinigameAreas.Area SARA_ROCK_SIDE = new MinigameAreas.Area(new Position(2409, 9503, 0), new Position(2410, 9504, 0));

	public enum CaveWallData {
		ZAMMY_TOP(0, ZAMMY_CAVE_TOP, new Position(2400,9512,0), ZAMMY_ROCK_TOP),
		ZAMMY_SIDE(1, ZAMMY_CAVE_SIDE, new Position(2391,9501,0), ZAMMY_ROCK_SIDE),
		SARA_BOTTOM(2, SARA_CAVE_BOTTOM, new Position(2401,9494,0), SARA_ROCK_BOTTOM),
		SARA_SIDE(3, SARA_CAVE_SIDE, new Position(2409,9503,0), SARA_ROCK_SIDE);
		
		public int index;
		private MinigameAreas.Area area;
		public Position rockPosition;
		public MinigameAreas.Area rockArea;
		
		private CaveWallData(int index, MinigameAreas.Area area, Position rockPosition, MinigameAreas.Area rockArea)
		{
			this.index = index;
			this.area = area;
			this.rockPosition = rockPosition;
			this.rockArea = rockArea;
		}
		
		public static CaveWallData forIndex(int index) {
	    	for (CaveWallData caveWallData : CaveWallData.values()) {
				if (caveWallData.index == index) {
			    	return caveWallData;
				}
	    	}
	    	return null;
		}
		
		public static CaveWallData forWallPosition(Position pos) {
	    	for (CaveWallData caveWallData : CaveWallData.values()) {
				if (MinigameAreas.isInArea(pos, caveWallData.area)) {
			    	return caveWallData;
				}
	    	}
	    	return null;
		}
		
		public static CaveWallData forRockPosition(Position pos) {
	    	for (CaveWallData caveWallData : CaveWallData.values()) {
				if (caveWallData.rockPosition.getX() == pos.getX() && caveWallData.rockPosition.getY() == pos.getY() &&
						caveWallData.rockPosition.getZ() == pos.getZ()) {
			    	return caveWallData;
				}
	    	}
	    	return null;
		}
	}
	
	private static boolean[] caveCollapsed = {
		true, //zammy top
		true, //zammy side
		true, //sara bottom
		true //sara side
	};
	
	public static void setCollapsedCave(int id, boolean val)
	{
		caveCollapsed[id] = val;
	}
	
	public static boolean getCollapsedCave(int id)
	{
		return caveCollapsed[id];
	}
	
	public static void collapseRock(CaveWallData caveWall)
	{
		try{
			if(caveWall != null)
			{
				setCollapsedCave(caveWall.index, true);
				int face = SkillHandler.getFace(4437, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ());
				GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ());
				if(empty != null){
					ObjectHandler.getInstance().removeObject(caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ(), 10);
				}
				new GameObject(4437, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ(), face, 10, Constants.EMPTY_OBJECT, 99999);
			    for (Player player : new ArrayList<Player>(Castlewars.zammyGamePlayers)) {
					if(player == null) {
					    continue; 
					}
					if(MinigameAreas.isInArea(player.getPosition(), caveWall.rockArea))
					{
						player.hit(player.getCurrentHp(), HitType.NORMAL);
					}
			    }
			    for (Player player : new ArrayList<Player>(Castlewars.saraGamePlayers)) {
					if(player == null) {
					    continue; 
					}
					if(MinigameAreas.isInArea(player.getPosition(), caveWall.rockArea))
					{
						player.hit(player.getCurrentHp(), HitType.NORMAL);
					}
			    }
			}
		}catch(Exception ex) { System.out.println("Problem collapsing Castlewars rocks"); }
	}
	
	public static void removeCollapse(CaveWallData caveWall, int id, int x, int y, int z)
	{
		try{
			if(caveWall != null)
			{
				setCollapsedCave(caveWall.index, false);
				int face = SkillHandler.getFace(id, x, y, z);
				GameObject rock = ObjectHandler.getInstance().getObject(4437, x, y, z);
				if(rock != null){
					ObjectHandler.getInstance().removeObject(x, y, z, 10);
				}
				new GameObject(Constants.EMPTY_OBJECT, x, y, z, face, 10, id, 99999, false);
				ObjectHandler.getInstance().removeClip(id, x, y, z, 10, face);
			}
		}catch(Exception ex) { System.out.println("Problem removing Castlewars rocks collapse"); }
	}
	
	public static void ResetAllRocks(){
		try{
			for(int i = 0; i < 4; i++)
			{
				if(!getCollapsedCave(i)){
					CaveWallData caveWall = CaveWallData.forIndex(i);
					if(caveWall != null)
					{
						setCollapsedCave(caveWall.index, true);
						GameObject empty = ObjectHandler.getInstance().getObject(Constants.EMPTY_OBJECT, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ());
						if(empty != null){
							ObjectHandler.getInstance().removeObject(caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ(), 10);
						}
						GameObject rocks = ObjectHandler.getInstance().getObject(4437, caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ());
						if(rocks != null){
							ObjectHandler.getInstance().removeObject(caveWall.rockPosition.getX(), caveWall.rockPosition.getY(), caveWall.rockPosition.getZ(), 10);
						}
					}
				}
			}
		}catch(Exception ex) { System.out.println("Problem resetting Castlewars rocks"); }
	}
}
