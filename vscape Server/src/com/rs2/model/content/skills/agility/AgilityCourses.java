package com.rs2.model.content.skills.agility;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.quests.MonkeyMadness.ApeAtoll.GreeGreeData;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;

public class AgilityCourses {
	
	private Player player;
	
	public AgilityCourses(Player player)
	{
		this.player = player;
	}
	
	private int[] courseStage = {
		0, // gnome
		0, // barb
		0, // wild
		0 // ape
	};
	
	public boolean handleCourse(int id, int x, int y)
	{
		if(gnomeCourse(id, x, y)){
			return true;
		}
		if(barbOutpost(id, x, y)){
			return true;
		}
		if(wildyCourse(id,x,y)){
			return true;
		}
		if(apeCourse(id,x,y)){
			return true;
		}
		return false;
	}
	
	private boolean gnomeCourse(int id, int x, int y)
	{
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
	
	private boolean barbOutpost(int id, int x, int y)
	{
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
	
	private boolean wildyCourse(int id, int x, int y)
	{
		switch(id)
		{
			case 2288:
				if(x == 3004 && y == 3938){
					Agility.crawlPipe(player, x, 3950, 14, 49, 12.5);
					setCourseStage(2,1);
				}
				return true;
			case 2283:
				if(x == 3005 && y == 3952){
					Agility.swingRope(player, x, 3958, 49, 20);
					if(getCourseStage(2) == 1) 
						setCourseStage(2,2);
				}
				return true;
			case 2311:
				if(x == 3001 && y == 3960){
					Agility.crossSkippingStones(player, 2996, y, 8, 49, 20);
					if(getCourseStage(2) == 2) 
						setCourseStage(2,3);
				}
				return true;
			case 2297:
				if(x == 3001 && y == 3945){
					Agility.crossLog(player, 2994, y, 11, 49, 20);
					if(getCourseStage(2) == 3) 
						setCourseStage(2,4);
				}
				return true;
			case 2328:
				if(x >= 2993 && x <= 2995 && y == 3936){
					if(getCourseStage(2) == 4) {
						setCourseStage(2,5);
					}
					if(getCourseStage(2) != 5) {
						Agility.crossObstacle(player, x, 3933, 844, 7, 1, 0);
						player.getDialogue().sendStatement("You have completed the course");
						setCourseStage(2,0);
					}else if(getCourseStage(2) == 5) {
						double bonusXp = (499d*Constants.EXP_RATE);
						Agility.crossObstacle(player, x, 3933, 844, 7, 1, bonusXp);
						player.getDialogue().sendStatement("You have completed the course","You were rewarded "+bonusXp+" bonus experience!" );
						setCourseStage(2,0);
					}
				}
				return true;
		}
		return false;
	}
	
	private boolean apeCourse(int id, int x, int y)
	{
		if(canDoApe(id)){
			switch(id)
			{
				case 12568 : // jump stone
					if(x == 2754 && y == 2742){
						if(player.getPosition().getX() > x)
						{
							Agility.crossObstacle(player, 2753, y, 3481, 3, 48, 40);
						}
					}
					return true;
				case 12570 : // tropical tree climb
					if(x == 2753 && y == 2741){
						Agility.climbTree(player, 2753, 2742, 2, 3487, 3, 48, 40);
					}
					return true;
				case 12573 : // monkey bars
					if(x == 2752 && y == 2741){
						Agility.crossMonkeyBars(player, 2747, y, 0, 3483, 6, 48, 40);
					}
					return true;
				case 12576 : // rock climb
					if(x == 2746 && y == 2741){
						if(player.getPosition().getX() >= x)
						{
							Agility.crossObstacle(player, 2743, y, 3485, 5, 48, 60);
						}
					}
					return true;
				case 12578 : // swing
					if(x == 2752 && y == 2731){
						if(player.getPosition().getX() <= x)
						{
							Agility.swingRope(player, 2756, y, 3488, 48, 100);
						}
					}
					return true;	
				case 12618 : // rope down (couldnt find right anim)
					if(x == 2757 && y == 2734){
						Agility.crossTreeRope(player, 2770, 2747, 3483, 14, 48, 100);
					}
					return true;	
			}
		}
		return false;
	}
	private boolean canDoApe(int object){
		switch(object)
		{
			case 12568 : // jump stone
			case 12570 : // tropical tree climb
			case 12573 : // monkey bars
			case 12576 : // rock climb
			case 12578 : // swing
			case 12618 : // rope down
				GreeGreeData g = GreeGreeData.forItemId(player.getEquipment().getId(Constants.WEAPON));
				if(g == null)
				{
					player.getActionSender().sendMessage("You need to be wielding a monkey GreeGree to do this course.");
					return false;
				}else{
					if(g.getItemId() != 4024 && g.getItemId() != 4025)
					{
						player.getActionSender().sendMessage("This Monkey is not agile enough to do this course.");
						return false;
					}
					if(g.getItemId() == 4024 || g.getItemId() == 4025)
					{
						if(player.transformNpc != g.getTransformId()){
							player.getActionSender().sendMessage("Requip the GreeGree to transform into a monkey.");
							return false;
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
