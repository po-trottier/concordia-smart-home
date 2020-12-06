package com.concordia.smarthomesimulator.views.customMapSettingsView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.enums.DeviceType;
import com.concordia.smarthomesimulator.enums.Orientation;
import com.concordia.smarthomesimulator.factories.DeviceFactory;
import com.concordia.smarthomesimulator.helpers.LayoutsHelper;
import com.concordia.smarthomesimulator.interfaces.IDevice;
import com.concordia.smarthomesimulator.interfaces.IInhabitant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_GARAGE;
import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_OUTDOORS;

public class CustomMapSettingsModel {

    //region Properties

    private HouseLayout layout;
    private Room selectedRoom;
    private Room modifiedRoom;

    //endregion

    //region Constructors

    public CustomMapSettingsModel() { }

    //endregion

    //region Public Methods

    //region Layout Methods

    /**
     * Gets the layout to be used by the UI.
     *
     * @return the layout
     */
    public HouseLayout getLayout() {
        return layout;
    }

    /**
     * Sets layout.
     *
     * @param layout the layout
     */
    public void setLayout(HouseLayout layout) {
        this.layout = layout;
        if (layout.getRooms().size() > 0) {
            selectedRoom = getOrderedRooms().get(0);
        }
    }

    /**
     * Update the layout to be used by the UI.
     *
     * @param context the context
     */
    public void updateLayout(Context context) {
        layout = LayoutsHelper.getSelectedLayout(context);
        if (layout.getRooms().size() > 0) {
            selectedRoom = getOrderedRooms().get(0);
        }
    }

    //endregion

    //region Room Methods

    /**
     * Gets ordered rooms.
     *
     * @return the ordered rooms
     */
    public ArrayList<Room> getOrderedRooms() {
        HouseLayout clone = (HouseLayout) layout.clone();

        Room outdoors = clone.getRoom(DEFAULT_NAME_OUTDOORS);
        Room garage = clone.getRoom(DEFAULT_NAME_GARAGE);
        clone.removeRoom(DEFAULT_NAME_OUTDOORS);
        clone.removeRoom(DEFAULT_NAME_GARAGE);

        ArrayList<Room> rooms = clone.getRooms();
        rooms.sort(new SortByName());

        rooms.add(garage);
        rooms.add(outdoors);

        return rooms;
    }

    /**
     * Gets selected room.
     *
     * @return the selected room
     */
    public Room getSelectedRoom() {
        return selectedRoom;
    }

    /**
     * Sets selected room.
     *
     * @param room the room
     */
    public void setSelectedRoom(Room room) {
        selectedRoom = room;
    }

    //endregion

    //region Add Methods

    public void addRoom(Context context, String name, int x, int y, int width, int height) {
        // Verify that the name is unique
        if (layout.getRooms().stream().anyMatch(room -> room.getName().equalsIgnoreCase(name))) {
            Toast.makeText(context, context.getString(R.string.text_alert_room_error), Toast.LENGTH_LONG).show();
            return;
        }
        // Add the new room
        Room room = new Room(name, new Geometry(x, y, width, height));
        layout.addRoom(room);
        // Update the UI
        CustomMapSettingsView view = ((Activity) context).findViewById(R.id.edit_map_settings);
        view.updateView();
    }

    /**
     * Add device.
     *
     * @param context     the context
     * @param type        the type
     * @param x           the x
     * @param y           the y
     * @param orientation the orientation
     */
    public void addDevice(Context context, DeviceType type, int x, int y, Orientation orientation) {
        // Insure valid values
        if (type == null) {
            return;
        }
        // Build the device
        DeviceFactory factory = new DeviceFactory();
        Geometry geometry = new Geometry(x, y, orientation);
        IDevice device = factory.createDevice(type, geometry);
        // Check that the device is not on top of another one
        for (IDevice d : selectedRoom.getDevices()) {
            if (d.getGeometry().equals(geometry))
                return;
        }
        // Build the device
        selectedRoom.addDevice(device);
        // Update the layout
        layout.removeRoom(selectedRoom.getName());
        layout.addRoom(selectedRoom);
        // Update the UI
        CustomMapSettingsView view = ((Activity) context).findViewById(R.id.edit_map_settings);
        view.updateView();
    }

    /**
     * Add inhabitant and their intruder status.
     *
     * @param context the context
     * @param name    the name
     * @param isIntruder intruder status
     */
    public void addInhabitant(Context context, String name, boolean isIntruder) {
        // Verify that the name is valid
        if (name.length() < 1) {
            Toast.makeText(context, context.getString(R.string.add_inhabitant_invalid), Toast.LENGTH_LONG).show();
            return;
        }
        Inhabitant inhabitant = new Inhabitant(name, isIntruder);
        // Verify that its name is unique
        ArrayList<IInhabitant> inhabitants =  layout.getAllInhabitants();
        if (inhabitants.stream().anyMatch(i -> i.getName().equalsIgnoreCase(inhabitant.getName()))) {
            Toast.makeText(context, context.getString(R.string.add_inhabitant_used), Toast.LENGTH_LONG).show();
            return;
        }
        // Add the inhabitant to the room
        selectedRoom.addInhabitant(inhabitant);
        // Update the room in the layout
        layout.removeRoom(selectedRoom.getName());
        layout.addRoom(selectedRoom);
        // Update the UI
        CustomMapSettingsView view = ((Activity) context).findViewById(R.id.edit_map_settings);
        view.updateView();
    }

    /**
     * Add heating zone boolean.
     *
     * @param context the context
     * @param name    the name
     * @return the boolean
     */
    public boolean addHeatingZone(Context context, String name) {
        // Make sure the zone is unique
        if (layout.getHeatingZones().stream().anyMatch(i -> i.getName().equalsIgnoreCase(name.trim()))) {
            Toast.makeText(context, context.getString(R.string.add_zone_unique), Toast.LENGTH_LONG).show();
            return false;
        }
        // Add the zone
        HeatingZone zone = new HeatingZone(name.trim());
        layout.addHeatingZone(zone);
        // Update the UI
        CustomMapSettingsView view = ((Activity) context).findViewById(R.id.edit_map_settings);
        view.updateView();
        // Added successfully
        return true;
    }

    //endregion

    //region Dialog Methods

    /**
     * Remove device.
     *
     * @param context the context
     * @param device  the device
     */
    public void removeDevice(Context context, IDevice device) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.title_alert_remove_device_edit_map))
            .setMessage(context.getString(R.string.text_alert_remove_device_edit_map))
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.generic_remove, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedRoom.removeDevice(device);
                    // Replace the room in the layout
                    layout.removeRoom(selectedRoom.getName());
                    layout.addRoom(selectedRoom);
                    // Update the Settings UI
                    CustomMapSettingsView view = ((Activity) context).findViewById(R.id.edit_map_settings);
                    view.updateView();
                }
            })
            .create();
        dialog.show();
    }

    /**
     * Edit inhabitant.
     *
     * @param context    the context
     * @param inhabitant the inhabitant
     */
    public void editInhabitant(Context context, IInhabitant inhabitant) {
        View customView = setupCustomInhabitantView(context);
        final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.title_alert_edit_inhabitant_edit_map))
            .setView(customView)
            .setNegativeButton(android.R.string.cancel, null)
            .setNeutralButton(R.string.generic_remove, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeInhabitant(inhabitant);
                    // Update the Settings UI
                    CustomMapSettingsView view = ((Activity) context).findViewById(R.id.edit_map_settings);
                    view.updateView();
                }
            })
            .setPositiveButton(R.string.generic_move, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (modifiedRoom == null) {
                        Toast.makeText(context, context.getString(R.string.text_alert_error_inhabitant_edit_map), Toast.LENGTH_LONG).show();
                        return;
                    }
                    // Remove from the first room
                    removeInhabitant(inhabitant);
                    // Add to the modified room
                    modifiedRoom.addInhabitant(inhabitant);
                    // Update the layout
                    layout.removeRoom(modifiedRoom.getName());
                    layout.addRoom(modifiedRoom);
                    // Update the Settings UI
                    CustomMapSettingsView view = ((Activity) context).findViewById(R.id.edit_map_settings);
                    view.updateView();
                }
            }).create();
        dialog.show();
    }

    //endregion

    //endregion

    //region Private Methods

    private void removeInhabitant(IInhabitant inhabitant) {
        Room room = null;
        // Find the room the inhabitant is in
        for (Room r : layout.getRooms()) {
            if (r.getInhabitants().stream().anyMatch(i -> i.getName().equalsIgnoreCase(inhabitant.getName()))) {
                room = r;
                break;
            }
        }
        // Crash check
        if (room == null) {
            return;
        }
        // Remove the Inhabitant
        room.removeInhabitant(inhabitant.getName());
        // Replace the room with the new one
        layout.removeRoom(room.getName());
        layout.addRoom(room);
        selectedRoom = room;
    }

    private View setupCustomInhabitantView(Context context) {
        View customView = LayoutInflater.from(context).inflate(R.layout.alert_move_inhabitant, null);
        ListView roomList = customView.findViewById(R.id.alert_move_inhabitant_list);

        ArrayList<Room> rooms = getOrderedRooms();
        String[] roomNames = rooms
                .stream()
                .map(Room::getName)
                .collect(Collectors.toList())
                .toArray(new String[] {});

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, roomNames);
        roomList.setAdapter(adapter);

        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modifiedRoom = rooms.get(position);
                for (int i = 0; i < parent.getChildCount(); i ++) {
                    View child = parent.getChildAt(i);
                    child.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
                view.setBackgroundColor(context.getColor(R.color.accentFaded));
            }
        });

        return customView;
    }

    //endregion

    //region Inner Classes

    // Simple class used to sort rooms in ascending name order
    private static class SortByName implements Comparator<Room>
    {
        public int compare(Room a, Room b)
        {
            return a.getName().compareTo(b.getName());
        }
    }

    //endregion
}
