package com.concordia.smarthomesimulator.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.HeatingZone;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.enums.LogImportance;
import com.concordia.smarthomesimulator.enums.VentilationStatus;
import com.concordia.smarthomesimulator.interfaces.OnIndoorTemperatureChangeListener;
import com.concordia.smarthomesimulator.views.customMapView.CustomMapView;

import java.util.Timer;
import java.util.TimerTask;

import static com.concordia.smarthomesimulator.Constants.*;

public class TemperatureHelper {
    private static Timer temperatureTimer;
    private static Timer saveTimer;
    private static final int SECOND_TO_MS = 1000;

    /**
     * Adjusts how the HVAC behaves for each room, updating ventilation status and actual temps.
     * This should be called whenever the time scale or outside temperature is modified
     *
     * @param context the context
     */
    public static void adjustTemperature(Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        double outsideTemperature = preferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE);
        int maxTemperatureAlert = preferences.getInt(PREFERENCES_KEY_MAX_TEMPERATURE_ALERT, DEFAULT_MAX_TEMPERATURE_ALERT);
        int minTemperatureAlert = preferences.getInt(PREFERENCES_KEY_MIN_TEMPERATURE_ALERT, DEFAULT_MIN_TEMPERATURE_ALERT);
        String connectorString = context.getString(R.string.in_the_segment_alert_text);
        // Create temperatureTimer so that the temperature of each room changes based on their actual temperature, and the desired temperature
        long period = (long) (SECOND_TO_MS / preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE));
        if (temperatureTimer != null) {
            temperatureTimer.cancel();
            temperatureTimer.purge();
        }
        temperatureTimer = new Timer();
        temperatureTimer.schedule(new TimerTask() {
            String temperatureAlertTitle;
            String temperatureAlertText;
            String roomName;
            String zoneName;
            @Override
            public void run() {
                // Make sure the scale is still valid
                long newPeriod = (long) (SECOND_TO_MS / preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE));
                if (newPeriod != period) {
                    adjustTemperature(context);
                }
                HouseLayout layout = LayoutsHelper.getSelectedLayout(context);
                if (layout == null) return;
                for (Room room: layout.getRooms()){
                    if (room.getActualTemperature() > MAXIMUM_TEMPERATURE){
                        room.setActualTemperature(outsideTemperature);
                    }

                    if (room.getDesiredTemperature() > MAXIMUM_TEMPERATURE){
                        room.setDesiredTemperature(outsideTemperature);
                    }

                    double actualTemperature = room.getActualTemperature();
                    double desiredTemperature = room.getDesiredTemperature();
                    VentilationStatus currentVentStatus = room.getVentilationStatus();
                    VentilationStatus newVentStatus = actualTemperature > desiredTemperature ? VentilationStatus.COOLING : VentilationStatus.HEATING;

                    // Because of unclear requirements, OFF and PAUSED really just mean the same thing since the
                    // HVAC system turns itself on.
                    switch (currentVentStatus) {
                        case OFF:
                        case PAUSED:
                            if (Math.abs(actualTemperature - desiredTemperature) > MAX_TEMPERATURE_DIFFERENCE_WHEN_PAUSED) {
                                room.setVentilationStatus(newVentStatus);
                                LogsHelper.add(context, new LogEntry("Temperature Change",
                                        room.getName() + newVentStatus.getDescription(), LogImportance.MINOR));
                            } else {
                                room.setActualTemperature(actualTemperature > outsideTemperature ?
                                        actualTemperature - OUTSIDE_TEMPERATURE_CHANGE : actualTemperature + OUTSIDE_TEMPERATURE_CHANGE);
                            }
                        case COOLING:
                        case HEATING:
                            if (Math.abs(actualTemperature - desiredTemperature) < HVAC_TEMPERATURE_CHANGE){
                                room.setVentilationStatus(VentilationStatus.PAUSED);
                            } else {
                                // In case the desired temp is changed while the ventilation is already running
                                if (currentVentStatus != newVentStatus){
                                    room.setVentilationStatus(newVentStatus);
                                    LogsHelper.add(context, new LogEntry("Temperature Change",
                                            room.getName() + newVentStatus.getDescription(), LogImportance.MINOR));
                                }
                                room.setActualTemperature(actualTemperature > desiredTemperature ?
                                        actualTemperature - HVAC_TEMPERATURE_CHANGE : actualTemperature + HVAC_TEMPERATURE_CHANGE);
                            }
                    }
                }
                for (HeatingZone heatingZone: LayoutsHelper.getSelectedLayout(context).getHeatingZones()) {
                     zoneName = heatingZone.getName();

                    for (Room room : heatingZone.getRooms()) {
                        if (layout.getRoom(room.getName()).getActualTemperature() > maxTemperatureAlert) {
                            temperatureAlertTitle = context.getString(R.string.max_temperature_alert_title);
                            temperatureAlertText = context.getString(R.string.max_temperature_alert_text);
                            roomName = room.getName();

                            String alertTitle = formatLogString(temperatureAlertTitle,connectorString,roomName);
                            LogsHelper.add(context, new LogEntry("Temperature Alert", alertTitle, LogImportance.CRITICAL));

                            if (!heatingZone.isExtremeTempDetected()) {
                                NotificationsHelper.sendTemperatureAlertNotification(context, temperatureAlertTitle, temperatureAlertText, zoneName);
                                heatingZone.setExtremeTempDetected(true);
                            }
                        }
                        else if(layout.getRoom(room.getName()).getActualTemperature() < minTemperatureAlert){
                            temperatureAlertTitle = context.getString(R.string.min_temperature_alert_title);
                            temperatureAlertText = context.getString(R.string.min_temperature_alert_text);
                            roomName = room.getName();

                            String alertTitle = formatLogString(temperatureAlertTitle,connectorString,roomName);
                            if(!heatingZone.isExtremeTempDetected()){
                                LogsHelper.add(context, new LogEntry("Temperature Alert", alertTitle, LogImportance.CRITICAL));
                                NotificationsHelper.sendTemperatureAlertNotification(context,temperatureAlertTitle,temperatureAlertText, zoneName);
                                heatingZone.setExtremeTempDetected(true);
                            }
                        }
                        else{
                            heatingZone.setExtremeTempDetected(false);
                        }
                    }
                }
                LayoutsHelper.updateSelectedLayout(context, layout);
                CustomMapView view = ((Activity) context).findViewById(R.id.custom_map_view);
                if (view != null) {
                    view.updateView();
                }
            }
        }, 0, period);

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
    private static String formatLogString(String alertString, String connectorString, String roomName){
        return alertString + " " +  connectorString + " " + roomName;
    }
}
