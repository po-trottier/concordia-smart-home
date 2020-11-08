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
import com.concordia.smarthomesimulator.dataModels.AwayModeEntry;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.IDevice;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.LogImportance;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;
import com.concordia.smarthomesimulator.helpers.HouseLayoutHelper;
import com.concordia.smarthomesimulator.helpers.ObserverHelper;
import com.concordia.smarthomesimulator.interfaces.IObserver;
import com.concordia.smarthomesimulator.views.customDateTimeView.CustomDateTimeView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardController extends Fragment  implements IObserver {

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

        ObserverHelper.addObserver(this);

        findControls();
        setKnownValues();
        setupEditIntent();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        setKnownValues();
    }

    @Override
    public void updateAwayMode(boolean awayMode, String callTimer) {
        if(awayMode){
            HouseLayout updatedHouseLayout =  HouseLayoutHelper.getSelectedLayout(context);
            updatedHouseLayout.setAwayModeEntry(new AwayModeEntry(awayMode,callTimer));
            for(Room room : updatedHouseLayout.getRooms()){
                for(IDevice device : room.getDevices()){
                    device.setIsOpened(false);
                }
            }
            HouseLayoutHelper.updateSelectedLayout(context, updatedHouseLayout);
        }
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
                ActivityLogHelper.add(context, new LogEntry("Dashboard","Started Editing the Simulation Context.", LogImportance.MINOR));
                Intent intent = new Intent(context, EditDashboardController.class);
                context.startActivity(intent);
            }
        });
    }

}