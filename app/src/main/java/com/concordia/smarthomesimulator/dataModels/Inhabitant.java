package com.concordia.smarthomesimulator.dataModels;

public class Inhabitant {

    private final String name;
    private Room room;

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

    /**
     * Gets the room in which the inhabitant is located. A null room means the inhabitant is outside the house.
     *
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Sets the room in which the inhabitant is located. A null room means the inhabitant is outside the house.
     *
     * @param room the room
     */
    public void setRoom(Room room) {
        this.room = room;
    }
}
