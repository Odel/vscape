package com.rs2.model.content.skills.agility;

import com.rs2.model.Position;
import com.rs2.model.players.Player;

public class ShortcutHandler {
	
	public static boolean handleShortcut(Player player, int id, int x, int y)
	{
		int targetX = 0;
		int targetY = 0;
		switch(id)
		{
			case 2618: // lumberyard fence (russian fence)
				targetY = player.getPosition().getY() < y ? 1 : -1;
				Agility.crossObstacle(player, x, y+targetY, 839, 2, 0);
			return true;
			case 12982: // stile
				targetY = player.getPosition().getY() < y ? 2 : -1;
				Agility.crossObstacle(player, x, y + targetY, 839, 3, 0);
			return true;
			case 2231: // rockslide
				if(x >= 2791 && x <= 2795 && y >= 2978 && y <= 2980){
					targetX = player.getPosition().getX() < x ? 2795 : 2791;
					Agility.crossObstacle(player, targetX, y, 844, 4, 0);
				}
			return true;
			//Arandar
			case 9296:
				if(y == 3299)//first rocks down
				Agility.crossObstacle(player, 2345, 3294, 844, 7, 59, 0);
				if(y == 3288)//second rocks down
				Agility.crossObstacle(player, 2338, 3286, 844, 7, 68, 0);
				if(y == 3252) //third rocks down
				Agility.crossObstacle(player, 2338, 3253, 844, 7, 85, 0);
				return true;
			case 9297:
				if(y == 3295) //first rocks up
				Agility.crossObstacle(player, 2345, 3300, 844, 7, 59, 0);
				if(y == 3285) //second rocks up
				Agility.crossObstacle(player, 2338, 3281, 844, 7, 68, 0);
				if(y == 3253) //third rocks up
				Agility.crossObstacle(player, 2332, 3252, 844, 7, 85, 0);
				return true;
			//Tirannwn
			case 3921: //tripwire
				if(x == 2294 && y == 3243){
					targetY = player.getPosition().getY() > y ? -1 : 1;
					Agility.crossObstacle(player, x, y+targetY, 0, 2, 0, 0);
				}
				return true;
			case 3933: //log
				if(x == 2290 && y == 3238)
					Agility.crossObstacle(player, x, 3232, 762, 8, 10, 0);
				if(x == 2290 && y == 3233)
					Agility.crossObstacle(player, x, 3239, 762, 8, 10, 0);
				return true;
			case 3937: //Dense Forest
			case 3938:
			case 3939:
			case 3998:
			case 3999:
				return true;
			//edgeville dungeon monkey bars
			case 2320:
				if(x == 3119 || x == 3120){
					if(y == 9964)
						Agility.crossMonkeyBars(player, player.getPosition().getX(), 9969, 15, 0);
					if(y == 9969)
						Agility.crossMonkeyBars(player, player.getPosition().getX(), 9964, 0, 0);
				}
			return true;
			//barbarian agility pipe entrance
			case 2287:
				if(x == 2552 && y == 3559){
					targetY = player.getPosition().getY() > y ? 3558 : 3561;
					Agility.crawlPipe(player, x, targetY, 3, 35, 0);
				}
			return true;
		}
		return false;
	}

}
