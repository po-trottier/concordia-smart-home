package com.concordia.smarthomesimulator.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class LayoutHelper {

    private final static String fileName = "layout.json";

    public static boolean saveHouseLayout(Context context, int requestCode, HouseLayout layout) {
        // Convert the House Layout to a JSON Object
        Gson gson = new Gson();
        String jsonLayout = gson.toJson(layout);
        // If we don't have write permissions request them
        if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                (Activity) context,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                requestCode
            );
            return false;
        // Otherwise, write the House Layout to a JSON file
        } else {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, fileName);
            try (FileOutputStream stream = new FileOutputStream(file)) {
                stream.write(jsonLayout.getBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static HouseLayout loadHouseLayout(Context context, int requestCode) {
        // If we don't have read permissions request them
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    requestCode
            );
        // Otherwise, read the House Layout from the JSON file
        } else {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, fileName);
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
        }
        return null;
    }
}
