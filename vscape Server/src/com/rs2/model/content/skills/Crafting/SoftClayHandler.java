package com.rs2.model.content.skills.Crafting;

import com.rs2.model.content.skills.Menus;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class SoftClayHandler {
    private static final int CLAY = 434;
    private static final int SOFT_CLAY = 1761;
    private static final int BUCKET_OF_WATER = 1929;
    private static final int BUCKET = 1925;
    
    public static boolean itemOnItemHandling(Player player, int firstItem, int secondItem) {
		if(firstItem == CLAY && secondItem == BUCKET_OF_WATER || 
				firstItem == BUCKET_OF_WATER && secondItem == CLAY) {
			player.setNewSkillTask();
			if(player.getInventory().getItemAmount(BUCKET_OF_WATER) == 1)
			{
				createTick(player, 1);
				return true;
			}
			player.setStatedInterface("softClayMake");
			Item itemDef = new Item(SOFT_CLAY);
			Menus.display1Item(player, SOFT_CLAY, itemDef.getDefinition().getName());
		}
		return false;
    }
    
    private static void createTick(final Player player, final int count){
		final int task = player.getTask();
		player.getMovementHandler().reset();
		player.getActionSender().removeInterfaces();
		player.setSkilling(new CycleEvent() {
			int crateAmount = count;
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkNewSkillTask() || !player.checkTask(task) || crateAmount == 0) {
					container.stop();
					return;
				}
				if (!player.getInventory().playerHasItem(CLAY)) {
				    player.getActionSender().sendMessage("You have ran out of clay!");
				    container.stop();
				    return;
				}
				if (!player.getInventory().playerHasItem(BUCKET_OF_WATER)) {
				    player.getActionSender().sendMessage("You have ran out of water!");
				    container.stop();
				    return;
				}
				player.getActionSender().sendMessage("You mix the clay and water.");
				if(player.getInventory().removeItem(new Item(BUCKET_OF_WATER,1))){
					player.getInventory().addItem(new Item(BUCKET,1));
				}
				if(player.getInventory().removeItem(new Item(CLAY))){
					player.getInventory().addItem(new Item(SOFT_CLAY,1));
				}
				player.getActionSender().sendMessage("You now have some soft workable clay.");
				crateAmount--;
				container.setTick(2);
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
    }
    
	public static boolean handleButtons(Player player, int buttonId) {
		if(player.getStatedInterface().equals("softClayMake")){
			switch (buttonId) {
				case 10239:
					createTick(player, 1);
					return true;
				case 10238:
					createTick(player, 5);
					return true;
				case 6211:
					createTick(player, 28);
					return true;
			}
		}
		return false;
	}
}
