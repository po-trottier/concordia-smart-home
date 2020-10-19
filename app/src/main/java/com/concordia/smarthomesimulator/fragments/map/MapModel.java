package com.concordia.smarthomesimulator.fragments.map;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.helpers.FileHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class MapModel extends ViewModel {

    private final static String FILE_NAME = "layout.json";
    private final static String LAYOUT_IMAGE = "demo_layout.txt";

    /**
     * Instantiates a new Map model.
     */
    public MapModel() {
    }

    /**
     * Save house layout.
     *
     * @param context the context
     * @param layout  the layout
     * @return whether the layout was saved or not
     */
    public boolean saveHouseLayout(Context context, HouseLayout layout) {
        return FileHelper.saveObjectToFile(context, FILE_NAME, layout);
    }

    /**
     * Load house layout.
     *
     * @param context the context
     * @return the house layout
     */
    public HouseLayout loadHouseLayout(Context context) {
        return (HouseLayout) FileHelper.loadObjectFromFile(context, FILE_NAME, HouseLayout.class);
    }

    public HouseLayout loadDemoHouseLayout(Context context){
        // Create new House Layout
        String base64Image = FileHelper.loadRawResource(context, R.raw.demo_layout);
        HouseLayout layout = new HouseLayout("Demo House", base64Image, 6f, 6f);

        // Create room #1
        Room bedroom = new Room("Bedroom", new Geometry(2f,2f));

        Window bedroomWindow = new Window();
        Light bedroomLight = new Light();
        Door bedroomDoor = new Door();

        bedroomWindow.setIsLocked(true);
        bedroomDoor.setIsOpened(true);

        bedroom.addDevices(new ArrayList<>(Arrays.asList(bedroomWindow, bedroomLight, bedroomDoor)));

        layout.addRoom(bedroom);

        // Create room #2
        Room kitchen = new Room("Kitchen", new Geometry(2f,2f));

        Window kitchenWindow = new Window();
        Light kitchenLight = new Light();
        Light kitchenLight2 = new Light();
        Door kitchenDoor = new Door();

        kitchen.addDevices(new ArrayList<>(Arrays.asList(kitchenWindow, kitchenLight, kitchenLight2, kitchenDoor)));

        layout.addRoom(kitchen);

        // Create room #3
        Room bathroom = new Room("Bathroom", new Geometry(2f,2f));

        Window bathroomWindow = new Window();
        Light bathroomLight = new Light();
        Door bathroomDoor = new Door();

        bathroomWindow.setIsLocked(true);
        bathroomDoor.setIsOpened(true);

        bathroom.addDevices(new ArrayList<>(Arrays.asList(bathroomWindow, bathroomLight, bathroomDoor)));

        layout.addRoom(bathroom);

        // Add inhabitant #1
        Inhabitant person1 = new Inhabitant("Person1");
        person1.setRoom(bedroom);

        layout.addInhabitant(person1);

        // Add inhabitant #2
        Inhabitant person2 = new Inhabitant("Person2");
        person1.setRoom(kitchen);

        layout.addInhabitant(person2);

        // Add inhabitant #3
        Inhabitant person3 = new Inhabitant("Person3");

        layout.addInhabitant(person3);

        return layout;
    }
}