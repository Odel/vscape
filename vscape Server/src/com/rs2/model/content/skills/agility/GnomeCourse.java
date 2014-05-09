package com.rs2.model.content.skills.agility;

import com.rs2.model.players.Player;

public class GnomeCourse {
	
	public static boolean handleCourse(Player player, int id, int x, int y)
	{
		int targetX = 0;
		int targetY = 0;
		switch(id)
		{
			case 2295: // gnome log
				Agility.crossLog(player, x, 3429, 1, 7.5);
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
				if(x >= 2484 && x <= 2487 && y == 3431){
					Agility.crawlPipe(player, x, 3437, 1, 7.5);
				}
			return true;
		}
		return false;
	}
}
