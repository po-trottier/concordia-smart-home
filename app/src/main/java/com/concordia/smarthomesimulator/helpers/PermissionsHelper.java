package com.concordia.smarthomesimulator.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

public final class PermissionsHelper {

    /**
     * Verify given permission and request them if they are not granted.
     *
     * @param context     the context
     * @param permission  the permission
     * @param requestCode the request code
     * @return whether the permission is granted or not
     */
    public static boolean verifyPermission(Context context, String permission, int requestCode) {
        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    new String[] { permission },
                    requestCode
            );
            return false;
        }
        return true;
    }
}
