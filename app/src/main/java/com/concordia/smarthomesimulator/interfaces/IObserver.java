package com.concordia.smarthomesimulator.interfaces;

/**
 * This interface is used observe specific actions made by
 * ISubjects.
 */
public interface IObserver {

    /**
     * Updates the Away mode status
     *
     * @param awayMode away mode
     */
     void updateAwayMode(boolean awayMode, String callTimer);
}
