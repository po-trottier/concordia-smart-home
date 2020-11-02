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

        loggedUser.getUserPreferences().loadUserPreferences(preferences);

        return true;
    }

}





