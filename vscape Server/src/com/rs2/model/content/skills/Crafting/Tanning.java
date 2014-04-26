package com.rs2.model.content.skills.Crafting;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 20:05 To change
 * this template use File | Settings | File Templates.
 */
public class Tanning {

	public static final int COWHIDE = 1739;
	public static final int GREEN_DHIDE = 1753;
	public static final int BLUE_DHIDE = 1751;
	public static final int RED_DHIDE = 1749;
	public static final int BLACK_DHIDE = 1747;
	public static final int SNAKE_HIDE = 6287;

	public static final int LEATHER = 1741;
	public static final int HARDLEATHER = 1743;
	public static final int GREEN_HIDE = 1745;
	public static final int BLUE_HIDE = 2505;
	public static final int RED_HIDE = 2507;
	public static final int BLACK_HIDE = 2509;
	public static final int SNAKESKIN = 6289;
	/**
	 * Initializes the tanning screen for the player. This needs to be redone,
	 * and light up when people have the correct itens in inventory.
	 */
	public static void tanningInterface(Player player) {
		if (!Constants.CRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		int multiplier = player.getClickId() == 1041 ? 2 : 1;
        player.setStatedInterface("tanning");
		player.getActionSender().sendInterface(14670);
		player.getActionSender().sendString("Soft Leather", 14777);
		if (player.getInventory().getItemAmount(995) >= 1)
			player.getActionSender().sendString(1 * multiplier+" coins", 14785);
		else
			player.getActionSender().sendString(1 * multiplier+" coins", 14785);
		player.getActionSender().sendString("Hard Leather", 14778);
		if (player.getInventory().getItemAmount(995) >= 3)
			player.getActionSender().sendString(3 * multiplier+" coins", 14786);
		else
			player.getActionSender().sendString(3 * multiplier+" coins", 14786);
		player.getActionSender().sendItemOnInterface(14769, 250, LEATHER);
		player.getActionSender().sendItemOnInterface(14770, 250, HARDLEATHER);
		player.getActionSender().sendItemOnInterface(14773, 250, GREEN_DHIDE);
		player.getActionSender().sendItemOnInterface(14774, 250, BLUE_DHIDE);
		player.getActionSender().sendItemOnInterface(14771, 250, SNAKE_HIDE);
		player.getActionSender().sendItemOnInterface(14775, 250, RED_DHIDE);
		player.getActionSender().sendItemOnInterface(14776, 250, BLACK_DHIDE);
		player.getActionSender().sendString("Snakeskin", 14779);
		if (player.getInventory().getItemAmount(995) >= 15)
			player.getActionSender().sendString(15 * multiplier+" coins", 14787);
		else
			player.getActionSender().sendString(15 * multiplier+" coins", 14787);
		player.getActionSender().sendString("", 14780);
		player.getActionSender().sendString("", 14788);

		int[] Line = {14781, 14789, 14783, 14791, 14782, 14790, 14784, 14792};
		String[] HideColor = {"Green d'hide", "Red d'hide", "Blue d'hide", "Black d'hide"};
		String[] HideCost = {20 * multiplier+" coins", 20 * multiplier+" coins", 20 * multiplier+" coins", 20 * multiplier+" coins"};
		int HC = 0;
		for (int i = 0; i < Line.length; i++) {
			if (HC == 0) {
				player.getActionSender().sendString(HideColor[(i / 2)], Line[i]);
				HC = 1;
			} else {
				if (player.getInventory().getItemAmount(995) >= 1)
					player.getActionSender().sendString(HideCost[(i / 2)], Line[i]);
				else
					player.getActionSender().sendString("" + HideCost[(i / 2)], Line[i]);
				HC = 0;
			}
		}

	}

	public static void tan(Player player, int amount, int payment, int deletedItem, int addedItem) {
		int multiplier = player.getClickId() == 1041 ? 2 : 1;
		Item iPayment = new Item(995, payment * amount * multiplier);
		player.getActionSender().removeInterfaces();
		if (!Constants.CRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
        if(player.getStatedInterface() != "tanning") return;
		if (!player.getInventory().getItemContainer().contains(deletedItem)) {
			player.getDialogue().sendStatement("You don't have enough rough hides in your inventory.");
			return;
		}
		if (!player.getInventory().getItemContainer().contains(995)) {
			player.getDialogue().sendStatement("You do not have enough coins.");
			return;
		}
		if (amount > player.getInventory().getItemContainer().getCount(deletedItem)) {
			amount = player.getInventory().getItemContainer().getCount(deletedItem);
		}
		player.getInventory().removeItem(iPayment);
		player.getInventory().removeItem(new Item(deletedItem, amount));
		player.getInventory().addItem(new Item(addedItem, amount));
	}

	public static boolean handleButtonsX(Player player, int buttonId, int amount) {
		switch (buttonId) {
			// soft
			case 57209 :
				tan(player, amount, 1, COWHIDE, LEATHER);
				return true;
			// hard
			case 57210 :
				tan(player, amount, 3, COWHIDE, HARDLEATHER);
				return true;
			// snakeskin
			case 57211 :
				tan(player, amount, 20, SNAKE_HIDE, SNAKESKIN);
				return true;
			// green
			case 57213:
				tan(player, amount, 20, GREEN_HIDE, GREEN_HIDE);
				return true;
			// blue
			case 57214 :
				tan(player, amount, 20, BLUE_HIDE, BLUE_HIDE);
				return true;
			// Red
			case 57215 :
				tan(player, amount, 20, RED_HIDE, RED_HIDE);
				return true;
			// Black
			case 57216 :
				tan(player, amount, 20, BLACK_HIDE, BLACK_HIDE);
				return true;
		}
		return false;
	}

	public static boolean handleButtons(Player player, int buttonId) {
        //System.out.println(buttonId);
		switch (buttonId) {
			// soft
			case 57225 :
				tan(player, 1, 1, COWHIDE, LEATHER);
				return true;
			case 57217 :
				tan(player, 5, 1, COWHIDE, LEATHER);
				return true;
			case 57201 :
				tan(player, player.getInventory().getItemContainer().getCount(COWHIDE), 1, COWHIDE, LEATHER);
				return true;
			// hard
			case 57226 :
				tan(player, 1, 3, COWHIDE, HARDLEATHER);
				return true;
			case 57218 :
				tan(player, 5, 3, COWHIDE, HARDLEATHER);
				return true;
			case 57202 :
				tan(player, player.getInventory().getItemContainer().getCount(COWHIDE), 3, COWHIDE, HARDLEATHER);
				return true;
			// snakeskin
			case 57227 :
				tan(player, 1, 20, SNAKE_HIDE, SNAKESKIN);
				return true;
			case 57219 :
				tan(player, 5, 20, SNAKE_HIDE, SNAKESKIN);
				return true;
			case 57203 :
				tan(player, player.getInventory().getItemContainer().getCount(SNAKE_HIDE), 20, SNAKE_HIDE, SNAKESKIN);
				return true;
			// green
			case 57229 :
				tan(player, 1, 20, GREEN_DHIDE, GREEN_HIDE);
				return true;
			case 57221 :
				tan(player, 5, 20, GREEN_DHIDE, GREEN_HIDE);
				return true;
			case 57205 :
				tan(player, player.getInventory().getItemContainer().getCount(GREEN_DHIDE), 20, GREEN_DHIDE, GREEN_HIDE);
				return true;
			// blue
			case 57230 :
				tan(player, 1, 20, BLUE_DHIDE, BLUE_HIDE);
				return true;
			case 57222 :
				tan(player, 5, 20, BLUE_DHIDE, BLUE_HIDE);
				return true;
			case 57206 :
				tan(player, player.getInventory().getItemContainer().getCount(BLUE_DHIDE), 20, BLUE_DHIDE, BLUE_HIDE);
				return true;
			// Red
			case 57231 :
				tan(player, 1, 20, RED_DHIDE, RED_HIDE);
				return true;
			case 57223 :
				tan(player, 5, 20, RED_DHIDE, RED_HIDE);
				return true;
			case 57207 :
				tan(player, player.getInventory().getItemContainer().getCount(RED_DHIDE), 20, RED_DHIDE, RED_HIDE);
				return true;
			// Black
			case 57232 :
				tan(player, 1, 20, BLACK_DHIDE, BLACK_HIDE);
				return true;
			case 57224 :
				tan(player, 5, 20, BLACK_DHIDE, BLACK_HIDE);
				return true;
			case 57208 :
				tan(player, player.getInventory().getItemContainer().getCount(BLACK_DHIDE), 20, BLACK_DHIDE, BLACK_HIDE);
				return true;
		}
		return false;

	}

}
