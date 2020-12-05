package com.concordia.smarthomesimulator.views.customMapView;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.helpers.LayoutsHelper;
import com.concordia.smarthomesimulator.interfaces.IDevice;
import com.concordia.smarthomesimulator.interfaces.IInhabitant;

import java.util.ArrayList;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_OUTDOORS;

public class CustomMapView extends View {

    //region Constants

    private final static float INHABITANT_SHIFT = 0.35f;
    private final static float INHABITANT_RADIUS = 30f;

    private final static float DEVICE_LIGHT_RADIUS = 30f;

    //endregion

    //region Properties

    private CustomMapModel model;

    private Context context;
    private Canvas canvas;

    private HouseLayout layout;

    private MapMeasurements measurements;

    private Paint strokePaint;
    private Paint fillPaint;
    private Paint fillOutdoorsPaint;
    private Paint inhabitantPaint;
    private Paint inhabitantTextPaint;
    private Paint intruderPaint;
    private Paint intruderTextPaint;
    private Paint textPaint;
    private Paint devicePaint;
    private Paint deviceStrokePaint;
    private Paint temperaturePaint;

    //endregion

    //region Constructors

    /**
     * Instantiates a new Custom map drawable.
     *
     * Important information:
     * - For best results, all rooms and devices in the layout should have a 1x1 aspect ratio (or close). For example, the layout could be of size 10x10
     * - Doors and windows are 1 unit long no matter the size of the layout so it's best to scale accordingly
     * - The garage and outdoors are automatically drawn at the bottom of the layout
     * - Rooms or devices with invalid coordinates and/or sizes will break the rendering (x = -1, etc.)
     * - Other than for these few key points, the layout should always scale accordingly to your device size
     *
     * @param context     the context
     */
    public CustomMapView(Context context) {
        super(context);
        initializeView(context);
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public CustomMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context);
    }

    //endregion

    //region Overrides

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Did the touch event occur inside a known shape ?
        if (model.queryClick(context, event, getPaddingStart()))
            return true;
        // No known shape was touched, let the original behaviour take place
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        this.canvas = canvas;

        setupCanvas();
        setupPaints();

        measurements = model.update(getWidth(), getHeight(), getPaddingStart(), layout);

        drawRooms(layout.getRooms());
    }

    //endregion

    //region Public Methods

    /**
     * Update the view by redrawing the canvas.
     */
    public void updateView() {
        this.layout = LayoutsHelper.getSelectedLayout(context);
        invalidate();
    }

    //endregion

    //region Private Methods

    //region Setup Methods

    private void initializeView(Context context) {
        this.context = context;
        this.model = new CustomMapModel();
        this.layout = LayoutsHelper.getSelectedLayout(context);
    }

    private void setupCanvas() {
        // make the padding double what it is on 1 side
        int padding = getPaddingStart() * 2;
        // Shift the canvas by half that size
        canvas.translate((float) padding / 2, (float) padding / 2);
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
        // Paint used to outline intruders
        intruderPaint = new Paint();
        intruderPaint.setStyle(Paint.Style.STROKE);
        intruderPaint.setStrokeWidth(6f);
        intruderPaint.setColor(Color.RED);
        // Paint used to write intruders's names
        intruderTextPaint = new Paint();
        intruderTextPaint.setStyle(Paint.Style.FILL);
        intruderTextPaint.setColor(Color.RED);
        intruderTextPaint.setTextSize(30f);
        intruderTextPaint.setSubpixelText(true);
        intruderTextPaint.setTextAlign(Paint.Align.CENTER);
        // Paint used to draw lights and doors
        devicePaint = new Paint();
        devicePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // Paint used to draw windows
        deviceStrokePaint = new Paint();
        deviceStrokePaint.setStyle(Paint.Style.STROKE);
        deviceStrokePaint.setStrokeWidth(6f);
        // Paint used to draw temperature status
        temperaturePaint = new Paint();
        temperaturePaint.setStyle(Paint.Style.FILL);
        temperaturePaint.setColor(Color.BLACK);
    }

    //endregion

    //region Draw Lists Methods

    private void drawRooms(ArrayList<Room> rooms) {
        // Draw the Rooms as the "Background"
        for (Room room : rooms) {
            drawRoom(room);
        }
        // Draw Inhabitants and Devices as the "Foreground"
        for (Room room : rooms) {
            float[] points = model.getPoints(room);
            // Center point of the Room
            float vertical = ((points[1] - points[3]) / 2f) + points[3];
            float horizontal = ((points[2] - points[0]) / 2f) + points[0];
            // Draw remaining shapes
            drawInhabitants(room.getInhabitants(), vertical, horizontal);
            drawDevices(room.getDevices());
        }
    }

    private void drawInhabitants(ArrayList<IInhabitant> inhabitants, float vertical, float horizontal) {
        // Shift the inhabitants down a little to space them from the Room Name Text
        vertical = vertical + ((INHABITANT_SHIFT / 1.5f) * measurements.getScaleY());
        // If there are more than 1 inhabitants, we need to shift the icons so they don't stack
        float adjustedHorizontal;
        for (int i = 0; i < inhabitants.size(); i++) {
            // If there's an even number of inhabitants, shift both so they are centered, otherwise, don't shift the middle one
            float shift = inhabitants.size() % 2 == 1 ? 1f : 0f;
            if (i < (inhabitants.size() / 2)) {
                // Inhabitants that are on the left of the center are shifted left
                adjustedHorizontal = horizontal - (INHABITANT_SHIFT * measurements.getScaleX() * (shift +  (float) (inhabitants.size() / 2) - i));
            } else if (i > (inhabitants.size() / 2)) {
                // Inhabitants that are on the right of the center are shifted right
                adjustedHorizontal = horizontal + (INHABITANT_SHIFT * measurements.getScaleX() * (shift + i - (float) (inhabitants.size() / 2)));
            } else {
                // Inhabitants that are in the center are only shifted if there's an even number of inhabitants
                adjustedHorizontal = horizontal + (INHABITANT_SHIFT * measurements.getScaleX() * Math.abs(1 - shift));
            }
            drawInhabitant(inhabitants.get(i), adjustedHorizontal, vertical);
        }
    }

    private void drawDevices(ArrayList<IDevice> devices) {
        for (IDevice device : devices) {
            // Get the color to use when drawing the device
            int color = device.getIsOpened() ? device.getOpenedTint() : device.getClosedTint();
            devicePaint.setColor(context.getColor(color));
            // Get the important coordinates for the device
            float[] points = model.getPoints(device);
            // Draw different shapes depending on the device type
            switch (device.getDeviceType()) {
                // Lights are circles with constant radii centered at the location given
                case LIGHT:
                    drawLight((Light) device, points);
                    break;
                // Windows are rectangle strokes and can be locked
                case WINDOW:
                    drawWindow((Window) device, points);
                    break;
                // Doors are filled rectangles
                case DOOR:
                    drawDoor((Door) device, points);
                    break;
                // The rest (?) are just printed with a regular fill
                default:
                    canvas.drawRect(points[0], points[1], points[2], points[3], devicePaint);
                    break;
            }
        }
    }

    //endregion

    //region Draw Single Shape Methods

    private void drawRoom(Room room) {
        float[] points = model.getPoints(room);
        // Draw a filled rectangle and an outline Stroke
        Paint paint = room.getName().equals(DEFAULT_NAME_OUTDOORS) ? fillOutdoorsPaint : fillPaint;
        canvas.drawRect(points[0], points[1], points[2], points[3], paint);
        canvas.drawRect(points[0], points[1], points[2], points[3], strokePaint);
        // Center point of the Room
        float nameVertical = ((points[1] - points[3]) / 2f) + points[3];
        float horizontal = ((points[2] - points[0]) / 2f) + points[0];
        // If there are inhabitant, shift the text up to leave some space
        if (room.getInhabitants().size() > 0) {
            nameVertical = nameVertical - (INHABITANT_SHIFT * measurements.getScaleY());
        }
        float tempVertical = nameVertical - ((INHABITANT_SHIFT + 0.15f) * measurements.getScaleY());
        // Write the name of the room
        canvas.drawText(room.getName(), horizontal, nameVertical, textPaint);
        // Add an HVAC status icon
        int size = (int) (0.65 * measurements.getScaleY());
        Bitmap bitmap = model.convertDrawableToBitmap(model.getVentilationDrawable(context, room));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, size, size, false);
        // Determine coordinates
        float left = horizontal - ((float) size / 2);
        float top = tempVertical - ((float)size * 0.8f);
        // Collect the new shape if it's not outdoors
        if (!room.getName().equals(DEFAULT_NAME_OUTDOORS)) {
            RectF shape = new RectF(left, top, left + size, top + size);
            model.addRoom(shape, room);
            // Draw the scaled bitmap
            canvas.drawBitmap(scaled, left, top, temperaturePaint);
        }
    }

    private void drawInhabitant(IInhabitant inhabitant, float x, float y) {
        // Define coordinates that will cover the circle
        float left = x - (0.25f * measurements.getScaleX());
        float top = y - (0.3f * measurements.getScaleY());
        float right = x + (0.25f * measurements.getScaleX());
        float bottom = y + (0.3f * measurements.getScaleY());
        // Create the rectangle to draw
        RectF shape = new RectF(left, top, right, bottom);
        // Collect the new shape
        model.addInhabitant(shape, inhabitant);
        // Create a paint that will be masked by the circle
        Paint masked = new Paint();
        masked.setStyle(Paint.Style.FILL);
        masked.setColor(Color.BLACK);
        masked.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST));
        //Set color for inhabitant
        Paint shapeColor;
        Paint textColor;
        if(inhabitant.isIntruder()){
             shapeColor = intruderPaint;
             textColor = intruderTextPaint;
        }
        else{
             shapeColor = inhabitantPaint;
             textColor = inhabitantTextPaint;
        }
        // Draw the new shape
        canvas.drawRect(shape, masked);
        canvas.drawCircle(x, y, INHABITANT_RADIUS, shapeColor);
        // Add the inhabitant's name's first letter in the middle of the circle
        String firstLetter = String.valueOf(inhabitant.getName().charAt(0)).toUpperCase();
        canvas.drawText(firstLetter, x, y + (INHABITANT_SHIFT * measurements.getScaleY() / 3), textColor);
    }

    private void drawLight(Light device, float[] points) {
        // Define coordinates that will cover the circle
        float left = points[0] + (0.25f * measurements.getScaleX());
        float top = points[1] - (0.75f * measurements.getScaleY());
        float right = points[0] + (0.75f * measurements.getScaleX());
        float bottom = points[1] - (0.25f * measurements.getScaleY());
        // Create the rectangle to draw
        RectF shape = new RectF(left, top, right, bottom);
        // Collect the new shape
        model.addLight(shape, device);
        // Create a paint that will be masked by the circle
        Paint masked = new Paint();
        masked.setStyle(Paint.Style.FILL);
        masked.setColor(Color.BLACK);
        masked.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // Draw the new shape
        canvas.drawRect(shape, masked);
        canvas.drawCircle(points[4], points[5], DEVICE_LIGHT_RADIUS, devicePaint);
    }

    private void drawWindow(Window device, float[] points) {
        if (device.getIsLocked()) {
            // If the window is locked change the paint color
            deviceStrokePaint.setColor(context.getColor(device.getLockedTint()));
        } else {
            // Get the color to use when drawing the device
            int color = device.getIsOpened() ? device.getOpenedTint() : device.getClosedTint();
            deviceStrokePaint.setColor(context.getColor(color));
        }
        // Create the rectangle to draw
        RectF shape = new RectF(points[0], points[1], points[2], points[3]);
        // Collect the new shape
        model.addWindow(shape, device);
        // Draw the new shape
        canvas.drawRect(shape, deviceStrokePaint);
    }

    private void drawDoor(Door device, float[] points) {
        if (device.getIsLocked()) {
            // If the window is locked change the paint color
            devicePaint.setColor(context.getColor(device.getLockedTint()));
        } else {
            // Get the color to use when drawing the device
            int color = device.getIsOpened() ? device.getOpenedTint() : device.getClosedTint();
            devicePaint.setColor(context.getColor(color));
        }
        // Create the rectangle to draw
        RectF shape = new RectF(points[0], points[1], points[2], points[3]);
        // Collect the new shape
        model.addDoor(shape, device);
        // Draw the new shape
        canvas.drawRect(shape, devicePaint);
    }

    //endregion

    //endregion
}