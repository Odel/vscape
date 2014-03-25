package com.rs2.model.ground;

import com.rs2.model.Entity;
import com.rs2.model.EntityRecord;
import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.TickStopWatch;

/**
 *
 */
public class GroundItem {
    private Item item;
    private EntityRecord dropper = null, viewFirst = null;
    private TickStopWatch timer;
    private GroundStage stage = null;
    private Position position;
    private boolean respawns;

    public enum GroundStage {PRIVATE, PUBLIC, HIDDEN}

    /*Use for world items*/
    public GroundItem(Item item, Position position, boolean respawns) {
        this.item = item;
        this.setPosition(position.clone());
        this.timer = new TickStopWatch();
        this.stage = GroundStage.PUBLIC;
        this.respawns = respawns;
    }

    public GroundItem(Item item, Entity dropper, Position position) {
        this(item, position, false);
        this.dropper = new EntityRecord(dropper);
        this.stage = GroundStage.PRIVATE;
        this.viewFirst = this.dropper;
    }

    /*Use for dropping an item*/
    public GroundItem(Item item, Entity dropper) {
        this(item, dropper, dropper.getPosition());
    }

    /*Use for kill drops*/
    public GroundItem(Item item, Entity dropper, Entity viewFirst, Position position) {
        this(item, dropper, position);
        this.viewFirst = new EntityRecord(viewFirst);
    }

    public EntityRecord getDropper() {
        if (dropper == null)
            return null;
        return dropper;
    }

    public EntityRecord getViewFirst() {
        if (viewFirst == null)
            return null;
        return viewFirst;
    }

    public Item getItem() {
        return item;
    }

    public TickStopWatch getTimer() {
        return timer;
    }

    public GroundStage getStage() {
        return stage;
    }

    public void setStage(GroundStage stage) {
        this.stage = stage;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean respawns() {
        return respawns;
    }

    public boolean canMergeWithItem(GroundItem other) {
        if (other == this)
            return false;
        if (other.respawns || respawns)
            return false;
        if (other.item.getId() != item.getId() || other.stage != stage)
            return false;
        if (!other.position.equals(position))
            return false;
        if (((long)item.getCount()+other.item.getCount()) > Integer.MAX_VALUE)
            return false;
        switch (other.getStage()) {
            case PUBLIC:
                return true;
            case PRIVATE:
                return viewFirst != null && viewFirst.equals(other.viewFirst);
        }
        return false;

    }

    public boolean canBeSeenByPlayer(Player player) {
        return position.getZ() == player.getPosition().getZ()
                && player.getLoadedLandscape().contains(position);
    }
}
