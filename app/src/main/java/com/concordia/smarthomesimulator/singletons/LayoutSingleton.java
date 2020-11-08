package com.concordia.smarthomesimulator.singletons;

import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.interfaces.OnIntruderDetectedListener;

import java.util.Observable;
import java.util.Observer;

/**
 * A singleton House Layout used throughout the app as "the selected layout"
 */
public class LayoutSingleton implements Observer {

    private static LayoutSingleton instance;

    private HouseLayout layout;
    private OnIntruderDetectedListener listener;

    private LayoutSingleton() { }

    //region Static Methods

    @Override
    public void update(Observable observable, Object arg) {
        HouseLayout layout = (HouseLayout) observable;
        // If an intruder was detected, call the listener's callback
        if (layout != null && layout.isIntruderDetected()) {
            listener.onIntruderDetected();
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static LayoutSingleton getInstance() {
        if (instance == null) {
            instance = new LayoutSingleton();
        }
        return instance;
    }

    //endregion

    /**
     * Gets layout.
     *
     * @return the layout
     */
    public HouseLayout getLayout() {
        return layout;
    }

    /**
     * Sets layout.
     *
     * @param layout the layout
     */
    public void setLayout(HouseLayout layout) {
        if (layout == null) {
            return;
        }
        if (this.layout != null) {
            // Remove the observer from the old layout
            this.layout.deleteObserver(this);
        }
        // Set the new layout and add the observer
        this.layout = layout;
        this.layout.addObserver(this);
        update(this.layout, null);
    }

    /**
     * Sets on intruder detected listener.
     *
     * @param listener the listener
     */
    public void setOnIntruderDetectedListener(OnIntruderDetectedListener listener) {
        this.listener = listener;
    }
}
