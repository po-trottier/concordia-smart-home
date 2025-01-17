package com.concordia.smarthomesimulator.dataModels;

import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import com.concordia.smarthomesimulator.enums.Action;
import com.concordia.smarthomesimulator.enums.Permissions;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.concordia.smarthomesimulator.Constants.*;

/**
 * Permissions configuration.
 * <p>
 * This specifies what minimum permissions are required to perform certain actions.
 */
public class PermissionsConfiguration implements Serializable {
    private final Map<Action, Permissions> actionPermissionsMap;

    /**
     * To make a default Permission configuration.
     * More importantly it adds all the keys that depict actions the user can take
     */
    public PermissionsConfiguration() {
        actionPermissionsMap = new LinkedHashMap<>();
        actionPermissionsMap.put(Action.INTERACT_ANY_WINDOW,DEFAULT_MIN_TO_INTERACT_ANY_WINDOW);
        actionPermissionsMap.put(Action.INTERACT_LOCAL_WINDOW,DEFAULT_MIN_TO_INTERACT_LOCAL_WINDOW);
        actionPermissionsMap.put(Action.INTERACT_ANY_LIGHT,DEFAULT_MIN_TO_INTERACT_ANY_LIGHT);
        actionPermissionsMap.put(Action.INTERACT_LOCAL_LIGHT,DEFAULT_MIN_TO_INTERACT_LOCAL_LIGHT);
        actionPermissionsMap.put(Action.INTERACT_GARAGE,DEFAULT_MIN_TO_INTERACT_GARAGE);
        actionPermissionsMap.put(Action.INTERACT_DOOR_LOCK,DEFAULT_MIN_TO_INTERACT_DOOR_LOCK);
        actionPermissionsMap.put(Action.CHANGE_AWAY_MODE,DEFAULT_MIN_TO_CHANGE_AWAY_MODE);
        actionPermissionsMap.put(Action.MODIFY_USERBASE,DEFAULT_MIN_TO_MODIFY_USERBASE);
        actionPermissionsMap.put(Action.MODIFY_TEMPERATURE,DEFAULT_MIN_TO_MODIFY_TEMPERATURE);
        actionPermissionsMap.put(Action.CHANGE_PERMISSIONS_CONFIG,DEFAULT_MIN_TO_CHANGE_PERMISSIONS_CONFIG);
    }

    /**
     * Copy constructor primarily used in the editDashboardModel so the userbase permission config is not edited
     * before the save button is clicked.
     *
     * @param permissionsConfiguration the permissions configuration
     */
    public PermissionsConfiguration(PermissionsConfiguration permissionsConfiguration) {
        this.actionPermissionsMap = new LinkedHashMap<>();
        if (permissionsConfiguration != null) {
            permissionsConfiguration.actionPermissionsMap.forEach(this.actionPermissionsMap::put);
        }
    }

    public PermissionsConfiguration(Map<Action, Permissions> actionPermissionsMap) {
        this.actionPermissionsMap = new LinkedHashMap<>();
        if (actionPermissionsMap != null) {
            actionPermissionsMap.forEach(this.actionPermissionsMap::put);
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != PermissionsConfiguration.class) {
            return false;
        }
        PermissionsConfiguration configuration = (PermissionsConfiguration) obj;
        for (Map.Entry<Action, Permissions> entry : actionPermissionsMap.entrySet()) {
            Permissions other = configuration.getActionPermissionsMap().get(entry.getKey());
            if (other == null || other.getBitValue() != entry.getValue().getBitValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets action permissions map.
     * The main use of this is to replace values in the map.
     *
     * @return the action permissions map
     */
    public Map<Action, Permissions> getActionPermissionsMap() {
        return actionPermissionsMap;
    }

    /**
     * Load permission configuration.
     *
     * @param preferences the preferences
     */
    public void sendToContext(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        actionPermissionsMap.forEach((k,v) -> editor.putInt(k.getDescription(), v.getBitValue()));
        editor.apply();
    }

    /**
     * Save permission configuration.
     *
     * @param preferences the preferences
     */
    public void receiveFromContext(SharedPreferences preferences){
        actionPermissionsMap.keySet().forEach(
            action -> actionPermissionsMap.replace(
                action,Permissions.fromInteger(preferences.getInt(action.getDescription(), 0))
            )
        );
    }

}
