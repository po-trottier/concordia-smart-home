package com.concordia.smarthomesimulator.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.factories.DeviceFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.concordia.smarthomesimulator.Constants.*;

public class HouseLayoutHelper {

    //region Public Methods

    //region Memory Operations

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
        ArrayList<HouseLayout> layouts = HouseLayoutHelper.listSavedLayouts(context);

        return layouts.stream().filter(layout -> layout.getName().equals(selected)).findFirst().orElse(null);
    }

    /**
     * List saved house layouts.
     *
     * @param context the context
     * @return the list of layouts saved on the device
     */
    public static ArrayList<HouseLayout> listSavedLayouts(Context context) {
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
     * Checks if the house layout name is a default name (Demo Layout or Empty Layout).
     *
     * @param layout the layout to verify
     * @return whether the layout name is default or not
     */
    public static boolean isLayoutNameDefault(HouseLayout layout) {
        String name = layout.getName().trim();
        return name.equalsIgnoreCase(DEMO_LAYOUT_NAME) || name.equalsIgnoreCase(EMPTY_LAYOUT_NAME);
    }

    /**
     * Checks if the house layout name if unique.
     *
     * @param context the context
     * @param layout  the layout to verify
     * @return whether the layout name is unique or not
     */
    public static boolean isLayoutNameUnique(Context context, HouseLayout layout) {
        String name = layout.getName().trim();
        // If the house layout is one of the 2 defaults it's not unique
        if (isLayoutNameDefault(layout)) {
            return false;
        }
        // Otherwise make sure it's not in the list of saved layouts
        return listSavedLayouts(context).stream().noneMatch(saved -> saved.getName().trim().equalsIgnoreCase(name));
    }

    //endregion

    //region File Operations

    /**
     * Remove a saved house layout's file.
     *
     * @param context the context
     * @param layout  the layout to be removed
     */
    public static void removeHouseLayout(Context context, HouseLayout layout) {
        String fileName = getHouseLayoutFileName(layout);
        File[] files = FileHelper.listFilesInDirectory(context, DIRECTORY_NAME_LAYOUTS);
        File selected = Arrays.stream(files)
                .filter(file -> file.getName().equalsIgnoreCase(fileName))
                .findFirst()
                .orElse(null);

        if (selected == null)
            return;

        //noinspection ResultOfMethodCallIgnored
        selected.delete();
    }

    /**
     * Save a house layout to a file.
     *
     * @param context the context
     * @param layout  the layout to save
     * @return whether the layout was saved or not
     */
    public  static boolean saveHouseLayout(Context context, HouseLayout layout) {
        String fileName = getHouseLayoutFileName(layout);

        return FileHelper.saveObjectToFile(context, DIRECTORY_NAME_LAYOUTS, fileName, layout);
    }

    /**
     * Gets house layout file name based on the layout's name.
     *
     * @param layout the layout
     * @return the house layout file name
     */
    public static String getHouseLayoutFileName(HouseLayout layout) {
        return layout.getName().trim().toLowerCase().replaceAll(" ", "_") + ".json";
    }

    //endregion

    //endregion

    //region Private Methods

    private static HouseLayout createNewHouseLayout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String currentUser = preferences.getString(PREFERENCES_KEY_USERNAME, null);

        // Create new House Layout
        return new HouseLayout(EMPTY_LAYOUT_NAME, currentUser);
    }

    private static HouseLayout loadDemoHouseLayout(Context context) {
        DeviceFactory deviceFactory = new DeviceFactory();

        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String currentUser = preferences.getString(PREFERENCES_KEY_USERNAME, null);

        // Create new House Layout
        HouseLayout layout = new HouseLayout(DEMO_LAYOUT_NAME, currentUser);

        // Create Bedroom
        Room bedroom = new Room("Bedroom", new Geometry(0,0, 4, 6));

        Window bedroomWindow = (Window) deviceFactory.createDevice(DeviceType.WINDOW, new Geometry(0,4, Orientation.VERTICAL));
        Window bedroomWindow2 = (Window) deviceFactory.createDevice(DeviceType.WINDOW, new Geometry(0,2, Orientation.VERTICAL));
        Door bedroomDoor = (Door) deviceFactory.createDevice(DeviceType.DOOR, new Geometry(2,6, Orientation.HORIZONTAL));
        Light bedroomLight = (Light) deviceFactory.createDevice(DeviceType.LIGHT, new Geometry(2, 1));
        Light bedroomLight2 = (Light) deviceFactory.createDevice(DeviceType.LIGHT, new Geometry(1, 1));

        bedroomWindow.setIsLocked(true);
        bedroomDoor.setIsOpened(true);
        bedroomLight2.setIsOpened(true);

        bedroom.addDevices(new ArrayList<>(Arrays.asList(
                bedroomWindow,
                bedroomWindow2,
                bedroomDoor,
                bedroomLight,
                bedroomLight2
        )));

        Inhabitant person1 = new Inhabitant("Alex");
        bedroom.addInhabitant(person1);

        Inhabitant person2 = new Inhabitant("Jane");
        bedroom.addInhabitant(person2);

        layout.addRoom(bedroom);

        // Create Kitchen
        Room kitchen = new Room("Kitchen", new Geometry(0, 6, 8,4));

        Window kitchenWindow = (Window) deviceFactory.createDevice(DeviceType.WINDOW, new Geometry(2,10, Orientation.HORIZONTAL));
        Window kitchenWindow2 = (Window) deviceFactory.createDevice(DeviceType.WINDOW, new Geometry(4,10, Orientation.HORIZONTAL));
        Window kitchenWindow3 = (Window) deviceFactory.createDevice(DeviceType.WINDOW, new Geometry(6,10, Orientation.HORIZONTAL));
        Light kitchenLight = (Light) deviceFactory.createDevice(DeviceType.LIGHT, new Geometry(2, 8));
        Light kitchenLight2 = (Light) deviceFactory.createDevice(DeviceType.LIGHT, new Geometry(6, 8));
        Door kitchenDoor = (Door) deviceFactory.createDevice(DeviceType.DOOR, new Geometry(0,8, Orientation.VERTICAL));
        Door kitchenDoor2 = (Door) deviceFactory.createDevice(DeviceType.DOOR, new Geometry(7,6, Orientation.HORIZONTAL));

        kitchenLight2.setIsOpened(true);
        kitchenWindow2.setIsOpened(true);

        kitchen.addDevices(new ArrayList<>(Arrays.asList(
            kitchenWindow,
            kitchenWindow2,
            kitchenWindow3,
            kitchenLight,
            kitchenLight2,
            kitchenDoor,
            kitchenDoor2
        )));

        Inhabitant person3 = new Inhabitant("Billy");
        kitchen.addInhabitant(person3);

        Inhabitant person4 = new Inhabitant("Mike");
        kitchen.addInhabitant(person4);

        Inhabitant person5 = new Inhabitant("Jackson");
        kitchen.addInhabitant(person5);

        layout.addRoom(kitchen);

        // Create Bathroom
        Room bathroom = new Room("Bathroom", new Geometry(4, 4, 2, 2));

        Light bathroomLight = (Light) deviceFactory.createDevice(DeviceType.LIGHT, new Geometry(5, 4));
        Door bathroomDoor = (Door) deviceFactory.createDevice(DeviceType.DOOR, new Geometry(4,4, Orientation.VERTICAL));
        Door bathroomDoor2 = (Door) deviceFactory.createDevice(DeviceType.DOOR, new Geometry(5,6, Orientation.HORIZONTAL));

        bathroomDoor.setIsOpened(true);

        bathroom.addDevices(new ArrayList<>(Arrays.asList(
            bathroomLight,
            bathroomDoor,
            bathroomDoor2
        )));

        layout.addRoom(bathroom);

        // Add an inhabitant in the Garage
        Inhabitant person6 = new Inhabitant("William");
        Room garage = layout.getRoom(DEFAULT_NAME_GARAGE);
        garage.addInhabitant(person6);

        return layout;
    }

    //endregion
}
