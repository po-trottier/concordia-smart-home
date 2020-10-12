package com.concordia.smarthomesimulator.fragments.map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MapController extends Fragment {
    private View view;
    private MapModel mapModel;
    private Context context;
    private HouseLayout houseLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        mapModel = new ViewModelProvider(this).get(MapModel.class);
        mapModel.setContext(context);

        // TODO: Replace these lines. This is for testing purpose only
        houseLayout = new HouseLayout(
            "House Layout Test",
            "data:image/gif;base64,R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs=",
            1.3f,
            1.5f
        );

        view = inflater.inflate(R.layout.fragment_map, container, false);

        // TODO: Remove these lines (part of the default activity)
        final TextView textView = view.findViewById(R.id.text_map);
        mapModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Set the Save Button behaviour
        final Button saveButton = view.findViewById(R.id.save_layout_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mapModel.saveHouseLayout(houseLayout))
                    Snackbar.make(view, R.string.snackbar_save_error, BaseTransientBottomBar.LENGTH_LONG).show();
                else
                    Snackbar.make(view, R.string.snackbar_save_success, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

        // Set the Load Button behaviour
        final Button loadButton = view.findViewById(R.id.load_layout_button);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseLayout before = houseLayout;
                houseLayout = mapModel.loadHouseLayout();
                if (houseLayout == null || before == houseLayout)
                    Snackbar.make(view, R.string.snackbar_load_error, BaseTransientBottomBar.LENGTH_LONG).show();
                else
                    Snackbar.make(view, R.string.snackbar_load_success, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

        return view;
    }
}