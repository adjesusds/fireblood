package com.arieldiax.codelab.fireblood.models;

import android.app.Activity;
import android.view.View;

import com.arieldiax.codelab.fireblood.utils.FormUtils;

public class Validation {

    /**
     * Type of the validations.
     */
    private static int TYPE_BASE = 0;
    public static final int TYPE_EMPTY = ++TYPE_BASE;
    public static final int TYPE_REGEX = ++TYPE_BASE;
    public static final int TYPE_CALLBACK = ++TYPE_BASE;

    /**
     * Instance of the Activity class.
     */
    private Activity mActivity;

    /**
     * Instance of the View class.
     */
    private View mView;

    /**
     * Field of the validation.
     */
    private int mField;

    /**
     * Type of the validation.
     */
    private int mType;

    /**
     * Error of the validation.
     */
    private int mError;

    /**
     * Creates a new Validation object.
     *
     * @param activity Instance of the Activity class.
     */
    public Validation(Activity activity) {
        mActivity = activity;
    }

    /**
     * Sets the field of the validation.
     *
     * @param field Field of the validation.
     * @return The instance of the Validation class.
     */
    public Validation setField(int field) {
        mField = field;
        mType = TYPE_EMPTY;
        return this;
    }

    /**
     * Sets the error of the validation.
     *
     * @param error Error of the validation.
     * @return The instance of the Validation class.
     */
    public Validation setError(int error) {
        mError = error;
        return this;
    }

    /**
     * Determines whether or not the validation has passed the rule.
     *
     * @return Whether or not the validation has passed the rule.
     */
    public boolean validate() {
        boolean hasPassedRule = true;
        if (mType == TYPE_EMPTY) {
            hasPassedRule = !FormUtils.hasEmptyValue(mActivity, getView());
        }
        if (!hasPassedRule) {
            FormUtils.setViewError(mActivity, getView(), mActivity.getString(mError));
        } else {
            FormUtils.setViewError(mActivity, getView(), null);
        }
        return hasPassedRule;
    }

    /**
     * Gets the view of the validation.
     */
    private View getView() {
        if (mView == null) {
            mView = mActivity.findViewById(mField);
        }
        return mView;
    }
}
