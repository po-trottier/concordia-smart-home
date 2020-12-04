package com.concordia.smarthomesimulator.dataModels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.adapters.InhabitantAdapter;
import com.concordia.smarthomesimulator.enums.Orientation;
import com.concordia.smarthomesimulator.enums.Permissions;
import com.concordia.smarthomesimulator.interfaces.IInhabitant;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.concordia.smarthomesimulator.Constants.*;

/**
 * The house layout is an observer to the rooms inside of it.
 */
public class HouseLayout extends Observable implements Observer, Serializable {

    private String name;
    private final String currentUser;
    private ArrayList<Room> rooms;
    private ArrayList<HeatingZone> heatingZones;

    /**
     * Instantiates a new House layout.
     *  @param name        the name
     *  @param currentUser the currently logged in user
     */
    public HouseLayout(String name, String currentUser) {
        this.name = name;
        this.currentUser = currentUser;

        rooms = new ArrayList<>();
        // Create the default rooms (Outdoors and Garage)
        Room garage = new Room(DEFAULT_NAME_GARAGE, new Geometry(-1, -1));
        Room outdoors = new Room(DEFAULT_NAME_OUTDOORS, new Geometry(-1, -1));
        // Add the Garage Door
        Door garageDoor = new Door(new Geometry(-1, -1, Orientation.HORIZONTAL));
        garageDoor.setAutoLock(true);
        garage.addDevice(garageDoor);
        // Add Backyard Door
        Door outdoorsDoor = new Door(new Geometry(-2, -2, Orientation.HORIZONTAL));
        outdoorsDoor.setAutoLock(true);
        outdoors.addDevice(outdoorsDoor);
        // Add the Garage Light
        Light garageLight = new Light(new Geometry(-3, -4, Orientation.HORIZONTAL));
        garageLight.setAutoOn(true);
        garage.addDevice(garageLight);
        // Add Backyard Light
        Light outdoorsLight = new Light(new Geometry(-4, -3, Orientation.HORIZONTAL));
        outdoorsLight.setAutoOn(true);
        outdoors.addDevice(outdoorsLight);
        // Add the rooms to the layout
        rooms.addAll(new ArrayList<>(Arrays.asList(garage, outdoors)));

        // Add the current user if he's not already in a room
        boolean userExists = currentUser != null && rooms.stream().anyMatch(room -> room.hasInhabitant(currentUser));
        if (!userExists) {
            // Create a dummy user to make the adapter work...
            User user = new User(currentUser, "", Permissions.STRANGER);
            this.getRoom(DEFAULT_NAME_OUTDOORS).addInhabitant(new InhabitantAdapter(user));
        }

        heatingZones = new ArrayList<>();
        // Create a default heating zone
        HeatingZone zone = new HeatingZone(DEFAULT_NAME_HEATING_ZONE);
        zone.addRooms(rooms);
        heatingZones.add(zone);
    }

    @Override
    public void update(Observable observable, Object arg) {
        notifyObservers();
    }

    @NonNull
    @Override
    public Object clone() {
        HouseLayout newLayout = new HouseLayout(name, currentUser);
        newLayout.clearForClone();
        ArrayList<Room> newRooms = new ArrayList<>();
        for (Room room : rooms) {
            newRooms.add((Room) room.clone());
        }
        ArrayList<HeatingZone> newZones = new ArrayList<>();
        for (HeatingZone zone : heatingZones) {
            newZones.add((HeatingZone) zone.clone());
        }
        newLayout.addRooms(newRooms);
        newLayout.addHeatingZones(newZones);
        return newLayout;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != HouseLayout.class) {
            return false;
        }
        return ((HouseLayout) obj).getName().equalsIgnoreCase(name);
    }

    /**
     * Clear all heating zones.
     *
     * To be used only by the clone operation!
     */
    public void clearForClone() {
        heatingZones = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a specific room based on its name.
     *
     * @return the room
     */
    public Room getRoom(String name) {
        return rooms.stream()
            .filter(room -> room.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    /**
     * Gets rooms.
     *
     * @return the rooms
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Gets heating zone.
     *
     * @param name the heating zone name
     * @return the heating zone
     */
    public HeatingZone getHeatingZone(String name) {
        return heatingZones.stream()
            .filter(zone -> zone.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    /**
     * Gets heating zones.
     *
     * @return the heating zones
     */
    public ArrayList<HeatingZone> getHeatingZones() {
        return heatingZones;
    }

    /**
     * Is intruder detected boolean.
     *
     * @return the boolean
     */
    public boolean isIntruderDetected() {
        return getAllInhabitants().stream().anyMatch(IInhabitant::isIntruder);
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Add a room.
     *
     * @param room the room
     */
    public void addRoom(Room room) {
        room.addObserver(this);
        rooms.add(room);
        heatingZones.stream()
            .filter(zone -> zone.getName().equalsIgnoreCase(DEFAULT_NAME_HEATING_ZONE))
            .findFirst()
            .ifPresent(heatingZone -> heatingZone.addRoom(room));
        update(room, null);
    }

    /**
     * Add rooms.
     *
     * @param rooms the rooms
     */
    public void addRooms(ArrayList<Room> rooms) {
        for (Room room : rooms) {
            addRoom(room);
        }
    }

    /**
     * Remove a room.
     *
     * @param name the name
     */
    public void removeRoom(String name) {
        for(Room room : rooms) {
           if (room.getName().equals(name)) {
               room.deleteObserver(this);
               heatingZones.stream()
                   .filter(zone -> zone.getRoom(name) != null)
                   .findFirst()
                   .ifPresent(heatingZone -> heatingZone.removeRoom(name));
               rooms.remove(room);
               return;
           }
        }
    }

    /**
     * Add a heating zone.
     *
     * @param heatingZone the heating zone
     */
    public void addHeatingZone(HeatingZone heatingZone) {
        heatingZones.add(heatingZone);
    }

    /**
     * Add heating zones.
     *
     * @param heatingZones the rooms
     */
    public void addHeatingZones(ArrayList<HeatingZone> heatingZones) {
        this.heatingZones.addAll(heatingZones);
    }

    /**
     * Remove a room.
     *
     * @param name the name
     */
    public void removeHeatingZone(String name) {
        HeatingZone heatingZone = heatingZones.stream()
            .filter(zone -> zone.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
        // Do not allow removing the default zone
        if (heatingZone == null || heatingZone.getName().equalsIgnoreCase(DEFAULT_NAME_HEATING_ZONE)) {
            return;
        }
        // Transfer all rooms inside the zone to the default zone
        if (heatingZone.getRooms().size() > 0) {
            heatingZones.stream()
                .filter(zone -> zone.getName().equalsIgnoreCase(DEFAULT_NAME_HEATING_ZONE))
                .findFirst()
                .ifPresent(zone -> zone.addRooms(heatingZone.getRooms()));
        }
        // Remove the actual zone
        heatingZones.remove(heatingZone);
    }

    /**
     * Gets all inhabitants in the layout.
     *
     * @return the all inhabitants
     */
    public ArrayList<IInhabitant> getAllInhabitants() {
        List<ArrayList<IInhabitant>> inhabitantLists = rooms
                .stream()
                .map(Room::getInhabitants)
                .collect(Collectors.toList());
        ArrayList<IInhabitant> inhabitants = new ArrayList<>();
        for (ArrayList<IInhabitant> list : inhabitantLists) {
            inhabitants.addAll(list);
        }
        return inhabitants;
    }
}
