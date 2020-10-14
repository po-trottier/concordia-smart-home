package com.concordia.smarthomesimulator.helpers;

import android.Manifest;
import android.content.Context;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.concordia.smarthomesimulator.Constants.READ_PERMISSION_REQUEST_CODE;
import static com.concordia.smarthomesimulator.Constants.WRITE_PERMISSION_REQUEST_CODE;

public final class LayoutHelper {

    private final static String FILE_NAME = "layout.json";

    /**
     * Save house layout.
     *
     * @param context     the context
     * @param layout      the layout
     * @return operation success
     */
    public static boolean saveHouseLayout(Context context, HouseLayout layout) {
        // Convert the House Layout to a JSON Object
        Gson gson = new Gson();
        String jsonLayout = gson.toJson(layout);
        // Make sure we have the right permissions
        if (PermissionsHelper.verifyPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_REQUEST_CODE))  {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, FILE_NAME);
            try (FileOutputStream stream = new FileOutputStream(file)) {
                stream.write(jsonLayout.getBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return  false;
    }

    /**
     * Load house layout.
     *
     * @param context     the context
     * @return the house layout
     */
    public static HouseLayout loadHouseLayout(Context context) {
        // Make sure we have the right permissions
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
                return gson.fromJson(fileContent.toString(), HouseLayout.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }
}
