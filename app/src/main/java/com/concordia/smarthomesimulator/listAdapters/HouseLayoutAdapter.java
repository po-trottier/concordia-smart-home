package com.concordia.smarthomesimulator.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;

import java.util.ArrayList;

public class HouseLayoutAdapter extends ArrayAdapter<ArrayList<HouseLayout>> {

    ArrayList<HouseLayout> layouts;
    Context context;

    /**
     * Constructor for the ActivityLogAdapter
     *
     * @param context  application environment
     * @param resource the resource ID
     * @param list list containing the different house layouts
     */
    public HouseLayoutAdapter(@NonNull Context context, int resource, ArrayList<HouseLayout> list) {
        super(context, resource);
        this.context = context;
        layouts = list;
    }

    @Override
    public int getCount(){
        return layouts.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.adapter_house_layout, parent, false);

        HouseLayout layout = layouts.get(position);

        TextView layoutName = row.findViewById(R.id.adapter_layout_name);
        layoutName.setText(layout.getName());

        return row;
    }
}