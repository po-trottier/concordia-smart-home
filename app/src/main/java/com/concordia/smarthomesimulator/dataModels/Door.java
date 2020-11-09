package com.concordia.smarthomesimulator.dataModels;

import androidx.annotation.NonNull;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.enums.DeviceType;
import com.concordia.smarthomesimulator.interfaces.IDevice;

import java.io.Serializable;

public class Door implements IDevice, Serializable {

    private boolean isOpened = false;
    private boolean isLocked = false;
    private Geometry geometry;

    public Door() {
        geometry = new Geometry();
    }

    public Door(Geometry geometry) {
        this.geometry = geometry;
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
        return !isLocked && isOpened;
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

    @NonNull
    @Override
    public IDevice clone() {
        Door copy = new Door(geometry);
        copy.setIsOpened(isOpened);
        copy.setIsLocked(isLocked);
        return copy;
    }

    @Override
    public boolean equals(IDevice other) {
        return getDeviceType() == other.getDeviceType()
            && getIsOpened() == other.getIsOpened()
            && getGeometry().equals(other.getGeometry());
    }

    /**
     * Gets locked tint.
     *
     * @return the locked tint
     */
    public int getLockedTint() {
        return R.color.charcoal;
    }

    /**
     * Gets is locked.
     *
     * @return is locked
     */
    public boolean getIsLocked() {
        return isLocked;
    }

    /**
     * Sets is locked.
     *
     * @param isLocked the is locked
     */
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}
