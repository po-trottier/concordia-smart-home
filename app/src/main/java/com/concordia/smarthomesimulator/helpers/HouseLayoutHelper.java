package com.concordia.smarthomesimulator.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.factories.DeviceFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.concordia.smarthomesimulator.Constants.*;
import static com.concordia.smarthomesimulator.Constants.DIRECTORY_NAME_LAYOUTS;

public class HouseLayoutHelper {

    /**
     * Update the selected layout.
     *
     * @param context the context
     * @param layout  the house layout to select
     */
    public static void updateSelectedLayout(Context context, HouseLayout layout) {
        if (layout == null)
            return;

        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFERENCES_KEY_LAYOUT, layout.getName());
        editor.apply();
    }

    /**
     * Gets the selected layout.
     *
     * @param context the context
     * @return the selected layout
     */
    public static HouseLayout getSelectedLayout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String selected = preferences.getString(PREFERENCES_KEY_LAYOUT, "");
        ArrayList<HouseLayout> layouts = HouseLayoutHelper.listSavedHouseLayouts(context);

        return layouts.stream().filter(layout -> layout.getName().equals(selected)).findFirst().orElse(null);
    }

    /**
     * List saved house layouts.
     *
     * @param context the context
     * @return the list of layouts saved on the device
     */
    public static ArrayList<HouseLayout> listSavedHouseLayouts(Context context) {
        ArrayList<HouseLayout> layouts = new ArrayList<>();
        File[] files = FileHelper.listFilesInDirectory(context, DIRECTORY_NAME_LAYOUTS);
        // Add a new Layout for every saved File
        for (File file : files) {
            HouseLayout layout = (HouseLayout) FileHelper.loadObjectFromFile(context, DIRECTORY_NAME_LAYOUTS, file.getName(), HouseLayout.class);
            layouts.add(layout);
        }
        // Add the 2 default layouts (Empty and Demo)
        layouts.add(0, HouseLayoutHelper.createNewHouseLayout(context));
        layouts.add(1, HouseLayoutHelper.loadDemoHouseLayout(context));
        // Return the full list
        return layouts;
    }

    /**
     * Remove a saved house layout's file.
     *
     * @param context the context
     * @param layout  the layout to be removed
     * @return whether the operation was successful or not
     */
    public static boolean removeSavedHouseLayout(Context context, HouseLayout layout) {
        String fileName = layout.getName().trim().toLowerCase().replaceAll(" ", "_") + ".json";
        File[] files = FileHelper.listFilesInDirectory(context, DIRECTORY_NAME_LAYOUTS);
        File selected = Arrays.stream(files)
                .filter(file -> file.getName().equalsIgnoreCase(fileName))
                .findFirst()
                .orElse(null);

        if (selected == null)
            return false;

        return selected.delete();
    }

    private static HouseLayout createNewHouseLayout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String currentUser = preferences.getString(PREFERENCES_KEY_USERNAME, null);

        // Create new House Layout
        return new HouseLayout("Empty Layout", 200f, 100f, currentUser);
    }

    private static HouseLayout loadDemoHouseLayout(Context context) {
        DeviceFactory deviceFactory = new DeviceFactory();

        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String currentUser = preferences.getString(PREFERENCES_KEY_USERNAME, null);

        // Create new House Layout
        HouseLayout layout = new HouseLayout("Demo Layout", 6f, 6f, currentUser);

        // Create room #1
        Room bedroom = new Room("Bedroom", new Geometry(2f,2f));

        Window bedroomWindow = (Window) deviceFactory.createDevice(DeviceType.WINDOW);
        Light bedroomLight = (Light) deviceFactory.createDevice(DeviceType.LIGHT);
        Door bedroomDoor = (Door) deviceFactory.createDevice(DeviceType.DOOR);

        bedroomWindow.setIsLocked(true);
        bedroomDoor.setIsOpened(true);

        bedroom.addDevices(new ArrayList<>(Arrays.asList(bedroomWindow, bedroomLight, bedroomDoor)));

        layout.addRoom(bedroom);

        // Create room #2
        Room kitchen = new Room("Kitchen", new Geometry(2f,2f));

        Window kitchenWindow = (Window) deviceFactory.createDevice(DeviceType.WINDOW);
        Light kitchenLight = (Light) deviceFactory.createDevice(DeviceType.LIGHT);
        Light kitchenLight2 = (Light) deviceFactory.createDevice(DeviceType.LIGHT);
        Door kitchenDoor = (Door) deviceFactory.createDevice(DeviceType.DOOR);

        kitchen.addDevices(new ArrayList<>(Arrays.asList(kitchenWindow, kitchenLight, kitchenLight2, kitchenDoor)));

        layout.addRoom(kitchen);

        // Create room #3
        Room bathroom = new Room("Bathroom", new Geometry(2f,2f));

        Window bathroomWindow = (Window) deviceFactory.createDevice(DeviceType.WINDOW);
        Light bathroomLight = (Light) deviceFactory.createDevice(DeviceType.LIGHT);
        Door bathroomDoor = (Door) deviceFactory.createDevice(DeviceType.DOOR);

        bathroomWindow.setIsLocked(true);
        bathroomDoor.setIsOpened(true);

        bathroom.addDevices(new ArrayList<>(Arrays.asList(bathroomWindow, bathroomLight, bathroomDoor)));

        layout.addRoom(bathroom);

        // Add inhabitants
        Inhabitant person1 = new Inhabitant("Person1");
        bedroom.addInhabitant(person1);

        Inhabitant person2 = new Inhabitant("Person2");
        kitchen.addInhabitant(person2);

        Inhabitant person4 = new Inhabitant("Person3");
        Room garage = layout.getRoom(DEFAULT_NAME_GARAGE);
        garage.addInhabitant(person4);

        return layout;
    }
}
