package com.concordia.smarthomesimulator.activities.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.main.MainController;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.LogImportance;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;

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
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Replace this line. This is for testing purpose only
                ActivityLogHelper.add(context, new LogEntry("Login","User logged in", LogImportance.IMPORTANT));

                Intent intent = new Intent(LoginController.this, MainController.class);
                LoginController.this.startActivity(intent);
                finish();
            }
        });
    }
}