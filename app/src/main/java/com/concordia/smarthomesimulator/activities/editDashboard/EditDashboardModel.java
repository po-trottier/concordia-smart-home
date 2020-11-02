package com.concordia.smarthomesimulator.activities.editDashboard;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.Permissions;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.UserPreferences;
import com.concordia.smarthomesimulator.dataModels.Userbase;

import java.util.TimeZone;

import static com.concordia.smarthomesimulator.Constants.*;

public class EditDashboardModel extends ViewModel{

    public EditDashboardModel() {
    }

    /**
     * Gets time zone index.
     *
     * @param timeZone the time zone
     * @return the time zone index
     */
    public int getTimeZoneIndex(String timeZone) {
        String[] available = TimeZone.getAvailableIDs();
        for (int i = 0; i < available.length; i++) {
            if (available[i].equals(timeZone)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Adds the user to the userbase if no similar users exists in the userbase.
     *
     * @param context   the context
     * @param userbase  the userbase the user is added to
     * @param userToAdd the user to add
     * @return the int code for the feedback message
     */
    public int addUser(Context context, Userbase userbase, User userToAdd){
        if (userbase.addUserIfPossible(context, userToAdd)){
            return R.string.create_user_successful;
        }
        return R.string.create_user_failed;
    }

    /**
     * Delete user.
     *
     * @param context          the context
     * @param userbase         the userbase
     * @param usernameToDelete the username to delete
     */
    public void deleteUser(Context context, Userbase userbase, String usernameToDelete){
        userbase.deleteUserFromUsernameIfPossible(context, usernameToDelete);
    }

    /**
     * Will edit a user account if the edited account is not similar to another account in the userbase.
     *
     * @param context          the context
     * @param preferences      the preferences
     * @param userbase         the userbase
     * @param username         the username
     * @param password         the password
     * @param permissions      the new permissions
     * @param previousUsername the previous username
     * @return the int code for the feedback message
     */
    public int editUser(Context context, SharedPreferences preferences, Userbase userbase, String username, String password, String permissions, String previousUsername){
        // Get the Old User
        User oldUser = userbase.getUserFromUsername(previousUsername);

        // Validate the values of the username and password
        String validUsername = username;
        String validPassword = password;
        Permissions validPermissions = Permissions.fromString(permissions);
        if (validUsername.isEmpty())
            validUsername = oldUser.getUsername();
        if (validPassword.isEmpty())
            validPassword = oldUser.getPassword();
        if (validPermissions == null)
            validPermissions = oldUser.getPermission();

        // Create the New User
        User newUser = new User(validUsername, validPassword, validPermissions);

        // If we haven't changed the user, then don't do anything
        if (newUser.equals(oldUser))
            return -1;

        // Validate that the new user doesn't already exist and that the username is unique
        if (userbase.getNumberOfSimilarUsers(newUser) > 1 || userbase.containsUser(newUser))
            return R.string.edit_conflict;

        // Set the preferences of the new user to that of the old user
        newUser.setUserPreferences(oldUser.getUserPreferences());

        // Delete the Old User and transfer the preferences
        userbase.deleteUserFromUsernameIfPossible(context, oldUser.getUsername());

        // Add the New User
        addUser(context, userbase, newUser);

        // If the edited user is the current user modify it
        if (preferences.getString(PREFERENCES_KEY_USERNAME, "").equalsIgnoreCase(oldUser.getUsername())) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREFERENCES_KEY_USERNAME, username);
            editor.putString(PREFERENCES_KEY_PASSWORD, password);
            editor.putInt(PREFERENCES_KEY_PERMISSIONS, Permissions.fromString(permissions).getBitValue());
            editor.apply();
        }

        return R.string.edit_success;
    }

    /**
     * Edit parameters.
     *
     * @param preferences the preferences
     * @param status      the status
     * @param temperature the temperature
     * @param timezone    the timezone
     */
    public void editParameters(SharedPreferences preferences, boolean status, int temperature, String timezone) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREFERENCES_KEY_STATUS, status);
        editor.putInt(PREFERENCES_KEY_TEMPERATURE, temperature);
        editor.putString(PREFERENCES_KEY_TIME_ZONE, timezone);
        editor.apply();
    }

    /**
     * Has selected self boolean.
     *
     * @param preferences      the preferences
     * @param username the username to check
     * @return the whether the user selected themselves or not
     */
    public boolean hasSelectedSelf(SharedPreferences preferences, String username){
        String loggedUsername = preferences.getString(PREFERENCES_KEY_USERNAME, "");
        return loggedUsername.equalsIgnoreCase(username);
    }
}