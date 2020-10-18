package com.concordia.smarthomesimulator.activities.editDashboard;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;

import java.util.LinkedList;
import java.util.List;

public class EditDashboardController extends AppCompatActivity {

    private Context context;
    private EditDashboardModel editDashboardModel;
    private Userbase userbase;
    Spinner permissions_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard);
        editDashboardModel = new ViewModelProvider(this).get(EditDashboardModel.class);
        context = EditDashboardController.this;
        setupToolbar();
        setupPermissionSpinner();

        //simulation context

        //users
        setDeleteUserIntent();
        setCreateUserIntent();

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
        permissions_spinner = (Spinner) findViewById(R.id.permissions_spinner);
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