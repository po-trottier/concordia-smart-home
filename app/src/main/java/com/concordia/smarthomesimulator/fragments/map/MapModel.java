package com.concordia.smarthomesimulator.fragments.map;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.helpers.LayoutHelper;

public class MapModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * Instantiates a new Map model.
     */
    public MapModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public LiveData<String> getText() {
        return mText;
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
}