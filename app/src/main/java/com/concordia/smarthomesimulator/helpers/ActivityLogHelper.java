package com.concordia.smarthomesimulator.helpers;

import android.content.Context;
import android.widget.Toast;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.LogEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

// CREDITS : The following methods are based on an Android Studio tutorial video by Coding In Flow
// URL https://www.youtube.com/watch?v=EcfUkjlL9RI&t=505s

public final class ActivityLogHelper {

    private final static String FILE_NAME = "logs.json";

    /**
     * First call will create the activityLog file inside internal storage.
     * Subsequent calls will strictly append to the existing file.
     *
     * @param context       Context of the application
     * @param entry         Log entry
     */
    public static void add(Context context, LogEntry entry) {
        // Read the existing file
        ArrayList<LogEntry> logs = read(context);
        if (logs == null) {
            logs = new ArrayList<>();
        }
        // Add an entry to the file
        logs.add(entry);
        ActivityLogs activityLogs = new ActivityLogs(logs);
        // Save to file
        FileHelper.saveObjectToFile(context, FILE_NAME, activityLogs);
    }

    /**
     * Reads log in it's entirety and stores it in a String
     * which is returned and the end of execution.
     *
     * @param context Context of the application
     * @return String Contents of the activityLog file
     */
    public static ArrayList<LogEntry> read(Context context) {
        ActivityLogs logs = (ActivityLogs) FileHelper.loadObjectFromFile(context, FILE_NAME, ActivityLogs.class);
        return logs == null ? new ArrayList<LogEntry>() : logs.getAll();
    }

    /**
     * This will delete the log.
     * Right click "files" in Device File Explorer and select Synchronize
     * in order to refresh folder.
     *
     * @param context Context of the application
     */
    public static void clear(Context context) {
        File path = context.getExternalFilesDir(null);
        File file = new File(path, FILE_NAME);
        if (file.delete())
            Toast.makeText(context, R.string.toast_deleted, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, R.string.toast_could_not_delete, Toast.LENGTH_LONG).show();
    }

    /**
     * The type ActivityLogs is used internally to help writing JSON data to the logs file. It has no other purpose.
     */
    private static class ActivityLogs {

        private final LogEntry[] logs;

        /**
         * Instantiates a new Activity logs.
         *
         * @param logs the logs
         */
        public ActivityLogs(ArrayList<LogEntry> logs) {
            this.logs = logs.toArray(new LogEntry[0]);
        }

        /**
         * Gets all logs.
         *
         * @return all logs
         */
        public ArrayList<LogEntry> getAll() {
            return new ArrayList<>(Arrays.asList(logs));
        }
    }
}