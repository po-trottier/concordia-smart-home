package com.concordia.smarthomesimulator.fragments.map;

import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MapModel extends ViewModel {

    private HouseLayout houseLayout;
    private int bottomCardState;

    public MapModel() {
        bottomCardState = BottomSheetBehavior.STATE_COLLAPSED;
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

    /**
     * Gets bottom card state.
     *
     * @return the bottom card state
     */
    public int getBottomCardState() {
        return bottomCardState;
    }

    /**
     * Sets bottom card state.
     *
     * @param bottomCardState the bottom card state
     */
    public void setBottomCardState(int bottomCardState) {
        this.bottomCardState = bottomCardState;
    }
}