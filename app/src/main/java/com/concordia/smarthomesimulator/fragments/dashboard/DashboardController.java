package com.concordia.smarthomesimulator.fragments.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.editDashboard.EditDashboardController;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.LogImportance;
import com.concordia.smarthomesimulator.dataModels.Permissions;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.concordia.smarthomesimulator.Constants.*;

public class DashboardController extends Fragment {

    private DashboardModel dashboardModel;
    private View view;
    private Context context;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardModel = new ViewModelProvider(this).get(DashboardModel.class);
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = getActivity();
        sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

        final TextView user = view.findViewById(R.id.text_user_username);
        user.setText(sharedPreferences.getString(PREFERENCES_KEY_USERNAME, ""));

        final TextView permissions = view.findViewById(R.id.text_user_permissions);
        String permissionValue;
        switch (sharedPreferences.getInt(PREFERENCES_KEY_PERMISSIONS, 1)) {
            case 15:
                permissionValue = getResources().getStringArray(R.array.permissions_spinner)[0];
                break;
            case 7:
                permissionValue = getResources().getStringArray(R.array.permissions_spinner)[1];
                break;
            case 3:
                permissionValue = getResources().getStringArray(R.array.permissions_spinner)[2];
                break;
            default:
                permissionValue = getResources().getStringArray(R.array.permissions_spinner)[3];
                break;
        }
        permissions.setText(permissionValue);

        final TextView status = view.findViewById(R.id.simulation_status);
        boolean statusValue = sharedPreferences.getBoolean(PREFERENCES_KEY_STATUS, false);
        status.setText(statusValue ? getString(R.string.simulation_status_started) : getString(R.string.simulation_status_stopped));
        status.setTextColor(statusValue ? context.getColor(R.color.primary) : context.getColor(R.color.charcoal));

        final TextView temperature = view.findViewById(R.id.simulation_temperature);
        String tempString = Integer.toString(sharedPreferences.getInt(PREFERENCES_KEY_TEMPERATURE, DEFAULT_TEMPERATURE));
        temperature.setText(tempString + getString(R.string.degrees_celsius));

        final TextView date = view.findViewById(R.id.date);
        date.setText(dashboardModel.getDate());

        final TextClock clock = view.findViewById(R.id.dashboard_clock);
        clock.setTimeZone(sharedPreferences.getString(PREFERENCES_KEY_TIME_ZONE, DEFAULT_TIME_ZONE));

         setupEditIntent();

        return view;
    }

    private void setupEditIntent() {
        FloatingActionButton fab = view.findViewById(R.id.fab_edit_dashboard);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //proceeding to the next activity and logging what happened
                Intent intent = new Intent(context, EditDashboardController.class);
                ActivityLogHelper.add(context, new LogEntry("Dashboard","Starting EditDashboard", LogImportance.MINOR));
                context.startActivity(intent);
            }
        });
    }

}