package com.rs2.model.content.skills.Woodcutting;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import com.rs2.model.Position;

/*
 *  this is very basic canoe class and its not very good but it works for now
 *  bobster
 * 
 */
public class Canoe {
	
// Canoes 12147, 12148, 12149, 12150
	
	public enum CanoeTravelData
	{
		LUMBRIDGE(0, 71079, 3243, 3235),
		CHAMPGUILD(1, 71080, 3204, 3343),
		BARBVILL(2, 71081, 3112, 3409),
		EDGE(3, 71086, 3132, 3508),
		WILDY(4, 71082, 3142, 3797);
		
		private int stationIndex;
		private int destButtonId;
		private int destX;
		private int destY;
		
		CanoeTravelData(int stationIndex, int destButtonId, int x, int y) {
			this.stationIndex = stationIndex;
			this.destButtonId = destButtonId;
			this.destX = x;
			this.destY = y;
		}
		
		public static CanoeTravelData forId(int destButtonId) {
			for (CanoeTravelData canoeTravelData : CanoeTravelData.values())
					if (destButtonId == canoeTravelData.destButtonId)
						return canoeTravelData;
			return null;
		}
	}

	public enum CanoeStationData {
		LUMBRIDGE(0, 12163, 3241, 3235),
		CHAMPGUILD(1, 12164, 3200, 3341),
		BARBVILL(2, 12165, 3110, 3409),
		EDGE(3, 12166, 3130, 3508);
		
		private int stationIndex;
		private int stationID;
		private int x;
		private int y;
	
	 	CanoeStationData(int stationIndex, int stationID, int x, int y) {
	 		this.stationIndex = stationIndex;
			this.stationID = stationID;
			this.x = x;
			this.y = y;
		}
		
		public static CanoeStationData forId(int stationID) {
			for (CanoeStationData canoeStationData : CanoeStationData.values())
					if (stationID == canoeStationData.stationID)
						return canoeStationData;
			return null;
		}
	}
	
	public enum CanoeData {
		LOG("Log", 71028, 12147, 12, 30.0, false),
		DUGOUT("Dugout", 71029, 12148, 27, 60.0, false),
		STABLEDUGOUT("Stable Dugout", 71030, 12149, 42, 90.0, false),
		WAKA("Waka", 71031, 12150, 57, 150.0, true);
		
		private String name;
		private int buttonID;
		private int objectID;
		private int level;
		private double xp;
		private boolean canWilderness;
	
		CanoeData(String name, int buttonID, int objectID, int level, double xp, boolean canWilderness) {
			this.name = name;
			this.buttonID = buttonID;
			this.objectID = objectID;
			this.level = level;
			this.xp = xp;
			this.canWilderness = canWilderness;
		}
		
		public static CanoeData forButtonId(int buttonID) {
			for (CanoeData canoeData : CanoeData.values())
					if (buttonID == canoeData.buttonID)
						return canoeData;
			return null;
		}
	}

	private Player player;
	
	public Canoe(final Player player)
	{
		this.player = player;
	}
	
	public boolean travelCanoe(int buttonId)
	{
		CanoeTravelData canoeTravelData = CanoeTravelData.forId(buttonId);
		if(canoeTravelData == null)
		{
			return false;
		}
		if(canoeTravelData.destButtonId == buttonId)
		{
	        if (!player.getStatedInterface().equals("canoe")) {
	        	player.getActionSender().removeInterfaces();
	            return false;
	        }
	        if(canoeTravelData == CanoeTravelData.WILDY)
	        {
	        	if(!getCanoeType().canWilderness){
					player.getActionSender().sendMessage("This canoe cannot travel into the wilderness.");
					return false;
	        	}
	        }
	        int stationDifference = Math.abs(getCanoeStation().stationIndex - canoeTravelData.stationIndex);
	        if(stationDifference <= 0)
	        {
				player.getActionSender().sendMessage("You're already at this location.");
				return false;
	        }
	       	switch(getCanoeType())
	       	{
				case LOG:
					if(stationDifference > 1)
					{
						player.getActionSender().sendMessage("This canoe cannot travel that far.");
						return false;
					}
					break;
				case DUGOUT:
					if(stationDifference > 2)
					{
						player.getActionSender().sendMessage("This canoe cannot travel that far.");
						return false;
					}
					break;
				case STABLEDUGOUT:
					break;
				case WAKA:
					break;
				default:
					break;
	       	}
	        SetCanoeObject(null);
	        SetCanoeType(null);
			setCanoeStation(null);
			player.getActionSender().sendInterface(18221);
			player.setStopPacket(true);
			final Position newPos = new Position(canoeTravelData.destX, canoeTravelData.destY);
	        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	            @Override
	            public void execute(CycleEventContainer container) {
	            	player.teleport(newPos);
	                player.setStatedInterface("");
	                player.getActionSender().removeInterfaces();
	                container.stop();
	            }
	            @Override
	            public void stop(){
	            	 player.setStopPacket(false);
	            }
	        }, 5);
			return true;
		}
		return false;
	}
	
	public boolean useCanoe(int objectID, int x, int y, int z)
	{
		switch (objectID) {
			case 12147:
			case 12148:
			case 12149:
			case 12150:
				if(getCanoeObject() == null || getCanoeObject() != ObjectHandler.getInstance().getObject(x, y, z))
				{
					player.getActionSender().sendMessage("This canoe does not belong to you.");
					return false;
				}
				player.setStatedInterface("canoe");
				player.getActionSender().sendInterface(18220);
				return true;
		}
		return false;
	}
	
	private boolean canMakeCanoe(CanoeData canoe){
		return player.getSkill().getPlayerLevel(Skill.WOODCUTTING) >= canoe.level;
	}
	
	private void CanoeCraftInterface(){
		player.getActionSender().sendInterface(18178);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(CanoeData.DUGOUT) ? 1 : 0, 18212);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(CanoeData.DUGOUT) ? 0 : 1, 18185);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(CanoeData.STABLEDUGOUT) ? 1 : 0, 18215);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(CanoeData.STABLEDUGOUT) ? 0 : 1, 18182);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(CanoeData.WAKA) ? 1 : 0, 18209);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(CanoeData.WAKA) ? 0 : 1, 18193);
	}

	public boolean craftCanoe(int buttonID)
	{
		final CanoeData canoe = CanoeData.forButtonId(buttonID);
		if(canoe == null || getCanoeStation() == null)
		{
			return false;
		}
		if(canoe.buttonID == buttonID)
		{
			final Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
			if(axe == null) {
				player.getActionSender().sendMessage("You do not have an axe which you have the woodcutting level to use.");
				return false;
			}
			if (!SkillHandler.hasRequiredLevel(player, Skill.WOODCUTTING, canoe.level, "make a " + canoe.name)) {
				return false;
			}
			player.getActionSender().removeInterfaces();
			player.getActionSender().sendMessage("You swing your axe at the tree");
			player.getActionSender().sendSound(472, 0, 0);
			player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task)) {
						container.stop();
						return;
					}
					if (SkillHandler.skillCheck(player.getSkill().getLevel()[Skill.WOODCUTTING], canoe.level, axe.getBonus())) {
						if (Misc.random(100) <= 100) 
						{
							player.getActionSender().sendMessage("You craft the tree into a " + canoe.name);
							player.getSkill().addExp(Skill.WOODCUTTING, canoe.xp);
							CanoeStationData curCanoeStation = getCanoeStation();
							int face = SkillHandler.getFace(curCanoeStation.stationID, curCanoeStation.x, curCanoeStation.y, player.getPosition().getZ());
							SetCanoeObject(new GameObject(canoe.objectID, curCanoeStation.x, curCanoeStation.y, player.getPosition().getZ(), face, 10, curCanoeStation.stationID, 10));
							SetCanoeType(canoe);
							container.stop();
							return;
						}
					}
					player.getActionSender().sendSound(472, 0, 0);
					player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
				}
				@Override
				public void stop() {
					player.getMovementHandler().reset();
					player.getUpdateFlags().sendAnimation(-1, 0);
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
			return true;
		}
		
		return false;
	}
	
	public boolean canoeStation(int objID)
	{
		CanoeStationData canoeStation = CanoeStationData.forId(objID);
		if(canoeStation == null)
		{
			return false;
		}
		if(canoeStation.stationID == objID)
		{
			setCanoeStation(canoeStation);
			CanoeCraftInterface();
			return true;
		}
		return false;
	}
	
	public void setCanoeStation(CanoeStationData newStation)
	{
		curCanoeStation = newStation;
	}
	
	public CanoeStationData getCanoeStation()
	{
		return curCanoeStation;
	}
	
	public void SetCanoeObject(GameObject obj)
	{
		canoeObject = obj;
	}
	
	public GameObject getCanoeObject()
	{
		return canoeObject;
	}
	
	public void SetCanoeType(CanoeData canoe)
	{
		canoeType = canoe;
	}
	
	public CanoeData getCanoeType()
	{
		return canoeType;
	}
	
	private CanoeStationData curCanoeStation = null;
	private GameObject canoeObject = null;
	private CanoeData canoeType = null;
}
