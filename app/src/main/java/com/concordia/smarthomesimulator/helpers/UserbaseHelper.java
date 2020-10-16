package com.concordia.smarthomesimulator.helpers;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.concordia.smarthomesimulator.dataModels.Permissions;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.concordia.smarthomesimulator.Constants.READ_PERMISSION_REQUEST_CODE;
import static com.concordia.smarthomesimulator.Constants.WRITE_PERMISSION_REQUEST_CODE;

public final class UserbaseHelper {

    private final static String USERS_FILE_NAME = "users.json";

    /**
     * Read userbase.
     *
     * @param context the context of the caller
     * @return the userbase which is either read from a json file stored in external memory or created
     */
    public static Userbase readUserBase(Context context){
        Userbase userBase = new Userbase(new ArrayList<User>());
        //checking if a user file is present, creating one if it isn't
        if (PermissionsHelper.verifyPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION_REQUEST_CODE)) {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, USERS_FILE_NAME);
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
                userBase = gson.fromJson(fileContent.toString(), Userbase.class);
            } catch (FileNotFoundException e){
                // no record of userbase, creating a default one
                List<User> users = new ArrayList<User>();
                users.add(new User("root","root", Permissions.PARENT));
                users.add(new User("parent","parent",Permissions.PARENT));
                userBase = new Userbase(users);
                writeUserbase(context, userBase);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userBase;
    }

    /**
     * Write userbase.
     *
     * @param context  the context
     * @param userbase the userbase
     * @return the boolean depicting if the write operation was successful
     */
    public static boolean writeUserbase(Context context, Userbase userbase){
        // Convert the Userbase to a JSON Object
        Gson gson = new Gson();
        String jsonUserbase = gson.toJson(userbase);
        // Make sure we have the right permissions
        if (PermissionsHelper.verifyPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_REQUEST_CODE))  {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, USERS_FILE_NAME);
            try (FileOutputStream stream = new FileOutputStream(file)) {
                stream.write(jsonUserbase.getBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return  false;
    }

    /**
     * Get user with the given credentials if it exists.
     *
     * @param username the username
     * @param password the password
     * @param userbase the userbase
     * @return the optional which is empty or contains the user
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<User> getUserWithCredentials(String username, String password, Userbase userbase){

        for(User user : userbase.getUsers()){
            if (user.getPassword().equals(password) && user.getUsername().equals(username)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

}
