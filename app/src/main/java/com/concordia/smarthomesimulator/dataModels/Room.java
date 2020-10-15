package com.concordia.smarthomesimulator.dataModels;

import java.util.ArrayList;

public class Room {

    private String name;
    private Geometry geometry;
    private final ArrayList<IDevice> devices;

    /**
     * Instantiates a new Room.
     *
     * @param name     the name
     * @param geometry the geometry
     */
    public Room (String name, Geometry geometry) {
        this.name = name;
        this.geometry = geometry;
        this.devices = new ArrayList<>();
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
        devices.add(device);
    }

    /**
     * Add devices.
     *
     * @param devices the devices
     */
    public void addDevices(ArrayList<IDevice> devices) {
        this.devices.addAll(devices);
    }

    /**
     * Remove a device.
     *
     * @param device the device
     */
    public void removeDevice(IDevice device) {
        devices.remove(device);
    }
}
