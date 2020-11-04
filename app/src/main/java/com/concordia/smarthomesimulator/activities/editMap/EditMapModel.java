package com.concordia.smarthomesimulator.activities.editMap;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.helpers.FileHelper;
import com.concordia.smarthomesimulator.helpers.HouseLayoutHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.concordia.smarthomesimulator.Constants.*;

public class EditMapModel  extends ViewModel {

    private HouseLayout houseLayout;

    public EditMapModel() {
    }

    /**
     * Gets house layout.
     *
     * @return the house layout
     */
    public HouseLayout getHouseLayout() {
        return houseLayout;
    }

    /**
     * Sets house layout.
     *
     * @param houseLayout the house layout
     */
    public void setHouseLayout(HouseLayout houseLayout) {
        this.houseLayout = houseLayout;
    }

    /**
     * Save a house layout.
     *
     * @param context the context
     * @param layout  the layout
     * @return whether the layout was saved or not
     */
    public boolean saveHouseLayout(Context context, HouseLayout layout) {
        String trimmedName = layout.getName().trim();

        // Do not allow saving files with the default names
        if (trimmedName.equalsIgnoreCase(DEMO_LAYOUT_NAME) || trimmedName.equalsIgnoreCase(EMPTY_LAYOUT_NAME))
            return false;

        String fileName = trimmedName.toLowerCase().replaceAll(" ", "_") + ".json";

        // Do not allow saving files that already exist
        File[] files = FileHelper.listFilesInDirectory(context, DIRECTORY_NAME_LAYOUTS);
        if (Arrays.stream(files).anyMatch(file -> file.getName().equalsIgnoreCase(fileName)))
            return false;

        return FileHelper.saveObjectToFile(context, DIRECTORY_NAME_LAYOUTS, fileName, layout);
    }

    /**
     * Delete a house layout.
     *
     * @param context  the context
     * @param layouts  the list of currently available layouts
     * @param position the position of the layout to remove
     */
    public void deleteHouseLayout(Context context, ArrayList<HouseLayout> layouts, int position) {
        HouseLayoutHelper.removeSavedHouseLayout(context, layouts.get(position));
        layouts.remove(position);
    }
}
