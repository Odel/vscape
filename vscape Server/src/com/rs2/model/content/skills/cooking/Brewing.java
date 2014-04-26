package com.rs2.model.content.skills.cooking;

import java.util.HashMap;

import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 23/12/11 Time: 22:11 To change
 * this template use File | Settings | File Templates.
 */
public class Brewing {

	public static final int BUCKET_ANIM = 895;
	public static final int PUTTING_ANIM = 1649;

	public static enum BrewData {

		CIDER(5992, 4, 7441, 7489, 7490, 7491, 7492, 7493, 7429, 7430, 14, 182), DWARVEN_STOUT(5994, 4, 7441, 7444, 7445, 7446, 7447, 7448, 7411, 7412, 19, 215), ASGANIAN_ALE(5996, 4, 7441, 7449, 7450, 7451, 7452, 7453, 7413, 7414, 24, 248), GREENMAN_ALE(255, 4, 7441, 7454, 7455, 7456, 7457, 7458, 7415, 7416, 29, 281), WIZARD_MINDBOMB(5998, 4, 7441, 7459, 7460, 7461, 7462, 7463, 7417, 7418, 34, 314), DRAGON_BITTER(6000, 4, 7441, 7464, 7465, 7466, 7467, 7468, 7419, 7420, 39, 347), MOONLIGHT_MEAD(
				6004, 4, 7441, 7469, 7470, 7471, 7472, 7473, 7421, 7422, 44, 380), AXEMAN_FOLLEY(6043, 1, 7441, 7474, 7475, 7476, 7477, 7478, 7423, 7424, 49, 347), CHEF_DELIGHT(1975, 4, 7441, 7479, 7480, 7481, 7482, 7483, 7425, 7426, 54, 380), SLAYER_RESPITE(6002, 4, 7441, 7484, 7485, 7486, 7487, 7488, 7427, 7428, 59, 413), BUCKER_OF_WATER(1929, 4, 7494, 7438, -1, -1, -1, -1, -1, -1, 1, 446), BARLEY_MALT(6008, 2, 7438, 7441, -1, -1, -1, -1, 1, -1, -1, 479);

		private int used;
		private int amount;
		private int objectId;
		private int newObjectId;
		private int fermentingObject;
		private int fermentingObject2;
		private int finishedObject;
		private int matureObject;
		private int fullBarrel;
		private int matureBarrel;
		private int level;
		private double experience;

		public static HashMap<Integer, BrewData> brewItems = new HashMap<Integer, BrewData>();
		public static HashMap<Integer, BrewData> brewObject = new HashMap<Integer, BrewData>();

		public static BrewData forId(int id) {
			return brewItems.get(id);
		}

		public static BrewData forObjectId(int id) {
			return brewObject.get(id);
		}

		static {
			for (BrewData data : BrewData.values()) {
				brewItems.put(data.used, data);
				brewObject.put(data.newObjectId, data);
			}
		}

		private BrewData(int used, int amount, int objectId, int newObjectId, int fermentingObject, int fermentingObject2, int finishedObject, int matureObject, int fullBarrel, int matureBarrel, int level, double experience) {
			this.used = used;
			this.amount = amount;
			this.objectId = objectId;
			this.newObjectId = newObjectId;
			this.fermentingObject = fermentingObject;
			this.fermentingObject2 = fermentingObject2;
			this.finishedObject = finishedObject;
			this.matureObject = matureObject;
			this.fullBarrel = fullBarrel;
			this.matureBarrel = matureBarrel;
			this.level = level;
			this.experience = experience;
		}

		public int getUsed() {
			return used;
		}

		public int getAmount() {
			return amount;
		}

		public int getObjectId() {
			return objectId;
		}

		public int getNewObjectId() {
			return newObjectId;
		}

		public int getFermentingObject() {
			return fermentingObject;
		}

		public int getFermentingObject2() {
			return fermentingObject2;
		}

		public int getFinishedObject() {
			return finishedObject;
		}

		public int getMatureObject() {
			return matureObject;
		}

		public int getFullBarrel() {
			return fullBarrel;
		}

		public int getMatureBarrel() {
			return matureBarrel;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}

	public static void handleFermentingVat(Player player, int itemId, int objectId) {
		BrewData brewData = BrewData.forId(itemId);
		if (brewData == null)
			return;
		if (itemId != brewData.getUsed() || objectId != brewData.getObjectId())
			return;
		//player.getActionSender().sendNewObject(BrewsFermenting.KELDAGRIM_VAT, brewData.getNewObjectId(), 0, 10);
		player.getBrewData().setDatas(brewData.getNewObjectId(), brewData.getFermentingObject(), brewData.getFermentingObject2(), brewData.getFinishedObject(), brewData.getMatureObject(), brewData.getFullBarrel(), brewData.getMatureBarrel());
		if (player.getSkill().getLevel()[Skill.COOKING] < brewData.getLevel()) {
			player.getDialogue().sendStatement("You need a cooking level of " + brewData.getLevel() + " to do this.");
			return;
		}
		if (!player.getInventory().playerHasItem(brewData.getUsed(), brewData.getAmount())) {
			player.getDialogue().sendStatement("You will need " + brewData.getAmount() + " " + ItemDefinition.forId(brewData.getUsed()).getName().toLowerCase() + " to do this");
			return;
		}
		player.getActionSender().sendMessage(brewData.getUsed() != FlourRelated.BUCKET_OF_WATER ? "You put " + brewData.getAmount() + " " + ItemDefinition.forId(brewData.getUsed()).getName().toLowerCase() + " in the fermenting vat." : "You fill the Fermenting vat with " + brewData.getAmount() + " " + ItemDefinition.forId(brewData.getUsed()).getName().toLowerCase() + ".");
		player.getInventory().removeItem(new Item(brewData.getUsed(), brewData.getAmount()));
		if (brewData.getUsed() == FlourRelated.BUCKET_OF_WATER)
			player.getInventory().addItem(new Item(FlourRelated.BUCKET, brewData.getAmount()));
		player.getUpdateFlags().sendAnimation(brewData.getUsed() == FlourRelated.BUCKET_OF_WATER ? BUCKET_ANIM : PUTTING_ANIM);
	}

}
