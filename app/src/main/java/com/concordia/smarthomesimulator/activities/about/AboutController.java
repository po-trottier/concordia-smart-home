package com.concordia.smarthomesimulator.activities.about;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.concordia.smarthomesimulator.R;

public class AboutController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupToolbar();
    }

    private void setupToolbar() {
        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_secondary);
        toolbar.setTitleTextColor(getColor(R.color.charcoal));
        setSupportActionBar(toolbar);

        // Add back button
        if (getSupportActionBar() != null) {
            toolbar.setNavigationIcon(getDrawable(R.drawable.ic_action_back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}