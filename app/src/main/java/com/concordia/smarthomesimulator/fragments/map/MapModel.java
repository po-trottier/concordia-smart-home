package com.concordia.smarthomesimulator.fragments.map;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.concordia.smarthomesimulator.dataModels.Door;
import com.concordia.smarthomesimulator.dataModels.Geometry;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.dataModels.IDevice;
import com.concordia.smarthomesimulator.dataModels.Inhabitant;
import com.concordia.smarthomesimulator.dataModels.Light;
import com.concordia.smarthomesimulator.dataModels.Room;
import com.concordia.smarthomesimulator.dataModels.Window;
import com.concordia.smarthomesimulator.helpers.LayoutHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class MapModel extends ViewModel {

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
        return LayoutHelper.saveHouseLayout(context, layout);
    }

    /**
     * Load house layout.
     *
     * @param context the context
     * @return the house layout
     */
    public HouseLayout loadHouseLayout(Context context) {
        return LayoutHelper.loadHouseLayout(context);
    }

    public HouseLayout loadDemoHouseLayout(){
        HouseLayout layout = new HouseLayout("Demo House", "R0lGODlhAQABAIAAAAUEBAAAACwAAAAAAQABAAACAkQBADs=", 1f, 1f);

        Room bedroom = new Room("Bedroom", new Geometry(2f,2f));

        Window bedroomWindow = new Window();
        Light bedroomLight = new Light();
        Door bedroomDoor = new Door();

        bedroomWindow.setIsLocked(true);
        bedroomDoor.setIsOpened(true);

        bedroom.addDevices(new ArrayList<>(Arrays.asList(bedroomWindow, bedroomLight, bedroomDoor)));

        layout.addRoom(bedroom);

        Room kitchen = new Room("Kitchen", new Geometry(2f,2f));

        Window kitchenWindow = new Window();
        Light kitchenLight = new Light();
        Light kitchenLight2 = new Light();
        Door kitchenDoor = new Door();

        kitchen.addDevices(new ArrayList<>(Arrays.asList(kitchenWindow, kitchenLight, kitchenLight2, kitchenDoor)));

        layout.addRoom(kitchen);

        Room bathroom = new Room("Bathroom", new Geometry(2f,2f));

        Window bathroomWindow = new Window();
        Light bathroomLight = new Light();
        Door bathroomDoor = new Door();

        bathroomWindow.setIsLocked(true);
        bathroomDoor.setIsOpened(true);

        bathroom.addDevices(new ArrayList<>(Arrays.asList(bathroomWindow, bathroomLight, bathroomDoor)));

        layout.addRoom(bathroom);

        Inhabitant person1 = new Inhabitant("Person1");
        person1.setRoom(bedroom);

        Inhabitant person2 = new Inhabitant("Person2");

        layout.addInhabitant(person1);
        layout.addInhabitant(person2);

        return layout;
    }
}