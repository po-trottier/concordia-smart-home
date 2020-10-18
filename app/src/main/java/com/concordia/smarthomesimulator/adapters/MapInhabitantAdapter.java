package com.concordia.smarthomesimulator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.IDevice;
import com.concordia.smarthomesimulator.dataModels.Inhabitant;
import com.concordia.smarthomesimulator.dataModels.Room;

import java.util.List;

public class MapInhabitantAdapter  extends ArrayAdapter<Inhabitant> {

    Context context;
    List<Inhabitant> inhabitants;

    public MapInhabitantAdapter(@NonNull Context context, int resource, @NonNull List<Inhabitant> inhabitants) {
        super(context, resource, inhabitants);
        this.context = context;
        this.inhabitants = inhabitants;
    }

    @Override
    public int getCount() {
        return inhabitants.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.adapter_map_inhabitant, parent, false);

        Inhabitant inhabitant = inhabitants.get(position);

        TextView inhabitantName = row.findViewById(R.id.adapter_inhabitant_name);
        inhabitantName.setText(inhabitant.getName());
        TextView inhabitantRoom = row.findViewById(R.id.adapter_inhabitant_room);

        if (inhabitant.getRoom() == null){
            inhabitantRoom.setText("Outside");
        }
        else {
        inhabitantRoom.setText(inhabitant.getRoom().getName());
        }

        return row;
    }
}
