package com.rs2.model.players.item.functions;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class Nests {

    public static final int[] COMMON_SEEDS = {5312, 5283, 5284, 5285, 5286, 5313};
    public static final int[] UNCOMMON_SEEDS = {5314, 5288, 5287, 5315, 5289};
    public static final int[] RARE_SEEDS = {5316, 5290};
    public static final int[] VERY_RARE_SEEDS = {5317};

    public static final int[] COMMON_RING = {1635, 1637};
    public static final int[] UNCOMMON_RING = {1639};
    public static final int[] RARE_RING = {1641};
    public static final int[] VERY_RARE_RING = {1643};

    public static boolean handleNest(Player player, int itemId){
        int[] commonItems, uncommonItems, rareItems, veryRareItems;
        switch(itemId){
            case 5070:
                player.getInventory().removeItem(new Item(itemId));
                player.getInventory().addItem(new Item(5075));
                player.getInventory().addItem(new Item(5076));
                return true;
            case 5071:
                player.getInventory().removeItem(new Item(itemId));
                player.getInventory().addItem(new Item(5075));
                player.getInventory().addItem(new Item(5078));
                return true;
            case 5072:
                player.getInventory().removeItem(new Item(itemId));
                player.getInventory().addItem(new Item(5075));
                player.getInventory().addItem(new Item(5077));
                return true;
            case 5073:
                commonItems = COMMON_SEEDS;
                uncommonItems = UNCOMMON_SEEDS;
                rareItems = RARE_SEEDS;
                veryRareItems = VERY_RARE_SEEDS;
                break;
            case 5074:
                commonItems = COMMON_RING;
                uncommonItems = UNCOMMON_RING;
                rareItems = RARE_RING;
                veryRareItems = VERY_RARE_RING;
                break;
            default :
                return false;
        }
        int randomNumber = Misc.random(100), finalItem;
        if(randomNumber <= 60) finalItem = commonItems[Misc.random(commonItems.length - 1)];
        else if(randomNumber <= 80) finalItem = uncommonItems[Misc.random(uncommonItems.length - 1)];
        else if(randomNumber <= 95) finalItem = rareItems[Misc.random(rareItems.length - 1)];
        else finalItem = veryRareItems[Misc.random(veryRareItems.length - 1)];

        player.getActionSender().sendMessage("You search the nest...and find something in it!");
        player.getInventory().removeItem(new Item(itemId));
        player.getInventory().addItem(new Item(5075));
        player.getInventory().addItem(new Item(finalItem));
        return true;
    }

}
