package com.rs2.model.content.minigames;

import java.util.ArrayList;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA.
 * User: vayken
 * Date: 7/7/12
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupMiniGame {


    private int currentGameTime;

    private int currentInterGameTime;

    private int currentWaitingTime = currentGameTime + currentInterGameTime;

    private MiniGameGroupData miniGameGroupData;

    private ArrayList<Player> inGamePlayers = new ArrayList<Player>();

    public GroupMiniGame(MiniGameGroupData miniGameGroupData) {
        this.miniGameGroupData = miniGameGroupData;
        currentGameTime = miniGameGroupData.getMiniGameInGameTimer();
        currentInterGameTime = miniGameGroupData.getMiniGameInterGameTimer();
    }

    public void sendEveryoneToGame(){
        resetGameTimes();
        for(LinkedGroupAreas linkedGroupAreas : miniGameGroupData.getLinkedAreas()){
            for(Player player : linkedGroupAreas.getWaitingArray()){
                    player.teleport(MinigameAreas.randomPosition(linkedGroupAreas.getInGameArea()));
                    miniGameGroupData.initializeSendToGameActions(player);
                    miniGameGroupData.updateInGameCounter(player, currentGameTime);
                    inGamePlayers.add(player);
            }
            linkedGroupAreas.getWaitingArray().clear();
        }
    }

    public void sendEveryOneOutSide() {
            for(Player player : inGamePlayers){
                    player.teleport(MinigameAreas.randomPosition(miniGameGroupData.getOutSideArea()));
                    miniGameGroupData.initializeSendOutSideActions(player);
                    player.getActionSender().sendWalkableInterface(-1);
                    for(Item item : miniGameGroupData.getRewards(player)){
                        player.getInventory().addItem(item);
                    }
            }
        inGamePlayers.clear();
    }

    public void sendToWaitingRoom(Player player, int index){
        miniGameGroupData.getLinkedAreas()[index - 1].getWaitingArray().add(player);
        player.teleport(MinigameAreas.randomPosition(miniGameGroupData.getLinkedAreas()[index - 1].getWaitingArea().enlarge(-10)));
    }

    public LimitType getLimitTypeInWaitingRoom(){
           for(LinkedGroupAreas linkedGroupAreas : miniGameGroupData.getLinkedAreas()){
                if(linkedGroupAreas.getWaitingArray().size() < miniGameGroupData.getMinimumPlayersNeeded())
                    return LimitType.NOT_ENOUGH_PLAYERS;
                if(linkedGroupAreas.getWaitingArray().size() >= miniGameGroupData.getMaximumPlayersNeeded())
                    return LimitType.PLAYER_MAXIMUM_REACHED;
           }

        return LimitType.PLAYER_MINIMUM_REACHED;
    }

    public void updateMiniGameCounter() {
        LimitType limitType = getLimitTypeInWaitingRoom();

        for(Player player : inGamePlayers) miniGameGroupData.updateInGameCounter(player, currentGameTime);

        for(LinkedGroupAreas linkedGroupAreas : miniGameGroupData.getLinkedAreas())
            for(Player player : linkedGroupAreas.getWaitingArray())
                miniGameGroupData.updateWaitingRoomCounter(player, currentGameTime);

        if(currentGameTime > 0) currentGameTime--;
        if(limitType == LimitType.NOT_ENOUGH_PLAYERS){
            return;
        }
        if(currentInterGameTime > 0) currentInterGameTime--;
        debug();
        if(currentGameTime == 0 && currentInterGameTime > 0) sendEveryOneOutSide();
        else if(currentInterGameTime == 0 || limitType == LimitType.PLAYER_MAXIMUM_REACHED) sendEveryoneToGame();
    }

    public void debug(){
        System.out.println("ingame timer :" + currentGameTime);
        System.out.println("intergame timer :" + currentInterGameTime);
        System.out.println("waiting timer :" + currentWaitingTime);
    }

    public void resetGameTimes(){
        currentGameTime = miniGameGroupData.getMiniGameInGameTimer();
        currentInterGameTime = miniGameGroupData.getMiniGameInterGameTimer();
    }

    public enum LimitType {
        NOT_ENOUGH_PLAYERS, PLAYER_MINIMUM_REACHED, PLAYER_MAXIMUM_REACHED
    }

    public ArrayList<Player> getInGamePlayers() {
        return inGamePlayers;
    }

    public MiniGameGroupData getMiniGameGroupData() {
        return miniGameGroupData;
    }
}
