package com.concordia.smarthomesimulator.controls;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.dataModels.*;

import java.util.ArrayList;

import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_GARAGE;
import static com.concordia.smarthomesimulator.Constants.DEFAULT_NAME_OUTDOORS;

public class CustomHouseLayout extends Drawable {

    private final static double AVAILABLE_HEIGHT = 0.7;
    private final static double RESERVED_HEIGHT = 1 - AVAILABLE_HEIGHT;
    private final static double SPACING = 0.5;
    private final static double ICON_SHIFT = 0.3;
    private final static double ICON_RADIUS = 30;

    private Canvas canvas;

    private int width;
    private int height;
    private double availableHeight;

    private final HouseLayout houseLayout;

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    private double scaleX = 1;
    private double scaleY = 1;

    private Paint stroke;
    private Paint fill;
    private Paint icon;
    private Paint iconText;
    private Paint text;

    public CustomHouseLayout(HouseLayout houseLayout) {
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

        for (Room room : houseLayout.getRooms()) {
            drawRoom(room);
        }
    }

    @Override
    public void setAlpha(int alpha) { }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) { }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public void receiveClick(int imageX, int imageY) {
        // TODO Figure out why this doesn't work
        Paint test = new Paint();
        test.setStyle(Paint.Style.FILL);
        test.setColor(Color.RED);
        canvas.drawCircle(imageX, imageY, 40, test);
    }

    private void drawRoom(Room room) {
        float[] points = getPoints(room);

        canvas.drawRect(points[0], points[1], points[2], points[3], fill);
        canvas.drawRect(points[0], points[1], points[2], points[3], stroke);

        float vertical = ((points[1] - points[3]) / 2) + points[3];
        float horizontal = ((points[2] - points[0]) / 2) + points[0];

        if (room.getInhabitants().size() > 0)
            vertical = (float) (vertical - (ICON_SHIFT * scaleY));

        canvas.drawText(room.getName(), horizontal, vertical, text);

        drawInhabitants(room.getInhabitants(), vertical, horizontal);
    }

    private void drawInhabitants(ArrayList<Inhabitant> inhabitants, float vertical, float horizontal) {
        vertical = (float) (vertical + (ICON_SHIFT * scaleY * 1.5));

        float adjustedHorizontal;
        for (int i = 0; i < inhabitants.size(); i++) {
            float shift = inhabitants.size() % 2 == 1 ? 1 : 0;
            if (i < (inhabitants.size() / 2)) {
                adjustedHorizontal = (float) (horizontal - (ICON_SHIFT * scaleX * (shift + (inhabitants.size() / 2) - i)));
            } else if (i > (inhabitants.size() / 2)) {
                adjustedHorizontal = (float) (horizontal + (ICON_SHIFT * scaleX * (i - (inhabitants.size() / 2) + 1)));
            } else {
                adjustedHorizontal = (float) (horizontal + (ICON_SHIFT * scaleX * Math.abs(1 - shift)));
            }
            canvas.drawCircle(adjustedHorizontal, vertical, (float) ICON_RADIUS, icon);
            canvas.drawCircle(adjustedHorizontal, vertical, (float) ICON_RADIUS, fill);

            String firstLetter = String.valueOf(inhabitants.get(i).getName().charAt(0)).toUpperCase();
            canvas.drawText(firstLetter, adjustedHorizontal, (float) (vertical + (ICON_SHIFT * scaleY / 3)), iconText);
        }
    }

    private float[] getPoints(Room room) {
        Geometry g = room.getGeometry();
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;

        if (g.getX() < 0) {
            switch (room.getName()) {
                case DEFAULT_NAME_GARAGE:
                    left = 0;
                    bottom = (float) height;
                    top = (float) (height - (height * RESERVED_HEIGHT) + (SPACING * scaleY));
                    right = (float) ((((maxX - minX) / 2) - (SPACING / 4)) * scaleX);
                    break;
                case DEFAULT_NAME_OUTDOORS:
                    left = (float) (width - ((((maxX - minX) / 2) - (SPACING / 4)) * scaleX));
                    bottom = (float) height;
                    top = (float) (height - (height * RESERVED_HEIGHT) + (SPACING * scaleY));
                    right = width;
                    break;
            }
        } else {
            left = (float) (g.getX() * scaleX);
            top = (float) ((float) availableHeight - ((g.getY() * scaleY) + (g.getHeight() * scaleY)));
            right = (float) ((g.getX() * scaleX) + (g.getWidth() * scaleX));
            bottom = (float) ((float) availableHeight - (g.getY() * scaleY));
        }

        return new float[] {left, top, right, bottom};
    }

    private void findCanvasSize() {
        width = getBounds().width();
        height = getBounds().height();
        availableHeight = AVAILABLE_HEIGHT * height;
    }

    private void findLayoutBounds() {
        for (Room room : houseLayout.getRooms()) {
            Geometry g = room.getGeometry();

            if (g.getX() < minX && g.getX() >= 0)
                minX = g.getX();
            if (g.getY() < minY && g.getY() >= 0)
                minY = g.getY();

            if (g.getX() + g.getWidth() > maxX)
                maxX = g.getX() + g.getWidth();
            if (g.getY() + g.getHeight() > maxY)
                maxY = g.getY() + g.getHeight();
        }
    }

    private void calculateScaleFactors() {
        if (minX == maxX || minY == maxY)
            return;

        scaleX = (double) width / (double) (maxX - minX);
        scaleY = availableHeight / (double) (maxY - minY);
    }

    private void setupPaints() {
        stroke = new Paint();
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(4f);
        stroke.setColor(0xFF909090);

        fill = new Paint();
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.BLACK);
        fill.setAlpha(16);

        icon = new Paint();
        icon.setStyle(Paint.Style.STROKE);
        icon.setStrokeWidth(6f);
        icon.setColor(Color.DKGRAY);

        text = new Paint();
        text.setStyle(Paint.Style.FILL);
        text.setColor(Color.BLACK);
        text.setTextSize(40f);
        text.setFakeBoldText(true);
        text.setSubpixelText(true);
        text.setTextAlign(Paint.Align.CENTER);

        iconText = new Paint();
        iconText.setStyle(Paint.Style.FILL);
        iconText.setColor(Color.BLACK);
        iconText.setTextSize(30f);
        iconText.setSubpixelText(true);
        iconText.setTextAlign(Paint.Align.CENTER);
    }
}