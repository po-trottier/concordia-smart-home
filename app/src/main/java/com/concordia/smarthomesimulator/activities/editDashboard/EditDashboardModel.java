package com.concordia.smarthomesimulator.activities.editDashboard;
import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;

public class EditDashboardModel extends ViewModel{

    public int addUser(Context context, User userToAdd, Userbase userbase){
        if (userbase.addUserIfPossible(userToAdd, context)){
            return R.string.create_user_successful;
        }
        return R.string.create_user_failed;
    }

}
