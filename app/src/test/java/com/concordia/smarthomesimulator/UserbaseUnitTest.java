package com.concordia.smarthomesimulator;

import com.concordia.smarthomesimulator.dataModels.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserbaseUnitTest {
    @Test
    public void userbaseCanBeCreated() {
        // Setup
        List<User> users = new ArrayList<>();
        users.add(new User("a","a",Permissions.STRANGER));
        Userbase userbase = new Userbase(users);
        // Test
        assertNotNull(userbase);
    }

    @Test
    public void userbaseReturnsProperUsernames() {
        // Setup
        List<User> users = new ArrayList<>();
        users.add(new User("a","a",Permissions.STRANGER));
        users.add(new User("b","b",Permissions.STRANGER));
        users.add(new User("c","c",Permissions.STRANGER));
        Userbase userbase = new Userbase(users);
        // Test
        List<String> usernames = new ArrayList<>();
        usernames.add("a");
        usernames.add("b");
        usernames.add("c");
        assertEquals(usernames, userbase.getUsernames());
    }

    @Test
    public void userbaseReturnsProperUserFromUsername() {
        // Setup
        List<User> users = new ArrayList<>();
        User userToGet = new User("b","b",Permissions.STRANGER);
        users.add(new User("a","a",Permissions.STRANGER));
        users.add(userToGet);
        users.add(new User("c","c",Permissions.STRANGER));
        Userbase userbase = new Userbase(users);
        // Test
        assertEquals(userToGet, userbase.getUserFromUsername("b"));
    }

    @Test
    public void userbaseDoesContainUser() {
        // Setup
        List<User> users = new ArrayList<>();
        User userToGet = new User("b","b",Permissions.STRANGER);
        users.add(new User("a","a",Permissions.STRANGER));
        users.add(userToGet);
        users.add(new User("c","c",Permissions.STRANGER));
        Userbase userbase = new Userbase(users);
        // Test
        assertTrue(userbase.containsUser(userToGet));
    }

    @Test
    public void userbaseReturnsProperAmountOfSimilarUsers() {
        // Setup
        List<User> users = new ArrayList<>();
        users.add(new User("a","a",Permissions.STRANGER));
        users.add(new User("c","b",Permissions.STRANGER));
        users.add(new User("c","c",Permissions.STRANGER));
        Userbase userbase = new Userbase(users);
        // Test
        assertEquals(2, userbase.getNumberOfSimilarUsers(new User("c","d",Permissions.STRANGER)));
    }
}
