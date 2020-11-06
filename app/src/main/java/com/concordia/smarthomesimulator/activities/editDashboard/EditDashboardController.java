package com.concordia.smarthomesimulator.activities.editDashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;
import com.concordia.smarthomesimulator.helpers.UserbaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.concordia.smarthomesimulator.Constants.*;

public class EditDashboardController extends AppCompatActivity {

    private Context context;
    private SharedPreferences preferences;

    private EditDashboardModel editDashboardModel;
    private Userbase userbase;
    private PermissionsConfiguration localPermissionsConfiguration;

    private SwitchCompat statusField;
    private TextView statusText;
    private EditText temperatureField;
    private FloatingActionButton saveContext;
    private Button deleteUser;
    private Spinner timezoneSpinner;
    private Spinner editPermissionsSpinner;
    private EditText editedUsername;
    private EditText editedPassword;
    private Button createUserButton;
    private EditText newUsernameField;
    private EditText newPasswordField;
    private Spinner newPermissionsSpinner;
    private Spinner usernameSpinner;
    private Spinner minimumPermissionsSpinnerWindowAll;
    private Spinner minimumPermissionsSpinnerWindowLocal;
    private Spinner minimumPermissionsSpinnerDoorAll;
    private Spinner minimumPermissionsSpinnerDoorLocal;
    private Spinner minimumPermissionsSpinnerLightAll;
    private Spinner minimumPermissionsSpinnerLightLocal;
    private Spinner minimumPermissionsSpinnerGarage;
    private Spinner minimumPermissionsSpinnerAwayMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_edit_dashboard);
        editDashboardModel = new ViewModelProvider(this).get(EditDashboardModel.class);

        preferences = getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        userbase = UserbaseHelper.loadUserbase(context);
        localPermissionsConfiguration = new PermissionsConfiguration(userbase.getPermissionConfiguration());

        setupToolbar();
        findControls();
        setSaveIntent();
        setDeleteUserIntent();
        setCreateUserIntent();
        setupTimezoneSpinner();
        setupPermissionSpinner();
        setupUsernamesSpinner();
        setupPermissionConfigurationRows();
        fillKnownValues();
        // DO NOT PUT BEFORE "fillKnownValues" !
        setupStatusSwitch();
    }

    private void findControls() {
        statusField = findViewById(R.id.on_off);
        statusText = findViewById(R.id.on_off_text);
        temperatureField = findViewById(R.id.set_temperature);
        saveContext = findViewById(R.id.save_context_button);
        timezoneSpinner = findViewById(R.id.timezone_spinner);
        editPermissionsSpinner = findViewById(R.id.edit_permissions_spinner);
        editedUsername = findViewById(R.id.edit_username_field);
        editedPassword = findViewById(R.id.edit_password_field);
        newPermissionsSpinner = findViewById(R.id.new_permissions_spinner);
        usernameSpinner = findViewById(R.id.username_spinner);
        deleteUser = findViewById(R.id.delete_button);
        createUserButton = findViewById(R.id.create_button);
        newUsernameField = findViewById(R.id.new_username_field);
        newPasswordField = findViewById(R.id.new_password_field);
        minimumPermissionsSpinnerWindowAll = findViewById(R.id.minimum_permissions_spinner_window_all);
        minimumPermissionsSpinnerWindowLocal = findViewById(R.id.minimum_permissions_spinner_window_local);
        minimumPermissionsSpinnerDoorAll = findViewById(R.id.minimum_permissions_spinner_door_all);
        minimumPermissionsSpinnerLightLocal = findViewById(R.id.minimum_permissions_spinner_door_local);
        minimumPermissionsSpinnerDoorLocal = findViewById(R.id.minimum_permissions_spinner_light_all);
        minimumPermissionsSpinnerLightAll = findViewById(R.id.minimum_permissions_spinner_light_local);
        minimumPermissionsSpinnerGarage = findViewById(R.id.minimum_permissions_spinner_garage);
        minimumPermissionsSpinnerAwayMode = findViewById(R.id.minimum_permissions_spinner_away_mode);
    }

    private void fillKnownValues() {
        // Set the simulation status
        statusField.setChecked(preferences.getBoolean(PREFERENCES_KEY_STATUS, false));
        // Set the known temperature
        temperatureField.setText(Integer.toString(preferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE)));
        // Set the know Time Zone
        String timeZone = preferences.getString(PREFERENCES_KEY_TIME_ZONE, "");
        timezoneSpinner.setSelection(editDashboardModel.getTimeZoneIndex(timeZone));
    }

    private void setSaveIntent() {
        saveContext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Get the User's information
                String username= editedUsername.getText().toString();
                String password = editedPassword.getText().toString();
                String permissions = editPermissionsSpinner.getSelectedItem().toString();
                String oldUsername = usernameSpinner.getSelectedItem().toString();
                // Edit user if it was changed
                int feedbackResource = editDashboardModel.editUser(context, preferences, userbase, username, password, permissions, oldUsername);
                if (feedbackResource != -1) {
                    String message = getString(feedbackResource);
                    ActivityLogHelper.add(context, new LogEntry("Edit Simulation Context", message, LogImportance.IMPORTANT));
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }

                // Get the Simulation Context Parameters
                String timezone = timezoneSpinner.getSelectedItem().toString();
                int temperature = DEFAULT_TEMPERATURE;
                try {
                    temperature = Integer.parseInt(temperatureField.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                // Edit the parameters
                editDashboardModel.editParameters(preferences, statusField.isChecked(), temperature, timezone);

                // Close the activity
                ActivityLogHelper.add(context, new LogEntry("Edit Simulation Context", "Simulation Context Was Saved Successfully.", LogImportance.IMPORTANT));
                finish();
            }
        });
    }

    private void setDeleteUserIntent(){
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                    .setTitle(getString(R.string.edit_text_delete_user_title))
                    .setMessage(getString(R.string.edit_text_delete_user))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final String usernameToDelete = usernameSpinner.getSelectedItem().toString();
                            if (editDashboardModel.hasSelectedSelf(preferences, usernameToDelete)){
                                Toast.makeText(context, R.string.delete_logged_user_warning, Toast.LENGTH_LONG).show();
                            } else {
                                editDashboardModel.deleteUser(context, userbase, usernameToDelete);

                                String message = getString(R.string.delete_logged_user_success);
                                ActivityLogHelper.add(context, new LogEntry("Edit Simulation Context", message, LogImportance.IMPORTANT));
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                setupUsernamesSpinner();
                            }
                        }})
                    .show();
            }
        });
    }

    private void setCreateUserIntent(){
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the User's information
                String newUsername = newUsernameField.getText().toString();
                String newPassword = newPasswordField.getText().toString();
                Permissions newPermissions = Permissions.fromString(newPermissionsSpinner.getSelectedItem().toString());
                // Add the new User
                int feedbackResource = editDashboardModel.addUser(context, userbase, new User(newUsername, newPassword, newPermissions));
                String message = getString(feedbackResource);
                ActivityLogHelper.add(context, new LogEntry("Edit Simulation Context", message, LogImportance.IMPORTANT));
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                // Reset the fields
                newUsernameField.setText("");
                newPasswordField.setText("");
                newPermissionsSpinner.setSelection(0);
                // Notify the list has changed
                setupUsernamesSpinner();
            }
        });
    }

    private void setupStatusSwitch() {
        if (statusField.isChecked()) {
            statusText.setText(getString(R.string.simulation_status_started));
            statusText.setTextColor(getColor(R.color.primary));
        } else {
            statusText.setText(getString(R.string.simulation_status_stopped));
            statusText.setTextColor(getColor(R.color.charcoal));
        }
        statusField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusField.isChecked()) {
                    statusText.setText(getString(R.string.simulation_status_started));
                    statusText.setTextColor(getColor(R.color.primary));
                } else {
                    statusText.setText(getString(R.string.simulation_status_stopped));
                    statusText.setTextColor(getColor(R.color.charcoal));
                }
            }
        });
    }

    private void setupTimezoneSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            TimeZone.getAvailableIDs()
        );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        timezoneSpinner.setAdapter(adapter);
    }

    private void setupPermissionSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            getResources().getStringArray(R.array.permissions_spinner)
        );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        editPermissionsSpinner.setAdapter(adapter);
        newPermissionsSpinner.setAdapter(adapter);
        minimumPermissionsSpinnerWindowAll.setAdapter(adapter);
        minimumPermissionsSpinnerWindowLocal.setAdapter(adapter);
        minimumPermissionsSpinnerDoorAll.setAdapter(adapter);
        minimumPermissionsSpinnerDoorLocal.setAdapter(adapter);
        minimumPermissionsSpinnerLightAll.setAdapter(adapter);
        minimumPermissionsSpinnerLightLocal.setAdapter(adapter);
        minimumPermissionsSpinnerGarage.setAdapter(adapter);
        minimumPermissionsSpinnerAwayMode.setAdapter(adapter);
    }

    private void setupUsernamesSpinner(){
        Userbase userbase = UserbaseHelper.loadUserbase(context);
        List<String> users = userbase.getUsernames();
        Collections.sort(users);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            users
        );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        usernameSpinner.setAdapter(adapter);

        // Set known information about the selected User to Edit
        setUserInformation(userbase.getUserFromUsername(users.get(0)));

        usernameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setUserInformation(userbase.getUserFromUsername(users.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setupPermissionConfigurationRows() {

        for(Map.Entry<Action, Permissions> entry : localPermissionsConfiguration.getActionPermissionsMap().entrySet()) {

        }

    }

    private void setUserInformation(User user) {
        editedUsername.setText(user.getUsername());
        editedPassword.setText(user.getPassword());
        switch (user.getPermission()) {
            case PARENT:
                editPermissionsSpinner.setSelection(0);
                break;
            case CHILD:
                editPermissionsSpinner.setSelection(1);
                break;
            case GUEST:
                editPermissionsSpinner.setSelection(2);
                break;
            default:
                editPermissionsSpinner.setSelection(3);
                break;
        }
    }

    private void setupToolbar() {
        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_secondary);
        toolbar.setTitle(getString(R.string.title_activity_edit_dashboard));
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