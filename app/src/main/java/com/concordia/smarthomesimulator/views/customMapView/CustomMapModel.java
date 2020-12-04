package com.concordia.smarthomesimulator.views.customMapView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.enums.Action;
import com.concordia.smarthomesimulator.helpers.LayoutsHelper;
import com.concordia.smarthomesimulator.helpers.UserbaseHelper;
import com.concordia.smarthomesimulator.interfaces.IDevice;
import com.concordia.smarthomesimulator.interfaces.IInhabitant;
import com.concordia.smarthomesimulator.views.customDeviceAlertView.CustomDeviceAlertView;
import com.concordia.smarthomesimulator.views.customRoomAlertView.CustomRoomAlertView;

import java.util.ArrayList;

import static com.concordia.smarthomesimulator.Constants.*;

public class CustomMapModel {

    //region Constants

    private final static float AVAILABLE_HEIGHT = 0.7f;
    private final static float RESERVED_HEIGHT = 1f - AVAILABLE_HEIGHT;

    private final static float SPACING = 0.5f;
    private final static float DEVICE_THICKNESS = 0.25f;

    private final static float DEFAULT_SCALING_MAX = 10f;

    //endregion

    //region Properties

    private final ArrayList<MapRoom> rooms;
    private final ArrayList<MapDevice> windows;
    private final ArrayList<MapDevice> doors;
    private final ArrayList<MapDevice> lights;
    private final ArrayList<MapInhabitant> inhabitants;

    private float scaleX;
    private float scaleY;

    private float width;
    private float height;
    private float available;

    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    //endregion

    //region Constructors

    /**
     * Instantiates a new Custom map model.
     */
    public CustomMapModel() {
        this.rooms = new ArrayList<>();
        this.windows = new ArrayList<>();
        this.doors = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.inhabitants = new ArrayList<>();
        // Default values to insure we have the right range
        minX = Float.MAX_VALUE;
        minY = Float.MAX_VALUE;
        maxX = 0f;
        maxY = 0f;
    }

    //endregion

    //region Public Methods

    /**
     * Update map measurements.
     *
     * @param width   the width of the view
     * @param height  the height of the view
     * @param padding the padding around the view
     * @param layout  the house layout to draw
     * @return the map measurements
     */
    public MapMeasurements update(int width, int height, int padding, HouseLayout layout) {
        // Get the physical Canvas Size from the device
        this.width = width - (padding * 2);
        this.height = height - (padding * 2);
        this.available = AVAILABLE_HEIGHT * this.height;
        // Calculate bounds and scaling
        findLayoutBounds(layout);
        calculateScaleFactors();
        // Return the values to the View
        return new MapMeasurements(this.width, this.height, available, minX, maxX, minY, maxY, scaleX, scaleY);
    }

    /**
     * Query whether click event occurred on a known shape or not.
     *
     * @param event   the click event
     * @param padding the padding of the view (used to shift the coordinates)
     * @return whether a shape was found or not
     */
    public boolean queryClick(Context context, MotionEvent event, int padding) {
        if (queryRooms(context, event, rooms, padding) ||
            queryDevices(context, event, windows, padding) ||
            queryDevices(context, event, doors, padding) ||
            queryDevices(context, event, lights, padding) ||
            queryInhabitants(context, event, inhabitants, padding)) {
            return true;
        }
        return false;
    }

    /**
     * Get coordinate points.
     *
     * @param room the room to get the coordinate points for
     * @return the coordinate points required for drawing the room
     */
    public float[] getPoints(Room room) {
        Geometry g = room.getGeometry();
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;
        // If we have a default room, draw it accordingly
        if (g.getX() < 0) {
            switch (room.getName()) {
                // Draw the garage in the reserved space at the bottom left
                case DEFAULT_NAME_GARAGE:
                    left = 0;
                    bottom = height;
                    top = height - (height * RESERVED_HEIGHT) + (SPACING * scaleY);
                    right = (((maxX - minX) / 2f) - (SPACING / 4f)) * scaleX;
                    break;
                // Draw the outdoors "room" in the reserved space at the bottom right
                case DEFAULT_NAME_OUTDOORS:
                    left = width - (((maxX - minX) / 2f - (SPACING / 4f)) * scaleX);
                    bottom = height;
                    top = height - (height * RESERVED_HEIGHT) + (SPACING * scaleY);
                    right = width;
                    break;
            }
        } else {
            // Otherwise draw the room normally
            left = g.getX() * scaleX;
            top = available - ((g.getY() * scaleY) + (g.getHeight() * scaleY));
            right = (g.getX() * scaleX) + (g.getWidth() * scaleX);
            bottom = available - (g.getY() * scaleY);
        }
        // Return the points as an array of floating point coordinates
        return new float[] {left, top, right, bottom};
    }

    /**
     * Get coordinate points.
     *
     * @param device the device to get the coordinate points for
     * @return the coordinate points required for drawing the device
     */
    public float[] getPoints(IDevice device) {
        Geometry g = device.getGeometry();
        float x;
        float y;
        float deviceWidth;
        float deviceHeight;
        // Define the device size depending on its orientation
        switch (g.getOrientation()) {
            // If a device is horizontal, it has a width of 1 and height of DEVICE_THICKNESS.
            // It is also slightly shifted vertically to make it vertically centered on the layout walls
            case HORIZONTAL:
                deviceWidth = g.getWidth();
                deviceHeight = DEVICE_THICKNESS;
                x = g.getX();
                y = g.getY() - (deviceHeight / 2);
                break;
            // If a device is vertical, it has a height of 1 and width of DEVICE_THICKNESS.
            // It is also slightly shifted horizontally to make it vertically centered on the layout walls
            case VERTICAL:
                deviceWidth = DEVICE_THICKNESS;
                deviceHeight = g.getHeight();
                x = g.getX() - (deviceWidth / 2);
                y = g.getY();
                break;
            // If we ignore the device orientation, just keep the original sizes
            default:
                x = g.getX();
                y = g.getY();
                deviceWidth = g.getWidth();
                deviceHeight = g.getHeight();
                break;
        }
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;
        // Default devices are the only devices with X < 0
        if (g.getX() < 0) {
            switch (g.getX()) {
                // Garage Door
                case -1:
                    // Find the top right point of the garage
                    float garageDoorX = (((maxX - minX) / 2f) - (SPACING / 4f)) * scaleX;
                    float garageDoorY = height - (height * RESERVED_HEIGHT) + (SPACING * scaleY);
                    // Define a rectangle that is horizontally centered at the top of the garage
                    right = (garageDoorX / 2) + ((deviceWidth * scaleX) / 2);
                    bottom = garageDoorY + ((deviceHeight * scaleY) / 2);
                    left = (garageDoorX / 2) - ((deviceWidth * scaleX) / 2);
                    top = garageDoorY - ((deviceHeight * scaleY) / 2);
                    break;
                // Backyard Door
                case -2:
                    // Find the top right point of the backyard
                    float backyardDoorX = (maxX + 0.1f) * scaleX;
                    float backyardDoorY = height - (height * RESERVED_HEIGHT) + (SPACING * scaleY);
                    // Define a rectangle that is horizontally centered at the top of the garage
                    right = (backyardDoorX * 0.75f) + ((deviceWidth * scaleX) / 2);
                    bottom = backyardDoorY + ((deviceHeight * scaleY) / 2);
                    left = (backyardDoorX  * 0.75f) - ((deviceWidth * scaleX) / 2);
                    top = backyardDoorY - ((deviceHeight * scaleY) / 2);
                    break;
                // Garage Light
                case -3:
                    // Find the top right point of the garage
                    float garageLightX = (((maxX - minX) / 2f) - (SPACING / 4f)) * scaleX;
                    float garageLightY = height - (height * RESERVED_HEIGHT) + (SPACING * scaleY) + (3.5f * scaleY);
                    // Define a rectangle that is horizontally centered at the top of the garage
                    right = (garageLightX / 2) + ((deviceWidth * scaleX) / 2);
                    bottom = garageLightY + ((deviceHeight * scaleY) / 2);
                    left = (garageLightX / 2) - ((deviceWidth * scaleX) / 2);
                    top = garageLightY - ((deviceHeight * scaleY) / 2);
                    break;
                // Backyard Light
                case -4:
                    // Find the top right point of the backyard
                    float backyardLightX = (maxX + 0.1f) * scaleX;
                    float backyardLightY = height - (height * RESERVED_HEIGHT) + (SPACING * scaleY) + (3.5f * scaleY);
                    // Define a rectangle that is horizontally centered at the top of the garage
                    right = (backyardLightX * 0.75f) + ((deviceWidth * scaleX) / 2);
                    bottom = backyardLightY + ((deviceHeight * scaleY) / 2);
                    left = (backyardLightX  * 0.75f) - ((deviceWidth * scaleX) / 2);
                    top = backyardLightY - ((deviceHeight * scaleY) / 2);
                    break;
            }
        } else {
            // If this is not a default door, just draw it where it should be
            left = x * scaleX;
            top = available - ((y * scaleY) + (deviceHeight * scaleY));
            right = (x * scaleX) + (deviceWidth * scaleX);
            bottom = available - (y * scaleY);
        }
        // Find the middle point of the device (to be used by lights)
        float midX = left + (0.5f * scaleX);
        float midY = top - (0.5f * scaleY);
        // Return the values as an array of floating point coordinates
        return new float[] {left, top, right, bottom, midX, midY};
    }

    public void addRoom(RectF shape, Room room) {
        this.rooms.add(new MapRoom(shape, room));
    }

    /**
     * Add a window to the list of know drawn windows.
     *
     * @param shape  the shape of the device
     * @param device the device to save
     */
    public void addWindow(RectF shape, Window device) {
        this.windows.add(new MapDevice(shape, device));
    }

    /**
     * Add a door to the list of know drawn doors.
     *
     * @param shape  the shape of the device
     * @param device the device to save
     */
    public void addDoor(RectF shape, Door device) {
        this.doors.add(new MapDevice(shape, device));
    }

    /**
     * Add light to the list of know drawn lights.
     *
     * @param shape  the shape of the device
     * @param device the device to save
     */
    public void addLight(RectF shape, Light device) {
        this.lights.add(new MapDevice(shape, device));
    }

    /**
     * Add inhabitant to the list of know drawn inhabitants.
     *
     * @param shape      the shape of the inhabitant
     * @param inhabitant the inhabitant to save
     */
    public void addInhabitant(RectF shape, IInhabitant inhabitant) {
        this.inhabitants.add(new MapInhabitant(shape, inhabitant));
    }

    /**
     * Convert drawable to bitmap bitmap.
     *
     * @param drawable the drawable
     * @return the bitmap
     */
    public Bitmap convertDrawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public Drawable getVentilationDrawable(Context context, Room room) {
        switch (room.getVentilationStatus()) {
            case HEATING: return context.getDrawable(R.drawable.ic_whatshot);
            case COOLING: return context.getDrawable(R.drawable.ic_ac_unit);
            case PAUSED: return context.getDrawable(R.drawable.ic_pause_circle_outline);
            default: return context.getDrawable(R.drawable.ic_highlight_off);
        }
    }

    //endregion

    //region Private Methods

    //region Update Methods

    private void findLayoutBounds(HouseLayout layout) {
        // Find the min and max coordinates given to the layout to allow for proper scaling
        for (Room room : layout.getRooms()) {
            Geometry g = room.getGeometry();
            // Make sure not to include the -1 X and Y coordinates associated with Garage and Outdoors
            if (g.getX() < minX && g.getX() >= 0)
                minX = g.getX();
            if (g.getY() < minY && g.getY() >= 0)
                minY = g.getY();
            // Get the extreme max values
            if (g.getX() + g.getWidth() > maxX)
                maxX = g.getX() + g.getWidth();
            if (g.getY() + g.getHeight() > maxY)
                maxY = g.getY() + g.getHeight();
        }
    }

    private void calculateScaleFactors() {
        // Avoid default values
        if (minX == Float.MAX_VALUE || minY == Float.MAX_VALUE) {
            minX = 0;
            minY = 0;
        }
        // Avoid division by 0
        maxX = maxX - minX == 0 ? DEFAULT_SCALING_MAX : maxX;
        maxY = maxY - minY == 0 ? DEFAULT_SCALING_MAX : maxY;
        // How many pixels is a "tick" on our layout's scale
        scaleX = width / (maxX - minX);
        scaleY = available / (maxY - minY);
    }

    //endregion

    //region Click Query Methods

    private boolean queryRooms(Context context, MotionEvent event, ArrayList<MapRoom> rooms, int padding) {
        // Shift the touch event do match the translation applied to the canvas
        int x = (int) event.getX() - padding;
        int y = (int) event.getY() - padding;
        // See if any of the devices is located on the touch area
        for (MapRoom room : rooms) {
            if (room.getShape().contains(x, y)) {
                //  Only act on the event if the action is of type ACTION_UP (Finger lifted)
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showTemperatureDialog(context, room.getRoom());
                }
                return true;
            }
        }
        return false;
    }

    private boolean queryDevices(Context context, MotionEvent event, ArrayList<MapDevice> devices, int padding) {
        // Shift the touch event do match the translation applied to the canvas
        int x = (int) event.getX() - padding;
        int y = (int) event.getY() - padding;
        // See if any of the devices is located on the touch area
        for (MapDevice device : devices) {
            if (device.getShape().contains(x, y)) {
                //  Only act on the event if the action is of type ACTION_UP (Finger lifted)
                Action action = Action.fromDevice(device.getDevice(), context);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (action == null || UserbaseHelper.verifyPermissions(action, context)) {
                        showDeviceDialog(context, device.getDevice());
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean queryInhabitants(Context context, MotionEvent event, ArrayList<MapInhabitant> inhabitants, int padding) {
        // Shift the touch event do match the translation applied to the canvas
        int x = (int) event.getX() - padding;
        int y = (int) event.getY() - padding;
        // See if any of the devices is located on the touch area
        for (MapInhabitant inhabitant : inhabitants) {
            if (inhabitant.getShape().contains(x, y)) {
                //  Only act on the event if the action is of type ACTION_UP (Finger lifted)
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showInhabitantDialog(context, inhabitant.getInhabitant());
                    return true;
                }
                // A known shape was clicked
                return true;
            }
        }
        return false;
    }

    //endregion

    //region Show Dialog Methods

    private void showTemperatureDialog(Context context, Room room) {
        final CustomRoomAlertView customView = (CustomRoomAlertView) LayoutInflater.from(context).inflate(R.layout.alert_edit_room, null, false);
        customView.setRoomInformation(room);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.alert_map_room_title))
            .setView(customView)
            .setPositiveButton(android.R.string.ok, null);
        if (UserbaseHelper.verifyPermissions(Action.MODIFY_TEMPERATURE, context)) {
            // If user has permissions to modify temp, then show the modify button
            builder.setNeutralButton(R.string.generic_modify, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showTemperatureEditDialog(context, room);
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showTemperatureEditDialog(Context context, Room room) {
        // Make sure we don't edit the original device
        Room deepCopy = (Room) room.clone();

        final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.alert_map_room_edit_title) + " " + room.getName())
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.generic_save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO: Save the modified room
                }
            })
            .create();
        dialog.show();
    }

    private void showDeviceDialog(Context context, IDevice device) {
        // Make sure we don't edit the original device
        IDevice deepCopy = device.clone();

        final CustomDeviceAlertView customView = (CustomDeviceAlertView) LayoutInflater.from(context).inflate(R.layout.alert_edit_device, null, false);
        customView.setDeviceInformation(deepCopy);

        final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.alert_map_device_title) + " " + device.getDeviceType().toString().toLowerCase())
            .setView(customView)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HouseLayout layout = LayoutsHelper.getSelectedLayout(context);
                    // We don't know which room the user clicked in so we have to check in all of them
                    boolean found = false;
                    for (Room room : layout.getRooms()) {
                        // Try to find the device that was clicked on in the room
                        for (IDevice roomDevice : room.getDevices()) {
                            // If we find it, remove it from the room
                            if (roomDevice.equals(device)) {
                                room.removeDevice(roomDevice);
                                found = true;
                                break;
                            }
                        }
                        // Only add the modified one if we found the old one first
                        if (found) {
                            room.addDevice(customView.getDeviceInformation());
                            break;
                        }
                    }
                    LayoutsHelper.updateSelectedLayout(context, layout);
                    saveUpdatedLayout(context, layout);
                }
            })
            .create();
        dialog.show();
    }

    private void showInhabitantDialog(Context context, IInhabitant inhabitant) {
        String message = inhabitant.getName().toUpperCase() + " " + context.getString(R.string.alert_map_inhabitant_text);

        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String username = preferences.getString(PREFERENCES_KEY_USERNAME, "");
        if (inhabitant.getName().equalsIgnoreCase(username))
            message = context.getString(R.string.alert_map_inhabitant_text_self);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.alert_map_inhabitant_title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create();
        dialog.show();
    }

    private void showRenameLayoutDialog(Context context, HouseLayout layout) {
        final View customView = LayoutInflater.from(context).inflate(R.layout.alert_save_house_layout, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.title_alert_save_layout))
            .setMessage(context.getString(R.string.text_alert_save_layout_default))
            .setView(customView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(context.getString(R.string.generic_save), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText layoutName = customView.findViewById(R.id.alert_save_layout_name);
                    if (layoutName != null) {
                        layout.setName(layoutName.getText().toString().trim());
                        LayoutsHelper.updateSelectedLayout(context, layout);
                        if (LayoutsHelper.isLayoutNameUnique(context, layout)) {
                            saveUpdatedLayout(context, layout);
                        } else {
                            Toast.makeText(context, context.getString(R.string.error_exists_alert_save_layout), Toast.LENGTH_LONG).show();
                            showRenameLayoutDialog(context, layout);
                        }
                    }
                }
            }).create();
        dialog.show();
    }

    //endregion

    //region House Layout Methods

    private void saveUpdatedLayout(Context context, HouseLayout layout) {
        if (layout.getName().equalsIgnoreCase(DEMO_LAYOUT_NAME) || layout.getName().equalsIgnoreCase(EMPTY_LAYOUT_NAME)) {
            showRenameLayoutDialog(context, layout);
            return;
        }

        if (!LayoutsHelper.saveHouseLayout(context, layout)) {
            Toast.makeText(context, context.getString(R.string.error_unknown_alert_save_layout), Toast.LENGTH_LONG).show();
            return;
        }

        // Clear the know shapes
        windows.clear();
        doors.clear();
        lights.clear();
        inhabitants.clear();

        // Update the canvas
        CustomMapView view = ((Activity) context).findViewById(R.id.custom_map_view);
        view.updateView();
    }

    //endregion

    //endregion
}
