package com.concordia.smarthomesimulator.enums;

import static com.concordia.smarthomesimulator.Constants.*;

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
}