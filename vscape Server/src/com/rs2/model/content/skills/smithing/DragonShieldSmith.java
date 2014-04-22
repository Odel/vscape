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
public class DragonShieldSmith 
{
	public static boolean canSmith(final Player player) 
	{
		if (!SkillHandler.hasRequiredLevel(player, Skill.SMITHING, 60, "repair this")) {
			return false;
		}
		if (!player.getInventory().getItemContainer().contains(2347)) {
			player.getActionSender().sendMessage("You need a hammer to smith on an anvil.");
			return false;
		}
		if(!player.carryingItem(2366) || !player.carryingItem(2368))
		{
			player.getActionSender().sendMessage("You need both shield halves.");
			return false;
		}
		return true;
	}
	
	public static void smithShield(final Player player)
	{
		if (!SkillHandler.hasRequiredLevel(player, Skill.SMITHING, 60, "repair this")) {
			return;
		}
		if (!player.getInventory().getItemContainer().contains(2347)) {
			player.getActionSender().sendMessage("You need a hammer to smith on an anvil.");
			return;
		}
		if(!player.carryingItem(2366) || !player.carryingItem(2368))
		{
			player.getActionSender().sendMessage("You need both shield halves.");
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
				if(!player.carryingItem(2366) || !player.carryingItem(2368))
				{
					player.getActionSender().sendMessage("You need both shield halves.");
					container.stop();
					return;
				}
				player.getInventory().removeItem(new Item(2366, 1));
				player.getInventory().removeItem(new Item(2368, 1));
				player.getInventory().addItem(new Item(1187, 1));
				player.getSkill().addExp(Skill.SMITHING, 75);
				Dialogues.sendDialogue(player, 10013, 3, 0);
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
