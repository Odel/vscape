package com.rs2.model.content.skills.herblore;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 20/12/11 Time: 22:53 To change
 * this template use File | Settings | File Templates.
 */
public class PoisoningWeapon {

	public enum PoisonData {

	BRONZE_ARROW(882, 883, 5616, 883),
        IRON_ARROW(884, 885, 5617, 885),
        STEEL_ARROW(886, 887, 5618, 887),
        MITHRIL_ARROW(888, 889, 5619, 889),
        ADAMANT_ARROW(890, 891, 5620, 891),
        RUNE_ARROW(892, 893, 5621, 893),
	DRAGON_ARROW(11212, 11227, 11228, 11229),

	IRON_KNIFE(863, 871, 5655, 5662),
        BRONZE_KNIFE(864, 870, 5654, 5661),
        STEEL_KNIFE(865, 872, 5656, 5663),
        BLACK_KNIFE(869, 874, 5658, 5665),
        MITHRIL_KNIFE(866, 873, 5657, 5664),
        ADAMANT_KNIFE(867, 875, 5659, 5666),
        RUNE_KNIFE(868, 876, 5660, 5667),

	BRONZE_DART(806, 812, 5628, 5635),
        IRON_DART(807, 813, 5629, 5636),
        STEEL_DART(808, 814, 5630, 5637),
        BLACK_DART(3093, 3094, 5631, 5638),
        MITHRIL_DART(809, 815, 5632, 5639),
        ADAMANT_DART(810, 816, 5633, 5640),
        RUNE_DART(811, 817, 5634, 5641),
	DRAGON_DART(11230, 11231, 11233, 11234),

	IRON_JAVELIN(825, 831, 5641, 5648),
        BRONZE_JAVELIN(826, 832, 5642, 5649),
        STEEL_JAVELIN(827, 833, 5643, 5650),
        MITHRIL_JAVELIN(828, 834, 5644, 5651),
        ADAMANT_JAVELIN(829, 835, 5645, 5652),
        RUNE_JAVELIN(830, 836, 5646, 5653),

	IRON_DAGGER(1203, 1219, 5668, 5686),
        BRONZE_DAGGER(1205, 1221, 5670, 5688),
        STEEL_DAGGER(1207, 1223, 5672, 5690),
        BLACK_DAGGER(1217, 1233, 5682, 5700),
        MITHRIL_DAGGER(1209, 1225, 5674, 5692),
        ADAMANT_DAGGER(1211, 1227, 5676, 5694),
        RUNE_DAGGER(1213, 1229, 5678, 5696),
        DRAGON_DAGGER(1215, 1231, 5680, 5698),

	BRONZE_SPEAR(1237, 1251, 5704, 5618),
        IRON_SPEAR(1239, 1253, 5706, 5620),
        STEEL_SPEAR(1241, 1255, 5708, 5622),
        BLACK_SPEAR(4580, 4582, 5734, 5636),
        MITHRIL_SPEAR(1243, 1257, 5710, 5624),
        ADAMANT_SPEAR(1245, 1259, 5712, 5626),
        RUNE_SPEAR(1247, 1261, 5714, 5628),
        DRAGON_SPEAR(1249, 1263, 5716, 5630);

		private int itemId;
		private int firstStageId;
		private int secondStageId;
		private int thirdStageId;

		private static Map<Integer, PoisonData> poisonItems = new HashMap<Integer, PoisonData>();

		public static PoisonData forId(int itemId) {
			return poisonItems.get(itemId);
		}

		static {
			for (PoisonData data : PoisonData.values()) {
				poisonItems.put(data.itemId, data);
			}
		}
		private PoisonData(int itemId, int firstStageId, int secondStageId, int thirdStageId) {
			this.itemId = itemId;
			this.firstStageId = firstStageId;
			this.secondStageId = secondStageId;
			this.thirdStageId = thirdStageId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getFirstStageId() {
			return firstStageId;
		}

		public int getSecondStageId() {
			return secondStageId;
		}

		public int getThirdStageId() {
			return thirdStageId;
		}

	}

	public static boolean handlePoison(Player player, Item itemUsed, Item usedWith) {
		if (itemUsed.getId() == 187 || usedWith.getId() == 187) {
			PoisoningWeapon.poisonWeapon(player, 1, usedWith.getId() == 187 ? itemUsed.getId() : usedWith.getId());
			return true;
		}
		if (itemUsed.getId() == 5937 || usedWith.getId() == 5937) {
			PoisoningWeapon.poisonWeapon(player, 2, usedWith.getId() == 5937 ? itemUsed.getId() : usedWith.getId());
			return true;

		}
		if (itemUsed.getId() == 5940 || usedWith.getId() == 5940) {
			PoisoningWeapon.poisonWeapon(player, 3, usedWith.getId() == 5940 ? itemUsed.getId() : usedWith.getId());
			return true;
		}
		return false;
	}

	public static void poisonWeapon(final Player player, final int stage, final int itemId) {
		final PoisonData poisonData = PoisonData.forId(itemId);
		if (poisonData == null)
			return;
		if (!Constants.HERBLORE_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		player.getUpdateFlags().sendAnimation(1652);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int finalId = 0;
			@Override
			public void execute(CycleEventContainer p) {
				switch (stage) {
					case 1 :
						finalId = poisonData.getFirstStageId();
						player.getInventory().removeItem(new Item(187));
						player.getInventory().addItem(new Item(229));
						break;
					case 2 :
						finalId = poisonData.getSecondStageId();
						player.getInventory().removeItem(new Item(5937));
						player.getInventory().addItem(new Item(229));
						break;
					case 3 :
						finalId = poisonData.getThirdStageId();
						player.getInventory().removeItem(new Item(5940));
						player.getInventory().addItem(new Item(229));
						break;
				}
				Item item = new Item(itemId);
				int count = player.getInventory().getItemAmount(itemId) < 15 ? player.getInventory().getItemAmount(itemId) : 15;
				if (!item.getDefinition().isStackable())
					count = 1;

				player.getInventory().removeItem(new Item(itemId, count));
				player.getInventory().addItem(new Item(finalId, count));
				player.getActionSender().sendMessage("You imbue the " + new Item(itemId).getDefinition().getName().toLowerCase() + ((player.getInventory().getItemAmount(itemId) > 1 && new Item(itemId).getDefinition().isStackable()) ? "s" : "") + " with poison");
				p.stop();
			}
			@Override
			public void stop() {
			}
		}, 2);

	}
}
