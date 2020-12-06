package com.concordia.smarthomesimulator.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.dataModels.Window;
import com.concordia.smarthomesimulator.enums.LogImportance;
import com.concordia.smarthomesimulator.enums.VentilationStatus;
import com.concordia.smarthomesimulator.views.customMapView.CustomMapView;

import java.util.*;

import static com.concordia.smarthomesimulator.Constants.*;

public class TemperatureHelper {
    private static final int SECOND_TO_MS = 1000;

    private static Timer temperatureTimer;
    private static Timer saveTimer;

    private static boolean notifiedAboutLockedWindows;

    private static HashMap<String, Double> extremeRooms;
    private static ArrayList<String> roomNamesWithLockedWindows;

    /**
     * Adjusts how the HVAC behaves for each room, updating ventilation status and actual temps.
     * This should be called whenever the time scale or outside temperature is modified
     *
     * @param context the context
     */
    public static void adjustTemperature(Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        // Initialize the dictionary
        extremeRooms = new HashMap<>();
        roomNamesWithLockedWindows = new ArrayList<>();
        notifiedAboutLockedWindows = false;
        // Create a timer so that the temperature of each room changes based on their actual temperature, and the desired temperature
        setupTemperatureTimer(context, preferences);
        // Create a timer to save the layout at a rate independent of the in-app time
        setupSaveTimer(context);
    }

    private static void setupTemperatureTimer(Context context, SharedPreferences preferences) {
        long period = (long) (SECOND_TO_MS / preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE));

        if (temperatureTimer != null) {
            temperatureTimer.cancel();
            temperatureTimer.purge();
        }

        temperatureTimer = new Timer();
        temperatureTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Make sure the scale is still valid
                long newPeriod = (long) (SECOND_TO_MS / preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE));
                if (newPeriod != period) {
                    adjustTemperature(context);
                }
                // Get the current layout
                HouseLayout layout = LayoutsHelper.getSelectedLayout(context);
                // Update room temperature and make sure none are extreme
                if (layout == null) {
                    return;
                }
                updateRoomTemperatures(context, preferences, layout);
                notifyWindowLocked(context);
                notifyExtremeTemperatures(context, preferences, layout);
                // Update the layout with the modifications
                LayoutsHelper.updateSelectedLayout(context, layout);
                // Update the Map UI if it's visible
                CustomMapView view = ((Activity) context).findViewById(R.id.custom_map_view);
                if (view != null) {
                    view.updateView();
                }
            }
        }, 0, period);
    }

    private static void setupSaveTimer(Context context) {
        if (saveTimer != null) {
            saveTimer.cancel();
            saveTimer.purge();
        }

        saveTimer = new Timer();
        saveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
            HouseLayout layout = LayoutsHelper.getSelectedLayout(context);
            if (layout == null) return;
            LayoutsHelper.saveHouseLayout(context, layout);
            LayoutsHelper.updateSelectedLayout(context, layout);
            }
        }, 0, TEMPERATURE_SAVE_INTERVAL);
    }

    private static void updateRoomTemperatures(Context context, SharedPreferences preferences, HouseLayout layout) {
        if (layout == null) {
            return;
        }
        boolean isAway = preferences.getBoolean(PREFERENCES_KEY_AWAY_MODE, DEFAULT_STATUS);
        double outsideTemperature = preferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE);
        // Update the temperature for every room in the layout
        for (Room room: layout.getRooms()){
            // Make sure the temperatures are not too extreme
            if (room.getActualTemperature() > MAXIMUM_TEMPERATURE){
                room.setActualTemperature(outsideTemperature);
            }
            if (room.getDesiredTemperature() > MAXIMUM_TEMPERATURE){
                room.setDesiredTemperature(outsideTemperature);
            }
            // Get current room information
            double actualTemperature = room.getActualTemperature();
            double desiredTemperature = room.getDesiredTemperature();
            VentilationStatus currentVentStatus = room.getVentilationStatus();
            VentilationStatus newVentStatus = actualTemperature > desiredTemperature ? VentilationStatus.COOLING : VentilationStatus.HEATING;
            // Because of unclear requirements, OFF and PAUSED really just mean the same thing since the system turns itself on.
            switch (currentVentStatus) {
                case OFF:
                case PAUSED:
                    if (Math.abs(actualTemperature - desiredTemperature) > MAX_TEMPERATURE_DIFFERENCE_WHEN_PAUSED) {
                        room.setVentilationStatus(newVentStatus);
                        String message = room.getName() + newVentStatus.getDescription();
                        LogsHelper.add(context, new LogEntry("Temperature Change", message, LogImportance.MINOR));
                    } else {
                        double less = actualTemperature - OUTSIDE_TEMPERATURE_CHANGE;
                        double more = actualTemperature + OUTSIDE_TEMPERATURE_CHANGE;
                        room.setActualTemperature(actualTemperature > outsideTemperature ? less : more);
                    }
                case COOLING:
                case HEATING:
                    if (Math.abs(actualTemperature - desiredTemperature) < HVAC_TEMPERATURE_CHANGE){
                        room.setVentilationStatus(VentilationStatus.PAUSED);
                    } else {
                        // In case the desired temp is changed while the ventilation is already running
                        if (currentVentStatus != newVentStatus){
                            room.setVentilationStatus(newVentStatus);
                            String message = room.getName() + newVentStatus.getDescription();
                            LogsHelper.add(context, new LogEntry("Temperature Change", message, LogImportance.MINOR));
                        }
                        if (newVentStatus == VentilationStatus.COOLING && outsideTemperature < desiredTemperature
                                && !isAway) {
                            tryOpeningWindows(room, preferences);
                        }
                        double less = actualTemperature - HVAC_TEMPERATURE_CHANGE;
                        double more = actualTemperature + HVAC_TEMPERATURE_CHANGE;
                        room.setActualTemperature(actualTemperature > desiredTemperature ? less : more);
                    }
            }
        }
    }

    private static void notifyExtremeTemperatures(Context context, SharedPreferences preferences, HouseLayout layout) {
        // Initialize default values
        String connectorString = context.getString(R.string.in_the_segment_alert_text);
        int maxTemperatureAlert = preferences.getInt(PREFERENCES_KEY_MAX_TEMPERATURE_ALERT, DEFAULT_MAX_TEMPERATURE_ALERT);
        int minTemperatureAlert = preferences.getInt(PREFERENCES_KEY_MIN_TEMPERATURE_ALERT, DEFAULT_MIN_TEMPERATURE_ALERT);
        // Go through every room
        for (Room room : layout.getRooms()) {
            double actualTemp = room.getActualTemperature();
            // Make sure the temperature is extreme
            if (actualTemp > minTemperatureAlert && actualTemp < maxTemperatureAlert) {
                // If not make sure the room is reset in the map
                extremeRooms.put(room.getName(), Double.MIN_VALUE);
                continue;
            }
            // If the room's desired temp has not changed since last notif, don't send a notification
            if (extremeRooms.getOrDefault(room.getName(), Double.MIN_VALUE) == room.getDesiredTemperature()) {
                continue;
            }
            // Set the room as extreme
            extremeRooms.put(room.getName(), room.getDesiredTemperature());
            // Build the notification
            int titleResource = actualTemp > maxTemperatureAlert ? R.string.max_temperature_alert_title : R.string.min_temperature_alert_title;
            int textResource = actualTemp > maxTemperatureAlert ? R.string.max_temperature_alert_text : R.string.min_temperature_alert_text;
            String alertTitle = context.getString(titleResource);
            String alertText = context.getString(textResource);
            // Add a log entry
            String title = alertTitle + " " +  connectorString + " " + room.getName();
            LogsHelper.add(context, new LogEntry("Temperature Alert", title, LogImportance.CRITICAL));
            // Send a notification
            NotificationsHelper.sendTemperatureAlertNotification(context, alertTitle, alertText, room.getName());
        }
    }

    private static void notifyWindowLocked(Context context){
        if (roomNamesWithLockedWindows.size() != 0){
            if (!notifiedAboutLockedWindows){
                notifiedAboutLockedWindows = true;
                String alertTitle = context.getString(R.string.window_locked_alert_title);
                String alertText = context.getString(R.string.window_locked_alert_text_start);
                for (String roomName : roomNamesWithLockedWindows){
                    alertText = alertText.concat(roomName + ", ");
                }
                alertText = alertText + context.getString(R.string.window_locked_alert_text_end);
                NotificationsHelper.sendWindowLockedAlertNotification(context, alertTitle, alertText);
            } //else the user was notified already, don't notify again
        } else {
            notifiedAboutLockedWindows = false;
        }
    }

    private static void tryOpeningWindows(Room room, SharedPreferences preferences){
        int summerStart = preferences.getInt(PREFERENCES_KEY_SUMMER_START,DEFAULT_MONTH);
        int summerEnd = preferences.getInt(PREFERENCES_KEY_SUMMER_END,DEFAULT_MONTH);
        int presentMonth = preferences.getInt(PREFERENCES_KEY_DATETIME_MONTH, DEFAULT_MONTH);
        boolean anyWindowLocked = false;
        if (!isInSummer(summerStart, summerEnd, presentMonth))  return;
        for (Window window : room.getWindows()){
            if (window.getIsLocked()){
                anyWindowLocked = true;
            } else {
                window.setIsOpened(true);
            }
        }
        if (!anyWindowLocked){
            roomNamesWithLockedWindows.remove(room.getName());
        } else {
            roomNamesWithLockedWindows.add(room.getName());
        }
    }

    public static boolean isInSummer(int start, int end, int now){
        // subtracting 1 to everything since we store months nums as 1-12
        for (int i = start-1; i != (end-1 + 1) % 12; i = (i + 1) % 12){
            if (now-1 == i) return true;
        }
        return false;
    }

}
