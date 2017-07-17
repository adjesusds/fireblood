package com.arieldiax.codelab.fireblood.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.arieldiax.codelab.fireblood.ui.navigation.search.SearchActivity;

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
     * @param activityPair1        Pair 1 of the activity.
     * @param activityPair2        Pair 2 of the activity.
     * @param shouldActivityFinish Whether or not the activity should be finished.
     * @return The start custom activity on click listener
     */
    public static View.OnClickListener getStartCustomActivityOnClickListener(
            final Context context,
            final Class activityClass,
            final Pair<View, String> activityPair1,
            final Pair<View, String> activityPair2,
            final boolean shouldActivityFinish
    ) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startCustomActivity(context, activityClass, activityPair1, activityPair2, shouldActivityFinish);
            }
        };
    }

    /**
     * Starts the custom activity.
     *
     * @param context              Instance of the Context class.
     * @param activityClass        Class of the activity.
     * @param activityPair1        Pair 1 of the activity.
     * @param activityPair2        Pair 2 of the activity.
     * @param shouldActivityFinish Whether or not the activity should be finished.
     */
    public static void startCustomActivity(
            Context context,
            Class activityClass,
            Pair<View, String> activityPair1,
            Pair<View, String> activityPair2,
            boolean shouldActivityFinish
    ) {
        final Activity activity = (Activity) context;
        Intent activityIntent = new Intent(context, activityClass);
        if (activityPair1 != null) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, activityPair1);
            if (activityPair2 != null) {
                activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, activityPair1, activityPair2);
            }
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
        if (activityClass.equals(SearchActivity.class)) {
            NavigationUtils.stackCustomActivity(context, activityClass, false);
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

    /**
     * Converts the density independent pixels into pixels.
     *
     * @param context Instance of the Context class.
     * @param dp      Space in DP measurement.
     * @return The density independent pixels converted.
     */
    public static int convertDpIntoPx(
            Context context,
            float dp
    ) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.50f);
    }

    /**
     * Converts the drawable into a bitmap.
     *
     * @param drawable Instance of the Drawable class.
     * @return The drawable converted.
     */
    public static Bitmap convertDrawableIntoBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
