package com.concordia.smarthomesimulator.fragments.dashboard;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardModel extends ViewModel {

    public String user = "Parent";
    public String temperature = "Outside temperature: 15°C";
    public String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    public DashboardModel() {

    }

    public String getUser(){
        return user;
    }

    public String getTemperature(){
        return temperature;
    }

    public String getDate(){
        return date;
    }


}





