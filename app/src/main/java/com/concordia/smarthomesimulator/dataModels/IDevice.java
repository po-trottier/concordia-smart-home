package com.concordia.smarthomesimulator.dataModels;

import android.graphics.drawable.Drawable;

public interface IDevice {

    public Drawable getOpenedIcon();
    public Drawable getClosedIcon();

    public int getOpenedTint();
    public int getClosedTint();

    public boolean getIsOpened();
    public void setIsOpened(boolean isOpened);
}
