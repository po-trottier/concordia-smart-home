package com.concordia.smarthomesimulator.factories;

import com.concordia.smarthomesimulator.dataModels.*;

public class DeviceFactory {

    public IDevice createDevice(DeviceType type) {
        switch (type) {
            case DOOR:
                return new Door();
            case LIGHT:
                return new Light();
            case WINDOW:
                return new Window();
            default:
                return null;
        }
    }

    public IDevice createDevice(DeviceType type, Geometry geometry) {
        switch (type) {
            case DOOR:
                return new Door(geometry);
            case LIGHT:
                return new Light(geometry);
            case WINDOW:
                return new Window(geometry);
            default:
                return null;
        }
    }
}
