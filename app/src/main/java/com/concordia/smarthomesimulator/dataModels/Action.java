package com.concordia.smarthomesimulator.dataModels;

import android.content.Context;
import com.concordia.smarthomesimulator.helpers.HouseLayoutHelper;

import java.util.List;

import static com.concordia.smarthomesimulator.Constants.*;
import static com.concordia.smarthomesimulator.dataModels.DeviceType.LIGHT;
import static com.concordia.smarthomesimulator.dataModels.DeviceType.WINDOW;

public enum Action {
    INTERACT_ANY_WINDOW(PREFERENCES_KEY_ACTION_INTERACT_ANY_WINDOW),
    INTERACT_LOCAL_WINDOW(PREFERENCES_KEY_ACTION_INTERACT_LOCAL_WINDOW),
    INTERACT_ANY_LIGHT(PREFERENCES_KEY_ACTION_INTERACT_ANY_LIGHT),
    INTERACT_LOCAL_LIGHT(PREFERENCES_KEY_ACTION_INTERACT_LOCAL_LIGHT),
    INTERACT_GARAGE(PREFERENCES_KEY_ACTION_INTERACT_GARAGE),
    CHANGE_AWAY_MODE(PREFERENCES_KEY_ACTION_CHANGE_AWAY_MODE),
    CHANGE_PERMISSIONS_CONFIG(PREFERENCES_KEY_ACTION_CHANGE_PERMISSIONS_CONFIG),
    INTERACT_DOOR_LOCK(PREFERENCES_KEY_ACTION_INTERACT_DOOR_LOCK);

    private final String description;

    Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Action fromInteractingDevice(IDevice device, Context context){
        Room localRoom = null;
        boolean isUserInSameRoom;
        String loggedUsername = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getString(PREFERENCES_KEY_USERNAME,"");
        HouseLayout houseLayout = HouseLayoutHelper.getSelectedLayout(context);
        List<Room> rooms = houseLayout.getRooms();
        for (Room room: rooms){
            for (IDevice device1: room.getDevices()){
                if (device1.equals(device)){
                    localRoom = room;
                    break;
                }
            }
        }
        // Checking if the user is in the room
        if (localRoom == null){
            isUserInSameRoom = false;
        } else {
            isUserInSameRoom = localRoom.getInhabitants().stream().anyMatch(inhabitant -> inhabitant.getName().equalsIgnoreCase(loggedUsername));
        }
        switch (device.getDeviceType()) {
            case LIGHT:
                if (isUserInSameRoom){
                    return INTERACT_LOCAL_LIGHT;
                }
                return INTERACT_ANY_LIGHT;
            case WINDOW:
                if (isUserInSameRoom){
                    return INTERACT_LOCAL_WINDOW;
                }
                return INTERACT_ANY_WINDOW;
            default:
                // no restrictions for opening/closing doors
                return null;
        }
    }


}
