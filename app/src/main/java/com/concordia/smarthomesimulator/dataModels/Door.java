package com.concordia.smarthomesimulator.dataModels;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.concordia.smarthomesimulator.R;

public class Door implements IDevice {

    private boolean isOpened = false;

    public Door() {

    }

    @Override
    public int getOpenedIcon() {
        return R.drawable.ic_door_open;
    }

    @Override
    public int getClosedIcon() {
        return R.drawable.ic_door;
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
        return isOpened;
    }

    @Override
    public void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DOOR;
    }
}
