package com.concordia.smarthomesimulator.dataModels;

import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.enums.Permissions;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.concordia.smarthomesimulator.Constants.*;

public class UserPreferences implements Serializable {

    private String username;
    private String password;
    private String layout;
    private Permissions permissions;
    private boolean status;
    private int temperature;
    private int maxTemperatureAlert;
    private int minTemperatureAlert;
    private int summerTemperature;
    private int winterTemperature;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private float timeFactor;
    private int minLightsHour;
    private int minLightsMinute;
    private int maxLightsHour;
    private int maxLightsMinute;
    private int winterStart;
    private int winterEnd;
    private int summerStart;
    private int summerEnd;

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
        this.maxTemperatureAlert = DEFAULT_MAX_TEMPERATURE_ALERT;
        this.minTemperatureAlert = DEFAULT_MIN_TEMPERATURE_ALERT;
        this.status = DEFAULT_STATUS;
        this.timeFactor = DEFAULT_TIME_SCALE;
        this.winterStart = DEFAULT_WINTER_START;
        this. winterEnd = DEFAULT_WINTER_END;
        this.summerStart = DEFAULT_SUMMER_START;
        this.summerEnd = DEFAULT_SUMMER_END;

        LocalTime timeMinLights = DEFAULT_MIN_LIGHTS_TIME;
        this.minLightsHour = timeMinLights.getHour();
        this.minLightsMinute = timeMinLights.getMinute();

        LocalTime timeMaxLights = DEFAULT_MAX_LIGHTS_TIME;
        this.maxLightsHour = timeMaxLights.getHour();
        this.maxLightsMinute = timeMaxLights.getMinute();

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
     * Gets maximum temperature
     *
     * @return the highest temperature permitted before alerting user
     */
    public int getMaxTemperatureAlert() { return maxTemperatureAlert; }

    /**
     * Sets the max temperature
     *
     * @param maxTemperatureAlert the highest temperature permitted before alerting user
     */
    public void setMaxTemperatureAlert(int maxTemperatureAlert) { this.maxTemperatureAlert = maxTemperatureAlert; }

    /**
     * Gets minimum temperature
     *
     * @return the lowest temperature permitted before alerting user
     */
    public int getMinTemperatureAlert() { return minTemperatureAlert; }

    /**
     * Sets the max temperature
     *
     * @param minTemperatureAlert the lowest temperature permitted before alerting user
     */
    public void setMinTemperatureAlert(int minTemperatureAlert) { this.minTemperatureAlert = minTemperatureAlert; }

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
     * Gets minimum lights time.
     *
     * @return the minimum lights time
     */
    public LocalTime getMinLightsTime() {
        return LocalTime.of(minLightsHour, minLightsMinute);
    }

    /**
     * Sets minimum lights time.
     *

     * @param minLightsHour the hour
     * @param minLightsMinute the minute
     */
    public void setMinLightsTime(int minLightsHour, int minLightsMinute) {
        this.minLightsHour = minLightsHour;
        this.minLightsMinute = minLightsMinute;
    }

    /**
     * Gets maximum lights time.
     *
     * @return the maximum lights time
     */
    public LocalTime getMaxLightsTime() {
        return LocalTime.of(maxLightsHour, maxLightsMinute);
    }

    /**
     * Sets maximum lights time.
     *

     * @param maxLightsHour the hour
     * @param maxLightsMinute the minute
     */
    public void setMaxLightsTime(int maxLightsHour, int maxLightsMinute) {
        this.maxLightsHour = maxLightsHour;
        this.maxLightsMinute = maxLightsMinute;
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
        editor.putString(PREFERENCES_KEY_LAYOUT, layout);
        editor.putBoolean(PREFERENCES_KEY_STATUS, status);
        editor.putFloat(PREFERENCES_KEY_TIME_SCALE, timeFactor);
        editor.putInt(PREFERENCES_KEY_PERMISSIONS, permissions.getBitValue());
        editor.putInt(PREFERENCES_KEY_TEMPERATURE, temperature);
        editor.putInt(PREFERENCES_KEY_MAX_TEMPERATURE_ALERT,maxTemperatureAlert);
        editor.putInt(PREFERENCES_KEY_MIN_TEMPERATURE_ALERT,minTemperatureAlert);
        editor.putInt(PREFERENCES_KEY_DATETIME_YEAR, year);
        editor.putInt(PREFERENCES_KEY_DATETIME_MONTH, month);
        editor.putInt(PREFERENCES_KEY_DATETIME_DAY, day);
        editor.putInt(PREFERENCES_KEY_DATETIME_HOUR, hour);
        editor.putInt(PREFERENCES_KEY_DATETIME_MINUTE, minute);
        editor.putInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_HOUR, minLightsHour);
        editor.putInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_MINUTE, minLightsMinute);
        editor.putInt(PREFERENCES_KEY_MAX_LIGHTS_TIME_HOUR, maxLightsHour);
        editor.putInt(PREFERENCES_KEY_MAX_LIGHTS_TIME_MINUTE, maxLightsMinute);
        editor.putInt(PREFERENCES_KEY_WINTER_START, winterStart);
        editor.putInt(PREFERENCES_KEY_WINTER_END, winterEnd);
        editor.putInt(PREFERENCES_KEY_SUMMER_START, summerStart);
        editor.putInt(PREFERENCES_KEY_SUMMER_END, summerEnd);
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
        temperature = preferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE);
        maxTemperatureAlert = preferences.getInt(PREFERENCES_KEY_MAX_TEMPERATURE_ALERT, DEFAULT_MAX_TEMPERATURE_ALERT);
        minTemperatureAlert = preferences.getInt(PREFERENCES_KEY_MIN_TEMPERATURE_ALERT, DEFAULT_MIN_TEMPERATURE_ALERT);
        status = preferences.getBoolean(PREFERENCES_KEY_STATUS, DEFAULT_STATUS);
        layout = preferences.getString(PREFERENCES_KEY_LAYOUT, "");
        timeFactor = preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE);
        winterStart = preferences.getInt(PREFERENCES_KEY_WINTER_START, DEFAULT_WINTER_START);
        winterEnd = preferences.getInt(PREFERENCES_KEY_WINTER_END, DEFAULT_WINTER_END);
        summerStart = preferences.getInt(PREFERENCES_KEY_SUMMER_START, DEFAULT_SUMMER_START);
        summerEnd = preferences.getInt(PREFERENCES_KEY_SUMMER_END, DEFAULT_SUMMER_END);

        LocalDateTime timeNow = LocalDateTime.now();
        year = preferences.getInt(PREFERENCES_KEY_DATETIME_YEAR, timeNow.getYear());
        month = preferences.getInt(PREFERENCES_KEY_DATETIME_MONTH, timeNow.getMonthValue());
        day = preferences.getInt(PREFERENCES_KEY_DATETIME_DAY, timeNow.getDayOfMonth());
        hour = preferences.getInt(PREFERENCES_KEY_DATETIME_HOUR, timeNow.getHour());
        minute = preferences.getInt(PREFERENCES_KEY_DATETIME_MINUTE, timeNow.getMinute());

        LocalTime timeMinLights = DEFAULT_MIN_LIGHTS_TIME;
        minLightsHour = preferences.getInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_HOUR, timeMinLights.getHour());
        minLightsMinute = preferences.getInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_MINUTE, timeMinLights.getMinute());

        LocalTime timeMaxLights = DEFAULT_MAX_LIGHTS_TIME;
        minLightsHour = preferences.getInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_HOUR, timeMaxLights.getHour());
        minLightsMinute = preferences.getInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_MINUTE, timeMaxLights.getMinute());
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
        editor.remove(PREFERENCES_KEY_MAX_TEMPERATURE_ALERT);
        editor.remove(PREFERENCES_KEY_MIN_TEMPERATURE_ALERT);
        editor.remove(PREFERENCES_KEY_STATUS);
        editor.remove(PREFERENCES_KEY_LAYOUT);
        editor.remove(PREFERENCES_KEY_TIME_SCALE);
        editor.remove(PREFERENCES_KEY_DATETIME_YEAR);
        editor.remove(PREFERENCES_KEY_DATETIME_MONTH);
        editor.remove(PREFERENCES_KEY_DATETIME_DAY);
        editor.remove(PREFERENCES_KEY_DATETIME_HOUR);
        editor.remove(PREFERENCES_KEY_DATETIME_MINUTE);
        editor.remove(PREFERENCES_KEY_MIN_LIGHTS_TIME_HOUR);
        editor.remove(PREFERENCES_KEY_MIN_LIGHTS_TIME_MINUTE);
        editor.remove(PREFERENCES_KEY_MAX_LIGHTS_TIME_HOUR);
        editor.remove(PREFERENCES_KEY_MAX_LIGHTS_TIME_MINUTE);
        editor.apply();
    }
}
