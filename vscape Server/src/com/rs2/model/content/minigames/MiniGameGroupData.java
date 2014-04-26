package com.rs2.model.content.minigames;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA.
 * User: vayken
 * Date: 7/7/12
 * Time: 8:21 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MiniGameGroupData {

    public abstract int getMiniGameInGameTimer();

    public abstract int getMiniGameInterGameTimer();

    public abstract LinkedGroupAreas[] getLinkedAreas();

    public abstract MinigameAreas.Area getOutSideArea();

    public abstract Item[] getRewards(Player player);

    public abstract void initializeSendToGameActions(Player player);

    public abstract void initializeSendOutSideActions(Player player);

    public abstract void updateWaitingRoomCounter(Player player, int counter);

    public abstract void updateInGameCounter(Player player, int counter);

    public abstract int getMinimumPlayersNeeded();

    public abstract int getMaximumPlayersNeeded();


}
