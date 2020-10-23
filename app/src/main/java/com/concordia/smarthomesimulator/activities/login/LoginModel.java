package com.concordia.smarthomesimulator.activities.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.LogImportance;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;
import com.concordia.smarthomesimulator.helpers.UserbaseHelper;

import java.util.Map;

import static com.concordia.smarthomesimulator.Constants.*;

public class LoginModel extends ViewModel {

    public LoginModel() {
    }

    /**
     * Gets is user logged in.
     *
     * @param preferences the preferences
     * @return whether the user id logged in or not
     */
    public boolean getIsUserLoggedIn(SharedPreferences preferences) {
        String previousUsername = preferences.getString(PREFERENCES_KEY_USERNAME, null);
        String previousPassword = preferences.getString(PREFERENCES_KEY_PASSWORD, null);
        int previousPermissions = preferences.getInt(PREFERENCES_KEY_PERMISSIONS, -1);

        return previousUsername != null && previousPassword != null && previousPermissions != -1;
    }

    /**
     * Log user in boolean.
     *
     * @param context     the context
     * @param preferences the preferences
     * @param username    the username
     * @param password    the password
     * @return the whether the operation was successful or not
     */
    public boolean logUserIn(Context context, SharedPreferences preferences, String username, String password) {
        Userbase userbase = new Userbase(context);

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, R.string.empty_username_password_fields,Toast.LENGTH_SHORT).show();
            return false;
        }

        User loggedUser = UserbaseHelper.getUserWithCredentials(username, password, userbase);
        if (loggedUser == null){
            Toast.makeText(context, R.string.wrong_credentials_message,Toast.LENGTH_SHORT).show();
            ActivityLogHelper.add(context, new LogEntry("Login", String.format("Someone tried to login to an account with username \"%s\" but failed.", username), LogImportance.CRITICAL));
            return  false;
        }

        SharedPreferences.Editor editor = preferences.edit();
        if (loggedUser.getPreferences().size() == 0) {
            // Save logged user info to preferences if the user has no saved preferences
            editor.putString(PREFERENCES_KEY_USERNAME, loggedUser.getUsername());
            editor.putString(PREFERENCES_KEY_PASSWORD, loggedUser.getPassword());
            editor.putInt(PREFERENCES_KEY_PERMISSIONS, loggedUser.getPermission().getBitValue());
        } else {
            // Otherwise load the preferences from the user obj
            loadPreferences(loggedUser, editor);
        }
        editor.apply();

        return true;
    }

    private void loadPreferences(User user, SharedPreferences.Editor editor){
        for (Map.Entry<String, ?> entry : user.getPreferences().entrySet()) {
            if (entry.getValue() instanceof String){
                editor.putString(entry.getKey(), (String)entry.getValue());
            } else if (entry.getValue() instanceof Integer){
                editor.putInt(entry.getKey(), (int)entry.getValue());
            } else if (entry.getValue() instanceof Double){
                // converting to int, problem caused by reading/writing the object file
                editor.putInt(entry.getKey(), ((Double) entry.getValue()).intValue());
            } else if (entry.getValue() instanceof Boolean){
                editor.putBoolean(entry.getKey(), (boolean)entry.getValue());
            }
        }
    }
}





