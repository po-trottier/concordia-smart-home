package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.concordia.smarthomesimulator.helpers.UserbaseHelper.getUserWithCredentials;
import static org.junit.Assert.*;

public class UserbaseHelperUnitTest {
    @Test
    public void userbaseHelperCanGetUserWithCredentials() {
        // Setup
        List<User> users = new ArrayList<>();
        users.add(new User("a","a",Permissions.STRANGER));
        users.add(new User("b","b",Permissions.STRANGER));
        users.add(new User("c","c",Permissions.STRANGER));
        Userbase userbase = new Userbase(users);
        // Test
        assertEquals(new User("b","b",Permissions.STRANGER), getUserWithCredentials("b","b", userbase));
    }
}
