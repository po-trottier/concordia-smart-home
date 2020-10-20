package com.concordia.smarthomesimulator.fragments.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.editMap.EditMapController;
import com.concordia.smarthomesimulator.adapters.MapInhabitantAdapter;
import com.concordia.smarthomesimulator.adapters.MapRoomAdapter;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;
import com.concordia.smarthomesimulator.helpers.FileHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MapController extends Fragment {
    private View view;
    private MapModel mapModel;
    private Context context;
    private HouseLayout houseLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        mapModel = new ViewModelProvider(this).get(MapModel.class);

        view = inflater.inflate(R.layout.fragment_map, container, false);

        setEditIntent();

        return view;
    }

    @Override
    public void onResume(){
        loadProperLayout();
        
        setMapDetails();
        setCustomRoomAdapter();
        setCustomInhabitantsAdapter();

        super.onResume();
    }

    private void loadProperLayout(){
        houseLayout = mapModel.loadHouseLayout(context);
        if (houseLayout == null){
            houseLayout = mapModel.loadDemoHouseLayout(context);
            mapModel.saveHouseLayout(context, houseLayout);
        }
    }

    private void setMapDetails() {
        ImageView layoutImage = view.findViewById(R.id.map_image);
        byte[] imageBytes = Base64.decode(houseLayout.getImage(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        layoutImage.setImageBitmap(decodedImage);
    }

    private void setCustomRoomAdapter() {
        ArrayList<Room> rooms = houseLayout.getRooms();
        MapRoomAdapter adapter = new MapRoomAdapter(context, 0, rooms);

        ListView roomList = view.findViewById(R.id.map_room_list);
        roomList.setAdapter(adapter);
    }

    private void setCustomInhabitantsAdapter() {
        ArrayList<Inhabitant> inhabitants = houseLayout.getInhabitants();
        MapInhabitantAdapter adapterInhabitant = new MapInhabitantAdapter(context, 0, inhabitants);

        ListView inhabitantList = view.findViewById(R.id.map_inhabitant_list);
        inhabitantList.setAdapter(adapterInhabitant);
    }

    private void setEditIntent() {
        FloatingActionButton fab = view.findViewById(R.id.fab_edit_map);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLogHelper.add(context, new LogEntry("House Layout", "Started editing the House Layout.", LogImportance.MINOR));
                Intent intent = new Intent(context, EditMapController.class);
                context.startActivity(intent);
            }
        });
    }
}
