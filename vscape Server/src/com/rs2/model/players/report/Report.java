package com.rs2.model.players.report;

import java.io.Serializable;

/**
 */
@SuppressWarnings("serial")
public class Report implements Serializable {
    
    private int defendant, prosecutor;
    private byte ruleId;
    
    public Report(byte ruleId, int defendant, int prosecutor) {
        this.ruleId = ruleId;
        this.defendant = defendant;
        this.prosecutor = prosecutor;
    }

    public byte getRuleId() {
        return ruleId;
    }

    public int getProsecutor() {
        return prosecutor;
    }

    public int getDefendant() {
        return defendant;
    }
}
