package com.rs2.model.content.skills.Crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 16:05 To change
 * this template use File | Settings | File Templates.
 */
public class SilverCrafting {
	public static final int SILVER_BAR = 2355;
	private static final int SILVER_ANIMATION = 899;

	public static enum SilverCraft {
		UNSTRUNG(34205, 2355, 1599, 1714, 1, 16, 50),
		UNSTRUNG5(34204, 2355, 1599, 1714, 5, 16, 50),
		UNSTRUNG10(34203, 2355, 1599, 1714, 10, 16, 50),
		UNSTRUNGX(34202, 2355, 1599, 1714, 0, 16, 50),
		SICKLE(34209, 2355, 2976, 2961, 1, 18, 50),
		SICKLE5(34208, 2355, 2976, 2961, 5, 18, 50),
		SICKLE10(34207, 2355, 2976, 2961, 10, 18, 50),
		SICKLEX(34206, 2355, 2976, 2961, 0, 18, 50),
		TIARA(34213, 2355, 5523, 5525, 1, 23, 52.5),
		TIARA5(34212, 2355, 5523, 5525, 5, 23, 52.5),
		TIARA10(34211, 2355, 5523, 5525, 10, 23, 52.5),
		TIARAX(34210, 2355, 5523, 5525, 0, 23, 52.5),
		EMBLEM(34217, 2355, 1594, 1720, 1, 17, 50),
		EMBLEM5(34216, 2355, 1594, 1720, 5, 17, 50),
		EMBLEM10(34215, 2355, 1594, 1720, 10, 17, 50),
		EMBLEMX(34214, 2355, 1594, 1720, 0, 17, 50);

		private int buttonId;
		private int used;
		private int mould;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, SilverCraft> silverItems = new HashMap<Integer, SilverCraft>();

		public static SilverCraft forId(int id) {
			return silverItems.get(id);
		}

		static {
			for (SilverCraft data : SilverCraft.values()) {
				silverItems.put(data.buttonId, data);
			}
		}

		private SilverCraft(int buttonId, int used, int mould, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.mould = mould;
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
		
		public int getMould() {
			return mould;
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
	public static boolean makeSilver(final Player player, int buttonId, final int amount) {
		final SilverCraft silverCraft = SilverCraft.forId(buttonId);
		if (silverCraft == null || (silverCraft.getAmount() == 0 && amount == 0)) {
			return false;
		}
		if (silverCraft.getUsed() == SILVER_BAR && player.getStatedInterface() == "silverCrafting") {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(SILVER_BAR)) {
				player.getDialogue().sendStatement("You need a silver bar to do this.");
				return true;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < silverCraft.getLevel()) {
				player.getDialogue().sendStatement("You need a crafting level of " + silverCraft.getLevel() + " to make this.");
				return true;
			}
			if(!player.getInventory().getItemContainer().contains(silverCraft.getMould()))
			{
				String mouldName = new Item(silverCraft.getMould()).getDefinition().getName();
				player.getDialogue().sendStatement("You need a "+mouldName+" to do this.");
				return true;
			}
			player.getUpdateFlags().sendAnimation(SILVER_ANIMATION);
		    player.getActionSender().removeInterfaces();

			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				int amnt = silverCraft.getAmount() != 0 ? silverCraft.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task) || amnt == 0 || !player.getInventory().getItemContainer().contains(SILVER_BAR)) {
						container.stop();
						return;
					}
					container.setTick(3);
					player.getUpdateFlags().sendAnimation(SILVER_ANIMATION);
					player.getActionSender().sendMessage("You make the silver bar into " + Menus.determineAorAn(new Item(silverCraft.getResult()).getDefinition().getName().toLowerCase()) + " " + new Item(silverCraft.getResult()).getDefinition().getName().toLowerCase() + ".");
					player.getInventory().removeItem(new Item(SILVER_BAR));
					player.getInventory().addItem(new Item(silverCraft.getResult()));
					player.getSkill().addExp(Skill.CRAFTING, silverCraft.getExperience());
					amnt--;
				}
				@Override
				public void stop() {
					player.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
			return true;
		}
		return false;
	}
	
	public static boolean chaosAltar(Player player, int object, int item, int x, int y) {
                if((item == 1722 && object == 411)) {
                    player.getActionSender().sendMessage("You vigorously rub the Unpowered Symbol on the altar.");
                    player.getInventory().removeItem(new Item(1722, 1));
                    player.getInventory().addItem(new Item(1724, 1));
                    return true;
                }
                else {
                    return false;
                }
        }

}
