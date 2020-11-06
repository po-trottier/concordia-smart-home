package com.concordia.smarthomesimulator.activities.editMap;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.adapters.HouseLayoutAdapter;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.helpers.HouseLayoutHelper;
import com.concordia.smarthomesimulator.views.customMapSettingsView.CustomMapSettingsView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditMapController extends AppCompatActivity {

    //region Properties

    private EditMapModel editMapModel;

    private Context context;
    private LayoutInflater inflater;

    //endregion

    //region Overrides

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_map);

        context = this;
        inflater = LayoutInflater.from(context);
        editMapModel = new ViewModelProvider(this).get(EditMapModel.class);

        setupToolbar();

        fillKnownValues();
        setSaveIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Add appropriate logic for the various Action Bar menu items.
        switch (item.getItemId()) {
            case R.id.action_open_layout:
                setOpenIntent();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    //endregion

    //region Private Methods

    //region Intents

    private void setSaveIntent() {
        FloatingActionButton saveButton = findViewById(R.id.save_map_file);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupSaveDialog();
            }
        });
    }

    private void setOpenIntent() {
        HouseLayout backupLayout = editMapModel.getHouseLayout();
        final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(getString(R.string.title_alert_open_layout))
            .setMessage(getString(R.string.text_alert_open_layout))
            .setView(setupCustomView())
            .setPositiveButton(R.string.generic_open, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HouseLayoutHelper.updateSelectedLayout(context, editMapModel.getHouseLayout());
                    fillKnownValues();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // If the previously selected layout is now deleted, select a default layout
                    ArrayList<HouseLayout> layouts = HouseLayoutHelper.listSavedLayouts(context);
                    if (layouts.stream().noneMatch(layout -> layout.equals(backupLayout))) {
                        editMapModel.setHouseLayout(layouts.get(0));
                        HouseLayoutHelper.updateSelectedLayout(context, editMapModel.getHouseLayout());
                    } else {
                        editMapModel.setHouseLayout(backupLayout);
                    }
                }
            }).create();
        dialog.show();
    }

    //endregion

    //region Dialogs

    private void setupSaveDialog() {
        final View customView = inflater.inflate(R.layout.alert_save_house_layout, null, false);
        final EditText layoutName = customView.findViewById(R.id.alert_save_layout_name);
        layoutName.setText(editMapModel.getHouseLayout().getName().trim());
        final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(getString(R.string.title_alert_save_layout))
            .setMessage(getString(R.string.text_alert_save_layout))
            .setView(customView)
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(getString(R.string.generic_save), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (layoutName != null) {
                        editMapModel.updateHouseLayoutName(layoutName.getText().toString().trim());
                        if (!HouseLayoutHelper.isLayoutNameDefault(editMapModel.getHouseLayout())) {
                            if (HouseLayoutHelper.isLayoutNameUnique(context, editMapModel.getHouseLayout())) {
                                editMapModel.saveHouseLayout(context);
                                finish();
                            } else {
                                final AlertDialog confirm = new AlertDialog.Builder(context)
                                    .setTitle(getString(R.string.title_alert_save_layout_overwrite))
                                    .setMessage(getString(R.string.text_alert_save_layout_overwrite))
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            setupSaveDialog();
                                        }
                                    })
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editMapModel.saveHouseLayout(context);
                                            finish();
                                        }
                                    }).create();
                                confirm.show();
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.error_exists_alert_save_layout), Toast.LENGTH_LONG).show();
                            setupSaveDialog();
                        }
                    }
                }
            }).create();
        dialog.show();
    }

    private void setupDeleteDialog(int position, ArrayList<HouseLayout> layouts, HouseLayoutAdapter adapter) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(String.format(getString(R.string.title_alert_delete_layout), layouts.get(position).getName()))
                .setMessage(getString(R.string.text_alert_delete_layout))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editMapModel.deleteHouseLayout(context, layouts, position);
                        adapter.notifyDataSetChanged();
                    }
                }).create();
        dialog.show();
    }

    //endregion

    //region Views

    private void fillKnownValues() {
        editMapModel.setHouseLayout(HouseLayoutHelper.getSelectedLayout(context));

        CustomMapSettingsView settings = findViewById(R.id.edit_map_settings);
        settings.forceUpdateView(editMapModel.getHouseLayout());
    }

    private View setupCustomView() {
        final View customView = inflater.inflate(R.layout.alert_open_house_layout, null, false);
        final ListView layoutList = customView.findViewById(R.id.alert_open_layout_list);

        ArrayList<HouseLayout> layouts = HouseLayoutHelper.listSavedLayouts(context);
        HouseLayoutAdapter adapter = new HouseLayoutAdapter(context, 0, layouts);
        layoutList.setAdapter(adapter);

        layoutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editMapModel.setHouseLayout(layouts.get(position));
                for (int i = 0; i < parent.getChildCount(); i ++) {
                    View child = parent.getChildAt(i);
                    child.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
                view.setBackgroundColor(context.getColor(R.color.accentFaded));
            }
        });
        layoutList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                setupDeleteDialog(position, layouts, adapter);
                return true;
            }
        });

        return customView;
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

    //endregion

    //endregion
}