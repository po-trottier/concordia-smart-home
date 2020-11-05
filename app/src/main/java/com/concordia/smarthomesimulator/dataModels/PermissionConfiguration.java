package com.concordia.smarthomesimulator.dataModels;

import android.content.SharedPreferences;

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
        editor.putInt(Action.INTERACT_ANY_WINDOW.getDescription(), minToInteractAnyWindow.getBitValue());
        editor.putInt(Action.INTERACT_LOCAL_WINDOW.getDescription(), minToInteractLocalWindow.getBitValue());
        editor.putInt(Action.INTERACT_ANY_LIGHT.getDescription(), minToInteractAnyLight.getBitValue());
        editor.putInt(Action.INTERACT_LOCAL_LIGHT.getDescription(), minToInteractLocalLight.getBitValue());
        editor.putInt(Action.INTERACT_GARAGE.getDescription(), minToInteractGarage.getBitValue());
        editor.putInt(Action.CHANGE_AWAY_MODE.getDescription(), minToChangeAwayMode.getBitValue());
        editor.apply();
    }

    /**
     * Save permission configuration.
     *
     * @param preferences the preferences
     */
    public void receiveFromContext(SharedPreferences preferences){
        minToInteractAnyWindow = Permissions.fromInteger(preferences.getInt(Action.INTERACT_ANY_WINDOW.getDescription(), 0));
        minToInteractAnyLight= Permissions.fromInteger(preferences.getInt(Action.INTERACT_LOCAL_WINDOW.getDescription(), 0));
        minToInteractLocalWindow= Permissions.fromInteger(preferences.getInt(Action.INTERACT_ANY_LIGHT.getDescription(), 0));
        minToInteractGarage= Permissions.fromInteger(preferences.getInt(Action.INTERACT_LOCAL_LIGHT.getDescription(), 0));
        minToInteractLocalLight= Permissions.fromInteger(preferences.getInt(Action.INTERACT_GARAGE.getDescription(), 0));
        minToChangeAwayMode= Permissions.fromInteger(preferences.getInt(Action.CHANGE_AWAY_MODE.getDescription(), 0));
    }

}
