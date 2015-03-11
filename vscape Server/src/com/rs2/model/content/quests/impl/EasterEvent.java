package com.rs2.model.content.quests;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;

public class EasterEvent implements Quest {

	public static final int EVENT_COMPLETE = 10;

	public int dialogueStage = 0;

	private static final int questPointReward = 0;

	public int getQuestID() {
		return 7;
	}

	public String getQuestName() {
		return "Easter Event";
	}

	public String getQuestSaveName() {
		return "easter-event";
	}

	public boolean canDoQuest(Player player) {
		return true;
	}

	public void getReward(Player player) {

	}

	public void completeQuest(Player player) {
		player.setQuestStage(getQuestID(), EVENT_COMPLETE);
	}

	public void sendQuestRequirements(Player player) {
	}

	public void sendQuestInterface(Player player) {
	}

	public void startQuest(Player player) {
	}

	public boolean questCompleted(Player player) {
		int questStage = player.getQuestStage(getQuestID());
		if (questStage >= EVENT_COMPLETE) {
			return true;
		}
		return false;
	}

	public void sendQuestTabStatus(Player player) {
	}

	public int getQuestPoints() {
		return questPointReward;
	}

	public void showInterface(Player player) {

	}

	public boolean itemHandling(final Player player, int itemId) {
		return false;
	}

	public boolean itemOnItemHandling(Player player, int firstItem, int secondItem, int firstSlot, int secondSlot) {
		return false;
	}

	public boolean doItemOnObject(final Player player, int object, int item) {
		return false;
	}

	public boolean doObjectClicking(final Player player, int object, int x, int y) {
		return false;
	}

	public boolean doObjectSecondClick(final Player player, int object, final int x, final int y) {
		switch (object) {

		}
		return false;
	}

	public void handleDeath(final Player player, final Npc died) {

	}

	public boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		return false;
	}

	@Override
	public boolean doNpcClicking(Player player, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doItemOnNpc(Player player, int itemId, Npc npc) {
		// TODO Auto-generated method stub
		return false;
	}

}
