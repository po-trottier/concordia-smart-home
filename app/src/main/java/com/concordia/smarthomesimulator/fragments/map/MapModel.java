package com.concordia.smarthomesimulator.fragments.map;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.helpers.LayoutHelper;

public class MapModel extends ViewModel {

    private Context context;
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
     * Sets context.
     *
     * @param context the context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Save house layout.
     *
     * @param layout the house layout
     */
    public boolean saveHouseLayout(HouseLayout layout) {
        return LayoutHelper.saveHouseLayout(context, layout);
    }

    /**
     * Load house layout.
     *
     * @return the house layout
     */
    public HouseLayout loadHouseLayout() {
        return LayoutHelper.loadHouseLayout(context);
    }
}