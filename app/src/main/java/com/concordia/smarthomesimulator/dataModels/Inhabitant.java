package com.concordia.smarthomesimulator.dataModels;

public class Inhabitant {

    private final String name;

    /**
     * Instantiates a new Inhabitant.
     *
     * @param name the name
     */
    public Inhabitant(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
