package com.concordia.smarthomesimulator.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import java.io.*;

import static com.concordia.smarthomesimulator.Constants.READ_PERMISSION_REQUEST_CODE;
import static com.concordia.smarthomesimulator.Constants.WRITE_PERMISSION_REQUEST_CODE;

public final class FilesHelper {

    /**
     * List files in a given directory.
     *
     * @param context   the context
     * @param directory the directory to look into
     * @return the array of files found
     */
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
     * @return the object
     */
    public static Object loadObjectFromFile(Context context, String directory, String fileName) throws IOException, ClassNotFoundException {
        if (verifyPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION_REQUEST_CODE)) {
            File path = context.getExternalFilesDir(null);
            // Find the right path
            if (directory != null) {
                path = new File(path, directory);
                if (!path.isDirectory())
                    return null;
            }
            // Create the File
            File file = new File(path, fileName);
            if (file.exists()){
                // Read the file
                try(FileInputStream stream = new FileInputStream(file)){
                    ObjectInputStream in = new ObjectInputStream(stream);
                    Object object = in.readObject();
                    in.close();
                    return object;
                } catch (IOException | ClassNotFoundException e){
                    throw e;
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
     * @return the object
     */
    public static Object loadObjectFromFile(Context context, String fileName) throws IOException, ClassNotFoundException {
        return loadObjectFromFile(context, null, fileName);
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
    public static boolean saveObjectToFile(Context context, String directory, String fileName, Object object) throws IOException {
        // Make sure we have the right permissions
        if (verifyPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_REQUEST_CODE)) {
            File path = context.getExternalFilesDir(null);
            // Find the right directory
            if (directory != null) {
                path = new File(path, directory);
                if (!path.isDirectory())
                    path.mkdir();
            }
            // Create the file
            File file = new File(path, fileName);
            // Output to the file
            try(FileOutputStream stream = new FileOutputStream(file)){
                ObjectOutputStream out = new ObjectOutputStream(stream);
                out.writeObject(object);
                out.close();
                return true;
            } catch (IOException e){
                throw e;
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
    public static boolean saveObjectToFile(Context context, String fileName, Object object) throws IOException {
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
