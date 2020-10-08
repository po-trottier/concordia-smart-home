package com.concordia.smarthomesimulator.dataModels;

public class Geometry {

    public float x;
    public float y;

    public float width;
    public float height;

    Geometry() {}

    Geometry(float width, float height) {
        x = 0;
        y = 0;
        this.width = width;
        this.height = height;
    }
}
