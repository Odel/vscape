package com.rs2.model.content.quests.MonkeyMadness;

import com.rs2.model.players.Player;

public class MonkeyMadnessVars {
    private final Player player;
    private boolean givenMonkeyChildBananas;
    private boolean monkeyChildHasToy;
    
    public MonkeyMadnessVars(final Player player) {
	this.player = player;
    }
    
    public boolean givenMonkeyChildBananas() {
	return this.givenMonkeyChildBananas;
    }
    
    public void setGivenMonkeyChildBananas(boolean set) {
	this.givenMonkeyChildBananas = set;
    }
    
    public boolean monkeyChildHasToy() {
	return this.monkeyChildHasToy;
    }
    
    public void setMonkeyChildHasToy(boolean set) {
	this.monkeyChildHasToy = set;
    }
}
