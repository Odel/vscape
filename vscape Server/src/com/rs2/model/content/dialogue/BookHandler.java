package com.rs2.model.content.dialogue;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class BookHandler {

	private static final int INTERFACE = 12624;
	private static final int LEFT_BUTTON_INDEX = 12712;
	private static final int LEFT_BUTTON_ID = 49167;
	private static final int RIGHT_BUTTON_INDEX = 12714;
	private static final int RIGHT_BUTTON_ID = 49169;

	private Player player;
	private String[] lines;
	private String title;
	private int pagesIndex = 1;

	public BookHandler(final Player player) {
		this.player = player;
	}
	
	private void determineButtons() {
		int lastPage = (lines.length / 22) + 1;
		if (pagesIndex == 1) {
			player.getActionSender().sendInterfaceHidden(1, LEFT_BUTTON_INDEX);
			player.getActionSender().sendInterfaceHidden(0, RIGHT_BUTTON_INDEX);
			if(lines.length < 23)
				player.getActionSender().sendInterfaceHidden(1, RIGHT_BUTTON_INDEX);
		} else if (pagesIndex == lastPage) {
			player.getActionSender().sendInterfaceHidden(0, LEFT_BUTTON_INDEX);
			player.getActionSender().sendInterfaceHidden(1, RIGHT_BUTTON_INDEX);
		} else {
			player.getActionSender().sendInterfaceHidden(0, LEFT_BUTTON_INDEX);
			player.getActionSender().sendInterfaceHidden(0, RIGHT_BUTTON_INDEX);
		}
	}
	
	private void sendStrings() {
		clearStrings();
		for(int i = 12715; i < 12737; i++) {
			int index = pagesIndex > 1 ? (((pagesIndex - 1) * 22)  + (i - 12715)) : (i - 12715);
			if(index >= 0 && index < lines.length && lines != null)
				player.getActionSender().sendString("" + lines[index], i);
			else
				player.getActionSender().sendString("", i);
		}
	}
	
	private void clearStrings() {
		for(int i = 12715; i < 12736; i++)
			player.getActionSender().sendString("", i);
	}
	
	public void initBook(String[] lines, String title) {
		player.setStatedInterface("readingBook");
		player.getUpdateFlags().sendAnimation(1350);
		this.lines = lines;
		this.title = title;
		pagesIndex = 1;
		determineButtons();
		sendStrings();
		player.getActionSender().sendString("" + title, 12666);
		player.getActionSender().sendInterface(INTERFACE);
	}
	
	public void reset() {
		clearStrings();
		player.getActionSender().sendString("", 12666);
		lines = null;
		title = null;
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
		    @Override
		    public void execute(CycleEventContainer b) {
			b.stop();
		    }
		    @Override
		    public void stop() {
			player.setStopPacket(false);
			player.getUpdateFlags().sendAnimation(-1);
		    }
		}, 3);
	}
	
	public boolean handleButtons(int buttonId) {
		if(player.stopPlayerPacket()) {
			return false;
		}
		if (player.getStatedInterface().equals("readingBook")) {
			player.setStopPacket(true);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer b) {
					b.stop();
				}
				@Override
				public void stop() {
					player.setStopPacket(false);
				}
			}, 2);
			switch (buttonId) {
				case LEFT_BUTTON_ID:
					pagesIndex -= 1;
					sendStrings();
					determineButtons();
					return true;
				case RIGHT_BUTTON_ID:
					pagesIndex += 1;
					sendStrings();
					determineButtons();
					return true;
			}
			return false;
		}
		return false;
	}
}
