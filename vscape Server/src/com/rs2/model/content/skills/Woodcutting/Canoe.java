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
	
	// canoe configs bit shifting
	// shape canoe config stage value = 10 (Shape canoe)
	// float canoe anim 3304
	//return (canoestage << (stationIndex*8);
	
	private Player player;
	
	public Canoe(final Player player)
	{
		this.player = player;
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
	
	public enum Canoes {
		LOG("Log", 71028, 12147, 1, 12, 30.0, false),
		DUGOUT("Dugout", 71029, 12148, 2, 27, 60.0, false),
		STABLEDUGOUT("Stable Dugout", 71030, 12149, 3, 42, 90.0, false),
		WAKA("Waka", 71031, 12150, 4, 57, 150.0, true);
		
		private String name;
		private int buttonID;
		private int objectID;
		private int config;
		private int level;
		private double xp;
		private boolean canWilderness;
	
		Canoes(String name, int buttonID, int objectID, int config, int level, double xp, boolean canWilderness) {
			this.name = name;
			this.buttonID = buttonID;
			this.objectID = objectID;
			this.config = config;
			this.level = level;
			this.xp = xp;
			this.canWilderness = canWilderness;
		}
		
		public static Canoes forButtonId(int buttonID) {
			for (Canoes canoeData : Canoes.values())
					if (buttonID == canoeData.buttonID)
						return canoeData;
			return null;
		}
	}
	
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
	
	public boolean objectClick(int objectID, int x, int y, int z){
		if(chopStation(objectID, x, y, z) || useCanoe(objectID, x, y, z))
			return true;
		return false;
	}
	
	public boolean chopStation(int objectID, int x, int y, int z){
		CanoeStationData canoeStation = CanoeStationData.forId(objectID);
		if(canoeStation == null)
		{
			return false;
		}
		if(canoeStation.stationID == objectID)
		{
			if(getStationStage() > 0 && curCanoeStation != null && curCanoeStation == canoeStation)
			{
				return false;
			}
			if(curCanoeStation != canoeStation)
			{
				setCanoeStation(null);
				sendCanoeConfig(0);
			}
			setCanoeStation(canoeStation);
			sendCanoeConfig(0);
			final Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
			if(axe == null) {
				player.getActionSender().sendMessage("You do not have an axe which you have the woodcutting level to use.");
				return true;
			}
			if (!SkillHandler.hasRequiredLevel(player, Skill.WOODCUTTING, 12, "make a canoe")) {
				return true;
			}
			player.getActionSender().removeInterfaces();
			player.getActionSender().sendMessage("You swing your axe at the tree.");
			player.getActionSender().sendSound(472, 0, 0);
			player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task) || !SkillHandler.hasRequiredLevel(player, Skill.WOODCUTTING, 12, "make a canoe.")) {
						container.stop();
						return;
					}
					if (SkillHandler.skillCheck(player.getSkill().getLevel()[Skill.WOODCUTTING], 12, axe.getBonus())) {
						setStationStage(1);
						sendCanoeConfig(10);
						container.stop();
						return;
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
	
	private void CanoeCraftInterface() {
		player.getActionSender().sendInterface(18178);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(Canoes.DUGOUT) ? 1 : 0, 18212);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(Canoes.DUGOUT) ? 0 : 1, 18185);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(Canoes.STABLEDUGOUT) ? 1 : 0, 18215);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(Canoes.STABLEDUGOUT) ? 0 : 1, 18182);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(Canoes.WAKA) ? 1 : 0, 18209);
		player.getActionSender().sendInterfaceHidden(canMakeCanoe(Canoes.WAKA) ? 0 : 1, 18193);
	}
	
	private boolean canMakeCanoe(Canoes canoe){
		return player.getSkill().getPlayerLevel(Skill.WOODCUTTING) >= canoe.level;
	}

	public boolean craftCanoe(int buttonID)
	{
		if(curCanoeStation != null)
		{
			final Canoes canoe = Canoes.forButtonId(buttonID);
			if(canoe == null)
			{
				return false;
			}
			if(canoe.buttonID == buttonID)
			{
		        if (!player.getStatedInterface().equals("canoeCraft")) {
		        	player.getActionSender().removeInterfaces();
		            return false;
		        }
				if(getStationStage() == 1)
				{
					final Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
					if(axe == null) {
						player.getActionSender().sendMessage("You do not have an axe which you have the woodcutting level to use.");
						return true;
					}
					if (!SkillHandler.hasRequiredLevel(player, Skill.WOODCUTTING, canoe.level, "make a " + canoe.name)) {
						return true;
					}
					player.getActionSender().removeInterfaces();
					player.getActionSender().sendMessage("You swing your axe at the log.");
					player.getActionSender().sendSound(472, 0, 0);
					player.getUpdateFlags().sendAnimation(getCraftAnimation(axe), 0);
					final int task = player.getTask();
					player.setSkilling(new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (!player.checkTask(task) || !SkillHandler.hasRequiredLevel(player, Skill.WOODCUTTING, canoe.level, "make a " + canoe.name)) {
								container.stop();
								return;
							}
							if (SkillHandler.skillCheck(player.getSkill().getLevel()[Skill.WOODCUTTING], canoe.level, axe.getBonus())) {
								setStationStage(2);
								sendCanoeConfig(canoe.config);
								currentCanoe = canoe;
								player.getActionSender().sendMessage("You craft the tree into a " + canoe.name);
								player.getSkill().addExp(Skill.WOODCUTTING, canoe.xp);
								container.stop();
								return;
							}
							player.getActionSender().sendSound(472, 0, 0);
							player.getUpdateFlags().sendAnimation(getCraftAnimation(axe), 0);
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
			}
		}
		return false;
	}
	
	public boolean useCanoe(int objectID, int x, int y, int z)
	{
		CanoeStationData canoeStation = CanoeStationData.forId(objectID);
		if(canoeStation == null)
		{
			return false;
		}
		if(curCanoeStation != null && curCanoeStation.stationID == objectID)
		{
			if(getStationStage() == 1)
			{
				player.setStatedInterface("canoeCraft");
				CanoeCraftInterface();
				return true;
			}
			else if(getStationStage() == 2)
			{
				if(currentCanoe != null) {
					setStationStage(3);
					sendCanoeConfig(4 + currentCanoe.config);
					player.getActionSender().animateObject(x, y, z, 3304);
				    CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
					    @Override
					    public void execute(CycleEventContainer b) {
							setStationStage(4);
							sendCanoeConfig(10 + currentCanoe.config);
					    	b.stop();
					    }

					    @Override
					    public void stop() {
					    }
					}, 3);
					return true;
				}
			}
			else if(getStationStage() == 3)
			{
				return true;
			}
			else if(getStationStage() == 4)
			{
				if(currentCanoe != null) {
					player.setStatedInterface("canoeTravel");
					player.getActionSender().sendInterface(18220);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean travelCanoe(int buttonId)
	{
		CanoeTravelData canoeTravelData = CanoeTravelData.forId(buttonId);
		if(canoeTravelData == null)
		{
			return false;
		}
		if(canoeTravelData.destButtonId == buttonId && curCanoeStation != null && currentCanoe != null)
		{
	        if (!player.getStatedInterface().equals("canoeTravel")) {
	        	player.getActionSender().removeInterfaces();
	            return false;
	        }
	        if(canoeTravelData == CanoeTravelData.WILDY)
	        {
	        	if(!currentCanoe.canWilderness){
					player.getActionSender().sendMessage("This canoe cannot travel into the wilderness.");
					return false;
	        	}
	        }
	        int stationDifference = Math.abs(curCanoeStation.stationIndex - canoeTravelData.stationIndex);
	        if(stationDifference <= 0)
	        {
				player.getActionSender().sendMessage("You're already at this location.");
				return false;
	        }
	       	switch(currentCanoe)
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
	       	setStationStage(0);
			setCanoeStation(null);
			currentCanoe = null;
			sendCanoeConfig(0);
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

	public int getCraftAnimation(Tool tool)
	{
		switch(tool)
		{
		case ADAMANT_AXE:
			return 6739;
		case BLACK_AXE:
			return 6741;
		case BRONZE_AXE:
			return 6744;
		case DRAGON_AXE:
			return 6745;
		case IRON_AXE:
			return 6743;
		case MITHRIL_AXE:
			return 6740;
		case RUNE_AXE:
			return 6738;
		case STEEL_AXE:
			return 6742;
		default:
			return tool.getAnimation();
		}
	}
	
	public void setCanoeStation(CanoeStationData station)
	{
		curCanoeStation = station;
	}
	
	private void setStationStage(int stage)
	{
		stationStage = stage;
	}
	
	private int getStationStage()
	{
		return stationStage;
	}
	
	private void sendCanoeConfig(int value) {
		if(curCanoeStation == null)
		{
			stationStage = 0;
			player.getActionSender().sendConfig(674, 0);
			return;
		}else{
			int stationIndex = curCanoeStation.stationIndex;
			player.getActionSender().sendConfig(674, (value << (stationIndex*8)));
		}
	}
	
	private CanoeStationData curCanoeStation = null;
	private int stationStage = 0; // 0 normal 1 chopped 2 crafted 3 pushing 4 floating
	private Canoes currentCanoe = null;
}
