package com.rs2.model.content.skills.agility;

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
				/*player.getUpdateFlags().sendAnimation(839);
				CrossObstacle.setForceMovement(player, 0, player.getPosition().getY() < y ? 2 : -2, 1, 80, 2, true, 0, 0); //exp is currently 0
				*/
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
		/*	case 2834: // battlement
				if(x == 2567 && y == 3021){
					targetX = player.getPosition().getX() < x ? 2568 : 2566;
					Agility.crossObstacle(player, targetX, y, 844, 4, 0);
				}
			return true;*/
		}
		return false;
	}

}
