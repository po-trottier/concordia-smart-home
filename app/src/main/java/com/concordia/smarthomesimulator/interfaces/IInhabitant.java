package com.concordia.smarthomesimulator.interfaces;

import androidx.annotation.NonNull;

public interface IInhabitant {
    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName();

    /**
     * Determines if inhabitant is an intruder.
     *
     * @return true or false
     */
    public boolean isIntruder();

    @NonNull
    public IInhabitant clone();
}
