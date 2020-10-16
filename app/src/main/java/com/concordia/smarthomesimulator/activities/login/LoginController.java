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

import java.util.Optional;

import static com.concordia.smarthomesimulator.helpers.UserbaseHelper.getUserWithCredentials;


public class LoginController extends AppCompatActivity {

    /**
     * The Context of the login activity.
     */
    Context context;
    /**
     * The Shared preferences used to store the info of the logged user.
     */
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_login);
        setLoginIntent();
    }

    private void setLoginIntent() {
        //binding elements from the view
        Button loginButton = findViewById(R.id.login_button);
        final EditText eUsername = findViewById(R.id.etUsername);
        final EditText ePassword = findViewById(R.id.etPassword);

        final Userbase userBase = new Userbase(context);
        sharedPreferences = this.getSharedPreferences("com.concordia.smarthomesimulator",Context.MODE_PRIVATE);

        //adding a listener to the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = eUsername.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginController.this, "Please enter a username and password",Toast.LENGTH_SHORT).show();
                } else {
                    User logged_user = getUserWithCredentials(inputUsername, inputPassword, userBase);
                    if (logged_user != null){
                        //save logged user info to preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username",logged_user.getUsername());
                        editor.putString("password",logged_user.getPassword());
                        editor.putInt("permissions", logged_user.getPermission().getBitVal());
                        editor.apply();

                        //proceeding to the next activity and logging what happened
                        Intent intent = new Intent(LoginController.this, MainController.class);
                        ActivityLogHelper.add(context, new LogEntry("Login","User logged in", LogImportance.IMPORTANT));
                        LoginController.this.startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginController.this, "The credentials entered do not correspond to a known account",Toast.LENGTH_SHORT).show();
                        ActivityLogHelper.add(context, new LogEntry("Login","The credentials entered do not correspond to a known account", LogImportance.IMPORTANT));
                    }
                }

            }
        });
    }



}