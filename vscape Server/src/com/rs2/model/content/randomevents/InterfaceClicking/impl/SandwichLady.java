package com.rs2.model.content.randomevents.InterfaceClicking.impl;

import com.rs2.model.content.combat.hit.HitType;
import java.util.ArrayList;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 14/04/12 Time: 23:24 To change
 * this template use File | Settings | File Templates.
 */
public class SandwichLady implements InterfaceClickEvent {
	private int randomNumber;

	public SandwichLady(boolean generateRandom) {
		if (generateRandom)
			this.randomNumber = Misc.random(6);

	}
	@Override
	public int npcId() {
		return 3117;
	}

	@Override
	public int interfaceSent() {
		return 16135;
	}

	@Override
	public String[] stringSent() {
		switch (this.randomNumber) {
			case 0 :
				return new String[]{"redberry pie"};
			case 1 :
				return new String[]{"kebab"};
			case 2 :
				return new String[]{"chocolate bar"};
			case 3 :
				return new String[]{"baguette"};
			case 4 :
				return new String[]{"triangle sandwich"};
			case 5 :
				return new String[]{"square sandwich"};
			case 6 :
				return new String[]{"bread roll"};
		}
		return null;
	}

	@Override
	public ArrayList<Integer> buttonsDisplayed() {
		ArrayList<Integer> array = new ArrayList<Integer>();
		array.add(63013);
		array.add(63014);
		array.add(63015);
		array.add(63009);
		array.add(63010);
		array.add(63011);
		array.add(63012);
		return array;
	}

	@Override
	public String[] cycleMessages() {
		return new String[]{"Hello %, I would like to offer you a treat!", "You'd better start showing some manners, %!", "Well, I've never been so insulted!!", "Fine, ignore me, you will regret it!"};
	}

	@Override
	public int numberOfStages() {
		return 1;
	}

	@Override
	public int cycleDuration() {
		return 20;
	}

	@Override
	public Item[] rewards() {
		return new Item[]{new Item(2325), new Item(1971), new Item(1973), new Item(6961), new Item(6962), new Item(6965), new Item(2309)};
	}

	@Override
	public String[] goodByeMessage() {
		return new String[]{"Hope that fills you up!!", "Let's see how you like this!", "I'm sorry, this wasn't the treat I offered you!"};
	}

	@Override
	public int getRandomNumber() {
		return this.randomNumber;
	}
	
	@Override
	public void setRandomNumber() {
		this.randomNumber = Misc.random(6);
	}

	@Override
	public void handleFailure(Player player) {
	    player.hit(1, HitType.NORMAL);
	}

	@Override
	public void handleSuccess(Player player) {

	}
}
