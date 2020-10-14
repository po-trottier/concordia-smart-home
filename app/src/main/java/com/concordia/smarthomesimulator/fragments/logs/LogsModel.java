package com.concordia.smarthomesimulator.fragments.logs;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.dataModels.LogEntry;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;

import java.util.Vector;

public class LogsModel extends ViewModel {

    private Context context;
    private Vector<LogEntry> logs;

    public LogsModel() {
        logs = new Vector<>();
    }

    public void readLogs(Context context) {
        logs = ActivityLogHelper.read(context);
    }

    public void clearLogs(Context context) {
        logs = new Vector<>();
        ActivityLogHelper.clear(context);
    }

    public Vector<LogEntry> getLogs() {
        return logs;
    }
}