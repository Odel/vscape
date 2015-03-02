package com.rs2.model.content.quests;

import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;

public interface Quest {

	/**
	 * Unique Quest ID of a quest.
	 *
	 * @return
	 */
	public int getQuestID();

	/**
	 * Returns the name of the quest.
	 */
	public String getQuestName();

	/**
	 * Returns the save token name of the quest.
	 */
	public String getQuestSaveName();

	/**
	 * Does the player meet the quest requirements? if yes return true.
	 *
	 * @param player
	 * @return
	 */
	public boolean canDoQuest(Player player);

	/**
	 * Reward handling for when a player completes a quest.
	 *
	 * @param player
	 */
	public void getReward(Player player);

	/**
	 * Method used to complete a quest.
	 *
	 * @param player
	 */
	public void completeQuest(Player player);

	/**
	 * Handles all the sending information to an interface etc...
	 *
	 * @param player
	 */
	public void sendQuestRequirements(Player player);

	/**
	 * Starts the quest obviously.
	 *
	 * @param player
	 */
	public void startQuest(Player player);

	/**
	 * is quest completed?
	 *
	 * @param player
	 */
	public boolean questCompleted(Player player);

	/**
	 * Return amount of quest points rewarded upon completion.
	 */
	public int getQuestPoints();

	public void showInterface(Player player);

	public void sendQuestTabStatus(Player player);

	public void sendQuestInterface(Player player);

	public boolean doNpcClicking(final Player player, final Npc npc);

	public boolean doItemOnNpc(final Player player, int itemId, final Npc npc);

	public boolean itemHandling(final Player player, int itemId);

	public boolean itemOnItemHandling(final Player player, int firstItem, int secondItem, int firstSlot, int secondSlot);

	public boolean doItemOnObject(final Player player, int object, int item);

	public boolean doObjectClicking(final Player player, int object, int x, int y);

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y);

	public void handleDeath(final Player player, final Npc died);

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId);
}
