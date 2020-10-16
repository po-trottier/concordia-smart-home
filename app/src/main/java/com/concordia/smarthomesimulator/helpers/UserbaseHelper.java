package com.concordia.smarthomesimulator.helpers;

import android.content.Context;
import com.concordia.smarthomesimulator.dataModels.Permissions;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class UserbaseHelper {

    private final static String USERS_FILE_NAME = "users.json";

    /**
     * Read userbase.
     *
     * @param context the context of the caller
     * @return the userbase which is either read from a json file stored in external memory or created
     */
    public static Userbase readUserbase(Context context){
        Userbase userbase = null;
        //checking if a user file is present, creating one if it isn't
        try {
            userbase = (Userbase) FileHelper.readObjectFromFile(context, USERS_FILE_NAME, Userbase.class);
            if (userbase == null){
                // no record of userbase, creating a default one
                List<User> users = new ArrayList<User>();
                users.add(new User("root","root", Permissions.PARENT));
                users.add(new User("parent","parent",Permissions.PARENT));
                userbase = new Userbase(users);
                writeUserbase(context, userbase);
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return userbase;
    }

    /**
     * Write userbase.
     *
     * @param context  the context
     * @param userbase the userbase
     * @return the boolean depicting if the write operation was successful
     */
    public static boolean writeUserbase(Context context, Userbase userbase){

        try {
            FileHelper.writeObjectToFile(context, USERS_FILE_NAME, userbase);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  false;
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
            if (user.getPassword().equals(password) && user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

}
