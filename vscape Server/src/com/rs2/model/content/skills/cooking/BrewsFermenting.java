package com.rs2.model.content.skills.cooking;

import java.util.HashMap;

import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 25/12/11 Time: 02:10 To change
 * this template use File | Settings | File Templates.
 */
public class BrewsFermenting {

	public static final int PUTTING_ANIM = 1649;
	public static final Position KELDAGRIM_VAT = new Position(2917, 10194);
	public static final Position KELDAGRIM_BARREL = new Position(2917, 10193);

	public static enum FermentingData {
		CIDER(5767, 7489, 7490, 14), DWARVEN_STOUT(5767, 7444, 7445, 19), ASGANIAN_ALE(5767, 7449, 7450, 24), GREENMAN_ALE(5767, 7454, 7455, 29), WIZARD_MINDBOMB(5767, 7459, 7460, 34), DRAGON_BITTER(5767, 7464, 7465, 39), MOONLIGHT_MEAD(5767, 7469, 7470, 44), AXEMAN_FOLLEY(5767, 7474, 7475, 49), CHEF_DELIGHT(5767, 7479, 7480, 54), SLAYER_RESPITE(5767, 7484, 7485, 59);

		private int used;
		private int objectId;
		private int newObjectId;
		private int level;

		public static HashMap<Integer, FermentingData> fermentingItems = new HashMap<Integer, FermentingData>();

		public static FermentingData forId(int id) {
			return fermentingItems.get(id);
		}

		static {
			for (FermentingData data : FermentingData.values()) {
				fermentingItems.put(data.objectId, data);
			}
		}

		private FermentingData(int used, int objectId, int newObjectId, int level) {
			this.used = used;
			this.objectId = objectId;
			this.newObjectId = newObjectId;
			this.level = level;
		}

		public int getUsed() {
			return used;
		}

		public int getObjectId() {
			return objectId;
		}

		public int getNewObjectId() {
			return newObjectId;
		}

		public int getLevel() {
			return level;
		}

	}

	public static void handleFermentingVat(Player player, int itemId, int objectId) {
		FermentingData fermentingData = FermentingData.forId(objectId);
		if (fermentingData == null)
			return;
		if (itemId != fermentingData.getUsed() || objectId == fermentingData.getObjectId())
			return;
		//player.getActionSender().sendNewObject(KELDAGRIM_VAT, fermentingData.getNewObjectId(), 0, 10);
		if (player.getSkill().getLevel()[Skill.COOKING] < fermentingData.getLevel()) {
			player.getDialogue().sendStatement("You need a cooking level of " + fermentingData.getLevel() + " to do this.");
			return;
		}
		if (!player.getInventory().playerHasItem(fermentingData.getUsed())) {
			player.getDialogue().sendStatement("You will need a " + ItemDefinition.forId(fermentingData.getUsed()).getName().toLowerCase() + " to do this");
			return;
		}

		player.getActionSender().sendMessage("You put a " + ItemDefinition.forId(fermentingData.getUsed()).getName().toLowerCase() + " in the fermenting vat.");
		player.getInventory().removeItem(new Item(fermentingData.getUsed()));
		player.getInventory().addItem(new Item(1931));
		player.getUpdateFlags().sendAnimation(PUTTING_ANIM);
		//player.getBrewData().setFermentingDate(Calendars.getYear(), Calendars.getMonth(), Calendars.getDay(), Calendars.HourOfYear());
		player.getBrewData().setAleRuns(8);
	}

}
