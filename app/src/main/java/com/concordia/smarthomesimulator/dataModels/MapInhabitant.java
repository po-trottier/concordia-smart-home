package com.concordia.smarthomesimulator.dataModels;

import android.graphics.RectF;

public class MapInhabitant {

    private final RectF shape;
    private final Inhabitant inhabitant;

    /**
     * Instantiates a new Map inhabitant.
     *
     * @param shape      the shape
     * @param inhabitant the inhabitant
     */
    public MapInhabitant(RectF shape, Inhabitant inhabitant) {
        this.shape = shape;
        this.inhabitant = inhabitant;
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
     * Gets inhabitant.
     *
     * @return the inhabitant
     */
    public Inhabitant getInhabitant() {
        return inhabitant;
    }
}
