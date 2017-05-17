package com.arieldiax.codelab.fireblood.models.validations;

import android.app.Activity;
import android.view.View;

import com.arieldiax.codelab.fireblood.utils.FormUtils;

class Rule {

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
    Rule() {
        // Required empty public constructor (to allow instantiation).
    }

    /**
     * Sets the regex of the rule.
     *
     * @param regex Regex of the rule.
     * @return The instance of the Rule class.
     */
    Rule setRegex(String regex) {
        mRegex = regex;
        return this;
    }

    /**
     * Sets the error of the rule.
     *
     * @param error Error of the rule.
     * @return The instance of the Rule class.
     */
    Rule setError(int error) {
        mError = error;
        return this;
    }

    /**
     * Determines whether or not the rule has passed the criteria.
     *
     * @param activity Instance of the Activity class.
     * @param view     Instance of the View class.
     * @return Whether or not the rule has passed the criteria.
     */
    boolean validate(
            Activity activity,
            View view
    ) {
        boolean hasPassedCriteria = (mRegex.equals(Validation.REGEX_NOT_EMPTY))
                ? !FormUtils.hasEmptyValue(activity, view)
                : FormUtils.getViewValue(activity, view).matches(mRegex);
        String viewError = (!hasPassedCriteria) ? activity.getString(mError) : null;
        FormUtils.setViewError(activity, view, viewError);
        return hasPassedCriteria;
    }
}
