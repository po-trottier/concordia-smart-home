package com.concordia.smarthomesimulator.dataModels;

import com.concordia.smarthomesimulator.R;

public class Door implements IDevice {

    private boolean isOpened = false;
    private Geometry geometry;

    public Door() {
        geometry = new Geometry();
    }

    /**
     * Instantiates a new Door.
     *
     * @param geometry the geometry of the device
     */
    public Door(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public int getOpenedIcon() {
        return R.drawable.ic_door_open;
    }

    @Override
    public int getClosedIcon() {
        return R.drawable.ic_door;
    }

    @Override
    public int getOpenedTint() {
        return R.color.primary;
    }

    @Override
    public int getClosedTint() {
        return R.color.accent;
    }

    @Override
    public boolean getIsOpened() {
        return isOpened;
    }

    @Override
    public void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    @Override
    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DOOR;
    }
}
