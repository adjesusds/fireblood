package com.arieldiax.codelab.fireblood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {

    /**
     * Image view field for app logo.
     */
    private ImageView mAppLogoImageView;

    /**
     * Button field for sign up.
     */
    private Button mSignUpButton;

    /**
     * Button field for sign in.
     */
    private Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initUi();
        initListeners();
    }

    /**
     * Initializes the user interface view bindings.
     */
    private void initUi() {
        mAppLogoImageView = (ImageView) findViewById(R.id.app_logo_image_view);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
    }

    /**
     * Initializes the event listener view bindings.
     */
    private void initListeners() {
        Pair<View, String> activityPair = Pair.create((View) mAppLogoImageView, getString(R.string.transition_app_logo_image_view));
        mSignUpButton.setOnClickListener(ViewUtils.getStartCustomActivityOnClickListener(this, SignUpActivity.class, activityPair, false));
        mSignInButton.setOnClickListener(ViewUtils.getStartCustomActivityOnClickListener(this, SignInActivity.class, activityPair, false));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
