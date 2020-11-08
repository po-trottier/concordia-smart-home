package com.concordia.smarthomesimulator.dataModels;

import android.content.Context;
import com.concordia.smarthomesimulator.helpers.UserbaseHelper;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Userbase.
 * The userbase contains all the users that can interact with the simulation
 * along with the permission configuration which specify what each permission is able to do.
 */
public class Userbase implements Serializable {

    private List<User> users;
    private PermissionsConfiguration permissionsConfiguration;

    /**
     * Instantiates a new Userbase using a passed List of users.
     * The default permission configuration is assigned to it.
     *
     * @param users the users
     */
    public Userbase(List<User> users){
        this.users = users;
        this.permissionsConfiguration = new PermissionsConfiguration();
    }

    /**
     * Gets permission configuration.
     *
     * @return the permission configuration
     */
    public PermissionsConfiguration getPermissionsConfiguration() {
        // Make sure to create the permissions if they don't exist
        if (permissionsConfiguration == null) {
            permissionsConfiguration = new PermissionsConfiguration();
        }
        return permissionsConfiguration;
    }

    /**
     * Sets permission configuration.
     *
     * @param permissionsConfiguration the permission configuration
     */
    public void setPermissionConfiguration(PermissionsConfiguration permissionsConfiguration) {
        this.permissionsConfiguration = permissionsConfiguration;
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
        return users.stream().map(User::getUsername).collect(Collectors.toList());
    }

    /**
     * Gets user from username.
     *
     * @param username the username
     * @return the user
     */
    public User getUserFromUsername(String username){
        for (User user: users) {
            if (user.getUsername().equalsIgnoreCase(username)){
                return user;
            }
        }
        return null;
    }

    /**
     * Deletes a user if present
     *
     * @param context          the context
     * @param usernameToDelete the username to delete
     * @return true if deletion successful
     */
    public boolean deleteUserFromUsernameIfPossible(Context context, String usernameToDelete){
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getUsername().equalsIgnoreCase(usernameToDelete)){
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
     * @param context   the context
     * @param userToAdd the user to add
     * @return the boolean showing if the addition was successful
     */
    public boolean addUserIfPossible(Context context, User userToAdd){
        if (getNumberOfSimilarUsers(userToAdd) > 0){
            return false;
        }
        users.add(userToAdd);
        UserbaseHelper.saveUserbase(context,this);
        return true;
    }

    /**
     * Checks if the userbase contains this user.
     *
     * @param userToCompare the user to compare
     * @return true if it contains the user
     */
    public boolean containsUser(User userToCompare){
        for (User user: getUsers()) {
            if (user.equals(userToCompare)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets number of similar users.
     *
     * @param userToCompare the user to compare
     * @return the number of similar users
     */
    public int getNumberOfSimilarUsers(User userToCompare){
        int count = 0;
        for (User user : getUsers()) {
            if (user.isSimilar(userToCompare)){
                count++;
            }
        }
        return count;
    }
}


