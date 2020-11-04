package com.concordia.smarthomesimulator.views.customMapView;

import android.graphics.RectF;
import android.view.MotionEvent;
import com.concordia.smarthomesimulator.dataModels.*;

import java.util.ArrayList;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_GARAGE;
import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_OUTDOORS;

public class CustomMapModel {

    //region Constants

    private final static float AVAILABLE_HEIGHT = 0.7f;
    private final static float RESERVED_HEIGHT = 1f - AVAILABLE_HEIGHT;

    private final static float SPACING = 0.5f;
    private final static float DEVICE_THICKNESS = 0.15f;

    private final static float DEFAULT_SCALING_MAX = 10f;

    //endregion

    //region Properties

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
    public boolean queryClick(MotionEvent event, int padding) {
        if (queryDevices(event, windows, padding))
            return true;
        if (queryDevices(event, doors, padding))
            return true;
        if (queryDevices(event, lights, padding))
            return true;
        if (queryInhabitants(event, inhabitants, padding))
            return true;
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
        float left;
        float top;
        float right;
        float bottom;
        // Garage door is the only device with X < 0
        if (g.getX() < 0) {
            // Find the top right point of the garage
            float roomX = (((maxX - minX) / 2f) - (SPACING / 4f)) * scaleX;
            float roomY = height - (height * RESERVED_HEIGHT) + (SPACING * scaleY);
            // Define a rectangle that is horizontally centered at the top of the garage
            left = (roomX / 2) - ((deviceWidth * scaleX) / 2);
            bottom = roomY - ((deviceHeight * scaleY) / 2);
            top = roomY + ((deviceHeight * scaleY) / 2);;
            right = (roomX / 2) + ((deviceWidth * scaleX) / 2);
        } else {
            // If this is not the garage door, just draw it where it should be
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
    public void addInhabitant(RectF shape, Inhabitant inhabitant) {
        this.inhabitants.add(new MapInhabitant(shape, inhabitant));
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

    private boolean queryDevices(MotionEvent event, ArrayList<MapDevice> devices, int padding) {
        // Shift the touch event do match the translation applied to the canvas
        int x = (int) event.getX() - padding;
        int y = (int) event.getY() - padding;
        // See if any of the devices is located on the touch area
        for (MapDevice device : devices) {
            if (device.getShape().contains(x, y)) {
                //  Only act on the event if the action is of type ACTION_UP (Finger lifted)
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // TODO: Do proper action
                    return true;
                }
                // A known shape was clicked
                return true;
            }
        }
        return false;
    }

    private boolean queryInhabitants(MotionEvent event, ArrayList<MapInhabitant> inhabitants, int padding) {
        // Shift the touch event do match the translation applied to the canvas
        int x = (int) event.getX() - padding;
        int y = (int) event.getY() - padding;
        // See if any of the devices is located on the touch area
        for (MapInhabitant inhabitant : inhabitants) {
            if (inhabitant.getShape().contains(x, y)) {
                //  Only act on the event if the action is of type ACTION_UP (Finger lifted)
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // TODO: Do proper action
                    return true;
                }
                // A known shape was clicked
                return true;
            }
        }
        return false;
    }

    //endregion

    //endregion
}
