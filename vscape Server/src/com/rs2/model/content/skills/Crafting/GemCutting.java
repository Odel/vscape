package com.rs2.model.content.skills.Crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 13:53 To change
 * this template use File | Settings | File Templates.
 */
public class GemCutting {
	public static final int CHISEL = 1755;
	public static final int CRUSHED_GEM = 1633;
	
	public static final int CRUSH_CHANCE = 8;

	public static boolean handleCutting(final Player player, int selectedItemId, int usedItemId, final int slot) {
		final int itemId = selectedItemId != CHISEL ? selectedItemId : usedItemId;
		final gemData gem = gemData.forId(itemId);
		if (gem != null) {
			if((selectedItemId == gem.getId() && usedItemId != CHISEL) || 
					(selectedItemId == CHISEL && usedItemId != gem.getId()))
			{
				return false;
			}
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(CHISEL)) {
				return true;
			} else if (player.getSkill().getLevel()[Skill.CRAFTING] < gem.getLevel()) {
				player.getDialogue().sendStatement("You need a crafting level of " + gem.getLevel() + " to cut this gem.");// wrong
				return true;
			}
			player.getUpdateFlags().sendAnimation(gem.getAnimation());
			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task)) {
						container.stop();
						return;
					}
					boolean shouldCrush = (Misc.random(CRUSH_CHANCE) == 0 && gem.isCrushable());
					player.getInventory().replaceItemWithItem(new Item(itemId), new Item(shouldCrush ? CRUSHED_GEM : gem.getGemId()));
					if(shouldCrush) { player.getActionSender().sendMessage("You accidentally crush the gem."); }
					player.getSkill().addExp(Skill.CRAFTING, shouldCrush ? 5 : gem.getExperience());
					container.stop();
				}

				@Override
				public void stop() {
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
			return true;
		}
        return false;
	}

	public static enum gemData {// uncut, gem, level, anim, experience
		SAPPHIRE(1623, 1607, 20, 888, 50, false), 
		EMERALD(1621, 1605, 27, 889, 67, false), 
		RUBY(1619, 1603, 34, 887, 85, false), 
		DIAMOND(1617, 1601, 43, 886, 107.5, false),  
		DRAGONSTONE(1631, 1615, 55, 885, 137.5, false), 
		ONYX(6571, 6573, 67, 885, 168, false), 
		OPAL(1625, 1609, 1, 891, 15, true), 
		JADE(1627, 1611, 13, 890, 20, true), 
		RED_TOPAZ(1629, 1613, 16, 887, 25, true),
		MYRE_SNELM_SMALL(3345, 3327, 15, 6702, 32.5, false),
		MYRE_SNELM_LARGE(3355, 3337, 15, 6702, 32.5, false),
		BLOOD_SNELM_SMALL(3347, 3329, 15, 6702, 32.5, false),
		BLOOD_SNELM_LARGE(3357, 3339, 15, 6702, 32.5, false),
		OCHRE_SNELM_SMALL(3349, 3331, 15, 6702, 32.5, false),
		OCHRE_SNELM_LARGE(3359, 3341, 15, 6702, 32.5, false),
		BLUE_SNELM_SMALL(3351, 3333, 15, 6702, 32.5, false),
		BLUE_SNELM_LARGE(3361, 3343, 15, 6702, 32.5, false),
		BARK_SNELM(3353, 3335, 15, 6702, 32.5, false);

		private short uncutId;
		private short gemId;
		private byte level;
		private short animId;
		private double experience;
		private boolean crushable;

		public static HashMap<Integer, gemData> craftinggems = new HashMap<Integer, gemData>();

		public static gemData forId(int id) {
			return craftinggems.get(id);
		}

		static {
			for (gemData c : gemData.values()) {
				craftinggems.put(c.getId(), c);
			}
		}

		private gemData(int uncutId, int gemId, int level, int animId, double experience, boolean crushable) {
			this.uncutId = (short) uncutId;
			this.gemId = (short) gemId;
			this.level = (byte) level;
			this.animId = (short) animId;
			this.experience = experience;
			this.crushable = crushable;
		}

		public int getId() {
			return uncutId;
		}

		public int getGemId() {
			return gemId;
		}

		public int getLevel() {
			return level;
		}

		public int getAnimation() {
			return animId;
		}

		public double getExperience() {
			return experience;
		}
		
		public boolean isCrushable() {
			return crushable;
		}
	}
}
