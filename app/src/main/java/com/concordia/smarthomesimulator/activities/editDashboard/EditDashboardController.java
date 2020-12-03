package com.concordia.smarthomesimulator.activities.editDashboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.enums.Action;
import com.concordia.smarthomesimulator.enums.LogImportance;
import com.concordia.smarthomesimulator.enums.Permissions;
import com.concordia.smarthomesimulator.helpers.LogsHelper;
import com.concordia.smarthomesimulator.helpers.LayoutsHelper;
import com.concordia.smarthomesimulator.helpers.NotificationsHelper;
import com.concordia.smarthomesimulator.helpers.UserbaseHelper;
import com.concordia.smarthomesimulator.interfaces.IInhabitant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static android.view.View.inflate;
import static com.concordia.smarthomesimulator.Constants.*;

public class EditDashboardController extends AppCompatActivity {

    private Context context;
    private SharedPreferences preferences;

    private EditDashboardModel model;

    private LinearLayout awayErrorLayout;
    private ImageButton awayDisabledHint;
    private SwitchCompat awayStatusField;
    private SwitchCompat statusField;
    private FloatingActionButton saveContext;
    private FloatingActionButton timeScaleMinus;
    private FloatingActionButton timeScalePlus;
    private Button deleteUser;
    private Button createUserButton;
    private EditText temperatureField;
    private EditText editedUsername;
    private EditText editedPassword;
    private EditText newUsernameField;
    private EditText newPasswordField;
    private EditText dateField;
    private EditText timeField;
    private EditText callTimerField;
    private TextView awayStatusText;
    private TextView timeScaleField;
    private TextView statusText;
    private Spinner editPermissionsSpinner;
    private Spinner newPermissionsSpinner;
    private Spinner usernameSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_edit_dashboard);
        model = new ViewModelProvider(this).get(EditDashboardModel.class);

        preferences = getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

        model.initializeModel(context);
        model.updateSimulationDateTime(preferences);

        setupToolbar();
        findControls();

        setSaveIntent();
        setDeleteUserIntent();
        setCreateUserIntent();

        setupPermissionsSpinner();
        setupUsernamesSpinner();
        setupPermissionConfigurationRows();

        setupTimePicker();
        setupDatePicker();
        setupTimeFactor();

        setupUsernamesSpinner();

        fillKnownValues();

        // DO NOT PUT BEFORE "fillKnownValues" !
        setupStatusSwitch();
        setupAwaySwitch();
    }

    private void findControls() {
        awayStatusField = findViewById(R.id.away_on_off);
        awayStatusText = findViewById(R.id.away_on_off_text);
        awayDisabledHint = findViewById(R.id.away_disable_hint);
        awayErrorLayout = findViewById(R.id.away_disable_layout);
        callTimerField = findViewById(R.id.set_authorities_Timer);
        statusField = findViewById(R.id.on_off);
        statusText = findViewById(R.id.on_off_text);
        temperatureField = findViewById(R.id.set_temperature);
        saveContext = findViewById(R.id.save_context_button);
        editPermissionsSpinner = findViewById(R.id.edit_permissions_spinner);
        editedUsername = findViewById(R.id.edit_username_field);
        editedPassword = findViewById(R.id.edit_password_field);
        newPermissionsSpinner = findViewById(R.id.new_permissions_spinner);
        usernameSpinner = findViewById(R.id.username_spinner);
        deleteUser = findViewById(R.id.delete_button);
        createUserButton = findViewById(R.id.create_button);
        newUsernameField = findViewById(R.id.new_username_field);
        newPasswordField = findViewById(R.id.new_password_field);
        timeScaleMinus = findViewById(R.id.time_scale_minus);
        timeScalePlus = findViewById(R.id.time_scale_plus);
        timeScaleField = findViewById(R.id.time_scale_text);
        dateField = findViewById(R.id.date_selector);
        timeField = findViewById(R.id.time_selector);
    }

    private void fillKnownValues() {
        // Set the simulation status
        statusField.setChecked(preferences.getBoolean(PREFERENCES_KEY_STATUS, false));
        // Set the away mode
        awayStatusField.setChecked(preferences.getBoolean(PREFERENCES_KEY_AWAY_MODE, false));
        // Set call timer
        callTimerField.setText(Integer.toString(preferences.getInt(PREFERENCES_KEY_CALL_DELAY, DEFAULT_CALL_DELAY)));
        // Set the known temperature
        temperatureField.setText(Integer.toString(preferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_OUTSIDE_TEMPERATURE)));
        // Set the know Date Time
        LocalDateTime dateTime = model.getSimulationDateTime();
        dateField.setText(dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        timeField.setText(dateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        // Set the time scale factor
        timeScaleField.setText(model.getTimeFactor() + "x");
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
                int feedbackResource = model.editUser(context, model.getUserbase(), username, password, permissions, oldUsername);
                if (feedbackResource != -1) {
                    String message = getString(feedbackResource);
                    LogsHelper.add(context, new LogEntry("Edit Simulation Context", message, LogImportance.IMPORTANT));
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
                // Get the Simulation Context Parameters
                int temperature = DEFAULT_OUTSIDE_TEMPERATURE;
                try {
                    temperature = Integer.parseInt(temperatureField.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                LocalDate date =LocalDate.now();
                LocalTime time = LocalTime.now();
                try {
                    date = LocalDate.parse(dateField.getText().toString(), DateTimeFormatter.ofPattern(DATE_FORMAT));
                    time = LocalTime.parse(timeField.getText().toString(), DateTimeFormatter.ofPattern(TIME_FORMAT));
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
                int callTimer = DEFAULT_CALL_DELAY;
                try {
                    callTimer = Integer.parseInt(callTimerField.getText().toString());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                boolean status = statusField.isChecked();
                boolean away = awayStatusField.isChecked();
                // Edit the parameters
                model.editParameters(context, status, away, callTimer, temperature, date, time);
                // Edit the permissions configuration
                Userbase currentUserbase = UserbaseHelper.loadUserbase(context);
                // If the permissions were modified check that the user is allowed
                if (!model.getUserbase().getPermissionsConfiguration().equals(currentUserbase.getPermissionsConfiguration())
                    && UserbaseHelper.verifyPermissions(Action.CHANGE_PERMISSIONS_CONFIG, context)) {
                    model.getUserbase().setPermissionConfiguration(model.getUserbase().getPermissionsConfiguration());
                    model.getUserbase().getPermissionsConfiguration().sendToContext(preferences);
                }
                // Saving changes if the user has proper rights
                if (!model.getUserbase().equals(currentUserbase)
                        && UserbaseHelper.verifyPermissions(Action.MODIFY_USERBASE, context)) {
                    UserbaseHelper.saveUserbase(context, model.getUserbase());
                }
                // Send notification if required
                if (away && status && LayoutsHelper.getSelectedLayout(context).isIntruderDetected()) {
                    NotificationsHelper.sendIntruderNotification(context);
                }
                // Close the activity
                LogsHelper.add(context, new LogEntry("Edit Simulation Context", "Simulation Context Was Saved Successfully.", LogImportance.IMPORTANT));
                finish();
            }
        });
    }

    private void setDeleteUserIntent(){
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure the user has proper permissions
                if (!UserbaseHelper.verifyPermissions(Action.MODIFY_USERBASE, context)) {
                    return;
                }
                new AlertDialog.Builder(context)
                    .setTitle(getString(R.string.edit_text_delete_user_title))
                    .setMessage(getString(R.string.edit_text_delete_user))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final String usernameToDelete = usernameSpinner.getSelectedItem().toString();
                            if (model.hasSelectedSelf(preferences, usernameToDelete)){
                                Toast.makeText(context, R.string.delete_logged_user_warning, Toast.LENGTH_LONG).show();
                            } else {
                                model.deleteUser(context, model.getUserbase(), usernameToDelete);

                                String message = getString(R.string.delete_logged_user_success);
                                LogsHelper.add(context, new LogEntry("Edit Simulation Context", message, LogImportance.IMPORTANT));
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
                // Make sure the user has proper permissions
                if (!UserbaseHelper.verifyPermissions(Action.MODIFY_USERBASE, context)) {
                    return;
                }
                // Get the User's information
                String newUsername = newUsernameField.getText().toString();
                String newPassword = newPasswordField.getText().toString();
                // Validate values
                if (newUsername.trim().length() < 1 || newPassword.trim().length() < 1) {
                    Toast.makeText(context, context.getString(R.string.create_user_invalid), Toast.LENGTH_LONG).show();
                    return;
                }
                Permissions newPermissions = Permissions.fromString(newPermissionsSpinner.getSelectedItem().toString());
                // Add the new User
                int feedbackResource = model.addUser(context, model.getUserbase(), new User(newUsername, newPassword, newPermissions));
                String message = getString(feedbackResource);
                LogsHelper.add(context, new LogEntry("Edit Simulation Context", message, LogImportance.IMPORTANT));
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

    private void setupAwaySwitch() {
        setAwaySwitchRestrictions();
        setAwayStatus();
        awayStatusField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awayStatusField.isChecked()){
                    LogsHelper.add(context, new LogEntry("Away Mode", "Away mode is activated", LogImportance.IMPORTANT));
                }
                else{
                    LogsHelper.add(context, new LogEntry("Away Mode", "Away mode is deactivated", LogImportance.IMPORTANT));
                }
                setAwaySwitchRestrictions();
                setAwayStatus();
            }
        });
    }

    private void setAwaySwitchRestrictions(){
        String error = "";
        if(LayoutsHelper.getSelectedLayout(context) == null){
            awayStatusField.setAlpha(.5f);
            awayStatusField.setChecked(false);
            awayStatusField.setEnabled(false);
            awayErrorLayout.setVisibility(View.VISIBLE);
            error = getString(R.string.missing_house_layout);
        }
        else if(!isHouseEmpty()){
            awayStatusField.setAlpha(.5f);
            awayStatusField.setChecked(false);
            awayStatusField.setEnabled(false);
            awayErrorLayout.setVisibility(View.VISIBLE);
            error = getString(R.string.away_disable_message);
        }
        else{
            awayStatusField.setAlpha(1.0f);
            awayStatusField.setEnabled(true);
            awayErrorLayout.setVisibility(View.GONE);
        }
        String finalError = error;
        awayDisabledHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(getString(R.string.away_message_title))
                    .setMessage(finalError)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
                dialog.show();
            }
        });
    }

    private boolean isHouseEmpty() {
        HouseLayout houseLayout = LayoutsHelper.getSelectedLayout(context);

        if (houseLayout != null) {
            ArrayList<IInhabitant> inhabitants = LayoutsHelper.getSelectedLayout(context).getAllInhabitants();
            return inhabitants.stream().allMatch(IInhabitant::isIntruder);
        }
        else{
            return false;
        }
    }

    private void setAwayStatus(){
        if (awayStatusField.isChecked()) {
            awayStatusText.setText(getString(R.string.away_mode_on));
            awayStatusText.setTextColor(getColor(R.color.primary));
        } else {
            awayStatusText.setText(getString(R.string.away_mode_off));
            awayStatusText.setTextColor(getColor(R.color.charcoal));
        }
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

    private void setupDatePicker() {
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDateTime timeNow = model.getSimulationDateTime();
                // IMPORTANT: For some reason the month is shifted by 1 in the picker ?
                // This is why we set the month to be -1 when we initialize the control
                // and why we set the simulation date to be +1
                DatePickerDialog timePickerDialog = new DatePickerDialog(
                    context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            model.setSimulationDate(year, month + 1, dayOfMonth);
                            fillKnownValues();
                        }
                    },
                    timeNow.getYear(),
                    timeNow.getMonthValue() - 1,
                    timeNow.getDayOfMonth()
                );
                timePickerDialog.show();
            }
        });
    }

    private void setupTimePicker() {
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDateTime timeNow = model.getSimulationDateTime();
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                model.setSimulationTime(hourOfDay, minute);
                                fillKnownValues();
                            }
                        },
                        timeNow.getHour(),
                        timeNow.getMinute(),
                        false
                );
                timePickerDialog.show();
            }
        });
    }

    private void setupTimeFactor() {
        model.setTimeFactor(preferences.getFloat(PREFERENCES_KEY_TIME_SCALE, DEFAULT_TIME_SCALE));
        timeScaleMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.decreaseTimeFactor();
                fillKnownValues();
            }
        });
        timeScalePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.increaseTimeFactor();
                fillKnownValues();
            }
        });
    }

    private void setupPermissionsSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            getResources().getStringArray(R.array.permissions_spinner)
        );
        editPermissionsSpinner.setAdapter(adapter);
        newPermissionsSpinner.setAdapter(adapter);
    }

    private void setupUsernamesSpinner(){
        List<String> users = model.getUserbase().getUsernames();
        Collections.sort(users);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            users
        );
        usernameSpinner.setAdapter(adapter);
        // Set known information about the selected User to Edit
        setUserInformation(model.getUserbase().getUserFromUsername(users.get(0)));
        usernameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setUserInformation(model.getUserbase().getUserFromUsername(users.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setupPermissionConfigurationRows() {
        LinearLayout layout = findViewById(R.id.permissions_configuration_table);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.support_simple_spinner_dropdown_item,
            getResources().getStringArray(R.array.permissions_spinner)
        );
        // Add children rows
        for(Map.Entry<Action, Permissions> entry : model.getUserbase().getPermissionsConfiguration().getActionPermissionsMap().entrySet()) {
           View child = inflate(context, R.layout.adapter_permissions_row, null);

           TextView actionName = child.findViewById(R.id.permission_action_name);
           actionName.setText(entry.getKey().getDescription().replace("_", " "));

           Spinner permissionsSpinner = child.findViewById(R.id.permission_level_spinner);
           permissionsSpinner.setAdapter(adapter);

           setupPermissionsSpinnerSelection(entry.getValue(), permissionsSpinner);
           permissionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 model.editPermissionsConfiguration(Permissions.fromPosition(position), entry.getKey());
               }
               @Override
               public void onNothingSelected(AdapterView<?> parent) { }
           });

           layout.addView(child);
        }

    }

    private void setupPermissionsSpinnerSelection(Permissions permission, Spinner permissionsSpinner){
        switch (permission) {
            case PARENT:
                permissionsSpinner.setSelection(0);
                break;
            case CHILD:
                permissionsSpinner.setSelection(1);
                break;
            case GUEST:
                permissionsSpinner.setSelection(2);
                break;
            default:
                permissionsSpinner.setSelection(3);
                break;
        }
    }

    private void setUserInformation(User user) {
        editedUsername.setText(user.getUsername());
        editedPassword.setText(user.getPassword());
        setupPermissionsSpinnerSelection(user.getPermission(), editPermissionsSpinner);
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