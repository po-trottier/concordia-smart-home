package com.concordia.smarthomesimulator.enums;

import android.content.Context;
import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.helpers.LayoutsHelper;
import com.concordia.smarthomesimulator.interfaces.IDevice;

import java.util.List;

import static com.concordia.smarthomesimulator.Constants.*;

public enum Action {

    INTERACT_ANY_WINDOW(PREFERENCES_KEY_ACTION_INTERACT_ANY_WINDOW),
    INTERACT_LOCAL_WINDOW(PREFERENCES_KEY_ACTION_INTERACT_LOCAL_WINDOW),
    INTERACT_ANY_LIGHT(PREFERENCES_KEY_ACTION_INTERACT_ANY_LIGHT),
    INTERACT_LOCAL_LIGHT(PREFERENCES_KEY_ACTION_INTERACT_LOCAL_LIGHT),
    INTERACT_GARAGE(PREFERENCES_KEY_ACTION_INTERACT_GARAGE),
    CHANGE_AWAY_MODE(PREFERENCES_KEY_ACTION_CHANGE_AWAY_MODE),
    CHANGE_PERMISSIONS_CONFIG(PREFERENCES_KEY_ACTION_CHANGE_PERMISSIONS_CONFIG),
    INTERACT_DOOR_LOCK(PREFERENCES_KEY_ACTION_INTERACT_DOOR_LOCK),
    MODIFY_USERBASE(PREFERENCES_KEY_ACTION_MODIFY_USERBASE),
    MODIFY_TEMPERATURE(PREFERENCES_KEY_ACTION_MODIFY_TEMPERATURE);

    private final String description;

    Action(String description) {
        this.description = description;
    }

    /**
     * Gets action description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get action from device (a limited set of actions can be returned).
     *
     * @param device  the device
     * @param context the context
     * @return the action
     */
    public static Action fromDevice(IDevice device, Context context){
        // Get logged user's username
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String loggedUser = preferences.getString(PREFERENCES_KEY_USERNAME,"");
        // Find the room in which the device resides
        HouseLayout houseLayout = LayoutsHelper.getSelectedLayout(context);
        List<Room> rooms = houseLayout.getRooms();
        Room room = rooms.stream()
            .filter(r -> r.getDevices().stream().anyMatch(d -> d.equals(device)))
            .findFirst()
            .orElse(null);
        // Checking if the user is in the same room
        boolean sameRoom;
        if (room == null) {
            sameRoom = false;
        } else {
            sameRoom = room.getInhabitants().stream().anyMatch(inhabitant -> inhabitant.getName().equalsIgnoreCase(loggedUser));
        }
        // Return the right action
        switch (device.getDeviceType()) {
            case LIGHT: return sameRoom ? INTERACT_LOCAL_LIGHT : INTERACT_ANY_LIGHT;
            case WINDOW: return sameRoom ? INTERACT_LOCAL_WINDOW : INTERACT_ANY_WINDOW;
            case DOOR: return room.getName().equals(DEFAULT_NAME_GARAGE) ? INTERACT_GARAGE : null;
            default: return null;
        }
    }


}
