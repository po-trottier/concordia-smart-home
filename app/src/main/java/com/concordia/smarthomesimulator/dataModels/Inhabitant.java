package com.concordia.smarthomesimulator.dataModels;

import java.io.Serializable;

public class Inhabitant implements Serializable {
    private final boolean isIntruder;
    private final String name;

    /**
     * Instantiates a new Inhabitant.
     *
     * @param name the name
     */
    public Inhabitant(String name) {
        this.name = name;
        this.isIntruder = false;
    }

    /**
     * Instantiates a new Inhabitant with option of adding as an intruder
     *
     * @param name the name
     * @param isIntruder intruder status
     */
    public Inhabitant(String name, boolean isIntruder) {
        this.name = name;
        this.isIntruder = isIntruder;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Determines if inhabitant is an intruder.
     *
     * @return true or false
     */
    public boolean isIntruder() {
        return this.isIntruder;
    }
}
