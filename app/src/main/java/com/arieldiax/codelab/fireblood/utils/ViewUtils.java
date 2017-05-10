package com.arieldiax.codelab.fireblood.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class ViewUtils {

    /**
     * Creates a new ViewUtils object (no, it won't).
     */
    private ViewUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Gets the start custom activity on click listener.
     *
     * @param context              Instance of the Context class.
     * @param activityClass        Class of the activity.
     * @param activityPair         Pair of the activity.
     * @param shouldActivityFinish Whether or not the activity should be finished.
     * @return The start custom activity on click listener
     */
    public static View.OnClickListener getStartCustomActivityOnClickListener(
            final Context context,
            final Class activityClass,
            final Pair<View, String> activityPair,
            final boolean shouldActivityFinish
    ) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startCustomActivity(context, activityClass, activityPair, shouldActivityFinish);
            }
        };
    }

    /**
     * Starts the custom activity.
     *
     * @param context              Instance of the Context class.
     * @param activityClass        Class of the activity.
     * @param activityPair         Pair of the activity.
     * @param shouldActivityFinish Whether or not the activity should be finished.
     */
    public static void startCustomActivity(
            Context context,
            Class activityClass,
            Pair<View, String> activityPair,
            boolean shouldActivityFinish
    ) {
        final Activity activity = (Activity) context;
        Intent activityIntent = new Intent(context, activityClass);
        if (activityPair != null) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, activityPair);
            context.startActivity(activityIntent, activityOptions.toBundle());
            if (shouldActivityFinish) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        activity.finish();
                    }
                }, DateUtils.SECOND_IN_MILLIS);
            }
        } else {
            context.startActivity(activityIntent);
            if (shouldActivityFinish) {
                activity.finish();
            }
        }
    }

    /**
     * Hides the keyboard.
     *
     * @param activity Instance of the Activity class.
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
