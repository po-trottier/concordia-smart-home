package com.concordia.smarthomesimulator.dataModels;

public class Geometry {

    /**
     * The X coordinate.
     */
    public float x;
    /**
     * The Y coordinate.
     */
    public float y;

    /**
     * The Width.
     */
    public float width;
    /**
     * The Height.
     */
    public float height;

    /**
     * Instantiates a new Geometry.
     */
    public Geometry() {}

    /**
     * Instantiates a new Geometry.
     *
     * @param width  the width
     * @param height the height
     */
    public Geometry(float width, float height) {
        x = 0;
        y = 0;
        this.width = width;
        this.height = height;
    }
}
