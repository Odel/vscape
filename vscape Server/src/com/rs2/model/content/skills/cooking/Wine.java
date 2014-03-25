package com.rs2.model.content.skills.cooking;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 08/04/12 Time: 12:52 To change
 * this template use File | Settings | File Templates.
 */
public class Wine {

	private Player player;

	public Wine(Player player) {
		this.player = player;
	}

	public void fermentWineInInv(int index) {
		if (!player.getInventory().removeItemSlot(new Item(1995), index)) {
			player.getInventory().removeItem(new Item(1995));
		}
		if (Cooking.cookedSuccessfully(player, 35, 112, 112)) {
			player.getSkill().addExp(Skill.COOKING, 110);
			player.getInventory().addItem(new Item(1993));
		} else {
			player.getInventory().addItem(new Item(1991));
		}

	}

	public void fermentWineInBank(int index) {
		player.getBank().remove(new Item(1995), index);
		if (Cooking.cookedSuccessfully(player, 35, 112, 112)) {
			player.getSkill().addExp(Skill.COOKING, 110);
			player.getInventory().addItem(new Item(1993));
		} else {
			player.getInventory().addItem(new Item(1991));
		}
	}
}
