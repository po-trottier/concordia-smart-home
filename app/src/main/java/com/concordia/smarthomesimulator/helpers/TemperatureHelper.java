package com.concordia.smarthomesimulator.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.enums.VentilationStatus;
import com.concordia.smarthomesimulator.singletons.LayoutSingleton;

import java.util.Timer;
import java.util.TimerTask;

import static com.concordia.smarthomesimulator.Constants.*;

public class TemperatureHelper {
    private static Timer temperatureTimer;
    private static Timer saveTimer;
    private static final int SECOND_TO_MS = 1000;

    // This should be called whenever the time scale or outside temperature is modified
    public static void adjustTemperature(Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        double outsideTemperature = preferences.getFloat(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE);

        // Create temperatureTimer so that the temperature of each room changes based on their actual temperature, and the desired temperature
        long period = (long) (SECOND_TO_MS / preferences.getFloat(PREFERENCES_KEY_TIME_SCALE,DEFAULT_TIME_SCALE));
        if (temperatureTimer != null) {
            temperatureTimer.cancel();
            temperatureTimer.purge();
        }
        temperatureTimer = new Timer();
        temperatureTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Room room: LayoutSingleton.getInstance().getLayout().getRooms()){
                    if (room.getActualTemperature() > MAX_ALLOWED_ROOM_TEMPERATURE){
                        room.setActualTemperature(outsideTemperature);
                    }

                    if (room.getDesiredTemperature() > MAX_ALLOWED_ROOM_TEMPERATURE){
                        room.setDesiredTemperature(outsideTemperature);
                    }

                    double actualTemperature = room.getActualTemperature();
                    double desiredTemperature = room.getDesiredTemperature();
                    VentilationStatus ventilationStatus = room.getVentilationStatus();
                    if (Math.abs(actualTemperature - desiredTemperature) > MAX_TEMPERATURE_DIFFERENCE_WHEN_PAUSED && ventilationStatus == VentilationStatus.PAUSED){
                        if (actualTemperature > desiredTemperature){
                            room.setVentilationStatus(VentilationStatus.COOLING);
                        } else {
                            room.setVentilationStatus(VentilationStatus.HEATING);
                        }
                    } else if (Math.abs(actualTemperature - desiredTemperature) > MAX_TEMPERATURE_DIFFERENCE_WHEN_OFF && ventilationStatus == VentilationStatus.OFF){
                        if (actualTemperature > desiredTemperature){
                            room.setVentilationStatus(VentilationStatus.COOLING);
                        } else {
                            room.setVentilationStatus(VentilationStatus.HEATING);
                        }
                    } else if (Math.abs(actualTemperature - desiredTemperature) < HVAC_TEMPERATURE_CHANGE &&
                            (ventilationStatus == VentilationStatus.HEATING || ventilationStatus == VentilationStatus.COOLING)){
                        room.setVentilationStatus(VentilationStatus.PAUSED);
                    } else if (ventilationStatus == VentilationStatus.HEATING || ventilationStatus == VentilationStatus.COOLING){
                        // Actual temperature is getting closer to desired temperature
                        if (actualTemperature > desiredTemperature){
                            room.setActualTemperature(actualTemperature - HVAC_TEMPERATURE_CHANGE);
                        } else {
                            room.setActualTemperature(actualTemperature + HVAC_TEMPERATURE_CHANGE);
                        }
                    } else if (ventilationStatus == VentilationStatus.OFF || ventilationStatus == VentilationStatus.PAUSED){
                        // Actual temperature is getting closer to outside temperature
                        if (actualTemperature > outsideTemperature){
                            room.setActualTemperature(actualTemperature - OUTSIDE_TEMPERATURE_CHANGE);
                        } else if (actualTemperature < outsideTemperature) {
                            room.setActualTemperature(actualTemperature + OUTSIDE_TEMPERATURE_CHANGE);
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
                LayoutsHelper.saveHouseLayout(context, LayoutSingleton.getInstance().getLayout());
            }
        }, 0, TEMPERATURE_SAVE_INTERVAL);

    }
}
