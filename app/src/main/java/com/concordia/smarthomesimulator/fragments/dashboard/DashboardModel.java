package com.concordia.smarthomesimulator.fragments.dashboard;

import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardModel extends ViewModel {

    public DashboardModel() {
    }

    public String getDate(){
        return new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
    }
}





