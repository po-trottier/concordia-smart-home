package com.concordia.smarthomesimulator.views.alerts;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.Room;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_TEMPERATURE;

/**
 * The type Custom device alert view.
 */
public class CustomEditRoomAlertView extends LinearLayout {

    private Room room;
    private double initialTemp;

    private EditText targetTemp;
    private SwitchCompat overrideTemp;

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     */
    public CustomEditRoomAlertView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomEditRoomAlertView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CustomEditRoomAlertView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets room's information in the UI.
     *
     * @param room               the room
     * @param zoneTemperature the zone temperature
     */
    public void setRoomInformation(Room room, double zoneTemperature) {
        this.room = room;
        this.initialTemp = zoneTemperature;

        targetTemp = findViewById(R.id.alert_edit_room_target_field);
        overrideTemp = findViewById(R.id.alert_edit_room_override_field);

        setupTemperature();
        setupOverride();
    }

    /**
     * Gets room information.
     *
     * @return the room information
     */
    public Room getRoomInformation() {
        return this.room;
    }

    private void setupTemperature() {
        targetTemp.setText(Double.toString(room.getDesiredTemperature()));
        // Define Text Watcher to change Switch
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Get the new temperature
                double newTemp;
                try {
                    newTemp = Double.parseDouble(targetTemp.getText().toString());
                } catch (NumberFormatException ignore) {
                    return;
                }
                // Set the override checkbox
                boolean override = newTemp != initialTemp;
                overrideTemp.setChecked(override);
                // Modify the temporary room
                room.setDesiredTemperature(newTemp);
                room.setIsTemperatureOverridden(override);
            }
        };
        // Add watcher
        targetTemp.addTextChangedListener(watcher);
    }

    private void setupOverride() {
        // Set switch status text
        TextView switchStatus = findViewById(R.id.alert_override_text_status);
        switchStatus.setText(Boolean.toString(room.isTemperatureOverridden()).toUpperCase());
        // Set switch status + listener
        overrideTemp.setChecked(room.isTemperatureOverridden());
        overrideTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchStatus.setText(Boolean.toString(isChecked).toUpperCase());
                // If we remove the override, put back the original temperature
                double newTemp = initialTemp;
                try {
                    newTemp = Double.parseDouble(targetTemp.getText().toString());
                } catch (NumberFormatException ignore) {}

                if (!isChecked && newTemp != initialTemp) {
                    targetTemp.setText(Double.toString(initialTemp));
                }
            }
        });
    }
}
