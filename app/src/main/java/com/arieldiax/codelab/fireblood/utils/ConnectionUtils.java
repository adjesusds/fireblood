package com.arieldiax.codelab.fireblood.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public final class ConnectionUtils {

    /**
     * Creates a new ConnectionUtils object (no, it won't).
     */
    private ConnectionUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Determines whether or not the device has Internet connection.
     *
     * @param context Instance of the Context class.
     * @return Whether or not the device has Internet connection.
     */
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (
                !isInAirplaneMode(context) &&
                        networkInfo != null &&
                        networkInfo.isConnected()
        );
    }

    /**
     * Determines whether or not the device is in airplane mode.
     *
     * @param context Instance of the Context class.
     * @return Whether or not the device is in airplane mode.
     */
    private static boolean isInAirplaneMode(Context context) {
        return (Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0);
    }
}
