package com.arieldiax.codelab.fireblood.models;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class FormValidator {

    /**
     * Instance of the Activity class.
     */
    private Activity mActivity;

    /**
     * List of Validation instances.
     */
    private List<Validation> mValidations;

    /**
     * Creates a new FormValidator object.
     *
     * @param activity Instance of the Activity class.
     */
    public FormValidator(Activity activity) {
        mActivity = activity;
        mValidations = new ArrayList<>();
    }

    /**
     * Adds a validation.
     *
     * @param fieldResourceId Resource ID of the field.
     * @param errorResourceId Resource ID of the error.
     * @return The instance of the FormValidator class.
     */
    public FormValidator addValidation(int fieldResourceId, int errorResourceId) {
        mValidations.add(new Validation(mActivity)
                .setField(fieldResourceId)
                .setError(errorResourceId)
        );
        return this;
    }

    /**
     * Determines whether or not the form has passed the validations.
     *
     * @return Whether or not the form has passed the validations.
     */
    public boolean validate() {
        boolean hasPassedValidations = true;
        for (Validation validation : mValidations) {
            if (!validation.validate()) {
                hasPassedValidations = false;
            }
        }
        return hasPassedValidations;
    }
}
