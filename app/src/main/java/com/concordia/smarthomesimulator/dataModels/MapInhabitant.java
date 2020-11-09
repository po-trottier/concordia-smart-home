package com.concordia.smarthomesimulator.dataModels;

import android.graphics.RectF;
import com.concordia.smarthomesimulator.interfaces.IInhabitant;

public class MapInhabitant {

    private final RectF shape;
    private final IInhabitant inhabitant;

    /**
     * Instantiates a new Map inhabitant.
     *
     * @param shape      the shape
     * @param inhabitant the inhabitant
     */
    public MapInhabitant(RectF shape, IInhabitant inhabitant) {
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
    public IInhabitant getInhabitant() {
        return inhabitant;
    }
}
