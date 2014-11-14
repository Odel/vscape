package com.rs2.model.content.skills;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 08/03/12 Time: 20:11 To change
 * this template use File | Settings | File Templates.
 */
public class ItemOnItemHandling {

	private Player player;

	public ItemOnItemHandling(Player player) {
		this.player = player;
	}

	public enum ItemOnItem {
		// cooking
		WINE(1987, 1937, new Item[]{new Item(1987), new Item(1937)}, new Item[]{new Item(1995)}, -1, new int[]{Skill.COOKING, 35}, 0, "You put the grapes in the jug of water, and make unfermented wine."), RAW_GNOMEBOWL(2171, 2166, new Item[]{new Item(2171), new Item(2166)}, new Item[]{new Item(2178)}, -1, new int[]{Skill.COOKING, 42}, 30, "You place the dough into the gnomebowl mould and make a gnomebowl."), HALF_MADE_BOWL(1973, 2177, new Item[]{new Item(1973, 4), new Item(2128), new Item(2177)}, new Item[]{new Item(2173)}, -1, new int[]{Skill.COOKING, 42}, 50, "You place the chocolate bars and leaves into the bowl."), HALF_MADE_BOWL2(2128, 2177, new Item[]{new Item(1973, 4), new Item(2128), new Item(2177)}, new Item[]{new Item(2173)}, -1, new int[]{Skill.COOKING, 42}, 50, "You place the chocolate bars and leaves into the bowl."),

		// firemaking
		NORMAL_PYRE4(3430, 1511, new Item[]{new Item(3430), new Item(1511)}, new Item[]{new Item(3434), new Item(3438)}, -1, null, 0, "You put some sacred oil on the log, and turn it into a pyre log"), NORMAL_PYRE3(3432, 1511, new Item[]{new Item(3432), new Item(1511)}, new Item[]{new Item(3436), new Item(3438)}, -1, null, 0, "You put some sacred oil on the log, and turn it into a pyre log"), NORMAL_PYRE2(3434, 1511, new Item[]{new Item(3434), new Item(1511)}, new Item[]{new Item(229), new Item(3438)}, -1, null, 0, "You put some sacred oil on the log, and turn it into a pyre log"), OAK_PYRE4(3430, 1521, new Item[]{new Item(3430), new Item(1521)}, new Item[]{new Item(3434), new Item(3440)}, -1, null, 0, "You put some sacred oil on the oak log, and turn it into an oak pyre log"), OAK_PYRE3(3432, 1521, new Item[]{new Item(3432), new Item(1521)}, new Item[]{new Item(3436), new Item(3440)}, -1, null, 0, "You put some sacred oil on the oak log, and turn it into an oak pyre log"), OAK_PYRE2(
				3434, 1521, new Item[]{new Item(3434), new Item(1521)}, new Item[]{new Item(229), new Item(3440)}, -1, null, 0, "You put some sacred oil on the oak log, and turn it into an oak pyre log"),

		WILLOW_PYRE4(3430, 1519, new Item[]{new Item(3430), new Item(1519)}, new Item[]{new Item(3436), new Item(3442)}, -1, null, 0, "You put some sacred oil on the maple log, and turn it into a willow pyre log"), WILLOW_PYRE3(3432, 1519, new Item[]{new Item(3432), new Item(1519)}, new Item[]{new Item(229), new Item(3442)}, -1, null, 0, "You put some sacred oil on the maple log, and turn it into a willow pyre log"), MAPLE_PYRE4(3430, 1517, new Item[]{new Item(3430), new Item(1517)}, new Item[]{new Item(3436), new Item(3444)}, -1, null, 0, "You put some sacred oil on the maple log, and turn it into a maple pyre log"), MAPLE_PYRE3(3432, 1517, new Item[]{new Item(3432), new Item(1517)}, new Item[]{new Item(229), new Item(3444)}, -1, null, 0, "You put some sacred oil on the maple log, and turn it into a maple pyre log"),

		YEW_PYRE4(3430, 1515, new Item[]{new Item(3430), new Item(1515)}, new Item[]{new Item(229), new Item(3446)}, -1, null, 0, "You put some sacred oil on the yew log, and turn it into a yew pyre log"), MAGIC_PYRE4(3430, 1513, new Item[]{new Item(3430), new Item(1513)}, new Item[]{new Item(229), new Item(3448)}, -1, null, 0, "You put some sacred oil on the magic log, and turn it into a magic pyre log"),

		RED_FIRELIGHTER(7329, 1511, new Item[]{new Item(7329), new Item(1511)}, new Item[]{new Item(7404)}, -1, null, 0, "You coat the logs with the red chemicals."), GREEN_FIRELIGHTER(7330, 1511, new Item[]{new Item(7330), new Item(1511)}, new Item[]{new Item(7405)}, -1, null, 0, "You coat the logs with the green chemicals."), BLUE_FIRELIGHTER(7331, 1511, new Item[]{new Item(7331), new Item(1511)}, new Item[]{new Item(7406)}, -1, null, 0, "You coat the logs with the blue chemicals."),

		TORCH(590, 596, new Item[]{new Item(596)}, new Item[]{new Item(594)}, -1, new int[]{Skill.FIREMAKING, 1}, 0, "You light the torch."), CANDLE(590, 36, new Item[]{new Item(36)}, new Item[]{new Item(33)}, -1, new int[]{Skill.FIREMAKING, 1}, 0, "You light the candle."), BLACK_CANDLE(590, 38, new Item[]{new Item(38)}, new Item[]{new Item(32)}, -1, new int[]{Skill.FIREMAKING, 1}, 0, "You light the black candle."),

		CANDLE_LANTERN1(36, 4527, new Item[]{new Item(36), new Item(4527)}, new Item[]{new Item(4529)}, -1, null, 0, null), CANDLE_LANTERN(590, 4529, new Item[]{new Item(4529)}, new Item[]{new Item(4531)}, -1, new int[]{Skill.FIREMAKING, 4}, 0, "You light the candle lantern."),

		BLACK_CANDLE_LANTERN1(38, 4527, new Item[]{new Item(38), new Item(4527)}, new Item[]{new Item(4532)}, -1, null, 0, null), BLACK_CANDLE_LANTERN(590, 4532, new Item[]{new Item(4532)}, new Item[]{new Item(4534)}, -1, new int[]{Skill.FIREMAKING, 4}, 0, "You light the black candle lantern."),

		OIL_LAMP1(277, 4525, new Item[]{new Item(4525)}, new Item[]{new Item(4522)}, -1, null, 0, null), OIL_LAMP(590, 4522, new Item[]{new Item(4522)}, new Item[]{new Item(4524)}, -1, new int[]{Skill.FIREMAKING, 12}, 0, "You light the oil lamp."), OIL_LANTERN1(277, 4535, new Item[]{new Item(4535)}, new Item[]{new Item(4537)}, -1, new int[]{Skill.FIREMAKING, 1}, 0, null), OIL_LANTERN2(4540, 4542, new Item[]{new Item(4540), new Item(4542)}, new Item[]{new Item(4535)}, -1, null, 0, null), OIL_LANTERN(590, 4537, new Item[]{new Item(4537)}, new Item[]{new Item(4539)}, -1, new int[]{Skill.FIREMAKING, 26}, 0, "You light the oil lantern."),

		BULLSEYE_LANTERN1(4544, 4542, new Item[]{new Item(4544), new Item(4542)}, new Item[]{new Item(4546)}, -1, null, 0, null), BULLSEYE_LANTERN2(277, 4546, new Item[]{new Item(4546)}, new Item[]{new Item(4548)}, -1, null, 0, null), BULLSEYE_LANTERN(590, 4548, new Item[]{new Item(4548)}, new Item[]{new Item(4550)}, -1, new int[]{Skill.FIREMAKING, 49}, 0, "You light the bullseye lantern."),

		MINING_HELMET(590, 5014, new Item[]{new Item(5014)}, new Item[]{new Item(5013)}, -1, new int[]{Skill.FIREMAKING, 65}, 0, "You light the mining helmet."), 
		
		CRYSTAL_KEY(985, 987, new Item[]{new Item(985), new Item(987)}, new Item[]{new Item(989)}, -1, null, 0, "You join the two halves of the key together."),
		
		WATERMELON_SLICES(5982, 946, new Item[]{new Item(5982)}, new Item[]{new Item(5984), new Item(5984), new Item(5984)}, -1, null, 0, "You cut the watermelon into 3 slices."),
		
		CHOCOLATE_DUST(946, 1973, new Item[]{new Item(1973)}, new Item[]{new Item(1975)}, -1, null, 0, "You use your knife to turn the chocolate to dust.");
		/*
		PINEAPPLE_CHUNKS(946, 2114, new Item[]{new Item(2114)}, new Item[]{new Item(2116)}, -1, null, 0, "You cut the pineapple into chunks."),
		
		PINEAPPLE_RING(2114, 946, new Item[]{new Item(2114)}, new Item[]{new Item(2118), new Item(2118), new Item(2118), new Item(2118)}, -1, null, 0, "You cut the pineapple into rings.");*/

		private int itemFirstClick;
		private int itemSecondClick;
		private Item[] toRemove;
		private Item[] toAdd;
		private int emote;
		private int[] levelData;
		private double experience;
		private String messageSent;

		ItemOnItem(int itemFirstClick, int itemSecondClick, Item[] toRemove, Item[] toAdd, int emote, int[] levelData, double experience, String messageSent) {
			this.itemFirstClick = itemFirstClick;
			this.itemSecondClick = itemSecondClick;
			this.toRemove = toRemove;
			this.toAdd = toAdd;
			this.emote = emote;
			this.levelData = levelData;
			this.experience = experience;
			this.messageSent = messageSent;
		}

		public static ItemOnItem forId(int firstItem, int secondItem) {
			for (ItemOnItem itemOnItem : ItemOnItem.values()) {
				if (itemOnItem == null)
					continue;
				if ((itemOnItem.itemFirstClick == firstItem && itemOnItem.itemSecondClick == secondItem) || (itemOnItem.itemFirstClick == secondItem && itemOnItem.itemSecondClick == firstItem)) {
					return itemOnItem;
				}

			}
			return null;
		}

	}

	public boolean handleItemOnItem(Item itemUsed, Item usedWith, int itemUsedSlot, int usedWithSlot) {
		// researching the item data
		ItemOnItem itemOnItem = ItemOnItem.forId(itemUsed.getId(), usedWith.getId());
		if (itemOnItem == null)
			return false;
		if (itemOnItem.levelData != null) {
			if (player.getSkill().getLevel()[itemOnItem.levelData[0]] < itemOnItem.levelData[1]) {
				player.getActionSender().sendMessage("Your " + Skill.SKILL_NAME[itemOnItem.levelData[0]].toLowerCase() + " level is not high enough to do this.");
				return true;
			}
			player.getSkill().addExp(itemOnItem.levelData[0], itemOnItem.experience);
		}

		if (itemOnItem.toRemove != null)
			for (Item items : itemOnItem.toRemove) {
				if (player.getInventory().getItemAmount(items.getId()) < items.getCount()) {
					return true;
				}
				if (itemUsed.equals(items) || usedWith.equals(items))
					if (!player.getInventory().removeItemSlot(itemUsed.equals(items) ? itemUsed : usedWith, itemUsed.equals(items) ? itemUsedSlot : usedWithSlot)) {
						player.getInventory().removeItem(itemUsed.equals(items) ? itemUsed : usedWith);
					} else {
						player.getInventory().removeItem(items);
					}
				else
					player.getInventory().removeItem(items);
			}

		if (itemOnItem.toAdd != null) {
			if (itemOnItem.toAdd.length == 2 && itemOnItem.toRemove.length == 2) {
				if (!itemOnItem.toAdd[0].getDefinition().isStackable())
					player.getInventory().addItemToSlot(itemOnItem.toAdd[0], itemOnItem.toRemove[0].equals(itemUsed) ? itemUsedSlot : usedWithSlot);
				else
					player.getInventory().addItem(itemOnItem.toAdd[0]);

				if (!itemOnItem.toAdd[1].getDefinition().isStackable())
					player.getInventory().addItemToSlot(itemOnItem.toAdd[1], itemOnItem.toRemove[1].equals(usedWith) ? usedWithSlot : itemUsedSlot);
				else
					player.getInventory().addItem(itemOnItem.toAdd[1]);

				return true;
			}
			player.getUpdateFlags().sendAnimation(itemOnItem.emote);

			if (itemOnItem.messageSent != null)
				player.getActionSender().sendMessage(itemOnItem.messageSent);

			for (Item items : itemOnItem.toAdd)
				player.getInventory().addItem(items);
		}
		return true;
	}
}
