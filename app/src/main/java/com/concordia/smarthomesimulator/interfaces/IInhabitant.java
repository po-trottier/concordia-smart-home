package com.concordia.smarthomesimulator.interfaces;

import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.UserPreferences;
import com.concordia.smarthomesimulator.enums.Permissions;

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
}
