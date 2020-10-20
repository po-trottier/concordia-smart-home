package com.concordia.smarthomesimulator.activities.editMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.fragments.map.MapModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.concordia.smarthomesimulator.Constants.RECEIVE_IMAGE_REQUEST_CODE;

public class EditMapController extends AppCompatActivity {

    private TextView mTextView;
    private View view;
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
        FloatingActionButton fab = context.findViewById(R.id.fab_upload_map_image);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (mapModel.encodeAndSaveImage(context, data.getData()))
                    Toast.makeText(context, "Image saved successfully", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context, "Could not save image", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onActivityResult(reqCode, resultCode, data);
                break;
        }
    }
}