package com.concordia.smarthomesimulator.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.concordia.smarthomesimulator.R;

import static androidx.core.content.ContextCompat.getSystemService;

public class IntruderNotificationHelper {
    public final static String CHANNEL_1_ID = "channel1";

    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, "Intruder Alert", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public static void notifyIntruder(Context context){
            createNotificationChannel(context);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_1_ID);
            builder.setContentTitle("Intruder alert");
            builder.setContentText("Intruder detected : Contacting authorities in" + HouseLayoutHelper.getSelectedLayout(context).getAwayModeEntry().getCallTimer() + " minutes");
            builder.setSmallIcon(R.drawable.ic_delete_forever);
            builder.setAutoCancel(false);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(1,builder.build());
    }
}
