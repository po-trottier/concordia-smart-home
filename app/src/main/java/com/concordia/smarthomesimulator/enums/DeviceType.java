package com.concordia.smarthomesimulator.enums;

public enum DeviceType {

    LIGHT,
    DOOR,
    WINDOW;

    public static DeviceType fromString(String text) {
        switch (text.toUpperCase()) {
            case "LIGHT": return LIGHT;
            case "DOOR": return DOOR;
            case "WINDOW": return WINDOW;
            default: return null;
        }
    }
}
