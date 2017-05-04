package com.arieldiax.codelab.fireblood.models.widgets;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;

public class ConfirmBottomSheetDialog {

    /**
     * Instance of the BottomSheetDialog class.
     */
    private BottomSheetDialog mBottomSheetDialog;

    /**
     * Text text field for title.
     */
    private TextView mTitleTextView;

    /**
     * Text text field for message.
     */
    private TextView mMessageTextView;

    /**
     * Text text field for positive button.
     */
    private TextView mPositiveButtonTextView;

    /**
     * Creates a new ConfirmBottomSheetDialog object.
     *
     * @param activity Instance of the Activity class.
     */
    public ConfirmBottomSheetDialog(Activity activity) {
        mBottomSheetDialog = new BottomSheetDialog(activity);
        View contentView = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_confirm, null);
        mTitleTextView = (TextView) contentView.findViewById(R.id.title_text_view);
        mMessageTextView = (TextView) contentView.findViewById(R.id.message_text_view);
        mPositiveButtonTextView = (TextView) contentView.findViewById(R.id.positive_button_text_view);
        mPositiveButtonTextView.setText(android.R.string.ok);
        TextView negativeButtonTextView = (TextView) contentView.findViewById(R.id.negative_button_text_view);
        negativeButtonTextView.setText(android.R.string.cancel);
        negativeButtonTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mBottomSheetDialog.setContentView(contentView);
        mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * Sets the title of the confirm bottom sheet dialog.
     *
     * @param titleResourceId Resource ID of the title.
     * @return The instance of the ConfirmBottomSheetDialog class.
     */
    public ConfirmBottomSheetDialog setTitle(int titleResourceId) {
        mTitleTextView.setText(titleResourceId);
        return this;
    }

    /**
     * Sets the message of the confirm bottom sheet dialog.
     *
     * @param messageResourceId Resource ID of the message.
     * @return The instance of the ConfirmBottomSheetDialog class.
     */
    public ConfirmBottomSheetDialog setMessage(int messageResourceId) {
        mMessageTextView.setText(messageResourceId);
        return this;
    }

    /**
     * Sets the positive button listener of the confirm bottom sheet dialog.
     *
     * @param positiveButtonListener Listener of the positive button.
     * @return The instance of the ConfirmBottomSheetDialog class.
     */
    public ConfirmBottomSheetDialog setPositiveButtonListener(View.OnClickListener positiveButtonListener) {
        mPositiveButtonTextView.setOnClickListener(positiveButtonListener);
        return this;
    }

    /**
     * Sets the negative button listener of the confirm bottom sheet dialog.
     *
     * @param negativeButtonListener Listener of the negative button.
     * @return The instance of the ConfirmBottomSheetDialog class.
     */
    public ConfirmBottomSheetDialog setNegativeButtonListener(DialogInterface.OnDismissListener negativeButtonListener) {
        mBottomSheetDialog.setOnDismissListener(negativeButtonListener);
        return this;
    }

    /**
     * Shows the confirm bottom sheet dialog.
     */
    public void show() {
        mBottomSheetDialog.show();
    }

    /**
     * Dismisses the confirm bottom sheet dialog.
     */
    public void dismiss() {
        mBottomSheetDialog.dismiss();
    }
}
