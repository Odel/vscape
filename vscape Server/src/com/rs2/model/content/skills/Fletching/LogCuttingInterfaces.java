package com.rs2.model.content.skills.Fletching;

import com.rs2.model.content.skills.Menus;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 30/12/11 Time: 00:14 To change
 * this template use File | Settings | File Templates.
 */
public class LogCuttingInterfaces {

	public static boolean handleItemOnItem(Player player, int itemUsed, int usedWith) {
		if (itemUsed == 946 || usedWith == 946) {
			if (itemUsed == 1511 || usedWith == 1511) {
				Menus.sendSkillMenu(player, "normalCutting");
				return true;
			} else if (itemUsed == 1521 || usedWith == 1521) {
				Menus.sendSkillMenu(player, "oakCutting");
				return true;
			} else if (itemUsed == 2862 || usedWith == 2862) {
				Menus.sendSkillMenu(player, "acheyCutting");
				return true;
			} else if (itemUsed == 1519 || usedWith == 1519) {
				Menus.sendSkillMenu(player, "willowCutting");
				return true;
			} else if (itemUsed == 6333 || usedWith == 6333) {
				Menus.sendSkillMenu(player, "teakCutting");
				return true;
			}  else if (itemUsed == 1517 || usedWith == 1517) {
				Menus.sendSkillMenu(player, "mapleCutting");
				return true;
			}  else if (itemUsed == 6332 || usedWith == 6332) {
				Menus.sendSkillMenu(player, "mahoganyCutting");
				return true;
			} else if (itemUsed == 1515 || usedWith == 1515) {
				Menus.sendSkillMenu(player, "yewCutting");
				return true;
			} else if (itemUsed == 1513 || usedWith == 1513) {
				Menus.sendSkillMenu(player, "magicCutting");
				return true;
			} else if (itemUsed == 771 || usedWith == 771) {
				Menus.sendSkillMenu(player, "dramenBranch");
				return true;
			}
		}
		return false;
	}

}
