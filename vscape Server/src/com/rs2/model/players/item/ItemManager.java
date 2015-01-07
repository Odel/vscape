package com.rs2.model.players.item;

import java.util.Arrays;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.farming.FarmingConstants;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class ItemManager {

	private static ItemManager instance = new ItemManager();

	public void lowerAllItemsTimers(Player p) {
		for (int i = 0; i < 28; i++) {
			Item item = p.getInventory().getItemContainer().get(i);
			if (item == null || item.getTimer() < 0 || item.getDefinition().getSlot() != -1)
				continue;
			item.setTimer(item.getTimer() - 1);
			if (item.getTimer() == 0) {
				for (int items : FarmingConstants.WATERED_SAPPLING)
					if (items == item.getId())
						p.getSeedling().makeSaplingInInv(item.getId());
				if (item.getId() == 1995)
					p.getWine().fermentWineInInv(i);

			}
		}
		for (int i = 0; i < p.getBankManager().getUsedTabs(); i++) {
			for(int j = 0; j < p.getBankManager().tabContainer(i).size(); j++){
				Item item = p.getBankManager().tabContainer(i).get(i);
				if (item == null || item.getTimer() < 0 || item.getDefinition().getSlot() != -1)
					continue;
				item.setTimer(item.getTimer() - 1);
				if (item.getTimer() == 0) {
					for (int items : FarmingConstants.WATERED_SAPPLING)
						if (items == item.getId())
							p.getSeedling().makeSaplingInBank(item.getId());
					if (item.getId() == 1995)
						p.getWine().fermentWineInBank(i, j);
				}
			}
		}
		//FIX THIS TO TAB BANK
	/*	for (int i = 0; i < BankManager.SIZE; i++) {
			Item item = p.getBank().get(i);
			if (item == null || item.getTimer() < 0 || item.getDefinition().getSlot() != -1)
				continue;
			item.setTimer(item.getTimer() - 1);
			if (item.getTimer() == 0) {
				for (int items : FarmingConstants.WATERED_SAPPLING)
					if (items == item.getId())
						p.getSeedling().makeSaplingInBank(item.getId());
				if (item.getId() == 1995)
					p.getWine().fermentWineInBank(i);
			}
		}*/
	}

	public void pickupItem(final Player player, final int itemId, final Position pos) {
		final int task = player.getTask();
        World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (!player.checkTask(task)) {
					this.stop();
					return;
				}
				if (!Misc.goodDistance(player.getPosition(), pos, 1)) {
					return;
				}
				if (!Misc.checkClip(player.getPosition(), pos, false)) {
					return;
				}
				if (Misc.checkClip(player.getPosition(), pos, true) && !player.getPosition().equals(pos)) {
					return;
				}
                if (player.getPrimaryDirection() >= 0 || player.getSecondaryDirection() >= 0)
                    return;
                stop();

                GroundItem item = GroundItemManager.getManager().findItem(player, itemId, pos);
                if (item == null)
                    return;
                
                if(item.getDropper() != null && item.getDropper().getEntity() != null && item.getViewFirst() != null && item.getViewFirst().getEntity() != null && item.getDropper().getEntity().isPlayer() && item.getViewFirst().getEntity().isPlayer())
                {
                	Player viewFirst = (Player) item.getViewFirst().getEntity();
                	Player dropper = (Player) item.getDropper().getEntity();
                	if(viewFirst != player && dropper != player && (viewFirst.isIronman() || dropper.isIronman()) && !player.isIronman()) {
	            		player.getActionSender().sendMessage("You cannot pickup items dropped by an ironman.");
	                	return;
                	}
                	if(viewFirst != player && dropper != player && player.isIronman()) {
	            		player.getActionSender().sendMessage("You cannot pickup items dropped by other players as an ironman.");
	                	return;
                	}
                }

                boolean takeFromTable = !player.getPosition().equals(pos) && !Misc.checkClip(player.getPosition(), pos, true);
            	if(takeFromTable)
            		player.getUpdateFlags().sendAnimation(832);
                
				boolean pickedUp = false;
				if (item.getItem().getDefinition().isStackable()) {
					if (player.getInventory().addItem(new Item(player.getClickId(), item.getItem().getCount()))) {
						pickedUp = true;
					}
				} else if (player.getInventory().addItem(new Item(player.getClickId(), 1))) {
					pickedUp = true;
				}
				if (pickedUp) {
					player.getEquipment().updateWeight();
                    GroundItemManager.getManager().destroyItem(item);
				}
			}
		});
	}

	public int getItemValue(int itemId, String type) {
		double value = 0;
		if (type.equals("buyfromshop")) {
			value = new Item(itemId).getDefinition().getShopPrice();
			value = value < 1 ? 1 : value;
		} else if (type.equals("selltoshop")) {
			value = Math.round(new Item(itemId).getDefinition().getShopPrice()/3.4);
		} else if (type.equals("lowalch")) {
			value = new Item(itemId).getDefinition().getLowAlcValue();
		} else if (type.equals("highalch")) {
			value = new Item(itemId).getDefinition().getHighAlcValue();
		}
		return (int) value;
    }

    public String getItemName(int itemId) {
        return new Item(itemId).getDefinition().getName();
    }

    public int getItemId(String itemName) {
        for (int i = 0; i < Constants.MAX_ITEMS; i++) {
            Item item = new Item(i);
            if (item.getDefinition().getName().equalsIgnoreCase(itemName)) {
                return i;
            }
        }
        return -1;
    }

    public static ItemManager getInstance() {
        return instance;
	}


    public boolean isUntradeable(int id) {
        return Arrays.binarySearch(Constants.UNTRADEABLE_ITEMS, id) > -1;
    }

	public void createGroundItem(Player player, Item item, Position position) {
		// TODO Auto-generated method stub
		
	}


}
