package com.concordia.smarthomesimulator.activities.editMap;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.dataModels.*;
import com.concordia.smarthomesimulator.helpers.FileHelper;
import com.concordia.smarthomesimulator.helpers.HouseLayoutHelper;

import java.util.ArrayList;

import static com.concordia.smarthomesimulator.Constants.*;

public class EditMapModel  extends ViewModel {

    public EditMapModel() {
    }

    /**
     * Save a house layout.
     *
     * @param context the context
     * @param layout  the layout
     * @return whether the layout was saved or not
     */
    public boolean saveHouseLayout(Context context, HouseLayout layout) {
        String fileName = layout.getName().trim().toLowerCase().replaceAll(" ", "_") + ".json";
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
