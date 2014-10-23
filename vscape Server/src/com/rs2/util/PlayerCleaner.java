package com.rs2.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.rs2.cache.Cache;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 *
 */
public class PlayerCleaner {

    private static final int GOLD_LIMIT = 100000000;
    private static Integer[] itemsToRemove = new Integer[] {7158, 7407, 7404, 7405, 7406};
    
    public static void start() {
        try {
            Cache.load();
            ItemDefinition.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Integer> removeItems = new LinkedList<Integer>();
        System.out.println("Max Gold is "+GOLD_LIMIT+" GP and Removing Items:");
        for (int i : itemsToRemove) {
            removeItems.add(i);
            /*if (ItemDefinition.forId(i+1).isNoted())
                removeItems.add(i+1);*/
            System.out.print(ItemDefinition.forId(i).getName()+", "+ItemDefinition.forId(i+1).getName()+", ");
        }
        itemsToRemove = removeItems.toArray(new Integer[removeItems.size()]);
        File directory = new File("./data/characters/");
        for (File file : directory.listFiles()) {
            String name = file.getName();
            name = name.substring(0, name.indexOf("."));
            Player player = new Player(null);
            player.setUsername(name);
            try {
                PlayerSave.load(player);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            //System.out.println("Loaded: "+player.toString());
            long gold = 0;
            long d2h = 0;
            for (Item item : player.getInventory().getItemContainer().getItems()) {
                if (item == null)
                    continue;
                if (item.getId() == 995)
                    gold += item.getCount();
                if (item.getId() == 7158 || item.getId() == 7407) {
                	d2h += item.getCount();
                }
            }
         /*   for (Item item : player.getBank().getItems()) {
                if (item == null)
                    continue;
                if (item.getId() == 995)
                    gold += item.getCount();
                if (item.getId() == 7158 || item.getId() == 7407) {
                	d2h += item.getCount();
                }
            }*/
            if (gold > GOLD_LIMIT) {
            	System.out.println(player.getUsername()+" gold: "+gold);
                gold = 500000;
            }
            if (d2h > 20) {
            	System.out.println(player.getUsername()+" d2h: "+d2h);
            	d2h = 0;
            }
            Item[] items = player.getInventory().getItemContainer().getItems();
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null)
                    continue;
                Item item = items[i];
                if (item.getId() == 995) {
                    items[i] = null;
                }
                for (int ban : itemsToRemove)
                    if (item.getId() == ban)
                        items[i] = null;
            }
          /*  items = player.getBank().getItems();
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null)
                    continue;
                Item item = items[i];
                if (item.getId() == 995)
                    items[i] = null;
                for (int ban : itemsToRemove)
                    if (item.getId() == ban)
                        items[i] = null;
            }*/
           /* items = player.getEquipment().getItemContainer().getItems();
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null)
                    continue;
                Item item = items[i];
                for (int ban : itemsToRemove)
                    if (item.getId() == ban)
                        items[i] = null;
            }*/

           /* player.getBank().add(new Item(995, (int)gold));
            if (d2h > 0) {
                player.getBank().add(new Item(7158, (int)d2h));
            }*/
            PlayerSave.save(player);
        }
        System.out.println("DONE!");
    }
}