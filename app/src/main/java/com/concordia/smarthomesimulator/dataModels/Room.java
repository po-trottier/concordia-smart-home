package com.concordia.smarthomesimulator.dataModels;

import java.util.ArrayList;

public class Room {

    private String name;
    private Geometry geometry;
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
