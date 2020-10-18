package com.concordia.smarthomesimulator.dataModels;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.concordia.smarthomesimulator.R;

public class Window implements IDevice {

    private boolean isOpened = false;
    private boolean isLocked = false;

    public Window() {
    }

    @Override
    public int getOpenedIcon() {
        return R.drawable.ic_window_open_variant;
    }

    @Override
    public int getClosedIcon() {
        return R.drawable.ic_window_closed_variant;
    }

    @Override
    public int getOpenedTint() {
        return R.color.primary;
    }

    @Override
    public int getClosedTint() {
        return R.color.accent;
    }

    @Override
    public boolean getIsOpened() {
        return !isLocked && isOpened;
    }

    @Override
    public void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.WINDOW;
    }

    /**
     * Gets locked icon.
     *
     * @return the locked icon
     */
    public int getLockedIcon() {
        return R.drawable.ic_window_locked_variant;
    }

    /**
     * Gets locked tint.
     *
     * @return the locked tint
     */
    public int getLockedTint() {
        return R.color.charcoal;
    }

    /**
     * Gets is locked.
     *
     * @return is locked
     */
    public boolean getIsLocked() {
        return isLocked;
    }

    /**
     * Sets is locked.
     *
     * @param isLocked the is locked
     */
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}
