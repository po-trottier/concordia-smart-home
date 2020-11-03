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
    public void geometryCanBeCreatedWithAllParameters() {
        // Setup
        Geometry geometry = new Geometry(1, 1, 1, 1);
        // Test
        assertNotNull(geometry);
    }

    @Test
    public void geometryGetProperValues() {
        // Setup
        int width = 1;
        int height = 2;
        Geometry geometry = new Geometry(1, 1, width, height);
        // Test
        assertEquals(geometry.getHeight(), height, EPSILON);
        assertEquals(geometry.getWidth(), width, EPSILON);
        assertNotEquals(geometry.getHeight(), width);
        assertNotEquals(geometry.getWidth(), height);
    }

    @Test
    public void geometryCanBeSet() {
        // Setup
        int width = 1;
        int height = 2;
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
        int x = 1;
        int y = 2;
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