package com.arieldiax.codelab.fireblood.models.validations;

import android.app.Activity;
import android.view.View;

import com.arieldiax.codelab.fireblood.utils.FormUtils;

import java.util.ArrayList;
import java.util.List;

public class Validation {

    /**
     * Regular expressions of a validation.
     */
    public static final String REGEX_NOT_EMPTY = "^.+$";
    public static final String REGEX_EMAIL = "^[\\w!#$%&'*+\\/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+\\/=?`{|}~^-]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$";
    public static final String REGEX_USERNAME = "^[\\w!#$%&'*+\\/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+\\/=?`{|}~^-]+)*$";
    public static final String REGEX_EMAIL_OR_USERNAME = "^[\\w!#$%&'*+\\/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+\\/=?`{|}~^-]+)*(?:@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,6})?$";
    public static final String REGEX_PASSWORD = "^(?=.{8,32}$)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*$";
    public static final String REGEX_PHONE = "^8[024]9[0-9]{7}$";

    /**
     * Instance of the View class.
     */
    private View mView;

    /**
     * Map key of the validation.
     */
    private String mMapKey;

    /**
     * List of Rule instances.
     */
    private List<Rule> mRules;

    /**
     * Creates a new Validation object.
     *
     * @param activity        Instance of the Activity class.
     * @param fieldResourceId Resource ID of the field.
     * @param fieldMapKey     Map key of the field.
     */
    Validation(
            Activity activity,
            int fieldResourceId,
            String fieldMapKey
    ) {
        mView = activity.findViewById(fieldResourceId);
        mMapKey = fieldMapKey;
        mRules = new ArrayList<>();
    }

    /**
     * Adds a rule.
     *
     * @param regexString     String of the regex.
     * @param errorResourceId Resource ID of the error.
     * @return The instance of the Validation class.
     */
    Validation addRule(
            String regexString,
            int errorResourceId
    ) {
        mRules.add(new Rule()
                .setRegex(regexString)
                .setError(errorResourceId)
        );
        return this;
    }

    /**
     * Gets the value of the validation.
     *
     * @param activity Instance of the Activity class.
     * @return The value of the validation.
     */
    String getValue(Activity activity) {
        return FormUtils.getViewValue(activity, mView);
    }

    /**
     * Gets the map key of the validation.
     *
     * @return The map key of the validation.
     */
    String getMapKey() {
        return mMapKey;
    }

    /**
     * Sets the value of the validation.
     *
     * @param viewValue Value of the view.
     */
    void setValue(Object viewValue) {
        FormUtils.setViewValue(mView, viewValue);
    }

    /**
     * Determines whether or not the validation has passed the rules.
     *
     * @param activity Instance of the Activity class.
     * @return Whether or not the validation has passed the rules.
     */
    boolean validate(Activity activity) {
        boolean hasPassedRules = true;
        for (Rule rule : mRules) {
            if (!rule.validate(activity, mView)) {
                hasPassedRules = false;
                break;
            }
        }
        return hasPassedRules;
    }
}
