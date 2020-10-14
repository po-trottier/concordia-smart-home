package com.concordia.smarthomesimulator.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.main.MainController;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;

public class LoginController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setLoginIntent();
    }

    private void setLoginIntent() {
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginController.this, MainController.class);
                LoginController.this.startActivity(intent);

                /**
                 *Two following two lines after this comment block are strictly for the developers
                 *These should be removed as they are only here for the
                 *purpose of showing how to use the methods
                 */
                ActivityLogHelper.add("button pressed","login screen", getApplicationContext());
                //ActivityLogHelper.clearLog(getApplicationContext());

                finish();
            }
        });
    }
}