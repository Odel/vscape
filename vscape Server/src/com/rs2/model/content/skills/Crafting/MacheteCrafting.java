package com.rs2.model.content.skills.Crafting;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class MacheteCrafting {
	
	public static final int MACHETE = 975;
	
	public static enum MacheteData {
		OPAL_MACHETE(1609, 3, 6313, 17, 13),
		JADE_MACHET(1611, 4, 6315, 20, 17),
		TOPAZ_MACHET(1613, 6, 6317, 23, 21);
		
		private int gemId;
		private int gemsRequired;
		private int macheteId;
		private int level;
		private double experience;
		
		private MacheteData(int gemId, int gemsRequired, int macheteId, int level, double experience) {
			this.gemId = gemId;
			this.gemsRequired = gemsRequired;
			this.macheteId = macheteId;
			this.level = level;
			this.experience = experience;
		}
		
		public static MacheteData forId(int item) {
			for (MacheteData macheteData : MacheteData.values()) {
				if (macheteData.gemId == item)
					return macheteData;
			}
			return null;
		}
		
		public int getGemId() {
			return gemId;
		}
		
		public int getGemsRequired() {
			return gemsRequired;
		}
		
		public int getMacheteId() {
			return macheteId;
		}
		
		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}
	}
	
	public static boolean handleMacheteCraft(final Player player, int selectedItemId, int usedItemId, final int slot) {
		final int itemId = selectedItemId != MACHETE ? selectedItemId : usedItemId;
		final MacheteData macheteData = MacheteData.forId(itemId);
		if(macheteData != null) {
			if((selectedItemId == macheteData.getGemId() && usedItemId != MACHETE) || 
					(selectedItemId == MACHETE && usedItemId != macheteData.getGemId()))
			{
				return false;
			}
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(MACHETE)) {
				return true;
			} 
			if (player.getSkill().getLevel()[Skill.CRAFTING] < macheteData.getLevel()) {
				player.getDialogue().sendStatement("You need a Crafting level of " + macheteData.getLevel() + " to attach this.");
				return true;
			}
			if(!player.getInventory().playerHasItem(macheteData.getGemId(), macheteData.getGemsRequired())) {
				String item1 = new Item(macheteData.getGemId()).getDefinition().getName().toLowerCase();
			    player.getActionSender().sendMessage("You need "+macheteData.getGemsRequired()+" "+item1+" to alter this blade!");
			    return true;
			}
		//	player.getUpdateFlags().sendAnimation(tipData.getAnimation());
			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task)) {
						container.stop();
						return;
					}
					String item1 = new Item(macheteData.getGemId()).getDefinition().getName().toLowerCase();
					player.getInventory().removeItem(new Item(MACHETE, 1));
					if (player.getInventory().removeItemSlot(new Item(itemId), slot)) {
						player.getInventory().removeItem(new Item(itemId, macheteData.getGemsRequired()-1));
						player.getInventory().addItemToSlot(new Item(macheteData.getMacheteId(), 1), slot);
					} else if (player.getInventory().removeItem(new Item(itemId, macheteData.getGemsRequired()))) {
						player.getInventory().addItem(new Item(macheteData.getMacheteId(), 1));
					}
					player.getActionSender().sendMessage("You attach the "+item1+" to the back of the machete.");
					player.getSkill().addExp(Skill.CRAFTING, macheteData.getExperience());
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
