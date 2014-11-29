package com.rs2.model.content.quests.MonkeyMadness;

import com.rs2.model.players.Player;

public class MonkeyMadnessVars {
    private final Player player;
    private boolean givenMonkeyChildBananas;
    private boolean monkeyChildHasToy;
    private boolean jailCheckRunning;
    private boolean dungeonRunning;
    private boolean isMonkey;
    
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
    
    public boolean jailCheckRunning() {
	return this.jailCheckRunning;
    }
    
    public void setJailCheckRunning(boolean set) {
	this.jailCheckRunning = set;
    }
    
    public boolean dungeonRunning() {
	return this.dungeonRunning;
    }
    
    public void setDungeonRunning(boolean set) {
	this.dungeonRunning = set;
    }
    
    public boolean isMonkey() {
	return isMonkey;
    }

    public void setIsMonkey(boolean set) {
	this.isMonkey = set;
    }
    
}
