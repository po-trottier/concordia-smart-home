package com.concordia.smarthomesimulator.fragments.map;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.dataModels.HouseLayout;
import com.concordia.smarthomesimulator.helpers.LayoutHelper;

public class MapModel extends ViewModel {

    public static final int WRITE_PERMISSION_REQUEST_CODE = 1000;
    public static final int READ_PERMISSION_REQUEST_CODE = 1001;

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private final MutableLiveData<String> mText;

    public MapModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean saveHouseLayout(HouseLayout layout) {
        return LayoutHelper.saveHouseLayout(context, WRITE_PERMISSION_REQUEST_CODE, layout);
    }

    public HouseLayout loadHouseLayout() {
        return LayoutHelper.loadHouseLayout(context, READ_PERMISSION_REQUEST_CODE);
    }
}