package com.concordia.smarthomesimulator.views.alerts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.Room;

/**
 * The type Custom device alert view.
 */
public class CustomRoomAlertView extends LinearLayout {

    private Context context;
    private Room room;

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     */
    public CustomRoomAlertView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomRoomAlertView(Context context, @Nullable AttributeSet attrs) {
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
    public CustomRoomAlertView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        setTemperatures();
        setVentilationStatus();
    }

    private void setTemperatures() {
        TextView actualTemp = findViewById(R.id.alert_edit_room_actual);
        TextView targetTemp = findViewById(R.id.alert_edit_room_target);
        TextView overrideTemp = findViewById(R.id.alert_edit_room_override);

        actualTemp.setText(Math.floor(room.getActualTemperature() * 100) / 100 + context.getString(R.string.generic_degrees_celsius));
        targetTemp.setText(Math.floor(room.getDesiredTemperature() * 100) / 100 + context.getString(R.string.generic_degrees_celsius));

        int color = room.isTemperatureOverridden() ? R.color.primary : R.color.charcoal;
        overrideTemp.setText(Boolean.toString(room.isTemperatureOverridden()).toUpperCase());
        overrideTemp.setTextColor(context.getColor(color));
    }

    private void setVentilationStatus() {
        TextView ventilationStatus = findViewById(R.id.alert_edit_room_ventilation);

        int color = R.color.charcoal;
        switch (room.getVentilationStatus()) {
            case COOLING:
                color = R.color.primary;
                break;
            case HEATING:
                color = R.color.danger;
                break;
            case PAUSED:
                color = R.color.accentDark;
                break;
        }

        ventilationStatus.setText(room.getVentilationStatus().toString());
        ventilationStatus.setTextColor(context.getColor(color));
    }
}
