package com.concordia.smarthomesimulator.dataModels;

import java.util.ArrayList;

public class Room {

    private String name;
    private Geometry geometry;
    private final ArrayList<Inhabitant> inhabitants;
    private final ArrayList<Door> doors;
    private final ArrayList<Light> lights;
    private final ArrayList<Window> windows;

    /**
     * Instantiates a new Room.
     *
     * @param name     the name
     * @param geometry the geometry
     */
    public Room (String name, Geometry geometry) {
        this.name = name;
        this.geometry = geometry;

        this.inhabitants = new ArrayList<>();
        this.windows = new ArrayList<>();
        this.doors = new ArrayList<>();
        this.lights = new ArrayList<>();
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
     * Gets geometry.
     *
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
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
     * Gets devices.
     *
     * @return the devices
     */
    public ArrayList<IDevice> getDevices() {
        ArrayList<IDevice> devices = new ArrayList<>();
        devices.addAll(doors);
        devices.addAll(lights);
        devices.addAll(windows);
        return devices;
    }

    /**
     * Whether an inhabitant is in this room or not.
     *
     * @param name the name of the inhabitant
     * @return whether the inhabitant is in this room or not.
     */
    public boolean hasInhabitant(String name) {
        return this.inhabitants.stream().anyMatch(inhabitant -> inhabitant.getName().equalsIgnoreCase(name));
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
     * Add a device.
     *
     * @param device the device
     */
    public void addDevice(IDevice device) {
        switch (device.getDeviceType()) {
            case DOOR:
                doors.add((Door) device);
                break;
            case LIGHT:
                lights.add((Light) device);
                break;
            case WINDOW:
                windows.add((Window) device);
                break;
        }
    }

    /**
     * Add devices.
     *
     * @param devices the devices
     */
    public void addDevices(ArrayList<IDevice> devices) {
        for (IDevice device : devices) {
            addDevice(device);
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

    /**
     * Remove a device.
     *
     * @param device the device
     */
    public void removeDevice(IDevice device) {
        switch (device.getDeviceType()) {
            case DOOR:
                doors.remove(device);
                break;
            case LIGHT:
                lights.remove(device);
                break;
            case WINDOW:
                windows.remove(device);
                break;
        }
    }
}
