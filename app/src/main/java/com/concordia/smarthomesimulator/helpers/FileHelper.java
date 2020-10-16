package com.concordia.smarthomesimulator.helpers;

import android.Manifest;
import android.content.Context;
import com.concordia.smarthomesimulator.dataModels.Userbase;
import com.google.gson.Gson;

import java.io.*;

import static com.concordia.smarthomesimulator.Constants.READ_PERMISSION_REQUEST_CODE;
import static com.concordia.smarthomesimulator.Constants.WRITE_PERMISSION_REQUEST_CODE;

public final class FileHelper {

    public static Object readObjectFromFile(Context context, String fileName, Class className) throws FileNotFoundException, IOException {
        if (PermissionsHelper.verifyPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION_REQUEST_CODE)) {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, fileName);
            if (file.exists()){
                // Read the file
                FileInputStream stream = new FileInputStream(file);
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
            }

        }
        return null;
    }

    public static void writeObjectToFile(Context context, String fileName, Object object) throws IOException{
        // Convert the Object to a JSON Object
        Gson gson = new Gson();
        String jsonObject = gson.toJson(object);

        // Make sure we have the right permissions
        if (PermissionsHelper.verifyPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_REQUEST_CODE)) {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, fileName);
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(jsonObject.getBytes());
        }
    }
}
