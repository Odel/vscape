package com.rs2.model.content.skills.cooking;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class wetClayHandler {
    private static final int CLAY = 434;
    private static final int SOFT_CLAY = 1761;
    private static final int BUCKET_OF_WATER = 1929;
    private static final int BUCKET = 1925;

    
    public static boolean itemOnItemHandling(Player player, int firstItem, int secondItem) {
	if(firstItem == CLAY && secondItem == BUCKET_OF_WATER) {
	    Dialogues.startDialogue(player, 10566);
	    return true;
	}
	else if(firstItem == BUCKET_OF_WATER && secondItem == CLAY) {
	    Dialogues.startDialogue(player, 10566);
	    return true;
	}
	return false;
    }
   
    public static void wetClay(final Player player, final int count, final boolean fountain) {
	final int task = player.getTask();
	player.setSkilling(new CycleEvent() {
	int amnt = count;

	    @Override
	    public void execute(CycleEventContainer container) {
		if (!player.checkTask(task) || amnt == 0) {
		    container.stop();
		    return;
		}
		if (!player.getInventory().playerHasItem(CLAY)) {
		    player.getActionSender().sendMessage("You have run out of clay!");
		    container.stop();
		    return;
		}
		if (!player.getInventory().playerHasItem(BUCKET_OF_WATER) && !fountain) {
		    player.getActionSender().sendMessage("You have run out of water!");
		    container.stop();
		    return;
		}
		player.getUpdateFlags().sendAnimation(2339);
		player.getActionSender().sendMessage("You wet the clay.");
		player.getInventory().replaceItemWithItem(new Item(CLAY), new Item(SOFT_CLAY));
		if(player.getInventory().playerHasItem(BUCKET_OF_WATER)) {
		    player.getInventory().replaceItemWithItem(new Item(BUCKET_OF_WATER), new Item(BUCKET));
		}
		amnt--;
	    }
	    @Override
	    public void stop() {
		player.resetAnimation();
	    }
	});
	CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
	return;
    }
    
    public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
	switch(id) {
	    case 10566: 
		switch (player.getDialogue().getChatId()) {
		    case 1:
			player.getDialogue().sendOption("1", "5", "10", "14", "Do nothing.");
			return true;
		    case 2:
			switch(optionId) {
			    case 1:
				wetClay(player, 1, false);
				break;
			    case 2:
				wetClay(player, 5, false);
				break;
			    case 3:
				wetClay(player, 10, false);
				break;
			    case 4:
				wetClay(player, 14, false);
				break;
			    case 5:
				break;
			}
		    break;  
		}
	    break;
	}
    return false;
    }
    
}