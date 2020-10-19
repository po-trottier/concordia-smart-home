package com.concordia.smarthomesimulator.activities.editDashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;
import com.concordia.smarthomesimulator.fragments.dashboard.DashboardController;

import java.util.LinkedList;
import java.util.List;

public class EditDashboardController extends AppCompatActivity {

    private Context context;
    private EditDashboardModel editDashboardModel;
    private Userbase userbase;
    Spinner permissions_spinner;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard);
        editDashboardModel = new ViewModelProvider(this).get(EditDashboardModel.class);
        context = EditDashboardController.this;
        sharedPreferences = this.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        setupToolbar();
        setupPermissionSpinner();

        //simulation context
        final TextView contextTitle = findViewById(R.id.edit_context_title);
        final EditText setTemperature = findViewById(R.id.set_temperature);
        final EditText setTimeZone = findViewById(R.id.set_timezone);
        final Button saveTemperature = findViewById(R.id.edit_context_button);

        saveTemperature.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String temperature = setTemperature.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Temperature", temperature);
                editor.apply();

                String timezone = setTimeZone.getText().toString();
                editor = sharedPreferences.edit();
                editor.putString("TimeZone", timezone);
                editor.apply();
            }
        });

        //users
        setDeleteUserIntent();
        setCreateUserIntent();

    }

    private String getTemperature(EditText setTemperature){
            return setTemperature.getText().toString();
    }

    private void setCreateUserIntent(){
        //adding listener to create button
        User userToAdd = getUserFromUI();
    }

    private void setDeleteUserIntent(){

    }

    private User getUserFromUI(){
        // fetch the username from the UI then all needed information from the userbase
        User user = null;
        String permissions = permissions_spinner.getSelectedItem().toString();
        return user;
    }

    private void setupPermissionSpinner(){
        permissions_spinner = (Spinner) findViewById(R.id.permissions_spinner);

        ArrayAdapter<String> permissions_adapter = new ArrayAdapter<>(context,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.permissions_spinner));
        permissions_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        permissions_spinner.setAdapter(permissions_adapter);
    }

    private void setupUsernamesSpinner(){
        permissions_spinner = (Spinner) findViewById(R.id.username_spinner);
        List<String> usernames = new LinkedList<>();
        usernames.add("woo");

        ArrayAdapter<String> permissions_adapter = new ArrayAdapter<>(context,
                R.layout.support_simple_spinner_dropdown_item, usernames);
        permissions_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        permissions_spinner.setAdapter(permissions_adapter);
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