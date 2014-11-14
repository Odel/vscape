package com.rs2.model.ground;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.util.Misc;

/**
 *
 */
public class GroundItemManager implements Runnable {
    
    static {
        manager = new GroundItemManager();
    }

    private LinkedList<GroundItem> groundItems = new LinkedList<GroundItem>();
    private Queue<GroundItem> publicItemsList = new LinkedList<GroundItem>();

    //private static final int UPDATE_TIME = (int)Misc.secondsToTicks(3);

	public static final int	HIDDEN_TICKS = (int)Misc.secondsToTicks(60);
	public static final int PLAYER_HIDE_TICKS = (int)Misc.secondsToTicks(60);
	public static final int PUBLIC_HIDE_TICKS = (int)Misc.secondsToTicks(150);
	public static final int UNTRADABLE_HIDE_TICKS = (int)Misc.secondsToTicks(180);

    @SuppressWarnings("incomplete-switch")
	@Override
    public void run() {
        try {
            int size = groundItems.size();
            iterateItems: for (int i = 0; i < size;) {
                GroundItem item = groundItems.get(i);
                switch (item.getStage()) {
                    case HIDDEN:
                        if(item.getGlobalTimer() == 0) {
                        	if (item.getTimer().elapsed() < HIDDEN_TICKS) {
                        		break;
                        	}
                        } else if(item.getGlobalTimer() > 0) {
                        	if (item.getTimer().elapsed() < Misc.secondsToTicks(item.getGlobalTimer())) {
                        		break;
                        	}
                        }
                        //going to update
                        item.getTimer().reset();
                        //make it respawn as public
                        item.setStage(GroundItem.GroundStage.PUBLIC);
                        //try merging
                        if (item.getItem().getDefinition().isStackable()) {
                            for (GroundItem other : publicItemsList) {
                                if (item.canMergeWithItem(other)) {
                                    merge(item, other, World.getPlayers());
                                    groundItems.remove(i);
                                    size--;
                                    continue iterateItems;
                                }
                            }
                        }
                        //couldn't merge, add to global list
                        publicItemsList.add(item);
                        addItem(item, World.getPlayers());
                    break;
                    case PUBLIC:
                    	if(item.getGlobalTimer() == 0) {
                    		if (item.getTimer().elapsed() < PUBLIC_HIDE_TICKS) {
                    			break;
                    		}
                    	} else if(item.getGlobalTimer() > 0) {
                    		if (item.getTimer().elapsed() < Misc.secondsToTicks(item.getGlobalTimer())) {
                    			break;
                    		}
                    	}
                        //going to update
                        item.getTimer().reset();
                        //remove if doesn't respawn
                        if (!item.respawns()) {
                            publicItemsList.remove(item);
                            removeItem(item, World.getPlayers());
                            groundItems.remove(i);
                            size--;
                            continue iterateItems;
                        }
                    break;
                    case PRIVATE: 
                        //if item is not tradeable then remove it from viewfirst
                        if (item.getItem().getDefinition().isUntradable()) {
                            if (item.getTimer().elapsed() < UNTRADABLE_HIDE_TICKS) {
                            	break;
                            }
                            //going to update
                            item.getTimer().reset();
                            Entity entity;
                            if (item.getViewFirst() != null && (entity = item.getViewFirst().getEntity()) != null && entity.isPlayer()) {
                                removeItem(item, new Player[] {(Player) entity});
                            }
                            groundItems.remove(i);
                            size--;
                            continue iterateItems;
                        } else {
                            if (item.getTimer().elapsed() < PLAYER_HIDE_TICKS) {
                            	break;
                            }
                            //going to update
                            item.getTimer().reset();
                            //make it public
                            item.setStage(GroundItem.GroundStage.PUBLIC);
                            //try merging
                            if (item.getItem().getDefinition().isStackable()) {
                                for (GroundItem other : publicItemsList) {
                                    if (item.canMergeWithItem(other)) {
                                        merge(item, other, World.getPlayers());
                                        groundItems.remove(i);
                                        size--;
                                        continue iterateItems;
                                    }
                                }
                            }
                            //couldn't merge, add to global list
                            publicItemsList.add(item);
                            Player[] players = World.getPlayers();
                            Entity viewFirst = item.getViewFirst() == null ? null : item.getViewFirst().getEntity();
                            if (viewFirst != null && !viewFirst.isPlayer())
                                viewFirst = null;
                            for (int p = 0; p < players.length; p++) {
                                Player player = players[p];
                                if (player == null || player == viewFirst && player.getGroundItems().contains(item))
                                    continue;
                                if (item.canBeSeenByPlayer(player)) {
                                    player.getGroundItems().add(item);
                                    player.getActionSender().sendGroundItem(item);
                                }
                            }
                        }
                        break;
                    }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void merge(GroundItem item, GroundItem other, Player[] possiblePlayers) {
        List<Player> list = new LinkedList<Player>();
        if (possiblePlayers != null) {
            for (Player player : possiblePlayers) {
                if (player == null)
                    continue;
                if (other.canBeSeenByPlayer(player)) {
                    if (player.getGroundItems().contains(other))
                        player.getActionSender().removeGroundItem(other);
                    list.add(player);
                }
            }
        }
        possiblePlayers = list.toArray(new Player[list.size()]);
        other.getItem().setCount(other.getItem().getCount()+item.getItem().getCount());
        other.getTimer().reset();
        if (possiblePlayers != null) {
            for (Player player : possiblePlayers) {
                if (item.canBeSeenByPlayer(player))
                    player.getActionSender().sendGroundItem(other);
            }
        }
    }

    private void removeItem(GroundItem item, Player[] players) {
        for (Player player : players) {
            if (player == null)
                continue;
            if (item.canBeSeenByPlayer(player)) {
                player.getGroundItems().remove(item);
                player.getActionSender().removeGroundItem(item);
            }
        }
    }

    private void addItem(GroundItem item, Player[] players) {
        for (Player player : players) {
            if (player == null)
                continue;
            if (item.canBeSeenByPlayer(player)) {
                player.getGroundItems().add(item);
                player.getActionSender().sendGroundItem(item);
            }
        }
    }
    
    public void destroyItem(GroundItem item) {
        if (item == null)
            return;
        Player[] updatePlayers = null;
        if (item.getStage() == GroundItem.GroundStage.PRIVATE) {
            Entity entity;
            if (item.getViewFirst() != null && (entity = item.getViewFirst().getEntity()) != null && entity.isPlayer())
                updatePlayers = new Player[] { (Player)entity };
            
        }  else if (item.getStage() == GroundItem.GroundStage.PUBLIC) {
            publicItemsList.remove(item);
            updatePlayers = World.getPlayers();
        }
        
        if (item.getStage() == GroundItem.GroundStage.PUBLIC && item.respawns()) {
            item.setStage(GroundItem.GroundStage.HIDDEN);
            item.getTimer().reset();
        } else {
        	groundItems.remove(item);
        }

        if (updatePlayers == null)
            return;
        
        for (Player player : updatePlayers) {
            if (player == null)
                continue;
            if (item.canBeSeenByPlayer(player)) {
                player.getGroundItems().remove(item);
                player.getActionSender().removeGroundItem(item);
            }
        }
        
    }


    @SuppressWarnings("incomplete-switch")
	public void dropItem(GroundItem item) {
        Player[] updatePlayers = null;
        switch (item.getStage()) {
            case PRIVATE:
                Entity entity;
                if (item.getViewFirst() != null && (entity = item.getViewFirst().getEntity()) != null && entity.isPlayer())
                    updatePlayers = new Player[]{(Player) entity};
                if (ItemDefinition.forId(item.getItem().getId()).isStackable()) {
                    for (GroundItem other : groundItems) {
                        if (item.canMergeWithItem(other)) {
                            merge(item, other, updatePlayers);
                            return;
                        }
                    }
                }
                break;
            case PUBLIC:
                updatePlayers = World.getPlayers();
                if (ItemDefinition.forId(item.getItem().getId()).isStackable()) {
                    for (GroundItem other : publicItemsList) {
                        if (item.canMergeWithItem(other)) {
                            merge(item, other, updatePlayers);
                            return;
                        }
                    }
                }
                break;
        }
        if (item.getStage() == GroundItem.GroundStage.PUBLIC)
            publicItemsList.add(item);

        groundItems.add(item);

        if (updatePlayers == null)
            return;

        addItem(item, updatePlayers);

    }

    private static GroundItemManager manager;

    public static GroundItemManager getManager() {
        return manager;
    }
    
    public boolean itemExists(Player player, GroundItem groundItem) {
        for (GroundItem item : player.getGroundItems())
            if (item.equals(groundItem))
                return true;
        return false;
    }

    public GroundItem findItem(Player player, int itemId, Position pos) {
        for (GroundItem item : player.getGroundItems())
            if (item.getItem().getId() == itemId && item.getPosition().equals(pos))
                return item;
        return null;
    }

    public void refreshLandscapeDisplay(Player player) {
        for (GroundItem item : player.getGroundItems())
            player.getActionSender().removeGroundItem(item);
        player.getGroundItems().clear();
        for (GroundItem item : groundItems) {
            if (!item.canBeSeenByPlayer(player))
                continue;
            if (item.getStage() == GroundItem.GroundStage.PUBLIC) { 
                player.getGroundItems().add(item);
                player.getActionSender().sendGroundItem(item);
         /*   } else if(item.getStage() == GroundItem.GroundStage.PRIVATE && item.getViewFirst() != null
                    && item.getViewFirst().equals(player)) {
                player.getGroundItems().add(item);
                player.getActionSender().sendGroundItem(item);*/
            }
        }
    }
}
