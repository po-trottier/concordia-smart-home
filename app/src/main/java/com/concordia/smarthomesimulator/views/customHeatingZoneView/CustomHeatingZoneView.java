package com.concordia.smarthomesimulator.views.customHeatingZoneView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.HeatingZone;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.enums.LogImportance;
import com.concordia.smarthomesimulator.helpers.LogsHelper;
import com.concordia.smarthomesimulator.views.customMapSettingsView.CustomMapSettingsView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.concordia.smarthomesimulator.Constants.*;

public class CustomHeatingZoneView extends LinearLayout {
    Context context;
    HouseLayout layout;
    HeatingZone zone;
    HeatingZone selectedZone;

    public CustomHeatingZoneView(Context context) {
        super(context);
        this.context = context;
    }

    public CustomHeatingZoneView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomHeatingZoneView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * Sets the heating zone to use to populate the view.
     *
     * @param zone the heating zone
     * @param layout the temporary house layout
     */
    public void setupView(HeatingZone zone, HouseLayout layout) {
        this.zone = zone;
        this.layout = layout;

        setZoneTitle();
        setZoneTemperature();
        setZoneTitleIntent();
        setZoneRooms();
    }

    private void updateView() {
        CustomMapSettingsView view = ((Activity) context).findViewById(R.id.edit_map_settings);
        view.updateView();
    }

    private void setZoneTitle() {
        TextView zoneName = findViewById(R.id.adapter_zone_name);
        zoneName.setText(zone.getName());
    }

    private void setZoneTemperature() {
        TextView zoneTemperature = findViewById(R.id.adapter_zone_temperature);
        zoneTemperature.setText(zone.getDesiredTemperature() + context.getString(R.string.generic_degrees_celsius));
    }

    private void setZoneRooms() {
        LinearLayout zoneRooms = findViewById(R.id.adapter_zone_rooms);
        TextView noRooms = findViewById(R.id.adapter_zone_no_room);

        noRooms.setVisibility(zone.getRooms().size() < 1 ? VISIBLE : GONE);
        zoneRooms.setVisibility(zone.getRooms().size() < 1 ? GONE : VISIBLE);

        zoneRooms.removeAllViews();

        for (Room room : zone.getRooms()) {
            ConstraintLayout child = (ConstraintLayout) inflate(context, R.layout.adapter_zone_room, null);

            TextView roomName = child.findViewById(R.id.adapter_zone_room_name);
            roomName.setText(room.getName());

            TextView override = child.findViewById(R.id.adapter_zone_room_override);
            override.setVisibility(room.isTemperatureOverridden() ? VISIBLE : GONE);

            ConstraintLayout roomLayout = child.findViewById(R.id.adapter_zone_room_layout);
            roomLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    View customView = setupMoveRoomLayout();
                    final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.text_alert_move_room))
                        .setView(customView)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (selectedZone == null) {
                                    return;
                                }
                                // Update the room
                                room.setDesiredTemperature(selectedZone.getDesiredTemperature());
                                room.setIsTemperatureOverridden(false);
                                // Update the layout
                                layout.removeRoom(room.getName());
                                layout.addRoom(room);
                                // Update the zones
                                selectedZone.addRoom(room);
                                layout.getHeatingZones().get(0).removeRoom(room.getName());
                                // Update the view
                                updateView();
                                // Log the action
                                String message = room.getName() + " was moved from zone " + zone.getName() + " to" + selectedZone.getName();
                                LogsHelper.add(context, new LogEntry("Map Set", message, LogImportance.IMPORTANT));
                            }
                        })
                        .create();
                    dialog.show();
                }
            });

            zoneRooms.addView(child);
        }
    }

    private void setZoneTitleIntent() {
        LinearLayout zoneTitle = findViewById(R.id.adapter_zone_title_bar);
        zoneTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View customView = setupTemperatureLayout();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.text_alert_edit_temperature))
                    .setView(customView)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText temperatureField = customView.findViewById(R.id.alert_edit_temperature);
                            // Make sure the temperature is reasonable
                            double temperature = DEFAULT_TEMPERATURE;
                            try {
                                temperature = Double.parseDouble(temperatureField.getText().toString().trim());
                            } catch (NumberFormatException ignored) {}
                            temperature = Math.min(MAXIMUM_TEMPERATURE, temperature);
                            temperature = Math.max(MINIMUM_TEMPERATURE, temperature);
                            // Set the zone temperature
                            zone.setDesiredTemperature(temperature);
                            // Set the room temperatures
                            ArrayList<Room> roomsToMove = new ArrayList<>(zone.getRooms());
                            for (Room room : roomsToMove) {
                                layout.removeRoom(room.getName());
                                layout.addRoom(room);
                                // Make sure to put it back in the right zone
                                layout.getHeatingZones().get(0).removeRoom(room.getName());
                                zone.addRoom(room);
                            }
                            // Update the view
                            updateView();
                            // Log the action
                            LogsHelper.add(context, new LogEntry("Map Settings", "Temperature setting changed for zone " + zone.getName(), LogImportance.IMPORTANT));
                        }
                    });
                if (!zone.getName().equalsIgnoreCase(DEFAULT_NAME_HEATING_ZONE)) {
                    builder.setNeutralButton(R.string.generic_remove, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            layout.removeHeatingZone(zone.getName());
                            updateView();
                            // Log the action
                            LogsHelper.add(context, new LogEntry("Map Settings", "Climate Zone removed: " + zone.getName(), LogImportance.IMPORTANT));
                        }
                    });
                }
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private View setupMoveRoomLayout() {
        View customView = inflate(context, R.layout.alert_select_zone, null);

        ListView zoneList = customView.findViewById(R.id.alert_select_zone_list);
        List<String> zoneNames = layout.getHeatingZones()
                .stream()
                .map(HeatingZone::getName)
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, zoneNames);
        zoneList.setAdapter(adapter);

        zoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected zone
                selectedZone = layout.getHeatingZone(zoneNames.get(position));
                // Color the selected item
                for (int i = 0; i < parent.getChildCount(); i ++) {
                    View child = parent.getChildAt(i);
                    child.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
                view.setBackgroundColor(context.getColor(R.color.accentFaded));
            }
        });

        return customView;
    }

    private View setupTemperatureLayout() {
        View customView = inflate(context, R.layout.alert_edit_temperature, null);
        EditText temperature = customView.findViewById(R.id.alert_edit_temperature);
        temperature.setText(Double.toString(zone.getDesiredTemperature()));
        return customView;
    }
}
