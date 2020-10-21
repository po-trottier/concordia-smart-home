package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.DeviceType;
import com.concordia.smarthomesimulator.dataModels.Door;
import com.concordia.smarthomesimulator.dataModels.Light;
import com.concordia.smarthomesimulator.dataModels.Window;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeviceUnitTest {

    @Test
    public void doorsCanBeCreated() {
        // Setup
        Door door = new Door();
        // Test
        assertNotNull(door);
        assertEquals(door.getDeviceType(), DeviceType.DOOR);
        assertNotEquals(door.getDeviceType(), DeviceType.WINDOW);
        assertNotEquals(door.getDeviceType(), DeviceType.LIGHT);
    }

    @Test
    public void windowsCanBeCreated() {
        // Setup
        Window window = new Window();
        // Test
        assertNotNull(window);
        assertEquals(window.getDeviceType(), DeviceType.WINDOW);
        assertNotEquals(window.getDeviceType(), DeviceType.DOOR);
        assertNotEquals(window.getDeviceType(), DeviceType.LIGHT);
    }

    @Test
    public void lightsCanBeCreated() {
        // Setup
        Light light = new Light();
        // Test
        assertNotNull(light);
        assertEquals(light.getDeviceType(), DeviceType.LIGHT);
        assertNotEquals(light.getDeviceType(), DeviceType.WINDOW);
        assertNotEquals(light.getDeviceType(), DeviceType.DOOR);
    }

    @Test
    public void doorsAreClosedByDefault() {
        // Setup
        Door door = new Door();
        // Test
        assertFalse(door.getIsOpened());
    }

    @Test
    public void windowsAreClosedByDefault() {
        // Setup
        Window window = new Window();
        // Test
        assertFalse(window.getIsOpened());
    }

    @Test
    public void lightsAreClosedByDefault() {
        // Setup
        Light light = new Light();
        // Test
        assertFalse(light.getIsOpened());
    }

    @Test
    public void windowsAreUnlockedByDefault() {
        // Setup
        Window window = new Window();
        // Test
        assertFalse(window.getIsLocked());
    }

    @Test
    public void doorsCanBeOpened() {
        // Setup
        Door door = new Door();
        // Act
        door.setIsOpened(true);
        // Test
        assertTrue(door.getIsOpened());
    }

    @Test
    public void windowsCanBeOpened() {
        // Setup
        Window window = new Window();
        // Act
        window.setIsOpened(true);
        // Test
        assertTrue(window.getIsOpened());
    }

    @Test
    public void lightsCanBeOpened() {
        // Setup
        Light light = new Light();
        // Act
        light.setIsOpened(true);
        // Test
        assertTrue(light.getIsOpened());
    }

    @Test
    public void windowsCanBeLocked() {
        // Setup
        Window window = new Window();
        // Act
        window.setIsLocked(true);
        // Test
        assertTrue(window.getIsLocked());
    }

    @Test
    public void windowsLockedCannotBeOpened() {
        // Setup
        Window window = new Window();
        // Act
        window.setIsLocked(true);
        window.setIsOpened(true);
        // Test
        assertFalse(window.getIsOpened());
    }
}