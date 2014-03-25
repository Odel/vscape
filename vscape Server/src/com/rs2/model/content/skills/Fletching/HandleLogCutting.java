package com.rs2.model.content.skills.Fletching;

import com.rs2.model.content.skills.Fletching.LogCuttingAction.impl.AcheyCut;
import com.rs2.model.content.skills.Fletching.LogCuttingAction.impl.LogCut;
import com.rs2.model.content.skills.Fletching.LogCuttingAction.impl.MagicCut;
import com.rs2.model.content.skills.Fletching.LogCuttingAction.impl.MapleCut;
import com.rs2.model.content.skills.Fletching.LogCuttingAction.impl.OakCut;
import com.rs2.model.content.skills.Fletching.LogCuttingAction.impl.WillowCut;
import com.rs2.model.content.skills.Fletching.LogCuttingAction.impl.YewCut;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 30/12/11 Time: 00:24 To change
 * this template use File | Settings | File Templates.
 */
public class HandleLogCutting {

	public static boolean handleButtons(Player player, int buttonId, int amount) {
		if (player.getStatedInterface() == "normalCutting") {
			if (LogCut.create(player, buttonId, amount) != null) {
				LogCut.create(player, buttonId, amount).logCuttingAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "oakCutting") {
			if (OakCut.create(player, buttonId, amount) != null) {
				OakCut.create(player, buttonId, amount).logCuttingAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "acheyCutting") {
			if (AcheyCut.create(player, buttonId, amount) != null) {
				AcheyCut.create(player, buttonId, amount).logCuttingAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "willowCutting") {
			if (WillowCut.create(player, buttonId, amount) != null) {
				WillowCut.create(player, buttonId, amount).logCuttingAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "mapleCutting") {
			if (MapleCut.create(player, buttonId, amount) != null) {
				MapleCut.create(player, buttonId, amount).logCuttingAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "yewCutting") {
			if (YewCut.create(player, buttonId, amount) != null) {
				YewCut.create(player, buttonId, amount).logCuttingAction();
				return true;
			}
		}
		if (player.getStatedInterface() == "magicCutting") {
			if (MagicCut.create(player, buttonId, amount) != null) {
				MagicCut.create(player, buttonId, amount).logCuttingAction();
				return true;
			}
		}
		return false;
	}
}
