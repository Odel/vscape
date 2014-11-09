package com.rs2.model.content.skills.Woodcutting;

import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;
import com.rs2.model.Position;
import com.rs2.model.tick.Tick;
import com.rs2.model.World;

/*
 *  this is very basic canoe class and its not very good but it works for now
 *  bobster
 * 
 */
public class Canoe {
	
// Canoes 12147, 12148, 12149, 12150
	
	public enum CanoeTravelData
	{
		LUMBRIDGE(71079, 3243, 3235),
		CHAMPGUILD(71080, 3204, 3343),
		BARBVILL(71081, 3112, 3409),
		EDGE(71086, 3132, 3508);
		
		private int destButtonId;
		private int destX;
		private int destY;
		
		CanoeTravelData(int destButtonId, int x, int y) {
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
		LUMBRIDGE(12163, 3241, 3235),
		CHAMPGUILD(12164, 3200, 3341),
		BARBVILL(12165, 3110, 3409),
		EDGE(12166, 3130, 3508);
		
		private int stationID;
		private int x;
		private int y;
	
	 	CanoeStationData(int stationID, int x, int y) {
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
		LOG("Log", 71028, 12147, 12),
		Dugout("Dugout", 71029, 12148, 27);
		
		private String name;
		private int buttonID;
		private int objectID;
		private int level;
	
		CanoeData(String name, int buttonID, int objectID, int level) {
			this.name = name;
			this.buttonID = buttonID;
			this.objectID = objectID;
			this.level = level;
		}
		
		public static CanoeData forButtonId(int buttonID) {
			for (CanoeData canoeData : CanoeData.values())
					if (buttonID == canoeData.buttonID)
						return canoeData;
			return null;
		}
	}
	
	public static boolean travelCanoe(final Player player, int buttonId)
	{
	    RSInterface inter = RSInterface.forId(18220);
        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("canoe")) {
        	player.getActionSender().removeInterfaces();
            return false;
        }
		CanoeTravelData canoeTravelData = CanoeTravelData.forId(buttonId);
		if(canoeTravelData == null)
		{
			return false;
		}
		if(canoeTravelData.destButtonId == buttonId)
		{
			player.getActionSender().sendInterface(18221);
			player.setStopPacket(true);
			final Position newPos = new Position(canoeTravelData.destX, canoeTravelData.destY);
	        final Tick travelFinish = new Tick(5) {
	            @Override
	            public void execute() {
	            	player.teleport(newPos);
	                player.setStopPacket(false);
	                player.getActionSender().removeInterfaces();
	                stop();
	            }
	        };
	        World.getTickManager().submit(travelFinish);
			return true;
		}
		return false;
	}
	
	public static boolean useCanoe(Player player, int objectID)
	{
		switch (objectID) {
			case 12147:
			case 12148:
			case 12149:
			case 12150:
				player.getActionSender().sendInterface(18220);
				player.setStatedInterface("canoe");
				return true;
		}
		return false;
	}

	public static boolean craftCanoe(final Player player, int buttonID)
	{
		final CanoeData canoe = CanoeData.forButtonId(buttonID);
		if(canoe == null)
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
			if (!SkillHandler.hasRequiredLevel(player, Skill.WOODCUTTING, canoe.level, "make this")) {
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
							CanoeStationData curCanoeStation = player.getCanoeStation();
							int face = SkillHandler.getFace(curCanoeStation.stationID, curCanoeStation.x, curCanoeStation.y, player.getPosition().getZ());
							new GameObject(canoe.objectID, curCanoeStation.x, curCanoeStation.y, player.getPosition().getZ(), face, 10, curCanoeStation.stationID, 10);
							player.setCanoeStation(null);
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
	
	public static boolean canoeStation(Player player, int objID)
	{
		CanoeStationData canoeStation = CanoeStationData.forId(objID);
		if(canoeStation == null)
		{
			return false;
		}
		if(canoeStation.stationID == objID)
		{
			player.setCanoeStation(canoeStation);
			player.getActionSender().sendInterface(18178);
			return true;
		}
		return false;
	}

}
