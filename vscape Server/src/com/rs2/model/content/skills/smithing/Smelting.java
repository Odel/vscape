package com.rs2.model.content.skills.smithing;

import com.rs2.Constants;
import com.rs2.model.content.quests.impl.FamilyCrest;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class Smelting {

	private static final int[][][] smeltBars = {{{2349, 1, 6}, {436, 1}, {438, 1}}, {{2351, 15, 13}, {440, 1}}, {{2355, 20, 14}, {442, 1}}, {{9467, 1, 10}, {668, 1}}, {{2353, 30, 18}, {440, 1}, {453, 2}}, {{2357, 40, 23}, {444, 1}}, {{2365, 40, 23}, {446, 1}}, {{2359, 50, 30}, {447, 1}, {453, 4}}, {{2361, 70, 38}, {449, 1}, {453, 6}}, {{2363, 85, 50}, {451, 1}, {453, 8}}};

	public static final int[][] smeltButtons = {{15147, 2349, 1}, {15146, 2349, 5}, {10247, 2349, 10}, {9110, 2349, 28}, {15151, 2351, 1}, {15150, 2351, 5}, {15149, 2351, 10}, {15148, 2351, 28}, {15155, 2355, 1}, {15154, 2355, 5}, {15153, 2355, 10}, {15152, 2355, 28}, {15159, 2353, 1}, {15158, 2353, 5}, {15157, 2353, 10}, {15156, 2353, 28}, {15163, 2357, 1}, {15162, 2357, 5}, {15161, 2357, 10}, {15160, 2357, 28}, {29017, 2359, 1}, {29016, 2359, 5}, {24253, 2359, 10}, {16062, 2359, 28}, {29022, 2361, 1}, {29020, 2361, 5}, {29019, 2361, 10}, {29018, 2361, 28}, {29026, 2363, 1}, {29025, 2363, 5}, {29024, 2363, 10}, {29023, 2363, 28}};

	public static void oreOnFurnace(Player player, int itemUsed) {
		for (int[][] smeltBar : smeltBars) {
			if (itemUsed == smeltBar[1][0] || smeltBar.length > 2 && itemUsed == smeltBar[2][0]) {
				player.setOldItem(smeltBar[0][0]);
				smeltBar(player, 1);
				break;
			}
		}
	}

	public static void smeltInterface(Player player) {
		if (!Constants.SMITHING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		player.getActionSender().sendItemOnInterface(2405, 150, 2349);
		player.getActionSender().sendItemOnInterface(2406, 150, 2351);
		player.getActionSender().sendItemOnInterface(2407, 150, 2355);
		player.getActionSender().sendItemOnInterface(2409, 150, 2353);
		player.getActionSender().sendItemOnInterface(2410, 150, 2357);
		player.getActionSender().sendItemOnInterface(2411, 150, 2359);
		player.getActionSender().sendItemOnInterface(2412, 150, 2361);
		player.getActionSender().sendItemOnInterface(2413, 150, 2363);
		player.getActionSender().sendChatInterface(2400);
	}

	public static boolean handleSmelting(final Player player, final int buttonId ,final int amount) {
		for (int[] element : smeltButtons) {
			if (buttonId == element[0]) {
				player.setOldItem(element[1]);
                if (element[2] == 28 && amount < 1){
                    player.getActionSender().openXInterface(buttonId);
				    return true;
                }
				smeltBar(player, amount < 1 ? element[2] : amount);
				return true;
			}
		}
		return false;
	}

	public static boolean smeltBar(final Player player, final int amount) {
		player.getActionSender().removeInterfaces();
		final int bar = player.getOldItem();
		for (int id = 0; id < smeltBars.length; id++) {
			if (bar == smeltBars[id][0][0]) {
				if (!Constants.SMITHING_ENABLED) {
					player.getActionSender().sendMessage("This skill is currently disabled.");
					return false;
				}
				final int level = smeltBars[id][0][1];
				final int xp = smeltBars[id][0][2];
				final int ore1 = smeltBars[id][1][0];
				final int ore1Amount = smeltBars[id][1][1];
				int ore2 = 0;
				int ore2Amount = 0;
				if (smeltBars[id].length > 2) {
					ore2 = smeltBars[id][2][0];
					ore2Amount = smeltBars[id][2][1];
				}
				if (!SkillHandler.hasRequiredLevel(player, Skill.SMITHING, level, "smelt this bar")) {
					return true;
				}
				final int task = player.getTask();
				final Item firstOre = new Item(ore1, ore1Amount);
				final Item secondOre = new Item(ore2, ore2Amount);
				final Item finalBar = new Item(bar);
				final boolean needSecondOre = ore2 > 0;
				if (!player.getInventory().playerHasItem(firstOre) || (needSecondOre && !player.getInventory().playerHasItem(secondOre))) {
					player.getActionSender().sendMessage("You have run out of ore to smith!");
					return true;
				}
				if (player.getNewComersSide().isInTutorialIslandStage()) {
					player.getDialogue().sendStatement("You smelt the " + firstOre.getDefinition().getName().toLowerCase() + " " + (needSecondOre ? "and " + secondOre.getDefinition().getName().toLowerCase() + " together " : "") + "in the furnace.");
					player.setClickId(0);
				} else {
					player.getActionSender().sendMessage("You smelt the " + firstOre.getDefinition().getName().toLowerCase() + " " + (needSecondOre ? "and " + secondOre.getDefinition().getName().toLowerCase() + " together " : "") + "in the furnace.");
				}
				player.getUpdateFlags().sendAnimation(899);
				player.getActionSender().sendSound(469, 0, 0);
				player.setSkilling(new CycleEvent() {
					int count = amount;
					@Override
					public void execute(CycleEventContainer b) {
						if (!player.checkTask(task)) {
							b.stop();
							return;
						}
						if (!player.getInventory().playerHasItem(firstOre) || (needSecondOre && !player.getInventory().playerHasItem(secondOre))) {
							player.getActionSender().sendMessage("You have run out of ore to smith!");
							b.stop();
							return;
						}
						player.getInventory().removeItem(firstOre);
						if (needSecondOre) {
							player.getInventory().removeItem(secondOre);
						}
					/*	int ironChance = 50 + (player.getSkill().getLevel()[Skill.SMITHING] < 15 ? 0 : player.getSkill().getLevel()[Skill.SMITHING] - 15);
						ironChance = ironChance > 80 ? 80 : ironChance;*/
						int ironChance = 50;
						if (bar == 2351 && Misc.random(100) > ironChance && player.getEquipment().getId(Constants.RING) != 2568) {
							player.getActionSender().sendMessage("You unsuccessfuly smelt the iron ore.");
						} else {
							if (bar == 2351 && player.getEquipment().getId(Constants.RING) == 2568) {
								player.setRingOfForgingLife(player.getRingOfForgingLife() - 1);
								if (player.getRingOfForgingLife() < 1) {
									player.getEquipment().removeAmount(Constants.RING, 1);
									player.getActionSender().sendMessage("Your ring of forging shatters!");
									player.setRingOfForgingLife(140);
								}
							}
							player.getInventory().addItem(finalBar);
							if(finalBar.getId() == 2357 && player.getEquipment().getId(Constants.HANDS) == FamilyCrest.GOLDSMITH_GAUNTLETS) {
							    player.getSkill().addExp(Skill.SMITHING, 56.2);
							} else {
							    player.getSkill().addExp(Skill.SMITHING, xp);
							}
							if (player.getNewComersSide().isInTutorialIslandStage()) {
								if (player.getNewComersSide().getTutorialIslandStage() == 34)
									player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
								player.setClickId(0);
								player.getDialogue().sendStatement("You retrieve a " + finalBar.getDefinition().getName().toLowerCase() + " from the furnace.");
							} else {
								player.getActionSender().sendMessage("You retrieve a " + finalBar.getDefinition().getName().toLowerCase() + " from the furnace.");
								//player.getActionSender().sendMessage("You smelt the " + firstOre.getDefinition().getName().toLowerCase() + " " + (needSecondOre ? "and " + secondOre.getDefinition().getName().toLowerCase() + " together " : "") + "in the furnace.");
							}
						}
						count--;
						if (count < 1) {
							b.stop();
							return;
						}
						if (!player.getInventory().playerHasItem(firstOre) || (needSecondOre && !player.getInventory().playerHasItem(secondOre))) {
							player.getActionSender().sendMessage("You have run out of ore to smith!");
							b.stop();
							return;
						}
						player.getUpdateFlags().sendAnimation(899);
						player.getActionSender().sendSound(469, 0, 0);
					}
					@Override
					public void stop() {
						player.getTask();
						player.resetAnimation();
					}
				});
				CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 4);
				return true;
			}
		}
		return false;
	}
}
