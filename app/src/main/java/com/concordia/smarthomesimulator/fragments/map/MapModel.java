package com.concordia.smarthomesimulator.fragments.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.factories.DeviceFactory;
import com.concordia.smarthomesimulator.helpers.FileHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static com.concordia.smarthomesimulator.Constants.*;

public class MapModel extends ViewModel {

    private final DeviceFactory deviceFactory;
    private final static String FILE_NAME = "layout.json";

    /**
     * Instantiates a new Map model.
     */
    public MapModel() {
        deviceFactory = new DeviceFactory();
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

    /**
     * Load demo house layout house layout.
     *
     * @param context the context
     * @return the house layout
     */
    public HouseLayout loadDemoHouseLayout(Context context){
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String currentUser = preferences.getString(PREFERENCES_KEY_USERNAME, null);

        // Create new House Layout
        String base64Image = FileHelper.loadRawResource(context, R.raw.demo_layout);
        HouseLayout layout = new HouseLayout("Demo House", base64Image, 6f, 6f, currentUser);

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

    /**
     * Encode and save image boolean.
     *
     * @param context the context
     * @param data    the data
     * @return the whether the operation was successful or not
     */
    public boolean encodeAndSaveImage(Context context, Uri data) {
        try {
            // Get the Bitmap for the selected Image
            final InputStream stream = context.getContentResolver().openInputStream(data);
            final Bitmap bitmap = BitmapFactory.decodeStream(stream);
            // Build a Byte Array with the Bitmap
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG ,100, output);
            byte[] bytes = output.toByteArray();
            // Encode the Byte Array to a Base64 string
            String image = Base64.encodeToString(bytes, Base64.DEFAULT);
            // Save the new House Layout
            HouseLayout layout = loadHouseLayout(context);
            layout.setImage(image);
            saveHouseLayout(context, layout);
            // Success
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}