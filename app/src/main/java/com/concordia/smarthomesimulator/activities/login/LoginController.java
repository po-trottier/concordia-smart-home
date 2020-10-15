package com.concordia.smarthomesimulator.activities.login;

import android.content.Context;
import android.content.Intent;
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
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;

import com.concordia.smarthomesimulator.dataModels.User;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;


public class LoginController extends AppCompatActivity {

    Context context;

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

        final User[] users = getAllUsers();

        //adding a listener to the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = eUsername.getText().toString();
                String inputPassword = ePassword.getText().toString();
                if (areCredentialsValid(inputUsername, inputPassword, users)) {
                    Intent intent = new Intent(LoginController.this, MainController.class);
                    // TODO: Replace this line. This is for testing purpose only
                    ActivityLogHelper.add(context, new LogEntry("Login","User logged in", LogImportance.IMPORTANT));
                    LoginController.this.startActivity(intent);
                    finish();
                } else if (inputUsername.isEmpty() || inputPassword.isEmpty()){
                    Toast.makeText(LoginController.this, "Please enter a username and password",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginController.this, "The credentials entered do not correspond to a known account",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean areCredentialsValid(String username, String password, User[] users){
//        try {
//            FileReader reader = new FileReader("users.json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Toast.makeText(LoginController.this, "Could not find credentials file",Toast.LENGTH_SHORT).show();
//        }
        return false;
    }

    private User[] getAllUsers(){
        //checking if a user file is present, creating one if it isn't

    }

}