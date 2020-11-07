package com.concordia.smarthomesimulator.helpers;

import com.concordia.smarthomesimulator.interfaces.IObserver;

import java.util.ArrayList;

public  class ObserverHelper {

    private ArrayList<IObserver> observers;
    private static ObserverHelper instance;

    /**
     * Instantiates Observer Helper singleton
     */
    private ObserverHelper(){
        observers = new ArrayList<>();
    }

    /**
     * This method makes sure that only one ObserverHelper is created
     *
     * @return instance
     */
    public static ObserverHelper getInstance(){
        if(instance == null){
            instance = new ObserverHelper();
        }
        return instance;
    }

    /**
     * Returns list of observers to be used
     *
     * @return observers
     */
    public ArrayList<IObserver> getObservers(){
        return this.observers;
    }

    /**
     * Adds an observer to the observers list
     *
     * @param observer
     */
    public void addObserver(IObserver observer){
        observers.add(observer);
    }
}

