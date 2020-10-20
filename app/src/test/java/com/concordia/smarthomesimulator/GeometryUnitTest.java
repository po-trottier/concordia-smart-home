package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.Geometry;
import org.junit.Test;

import static org.junit.Assert.*;

public class GeometryUnitTest {

    final static float EPSILON = Float.intBitsToFloat(Float.floatToIntBits(1f) + 1);

    @Test
    public void geometryCanBeCreated() {
        // Setup
        Geometry geometry = new Geometry();
        // Test
        assertNotNull(geometry);
    }

    @Test
    public void geometryCanBeCreatedWithParameters() {
        // Setup
        Geometry geometry = new Geometry(1f, 1f);
        // Test
        assertNotNull(geometry);
    }

    @Test
    public void geometryGetProperValues() {
        // Setup
        float width = 1f;
        float height = 2f;
        Geometry geometry = new Geometry(width, height);
        // Test
        assertEquals(geometry.getHeight(), height, EPSILON);
        assertEquals(geometry.getWidth(), width, EPSILON);
        assertNotEquals(geometry.getHeight(), width);
        assertNotEquals(geometry.getWidth(), height);
    }

    @Test
    public void geometryCanBeSet() {
        // Setup
        float width = 1f;
        float height = 2f;
        Geometry geometry = new Geometry();
        // Act
        geometry.setWidth(width);
        geometry.setHeight(height);
        // Test
        assertEquals(geometry.getHeight(), height, EPSILON);
        assertEquals(geometry.getWidth(), width, EPSILON);
        assertNotEquals(geometry.getHeight(), width);
        assertNotEquals(geometry.getWidth(), height);
    }

    @Test
    public void geometryCoordinatesCanBeSet() {
        // Setup
        float x = 1f;
        float y = 2f;
        Geometry geometry = new Geometry();
        // Act
        geometry.setX(x);
        geometry.setY(y);
        // Test
        assertEquals(geometry.getX(), x, EPSILON);
        assertEquals(geometry.getY(), y, EPSILON);
        assertNotEquals(geometry.getX(), y);
        assertNotEquals(geometry.getY(), x);
    }
}