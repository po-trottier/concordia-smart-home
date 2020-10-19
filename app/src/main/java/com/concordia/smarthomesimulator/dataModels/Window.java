package com.concordia.smarthomesimulator.dataModels;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.concordia.smarthomesimulator.R;

public class Window implements IDevice {

    private final Resources res;
    private boolean isOpened = false;
    private boolean isLocked = false;

    public Window() {
        res = Resources.getSystem();
    }

    @Override
    public Drawable getOpenedIcon() {
        return res.getDrawable(R.drawable.ic_window_open_variant, null);
    }

    @Override
    public Drawable getClosedIcon() {
        return res.getDrawable(R.drawable.ic_window_closed_variant, null);
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
        return !isLocked && isOpened;
    }

    @Override
    public void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    /**
     * Gets locked icon.
     *
     * @return the locked icon
     */
    public Drawable getLockedIcon() {
        return res.getDrawable(R.drawable.ic_window_locked_variant, null);
    }

    /**
     * Gets locked tint.
     *
     * @return the locked tint
     */
    public int getLockedTint() {
        return res.getColor(R.color.charcoal, null);
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
