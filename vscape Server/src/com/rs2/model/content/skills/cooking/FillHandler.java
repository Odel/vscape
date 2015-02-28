package com.rs2.model.content.skills.cooking;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class FillHandler 
{
	private Player player;
	
	public FillHandler(Player player) {
		this.player = player;
	}
	
	public enum FillData {
		WATERSKIN(new int[]{1831,1829,1827,1825}, 1823),
		VIAL(new int[]{229}, 227),
		BOWL(new int[]{1923}, 1921),
		JUG(new int[]{1935}, 1937),
		CUP(new int[]{1980}, 4458),
		WATERINGCAN(new int[]{5331,5333,5334,5335,5336,5337,5338,5339}, 5340),
		BUCKET(new int[]{1925}, 1929);

		private int[] emptyIds;
		private int filledId;

		private FillData(int[] emptyIds, int filledId) {
			this.emptyIds = emptyIds;
			this.filledId = filledId;
		}

		public int[] getEmptyIds() {
			return emptyIds;
		}

		public int getFilledId() {
			return filledId;
		}
		
		public static FillData forId(int itemId) {
			for (FillData fillData : FillData.values()) {
				for(int emptyId : fillData.emptyIds)
					if (emptyId == itemId)
						return fillData;
			}
			return null;
		}
	}
	
	public boolean handleInterface(int item, int objectId, int objectX, int objectY) {
		FillData fill = FillData.forId(item);
		if (fill == null)
			return false;
		player.getActionSender().removeInterfaces();
		player.setOldObject(objectId);
		final CacheObject obj = ObjectLoader.object(objectId, objectX, objectY, player.getPosition().getZ());
		final GameObjectDef def = SkillHandler.getObject(objectId, objectX, objectY, player.getPosition().getZ());
        if (obj != null || def != null) {
			String name = GameObjectData.forId(obj != null ? obj.getDef().getId() : def.getId()).getName().toLowerCase();
			if (name.equalsIgnoreCase("fountain") || name.equalsIgnoreCase("sink") || name.equalsIgnoreCase("tap") || name.equalsIgnoreCase("waterpump") || name.equalsIgnoreCase("well") || name.equalsIgnoreCase("fairy fountain")) {
				player.setNewSkillTask();
				player.setTempInteger(item);
				player.setStatedInterface("fillWater");
				Item itemDef = new Item(fill.filledId);
				Menus.display1Item(player, fill.filledId, itemDef.getDefinition().getName());
				return true;
			}
        }
		return false;
	}
	
	public static void handleFillTick(final Player player, final int amount) {
		final int task = player.getTask();
		player.getMovementHandler().reset();
		player.getActionSender().removeInterfaces();
		player.setSkilling(new CycleEvent() {
			int fillAmount = amount;
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkNewSkillTask() || !player.checkTask(task) || !player.getInventory().getItemContainer().contains(player.getTempInteger()) || fillAmount == 0) {
					player.setTempInteger(0);
					container.stop();
					return;
				}
				handleFill(player);
				fillAmount--;
				container.setTick(2);
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
	}
	
	public static void handleFill(final Player player) {
		final int item = player.getTempInteger();
		FillData fill = FillData.forId(item);
		if (fill == null)
		{
			return;
		}
		if (!player.getInventory().getItemContainer().contains(item))
		{
			return;
		}
		player.getActionSender().removeInterfaces();
		player.getUpdateFlags().sendAnimation(883);
		player.getInventory().removeItem(new Item(item));
		player.getInventory().addItem(new Item(fill.filledId));
		player.getActionSender().sendMessage("You fill the "+new Item(item).getDefinition().getName()+" with water.");
	}
	
	public static boolean handleButtons(Player player, int buttonId) {
		switch (buttonId) {
			case 10239:
				handleFillTick(player, 1);
				return true;
			case 10238:
				handleFillTick(player, 5);
				return true;
			case 6211:
				handleFillTick(player, 28);
				return true;
		}
		return false;
	}
}
