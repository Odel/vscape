package com.rs2.model.content.skills.agility;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;

public class AgilityCourses {
	
	private Player player;
	
	public AgilityCourses(Player player)
	{
		this.player = player;
	}
	
	public int[] courseStage = {
		0,
		0
	};
	
	public boolean handleCourse(int id, int x, int y)
	{
		if(gnomeCourse(id, x, y))
		{
			return true;
		}
		if(barbOutpost(id, x, y))
		{
			return true;
		}
		return false;
	}
	
	public boolean gnomeCourse(int id, int x, int y)
	{
		int targetX = 0;
		int targetY = 0;
		switch(id)
		{
			case 2295: // gnome log
				Agility.crossLog(player, x, 3429, 8, 1, 7.5);
				setCourseStage(0,1);
			return true;
			case 2285: // gnome net
				Agility.climbNet(player, x, y-1, 1, 1, 7.5);
				if(getCourseStage(0) == 1)
					setCourseStage(0,2);
			return true;
			case 2313: // gnome treebranch
				Agility.climbBranch(player, x, y-1, 2, 1, 5);
				if(getCourseStage(0) == 2)
					setCourseStage(0,3);
			return true;
			case 2312: // gnome rope
				Agility.crossRope(player, 2483, y, 1, 7.5);
				if(getCourseStage(0) == 3)
					setCourseStage(0,4);
			return true;
			case 2314: // gnome branch down
			case 2315: // gnome branch down
				Agility.climbBranch(player, player.getPosition().getX(), player.getPosition().getY(), 0, 1, 5);
				if(getCourseStage(0) == 4)
					setCourseStage(0,5);
			return true;
			case 2286: // gnome net
				if(player.getPosition().getY() < y){
					Agility.climbNet(player, x, y+1, 0, 1, 7.5);
					if(getCourseStage(0) == 5)
						setCourseStage(0,6);
				}
			return true;
			case 154: // gnome pipe
			case 4058:
				if(x >= 2484 && x <= 2487 && y == 3431){
					if(getCourseStage(0) == 6) 
						setCourseStage(0,7);

					if(getCourseStage(0) != 7) {
						Agility.crawlPipe(player, x, 3437, 7, 1, 7.5);
						player.getDialogue().sendStatement("You have completed the course");
						setCourseStage(0,0);
					}else if(getCourseStage(0) == 7) {
						double bonusXp = (39d*Constants.EXP_RATE);
						Agility.crawlPipe(player, x, 3437, 7, 1, (7.5+bonusXp));
						player.getDialogue().sendStatement("You have completed the course","You were rewarded "+bonusXp+" bonus experience!" );
						setCourseStage(0,0);
					}
				}
			return true;
		}
		return false;
	}
	
	public boolean barbOutpost(int id, int x, int y)
	{
		int targetX = 0;
		int targetY = 0;
		switch(id)
		{
			case 2282: // barb swing 1
				if(player.getPosition().getY() > y) {
					player.getActionSender().animateObject(x, y, 0, 751);
					Agility.swingRope(player, x, 3549, 1, 22);
					setCourseStage(1,1);
				}
			return true;
			case 2294: // barb log 1
				Agility.crossLog(player, 2541, y, 11, 1, 13.7);
				if(getCourseStage(1) == 1) 
					setCourseStage(1,2);
			return true;
			case 2284: // barb net 1
				Agility.climbNet(player, x-1, y, 1, 1, 8.2);
				if(getCourseStage(1) == 2) 
					setCourseStage(1,3);
			return true;
			case 2302: // barb ledge 1
				Agility.crossLedge(player, 2532, y, 3, 3, 1, 22);
				if(getCourseStage(1) == 3) 
					setCourseStage(1,4);
			return true;
			case 3205: // barb ladder 1
			    if(player.getPosition().getX() == 2532) {
				Ladders.climbLadder(player, new Position(2532, 3546, 0));
				if(getCourseStage(1) == 4) 
					setCourseStage(1,5);
			    }
			return true;
			case 1948: // walls
				//1
				if(player.getPosition().getX() < x)
				{
					if(x == 2536){
						Agility.climbOver(player, 2537, y, 1, 13.7);
						if(getCourseStage(1) == 5) 
							setCourseStage(1,6);
					}
					if(x == 2539){
						Agility.climbOver(player, 2540, y, 1, 13.7);
						if(getCourseStage(1) == 6) 
							setCourseStage(1,7);
					}
					if(x == 2542){
						if(getCourseStage(1) == 7) {
							setCourseStage(1,8);
						}
						if(getCourseStage(1) != 8) {
							Agility.climbOver(player, 2543, y, 1, 13.7);
							player.getDialogue().sendStatement("You have completed the course");
							setCourseStage(1,0);
						}else if(getCourseStage(1) == 8) {
							double bonusXp = (139.5d*Constants.EXP_RATE);
							Agility.climbOver(player, 2543, y, 1, (13.7+bonusXp));
							player.getDialogue().sendStatement("You have completed the course","You were rewarded "+bonusXp+" bonus experience!" );
							setCourseStage(1,0);
						}
					}
				}
			return true;
		}
		return false;
	}
	
	public void setCourseStage(int course, int stage){
		courseStage[course] = stage;
	}
	
	public int getCourseStage(int course){
		return courseStage[course];
	}
}
