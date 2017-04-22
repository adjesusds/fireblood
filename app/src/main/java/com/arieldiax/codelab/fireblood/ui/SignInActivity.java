package com.arieldiax.codelab.fireblood.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;

public class SignInActivity extends AppCompatActivity {

    /**
     * Image view field for app logo.
     */
    private ImageView mAppLogoImageView;

    /**
     * Text view field for forgot your password.
     */
    private TextView mForgotYourPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUi();
        initListeners();
    }

    /**
     * Initializes the user interface view bindings.
     */
    private void initUi() {
        mAppLogoImageView = (ImageView) findViewById(R.id.app_logo_image_view);
        mForgotYourPasswordTextView = (TextView) findViewById(R.id.forgot_your_password_text_view);
    }

    /**
     * Initializes the event listener view bindings.
     */
    private void initListeners() {
        Pair<View, String> activityPair = Pair.create((View) mAppLogoImageView, getString(R.string.transition_app_logo_image_view));
        mForgotYourPasswordTextView.setOnClickListener(ViewUtils.getStartCustomActivityOnClickListener(this, ForgotPasswordActivity.class, activityPair, false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
