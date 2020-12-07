package com.concordia.smarthomesimulator.views.alerts;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.*;
import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.Door;
import com.concordia.smarthomesimulator.dataModels.Light;
import com.concordia.smarthomesimulator.dataModels.Window;
import com.concordia.smarthomesimulator.enums.Action;
import com.concordia.smarthomesimulator.enums.DeviceType;
import com.concordia.smarthomesimulator.exceptions.PermissionNotFoundException;
import com.concordia.smarthomesimulator.factories.DeviceFactory;
import com.concordia.smarthomesimulator.helpers.UserbaseHelper;
import com.concordia.smarthomesimulator.interfaces.IDevice;

/**
 * The type Custom device alert view.
 */
public class CustomDeviceAlertView extends LinearLayout {

    private DeviceFactory factory;
    private Context context;
    private IDevice device;

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     */
    public CustomDeviceAlertView(Context context) {
        super(context);
        initializeView(context);
    }

    /**
     * Instantiates a new Custom device alert view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomDeviceAlertView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
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
        initializeView(context);
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

        CheckBox lockedCheckbox = findViewById(R.id.device_lock_checkbox);
        CheckBox autoLockCheckbox = findViewById(R.id.device_auto_lock_checkbox);
        CheckBox autoOnCheckbox = findViewById(R.id.device_auto_on_checkbox);
        switch (newDevice.getDeviceType()) {
            case DOOR:
                ((Door) newDevice).setIsLocked(lockedCheckbox.isChecked());
                ((Door) newDevice).setAutoLock(autoLockCheckbox.isChecked());
                break;
            case WINDOW:
                ((Window) newDevice).setIsLocked(lockedCheckbox.isChecked());
                break;
            case LIGHT:
                ((Light) newDevice).setAutoOn(autoOnCheckbox.isChecked());
                break;
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
        setAutoLockLayout();
        setAutoOnLayout();
    }

    private void initializeView(Context context) {
        this.context = context;
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
        if (device.getDeviceType() != DeviceType.WINDOW && device.getDeviceType() != DeviceType.DOOR)
            return;

        LinearLayout layout = findViewById(R.id.device_lock_layout);
        layout.setVisibility(VISIBLE);

        setLockText();
        setLockCheckbox();
    }

    private void setAutoLockLayout() {
        if (device.getDeviceType() != DeviceType.DOOR)
            return;

        LinearLayout layout = findViewById(R.id.device_auto_lock_layout);
        layout.setVisibility(VISIBLE);

        setAutoLockText((Door) device);
        setAutoLockCheckbox((Door) device);
    }

    private void setAutoOnLayout() {
        if (device.getDeviceType() != DeviceType.LIGHT)
            return;

        LinearLayout layout = findViewById(R.id.device_auto_on_layout);
        layout.setVisibility(VISIBLE);

        setAutoOnText((Light) device);
        setAutoOnCheckbox((Light) device);
    }

    private void setLockText() {
        int color;
        int text;

        switch (device.getDeviceType()) {
            case DOOR:
                color = ((Door) device).getIsLocked() ? R.color.danger : ((Door) device).getLockedTint();
                text = ((Door) device).getIsLocked() ? R.string.map_locked : R.string.map_unlocked;
                break;
            case WINDOW:
                color = ((Window) device).getIsLocked() ? R.color.danger : ((Window) device).getLockedTint();
                text = ((Window) device).getIsLocked() ? R.string.map_locked : R.string.map_unlocked;
                break;
            default:
                color = R.color.danger;
                text = R.string.map_locked;
        }

        TextView statusText = findViewById(R.id.device_lock_text);
        statusText.setText(getContext().getString(text));
        statusText.setTextColor(getContext().getColor(color));
    }

    private void setLockCheckbox() {
        int color;
        boolean locked;

        switch (device.getDeviceType()) {
            case DOOR:
                locked = ((Door) device).getIsLocked();
                color = locked ? R.color.danger : ((Door) device).getLockedTint();
                break;
            case WINDOW:
                locked = ((Window) device).getIsLocked();
                color = locked ? R.color.danger : ((Window) device).getLockedTint();
                break;
            default:
                color = R.color.danger;
                locked = true;
        }

        CheckBox statusCheck = findViewById(R.id.device_lock_checkbox);
        statusCheck.setChecked(locked);
        statusCheck.setCompoundDrawableTintList(ColorStateList.valueOf(getContext().getColor(color)));
        statusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (device.getDeviceType()) {
                    case DOOR:
                        try {
                            if (UserbaseHelper.verifyPermissions(Action.INTERACT_DOOR_LOCK, context)) {
                                ((Door) device).setIsLocked(isChecked);
                            }
                        } catch (PermissionNotFoundException ignore) {
                            Toast.makeText(context, context.getString(R.string.permission_error), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case WINDOW:
                        ((Window) device).setIsLocked(isChecked);
                        break;
                }
                setDeviceInformation(device);
            }
        });
    }

    private void setAutoLockText(Door device) {
        int text = device.isAutoLock() ? R.string.map_activated : R.string.map_deactivated;

        TextView statusText = findViewById(R.id.device_auto_lock_text);
        statusText.setText(getContext().getString(text));
    }

    private void setAutoLockCheckbox(Door device) {
        CheckBox statusCheck = findViewById(R.id.device_auto_lock_checkbox);
        statusCheck.setChecked(device.isAutoLock());
        statusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                device.setAutoLock(isChecked);
                setDeviceInformation(device);
            }
        });
    }

    private void setAutoOnText(Light device) {
        int text = device.isAutoOn() ? R.string.map_activated : R.string.map_deactivated;

        TextView statusText = findViewById(R.id.device_auto_on_text);
        statusText.setText(getContext().getString(text));
    }

    private void setAutoOnCheckbox(Light device) {
        CheckBox statusCheck = findViewById(R.id.device_auto_on_checkbox);
        statusCheck.setChecked(device.isAutoOn());
        statusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                device.setAutoOn(isChecked);
                setDeviceInformation(device);
            }
        });
    }
}
