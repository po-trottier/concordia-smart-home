package com.concordia.smarthomesimulator.activities.editMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.LogImportance;
import com.concordia.smarthomesimulator.fragments.map.MapModel;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;

import static com.concordia.smarthomesimulator.Constants.RECEIVE_IMAGE_REQUEST_CODE;

public class EditMapController extends AppCompatActivity {

    private MapModel mapModel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_map);
        context = this;

        mapModel = new ViewModelProvider(this).get(MapModel.class);

        setupToolbar();
        setPhotoPickerIntent(this);
    }

    private void setupToolbar() {
        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_secondary);
        toolbar.setTitle(getString(R.string.title_activity_edit_map));
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

    private void setPhotoPickerIntent(Activity context) {
        Button button = context.findViewById(R.id.upload_map_image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLogHelper.add(context, new LogEntry("Edit House Layout", "User is choosing a new layout image.", LogImportance.IMPORTANT));
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RECEIVE_IMAGE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        switch (reqCode) {
            case RECEIVE_IMAGE_REQUEST_CODE:
                if (resultCode != RESULT_OK)
                    return;
                if (mapModel.encodeAndSaveImage(context, data.getData())) {
                    String message = getString(R.string.edit_layout_upload_success);
                    ActivityLogHelper.add(context, new LogEntry("Edit House Layout", message, LogImportance.IMPORTANT));
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
                else {
                    String message = getString(R.string.edit_layout_upload_error);
                    ActivityLogHelper.add(context, new LogEntry("Edit House Layout", message, LogImportance.CRITICAL));
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(reqCode, resultCode, data);
                break;
        }
    }
}