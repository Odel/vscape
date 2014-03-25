package com.rs2.model.transport;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class Travel {

    public static boolean startTravel(final Player player, final Route route) {
        if (route.cost > 0) {
            final Item gold = new Item(995, route.cost);
            if (!player.getInventory().playerHasItem(gold)) {
                player.getDialogue().sendPlayerChat("Sorry, I don't have enough coins for that.", Dialogues.SAD);
                player.getDialogue().endDialogue();
                return false;
            }
            player.getInventory().removeItem(gold);
        }
        player.fadeTeleport(route.to.clone());
        return true;
    }

    public enum Route {
        BRIMHAVEN_TO_SHILO(new Position(2836, 2956), 3, 10),
        SHILO_TO_BRIMHAVEN(new Position(2780, 3211), 3, 10);

        int cost;
        Position to;
        int duration;
        
        Route(Position to, int duration, int cost) {
            this.to = to;
            this.duration = duration;
            this.cost = cost;
        }
    }
    
}
