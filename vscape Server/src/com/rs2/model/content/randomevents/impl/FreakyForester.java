package com.rs2.model.content.randomevents.impl;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

import java.util.Random;

public class FreakyForester {
    public Player player;
    public int tails;
    public boolean active = false;
    public Position oldPos = new Position(3222, 3218, 0);
    
    public static final int FREAKY_FORESTER = 2458;
    public static final int HAT = 6182;
    public static final int TOP = 6180;
    public static final int LEGS = 6181;
    public static final int[] CLOTHES = {6182, 6180, 6181};
    
    public FreakyForester(Player eventee) {
	this.player = eventee;
    }
    
    public int getTails() {
	return this.tails;
    }
    
    public boolean isActive() {
	return this.active;
    }
    
    public void setInactive() {
	this.active = false;
    }
    
    public Position getOldPos() {
	return this.oldPos;
    }
    
    public Item getRandomPieceLeft() {
	if(!player.getInventory().ownsItem(HAT) && !player.getInventory().ownsItem(TOP) && !player.getInventory().ownsItem(LEGS)) {
	    return new Item(CLOTHES[Misc.random(2)]);
	}
	else if(player.getInventory().ownsItem(HAT) && !player.getInventory().ownsItem(TOP) && !player.getInventory().ownsItem(LEGS)) {
	    return new Item(50 >= new Random().nextDouble() * 100 ? TOP : LEGS);
	}
	else if(!player.getInventory().ownsItem(HAT) && player.getInventory().ownsItem(TOP) && !player.getInventory().ownsItem(LEGS)) {
	    return new Item(50 >= new Random().nextDouble() * 100 ? HAT : LEGS);
	}
	else if(!player.getInventory().ownsItem(HAT) && !player.getInventory().ownsItem(TOP) && player.getInventory().ownsItem(LEGS)) {
	    return new Item(50 >= new Random().nextDouble() * 100 ? HAT : TOP);
	}
	else if(player.getInventory().ownsItem(HAT) && player.getInventory().ownsItem(TOP) && !player.getInventory().ownsItem(LEGS)) {
	    return new Item(LEGS);
	}
	else if(player.getInventory().ownsItem(HAT) && !player.getInventory().ownsItem(TOP) && player.getInventory().ownsItem(LEGS)) {
	    return new Item(TOP);
	}
	else if(!player.getInventory().ownsItem(HAT) && player.getInventory().ownsItem(TOP) && player.getInventory().ownsItem(LEGS)) {
	    return new Item(HAT);
	}
	else {
	    return new Item(995, 1250);
	}
    }
    private int getNpcIdForTails() {
	switch(this.tails) {
	    case 1:
		return 2459;
	    case 2:
		return 2460;
	    case 3:
		return 2461;
	    case 4:
		return 2462;
	}
	return 0;
    }
    public void spawnForester() {
	this.tails = Misc.randomMinusOne(4) + 1;
	this.active = true;
	this.oldPos = player.getPosition().clone();
	final Npc npc = new Npc(FREAKY_FORESTER);
	NpcLoader.spawnNpc(player, npc, false, false);
	player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
	player.setSpawnedNpc(npc);
	cycleEvent(npc, player, new String[]{"Hello 1! I need your help!", "Quickly, come with me 1!"}, 0);
    }
    
    public void cycleEvent(final Npc npc, final Player player, final String[] chat, final int transformId) {
	CycleEventHandler.getInstance().addEvent(npc, new CycleEvent() {
	    int cycle = 0;
	    String name = Misc.formatPlayerName(player.getUsername());

	    @Override
	    public void execute(CycleEventContainer container) {
		if (cycle <= 3 ) {
		    switch (cycle) {
			case 0:
			    npc.getUpdateFlags().sendForceMessage(chat[0].replaceAll("1", name));
			    break;
			case 3:
			    npc.getUpdateFlags().sendForceMessage(chat[1].replaceAll("1", name));
			    break;
		    }
		    cycle++;
		} else {
		    NpcLoader.destroyNpc(npc);
		    player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, npc.getPosition(), 100 << 16);
		    player.teleport(new Position(2601, 4776, 0));
		    Dialogues.startDialogue(player, FREAKY_FORESTER);
		    container.stop();
		    return;
		}
	    }

	    @Override
	    public void stop() {
	    }
	}, 1);
    }
    
    public void handleDrops(Npc npc) {
	if(!this.active) {
	    return;
	}
	if(npc.getNpcId() == getNpcIdForTails()) {
	    GroundItem drop = new GroundItem(new Item(6178), player, npc.getPosition().clone()); //good pheasant
	    GroundItemManager.getManager().dropItem(drop);
	}
	else {
	    switch(npc.getNpcId()) {
		case 2459:
		case 2460:
		case 2461:
		case 2462:
		    GroundItem drop = new GroundItem(new Item(6179), player, npc.getPosition().clone()); //bad pheasant
		    GroundItemManager.getManager().dropItem(drop);
	    }
	}
    }
}
