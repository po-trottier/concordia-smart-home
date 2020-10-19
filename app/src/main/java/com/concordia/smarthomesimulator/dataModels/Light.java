package com.concordia.smarthomesimulator.dataModels;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.concordia.smarthomesimulator.R;

public class Light implements IDevice {

    private boolean isOpened = false;

    public Light() {

    }

    @Override
    public int getOpenedIcon() {
        return R.drawable.ic_lightbulb_on;
    }

    @Override
    public int getClosedIcon() {
        return R.drawable.ic_lightbulb_off;
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
        return DeviceType.LIGHT;
    }
}