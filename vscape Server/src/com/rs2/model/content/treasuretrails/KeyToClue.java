package com.rs2.model.content.treasuretrails;

import com.rs2.model.Position;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 13/01/12 Time: 23:49 To change
 * this template use File | Settings | File Templates.
 */
public class KeyToClue {

	/* the differents key obtainables */

	public static int KEY_1_CHEST = 2832;
	public static int KEY_2_DRAWERS = 2834;
	public static int KEY_3_DRAWERS = 2836;
	public static int KEY_4_DRAWERS = 2838;
	public static int KEY_5_CHEST = 2840;
	public static int KEY_6_DRAWERS = 3606;
	public static int KEY_7_DRAWERS = 3608;
	public static int KEY_8_CHEST = 7297;
	public static int KEY_9_DRAWERS = 7299;
	public static int KEY_10_DRAWERS = 7302;

	/* handle the key based on the clue is */

	public static boolean handleKey(Player player, int clueId) {
		switch (clueId) {
			case 7278 :
				if (player.getInventory().playerHasItem(KEY_2_DRAWERS)) {
					player.getInventory().removeItem(new Item(KEY_2_DRAWERS, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("Wait till I get my hands on Penda, he's nicked the key again.");
					return false;
				}
			case 7280 :
				if (player.getInventory().playerHasItem(KEY_3_DRAWERS)) {
					player.getInventory().removeItem(new Item(KEY_3_DRAWERS, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("I'm guarding the key at the market.");
					return false;
				}
			case 7282 :
				if (player.getInventory().playerHasItem(KEY_4_DRAWERS)) {
					player.getInventory().removeItem(new Item(KEY_4_DRAWERS, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("Don't forget to feed the chickens.");
					return false;
				}
			case 7284 :
				if (player.getInventory().playerHasItem(KEY_6_DRAWERS)) {
					player.getInventory().removeItem(new Item(KEY_6_DRAWERS, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("It's a guard's life.");
					return false;
				}
			case 7296 :
				if (player.getInventory().playerHasItem(KEY_1_CHEST)) {
					player.getInventory().removeItem(new Item(KEY_1_CHEST, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("Stand by your man.");
					return false;
				}
			case 7298 :
				if (player.getInventory().playerHasItem(KEY_7_DRAWERS)) {
					player.getInventory().removeItem(new Item(KEY_7_DRAWERS, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("Beware of dog.");
					return false;
				}
			case 7300 :
				if (player.getInventory().playerHasItem(KEY_9_DRAWERS)) {
					player.getInventory().removeItem(new Item(KEY_9_DRAWERS, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("One of the wizards might have the key.");
					return false;
				}
			case 7301 :
				if (player.getInventory().playerHasItem(KEY_5_CHEST)) {
					player.getInventory().removeItem(new Item(KEY_5_CHEST, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("You, red monster, have been slain by me, a simple barbarian man.");
					return false;
				}
			case 7303 :
				if (player.getInventory().playerHasItem(KEY_10_DRAWERS)) {
					player.getInventory().removeItem(new Item(KEY_10_DRAWERS, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("You'll never get these drawers open now! - Zamorak Monk");
					return false;
				}
			case 7304 :
				if (player.getInventory().playerHasItem(KEY_8_CHEST)) {
					player.getInventory().removeItem(new Item(KEY_8_CHEST, 1));
					return true;
				} else {
					player.getActionSender().sendMessage("Property of the Clocktower Monastery");
					return false;
				}
		}
		return true;
	}

	/* drop the key if the player has the corresponding clue */

	public static void dropKey(Player player, Npc n) {
		switch (n.getDefinition().getId()) {
			case 1087 :// penda
				if (!player.getInventory().playerHasItem(KEY_2_DRAWERS) && player.getInventory().playerHasItem(7278)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_2_DRAWERS), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
			case 1317 :// freminik market guard
			case 2236 :
			case 2571 :
				if (!player.getInventory().playerHasItem(KEY_3_DRAWERS) && player.getInventory().playerHasItem(7280)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_3_DRAWERS), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
			case 41 :// chickens
			case 288 :
			case 951 :
			case 1017 :
			case 1401 :
			case 1402 :
			case 2313 :
			case 2314 :
			case 2315 :
				if (!player.getInventory().playerHasItem(KEY_4_DRAWERS) && player.getInventory().playerHasItem(7282)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_4_DRAWERS), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
			case 32 :// guard
				if (!player.getInventory().playerHasItem(KEY_6_DRAWERS) && player.getInventory().playerHasItem(7284)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_6_DRAWERS), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
			case 1 :// man
			case 2 :
			case 3 :
				if (!player.getInventory().playerHasItem(KEY_1_CHEST) && player.getInventory().playerHasItem(7296)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_1_CHEST), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
			case 99 :// guard dog
			case 3582 :// guard dog
				if (!player.getInventory().playerHasItem(KEY_7_DRAWERS) && player.getInventory().playerHasItem(7298)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_7_DRAWERS), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
			case 13 :// wizard
				if (!player.getInventory().playerHasItem(KEY_9_DRAWERS) && player.getInventory().playerHasItem(7300)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_9_DRAWERS), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
			case 3246 :// barbarian
			case 3247 :
			case 3248 :
			case 3249 :
			case 3250 :
			case 3251 :
			case 3252 :
			case 3253 :
			case 3255 :// barbarian
			case 3256 :
			case 3257 :
			case 3258 :
			case 3259 :
			case 3260 :
			case 3261 :
			case 3262 :
			case 3263 :
				if (!player.getInventory().playerHasItem(KEY_5_CHEST) && player.getInventory().playerHasItem(7301)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_5_CHEST), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
			case 188 :// monk of zamorak
			case 189 :
			case 190 :
			case 1044 :
			case 1045 :
			case 1046 :
				if (!player.getInventory().playerHasItem(KEY_10_DRAWERS) && player.getInventory().playerHasItem(7303)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_10_DRAWERS), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
			case 222 :// monk
			case 651 :
			case 3079 :
			case 3080 :
			case 281 :
				if (!player.getInventory().playerHasItem(KEY_8_CHEST) && player.getInventory().playerHasItem(7304)) {
                    GroundItemManager.getManager().dropItem(new GroundItem(new Item(KEY_8_CHEST), player, new Position(n.getPosition().getX(), n.getPosition().getY(), n.getPosition().getZ())));
				}
				break;
		}
	}

}
