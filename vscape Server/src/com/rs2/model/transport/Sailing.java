package com.rs2.model.transport;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

public class Sailing {
    
    public static boolean sailShip(final Player player, final ShipRoute route, final int npcTalkedTo) {
        if (route.cost > 0) {
            final Item gold = new Item(995, route.cost);
            if (!player.getInventory().playerHasItem(gold)) {
                player.getDialogue().sendPlayerChat("Sorry, I don't have enough coins for that.", Dialogues.SAD);
                player.getDialogue().endDialogue();
                return false;
            }
            player.getInventory().removeItem(gold);
        }
        if (route == ShipRoute.PORT_SARIM_TO_ENTRANA) {
        	//combat item scan
        	if (player.hasCombatEquipment()) {
        		Dialogues.sendDialogue(player, npcTalkedTo, 4, 0);
                return false;
        	}
        }
        player.setStopPacket(true);
        player.teleport(route.to.clone());
	if(player.getQuestStage(15) == 7 && route == ShipRoute.PORT_SARIM_TO_CRANDOR) {
	    Dialogues.startDialogue(player, 918);
	}
        if (route.ordinal() < 16) {
            player.getActionSender().sendConfig(75, route.ordinal() + 1);
            player.getActionSender().sendInterface(3281);
        } else {
            player.getActionSender().sendInterface(11092);
        }
        player.getActionSender().sendMapState(2);

        final Tick clearInterface = new Tick(route.duration) {
            @Override
            public void execute() {
                if (route.ordinal() < 16) {
                	player.getActionSender().sendConfig(75, -1);
                }
                player.getActionSender().removeInterfaces();
                player.getActionSender().sendMapState(0);
                player.setStopPacket(false);
		if(player.getQuestStage(15) == 7 && route == ShipRoute.PORT_SARIM_TO_CRANDOR) {
		    Dialogues.startDialogue(player, 918);
		}
                stop();
            }
        };
        World.getTickManager().submit(clearInterface);
        return true;
    }

    public enum ShipRoute {
        
        PORT_SARIM_TO_ENTRANA(new Position(2834, 3335), 15, 0),
        ENTRANA_TO_PORT_SARIM(new Position(3048, 3234), 15, 0),
        PORT_SARIM_TO_CRANDOR(new Position(2853, 3237), 12, 0),
        CRANDOR_TO_PORT_SARIM(new Position(2834, 3335), 14, 0),
        PORT_SARIM_TO_KARAMJA(new Position(2956, 3146), 10, 30),
        KARAMJA_TO_PORT_SARIM(new Position(3029, 3217), 10, 30),
        ARDOUGNE_TO_BRIMHAVEN(new Position(2772, 3234), 4, 0),
        BRIMHAVEN_TO_ARDOUGNE(new Position(2683, 3271), 4, 0),
        UNUSED1(null, 0, 0),
        UNUSED2(null, 0, 0),
        PORT_KHAZARD_TO_SHIP_YARD(new Position(2998, 3043), 24, 0),
        SHIP_YARD_TO_PORT_KHAZARD(new Position(2676, 3170), 24, 0),
        CAIRN_ISLAND_TO_SHIP_YARD(new Position(2998, 3043), 17, 0),
        PORT_SARIM_TO_PEST_CONTROL(new Position(2659, 2676), 12, 0),
        PEST_CONTROL_TO_PORT_SARIM(new Position(3041, 3202), 12, 0),
        FELDIP_HILLS_TO_CAIRN_ISLAND(new Position(2763, 2956, 1), 10, 0),
        RELLEKKA_TO_WATERBIRTH(new Position(2550, 3759, 0), 10, 1000),
        WATERBIRTH_TO_RELLEKKA(new Position(2620, 3686, 0), 10, 0);
        
        int cost;
        Position to;
        int duration;
        
        ShipRoute(Position to, int duration, int cost) {
            this.to = to;
            this.duration = duration;
            this.cost = cost;
        }
    }
    
}
