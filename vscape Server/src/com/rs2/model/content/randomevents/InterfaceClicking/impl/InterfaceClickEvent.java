package com.rs2.model.content.randomevents.InterfaceClicking.impl;

import java.util.ArrayList;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 14/04/12 Time: 16:12 To change
 * this template use File | Settings | File Templates.
 */
public interface InterfaceClickEvent {

	public int npcId();

	public int interfaceSent();

	public String[] stringSent();

	public ArrayList<Integer> buttonsDisplayed();

	public int numberOfStages();
	
	public int getRandomNumber();
	
	public void setRandomNumber();

	public String[] cycleMessages();

	public int cycleDuration();

	public Item[] rewards();

	public String[] goodByeMessage();

	public void handleFailure(Player player);

	public void handleSuccess(Player player);

}
