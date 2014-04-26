package com.rs2.model.content.skills.cooking;

import java.util.HashMap;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 25/12/11 Time: 17:13 To change
 * this template use File | Settings | File Templates.
 */
public class BrewCollecting {

	public static final int VAT_CONFIG = 509;

	@SuppressWarnings("unused")
	private static final int EMPTY_VAT = 7437;
	@SuppressWarnings("unused")
	private static final int EMPTY_BARREL = 7407;
	private static final int COLLECTING_ANIM = 1;

	public static enum BrewCollectData {

		CIDER(5769, 5849, 7429, 14), CIDER2(5769, 5929, 7430, 14), DWARVEN_STOUT(5769, 5777, 7411, 19), DWARVEN_STOUT2(5769, 5857, 7412, 19), ASGANIAN_ALE(5769, 5785, 7413, 24), ASGANIAN_ALE2(5769, 5865, 7414, 24), GREENMAN_ALE(5769, 5793, 7415, 29), GREENMAN_ALE2(5769, 5873, 7416, 29), WIZARD_MINDBOMB(5769, 5801, 7417, 34), WIZARD_MINDBOMB2(5769, 5881, 7418, 34), DRAGON_BITTER(5769, 5809, 7419, 39), DRAGON_BITTER2(5769, 5889, 7420, 39), MOONLIGHT_MEAD(5769, 5817, 7421, 44), MOONLIGHT_MEAD2(
				5769, 5897, 7422, 44), AXEMAN_FOLLEY(5769, 5825, 7423, 49), AXEMAN_FOLLEY2(5769, 5905, 7424, 49), CHEF_DELIGHT(5769, 5833, 7425, 54), CHEF_DELIGHT2(5769, 5913, 7426, 54), SLAYER_RESPITE(5769, 5841, 7427, 59), SLAYER_RESPITE2(5769, 5921, 7428, 59),

		CIDERBEER(5763, 5763, 7429, 14), CIDER2BEER(5765, 5765, 7430, 14), DWARVEN_STOUTBEER(5769, 1913, 7411, 19), DWARVEN_STOUT2BEER(5769, 5747, 7412, 19), ASGANIAN_ALEBEER(5769, 1905, 7413, 24), ASGANIAN_ALE2BEER(5769, 5865, 7414, 24), GREENMAN_ALEBEER(5769, 1909, 7415, 29), GREENMAN_ALE2BEER(5769, 5743, 7416, 29), WIZARD_MINDBOMBBEER(5769, 1907, 7417, 34), WIZARD_MINDBOMB2BEER(5769, 5741, 7418, 34), DRAGON_BITTERBEER(5769, 1911, 7419, 39), DRAGON_BITTER2BEER(5769, 5745, 7420, 39), MOONLIGHT_MEADBEER(
				5769, 2955, 7421, 44), MOONLIGHT_MEAD2BEER(5769, 5749, 7422, 44), AXEMAN_FOLLEYBEER(5769, 5751, 7423, 49), AXEMAN_FOLLEY2BEER(5769, 5753, 7424, 49), CHEF_DELIGHTBEER(5769, 5755, 7425, 54), CHEF_DELIGHT2BEER(5769, 5757, 7426, 54), SLAYER_RESPITEBEER(5769, 5759, 7427, 59), SLAYER_RESPITE2BEER(5769, 5761, 7428, 59);

		private int used;
		private int result;
		private int objectId;
		private int level;

		public static HashMap<Integer, BrewCollectData> brewItems = new HashMap<Integer, BrewCollectData>();

		public static BrewCollectData forId(int id) {
			return brewItems.get(id);
		}

		static {
			for (BrewCollectData collectData : BrewCollectData.values()) {
				brewItems.put(collectData.used, collectData);
			}
		}

		private BrewCollectData(int used, int result, int objectId, int level) {
			this.used = used;
			this.result = result;
			this.objectId = objectId;
			this.level = level;
		}

		public int getUsed() {
			return used;
		}

		public int getResult() {
			return result;
		}

		public int getObjectId() {
			return objectId;
		}

		public int getLevel() {
			return level;
		}

	}

	public static void handleCollectingBrew(Player player, int itemId, int objectId) {
		BrewCollectData brewCollectData = BrewCollectData.forId(itemId);
		if (brewCollectData == null)
			return;
		if (itemId != brewCollectData.getUsed() || objectId == brewCollectData.getObjectId())
			return;
		if (player.getSkill().getLevel()[Skill.COOKING] < brewCollectData.getLevel()) {
			player.getDialogue().sendStatement("You need a cooking level of " + brewCollectData.getLevel() + " to do this.");
			return;
		}

		player.getActionSender().sendMessage("You collect the liquid in the barrel with your " + ItemDefinition.forId(brewCollectData.getUsed()).getName().toLowerCase() + ".");
		player.getInventory().removeItem(new Item(brewCollectData.getUsed()));
		player.getInventory().addItem(new Item(brewCollectData.getResult()));
		player.getUpdateFlags().sendAnimation(COLLECTING_ANIM);
		player.getBrewData().setAleRuns(player.getBrewData().getAleRuns() - 1);
		if (player.getBrewData().getAleRuns() <= 0) {
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_VAT, EMPTY_VAT, 0, 10);
			//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_BARREL, EMPTY_BARREL, 0, 10);
			player.getActionSender().sendMessage("You get the last portion from the barrel.");
			player.getBrewData().resetDatas();
		}
	}
}
