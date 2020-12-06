package com.concordia.smarthomesimulator.enums;

public enum VentilationStatus {
    HEATING("HEATING"),
    COOLING("COOLING"),
    PAUSED("PAUSED"),
    OFF("OFF");

    private final String name;

    private VentilationStatus(String s) {
        name = s;
    }

    public String getDescription(){
        switch (this){
            case HEATING:
                return " is being heated";
            case COOLING:
                return " is being cooled";
            case PAUSED:
                return  "has its HVAC system paused";
            case OFF:
                return " has its HVAC system turned off";
            default:
                return "ERROR";

        }
    }

    public String toString() {
        return this.name;
    }
}
