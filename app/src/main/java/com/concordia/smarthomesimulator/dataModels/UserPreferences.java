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

    /**
     * Instantiates a new User preferences.
     *
     * @param user        the user
     * @param temperature the temperature
     * @param timeZone    the time zone
     * @param status      the status
     */
    public UserPreferences(User user, int temperature, String timeZone, boolean status) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.permissions = user.getPermission();
        this.temperature = temperature;
        this.timeZone = timeZone;
        this.status = status;
    }

    /**
     * Instantiates a new User preferences.
     *
     * @param user the user
     */
    public UserPreferences(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.permissions = user.getPermission();
        this.temperature = DEFAULT_TEMPERATURE;
        this.timeZone = DEFAULT_TIME_ZONE;
        this.status = DEFAULT_STATUS;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets permissions.
     *
     * @return the permissions
     */
    public Permissions getPermissions() {
        return permissions;
    }

    /**
     * Sets permissions.
     *
     * @param permissions the permissions
     */
    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    /**
     * Gets temperature.
     *
     * @return the temperature
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * Sets temperature.
     *
     * @param temperature the temperature
     */
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets time zone.
     *
     * @return the time zone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets time zone.
     *
     * @param timeZone the time zone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Is status boolean.
     *
     * @return the boolean
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Load user preferences.
     *
     * @param preferences the preferences
     */
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

    /**
     * Save user preferences.
     *
     * @param preferences the preferences
     */
    public void saveUserPreferences(SharedPreferences preferences){
        username = preferences.getString(PREFERENCES_KEY_USERNAME, "");
        password = preferences.getString(PREFERENCES_KEY_PASSWORD,"");
        permissions = Permissions.fromInteger(preferences.getInt(PREFERENCES_KEY_PERMISSIONS,0));
        temperature = preferences.getInt(PREFERENCES_KEY_TEMPERATURE,0);
        timeZone = preferences.getString(PREFERENCES_KEY_TIME_ZONE,"");
        status = preferences.getBoolean(PREFERENCES_KEY_STATUS, false);
    }
}
