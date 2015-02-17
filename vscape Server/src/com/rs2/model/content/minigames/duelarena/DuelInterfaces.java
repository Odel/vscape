package com.rs2.model.content.minigames.duelarena;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;
import com.rs2.util.NameUtil;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/24/12 Time: 1:07 PM To change
 * this template use File | Settings | File Templates.
 */
public class DuelInterfaces {
	private Player player;

	public DuelInterfaces(Player player) {
		this.player = player;
	}

	public int totalDuelConfigs = 0;

	public static final int[] secondScreenRuleLines = {8242, 8243, 8244, 8245, 8246, 8247, 8248, 8249, 8251, 8252, 8253};

	private static final int[] DUELING_CONFIG_IDS = {1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 2097152, 8388608, 16777216, 67108864, 134217728, 268435456};

	public void openDuelInterface() {
		resetDuelRules();
		player.getActionSender().sendInterface(DuelMainData.DUEL_ARENA_INTERFACE_1);
		player.setStatedInterface("duel");
		player.getActionSender().sendString("Dueling with:  " + player.getDuelMainData().getOpponent().getUsername() + "  Opponent's combat level: " + NameUtil.GetPlayerLevelColour(player.getCombatLevel(), player.getDuelMainData().getOpponent().getCombatLevel()), 6671);
		player.getActionSender().sendUpdateItems(13824, player.getEquipment().getItemContainer().toArray());
		sendStakedItems();
		updateAcceptString();
	}

	public void openDuelConfirmInterface() {
		player.getActionSender().sendInterface(DuelMainData.DUEL_ARENA_INTERFACE_2);
		player.setStatedInterface("duel2");
		player.getDuelInteraction().setAccepted(false);
		clearSecondScreen();
		sendStakedItemText();
		if (player.getDuelMainData().getRulesWorking().size() == 0)
			player.getActionSender().sendString("Everything will be allowed!", secondScreenRuleLines[0]);
		else {
			for (int i = 0; i < player.getDuelMainData().getRulesWorking().size(); i++) {
				player.getActionSender().sendString(player.getDuelMainData().getRulesWorking().get(i), secondScreenRuleLines[i]);
			}
		}
		updateAcceptString();
	}

	public void sendStakedItemText() {
		String itemLine = player.getDuelMainData().getItemStaked().size() <= 0 ? "Absolutely nothing!" : "";
		for (Item item : DuelMainData.listIntoArray(player.getDuelMainData().getItemStaked())) {
			if (item == null)
				continue;
			if (item.getDefinition().isStackable() || item.getDefinition().isNoted()) {
				itemLine += item.getDefinition().getName() + " x @cya@" + Misc.format(item.getCount()) + "\\n";
			} else {
				itemLine += item.getDefinition().getName() + "\\n";
			}
		}
		player.getActionSender().sendString(itemLine, 6516);
		itemLine = player.getDuelMainData().getOpponent().getDuelMainData().getItemStaked().size() <= 0 ? "Absolutely nothing!" : "";
		for (Item item : DuelMainData.listIntoArray(player.getDuelMainData().getOpponent().getDuelMainData().getItemStaked())) {
			if (item == null)
				continue;
			if (item.getDefinition().isStackable() || item.getDefinition().isNoted()) {
				itemLine += item.getDefinition().getName() + " x @cya@" + Misc.format(item.getCount()) + "\\n";
			} else {
				itemLine += item.getDefinition().getName() + "\\n";
			}
		}
		player.getActionSender().sendString(itemLine, 6517);
	}

	public void clearSecondScreen() {
		player.getActionSender().sendString("Hitpoints will be restored.", 8250);
		player.getActionSender().sendString("Boosted stats will be restored.", 8238);
		for (int i = 11; i < player.getDuelMainData().getRuleStates().length; i++)
			if (player.getDuelMainData().getRuleStates()[i]) {
				player.getActionSender().sendString("Some worn items will be taken off", 8239);
				return;
			}
		player.getActionSender().sendString("", 8240);
		player.getActionSender().sendString("", 8241);
		for (int line : secondScreenRuleLines)
			player.getActionSender().sendString("", line);
	}

	public void updateAcceptString() {
		if (player.getDuelMainData().getOpponent() == null || !player.getDuelMainData().getOpponent().isLoggedIn() || player.getDuelMainData().getOpponent().getDuelInteraction() == null){
			player.getDuelInteraction().endDuelInteraction(true);
            return;
        }
		if (player.getStatedInterface() == "duel") {
			if (player.getDuelMainData().getOpponent().getDuelInteraction().isAccepted())
				player.getActionSender().sendString("Other player accepted.", 6684);
			else if (player.getDuelInteraction().isAccepted())
				player.getActionSender().sendString("Waiting for other player...", 6684);
			else
				player.getActionSender().sendString("", 6684);
		} else if (player.getStatedInterface() == "duel2") {
			if (player.getDuelMainData().getOpponent().getDuelInteraction().isAccepted())
				player.getActionSender().sendString("Other player accepted.", 6571);
			else if (player.getDuelInteraction().isAccepted())
				player.getActionSender().sendString("Waiting for other player...", 6571);
			else
				player.getActionSender().sendString("", 6571);
		}
	}

	public int countRemoveItems() {
		int count = 0;
		for (Item item : player.getDuelMainData().getItemToRemove()) {
			if (item.getId() > 0) {
				count++;
			}
		}
		return count;
	}

	public boolean canStakeItems() {
		Player opponent = player.getDuelMainData().getOpponent();
		int itemCount = player.getDuelMainData().getItemStaked().size() + opponent.getDuelMainData().getItemStaked().size() + player.getDuelInterfaces().countRemoveItems();
		System.out.println(itemCount+" "+player.getInventory().getItemContainer().freeSlots());
		if (itemCount > player.getInventory().getItemContainer().freeSlots()) {
			player.getActionSender().sendMessage("You or your opponent doesn't have enough spaces for that.");
			return false;
		}
		itemCount = opponent.getDuelMainData().getItemStaked().size() + player.getDuelMainData().getItemStaked().size() + opponent.getDuelInterfaces().countRemoveItems();
		if (itemCount > opponent.getInventory().getItemContainer().freeSlots()) {
			player.getActionSender().sendMessage("You or your opponent doesn't have enough spaces for that.");
			return false;
		}
		return true;
	}

	public void sendStakedItems() {
		if (player.getDuelMainData().getOpponent() == null || !player.getDuelMainData().getOpponent().isLoggedIn()){
			player.getDuelInteraction().endDuelInteraction(true);
            return;
        }
		player.getActionSender().sendInterface(6575, 3321);
		player.getActionSender().sendUpdateItems(3322, player.getInventory().getItemContainer().toArray());
		player.getActionSender().sendUpdateItems(6669, DuelMainData.listIntoArray(player.getDuelMainData().getItemStaked()));
		player.getActionSender().sendUpdateItems(6670, DuelMainData.listIntoArray(player.getDuelMainData().getOpponent().getDuelMainData().getItemStaked()));

	}

	public void activateRule(int ruleId, String string) {
		player.getDuelInteraction().setAccepted(false);
		if (player.getDuelMainData().getRuleStates()[ruleId]) {
			totalDuelConfigs -= DUELING_CONFIG_IDS[ruleId];
			player.getDuelMainData().getRuleStates()[ruleId] = false;
			if (string != null)
				player.getDuelMainData().getRulesWorking().remove(string);
		} else {
			totalDuelConfigs += DUELING_CONFIG_IDS[ruleId];
			player.getDuelMainData().getRuleStates()[ruleId] = true;
			if (string != null)
				player.getDuelMainData().getRulesWorking().add(string);
		}
		player.getActionSender().sendConfig(286, totalDuelConfigs);
		updateAcceptString();

	}

public void resetDuelRules() {
		/*
		 * Resets the actual buttons first.
		 */
		for (int i = 0; i < player.getDuelMainData().getRuleStates().length; i++) {
			if (player.getDuelMainData().getRuleStates()[i]) {
				totalDuelConfigs -= DUELING_CONFIG_IDS[i];
				player.getDuelMainData().getRuleStates()[i] = false;
			}
		}
		player.getActionSender().sendConfig(286, totalDuelConfigs);
		/*
		 * Then we reset the serversided configs for the buttons.
		 */
		for (int i = 0; i < player.getDuelMainData().getRuleStates().length; i++) {
			player.getDuelMainData().getRuleStates()[i] = false;
		}
	}
}
