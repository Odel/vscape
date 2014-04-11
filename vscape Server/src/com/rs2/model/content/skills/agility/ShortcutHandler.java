package com.rs2.model.content.skills.agility;

import com.rs2.model.players.Player;

public class ShortcutHandler {
	
	public static boolean handleShortcut(Player player, int id, int x, int y)
	{
		switch(id)
		{
			case 2618: // lumberyard fence (russian fence)
				player.getUpdateFlags().sendAnimation(839);
				CrossObstacle.setForceMovement(player, 0, player.getPosition().getY() < y ? 2 : -2, 1, 80, 2, true, 0, 0); //exp is currently 0
			return true;
		}
		return false;
	}

}
