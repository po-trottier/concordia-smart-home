package com.concordia.smarthomesimulator.activities.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.main.MainController;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.enums.LogImportance;
import com.concordia.smarthomesimulator.helpers.LogsHelper;

public class LoginController extends AppCompatActivity {

    private LoginModel loginModel;
    private Context context;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        loginModel = new ViewModelProvider(this).get(LoginModel.class);
        preferences = getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

        if(loginModel.getIsUserLoggedIn(preferences)) {
            LogsHelper.add(context, new LogEntry("Login","User was already logged in. Going to the Dashboard.", LogImportance.MINOR));
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

        //adding a listener to the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = usernameField.getText().toString();
                String inputPassword = passwordField.getText().toString();

                if (loginModel.logUserIn(context, preferences, inputUsername, inputPassword)) {
                    // Proceeding to the next activity and logging what happened
                    LogsHelper.add(context, new LogEntry("Login",String.format("User \"%s\" logged in successfully.", inputUsername), LogImportance.IMPORTANT));
                    Intent intent = new Intent(LoginController.this, MainController.class);
                    LoginController.this.startActivity(intent);
                    finish();
                }
            }
        });
    }



}