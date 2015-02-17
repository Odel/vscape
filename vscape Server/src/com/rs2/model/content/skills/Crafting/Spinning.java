package com.rs2.model.content.skills.Crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 17:17 To change
 * this template use File | Settings | File Templates.
 */
public class Spinning {
	private static final int SPINNING_ANIMATION = 896;

	public static enum SpinningWheel {
		WOOL(34205, 1737, 1759, 1, 1, 2.5),
        WOOL5(34204, 1737, 1759, 5, 1, 2.5),
        WOOL10(34203, 1737, 1759, 10, 1, 2.5),
        WOOLX(34202, 1737, 1759, 0, 1, 2.5),
        FLAX(34209, 1779, 1777, 1, 10, 15),
        FLAX5(34208, 1779, 1777, 5, 10, 15),
        FLAX10(34207, 1779, 1777, 10, 10, 15),
        FLAXX(34206, 1779, 1777, 0, 10, 15),
	SINEW(34213, 9436, 9438, 1, 10, 15),
        SINEW5(34212, 9436, 9438, 5, 10, 15),
        SINEW10(34211, 9436, 9438, 10, 10, 15),
        SINEWX(34210, 9436, 9438, 0, 10, 15),
        ROOT(34217, 6051, 6038, 1, 19, 30),
        ROOT5(34216, 6051, 6038, 5, 19, 30),
        ROOT10(34215, 6051, 6038, 10, 19, 30),
        ROOTX(34214, 6051, 6038, 0, 19, 30);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, SpinningWheel> spinningItems = new HashMap<Integer, SpinningWheel>();

		public static SpinningWheel forId(int id) {
			return spinningItems.get(id);
		}

		static {
			for (SpinningWheel data : SpinningWheel.values()) {
				spinningItems.put(data.buttonId, data);
			}
		}

		private SpinningWheel(int buttonId, int used, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.result = result;
			this.amount = amount;
			this.level = level;
			this.experience = experience;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getUsed() {
			return used;
		}

		public int getResult() {
			return result;
		}

		public int getAmount() {
			return amount;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}
	public static boolean spin(final Player player, int buttonId, final int amount) {
		final SpinningWheel spinningWheel = SpinningWheel.forId(buttonId);
		if (spinningWheel == null || (spinningWheel.getAmount() == 0 && amount == 0))
			return false;
		if (player.getStatedInterface() == "spinning") {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(spinningWheel.getUsed())) {
				player.getDialogue().sendStatement("You need " + new Item(spinningWheel.getUsed()).getDefinition().getName().toLowerCase() + " to do this.");
				return true;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < spinningWheel.getLevel()) {
				player.getDialogue().sendStatement("You need a crafting level of " + spinningWheel.getLevel() + " to make this.");
				return true;
			}
		    player.getActionSender().removeInterfaces();
			player.getUpdateFlags().sendAnimation(SPINNING_ANIMATION);

			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				int amnt = spinningWheel.getAmount() != 0 ? spinningWheel.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task) || amnt == 0 || !player.getInventory().getItemContainer().contains(spinningWheel.getUsed())) {
						container.stop();
						return;
					}
					player.getUpdateFlags().sendAnimation(SPINNING_ANIMATION);
					player.getActionSender().sendMessage("You make the " + new Item(spinningWheel.getUsed()).getDefinition().getName() + " into a " + new Item(spinningWheel.getResult()).getDefinition().getName() + ".");
					player.getInventory().removeItem(new Item(spinningWheel.getUsed()));
					player.getInventory().addItem(new Item(spinningWheel.getResult()));
					player.getSkill().addExp(Skill.CRAFTING, spinningWheel.getExperience());
					amnt--;

				}

				@Override
				public void stop() {
					player.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
			return true;
		}
		return false;
	}

}
