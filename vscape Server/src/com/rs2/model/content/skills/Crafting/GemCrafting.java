package com.rs2.model.content.skills.Crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Crafting
 * 
 * @author Killamess
 * 
 */
public class GemCrafting extends GemData {
	/*
	 * Contains all our crafting details.
	 */
	private static HashMap<Integer, gem_Data> data = new HashMap<Integer, gem_Data>();
	private static final int JEWELCRAFTING = 899;
	public static final int GOLD_BAR = 2357;

	public static gem_Data getData(int id) {
		return data.get(id);
	}
	public enum gem_Data {

		/*
		 * GOLD: Rings, Necklaces, Amulets.
		 */
		gold(2357, -1, -1, 5, 15, 1635, 6, 20, 1654, 8, 30, 1673, 1692),

		/*
		 * SAPPGHIRE: Rings, Necklaces, Amulets.
		 */
		sapphire(1607, 20, 50, 20, 40, 1637, 22, 55, 1656, 24, 65, 1675, 1694),

		/*
		 * EMERALD: Rings, Necklaces, Amulets.
		 */
		emerald(1605, 27, 67, 27, 55, 1639, 29, 60, 1658, 31, 70, 1677, 1696),

		/*
		 * RUBY: Rings, Necklaces, Amulets.
		 */
		ruby(1603, 34, 85, 34, 70, 1641, 40, 75, 1660, 50, 85, 1679, 1698),

		/*
		 * DIAMOND: Rings, Necklaces, Amulets.
		 */
		diamond(1601, 43, 107.5, 43, 85, 1643, 56, 90, 1662, 70, 100, 1681, 1700),

		/*
		 * DRAGON_STONE: Rings, Necklaces, Amulets.
		 */
		dragon_stone(1615, 55, 137.5, 55, 100, 1645, 72, 105, 1664, 80, 150, 1683, 1702),

		/*
		 * ONYX: Rings, Necklaces, Amulets.
		 */
		onyx(6573, 67, 167.5, 67, 115, 6575, 82, 120, 6577, 90, 165, 6579, 6581);

		private int[] mapLocation = new int[13];

		gem_Data(int baseElement, int cutLevel, double cutExp, int ringLevel, double ringExp, int ringFinal, int necklaceLevel, double necklaceExp, int necklaceFinal, int amuletLevel, double amuletExp, int midAmulet, int finalAmulet) {
			this.mapLocation[GEM_SLOT] = baseElement;
			this.mapLocation[GEM_CUT_LEVEL] = cutLevel;
			this.mapLocation[GEM_CUT_EXP] = (int) cutExp;
			this.mapLocation[GEM_CRAFT_RING_LEVEL] = ringLevel;
			this.mapLocation[GEM_CRAFT_RING_EXP] = (int) ringExp;
			this.mapLocation[GEM_CRAFT_RING_FINAL_PRODUCT] = ringFinal;
			this.mapLocation[GEM_CRAFT_NECKLACE_LEVEL] = necklaceLevel;
			this.mapLocation[GEM_CRAFT_NECKLACE_EXP] = (int) necklaceExp;
			this.mapLocation[GEM_CRAFT_NECKLACE_FINAL_PRODUCT] = necklaceFinal;
			this.mapLocation[GEM_CRAFT_AMULET_LEVEL] = amuletLevel;
			this.mapLocation[GEM_CRAFT_AMULET_EXP] = (int) amuletExp;
			this.mapLocation[GEM_CRAFT_AMULET_MID_PRODUCT] = midAmulet;
			this.mapLocation[GEM_CRAFT_AMULET_FINAL_PRODUCT] = finalAmulet;
		}
	}
	static {
		for (gem_Data pointer : gem_Data.values()) {
			data.put(pointer.mapLocation[0x0], pointer);
		}
	}
	public static void showInterface(Player player, int face) {
		if (!Constants.CRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		int interfaceType = (face == 1592 ? 0 : face == 1597 ? 1 : face == 1595 ? 2 : -1);
		if (interfaceType < 0)
			return;
		if (player.getInventory().getItemContainer().contains(face)) {
			for (int i = 0; i < GemData.craftInterfaceArray[interfaceType].length; i++) {
				player.getActionSender().sendUpdateItem(i, interfaceFrames[interfaceType][1], new Item(GemData.craftInterfaceArray[interfaceType][i], 1));
			}
			player.getActionSender().sendItemOnInterface(interfaceFrames[interfaceType][0], 0, -1);
			player.getActionSender().sendString("Choose an item to make.", interfaceFrames[interfaceType][1] - 3);
		} else {
			player.getActionSender().sendItemOnInterface(interfaceFrames[interfaceType][0], 120, 1595);
			player.getActionSender().sendString(interfaceMessage[interfaceType], interfaceFrames[interfaceType][1] - 3);
			for (int i = 0; i < GemData.craftInterfaceArray[interfaceType].length; i++) {
				player.getActionSender().sendUpdateItem(i, interfaceFrames[interfaceType][1], new Item(0));
			}
		}
		player.getActionSender().sendString("What would you like to make?", 4226);
	}

	public static void openInterface(Player player) {
		if (!Constants.CRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		showInterface(player, 1592);
		showInterface(player, 1595);
		showInterface(player, 1597);
		player.getActionSender().sendInterface(4161);
	}

	public static void startCrafter(final Player player, final int gem, final int actionAmount, final int actionType) {
		if (getData(gem) == null || actionAmount < 1 || actionType < 0) {
			return;
		}
		if (!player.getInventory().getItemContainer().contains(getData(gem).mapLocation[0x0])) {
			player.getDialogue().sendStatement("You do not have the required items to do that.");
			return;
		}
		player.getActionSender().removeInterfaces();
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			private int gemType = gem;
			private int amount = actionAmount + 1;
			private int type = actionType;

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || this.amount-- < 1) {
					container.stop();
					return;
				}
				if (getData(gemType) == null || !player.getInventory().getItemContainer().contains(getData(gemType).mapLocation[0]) || !player.getInventory().getItemContainer().contains(2357)) {
					container.stop();
					return;
				}
				container.setTick(4);
				player.getUpdateFlags().sendAnimation(JEWELCRAFTING);
				switch (type) {

					/*
					 * RINGS
					 */
					case 0 :
						if (player.getSkill().getLevel()[Skill.CRAFTING] < getData(gemType).mapLocation[3]) {
							player.getDialogue().sendStatement("You need a crafting level of " + getData(gemType).mapLocation[3] + " to craft this.");
							container.stop();
							return;
						}
						if (getData(gemType).mapLocation[0] != 2357)
							player.getInventory().removeItem(new Item(getData(gemType).mapLocation[0], 1));
						player.getInventory().removeItem(new Item(2357, 1));
						player.getInventory().addItem(new Item(getData(gemType).mapLocation[5], 1));
						player.getActionSender().sendMessage("You craft a ring.");
						player.getSkill().addExp(Skill.CRAFTING, getData(gemType).mapLocation[4]);
						break;
					/*
					 * NECKLACES
					 */
					case 1 :
						if (player.getSkill().getLevel()[Skill.CRAFTING] < getData(gemType).mapLocation[6]) {
							player.getDialogue().sendStatement("You need a Crafting level of " + getData(gemType).mapLocation[6] + " to craft this.");
							container.stop();
							return;
						}
						if (getData(gemType).mapLocation[0] != 2357)
							player.getInventory().removeItem(new Item(getData(gemType).mapLocation[0], 1));
						player.getInventory().removeItem(new Item(2357, 1));
						player.getInventory().addItem(new Item(getData(gemType).mapLocation[8], 1));
						player.getActionSender().sendMessage("You craft a necklace.");
						player.getSkill().addExp(Skill.CRAFTING, getData(gemType).mapLocation[7]);
						break;
					/*
					 * AMULETS
					 */
					case 2 :
						if (player.getSkill().getLevel()[Skill.CRAFTING] < getData(gemType).mapLocation[9]) {
							player.getDialogue().sendStatement("You need a Crafting level of " + getData(gemType).mapLocation[9] + " to craft this.");
							container.stop();
							return;
						}
						if (getData(gemType).mapLocation[0] != 2357)
							player.getInventory().removeItem(new Item(getData(gemType).mapLocation[0], 1));
						player.getInventory().removeItem(new Item(2357, 1));
						if (player.getInventory().getItemContainer().contains(1759)) {
							player.getInventory().removeItem(new Item(1759, 1));
							player.getInventory().addItem(new Item(getData(gemType).mapLocation[12], 1));
							player.getActionSender().sendMessage("You craft an amulet and attach a string to it.");
						} else {
							player.getInventory().addItem(new Item(getData(gemType).mapLocation[11], 1));
							player.getActionSender().sendMessage("You craft an amulet.");
						}
						player.getSkill().addExp(Skill.CRAFTING, getData(gemType).mapLocation[10]);
						break;
					default :
						container.stop();
						return;
				}
			}
			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
	}

	public static void string(final Player player, final int gemSlot) {
		if (!player.getInventory().getItemContainer().contains(1759) || !player.getInventory().getItemContainer().contains(stringItems[gemSlot][0])) {
			return;
		}
		if (!Constants.CRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		player.getInventory().removeItem(new Item(stringItems[gemSlot][0], 1));
		player.getInventory().removeItem(new Item(1759, 1));
		player.getInventory().addItem(new Item(stringItems[gemSlot][1], 1));
		player.getActionSender().sendMessage("You attach a string to the "+new Item(stringItems[gemSlot][0]).getDefinition().getName().toLowerCase()+".");
	}
}
