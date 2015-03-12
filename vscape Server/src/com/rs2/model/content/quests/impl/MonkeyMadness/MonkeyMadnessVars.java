package com.rs2.model.content.quests.impl.MonkeyMadness;

import com.rs2.model.Position;
import com.rs2.model.content.treasuretrails.Puzzle;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import java.util.ArrayList;

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
    private boolean firstTimeJail = true;
    private boolean spokenToMonkeyChild;
    private boolean canHideInGrass = false;
    private boolean trainingComplete = false;
    private boolean receivedClueFromMonkey = false;
    public boolean whereAreWeWaydar = false;
    public boolean inProcessOfBeingJailed = false;
    public boolean hitBySpikes = false;
    public boolean monkeyPetDeleted = false;
    public Position hitBySpikesHere = null;
    private ArrayList<Npc> finalFightNpcs = new ArrayList<>();
    public Npc jungleDemon = null;
    public Npc guardCalled = null;
    public boolean spokeToGarkorEndOfFight = false;
    public boolean startedGliderPuzzle = false;
    private Puzzle puzzle = null;
    
    public MonkeyMadnessVars(final Player player) {
	this.player = player;
	puzzle = new Puzzle(this.player);
    }
    
    public boolean spokenToMonkeyChild() {
        return this.spokenToMonkeyChild;
    }
    
    public void setSpokenToMonkeyChild(boolean set) {
        this.spokenToMonkeyChild = set;
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
    
    public boolean canHideInGrass() {
        return canHideInGrass;
    }
    
    public void setCanHideInGrass(boolean set) {
        this.canHideInGrass = set;
    }
    
    public boolean firstTimeJail() {
        return firstTimeJail;
    }
    
    public void setFirstTimeJail(boolean set) {
        this.firstTimeJail = set;
    }
    
    public boolean trainingComplete() {
        return trainingComplete;
    }
    
    public void setTrainingComplete(boolean set) {
        this.trainingComplete = set;
    }
    
    public boolean receivedClueFromMonkey() {
	return receivedClueFromMonkey;
    }
    
    public void setRecievedClueFromMonkey(boolean set) {
	this.receivedClueFromMonkey = set;
    }
    
    public ArrayList<Npc> getFinalFightNpcs() {
	return this.finalFightNpcs;
    }
    
    public Puzzle getPuzzle() {
	return puzzle;
    }
    
}
