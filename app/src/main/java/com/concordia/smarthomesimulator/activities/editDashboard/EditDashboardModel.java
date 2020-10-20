package com.concordia.smarthomesimulator.activities.editDashboard;
import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.concordia.smarthomesimulator.R;
import com.concordia.smarthomesimulator.dataModels.User;
import com.concordia.smarthomesimulator.dataModels.Userbase;

public class EditDashboardModel extends ViewModel{


    /**
     * Adds the user to the userbase if no similar users exists in the userbase.
     *
     * @param context   the context
     * @param userbase  the userbase the user is added to
     * @param userToAdd the user to add
     * @return the int code for the feedback message
     */
    public int addUser(Context context, Userbase userbase, User userToAdd){
        if (userbase.addUserIfPossible(context, userToAdd)){
            return R.string.create_user_successful;
        }
        return R.string.create_user_failed;
    }

    /**
     * Delete user.
     *  @param context          the context
     * @param userbase         the userbase
     * @param usernameToDelete the username to delete
     */
    public void deleteUser(Context context, Userbase userbase, String usernameToDelete){
        userbase.deleteUserFromUsernameIfPossible(context, usernameToDelete);
    }

    /**
     * Will edit a user account if the edited account is not similar to another account in the userbase.
     *
     * @param context         the context
     * @param userbase        the userbase
     * @param editedUser      the edited user
     * @param oldUser         the old user
     * @return the int code for the feedback message
     */
    public int editUser(Context context, Userbase userbase, User editedUser, User oldUser){
        String newUsername;
        String newPassword;

        if (editedUser.getUsername().isEmpty()){
            newUsername = oldUser.getUsername();
            newPassword = editedUser.getPassword();
        } else if (editedUser.getPassword().isEmpty()){
            newUsername = editedUser.getUsername();
            newPassword = oldUser.getPassword();
        } else {
            newUsername = editedUser.getUsername();
            newPassword = editedUser.getPassword();
        }

        User userToAdd = new User(newUsername, newPassword, editedUser.getPermission());
        // if the new user is not similar to any other use except the one it's replacing, add it and delete the old user
        if (userbase.getNumberOfSimilarUsers(userToAdd) < 2 && !userbase.containsUser(userToAdd)) {
            userbase.deleteUserFromUsernameIfPossible(context, oldUser.getUsername());
            // we don't care about the return of addUser since we know it will be successful as the similar user was deleted
            addUser(context, userbase, userToAdd);
            return R.string.edit_success;
        } else{
            return R.string.edit_conflict;
        }
    }

}