package com.concordia.smarthomesimulator.dataModels;

import android.content.Context;

import com.concordia.smarthomesimulator.helpers.UserbaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Userbase {

    private List<User> users;

    /**
     * Instantiates a new Userbase using the context of the caller.
     *
     * @param context the context of the caller, used to get permissions to read from external storage
     */
    public Userbase(Context context){
        this.users = UserbaseHelper.loadUserbase(context).getUsers();
    }

    /**
     * Instantiates a new Userbase using a passed List of users.
     *
     * @param users the users
     */
    public Userbase(List<User> users){
        this.users = users;
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }

    public List<String> getUsernames(){
        List<String> usernames = new ArrayList<>();
        for (User user: users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }
}


