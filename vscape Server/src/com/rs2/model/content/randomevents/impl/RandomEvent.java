package com.rs2.model.content.randomevents.impl;

public interface RandomEvent {
	
	public void spawnEvent();
	public void destroyEvent();
	public void showInterface();
	public boolean handleButtons(int buttonID);
	public boolean sendDialogue(int id, int chatId, int optionId, int npcChatId);
	
}
