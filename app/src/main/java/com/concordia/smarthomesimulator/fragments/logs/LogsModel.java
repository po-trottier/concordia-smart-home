package com.concordia.smarthomesimulator.fragments.logs;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;

import java.util.ArrayList;

public class LogsModel extends ViewModel {

    private Context context;
    private ArrayList<LogEntry> logs;

    public LogsModel() {
        logs = new ArrayList<>();
    }

    public void readLogs(Context context) {
        logs = ActivityLogHelper.read(context);
        logs.sort((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
    }

    public void clearLogs(Context context) {
        logs.clear();
        ActivityLogHelper.clear(context);
    }

    public ArrayList<LogEntry> getLogs() {
        return logs;
    }
}