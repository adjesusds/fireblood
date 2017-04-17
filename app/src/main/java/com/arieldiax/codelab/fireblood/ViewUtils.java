package com.arieldiax.codelab.fireblood;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

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
     * @param shouldActivityFinish Whether or not the activity should be finished.
     * @return The start custom activity on click listener
     */
    public static View.OnClickListener getStartCustomActivityOnClickListener(final Context context, final Class activityClass, final boolean shouldActivityFinish) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startCustomActivity(context, activityClass, shouldActivityFinish);
            }
        };
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
        View contentView = activity.getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
        bottomSheetDialog.setContentView(contentView);
        TextView titleTextView = (TextView) contentView.findViewById(R.id.title_text_view);
        titleTextView.setText(titleResourceId);
        TextView messageTextView = (TextView) contentView.findViewById(R.id.message_text_view);
        messageTextView.setText(messageResourceId);
        LinearLayout positiveLinearLayout = (LinearLayout) contentView.findViewById(R.id.positive_linear_layout);
        positiveLinearLayout.setOnClickListener(positiveButtonOnClickListener);
        TextView positiveTextView = (TextView) positiveLinearLayout.findViewById(R.id.positive_text_view);
        positiveTextView.setText(android.R.string.ok);
        LinearLayout negativeLinearLayout = (LinearLayout) contentView.findViewById(R.id.negative_linear_layout);
        negativeLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        TextView negativeTextView = (TextView) negativeLinearLayout.findViewById(R.id.negative_text_view);
        negativeTextView.setText(android.R.string.cancel);
        if (negativeButtonOnClickListener != null) {
            bottomSheetDialog.setOnDismissListener(negativeButtonOnClickListener);
        }
        bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return bottomSheetDialog;
    }
}
