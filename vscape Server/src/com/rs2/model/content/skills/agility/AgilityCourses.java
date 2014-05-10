package com.rs2.model.content.skills.agility;

import com.rs2.model.Position;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;

public class AgilityCourses {
	
	public static boolean handleCourse(Player player, int id, int x, int y)
	{
		if(gnomeCourse(player, id, x, y))
		{
			return true;
		}
		if(barbOutpost(player, id, x, y))
		{
			return true;
		}
		return false;
	}
	
	public static boolean gnomeCourse(Player player, int id, int x, int y)
	{
		int targetX = 0;
		int targetY = 0;
		switch(id)
		{
			case 2295: // gnome log
				Agility.crossLog(player, x, 3429, 8, 1, 7.5);
			return true;
			case 2285: // gnome net
				Agility.climbNet(player, x, y-1, 1, 1, 7.5);
			return true;
			case 2313: // gnome treebranch
				Agility.climbBranch(player, x, y-1, 2, 1, 5);
			return true;
			case 2312: // gnome rope
				Agility.crossRope(player, 2483, y, 1, 7.5);
			return true;
			case 2314: // gnome branch down
			case 2315: // gnome branch down
				Agility.climbBranch(player, player.getPosition().getX(), player.getPosition().getY(), 0, 1, 5);
			return true;
			case 2286: // gnome net
				if(player.getPosition().getY() < y){
					Agility.climbNet(player, x, y+1, 0, 1, 7.5);
				}
			return true;
			case 154: // gnome pipe
			case 4058:
				if(x >= 2484 && x <= 2487 && y == 3431){
					Agility.crawlPipe(player, x, 3437, 7, 1, 7.5);
				}
			return true;
		}
		return false;
	}
	
	public static boolean barbOutpost(Player player, int id, int x, int y)
	{
		int targetX = 0;
		int targetY = 0;
		switch(id)
		{
			case 2282: // barb swing 1
				if(player.getPosition().getY() > y) {
					player.getActionSender().animateObject(x, y, 0, 751);
					Agility.swingRope(player, x, 3549, 1, 22);
				}
			return true;
			case 2294: // barb log 1
				Agility.crossLog(player, 2541, y, 11, 1, 13.7);
			return true;
			case 2284: // barb net 1
				Agility.climbNet(player, x-1, y, 1, 1, 8.2);
			return true;
			case 2302: // barb ledge 1
				Agility.crossLedge(player, 2532, y, 3, 3, 1, 22);
			return true;
			case 3205: // barb ladder 1
				Ladders.climbLadder(player, new Position(2532, 3546, 0));
			return true;
			case 1948: // walls
				//1
				if(player.getPosition().getX() < x)
				{
					if(x == 2536)
						Agility.climbOver(player, 2537, y, 1, 13.7);
					if(x == 2539)
						Agility.climbOver(player, 2540, y, 1, 13.7);
					if(x == 2542)
						Agility.climbOver(player, 2543, y, 1, 13.7);
				}
			return true;
		}
		return false;
	}
	
}
