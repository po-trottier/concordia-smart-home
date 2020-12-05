package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.enums.Permissions;

public class Constants {
    // Permissions Request Codes
    public static final int WRITE_PERMISSION_REQUEST_CODE = 1000;
    public static final int READ_PERMISSION_REQUEST_CODE = 1001;

    // Keys for Shared Preferences
    public static final String PREFERENCES_KEY_AWAY_MODE = "awayMode";
    public static final String PREFERENCES_KEY_CALL_DELAY = "callTimer";
    public static final String PREFERENCES_KEY_STATUS = "status";
    public static final String PREFERENCES_KEY_TEMPERATURE = "temperature";
    public static final String PREFERENCES_KEY_MAX_TEMPERATURE_ALERT = "maxAlertTemperature";
    public static final String PREFERENCES_KEY_MIN_TEMPERATURE_ALERT = "minAlertTemperature";
    public static final String PREFERENCES_KEY_USERNAME = "username";
    public static final String PREFERENCES_KEY_PASSWORD = "password";
    public static final String PREFERENCES_KEY_PERMISSIONS = "permissions";
    public static final String PREFERENCES_KEY_LAYOUT = "layout";
    public static final String PREFERENCES_KEY_DATETIME_YEAR = "year";
    public static final String PREFERENCES_KEY_DATETIME_MONTH = "month";
    public static final String PREFERENCES_KEY_DATETIME_DAY = "day";
    public static final String PREFERENCES_KEY_DATETIME_HOUR = "hour";
    public static final String PREFERENCES_KEY_DATETIME_MINUTE = "minute";
    public static final String PREFERENCES_KEY_TIME_SCALE = "scale";

    // Keys for Shared Preferences Actions
    public static final String PREFERENCES_KEY_ACTION_INTERACT_ANY_WINDOW = "Open/Close_Any_Window";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_LOCAL_WINDOW = "Open/Close_Local_Windows";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_ANY_LIGHT = "Turn_On/Off_Any_Light";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_LOCAL_LIGHT = "Turn_On/Off_Local Lights";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_GARAGE = "Open/Close_Garage";
    public static final String PREFERENCES_KEY_ACTION_CHANGE_AWAY_MODE = "Set_Away_Mode";
    public static final String PREFERENCES_KEY_ACTION_CHANGE_PERMISSIONS_CONFIG = "Edit_Permissions_Configuration";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_DOOR_LOCK = "Lock/Unlock_Doors";
    public static final String PREFERENCES_KEY_ACTION_MODIFY_USERBASE = "Modify_Userbase";
    public static final String PREFERENCES_KEY_ACTION_MODIFY_TEMPERATURE = "Modify_Temperature";

    // Default Minimum User Type for Permissions
    public static final Permissions DEFAULT_MIN_TO_INTERACT_ANY_WINDOW = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_LOCAL_WINDOW = Permissions.GUEST;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_ANY_LIGHT = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_LOCAL_LIGHT = Permissions.GUEST;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_GARAGE = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_CHANGE_AWAY_MODE = Permissions.CHILD;
    public static final Permissions DEFAULT_MIN_TO_CHANGE_PERMISSIONS_CONFIG = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_DOOR_LOCK = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_MODIFY_USERBASE = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_MODIFY_TEMPERATURE = Permissions.PARENT;

    // Default Simulation Context Values
    public static final int DEFAULT_CALL_DELAY = 5;
    public static final int DEFAULT_TEMPERATURE = 20;
    public static final int DEFAULT_MAX_TEMPERATURE_ALERT = 35;
    public static final int DEFAULT_MIN_TEMPERATURE_ALERT = 0;
    public static final float DEFAULT_TIME_SCALE = 1f;
    public static final boolean DEFAULT_STATUS = false;
    public static final boolean DEFAULT_EXTREME_TEMPERATURE_DETECTION = false;

    // Default House Layout Values
    public static final String DEFAULT_NAME_OUTDOORS = "Backyard";
    public static final String DEFAULT_NAME_GARAGE = "Garage";
    public static final String DEFAULT_NAME_HEATING_ZONE = "Default";

    // Default House Layout names
    public static final String DEMO_LAYOUT_NAME = "Demo Layout";
    public static final String EMPTY_LAYOUT_NAME = "Empty Layout";

    // Directories
    public static final String DIRECTORY_NAME_LAYOUTS = "layouts";

    // Clock Values
    public static final float[] AVAILABLE_TIME_FACTORS = { 0.1f, 0.5f, 1f, 1.5f, 2f, 5f, 10f, 50f, 100f, 200f };
    public static final String DATE_FORMAT = "MMMM d yyyy";
    public static final String TIME_FORMAT = "h:mm a";

    // Temperature Values
    public static final double MAXIMUM_TEMPERATURE = 100;
    public static final double MINIMUM_TEMPERATURE = -100;

    // Notifications
    public final static String NOTIFICATION_CHANNEL = "86b2d2f2-3a9d-431b-a757-fd35c6d943f6";
}
