package com.concordia.smarthomesimulator.fragments.dashboard;

import android.media.Image;

import androidx.lifecycle.ViewModel;

public class DashboardModel extends ViewModel {

 /*   private MutableLiveData<String> mText;

    public DashboardModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    } */

    public String sw = "On/Off";
    public String btn = "Edit Simulation";
    public String user = "Parent";
    public String location = "Location";
    public String room = "Kitchen";
    public String temperature = "Outside temperature: 15°C";
    public String date = "Friday 16th October 2020";
    public String time = "10:38:20";

    public DashboardModel() {

    }

    public String getSw(){
        return sw;
    }

    public String getBtn(){
        return btn;
    }

    public String getUser(){
        return user;
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





