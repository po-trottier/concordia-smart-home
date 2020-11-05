package com.concordia.smarthomesimulator.views.customDeviceAlertView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.DeviceType;
import com.concordia.smarthomesimulator.dataModels.IDevice;
import com.concordia.smarthomesimulator.dataModels.Window;

/**
 * The type Custom device alert view.
 */
public class CustomDeviceAlertView extends LinearLayout {

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     */
    public CustomDeviceAlertView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomDeviceAlertView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CustomDeviceAlertView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets device's information in the UI.
     *
     * @param device the device
     */
    public void setDeviceInformation(IDevice device) {
        setStatusText(device);
        setStatusCheckbox(device);
        setLockLayout(device);
    }

    private void setStatusText(IDevice device) {
        int color = device.getIsOpened() ? device.getOpenedTint() : device.getClosedTint();
        int text = device.getIsOpened() ? R.string.map_opened : R.string.map_closed;

        TextView statusText = findViewById(R.id.device_status_text);
        statusText.setText(getContext().getString(text));
        statusText.setTextColor(getContext().getColor(color));
    }

    private void setStatusCheckbox(IDevice device) {
        int color = device.getIsOpened() ? device.getOpenedTint() : device.getClosedTint();

        CheckBox statusCheck = findViewById(R.id.device_status_checkbox);
        statusCheck.setChecked(device.getIsOpened());
        statusCheck.setCompoundDrawableTintList(ColorStateList.valueOf(getContext().getColor(color)));
        statusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                device.setIsOpened(isChecked);
                setDeviceInformation(device);
            }
        });
    }

    private void setLockLayout(IDevice device) {
        if (device.getDeviceType() != DeviceType.WINDOW)
            return;

        LinearLayout layout = findViewById(R.id.device_lock_layout);
        layout.setVisibility(VISIBLE);

        Window window = (Window) device;
        setLockText(window);
        setLockCheckbox(window);
    }

    private void setLockText(Window device) {
        int color = device.getIsLocked() ? R.color.danger : device.getLockedTint();
        int text = device.getIsLocked() ? R.string.map_locked : R.string.map_unlocked;

        TextView statusText = findViewById(R.id.device_lock_text);
        statusText.setText(getContext().getString(text));
        statusText.setTextColor(getContext().getColor(color));
    }

    private void setLockCheckbox(Window device) {
        int color = device.getIsLocked() ? R.color.danger : device.getLockedTint();

        CheckBox statusCheck = findViewById(R.id.device_lock_checkbox);
        statusCheck.setChecked(device.getIsLocked());
        statusCheck.setCompoundDrawableTintList(ColorStateList.valueOf(getContext().getColor(color)));
        statusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                device.setIsLocked(isChecked);
                setDeviceInformation(device);
            }
        });
    }
}
