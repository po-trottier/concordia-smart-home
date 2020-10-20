package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.Geometry;
import com.concordia.smarthomesimulator.dataModels.Inhabitant;
import com.concordia.smarthomesimulator.dataModels.Room;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InhabitantUnitTest {

    @Test
    public void inhabitantCanBeCreated() {
        // Setup
        Inhabitant inhabitant = new Inhabitant("inhabitant");
        // Test
        assertNotNull(inhabitant);
    }

    @Test
    public void inhabitantGetsRightName() {
        // Setup
        final String name = "inhabitant";
        Inhabitant inhabitant = new Inhabitant(name);
        // Test
        assertEquals(inhabitant.getName(), name);
    }

    @Test
    public void inhabitantRoomCanBeSet() {
        // Setup
        Inhabitant inhabitant = new Inhabitant("inhabitant");
        // Act
        Room room = new Room("room", new Geometry());
        inhabitant.setRoom(room);
        // Test
        assertEquals(inhabitant.getRoom(), room);
    }
}