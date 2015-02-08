package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.model.players.Player.TradeStage;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.util.LogHandler;
import com.rs2.util.NameUtil;
import com.rs2.util.PlayerSave;

public class TradeManager {

	public static void refresh(Player player, Player otherPlayer) {
		player.getActionSender().sendUpdateItems(3322, player.getInventory().getItemContainer().toArray());
		otherPlayer.getActionSender().sendUpdateItems(3322, otherPlayer.getInventory().getItemContainer().toArray());
		player.getActionSender().sendUpdateItems(3415, player.getTrade().toArray());
		player.getActionSender().sendUpdateItems(3416, otherPlayer.getTrade().toArray());
		otherPlayer.getActionSender().sendUpdateItems(3415, otherPlayer.getTrade().toArray());
		otherPlayer.getActionSender().sendUpdateItems(3416, player.getTrade().toArray());
		player.getActionSender().sendString("Trading With: " + otherPlayer.getUsername(), 3417);
		otherPlayer.getActionSender().sendString("Trading With: " + player.getUsername(), 3417);
	}

	public static void handleTradeRequest(Player player, Player otherPlayer) {
		if (!Constants.ADMINS_CAN_INTERACT && (player.getStaffRights() >= 2 || otherPlayer.getStaffRights() >= 2)) {
			player.getActionSender().sendMessage("You are not allowed to trade this player.", true);
			return;
	    }
		if (otherPlayer.getLastPersonTraded() != player) {
			player.getActionSender().sendMessage("Sending trade offer...", true);
			otherPlayer.getActionSender().sendMessage("" + NameUtil.uppercaseFirstLetter(player.getUsername()) + ":tradereq:");
			player.setTradeStage(TradeStage.SEND_REQUEST);
			player.setLastPersonTraded(otherPlayer);
		} else {
			player.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
			otherPlayer.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
			sendTrade(player, otherPlayer);
			player.setLastPersonTraded(null);
			otherPlayer.setLastPersonTraded(null);
		}
	}

	private static void sendTrade(Player player, Player otherPlayer) {
		player.getTrade().clear();
		otherPlayer.getTrade().clear();
		player.getActionSender().sendInterface(3323, 3321);
		otherPlayer.getActionSender().sendInterface(3323, 3321);
		player.getActionSender().sendString("Trading With: " + otherPlayer.getUsername() + " has " + otherPlayer.getInventory().getItemContainer().freeSlots() + " free inventory slots.", 3417);
		otherPlayer.getActionSender().sendString("Trading With: " + player.getUsername() + " has " + player.getInventory().getItemContainer().freeSlots() + " free inventory slots.", 3417);
		player.getActionSender().sendString("", 3431);
		otherPlayer.getActionSender().sendString("", 3431);
		refresh(player, otherPlayer);
		player.setTradingEntity(otherPlayer);
        otherPlayer.setTradingEntity(player);
	}

	public static void declineTrade(Player player) {
		if (player.getTradingEntity() == null) {
			return;
		}
		Player otherPlayer = (Player) player.getTradingEntity();
		otherPlayer.getActionSender().sendMessage("Other player has declined the trade.", true);
		player.getActionSender().removeInterfaces();
        otherPlayer.getActionSender().removeInterfaces();
		player.setTradeStage(TradeStage.WAITING);
        otherPlayer.setTradeStage(TradeStage.WAITING);
        giveBackItems(otherPlayer);
        giveBackItems(player);
		player.setTradingEntity(null);
        otherPlayer.setTradingEntity(null);
	}

	public static void giveBackItems(Player player) {
		for (int i = 0; i < Inventory.SIZE; i++) {
			if (player.getTrade().get(i) != null) {
				Item item = player.getTrade().get(i);
				if (item != null) {
					player.getTrade().remove(item);
					player.getInventory().addItem(item);
				}
			}
		}
        player.getTrade().clear();
	}

	public static void offerItem(Player player, int slot, int tradeItem, int amount) {
		Player otherPlayer = (Player) player.getTradingEntity();
		if (player.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			return;
		}
		if (tradeItem == -1 || otherPlayer == null) {
			return;
		}
		Item inv = player.getInventory().getItemContainer().get(slot);
		int invAmount = player.getInventory().getItemContainer().getCount(tradeItem);
		if (inv == null || inv.getId() != tradeItem || !inv.validItem()) {
			return;
		}
		if (inv.getId() <= 0 || !inv.validItem() || amount < 1) {
			return;
		}
		if (new Item(tradeItem).getDefinition().isUntradable() || tradeItem == 8851) {
			player.getActionSender().sendMessage("You cannot trade that item.", true);
			return;
		}
		if (invAmount > amount) {
			invAmount = amount;
		}
		if (inv.getDefinition().isStackable()) {
			if (!player.getInventory().removeItemSlot(new Item(tradeItem, invAmount), slot)) {
				player.getInventory().removeItem(new Item(tradeItem, invAmount));
			}
		} else {
			for (int i = 0; i < invAmount; i++) {
				player.getInventory().removeItem(new Item(tradeItem, 1));
			}
		}
		int tradeAmount = player.getTrade().getCount(tradeItem);
		if (tradeAmount > 0 && inv.getDefinition().isStackable()) {
			player.getTrade().set(player.getTrade().getSlotById(inv.getId()), new Item(tradeItem, tradeAmount + invAmount));
		} else {
			player.getTrade().add(new Item(inv.getId(), invAmount));
		}
		refresh(player, otherPlayer);
		player.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		otherPlayer.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		player.getActionSender().sendString("", 3431);
		otherPlayer.getActionSender().sendString("", 3431);
	}

	public static void removeTradeItem(Player player, int slot, int tradeItem, int amount) {
		Player otherPlayer = (Player) player.getTradingEntity();
		if (player.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			return;
		}
		if (tradeItem == -1 || otherPlayer == null) {
			return;
		}
		Item itemOnScreen = player.getTrade().get(slot);
		int itemOnScreenAmount = player.getTrade().getCount(tradeItem);
		if (itemOnScreen == null || itemOnScreen.getId() != tradeItem || amount < 1) {
			return;
		}
		if (itemOnScreenAmount > amount) {
			itemOnScreenAmount = amount;
		}
		int remove = player.getTrade().remove(new Item(tradeItem, itemOnScreenAmount), slot);
		player.getInventory().addItem(new Item(itemOnScreen.getId(), remove));
		refresh(player, otherPlayer);
		player.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		otherPlayer.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		player.getActionSender().sendString("", 3431);
		otherPlayer.getActionSender().sendString("", 3431);
	}

	public static void acceptStageOne(Player player) {
		Player otherPlayer = (Player) player.getTradingEntity();
		player.setTradeStage(TradeStage.ACCEPT);
		if (!otherPlayer.getTradeStage().equals(TradeStage.ACCEPT)) {
			player.getActionSender().sendString("Waiting for other player...", 3431);
			otherPlayer.getActionSender().sendString("Other player accepted.", 3431);
		} else {
			int tradeItems = 0;
			for (Item item : player.getTrade().getItems()) {
				if (item != null && (!item.getDefinition().isStackable() || !otherPlayer.getInventory().playerHasItem(item.getId()))) {
					tradeItems++;
				}
			}
			if (otherPlayer.getInventory().getItemContainer().freeSlots() < tradeItems) {
				player.getActionSender().sendString("Other player doesn't have enough inventory space for this trade.", 3431);
				otherPlayer.getActionSender().sendString("You don't have enough inventory space for this trade.", 3431);
				return;
			}
			tradeItems = 0;
			for (Item item : otherPlayer.getTrade().getItems()) {
				if (item != null && (!item.getDefinition().isStackable() || !player.getInventory().playerHasItem(item.getId()))) {
					tradeItems++;
				}
			}
			if (player.getInventory().getItemContainer().freeSlots() < tradeItems) {
				otherPlayer.getActionSender().sendString("Other player doesn't have enough inventory space for this trade.", 3431);
				player.getActionSender().sendString("You don't have enough inventory space for this trade.", 3431);
				return;
			}
			refresh(player, otherPlayer);
			player.getActionSender().sendInterface(3443, 3213);
			otherPlayer.getActionSender().sendInterface(3443, 3213);
			player.setTradeStage(TradeStage.SECOND_TRADE_WINDOW);
			otherPlayer.setTradeStage(TradeStage.SECOND_TRADE_WINDOW);
			player.getActionSender().sendString("Are you sure you want to accept this trade?", 3535);
			otherPlayer.getActionSender().sendString("Are you sure you want to accept this trade?", 3535);
			sendSecondScreen(player);
			sendSecondScreen(otherPlayer);
		}
	}

	public static void acceptStageTwo(Player player) {
		if (!player.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			return;
		}
		Player otherPlayer = (Player) player.getTradingEntity();
		player.setTradeStage(TradeStage.ACCEPT);
		if (!otherPlayer.getTradeStage().equals(TradeStage.ACCEPT)) {
			player.getActionSender().sendString("Waiting for other player...", 3535);
			otherPlayer.getActionSender().sendString("Other player accepted.", 3535);
		} else {
			for (int i = 0; i < Inventory.SIZE; i++) {
				Item newItems = player.getTrade().get(i);
				if (newItems == null) {
					continue;
				}
				player.getTrade().remove(newItems);
				otherPlayer.getInventory().addItem(newItems);
				LogHandler.logTrade(player, otherPlayer, newItems);
			}
			for (int i = 0; i < Inventory.SIZE; i++) {
				Item newItems = otherPlayer.getTrade().get(i);
				if (newItems == null) {
					continue;
				}
				otherPlayer.getTrade().remove(newItems);
				player.getInventory().addItem(newItems);
				LogHandler.logTrade(otherPlayer, player, newItems);
				if(newItems.getId() == 769 && player.getQuestStage(13) == 10)
				    player.setQuestStage(13, 11);
			}
			player.setTradeStage(TradeStage.WAITING);
			otherPlayer.setTradeStage(TradeStage.WAITING);
			player.getActionSender().sendMessage("You accept the trade.", true);
			otherPlayer.getActionSender().sendMessage("You accept the trade.", true);
			player.getActionSender().removeInterfaces();
			otherPlayer.getActionSender().removeInterfaces();
			player.setTradingEntity(null);
	        otherPlayer.setTradingEntity(null);
	        PlayerSave.save(player);
	        PlayerSave.save(otherPlayer);
		}
	}

	private static void sendSecondScreen(Player player) {
		Player otherPlayer = (Player) player.getTradingEntity();
		StringBuilder trade = new StringBuilder();
		boolean empty = true;
		for (int i = 0; i < Inventory.SIZE; i++) {
			Item item = player.getTrade().get(i);
			String prefix = "";
			if (item != null) {
				empty = false;
				if (item.getCount() >= 1000 && item.getCount() < 1000000) {
					prefix = "@cya@" + item.getCount() / 1000 + "K @whi@(" + item.getCount() + ")";
				} else if (item.getCount() >= 1000000) {
					prefix = "@gre@" + item.getCount() / 1000000 + " million @whi@(" + item.getCount() + ")";
				} else {
					prefix = "" + item.getCount();
				}
				trade.append(item.getDefinition().getName());
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if (empty) {
			trade.append("Absolutely nothing!");
		}
		player.getActionSender().sendString(trade.toString(), 3557);
		trade = new StringBuilder();
		empty = true;
		if (otherPlayer == null) {
			return;
		}
		for (int i = 0; i < Inventory.SIZE; i++) {
			Item item = otherPlayer.getTrade().get(i);
			String prefix = "";
			if (item != null) {
				empty = false;
				if (item.getCount() >= 100 && item.getCount() < 1000000) {
					prefix = "@cya@" + item.getCount() / 1000 + "K @whi@(" + item.getCount() + ")";
				} else if (item.getCount() >= 1000000) {
					prefix = "@gre@" + item.getCount() / 1000000 + " million @whi@(" + item.getCount() + ")";
				} else {
					prefix = "" + item.getCount();
				}
				trade.append(item.getDefinition().getName());
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if (empty) {
			trade.append("Absolutely nothing!");
		}
		player.getActionSender().sendString(trade.toString(), 3558);
	}

}
