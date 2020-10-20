package com.concordia.smarthomesimulator.activities.editDashboard;
import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;

public class EditDashboardModel extends ViewModel{


    /**
     * Adds the user to the userbase if no similar users exists in the userbase.
     *
     * @param context   the context
     * @param userToAdd the user to add
     * @param userbase  the userbase the user is added to
     * @return the int code for the feedback message
     */
    public int addUser(Context context, User userToAdd, Userbase userbase){
        if (userbase.addUserIfPossible(userToAdd, context)){
            return R.string.create_user_successful;
        }
        return R.string.create_user_failed;
    }

    public void deleteUser(Context context, String usernameToDelete, Userbase userbase){
        userbase.deleteUserFromUsernameIfPossible(usernameToDelete, context);
    }

    /**
     * Will edit a user account if the edited account is not similar to another account in the userbase.
     *
     * @param editedUser      the edited user
     * @param oldUser         the old user
     * @param context         the context
     * @param userbase        the userbase
     * @return the int code for the feedback message
     */
    public int editUser(User editedUser, User oldUser, Context context, Userbase userbase){
        String newUsername = oldUser.getUsername();
        String newPassword = oldUser.getPassword();
        if (editedUser.getUsername().isEmpty()){
            newUsername = oldUser.getUsername();
            newPassword = editedUser.getPassword();
        } else if (editedUser.getPassword().isEmpty()){
            newUsername = editedUser.getUsername();
            newPassword = oldUser.getPassword();
        } else if (!editedUser.getUsername().isEmpty() && !editedUser.getPassword().isEmpty()){
            newUsername = editedUser.getUsername();
            newPassword = editedUser.getPassword();
        }
        User userToAdd = new User(newUsername, newPassword, editedUser.getPermission());
        // if the new user is not similar to any other use except the one it's replacing, add it and delete
        // the old user
        if (userbase.getNumberOfSimilarUsers(userToAdd) < 2 && !userbase.containsUser(userToAdd)) {
            userbase.deleteUserFromUsernameIfPossible(oldUser.getUsername(), context);
            // we don't care about the return of addUser since we know it will be successful as the similar user
            // was deleted
            addUser(context, userToAdd, userbase);
            return R.string.edit_success;
        } else{
            return R.string.edit_conflict;
        }
    }

}