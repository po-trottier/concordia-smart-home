package com.concordia.smarthomesimulator.activities.editDashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.Permissions;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;
import com.concordia.smarthomesimulator.helpers.UserbaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static com.concordia.smarthomesimulator.Constants.*;

public class EditDashboardController extends AppCompatActivity {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private EditDashboardModel editDashboardModel;
    private Userbase userbase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard);
        context = this;
        userbase = new Userbase(context);
        editDashboardModel = new ViewModelProvider(this).get(EditDashboardModel.class);
        sharedPreferences = getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        setupToolbar();

        findControls();

        setSaveIntent();
        setDeleteUserIntent();
        setCreateUserIntent();

        setupTimezoneSpinner();
        setupPermissionSpinner();
        setupUsernamesSpinner();

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
    }

    private void fillKnownValues() {
        // Set the simulation status
        statusField.setChecked(sharedPreferences.getBoolean(PREFERENCES_KEY_STATUS, false));

        // Set the known temperature
        temperatureField.setText(Integer.toString(sharedPreferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE)));

        // Set the know Time Zone
        String timeZone = sharedPreferences.getString(PREFERENCES_KEY_TIME_ZONE, "");
        String[] available = TimeZone.getAvailableIDs();
        for (int i = 0; i < available.length; i++) {
            if (available[i].equals(timeZone)) {
                timezoneSpinner.setSelection(i);
                break;
            }
        }
    }

    private void setSaveIntent() {
        saveContext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String timezone = timezoneSpinner.getSelectedItem().toString();

                int temperature = DEFAULT_TEMPERATURE;
                try {
                    temperature = Integer.parseInt(temperatureField.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                sharedPreferencesEditor.putBoolean(PREFERENCES_KEY_STATUS, statusField.isChecked());
                sharedPreferencesEditor.putInt(PREFERENCES_KEY_TEMPERATURE, temperature);
                sharedPreferencesEditor.putString(PREFERENCES_KEY_TIME_ZONE, timezone);

                sharedPreferencesEditor.apply();

                User editedUser = new User(
                        editedUsername.getText().toString(),
                        editedPassword.getText().toString(),
                        Permissions.toPermissions(editPermissionsSpinner.getSelectedItem().toString())
                );
                User oldUser = userbase.getUserFromUsername(usernameSpinner.getSelectedItem().toString());
                int feedback = editDashboardModel.editUser(editedUser, oldUser, context, userbase);
                Toast.makeText(context, feedback, Toast.LENGTH_LONG).show();

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
                            if (hasSelectedSelf(usernameToDelete)){
                                Toast.makeText(context, R.string.delete_logged_user_warning, Toast.LENGTH_LONG).show();
                            } else {
                                editDashboardModel.deleteUser(context, usernameToDelete,userbase);
                                Toast.makeText(context, R.string.delete_logged_user_success, Toast.LENGTH_LONG).show();
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
                String newUsername = newUsernameField.getText().toString();
                String newPassword = newPasswordField.getText().toString();
                Permissions newPermissions = Permissions.toPermissions(newPermissionsSpinner.getSelectedItem().toString());
                int feedback = editDashboardModel.addUser(context, new User(newUsername, newPassword, newPermissions), userbase);
                Toast.makeText(context, feedback, Toast.LENGTH_LONG).show();
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
    }

    private void setupUsernamesSpinner(){
        List<User> users = UserbaseHelper.loadUserbase(context).getUsers();
        List<String> usernames = users.stream().map(User::getUsername).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            usernames
        );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        usernameSpinner.setAdapter(adapter);
    }

    private boolean hasSelectedSelf(String usernameToDelete){
        String loggedUsername = sharedPreferences.getString("username", "username not found");
        return loggedUsername.equals(usernameToDelete);
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