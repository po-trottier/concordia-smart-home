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
import com.concordia.smarthomesimulator.enums.DeviceType;
import com.concordia.smarthomesimulator.interfaces.IDevice;
import com.concordia.smarthomesimulator.dataModels.Window;
import com.concordia.smarthomesimulator.factories.DeviceFactory;

/**
 * The type Custom device alert view.
 */
public class CustomDeviceAlertView extends LinearLayout {

    private DeviceFactory factory;
    private IDevice device;

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     */
    public CustomDeviceAlertView(Context context) {
        super(context);
        initializeFactory();
    }

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomDeviceAlertView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeFactory();
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
        initializeFactory();
    }

    /**
     * Gets updated device information.
     *
     * @return the device information
     */
    public IDevice getDeviceInformation() {
        IDevice newDevice = null;

        switch (device.getDeviceType()) {
            case LIGHT:
                newDevice = factory.createDevice(DeviceType.LIGHT, device.getGeometry());
                break;
            case DOOR:
                newDevice = factory.createDevice(DeviceType.DOOR, device.getGeometry());
                break;
            case WINDOW:
                newDevice = factory.createDevice(DeviceType.WINDOW, device.getGeometry());
                break;
        }

        CheckBox openedCheckbox = findViewById(R.id.device_status_checkbox);
        newDevice.setIsOpened(openedCheckbox.isChecked());

        if (newDevice.getDeviceType() == DeviceType.WINDOW) {
            CheckBox lockedCheckbox = findViewById(R.id.device_lock_checkbox);
            ((Window) newDevice).setIsLocked(lockedCheckbox.isChecked());
        }

        return newDevice;
    }

    /**
     * Sets device's information in the UI.
     *
     * @param device the device
     */
    public void setDeviceInformation(IDevice device) {
        this.device = device;

        setStatusText();
        setStatusCheckbox();
        setLockLayout();
    }

    private void initializeFactory() {
        factory = new DeviceFactory();
    }

    private void setStatusText() {
        int color = device.getIsOpened() ? device.getOpenedTint() : device.getClosedTint();
        int text = device.getIsOpened() ? R.string.map_opened : R.string.map_closed;

        TextView statusText = findViewById(R.id.device_status_text);
        statusText.setText(getContext().getString(text));
        statusText.setTextColor(getContext().getColor(color));
    }

    private void setStatusCheckbox() {
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

    private void setLockLayout() {
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
