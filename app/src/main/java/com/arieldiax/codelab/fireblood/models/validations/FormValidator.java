package com.arieldiax.codelab.fireblood.models.validations;

import android.app.Activity;
import android.util.SparseArray;

import com.arieldiax.codelab.fireblood.utils.FormUtils;
import com.arieldiax.codelab.fireblood.utils.Utils;

import java.util.HashMap;

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
     * Hash of the form.
     */
    private String mHash;

    /**
     * Creates a new FormValidator object.
     *
     * @param activity Instance of the Activity class.
     */
    public FormValidator(Activity activity) {
        mActivity = activity;
        mValidations = new SparseArray<>();
        mHash = "";
    }

    /**
     * Adds a validation.
     *
     * @param fieldResourceId Resource ID of the field.
     * @param fieldMapKey     Map key of the field.
     * @param regexString     String of the regex.
     * @param errorResourceId Resource ID of the error.
     * @return The instance of the FormValidator class.
     */
    public FormValidator addValidation(
            int fieldResourceId,
            String fieldMapKey,
            String regexString,
            int errorResourceId
    ) {
        Validation validation = mValidations.get(fieldResourceId);
        if (validation != null) {
            validation.addRule(regexString, errorResourceId);
        } else {
            validation = new Validation(mActivity, fieldResourceId, fieldMapKey).addRule(regexString, errorResourceId);
        }
        mValidations.put(fieldResourceId, validation);
        return this;
    }

    /**
     * Updates the hash of the form.
     */
    public void updateHash() {
        mHash = hash();
    }

    /**
     * Populates the field entries of the form.
     *
     * @param objectMap Map of the object.
     */
    public void populate(HashMap<String, Object> objectMap) {
        for (int i = 0; i < mValidations.size(); i++) {
            Validation validation = mValidations.valueAt(i);
            Object viewValue = FormUtils.getMapValue(objectMap, validation.getMapKey());
            validation.setValue(viewValue);
        }
        updateHash();
    }

    /**
     * Determines whether or not the form has changed its fields.
     *
     * @return Whether or not the form has changed its fields.
     */
    public boolean hasChanged() {
        return (!mHash.equals(hash()));
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
    public HashMap<String, Object> serialize() {
        HashMap<String, Object> serializeMap = new HashMap<>();
        for (int i = 0; i < mValidations.size(); i++) {
            Validation validation = mValidations.valueAt(i);
            serializeMap.put(validation.getMapKey(), validation.getValue(mActivity));
        }
        return serializeMap;
    }

    /**
     * Generates the hashed string of the form.
     *
     * @return The hashed string of the form.
     */
    private String hash() {
        StringBuilder hashStringBuilder = new StringBuilder();
        for (int i = 0; i < mValidations.size(); i++) {
            hashStringBuilder.append(mValidations.valueAt(i).getValue(mActivity));
        }
        return Utils.md5(hashStringBuilder.toString());
    }
}
