package com.concordia.smarthomesimulator.dataModels;

/**
 * The type Geometry.
 */
public class Geometry {
    private int x;
    private int y;
    private int width;
    private int height;
    private Orientation orientation = Orientation.IGNORE;

    /**
     * Instantiates a new Geometry.
     */
    public Geometry() {}

    /**
     * Instantiates a new Geometry.
     *
     * @param x           the x coordinate
     * @param y           the y coordinate
     */
    public Geometry(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 0;
        this.height = 0;
    }

    /**
     * Instantiates a new Geometry.
     *
     * @param x           the x coordinate
     * @param y           the y coordinate
     * @param orientation the orientation of the device
     */
    public Geometry(int x, int y, Orientation orientation) {
        this.x = x;
        this.y = y;
        setOrientation(orientation);
    }

    /**
     * Instantiates a new Geometry.
     *
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width
     * @param height the height
     */
    public Geometry(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the x coordinate.
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets orientation.
     *
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Sets the x coordinate.
     *
     * @param x the x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate.
     *
     * @param y the y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the width.
     *
     * @param width the width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the height.
     *
     * @param height the height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets orientation.
     *
     * @param orientation the orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        switch (orientation) {
            case HORIZONTAL:
                this.width = 1;
                this.height = 0;
                break;
            case VERTICAL:
                this.width = 0;
                this.height = 1;
                break;
        }
    }
}
