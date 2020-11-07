package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.Permissions;

public class Constants {
    public static final int WRITE_PERMISSION_REQUEST_CODE = 1000;
    public static final int READ_PERMISSION_REQUEST_CODE = 1001;

    public static final String PREFERENCES_KEY_STATUS = "status";
    public static final String PREFERENCES_KEY_TEMPERATURE = "temperature";
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
    public static final String PREFERENCES_KEY_ACTION_INTERACT_ANY_WINDOW = "INTERACT_ANY_WINDOW";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_LOCAL_WINDOW = "INTERACT_LOCAL_WINDOW";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_ANY_LIGHT = "INTERACT_ANY_LIGHT";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_LOCAL_LIGHT = "INTERACT_LOCAL_LIGHT";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_GARAGE = "INTERACT_GARAGE";
    public static final String PREFERENCES_KEY_ACTION_CHANGE_AWAY_MODE = "CHANGE_AWAY_MODE";
    public static final String PREFERENCES_KEY_ACTION_CHANGE_PERMISSIONS_CONFIG = "CHANGE_PERMISSIONS_CONFIG";
    public static final String PREFERENCES_KEY_ACTION_INTERACT_DOOR_LOCK = "INTERACT_DOOR_LOCK";

    public static final Permissions DEFAULT_MIN_TO_INTERACT_ANY_WINDOW = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_LOCAL_WINDOW = Permissions.GUEST;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_ANY_LIGHT = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_LOCAL_LIGHT = Permissions.GUEST;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_GARAGE = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_CHANGE_AWAY_MODE = Permissions.CHILD;
    public static final Permissions DEFAULT_MIN_TO_CHANGE_PERMISSIONS_CONFIG = Permissions.PARENT;
    public static final Permissions DEFAULT_MIN_TO_INTERACT_DOOR_LOCK = Permissions.PARENT;

    public static final int DEFAULT_TEMPERATURE = 20;
    public static final float DEFAULT_TIME_SCALE = 1f;
    public static final boolean DEFAULT_STATUS = false;

    public static final String DEFAULT_NAME_OUTDOORS = "Outdoors";
    public static final String DEFAULT_NAME_GARAGE = "Garage";

    public static final String DEMO_LAYOUT_NAME = "Demo Layout";
    public static final String EMPTY_LAYOUT_NAME = "Empty Layout";

    public static final String DIRECTORY_NAME_LAYOUTS = "layouts";

    public static final float[] AVAILABLE_TIME_FACTORS = { 0.5f, 1f, 1.5f, 2f, 5f, 10f, 50f, 100f };
    public static final String DATE_FORMAT = "MMMM d yyyy";
    public static final String TIME_FORMAT = "h:mm a";
}
