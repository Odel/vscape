package com.rs2.model.content.minigames.barrows;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class Barrows {

	public static final int[][] barrowsBrothers = {{6823, 2030}, {6772, 2029}, {6822, 2028}, {6773, 2027}, {6771, 2026}, {6821, 2025}};

	public static final int[] Items = {4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 1149, 165, 117, 141, 129, 385};
	public static final int[][] Stackables = {{4740, 5}, {558, 25}, {562, 11}, {560, 5}, {565, 2}, {995, 55}};

	public static boolean barrowsObject(Player player, int objectId) {
		switch (objectId) {
			case 10284 : // chest
				if (player.getKillCount() < 1) {
					player.getActionSender().sendMessage("You search the chest but don't find anything.");
					return true;
				}
				for (int x = 0; x < barrowsBrothers.length; x++) {
					if (x == player.getRandomGrave()) {
						if (NpcLoader.checkSpawn(player, barrowsBrothers[x][1])) {
							player.getActionSender().sendMessage("You must kill the the brother before searching this.");
							player.getActionSender().sendMessage("Log out and in if you killed him, but it does not work.");
							return true;
						}
						if (!player.getBarrowsNpcDead()[x]) {
							NpcLoader.spawnNpc(player, new Npc(barrowsBrothers[x][1]), true, true);
							return true;
						}
						break;
					}
				}
				getReward(player);
				return true;
			case 6823 :
			case 6772 :
			case 6821 :
			case 6771 :
			case 6773 :
			case 6822 :
				for (int x = 0; x < barrowsBrothers.length; x++) {
					if (objectId == barrowsBrothers[x][0]) {
						if (x == player.getRandomGrave()) {
							Dialogues.startDialogue(player, 10001);
							return true;
						}
						if (NpcLoader.checkSpawn(player, barrowsBrothers[x][1])) {
							player.getActionSender().sendMessage("You must kill the the brother before searching this.");
							player.getActionSender().sendMessage("Log out and in if you killed him, but it does not work.");
							return true;
						}
						if (player.getBarrowsNpcDead()[x]) {
							player.getActionSender().sendMessage("You have already searched this sarcophagus.");
							return true;
						}
						NpcLoader.spawnNpc(player, new Npc(barrowsBrothers[x][1]), true, true);
						if (x != player.getRandomGrave()) {
							player.getActionSender().sendMessage("You don't find anything.");
						}
						return true;
					}
				}
				return true;
			case 6707 : // verac stairs
				player.teleport(new Position(3556, 3298, 0));
				return true;
			case 6706 : // torag stairs
				player.teleport(new Position(3553, 3283, 0));
				break;
			case 6705 : // karil stairs
				player.teleport(new Position(3565, 3276, 0));
				return true;
			case 6704 : // guthan stairs
				player.teleport(new Position(3578, 3284, 0));
				return true;
			case 6703 : // dharok stairs
				player.teleport(new Position(3574, 3298, 0));
				return true;
			case 6702 : // ahrim stairs
				player.teleport(new Position(3565, 3290, 0));
				return true;
		}
		return false;
	}

	public static boolean digCrypt(Player player) {
		if (player.Area(3553, 3561, 3294, 3301)) {
			player.teleport(new Position(3578, 9706, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		} else if (player.Area(3550, 3557, 3278, 3287)) {
			player.teleport(new Position(3568, 9683, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		} else if (player.Area(3561, 3568, 3285, 3292)) {
			player.teleport(new Position(3557, 9703, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		} else if (player.Area(3570, 3579, 3293, 3302)) {
			player.teleport(new Position(3556, 9718, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		} else if (player.Area(3571, 3582, 3278, 3285)) {
			player.teleport(new Position(3534, 9704, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		} else if (player.Area(3562, 3569, 3273, 3279)) {
			player.teleport(new Position(3546, 9684, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
	        player.getActionSender().sendMapState(2);
			return true;
		}
		return false;
	}

	public static void getReward(Player player) {
		final int number = Misc.randomMinusOne(Stackables.length);
		final int rune = Stackables[number][0];
		final int amount = Stackables[number][1];
		final int reward = Items[Misc.randomMinusOne(Items.length)];
		int kills = brotherKillCount(player);
		if (kills < 1) {
			player.getActionSender().sendMessage("You can only loot the chest after killing at least 1 brother.");
			return;
		}
		boolean getBarrows = Misc.random(612 - (kills * 50)) == 0;
		if (getBarrows) {
			if (player.getInventory().getItemContainer().freeSlots() == 1) {
				if (!player.getInventory().playerHasItem(rune)) {
					player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
					return;
				}
			} else if (player.getInventory().getItemContainer().freeSlots() < 1) {
				player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
				return;
			}
			player.getInventory().addItem(new Item(rune, Misc.random(amount * kills) + 1));
			player.getInventory().addItem(new Item(reward, 1));
		} else {
			final int number2 = Misc.randomMinusOne(Stackables.length);
			final int rune2 = Stackables[number2][0];
			final int amount2 = Stackables[number2][1];
			if (player.getInventory().getItemContainer().freeSlots() < 1) {
				if (!player.getInventory().playerHasItem(rune) || !player.getInventory().playerHasItem(rune2)) {
					player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
					return;
				}
			}
			if (player.getInventory().getItemContainer().freeSlots() == 1) {
				if (!player.getInventory().playerHasItem(rune) && !player.getInventory().playerHasItem(rune2)) {
					player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
					return;
				}
			}
			player.getInventory().addItem(new Item(rune, Misc.random(amount * kills) + 1));
			player.getInventory().addItem(new Item(rune2, Misc.random(amount2 * kills) + 1));
		}
		player.getUpdateFlags().sendAnimation(714);
		player.getUpdateFlags().sendHighGraphic(301);
		player.getTeleportation().teleport(3565, 3298, 0, true);
		player.getActionSender().sendMessage("You grab the loot and teleport away.");
		resetBarrows(player);
		return;
	}

	private static int brotherKillCount(Player player) {
		int brotherKillCount = 0;
		for (boolean kill : player.getBarrowsNpcDead()) {
			if (kill) {
				brotherKillCount++;
			}
		}
		return brotherKillCount;
	}

	public static void resetBarrows(Player player) {
		for (int x = 0; x < Barrows.barrowsBrothers.length; x++) {
			player.setBarrowsNpcDead(x, false);
		}
		player.setKillCount(0);
		player.setRandomGrave(Misc.random(5));
	}

	public static void handleDeath(Player player, Npc npc) {
		for (int x = 0; x < barrowsBrothers.length; x++) {
			if (npc.getNpcId() == barrowsBrothers[x][1]) {
				player.setKillCount(player.getKillCount() + 1);
				player.getActionSender().sendString("Kill count: " + player.getKillCount(), 4536);
				player.setBarrowsNpcDead(x, true);
				break;
			}
		}
	}
}
