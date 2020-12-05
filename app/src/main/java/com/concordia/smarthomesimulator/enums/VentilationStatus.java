package com.concordia.smarthomesimulator.enums;

public enum VentilationStatus {
    HEATING(" is being heated"),
    COOLING(" is being cooled"),
    PAUSED (" has its HVAC system paused"),
    OFF (" has its HVAC system turned off");

    private final String name;

    private VentilationStatus(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
