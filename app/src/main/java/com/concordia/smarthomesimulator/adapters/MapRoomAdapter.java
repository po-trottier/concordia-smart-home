package com.concordia.smarthomesimulator.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.DeviceType;
import com.concordia.smarthomesimulator.dataModels.IDevice;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.dataModels.Window;

import java.util.List;

public class MapRoomAdapter extends ArrayAdapter<Room> {

    Context context;
    List<Room> rooms;

    public MapRoomAdapter(@NonNull Context context, int resource, @NonNull List<Room> rooms) {
        super(context, resource, rooms);
        this.context = context;
        this.rooms = rooms;
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.adapter_map_room, parent, false);

        Room room = rooms.get(position);

        TextView roomName = row.findViewById(R.id.adapter_room_name);
        roomName.setText(room.getName());

        LinearLayout deviceList = row.findViewById(R.id.adapter_device_list);
        for (IDevice device : room.getDevices()){
            View child = inflater.inflate(R.layout.adapter_map_device, null);

            DeviceType type = device.getDeviceType();

            ImageView deviceIcon = child.findViewById(R.id.adapter_device_image);
            TextView deviceStatus = child.findViewById(R.id.adapter_device_status);

            if(type == DeviceType.WINDOW && ((Window) device).getIsLocked()){
                deviceIcon.setImageResource(((Window) device).getLockedIcon());
                deviceIcon.setColorFilter(context.getColor(((Window) device).getLockedTint()), PorterDuff.Mode.SRC_IN);

                deviceStatus.setText(context.getString(R.string.map_locked));
            } else {
                int tintResource = device.getIsOpened() ? device.getOpenedTint() : device.getClosedTint();
                deviceIcon.setImageResource(device.getIsOpened() ? device.getOpenedIcon() : device.getClosedIcon());
                deviceIcon.setColorFilter(context.getColor(tintResource), PorterDuff.Mode.SRC_IN);

                deviceStatus.setText(device.getIsOpened() ? context.getString(R.string.map_opened) : context.getString(R.string.map_closed));
            }

            TextView deviceType = child.findViewById(R.id.adapter_device_type_text);
            String deviceTypeText;
            switch (type) {
                case LIGHT:
                    deviceTypeText = context.getString(R.string.map_light);
                    break;
                case DOOR:
                    deviceTypeText = context.getString(R.string.map_door);
                    break;
                case WINDOW:
                    deviceTypeText = context.getString(R.string.map_window);
                    break;
                default:
                    deviceTypeText = context.getString(R.string.default_device_name);
            }
            deviceType.setText(deviceTypeText);

            deviceList.addView(child);
        }

        return row;
    }
}
