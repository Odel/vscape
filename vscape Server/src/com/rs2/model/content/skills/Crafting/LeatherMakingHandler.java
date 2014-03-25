package com.rs2.model.content.skills.Crafting;

import com.rs2.Constants;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl.BlackLeather;
import com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl.BlueLeather;
import com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl.GreenLeather;
import com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl.HardLeather;
import com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl.NormalLeather;
import com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl.RedLeather;
import com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl.SnakeskinOne;
import com.rs2.model.content.skills.Crafting.LeatherMakingAction.impl.SnakeskinTwo;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 19:03 To change
 * this template use File | Settings | File Templates.
 */
public class LeatherMakingHandler {
	public static final int THREAD = 1734;
	public static final int NEEDLE = 1733;
	public static final int KNIFE = 946;
	public static final int LEATHER_MAKING = 1249;
	public static final int STUDS = 2370;

	public static boolean handleItemOnItem(Player player, int itemUsed, int usedWith, int usedSlot, int withSlot) {
		int slot = (itemUsed == STUDS ? withSlot : usedSlot);
		/* studded things */
		if (itemUsed == STUDS || usedWith == STUDS) {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (itemUsed == 1131 || usedWith == 1131) {
				player.getActionSender().sendMessage("You attach the steel studs to the hard leather body");
				if (player.getInventory().removeItemSlot(new Item(STUDS), slot)) {
					player.getInventory().addItemToSlot(new Item(1133), slot);
				} else if (player.getInventory().removeItem(new Item(STUDS))) {
					player.getInventory().addItem(new Item(1133));
				}
				return true;
			} else if (itemUsed == 1095 || usedWith == 1095) {
				player.getActionSender().sendMessage("You attach the steel studs to the leather chaps");
				if (player.getInventory().removeItemSlot(new Item(STUDS), slot)) {
					player.getInventory().addItemToSlot(new Item(1097), slot);
				} else if (player.getInventory().removeItem(new Item(STUDS))) {
					player.getInventory().addItem(new Item(1097));
				}
				return true;
			}
		}
		if (itemUsed == NEEDLE || usedWith == NEEDLE) {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (itemUsed == 1741 || usedWith == 1741) {// normal leather
				Menus.sendSkillMenu(player, "normalLeather");
				return true;
			} else if (itemUsed == 1743 || usedWith == 1743) {// hard
				Menus.sendSkillMenu(player, "hardLeather");
				return true;
			} else if (itemUsed == 1745 || usedWith == 1745) {// Green
				Menus.sendSkillMenu(player, "greenLeather");
				return true;
			} else if (itemUsed == 2505 || usedWith == 2505) {// blue
				Menus.sendSkillMenu(player, "blueLeather");
				return true;
			} else if (itemUsed == 2507 || usedWith == 2507) {// red
				Menus.sendSkillMenu(player, "redLeather");
				return true;
			} else if (itemUsed == 2509 || usedWith == 2509) {// black
				Menus.sendSkillMenu(player, "blackLeather");
				return true;
			} else if (itemUsed == 6287 || usedWith == 6287) {// snakeskin1
				Menus.sendSkillMenu(player, "snakeskin1");
				return true;
			} else if (itemUsed == 6289 || usedWith == 6289) {// snakeskin2
				Menus.sendSkillMenu(player, "snakeskin2");
				return true;
			}
		}
        return false;
	}

	public static boolean handleButtons(Player player, int buttonId, int amount) {
		if (player.getStatedInterface() == "normalLeather") {
			if (NormalLeather.create(player, buttonId, amount) != null) {
				NormalLeather.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "greenLeather") {
			if (GreenLeather.create(player, buttonId, amount) != null) {
				GreenLeather.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "blueLeather") {
			if (BlueLeather.create(player, buttonId, amount) != null) {
				BlueLeather.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "redLeather") {
			if (RedLeather.create(player, buttonId, amount) != null) {
				RedLeather.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "blackLeather") {
			if (BlackLeather.create(player, buttonId, amount) != null) {
				BlackLeather.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "hardLeather") {
			if (HardLeather.create(player, buttonId, amount) != null) {
				HardLeather.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "snakeskin1") {
			if (SnakeskinOne.create(player, buttonId, amount) != null) {
				SnakeskinOne.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "snakeskin2") {
			if (SnakeskinTwo.create(player, buttonId, amount) != null) {
				SnakeskinTwo.create(player, buttonId, amount).makeLeatherAction();
				return true;
			}
		}
		return false;
	}

}
