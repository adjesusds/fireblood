package com.arieldiax.codelab.fireblood.ui.launch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.ui.registration.signin.SignInActivity;
import com.arieldiax.codelab.fireblood.ui.registration.signup.SignUpActivity;
import com.arieldiax.codelab.fireblood.utils.ViewUtils;

public class WelcomeActivity extends AppCompatActivity {

    /**
     * Views of the activity.
     */
    ImageView mAppLogoImageView;
    Button mSignUpButton;
    Button mSignInButton;

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
    void initUi() {
        mAppLogoImageView = (ImageView) findViewById(R.id.app_logo_image_view);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
    }

    /**
     * Initializes the event listener view bindings.
     */
    void initListeners() {
        Pair<View, String> activityPair1 = Pair.create((View) mAppLogoImageView, mAppLogoImageView.getTransitionName());
        mSignUpButton.setOnClickListener(ViewUtils.getStartCustomActivityOnClickListener(this, SignUpActivity.class, activityPair1, null, false));
        mSignInButton.setOnClickListener(ViewUtils.getStartCustomActivityOnClickListener(this, SignInActivity.class, activityPair1, null, false));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
