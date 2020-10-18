package com.concordia.smarthomesimulator.activities.editDashboard;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.concordia.smarthomesimulator.R;

public class EditDashboardController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard);
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