package com.concordia.smarthomesimulator.dataModels;

import android.content.SharedPreferences;

import static com.concordia.smarthomesimulator.Constants.*;

/**
 * Permissions configuration.
 *
 * This specifies what minimum permissions are required to perform certain actions.
 */
public class PermissionsConfiguration {
    private Permissions minToInteractAnyWindow;
    private Permissions minToInteractLocalWindow;
    private Permissions minToInteractAnyLight;
    private Permissions minToInteractLocalLight;
    private Permissions minToInteractGarage;
    private Permissions minToChangeAwayMode;
    private Permissions minToChangePermissionsConfigs;
    private Permissions minToInteractDoorLock;

    /**
     * To make a default Permission configuration
     */
    public PermissionsConfiguration() {
        this.minToInteractAnyWindow = DEFAULT_MIN_TO_INTERACT_ANY_WINDOW;
        this.minToInteractLocalWindow = DEFAULT_MIN_TO_INTERACT_LOCAL_WINDOW;
        this.minToInteractAnyLight = DEFAULT_MIN_TO_INTERACT_ANY_LIGHT;
        this.minToInteractLocalLight = DEFAULT_MIN_TO_INTERACT_LOCAL_LIGHT;
        this.minToInteractGarage = DEFAULT_MIN_TO_INTERACT_GARAGE;
        this.minToChangeAwayMode = DEFAULT_MIN_TO_CHANGE_AWAY_MODE;
        this.minToChangePermissionsConfigs = DEFAULT_MIN_TO_CHANGE_PERMISSIONS_CONFIG;
        this.minToInteractDoorLock = DEFAULT_MIN_TO_INTERACT_DOOR_LOCK;
    }

    /**
     * Constructor used to change the permission configuration. This changes the configs stored in the preferences as well.
     *
     * @param minToInteractAnyWindow        the min to interact any window
     * @param minToInteractLocalWindow      the min to interact local window
     * @param minToInteractAnyLight         the min to interact any light
     * @param minToInteractLocalLight       the min to interact local light
     * @param minToInteractGarage           the min to interact garage
     * @param minToChangeAwayMode           the min to change away mode
     * @param minToChangePermissionsConfigs the min to change permissions configs
     * @param minToInteractDoorLock         the min to interact door lock
     * @param preferences                   the preferences
     */
    public PermissionsConfiguration(Permissions minToInteractAnyWindow, Permissions minToInteractLocalWindow,
                                    Permissions minToInteractAnyLight, Permissions minToInteractLocalLight,
                                    Permissions minToInteractGarage, Permissions minToChangeAwayMode,
                                    Permissions minToChangePermissionsConfigs, Permissions minToInteractDoorLock,
                                    SharedPreferences preferences) {
        this.minToInteractAnyWindow = minToInteractAnyWindow;
        this.minToInteractLocalWindow = minToInteractLocalWindow;
        this.minToInteractAnyLight = minToInteractAnyLight;
        this.minToInteractLocalLight = minToInteractLocalLight;
        this.minToInteractGarage = minToInteractGarage;
        this.minToChangeAwayMode = minToChangeAwayMode;
        this.minToChangePermissionsConfigs = minToChangePermissionsConfigs;
        this.minToInteractDoorLock = minToInteractDoorLock;
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
        editor.putInt(Action.CHANGE_PERMISSIONS_CONFIG.getDescription(), minToChangePermissionsConfigs.getBitValue());
        editor.putInt(Action.INTERACT_DOOR_LOCK.getDescription(), minToInteractDoorLock.getBitValue());
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
        minToChangePermissionsConfigs= Permissions.fromInteger(preferences.getInt(Action.CHANGE_PERMISSIONS_CONFIG.getDescription(), 0));
        minToInteractDoorLock= Permissions.fromInteger(preferences.getInt(Action.INTERACT_DOOR_LOCK.getDescription(), 0));
    }

}
