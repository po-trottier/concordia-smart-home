package com.concordia.smarthomesimulator.dataModels;

import android.graphics.RectF;

public class MapRoom {

    private final RectF shape;
    private final Room room;

    /**
     * Instantiates a new Map device.
     *
     * @param shape  the shape
     * @param room the room
     */
    public MapRoom(RectF shape, Room room) {
        this.shape = shape;
        this.room = room;
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
     * Gets the room.
     *
     * @return the room
     */
    public Room getRoom() {
        return room;
    }
}
