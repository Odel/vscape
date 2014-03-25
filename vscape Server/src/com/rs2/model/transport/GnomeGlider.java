package com.rs2.model.transport;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/*
 * @author Liberty
 */

public class GnomeGlider {

	public static boolean flightButtons(Player player, int button) {
		for (GliderRoute route : GliderRoute.values()) {
			if (button == route.buttonId) {
				handleFlight(player, route);
				return true;
			}
		}
		return false;
	}

	public static void handleFlight(final Player player, final GliderRoute route) {
        if (player.getInteractingEntity() == null)
            return;
		player.getActionSender().sendInterface(802);
		int flight = getConfig(player.getInteractingEntity().getPosition().getX(), route);
		if (flight < 1) {
			player.getActionSender().sendMessage("You can't fly there from here.");
			return;
		}
		if (flight == 20) {
			player.getActionSender().sendMessage("You can't fly to the same place you are at.");
			return;
		}
		if (flight == 25) {
			player.getActionSender().sendMessage("You can't fly to that place yet.");
			return;
		}
		player.getActionSender().sendConfig(153, flight);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				player.teleport(route.to);
				e.stop();
			}
			@Override
			public void stop() {
			}
		}, 3);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				player.setStopPacket(false);
				player.getActionSender().removeInterfaces();
				player.getActionSender().sendConfig(153, -1);
				e.stop();
			}
			@Override
			public void stop() {
			}
		}, 4);
	}

    public enum GliderRoute {
        SINDARPOS(3058, new Position(2848, 3497), 0),
        TA_QUIR_PRIW(3057, new Position(2465, 3501, 3), 1),
        LEMANTO_ANDRA(3059, new Position(3321, 3427), 2),
        KAR_HEWO(3060, new Position(3278, 3212), 3),
        GANDIUS(3056, new Position(2894, 2729), 4),
        LEMANTOLLY_UNDRI(48054, new Position(2544, 2970), 5);

        int buttonId;
        Position to;
        int move;
        
        GliderRoute(int buttonId, Position to, int move) {
            this.buttonId = buttonId;
            this.to = to;
            this.move = move;
        }
    }

	public static int[] configs = {2847, 2464, 3318, 3283, 2894, 2547};

	private static int getConfig(int x, final GliderRoute route) {
		int startPoint = 0, endPoint = route.move;
		for (int c = 0; c < configs.length; c++) {
			if (configs[c] == x) {
				startPoint = c;
				break;
			}
		}
		if (startPoint == endPoint) {
			return 20; // returns "cant fly to same place" error
		}
		switch (startPoint) {
			case 0 : // Sin
				if (endPoint == 1) {
					return 2;
				}
				break;
			case 1 : // Ta
				if (endPoint == 0) {
					return 1;
				} else if (endPoint == 2) {
					return 3;
				} else if (endPoint == 3) {
					return 4;
				} else if (endPoint == 4) {
					return 7;
				} else if (endPoint == 5) {
					return 25;// return 10;
				}
				break;
			case 3 : // Kar
				if (endPoint == 1) {
					return 5;
				}
				break;
			case 4 : // Gan
				if (endPoint == 1) {
					return 6;
				}
				break;
			case 5 : // Lem und
				if (endPoint == 1) {
					return 11;
				}
				break;
		}
		return 0;
	}

}