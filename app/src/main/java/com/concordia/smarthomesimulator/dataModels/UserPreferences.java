package com.concordia.smarthomesimulator.dataModels;

import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.enums.Permissions;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.concordia.smarthomesimulator.Constants.*;

public class UserPreferences implements Serializable {

    private String username;
    private String password;
    private String layout;
    private Permissions permissions;
    private boolean status;
    private int temperature;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private float timeFactor;

    /**
     * Instantiates a new User preferences.
     *
     * @param username    the username
     * @param password    the password
     * @param permissions the permissions
     */
    public UserPreferences(String username, String password, Permissions permissions) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.temperature = DEFAULT_TEMPERATURE;
        this.status = DEFAULT_STATUS;
        this.timeFactor = 1f;

        LocalDateTime timeNow = LocalDateTime.now();
        this.year = timeNow.getYear();
        this.month = timeNow.getMonthValue();
        this.day = timeNow.getDayOfMonth();
        this.hour = timeNow.getHour();
        this.minute = timeNow.getMinute();
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
     * Is status boolean.
     *
     * @return the boolean
     */
    public boolean getStatus() {
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
     * Gets the selected layout.
     *
     * @return the layout
     */
    public String getLayout() {
        return layout;
    }

    /**
     * Sets the selected layout.
     *
     * @param layout the layout
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * Gets date time.
     *
     * @return the date time
     */
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    /**
     * Sets date time.
     *
     * @param year   the year
     * @param month  the month
     * @param day    the day
     * @param hour   the hour
     * @param minute the minute
     */
    public void setDateTime(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Gets time factor.
     *
     * @return the time factor
     */
    public float getTimeFactor() {
        return timeFactor;
    }

    /**
     * Sets time factor.
     *
     * @param timeFactor the time factor
     */
    public void setTimeFactor(float timeFactor) {
        this.timeFactor = timeFactor;
    }

    /**
     * Load user preferences.
     *
     * @param preferences the preferences
     */
    public void sendToContext(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFERENCES_KEY_USERNAME, username);
        editor.putString(PREFERENCES_KEY_PASSWORD, password);
        editor.putInt(PREFERENCES_KEY_PERMISSIONS, permissions.getBitValue());
        editor.putInt(PREFERENCES_KEY_TEMPERATURE, temperature);
        editor.putBoolean(PREFERENCES_KEY_STATUS, status);
        editor.putString(PREFERENCES_KEY_LAYOUT, layout);
        editor.putFloat(PREFERENCES_KEY_TIME_SCALE, timeFactor);
        editor.putInt(PREFERENCES_KEY_DATETIME_YEAR, year);
        editor.putInt(PREFERENCES_KEY_DATETIME_MONTH, month);
        editor.putInt(PREFERENCES_KEY_DATETIME_DAY, day);
        editor.putInt(PREFERENCES_KEY_DATETIME_HOUR, hour);
        editor.putInt(PREFERENCES_KEY_DATETIME_MINUTE, minute);
        editor.apply();
    }

    /**
     * Save user preferences.
     *
     * @param preferences the preferences
     */
    public void receiveFromContext(SharedPreferences preferences){
        username = preferences.getString(PREFERENCES_KEY_USERNAME, "");
        password = preferences.getString(PREFERENCES_KEY_PASSWORD,"");
        permissions = Permissions.fromInteger(preferences.getInt(PREFERENCES_KEY_PERMISSIONS,0));
        temperature = preferences.getInt(PREFERENCES_KEY_TEMPERATURE,DEFAULT_TEMPERATURE);
        status = preferences.getBoolean(PREFERENCES_KEY_STATUS, DEFAULT_STATUS);
        layout = preferences.getString(PREFERENCES_KEY_LAYOUT, "");
        timeFactor = preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE);

        LocalDateTime timeNow = LocalDateTime.now();
        year = preferences.getInt(PREFERENCES_KEY_DATETIME_YEAR, timeNow.getYear());
        month = preferences.getInt(PREFERENCES_KEY_DATETIME_MONTH, timeNow.getMonthValue());
        day = preferences.getInt(PREFERENCES_KEY_DATETIME_DAY, timeNow.getDayOfMonth());
        hour = preferences.getInt(PREFERENCES_KEY_DATETIME_HOUR, timeNow.getHour());
        minute = preferences.getInt(PREFERENCES_KEY_DATETIME_MINUTE, timeNow.getMinute());
    }

    /**
     * Clear the user preferences
     *
     * @param sharedPreferences the shared preferences
     */
    public static void clear(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PREFERENCES_KEY_USERNAME);
        editor.remove(PREFERENCES_KEY_PASSWORD);
        editor.remove(PREFERENCES_KEY_PERMISSIONS);
        editor.remove(PREFERENCES_KEY_TEMPERATURE);
        editor.remove(PREFERENCES_KEY_STATUS);
        editor.remove(PREFERENCES_KEY_LAYOUT);
        editor.remove(PREFERENCES_KEY_TIME_SCALE);
        editor.remove(PREFERENCES_KEY_DATETIME_YEAR);
        editor.remove(PREFERENCES_KEY_DATETIME_MONTH);
        editor.remove(PREFERENCES_KEY_DATETIME_DAY);
        editor.remove(PREFERENCES_KEY_DATETIME_HOUR);
        editor.remove(PREFERENCES_KEY_DATETIME_MINUTE);
        editor.apply();
    }
}
