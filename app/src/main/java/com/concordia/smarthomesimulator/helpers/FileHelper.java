package com.concordia.smarthomesimulator.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

import static com.concordia.smarthomesimulator.Constants.READ_PERMISSION_REQUEST_CODE;
import static com.concordia.smarthomesimulator.Constants.WRITE_PERMISSION_REQUEST_CODE;

public final class FileHelper {

    public static File[] listFilesInDirectory(Context context, String directory) {
        if (verifyPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION_REQUEST_CODE)) {
            File base = context.getExternalFilesDir(null);
            File path = new File(base, directory);

            if (!path.exists() || !path.isDirectory())
                return new File[]{};

            return path.listFiles();
        }
        return new File[]{};
    }

    /**
     * Load object from file object.
     *
     * @param context   the context
     * @param directory the directory in which the file is located
     * @param fileName  the name of the file where the object is stored
     * @param className the class of the object
     * @return the object
     */
    public static Object loadObjectFromFile(Context context, String directory, String fileName, Class className) {
        if (verifyPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION_REQUEST_CODE)) {
            File base = context.getExternalFilesDir(null);
            File path = base;

            if (directory != null) {
                path = new File(base, directory);
                if (!path.isDirectory())
                    return null;
            }

            File file = new File(path, fileName);
            if (file.exists()){
                // Read the file
                try(FileInputStream stream = new FileInputStream(file)){
                    // Build the string from the file buffer
                    StringBuilder fileContent = new StringBuilder();
                    byte[] buffer = new byte[1024];
                    int n;
                    while ((n = stream.read(buffer)) != -1) {
                        fileContent.append(new String(buffer, 0, n));
                    }
                    // Convert the JSON string to a Java Object
                    Gson gson = new Gson();
                    return gson.fromJson(fileContent.toString(), className);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Load object from file object.
     *
     * @param context   the context
     * @param fileName  the name of the file where the object is stored
     * @param className the class of the object
     * @return the object
     */
    public static Object loadObjectFromFile(Context context, String fileName, Class className) {
        return loadObjectFromFile(context, null, fileName, className);
    }

    /**
     * Save object to file.
     *
     * @param context   the context
     * @param directory the directory in which to save the file
     * @param fileName  the file name
     * @param object    the object
     * @return the boolean
     */
    public static boolean saveObjectToFile(Context context, String directory, String fileName, Object object) {
        // Convert the Object to a JSON Object
        Gson gson = new Gson();
        String jsonObject = gson.toJson(object);

        // Make sure we have the right permissions
        if (verifyPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_REQUEST_CODE)) {
            File base = context.getExternalFilesDir(null);
            File path = base;
            if (directory != null) {
                path = new File(base, directory);
                if (!path.isDirectory());
                    path.mkdir();
            }
            File file = new File(path, fileName);
            try(FileOutputStream stream = new FileOutputStream(file)){
                stream.write(jsonObject.getBytes());
                return true;
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Save object to file.
     *
     * @param context  the context
     * @param fileName the file name
     * @param object   the object
     */
    public static boolean saveObjectToFile(Context context, String fileName, Object object) {
        return saveObjectToFile(context, null, fileName, object);
    }

    /**
     * Load raw resource files.
     *
     * @param context      the context
     * @param fileResource the file resource
     * @return the file content
     */
    public static String loadRawResource(Context context, int fileResource) {
        StringBuilder total = new StringBuilder();
        try (InputStream inputStream = context.getResources().openRawResource(fileResource)) {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }

    private static boolean verifyPermission(Context context, String permission, int requestCode) {
        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                (Activity) context,
                new String[] { permission },
                requestCode
            );
            return false;
        }
        return true;
    }
}
