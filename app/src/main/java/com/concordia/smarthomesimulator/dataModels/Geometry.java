package com.concordia.smarthomesimulator.dataModels;

/**
 * The type Geometry.
 */
public class Geometry {
    private float x;
    private float y;
    private float width;
    private float height;

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

    /**
     * Gets the x coordinate.
     *
     * @return the x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return the y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height.
     *
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the x coordinate.
     *
     * @param x the x coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y the y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the width.
     *
     * @param width the width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Sets the height.
     *
     * @param height the height
     */
    public void setHeight(float height) {
        this.height = height;
    }
}
