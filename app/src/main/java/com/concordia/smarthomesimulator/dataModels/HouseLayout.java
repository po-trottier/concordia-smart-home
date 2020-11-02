package com.concordia.smarthomesimulator.dataModels;

import java.util.ArrayList;
import java.util.Optional;

import static com.concordia.smarthomesimulator.Constants.*;

public class HouseLayout {

    private String image;
    private String name;
    private Geometry geometry;
    private final ArrayList<Room> rooms;

    /**
     * Instantiates a new House layout.
     *
     * @param name   the name
     * @param image  the image
     * @param width  the width
     * @param height the height
     */
    public HouseLayout(String name, String image, float width, float height, String currentUser) {
        this.name = name;
        this.image = image;
        this.geometry = new Geometry(width, height);

        rooms = new ArrayList<>();

        // Add the default rooms (Outdoors and Garage)
        rooms.add(new Room(DEFAULT_NAME_OUTDOORS, new Geometry()));
        rooms.add(new Room(DEFAULT_NAME_GARAGE, new Geometry()));

        // Add the current user if he's not already in a room
        boolean userExists = currentUser != null && rooms.stream().anyMatch(room -> room.hasInhabitant(currentUser));
        if (!userExists)
            this.getRoom(DEFAULT_NAME_OUTDOORS).addInhabitant(new Inhabitant(currentUser));
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
     * Gets image in base64 encoding.
     *
     * @return the image in base64 encoding
     */
    public String getImage() {
        return image;
    }

    /**
     * Gets geometry.
     *
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * Gets rooms.
     *
     * @return the rooms
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
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets name.
     *
     * @param image the image encoded in bas64 format
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Sets geometry.
     *
     * @param geometry the geometry
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * Add a room.
     *
     * @param room the room
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    /**
     * Add rooms.
     *
     * @param rooms the rooms
     */
    public void addRooms(ArrayList<Room> rooms) {
        this.rooms.addAll(rooms);
    }

    /**
     * Remove a room.
     *
     * @param name the name
     */
    public void removeRoom(String name) {
        for(Room room : rooms) {
           if (room.getName().equals(name)) {
               rooms.remove(room);
               return;
           }
        }
    }
}
