package com.concordia.smarthomesimulator.views.customDateTimeView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.concordia.smarthomesimulator.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.concordia.smarthomesimulator.Constants.DATE_FORMAT;
import static com.concordia.smarthomesimulator.Constants.TIME_FORMAT;

/**
 * The type Custom clock view.
 */
public class CustomDateTimeView extends LinearLayout {

    LocalDateTime dateTime = null;

    /**
     * Instantiates a new Custom clock view.
     *
     * @param context the context
     */
    public CustomDateTimeView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Custom clock view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomDateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Custom clock view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CustomDateTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets date time.
     *
     * @param dateTime the date time
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        fillKnowInformation();
    }

    private void fillKnowInformation() {
        TextView time = findViewById(R.id.dashboard_time);
        TextView date = findViewById(R.id.dashboard_date);

        if (time == null || date == null) {
            return;
        }

        time.setText(dateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        date.setText(dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
    }
}


