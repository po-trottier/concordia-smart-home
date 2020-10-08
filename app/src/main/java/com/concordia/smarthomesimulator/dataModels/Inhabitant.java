package com.concordia.smarthomesimulator.dataModels;

public class Inhabitant {

    private final String name;
    private Room room;

    public Inhabitant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
