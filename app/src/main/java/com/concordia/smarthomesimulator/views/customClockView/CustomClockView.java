package com.concordia.smarthomesimulator.views.customClockView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextClock;

public class CustomClockView extends TextClock{

    public CustomClockView(Context context) {
        super(context);
        this.setDesigningText();
    }

    public CustomClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDesigningText();
    }

    public CustomClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setDesigningText();
    }

    private void setDesigningText() {
            // The default text is displayed when designing the interface.
            this.setText("00:00:00");
        }

        // Fix for a runtime error
        @Override
        protected void onAttachedToWindow() {
            try {
                super.onAttachedToWindow();
            } catch (Exception e)  {
                e.printStackTrace();
            }
        }
    }


