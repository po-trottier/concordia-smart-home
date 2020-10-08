package com.concordia.smarthomesimulator.helpers;

import com.concordia.smarthomesimulator.dataContracts.HouseLayout;

public class LayoutHelper {

    private HouseLayout layout;

    public HouseLayout getHouseLayout() {
        return layout == null ? readHouseLayout() : layout;
    }

    public static void saveHouseLayout(HouseLayout layout) {
    }

    private static HouseLayout readHouseLayout() {
        return null;
    }
}
