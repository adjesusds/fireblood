package com.arieldiax.codelab.fireblood.models.validations;

import android.app.Activity;
import android.util.SparseArray;

import com.arieldiax.codelab.fireblood.utils.Utils;

public class FormValidator {

    /**
     * Instance of the Activity class.
     */
    private Activity mActivity;

    /**
     * Map of Validation instances.
     */
    private SparseArray<Validation> mValidations;

    /**
     * Creates a new FormValidator object.
     *
     * @param activity Instance of the Activity class.
     */
    public FormValidator(Activity activity) {
        mActivity = activity;
        mValidations = new SparseArray<>();
    }

    /**
     * Adds a validation.
     *
     * @param fieldResourceId Resource ID of the field.
     * @param errorResourceId Resource ID of the error.
     * @return The instance of the FormValidator class.
     */
    public FormValidator addValidation(int fieldResourceId, int errorResourceId) {
        Validation validation = mValidations.get(fieldResourceId);
        if (validation != null) {
            validation.addRule(errorResourceId);
        } else {
            validation = new Validation(mActivity, fieldResourceId).addRule(errorResourceId);
        }
        mValidations.put(fieldResourceId, validation);
        return this;
    }

    /**
     * Adds a validation.
     *
     * @param fieldResourceId Resource ID of the field.
     * @param regexString     String of the regex.
     * @param errorResourceId Resource ID of the error.
     * @return The instance of the FormValidator class.
     */
    public FormValidator addValidation(int fieldResourceId, String regexString, int errorResourceId) {
        Validation validation = mValidations.get(fieldResourceId);
        if (validation != null) {
            validation.addRule(regexString, errorResourceId);
        } else {
            validation = new Validation(mActivity, fieldResourceId).addRule(regexString, errorResourceId);
        }
        mValidations.put(fieldResourceId, validation);
        return this;
    }

    /**
     * Generates the hashed string of the form.
     *
     * @return The hashed string of the form.
     */
    public String hash() {
        String hashString = "";
        for (int i = 0; i < mValidations.size(); i++) {
            hashString += mValidations.valueAt(i).getValue(mActivity);
        }
        return Utils.md5(hashString);
    }

    /**
     * Determines whether or not the form has passed the validations.
     *
     * @return Whether or not the form has passed the validations.
     */
    public boolean validate() {
        boolean hasPassedValidations = true;
        for (int i = 0; i < mValidations.size(); i++) {
            if (!mValidations.valueAt(i).validate(mActivity)) {
                hasPassedValidations = false;
            }
        }
        return hasPassedValidations;
    }

    /**
     * Generates the serialized map of the form.
     *
     * @return The serialized map of the form.
     */
    public SparseArray<String> serialize() {
        SparseArray<String> serializeMap = new SparseArray<>();
        for (int i = 0; i < mValidations.size(); i++) {
            serializeMap.put(mValidations.keyAt(i), mValidations.valueAt(i).getValue(mActivity));
        }
        return serializeMap;
    }
}
