package com.rs2.model.content.skills.farming;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class MithrilSeeds {
    final Player player;
    private int lastFlowerX = 0;
    private int lastFlowerY = 0;
    
    public MithrilSeeds(Player player) {
	this.player = player;
    }
    
    public static final int[] flowerObjects = {2980, 2981, 2982, 2983, 2984, 2985, 2986, 2987, 2988};
    public static final int[] flowerItems = {2460, 2462, 2464, 2466, 2468, 2470, 2472, 2474, 2476};

    public void plantMithrilSeed(final Player player) {
	int flower = flowerObjects[Misc.randomMinusOne(flowerObjects.length)];
	int x = player.getPosition().getX();
	int y = player.getPosition().getY();
	this.lastFlowerX = x;
	this.lastFlowerY = y;
	GameObject p = ObjectHandler.getInstance().getObject(x, y, player.getPosition().getZ());
	if (p != null) {
	    player.getActionSender().sendMessage("You can't plant a flower here.");
	    return;
	}
	if (player.getInventory().getItemContainer().contains(299)) {
	    player.getInventory().removeItem(new Item(299));
	} else {
	    return;
	}
	player.getUpdateFlags().sendAnimation(827);
	final GameObject f = new GameObject(flower, x, y, player.getPosition().getZ(), 0, 10, -1, 500);
	ObjectHandler.getInstance().addObject(f, false);
	if (player.canMove(-1, 0)) {
	    player.getActionSender().walkTo(-1, 0, false);
	} else {
	    player.getActionSender().walkTo(1, 0, false);
	}
	player.getUpdateFlags().sendFaceToDirection(new Position(x, y));
	Dialogues.startDialogue(player, flower + 10000);
	CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
	    @Override
	    public void execute(CycleEventContainer b) {
		b.stop();
	    }

	    @Override
	    public void stop() {
		removeFlowerObject(f);
	    }
	}, 300);
    }
    
    public int getLastFlowerX() {
	return this.lastFlowerX;
    }
    
    public int getLastFlowerY() {
	return this.lastFlowerY;
    }
    
    public int itemForObject(int object) {
	for (int i = 0; i < flowerObjects.length; i++) {
	    if(flowerObjects[i] == object) {
		return flowerItems[i];
	    }
	}
	return -1;
    }
    
    public void removeFlowerObject(GameObject f) {
	if (f != null) {
	    ObjectHandler.getInstance().removeObject(f.getDef().getPosition().getX(), f.getDef().getPosition().getY(), f.getDef().getPosition().getZ(), f.getDef().getType());
	    for (Player player : World.getPlayers()) {
		if (player == null) {
		    continue;
		}
		if (player.getPosition().getZ() == f.getDef().getPosition().getZ() && Misc.goodDistance(f.getDef().getPosition().getX(), f.getDef().getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), 60)) {
		    player.getActionSender().sendObject(-1, f.getDef().getPosition().getX(), f.getDef().getPosition().getY(), f.getDef().getPosition().getZ(), f.getDef().getFace(), f.getDef().getType());
		}
	    }
	}
    }
    
}
