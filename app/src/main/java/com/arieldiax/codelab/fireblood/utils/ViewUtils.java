package com.arieldiax.codelab.fireblood.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;

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
    public static View.OnClickListener getStartCustomActivityOnClickListener(final Context context, final Class activityClass, final Pair<View, String> activityPair, final boolean shouldActivityFinish) {
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
    public static void startCustomActivity(Context context, Class activityClass, Pair<View, String> activityPair, boolean shouldActivityFinish) {
        final Activity activity = (Activity) context;
        Intent activityIntent = new Intent(context, activityClass);
        if (activityPair != null) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, activityPair);
            context.startActivity(activityIntent, activityOptions.toBundle());
            if (shouldActivityFinish) {
                Handler handler = new Handler();
                int delay = 1000;
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        activity.finish();
                    }
                }, delay);
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

    /**
     * Builds a bottom sheet dialog.
     *
     * @param activity                      Instance of the Activity class.
     * @param titleResourceId               Resource ID of the title.
     * @param messageResourceId             Resource ID of the message.
     * @param positiveButtonOnClickListener Click listener of the positive button.
     * @param negativeButtonOnClickListener Click listener of the negative button.
     */
    public static BottomSheetDialog buildBottomSheetDialog(Activity activity, int titleResourceId, int messageResourceId, View.OnClickListener positiveButtonOnClickListener, DialogInterface.OnDismissListener negativeButtonOnClickListener) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View contentView = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_confirm, null);
        TextView titleTextView = (TextView) contentView.findViewById(R.id.title_text_view);
        titleTextView.setText(titleResourceId);
        TextView messageTextView = (TextView) contentView.findViewById(R.id.message_text_view);
        messageTextView.setText(messageResourceId);
        TextView mPositiveButtonTextView = (TextView) contentView.findViewById(R.id.positive_button_text_view);
        mPositiveButtonTextView.setText(android.R.string.ok);
        mPositiveButtonTextView.setOnClickListener(positiveButtonOnClickListener);
        TextView mNegativeButtonTextView = (TextView) contentView.findViewById(R.id.negative_button_text_view);
        mNegativeButtonTextView.setText(android.R.string.cancel);
        mNegativeButtonTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        if (negativeButtonOnClickListener != null) {
            bottomSheetDialog.setOnDismissListener(negativeButtonOnClickListener);
        }
        bottomSheetDialog.setContentView(contentView);
        bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return bottomSheetDialog;
    }
}
