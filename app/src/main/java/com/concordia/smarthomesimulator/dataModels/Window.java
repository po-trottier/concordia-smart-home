package com.concordia.smarthomesimulator.dataModels;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.concordia.smarthomesimulator.R;

public class Window implements IDevice {

    private final Drawable iconOpened;
    private final Drawable iconClosed;
    private final Drawable iconLocked;
    private final int tintOpened;
    private final int tintClosed;
    private final int tintLocked;
    private boolean isOpened = false;
    private boolean isLocked = false;

    Window() {
        Resources res = Resources.getSystem();

        iconOpened = res.getDrawable(R.drawable.ic_window_open_variant, null);
        iconClosed = res.getDrawable(R.drawable.ic_window_closed_variant, null);
        iconLocked = res.getDrawable(R.drawable.ic_window_locked_variant, null);
        tintOpened = res.getColor(R.color.primary, null);
        tintClosed = res.getColor(R.color.accent, null);
        tintLocked = res.getColor(R.color.charcoal, null);
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
        return !isLocked && isOpened;
    }

    @Override
    public void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    public Drawable getLockedIcon() {
        return iconLocked;
    }

    public int getLockedTint() {
        return tintLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}
