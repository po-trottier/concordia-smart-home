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
import com.concordia.smarthomesimulator.exceptions.PermissionNotFoundException;
import com.concordia.smarthomesimulator.helpers.*;
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
    private EditText summerTemperatureField;
    private EditText winterTemperatureField;
    private EditText maxAlertTemperatureField;
    private EditText minAlertTemperatureField;
    private EditText editedUsername;
    private EditText editedPassword;
    private EditText newUsernameField;
    private EditText newPasswordField;
    private EditText callTimerField;
    private TextView dateField;
    private TextView timeField;
    private TextView awayStatusText;
    private TextView timeScaleField;
    private TextView statusText;
    private TextView minLightsTimeField;
    private TextView maxLightsTimeField;
    private Spinner editPermissionsSpinner;
    private Spinner newPermissionsSpinner;
    private Spinner usernameSpinner;
    private Spinner winterStartSpinner;
    private Spinner winterEndSpinner;
    private Spinner summerStartSpinner;
    private Spinner summerEndSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_edit_dashboard);
        model = new ViewModelProvider(this).get(EditDashboardModel.class);

        preferences = getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

        model.initializeModel(context);
        model.updateSimulationDateTime(preferences);
        model.updateMinLightsTime(preferences);
        model.updateMaxLightsTime(preferences);

        setupToolbar();
        findControls();

        setSaveIntent();
        setDeleteUserIntent();
        setCreateUserIntent();

        setupPermissionsSpinner();
        setupUsernamesSpinner();
        setupSeasonsSpinner();
        setupPermissionConfigurationRows();

        setupTimePicker();
        setupDatePicker();
        setupTimeFactor();
        setupMinLightsTimePicker();
        setupMaxLightsTimePicker();

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
        summerTemperatureField = findViewById(R.id.set_summer_temperature);
        winterTemperatureField = findViewById(R.id.set_winter_temperature);
        maxAlertTemperatureField = findViewById(R.id.set_max_temperature_alert);
        minAlertTemperatureField = findViewById(R.id.set_min_temperature_alert);
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
        minLightsTimeField = findViewById(R.id.set_time_min);
        maxLightsTimeField = findViewById(R.id.set_time_max);
        winterStartSpinner = findViewById(R.id.winter_start_spinner);
        winterEndSpinner = findViewById(R.id.winter_end_spinner);
        summerStartSpinner = findViewById(R.id.summer_start_spinner);
        summerEndSpinner = findViewById(R.id.summer_end_spinner);
    }

    private void fillKnownValues() {
        // Set the simulation status
        statusField.setChecked(preferences.getBoolean(PREFERENCES_KEY_STATUS, false));
        // Set the away mode
        awayStatusField.setChecked(preferences.getBoolean(PREFERENCES_KEY_AWAY_MODE, false));
        // Set call timer
        callTimerField.setText(Integer.toString(preferences.getInt(PREFERENCES_KEY_CALL_DELAY, DEFAULT_CALL_DELAY)));
        // Set the known temperature
        temperatureField.setText(Integer.toString(preferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE)));
        //Set desired temperature for away mode during the summer
        summerTemperatureField.setText(Integer.toString(preferences.getInt(PREFERENCES_KEY_SUMMER_TEMPERATURE, DEFAULT_SUMMER_TEMPERATURE)));
        //Set desired temperature for away mode during the winter
        winterTemperatureField.setText(Integer.toString(preferences.getInt(PREFERENCES_KEY_WINTER_TEMPERATURE, DEFAULT_WINTER_TEMPERATURE)));
        //Set maximum alert temperature
        maxAlertTemperatureField.setText(Integer.toString(preferences.getInt(PREFERENCES_KEY_MAX_TEMPERATURE_ALERT, DEFAULT_MAX_TEMPERATURE_ALERT)));
        //Set minimum alert temperature
        minAlertTemperatureField.setText(Integer.toString(preferences.getInt(PREFERENCES_KEY_MIN_TEMPERATURE_ALERT, DEFAULT_MIN_TEMPERATURE_ALERT)));
        // Set the know Date Time
        LocalDateTime dateTime = model.getSimulationDateTime();
        dateField.setText(dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        timeField.setText(dateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        // Set the time scale factor
        timeScaleField.setText(model.getTimeFactor() + "x");
        // Set the min and max times
        LocalTime minTime = model.getMinLightsTime();
        LocalTime maxTime = model.getMaxLightsTime();
        minLightsTimeField.setText(minTime.format((DateTimeFormatter.ofPattern(TIME_FORMAT))));
        maxLightsTimeField.setText(maxTime.format((DateTimeFormatter.ofPattern(TIME_FORMAT))));
    }

    private void setSaveIntent() {
        saveContext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // REFACTOR: Created a parameter object instead of passing all the parameters separately allowed
                // us to split the previously extremely long method call to a short one. This also makes it clear
                // when parameters are being modified.
                ParametersArgument parameters = new ParametersArgument();
                // Get the User's information
                String username = editedUsername.getText().toString();
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
                // Get the numbers
                int temperature = DEFAULT_TEMPERATURE;
                int summerTemperature = DEFAULT_SUMMER_TEMPERATURE;
                int winterTemperature = DEFAULT_WINTER_TEMPERATURE;
                int callTimer = DEFAULT_CALL_DELAY;
                int maxAlertTemperature = DEFAULT_MAX_TEMPERATURE_ALERT;
                int minAlertTemperature = DEFAULT_MIN_TEMPERATURE_ALERT;
                try {
                    temperature = Integer.parseInt(temperatureField.getText().toString());
                    summerTemperature = Integer.parseInt(summerTemperatureField.getText().toString());
                    winterTemperature = Integer.parseInt(winterTemperatureField.getText().toString());
                    maxAlertTemperature = Integer.parseInt(maxAlertTemperatureField.getText().toString());
                    minAlertTemperature = Integer.parseInt(minAlertTemperatureField.getText().toString());
                    if (Math.abs(temperature) > MAXIMUM_TEMPERATURE){
                        temperature = DEFAULT_TEMPERATURE;
                        callTimer = Integer.parseInt(callTimerField.getText().toString());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                parameters.setTemperature(temperature);
                parameters.setSummerTemperature(summerTemperature);
                parameters.setWinterTemperature(winterTemperature);
                parameters.setMinAlertTemperature(Math.min(minAlertTemperature, maxAlertTemperature));
                parameters.setMaxAlertTemperature(Math.max(minAlertTemperature, maxAlertTemperature));
                parameters.setCallTimer(callTimer);
                // Get the times and dates
                LocalDate date =LocalDate.now();
                LocalTime time = LocalTime.now();
                LocalTime minLightsTime = DEFAULT_MIN_LIGHTS_TIME;
                LocalTime maxLightsTime = DEFAULT_MAX_LIGHTS_TIME;
                try {
                    date = LocalDate.parse(dateField.getText().toString(), DateTimeFormatter.ofPattern(DATE_FORMAT));
                    time = LocalTime.parse(timeField.getText().toString(), DateTimeFormatter.ofPattern(TIME_FORMAT));
                    minLightsTime = LocalTime.parse(minLightsTimeField.getText().toString(), DateTimeFormatter.ofPattern(TIME_FORMAT));
                    maxLightsTime = LocalTime.parse(maxLightsTimeField.getText().toString(), DateTimeFormatter.ofPattern(TIME_FORMAT));
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
                parameters.setDate(date);
                parameters.setTime(time);
                parameters.setMinLightsTime(minLightsTime);
                parameters.setMaxLightsTime(maxLightsTime);
                // Get the booleans
                parameters.setStatus(statusField.isChecked());
                parameters.setAwayMode(awayStatusField.isChecked());
                // Getting the spinners
                int winterStart = winterStartSpinner.getSelectedItemPosition() + 1;
                int winterEnd = winterEndSpinner.getSelectedItemPosition() + 1;
                int summerStart = summerStartSpinner.getSelectedItemPosition() + 1;
                int summerEnd = summerEndSpinner.getSelectedItemPosition() + 1;
                parameters.setWinterStart(winterStart);
                parameters.setWinterEnd(winterEnd);
                parameters.setSummerStart(summerStart);
                parameters.setSummerEnd(summerEnd);
                // Edit the parameters
                model.editParameters(context, parameters);
                // Update temperature behaviour of the rooms
                if (parameters.getStatus()) {
                    TemperatureHelper.adjustTemperature(context);
                }
                // Edit the permissions configuration
                Userbase currentUserbase = UserbaseHelper.loadUserbase(context);
                // If the permissions were modified check that the user is allowed
                try {
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
                } catch (PermissionNotFoundException ignore) {
                    Toast.makeText(context, context.getString(R.string.permission_error), Toast.LENGTH_SHORT).show();
                }
                // If simulation is running and away mode is on
                if (parameters.isAwayMode() && parameters.getStatus()) {
                    // Send notification if required
                    if (LayoutsHelper.getSelectedLayout(context).isIntruderDetected()) {
                        NotificationsHelper.sendIntruderNotification(context);
                    }
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
                try {
                    if (!UserbaseHelper.verifyPermissions(Action.MODIFY_USERBASE, context)) {
                        return;
                    }
                } catch (PermissionNotFoundException ignore) {
                    Toast.makeText(context, context.getString(R.string.permission_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                new AlertDialog.Builder(context)
                    .setTitle(getString(R.string.edit_text_delete_user_title))
                    .setMessage(getString(R.string.edit_text_delete_user))
                    .setNegativeButton(android.R.string.cancel, null)
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
                try {
                    if (!UserbaseHelper.verifyPermissions(Action.MODIFY_USERBASE, context)) {
                        return;
                    }
                } catch (PermissionNotFoundException ignore) {
                    Toast.makeText(context, context.getString(R.string.permission_error), Toast.LENGTH_SHORT).show();
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
                if (awayStatusField.isChecked()) {
                    LogsHelper.add(context, new LogEntry("Away Mode", "Away mode is activated", LogImportance.IMPORTANT));
                } else {
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
        else if(!model.isHouseEmpty(context)){
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

    private void setupMinLightsTimePicker() {
        minLightsTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalTime minTimeNow = model.getMinLightsTime();
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            model.setMinLightsTime(hourOfDay, minute);
                            fillKnownValues();
                        }
                    },
                    minTimeNow.getHour(),
                    minTimeNow.getMinute(),
                    false
                );
                timePickerDialog.show();
            }
        });
    }

    private void setupMaxLightsTimePicker() {
        maxLightsTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalTime maxTimeNow = model.getMaxLightsTime();
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            model.setMaxLightsTime(hourOfDay, minute);
                            fillKnownValues();
                        }
                    },
                    maxTimeNow.getHour(),
                    maxTimeNow.getMinute(),
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

    private void setupSeasonsSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.months_spinner)
        );
        // Set the adapters
        winterStartSpinner.setAdapter(adapter);
        winterEndSpinner.setAdapter(adapter);
        summerStartSpinner.setAdapter(adapter);
        summerEndSpinner.setAdapter(adapter);
        // Set the initial values
        winterStartSpinner.setSelection(preferences.getInt(PREFERENCES_KEY_WINTER_START, DEFAULT_WINTER_START) - 1);
        winterEndSpinner.setSelection(preferences.getInt(PREFERENCES_KEY_WINTER_END, DEFAULT_WINTER_END) - 1);
        summerStartSpinner.setSelection(preferences.getInt(PREFERENCES_KEY_SUMMER_START, DEFAULT_SUMMER_START) - 1);
        summerEndSpinner.setSelection(preferences.getInt(PREFERENCES_KEY_SUMMER_END, DEFAULT_SUMMER_END) - 1);
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