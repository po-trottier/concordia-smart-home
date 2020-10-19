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
 *
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

    /**
     * @return number of logs stored
     */
    @Override
    public int getCount(){
        return items.size();
    }

    /**
     * This method allows for the inflation of the textView to be inserted
     * within the fragments_log ListView as an item.
     * Formatting of the text is also applied here.
     *
     * @param position index used for item position
     * @param convertView
     * @param parent
     * @return a list item
     */
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
                entryView.setTextColor(Color.rgb(228,123,0));
                break;
            default:
                entryView.setTextColor(Color.BLACK);
                break;
        }
        return row;
    }
}