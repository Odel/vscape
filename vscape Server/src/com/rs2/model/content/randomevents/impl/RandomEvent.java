package com.rs2.model.content.randomevents.impl;


public interface RandomEvent {
	
	public void spawnEvent();
	public void destroyEvent(boolean logout);
	public void showInterface();
	public boolean handleButtons(int buttonID);
	public boolean sendDialogue(int id, int chatId, int optionId, int npcChatId);
	public boolean doObjectClicking(int object, int x, int y, int z);
}
