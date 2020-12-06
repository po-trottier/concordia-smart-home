package com.concordia.smarthomesimulator.dataModels;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import static com.concordia.smarthomesimulator.Constants.*;

/**
 * Heating zone, used to set the desired temperature of a group of rooms.
 * The rooms don't have to be adjacent to be part of the same zone.
 */
public class HeatingZone implements Serializable {

    private double desiredTemp;
    private String name;
    private final ArrayList<Room> rooms;

    /**
     * Instantiates a new Heating zone.
     *
     * @param name the name
     */
    public HeatingZone(String name) {
        this.name = name;
        this.desiredTemp = DEFAULT_TEMPERATURE;

        rooms = new ArrayList<>();
    }

    @NonNull
    @Override
    public Object clone() {
        HeatingZone newZone = new HeatingZone(name);
        newZone.addRooms(rooms);
        newZone.setDesiredTemperature(desiredTemp);
        return newZone;
    }

    /**
     * Gets desired temperature.
     *
     * @return the temperature
     */
    public double getDesiredTemperature() {
        return desiredTemp;
    }

    /**
     * Sets desired temperature for zone and all included rooms.
     *
     * @param temperature the temperature
     */
    public void setDesiredTemperature(double temperature) {
        this.desiredTemp = temperature;
        for (Room room : rooms) {
            if (!room.isTemperatureOverridden()) {
                room.setDesiredTemperature(temperature);
            }
        }
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
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets room.
     *
     * @param name the name
     * @return the room
     */
    public Room getRoom(String name) {
        return rooms.stream()
            .filter(room -> room.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    /**
     * Gets rooms.
     *
     * @return the rooms
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Add a room.
     *
     * @param room the room
     */
    public void addRoom(Room room) {
        // Outdoors should not be in a heating zone
        if (room.getName().equalsIgnoreCase(DEFAULT_NAME_OUTDOORS)) {
            return;
        }
        rooms.add(room);
    }

    /**
     * Add rooms.
     *
     * @param rooms the rooms
     */
    public void addRooms(ArrayList<Room> rooms) {
        for (Room room : rooms) {
            addRoom(room);
        }
    }

    /**
     * Remove room from a heating zone. If a room is removed from a zone, we must make sure it is added to another zone
     * All rooms must be in a zone.
     * @param name the name
     */
    public void removeRoom(String name) {
        rooms.stream()
            .filter(room -> room.getName().equalsIgnoreCase(name))
            .findFirst()
            .ifPresent(rooms::remove);
    }
}
