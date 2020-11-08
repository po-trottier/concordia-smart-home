package com.concordia.smarthomesimulator.interfaces;

import androidx.annotation.NonNull;
import com.concordia.smarthomesimulator.enums.DeviceType;
import com.concordia.smarthomesimulator.dataModels.Geometry;

/**
 * The interface Device.
 */
public interface IDevice {

    /**
     * Gets opened tint.
     *
     * @return the opened tint
     */
    public int getOpenedTint();

    /**
     * Gets closed tint.
     *
     * @return the closed tint
     */
    public int getClosedTint();

    /**
     * Gets is opened.
     *
     * @return the is opened
     */
    public boolean getIsOpened();

    /**
     * Sets is opened.
     *
     * @param isOpened the is opened
     */
    public void setIsOpened(boolean isOpened);

    /**
     * Gets geometry for the device.
     *
     * @return the geometry
     */
    public Geometry getGeometry();

    /**
     * Sets the geometry for the device.
     *
     * @param geometry the geometry
     */
    public void setGeometry(Geometry geometry);

    /**
     * Gets device type.
     *
     * @return the device type
     */
    public DeviceType getDeviceType();

    /**
     * Deep copy device.
     *
     * @return the device
     */
    @NonNull
    public IDevice clone();

    /**
     * Checks if the device is equal an other.
     *
     * To perform the check we use the status and geometry.
     * We therefore assume that there will never be 2 devices on top of each other
     *
     * @param other the other device
     * @return whether the device is equal to the other
     */
    public boolean equals(IDevice other);
}
