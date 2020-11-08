package com.concordia.smarthomesimulator.interfaces;

/**
 * This interface is used as a publisher of information to
 * classes implementing IObserver
 */
public interface ISubject {
    /**
     * Registers an observer to the ISubject class
     *
     * @param newObserver
     */
    public void register (IObserver newObserver);

    /**
     * Unregisters an observer to the ISubject class
     *
     * @param observer
     */
    public void unregister (IObserver observer);

    /**
     * Notifies observers of a specific action done by an ISubject class
     */
    public void notifyObserver ();


}
