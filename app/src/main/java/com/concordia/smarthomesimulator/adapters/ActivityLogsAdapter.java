package com.concordia.smarthomesimulator.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import java.util.ArrayList;

/**
 * This code is based off/inspired by a tutorial named "ListView custom adapter"
 * by VoidRealms on Youtube
 * URL : https://www.youtube.com/watch?v=1olQnH9bE2c
 * <p>
 * ActivityLogAdapter serves as bridge between our data (activity logs) and the ListView within
 * the fragments_logs.xml file. From here the format of the text
 * and the color of the text to be added are determined.
 */
public class ActivityLogsAdapter extends ArrayAdapter<ArrayList<LogEntry>> {

    ArrayList<LogEntry> items;
    Context ctx;

    /**
     * Constructor for the ActivityLogAdapter
     *
     * @param context  application environment
     * @param resource
     * @param list list containing activity log entries to be inserted into items
     */
    public ActivityLogsAdapter(@NonNull Context context, int resource, ArrayList<LogEntry> list) {
        super(context, resource);
        items = list;
        ctx = context;
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View row = inflater.inflate(R.layout.adapter_logs_entry, parent, false);

        LogEntry entry = items.get(position);

        TextView componentText = row.findViewById(R.id.log_component_text);
        componentText.setText(entry.getComponent());

        TextView mainText = row.findViewById(R.id.log_main_text);
        mainText.setText(entry.getMessage());

        TextView dateTimeText = row.findViewById(R.id.log_date_time_text);
        dateTimeText.setText(entry.getDateTime().toString());

        TextView importanceText = row.findViewById(R.id.log_importance_text);
        importanceText.setText(entry.getImportance().toString());

        int textColor;
        switch(entry.getImportance()) {
            case CRITICAL:
                textColor = getContext().getColor(R.color.danger);;
                break;
            case IMPORTANT:
                // Orange
                textColor = getContext().getColor(R.color.accentDark);
                break;
            default:
                textColor = getContext().getColor(R.color.primary);;
                break;
        }

        mainText.setTextColor(textColor);
        importanceText.setTextColor(textColor);

        return row;
    }
}