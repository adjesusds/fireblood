package com.arieldiax.codelab.fireblood;

import android.widget.Spinner;

public final class FormUtils {

    /**
     * Creates a new FormUtils object (no, it won't).
     */
    private FormUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Determines whether or not the spinner has an empty value.
     *
     * @param spinner Instance of the Spinner class.
     * @return Whether or not the spinner has an empty value.
     */
    public static boolean hasEmptyValue(Spinner spinner) {
        return (spinner.getSelectedItemPosition() == 0);
    }

    /**
     * Gets the spinner value.
     *
     * @param spinner Instance of the Spinner class.
     * @return The spinner value.
     */
    public static String getSpinnerValue(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }
}
