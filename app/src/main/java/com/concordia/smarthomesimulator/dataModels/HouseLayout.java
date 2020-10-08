package com.concordia.smarthomesimulator.dataModels;

import java.util.Vector;

public class HouseLayout {

    private String name;
    private Geometry geometry;
    private Vector<Room> rooms;
    private Vector<Inhabitant> inhabitants;

    HouseLayout(String name, float width, float height) {
        this.name = name;
        this.geometry = new Geometry(width, height);
    }

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Vector<Room> getRooms() {
        return rooms;
    }

    public Vector<Inhabitant> getInhabitants() {
        return inhabitants;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addRooms(Vector<Room> rooms) {
        this.rooms.addAll(rooms);
    }

    public void addInhabitant(Inhabitant inhabitant) {
        inhabitants.add(inhabitant);
    }

    public void addInhabitants(Vector<Inhabitant> inhabitants) {
        this.inhabitants.addAll(inhabitants);
    }

    public void removeRoom(String name) {
        for(Room room : rooms) {
           if (room.getName().equals(name)) {
               rooms.remove(room);
               return;
           }
        }
    }

    public void removeInhabitant(String name) {
        for(Inhabitant inhabitant : inhabitants) {
            if (inhabitant.getName().equals(name)) {
                inhabitants.remove(inhabitant);
                return;
            }
        }
    }
}
