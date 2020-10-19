package com.concordia.smarthomesimulator.fragments.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextClock;
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

import java.util.TimeZone;

public class DashboardController extends Fragment {

    private DashboardModel dashboardModel;
    private View view;
    private Context context;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardModel = new ViewModelProvider(this).get(DashboardModel.class);
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = getActivity();
        sharedPreferences = getActivity().getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

        final ImageView usersImage = view.findViewById(R.id.image_dashboard);
        usersImage.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_account_box_24));

        final TextView user = view.findViewById(R.id.text_user);
        user.setText(dashboardModel.getUser());

        final TextView temperature = view.findViewById(R.id.temperature);
        temperature.setText(dashboardModel.getTemperature());

        final TextView date = view.findViewById(R.id.date);
        date.setText(dashboardModel.getDate());

        final TextClock clock = view.findViewById(R.id.dashboard_clock);
        clock.setTimeZone(TimeZone.getTimeZone("America/New_York").toString());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeZone", clock.getTimeZone());
        editor.apply();

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