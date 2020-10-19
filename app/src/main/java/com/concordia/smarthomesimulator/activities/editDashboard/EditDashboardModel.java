package com.concordia.smarthomesimulator.activities.editDashboard;
import android.content.Context;
import android.widget.Toast;
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

    public int editUser(User editedUser, String spinnerUsername, Context context, Userbase userbase){
        final User oldUser = userbase.getUserFromUsername(spinnerUsername);
        String newUsername = "newUsername";
        String newPassword = "newPassword";
        if (editedUser.getUsername().isEmpty()){
            newUsername = spinnerUsername;
            newPassword = editedUser.getPassword();
        } else if (editedUser.getPassword().isEmpty()){
            newUsername = editedUser.getUsername();
            newPassword = oldUser.getPassword();
        } else {
            newUsername = editedUser.getUsername();
            newPassword = editedUser.getPassword();
        }
        User userToAdd = new User(newUsername, newPassword, editedUser.getPermission());
        // if the new user is not similar to any other use except the one it's replacing, add it and delete
        // the old user
        if (userbase.getNumberOfSimilarUsers(userToAdd) < 2) {
            userbase.deleteUserFromUsernameIfPossible(oldUser.getUsername(), context);
            // we don't care about the return of addUser since we know it will be successful as the similar user
            // was deleted
            addUser(context, userToAdd, userbase);
            return R.string.edit_success;
        } else{
            return R.string.edit_error;
        }
    }

}
