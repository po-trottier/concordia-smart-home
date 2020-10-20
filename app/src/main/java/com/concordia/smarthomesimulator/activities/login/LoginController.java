package com.concordia.smarthomesimulator.activities.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.main.MainController;

import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.LogImportance;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;

import com.concordia.smarthomesimulator.helpers.UserbaseHelper;

import static com.concordia.smarthomesimulator.Constants.*;

public class LoginController extends AppCompatActivity {

    private Context context;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        sharedPreferences = getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

        String previousUsername = sharedPreferences.getString(PREFERENCES_KEY_USERNAME, null);
        String previousPassword = sharedPreferences.getString(PREFERENCES_KEY_PASSWORD, null);
        int previousPermissions = sharedPreferences.getInt(PREFERENCES_KEY_PERMISSIONS, -1);
        if (previousUsername != null && previousPassword != null && previousPermissions != -1) {
            ActivityLogHelper.add(context, new LogEntry("Login",String.format("User \"%s\" was already logged in.", previousUsername), LogImportance.IMPORTANT));
            Intent intent = new Intent(LoginController.this, MainController.class);
            LoginController.this.startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        setLoginIntent();
    }

    private void setLoginIntent() {
        //binding elements from the view
        final Button loginButton = findViewById(R.id.login_button);
        final EditText usernameField = findViewById(R.id.usernameField);
        final EditText passwordField = findViewById(R.id.passwordField);

        Userbase userbase = new Userbase(context);

        //adding a listener to the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = usernameField.getText().toString();
                String inputPassword = passwordField.getText().toString();

                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(context, R.string.empty_username_password_fields,Toast.LENGTH_SHORT).show();
                } else {
                    User loggedUser = UserbaseHelper.getUserWithCredentials(inputUsername, inputPassword, userbase);
                    if (loggedUser != null){
                        //save logged user info to preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PREFERENCES_KEY_USERNAME, loggedUser.getUsername());
                        editor.putString(PREFERENCES_KEY_PASSWORD, loggedUser.getPassword());
                        editor.putInt(PREFERENCES_KEY_PERMISSIONS, loggedUser.getPermission().getBitValue());
                        editor.apply();

                        // Proceeding to the next activity and logging what happened
                        ActivityLogHelper.add(context, new LogEntry("Login",String.format("User \"%s\" logged in successfully.", loggedUser.getUsername()), LogImportance.IMPORTANT));
                        Intent intent = new Intent(LoginController.this, MainController.class);
                        LoginController.this.startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(context, R.string.wrong_credentials_message,Toast.LENGTH_SHORT).show();
                        ActivityLogHelper.add(context, new LogEntry("Login",String.format("Someone tried to login to an account with username \"%s\" but failed.", inputUsername), LogImportance.CRITICAL));
                    }
                }

            }
        });
    }



}