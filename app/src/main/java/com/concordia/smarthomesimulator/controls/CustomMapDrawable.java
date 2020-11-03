package com.concordia.smarthomesimulator.controls;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;

import java.util.ArrayList;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_GARAGE;
import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_OUTDOORS;

public class CustomMapDrawable extends Drawable {

    private final static int DEFAULT_SCALING_MAX = 10;

    private final static float AVAILABLE_HEIGHT = 0.7f;
    private final static float RESERVED_HEIGHT = 1f - AVAILABLE_HEIGHT;

    private final static float SPACING = 0.5f;

    private final static float ICON_SHIFT = 0.3f;
    private final static float ICON_RADIUS = 30f;

    private final static float DEVICE_THICKNESS = 0.2f;
    private final static float DEVICE_RADIUS = 25f;

    private final Context context;
    private Canvas canvas;

    private final HouseLayout houseLayout;

    private float scaleX;
    private float scaleY;

    private float width;
    private float height;
    private float availableHeight;

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    private Paint strokePaint;
    private Paint fillPaint;
    private Paint fillOutdoorsPaint;
    private Paint inhabitantPaint;
    private Paint inhabitantTextPaint;
    private Paint textPaint;
    private Paint devicePaint;
    private Paint deviceStrokePaint;

    public CustomMapDrawable(Context context, HouseLayout houseLayout) {
        this.context = context;
        this.houseLayout = houseLayout;

        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        maxX = 0;
        maxY = 0;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        this.canvas = canvas;

        findCanvasSize();
        findLayoutBounds();
        calculateScaleFactors();
        setupPaints();

        drawRooms(houseLayout.getRooms());
    }

    @Override
    public void setAlpha(int alpha) { }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) { }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    private void drawRooms(ArrayList<Room> rooms) {
        // Draw the Rooms as the "Background"
        for (Room room : rooms) {
            float[] points = getPoints(room);
            // Draw a filled rectangle and an outline Stroke
            Paint dynamicPaint = room.getName().equals(DEFAULT_NAME_OUTDOORS) ? fillOutdoorsPaint : fillPaint;
            canvas.drawRect(points[0], points[1], points[2], points[3], dynamicPaint);
            canvas.drawRect(points[0], points[1], points[2], points[3], strokePaint);
            // Center point of the Room
            float vertical = ((points[1] - points[3]) / 2f) + points[3];
            float horizontal = ((points[2] - points[0]) / 2f) + points[0];
            // If there are inhabitant, shift the text up to leave some space
            if (room.getInhabitants().size() > 0)
                vertical = vertical - (ICON_SHIFT * scaleY);
            // Write the name of the room
            canvas.drawText(room.getName(), horizontal, vertical, textPaint);
        }
        // Draw Inhabitants and Devices as the "Foreground"
        for (Room room : rooms) {
            float[] points = getPoints(room);
            // Center point of the Room
            float vertical = ((points[1] - points[3]) / 2f) + points[3];
            float horizontal = ((points[2] - points[0]) / 2f) + points[0];
            // Draw remaining shapes
            drawInhabitants(room.getInhabitants(), vertical, horizontal);
            drawDevices(room.getDevices());
        }
    }

    private void drawInhabitants(ArrayList<Inhabitant> inhabitants, float vertical, float horizontal) {
        // Shift the inhabitants down a little to space them from the Room Name Text
        vertical = vertical + (ICON_SHIFT * scaleY);
        // If there are more than 1 inhabitants, we need to shift the icons so they don't stack
        float adjustedHorizontal;
        for (int i = 0; i < inhabitants.size(); i++) {
            // If there's an even number of inhabitants, shift both so they are centered, otherwise, don't shift the middle one
            float shift = inhabitants.size() % 2 == 1 ? 1f : 0f;
            if (i < (inhabitants.size() / 2)) {
                // Inhabitants that are on the left of the center are shifted left
                adjustedHorizontal = horizontal - (ICON_SHIFT * scaleX * (shift +  (float) (inhabitants.size() / 2) - i));
            } else if (i > (inhabitants.size() / 2)) {
                // Inhabitants that are on the right of the center are shifted right
                adjustedHorizontal = horizontal + (ICON_SHIFT * scaleX * (shift + i - (float) (inhabitants.size() / 2)));
            } else {
                // Inhabitants that are in the center are only shifted if there's an even number of inhabitants
                adjustedHorizontal = horizontal + (ICON_SHIFT * scaleX * Math.abs(1 - shift));
            }
            // Draw a filled circle
            canvas.drawCircle(adjustedHorizontal, vertical, ICON_RADIUS, inhabitantPaint);
            canvas.drawCircle(adjustedHorizontal, vertical, ICON_RADIUS, fillPaint);
            // Add the inhabitant's name's first letter in the middle of the circle
            String firstLetter = String.valueOf(inhabitants.get(i).getName().charAt(0)).toUpperCase();
            canvas.drawText(firstLetter, adjustedHorizontal, vertical + (ICON_SHIFT * scaleY / 3), inhabitantTextPaint);
        }
    }

    private void drawDevices(ArrayList<IDevice> devices) {
        for (IDevice device : devices) {
            // Get the color to use when drawing the device
            int color = device.getIsOpened() ? device.getOpenedTint() : device.getClosedTint();
            devicePaint.setColor(context.getColor(color));
            // Get the important coordinates for the device
            float[] points = getPoints(device);
            // Draw different shapes depending on the device type
            switch (device.getDeviceType()) {
                // Lights are circles with constant radii centered at the location given
                case LIGHT:
                    canvas.drawCircle(points[4], points[5], DEVICE_RADIUS, devicePaint);
                    break;
                // Windows are rectangle strokes and can be locked
                case WINDOW:
                    Window window = (Window) device;
                    color = window.getIsLocked() ? window.getLockedTint() : color;
                    deviceStrokePaint.setColor(context.getColor(color));
                    canvas.drawRect(points[0], points[1], points[2], points[3], deviceStrokePaint);
                    break;
                // The rest (Doors) are filled rectangles
                default:
                    canvas.drawRect(points[0], points[1], points[2], points[3], devicePaint);
                    break;
            }
        }
    }

    private float[] getPoints(Room room) {
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
            top = availableHeight - ((g.getY() * scaleY) + (g.getHeight() * scaleY));
            right = (g.getX() * scaleX) + (g.getWidth() * scaleX);
            bottom = availableHeight - (g.getY() * scaleY);
        }
        // Return the points as an array of floating point coordinates
        return new float[] {left, top, right, bottom};
    }

    private float[] getPoints(IDevice device) {
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
            top = availableHeight - ((y * scaleY) + (deviceHeight * scaleY));
            right = (x * scaleX) + (deviceWidth * scaleX);
            bottom = availableHeight - (y * scaleY);
        }
        // Find the middle point of the device (to be used by lights)
        float midX = left + (0.5f * scaleX);
        float midY = top - (0.5f * scaleY);
        // Return the values as an array of floating point coordinates
        return new float[] {left, top, right, bottom, midX, midY};
    }

    private void findCanvasSize() {
        // Get the physical Canvas Size from the device
        width = getBounds().width();
        height = getBounds().height();
        availableHeight = AVAILABLE_HEIGHT * height;
    }

    private void findLayoutBounds() {
        // Find the min and max coordinates given to the layout to allow for proper scaling
        for (Room room : houseLayout.getRooms()) {
            Geometry g = room.getGeometry();
            // Make sure not to include the -1 X and Y coordinates associated with Garage and Outdors
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
        if (minX == Integer.MAX_VALUE || minY == Integer.MAX_VALUE) {
            minX = 0;
            minY = 0;
        }
        // Avoid division by 0
        maxX = maxX - minX == 0 ? DEFAULT_SCALING_MAX : maxX;
        maxY = maxY - minY == 0 ? DEFAULT_SCALING_MAX : maxY;
        // How many pixels is a "tick" on our layout's scale
        scaleX = width / (float) (maxX - minX);
        scaleY = availableHeight / (float) (maxY - minY);
    }

    private void setupPaints() {
        // Stroke paint used to outline rooms
        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(4f);
        strokePaint.setColor(context.getColor(R.color.charcoal));
        // Fill paint used to fill in rooms
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.BLACK);
        fillPaint.setAlpha(8);
        // Paint used to write room names
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
        textPaint.setFakeBoldText(true);
        textPaint.setSubpixelText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        // Special paint used to fill outdoors room
        fillOutdoorsPaint = new Paint();
        fillOutdoorsPaint.setStyle(Paint.Style.FILL);
        fillOutdoorsPaint.setColor(context.getColor(R.color.primaryFaded));
        fillOutdoorsPaint.setAlpha(96);
        // Paint used to outline inhabitants
        inhabitantPaint = new Paint();
        inhabitantPaint.setStyle(Paint.Style.STROKE);
        inhabitantPaint.setStrokeWidth(6f);
        inhabitantPaint.setColor(Color.DKGRAY);
        // Paint used to write inhabitant's names
        inhabitantTextPaint = new Paint();
        inhabitantTextPaint.setStyle(Paint.Style.FILL);
        inhabitantTextPaint.setColor(Color.BLACK);
        inhabitantTextPaint.setTextSize(30f);
        inhabitantTextPaint.setSubpixelText(true);
        inhabitantTextPaint.setTextAlign(Paint.Align.CENTER);
        // Paint used to draw lights and doors
        devicePaint = new Paint();
        devicePaint.setStyle(Paint.Style.FILL);
        // Paint used to draw windows
        deviceStrokePaint = new Paint();
        deviceStrokePaint.setStyle(Paint.Style.STROKE);
        deviceStrokePaint.setStrokeWidth(6f);
    }

    /**
     * Receive a click event and post proper action as a result.
     *
     * @param imageX the x coordinate of the click relative to the image
     * @param imageY the y coordinate of the click relative to the image
     */
    public void receiveClick(int imageX, int imageY) {
        // TODO Figure out why this doesn't work
        Paint test = new Paint();
        test.setStyle(Paint.Style.FILL);
        test.setColor(Color.RED);
        canvas.drawCircle(imageX, imageY, 40f, test);
    }
}