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

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getAvailable() {
        return available;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }
}
