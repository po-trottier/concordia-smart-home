package com.concordia.smarthomesimulator.dataModels;

import android.content.SharedPreferences;

import java.security.Permission;

import static com.concordia.smarthomesimulator.Constants.*;

public class PermissionConfiguration {
    private Permissions minToInteractAnyWindow;
    private Permissions minToInteractLocalWindow;
    private Permissions minToInteractAnyLight;
    private Permissions minToInteractLocalLight;
    private Permissions minToInteractGarage;
    private Permissions minToChangeAwayMode;

    public PermissionConfiguration() {
        this.minToInteractAnyWindow = DEFAULT_MIN_TO_INTERACT_ANY_WINDOW;
        this.minToInteractLocalWindow = DEFAULT_MIN_TO_INTERACT_LOCAL_WINDOW;
        this.minToInteractAnyLight = DEFAULT_MIN_TO_INTERACT_ANY_LIGHT;
        this.minToInteractLocalLight = DEFAULT_MIN_TO_INTERACT_LOCAL_LIGHT;
        this.minToInteractGarage = DEFAULT_MIN_TO_INTERACT_GARAGE;
        this.minToChangeAwayMode = DEFAULT_MIN_TO_CHANGE_AWAY_MODE;
    }

    public PermissionConfiguration(Permissions minToInteractAnyWindow, Permissions minToInteractLocalWindow,
                                   Permissions minToInteractAnyLight, Permissions minToInteractLocalLight,
                                   Permissions minToInteractGarage, Permissions minToChangeAwayMode,
                                   SharedPreferences preferences) {
        this.minToInteractAnyWindow = minToInteractAnyWindow;
        this.minToInteractLocalWindow = minToInteractLocalWindow;
        this.minToInteractAnyLight = minToInteractAnyLight;
        this.minToInteractLocalLight = minToInteractLocalLight;
        this.minToInteractGarage = minToInteractGarage;
        this.minToChangeAwayMode = minToChangeAwayMode;
        // the parametrized constructor is called when the permission configs are changing, changing the config now
        sendToContext(preferences);
    }

    /**
     * Load permission configuration.
     *
     * @param preferences the preferences
     */
    public void sendToContext(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREFERENCES_ACTION_INTERACT_ANY_WINDOW, minToInteractAnyWindow.getBitValue());
        editor.putInt(PREFERENCES_ACTION_INTERACT_LOCAL_WINDOW, minToInteractLocalWindow.getBitValue());
        editor.putInt(PREFERENCES_ACTION_INTERACT_ANY_LIGHT, minToInteractAnyLight.getBitValue());
        editor.putInt(PREFERENCES_ACTION_INTERACT_LOCAL_LIGHT, minToInteractLocalLight.getBitValue());
        editor.putInt(PREFERENCES_ACTION_INTERACT_GARAGE, minToInteractGarage.getBitValue());
        editor.putInt(PREFERENCES_ACTION_CHANGE_AWAY_MODE, minToChangeAwayMode.getBitValue());
        editor.apply();
    }

    /**
     * Save permission configuration.
     *
     * @param preferences the preferences
     */
    public void receiveFromContext(SharedPreferences preferences){
        minToInteractAnyWindow = Permissions.fromInteger(preferences.getInt(PREFERENCES_ACTION_INTERACT_ANY_WINDOW, 0));
        minToInteractAnyLight= Permissions.fromInteger(preferences.getInt(PREFERENCES_ACTION_INTERACT_ANY_WINDOW, 0));
        minToInteractLocalWindow= Permissions.fromInteger(preferences.getInt(PREFERENCES_ACTION_INTERACT_ANY_WINDOW, 0));
        minToInteractGarage= Permissions.fromInteger(preferences.getInt(PREFERENCES_ACTION_INTERACT_ANY_WINDOW, 0));
        minToInteractLocalLight= Permissions.fromInteger(preferences.getInt(PREFERENCES_ACTION_INTERACT_ANY_WINDOW, 0));
        minToChangeAwayMode= Permissions.fromInteger(preferences.getInt(PREFERENCES_ACTION_INTERACT_ANY_WINDOW, 0));
    }

}
