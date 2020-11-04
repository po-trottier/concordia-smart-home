package com.concordia.smarthomesimulator.dataModels;

import android.graphics.RectF;

public class MapDevice {

    private final RectF shape;
    private final IDevice device;

    /**
     * Instantiates a new Map device.
     *
     * @param shape  the shape
     * @param device the device
     */
    public MapDevice(RectF shape, IDevice device) {
        this.shape = shape;
        this.device = device;
    }

    /**
     * Gets shape.
     *
     * @return the shape
     */
    public RectF getShape() {
        return shape;
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    public IDevice getDevice() {
        return device;
    }
}
