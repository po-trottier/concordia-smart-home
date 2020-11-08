package com.concordia.smarthomesimulator.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.concordia.smarthomesimulator.R;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.concordia.smarthomesimulator.Constants.*;

public class NotificationsHelper {

    private static int id = 0;

    /**
     * Create notification channel.
     *
     * @param context the context
     */
    public static void createNotificationChannel(Context context) {
        // If the channel was already created we're done
        NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
        if (notificationManager == null || notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL) != null) {
            return;
        }
        // Register the channel with the system; you can't change the importance or other notification behaviors after this
        String category = context.getString(R.string.intruder_category);
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, category, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Create notification.
     *
     * @param context the context
     */
    public static void sendIntruderNotification(Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        int delay = preferences.getInt(PREFERENCES_KEY_CALL_DELAY, DEFAULT_CALL_DELAY);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL);
        builder.setContentTitle(context.getString(R.string.intruder_title));
        builder.setContentText(String.format(context.getString(R.string.intruder_text), delay));
        builder.setSmallIcon(R.drawable.ic_home);
        builder.setAutoCancel(false);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id++, builder.build());
    }
}
