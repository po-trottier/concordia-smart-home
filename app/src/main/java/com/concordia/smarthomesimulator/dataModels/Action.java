package com.concordia.smarthomesimulator.dataModels;

import static com.concordia.smarthomesimulator.Constants.*;

public enum Action {
    INTERACT_ANY_WINDOW(PREFERENCES_ACTION_INTERACT_ANY_WINDOW),
    INTERACT_LOCAL_WINDOW(PREFERENCES_ACTION_INTERACT_LOCAL_WINDOW),
    INTERACT_ANY_LIGHT(PREFERENCES_ACTION_INTERACT_ANY_LIGHT),
    INTERACT_LOCAL_LIGHT(PREFERENCES_ACTION_INTERACT_LOCAL_LIGHT),
    INTERACT_GARAGE(PREFERENCES_ACTION_INTERACT_GARAGE),
    CHANGE_AWAY_MODE(PREFERENCES_ACTION_CHANGE_AWAY_MODE);

    private final String description;

    Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
