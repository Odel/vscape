package com.rs2.model.content.quests.MonkeyMadness;

import com.rs2.model.players.Player;

public class MonkeyMadnessVars {
    private final Player player;
    private boolean givenMonkeyChildBananas;
    private boolean monkeyChildHasToy;
    private boolean jailCheckRunning;
    private boolean dungeonRunning;
    private boolean isMonkey;
    private boolean gotAmulet;
    private boolean gotTalisman;
    private boolean openGate;
    private boolean spokenToMonkeyChild1;
    public boolean whereAreWeWaydar = false;
    public boolean inProcessOfBeingJailed = false;
    
    public MonkeyMadnessVars(final Player player) {
	this.player = player;
    }
    
    public boolean spokenToMonkeyChild1() {
        return this.spokenToMonkeyChild1;
    }
    
    public void setSpokenToMonkeyChild1(boolean set) {
        this.spokenToMonkeyChild1 = set;
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
    
    public boolean gotAmulet() {
        return gotAmulet;
    }
    
    public void setGotAmulet(boolean set) {
        this.gotAmulet = set;
    }
    
    public boolean gotTalisman() {
        return gotTalisman;
    }
    
    public void setGotTalisman(boolean set) {
        this.gotTalisman = set;
    }
    
    public boolean openGate() {
        return openGate;
    }
    
    public void setOpenGate(boolean set) {
        this.openGate = set;
    }
    
}
