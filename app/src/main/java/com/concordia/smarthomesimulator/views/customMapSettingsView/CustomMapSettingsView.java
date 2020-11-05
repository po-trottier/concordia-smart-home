package com.concordia.smarthomesimulator.views.customMapSettingsView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.*;
import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.R;

public class CustomMapSettingsView extends ScrollView {

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

    public void updateView() {
        model.updateLayout(this.context);

        findControls();
        updateVisibility();
    }

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

    private void updateVisibility() {
        noRoomsText.setVisibility(GONE);
        noDevicesText.setVisibility(GONE);
        noInhabitantsText.setVisibility(GONE);
        selectDevicesText.setVisibility(GONE);
        selectInhabitantsText.setVisibility(GONE);
        roomsListLayout.setVisibility(VISIBLE);
        devicesListLayout.setVisibility(VISIBLE);
        inhabitantsListLayout.setVisibility(VISIBLE);
        removeRoomButton.setActivated(true);

        if (model.getLayout().getRooms().size() < 1) {
            noRoomsText.setVisibility(VISIBLE);
            selectDevicesText.setVisibility(VISIBLE);
            selectInhabitantsText.setVisibility(VISIBLE);

            roomsListLayout.setVisibility(GONE);
            devicesListLayout.setVisibility(GONE);
            inhabitantsListLayout.setVisibility(GONE);

            removeRoomButton.setActivated(false);
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

    private void fillKnownValues() {
        houseLayoutName.setText(model.getLayout().getName());
    }
}
