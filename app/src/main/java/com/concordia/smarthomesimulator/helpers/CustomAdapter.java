package com.concordia.smarthomesimulator.helpers;
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
import java.util.HashMap;

/**
 * This code is based off/inspired by a tutorial named "ListView custom adapter"
 * by VoidRealms on Youtube
 * URL : https://www.youtube.com/watch?v=1olQnH9bE2c
 * */

public class CustomAdapter extends ArrayAdapter<ArrayList<LogEntry>> {

    ArrayList<LogEntry> items;
    Context ctx;

    public CustomAdapter(@NonNull Context context, int resource, ArrayList<LogEntry> list) {
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
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_item,null,true);
        TextView entry = (TextView)row.findViewById(R.id.entry);
        entry.setText(items.get(position).getComponent().toString() + ": " + items.get(position).getMessage().toString() + " " + items.get(position).getDateTime().toString() + " " +
        items.get(position).getImportance().toString());

        return row;
    }

}