package com.arieldiax.codelab.fireblood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public final class ViewUtils {

    /**
     * Creates a new ViewUtils object (no, it won't).
     */
    private ViewUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Starts the custom activity.
     *
     * @param context              Instance of the Context class.
     * @param activityClass        Class of the activity.
     * @param shouldActivityFinish Whether or not the activity should be finished.
     */
    public static void startCustomActivity(Context context, Class activityClass, boolean shouldActivityFinish) {
        Intent activityIntent = new Intent(context, activityClass);
        context.startActivity(activityIntent);
        if (shouldActivityFinish) {
            ((Activity) context).finish();
        }
    }
}
