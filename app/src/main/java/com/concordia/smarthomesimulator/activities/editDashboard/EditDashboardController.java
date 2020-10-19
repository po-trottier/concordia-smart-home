package com.concordia.smarthomesimulator.activities.editDashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;

import java.util.List;

public class EditDashboardController extends AppCompatActivity {

    private Context context;
    private EditDashboardModel editDashboardModel;
    private SharedPreferences sharedPreferences;
    private Userbase userbase;
    private Spinner permissionsSpinner;
    private Spinner usernameSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dashboard);
        editDashboardModel = new ViewModelProvider(this).get(EditDashboardModel.class);
        context = this;
        sharedPreferences = this.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        userbase = new Userbase(context);
        setupToolbar();
        setupPermissionSpinner();
        setupUsernamesSpinner();

        //simulation context

        // --- USERS ---
        setDeleteUserIntent();
        setEditUserIntent();
        setCreateUserIntent();

    }

    private void setDeleteUserIntent(){
        final Button deleteUserButton = findViewById(R.id.delete_button);

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameToDelete = usernameSpinner.getSelectedItem().toString();
                //making sure the user doesn't delete itself
                if (hasSelectedSelf(usernameToDelete)){
                    Toast.makeText(context, R.string.delete_logged_user_warning, Toast.LENGTH_LONG).show();
                } else {
                    userbase.deleteUserFromUsernameIfPossible(usernameToDelete, context);
                    resetActivity();
                }
            }
        });
    }

    private void setCreateUserIntent(){
        final Button createUserButton = findViewById(R.id.create_button);

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User userToAdd = getUserFromTextFields();
                if (!userToAdd.getUsername().isEmpty() && !userToAdd.getPassword().isEmpty()) {
                    int feedback = editDashboardModel.addUser(context, userToAdd, userbase);
                    Toast.makeText(context, feedback, Toast.LENGTH_LONG).show();
                    resetActivity();
                } else {
                    Toast.makeText(context, R.string.create_user_missing_fields, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setEditUserIntent(){
        final Button editUserButton = findViewById(R.id.edit_button);

        editUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User editedUser = getUserFromTextFields();
                final String spinnerUsername = usernameSpinner.getSelectedItem().toString();

                if (!editedUser.getUsername().isEmpty() || !editedUser.getPassword().isEmpty()) {
                    int feedback = editDashboardModel.editUser(editedUser, spinnerUsername, context, userbase);
                    Toast.makeText(context, feedback, Toast.LENGTH_LONG).show();
                    resetActivity();
                } else {
                    Toast.makeText(context, R.string.edit_user_missing_field, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private User getUserFromTextFields(){
        // fetch the username from the UI then all needed information from the userbase
        final EditText usernameField = findViewById(R.id.newUsernameField);
        final EditText passwordField = findViewById(R.id.newPasswordField);
        String permissionsString = permissionsSpinner.getSelectedItem().toString();
        Permissions permissions = Permissions.toPermissions(permissionsString);
        return new User(usernameField.getText().toString(), passwordField.getText().toString(), permissions);
    }

    private boolean hasSelectedSelf(String usernameToDelete){
        String loggedUsername = sharedPreferences.getString("username", "username not found");
        return loggedUsername.equals(usernameToDelete);
    }

    private void resetActivity(){
        //resetting the activity, called when spinners must be updated
        Intent intent = new Intent(EditDashboardController.this, EditDashboardController.class);
        EditDashboardController.this.startActivity(intent);
        finish();
    }

    private void setupPermissionSpinner(){
        permissionsSpinner = (Spinner) findViewById(R.id.permissions_spinner);

        ArrayAdapter<String> permissionsAdapter = new ArrayAdapter<>(context,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.permissions_spinner));
        permissionsSpinner.setAdapter(permissionsAdapter);
    }

    private void setupUsernamesSpinner(){
        usernameSpinner = (Spinner) findViewById(R.id.username_spinner);
        List<String> usernames = userbase.getUsernames();

        ArrayAdapter<String> permissionsAdapter = new ArrayAdapter<>(context,
                R.layout.support_simple_spinner_dropdown_item, usernames);
        usernameSpinner.setAdapter(permissionsAdapter);
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