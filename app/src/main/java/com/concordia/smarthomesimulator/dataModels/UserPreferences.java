package com.concordia.smarthomesimulator.dataModels;

import android.content.SharedPreferences;

import static com.concordia.smarthomesimulator.Constants.*;
import static com.concordia.smarthomesimulator.Constants.PREFERENCES_KEY_STATUS;

public class UserPreferences {
    private String username;
    private String password;
    private Permissions permissions;
    private int temperature;
    private String timeZone;
    private boolean status;

    private final static String DEFAULT_TIME_ZONE = "America/Los_Angeles";
    private final static int DEFAULT_TEMPERATURE = 20;
    private final static boolean DEFAULT_STATUS = false;

    public UserPreferences(User user, int temperature, String timeZone, boolean status) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.permissions = user.getPermission();
        this.temperature = temperature;
        this.timeZone = timeZone;
        this.status = status;
    }

    public UserPreferences(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.permissions = user.getPermission();
        this.temperature = DEFAULT_TEMPERATURE;
        this.timeZone = DEFAULT_TIME_ZONE;
        this.status = DEFAULT_STATUS;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void loadUserPreferences(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFERENCES_KEY_USERNAME, username);
        editor.putString(PREFERENCES_KEY_PASSWORD, password);
        editor.putInt(PREFERENCES_KEY_PERMISSIONS, permissions.getBitValue());
        editor.putInt(PREFERENCES_KEY_TEMPERATURE, temperature);
        editor.putString(PREFERENCES_KEY_TIME_ZONE, timeZone);
        editor.putBoolean(PREFERENCES_KEY_STATUS, status);
        editor.apply();
    }

    public void saveUserPreferences(SharedPreferences preferences){
        username = preferences.getString(PREFERENCES_KEY_USERNAME, "");
        password = preferences.getString(PREFERENCES_KEY_PASSWORD,"");
        permissions = Permissions.fromInteger(preferences.getInt(PREFERENCES_KEY_PERMISSIONS,0));
        temperature = preferences.getInt(PREFERENCES_KEY_TEMPERATURE,0);
        timeZone = preferences.getString(PREFERENCES_KEY_TIME_ZONE,"");
        status = preferences.getBoolean(PREFERENCES_KEY_STATUS, false);
    }
}
