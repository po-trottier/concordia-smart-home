package com.concordia.smarthomesimulator.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.enums.Action;
import com.concordia.smarthomesimulator.enums.LogImportance;
import com.concordia.smarthomesimulator.enums.Permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.concordia.smarthomesimulator.Constants.PREFERENCES_KEY_PERMISSIONS;

public final class UserbaseHelper {

    private final static String USERS_FILE_NAME = "users.txt";

    /**
     * Loads userbase.
     *
     * @param context the context of the caller
     * @return the userbase which is either read from a json file stored in external memory or created
     */
    public static Userbase loadUserbase(Context context){
        Userbase userbase = null;
        //checking if a user file is present, creating one if it isn't
        try {
            userbase = (Userbase) FilesHelper.loadObjectFromFile(context, USERS_FILE_NAME);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (userbase == null){
            // no record of userbase, creating a default one
            userbase = setupDefaultUserbase(context);
        }
        return userbase;
    }

    /**
     * Saves userbase.
     *
     * @param context  the context
     * @param userbase the userbase
     */
    public static void saveUserbase(Context context, Userbase userbase){
        try {
            FilesHelper.saveObjectToFile(context, USERS_FILE_NAME, userbase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get user with the given credentials if it exists.
     *
     * @param username the username
     * @param password the password
     * @param userbase the userbase
     * @return the user with the passed username and password in the userbase or null
     */
    public static User getUserWithCredentials(String username, String password, Userbase userbase){
        for(User user : userbase.getUsers()){
            if (user.getPassword().equals(password) && user.getUsername().toLowerCase().equals(username.toLowerCase())){
                return user;
            }
        }
        return null;
    }

    public static boolean verifyPermissions(Action action, Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        int loggedUserPermissions = preferences.getInt(PREFERENCES_KEY_PERMISSIONS,0);
        int minPermissionsForAction = preferences.getInt(action.getDescription(),0);
        // Default log message
        String message = String.format("User was stopped from performing permission-restricted action: %s", action.getDescription());
        // Something went wrong
        if (loggedUserPermissions == 0 || minPermissionsForAction == 0){
            Toast.makeText(context, context.getString(R.string.generic_error_message),Toast.LENGTH_SHORT).show();
            return false;
        }
        // User has required permissions
        if ((loggedUserPermissions & minPermissionsForAction) == minPermissionsForAction){
            message = String.format("User performed permission-restricted action: %s", action.getDescription());
            LogsHelper.add(context, new LogEntry("Permission", message, LogImportance.MINOR));
            return true;
        }
        // USer doesn't have permissions
        Toast.makeText(context, String.format(context.getString(R.string.permission_edit_not_allows), action.getDescription()),Toast.LENGTH_SHORT).show();
        LogsHelper.add(context, new LogEntry("Permission", message, LogImportance.IMPORTANT));
        return false;
    }

    private static Userbase setupDefaultUserbase(Context context){
        List<User> users = new ArrayList<>();
        users.add(new User("parent","parent", Permissions.PARENT));
        users.add(new User("child","child", Permissions.CHILD));
        users.add(new User("guest","guest", Permissions.GUEST));
        users.add(new User("stranger","stranger", Permissions.STRANGER));
        Userbase userbase = new Userbase(users);
        saveUserbase(context, userbase);
        return userbase;
    }

}
