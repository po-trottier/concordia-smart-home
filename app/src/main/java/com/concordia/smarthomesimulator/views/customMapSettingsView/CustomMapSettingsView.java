package com.concordia.smarthomesimulator.views.customMapSettingsView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.factories.DeviceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.concordia.smarthomesimulator.Constants.*;

public class CustomMapSettingsView extends ScrollView {

    //region Properties

    private CustomMapSettingsModel model;
    private Context context;

    private EditText houseLayoutName;
    private TextView noRoomsText;
    private TextView noDevicesText;
    private TextView noInhabitantsText;
    private TextView selectDevicesText;
    private TextView selectInhabitantsText;
    private LinearLayout roomsListLayout;
    private LinearLayout devicesListLayout;
    private LinearLayout inhabitantsListLayout;
    private Button removeRoomButton;
    private Button addRoomButton;
    private Button addDeviceButton;
    private Button addInhabitantButton;

    //endregion

    //region Constructors

    public CustomMapSettingsView(Context context) {
        super(context);
        initializeView(context);
    }

    public CustomMapSettingsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public CustomMapSettingsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context);
    }

    //endregion

    //region Public Methods

    /**
     * Update view.
     *
     * @param selectedLayout the selected layout
     */
    public void forceUpdateView(HouseLayout selectedLayout) {
        model.setLayout(selectedLayout);
        updateView();
    }

    /**
     * Update view.
     */
    public void updateView() {
        if (model.getLayout() == null) {
            model.updateLayout(this.context);
        }

        findControls();
        fillKnownValues();
        setButtonListeners();
    }

    //endregion

    //region Private Methods

    //region UI Update Methods

    private void initializeView(Context context) {
        this.context = context;
        model = new CustomMapSettingsModel();
    }

    private void findControls() {
        houseLayoutName = findViewById(R.id.edit_map_layout_name);
        noRoomsText = findViewById(R.id.edit_map_room_empty);
        noDevicesText = findViewById(R.id.edit_map_device_empty);
        noInhabitantsText = findViewById(R.id.edit_map_inhabitant_empty);
        selectDevicesText = findViewById(R.id.edit_map_device_select);
        selectInhabitantsText = findViewById(R.id.edit_map_inhabitant_select);
        roomsListLayout = findViewById(R.id.edit_map_room_list);
        devicesListLayout = findViewById(R.id.edit_map_device_list);
        inhabitantsListLayout = findViewById(R.id.edit_map_inhabitant_list);
        removeRoomButton = findViewById(R.id.edit_map_remove_room);
        addRoomButton = findViewById(R.id.edit_map_add_room);
        addDeviceButton = findViewById(R.id.edit_map_add_device);
        addInhabitantButton = findViewById(R.id.edit_map_add_inhabitant);
    }

    private void fillKnownValues() {
        houseLayoutName.setText(model.getLayout().getName());

        updateLayoutVisibility();
        addRoomsToList();

        Room room = model.getSelectedRoom();
        if (room != null) {
            addDevicesToList();
            addInhabitantsToList();

            if (room.getName().equals(DEFAULT_NAME_OUTDOORS) || room.getName().equals(DEFAULT_NAME_GARAGE)) {
                updateButtonAvailability(removeRoomButton, false, false);
                updateButtonAvailability(addDeviceButton, false, true);
            } else {
                updateButtonAvailability(removeRoomButton, true, false);
                updateButtonAvailability(addDeviceButton, true, true);
            }
        }
    }

    private void setButtonListeners() {
        setRemoveRoomListener();
        setAddRoomListener();
        setAddDeviceListener();
        setAddInhabitantListener();
    }

    private void updateLayoutVisibility() {
        noRoomsText.setVisibility(GONE);
        noDevicesText.setVisibility(GONE);
        noInhabitantsText.setVisibility(GONE);
        selectDevicesText.setVisibility(GONE);
        selectInhabitantsText.setVisibility(GONE);
        roomsListLayout.setVisibility(VISIBLE);
        devicesListLayout.setVisibility(VISIBLE);
        inhabitantsListLayout.setVisibility(VISIBLE);
        updateButtonAvailability(removeRoomButton, true, false);
        updateButtonAvailability(addDeviceButton, true, true);
        updateButtonAvailability(addInhabitantButton, true, true);

        // There will always be "Outdoors" and "Garage"
        if (model.getLayout().getRooms().size() <= 0) {
            noRoomsText.setVisibility(VISIBLE);
            selectDevicesText.setVisibility(VISIBLE);
            selectInhabitantsText.setVisibility(VISIBLE);

            roomsListLayout.setVisibility(GONE);
            devicesListLayout.setVisibility(GONE);
            inhabitantsListLayout.setVisibility(GONE);

            updateButtonAvailability(removeRoomButton, false, false);
            updateButtonAvailability(addDeviceButton, false, true);
            updateButtonAvailability(addInhabitantButton, false, true);
        } else if (model.getSelectedRoom() == null) {
            selectDevicesText.setVisibility(VISIBLE);
            selectInhabitantsText.setVisibility(VISIBLE);

            devicesListLayout.setVisibility(GONE);
            inhabitantsListLayout.setVisibility(GONE);
        } else {
            if (model.getSelectedRoom().getDevices().size() < 1) {
                noDevicesText.setVisibility(VISIBLE);
                devicesListLayout.setVisibility(GONE);
            }
            if (model.getSelectedRoom().getInhabitants().size() < 1) {
                noInhabitantsText.setVisibility(VISIBLE);
                inhabitantsListLayout.setVisibility(GONE);
            }
        }
    }

    private void updateButtonAvailability(Button button, boolean enabled, boolean isButtonPositive) {
        GradientDrawable background = (GradientDrawable) context.getDrawable(R.drawable.rounded_outlined_button_background);
        int strokeWidth = 0;
        int buttonColor;
        int textColor;
        // If the button is positive it's Primary, otherwise it's Danger
        if (isButtonPositive) {
            buttonColor = enabled ? R.color.primary : R.color.primaryFaded;
            textColor = android.R.color.white;
        } else {
            strokeWidth = (int) getResources().getDimension(R.dimen.button_stroke_width);
            buttonColor = enabled ? R.color.danger : R.color.dangerFaded;
            textColor = enabled ? R.color.danger : R.color.dangerFaded;
        }
        background.setStroke(strokeWidth, context.getColor(buttonColor));
        button.setBackgroundTintList(ColorStateList.valueOf(context.getColor(buttonColor)));
        button.setTextColor(context.getColor(textColor));
        button.setEnabled(enabled);
    }

    //endregion

    //region On Click Listeners

    private void setRemoveRoomListener() {
        removeRoomButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.title_alert_remove_room_edit_map))
                    .setMessage(context.getString(R.string.text_alert_remove_room_edit_map))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(R.string.generic_remove, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Get the layout and remove the room
                            HouseLayout layout = model.getLayout();
                            layout.removeRoom(model.getSelectedRoom().getName());
                            model.setLayout(layout);
                            // Update the UI
                            updateView();
                        }
                    })
                    .create();
                dialog.show();
            }
        });
    }

    private void setAddRoomListener() {
        addRoomButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View customView = inflate(context, R.layout.alert_add_room, null);
                final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.title_alert_add_room_edit_map))
                    .setView(customView)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.generic_add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Get the controls
                            EditText roomName = customView.findViewById(R.id.alert_add_room_name);
                            EditText roomWidth = customView.findViewById(R.id.alert_add_room_width);
                            EditText roomHeight = customView.findViewById(R.id.alert_add_room_height);
                            EditText xCoordinate = customView.findViewById(R.id.alert_add_room_x);
                            EditText yCoordinate = customView.findViewById(R.id.alert_add_room_y);
                            // Get the values
                            String name = roomName.getText().toString().trim();
                            String widthText = roomWidth.getText().toString().trim();
                            String heightText = roomHeight.getText().toString().trim();
                            String xText = xCoordinate.getText().toString().trim();
                            String yText = yCoordinate.getText().toString().trim();
                            // Validate name
                            if (name.length() < 1) {
                                Toast.makeText(context, context.getString(R.string.text_alert_room_empty), Toast.LENGTH_LONG).show();
                                return;
                            }
                            // Convert the values
                            int width = Integer.parseInt(widthText.equals("") ? "1" : widthText);
                            int height = Integer.parseInt(heightText.equals("") ? "1" : heightText);
                            int x = Integer.parseInt(xText.equals("") ? "0" : xText);
                            int y = Integer.parseInt(yText.equals("") ? "0" : yText);
                            // Add the room
                            model.addRoom(context, name, x, y, width, height);
                        }
                    })
                    .create();
                dialog.show();
            }
        });
    }

    private void setAddDeviceListener() {
        addDeviceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View customView = setupAddDeviceLayout();
                final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.title_alert_add_device_edit_map))
                    .setView(customView)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.generic_add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Get the controls
                            Spinner deviceType = customView.findViewById(R.id.alert_add_device_type);
                            Spinner deviceOrientation = customView.findViewById(R.id.alert_add_device_orientation);
                            EditText xCoordinate = customView.findViewById(R.id.alert_add_device_x);
                            EditText yCoordinate = customView.findViewById(R.id.alert_add_device_y);
                            // Get the values from the controls
                            DeviceType type = DeviceType.fromString((String) deviceType.getSelectedItem());
                            Orientation orientation = Orientation.fromString((String) deviceOrientation.getSelectedItem());
                            String xText = xCoordinate.getText().toString().trim();
                            String yText = yCoordinate.getText().toString().trim();
                            int x = Integer.parseInt(xText.equals("") ? Integer.toString(model.getSelectedRoom().getGeometry().getX()) : xText);
                            int y = Integer.parseInt(yText.equals("") ? Integer.toString(model.getSelectedRoom().getGeometry().getY()) : yText);
                            // Create the device
                            model.addDevice(context, type, x, y, orientation);
                        }
                    })
                    .create();
                dialog.show();
            }
        });
    }

    private void setAddInhabitantListener() {
        addInhabitantButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View customView = setupAddInhabitantLayout();
                final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.title_alert_add_inhabitant_edit_map))
                    .setView(customView)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.generic_add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText nameView = customView.findViewById(R.id.alert_add_inhabitant_name);
                            model.addInhabitant(context, nameView.getText().toString().trim());
                        }
                    })
                    .create();
                dialog.show();
            }
        });
    }

    //endregion

    //region Populate Lists Methods

    private void addRoomsToList() {
        roomsListLayout.removeAllViews();
        for (Room room : model.getOrderedRooms()) {
            LinearLayout child = (LinearLayout) inflate(context, R.layout.adapter_generic, null);

            TextView roomName = child.findViewById(R.id.adapter_generic_text);
            roomName.setText(room.getName());

            if (room.getName().equals(DEFAULT_NAME_OUTDOORS) || room.getName().equals(DEFAULT_NAME_GARAGE)) {
                TextView roomExtra = child.findViewById(R.id.adapter_generic_text_secondary);
                roomExtra.setText(context.getString(R.string.room_default_edit_map));
                roomExtra.setVisibility(VISIBLE);
            }

            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setSelectedRoom(room);
                    fillKnownValues();
                }
            });

            if (room.equals(model.getSelectedRoom())) {
                child.setBackground(context.getDrawable(R.drawable.rounded_text_field_background));
                child.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.primaryFaded)));
                roomName.setTextColor(context.getColor(R.color.primaryDark));
            } else {
                child.setBackgroundColor(context.getColor(android.R.color.transparent));
                roomName.setTextColor(context.getColor(R.color.charcoal));
            }

            roomsListLayout.addView(child);
        }
    }

    private void addDevicesToList() {
        devicesListLayout.removeAllViews();
        if (model.getSelectedRoom() == null) {
            return;
        }
        for (IDevice device : model.getSelectedRoom().getDevices()) {
            LinearLayout child = (LinearLayout) inflate(context, R.layout.adapter_generic, null);

            String deviceText = device.getDeviceType().toString();
            deviceText = deviceText.charAt(0) + deviceText.toLowerCase().substring(1);

            TextView deviceName = child.findViewById(R.id.adapter_generic_text);
            deviceName.setText(deviceText);

            String coordinatesText = context.getString(R.string.room_coordinates_edit_map) + " " + device.getGeometry().toString();
            boolean isDefaultDevice = false;
            if (model.getSelectedRoom().getName().equals(DEFAULT_NAME_GARAGE)) {
                isDefaultDevice = true;
                coordinatesText = context.getString(R.string.room_default_edit_map);
            }

            TextView coordinates = child.findViewById(R.id.adapter_generic_text_secondary);
            coordinates.setText(coordinatesText);
            coordinates.setVisibility(VISIBLE);

            boolean finalIsDefaultDevice = isDefaultDevice;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalIsDefaultDevice) {
                        Toast.makeText(context,context.getString(R.string.toast_no_remove_device_edit_map), Toast.LENGTH_LONG).show();
                        return;
                    }
                    model.removeDevice(context, device);
                }
            });

            devicesListLayout.addView(child);
        }
    }

    private void addInhabitantsToList() {
        inhabitantsListLayout.removeAllViews();
        if (model.getSelectedRoom() == null) {
            return;
        }
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String currentUser = preferences.getString(PREFERENCES_KEY_USERNAME, "");

        for (Inhabitant inhabitant : model.getSelectedRoom().getInhabitants()) {
            LinearLayout child = (LinearLayout) inflate(context, R.layout.adapter_generic, null);

            String inhabitantText = inhabitant.getName();

            TextView inhabitantName = child.findViewById(R.id.adapter_generic_text);
            inhabitantName.setText(inhabitantText);

            if (inhabitantText.equals(currentUser)) {
                TextView inhabitantExtra = child.findViewById(R.id.adapter_generic_text_secondary);
                inhabitantExtra.setText(context.getString(R.string.inhabitant_you_edit_map));
                inhabitantExtra.setVisibility(VISIBLE);
            }

            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.editInhabitant(context, inhabitant);
                }
            });

            inhabitantsListLayout.addView(child);
        }
    }

    //endregion

    //region Setup Custom Views

    private View setupAddInhabitantLayout() {
        // Create the custom view
        View customView = inflate(context, R.layout.alert_add_inhabitant, null);
        // Get all the inhabitants in the layout
        ArrayList<Inhabitant> inhabitants =  model.getLayout().getAllInhabitants();
        // Get the currently logged in user
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String currentUser = preferences.getString(PREFERENCES_KEY_USERNAME, "");
        // Remove the checkbox layout if the user already has an inhabitant
        if (inhabitants.stream().anyMatch(i -> i.getName().equalsIgnoreCase(currentUser))) {
            LinearLayout checkBoxLayout = customView.findViewById(R.id.alert_add_inhabitant_checkbox_layout);
            checkBoxLayout.setVisibility(GONE);
        } else {
            CheckBox checkBox = customView.findViewById(R.id.alert_add_inhabitant_checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        EditText nameView = customView.findViewById(R.id.alert_add_inhabitant_name);
                        nameView.setText(currentUser);
                    }
                }
            });
        }
        return customView;
    }

    private View setupAddDeviceLayout() {
        View customView = inflate(context, R.layout.alert_add_device, null);
        // Get the spinners
        Spinner deviceType = customView.findViewById(R.id.alert_add_device_type);
        Spinner deviceOrientation = customView.findViewById(R.id.alert_add_device_orientation);
        // Get the list of strings
        String[] deviceTypes = context.getResources().getStringArray(R.array.device_type_spinner);
        String[] deviceOrientations = context.getResources().getStringArray(R.array.device_orientation_spinner);
        // Create the adapters
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, deviceTypes);
        ArrayAdapter<String> orientationAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, deviceOrientations);
        // Set the adapters
        deviceType.setAdapter(typeAdapter);
        deviceOrientation.setAdapter(orientationAdapter);
        // When a light is chosen, hide the orientation picker
        deviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = customView.findViewById(R.id.alert_add_device_orientation_layout);
                if (deviceTypes[position].equalsIgnoreCase("Light")) {
                    layout.setVisibility(GONE);
                } else {
                    layout.setVisibility(VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        // Get current room geometry
        Geometry geo = model.getSelectedRoom().getGeometry();
        // Get coordinates entered
        EditText xCoordinate = customView.findViewById(R.id.alert_add_device_x);
        EditText yCoordinate = customView.findViewById(R.id.alert_add_device_y);
        // Set watchers for coordinates
        String error = context.getString(R.string.text_alert_device_error);
        xCoordinate.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                }
                try {
                    // Insure that the coordinates are inside the room
                    int x = Integer.parseInt(xCoordinate.getText().toString());
                    if (x < geo.getX()) {
                        xCoordinate.setText(Integer.toString(geo.getX()));
                        Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                    } else if (x > geo.getX() + geo.getWidth()) {
                        xCoordinate.setText(Integer.toString(geo.getX() + geo.getWidth()));
                        Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException ignored) { }
            }
        });
        yCoordinate.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                }
                try {
                    // Insure that the coordinates are inside the room
                    int y = Integer.parseInt(yCoordinate.getText().toString());
                    if (y < geo.getY()) {
                        yCoordinate.setText(Integer.toString(geo.getY()));
                        Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                    } else if (y > geo.getY() + geo.getHeight()) {
                        yCoordinate.setText(Integer.toString(geo.getY() + geo.getHeight()));
                        Toast.makeText(context,error, Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException ignored) { }
            }
        });
        return customView;
    }

    //endregion

    //endregion
}
