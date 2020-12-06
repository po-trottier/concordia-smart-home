package com.concordia.smarthomesimulator.activities.editDashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.enums.Action;
import com.concordia.smarthomesimulator.enums.Permissions;
import com.concordia.smarthomesimulator.helpers.LayoutsHelper;
import com.concordia.smarthomesimulator.helpers.UserbaseHelper;
import com.concordia.smarthomesimulator.interfaces.IDevice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static com.concordia.smarthomesimulator.Constants.*;

public class EditDashboardModel extends ViewModel{

    private float timeFactor = 1f;
    private LocalDateTime dateTime = LocalDateTime.now();

    private Userbase userbase;

    public EditDashboardModel() {
    }

    /**
     * Initialize model.
     *
     * @param context the context
     */
    public void initializeModel(Context context) {
        userbase = UserbaseHelper.loadUserbase(context);
    }

    /**
     * Gets userbase.
     *
     * @return the userbase
     */
    public Userbase getUserbase() {
        return userbase;
    }

    /**
     * Sets userbase.
     *
     * @param userbase the userbase
     */
    public void setUserbase(Userbase userbase) {
        this.userbase = userbase;
    }

    /**
     * Gets time factor.
     *
     * @return the time factor
     */
    public float getTimeFactor() {
        return timeFactor;
    }

    /**
     * Sets time factor.
     *
     * @param timeFactor the time factor
     */
    public void setTimeFactor(float timeFactor) {
        this.timeFactor = timeFactor;
    }

    /**
     * Increase time factor.
     */
    public void increaseTimeFactor() {
        for (int i = 0; i < AVAILABLE_TIME_FACTORS.length; i++) {
            if (AVAILABLE_TIME_FACTORS[i] != timeFactor) {
                continue;
            }
            // Avoid out of bounds exception
            int index = i + 1 == AVAILABLE_TIME_FACTORS.length ? i : i + 1;
            timeFactor = AVAILABLE_TIME_FACTORS[index];
            return;
        }
        // The current time factor was not found.
        timeFactor = AVAILABLE_TIME_FACTORS[0];
    }

    /**
     * Decrease time factor.
     */
    public void decreaseTimeFactor() {
        for (int i = 0; i < AVAILABLE_TIME_FACTORS.length; i++) {
            if (AVAILABLE_TIME_FACTORS[i] != timeFactor) {
                continue;
            }
            // Avoid out of bounds exception
            int index = i == 0 ? i : i - 1;
            timeFactor = AVAILABLE_TIME_FACTORS[index];
            return;
        }
        // The current time factor was not found.
        timeFactor = AVAILABLE_TIME_FACTORS[0];
    }

    /**
     * Update simulation date and time based on the preferences.
     *
     * @param preferences the preferences
     */
    public void updateSimulationDateTime(SharedPreferences preferences) {
        LocalDateTime timeNow = LocalDateTime.now();
        int year = preferences.getInt(PREFERENCES_KEY_DATETIME_YEAR, timeNow.getYear());
        int month = preferences.getInt(PREFERENCES_KEY_DATETIME_MONTH, timeNow.getMonthValue());
        int day = preferences.getInt(PREFERENCES_KEY_DATETIME_DAY, timeNow.getDayOfMonth());
        int hour = preferences.getInt(PREFERENCES_KEY_DATETIME_HOUR, timeNow.getHour());
        int minute = preferences.getInt(PREFERENCES_KEY_DATETIME_MINUTE, timeNow.getMinute());
        dateTime = LocalDateTime.of(year, month, day, hour, minute);
    }

    /**
     * Gets simulation date time.
     *
     * @return the simulation date time
     */
    public LocalDateTime getSimulationDateTime() {
        return dateTime;
    }

    /**
     * Sets simulation time.
     *
     * @param hour   the hour
     * @param minute the minute
     */
    public void setSimulationTime(int hour, int minute) {
        dateTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), hour, minute);
    }

    /**
     * Sets simulation date.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     */
    public void setSimulationDate(int year, int month, int day) {
        dateTime = LocalDateTime.of(year, month, day, dateTime.getHour(), dateTime.getMinute());
    }

    /**
     * Edit local permissions configuration. The userbase's permissionConfiguration will be updated when the user
     * clicks the save FAB given that the user has permission.
     *
     * @param permissions                   the permissions
     * @param action                        the action
     */
    public void editPermissionsConfiguration(Permissions permissions, Action action){
        Map<Action, Permissions> actionPermissionsMapToEdit = userbase.getPermissionsConfiguration().getActionPermissionsMap();
        actionPermissionsMapToEdit.replace(action, permissions);
        userbase.setPermissionConfiguration(new PermissionsConfiguration(actionPermissionsMapToEdit));
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
     * @param userbase         the userbase
     * @param username         the username
     * @param password         the password
     * @param permissions      the new permissions
     * @param previousUsername the previous username
     * @return the int code for the feedback message
     */
    public int editUser(Context context, Userbase userbase, String username, String password, String permissions, String previousUsername){
        // Validate user permissions
        if (!UserbaseHelper.verifyPermissions(Action.MODIFY_USERBASE, context)) {
            return -1;
        }

        // Get Preferences
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

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
        if (newUser.equals(oldUser)) {
            return -1;
        }

        // Validate that the new user doesn't already exist and that the username is unique
        if (userbase.getNumberOfSimilarUsers(newUser) > 1 || userbase.containsUser(newUser)) {
            return R.string.edit_conflict;
        }

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
     * @param context     the context
     * @param status      the status
     * @param temperature the temperature
     * @param date        the date
     * @param time        the time
     */
    public void editParameters(Context context, boolean status, boolean awayMode, int callTimer, int temperature,
                               LocalDate date, LocalTime time, int winterStart, int winterEnd, int summerStart, int summerEnd) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // Verify the permissions if the user changed the away mode
        boolean awayChanged = preferences.getBoolean(PREFERENCES_KEY_AWAY_MODE, false) != awayMode;
        if (awayChanged && UserbaseHelper.verifyPermissions(Action.CHANGE_AWAY_MODE, context)) {
            editor.putBoolean(PREFERENCES_KEY_AWAY_MODE, awayMode);
            if (awayMode) {
                setLayoutInAwayMode(context);
            }
        }
        // Set the other parameters
        editor.putBoolean(PREFERENCES_KEY_STATUS, status);
        editor.putInt(PREFERENCES_KEY_CALL_DELAY, callTimer);
        editor.putInt(PREFERENCES_KEY_TEMPERATURE, temperature);
        editor.putInt(PREFERENCES_KEY_DATETIME_YEAR, date.getYear());
        editor.putInt(PREFERENCES_KEY_DATETIME_MONTH, date.getMonthValue());
        editor.putInt(PREFERENCES_KEY_DATETIME_DAY, date.getDayOfMonth());
        editor.putInt(PREFERENCES_KEY_DATETIME_HOUR, time.getHour());
        editor.putInt(PREFERENCES_KEY_DATETIME_MINUTE, time.getMinute());
        editor.putFloat(PREFERENCES_KEY_TIME_SCALE, timeFactor);
        if (validateSeasons(winterStart, winterEnd, summerStart, summerEnd)) {
            editor.putInt(PREFERENCES_KEY_WINTER_START, winterStart);
            editor.putInt(PREFERENCES_KEY_WINTER_END, winterEnd);
            editor.putInt(PREFERENCES_KEY_SUMMER_START, summerStart);
            editor.putInt(PREFERENCES_KEY_SUMMER_END, summerEnd);
        } else {
            Toast.makeText(context, context.getString(R.string.invalid_seasons_range), Toast.LENGTH_SHORT).show();
        }
        editor.apply();
    }

    /**
     * Has selected self boolean.
     *
     * @param preferences the preferences
     * @param username    the username to check
     * @return the whether the user selected themselves or not
     */
    public boolean hasSelectedSelf(SharedPreferences preferences, String username){
        String loggedUsername = preferences.getString(PREFERENCES_KEY_USERNAME, "");
        return loggedUsername.equalsIgnoreCase(username);
    }

    private boolean validateSeasons(int winterStart, int winterEnd, int summerStart, int summerEnd) {
        // 4 cases: (SS = Summer Start, SE = Summer End, WS = Winter Start, etc.)
        //  1) SS <= SE <= WS <= WE
        //  2) SE <= WS <= WE <= SS
        //  3) WS <= WE <= SS <= SE
        //  4) WE <= SS <= SE <= WS
        boolean case1 = summerStart <= summerEnd && summerEnd <= winterStart && winterStart <= winterEnd;
        boolean case2 = summerEnd <= winterStart && winterStart <= winterEnd && winterEnd <= summerStart;
        boolean case3 = winterStart <= winterEnd && winterEnd <= summerStart && summerStart <= summerEnd;
        boolean case4 = winterEnd <= summerStart && summerStart <= summerEnd && summerEnd <= winterStart;
        // Is any of the cases true ?
        return case1 || case2 || case3 || case4;
    }

    private void setLayoutInAwayMode(Context context) {
        HouseLayout layout = LayoutsHelper.getSelectedLayout(context);
        for (Room room : layout.getRooms()) {
            if (room.getDevices().size() == 0) {
                continue;
            }
            for (IDevice device : room.getDevices()) {
                switch (device.getDeviceType()) {
                    case DOOR:
                        Door door = (Door) device;
                        if (door.isAutoLock()) {
                            door.setIsLocked(true);
                        }
                        break;
                    case WINDOW:
                        Window window = (Window) device;
                        window.setIsOpened(false);
                }
            }
        }
        if (!LayoutsHelper.isLayoutNameDefault(layout)) {
            LayoutsHelper.saveHouseLayout(context, layout);
        }
        LayoutsHelper.updateSelectedLayout(context, layout);
    }
}