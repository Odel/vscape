package com.rs2.model.content.minigames.groupminigames;

import com.rs2.model.Position;
import com.rs2.model.content.minigames.LinkedGroupAreas;
import com.rs2.model.content.minigames.MiniGameGroupData;
import com.rs2.model.content.minigames.MinigameAreas;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA.
 * User: vayken
 * Date: 7/9/12
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CastleWarsCounter extends MiniGameGroupData {
    @Override
    public int getMiniGameInGameTimer() {
        return 20;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMiniGameInterGameTimer() {
        return 5;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LinkedGroupAreas[] getLinkedAreas() {
        return new LinkedGroupAreas[]{new LinkedGroupAreas(new MinigameAreas.Area(new Position(2410, 9510, 0), new Position(2427, 9533, 0)), new MinigameAreas.Area(new Position(2368, 3127, 1), new Position(2376, 3135, 1))),
                                      new LinkedGroupAreas(new MinigameAreas.Area(new Position(2362, 9473, 0), new Position(2392, 9499, 0)), new MinigameAreas.Area(new Position(2423, 3072, 1), new Position(2431, 3080, 1)))};
    }

    @Override
    public MinigameAreas.Area getOutSideArea() {
        return new MinigameAreas.Area(new Position(2438, 3082, 0), new Position(2444, 3097, 0));
    }

    @Override
    public Item[] getRewards(Player player) {
        return new Item[0];
    }

    @Override
    public void initializeSendToGameActions(Player player) {
        player.getActionSender().sendMessage("send to game");
    }

    @Override
    public void initializeSendOutSideActions(Player player) {
        player.getActionSender().sendMessage("send outside");
    }

    @Override
    public void updateWaitingRoomCounter(Player player, int counter) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateInGameCounter(Player player, int counter) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMinimumPlayersNeeded() {
        return 1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMaximumPlayersNeeded() {
        return 3;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
