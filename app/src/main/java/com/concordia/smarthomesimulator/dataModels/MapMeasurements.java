package com.concordia.smarthomesimulator.dataModels;

public class MapMeasurements {

    private float scaleX;
    private float scaleY;

    private float width;
    private float height;
    private float available;

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public MapMeasurements(float width, float height, float available, int minX, int maxX, int minY, int maxY, float scaleX, float scaleY) {
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

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }
}
