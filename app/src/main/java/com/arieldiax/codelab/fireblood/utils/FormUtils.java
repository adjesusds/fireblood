package com.arieldiax.codelab.fireblood.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;

import java.util.HashMap;
import java.util.regex.Pattern;

public final class FormUtils {

    /**
     * Nest delimiter of a map key.
     */
    public static final String MAP_KEY_NEST_DELIMITER = ".";

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
    public static boolean hasEmptyValue(
            Activity activity,
            View view
    ) {
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
    public static String getViewValue(
            Activity activity,
            View view
    ) {
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString().trim();
        } else if (view instanceof RadioGroup) {
            RadioGroup radioGroup = (RadioGroup) view;
            if (radioGroup.getCheckedRadioButtonId() >= 0) {
                RadioButton radioButton = (RadioButton) activity.findViewById(radioGroup.getCheckedRadioButtonId());
                return radioButton.getText().toString().trim();
            }
        } else if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;
            if (spinner.getSelectedItemPosition() > 0) {
                return spinner.getSelectedItem().toString().trim();
            }
        } else if (view instanceof Switch) {
            if (((Switch) view).isChecked()) {
                return " ";
            }
        }
        return "";
    }

    /**
     * Gets the map value.
     *
     * @param objectMap   Map of the object.
     * @param fieldMapKey Map key of the field.
     * @return The map value.
     */
    public static Object getMapValue(
            HashMap<String, Object> objectMap,
            String fieldMapKey
    ) {
        if (objectMap.containsKey(fieldMapKey)) {
            return objectMap.get(fieldMapKey);
        }
        String[] segments = fieldMapKey.split(Pattern.quote(MAP_KEY_NEST_DELIMITER));
        for (int i = 0; i < segments.length - 1; i++) {
            objectMap = (HashMap<String, Object>) objectMap.get(segments[i]);
        }
        return objectMap.get(segments[segments.length - 1]);
    }

    /**
     * Sets the view value.
     *
     * @param view      Instance of the View class.
     * @param viewValue Value of the view.
     */
    public static void setViewValue(
            View view,
            Object viewValue
    ) {
        if (view instanceof EditText) {
            ((EditText) view).setText(viewValue.toString());
        } else if (view instanceof RadioGroup) {
            ((RadioButton) ((RadioGroup) view).getChildAt(Integer.valueOf(viewValue.toString()))).setChecked(true);
        } else if (view instanceof Spinner) {
            Spinner spinner = (Spinner) view;
            int selectedItemPosition = ((ArrayAdapter<CharSequence>) spinner.getAdapter()).getPosition(viewValue.toString());
            spinner.setSelection(selectedItemPosition);
        } else if (view instanceof Switch) {
            ((Switch) view).setChecked(Boolean.valueOf(viewValue.toString()));
        }
    }

    /**
     * Sets the view error.
     *
     * @param activity  Instance of the Activity class.
     * @param view      Instance of the View class.
     * @param viewError Error of the view.
     */
    public static void setViewError(
            Activity activity,
            View view,
            String viewError
    ) {
        TypedValue outValue = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.themeName, outValue, true);
        String themeNameAppBase = activity.getString(R.string.theme_name_app_base);
        boolean isAppBaseTheme = outValue.string.equals(themeNameAppBase);
        if (view instanceof EditText) {
            TextInputLayout textInputLayout = (TextInputLayout) view.getParent().getParent();
            textInputLayout.setError(viewError);
            if (viewError == null) {
                textInputLayout.setErrorEnabled(false);
            }
        } else if (view instanceof RadioGroup) {
            RadioGroup radioGroup = (RadioGroup) view;
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(radioGroup.getChildCount() - 1);
            Drawable viewIcon = (isAppBaseTheme)
                    ? activity.getDrawable(R.drawable.ic_error_black_24dp)
                    : activity.getDrawable(R.drawable.ic_error_red_24dp);
            viewIcon.setBounds(0, 0, viewIcon.getIntrinsicWidth(), viewIcon.getIntrinsicHeight());
            radioButton.setError(viewError, viewIcon);
        } else if (view instanceof Spinner) {
            if (viewError != null) {
                TextView textView = (TextView) ((Spinner) view).getSelectedView();
                textView.setText(viewError);
                int textColor = (isAppBaseTheme)
                        ? Color.BLACK
                        : activity.getResources().getColor(R.color.colorAccent);
                textView.setTextColor(textColor);
            }
        }
    }
}
