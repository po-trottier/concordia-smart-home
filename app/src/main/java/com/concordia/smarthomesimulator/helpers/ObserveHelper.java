package com.concordia.smarthomesimulator.helpers;

import com.concordia.smarthomesimulator.interfaces.IObserver;

import java.util.ArrayList;

public  class ObserveHelper {

    private ArrayList<IObserver> observers;
    private static  ObserveHelper instance;

    private ObserveHelper(){
        observers = new ArrayList<>();

    }

    public static ObserveHelper getInstance(){
        if(instance == null){
            instance = new ObserveHelper();
        }
        return instance;
    }

    public ArrayList<IObserver> getObservers(){
        return this.observers;
    }

    public void addObserver(IObserver observer){
        observers.add(observer);
    }
}

