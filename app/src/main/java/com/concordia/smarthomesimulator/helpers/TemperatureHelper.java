package com.concordia.smarthomesimulator.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.enums.LogImportance;
import com.concordia.smarthomesimulator.enums.VentilationStatus;
import com.concordia.smarthomesimulator.singletons.LayoutSingleton;

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

        // Create temperatureTimer so that the temperature of each room changes based on their actual temperature, and the desired temperature
        long period = (long) (SECOND_TO_MS / preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE));
        if (temperatureTimer != null) {
            temperatureTimer.cancel();
            temperatureTimer.purge();
        }
        temperatureTimer = new Timer();
        temperatureTimer.schedule(new TimerTask() {
            @Override
            public void run() {
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
                    VentilationStatus ventilationStatus = room.getVentilationStatus();

                    // Because of unclear requirements, OFF and PAUSED really just mean the same thing since the
                    // HVAC system turns itself on.
                    switch (ventilationStatus){
                        case OFF:
                        case PAUSED:
                            if (Math.abs(actualTemperature - desiredTemperature) > MAX_TEMPERATURE_DIFFERENCE_WHEN_PAUSED) {
                                room.setVentilationStatus(actualTemperature > desiredTemperature ?
                                        VentilationStatus.COOLING : VentilationStatus.HEATING);
                                //LogsHelper.add(context, new LogEntry("Temperature Change", room.getName() + "is being cooled" + R.string.menu_dashboard, LogImportance.MINOR));
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
                                room.setVentilationStatus(actualTemperature > desiredTemperature ?
                                        VentilationStatus.COOLING : VentilationStatus.HEATING);
                                room.setActualTemperature(actualTemperature > desiredTemperature ?
                                        actualTemperature - HVAC_TEMPERATURE_CHANGE : actualTemperature + HVAC_TEMPERATURE_CHANGE);
                            }
                    }
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
}
