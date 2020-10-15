package com.concordia.smarthomesimulator.fragments.logs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.helpers.ActivityLogHelper;

public class LogsController extends Fragment {

    private Context context;
    private LogsModel logsModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        logsModel = new ViewModelProvider(this).get(LogsModel.class);
        logsModel.readLogs(context);

        View root = inflater.inflate(R.layout.fragment_logs, container, false);

        final TextView logsCount = root.findViewById(R.id.logs_count_text);
        logsCount.setText(logsModel.getLogs().size() + " log(s) found");

        final Button clearLogs = root.findViewById(R.id.clear_logs_button);
        clearLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logsModel.clearLogs(context);
                logsCount.setText(logsModel.getLogs().size() + " log(s) found");
            }
        });

        return root;
    }
}