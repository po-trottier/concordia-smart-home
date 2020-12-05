package com.concordia.smarthomesimulator.fragments.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.editDashboard.EditDashboardController;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.enums.LogImportance;
import com.concordia.smarthomesimulator.helpers.LayoutsHelper;
import com.concordia.smarthomesimulator.helpers.LogsHelper;
import com.concordia.smarthomesimulator.helpers.NotificationsHelper;
import com.concordia.smarthomesimulator.interfaces.OnIndoorTemperatureChangeListener;
import com.concordia.smarthomesimulator.interfaces.OnIntruderDetectedListener;
import com.concordia.smarthomesimulator.singletons.LayoutSingleton;
import com.concordia.smarthomesimulator.views.customDateTimeView.CustomDateTimeView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_MAX_TEMPERATURE_ALERT;
import static com.concordia.smarthomesimulator.Constants.DEFAULT_MIN_TEMPERATURE_ALERT;
import static com.concordia.smarthomesimulator.Constants.PREFERENCES_KEY_AWAY_MODE;
import static com.concordia.smarthomesimulator.Constants.PREFERENCES_KEY_MAX_TEMPERATURE_ALERT;
import static com.concordia.smarthomesimulator.Constants.PREFERENCES_KEY_MIN_TEMPERATURE_ALERT;
import static com.concordia.smarthomesimulator.Constants.PREFERENCES_KEY_STATUS;

public class DashboardController extends Fragment {

    private DashboardModel dashboardModel;
    private View view;
    private Context context;
    private SharedPreferences preferences;
    private TextView status;
    private TextView temperature;
    private TextView user;
    private TextView permissions;
    private CustomDateTimeView clock;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardModel = new ViewModelProvider(this).get(DashboardModel.class);
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = getActivity();
        preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

        findControls();
        setKnownValues();
        setupEditIntent();
        setupNotifications();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        setKnownValues();
    }

    private void findControls() {
        status = view.findViewById(R.id.simulation_status);
        temperature = view.findViewById(R.id.simulation_temperature);
        clock = view.findViewById(R.id.dashboard_clock);
        user = view.findViewById(R.id.text_user_username);
        permissions = view.findViewById(R.id.text_user_permissions);
    }

    private void setKnownValues() {
        // Set the Permissions
        permissions.setText(dashboardModel.getPermissions(context, preferences));
        // Set the Username
        user.setText(dashboardModel.getUsername(preferences));
        // Set the Temperature
        temperature.setText(dashboardModel.getTemperature(context, preferences));
        // Set the Simulation Status
        status.setText(dashboardModel.getStatusText(context, preferences));
        status.setTextColor(dashboardModel.getStatusColor(context, preferences));
        // Set the Date Time
        clock.setDateTime(dashboardModel.getDateTime(preferences));
    }

    private void setupEditIntent() {
        FloatingActionButton fab = view.findViewById(R.id.fab_edit_dashboard);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //proceeding to the next activity and logging what happened
                LogsHelper.add(context, new LogEntry("Dashboard","Started Editing the Simulation Context.", LogImportance.MINOR));
                Intent intent = new Intent(context, EditDashboardController.class);
                context.startActivity(intent);
            }
        });
    }

    private void setupNotifications() {
        NotificationsHelper.createNotificationChannel(context);
        LayoutSingleton layoutInstance = LayoutSingleton.getInstance();
        layoutInstance.setOnIntruderDetectedListener(new OnIntruderDetectedListener() {
            @Override
            public void onIntruderDetected() {
                boolean away = preferences.getBoolean(PREFERENCES_KEY_AWAY_MODE, false);
                boolean status = preferences.getBoolean(PREFERENCES_KEY_STATUS, false);
                if (away && status) {
                    NotificationsHelper.sendIntruderNotification(context);
                }
            }
        });

        layoutInstance.setOnIndoorTemperatureChangeListener(new OnIndoorTemperatureChangeListener() {
            @Override
            public void OnIndoorTemperatureChange() {
                int maxTemperature = preferences.getInt(PREFERENCES_KEY_MAX_TEMPERATURE_ALERT, DEFAULT_MAX_TEMPERATURE_ALERT);
                int minTemperature = preferences.getInt(PREFERENCES_KEY_MIN_TEMPERATURE_ALERT, DEFAULT_MIN_TEMPERATURE_ALERT);
                String temperatureAlertTitle;
                String temperatureAlertText;
                String roomName;

                for (Room room: LayoutsHelper.getSelectedLayout(context).getRooms()) {
                    if(room.getActualTemperature() > maxTemperature){
                        temperatureAlertTitle = context.getString(R.string.max_temperature_alert_title);
                        temperatureAlertText = context.getString(R.string.max_temperature_alert_text);
                        roomName = room.getName();
                        NotificationsHelper.sendTemperatureAlertNotification(context,temperatureAlertTitle,temperatureAlertText, roomName);
                    }
                    else if(room.getActualTemperature() < minTemperature){
                        temperatureAlertTitle = context.getString(R.string.min_temperature_alert_title);
                        temperatureAlertText = context.getString(R.string.min_temperature_alert_text);
                        roomName = room.getName();
                        NotificationsHelper.sendTemperatureAlertNotification(context,temperatureAlertTitle,temperatureAlertText, roomName);
                    }
                }
            }
        });
    }
}