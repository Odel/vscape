package com.rs2.model.content.skills.Fletching.LogCuttingAction;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 14:24 To change
 * this template use File | Settings | File Templates.
 */
public abstract class LogCutting {

	/* set of constants */

	final protected int LOG_CUTTING_ANIM = 1248;
	final protected int CUT_SOUND = 375;
	final protected int KNIFE = 946;
	final protected int ARROW_SHAFT = 52;

	/* The player instance */

	protected Player player;

	/* The id of the item used */

	protected int used;

	/* The id of the item resulting */

	protected int result;

	/* The level required for the action */

	protected int level;

	/* The experience given for the action */

	protected double experience;

	/* The amount of the action */

	protected int amount;

	/* The amount manually given for the action (reserved for enterX amount */

	protected int manualAmount;

	/* The method constructor */

	public LogCutting(Player player, int used, int result, int level, double experience, int amount, int manualAmount) {
		this.player = player;
		this.used = used;
		this.result = result;
		this.level = level;
		this.experience = experience;
		this.amount = amount;
		this.manualAmount = manualAmount;
	}

	/* The action itself */

	public void logCuttingAction() {
		player.getActionSender().removeInterfaces();
		if (!Constants.FLETCHING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (!player.getInventory().getItemContainer().contains(KNIFE)) {
			player.getDialogue().sendStatement("You need a knife to do this.");
			player.getDialogue().endDialogue();
			return;
		}
		if (!player.getInventory().getItemContainer().contains(used)) {
			player.getDialogue().sendStatement("You need a " + new Item(used).getDefinition().getName().toLowerCase() + "s to do this.");
			player.getDialogue().endDialogue();
			return;
		}
		if (player.getSkill().getLevel()[Skill.FLETCHING] < level) {
			player.getDialogue().sendStatement("You need a fletching level of " + level + " to make this.");
			player.getDialogue().endDialogue();
			return;
		}

		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			int actionAmount = amount != 0 ? amount : manualAmount;
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || actionAmount == 0 || !player.getInventory().getItemContainer().contains(used)) {
					player.setStatedInterface("");
					player.resetAnimation();
					container.stop();
					return;
				}

				player.getUpdateFlags().sendAnimation(LOG_CUTTING_ANIM);
				String resultName = new Item(result).getDefinition().getName().toLowerCase();
				player.getActionSender().sendMessage("You carefully cut the " + new Item(used).getDefinition().getName().toLowerCase() + " into " + (resultName.contains("shaft") ? "some arrow shafts" : resultName.contains("longbow") ? "a longbow" : "a shortbow") + ".");

				player.getInventory().removeItem(new Item(used));
				player.getInventory().addItem(new Item(result, result == ARROW_SHAFT ? 15 : 1));
				player.getSkill().addExp(Skill.FLETCHING, experience);
				actionAmount--;
				container.setTick(3);
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);

	}

}
