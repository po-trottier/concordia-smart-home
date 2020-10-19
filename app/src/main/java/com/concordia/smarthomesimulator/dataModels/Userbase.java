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

    /**
     * Gets a list of all usernames
     *
     * @return the list
     */
    public List<String> getUsernames(){
        List<String> usernames = new ArrayList<>();
        for (User user: users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    /**
     * Gets user from username.
     *
     * @param username the username
     * @return the user
     */
    public User getUserFromUsername(String username){
        for (User user: users) {
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    /**
     * Deletes a user if present
     *
     * @param usernameToDelete the username to delete
     * @param context          the context
     * @return the boolean showing if the deletion was successful
     */
    public boolean deleteUserFromUsernameIfPossible(String usernameToDelete, Context context){
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getUsername().equals(usernameToDelete)){
                users.remove(i);
                UserbaseHelper.saveUserbase(context, this);
                return true;
            }
        }
        return false;
    }

    /**
     * Will add a user if no similar users exist in the userbase.
     *
     * @param userToAdd the user to add
     * @param context   the context
     * @return the boolean showing if the addition was successful
     */
    public boolean addUserIfPossible(User userToAdd, Context context){
        if (containsSimilarUser(userToAdd)){
            return false;
        }
        users.add(userToAdd);
        UserbaseHelper.saveUserbase(context,this);
        return true;
    }

    /**
     * Checks if the userbase contains a similar user
     *
     * @param userToCompare the user to compare
     * @return the boolean
     */
    public boolean containsSimilarUser(User userToCompare){
        return getNumberOfSimilarUsers(userToCompare) > 0;
    }

    /**
     * Gets number of similar users.
     *
     * @param userToCompare the user to compare
     * @return the number of similar users
     */
    public int getNumberOfSimilarUsers(User userToCompare){
        int count = 0;
        for (User user: getUsers()) {
            if (user.isSimilar(userToCompare)){
                count++;
            }
        }
        return count;
    }
}


