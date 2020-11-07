package com.concordia.smarthomesimulator.fragments.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import static com.concordia.smarthomesimulator.Constants.*;

public class DashboardModel extends ViewModel {

    public DashboardModel() {
    }

    /**
     * Gets permissions.
     *
     * @param context     the context
     * @param preferences the preferences
     * @return the permissions
     */
    public String getPermissions(Context context, SharedPreferences preferences) {
        switch (preferences.getInt(PREFERENCES_KEY_PERMISSIONS, 1)) {
            case 15:
                return context.getResources().getStringArray(R.array.permissions_spinner)[0];
            case 7:
                return context.getResources().getStringArray(R.array.permissions_spinner)[1];
            case 3:
                return context.getResources().getStringArray(R.array.permissions_spinner)[2];
            default:
                return context.getResources().getStringArray(R.array.permissions_spinner)[3];
        }
    }

    /**
     * Gets username.
     *
     * @param preferences the preferences
     * @return the username
     */
    public String getUsername(SharedPreferences preferences) {
        return preferences.getString(PREFERENCES_KEY_USERNAME, "");
    }

    /**
     * Get date string.
     *
     * @return the string
     */
    public String getDate(){
        return new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
    }

    /**
     * Get time zone string.
     *
     * @param preferences the preferences
     * @return the string
     */
    public LocalDateTime getDateTime(SharedPreferences preferences){
        LocalDateTime timeNow = LocalDateTime.now();
        int year = preferences.getInt(PREFERENCES_KEY_DATETIME_YEAR, timeNow.getYear());
        int month = preferences.getInt(PREFERENCES_KEY_DATETIME_MONTH, timeNow.getMonthValue());
        int day = preferences.getInt(PREFERENCES_KEY_DATETIME_DAY, timeNow.getDayOfMonth());
        int hour = preferences.getInt(PREFERENCES_KEY_DATETIME_HOUR, timeNow.getHour());
        int minute = preferences.getInt(PREFERENCES_KEY_DATETIME_MINUTE, timeNow.getMinute());
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    /**
     * Gets temperature.
     *
     * @param context     the context
     * @param preferences the preferences
     * @return the temperature
     */
    public String getTemperature(Context context, SharedPreferences preferences) {
        String tempString = Integer.toString(preferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE));
        return tempString + context.getString(R.string.generic_degrees_celsius);
    }

    /**
     * Gets status text.
     *
     * @param context     the context
     * @param preferences the preferences
     * @return the status text
     */
    public String getStatusText(Context context, SharedPreferences preferences) {
        boolean statusValue = preferences.getBoolean(PREFERENCES_KEY_STATUS, false);
        return statusValue ? context.getString(R.string.simulation_status_started) : context.getString(R.string.simulation_status_stopped);
    }

    /**
     * Gets status color.
     *
     * @param context     the context
     * @param preferences the preferences
     * @return the status color
     */
    public int getStatusColor(Context context, SharedPreferences preferences) {
        boolean statusValue = preferences.getBoolean(PREFERENCES_KEY_STATUS, false);
        return statusValue ? context.getColor(R.color.primary) : context.getColor(R.color.charcoal);
    }
}





