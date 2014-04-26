package com.rs2.model.objects.functions;

import com.rs2.model.players.Player;

public class ChopVines {

	public static void walkThru(Player player, int x, int y) {
		if(x > player.getPosition().getX() && y == player.getPosition().getY())
	        player.getActionSender().walkTo(2, 0, true);
	    else if(x < player.getPosition().getX() && y == player.getPosition().getY())
	        player.getActionSender().walkTo(-2, 0, true);
	    else if(x == player.getPosition().getX() && y < player.getPosition().getY())
	        player.getActionSender().walkTo(0, -2, true);
	    else
	        player.getActionSender().walkTo(0, 2, true);
	}
}
