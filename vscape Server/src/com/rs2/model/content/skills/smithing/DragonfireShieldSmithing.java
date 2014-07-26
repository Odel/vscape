package com.rs2.model.content.skills.smithing;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

//made seperate class to handle this since the main smithing class is not currently good enough
//to handle misc smithing
public class DragonfireShieldSmithing 
{
	public static boolean canSmith(final Player player) 
	{
		if (!SkillHandler.hasRequiredLevel(player, Skill.SMITHING, 90, "create this")) {
			return false;
		}
		if (!player.getInventory().getItemContainer().contains(2347)) {
			player.getActionSender().sendMessage("You need a hammer to smith on an anvil.");
			return false;
		}
		if(!player.carryingItem(1540) || !player.carryingItem(11286))
		{
			player.getActionSender().sendMessage("You need both an anti-dragon shield and the draconic visage.");
			return false;
		}
		return true;
	}
	
	public static void smithShield(final Player player)
	{
		if (!SkillHandler.hasRequiredLevel(player, Skill.SMITHING, 90, "create this")) {
			return;
		}
		if (!player.getInventory().getItemContainer().contains(2347)) {
			player.getActionSender().sendMessage("You need a hammer to smith on an anvil.");
			return;
		}
		if(!player.carryingItem(1540) || !player.carryingItem(11286))
		{
			player.getActionSender().sendMessage("You need both an anti-dragon shield and the draconic visage.");
			return;
		}
		final int task = player.getTask();
		player.getUpdateFlags().sendAnimation(898);
		player.getActionSender().sendSound(468, 0, 0);
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				if (!player.carryingItem(1540) || !player.carryingItem(11286)) {
					player.getActionSender().sendMessage("You need both an anti-dragon shield and the draconic visage.");
					return;
				}
				player.getInventory().removeItem(new Item(1540, 1));
				player.getInventory().removeItem(new Item(11286, 1));
				player.getInventory().addItem(new Item(11284, 1));
				player.getSkill().addExp(Skill.SMITHING, 2000);
				Dialogues.sendDialogue(player, 10604, 3, 0);
				container.stop();
			}
			@Override
			public void stop() {
				player.getTask();
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 4);
	}
	
}
