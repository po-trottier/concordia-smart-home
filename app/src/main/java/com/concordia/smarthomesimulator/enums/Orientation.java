package com.concordia.smarthomesimulator.enums;

public enum Orientation {

    VERTICAL,
    HORIZONTAL,
    IGNORE;

    /**
     * From string orientation.
     *
     * @param text the text
     * @return the orientation
     */
    public static Orientation fromString(String text) {
        switch (text.toUpperCase()) {
            case "HORIZONTAL": return HORIZONTAL;
            case "VERTICAL": return VERTICAL;
            default: return IGNORE;
        }
    }
}
