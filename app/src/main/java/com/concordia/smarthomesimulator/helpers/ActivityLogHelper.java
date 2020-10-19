package com.concordia.smarthomesimulator.helpers;

import android.Manifest;
import android.content.Context;
import android.widget.Toast;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.concordia.smarthomesimulator.Constants.READ_PERMISSION_REQUEST_CODE;
import static com.concordia.smarthomesimulator.Constants.WRITE_PERMISSION_REQUEST_CODE;

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
        // Convert to JSON
        Gson gson = new Gson();
        String jsonLogs = gson.toJson(activityLogs);
        // Make sure we have the proper permissions
        if (PermissionsHelper.verifyPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_REQUEST_CODE)) {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, FILE_NAME);
            try (FileOutputStream stream = new FileOutputStream(file)) {
                stream.write(jsonLogs.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads log in it's entirety and stores it in a String
     * which is returned and the end of execution.
     *
     * @param context Context of the application
     * @return String Contents of the activityLog file
     */
    public static ArrayList<LogEntry> read(Context context) {
        if (PermissionsHelper.verifyPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION_REQUEST_CODE)) {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, FILE_NAME);
            // Read the file
            try (FileInputStream stream = new FileInputStream(file)) {
                // Build the string from the file buffer
                StringBuilder fileContent = new StringBuilder();
                byte[] buffer = new byte[1024];
                int n;
                while ((n = stream.read(buffer)) != -1) {
                    fileContent.append(new String(buffer, 0, n));
                }
                // Convert the JSON string to a Java Object
                Gson gson = new Gson();
                ActivityLogs logs = gson.fromJson(fileContent.toString(), ActivityLogs.class);
                return logs.getAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
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