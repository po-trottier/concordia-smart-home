package com.concordia.smarthomesimulator.dataModels;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.concordia.smarthomesimulator.R;

public class Door implements IDevice {

    private final Resources res;
    private boolean isOpened = false;

    public Door() {
        res = Resources.getSystem();
    }

    @Override
    public Drawable getOpenedIcon() {
        return res.getDrawable(R.drawable.ic_door_open, null);
    }

    @Override
    public Drawable getClosedIcon() {
        return res.getDrawable(R.drawable.ic_door, null);
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
