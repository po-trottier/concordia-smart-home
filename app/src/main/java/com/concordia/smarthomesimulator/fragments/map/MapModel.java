package com.concordia.smarthomesimulator.fragments.map;

import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;

public class MapModel extends ViewModel {

    private HouseLayout houseLayout;

    public MapModel() {
    }

    /**
     * Gets house layout.
     *
     * @return the house layout
     */
    public HouseLayout getHouseLayout() {
        return houseLayout;
    }

    /**
     * Sets house layout.
     *
     * @param houseLayout the house layout
     */
    public void setHouseLayout(HouseLayout houseLayout) {
        this.houseLayout = houseLayout;
    }
}