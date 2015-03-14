package com.rs2.model.content.skills.smithing;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class SmithBars {

	private static final String[] smithItems = {"dagger", "axe", "mace", "med helm", "sword", "nails", "dart tip", "arrowtips", "scimitar", "longsword", "full helm", "knife", "sq shield", "warhammer", "battleaxe", "chainbody", "kiteshield", "claws", "2h sword", "plateskirt", "platelegs", "platebody", "Steel studs", "bolts (unf)", "limbs"};

	private static final String[] smithMetal = {"bronze", "iron", "steel", "mithril", "adamant", "rune"};

	// level, xp, bars
	public static final int[][] smithInfo = {{0, 12, 1}, {1, 12, 1}, {2, 12, 1}, {3, 12, 1}, {4, 12, 1}, {4, 12, 1}, {4, 12, 1}, {5, 12, 1}, {5, 25, 2}, {6, 25, 2}, {7, 25, 2}, {7, 12, 1}, {8, 25, 2}, {9, 37, 3}, {10, 37, 3}, {11, 37, 3}, {12, 37, 3}, {13, 25, 2}, {14, 37, 3}, {16, 37, 3}, {16, 37, 3}, {18, 62, 5}, {6, 37, 1},{3, 12, 1},{6, 12, 1}};

	public static void smithInterface(Player player, int itemId) {
		if (!Constants.SMITHING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (!player.getInventory().playerHasItem(2347)) {
			player.getDialogue().sendStatement("You need a hammer to start smithing.");
			return;
		}
		if (itemId == 9467) {
		    Dialogues.startDialogue(player, 10201);
		    return;
		}
		if (itemId == 2349) {
			player.setSmithInterface(0);
		} else if (itemId == 2351) {
			player.setSmithInterface(1);
		} else if (itemId == 2353) {
			player.setSmithInterface(2);
		} else if (itemId == 2359) {
			player.setSmithInterface(3);
		} else if (itemId == 2361) {
			player.setSmithInterface(4);
		} else if (itemId == 2363) {
			player.setSmithInterface(5);
		} else {
			return;
		}
		if (!SkillHandler.hasRequiredLevel(player, Skill.SMITHING, smithLevel(0, player.getSmithInterface()), "smith this bar")) {
			player.getTask();
			player.resetAnimation();
			return;
		}
		player.setOldItem(itemId);
		String fiveb = checkBars(itemId, 5, player);
		String threeb = checkBars(itemId, 3, player);
		String twob = checkBars(itemId, 2, player);
		String oneb = checkBars(itemId, 1, player);
		player.getActionSender().sendString(fiveb + "5bars" + fiveb, 1112);
		player.getActionSender().sendString(threeb + "3bars" + threeb, 1109);
		player.getActionSender().sendString(threeb + "3bars" + threeb, 1110);
		player.getActionSender().sendString(threeb + "3bars" + threeb, 1118);
		player.getActionSender().sendString(threeb + "3bars" + threeb, 1111);
		player.getActionSender().sendString(threeb + "3bars" + threeb, 1095);
		player.getActionSender().sendString(threeb + "3bars" + threeb, 1115);
		player.getActionSender().sendString(threeb + "3bars" + threeb, 1090);
		player.getActionSender().sendString(twob + "2bars" + twob, 1113);
		player.getActionSender().sendString(twob + "2bars" + twob, 1116);
		player.getActionSender().sendString(twob + "2bars" + twob, 1114);
		player.getActionSender().sendString(twob + "2bars" + twob, 1089);
		player.getActionSender().sendString(twob + "2bars" + twob, 8428);
		player.getActionSender().sendString(oneb + "1bar" + oneb, 1124);
		player.getActionSender().sendString(oneb + "1bar" + oneb, 1125);
		player.getActionSender().sendString(oneb + "1bar" + oneb, 1126);
		player.getActionSender().sendString(oneb + "1bar" + oneb, 1127);
		player.getActionSender().sendString(oneb + "1bar" + oneb, 1128);
		player.getActionSender().sendString(oneb + "1bar" + oneb, 1129);
		player.getActionSender().sendString(oneb + "1bar" + oneb, 1130);
		player.getActionSender().sendString(oneb + "1bar" + oneb, 1131);
		player.getActionSender().sendString(oneb + "1bar" + oneb, 13357);
		player.getActionSender().sendString(checkLevel(player, "platebody", "Plate body"), 1101);
		player.getActionSender().sendString(checkLevel(player, "platelegs", "Plate legs"), 1099);
		player.getActionSender().sendString(checkLevel(player, "plateskirt", "Plate skirt"), 1100);
		player.getActionSender().sendString(checkLevel(player, "2h sword", "2 hand sword"), 1088);
		player.getActionSender().sendString(checkLevel(player, "claws", "Claws"), 8429);
		player.getActionSender().sendString(checkLevel(player, "kiteshield", "Kite shield"), 1105);
		player.getActionSender().sendString(checkLevel(player, "chainbody", "Chain body"), 1098);
		player.getActionSender().sendString(checkLevel(player, "battleaxe", "Battle axe"), 1092);
		player.getActionSender().sendString(checkLevel(player, "warhammer", "Warhammer"), 1083);
		player.getActionSender().sendString(checkLevel(player, "sq shield", "Square shield"), 1104);
		player.getActionSender().sendString(checkLevel(player, "knife", "Throwing knives"), 1106);
		player.getActionSender().sendString(checkLevel(player, "full helm", "Full helm"), 1103);
		player.getActionSender().sendString(checkLevel(player, "longsword", "Long sword"), 1086);
		player.getActionSender().sendString(checkLevel(player, "scimitar", "Scimitar"), 1087);
		player.getActionSender().sendString(checkLevel(player, "arrowtips", "Arrowtips"), 1108);
		player.getActionSender().sendString(checkLevel(player, "dart tip", "Dart tips"), 1107);
		player.getActionSender().sendString(checkLevel(player, "nails", "Nails"), 13358);
		player.getActionSender().sendString(checkLevel(player, "sword", "Sword"), 1085);
		player.getActionSender().sendString(checkLevel(player, "med helm", "Medium helm"), 1102);
		player.getActionSender().sendString(checkLevel(player, "mace", "Mace"), 1093);
		player.getActionSender().sendString(checkLevel(player, "axe", "Axe"), 1091);
		player.getActionSender().sendString(checkLevel(player, "dagger", "Dagger"), 1094);

		player.getActionSender().sendUpdateItem(getItemType(player, "dagger"), 0, 1119, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "axe"), 0, 1120, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "chainbody"), 0, 1121, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "med helm"), 0, 1122, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "dart tip"), 0, 1123, 10);
		player.getActionSender().sendUpdateItem(getItemType(player, "sword"), 1, 1119, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "mace"), 1, 1120, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "platelegs"), 1, 1121, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "full helm"), 1, 1122, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "arrowtips"), 1, 1123, 15);
		player.getActionSender().sendUpdateItem(getItemType(player, "scimitar"), 2, 1119, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "warhammer"), 2, 1120, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "plateskirt"), 2, 1121, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "sq shield"), 2, 1122, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "knife"), 2, 1123, 5);
		player.getActionSender().sendUpdateItem(getItemType(player, "longsword"), 3, 1119, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "battleaxe"), 3, 1120, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "platebody"), 3, 1121, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "kiteshield"), 3, 1122, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "2h sword"), 4, 1119, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "claws"), 4, 1120, 1);
		player.getActionSender().sendUpdateItem(getItemType(player, "nails"), 4, 1122, 15);
		
		if(smithMetal[player.getSmithInterface()].equalsIgnoreCase("steel"))
		{
			player.getActionSender().sendString(oneb + "1bar" + oneb, 11459);
			player.getActionSender().sendString(checkLevel(player, "Steel studs", "Steel studs"), 11461);
			player.getActionSender().sendUpdateItem(getItemType(player, "Steel studs"), 4, 1121, 1);
		}
		else
		{
			player.getActionSender().sendString("", 11461);
			player.getActionSender().sendString("", 11459);
			player.getActionSender().sendUpdateItem(new Item(-1,0), 4, 1121, 1);
		}

		player.getActionSender().sendString(oneb + "1bar" + oneb, 1132);
		player.getActionSender().sendString(checkLevel(player, "bolts (unf)", "Bolts (unf)"), 1096);
		player.getActionSender().sendUpdateItem(getItemType(player, "bolts (unf)"), 3, 1123, 10);
			
		player.getActionSender().sendString(oneb + "1bar" + oneb, 1135);
		player.getActionSender().sendString(checkLevel(player, "limbs", "Limbs"), 1134);
		player.getActionSender().sendUpdateItem(getItemType(player, "limbs"), 4, 1123, 1);
		
		player.getActionSender().sendInterface(994);
		player.setStatedInterface("smithing");
	}

	public static void startSmithing(final Player player, final int item, final int amount) {
		final int id = getArrayForFullName(player, item);
		if (id > -1) {
		    RSInterface inter = RSInterface.forId(994);
	        if (!player.hasInterfaceOpen(inter) || !player.getStatedInterface().equals("smithing")) {
	        	player.getActionSender().removeInterfaces();
	            return;
	        }
			final Item finalItem = new Item(item);
			final String itemName = finalItem.getDefinition().getName().toLowerCase();
			if (!SkillHandler.hasRequiredLevel(player, Skill.SMITHING, smithLevel(id, player.getSmithInterface()), "smith " + itemName) ) {
				return;
			}
			if (!player.getInventory().getItemContainer().contains(2347)) {
				player.getActionSender().sendMessage("You need a hammer to smith on an anvil.");
				return;
			}
			final Item bar = new Item(player.getOldItem(), smithInfo[id][2]);
			if (!player.getInventory().playerHasItem(bar)) {
				player.getActionSender().sendMessage("You need at least " + smithInfo[id][2] + " bars to make " + itemName + ".");
				return;
			}
			if (player.getNewComersSide().isInTutorialIslandStage()) {
				//TutorialIsland.sendStartInfo(player);
			} else {
				player.getActionSender().removeInterfaces();
			}
			if(itemName.contains("claws") && !QuestHandler.questCompleted(player, 43)) {
				player.getDialogue().sendStatement("You must complete Death Plateau to smith this.");
				return;
			}
			final int task = player.getTask();
			player.getUpdateFlags().sendAnimation(898);
			player.getActionSender().sendSound(468, 0, 0);
			player.setSkilling(new CycleEvent() {
				int amounts = amount;
				@Override
				public void execute(CycleEventContainer container) {
					if (player.getOldItem() < 1 || !player.checkTask(task)) {
						container.stop();
						return;
					}
					if (!player.getInventory().playerHasItem(bar)) {
						player.getActionSender().sendMessage("You have run out of bars!");
						container.stop();
						return;
					}
					player.getInventory().removeItem(bar);
					if (itemName.contains("arrowtips") || itemName.contains("nails")) {
						player.getInventory().addItem(new Item(item, 15));
					} else if (itemName.contains("dart tip") || itemName.contains("bolts")) {
						player.getInventory().addItem(new Item(item, 10));
					} else if (itemName.contains("knife")) {
						player.getInventory().addItem(new Item(item, 5));
					} else {
						player.getInventory().addItem(new Item(item, 1));
					}
					player.getSkill().addExp(Skill.SMITHING, smithInfo[id][1] + 12.5 * smithInfo[id][2] * player.getSmithInterface() + 0.5);
					if (player.getNewComersSide().isInTutorialIslandStage()) {
						if (player.getNewComersSide().getTutorialIslandStage() == 36)
							player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
						player.setClickId(0);
						player.getDialogue().sendStatement("You hammer the " + bar.getDefinition().getName().toLowerCase() + " and make " + finalItem.getDefinition().getName().toLowerCase() + ".");
					} else {
						player.getActionSender().sendMessage("You hammer the " + bar.getDefinition().getName().toLowerCase() + " and make " + finalItem.getDefinition().getName().toLowerCase() + ".");
					}
					amounts--;
					if (amounts < 1) {
						container.stop();
						return;
					}
					if (!player.getInventory().playerHasItem(bar)) {
						player.getActionSender().sendMessage("You have run out of bars!");
						container.stop();
						return;
					}
					player.getUpdateFlags().sendAnimation(898);
					player.getActionSender().sendSound(468, 0, 0);
				}
				@Override
				public void stop() {
					player.getTask();
					player.resetAnimation();
					player.setStatedInterface("");
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 4);
		}
	}

	public static void startSmithingBlurite(final Player player, final int item, final int amount) {
		final int id = 0;
		if (id > -1) {
			final Item finalItem = new Item(item);
			if (!player.getInventory().getItemContainer().contains(2347)) {
				player.getActionSender().sendMessage("You need a hammer to smith on an anvil.");
				return;
			}
			final Item bar = new Item(9467, 1);
			if (player.getNewComersSide().isInTutorialIslandStage()) {
				//TutorialIsland.sendStartInfo(player);
			} else {
				player.getActionSender().removeInterfaces();
			}
			final int task = player.getTask();
			player.getUpdateFlags().sendAnimation(898);
			player.getActionSender().sendSound(468, 0, 0);
			player.setSkilling(new CycleEvent() {
				int amounts = amount;
				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task)) {
						container.stop();
						return;
					}
					if (!player.getInventory().playerHasItem(bar)) {
						player.getActionSender().sendMessage("You have run out of bars!");
						container.stop();
						return;
					}
					player.getInventory().removeItem(bar);
					if (item == 9376 ) {
						player.getInventory().addItem(new Item(item, 10));
					} else {
						player.getInventory().addItem(new Item(item, 1));
					}
					player.getSkill().addExp(Skill.SMITHING, 5);
					if (player.getNewComersSide().isInTutorialIslandStage()) {
						if (player.getNewComersSide().getTutorialIslandStage() == 36)
							player.getNewComersSide().setTutorialIslandStage(player.getNewComersSide().getTutorialIslandStage() + 1, true);
						player.setClickId(0);
						player.getDialogue().sendStatement("You hammer the " + bar.getDefinition().getName().toLowerCase() + " and make " + finalItem.getDefinition().getName().toLowerCase() + ".");
					} else {
						player.getActionSender().sendMessage("You hammer the " + bar.getDefinition().getName().toLowerCase() + " and make " + finalItem.getDefinition().getName().toLowerCase() + ".");
					}
					amounts--;
					if (amounts < 1) {
						container.stop();
						return;
					}
					if (!player.getInventory().playerHasItem(bar)) {
						player.getActionSender().sendMessage("You have run out of bars!");
						container.stop();
						return;
					}
					player.getUpdateFlags().sendAnimation(898);
					player.getActionSender().sendSound(468, 0, 0);
				}
				@Override
				public void stop() {
					player.getTask();
					player.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 4);
		}
	}
	public static final int addLevel(int id) {
		switch (id) {
			case 1 :
				return 15;
			case 2 :
				return 30;
			case 3 :
				return 50;
			case 4 :
				return 70;
			case 5 :
				return 85;
		}
		return 0;
	}

	public static final int smithLevel(int i, int id) {
		int level = smithInfo[i][0] + addLevel(id);
		return level < 1 ? 1 : level > 99 ? 99 : level;
	}

	private static String checkBars(int item, int amount, Player player) {
		if (player.getInventory().playerHasItem(item, amount)) {
			return "@gre@";
		}
		return "";
	}

	private static String checkLevel(Player player, String exactItem, String item) {
		int i = getArrayForItem(exactItem);
		int level = smithLevel(i, player.getSmithInterface());
		if (player.getNewComersSide().isInTutorialIslandStage() && !item.equalsIgnoreCase("dagger")) {
			return "" + item;
		}
		if (player.getSkill().getLevel()[Skill.SMITHING] >= level) {
			return "@whi@" + item;
		} else {
			return "" + item;
		}
	}

	public static final int getArrayForFullName(Player c, int item) {
		String name = new Item(item).getDefinition().getName();
		for (int i = 0; i < smithItems.length; i++) {
			if (name.equalsIgnoreCase(smithMetal[c.getSmithInterface()] + " " + smithItems[i])) {
				return i;
			}
			if (smithMetal[c.getSmithInterface()].equalsIgnoreCase("adamant")) {
				if (name.equalsIgnoreCase("addy " + smithItems[i])) {
					return i;
				}
				else if (name.equalsIgnoreCase("adamantite " + smithItems[i])) {
					return i;
				}
			}
			if (smithMetal[c.getSmithInterface()].equalsIgnoreCase("rune")) {
				if (name.equalsIgnoreCase("runite " + smithItems[i])) {
					return i;
				}
			}
			if (smithMetal[c.getSmithInterface()].equalsIgnoreCase("steel")) {
				if (name.equalsIgnoreCase(smithItems[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	public static final int getArrayForItem(String name) {
		for (int i = 0; i < smithItems.length; i++) {
			if (smithItems[i].equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}

	public static final Item getItemType(Player player, String exactItem) {
		String metal = smithMetal[player.getSmithInterface()];
		int id = ItemManager.getInstance().getItemId(metal + " " + exactItem);
		if (id == -1) {
			if (metal.equalsIgnoreCase("adamant")) {
				id = ItemManager.getInstance().getItemId("addy " + exactItem);
			}
		}
		if (id == -1) {
			if (metal.equalsIgnoreCase("adamant")) {
				id = ItemManager.getInstance().getItemId("adamantite " + exactItem);
			}
		}
		if (id == -1) {
			if (metal.equalsIgnoreCase("rune")) {
				id = ItemManager.getInstance().getItemId("runite " + exactItem);
			}
		}
		if (id == -1) {
			if (metal.equalsIgnoreCase("steel") && exactItem.equalsIgnoreCase("Steel studs")) {
				id = ItemManager.getInstance().getItemId(exactItem);
			}
		}
		return new Item(id);
	}

}
