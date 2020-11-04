package com.concordia.smarthomesimulator.fragments.map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.activities.editMap.EditMapController;
import com.concordia.smarthomesimulator.adapters.HouseLayoutAdapter;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.dataModels.LogImportance;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;
import com.concordia.smarthomesimulator.helpers.HouseLayoutHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MapController extends Fragment {

    private View view;
    private MapModel mapModel;
    private Context context;
    private LayoutInflater inflater;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        mapModel = new ViewModelProvider(this).get(MapModel.class);

        this.inflater = inflater;
        view = this.inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }

    @Override
    public void onResume(){
        updateContent();
        super.onResume();
    }

    private void updateContent() {
        mapModel.setHouseLayout(HouseLayoutHelper.getSelectedLayout(context));
        FrameLayout content = view.findViewById(R.id.map_fragment);
        if (mapModel.getHouseLayout() == null) {
            hideViews();
            content.removeAllViews();
            content.addView(inflater.inflate(R.layout.fragment_map_no_layout, null, false));
            setOpenIntent();
        } else {
            content.removeAllViews();
            content.addView(inflater.inflate(R.layout.fragment_map_with_layout, null, false));
            setBottomSheetBehavior();
            fillKnownInformation();
            setEditIntent();
        }
    }

    private void setOpenIntent() {
        Button openButton = view.findViewById(R.id.map_open_house_layout);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOpenDialog();
            }
        });
    }

    private void setEditIntent() {
        FloatingActionButton editMapFab = view.findViewById(R.id.fab_edit_map);
        editMapFab.setVisibility(View.VISIBLE);
        editMapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLogHelper.add(context, new LogEntry("House Layout", "Started editing the House Layout.", LogImportance.MINOR));
                Intent intent = new Intent(context, EditMapController.class);
                context.startActivity(intent);
            }
        });
    }

    private void setBottomSheetBehavior() {
        LinearLayout bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheet.setVisibility(View.VISIBLE);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);

        ImageView arrow = bottomSheet.findViewById(R.id.bottom_sheet_arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                else
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // If this is an intermittent state then ignore it
                if (newState != BottomSheetBehavior.STATE_COLLAPSED && newState != BottomSheetBehavior.STATE_EXPANDED)
                    return;

                // If the state hasn't changed don't start the animation
                if (newState == mapModel.getBottomCardState())
                    return;

                mapModel.setBottomCardState(newState);
                
                int angle = newState == BottomSheetBehavior.STATE_COLLAPSED ? 180 : 0;

                RotateAnimation rotate = new RotateAnimation(angle, 180 - angle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(300);
                rotate.setInterpolator(new FastOutSlowInInterpolator());
                rotate.setFillAfter(true);

                ImageView arrow = bottomSheet.findViewById(R.id.bottom_sheet_arrow);
                arrow.startAnimation(rotate);
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
        });
    }

    private void hideViews() {
        FloatingActionButton editMapFab = view.findViewById(R.id.fab_edit_map);
        editMapFab.setVisibility(View.GONE);

        LinearLayout infoBottomSheet = view.findViewById(R.id.bottom_sheet);
        infoBottomSheet.setVisibility(View.GONE);
    }

    private void fillKnownInformation() {
        HouseLayout layout = mapModel.getHouseLayout();

        TextView layoutName = view.findViewById(R.id.information_layout_name);
        layoutName.setText(layout.getName());

        TextView numberRooms = view.findViewById(R.id.information_number_rooms);
        numberRooms.setText(layout.getRooms().size() + " " + getString(R.string.map_active_layout_rooms_suffix));

        TextView numberDevices = view.findViewById(R.id.information_number_devices);
        int deviceCount = 0;
        for (Room room : layout.getRooms()) {
            deviceCount += room.getDevices().size();
        }
        numberDevices.setText(deviceCount + " " + getString(R.string.map_active_layout_devices_suffix));
    }

    private void setupOpenDialog() {
        HouseLayout backupLayout = mapModel.getHouseLayout();
        final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(getString(R.string.title_alert_open_layout))
            .setMessage(getString(R.string.text_alert_open_layout))
            .setView(setupCustomView())
            .setPositiveButton(R.string.generic_open, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HouseLayoutHelper.updateSelectedLayout(context, mapModel.getHouseLayout());
                    updateContent();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mapModel.setHouseLayout(backupLayout);
                    updateContent();
                }
            }).create();
        dialog.show();
    }

    private View setupCustomView() {
        final View customView = LayoutInflater.from(context).inflate(R.layout.alert_open_house_layout, null, false);
        final ListView layoutList = customView.findViewById(R.id.alert_open_layout_list);

        ArrayList<HouseLayout> layouts = HouseLayoutHelper.listSavedHouseLayouts(context);

        HouseLayoutAdapter adapter = new HouseLayoutAdapter(context, 0, layouts);
        layoutList.setAdapter(adapter);
        layoutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapModel.setHouseLayout(layouts.get(position));
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View child = parent.getChildAt(i);
                    child.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
                view.setBackgroundColor(context.getColor(R.color.accentFaded));
            }
        });

        return customView;
    }
}
