package com.rs2.model.content.quests;

import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;

/**
 * @date 1-jun-2011
 * @author Satan666
 */
public interface Quest {

    /**
     * Unique Quest ID of a quest.
     * @return
     */
    public int getQuestID();

    /**
     * Returns the name of the quest.
     */
    public String getQuestName();

    /**
     * Does the player meet the quest requirements? if yes return true.
     * @param player
     * @return
     */
    public boolean canDoQuest(Player player);

    /**
     * Reward handling for when a player completes a quest.
     * @param player
     */
    public void getReward(Player player);

    /**
     * Method used to complete a quest.
     * @param player
     */
    public void completeQuest(Player player);

    /**
     * Handles all the sending information to an interface etc...
     * @param player
     */
    public void sendQuestRequirements(Player player);

    /**
     * Starts the quest obviously.
     * @param player
     */
    public void startQuest(Player player);

    /**
     * Return amount of quest points rewarded upon completion.
     */
    public int getQuestPoints();

    /**
     * Handles all the object clicking in a quest!
     * @param player
     * @param object
     */
    public void clickObject(Player player, int object);
    
    public void showInterface(Player player);

	public void dialogue(Player player, Npc npc);

	public int getDialogueStage(Player player);

	public void setDialogueStage(int i);
	
    public void sendQuestTabStatus(Player player);
    
    public void sendQuestInterface(Player player);
}