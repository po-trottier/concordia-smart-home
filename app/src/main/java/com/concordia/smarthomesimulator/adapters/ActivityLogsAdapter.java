package com.concordia.smarthomesimulator.adapters;
import android.content.Context;
import android.graphics.Color;
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
 * */

public class ActivityLogsAdapter extends ArrayAdapter<ArrayList<LogEntry>> {

    ArrayList<LogEntry> items;
    Context ctx;

    /**
     * ActivityLogAdapter serves as bridge between our data (activity logs) and the ListView within
     * the fragments_logs.xml file. From here the format of the text
     * and the color of the text to be added are determined.
     * */

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
        View row = inflater.inflate(R.layout.activity_log_list_item, parent, false);
        TextView entryView = (TextView)row.findViewById(R.id.entry);
        LogEntry entry = items.get(position);
        entryView.setText(entry.getComponent().toString() + ": " + entry.getMessage().toString() + " " + entry.getDateTime().toString() + " " + entry.getImportance().toString());

        switch(entry.getImportance()) {
            case CRITICAL:
                entryView.setTextColor(Color.RED);
                break;
            case IMPORTANT:
                entryView.setTextColor(Color.BLACK);
                break;
            default:
                entryView.setTextColor(Color.GRAY);
                break;
        }
        return row;
    }

}