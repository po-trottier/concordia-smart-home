package com.concordia.smarthomesimulator.dataModels;

import java.util.ArrayList;

public class HouseLayout {

    private final String image;
    private String name;
    private Geometry geometry;
    private ArrayList<Room> rooms;
    private ArrayList<Inhabitant> inhabitants;

    /**
     * Instantiates a new House layout.
     *
     * @param name   the name
     * @param image  the image
     * @param width  the width
     * @param height the height
     */
    public HouseLayout(String name, String image, float width, float height) {
        this.name = name;
        this.image = image;
        this.geometry = new Geometry(width, height);
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
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Gets inhabitants.
     *
     * @return the inhabitants
     */
    public ArrayList<Inhabitant> getInhabitants() {
        return inhabitants;
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
     * Add an inhabitant.
     *
     * @param inhabitant the inhabitant
     */
    public void addInhabitant(Inhabitant inhabitant) {
        inhabitants.add(inhabitant);
    }

    /**
     * Add inhabitants.
     *
     * @param inhabitants the inhabitants
     */
    public void addInhabitants(ArrayList<Inhabitant> inhabitants) {
        this.inhabitants.addAll(inhabitants);
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

    /**
     * Remove an inhabitant.
     *
     * @param name the name
     */
    public void removeInhabitant(String name) {
        for(Inhabitant inhabitant : inhabitants) {
            if (inhabitant.getName().equals(name)) {
                inhabitants.remove(inhabitant);
                return;
            }
        }
    }
}
