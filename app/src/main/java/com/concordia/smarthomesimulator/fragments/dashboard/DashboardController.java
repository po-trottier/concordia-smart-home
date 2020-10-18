package com.concordia.smarthomesimulator.fragments.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.editDashboard.EditDashboardController;
import com.concordia.smarthomesimulator.activities.login.LoginController;
import com.concordia.smarthomesimulator.activities.main.MainController;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.LogImportance;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardController extends Fragment {

    private DashboardModel dashboardModel;
    private View view;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardModel = new ViewModelProvider(this).get(DashboardModel.class);
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = getActivity();

        final Switch sw = view.findViewById(R.id.onoff);
        sw.setText(dashboardModel.getSw());

        final ImageView img = view.findViewById(R.id.image);

        final TextView user = view.findViewById(R.id.text_user);
        user.setText(dashboardModel.getUser());

        final TextView location = view.findViewById(R.id.location);
        location.setText(dashboardModel.getLocation());

        final TextView room = view.findViewById(R.id.room);
        room.setText(dashboardModel.getRoom());

        final TextView temperature = view.findViewById(R.id.temperature);
        temperature.setText(dashboardModel.getTemperature());

        final TextView date = view.findViewById(R.id.date);
        date.setText(dashboardModel.getDate());

        final TextView time = view.findViewById(R.id.time);
        time.setText(dashboardModel.getTime());

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