package com.rs2.model.content.skills.agility;

import java.util.HashMap;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.farming.Allotments.AllotmentData;
import com.rs2.model.players.Player;


public class Agility {

	//0 gnome, 1+ todo
	public boolean[][] courseCompletion = {{false, false, false, false, false, false, false}};
	
	public enum ShortcutData {
		TAVERLY_BLUEDRAGONS(9293, 70, false);
		private int objectId;
		private int level;
		private int x, y, z;
		private boolean northToSouth; //north to south travel or east to west
		
		private static Map<Integer, ShortcutData> shortcuts = new HashMap<Integer, ShortcutData>();

		static {
			for (ShortcutData data : ShortcutData.values()) {
				shortcuts.put(data.objectId, data);
			}
		}
		
		ShortcutData(int id, int level, boolean northToSouth) {
			this.objectId = id;
			this.level = level;
			this.northToSouth = northToSouth;
		}
		
		public static ShortcutData forId(int objectId) {
			return shortcuts.get(objectId);
		}
		
		public int getObjectId() {
			return objectId;
		}
		
		public int getLevel() {
			return level;
		}		
	}
	
	public enum ObstacleData {
		//gnome stronghold course
		GNOME_LOG(1, 2295, 7.5, 0, -7, 0, 1, 770, "You cross the log Safely."), GNOME_ROPEWALLUP(2, 2285, 7.5, 0, -3, 1, 1, 839, "You climb up the tree."), GNOME_TREEBRANCH(3, 2313, 5, 0, -3, 1, 1, 839, "You climb up the wall."), GNOME_TIGHTROPE(4, 2312, 7.5, 6, 0, 0, 1, 770, "You cross the rope safely."), GNOME_TREEBRANCHDOWN(5, 2314, 5, 0, 0, -2, 1, 839, "You climb down the tree."), GNOME_ROPEWALL(6, 2286, 7.5, 0, 2, 0, 1, 839, "You climb over the obstacle."), GNOME_PIPEWEST(7, 154, 7.5, 0, 7, 0, 1, 746, "You squeeze through the pipe."), GNOME_PIPEEAST(7, 4058, 7.5, 0, 7, 0, 1, 746, "You squeeze through the pipe.");
		
		private int courseIndex;
		private int objectId;
		private int levelRequired;
		private int deltaX;
		private int deltaY;
		private int deltaZ;
		//private double failChance;
		private double xp;
		private int animId;
		private String message;

		private static Map<Integer, ObstacleData> obstacles = new HashMap<Integer, ObstacleData>();

		static {
			for (ObstacleData data : ObstacleData.values()) {
				obstacles.put(data.objectId, data);
			}
		}

		ObstacleData(int courseIndex, int objectId, double xp, int deltaX, int deltaY, int deltaZ, int levelRequired, int animId, String message) { //, double failChance) {
			this.courseIndex = courseIndex;
			this.objectId = objectId;
			this.xp = xp;
			this.deltaX = deltaX;
			this.deltaY = deltaY;
			this.deltaZ = deltaZ;
			this.levelRequired = levelRequired;
			this.animId = animId;
			this.message = message;
			//this.failChance = failChance;
			
		}

		public static ObstacleData forId(int objectId) {
			return obstacles.get(objectId);
		}
		
		public int getCourse() {
			return courseIndex / 10;
		}
		
		public int getCourseIndex() {
			return courseIndex % 10 - 1;
		}

		public int getObjectId() {
			return objectId;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getFailChance() {
			return 0;//failChance;
		}
		
		public int getDeltaX() {
			return deltaX;
		}
		
		public int getDeltaY() {
			return deltaY;
		}
		
		public int getDeltaZ() {
			return deltaZ;
		}

		public double getXp() {
			return xp;
		}
		
		public String getMessage(){
			return message;
		}
		
		public int getAnimId() {
			return animId;
		}
	}
	
	public static boolean trainingObject(Player player, int id){
		final ObstacleData obstacleData = ObstacleData.forId(id);
		if(obstacleData == null)
			return false;
		if(!Constants.AGILITY_ENABLED){
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		handleObstacle(player, id);
		return true;
	}
	
	public static void handleObstacle(Player player, int objectId){
		ObstacleData object = ObstacleData.forId(objectId);
		if(object == null)
			return;
		CrossObstacle.walkAcross(player, object.getXp(), object.getDeltaX(), object.getDeltaY(), object.getDeltaZ(), 1, 1, object.getAnimId());
		player.getActionSender().sendMessage(object.getMessage());
		Agility agility = player.getAgility();
		player.getActionSender().sendMessage("course id: " + object.getCourse());
		agility.courseCompletion[object.getCourse()][object.getCourseIndex()] = true;
		if(agility.isCourseComplete(agility, object.getCourse())) {
			player.getSkill().addExp(Skill.AGILITY, getCompletionXp(object.getCourse()));
			for(int i = 0; i < agility.courseCompletion[object.getCourse()].length; i++) {
				agility.courseCompletion[object.getCourse()][i] = false;
			}
		}
		player.isCrossingObstacle = false;
    }
	
	public static int getCompletionXp(int course) {
		switch(course){
		case 0:
			return 39;
		}
		return 0;
	}
	
	private boolean isCourseComplete(Agility agility, int course) {
		for(boolean courseIndex : agility.courseCompletion[course]){
			if(!courseIndex)
				return false;
		}
		return true;
	}
	
	public static void handleShortcut(Player player, int id, int x, int y, int z) {
		ShortcutData shortcut = ShortcutData.forId(id);
		if(shortcut == null)
			return;
		if(shortcut.level > player.getSkill().getLevel()[Skill.AGILITY]) {
			player.getActionSender().sendMessage("You need an agility level of " + shortcut.getLevel() + " to use this shortcut.");
			return;
		}
		if(!shortcut.northToSouth) {
			switch(x){
			case 2890: //blue dragon out
				player.teleport(new Position(2886, 9799, 0));
				break;
			case 2887: //blue dragon in
				player.teleport(new Position(2892, 9799, 0));
				break;
			}
		}
		
	}

	public static boolean shortcutObject(Player player, int id, int x, int y, int z) {
		ShortcutData object = ShortcutData.forId(id);
		if(object == null)
			return false;
		if(!Constants.AGILITY_ENABLED){
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		handleShortcut(player, id, x, y, z);
		return true;
	}
}