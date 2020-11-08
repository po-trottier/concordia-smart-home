package com.concordia.smarthomesimulator.dataModels;

public class AwayModeEntry {
    private boolean awayMode;
    private String callTimer;

    public String getCallTimer() {
        return callTimer;
    }

    public AwayModeEntry(boolean awayMode, String callTimer) {
        this.awayMode = awayMode;
        this.callTimer = callTimer;
    }



}
