package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class RoomUnitTest {

    @Test
    public void roomCanBeCreated() {
        // Setup
        Room room = new Room("room", new Geometry());
        // Test
        assertNotNull(room);
    }

    @Test
    public void roomReturnProperName() {
        // Setup
        final String name ="room";
        Room room = new Room(name, new Geometry());
        // Test
        assertEquals(room.getName(), name);
    }

    @Test
    public void roomReturnProperGeometry() {
        // Setup
        final Geometry geometry = new Geometry(1, 2, 3, 4);
        geometry.setX(3);
        geometry.setY(4);
        Room room = new Room("room", geometry);
        // Test
        assertEquals(room.getGeometry(), geometry);
    }

    @Test
    public void roomHasNoDevicesByDefault() {
        // Setup
        Room room = new Room("room", new Geometry());
        // Test
        assertEquals(room.getDevices().size(), 0);
    }

    @Test
    public void roomDeviceCanBeAdded() {
        // Setup
        Room room = new Room("room", new Geometry());
        // Act
        room.addDevice(new Light());
        // Test
        assertEquals(room.getDevices().size(), 1);
    }

    @Test
    public void roomDevicesCanBeAdded() {
        // Setup
        Room room = new Room("room", new Geometry());
        // Act
        room.addDevices(new ArrayList<>(Arrays.asList(new Door(), new Window(), new Light())));
        // Test
        assertEquals(room.getDevices().size(), 3);
    }

    @Test
    public void roomDeviceCanBeRemoved() {
        // Setup
        Room room = new Room("room", new Geometry());
        room.addDevice(new Window());
        Door door = new Door();
        room.addDevice(door);
        // Act
        room.removeDevice(door);
        // Test
        assertEquals(room.getDevices().size(), 1);
    }

    @Test
    public void roomHasNoInhabitantsByDefault() {
        // Setup
        Room room = new Room("room", new Geometry());
        // Test
        assertEquals(room.getInhabitants().size(), 0);
    }

    @Test
    public void roomInhabitantCanBeAdded() {
        // Setup
        Room room = new Room("room", new Geometry());
        // Act
        room.addInhabitant(new Inhabitant("inhabitant"));
        // Test
        assertEquals(room.getInhabitants().size(), 1);
    }

    @Test
    public void roomInhabitantsCanBeAdded() {
        // Setup
        Inhabitant inhabitant = new Inhabitant("inhabitant");
        Inhabitant inhabitant2 = new Inhabitant("inhabitant2");
        Room room = new Room("room", new Geometry());
        // Act
        room.addInhabitants(new ArrayList<>(Arrays.asList(inhabitant, inhabitant2)));
        // Test
        assertEquals(room.getInhabitants().size(), 2);
    }

    @Test
    public void roomInhabitantsCanBeRemoved() {
        // Setup
        Inhabitant inhabitant = new Inhabitant("inhabitant");
        Inhabitant inhabitant2 = new Inhabitant("inhabitant2");
        Room room = new Room("room", new Geometry());
        room.addInhabitants(new ArrayList<>(Arrays.asList(inhabitant, inhabitant2)));
        // Act
        room.removeInhabitant(inhabitant.getName());
        // Test
        assertEquals(room.getInhabitants().size(), 1);
    }
}