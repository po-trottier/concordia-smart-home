package com.concordia.smarthomesimulator.views.customDateTimeView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.Light;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.enums.DeviceType;
import com.concordia.smarthomesimulator.helpers.LayoutsHelper;
import com.concordia.smarthomesimulator.interfaces.IDevice;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.concordia.smarthomesimulator.Constants.*;

/**
 * The type Custom clock view.
 */
public class CustomDateTimeView extends LinearLayout {

    private static final int MINUTE = 60000;

    private Context context;
    private SharedPreferences preferences;
    private LocalDateTime dateTime = null;
    private Timer timer;

    /**
     * Instantiates a new Custom clock view.
     *
     * @param context the context
     */
    public CustomDateTimeView(Context context) {
        super(context);
        initializeView(context);
    }

    /**
     * Instantiates a new Custom clock view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomDateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    /**
     * Instantiates a new Custom clock view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CustomDateTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context);
    }

    /**
     * Sets date time.
     *
     * @param dateTime the date time
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        if (preferences.getBoolean(PREFERENCES_KEY_STATUS, false)) {
            setClockBehavior();
        }
        updateView();
    }

    private void initializeView(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
    }

    private void setClockBehavior() {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        // Set the clock's update rate (i.e. 10x means a period 10 times shorter)
        // Use millisecond to allow time acceleration of up to 100x
        float scale = preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE);
        long period = (long) (MINUTE / scale);
        // Update the clock time
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dateTime = dateTime.plusMinutes(1);
                setPreferences(preferences);
                updateView();

                LocalTime defaultMinLightsTime = DEFAULT_MIN_LIGHTS_TIME;
                int minLightsHour = preferences.getInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_HOUR, defaultMinLightsTime.getHour());
                int minLightsMinute = preferences.getInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_MINUTE, defaultMinLightsTime.getMinute());

                    if(dateTime.getHour() == minLightsHour && dateTime.getMinute() == minLightsMinute){
                        HouseLayout layout = LayoutsHelper.getSelectedLayout(context);
                        for (Room room : layout.getRooms()) {
                            for (IDevice device : room.getDevices()) {
                                if (device.getDeviceType() == DeviceType.LIGHT && ((Light) device).isAutoOn()) {
                                    device.setIsOpened(true);
                                }
                            }
                        }
                        LayoutsHelper.saveHouseLayout(context, layout);
                        LayoutsHelper.updateSelectedLayout(context, layout);
                    }


                LocalTime maxLightsTime = DEFAULT_MAX_LIGHTS_TIME;
                int maxLightsHour = preferences.getInt(PREFERENCES_KEY_MAX_LIGHTS_TIME_HOUR, maxLightsTime.getHour());
                int maxLightsMinute = preferences.getInt(PREFERENCES_KEY_MAX_LIGHTS_TIME_MINUTE, maxLightsTime.getMinute());

                if(dateTime.getHour() == maxLightsHour && dateTime.getMinute() == maxLightsMinute){
                    HouseLayout layout = LayoutsHelper.getSelectedLayout(context);
                    for (Room room : layout.getRooms()) {
                        for (IDevice device : room.getDevices()) {
                            if (device.getDeviceType() == DeviceType.LIGHT && ((Light) device).isAutoOn()) {
                                device.setIsOpened(false);
                            }
                        }
                    }
                    LayoutsHelper.saveHouseLayout(context, layout);
                    LayoutsHelper.updateSelectedLayout(context, layout);
                }
            }
        }, 0, period);
    }

    private void setPreferences(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREFERENCES_KEY_DATETIME_YEAR, dateTime.getYear());
        editor.putInt(PREFERENCES_KEY_DATETIME_MONTH, dateTime.getMonthValue());
        editor.putInt(PREFERENCES_KEY_DATETIME_DAY, dateTime.getDayOfMonth());
        editor.putInt(PREFERENCES_KEY_DATETIME_HOUR, dateTime.getHour());
        editor.putInt(PREFERENCES_KEY_DATETIME_MINUTE, dateTime.getMinute());
        editor.apply();
    }

    private void updateView() {
        // Running on UI Thread makes the UI update thread-safe
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView time = findViewById(R.id.dashboard_time);
                TextView date = findViewById(R.id.dashboard_date);
                if (time != null) {
                    time.setText(dateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
                }
                if (date != null) {
                    date.setText(dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
                }
            }
        });
    }
}


