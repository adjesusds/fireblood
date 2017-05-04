package com.arieldiax.codelab.fireblood.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;

public final class FormUtils {

    /**
     * Creates a new FormUtils object (no, it won't).
     */
    private FormUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Determines whether or not the view has an empty value.
     *
     * @param activity Instance of the Activity class.
     * @param view     Instance of the View class.
     * @return Whether or not the view has an empty value.
     */
    public static boolean hasEmptyValue(Activity activity, View view) {
        if (view instanceof EditText) {
            return getViewValue(activity, view).isEmpty();
        } else if (view instanceof RadioGroup) {
            return (((RadioGroup) view).getCheckedRadioButtonId() < 0);
        } else if (view instanceof Spinner) {
            return (((Spinner) view).getSelectedItemPosition() == 0);
        }
        return false;
    }

    /**
     * Gets the view value.
     *
     * @param activity Instance of the Activity class.
     * @param view     Instance of the View class.
     * @return The view value.
     */
    public static String getViewValue(Activity activity, View view) {
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString();
        } else if (view instanceof RadioGroup) {
            RadioGroup radioGroup = (RadioGroup) view;
            if (radioGroup.getCheckedRadioButtonId() >= 0) {
                return String.valueOf(radioGroup.getCheckedRadioButtonId());
            }
        } else if (view instanceof Spinner) {
            return ((Spinner) view).getSelectedItem().toString();
        } else if (view instanceof Switch) {
            if (((Switch) view).isChecked()) {
                return " ";
            }
        }
        return "";
    }

    /**
     * Sets the view error.
     *
     * @param activity  Instance of the Activity class.
     * @param view      Instance of the View class.
     * @param viewError Error of the view.
     */
    public static void setViewError(Activity activity, View view, String viewError) {
        if (view instanceof EditText) {
            TextInputLayout textInputLayout = (TextInputLayout) view.getParent().getParent();
            textInputLayout.setError(viewError);
            if (viewError == null) {
                textInputLayout.setErrorEnabled(false);
            }
        } else if (view instanceof RadioGroup) {
            RadioGroup radioGroup = (RadioGroup) view;
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(radioGroup.getChildCount() - 1);
            Drawable viewIcon = activity.getDrawable(R.drawable.ic_error_black_24dp);
            viewIcon.setBounds(0, 0, viewIcon.getIntrinsicWidth(), viewIcon.getIntrinsicHeight());
            radioButton.setError(viewError, viewIcon);
        } else if (view instanceof Spinner) {
            if (viewError != null) {
                TextView textView = (TextView) ((Spinner) view).getSelectedView();
                textView.setText(viewError);
                textView.setTextColor(Color.BLACK);
            }
        }
    }
}
