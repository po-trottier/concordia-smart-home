package com.concordia.smarthomesimulator.fragments.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.concordia.smarthomesimulator.Constants.*;
import static com.concordia.smarthomesimulator.Constants.DEFAULT_TIME_ZONE;

public class DashboardModel extends ViewModel {

    public DashboardModel() {
    }
    
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

    public String getUsername(SharedPreferences preferences) {
        return preferences.getString(PREFERENCES_KEY_USERNAME, "");
    }

    public String getDate(){
        return new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
    }

    public String getTimeZone(SharedPreferences preferences){
        return preferences.getString(PREFERENCES_KEY_TIME_ZONE, DEFAULT_TIME_ZONE);
    }

    public String getTemperature(Context context, SharedPreferences preferences) {
        String tempString = Integer.toString(preferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE));
        return tempString + context.getString(R.string.degrees_celsius);
    }

    public String getStatusText(Context context, SharedPreferences preferences) {
        boolean statusValue = preferences.getBoolean(PREFERENCES_KEY_STATUS, false);
        return statusValue ? context.getString(R.string.simulation_status_started) : context.getString(R.string.simulation_status_stopped);
    }

    public int getStatusColor(Context context, SharedPreferences preferences) {
        boolean statusValue = preferences.getBoolean(PREFERENCES_KEY_STATUS, false);
        return statusValue ? context.getColor(R.color.primary) : context.getColor(R.color.charcoal);
    }
}





