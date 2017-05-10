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
     * List of Rule instances.
     */
    private List<Rule> mRules;

    /**
     * Creates a new Validation object.
     *
     * @param activity        Instance of the Activity class.
     * @param fieldResourceId Resource ID of the field.
     */
    public Validation(
            Activity activity,
            int fieldResourceId
    ) {
        mView = activity.findViewById(fieldResourceId);
        mRules = new ArrayList<>();
    }

    /**
     * Adds a rule.
     *
     * @param regexString     String of the regex.
     * @param errorResourceId Resource ID of the error.
     * @return The instance of the Validation class.
     */
    public Validation addRule(
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
     * @return The value of the validation.
     */
    public String getValue() {
        return FormUtils.getViewValue(mView);
    }

    /**
     * Determines whether or not the validation has passed the rules.
     *
     * @return Whether or not the validation has passed the rules.
     */
    public boolean validate(Activity activity) {
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
