package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.Geometry;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.Room;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HouseLayoutUnitTest {

    @Test
    public void houseLayoutCanBeCreated() {
        // Setup
        HouseLayout layout = new HouseLayout("layout", null);
        // Test
        assertNotNull(layout);
    }

    @Test
    public void houseLayoutReturnsProperName() {
        // Setup
        final String name = "layout";
        HouseLayout layout = new HouseLayout(name, null);
        // Test
        assertEquals(layout.getName(), name);
    }

    @Test
    public void houseLayoutHasNoRoomByDefault() {
        // Setup
        HouseLayout layout = new HouseLayout("layout", null);
        // Test
        assertEquals(layout.getRooms().size(), 0);
    }

    @Test
    public void houseLayoutRoomCanBeAdded() {
        // Setup
        HouseLayout layout = new HouseLayout("layout", null);
        // Act
        layout.addRoom(new Room("room", new Geometry()));
        // Test
        assertEquals(layout.getRooms().size(), 1);
    }

    @Test
    public void houseLayoutRoomsCanBeAdded() {
        // Setup
        Room room = new Room("room", new Geometry());
        Room room2 = new Room("room2", new Geometry());
        HouseLayout layout = new HouseLayout("layout", null);
        // Act
        layout.addRooms(new ArrayList<>(Arrays.asList(room, room2)));
        // Test
        assertEquals(layout.getRooms().size(), 2);
    }

    @Test
    public void houseLayoutRoomCanBeRemoved() {
        // Setup
        Room room = new Room("room", new Geometry());
        Room room2 = new Room("room2", new Geometry());
        HouseLayout layout = new HouseLayout("layout", null);
        layout.addRooms(new ArrayList<>(Arrays.asList(room, room2)));
        // Act
        layout.removeRoom(room.getName());
        // Test
        assertEquals(layout.getRooms().size(), 1);
    }
}