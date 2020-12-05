package com.concordia.smarthomesimulator.views.alerts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.Room;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_TEMPERATURE;

/**
 * The type Custom device alert view.
 */
public class CustomEditRoomAlertView extends LinearLayout {

    private final Context context;
    private Room room;

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     */
    public CustomEditRoomAlertView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomEditRoomAlertView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
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
        this.context = context;
    }

    /**
     * Sets room's information in the UI.
     *
     * @param room the room
     */
    public void setRoomInformation(Room room) {
        this.room = room;

        setTemperature();
    }

    private void setTemperature() {
        CheckBox overrideTemp = findViewById(R.id.alert_edit_room_override);
        overrideTemp.setChecked(room.isTemperatureOverridden());

        TextView targetTemp = findViewById(R.id.alert_edit_room_target);
        targetTemp.setText(Double.toString(room.getDesiredTemperature()));
        targetTemp.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                }
                double newTemp = DEFAULT_TEMPERATURE;
                try {
                    newTemp = Double.parseDouble(targetTemp.getText().toString());
                } catch (NumberFormatException ignore) {}
                overrideTemp.setChecked(newTemp != room.getDesiredTemperature());
            }
        });
    }
}
