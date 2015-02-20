package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import java.util.Random;

public class MixingRunes {

	public final static int BINDING_NECKLACE = 5521;
	
	public enum MixingRunesData {
		MIST_RUNES_AIR(1438, 556, 2480, 4695, 6, 8), MIST_RUNES_WATER(1444, 555, 2478, 4695, 6, 8), DUST_RUNES_AIR(1438, 556, 2481, 4696, 10, 8.3), DUST_RUNES_EARTH(1440, 557, 2478, 4696, 10, 8.3), MUD_RUNES_WATER(1444, 555, 2481, 4698, 13, 9.3), MUD_RUNES_EARTH(1440, 557, 2480, 4698, 13, 9.3), SMOKE_RUNES_AIR(1438, 556, 2482, 4697, 15, 9.5), SMOKE_RUNES_FIRE(1442, 554, 2478, 4697, 15, 9.5), STEAM_RUNES_WATER(1444, 555, 2482, 4694, 19, 10), STEAM_RUNES_FIRE(1442, 554, 2480, 4694, 19, 10), LAVA_RUNES_EARTH(1440, 557, 2482, 4699, 23, 10.5), LAVA_RUNES_FIRE(1442, 554, 2481, 4699, 23, 10.5), ;
		private int talismanId;
		private int elementalRuneId;
		private int altarId;
		private int combinedRune;
		private int levelRequired;
		private double experienceReceived;

		public static MixingRunesData forId(int itemUsed, int objectId) {
			for (MixingRunesData mixingRunesData : MixingRunesData.values()) {
				if (itemUsed == mixingRunesData.talismanId && objectId == mixingRunesData.altarId) {
					return mixingRunesData;
				}
			}
			return null;
		}
		MixingRunesData(int talismanId, int elementalRuneId, int altarId, int combinedRune, int levelRequired, double experienceReceived) {
			this.talismanId = talismanId;
			this.elementalRuneId = elementalRuneId;
			this.altarId = altarId;
			this.combinedRune = combinedRune;
			this.levelRequired = levelRequired;
			this.experienceReceived = experienceReceived;
		}

		public int getTalismanId() {
			return talismanId;
		}

		public int getElementalRuneId() {
			return elementalRuneId;
		}

		public int getCombinedRune() {
			return combinedRune;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperienceReceived() {
			return experienceReceived;
		}
	}

	public static boolean combineRunes(final Player player, int itemUsed, int objectId) {
		final MixingRunesData mixingRunesData = MixingRunesData.forId(itemUsed, objectId);
		if (mixingRunesData == null) {
			return false;
		}
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		if(!QuestHandler.questCompleted(player, 5))
		{
			player.getDialogue().sendStatement("You must complete Rune Mysteries to access this skill.");
			return true;
		}
		if (!player.getInventory().playerHasItem(mixingRunesData.getTalismanId())) {
			player.getActionSender().sendMessage("You need a " + ItemManager.getInstance().getItemName(mixingRunesData.getTalismanId()).toLowerCase() + " to do this.");
			return true;
		}
		if (!player.getInventory().playerHasItem(mixingRunesData.getElementalRuneId()) || !player.getInventory().playerHasItem(Runecrafting.PureEss)) {
			player.getActionSender().sendMessage("You need pure essences to do this.");
			return true;
		}
		if (player.getSkill().getPlayerLevel(Skill.RUNECRAFTING) < mixingRunesData.getLevelRequired()) {
			player.getActionSender().sendMessage("You need a runecrafting level of " + mixingRunesData.getLevelRequired() + " to do this.");
			return true;
		}
		player.getInventory().removeItem(new Item(mixingRunesData.getTalismanId(), 1));
		player.getActionSender().sendMessage("You attempt to bind the Runes...");
		player.getUpdateFlags().sendAnimation(791);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			b.stop();
		    }

		    @Override
		    public void stop() {
			player.setStopPacket(false);
			if (player.getEquipment().getId(Constants.AMULET) == BINDING_NECKLACE) {
			    player.setBindingNeckCharge(player.getBindingNeckCharge() - 1);
			    if (player.getBindingNeckCharge() <= 0) {
				player.setBindingNeckCharge(15);
				player.getEquipment().removeAmount(Constants.AMULET, 1);
				player.getEquipment().refresh();
				player.getActionSender().sendMessage("You successfully bind the runes together. Your binding necklace crumbles into dust.");
			    } else {
				player.getActionSender().sendMessage("You successfully bind the runes together. You feel your necklace uncharge slightly.");
			    }
			} else {
			    player.getActionSender().sendMessage("You manage to bind most of the runes together with the power of the Talisman.");
			}
			while (player.getInventory().playerHasItem(mixingRunesData.getElementalRuneId()) && player.getInventory().playerHasItem(Runecrafting.PureEss)) {
			    player.getInventory().removeItem(new Item(mixingRunesData.getElementalRuneId(), 1));
			    player.getInventory().removeItem(new Item(Runecrafting.PureEss, 1));
			    if ((50 >= (new Random().nextDouble() * 100)) || player.getEquipment().getId(Constants.AMULET) == BINDING_NECKLACE) {
				player.getInventory().addItem(new Item(mixingRunesData.getCombinedRune(), 1));
				player.getSkill().addExp(Skill.RUNECRAFTING, mixingRunesData.getExperienceReceived());
			    }
			}
		    }
		}, 3);
		return true;
	}
}
