package com.concordia.smarthomesimulator.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.dataModels.*;

import java.util.ArrayList;
import java.util.List;

import static com.concordia.smarthomesimulator.Constants.PREFERENCES_KEY_USERNAME;

public final class UserbaseHelper {

    private final static String USERS_FILE_NAME = "users.json";

    /**
     * Loads userbase.
     *
     * @param context the context of the caller
     * @return the userbase which is either read from a json file stored in external memory or created
     */
    public static Userbase loadUserbase(Context context){
        Userbase userbase = null;
        //checking if a user file is present, creating one if it isn't
        userbase = (Userbase) FileHelper.loadObjectFromFile(context, USERS_FILE_NAME, Userbase.class);
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
        FileHelper.saveObjectToFile(context, USERS_FILE_NAME, userbase);
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

    public static boolean canLoggedUserInteract(Action action, SharedPreferences sharedPreferences, Userbase userbase, Context context){
        User loggedUser = userbase.getUserFromUsername(sharedPreferences.getString(PREFERENCES_KEY_USERNAME, ""));
        if ((loggedUser.getPermission().getBitValue()&sharedPreferences.getInt(action.getDescription(),0)) == sharedPreferences.getInt(action.getDescription(),0)){
            ActivityLogHelper.add(context, new LogEntry("Permission","User allowed to perform", LogImportance.MINOR));
            return true;
        }
        ActivityLogHelper.add(context, new LogEntry("Permission","User cannot perform", LogImportance.MINOR));
        return false;
    }

    private static Userbase setupDefaultUserbase(Context context){
        List<User> users = new ArrayList<>();
        users.add(new User("parent","parent",Permissions.PARENT));
        users.add(new User("child","child", Permissions.CHILD));
        users.add(new User("guest","guest", Permissions.GUEST));
        users.add(new User("stranger","stranger", Permissions.STRANGER));
        Userbase userbase = new Userbase(users);
        saveUserbase(context, userbase);
        return userbase;
    }

}
