package com.arieldiax.codelab.fireblood.models.widgets;

import android.app.Activity;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;

public class PhotoBottomSheetDialog {

    /**
     * Instance of the BottomSheetDialog class.
     */
    private BottomSheetDialog mBottomSheetDialog;

    /**
     * Text view field for gallery button.
     */
    private TextView mGalleryButtonTextView;

    /**
     * Text view field for remove photo button.
     */
    private TextView mRemovePhotoButtonTextView;

    /**
     * Creates a new PhotoBottomSheetDialog object.
     *
     * @param activity Instance of the Activity class.
     */
    public PhotoBottomSheetDialog(Activity activity) {
        mBottomSheetDialog = new BottomSheetDialog(activity);
        View contentView = activity.getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_photo, null);
        mGalleryButtonTextView = (TextView) contentView.findViewById(R.id.gallery_button_text_view);
        mRemovePhotoButtonTextView = (TextView) contentView.findViewById(R.id.remove_photo_button_text_view);
        mBottomSheetDialog.setContentView(contentView);
        mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * Sets the gallery button listener of the photo bottom sheet dialog.
     *
     * @param galleryButtonListener Listener of the gallery button.
     * @return The instance of the PhotoBottomSheetDialog class.
     */
    public PhotoBottomSheetDialog setGalleryButtonListener(View.OnClickListener galleryButtonListener) {
        mGalleryButtonTextView.setOnClickListener(galleryButtonListener);
        return this;
    }

    /**
     * Sets the remove photo button listener of the photo bottom sheet dialog.
     *
     * @param removePhotoButtonListener Listener of the remove photo button.
     * @return The instance of the PhotoBottomSheetDialog class.
     */
    public PhotoBottomSheetDialog setRemovePhotoButtonListener(View.OnClickListener removePhotoButtonListener) {
        mRemovePhotoButtonTextView.setOnClickListener(removePhotoButtonListener);
        return this;
    }

    /**
     * Sets the remove photo button state of the photo bottom sheet dialog.
     *
     * @param removePhotoButtonState State of the remove photo button.
     * @return The instance of the PhotoBottomSheetDialog class.
     */
    public PhotoBottomSheetDialog setRemovePhotoButtonState(boolean removePhotoButtonState) {
        mRemovePhotoButtonTextView.setEnabled(removePhotoButtonState);
        return this;
    }

    /**
     * Shows the photo bottom sheet dialog.
     */
    public void show() {
        mBottomSheetDialog.show();
    }

    /**
     * Dismisses the photo bottom sheet dialog.
     */
    public void dismiss() {
        mBottomSheetDialog.dismiss();
    }
}
