package com.arieldiax.codelab.fireblood.models;

import android.app.Activity;
import android.view.View;

import com.arieldiax.codelab.fireblood.utils.FormUtils;

public class Rule {

    /**
     * Type of the rules.
     */
    private static int TYPE_INCREMENTER = 0;
    private static final int TYPE_EMPTY = ++TYPE_INCREMENTER;
    private static final int TYPE_REGEX = ++TYPE_INCREMENTER;

    /**
     * Type of the rule.
     */
    private int mType;

    /**
     * Regex of the rule.
     */
    private String mRegex;

    /**
     * Error of the rule.
     */
    private int mError;

    /**
     * Creates a new Rule object.
     */
    public Rule() {
        mType = TYPE_EMPTY;
    }

    /**
     * Sets the regex of the rule.
     *
     * @param regex Regex of the rule.
     * @return The instance of the Rule class.
     */
    public Rule setRegex(String regex) {
        mRegex = regex;
        mType = TYPE_REGEX;
        return this;
    }

    /**
     * Sets the error of the rule.
     *
     * @param error Error of the rule.
     * @return The instance of the Rule class.
     */
    public Rule setError(int error) {
        mError = error;
        return this;
    }

    /**
     * Determines whether or not the rule has passed the criteria.
     *
     * @return Whether or not the rule has passed the criteria.
     */
    public boolean validate(Activity activity, View view) {
        boolean hasPassedCriteria = true;
        if (mType == TYPE_EMPTY) {
            hasPassedCriteria = !FormUtils.hasEmptyValue(activity, view);
        } else if (mType == TYPE_REGEX) {
            hasPassedCriteria = FormUtils.getViewValue(activity, view).matches(mRegex);
        }
        if (!hasPassedCriteria) {
            FormUtils.setViewError(activity, view, activity.getString(mError));
        } else {
            FormUtils.setViewError(activity, view, null);
        }
        return hasPassedCriteria;
    }
}
