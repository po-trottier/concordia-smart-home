package com.concordia.smarthomesimulator.fragments.dashboard;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardModel extends ViewModel {

    public String onOffSwitch = "On/Off";
    public String editButton = "Edit Simulation";
    public Drawable image;
    public String user = "Parent";
    public String location = "Location:";
    public String room = "Kitchen";
    public String temperature = "Outside temperature: 15°C";
    public String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    public String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

    public DashboardModel() {

    }

    public String getOnOffSwitch(){
        return onOffSwitch;
    }

    public String getEditButton(){
        return editButton;
    }

    public String getUser(){
        return user;
    }

    public Drawable getImage(){
        return image;
    }

    public String getLocation(){
        return location;
    }

    public String getRoom(){
        return room;
    }

    public String getTemperature(){
        return temperature;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

}





