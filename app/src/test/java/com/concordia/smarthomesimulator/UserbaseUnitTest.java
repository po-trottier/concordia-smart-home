package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
public class UserbaseUnitTest {
    @Test
    public void userbaseCanBeCreated() {
        // Setup
        List<User> users = new ArrayList<>();
        users.add(new User());
        Userbase userbase = new Userbase()
        // Test
        assertNotNull(userbase);
    }
}
