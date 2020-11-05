package com.concordia.smarthomesimulator.dataModels;

public class MapMeasurements {

    private final float scaleX;
    private final float scaleY;

    private final float width;
    private final float height;
    private final float available;

    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;

    /**
     * Instantiates a new Map measurement.
     *
     * @param width     the width of the canvas
     * @param height    the height of the canvas
     * @param available the available space to draw on the canvas
     * @param minX      the min x value in the layout
     * @param maxX      the max x value in the layout
     * @param minY      the min y value in the layout
     * @param maxY      the max y value in the layout
     * @param scaleX    the scale x (Layout ticks to pixels)
     * @param scaleY    the scale y (Layout ticks to pixels)
     */
    public MapMeasurements(float width, float height, float available, float minX, float maxX, float minY, float maxY, float scaleX, float scaleY) {
        this.width = width;
        this.height = height;
        this.available = available;

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     * Gets scale in terms of x. (Layout ticks to pixels)
     *
     * @return the scale x
     */
    public float getScaleX() {
        return scaleX;
    }

    /**
     * Gets scale in terms of y. (Layout ticks to pixels)
     *
     * @return the scale y
     */
    public float getScaleY() {
        return scaleY;
    }

    /**
     * Gets the width of the canvas.
     *
     * @return the width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height of the canvas.
     *
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Gets the available space to draw on the canvas. (Not reserved to default rooms)
     *
     * @return the available
     */
    public float getAvailable() {
        return available;
    }

    /**
     * Gets min x value in the layout.
     *
     * @return the min x
     */
    public float getMinX() {
        return minX;
    }

    /**
     * Gets max x value in the layout.
     *
     * @return the max x
     */
    public float getMaxX() {
        return maxX;
    }

    /**
     * Gets min y value in the layout.
     *
     * @return the min y
     */
    public float getMinY() {
        return minY;
    }

    /**
     * Gets max y value in the layout.
     *
     * @return the max y
     */
    public float getMaxY() {
        return maxY;
    }
}
