package com.rs2.model.content.treasuretrails;

import java.util.ArrayList;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.util.RingEffect;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 04/01/12 Time: 17:17 To change
 * this template use File | Settings | File Templates.
 */
public class ClueScroll {

	/*
	 * level 3 scroll : I expect you to die! find clue : You've found another
	 * clue!- You've been given a casket! - You found a casket! 6960 clue reward
	 */

	/* the main clue scroll hint interface */

	public static final int CLUE_SCROLL_INTERFACE = 6965;

	public static final int CASKET_LV1 = 2724;
	public static final int CASKET_LV2 = 2726;
	public static final int CASKET_LV3 = 2728;

	public static final int REWARD_CASKET_LV1 = 2717;
	public static final int REWARD_CASKET_LV2 = 2714;
	public static final int REWARD_CASKET_LV3 = 2715;

	public static final int CLUE_ITEM = 2677;

	/* the puzzle class constants */

	public static final int PUZZLE_INTERFACE = 6976;

	public static final int PUZZLE_INTERFACE_CONTAINER = 6980;

	public static final int PUZZLE_INTERFACE_DEFAULT_CONTAINER = 6985;

	public static final int CASTLE_PUZZLE = 2800;
	public static final int TREE_PUZZLE = 3565;
	public static final int TROLL_PUZZLE = 3571;

	public static final int PUZZLE_LENGTH = 25;

	public static final int[] firstPuzzle = {2749, 2750, 2751, 2752, 2753, 2754, 2755, 2756, 2757, 2758, 2759, 2760, 2761, 2762, 2763, 2764, 2765, 2766, 2767, 2768, 2769, 2770, 2771, 2772, -1};

	public static final int[] secondPuzzle = {3619, 3620, 3621, 3622, 3623, 3624, 3625, 3626, 3627, 3628, 3629, 3630, 3631, 3632, 3633, 3634, 3635, 3636, 3637, 3638, 3639, 3640, 3641, 3642, -1};

	public static final int[] thirdPuzzle = {3643, 3644, 3645, 3646, 3647, 3648, 3649, 3650, 3651, 3652, 3653, 3654, 3655, 3656, 3657, 3658, 3659, 3660, 3661, 3662, 3663, 3664, 3665, 3666, -1};

	public static String[] levelOneClueNpc = {"Man", "Woman", "Goblin", "Mugger", "Barbarian", "Farmer", "Al-Kharid", "Thug", "Rock Crabs", "Rogue", "Thief", "H.A.M", "Banshees", "Cave Slime", "Afflicted", "Borrakar", "Freidar", "Freygerd", "Inga", "Jennella", "Lensa", "Lanzig"};

	public static String[] levelTwoClueNpc = {"Guard", "Tribesman", "Bandit Camp Human", "Cockatrice", "Abyssal Leech", "Pyrefiend", "Harpie Bug Swarm", "Black Guard", "Rellekka Warrior", "Market Guard", "Jogre", "Ice Warrior", "Abyssal Guardian", "Paladin", "Vampire", "Dagannoth", "Skeleton", "Abyssal Walker", "Dagannoth", "Wallasalki", "Mummy", "Giant Rock Crab", "Werewolf", "Hill Giant",
						    "Alexis", "Boris", "Eduard", "Galina", "Georgy", "Imre", "Irina", "Joseph", "Ksenia", "Liliya", "Lev", "Milla", "Nikita", "Nikolai", "Sofiya", "Svetlana", "Vera", "Yadviga", "Yuri", "Zoja", "Moss Giant"};

	public static String[] levelThreeClueNpc = {"Greater Demon", "Elf Warrior", "Tyras Guard", "Hellhound", "Dragon", "Dagannoth", "Turoth", "Jelly", "Aberrant Specter", "Gargoyle", "Nechryael", "Abyssal Demon", "Black Demon", "Aberrant Spectre"};

	// todo torn page make into mage books + firelighters + reward items to reward

	public static int[] mainJunk = {554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 374, 380, 362, 1379, 1381, 1383, 1385, 1387, 1065, 1099, 1135, 1097, 1169, 841, 843, 845, 847, 849};
	public static int[] junkItem1 = {1367, 1217, 1179, 1151, 1107, 1077, 1269, 1089, 1125, 1165, 1195, 1283, 1297, 1313, 1327, 1341, 1367, 1426, 334, 330, 851, 853, 855, 857, 859, 4821, 1765};
	public static int[] junkItem2 = {1430, 1371, 1345, 1331, 1317, 1301, 1287, 1271, 1211, 1199, 1073, 1161, 1183, 1091, 1111, 1123, 1145, 1199, 1681, 4823};
	public static int[] junkItem3 = {1432, 1373, 1347, 1333, 1319, 1303, 1289, 1275, 1213, 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201, 4824, 386, 2491, 2497, 2503};

	public static int[] levelOneRewards = {2583, 2585, 2587, 2589, 2591, 2593, 2595, 2597, 3472, 3473, 2579, 2633, 2635, 2637, 2631, 7362, 7364, 7366, 7368, 7386, 7388, 7390, 7392, 7394, 7396, 7329, 7330, 7331, 7332, 7338, 7344, 7350, 7356, 3827, 3831, 3835, 3827, 3831, 3835, 3827, 3831, 3835, 10316, 10318, 10320, 10322, 10324, 10408, 10410, 10428, 10430, 10412, 10414, 10432, 10434, 10404, 10406, 10424, 10426, 10458, 10460, 10462, 10464, 10466, 10468, 10608, 10392, 10394, 10396, 10398};

	public static int[] levelTwoRewards = {7329, 7330, 7331, 7319, 7321, 7323, 7325, 7327, 7370, 7372, 7378, 7380, 2645, 2647, 2649, 2579, 2577, 2599, 2601, 2603, 2605, 2607, 2609, 2611, 2613, 7334, 7340, 7346, 7352, 7358, 3828, 3832, 3836, 3829, 3833, 3837, 3829, 3833, 3837, 3829, 3833, 3837, 10400, 10402, 10420, 10422, 10416, 10418, 10436, 10438, 10446, 10448, 10450, 10452, 10454, 10456, 10608};

	public static int[] levelThreeRewards = {3480, 3481, 3483, 3486, 3488, 2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667, 2669, 2671, 2673, 2675, 2581, 2651, 7398, 7399, 7400, 7329, 7330, 7331, 7374, 7376, 7382, 7384, 2615, 2617, 2619, 2621, 2623, 2625, 2627, 2629, 7336, 7342, 7348, 7354, 7360, 3830, 3834, 3838, 3830, 3834, 3838, 3830, 3834, 3838, 2639, 2643, 10368, 10370, 10372, 10374, 10384, 10386, 10388, 10390, 10376, 10378, 10380, 10382, 10330, 10332, 10334, 10336, 10338, 10340, 10342, 10344, 10346, 10348, 10350, 10352, 10440, 10442, 10444, 10470, 10472, 10474, 10608}; 

	/* cleaning the actual clue interfaces(strings) */

	public static void cleanClueInterface(Player player) {
		for (int i = 6968; i <= 6975; i++) {
			player.getActionSender().sendString("", i);
		}
	}

	public static void clueReward(Player player, int clueLevel, String string, boolean isDialogue, String rewardString) {
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		player.getDialogue().setLastNpcTalk(npc != null ? npc.getNpcId() : 0);
		switch (clueLevel) {
			case 1 :
				if (player.clue1Amount < 4 && Misc.random(3) == 0 || player.clue1Amount == 4) {
					player.clue1Amount = 0;
					if (isDialogue) {
						Dialogues.setNextDialogue(player, 10009, 1);
						player.getDialogue().sendNpcChat(rewardString, Dialogues.HAPPY);
					} else {
						itemReward(player, clueLevel);
					}
				} else {
					player.getDialogue().sendStatement(string, CLUE_ITEM);
					player.getDialogue().endDialogue();
					addNewClue(player, clueLevel);
					player.clue1Amount++;
				}
				break;
			case 2 :
				if (player.clue2Amount < 5 && Misc.random(4) == 0 || player.clue2Amount == 5) {
					player.clue2Amount = 0;
					if (isDialogue) {
						Dialogues.setNextDialogue(player, 10009, 1);
						player.getDialogue().sendNpcChat(rewardString, Dialogues.HAPPY);
					} else {
						itemReward(player, clueLevel);
					}
				} else {
					player.getDialogue().sendStatement(string, CLUE_ITEM);
					player.getDialogue().endDialogue();
					addNewClue(player, clueLevel);
					player.clue2Amount++;
				}
				break;
			case 3 :
				if (player.clue3Amount < 7 && Misc.random(6) == 0 || player.clue3Amount == 7) {
					player.clue3Amount = 0;
					if (isDialogue) {
						Dialogues.setNextDialogue(player, 10009, 1);
						player.getDialogue().sendNpcChat(rewardString, Dialogues.HAPPY);

					} else {
						itemReward(player, clueLevel);
					}
				} else {
					player.getDialogue().sendStatement(string, CLUE_ITEM);
					player.getDialogue().endDialogue();
					addNewClue(player, clueLevel);
					player.clue3Amount++;
				}
				break;
		}
	}
	public static boolean duplicate(int i, int[] array) {
	    for(int k : array) {
		if(k == i) return true;
	    }
	    return false;
	}
	private static void addNewClue(Player player, int clueLevel) {
		player.getInventory().addItemOrDrop(new Item(getRandomClue(clueLevel), 1));
	}

	public static void itemReward(Player player, int clueLevel) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		int random = Misc.random(5) + 3;
		switch (clueLevel) {
			case 1 :
				for (int i = 0; i < random; i++) {
					int percent = Misc.random(100);
					if (percent <= 7) {
					    int reward = levelOneRewards[Misc.random(levelOneRewards.length - 1)];
					    int reward2 = reward;
					    if (!array.contains(reward)) {
						array.add(reward);
					    } else {
						while(reward2 == reward) {
						    reward2 = levelOneRewards[Misc.random(levelOneRewards.length - 1)];
						    if (!array.contains(reward2)) {
							array.add(reward2);
							break;
						    }
						}
					    }
					} else if (percent > 7 && percent <= 30 && !array.contains(995)) {
						array.add(995);
					} else {
						int reward = Misc.random(2) == 1 ? junkItem1[Misc.random(junkItem1.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)];
						int reward2 = reward;
						if(!array.contains(reward)) {
						    array.add(reward);
						} else {
						    while(reward2 == reward) {
							reward2 = Misc.random(2) == 1 ? junkItem1[Misc.random(junkItem1.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)];
							if (!array.contains(reward2)) {
							    array.add(reward2);
							    break;
							}
						    }
						}
					}
				}
				break;
			case 2 :
				for (int i = 0; i < random; i++) {
					int percent = Misc.random(110);
					if (percent <= 7) {
					    int reward = levelTwoRewards[Misc.random(levelTwoRewards.length - 1)];
					    int reward2 = reward;
					    if (!array.contains(reward)) {
						array.add(reward);
					    } else {
						while(reward2 == reward) {
						    reward2 = levelTwoRewards[Misc.random(levelTwoRewards.length - 1)];
						    if (!array.contains(reward2)) {
							array.add(reward2);
							break;
						    }
						}
					    }
					} else if (percent > 7 && percent <= 30 && !array.contains(995)) {
						array.add(995);
					} else {
						int reward = Misc.random(2) == 1 ? junkItem2[Misc.random(junkItem2.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)];
						int reward2 = reward;
						if(!array.contains(reward)) {
						    array.add(reward);
						} else {
						    while(reward2 == reward) {
							reward2 = Misc.random(2) == 1 ? junkItem2[Misc.random(junkItem2.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)];
							if(!array.contains(reward2)) {
							    array.add(reward2);
							    break;
							}
						    }
						}
					}

				}
				break;
			case 3 :
				for (int i = 0; i < random; i++) {
					int percent = Misc.random(120);
					if (percent <= 7) {
						int reward = levelThreeRewards[Misc.random(levelThreeRewards.length - 1)];
						int reward2 = reward;
						Item junkItem = new Item(reward);
						if(junkItem.getDefinition().getName().toLowerCase().contains("3rd age")) {
						    if(Misc.random(3) == 0) {
							array.add(reward);
						    }
						} else {
						    if(!array.contains(reward)) {
							array.add(reward);
						    } else {
							while (reward2 == reward) {
							    reward2 = levelThreeRewards[Misc.random(levelThreeRewards.length - 1)];
							    if(!array.contains(reward2)) {
								array.add(reward2);
								break;
							    }
							}
						    }
						}
					} else if (percent > 7 && percent <= 30 && !array.contains(995)) {
						array.add(995);
					} else {
						int reward = Misc.random(2) == 1 ? junkItem3[Misc.random(junkItem3.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)];
						int reward2 = reward;
						if(!array.contains(reward)) {
						    array.add(Misc.random(2) == 1 ? junkItem3[Misc.random(junkItem3.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)]);
						} else {
						    while(reward2 == reward) {
							reward2 = Misc.random(2) == 1 ? junkItem3[Misc.random(junkItem3.length - 1)] : mainJunk[Misc.random(mainJunk.length - 1)];
							if(!array.contains(reward2)) {
							    array.add(reward2);
							    break;
							}
						    }
						}
					}
				}
				break;
		}
		for(int i = 0; i < array.size() - 1; i++) {
		    for(int k = 0; k < array.size() - 1; k++) {
			if(array.get(i).equals(array.get(k)) && i != k && (k+1) != array.size()) {
			    array.remove(i);
			}
		    }
		}
		int[] items = new int[random];
		int[] amounts = new int[random];
		Item[] item = new Item[random];
		for (int i = 0; i < (array.size() - 1); i++) {
		    items[i] = array.get(i);
		    amounts[i] = new Item(items[i]).getDefinition().isStackable() ? items[i] == 995 ? Misc.random(10000) : Misc.random(4) + 9 : 1;
		    if (new Item(items[i]).getDefinition().getName().toLowerCase().contains("page")) {
			amounts[i] = 1;
		    }
		    item[i] = new Item(items[i], amounts[i]);
		    player.getInventory().addItemOrDrop(new Item(items[i], amounts[i]));
		}
		player.getActionSender().sendUpdateItems(6963, item);
		player.getActionSender().sendInterface(6960);

		player.getActionSender().sendMessage("Well done, you've completed the Treasure Trail!");
        player.getActionSender().sendQuickSong(95, 12); //not the correct music but it fits fine
	}

	public static void dropClue(Player player, Npc npc) {
		int chance = RingEffect.ringOfWealth(player) ? 30 : 40;
		if (Misc.random(chance) != 0) { //1% chance //originally 25 
			return;
		}
		if (player.hasClueScroll()) {
			return;
		}
		for (String element : levelOneClueNpc) {
			if (npc.getDefinition().getName().toLowerCase().contains(element.toLowerCase())) {
				GroundItemManager.getManager().dropItem(new GroundItem(new Item(getRandomClue(1)), player,  new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ())));
				return;
			}
		}
		for (String element : levelTwoClueNpc) {
			if (npc.getDefinition().getName().toLowerCase().contains(element.toLowerCase())) {
				GroundItemManager.getManager().dropItem(new GroundItem(new Item(getRandomClue(2)), player,  new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ())));
                return;
        	}
		}
		for (String element : levelThreeClueNpc) {
			if (npc.getDefinition().getName().toLowerCase().contains(element.toLowerCase())) {
                GroundItemManager.getManager().dropItem(new GroundItem(new Item(getRandomClue(3)), player,  new Position(npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ())));
                return;
        	}
		}
	}

	public static int getRandomClue(int clueLevel) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		switch (clueLevel) {
			case 1 :
				array.add(MapScrolls.getRandomScroll(1));
				array.add(SearchScrolls.getRandomScroll(1));
				array.add(SpeakToScrolls.getRandomScroll(1));

				array.add(MapScrolls.getRandomScroll(1));
				array.add(SearchScrolls.getRandomScroll(1));
				array.add(SpeakToScrolls.getRandomScroll(1));

				array.add(DiggingScrolls.getRandomScroll(1));

				return array.get(Misc.random(array.size() - 1));

			case 2 :
				array.add(AnagramsScrolls.getRandomScroll(2));
				array.add(MapScrolls.getRandomScroll(2));
				array.add(SearchScrolls.getRandomScroll(2));
				array.add(SpeakToScrolls.getRandomScroll(2));
				array.add(CoordinateScrolls.getRandomScroll(2));

				return array.get(Misc.random(array.size() - 1));

			case 3 :
				array.add(AnagramsScrolls.getRandomScroll(3));
				array.add(MapScrolls.getRandomScroll(3));
				array.add(SearchScrolls.getRandomScroll(3));
				array.add(SpeakToScrolls.getRandomScroll(3));
				array.add(DiggingScrolls.getRandomScroll(3));
				array.add(CoordinateScrolls.getRandomScroll(3));
				return array.get(Misc.random(array.size() - 1));

		}

		return -1;
	}

	public static boolean handleCasket(Player player, int itemId) {
		switch (itemId) {
			case REWARD_CASKET_LV1 :
				player.getInventory().removeItem(new Item(itemId, 1));
				itemReward(player, 1);
				return true;
			case REWARD_CASKET_LV2 :
				player.getInventory().removeItem(new Item(itemId, 1));
				itemReward(player, 2);
				return true;
			case REWARD_CASKET_LV3 :
				player.getInventory().removeItem(new Item(itemId, 1));
				itemReward(player, 3);
				return true;
			case CASKET_LV1 :
				player.getInventory().removeItem(new Item(itemId, 1));
				clueReward(player, 1, "You've found another clue!", false, "Here is your reward!");
				return true;
			case CASKET_LV2 :
				player.getInventory().removeItem(new Item(itemId, 1));
				clueReward(player, 2, "You've found another clue!", false, "Here is your reward!");
				return true;
			case CASKET_LV3 :
				player.getInventory().removeItem(new Item(itemId, 1));
				clueReward(player, 3, "You've found another clue!", false, "Here is your reward!");
				return true;
		}
		return false;
	}
	public static boolean hasClue(Player player) {
		return player.getInventory().ownsItem("clue scroll");
	}

	public static void spawnAttacker(Player player) {
	    int id = player.inWild() ? 1007 : 1264;
	    if (!NpcLoader.checkSpawn(player, id)) {
		NpcLoader.spawnNpc(player, player.inWild() ? new Npc(1007) : new Npc(1264), true, true);
	    }
	}

	public static void handleAttackerDeath(Player player, Npc npc) {
		if (npc.getNpcId() == 1007 || npc.getNpcId() == 1264) {
			player.setKilledClueAttacker(true);
		}
	}

	public static void handleItemOnItem(Player player, int itemUsed, int useWith) {

	}

	public static void sendAmountOfScrolls(Player c) {
		int i = AnagramsScrolls.AnagramsData.values().length;
		int i2 = ChallengeScrolls.ChallengeData.values().length;
		int i3 = CoordinateScrolls.CoordinateData.values().length;
		int i4 = DiggingScrolls.DigData.values().length;
		int i5 = MapScrolls.MapCluesData.values().length;
		int i6 = SearchScrolls.SearchData.values().length;
		int i7 = SpeakToScrolls.SpeakToData.values().length;

		int finalNumber = i + i2 + i3 + i4 + i5 + i6 + i7;

		c.getActionSender().sendMessage(finalNumber + " clues scrolls loaded.");
	}
}
