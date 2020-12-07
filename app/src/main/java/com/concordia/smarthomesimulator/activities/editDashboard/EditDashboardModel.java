package com.concordia.smarthomesimulator.activities.editDashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.enums.Action;
import com.concordia.smarthomesimulator.enums.Permissions;
import com.concordia.smarthomesimulator.exceptions.PermissionNotFoundException;
import com.concordia.smarthomesimulator.helpers.LayoutsHelper;
import com.concordia.smarthomesimulator.helpers.UserbaseHelper;
import com.concordia.smarthomesimulator.interfaces.IDevice;
import com.concordia.smarthomesimulator.interfaces.IInhabitant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import static com.concordia.smarthomesimulator.Constants.*;

public class EditDashboardModel extends ViewModel{

    private float timeFactor = 1f;
    private LocalDateTime dateTime = LocalDateTime.now();
    private LocalTime minLightsTime = DEFAULT_MIN_LIGHTS_TIME;
    private LocalTime maxLightsTime = DEFAULT_MAX_LIGHTS_TIME;

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

    public void updateMinLightsTime(SharedPreferences preferences) {
        LocalTime timeLightsMin = minLightsTime;
        int hour = preferences.getInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_HOUR, timeLightsMin.getHour());
        int minute = preferences.getInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_MINUTE, timeLightsMin.getMinute());
        minLightsTime = LocalTime.of(hour, minute);
    }

    public void updateMaxLightsTime(SharedPreferences preferences) {
        LocalTime timeLightsMax = maxLightsTime;
        int hour = preferences.getInt(PREFERENCES_KEY_MAX_LIGHTS_TIME_HOUR, timeLightsMax.getHour());
        int minute = preferences.getInt(PREFERENCES_KEY_MAX_LIGHTS_TIME_MINUTE, timeLightsMax.getMinute());
        maxLightsTime = LocalTime.of(hour, minute);
    }

    /**
     * Gets lights' minimum date time.
     *
     * @return the lights' minimum date time
     */
    public LocalTime getMinLightsTime() {
        return minLightsTime;
    }

    /**
     * Sets lights' minimum time.
     *
     * @param hour   the hour
     * @param minute the minute
     */
    public void setMinLightsTime(int hour, int minute) {
        minLightsTime = LocalTime.of(hour, minute);
    }

    /**
     * Gets lights' maximum date time.
     *
     * @return the lights' minimum date time
     */
    public LocalTime getMaxLightsTime() {
        return maxLightsTime;
    }

    /**
     * Sets lights' maximum time.
     *
     * @param hour   the hour
     * @param minute the minute
     */
    public void setMaxLightsTime(int hour, int minute) {
        maxLightsTime = LocalTime.of(hour, minute);
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
        try {
            if (!UserbaseHelper.verifyPermissions(Action.MODIFY_USERBASE, context)) {
                return -1;
            }
        } catch (PermissionNotFoundException ignore) {
            Toast.makeText(context, context.getString(R.string.permission_error), Toast.LENGTH_SHORT).show();
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
     *  @param context     the context
     * @param parametersArgument arguments
     */
    public void editParameters(Context context, ParametersArgument parametersArgument) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // Verify the permissions if the user changed the away mode
        boolean awayChanged = preferences.getBoolean(PREFERENCES_KEY_AWAY_MODE, false) != parametersArgument.isAwayMode();
        try {
            if (awayChanged && UserbaseHelper.verifyPermissions(Action.CHANGE_AWAY_MODE, context)) {
                editor.putBoolean(PREFERENCES_KEY_AWAY_MODE, parametersArgument.isAwayMode());
                if (parametersArgument.isAwayMode()) {
                    setLayoutInAwayMode(context);
                }
            }
        } catch (PermissionNotFoundException ignore) {
            Toast.makeText(context, context.getString(R.string.permission_error), Toast.LENGTH_SHORT).show();
        }
        // Set the other parameters
        editor.putBoolean(PREFERENCES_KEY_STATUS, parametersArgument.getStatus());
        editor.putInt(PREFERENCES_KEY_CALL_DELAY, parametersArgument.getCallTimer());
        editor.putInt(PREFERENCES_KEY_TEMPERATURE, parametersArgument.getTemperature());
        editor.putInt(PREFERENCES_KEY_SUMMER_TEMPERATURE, parametersArgument.getSummerTemperature());
        editor.putInt(PREFERENCES_KEY_WINTER_TEMPERATURE, parametersArgument.getWinterTemperature());
        editor.putInt(PREFERENCES_KEY_DATETIME_YEAR, parametersArgument.getDate().getYear());
        editor.putInt(PREFERENCES_KEY_DATETIME_MONTH, parametersArgument.getDate().getMonthValue());
        editor.putInt(PREFERENCES_KEY_DATETIME_DAY, parametersArgument.getDate().getDayOfMonth());
        editor.putInt(PREFERENCES_KEY_DATETIME_HOUR, parametersArgument.getTime().getHour());
        editor.putInt(PREFERENCES_KEY_DATETIME_MINUTE, parametersArgument.getTime().getMinute());
        editor.putInt(PREFERENCES_KEY_MIN_TEMPERATURE_ALERT, parametersArgument.getMinAlertTemperature());
        editor.putInt(PREFERENCES_KEY_MAX_TEMPERATURE_ALERT, parametersArgument.getMaxAlertTemperature());
        editor.putInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_HOUR, parametersArgument.getMinLightsTime().getHour());
        editor.putInt(PREFERENCES_KEY_MIN_LIGHTS_TIME_MINUTE, parametersArgument.getMinLightsTime().getMinute());
        editor.putInt(PREFERENCES_KEY_MAX_LIGHTS_TIME_HOUR, parametersArgument.getMaxLightsTime().getHour());
        editor.putInt(PREFERENCES_KEY_MAX_LIGHTS_TIME_MINUTE, parametersArgument.getMaxLightsTime().getMinute());
        editor.putFloat(PREFERENCES_KEY_TIME_SCALE, timeFactor);
        if (validateSeasons(parametersArgument)) {
            editor.putInt(PREFERENCES_KEY_WINTER_START, parametersArgument.getWinterStart());
            editor.putInt(PREFERENCES_KEY_WINTER_END, parametersArgument.getWinterEnd());
            editor.putInt(PREFERENCES_KEY_SUMMER_START, parametersArgument.getSummerStart());
            editor.putInt(PREFERENCES_KEY_SUMMER_END, parametersArgument.getSummerEnd());
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

    /**
     * Is house empty boolean.
     *
     * @return the boolean
     */
    public boolean isHouseEmpty(Context context) {
        HouseLayout houseLayout = LayoutsHelper.getSelectedLayout(context);
        if (houseLayout != null) {
            ArrayList<IInhabitant> inhabitants = houseLayout.getAllInhabitants();
            return inhabitants.stream().allMatch(IInhabitant::isIntruder);
        }
        else{
            return false;
        }
    }

    private boolean validateSeasons(ParametersArgument parametersArgument) {
        // 4 cases: (SS = Summer Start, SE = Summer End, WS = Winter Start, etc.)
        //  1) SS <= SE <= WS <= WE
        //  2) SE <= WS <= WE <= SS
        //  3) WS <= WE <= SS <= SE
        //  4) WE <= SS <= SE <= WS
        int winterStart = parametersArgument.getWinterStart();
        int winterEnd = parametersArgument.getWinterEnd();
        int summerStart = parametersArgument.getSummerStart();
        int summerEnd = parametersArgument.getSummerEnd();
        // Evaluate the cases
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