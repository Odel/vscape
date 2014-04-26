package com.rs2.model.players;

import java.io.BufferedReader;
import java.io.FileReader;

import com.rs2.model.Position;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/25/12 Time: 2:53 PM To change
 * this template use File | Settings | File Templates.
 */
public class GlobalGroundItem {

	/**
	 * loads the items
	 */
	public static void initialize() {
		String Data;
		BufferedReader Checker = null;
        int drops = 0;
		try {
			Checker = new BufferedReader(new FileReader("./data/world/globaldrops.txt"));
			while ((Data = Checker.readLine()) != null) {
				if (Data.startsWith("#")) {
					continue;
				}
				String[] args = Data.split(":");
                GroundItem item = new GroundItem(new Item(Integer.parseInt(args[0]), Integer.parseInt(args[1])), new Position(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])), true);
                GroundItemManager.getManager().dropItem(item);
                drops++;
			}
			Checker.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + drops + " global drops.");
	}

}
