package com.rs2.model.content.skills.Fletching;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class GemTips {
	public static final int CHISEL = 1755;
	
	public static enum TipData {
		OPAL_TIP(1609, 45, 12, 891, 11, 1.5),
		JADE_TIP(1611, 9187, 12, 890, 26, 2.4),
		PEARL_TIP(411, 46, 6, 891, 41, 3.2),
		PEARL_TIPB(413, 46, 24, 891, 41, 3.2),
		TOPAZ_TIP(1613, 9188, 12, 887, 48, 3.9),
		SAPPHIRE_TIP(1607, 9189, 12, 888, 56, 4),
		EMERALD_TIP(1605, 9190, 12, 889, 58, 5.5),
		RUBY_TIP(1603, 9191, 12, 887, 63, 6.3),
		DIAMOND_TIP(1601, 9192, 12, 886, 65, 7),
		DRAGONSTONE_TIP(1615, 9193, 12, 885, 71, 8.2),
		ONYX_TIP(6573, 9194, 24, 885, 73, 9.4);
		
		private int gemId;
		private int tipId;
		private int amount;
		private int animId;
		private int level;
		private double experience;
		
		private TipData(int gemId, int tipId, int amount, int animId, int level, double experience) {
			this.gemId = gemId;
			this.tipId = tipId;
			this.amount = amount;
			this.animId = animId;
			this.level = level;
			this.experience = experience;
		}
		
		public static TipData forId(int item) {
			for (TipData tipData : TipData.values()) {
				if (tipData.gemId == item)
					return tipData;
			}
			return null;
		}
		
		public int getGemId() {
			return gemId;
		}
		
		public int getTipId() {
			return tipId;
		}
		
		public int getAmount() {
			return amount;
		}
		
		public int getAnimation() {
			return animId;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}
	}
	
	public static boolean handleCutting(final Player player, int selectedItemId, int usedItemId, final int slot) {
		final int itemId = selectedItemId != CHISEL ? selectedItemId : usedItemId;
		final TipData tipData = TipData.forId(itemId);
		if(tipData != null) {
			if (!Constants.FLETCHING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(CHISEL)) {
				return true;
			} else if (player.getSkill().getLevel()[Skill.FLETCHING] < tipData.getLevel()) {
				player.getDialogue().sendStatement("You need a fletching level of " + tipData.getLevel() + " to cut this gem into bolt tips.");// wrong
				return true;
			}
			player.getUpdateFlags().sendAnimation(tipData.getAnimation());
			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task)) {
						container.stop();
						return;
					}
					String item1 = new Item(tipData.getGemId()).getDefinition().getName().toLowerCase();
					String item2 = new Item(tipData.getTipId()).getDefinition().getName().toLowerCase();
					if (player.getInventory().removeItemSlot(new Item(itemId), slot)) {
						player.getInventory().addItemToSlot(new Item(tipData.getTipId(), tipData.getAmount()), slot);
					} else if (player.getInventory().removeItem(new Item(itemId))) {
						player.getInventory().addItem(new Item(tipData.getTipId(), tipData.getAmount()));
					}
					player.getActionSender().sendMessage("You cut the "+item1+" into "+tipData.getAmount()+" "+item2+".");
					player.getSkill().addExp(Skill.FLETCHING, tipData.getExperience());
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
		
}
