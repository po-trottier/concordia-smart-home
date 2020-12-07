package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.Geometry;
import com.concordia.smarthomesimulator.dataModels.HeatingZone;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.helpers.TemperatureHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.concordia.smarthomesimulator.Constants.*;
import static org.junit.Assert.*;

public class HeatingZoneUnitTest {

    private static final String heatingZoneTestName = "test zone";
    private static final String testRoomName = "testRoom";
    private static final Room testRoom = new Room(testRoomName, new Geometry());
    private static final Room outdoors = new Room(DEFAULT_NAME_OUTDOORS, new Geometry());

    @Test
    public void canGetNameOfHeatingZone() {
        // Setup
        HeatingZone heatingZone = new HeatingZone(heatingZoneTestName);

        // Test
        assertEquals(heatingZone.getName(), heatingZoneTestName);
    }

    @Test
    public void canSetNameOfHeatingZone() {
        // Setup
        HeatingZone heatingZone = new HeatingZone(heatingZoneTestName);
        heatingZone.setName("name");

        // Test
        assertEquals(heatingZone.getName(), "name");
    }

    @Test
    public void canGetRoomFromName() {
        // Setup
        HeatingZone heatingZone = new HeatingZone(heatingZoneTestName);
        heatingZone.addRoom(testRoom);

        // Test
        assertEquals(heatingZone.getRooms().size(), 1);
    }

    @Test
    public void outdoorsRoomIsNotAdded() {
        // Setup
        HeatingZone heatingZone = new HeatingZone(heatingZoneTestName);
        heatingZone.addRoom(outdoors);

        // Test
        assertEquals(heatingZone.getRooms().size(), 0);
    }

    @Test
    public void roomsAreRemovedFromHeatingZones() {
        // Setup
        HeatingZone heatingZone = new HeatingZone(heatingZoneTestName);
        heatingZone.addRoom(testRoom);
        heatingZone.removeRoom(testRoomName);

        // Test
        assertEquals(heatingZone.getRooms().size(), 0);
    }

    @Test
    public void canAddListOfRooms() {
        // Setup
        HeatingZone heatingZone = new HeatingZone(heatingZoneTestName);
        ArrayList<Room> testRooms = new ArrayList<>();
        testRooms.add(testRoom);
        heatingZone.addRooms(testRooms);

        // Test
        assertEquals(heatingZone.getRooms().size(), 1);
    }

}
