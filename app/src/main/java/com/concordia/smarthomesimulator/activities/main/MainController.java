package com.concordia.smarthomesimulator.activities.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.about.AboutController;
import com.concordia.smarthomesimulator.activities.login.LoginController;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.LogImportance;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;
import com.google.android.material.navigation.NavigationView;

import static com.concordia.smarthomesimulator.Constants.*;

public class MainController extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Context context;
    SharedPreferences sharedPreferences;

    // The Main Model instantiates some fragments inside the content view
    // to allow for dynamic content to be set by the navigation drawer.
    // This is therefore the "parent" controller that controls the fragment
    // views, which are themselves controlled by the "children" controllers
    // present in the appropriate fragments sub-folders.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize to the home screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getColor(R.color.charcoal));
        setSupportActionBar(toolbar);

        //Setup the navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // Menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_map, R.id.nav_logs)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Manage "back" navigation button logic.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Add appropriate logic for the various Action Bar menu items.
        switch (item.getItemId()) {
            case R.id.action_about:
                ActivityLogHelper.add(context, new LogEntry("About","Opened the About Page.", LogImportance.MINOR));
                MainController.this.startActivity(new Intent(MainController.this, AboutController.class));
                return true;
            case R.id.action_logout:
                // Remove Logged In User Information
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(PREFERENCES_KEY_USERNAME);
                editor.remove(PREFERENCES_KEY_PASSWORD);
                editor.remove(PREFERENCES_KEY_PERMISSIONS);
                editor.commit();
                // Redirect to the Login Screen
                ActivityLogHelper.add(context, new LogEntry("Exit","User logged out.", LogImportance.IMPORTANT));
                MainController.this.startActivity(new Intent(MainController.this, LoginController.class));
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION_REQUEST_CODE:
                if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, R.string.toast_allow_write_permissions, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, R.string.toast_write_permissions, Toast.LENGTH_LONG).show();
                break;
            case READ_PERMISSION_REQUEST_CODE:
                if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, R.string.toast_allow_read_permissions, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, R.string.toast_read_permissions, Toast.LENGTH_LONG).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}