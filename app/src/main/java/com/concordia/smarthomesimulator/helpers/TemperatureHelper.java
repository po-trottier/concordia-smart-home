package com.concordia.smarthomesimulator.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.enums.HAVCStatus;
import com.concordia.smarthomesimulator.singletons.LayoutSingleton;

import java.util.Timer;
import java.util.TimerTask;

import static com.concordia.smarthomesimulator.Constants.PREFERENCES_KEY_TEMPERATURE;
import static com.concordia.smarthomesimulator.Constants.PREFERENCES_KEY_TIME_SCALE;

public class TemperatureHelper {
    private static Timer timer;
    private static final int SECOND = 1000;
    private static final double MaxTemperatureDifferenceWhenPaused = 0.25;
    private static final double MaxTemperatureDifferenceWhenOff = 1;
    private static final double HAVCTemperatureChange = 0.1;
    private static final double OutsideTemperatureChange = 0.05;

    public static void adjustTemperature(Context context){
        LayoutSingleton layoutSingleton = LayoutSingleton.getInstance();
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        double outsideTemperature = preferences.getFloat(PREFERENCES_KEY_TEMPERATURE,0);

        //initialise temperature according to preferences
        for (Room room: layoutSingleton.getLayout().getRooms()){
            if (room.getActualTemperature() > 100){
                room.setActualTemperature(outsideTemperature);
            }

            if (room.getDesiredTemperature() > 100){
                room.setDesiredTemperature(outsideTemperature);
            }
        }
        //create timer so that the temperature of each room changes based on their actual temperature, and the desired temperature
        long period = (long) (SECOND / preferences.getFloat(PREFERENCES_KEY_TIME_SCALE,0));
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Room room: layoutSingleton.getLayout().getRooms()){
                    double actualTemperature = room.getActualTemperature();
                    double desiredTemperature = room.getDesiredTemperature();
                    HAVCStatus havcStatus = room.getHavcStatus();
                    if (Math.abs(actualTemperature - desiredTemperature) > MaxTemperatureDifferenceWhenPaused && havcStatus == HAVCStatus.PAUSED){
                        room.setHavcStatus(HAVCStatus.ON);
                    } else if (Math.abs(actualTemperature - desiredTemperature) > MaxTemperatureDifferenceWhenOff && havcStatus == HAVCStatus.OFF){
                        room.setHavcStatus(HAVCStatus.ON);
                    } else if (Math.abs(actualTemperature - desiredTemperature) < HAVCTemperatureChange && havcStatus == HAVCStatus.ON){
                        room.setHavcStatus(HAVCStatus.PAUSED);
                    } else if (havcStatus == HAVCStatus.ON){
                        // Actual temperature is getting closer to desired temperature
                        if (actualTemperature > desiredTemperature){
                            room.setActualTemperature(actualTemperature - HAVCTemperatureChange);
                        } else {
                            room.setActualTemperature(actualTemperature + HAVCTemperatureChange);
                        }
                    } else if (havcStatus == HAVCStatus.OFF || havcStatus == HAVCStatus.PAUSED){
                        // Actual temperature is getting closer to outside temperature
                        if (actualTemperature > outsideTemperature){
                            room.setActualTemperature(actualTemperature - OutsideTemperatureChange);
                        } else if (actualTemperature < outsideTemperature) {
                            room.setActualTemperature(actualTemperature + OutsideTemperatureChange);
                        }
                    }
                }
            }
        }, 0, period);

    }
}
