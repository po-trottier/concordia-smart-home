package com.concordia.smarthomesimulator.dataModels;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.concordia.smarthomesimulator.R;

public class Light implements IDevice {

    private final Drawable iconOpened;
    private final Drawable iconClosed;
    private final int tintOpened;
    private final int tintClosed;
    private boolean isOpened = false;

    Light() {
        Resources res = Resources.getSystem();

        iconOpened = res.getDrawable(R.drawable.ic_lightbulb_on, null);
        iconClosed = res.getDrawable(R.drawable.ic_lightbulb_off, null);
        tintOpened = res.getColor(R.color.primary, null);
        tintClosed = res.getColor(R.color.accent, null);
    }

    @Override
    public Drawable getOpenedIcon() {
        return iconOpened;
    }

    @Override
    public Drawable getClosedIcon() {
        return iconClosed;
    }

    @Override
    public int getOpenedTint() {
        return tintOpened;
    }

    @Override
    public int getClosedTint() {
        return tintClosed;
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