package com.concordia.smarthomesimulator.dataModels;

import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.enums.Orientation;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_GARAGE;
import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_OUTDOORS;

/**
 * The house layout is an observer to the rooms inside of it.
 */
public class HouseLayout extends Observable implements Observer, Serializable {

    private String name;
    private final ArrayList<Room> rooms;

    /**
     * Instantiates a new House layout.
     *  @param name        the name
     *  @param currentUser the currently logged in user
     */
    public HouseLayout(String name, String currentUser) {
        this.name = name;

        rooms = new ArrayList<>();
        // Create the default rooms (Outdoors and Garage)
        Room garage = new Room(DEFAULT_NAME_GARAGE, new Geometry(-1, -1));
        Room outdoors = new Room(DEFAULT_NAME_OUTDOORS, new Geometry(-1, -1));
        // Add the Garage Door
        Door garageDoor = new Door();
        garageDoor.setGeometry(new Geometry(-1, -1, Orientation.HORIZONTAL));
        garage.addDevice(garageDoor);
        // Add the rooms to the layout
        rooms.addAll(new ArrayList<>(Arrays.asList(garage, outdoors)));

        // Add the current user if he's not already in a room
        boolean userExists = currentUser != null && rooms.stream().anyMatch(room -> room.hasInhabitant(currentUser));
        if (!userExists) {
            this.getRoom(DEFAULT_NAME_OUTDOORS).addInhabitant(new Inhabitant(currentUser));
        }
    }

    @Override
    public void update(Observable observable, Object arg) {
        notifyObservers();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != HouseLayout.class) {
            return false;
        }
        return ((HouseLayout) obj).getName().equalsIgnoreCase(name);
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
     * Gets a specific room based on its name.
     *
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
     * Is intruder detected boolean.
     *
     * @return the boolean
     */
    public boolean isIntruderDetected() {
        return getAllInhabitants().stream().anyMatch(Inhabitant::isIntruder);
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
     * Add a room.
     *
     * @param room the room
     */
    public void addRoom(Room room) {
        room.addObserver(this);
        rooms.add(room);
        update(room, null);
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
     * Remove a room.
     *
     * @param name the name
     */
    public void removeRoom(String name) {
        for(Room room : rooms) {
           if (room.getName().equals(name)) {
               room.deleteObserver(this);
               rooms.remove(room);
               return;
           }
        }
    }

    /**
     * Gets all inhabitants in the layout.
     *
     * @return the all inhabitants
     */
    public ArrayList<Inhabitant> getAllInhabitants() {
        List<ArrayList<Inhabitant>> inhabitantLists = rooms
                .stream()
                .map(Room::getInhabitants)
                .collect(Collectors.toList());
        ArrayList<Inhabitant> inhabitants = new ArrayList<>();
        for (ArrayList<Inhabitant> list : inhabitantLists) {
            inhabitants.addAll(list);
        }
        return inhabitants;
    }
}
