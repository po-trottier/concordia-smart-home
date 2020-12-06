package com.concordia.smarthomesimulator.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.enums.LogImportance;

import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.concordia.smarthomesimulator.Constants.*;

public class NotificationsHelper {

    private static final int MINUTES_TO_MILLISECONDS = 60000;

    private static Timer timer;
    private static int id = 0;

    /**
     * Create notification channel.
     *
     * @param context the context
     */
    public static void createNotificationChannels(Context context) {
        NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
        if (notificationManager == null) {
            return;
        }
        // If the channel was already created we're done
        boolean intruderExists = notificationManager.getNotificationChannel(INTRUDER_NOTIFICATION_CHANNEL) != null;
        boolean temperatureExists = notificationManager.getNotificationChannel(TEMPERATURE_NOTIFICATION_CHANNEL) != null;
        // Register the channel with the system; you can't change the importance or other notification behaviors after this
        if (!intruderExists) {
            String intruderCategory = context.getString(R.string.intruder_category);
            NotificationChannel intruderChannel = new NotificationChannel(INTRUDER_NOTIFICATION_CHANNEL, intruderCategory, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(intruderChannel);
        }
        if (!temperatureExists) {
            String temperatureCategory = context.getString(R.string.temperature_category);
            NotificationChannel temperatureChannel = new NotificationChannel(TEMPERATURE_NOTIFICATION_CHANNEL, temperatureCategory, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(temperatureChannel);
        }
    }

    /**
     * Create notification.
     *
     * @param context the context
     */
    public static void sendIntruderNotification(Context context){
        // Get the delay from preferences
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        int delay = preferences.getInt(PREFERENCES_KEY_CALL_DELAY, DEFAULT_CALL_DELAY);
        float scale = preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE);
        // Set timer to send authorities notification
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendAuthoritiesNotification(context);
            }
        }, (long) ((delay * MINUTES_TO_MILLISECONDS) / scale));
        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, INTRUDER_NOTIFICATION_CHANNEL);
        builder.setContentTitle(context.getString(R.string.intruder_title));
        builder.setContentText(String.format(context.getString(R.string.intruder_text), delay));
        builder.setSmallIcon(R.drawable.ic_home);
        builder.setAutoCancel(false);
        // Send notification
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id++, builder.build());
        // Add log
        LogsHelper.add(context, new LogEntry("Intruder", "An intruder was detected", LogImportance.CRITICAL));
    }

    public static void sendAuthoritiesNotification(Context context){
        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, INTRUDER_NOTIFICATION_CHANNEL);
        builder.setContentTitle(context.getString(R.string.authorities_title));
        builder.setContentText(context.getString(R.string.authorities_text));
        builder.setSmallIcon(R.drawable.ic_home);
        builder.setAutoCancel(false);
        // Send notification
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id++, builder.build());
        // Add log
        LogsHelper.add(context, new LogEntry("Intruder", "Authorities were contacted", LogImportance.CRITICAL));
    }

    public static void sendTemperatureAlertNotification(Context context, String alertTitle, String alertText, String roomName){
        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TEMPERATURE_NOTIFICATION_CHANNEL);
        builder.setContentTitle(alertTitle);
        String connectorString = context.getString(R.string.in_zone_segment_alert_text);
        String content = alertText + " " +  connectorString + " " + roomName;
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_home);
        builder.setAutoCancel(false);
        // Send notification
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id++, builder.build());
        // Add log
        LogsHelper.add(context, new LogEntry("Extreme Temperature", content, LogImportance.CRITICAL));
    }
}
