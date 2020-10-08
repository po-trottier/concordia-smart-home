package com.concordia.smarthomesimulator.dataModels;

import java.util.Vector;

public class Room {

    private String name;
    private Geometry geometry;
    private final Vector<IDevice> devices;

    Room (String name, Geometry geometry) {
        this.name = name;
        this.geometry = geometry;
        this.devices = new Vector<>();
    }

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Vector<IDevice> getDevices() {
        return devices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void addDevice(IDevice device) {
        devices.add(device);
    }

    public void addDevices(Vector<IDevice> devices) {
        this.devices.addAll(devices);
    }

    public void removeDevice(IDevice device) {
        devices.remove(device);
    }
}
