package com.rs2.model.content.skills.mining;

import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class MineEssence {

	public static void startMiningEss(final Player player) {
		if(!QuestHandler.questCompleted(player, 5))
		{
			player.getDialogue().sendStatement("You must complete Rune Mysteries","to access this skill.");
			return;
		}
		final Tool pickaxe = Tools.getTool(player, Skill.MINING);
		if (pickaxe == null) {
			player.getActionSender().sendMessage("You do not have a pickaxe that you can use.", true);
			if (player.getNewComersSide().isInTutorialIslandStage()) {
				player.getDialogue().sendStatement("You do not have a pickaxe that you can use.");
				player.setClickId(0);
			}
			return;
		}
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.", true);
			if (player.getNewComersSide().isInTutorialIslandStage()) {
				player.getDialogue().sendStatement("Not enough space in your inventory.");
				player.setClickId(0);
			}
			return;
		}
		final int task = player.getTask();
		final int ess = player.getSkill().getPlayerLevel(Skill.MINING) < 30 ? 1436 : 7936;
		final int anim = pickaxe.getAnimation();
		player.getActionSender().sendMessage("You swing your pick at the rock.", true);
		player.getUpdateFlags().sendAnimation(anim);
		player.getActionSender().sendSound(432, 0, 0);
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				player.getUpdateFlags().sendAnimation(anim);
				player.getActionSender().sendSound(432, 0, 0);
				if (!player.getInventory().addItem(new Item(ess, 1))) {
					container.stop();
					return;
				} else {
					player.getSkill().addExp(Skill.MINING, 5);
				}
			}
			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), pickaxe.getEssSpeed());
	}

}
