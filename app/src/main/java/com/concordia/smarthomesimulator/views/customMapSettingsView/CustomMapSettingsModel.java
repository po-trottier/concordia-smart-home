package com.concordia.smarthomesimulator.views.customMapSettingsView;

import android.content.Context;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.helpers.HouseLayoutHelper;

public class CustomMapSettingsModel {

    private HouseLayout layout;
    private Room selectedRoom;

    public CustomMapSettingsModel() { }

    public HouseLayout getLayout() {
        return layout;
    }

    public void updateLayout(Context context) {
        layout = HouseLayoutHelper.getSelectedLayout(context);
    }

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room room) {
        selectedRoom = room;
    }
}
