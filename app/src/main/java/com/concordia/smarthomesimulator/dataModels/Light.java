package com.concordia.smarthomesimulator.dataModels;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.concordia.smarthomesimulator.R;

public class Light implements IDevice {

    private final Resources res;
    private boolean isOpened = false;

    public Light() {
        res = Resources.getSystem();
    }

    @Override
    public Drawable getOpenedIcon() {
        return res.getDrawable(R.drawable.ic_lightbulb_on, null);
    }

    @Override
    public Drawable getClosedIcon() {
        return res.getDrawable(R.drawable.ic_lightbulb_off, null);
    }

    @Override
    public int getOpenedTint() {
        return res.getColor(R.color.primary, null);
    }

    @Override
    public int getClosedTint() {
        return res.getColor(R.color.accent, null);
    }

    @Override
    public boolean getIsOpened() {
        return isOpened;
    }

    @Override
    public void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }
}